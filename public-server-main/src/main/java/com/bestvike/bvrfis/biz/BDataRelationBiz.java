package com.bestvike.bvrfis.biz;

import com.bestvike.commons.exception.MsgException;

/**
 * @Author: yinxunyang
 * @Description: 维修资金挂接关系表的biz
 * @Date: 2019/12/10 17:03
 */
public interface BDataRelationBiz {

	/**
	 * @Author: yinxunyang
	 * @Description: 建立挂接关系
	 * @Date: 2019/12/11 13:14
	 * @param:
	 * @return:
	 */
	void generateRelation(String matchType, String matchId) throws MsgException;
}
