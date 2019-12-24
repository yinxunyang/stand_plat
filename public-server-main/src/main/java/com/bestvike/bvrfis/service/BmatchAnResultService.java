package com.bestvike.bvrfis.service;

import com.bestvike.bvrfis.entity.BmatchAnResultInfo;
import com.bestvike.bvrfis.param.BmatchAnResultParam;

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
	/**
	 * @Author: yinxunyang
	 * @Description: 新增匹配分析表
	 * @Date: 2019/12/9 13:56
	 * @param:
	 * @return:
	 */
	void insertBmatchAnResult(BmatchAnResultInfo bmatchAnResultInfo);

	/**
	 * @Author: yinxunyang
	 * @Description: 查询b_matchAnResult
	 * @Date: 2019/12/23 13:18
	 * @param:
	 * @return:
	 */
	BmatchAnResultInfo selectBmatchAnResultById(String matchId);

	/**
	 * @Author: yinxunyang
	 * @Description: 查询b_matchAnResult
	 * @Date: 2019/12/23 13:18
	 * @param:
	 * @return:
	 */
	BmatchAnResultInfo selectBmatchAnResult(BmatchAnResultParam bmatchAnResultParam);
}
