package com.bestvike.bvrfis.service.impl;

import com.bestvike.bvrfis.dao.BDataRelationDao;
import com.bestvike.bvrfis.entity.BDataRelation;
import com.bestvike.bvrfis.entity.BmatchAnResultInfo;
import com.bestvike.bvrfis.param.BDataRelationParam;
import com.bestvike.bvrfis.service.BDataRelationService;
import com.bestvike.commons.enums.ReturnCode;
import com.bestvike.commons.exception.MsgException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Author: yinxunyang
 * @Description: 挂接关系表service实现类
 * @Date: 2019/12/10 17:06
 */
@Service
@Slf4j
public class BDataRelationServiceImpl implements BDataRelationService {
	@Autowired
	private BDataRelationDao bDataRelationDao;

	/**
	 * @Author: yinxunyang
	 * @Description: 新增挂接关系表
	 * @Date: 2019/12/6 14:40
	 * @param:
	 * @return:
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void insertBDataRelation(BDataRelation bDataRelation) throws MsgException {
		if (null != bDataRelation) {
			// 批量匹配分析表
			int inNum = bDataRelationDao.insertBDataRelation(bDataRelation);
			if (1 != inNum) {
				throw new MsgException(ReturnCode.sdp_insert_fail, "新增挂接关系表失败");
			}
		}
	}

	/**
	 * @Author: yinxunyang
	 * @Description: 查询挂接关系
	 * @Date: 2019/12/23 13:20
	 * @param:
	 * @return:
	 */
	@Override
	public BDataRelation selectBDataRelation(BDataRelationParam bDataRelationParam) {
		return bDataRelationDao.selectBDataRelation(bDataRelationParam);
	}
}
