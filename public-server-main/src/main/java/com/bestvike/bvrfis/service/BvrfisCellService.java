package com.bestvike.bvrfis.service;

import com.bestvike.bvrfis.param.BvrfisCellParam;

/**
 * @Author: yinxunyang
 * @Description: 维修资金单元信息的service
 * @Date: 2019/12/10 17:03
 */
public interface BvrfisCellService {

	/**
	 * @Author: yinxunyang
	 * @Description: 查询bvrfis单元的数据
	 * @Date: 2019/12/9 13:14
	 * @param:
	 * @return:
	 */
	BvrfisCellParam selectBvrfisCellInfo(BvrfisCellParam queryParam);
}
