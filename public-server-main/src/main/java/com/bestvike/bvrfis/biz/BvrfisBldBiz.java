package com.bestvike.bvrfis.biz;

import com.bestvike.commons.exception.MsgException;

import javax.servlet.http.HttpSession;

/**
 * @Author: yinxunyang
 * @Description: 维修资金自然幢信息的biz
 * @Date: 2019/12/10 17:03
 */
public interface BvrfisBldBiz {

	/**
	 * @Author: yinxunyang
	 * @Description: 将bvrfis自然幢跟es中的匹配
	 * @Date: 2019/12/11 13:14
	 * @param:
	 * @return:
	 */
	void bvrfisBldMatchEs(HttpSession httpSession) throws MsgException;
}
