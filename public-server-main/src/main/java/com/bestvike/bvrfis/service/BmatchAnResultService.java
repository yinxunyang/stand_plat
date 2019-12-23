package com.bestvike.bvrfis.service;

import com.bestvike.bvrfis.entity.BmatchAnResultInfo;

import java.util.List;

/**
 * @Author: yinxunyang
 * @Description: 匹配分析表的service
 * @Date: 2019/12/10 17:03
 */
public interface BmatchAnResultService {

	/**
	 * @Author: yinxunyang
	 * @Description: 批量新增匹配分析表
	 * @Date: 2019/12/9 13:56
	 * @param:
	 * @return:
	 */
	void insertBmatchAnResultByBatch(List<BmatchAnResultInfo> bmatchAnResultInfoList);
}
