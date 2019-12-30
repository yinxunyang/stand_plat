package com.bestvike.dataCenter.biz.impl;

import com.bestvike.commons.enums.DataCenterEnum;
import com.bestvike.commons.enums.MatchTypeEnum;
import com.bestvike.commons.enums.RecordTimeEnum;
import com.bestvike.commons.enums.ReturnCode;
import com.bestvike.commons.exception.MsgException;
import com.bestvike.commons.utils.UtilTool;
import com.bestvike.dataCenter.biz.BvdfHouseBiz;
import com.bestvike.dataCenter.entity.BvdfToEsRecordTime;
import com.bestvike.dataCenter.param.BvdfBldParam;
import com.bestvike.dataCenter.param.BvdfHouseParam;
import com.bestvike.dataCenter.service.BvdfBldService;
import com.bestvike.dataCenter.service.BvdfCorpService;
import com.bestvike.dataCenter.service.BvdfHouseService;
import com.bestvike.dataCenter.service.BvdfRegionService;
import com.bestvike.elastic.service.ElasticSearchService;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
@Slf4j
public class BvdfHouseBizImpl implements BvdfHouseBiz {
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
	 * es房屋信息的索引
	 */
	@Value("${esConfig.houseindex}")
	private String houseindex;
	/**
	 * es房屋信息的映射
	 */
	@Value("${esConfig.housetype}")
	private String housetype;
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
	@Autowired
	private BvdfHouseService bvdfHouseService;

	/**
	 * @Author: yinxunyang
	 * @Description: 将bvdf房屋信息迁移至elasticsearch
	 * @Date: 2019/12/19 11:25
	 * @param:
	 * @return:
	 */
	@Override
	@Scheduled(cron = "${standplatConfig.houseToEsSchedule.cronTime}")
	public void bvdfHouseToEs() {
		Query query = new Query(Criteria.where("_id").is(RecordTimeEnum.BVDF_HOUSE_ID.getCode()));
		BvdfToEsRecordTime bvdfToEsRecordTime = mongoTemplate.findOne(query, BvdfToEsRecordTime.class);
		String scopeBeginTime = null;
		if (null != bvdfToEsRecordTime) {
			// 开始时间取上一次执行的最后时间
			scopeBeginTime = bvdfToEsRecordTime.getLastExcuteTime();
		}
		// todo 查询房屋的总数量，如果总数量大于20000，ScopeEndTime取结束和开始的中间数，直至小于20000
		BvdfHouseParam queryParam = new BvdfHouseParam();
		// 状态正常
		queryParam.setState(DataCenterEnum.NORMAL_STATE.getCode());
		queryParam.setAppcode(DataCenterEnum.BVDF_APP_CODE_LOWER.getCode());
		queryParam.setScopeBeginTime(scopeBeginTime);
		String scopeEndTime = UtilTool.nowTime();
		queryParam.setScopeEndTime(scopeEndTime);
		List<BvdfHouseParam> bvdfHouseParamList = bvdfHouseService.queryBvdfHouseInfo(queryParam);
		if (bvdfHouseParamList.isEmpty()) {
			log.info("没有bvdfHouseToEs的数据");
			return;
		}
		// 添加小区名称和开发企业名称
		/*bvdfHouseParamList.forEach(bvdfBldParam -> {
			BvdfRegionParam regionParam = new BvdfRegionParam();
			regionParam.setRegionNo(bvdfBldParam.getRegionNo());
			regionParam.setState(DataCenterEnum.NORMAL_STATE.getCode());
			BvdfRegionParam bvdfRegionParam = bvdfRegionService.selectBvdfRegionInfo(regionParam);
			if (null != bvdfRegionParam) {
				bvdfBldParam.setRegionName(bvdfRegionParam.getRegionName());
			}
			BvdfCorpParam corpParam = new BvdfCorpParam();
			corpParam.setCorpId(bvdfBldParam.getCorpNo());
			corpParam.setState(DataCenterEnum.NORMAL_STATE.getCode());
			BvdfCorpParam bvdfCorpParam = bvdfCorpService.selectBvdfCorpInfo(corpParam);
			if (null != bvdfCorpParam) {
				bvdfBldParam.setCorpName(bvdfCorpParam.getCorpName());
			}
		});*/
		/*try (TransportClient client = new PreBuiltTransportClient(Settings.builder().put("cluster.name", esClusterName).build())
				.addTransportAddress(new TransportAddress(InetAddress.getByName(esIP), Integer.parseInt(esPort)))) {
			bvdfBldParamList.forEach(bvdfBldParam -> {
				// 拼装新增es的数据
				XContentBuilder doc = organizeBldToEsData(bvdfBldParam);
				// 往elasticsearch迁移一条数据，elasticsearch主键相同会覆盖原数据，该处不用判断
				elasticSearchService.insertElasticSearch(client, doc, houseindex, housetype, bvdfBldParam.getBldNo());
			});
		} catch (UnknownHostException e) {
			log.error("创建elasticsearch客户端连接失败" + e);
			throw new MsgException(ReturnCode.sdp_sys_error, "创建elasticsearch客户端连接失败");
		}*/
		// bvdfToEsRecordTime为空时新增一条数据
		if (null == bvdfToEsRecordTime) {
			BvdfToEsRecordTime bvdfToEsForAdd = new BvdfToEsRecordTime();
			bvdfToEsForAdd.setId(RecordTimeEnum.BVDF_HOUSE_ID.getCode());
			bvdfToEsForAdd.setLastExcuteTime(scopeEndTime);
			bvdfToEsRecordTime.setMatchType(MatchTypeEnum.BLD.getCode());
			bvdfToEsRecordTime.setDescribe(MatchTypeEnum.BLD.getDesc());
			mongoTemplate.insert(bvdfToEsForAdd);
		} else {
			Query queryupdate = new Query(Criteria.where("id").is(RecordTimeEnum.BVDF_HOUSE_ID.getCode()));
			Update update = new Update().set(RecordTimeEnum.LAST_EXCUTE_TIME.getCode(), scopeEndTime);
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
