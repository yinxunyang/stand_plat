package com.bestvike.mid.service;

import com.bestvike.mid.entity.MidHouseInfo;
import com.bestvike.pub.param.BvdfHouseParam;

public interface MidHouseService {
	/**
	 * @Author: yinxunyang
	 * @Description: 往中间库新增房屋信息
	 * @Date: 2019/12/9 13:56
	 * @param:
	 * @return:
	 */
	int insertBvdfHouseInfo(BvdfHouseParam bvdfHouseParam);
	/**
	 * @Author: yinxunyang
	 * @Description: 根据主键往中间库更新房屋信息
	 * @Date: 2019/12/9 13:56
	 * @param:
	 * @return:
	 */
	int updateBvdfHouseInfoById(BvdfHouseParam bvdfHouseParam);

	/**
	 * @Author: yinxunyang
	 * @Description: 根据主键查询中间库房屋信息
	 * @Date: 2019/12/9 13:56
	 * @param:
	 * @return:
	 */
	MidHouseInfo queryMidHouseInfoById(BvdfHouseParam bvdfHouseParam);
}
