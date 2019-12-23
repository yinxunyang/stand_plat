package com.bestvike.dataCenter.biz.impl;

import com.bestvike.commons.enums.ReturnCode;
import com.bestvike.commons.exception.MsgException;
import com.bestvike.dataCenter.biz.BvdfRegionBiz;
import com.bestvike.dataCenter.dao.BvdfHouseDao;
import com.bestvike.dataCenter.entity.BvdfToEsRecordTime;
import com.bestvike.dataCenter.param.BvdfCorpParam;
import com.bestvike.dataCenter.service.BvdfCorpService;
import com.bestvike.dataCenter.service.BvdfHouseService;
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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@Slf4j
public class BvdfRegionBizImpl implements BvdfRegionBiz {
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
	@Value("${esConfig.regionindex}")
	private String regionindex;
	/**
	 * es开发企业的映射
	 */
	@Value("${esConfig.regiontype}")
	private String regiontype;
	@Autowired
	private BvdfHouseService bvdfHouseService;
	@Autowired
	private BvdfHouseDao bvdfHouseDao;
	@Autowired
	private ElasticSearchService elasticSearchService;
	@Autowired
	private BvdfCorpService bvdfCorpService;
	@Autowired
	private MongoTemplate mongoTemplate;
	private static final DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

	/**
	 * @Author: yinxunyang
	 * @Description: 将bvdf小区信息迁移至elasticsearch
	 * @Date: 2019/12/19 11:25
	 * @param:
	 * @return:
	 */
	@Override
	@Scheduled(cron = "${standplatConfig.corpToEsSchedule.cronTime}")
	public void bvdfRegionToEs() {
		BvdfCorpParam queryParam = new BvdfCorpParam();
		// 状态正
		queryParam.setState("normal");
		queryParam.setAppcode("BVDF");
		Query query = new Query(Criteria.where("_id").is("bvdfRegion"));
		BvdfToEsRecordTime bvdfToEsRecordTime = mongoTemplate.findOne(query, BvdfToEsRecordTime.class);
		String scopeBeginTime = null;
		if (null != bvdfToEsRecordTime) {
			// 开始时间取上一次执行的最后时间
			scopeBeginTime = bvdfToEsRecordTime.getCorpLastExcuteTime();
		}
		queryParam.setScopeBeginTime(scopeBeginTime);
		String scopeEndTime = df.format(LocalDateTime.now());
		queryParam.setScopeEndTime(scopeEndTime);
		List<BvdfCorpParam> bvdfCorpParamList = bvdfCorpService.queryBvdfCorpInfo(queryParam);
		if (bvdfCorpParamList.isEmpty()) {
			log.info("没有bvdfCorpToEs的数据");
			return;
		}
		try (TransportClient client = new PreBuiltTransportClient(Settings.builder().put("cluster.name", esClusterName).build())
				.addTransportAddress(new TransportAddress(InetAddress.getByName(esIP), Integer.parseInt(esPort)))) {
			bvdfCorpParamList.forEach(bvdfCorpParam -> {
				// 拼装新增es的数据
				XContentBuilder doc = organizeCorpToEsData(bvdfCorpParam);
				// 往elasticsearch迁移一条数据，elasticsearch主键相同会覆盖原数据，该处不用判断
				elasticSearchService.insertElasticSearch(client, doc, regionindex, regiontype, bvdfCorpParam.getCorpId());
			});
		} catch (UnknownHostException e) {
			log.error("创建elasticsearch客户端连接失败" + e);
			throw new MsgException(ReturnCode.sdp_sys_error, "创建elasticsearch客户端连接失败");
		}
		// bvdfToEsRecordTime为空时新增一条数据
		if (null == bvdfToEsRecordTime) {
			BvdfToEsRecordTime bvdfToEsForAdd = new BvdfToEsRecordTime();
			bvdfToEsForAdd.setId("bvdfRegion");
			bvdfToEsForAdd.setCorpLastExcuteTime(scopeEndTime);
			mongoTemplate.insert(bvdfToEsForAdd);
		} else {
			Query queryupdate = new Query(Criteria.where("id").is("bvdfRegion"));
			Update update = new Update().set("corpLastExcuteTime", scopeEndTime);
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