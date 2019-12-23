package com.bestvike.bvrfis.dao;

import com.bestvike.bvrfis.entity.BDataRelation;
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
}
