package com.bestvike.bvrfis.service;

import com.bestvike.bvrfis.param.BvrfisCorpInfoParam;

import java.util.List;

/**
 * @Author: yinxunyang
 * @Description: 维修资金开发企业信息的service
 * @Date: 2019/12/10 17:03
 */
public interface BvrfisCorpService {

	/**
	 * @Author: yinxunyang
	 * @Description: 查询bvrfis开发企业的数据
	 * @Date: 2019/12/9 13:14
	 * @param:
	 * @return:
	 */
	List<BvrfisCorpInfoParam> queryBvrfisCorpInfo(BvrfisCorpInfoParam queryParam);
}
