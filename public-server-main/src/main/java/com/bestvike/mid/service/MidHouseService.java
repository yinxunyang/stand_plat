package com.bestvike.mid.service;

import com.bestvike.dataCenter.param.BvdfHouseParam;
import com.bestvike.mid.entity.MidHouseInfo;

import java.util.List;

public interface MidHouseService {
	/**
	 * @Author: yinxunyang
	 * @Description: 批量往中间库新增房屋信息
	 * @Date: 2019/12/9 13:56
	 * @param:
	 * @return:
	 */
	int insertBvdfHouseInfoByBatch(List<BvdfHouseParam> bvdfHouseParamListForAdd);
	/**
	 * @Author: yinxunyang
	 * @Description: 批量更新房屋信息
	 * @Date: 2019/12/9 13:56
	 * @param:
	 * @return:
	 */
	int updateBvdfHouseInfoByBatch(List<BvdfHouseParam> bvdfHouseParamListForEdit);

	/**
	 * @Author: yinxunyang
	 * @Description: 根据主键查询中间库房屋信息
	 * @Date: 2019/12/9 13:56
	 * @param:
	 * @return:
	 */
	MidHouseInfo queryMidHouseInfoById(BvdfHouseParam bvdfHouseParam);
}
