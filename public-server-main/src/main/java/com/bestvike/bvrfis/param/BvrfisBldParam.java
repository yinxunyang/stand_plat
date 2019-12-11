package com.bestvike.bvrfis.param;

import com.alibaba.fastjson.JSONObject;
import lombok.Getter;
import lombok.Setter;

/**
 * @Author: yinxunyang
 * @Description: bvrfis的楼幢信息参数类
 * @Date: 2019/12/10 11:08
 */
@Getter
@Setter
public class BvrfisBldParam {
	/**
	 * 楼幢名称
	 */
	private String bldName;
	/**
	 * 楼幢名称
	 */
	private String developNo;

	@Override
	public String toString() {
		return JSONObject.toJSONString(this);
	}

}
