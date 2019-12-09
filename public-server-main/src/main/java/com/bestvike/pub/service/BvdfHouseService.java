package com.bestvike.pub.service;

import com.bestvike.pub.param.BvdfHouseParam;
import org.elasticsearch.client.transport.TransportClient;

public interface BvdfHouseService {
	/**
	 * @Author: yinxunyang
	 * @Description: 新增房屋信息和迁移elasticsearch
	 * @Date: 2019/12/5 18:12
	 * @param:
	 * @return:
	 */
	void insertCopyHouseAndEs(BvdfHouseParam bvdfHouseParam, TransportClient client) throws Exception;
}
