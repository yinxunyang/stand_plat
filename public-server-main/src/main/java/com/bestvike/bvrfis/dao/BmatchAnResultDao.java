package com.bestvike.bvrfis.dao;

import com.bestvike.bvrfis.entity.BmatchAnResultInfo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Author: yinxunyang
 * @Description: 匹配分析表Dao
 * @Date: 2019/12/5 11:22
 * @param:
 * @return:
 */
@Repository
public interface BmatchAnResultDao {
	/**
	 * @Author: yinxunyang
	 * @Description: 批量插入匹配分析表
	 * @Date: 2019/12/19 18:22
	 * @param:
	 * @return:
	 */
	int insertBmatchAnResultByBatch(List<BmatchAnResultInfo> bmatchAnResultInfoList);
	/**
	 * @Author: yinxunyang
	 * @Description: 插入匹配分析表
	 * @Date: 2019/12/19 18:22
	 * @param:
	 * @return:
	 */
	int insertBmatchAnResult(@Param("param") BmatchAnResultInfo bmatchAnResultInfo);
}
