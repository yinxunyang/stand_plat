package com.bestvike.bvrfis.service;

import com.bestvike.bvrfis.entity.BLogOper;

/**
 * @Author: yinxunyang
 * @Description: 操作日志表的service
 * @Date: 2019/12/10 17:03
 */
public interface BLogOperService {

	/**
	 * @Author: yinxunyang
	 * @Description: 新增操作日志表
	 * @Date: 2019/12/9 13:14
	 * @param:
	 * @return:
	 */
	void insertBLogOper(BLogOper bLogOper);
}
