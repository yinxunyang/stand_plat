package com.bestvike.bvrfis.service.impl;

import com.bestvike.bvrfis.dao.BvrfisHouseDao;
import com.bestvike.bvrfis.param.BvrfisFloorParam;
import com.bestvike.bvrfis.service.BvrfisFloorService;
import com.bestvike.commons.enums.ReturnCode;
import com.bestvike.commons.exception.MsgException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author: yinxunyang
 * @Description: 维修资金楼层的service实现类
 * @Date: 2019/12/10 17:06
 */
@Service
@Slf4j
public class BvrfisFloorServiceImpl implements BvrfisFloorService {
	@Autowired
	private BvrfisHouseDao bvrfisHouseDao;


	/**
	 * @Author: yinxunyang
	 * @Description: 查询bvrfis楼层的数据
	 * @Date: 2019/12/10 13:15
	 * @param:
	 * @return:
	 */
	@Override
	public BvrfisFloorParam selectBvrfisFloorInfo(BvrfisFloorParam queryParam)  {
		BvrfisFloorParam bvrfisFloorParam;
		try {
			bvrfisFloorParam = bvrfisHouseDao.selectBvrfisFloorInfo(queryParam);
		} catch (Exception e) {
			log.error("查询bvrfis楼层的数据失败" + e);
			throw new MsgException(ReturnCode.sdp_select_fail, "查询bvrfis楼层的数据失败");
		}
		return bvrfisFloorParam;
	}
}
