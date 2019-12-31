package com.bestvike.dataCenter.service.impl;

import com.bestvike.commons.enums.ReturnCode;
import com.bestvike.commons.exception.MsgException;
import com.bestvike.dataCenter.dao.BvdfHouseDao;
import com.bestvike.dataCenter.param.BvdfBldParam;
import com.bestvike.dataCenter.service.BvdfBldService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class BvdfBldServiceImpl implements BvdfBldService {
	@Autowired
	private BvdfHouseDao bvdfHouseDao;

	/**
	 * @Author: yinxunyang
	 * @Description: 查询bvdf自然幢的数据
	 * @Date: 2019/12/23 16:39
	 * @param:
	 * @return:
	 */
	@Override
	public List<BvdfBldParam> queryBvdfBldInfo(BvdfBldParam queryParam) {
		List<BvdfBldParam> bvdfBldParamList;
		try {
			bvdfBldParamList = bvdfHouseDao.queryBvdfBldInfo(queryParam);
		} catch (Exception e) {
			log.error("查询bvdf自然幢的数据失败" + e);
			throw new MsgException(ReturnCode.sdp_select_fail, "查询bvdf自然幢的数据失败");
		}
		return bvdfBldParamList;
	}
	/**
	 * @Author: yinxunyang
	 * @Description: 查询bvdf自然幢的数据
	 * @Date: 2019/12/23 16:39
	 * @param:
	 * @return:
	 */
	@Override
	public BvdfBldParam selectBvdfBldInfo(BvdfBldParam queryParam) {
		BvdfBldParam bvdfBldParam;
		try {
			bvdfBldParam = bvdfHouseDao.selectBvdfBldInfo(queryParam);
		} catch (Exception e) {
			log.error("查询bvdf自然幢的数据失败" + e);
			throw new MsgException(ReturnCode.sdp_select_fail, "查询bvdf自然幢的数据失败");
		}
		return bvdfBldParam;
	}
}
