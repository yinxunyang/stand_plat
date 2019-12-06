package com.bestvike.pub.service.impl;

import com.bestvike.mid.service.MidHouseService;
import com.bestvike.pub.param.BvdfHouseParam;
import com.bestvike.pub.service.BvdfHouseService;
import com.bestvike.pub.service.ElasticSearchService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class BvdfHouseServiceImpl implements BvdfHouseService {

	@Autowired
	private MidHouseService midHouseService;
	@Autowired
	private ElasticSearchService elasticSearchService;

	/**
	 * @Author: yinxunyang
	 * @Description: 新增房屋信息和迁移elasticsearch
	 * @Date: 2019/12/6 14:40
	 * @param:
	 * @return:
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void insertCopyHouseAndEs(BvdfHouseParam bvdfHouseParam) throws Exception {
		// 新增房屋信息
		int inNum = midHouseService.insertBvdfHouseInfo(bvdfHouseParam);
		if (1 != inNum) {
			log.error("新增失败");
			// 返回，不再迁移elasticsearch
			return;
		}
		// 往elasticsearch迁移一条数据
		elasticSearchService.insertElasticSearch(bvdfHouseParam);
	}
}
