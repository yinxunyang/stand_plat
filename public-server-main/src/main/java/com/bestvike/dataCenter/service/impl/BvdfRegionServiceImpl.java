package com.bestvike.dataCenter.service.impl;

import com.bestvike.commons.enums.ReturnCode;
import com.bestvike.commons.exception.MsgException;
import com.bestvike.dataCenter.dao.BvdfHouseDao;
import com.bestvike.dataCenter.param.BvdfRegionParam;
import com.bestvike.dataCenter.service.BvdfRegionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class BvdfRegionServiceImpl implements BvdfRegionService {
	@Autowired
	private BvdfHouseDao bvdfHouseDao;
	/**
	 * @Author: yinxunyang
	 * @Description:
	 * @Date: 2019/12/23 16:39
	 * @param:
	 * @return:
	 */
	@Override
	public List<BvdfRegionParam> queryBvdfRegionInfo(BvdfRegionParam queryParam) {
		List< BvdfRegionParam > bvdfRegionParamList;
		try {
			bvdfRegionParamList = bvdfHouseDao.queryBvdfRegionInfo(queryParam);
		} catch (Exception e) {
			log.error("查询bvdf小区的数据失败", e);
			throw new MsgException(ReturnCode.sdp_select_fail, "查询bvdf小区的数据失败");
		}
		return bvdfRegionParamList;
	}

	/**
	 * @Author: yinxunyang
	 * @Description: 查询bvdf小区的数据
	 * @Date: 2019/12/23 16:39
	 * @param:
	 * @return:
	 */
	@Override
	public BvdfRegionParam selectBvdfRegionInfo(BvdfRegionParam queryParam) {
		BvdfRegionParam bvdfRegionParam;
		try {
			bvdfRegionParam = bvdfHouseDao.selectBvdfRegionInfo(queryParam);
		} catch (Exception e) {
			log.error("查询bvdf小区的数据失败" + e);
			throw new MsgException(ReturnCode.sdp_select_fail, "查询bvdf小区的数据失败");
		}
		return bvdfRegionParam;
	}
}
