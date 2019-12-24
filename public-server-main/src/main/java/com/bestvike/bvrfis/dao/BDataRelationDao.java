package com.bestvike.bvrfis.dao;

import com.bestvike.bvrfis.entity.BDataRelation;
import com.bestvike.bvrfis.param.BDataRelationParam;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * @Author: yinxunyang
 * @Description: 挂接关系表Dao
 * @Date: 2019/12/5 11:22
 * @param:
 * @return:
 */
@Repository
public interface BDataRelationDao {
	/**
	 * @Author: yinxunyang
	 * @Description: 插入挂接关系表
	 * @Date: 2019/12/19 18:22
	 * @param:
	 * @return:
	 */
	int insertBDataRelation(@Param("param") BDataRelation bDataRelation);

	/**
	 * @Author: yinxunyang
	 * @Description: 查询挂接关系表
	 * @Date: 2019/12/23 13:21
	 * @param:
	 * @return:
	 */
	BDataRelation selectBDataRelation(@Param("param") BDataRelationParam bDataRelationParam);
}
