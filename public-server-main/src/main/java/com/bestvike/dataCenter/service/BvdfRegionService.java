package com.bestvike.dataCenter.service;

import com.bestvike.dataCenter.param.BvdfRegionParam;

import java.util.List;

public interface BvdfRegionService {
	/**
	 * @Author: yinxunyang
	 * @Description: 查询bvdf小区的数据
	 * @Date: 2019/12/9 13:14
	 * @param:
	 * @return:
	 */
	List<BvdfRegionParam> queryBvdfRegionInfo(BvdfRegionParam queryParam);

	/**
	 * @Author: yinxunyang
	 * @Description: 查询bvdf小区的数据
	 * @Date: 2019/12/9 13:14
	 * @param:
	 * @return:
	 */
	BvdfRegionParam selectBvdfRegionInfo(BvdfRegionParam queryParam);
}
