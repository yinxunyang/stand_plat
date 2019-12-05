package com.bestvike.pub.service.impl;

import com.bestvike.portal.service.BaseService;
import com.bestvike.pub.dao.BvdfHouseDao;
import com.bestvike.pub.param.BvdfHouseParam;
import com.bestvike.pub.service.BvdfService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BvdfServiceImpl extends BaseService implements BvdfService {
	@Autowired
	private BvdfHouseDao bvdfHouseDao;

	@Override
	public List<BvdfHouseParam> queryBvdfHouseInfo() {
		List<BvdfHouseParam> bvdfHouseParamList = bvdfHouseDao.queryBvdfHouseInfo();
		// todo 往es数据库迁移数据和其他用户传数据
		int i= 0;
		return bvdfHouseParamList;
	}
}
