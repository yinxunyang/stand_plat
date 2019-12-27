package com.bestvike.dataCenter.service.impl;

import com.bestvike.commons.enums.ReturnCode;
import com.bestvike.commons.exception.MsgException;
import com.bestvike.dataCenter.dao.BvdfHouseDao;
import com.bestvike.dataCenter.param.BvdfCorpParam;
import com.bestvike.dataCenter.service.BvdfCorpService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class BvdfCorpServiceImpl implements BvdfCorpService {
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
	public List<BvdfCorpParam> queryBvdfCorpInfo(BvdfCorpParam queryParam) throws MsgException {
		List<BvdfCorpParam> bvdfCorpParamList;
		try {
			bvdfCorpParamList = bvdfHouseDao.queryBvdfCorpInfo(queryParam);
		} catch (Exception e) {
			log.error("查询bvdf企业的数据失败" + e);
			throw new MsgException(ReturnCode.sdp_select_fail, "查询bvdf企业的数据失败");
		}
		return bvdfCorpParamList;
	}
	/**
	 * @Author: yinxunyang
	 * @Description: 查询bvdf企业的数据
	 * @Date: 2019/12/9 13:15
	 * @param:
	 * @return:
	 */
	@Override
	public BvdfCorpParam selectBvdfCorpInfo(BvdfCorpParam queryParam) throws MsgException {
		BvdfCorpParam bvdfCorpParam;
		try {
			bvdfCorpParam = bvdfHouseDao.selectBvdfCorpInfo(queryParam);
		} catch (Exception e) {
			log.error("查询bvdf企业的数据失败" + e);
			throw new MsgException(ReturnCode.sdp_select_fail, "查询bvdf企业的数据失败");
		}
		return bvdfCorpParam;
	}
}
