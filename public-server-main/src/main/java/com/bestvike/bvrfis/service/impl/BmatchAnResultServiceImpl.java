package com.bestvike.bvrfis.service.impl;

import com.bestvike.bvrfis.dao.BmatchAnResultDao;
import com.bestvike.bvrfis.entity.BmatchAnResultInfo;
import com.bestvike.bvrfis.service.BmatchAnResultService;
import com.bestvike.commons.enums.ReturnCode;
import com.bestvike.commons.exception.MsgException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Author: yinxunyang
 * @Description: 匹配分析表service实现类
 * @Date: 2019/12/10 17:06
 */
@Service
@Slf4j
public class BmatchAnResultServiceImpl implements BmatchAnResultService {
	@Autowired
	private BmatchAnResultDao bmatchAnResultDao;

	/**
	 * @Author: yinxunyang
	 * @Description: 批量新增匹配分析表
	 * @Date: 2019/12/6 14:40
	 * @param:
	 * @return:
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void insertBmatchAnResultByBatch(List<BmatchAnResultInfo> bmatchAnResultInfoList) throws MsgException {
		if (!bmatchAnResultInfoList.isEmpty()) {
			// 批量匹配分析表
			int inNum = bmatchAnResultDao.insertBmatchAnResultByBatch(bmatchAnResultInfoList);
			if (bmatchAnResultInfoList.size() != inNum) {
				throw new MsgException(ReturnCode.sdp_insert_fail, "批量新增匹配分析表失败");
			}
		}
	}
	/**
	 * @Author: yinxunyang
	 * @Description: 新增匹配分析表
	 * @Date: 2019/12/6 14:40
	 * @param:
	 * @return:
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void insertBmatchAnResult(BmatchAnResultInfo bmatchAnResultInfo) throws MsgException {
		if (null != bmatchAnResultInfo) {
			// 批量匹配分析表
			int inNum = bmatchAnResultDao.insertBmatchAnResult(bmatchAnResultInfo);
			if (1 != inNum) {
				throw new MsgException(ReturnCode.sdp_insert_fail, "新增匹配分析表失败");
			}
		}
	}
}
