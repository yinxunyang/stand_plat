package com.bestvike.bvdf.service;

import com.bestvike.commons.exception.MsgException;
import com.bestvike.bvdf.param.EsHouseParam;
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
	void insertElasticSearch(TransportClient client, EsHouseParam esHouseParam) throws MsgException;
}
