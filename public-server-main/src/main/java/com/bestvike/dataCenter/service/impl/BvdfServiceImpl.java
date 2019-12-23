package com.bestvike.dataCenter.service.impl;

import com.bestvike.commons.enums.ReturnCode;
import com.bestvike.commons.exception.MsgException;
import com.bestvike.dataCenter.dao.BvdfHouseDao;
import com.bestvike.dataCenter.entity.BvdfToEsRecordTime;
import com.bestvike.dataCenter.param.BvdfCorpParam;
import com.bestvike.dataCenter.param.BvdfHouseParam;
import com.bestvike.dataCenter.service.BvdfCorpService;
import com.bestvike.dataCenter.service.BvdfHouseService;
import com.bestvike.dataCenter.service.BvdfService;
import com.bestvike.elastic.param.EsHouseParam;
import com.bestvike.elastic.service.ElasticSearchService;
import com.bestvike.mid.entity.MidHouseInfo;
import com.bestvike.mid.service.MidHouseService;
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
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class BvdfServiceImpl implements BvdfService {
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
	private BvdfHouseService bvdfHouseService;
	@Autowired
	private MidHouseService midHouseService;
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
	 * @Description: 将bvdf开发公司信息迁移至elasticsearch
	 * @Date: 2019/12/19 11:25
	 * @param:
	 * @return:
	 */
	@Override
	@Scheduled(cron = "${standplatConfig.corpToEsSchedule.cronTime}")
	public void bvdfCorpToEs() {
		BvdfCorpParam queryParam = new BvdfCorpParam();
		// 状态正
		queryParam.setState("normal");
		queryParam.setAppcode("BVDF");
		Query query = new Query(Criteria.where("_id").is("bvdfCorp"));
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
		// bvdfToEsRecordTime为空时新增一条数据
		if (null == bvdfToEsRecordTime) {
			BvdfToEsRecordTime bvdfToEsForAdd = new BvdfToEsRecordTime();
			bvdfToEsForAdd.setId("bvdfCorp");
			bvdfToEsForAdd.setCorpLastExcuteTime(scopeEndTime);
			mongoTemplate.insert(bvdfToEsForAdd);
		} else {
			Query queryupdate = new Query(Criteria.where("id").is("bvdfCorp"));
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
	/**
	 * @Author: yinxunyang
	 * @Description: 将bvdf房屋信息迁移至elasticsearch
	 * 定时5分钟一次, 开局自动执行
	 * @Date: 2019/12/5 13:41
	 * @param:
	 * @return:
	 */
	@Override
	public void bvdfHouseToEs() {
		try {
			Map<String, Object> parameterMap = new HashMap<>();
			parameterMap.put("houseMaxNum", Integer.valueOf(houseMaxNum));
			// 查询bvdf房屋的数据
			List<BvdfHouseParam> bvdfHouseParamList = bvdfHouseService.queryBvdfHouseInfo(parameterMap);
			if (bvdfHouseParamList.isEmpty()) {
				log.info("bvdf没有需要往elasticsearch迁移的房屋数据");
				return;
			}
			// 遍历新增房屋信息和迁移elasticsearch
			addCopyHouseAndEs(bvdfHouseParamList);
		} catch (Exception e) {
			log.error("定时任务：往ES迁移bvdf数据失败！" + e);
		}
	}

	/**
	 * @Author: yinxunyang
	 * @Description: 遍历新增房屋信息和迁移elasticsearch
	 * @Date: 2019/12/9 13:26
	 * @param:
	 * @return:
	 */
	private void addCopyHouseAndEs(List<BvdfHouseParam> bvdfHouseParamList) {
		try (TransportClient client = new PreBuiltTransportClient(Settings.builder().put("cluster.name", esClusterName).build())
				.addTransportAddress(new TransportAddress(InetAddress.getByName(esIP), Integer.parseInt(esPort)))) {
			// 批量新增的数量
			int houseMaxNumInt = Integer.parseInt(bvdfBatchNum);
			// 分批执行
			int numbers = bvdfHouseParamList.size();
			int pageNum = (numbers + houseMaxNumInt - 1) / houseMaxNumInt;
			for (int i = 1; i <= pageNum; i++) {
				// 每批次eachBvdfHouseParamList
				List<BvdfHouseParam> eachBvdfHouseParamList = bvdfHouseParamList.stream()
						.skip(houseMaxNumInt * (i - 1))
						.limit(houseMaxNumInt)
						.collect(Collectors.toList());
				// 批量新增房屋和es
				addHouseAndEsByBatch(eachBvdfHouseParamList, client);
			}
		} catch (UnknownHostException e) {
			log.error("创建elasticsearch客户端连接失败" + e);
			throw new MsgException(ReturnCode.sdp_sys_error, "创建elasticsearch客户端连接失败");
		} catch (Exception e) {
			log.error("批量新增房屋和es失败" + e);
		}
	}

	/**
	 * @Author: yinxunyang
	 * @Description: 批量新增房屋和es
	 * @Date: 2019/12/18 14:35
	 * @param:
	 * @return:
	 */
	private void addHouseAndEsByBatch(List<BvdfHouseParam> bvdfHouseParamList, TransportClient client) {
		List<BvdfHouseParam> bvdfHouseParamListForAdd = new ArrayList<>();
		List<BvdfHouseParam> bvdfHouseParamListForEdit = new ArrayList<>();
		List<EsHouseParam> esHouseParamList = new ArrayList<>();
		bvdfHouseParamList.forEach(bvdfHouseParam -> {
			// 根据主键查询中间库房屋信息
			MidHouseInfo midHouseInfo = midHouseService.queryMidHouseInfoById(bvdfHouseParam);
			if (null == midHouseInfo) {
				bvdfHouseParamListForAdd.add(bvdfHouseParam);
			} else {
				bvdfHouseParamListForEdit.add(bvdfHouseParam);
			}
			// 组织往elasticSearch推送的数据
			EsHouseParam esHouseParam = organizeEsHouseParam(bvdfHouseParam);
			esHouseParamList.add(esHouseParam);
		});
		bvdfHouseService.insertCopyHouseAndEsByBatch(bvdfHouseParamListForAdd, bvdfHouseParamListForEdit, client, esHouseParamList);
	}
	/**
	 * @Author: yinxunyang
	 * @Description: 组织往elasticSearch推送的数据
	 * @Date: 2019/12/10 13:29
	 * @param:
	 * @return:
	 */
	private EsHouseParam organizeEsHouseParam(BvdfHouseParam bvdfHouseParam) {
		try {
			EsHouseParam esHouseParam = new EsHouseParam();
			esHouseParam.setId(bvdfHouseParam.getSysguid());
			String corpName = null;
			String corpNo = bvdfHouseParam.getCorpno();
			String licenseNo = null;
			if (!StringUtils.isEmpty(corpNo)) {
				BvdfCorpParam bvdfCorpParam = bvdfHouseDao.selectCorpNameByCorpNo(corpNo);
				if (null != bvdfCorpParam) {
					corpName = bvdfCorpParam.getCorpName();
					licenseNo = bvdfCorpParam.getLicenseNo();
				}
			}
			if (StringUtils.isEmpty(corpName)) {
				corpName = "无";
			}
			if (StringUtils.isEmpty(licenseNo)) {
				licenseNo = "无";
			}
			// 开发企业名称
			esHouseParam.setDevelopName(corpName);
			esHouseParam.setLicenseNo(licenseNo);
			String bldName = null;
			String bldNo = bvdfHouseParam.getBldno();
			if (!StringUtils.isEmpty(bldNo)) {
				bldName = bvdfHouseDao.selectBldNameByBldNo(bldNo);
			}
			if (StringUtils.isEmpty(bldName)) {
				bldName = "无";
			}
			// 楼幢名称
			esHouseParam.setBldName(bldName);
			String cellName = null;
			String cellNo = bvdfHouseParam.getCellno();
			String housetype = bvdfHouseParam.getHousetype();
			if (!StringUtils.isEmpty(cellNo) && !StringUtils.isEmpty(bldNo)) {
				Map<String, Object> parameterMap = new HashMap<>();
				parameterMap.put("cellNo", cellNo);
				parameterMap.put("bldNo", bldNo);
				parameterMap.put("housetype", housetype);
				cellName = bvdfHouseDao.selectCellNameByCellNo(parameterMap);
			}
			if (StringUtils.isEmpty(cellName)) {
				cellName = "无";
			}
			// 单元名称
			esHouseParam.setCellName(cellName);
			esHouseParam.setFloorName(bvdfHouseParam.getFloorname());
			esHouseParam.setRoomno(bvdfHouseParam.getRoomno());
			esHouseParam.setBuyCertNos(bvdfHouseParam.getBuycertnos());
			esHouseParam.setBuyNames(bvdfHouseParam.getBuynames());
			esHouseParam.setHouseAddress(bvdfHouseParam.getAddress());
			// 标准化处理跟es交互的数据
			elasticSearchService.bvdfHouseParamFormat(esHouseParam);
			return esHouseParam;
		} catch (Exception e) {
			log.error("组织往elasticSearch推送的数据失败" + e);
			throw new MsgException(ReturnCode.sdp_select_fail, "组织往elasticSearch推送的数据失败");
		}
	}
}
