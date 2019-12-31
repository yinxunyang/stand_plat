package com.bestvike.dataCenter.service;

import com.bestvike.dataCenter.param.BvdfBldParam;

import java.util.List;

public interface BvdfBldService {
	/**
	 * @Author: yinxunyang
	 * @Description: 查询bvdf自然幢的数据
	 * @Date: 2019/12/9 13:14
	 * @param:
	 * @return:
	 */
	List<BvdfBldParam> queryBvdfBldInfo(BvdfBldParam queryParam);

	/**
	 * @Author: yinxunyang
	 * @Description: 查询bvdf自然幢的数据
	 * @Date: 2019/12/9 13:14
	 * @param:
	 * @return:
	 */
	BvdfBldParam selectBvdfBldInfo(BvdfBldParam queryParam);
}