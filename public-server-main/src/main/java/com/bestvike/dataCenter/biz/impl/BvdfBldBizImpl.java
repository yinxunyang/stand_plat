package com.bestvike.dataCenter.biz.impl;

import com.bestvike.commons.enums.MatchTypeEnum;
import com.bestvike.commons.enums.ReturnCode;
import com.bestvike.commons.exception.MsgException;
import com.bestvike.commons.utils.UtilTool;
import com.bestvike.dataCenter.biz.BvdfBldBiz;
import com.bestvike.dataCenter.entity.BvdfToEsRecordTime;
import com.bestvike.dataCenter.param.BvdfBldParam;
import com.bestvike.dataCenter.param.BvdfCorpParam;
import com.bestvike.dataCenter.param.BvdfRegionParam;
import com.bestvike.dataCenter.service.BvdfBldService;
import com.bestvike.dataCenter.service.BvdfCorpService;
import com.bestvike.dataCenter.service.BvdfRegionService;
import com.bestvike.elastic.service.ElasticSearchService;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

@Service
@Slf4j
public class BvdfBldBizImpl implements BvdfBldBiz {
	/**
	 * es集群的名称
	 */
	@Value("${esConfig.esClusterName}")
	private String esClusterName;
	/**
	 * es的IP
	 */
	@Value("${esConfig.esIP}")
	private String esIP;
	/**
	 * es的esPort
	 */
	@Value("${esConfig.esPort}")
	private String esPort;
	/**
	 * 房屋信息表(arc_houseinfo)最大查询条数,防止内存溢出
	 */
	@Value("${standplatConfig.bvdfToEsSchedule.houseMaxNum}")
	private String houseMaxNum;
	/**
	 * 批量新增、修改bvdf表的数量
	 */
	@Value("${standplatConfig.bvdfToEsSchedule.bvdfBatchNum}")
	private String bvdfBatchNum;
	/**
	 * es自然幢的索引
	 */
	@Value("${esConfig.bldindex}")
	private String bldindex;
	/**
	 * es自然幢的映射
	 */
	@Value("${esConfig.bldtype}")
	private String bldtype;
	@Autowired
	private ElasticSearchService elasticSearchService;
	@Autowired
	private BvdfBldService bvdfBldService;
	@Autowired
	private MongoTemplate mongoTemplate;
	@Autowired
	private BvdfRegionService bvdfRegionService;
	@Autowired
	private BvdfCorpService bvdfCorpService;

	/**
	 * @Author: yinxunyang
	 * @Description: 将bvdf自然幢信息迁移至elasticsearch
	 * @Date: 2019/12/19 11:25
	 * @param:
	 * @return:
	 */
	@Override
	@Scheduled(cron = "${standplatConfig.bldToEsSchedule.cronTime}")
	public void bvdfBldToEs() {
		Query query = new Query(Criteria.where("_id").is("bvdfBld"));
		BvdfToEsRecordTime bvdfToEsRecordTime = mongoTemplate.findOne(query, BvdfToEsRecordTime.class);
		String scopeBeginTime = null;
		if (null != bvdfToEsRecordTime) {
			// 开始时间取上一次执行的最后时间
			scopeBeginTime = bvdfToEsRecordTime.getLastExcuteTime();
		}
		BvdfBldParam queryParam = new BvdfBldParam();
		// 状态正常
		queryParam.setState("normal");
		queryParam.setScopeBeginTime(scopeBeginTime);
		String scopeEndTime = UtilTool.nowTime();
		queryParam.setScopeEndTime(scopeEndTime);
		List<BvdfBldParam> bvdfBldParamList = bvdfBldService.queryBvdfBldInfo(queryParam);
		if (bvdfBldParamList.isEmpty()) {
			log.info("没有bvdfBldToEs的数据");
			return;
		}
		// 添加小区名称和开发企业名称
		bvdfBldParamList.forEach(bvdfBldParam -> {
			BvdfRegionParam regionParam = new BvdfRegionParam();
			regionParam.setRegionNo(bvdfBldParam.getRegionNo());
			regionParam.setState("normal");
			BvdfRegionParam bvdfRegionParam = bvdfRegionService.selectBvdfRegionInfo(regionParam);
			if (null != bvdfRegionParam) {
				bvdfBldParam.setRegionName(bvdfRegionParam.getRegionName());
			}
			BvdfCorpParam corpParam = new BvdfCorpParam();
			corpParam.setCorpId(bvdfBldParam.getCorpNo());
			corpParam.setState("normal");
			BvdfCorpParam bvdfCorpParam = bvdfCorpService.selectBvdfCorpInfo(corpParam);
			if (null != bvdfCorpParam) {
				bvdfBldParam.setCorpName(bvdfCorpParam.getCorpName());
			}
		});
		try (TransportClient client = new PreBuiltTransportClient(Settings.builder().put("cluster.name", esClusterName).build())
				.addTransportAddress(new TransportAddress(InetAddress.getByName(esIP), Integer.parseInt(esPort)))) {
			bvdfBldParamList.forEach(bvdfBldParam -> {
				// 拼装新增es的数据
				XContentBuilder doc = organizeBldToEsData(bvdfBldParam);
				// 往elasticsearch迁移一条数据，elasticsearch主键相同会覆盖原数据，该处不用判断
				elasticSearchService.insertElasticSearch(client, doc, bldindex, bldtype, bvdfBldParam.getBldNo());
			});
		} catch (UnknownHostException e) {
			log.error("创建elasticsearch客户端连接失败" + e);
			throw new MsgException(ReturnCode.sdp_sys_error, "创建elasticsearch客户端连接失败");
		}
		// bvdfToEsRecordTime为空时新增一条数据
		if (null == bvdfToEsRecordTime) {
			BvdfToEsRecordTime bvdfToEsForAdd = new BvdfToEsRecordTime();
			bvdfToEsForAdd.setId("bvdfBld");
			bvdfToEsForAdd.setLastExcuteTime(scopeEndTime);
			bvdfToEsRecordTime.setMatchType(MatchTypeEnum.BLD.getCode());
			bvdfToEsRecordTime.setDescribe(MatchTypeEnum.BLD.getDesc());
			mongoTemplate.insert(bvdfToEsForAdd);
		} else {
			Query queryupdate = new Query(Criteria.where("id").is("bvdfBld"));
			Update update = new Update().set("lastExcuteTime", scopeEndTime);
			mongoTemplate.updateFirst(queryupdate, update, BvdfToEsRecordTime.class);
		}

	}

	/**
	 * @Author: yinxunyang
	 * @Description: 拼装regionToElasticSearch的数据
	 * @Date: 2019/12/19 14:23
	 * @param:
	 * @return:
	 */
	private XContentBuilder organizeBldToEsData(BvdfBldParam bvdfBldParam) {
		XContentBuilder doc;
		try {
			doc = XContentFactory.jsonBuilder()
					.startObject()
					.field("dataCenterId", bvdfBldParam.getDataCenterId())
					.field("bldNo", bvdfBldParam.getBldNo())
					.field("bldName", bvdfBldParam.getBldName())
					.field("bldNameForKey", bvdfBldParam.getBldName())
					.field("address", bvdfBldParam.getAddress())
					.field("addressForKey", bvdfBldParam.getAddress())
					.field("totalArea", bvdfBldParam.getTotalArea())
					.field("startdate", bvdfBldParam.getStartdate())
					.field("finishdate", bvdfBldParam.getFinishdate())
					.field("regionNo", bvdfBldParam.getRegionNo())
					.field("regionName", bvdfBldParam.getRegionName())
					.field("corpNo", bvdfBldParam.getCorpNo())
					.field("corpName", bvdfBldParam.getCorpName())
					.field("versionnumber", bvdfBldParam.getVersionnumber())
					.field("divisionCode", bvdfBldParam.getDivisionCode())
					.endObject();
		} catch (IOException e) {
			log.error("拼装bldToElasticSearch的数据失败" + e);
			throw new MsgException(ReturnCode.fail, "拼装bldToElasticSearch的数据失败");
		}
		return doc;
	}
}
