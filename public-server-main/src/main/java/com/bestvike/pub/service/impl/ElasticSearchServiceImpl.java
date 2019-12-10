package com.bestvike.pub.service.impl;

import com.bestvike.pub.enums.EsStatusEnum;
import com.bestvike.pub.enums.ReturnCode;
import com.bestvike.pub.exception.MsgException;
import com.bestvike.pub.param.BvdfHouseParam;
import com.bestvike.pub.service.ElasticSearchService;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * @Author: yinxunyang
 * @Description: elasticSearch实现类
 * @Date: 2019/12/6 16:57
 */
@Slf4j
@Service
public class ElasticSearchServiceImpl implements ElasticSearchService {
	/**
	 * es房屋的索引
	 */
	@Value("${esConfig.index}")
	private String index;
	/**
	 * es房屋的映射
	 */
	@Value("${esConfig.type}")
	private String type;

	/**
	 * @Author: yinxunyang
	 * @Description: 往elasticsearch迁移一条数据
	 * @Date: 2019/12/9 15:26
	 * @param:
	 * @return:
	 */
	@Override
	public void insertElasticSearch(BvdfHouseParam bvdfHouseParam, TransportClient client) throws MsgException {
		// 唯一编号
		String id = bvdfHouseParam.getSysguid();
		// 拼装新增es的数据
		XContentBuilder doc = organizeEsData(bvdfHouseParam);
		// es的返回状态
		String esStatus;
		IndexResponse response;
		try {
			// 往es新增数据及获取返回报文
			response = client.prepareIndex(index, type, id).setSource(doc).get();
			esStatus = response.status().toString();
			log.info("esStatus状态为：{}", esStatus);
		} catch (Exception e) {
			log.error("往ElasticSearch迁移失败" + e);
			throw new MsgException(ReturnCode.sdp_es_insert_fail, "往ElasticSearch迁移失败");
		}
		if (EsStatusEnum.ES_INSERTED.getCode().equals(esStatus)) {
			log.info("往ElasticSearch新增成功一条数据");
		} else if (EsStatusEnum.ES_UPDATED.getCode().equals(esStatus)) {
			log.info("往ElasticSearch更新成功一条数据");
		} else {
			log.error("往ElasticSearch迁移失败，返回状态不是CREATED或OK，response为：{}", response);
			throw new MsgException(ReturnCode.sdp_es_insert_fail, "往ElasticSearch迁移失败，返回状态不是CREATED或OK");
		}
	}

	/**
	 * @Author: yinxunyang
	 * @Description: 拼装新增es的数据
	 * @Date: 2019/12/9 14:05
	 * @param:
	 * @return:
	 */
	private XContentBuilder organizeEsData(BvdfHouseParam bvdfHouseParam) {
		XContentBuilder doc;
		try {
			doc = XContentFactory.jsonBuilder()
					.startObject()
					.field("buycertnos", bvdfHouseParam.getBuycertnos())
					.field("regionno", bvdfHouseParam.getProjectno())
					.field("bldno", bvdfHouseParam.getBldno())
					.field("cellno", bvdfHouseParam.getCellno())
					.field("floorname", bvdfHouseParam.getFloorname())
					.field("roomno", bvdfHouseParam.getRoomno())
					.field("buynames", bvdfHouseParam.getBuynames())
					.field("houseAddress", bvdfHouseParam.getAddress())
					.endObject();
		} catch (IOException e) {
			log.error("拼装ElasticSearch的数据失败" + e);
			throw new MsgException(ReturnCode.fail, "拼装ElasticSearch的数据失败");
		}
		return doc;
	}
}
