package com.bestvike.bvrfis.service.impl;

import com.bestvike.bvrfis.dao.BLogOperDao;
import com.bestvike.bvrfis.entity.BLogOper;
import com.bestvike.bvrfis.service.BLogOperService;
import com.bestvike.commons.enums.ReturnCode;
import com.bestvike.commons.exception.MsgException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Author: yinxunyang
 * @Description: 操作日志表的service实现类
 * @Date: 2019/12/10 17:06
 */
@Service
@Slf4j
public class BLogOperServiceImpl implements BLogOperService {
	@Autowired
	private BLogOperDao bLogOperDao;

	/**
	 * @Author: yinxunyang
	 * @Description: 新增操作日志表
	 * @Date: 2019/12/6 14:40
	 * @param:
	 * @return:
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void insertBLogOper(BLogOper bLogOper) throws MsgException {
		if (null != bLogOper) {
			// 操作日志表
			int inNum = bLogOperDao.insertBLogOper(bLogOper);
			if (1 != inNum) {
				throw new MsgException(ReturnCode.sdp_insert_fail, "新增操作日志表失败");
			}
		}
	}
}
