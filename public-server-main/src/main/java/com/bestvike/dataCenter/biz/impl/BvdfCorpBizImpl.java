package com.bestvike.dataCenter.biz.impl;

import com.bestvike.commons.enums.DataCenterEnum;
import com.bestvike.commons.enums.MatchTypeEnum;
import com.bestvike.commons.enums.RecordTimeEnum;
import com.bestvike.commons.enums.ReturnCode;
import com.bestvike.commons.exception.MsgException;
import com.bestvike.commons.utils.UtilTool;
import com.bestvike.dataCenter.biz.BvdfCorpBiz;
import com.bestvike.dataCenter.entity.BvdfToEsRecordTime;
import com.bestvike.dataCenter.param.BvdfCorpParam;
import com.bestvike.dataCenter.service.BvdfCorpService;
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
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

@Service
@Slf4j
public class BvdfCorpBizImpl implements BvdfCorpBiz {
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
	 * es开发企业的索引
	 */
	@Value("${esConfig.corpindex}")
	private String corpindex;
	/**
	 * es开发企业的映射
	 */
	@Value("${esConfig.corptype}")
	private String corptype;
	@Autowired
	private ElasticSearchService elasticSearchService;
	@Autowired
	private BvdfCorpService bvdfCorpService;
	@Autowired
	private MongoTemplate mongoTemplate;
	/**
	 * @Author: yinxunyang
	 * @Description: 将bvdf开发公司信息迁移至elasticsearch
	 * @Date: 2019/12/19 11:25
	 * @param:
	 * @return:
	 */
	@Override
	//@Scheduled(cron = "${standplatConfig.corpToEsSchedule.cronTime}")
	public void bvdfCorpToEs() {
		BvdfCorpParam queryParam = new BvdfCorpParam();
		// 状态正常
		queryParam.setState(DataCenterEnum.NORMAL_STATE.getCode());
		queryParam.setAppcode(DataCenterEnum.BVDF_APP_CODE_CAPITAL.getCode());
		Query query = new Query(Criteria.where("_id").is(RecordTimeEnum.BVDF_CORP_ID.getCode()));
		BvdfToEsRecordTime bvdfToEsRecordTime = mongoTemplate.findOne(query, BvdfToEsRecordTime.class);
		String scopeBeginTime = null;
		if (null != bvdfToEsRecordTime) {
			// 开始时间取上一次执行的最后时间
			scopeBeginTime = bvdfToEsRecordTime.getLastExcuteTime();
		}
		queryParam.setScopeBeginTime(scopeBeginTime);
		String scopeEndTime = UtilTool.nowTime();
		queryParam.setScopeEndTime(scopeEndTime);
		// 根据时间范围查询bvdf开发企业信息
		List<BvdfCorpParam> bvdfCorpParamList = bvdfCorpService.queryBvdfCorpInfo(queryParam);
		if(bvdfCorpParamList.isEmpty()){
			log.info("没有bvdfCorpToEs的数据");
			return;
		}
		try (TransportClient client = new PreBuiltTransportClient(Settings.builder().put("cluster.name", esClusterName).build())
				.addTransportAddress(new TransportAddress(InetAddress.getByName(esIP), Integer.parseInt(esPort)))) {
			bvdfCorpParamList.forEach(bvdfCorpParam -> {
				// 拼装新增es的数据
				XContentBuilder doc = organizeCorpToEsData(bvdfCorpParam);
				// 往elasticsearch迁移一条数据，elasticsearch主键相同会覆盖原数据，该处不用判断
				elasticSearchService.insertElasticSearch(client, doc, corpindex, corptype, bvdfCorpParam.getCorpId());
			});
		} catch (UnknownHostException e) {
			log.error("创建elasticsearch客户端连接失败" + e);
			throw new MsgException(ReturnCode.sdp_sys_error, "创建elasticsearch客户端连接失败");
		}
		// bvdfToEsRecordTime为空时新增时间记录表
		if (null == bvdfToEsRecordTime) {
			BvdfToEsRecordTime bvdfToEsForAdd = new BvdfToEsRecordTime();
			bvdfToEsForAdd.setId(RecordTimeEnum.BVDF_CORP_ID.getCode());
			bvdfToEsForAdd.setLastExcuteTime(scopeEndTime);
			bvdfToEsRecordTime.setMatchType(MatchTypeEnum.DEVELOP.getCode());
			bvdfToEsRecordTime.setDescribe(MatchTypeEnum.DEVELOP.getDesc());
			mongoTemplate.insert(bvdfToEsForAdd);
		} else {
			Query queryupdate = new Query(Criteria.where("id").is(RecordTimeEnum.BVDF_CORP_ID.getCode()));
			Update update = new Update().set(RecordTimeEnum.LAST_EXCUTE_TIME.getCode(), scopeEndTime);
			// 更新时间记录表
			mongoTemplate.updateFirst(queryupdate, update, BvdfToEsRecordTime.class);
		}

	}

	/**
	 * @Author: yinxunyang
	 * @Description: 拼装corpToElasticSearch的数据
	 * @Date: 2019/12/19 14:23
	 * @param:
	 * @return:
	 */
	private XContentBuilder organizeCorpToEsData(BvdfCorpParam bvdfCorpParam) {
		XContentBuilder doc;
		try {
			doc = XContentFactory.jsonBuilder()
					.startObject()
					.field("dataCenterId", bvdfCorpParam.getDataCenterId())
					.field("corpId", bvdfCorpParam.getCorpId())
					.field("corpName", bvdfCorpParam.getCorpName())
					.field("corpNameForKey", bvdfCorpParam.getCorpName())
					.field("certificateNo", bvdfCorpParam.getCertificateNo())
					.field("versionnumber", bvdfCorpParam.getVersionnumber())
					.endObject();
		} catch (IOException e) {
			log.error("拼装corpToElasticSearch的数据失败" + e);
			throw new MsgException(ReturnCode.fail, "拼装corpToElasticSearch的数据失败");
		}
		return doc;
	}
}
