package com.bestvike.pub.service.impl;

import com.bestvike.commons.exception.ServiceException;
import com.bestvike.commons.utils.DateUtils;
import com.bestvike.portal.service.BaseService;
import com.bestvike.pub.dao.PQuestionDao;
import com.bestvike.pub.data.table.PQuestion;
import com.bestvike.pub.data.table.PSurvey;
import com.bestvike.pub.service.PQuestionService;
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
public class PQuestionServiceImpl extends BaseService implements PQuestionService {

	@Autowired
	private PQuestionDao pQuestionDao;

	@Override
	public PageInfo<PQuestion> fetchQuestions(PQuestion pQuestion) {
		return MybatisUtils.page(pQuestion, pQuestionDao, new ExampleCriteria() {
			@Override
			public void initCriteria(Example.Criteria criteria) {
				if (!StringUtils.isEmpty(pQuestion.getFuzzy())) {
					criteria.andLike("summary", "%" + pQuestion.getFuzzy() + "%");
				} else {
					if (!StringUtils.isEmpty(pQuestion.getQuestionType())) {
						criteria.andEqualTo("questionType", pQuestion.getQuestionType());
					}
					if (!StringUtils.isEmpty(pQuestion.getSummary())) {
						criteria.andLike("summary", "%" + pQuestion.getSummary() + "%");
					}
				}
			}
		});
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public PQuestion insert(PQuestion pQuestion, String userIdAndUserName) {
		pQuestion.setQuestionId(com.bestvike.commons.utils.StringUtils.getId());
		pQuestion.setCreateTime(DateUtils.getDateDate());
		pQuestion.setCreateUser(userIdAndUserName);
		pQuestion.setLastModifyTime(DateUtils.getDateDate());
		int result = pQuestionDao.insertSelective(pQuestion);
		if (result <= 0) {
			throw new ServiceException("99", "内部异常");
		}
		return pQuestion;
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public PQuestion modify(PQuestion pQuestion) {
		pQuestion.setLastModifyTime(DateUtils.getDateDate());
		int result = pQuestionDao.updateByPrimaryKeySelective(pQuestion);
		if (result <= 0) {
			throw new ServiceException("99", "内部异常");
		}
		return pQuestionDao.selectByPrimaryKey(pQuestion.getQuestionId());
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public int remove(String ids) {
		Example example = new Example(PSurvey.class);
		example.createCriteria().andIn("questionId", Arrays.asList(ids.split(",")));
		return pQuestionDao.deleteByExample(example);
	}
}