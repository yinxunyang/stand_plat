package com.bestvike.dataCenter.service;

import com.bestvike.dataCenter.param.BvdfCellParam;

public interface BvdfCellService {
	/**
	 * @Author: yinxunyang
	 * @Description: 查询bvdf开发公司的数据
	 * @Date: 2019/12/9 13:14
	 * @param:
	 * @return:
	 */
	BvdfCellParam selectBvdfCellInfo(BvdfCellParam queryParam);
}
