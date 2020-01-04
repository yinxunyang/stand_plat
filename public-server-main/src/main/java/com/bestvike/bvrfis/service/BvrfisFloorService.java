package com.bestvike.bvrfis.service;

import com.bestvike.bvrfis.param.BvrfisFloorParam;

/**
 * @Author: yinxunyang
 * @Description: 维修资金楼层信息的service
 * @Date: 2019/12/10 17:03
 */
public interface BvrfisFloorService {

	/**
	 * @Author: yinxunyang
	 * @Description: 查询bvrfis楼层的数据
	 * @Date: 2019/12/9 13:14
	 * @param:
	 * @return:
	 */
	BvrfisFloorParam selectBvrfisFloorInfo(BvrfisFloorParam queryParam);
}
