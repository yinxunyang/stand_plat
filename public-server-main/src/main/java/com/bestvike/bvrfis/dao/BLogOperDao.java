package com.bestvike.bvrfis.dao;

import com.bestvike.bvrfis.entity.BLogOper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * @Author: yinxunyang
 * @Description: 操作日志表Dao
 * @Date: 2019/12/5 11:22
 * @param:
 * @return:
 */
@Repository
public interface BLogOperDao {
	/**
	 * @Author: yinxunyang
	 * @Description: 新增操作日志表
	 * @Date: 2019/12/19 18:22
	 * @param:
	 * @return:
	 */
	int insertBLogOper(@Param("param") BLogOper bLogOper);
}
