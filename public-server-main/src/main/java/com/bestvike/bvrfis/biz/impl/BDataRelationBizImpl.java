package com.bestvike.bvrfis.biz.impl;

import com.bestvike.bvrfis.biz.BDataRelationBiz;
import com.bestvike.bvrfis.entity.BmatchAnResultInfo;
import com.bestvike.bvrfis.service.BmatchAnResultService;
import com.bestvike.commons.exception.MsgException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author: yinxunyang
 * @Description: 维修资金挂接关系表biz实现类
 * @Date: 2019/12/10 17:06
 */
@Service
@Slf4j
public class BDataRelationBizImpl implements BDataRelationBiz {
	@Autowired
	private BmatchAnResultService bmatchAnResultService;

	/**
	 * @Author: yinxunyang
	 * @Description: 建立挂接关系
	 * @Date: 2019/12/23 13:07
	 * @param:
	 * @return:
	 */
	@Override
	public void generateRelation(String matchType, String matchId) throws MsgException {
		// 查询b_matchAnResult
		BmatchAnResultInfo bmatchAnResultInfo = bmatchAnResultService.selectBmatchAnResultById(matchId);
		int i = 0;
	}
}
