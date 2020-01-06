package com.bestvike.bvrfis.service.impl;

import com.bestvike.bvrfis.entity.BmatchAnResultInfo;
import com.bestvike.bvrfis.service.BmatchAnResultService;
import com.bestvike.bvrfis.service.BvrfisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Author: yinxunyang
 * @Description: 维修资金的service实现类
 * @Date: 2019/12/11 17:06
 */
@Service
@Slf4j
public class BvrfisServiceImpl implements BvrfisService {
	@Autowired
	private BmatchAnResultService bmatchAnResultService;


	/**
	 * @Author: yinxunyang
	 * @Description: 先删除再新增匹配结果表，同事务
	 * @Date: 2019/12/25 9:21
	 * @param:
	 * @return:
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void delAndInsertBmatchAnResult(BmatchAnResultInfo bmatchAnResultInfo) {
		bmatchAnResultService.delBmatchAnResultByWxId(bmatchAnResultInfo.getWxbusiid());
		bmatchAnResultService.insertBmatchAnResult(bmatchAnResultInfo);
	}
}