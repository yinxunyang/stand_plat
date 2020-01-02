package com.bestvike.bvrfis.service;

import com.bestvike.bvrfis.param.BvrfisBldParam;

import java.util.List;

/**
 * @Author: yinxunyang
 * @Description: 维修资金自然幢信息的service
 * @Date: 2019/12/10 17:03
 */
public interface BvrfisBldService {

	/**
	 * @Author: yinxunyang
	 * @Description: 查询bvrfis自然幢的数据
	 * @Date: 2019/12/9 13:14
	 * @param:
	 * @return:
	 */
	List<BvrfisBldParam> queryBvrfisBldInfo(BvrfisBldParam queryParam);
	/**
	 * @Author: yinxunyang
	 * @Description: 查询bvrfis自然幢的数据
	 * @Date: 2019/12/9 13:14
	 * @param:
	 * @return:
	 */
	BvrfisBldParam selectBvrfisBldInfo(BvrfisBldParam queryParam);
}
