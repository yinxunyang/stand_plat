package com.bestvike.pub.service.impl;

import com.bestvike.mid.entity.MidHouseInfo;
import com.bestvike.mid.service.MidHouseService;
import com.bestvike.pub.dao.BvdfHouseDao;
import com.bestvike.pub.enums.ReturnCode;
import com.bestvike.pub.exception.MsgException;
import com.bestvike.pub.param.BvdfHouseParam;
import com.bestvike.pub.service.BvdfHouseService;
import com.bestvike.pub.service.ElasticSearchService;
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
	 * @Description: 新增房屋信息和迁移elasticsearch
	 * @Date: 2019/12/6 14:40
	 * @param:
	 * @return:
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void insertCopyHouseAndEs(BvdfHouseParam bvdfHouseParam, TransportClient client, MidHouseInfo midHouseInfo) throws MsgException {
		// midHouseInfo为空的话新增一条midHouseInfo，否则更新
		if (null == midHouseInfo) {
			// 新增房屋信息
			int inNum = midHouseService.insertBvdfHouseInfo(bvdfHouseParam);
			if (1 != inNum) {
				throw new MsgException(ReturnCode.sdp_insert_fail, "新增中间库房屋信息失败");
			}
		} else {
			// 更新房屋信息
			int upNum = midHouseService.updateBvdfHouseInfoById(bvdfHouseParam);
			if (1 != upNum) {
				throw new MsgException(ReturnCode.sdp_update_fail, "更新中间库房屋信息失败");
			}
		}
		// 往elasticsearch迁移一条数据，elasticsearch主键相同会覆盖原数据，改处不用判断
		elasticSearchService.insertElasticSearch(bvdfHouseParam, client);
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
