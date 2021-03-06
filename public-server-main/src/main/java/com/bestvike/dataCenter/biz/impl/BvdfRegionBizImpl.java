package com.bestvike.dataCenter.biz.impl;

import com.bestvike.commons.enums.DataCenterEnum;
import com.bestvike.commons.enums.MatchTypeEnum;
import com.bestvike.commons.enums.RecordTimeEnum;
import com.bestvike.commons.enums.ReturnCode;
import com.bestvike.commons.exception.MsgException;
import com.bestvike.commons.utils.UtilTool;
import com.bestvike.dataCenter.biz.BvdfRegionBiz;
import com.bestvike.dataCenter.entity.BvdfToEsRecordTime;
import com.bestvike.dataCenter.param.BvdfRegionParam;
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
public class BvdfRegionBizImpl implements BvdfRegionBiz {
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
	private ElasticSearchService elasticSearchService;
	@Autowired
	private BvdfRegionService bvdfRegionService;
	@Autowired
	private MongoDBService mongoDBService;

	/**
	 * @Author: yinxunyang
	 * @Description: 将bvdf小区信息迁移至elasticsearch
	 * @Date: 2019/12/19 11:25
	 * @param:
	 * @return:
	 */
	@Override
	//@Scheduled(cron = "${standplatConfig.corpToEsSchedule.cronTime}")
	public void bvdfRegionToEs() {
		try {
			BvdfRegionParam queryParam = new BvdfRegionParam();
			// 状态正
			queryParam.setState(DataCenterEnum.NORMAL_STATE.getCode());
			queryParam.setAppcode(DataCenterEnum.BVDF_APP_CODE_CAPITAL.getCode());
			// 查询时间记录表
			BvdfToEsRecordTime bvdfToEsRecordTime = mongoDBService.queryBvdfToEsRecordTimeById(RecordTimeEnum.BVDF_REGION_ID);
			String scopeBeginTime = null;
			if (null != bvdfToEsRecordTime) {
				// 开始时间取上一次执行的最后时间
				scopeBeginTime = bvdfToEsRecordTime.getLastExcuteTime();
			}
			queryParam.setScopeBeginTime(scopeBeginTime);
			String scopeEndTime = UtilTool.nowTime();
			queryParam.setScopeEndTime(scopeEndTime);
			List<BvdfRegionParam> bvdfRegionParamList = bvdfRegionService.queryBvdfRegionInfo(queryParam);
			if (bvdfRegionParamList.isEmpty()) {
				log.info("没有bvdfRegionToEs的数据");
				return;
			}
			// 遍历
			bvdfRegionParamList.forEach(bvdfRegionParam -> {
				// 拼装新增es的数据
				XContentBuilder doc = organizeRegionToEsData(bvdfRegionParam);
				// 往elasticsearch迁移一条数据，elasticsearch主键相同会覆盖原数据，该处不用判断
				elasticSearchService.insertElasticSearch(doc, regionindex, regiontype, bvdfRegionParam.getRegionNo());
			});
			// bvdfToEsRecordTime为空时新增时间记录表
			if (null == bvdfToEsRecordTime) {
				mongoDBService.insertBvdfToEsRecordTime(RecordTimeEnum.BVDF_REGION_ID, MatchTypeEnum.REGION, scopeEndTime);
			} else {
				mongoDBService.updateBvdfToEsRecordTime(RecordTimeEnum.BVDF_REGION_ID, MatchTypeEnum.REGION, scopeEndTime);
			}
		} catch (Exception e) {
			log.error("bvdfRegionToEs定时任务失败", e);
		}

	}

	/**
	 * @Author: yinxunyang
	 * @Description: 拼装regionToElasticSearch的数据
	 * @Date: 2019/12/19 14:23
	 * @param:
	 * @return:
	 */
	private XContentBuilder organizeRegionToEsData(BvdfRegionParam bvdfRegionParam) {
		XContentBuilder doc;
		try {
			doc = XContentFactory.jsonBuilder()
					.startObject()
					.field("dataCenterId", bvdfRegionParam.getDataCenterId())
					.field("corpNo", bvdfRegionParam.getCorpNo())
					.field("regionNo", bvdfRegionParam.getRegionNo())
					.field("regionName", bvdfRegionParam.getRegionName())
					.field("regionNameForKey", bvdfRegionParam.getRegionName())
					.field("divisionCode", bvdfRegionParam.getDivisionCode())
					.field("address", bvdfRegionParam.getAddress())
					.field("addressForKey", bvdfRegionParam.getAddress())
					.field("versionnumber", bvdfRegionParam.getVersionnumber())
					.field("floorArea", bvdfRegionParam.getFloorArea())
					.endObject();
		} catch (IOException e) {
			log.error("拼装regionToElasticSearch的数据失败", e);
			throw new MsgException(ReturnCode.fail, "拼装regionToElasticSearch的数据失败");
		}
		return doc;
	}

}
