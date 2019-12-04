package com.bestvike.pub.service.impl;

import com.bestvike.commons.exception.ServiceException;
import com.bestvike.commons.utils.DateUtils;
import com.bestvike.portal.service.BaseService;
import com.bestvike.pub.dao.PPublishDao;
import com.bestvike.pub.data.table.PPublish;
import com.bestvike.pub.service.PPublishService;
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
public class PPublishServiceImpl extends BaseService implements PPublishService {

	@Autowired
	private PPublishDao pPublishDao;

	@Override
	public PageInfo<PPublish> fetchPublishes(PPublish pPublish) {
		return MybatisUtils.page(pPublish, pPublishDao, new ExampleCriteria() {
			@Override
			public void initCriteria(Example.Criteria criteria) {
				if (!StringUtils.isEmpty(pPublish.getFuzzy())) {
					criteria.andLike("publishTitle", "%" + pPublish.getFuzzy() + "%");
					criteria.orLike("publishSummary", "%" + pPublish.getFuzzy() + "%");
				} else {
					if (!StringUtils.isEmpty(pPublish.getDataType())) {
						criteria.andEqualTo("dataType", pPublish.getDataType());
					}
					if (!StringUtils.isEmpty(pPublish.getTitle())) {
						criteria.andLike("publishTitle", "%" + pPublish.getTitle() + "%");
					}
					if (!StringUtils.isEmpty(pPublish.getSummary())) {
						criteria.andLike("publishSummary", "%" + pPublish.getSummary() + "%");
					}
				}
			}
		});
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public PPublish insert(PPublish pPublish, String userIdAndUserName) {
		pPublish.setPublishId(com.bestvike.commons.utils.StringUtils.guid());
		pPublish.setCreateTime(DateUtils.getDateDate());
		pPublish.setCreateUser(userIdAndUserName);
		pPublish.setLastModifyTime(DateUtils.getDateDate());
		int result = pPublishDao.insertSelective(pPublish);
		if (result <= 0) {
			throw new ServiceException("99", "内部异常");
		}
		return pPublish;
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public PPublish modify(PPublish pPublish) {
		pPublish.setLastModifyTime(DateUtils.getDateDate());
		int result = pPublishDao.updateByPrimaryKeySelective(pPublish);
		if (result <= 0) {
			throw new ServiceException("99", "内部异常");
		}
		return pPublishDao.selectByPrimaryKey(pPublish.getPublishId());
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public int remove(String ids) {
		Example example = new Example(PPublish.class);
		example.createCriteria().andIn("publishId", Arrays.asList(ids.split(",")));
		return pPublishDao.deleteByExample(example);
	}
}