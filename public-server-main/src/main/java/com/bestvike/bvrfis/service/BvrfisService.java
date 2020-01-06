package com.bestvike.bvrfis.service;

import com.bestvike.bvrfis.entity.BmatchAnResultInfo;
import com.bestvike.commons.exception.MsgException;

/**
 * @Author: yinxunyang
 * @Description: 维修资金的service
 * @Date: 2019/12/10 17:03
 */
public interface BvrfisService {
	/**
	 * @Author: yinxunyang
	 * @Description: 先删除再新增匹配结果表，同事务
	 * @Date: 2019/12/25 9:20
	 * @param:
	 * @return:
	 */
	void delAndInsertBmatchAnResult(BmatchAnResultInfo bmatchAnResultInfo);
}
