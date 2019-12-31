package com.bestvike.dataCenter.biz.impl;

import com.bestvike.commons.enums.DataCenterEnum;
import com.bestvike.commons.enums.MatchTypeEnum;
import com.bestvike.commons.enums.RecordTimeEnum;
import com.bestvike.commons.enums.ReturnCode;
import com.bestvike.commons.exception.MsgException;
import com.bestvike.commons.utils.StringUtils;
import com.bestvike.commons.utils.UtilTool;
import com.bestvike.dataCenter.biz.BvdfHouseBiz;
import com.bestvike.dataCenter.entity.BvdfToEsRecordTime;
import com.bestvike.dataCenter.param.BvdfBldParam;
import com.bestvike.dataCenter.param.BvdfHouseParam;
import com.bestvike.dataCenter.service.BvdfBldService;
import com.bestvike.dataCenter.service.BvdfCorpService;
import com.bestvike.dataCenter.service.BvdfHouseService;
import com.bestvike.dataCenter.service.BvdfRegionService;
import com.bestvike.elastic.param.EsHouseParam;
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
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
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
	private static final DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

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
		String scopeBeginTime = "1970-01-01 08:00:00";
		if (null != bvdfToEsRecordTime) {
			// 开始时间取上一次执行的最后时间
			scopeBeginTime = bvdfToEsRecordTime.getLastExcuteTime();
		}
		BvdfHouseParam queryParam = new BvdfHouseParam();
		// 状态正常
		queryParam.setState(DataCenterEnum.NORMAL_STATE.getCode());
		queryParam.setAppcode(DataCenterEnum.BVDF_APP_CODE_LOWER.getCode());
		queryParam.setScopeBeginTime(scopeBeginTime);
		// 最多查询2万条
		queryParam.setHouseMaxNum(Integer.parseInt(houseMaxNum));
		String scopeEndTime = UtilTool.nowTime();
		queryParam.setScopeEndTime(scopeEndTime);
		List<BvdfHouseParam> bvdfHouseParamList = bvdfHouseService.queryBvdfHouseInfo(queryParam);
		if (bvdfHouseParamList.isEmpty()) {
			log.info("没有bvdfHouseToEs的数据");
			return;
		}
		List<EsHouseParam> esHouseParamList = new ArrayList<>();
		// 组织es的数据 添加小区名称和开发企业名称等
		bvdfHouseParamList.stream().forEach(bvdfHouseParam -> {
			BvdfHouseParam bvdfHouse = bvdfHouseService.selectBvdfHouseInfo(bvdfHouseParam);
			EsHouseParam esHouseParam = new EsHouseParam();
			// todo
			esHouseParam.setDevelopName(null);
			esHouseParam.setDataCenterId(bvdfHouse.getDataCenterId());
			esHouseParam.setHouseId(bvdfHouse.getHouseid());
			esHouseParam.setHouseType(bvdfHouse.getHousetype());
			esHouseParam.setBldNo(bvdfHouse.getBldno());
			BvdfBldParam bldQueryParam = new BvdfBldParam();
			bldQueryParam.setBldNo(bvdfHouse.getBldno());
			// 根据自然幢编号查询自然幢名称
			BvdfBldParam bvdfBldParam = bvdfBldService.selectBvdfBldInfo(bldQueryParam);
			String bldName = null;
			if (null != bvdfBldParam) {
				bldName = bvdfBldParam.getBldName();
			}
			if (StringUtils.isEmpty(bldName)) {
				bldName = "无";
			}
			esHouseParam.setBldName(bldName);
			esHouseParam.setCellNo(bvdfHouse.getCellno());
			// todo
			esHouseParam.setCellName(null);
			esHouseParam.setFloorNo(bvdfHouse.getFloorno());
			esHouseParam.setFloorName(bvdfHouse.getFloorname());
			esHouseParam.setShowName(bvdfHouse.getShowname());
			esHouseParam.setRoomNo(bvdfHouse.getRoomno());
			esHouseParam.setConstructArea(bvdfHouse.getConstructArea());
			esHouseParam.setHouseAddress(bvdfHouse.getAddress());
			esHouseParam.setVersionnumber(bvdfHouse.getVersionnumber());
			esHouseParamList.add(esHouseParam);
		});
		try (TransportClient client = new PreBuiltTransportClient(Settings.builder().put("cluster.name", esClusterName).build())
				.addTransportAddress(new TransportAddress(InetAddress.getByName(esIP), Integer.parseInt(esPort)))) {
			esHouseParamList.forEach(esHouseParam -> {
				// 拼装新增es的数据
				XContentBuilder doc = organizeHouseToEsData(esHouseParam);
				// 往elasticsearch迁移一条数据，elasticsearch主键相同会覆盖原数据，该处不用判断
				elasticSearchService.insertElasticSearch(client, doc, houseindex, housetype, esHouseParam.getHouseId());
			});
		} catch (UnknownHostException e) {
			log.error("创建elasticsearch客户端连接失败" + e);
			throw new MsgException(ReturnCode.sdp_sys_error, "创建elasticsearch客户端连接失败");
		}
			// bvdfToEsRecordTime为空时新增一条数据
			if (null == bvdfToEsRecordTime) {
				BvdfToEsRecordTime bvdfToEsForAdd = new BvdfToEsRecordTime();
				bvdfToEsForAdd.setId(RecordTimeEnum.BVDF_HOUSE_ID.getCode());
				bvdfToEsForAdd.setLastExcuteTime(queryParam.getScopeEndTime());
				bvdfToEsForAdd.setMatchType(MatchTypeEnum.HOUSE.getCode());
				bvdfToEsForAdd.setDescribe(MatchTypeEnum.HOUSE.getDesc());
				mongoTemplate.insert(bvdfToEsForAdd);
			} else {
				Query queryupdate = new Query(Criteria.where("id").is(RecordTimeEnum.BVDF_HOUSE_ID.getCode()));
				Update update = new Update().set(RecordTimeEnum.LAST_EXCUTE_TIME.getCode(), queryParam.getScopeEndTime());
				mongoTemplate.updateFirst(queryupdate, update, BvdfToEsRecordTime.class);
			}
	}
	/**
	 * @Author: yinxunyang
	 * @Description: 拼装houseToElasticSearch的数据
	 * @Date: 2019/12/19 14:23
	 * @param:
	 * @return:
	 */
	private XContentBuilder organizeHouseToEsData(EsHouseParam esHouseParam) {
		XContentBuilder doc;
		try {
			doc = XContentFactory.jsonBuilder()
					.startObject()
					.field("developName", esHouseParam.getDevelopName())
					.field("dataCenterId", esHouseParam.getDataCenterId())
					.field("houseId", esHouseParam.getHouseId())
					.field("houseType", esHouseParam.getHouseType())
					.field("bldNo", esHouseParam.getBldNo())
					.field("bldName", esHouseParam.getBldName())
					.field("cellNo", esHouseParam.getCellNo())
					.field("cellName", esHouseParam.getCellName())
					.field("floorNo", esHouseParam.getFloorNo())
					.field("floorName", esHouseParam.getFloorName())
					.field("showName", esHouseParam.getShowName())
					.field("roomNo", esHouseParam.getRoomNo())
					.field("constructArea", esHouseParam.getConstructArea())
					.field("houseAddress", esHouseParam.getHouseAddress())
					.field("versionnumber", esHouseParam.getVersionnumber())
					.endObject();
		} catch (IOException e) {
			log.error("拼装houseToElasticSearch的数据失败" + e);
			throw new MsgException(ReturnCode.fail, "拼装houseToElasticSearch的数据失败");
		}
		return doc;
	}
}
