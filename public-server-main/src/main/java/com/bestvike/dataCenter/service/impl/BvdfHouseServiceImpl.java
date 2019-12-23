package com.bestvike.dataCenter.service.impl;

import com.bestvike.commons.enums.ReturnCode;
import com.bestvike.commons.exception.MsgException;
import com.bestvike.dataCenter.dao.BvdfHouseDao;
import com.bestvike.dataCenter.param.BvdfHouseParam;
import com.bestvike.dataCenter.service.BvdfHouseService;
import com.bestvike.elastic.param.EsHouseParam;
import com.bestvike.elastic.service.ElasticSearchService;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class BvdfHouseServiceImpl implements BvdfHouseService {

	@Autowired
	private ElasticSearchService elasticSearchService;
	@Autowired
	private BvdfHouseDao bvdfHouseDao;
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
	 * @Description: 批量新增房屋信息和迁移elasticsearch
	 * @Date: 2019/12/6 14:40
	 * @param:
	 * @return:
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void insertCopyHouseAndEsByBatch(List<BvdfHouseParam> bvdfHouseParamListForAdd, List<BvdfHouseParam> bvdfHouseParamListForEdit,
	                                        TransportClient client, List<EsHouseParam> esHouseParamList) throws MsgException {
		esHouseParamList.forEach(esHouseParam -> {
			// 拼装新增es的数据
			XContentBuilder doc = organizeEsData(esHouseParam);
			// 往elasticsearch迁移一条数据，elasticsearch主键相同会覆盖原数据，该处不用判断
			elasticSearchService.insertElasticSearch(client, doc, index, type, esHouseParam.getId());
		});
	}

	/**
	 * @Author: yinxunyang
	 * @Description: 拼装新增es的数据
	 * @Date: 2019/12/9 14:05
	 * @param:
	 * @return:
	 */
	private XContentBuilder organizeEsData(EsHouseParam esHouseParam) {
		XContentBuilder doc;
		try {
			doc = XContentFactory.jsonBuilder()
					.startObject()
					.field("buyCertNos", esHouseParam.getBuyCertNos())
					.field("developName", esHouseParam.getDevelopName())
					.field("licenseNo", esHouseParam.getLicenseNo())
					.field("bldName", esHouseParam.getBldName())
					.field("cellName", esHouseParam.getCellName())
					.field("floorName", esHouseParam.getFloorName())
					.field("roomNo", esHouseParam.getRoomno())
					.field("buyNames", esHouseParam.getBuyNames())
					.field("houseAddress", esHouseParam.getHouseAddress())
					.field("buyCertNosForKey", esHouseParam.getBuyCertNos())
					.field("developNameForKey", esHouseParam.getDevelopName())
					.field("licenseNoForKey", esHouseParam.getLicenseNo())
					.field("bldNameForKey", esHouseParam.getBldName())
					.field("cellNameForKey", esHouseParam.getCellName())
					.field("floorNameForKey", esHouseParam.getFloorName())
					.field("roomNoForKey", esHouseParam.getRoomno())
					.field("buyNamesForKey", esHouseParam.getBuyNames())
					.field("houseAddressForKey", esHouseParam.getHouseAddress())
					.endObject();
		} catch (IOException e) {
			log.error("拼装ElasticSearch的数据失败" + e);
			throw new MsgException(ReturnCode.fail, "拼装ElasticSearch的数据失败");
		}
		return doc;
	}

	/**
	 * @Author: yinxunyang
	 * @Description: 查询bvdf房屋的数据
	 * @Date: 2019/12/9 13:15
	 * @param:
	 * @return:
	 */
	@Override
	public List<BvdfHouseParam> queryBvdfHouseInfo(Map<String, Object> parameterMap) throws MsgException {
		List<BvdfHouseParam> bvdfHouseParamList;
		try {
			bvdfHouseParamList = bvdfHouseDao.queryBvdfHouseInfo(parameterMap);
		} catch (Exception e) {
			log.error("查询bvdf房屋的数据失败");
			throw new MsgException(ReturnCode.sdp_select_fail, "查询bvdf房屋的数据失败");
		}
		return bvdfHouseParamList;
	}
}
