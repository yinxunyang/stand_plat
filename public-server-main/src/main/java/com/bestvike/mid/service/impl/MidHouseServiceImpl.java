package com.bestvike.mid.service.impl;

import com.bestvike.mid.dao.MidHouseDao;
import com.bestvike.mid.entity.MidHouseInfo;
import com.bestvike.mid.service.MidHouseService;
import com.bestvike.commons.enums.ReturnCode;
import com.bestvike.commons.exception.MsgException;
import com.bestvike.bvdf.param.BvdfHouseParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
public class MidHouseServiceImpl implements MidHouseService {
	@Autowired
	private MidHouseDao midHouseDao;

	/**
	 * @Author: yinxunyang
	 * @Description: 批量往中间库新增房屋信息
	 * @Date: 2019/12/9 13:52
	 * @param:
	 * @return:
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public int insertBvdfHouseInfoByBatch(List<BvdfHouseParam> bvdfHouseParamListForAdd) {
		int inNum;
		try {
			inNum = midHouseDao.insertBvdfHouseInfoByBatch(bvdfHouseParamListForAdd);
		} catch (Exception e) {
			log.error("批量往中间库新增房屋信息失败" + e);
			throw new MsgException(ReturnCode.sdp_insert_fail, "批量往中间库新增房屋信息失败");
		}
		return inNum;
	}

	/**
	 * @Author: yinxunyang
	 * @Description: 批量更新房屋信息
	 * @Date: 2019/12/9 17:22
	 * @param:
	 * @return:
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public int updateBvdfHouseInfoByBatch(List<BvdfHouseParam> bvdfHouseParamListForEdit) {
		int upNum;
		try {
			upNum = midHouseDao.updateBvdfHouseInfoByBatch(bvdfHouseParamListForEdit);
		} catch (Exception e) {
			log.error("往中间库新增房屋信息失败" + e);
			throw new MsgException(ReturnCode.sdp_insert_fail, "往中间库新增房屋信息失败");
		}
		return upNum;
	}

	/**
	 * @Author: yinxunyang
	 * @Description: 根据主键查询中间库房屋信息
	 * @Date: 2019/12/9 17:02
	 * @param:
	 * @return:
	 */
	@Override
	public MidHouseInfo queryMidHouseInfoById(BvdfHouseParam bvdfHouseParam) {
		try {
			return midHouseDao.queryMidHouseInfoById(bvdfHouseParam);
		} catch (Exception e) {
			log.error("根据主键查询中间库房屋信息失败" + e);
			throw new MsgException(ReturnCode.sdp_select_fail, "根据主键查询中间库房屋信息失败");
		}
	}
}
