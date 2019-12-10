package com.bestvike.pub.service;

import com.bestvike.mid.entity.MidHouseInfo;
import com.bestvike.pub.exception.MsgException;
import com.bestvike.pub.param.BvdfHouseParam;
import org.elasticsearch.client.transport.TransportClient;

import java.util.List;
import java.util.Map;

public interface BvdfHouseService {
	/**
	 * @Author: yinxunyang
	 * @Description: 新增房屋信息和迁移elasticsearch
	 * @Date: 2019/12/5 18:12
	 * @param:
	 * @return:
	 */
	void insertCopyHouseAndEs(BvdfHouseParam bvdfHouseParam, TransportClient client, MidHouseInfo midHouseInfo) throws MsgException;

	/**
	 * @Author: yinxunyang
	 * @Description: 查询bvdf房屋的数据
	 * @Date: 2019/12/9 13:14
	 * @param:
	 * @return:
	 */
	List<BvdfHouseParam> queryBvdfHouseInfo(Map<String, Object> parameterMap);
}