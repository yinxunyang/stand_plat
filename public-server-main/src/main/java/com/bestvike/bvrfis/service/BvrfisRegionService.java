package com.bestvike.bvrfis.service;

import com.bestvike.bvrfis.param.BvrfisRegionParam;

import java.util.List;

/**
 * @Author: yinxunyang
 * @Description: 维修资金小区信息的service
 * @Date: 2019/12/10 17:03
 */
public interface BvrfisRegionService {

	/**
	 * @Author: yinxunyang
	 * @Description: 查询bvrfis小区的数据
	 * @Date: 2019/12/9 13:14
	 * @param:
	 * @return:
	 */
	List<BvrfisRegionParam> queryBvrfisRegionInfo(BvrfisRegionParam queryParam);
	/**
	 * @Author: yinxunyang
	 * @Description: 查询bvrfis小区的数据
	 * @Date: 2019/12/9 13:14
	 * @param:
	 * @return:
	 */
	BvrfisRegionParam selectBvrfisRegionInfo(BvrfisRegionParam queryParam);
}
