package com.bestvike.bvrfis.service.impl;

import com.bestvike.bvrfis.dao.BvrfisHouseDao;
import com.bestvike.bvrfis.param.BvrfisCellParam;
import com.bestvike.bvrfis.service.BvrfisCellService;
import com.bestvike.commons.enums.ReturnCode;
import com.bestvike.commons.exception.MsgException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author: yinxunyang
 * @Description: 维修资金单元的service实现类
 * @Date: 2019/12/10 17:06
 */
@Service
@Slf4j
public class BvrfisCellServiceImpl implements BvrfisCellService {
	@Autowired
	private BvrfisHouseDao bvrfisHouseDao;


	/**
	 * @Author: yinxunyang
	 * @Description: 查询bvrfis单元的数据
	 * @Date: 2019/12/10 13:15
	 * @param:
	 * @return:
	 */
	@Override
	public BvrfisCellParam selectBvrfisCellInfo(BvrfisCellParam queryParam) throws MsgException {
		BvrfisCellParam bvrfisCellParam;
		try {
			bvrfisCellParam = bvrfisHouseDao.selectBvrfisCellInfo(queryParam);
		} catch (Exception e) {
			log.error("查询bvrfis单元的数据失败" + e);
			throw new MsgException(ReturnCode.sdp_select_fail, "查询bvrfis单元的数据失败");
		}
		return bvrfisCellParam;
	}
}
