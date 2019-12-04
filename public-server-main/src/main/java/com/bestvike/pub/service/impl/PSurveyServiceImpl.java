package com.bestvike.pub.service.impl;

import com.bestvike.commons.exception.ServiceException;
import com.bestvike.commons.utils.DateUtils;
import com.bestvike.portal.service.BaseService;
import com.bestvike.pub.dao.PSurveyDao;
import com.bestvike.pub.dao.PSurveyItemDao;
import com.bestvike.pub.data.table.PSurvey;
import com.bestvike.pub.data.table.PSurveyItem;
import com.bestvike.pub.service.PSurveyService;
import com.bestvike.pub.support.ExampleCriteria;
import com.bestvike.pub.support.MybatisUtils;
import com.github.pagehelper.PageInfo;
import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Example;

@Service
@Transactional(readOnly = true, propagation = Propagation.NOT_SUPPORTED)
public class PSurveyServiceImpl extends BaseService implements PSurveyService {

	@Autowired
	private PSurveyDao pSurveyDao;
	@Autowired
	private PSurveyItemDao pSurveyItemDao;

	@Override
	public PageInfo<PSurvey> fetchSurveys(PSurvey pSurvey) {
		return MybatisUtils.page(pSurvey, pSurveyDao, new ExampleCriteria() {
			@Override
			public void initCriteria(Example.Criteria criteria) {
				if (!StringUtils.isEmpty(pSurvey.getFuzzy())) {
					criteria.andLike("title", "%" + pSurvey.getFuzzy() + "%");
					criteria.orLike("summary", "%" + pSurvey.getFuzzy() + "%");
				} else {
					if (!StringUtils.isEmpty(pSurvey.getTitle())) {
						criteria.andLike("title", "%" + pSurvey.getTitle() + "%");
					}
					if (!StringUtils.isEmpty(pSurvey.getSummary())) {
						criteria.andLike("summary", "%" + pSurvey.getSummary() + "%");
					}
				}
			}
		});
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public PSurvey insert(PSurvey pSurvey, String userIdAndUserName) {
		pSurvey.setSurveyId(com.bestvike.commons.utils.StringUtils.guid());
		pSurvey.setSurveyStatus("9999"); /// 暂存
		pSurvey.setCreateTime(DateUtils.getDateDate());
		pSurvey.setCreateUser(userIdAndUserName);
		pSurvey.setLastModifyTime(DateUtils.getDateDate());
		int result = pSurveyDao.insertSelective(pSurvey);
		if (result <= 0) {
			throw new ServiceException("99", "内部异常");
		}
		return pSurvey;
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public PSurvey modify(PSurvey pSurvey) {
		pSurvey.setLastModifyTime(DateUtils.getDateDate());
		int result = pSurveyDao.updateByPrimaryKeySelective(pSurvey);
		if (result <= 0) {
			throw new ServiceException("99", "内部异常");
		}
		return pSurveyDao.selectByPrimaryKey(pSurvey.getSurveyId());
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public int remove(String ids) {
		Example example = new Example(PSurveyItem.class);
		example.createCriteria().andIn("surveyId", Arrays.asList(ids.split(",")));
		pSurveyItemDao.deleteByExample(example);
		example = new Example(PSurvey.class);
		example.createCriteria().andIn("surveyId", Arrays.asList(ids.split(",")));
		return pSurveyDao.deleteByExample(example);
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public PSurvey publishSurvey(PSurvey pSurvey) {
		pSurvey.setSurveyStatus("0001"); // 执行中
		pSurvey.setLastModifyTime(DateUtils.getDateDate());
		int result = pSurveyDao.updateByPrimaryKeySelective(pSurvey);
		if (result <= 0) {
			throw new ServiceException("99", "内部异常");
		}
		Example example = new Example(PSurveyItem.class);
		example.createCriteria().andEqualTo("surveyId", pSurvey.getSurveyId());
		List<PSurveyItem> surveyItems = pSurveyItemDao.selectByExample(example);
		pSurvey.setSurveyItems(surveyItems);
		return pSurvey;
	}
}