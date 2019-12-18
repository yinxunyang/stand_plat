package com.bestvike.mid.dao;

import com.bestvike.mid.entity.MidHouseInfo;
import com.bestvike.bvdf.param.BvdfHouseParam;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Author: yinxunyang
 * @Description: 中间库房屋信息Dao
 * @Date: 2019/12/5 11:22
 * @param:
 * @return:
 */
@Repository
public interface MidHouseDao {
	/**
	 * @Author: yinxunyang
	 * @Description: 批量插入房屋信息
	 * @Date: 2019/12/5 11:34
	 * @param:
	 * @return:
	 */
	int insertBvdfHouseInfoByBatch(List<BvdfHouseParam> bvdfHouseParamListForAdd);

	/**
	 * @Author: yinxunyang
	 * @Description: 根据主键往中间库更新房屋信息
	 * @Date: 2019/12/5 11:34
	 * @param:
	 * @return:
	 */
	int updateBvdfHouseInfoById(@Param("param") BvdfHouseParam bvdfHouseParam);

	/**
	 * @Author: yinxunyang
	 * @Description: 根据主键查询中间库房屋信息
	 * @Date: 2019/12/9 17:05
	 * @param:
	 * @return:
	 */
	MidHouseInfo queryMidHouseInfoById(@Param("param") BvdfHouseParam bvdfHouseParam);
}
