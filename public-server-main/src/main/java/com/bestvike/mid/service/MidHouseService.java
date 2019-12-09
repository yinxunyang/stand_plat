package com.bestvike.mid.service;

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
}
