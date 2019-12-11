package com.bestvike.bvdf.service;

import com.bestvike.mid.entity.MidHouseInfo;
import com.bestvike.bvdf.exception.MsgException;
import com.bestvike.bvdf.param.BvdfHouseParam;
import com.bestvike.bvdf.param.EsHouseParam;
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
	void insertCopyHouseAndEs(BvdfHouseParam bvdfHouseParam, TransportClient client, MidHouseInfo midHouseInfo, EsHouseParam esHouseParam) throws MsgException;

	/**
	 * @Author: yinxunyang
	 * @Description: 查询bvdf房屋的数据
	 * @Date: 2019/12/9 13:14
	 * @param:
	 * @return:
	 */
	List<BvdfHouseParam> queryBvdfHouseInfo(Map<String, Object> parameterMap);
}
