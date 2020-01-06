package com.bestvike.bvrfis.service.impl;

import com.bestvike.bvrfis.dao.BvrfisHouseDao;
import com.bestvike.bvrfis.param.BvrfisRegionParam;
import com.bestvike.bvrfis.service.BvrfisRegionService;
import com.bestvike.commons.enums.ReturnCode;
import com.bestvike.commons.exception.MsgException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author: yinxunyang
 * @Description: 维修资金小区的service实现类
 * @Date: 2019/12/10 17:06
 */
@Service
@Slf4j
public class BvrfisRegionServiceImpl implements BvrfisRegionService {
	@Autowired
	private BvrfisHouseDao bvrfisHouseDao;

	/**
	 * @Author: yinxunyang
	 * @Description: 查询bvrfis小区的数据
	 * @Date: 2019/12/10 13:15
	 * @param:
	 * @return:
	 */
	@Override
	public List<BvrfisRegionParam> queryBvrfisRegionInfo(BvrfisRegionParam queryParam)  {
		List<BvrfisRegionParam> bvrfisRegionParamList;
		try {
			bvrfisRegionParamList = bvrfisHouseDao.queryBvrfisRegionInfo(queryParam);
		} catch (Exception e) {
			log.error("查询bvrfis小区的数据失败" + e);
			throw new MsgException(ReturnCode.sdp_select_fail, "查询bvrfis小区的数据失败");
		}
		return bvrfisRegionParamList;
	}
	/**
	 * @Author: yinxunyang
	 * @Description: 查询bvrfis小区的数据
	 * @Date: 2019/12/10 13:15
	 * @param:
	 * @return:
	 */
	@Override
	public BvrfisRegionParam selectBvrfisRegionInfo(BvrfisRegionParam queryParam)  {
		BvrfisRegionParam bvrfisRegionParam;
		try {
			bvrfisRegionParam = bvrfisHouseDao.selectBvrfisRegionInfo(queryParam);
		} catch (Exception e) {
			log.error("查询bvrfis小区的数据失败" + e);
			throw new MsgException(ReturnCode.sdp_select_fail, "查询bvrfis小区的数据失败");
		}
		return bvrfisRegionParam;
	}
}
