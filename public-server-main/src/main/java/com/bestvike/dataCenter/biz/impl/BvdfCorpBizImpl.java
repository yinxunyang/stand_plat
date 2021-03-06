package com.bestvike.dataCenter.biz.impl;

import com.bestvike.commons.enums.CorpTypeEnum;
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
import com.bestvike.dataCenter.service.MongoDBService;
import com.bestvike.elastic.service.ElasticSearchService;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class BvdfCorpBizImpl implements BvdfCorpBiz {
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
	private MongoDBService mongoDBService;
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
		try {
			BvdfCorpParam queryParam = new BvdfCorpParam();
			// 状态正常
			queryParam.setState(DataCenterEnum.NORMAL_STATE.getCode());
			queryParam.setAppcode(DataCenterEnum.BVDF_APP_CODE_CAPITAL.getCode());
			// 开发企业
			queryParam.setCorpType(CorpTypeEnum.HOUSE_DEVELOPER.getCode());
			// 查询时间记录表
			BvdfToEsRecordTime bvdfToEsRecordTime = mongoDBService.queryBvdfToEsRecordTimeById(RecordTimeEnum.BVDF_CORP_ID);
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
			if (bvdfCorpParamList.isEmpty()) {
				log.info("没有bvdfCorpToEs的数据");
				return;
			}
			// 往elasticsearch迁移数据
			bvdfCorpToElasticSearch(bvdfCorpParamList);
			// bvdfToEsRecordTime为空时新增时间记录表
			if (null == bvdfToEsRecordTime) {
				mongoDBService.insertBvdfToEsRecordTime(RecordTimeEnum.BVDF_CORP_ID, MatchTypeEnum.DEVELOP, scopeEndTime);
			} else {
				mongoDBService.updateBvdfToEsRecordTime(RecordTimeEnum.BVDF_CORP_ID, MatchTypeEnum.DEVELOP, scopeEndTime);
			}
		} catch (Exception e) {
			log.error("bvdfCorpToEs定时任务失败", e);
		}
	}

	/**
	 * @Author: yinxunyang
	 * @Description: 往elasticsearch迁移数据
	 * @Date: 2020/1/7 9:13
	 * @param:
	 * @return:
	 */
	private void bvdfCorpToElasticSearch(List<BvdfCorpParam> bvdfCorpParamList) {
		// 遍历
		bvdfCorpParamList.forEach(bvdfCorpParam -> {
			try {
				// 拼装新增es的数据
				XContentBuilder doc = organizeCorpToEsData(bvdfCorpParam);
				// 往elasticsearch迁移一条数据，elasticsearch主键相同会覆盖原数据，该处不用判断
				elasticSearchService.insertElasticSearch(doc, corpindex, corptype, bvdfCorpParam.getCorpId());
			} catch (Exception e) {
				log.error("bvdfCorpToEs迁移数据失败，参数为{}", bvdfCorpParam);
			}
		});
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
		} catch (Exception e) {
			log.error("拼装corpToElasticSearch的数据失败" + e);
			throw new MsgException(ReturnCode.fail, "拼装corpToElasticSearch的数据失败");
		}
		return doc;
	}
}
