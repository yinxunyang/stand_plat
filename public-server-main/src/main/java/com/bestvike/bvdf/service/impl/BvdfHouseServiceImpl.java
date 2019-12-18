package com.bestvike.bvdf.service.impl;

import com.bestvike.bvdf.dao.BvdfHouseDao;
import com.bestvike.bvdf.param.BvdfHouseParam;
import com.bestvike.bvdf.service.BvdfHouseService;
import com.bestvike.commons.enums.ReturnCode;
import com.bestvike.commons.exception.MsgException;
import com.bestvike.elastic.param.EsHouseParam;
import com.bestvike.elastic.service.ElasticSearchService;
import com.bestvike.mid.service.MidHouseService;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.client.transport.TransportClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class BvdfHouseServiceImpl implements BvdfHouseService {

	@Autowired
	private MidHouseService midHouseService;
	@Autowired
	private ElasticSearchService elasticSearchService;
	@Autowired
	private BvdfHouseDao bvdfHouseDao;

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
		if (!bvdfHouseParamListForAdd.isEmpty()) {
			// 批量新增房屋信息
			int inNum = midHouseService.insertBvdfHouseInfoByBatch(bvdfHouseParamListForAdd);
			if (bvdfHouseParamListForAdd.size() != inNum) {
				throw new MsgException(ReturnCode.sdp_insert_fail, "批量新增中间库房屋信息失败");
			}
		}
		if (!bvdfHouseParamListForEdit.isEmpty()) {
			// 批量更新房屋信息
			int upNum = midHouseService.updateBvdfHouseInfoByBatch(bvdfHouseParamListForEdit);
			if (-1 != upNum) {
				throw new MsgException(ReturnCode.sdp_update_fail, "批量更新中间库房屋信息失败");
			}
		}
		esHouseParamList.forEach(esHouseParam -> {
			// 往elasticsearch迁移一条数据，elasticsearch主键相同会覆盖原数据，该处不用判断
			elasticSearchService.insertElasticSearch(client, esHouseParam);
		});
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
