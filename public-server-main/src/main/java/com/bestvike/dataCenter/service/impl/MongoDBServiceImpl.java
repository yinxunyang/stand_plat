package com.bestvike.dataCenter.service.impl;

import com.bestvike.commons.enums.MatchTypeEnum;
import com.bestvike.commons.enums.RecordTimeEnum;
import com.bestvike.commons.enums.ReturnCode;
import com.bestvike.commons.exception.MsgException;
import com.bestvike.dataCenter.entity.BvdfToEsRecordTime;
import com.bestvike.dataCenter.service.MongoDBService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

/**
 * @Author: yinxunyang
 * @Description: MongoDB的service实现
 * @Date: 2020/1/6 15:40
 */
@Service
@Slf4j
public class MongoDBServiceImpl implements MongoDBService {
	@Autowired
	private MongoTemplate mongoTemplate;

	/**
	 * @Author: yinxunyang
	 * @Description: 根据主键查询MongoDB中的时间记录表
	 * @Date: 2019/12/9 13:15
	 * @param:
	 * @return:
	 */
	@Override
	public BvdfToEsRecordTime queryBvdfToEsRecordTimeById(RecordTimeEnum recordTimeEnum) {
		BvdfToEsRecordTime bvdfToEsRecordTime;
		try {
			Query query = new Query(Criteria.where("_id").is(recordTimeEnum.getCode()));
			bvdfToEsRecordTime = mongoTemplate.findOne(query, BvdfToEsRecordTime.class);
		} catch (Exception e) {
			String returnMsg = "根据" + recordTimeEnum.getDesc() + "查询MongoDB中的时间记录表失败";
			log.error(returnMsg, e);
			throw new MsgException(ReturnCode.sdp_select_fail, returnMsg);
		}
		return bvdfToEsRecordTime;
	}

	/**
	 * @Author: yinxunyang
	 * @Description: 新增时间记录表
	 * @Date: 2020/1/6 16:01
	 * @param:
	 * @return:
	 */
	@Override
	public void insertBvdfToEsRecordTime(RecordTimeEnum recordTimeEnum, MatchTypeEnum matchTypeEnum, String scopeEndTime) {
		try {
			BvdfToEsRecordTime bvdfToEsForAdd = new BvdfToEsRecordTime();
			bvdfToEsForAdd.setId(recordTimeEnum.getCode());
			bvdfToEsForAdd.setLastExcuteTime(scopeEndTime);
			bvdfToEsForAdd.setMatchType(matchTypeEnum.getCode());
			bvdfToEsForAdd.setDescribe(matchTypeEnum.getDesc());
			mongoTemplate.insert(bvdfToEsForAdd);
		} catch (Exception e) {
			String returnMsg = "新增" + matchTypeEnum.getDesc() + "的MongoDB中的时间记录表失败";
			log.error(returnMsg, e);
			throw new MsgException(ReturnCode.sdp_select_fail, returnMsg);
		}
	}

	/**
	 * @Author: yinxunyang
	 * @Description: 更新时间记录表
	 * @Date: 2020/1/6 16:01
	 * @param:
	 * @return:
	 */
	@Override
	public void updateBvdfToEsRecordTime(RecordTimeEnum recordTimeEnum, MatchTypeEnum matchTypeEnum, String scopeEndTime) {
		try {
			Query queryupdate = new Query(Criteria.where("id").is(recordTimeEnum.getCode()));
			Update update = new Update().set(RecordTimeEnum.LAST_EXCUTE_TIME.getCode(), scopeEndTime);
			// 更新时间记录表
			mongoTemplate.updateFirst(queryupdate, update, BvdfToEsRecordTime.class);
		} catch (Exception e) {
			String returnMsg = "更新" + matchTypeEnum.getDesc() + "的MongoDB中的时间记录表失败";
			log.error(returnMsg, e);
			throw new MsgException(ReturnCode.sdp_select_fail, returnMsg);
		}
	}
}
