package com.bestvike.pub.dao;

import com.bestvike.pub.param.BvdfHouseParam;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @Author: yinxunyang
 * @Description: bvdf房屋信息Dao
 * @Date: 2019/12/5 11:22
 * @param:
 * @return:
 */
@Repository
public interface BvdfHouseDao {
	/**
	 * @Author: yinxunyang
	 * @Description: 查询房屋信息
	 * @Date: 2019/12/5 11:34
	 * @param:
	 * @return:
	 */
	List<BvdfHouseParam> queryBvdfHouseInfo(Map<String, Object> parameterMap);
}
