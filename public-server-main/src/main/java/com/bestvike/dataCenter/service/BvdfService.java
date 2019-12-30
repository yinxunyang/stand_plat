package com.bestvike.dataCenter.service;

public interface BvdfService {
	/**
	 * @Author: yinxunyang
	 * @Description: 将bvdf开发公司信息迁移至elasticsearch
	 * @Date: 2019/12/19 11:24
	 * @param:
	 * @return:
	 */
	void bvdfCorpToEs();

	/**
	 * @Author: yinxunyang
	 * @Description: 将bvdf房屋信息迁移至elasticsearch
	 * @Date: 2019/12/5 18:12
	 * @param:
	 * @return:
	 */
	//void bvdfHouseToEs();
}
