package com.bestvike.elastic.service;

import com.bestvike.commons.exception.MsgException;
import com.bestvike.dataCenter.param.BvdfHouseParam;
import org.elasticsearch.common.xcontent.XContentBuilder;

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
	void insertElasticSearch(XContentBuilder doc, String index, String type, String id) throws MsgException;

	/**
	 * @Author: yinxunyang
	 * @Description: 标准化处理跟es交互的数据
	 * @Date: 2019/12/12 17:25
	 * @param:
	 * @return:
	 */
	void bvdfHouseParamFormat(BvdfHouseParam bvdfHouseParam);

	/**
	 * @Author: yinxunyang
	 * @Description: 根据json文件组织查询Es的语句
	 * @Date: 2019/12/24 15:15
	 * @param:
	 * @return:
	 */
	String organizeQueryEsByJson(String jsonPath);
}
