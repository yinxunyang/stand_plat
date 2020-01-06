package com.bestvike.dataCenter.service;

import com.bestvike.commons.exception.MsgException;
import com.bestvike.dataCenter.param.BvdfCorpParam;

import java.util.List;

public interface BvdfCorpService {

	/**
	 * @Author: yinxunyang
	 * @Description: 查询bvdf开发公司的数据
	 * @Date: 2019/12/9 13:14
	 * @param:
	 * @return:
	 */
	List<BvdfCorpParam> queryBvdfCorpInfo(BvdfCorpParam queryParam) throws MsgException;
	/**
	 * @Author: yinxunyang
	 * @Description: 查询bvdf开发公司的数据
	 * @Date: 2019/12/9 13:14
	 * @param:
	 * @return:
	 */
	BvdfCorpParam selectBvdfCorpInfo(BvdfCorpParam queryParam);
}
