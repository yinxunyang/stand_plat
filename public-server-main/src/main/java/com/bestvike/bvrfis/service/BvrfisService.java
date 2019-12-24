package com.bestvike.bvrfis.service;

import com.bestvike.commons.exception.MsgException;

/**
 * @Author: yinxunyang
 * @Description: 维修资金的service
 * @Date: 2019/12/10 17:03
 */
public interface BvrfisService {
	/**
	 * @Author: yinxunyang
	 * @Description: 将bvrfis房屋信息跟es中的匹配
	 * @Date: 2019/12/11 13:14
	 * @param:
	 * @return:
	 */
	void bvrfisHouseMatchEs() throws MsgException;

	/**
	 * @Author: yinxunyang
	 * @Description: 根据json文件组织查询Es的语句
	 * @Date: 2019/12/24 15:15
	 * @param:
	 * @return:
	 */
	String organizeQueryEsByJson(String jsonPath);
}
