package com.bestvike.bvrfis.service;

import com.bestvike.bvrfis.param.BvrfisHouseParam;

import java.util.List;

/**
 * @Author: yinxunyang
 * @Description: 维修资金房屋信息的service
 * @Date: 2019/12/10 17:03
 */
public interface BvrfisHouseService {

	/**
	 * @Author: yinxunyang
	 * @Description: 查询bvrfis房屋的数据
	 * @Date: 2019/12/9 13:14
	 * @param:
	 * @return:
	 */
	List<BvrfisHouseParam> queryBvrfisHouseInfo(BvrfisHouseParam queryParam);
}
