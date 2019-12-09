package com.bestvike.pub.service;

import com.bestvike.pub.exception.BusinessException;
import com.bestvike.pub.param.BvdfHouseParam;
import org.elasticsearch.client.transport.TransportClient;

/**
 * @Author: yinxunyang
 * @Description: elasticsearch
 * @Date: 2019/12/6 16:57
 */
public interface ElasticSearchService {
	/**
	 * @Author: yinxunyang
	 * @Description: 往elasticsearch迁移一条数据
	 * @Date: 2019/12/9 13:42
	 * @param:
	 * @return:
	 */
	void insertElasticSearch(BvdfHouseParam bvdfHouseParam, TransportClient client) throws BusinessException;
}
