package com.bestvike.bvrfis.service;

import com.bestvike.bvrfis.entity.BDataRelation;
import com.bestvike.bvrfis.param.BDataRelationParam;

/**
 * @Author: yinxunyang
 * @Description: 挂接关系表的service
 * @Date: 2019/12/10 17:03
 */
public interface BDataRelationService {
	/**
	 * @Author: yinxunyang
	 * @Description: 新增挂接关系表
	 * @Date: 2019/12/9 13:56
	 * @param:
	 * @return:
	 */
	void insertBDataRelation(BDataRelation bDataRelation);

	/**
	 * @Author: yinxunyang
	 * @Description: 查询挂接关系表
	 * @Date: 2019/12/9 13:56
	 * @param:
	 * @return:
	 */
	BDataRelation selectBDataRelation(BDataRelationParam bDataRelationParam);
}
