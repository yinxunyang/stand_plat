package com.bestvike.pub.service.impl;

import com.bestvike.pub.param.BvdfHouseParam;
import com.bestvike.pub.service.ElasticSearchService;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.springframework.stereotype.Service;

/**
 * @Author: yinxunyang
 * @Description:
 * @Date: 2019/12/6 16:57
 * @param:
 * @return:
 */
@Slf4j
@Service
public class ElasticSearchServiceImpl implements ElasticSearchService {
	@Override
	public void insertElasticSearch(BvdfHouseParam bvdfHouseParam, TransportClient client) throws Exception {
		String index = "house_index";
		String type = "house_type";
		// 唯一编号
		String id = bvdfHouseParam.getSysguid();
		XContentBuilder doc = XContentFactory.jsonBuilder()
				.startObject()
				.field("buycertnos", bvdfHouseParam.getBuycertnos())
				.field("regionno", bvdfHouseParam.getRegionno())
				.field("bldno", bvdfHouseParam.getBldno())
				.field("cellno", bvdfHouseParam.getCellno())
				.field("floorname", bvdfHouseParam.getFloorname())
				.field("roomno", bvdfHouseParam.getRoomno())
				.field("buynames", bvdfHouseParam.getBuynames())
				.field("houseAddress", bvdfHouseParam.getHouseAddress())
				.endObject();
			IndexResponse response = client.prepareIndex(index, type, id).setSource(doc).get();
			String esStatus = response.status().toString();
			if ("CREATED".equals(esStatus)) {
				log.info("往ElasticSearch新增成功一条数据");
			} else if ("OK".equals(esStatus)) {
				log.info("往ElasticSearch更新成功一条数据");
			} else {
				throw new Exception("往ElasticSearch迁移失败response:" + response);
			}

	}
}
