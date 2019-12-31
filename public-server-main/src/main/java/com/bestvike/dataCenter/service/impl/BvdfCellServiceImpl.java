package com.bestvike.dataCenter.service.impl;

import com.bestvike.commons.enums.ReturnCode;
import com.bestvike.commons.exception.MsgException;
import com.bestvike.dataCenter.dao.BvdfHouseDao;
import com.bestvike.dataCenter.param.BvdfCellParam;
import com.bestvike.dataCenter.service.BvdfCellService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class BvdfCellServiceImpl implements BvdfCellService {
	@Autowired
	private BvdfHouseDao bvdfHouseDao;

	/**
	 * @Author: yinxunyang
	 * @Description: 查询bvdf企业的数据
	 * @Date: 2019/12/9 13:15
	 * @param:
	 * @return:
	 */
	@Override
	public BvdfCellParam selectBvdfCellInfo(BvdfCellParam queryParam) throws MsgException {
		BvdfCellParam bvdfCellParam;
		try {
			bvdfCellParam = bvdfHouseDao.selectBvdfCellInfo(queryParam);
		} catch (Exception e) {
			log.error("查询bvdf单元的数据失败" + e);
			throw new MsgException(ReturnCode.sdp_select_fail, "查询bvdf单元的数据失败");
		}
		return bvdfCellParam;
	}
}
