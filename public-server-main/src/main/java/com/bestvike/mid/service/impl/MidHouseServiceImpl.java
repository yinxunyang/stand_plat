package com.bestvike.mid.service.impl;

import com.bestvike.mid.dao.MidHouseDao;
import com.bestvike.mid.service.MidHouseService;
import com.bestvike.pub.enums.ReturnCode;
import com.bestvike.pub.exception.MsgException;
import com.bestvike.pub.param.BvdfHouseParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class MidHouseServiceImpl implements MidHouseService {
	@Autowired
	private MidHouseDao midHouseDao;

	/**
	 * @Author: yinxunyang
	 * @Description: 往中间库新增房屋信息
	 * @Date: 2019/12/9 13:52
	 * @param:
	 * @return:
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public int insertBvdfHouseInfo(BvdfHouseParam bvdfHouseParam) {
		int inNum;
		try {
			inNum = midHouseDao.insertBvdfHouseInfo(bvdfHouseParam);
		} catch (Exception e) {
			log.error("往中间库新增房屋信息失败" + e);
			throw new MsgException(ReturnCode.sdp_insert_fail.toCode(), "往中间库新增房屋信息失败");
		}
		return inNum;
	}
}
