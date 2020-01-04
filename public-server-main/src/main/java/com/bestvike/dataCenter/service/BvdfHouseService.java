package com.bestvike.dataCenter.service;

import com.bestvike.dataCenter.param.BvdfHouseParam;

import java.util.List;

public interface BvdfHouseService {
	/**
	 * @Author: yinxunyang
	 * @Description: 批量新增房屋信息和迁移elasticsearch
	 * @Date: 2019/12/5 18:12
	 * @param:
	 * @return:
	 *//*
	void insertCopyHouseAndEsByBatch(List<BvdfHouseParam> bvdfHouseParamListForAdd, List<BvdfHouseParam> bvdfHouseParamListForEdit,
	                                 TransportClient client, List<EsHouseParam> esHouseParamList) throws MsgException;*/

	/**
	 * @Author: yinxunyang
	 * @Description: 查询bvdf房屋的数据
	 * @Date: 2019/12/9 13:14
	 * @param:
	 * @return:
	 */
	List<BvdfHouseParam> queryBvdfHouseInfo(BvdfHouseParam queryParam);
	/**
	 * @Author: yinxunyang
	 * @Description: 查询bvdf房屋的数据
	 * @Date: 2019/12/9 13:14
	 * @param:
	 * @return:
	 */
	BvdfHouseParam selectBvdfHouseInfo(BvdfHouseParam queryParam);
	/**
	 * @Author: yinxunyang
	 * @Description: 查询bvdf房屋的数量
	 * @Date: 2019/12/9 13:14
	 * @param:
	 * @return:
	 */
	int countBvdfHouseInfo(BvdfHouseParam queryParam);
}
