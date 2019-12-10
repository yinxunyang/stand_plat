package com.bestvike.bvrfis.dao;

import com.bestvike.bvrfis.param.BvrfisHouseParam;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @Author: yinxunyang
 * @Description: 维修资金房屋信息Dao
 * @Date: 2019/12/5 11:22
 * @param:
 * @return:
 */
@Repository
public interface BvrfisHouseDao {
	/**
	 * @Author: yinxunyang
	 * @Description: 查询bvrfis房屋的数据
	 * @Date: 2019/12/10 17:25
	 * @param:
	 * @return:
	 */
	List<BvrfisHouseParam> queryBvrfisHouseInfo(Map<String, Object> parameterMap);
}
