package com.bestvike.dataCenter.service.impl;

import com.bestvike.commons.enums.ReturnCode;
import com.bestvike.commons.exception.MsgException;
import com.bestvike.dataCenter.dao.BvdfHouseDao;
import com.bestvike.dataCenter.param.BvdfHouseParam;
import com.bestvike.dataCenter.service.BvdfHouseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class BvdfHouseServiceImpl implements BvdfHouseService {
	@Autowired
	private BvdfHouseDao bvdfHouseDao;
	/**
	 * @Author: yinxunyang
	 * @Description: 查询bvdf房屋的数据
	 * @Date: 2019/12/9 13:15
	 * @param:
	 * @return:
	 */
	@Override
	public List<BvdfHouseParam> queryBvdfHouseInfo(BvdfHouseParam queryParam) {
		List<BvdfHouseParam> bvdfHouseParamList;
		try {
			bvdfHouseParamList = bvdfHouseDao.queryBvdfHouseInfo(queryParam);
		} catch (Exception e) {
			log.error("查询bvdf房屋的数据失败" + e);
			throw new MsgException(ReturnCode.sdp_select_fail, "查询bvdf房屋的数据失败");
		}
		return bvdfHouseParamList;
	}
	/**
	 * @Author: yinxunyang
	 * @Description: 查询bvdf房屋的数据
	 * @Date: 2019/12/9 13:15
	 * @param:
	 * @return:
	 */
	@Override
	public BvdfHouseParam selectBvdfHouseInfo(BvdfHouseParam queryParam)  {
		BvdfHouseParam bvdfHouseParam;
		try {
			bvdfHouseParam = bvdfHouseDao.selectBvdfHouseInfo(queryParam);
		} catch (Exception e) {
			log.error("查询bvdf房屋的数据失败" + e);
			throw new MsgException(ReturnCode.sdp_select_fail, "查询bvdf房屋的数据失败");
		}
		return bvdfHouseParam;
	}

	/**
	 * @Author: yinxunyang
	 * @Description: 查询bvdf房屋的数量
	 * @Date: 2019/12/30 11:15
	 * @param:
	 * @return:
	 */
	@Override
	public int countBvdfHouseInfo(BvdfHouseParam queryParam) {
		int countNum;
		try {
			countNum = bvdfHouseDao.countBvdfHouseInfo(queryParam);
		} catch (Exception e) {
			log.error("查询bvdf房屋的数量失败" + e);
			throw new MsgException(ReturnCode.sdp_select_fail, "查询bvdf房屋的数量失败");
		}
		return countNum;
	}
}
