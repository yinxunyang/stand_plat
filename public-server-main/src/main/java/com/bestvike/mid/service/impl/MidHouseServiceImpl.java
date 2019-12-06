package com.bestvike.mid.service.impl;

import com.bestvike.mid.dao.MidHouseDao;
import com.bestvike.mid.service.MidHouseService;
import com.bestvike.pub.param.BvdfHouseParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MidHouseServiceImpl implements MidHouseService {
	@Autowired
	private MidHouseDao midHouseDao;
	@Override
	public int insertBvdfHouseInfo(BvdfHouseParam bvdfHouseParam) {
		return midHouseDao.insertBvdfHouseInfo(bvdfHouseParam);
	}
}
