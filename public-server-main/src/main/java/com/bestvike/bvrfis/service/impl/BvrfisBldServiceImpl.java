package com.bestvike.bvrfis.service.impl;

import com.bestvike.bvrfis.dao.BvrfisHouseDao;
import com.bestvike.bvrfis.param.BvrfisBldParam;
import com.bestvike.bvrfis.service.BvrfisBldService;
import com.bestvike.commons.enums.ReturnCode;
import com.bestvike.commons.exception.MsgException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author: yinxunyang
 * @Description: 维修资金自然幢的service实现类
 * @Date: 2019/12/10 17:06
 */
@Service
@Slf4j
public class BvrfisBldServiceImpl implements BvrfisBldService {
	@Autowired
	private BvrfisHouseDao bvrfisHouseDao;

	/**
	 * @Author: yinxunyang
	 * @Description: 查询bvrfis自然幢的数据
	 * @Date: 2019/12/10 13:15
	 * @param:
	 * @return:
	 */
	@Override
	public List<BvrfisBldParam> queryBvrfisBldInfo(BvrfisBldParam queryParam)  {
		List<BvrfisBldParam> bvrfisBldParamList;
		try {
			bvrfisBldParamList = bvrfisHouseDao.queryBvrfisBldInfo(queryParam);
		} catch (Exception e) {
			log.error("查询bvrfis自然幢的数据失败" + e);
			throw new MsgException(ReturnCode.sdp_select_fail, "查询bvrfis自然幢的数据失败");
		}
		return bvrfisBldParamList;
	}
	/**
	 * @Author: yinxunyang
	 * @Description: 查询bvrfis自然幢的数据
	 * @Date: 2019/12/10 13:15
	 * @param:
	 * @return:
	 */
	@Override
	public BvrfisBldParam selectBvrfisBldInfo(BvrfisBldParam queryParam)  {
		BvrfisBldParam bvrfisBldParam;
		try {
			bvrfisBldParam = bvrfisHouseDao.selectBvrfisBldInfo(queryParam);
		} catch (Exception e) {
			log.error("查询bvrfis自然幢的数据失败" + e);
			throw new MsgException(ReturnCode.sdp_select_fail, "查询bvrfis自然幢的数据失败");
		}
		return bvrfisBldParam;
	}
}
