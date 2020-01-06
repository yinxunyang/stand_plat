package com.bestvike.bvrfis.biz;

import javax.servlet.http.HttpSession;

/**
 * @Author: yinxunyang
 * @Description: 维修资金小区信息的biz
 * @Date: 2019/12/10 17:03
 */
public interface BvrfisRegionBiz {

	/**
	 * @Author: yinxunyang
	 * @Description: 将bvrfis小区信息跟es中的匹配
	 * @Date: 2019/12/11 13:14
	 * @param:
	 * @return:
	 */
	void bvrfisRegionMatchEs(HttpSession httpSession) ;
}
