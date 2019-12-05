package com.bestvike.pub.service.impl;

import com.bestvike.portal.service.BaseService;
import com.bestvike.pub.dao.BvdfHouseDao;
import com.bestvike.pub.param.BvdfHouseParam;
import com.bestvike.pub.service.BvdfService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BvdfServiceImpl extends BaseService implements BvdfService {
	@Autowired
	private BvdfHouseDao bvdfHouseDao;

	/**
	 * @Author: yinxunyang
	 * @Description: 定时5分钟一次, 开局自动执行
	 * @Date: 2019/12/5 13:41
	 * @param:
	 * @return:
	 */
	@Override
	@Scheduled(fixedRate = 1000 * 60 * 5)
	public List<BvdfHouseParam> queryBvdfHouseInfo() {
		List<BvdfHouseParam> bvdfHouseParamList = bvdfHouseDao.queryBvdfHouseInfo();
		// todo 往es数据库迁移数据和其他用户传数据
		int i= 0;
		return bvdfHouseParamList;
	}
}
