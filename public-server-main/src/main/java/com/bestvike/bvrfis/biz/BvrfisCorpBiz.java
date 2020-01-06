package com.bestvike.bvrfis.biz;

import javax.servlet.http.HttpSession;

/**
 * @Author: yinxunyang
 * @Description: 维修资金开发企业信息的biz
 * @Date: 2019/12/10 17:03
 */
public interface BvrfisCorpBiz {

	/**
	 * @Author: yinxunyang
	 * @Description: 将bvrfis公司信息跟es中的匹配
	 * @Date: 2019/12/11 13:14
	 * @param:
	 * @return:
	 */
	void bvrfisCorpMatchEs(HttpSession httpSession) ;
}
