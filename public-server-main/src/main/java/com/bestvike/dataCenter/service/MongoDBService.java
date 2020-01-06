package com.bestvike.dataCenter.service;

import com.bestvike.commons.enums.MatchTypeEnum;
import com.bestvike.commons.enums.RecordTimeEnum;
import com.bestvike.commons.exception.MsgException;
import com.bestvike.dataCenter.entity.BvdfToEsRecordTime;

/**
 * @Author: yinxunyang
 * @Description: MongoDB的service
 * @Date: 2020/1/6 15:40
 */
public interface MongoDBService {
	/**
	 * @Author: yinxunyang
	 * @Description: 查询时间记录表
	 * @Date: 2019/12/9 13:14
	 * @param:
	 * @return:
	 */
	BvdfToEsRecordTime queryBvdfToEsRecordTimeById(RecordTimeEnum recordTimeEnum) throws MsgException;

	/**
	 * @Author: yinxunyang
	 * @Description: 新增时间记录表
	 * @Date: 2020/1/6 16:00
	 * @param:
	 * @return:
	 */
	void insertBvdfToEsRecordTime(RecordTimeEnum recordTimeEnum, MatchTypeEnum matchTypeEnum, String scopeEndTime) throws MsgException;

	/**
	 * @Author: yinxunyang
	 * @Description: 更新时间记录表
	 * @Date: 2020/1/6 16:00
	 * @param:
	 * @return:
	 */
	void updateBvdfToEsRecordTime(RecordTimeEnum recordTimeEnum, MatchTypeEnum matchTypeEnum, String scopeEndTime) throws MsgException;
}
