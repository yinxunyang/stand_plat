package com.bestvike.bvrfis.biz;

import com.bestvike.commons.exception.MsgException;

import javax.servlet.http.HttpSession;

/**
 * @Author: yinxunyang
 * @Description: 维修资金房屋信息的biz
 * @Date: 2019/12/10 17:03
 */
public interface BvrfisHouseBiz {

	/**
	 * @Author: yinxunyang
	 * @Description: 将bvrfis房屋信息跟es中的匹配
	 * @Date: 2019/12/11 13:14
	 * @param:
	 * @return:
	 */
	void bvrfisHouseMatchEs(HttpSession httpSession) throws MsgException;
}
