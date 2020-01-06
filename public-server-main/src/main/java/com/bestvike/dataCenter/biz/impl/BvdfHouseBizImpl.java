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
import com.bestvike.dataCenter.param.BvdfCellParam;
import com.bestvike.dataCenter.param.BvdfCorpParam;
import com.bestvike.dataCenter.param.BvdfHouseParam;
import com.bestvike.dataCenter.param.BvdfRegionParam;
import com.bestvike.dataCenter.service.BvdfBldService;
import com.bestvike.dataCenter.service.BvdfCellService;
import com.bestvike.dataCenter.service.BvdfCorpService;
import com.bestvike.dataCenter.service.BvdfHouseService;
import com.bestvike.dataCenter.service.BvdfRegionService;
import com.bestvike.dataCenter.service.MongoDBService;
import com.bestvike.elastic.service.ElasticSearchService;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
@Slf4j
public class BvdfHouseBizImpl implements BvdfHouseBiz {
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
	private MongoDBService mongoDBService;
	@Autowired
	private BvdfRegionService bvdfRegionService;
	@Autowired
	private BvdfCorpService bvdfCorpService;
	@Autowired
	private BvdfHouseService bvdfHouseService;
	@Autowired
	private BvdfCellService bvdfCellService;

	/**
	 * @Author: yinxunyang
	 * @Description: 将bvdf房屋信息迁移至elasticsearch
	 * @Date: 2019/12/19 11:25
	 * @param:
	 * @return:
	 */
	@Override
	//@Scheduled(cron = "${standplatConfig.houseToEsSchedule.cronTime}")
	public void bvdfHouseToEs() {
		// 查询时间记录表
		BvdfToEsRecordTime bvdfToEsRecordTime = mongoDBService.queryBvdfToEsRecordTimeById(RecordTimeEnum.BVDF_HOUSE_ID);
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
		// 组织es的数据 添加小区名称和开发企业名称等
		organizeEsHouseParamList(bvdfHouseParamList);
		// 遍历
		bvdfHouseParamList.forEach(bvdfHouseParam -> {
			// 拼装新增es的数据
			XContentBuilder doc = organizeHouseToEsData(bvdfHouseParam);
			// 往elasticsearch迁移一条数据，elasticsearch主键相同会覆盖原数据，该处不用判断
			elasticSearchService.insertElasticSearch(doc, houseindex, housetype, bvdfHouseParam.getHouseid());
		});
		// bvdfToEsRecordTime为空时新增时间记录表
		if (null == bvdfToEsRecordTime) {
			mongoDBService.insertBvdfToEsRecordTime(RecordTimeEnum.BVDF_HOUSE_ID, MatchTypeEnum.HOUSE, scopeEndTime);
		} else {
			mongoDBService.updateBvdfToEsRecordTime(RecordTimeEnum.BVDF_HOUSE_ID, MatchTypeEnum.HOUSE, scopeEndTime);
		}
	}

	/**
	 * @Author: yinxunyang
	 * @Description: 组织房屋信息推送es的数据 添加小区名称和开发企业名称等
	 * @Date: 2019/12/31 15:04
	 * @param:
	 * @return:
	 */
	private void organizeEsHouseParamList(List<BvdfHouseParam> bvdfHouseParamList) {
		bvdfHouseParamList.stream().forEach(bvdfHouseParam -> {
			try {
				BvdfHouseParam bvdfHouse = bvdfHouseService.selectBvdfHouseInfo(bvdfHouseParam);
				BvdfBldParam bldQueryParam = new BvdfBldParam();
				bldQueryParam.setBldNo(bvdfHouse.getBldno());
				// 根据自然幢编号查询自然幢名称
				BvdfBldParam bvdfBldParam = bvdfBldService.selectBvdfBldInfo(bldQueryParam);
				String bldName = "无";
				String corpNo = "无";
				String regionNo = "无";
				if (null != bvdfBldParam) {
					bldName = bvdfBldParam.getBldName();
					corpNo = bvdfBldParam.getCorpNo();
					regionNo = bvdfBldParam.getRegionNo();
				}
				bvdfHouseParam.setBldno(bvdfHouse.getBldno());
				bvdfHouseParam.setHouseid(bvdfHouse.getHouseid());
				bvdfHouseParam.setHousetype(bvdfHouse.getHousetype());
				bvdfHouseParam.setBldName(bldName);
				String developName = "无";
				BvdfCorpParam corpQueryParam = new BvdfCorpParam();
				corpQueryParam.setCorpId(corpNo);
				// 查询开发企业名称
				BvdfCorpParam bvdfCorpParam = bvdfCorpService.selectBvdfCorpInfo(corpQueryParam);
				if (null != bvdfCorpParam) {
					developName = bvdfCorpParam.getCorpName();
				}
				bvdfHouseParam.setDevelopNo(corpNo);
				bvdfHouseParam.setDevelopName(developName);
				BvdfRegionParam regionQueryParam = new BvdfRegionParam();
				regionQueryParam.setRegionNo(regionNo);
				BvdfRegionParam bvdfRegionParam = bvdfRegionService.selectBvdfRegionInfo(regionQueryParam);
				String regionName = "无";
				if (null != bvdfRegionParam) {
					regionName = bvdfRegionParam.getRegionName();
				}
				bvdfHouseParam.setRegionNo(regionNo);
				bvdfHouseParam.setRegionName(regionName);
				bvdfHouseParam.setCellno(bvdfHouse.getCellno());
				BvdfCellParam cellQueryParam = new BvdfCellParam();
				cellQueryParam.setBldNo(bvdfHouse.getBldno());
				cellQueryParam.setCellNo(bvdfHouse.getCellno());
				cellQueryParam.setHouseType(bvdfHouse.getHousetype());
				// 查询单元名称
				BvdfCellParam bvdfCellParam = bvdfCellService.selectBvdfCellInfo(cellQueryParam);
				String cellName = "无";
				if (null != bvdfCellParam) {
					cellName = bvdfCellParam.getCellName();
				}
				bvdfHouseParam.setCellName(cellName);
				bvdfHouseParam.setFloorno(bvdfHouse.getFloorno());
				bvdfHouseParam.setFloorname(bvdfHouse.getFloorname());
				bvdfHouseParam.setShowname(bvdfHouse.getShowname());
				bvdfHouseParam.setRoomno(bvdfHouse.getRoomno());
				bvdfHouseParam.setConstructArea(bvdfHouse.getConstructArea());
				bvdfHouseParam.setAddress(bvdfHouse.getAddress());
				bvdfHouseParam.setVersionnumber(bvdfHouse.getVersionnumber());
				// 标准化处理跟es交互的数据
				elasticSearchService.bvdfHouseParamFormat(bvdfHouseParam);
			} catch (Exception e) {
				log.error("组织房屋信息推送es的数据失败,bvdfHouseParam为{}",bvdfHouseParam,e);
			}
		});

	}
	/**
	 * @Author: yinxunyang
	 * @Description: 拼装houseToElasticSearch的数据
	 * @Date: 2019/12/19 14:23
	 * @param:
	 * @return:
	 */
	private XContentBuilder organizeHouseToEsData(BvdfHouseParam bvdfHouseParam) {
		XContentBuilder doc;
		try {
			doc = XContentFactory.jsonBuilder()
					.startObject()
					.field("regionNo", bvdfHouseParam.getRegionNo())
					.field("regionName", bvdfHouseParam.getRegionName())
					.field("developNo", bvdfHouseParam.getDevelopNo())
					.field("developName", bvdfHouseParam.getDevelopName())
					.field("dataCenterId", bvdfHouseParam.getDataCenterId())
					.field("houseId", bvdfHouseParam.getHouseid())
					.field("houseType", bvdfHouseParam.getHousetype())
					.field("bldNo", bvdfHouseParam.getBldno())
					.field("bldName", bvdfHouseParam.getBldName())
					.field("cellNo", bvdfHouseParam.getCellno())
					.field("cellName", bvdfHouseParam.getCellName())
					.field("floorNo", bvdfHouseParam.getFloorno())
					.field("floorName", bvdfHouseParam.getFloorname())
					.field("showName", bvdfHouseParam.getShowname())
					.field("roomNo", bvdfHouseParam.getRoomno())
					.field("constructArea", bvdfHouseParam.getConstructArea())
					.field("houseAddress", bvdfHouseParam.getAddress())
					.field("versionnumber", bvdfHouseParam.getVersionnumber())
					.endObject();
		} catch (IOException e) {
			log.error("拼装houseToElasticSearch的数据失败" + e);
			throw new MsgException(ReturnCode.fail, "拼装houseToElasticSearch的数据失败");
		}
		return doc;
	}
}
