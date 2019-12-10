package com.bestvike.bvrfis.service.impl;

import com.bestvike.bvrfis.dao.BvrfisHouseDao;
import com.bestvike.bvrfis.param.BvrfisHouseParam;
import com.bestvike.bvrfis.service.BvrfisHouseService;
import com.bestvike.pub.enums.ReturnCode;
import com.bestvike.pub.exception.MsgException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @Author: yinxunyang
 * @Description: 维修资金房屋信息的service实现类
 * @Date: 2019/12/10 17:06
 */
@Service
@Slf4j
public class BvrfisHouseServiceImpl implements BvrfisHouseService {
	@Autowired
	private BvrfisHouseDao bvrfisHouseDao;

	/**
	 * @Author: yinxunyang
	 * @Description: 查询bvrfis房屋的数据
	 * @Date: 2019/12/10 13:15
	 * @param:
	 * @return:
	 */
	@Override
	public List<BvrfisHouseParam> queryBvrfisHouseInfo(Map<String, Object> parameterMap) throws MsgException {
		List<BvrfisHouseParam> bvrfisHouseParamList;
		try {
			bvrfisHouseParamList = bvrfisHouseDao.queryBvrfisHouseInfo(parameterMap);
		} catch (Exception e) {
			log.error("查询bvrfis房屋的数据失败");
			throw new MsgException(ReturnCode.sdp_select_fail, "查询bvrfis房屋的数据失败");
		}
		return bvrfisHouseParamList;
	}
}
