package com.bestvike.pub.service.impl;

import com.bestvike.commons.exception.ServiceException;
import com.bestvike.commons.utils.DateUtils;
import com.bestvike.portal.service.BaseService;
import com.bestvike.pub.dao.PSurveyItemDao;
import com.bestvike.pub.data.table.PSurvey;
import com.bestvike.pub.data.table.PSurveyItem;
import com.bestvike.pub.service.PSurveyItemService;
import com.bestvike.pub.support.ExampleCriteria;
import com.bestvike.pub.support.MybatisUtils;
import com.github.pagehelper.PageInfo;
import java.util.Arrays;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Example;

@Service
@Transactional(readOnly = true, propagation = Propagation.NOT_SUPPORTED)
public class PSurveyItemServiceImpl extends BaseService implements PSurveyItemService {

	@Autowired
	private PSurveyItemDao pSurveyItemDao;

	@Override
	public PageInfo<PSurveyItem> fetchSurveyItems(PSurveyItem pSurveyItem) {
		return MybatisUtils.page(pSurveyItem, pSurveyItemDao, new ExampleCriteria() {
			@Override
			public void initCriteria(Example.Criteria criteria) {
				if (!StringUtils.isEmpty(pSurveyItem.getSurveyId())) {
					criteria.andEqualTo("surveyId", pSurveyItem.getSurveyId());
				}
				if (!StringUtils.isEmpty(pSurveyItem.getFuzzy())) {
					criteria.andLike("summary", "%" + pSurveyItem.getFuzzy() + "%");
				} else {
					if (!StringUtils.isEmpty(pSurveyItem.getQuestionType())) {
						criteria.andEqualTo("questionType", pSurveyItem.getQuestionType());
					}
					if (!StringUtils.isEmpty(pSurveyItem.getSummary())) {
						criteria.andLike("summary", "%" + pSurveyItem.getSummary() + "%");
					}
				}
			}
		});
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public PSurveyItem insert(PSurveyItem pSurveyItem, String userIdAndUserName) {
		if (StringUtils.isEmpty(pSurveyItem.getSysId())) {
			pSurveyItem.setSysId(com.bestvike.commons.utils.StringUtils.guid());
		}
		pSurveyItem.setCreateTime(DateUtils.getDateDate());
		pSurveyItem.setCreateUser(userIdAndUserName);
		pSurveyItem.setLastModifyTime(DateUtils.getDateDate());
		int result = pSurveyItemDao.insertSelective(pSurveyItem);
		if (result <= 0) {
			throw new ServiceException("99", "内部异常");
		}
		return pSurveyItem;
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public PSurveyItem modify(PSurveyItem pSurveyItem) {
		pSurveyItem.setLastModifyTime(DateUtils.getDateDate());
		int result = pSurveyItemDao.updateByPrimaryKeySelective(pSurveyItem);
		if (result <= 0) {
			throw new ServiceException("99", "内部异常");
		}
		return pSurveyItemDao.selectByPrimaryKey(pSurveyItem.getSysId());
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public int remove(String ids) {
		Example example = new Example(PSurveyItem.class);
		example.createCriteria().andIn("sysId", Arrays.asList(ids.split(",")));
		return pSurveyItemDao.deleteByExample(example);
	}
}