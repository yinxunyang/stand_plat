package com.bestvike.elastic.service;

import com.bestvike.commons.exception.MsgException;
import com.bestvike.elastic.param.EsHouseParam;
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

	/**
	 * @Author: yinxunyang
	 * @Description: 标准化处理跟es交互的数据
	 * @Date: 2019/12/12 17:25
	 * @param:
	 * @return:
	 */
	void bvdfHouseParamFormat(EsHouseParam esHouseParam);
}