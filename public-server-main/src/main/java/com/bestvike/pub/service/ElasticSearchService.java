package com.bestvike.pub.service;

import com.bestvike.pub.exception.BusinessException;
import com.bestvike.pub.param.BvdfHouseParam;
import org.elasticsearch.client.transport.TransportClient;

/**
 * @Author: yinxunyang
 * @Description:
 * @Date: 2019/12/6 16:57
 * @param:
 * @return:
 */
public interface ElasticSearchService {
	void insertElasticSearch(BvdfHouseParam bvdfHouseParam, TransportClient client) throws BusinessException;
}
