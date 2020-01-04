package com.bestvike.bvrfis.param;

import com.alibaba.fastjson.JSONObject;
import lombok.Getter;
import lombok.Setter;

/**
 * @Author: yinxunyang
 * @Description: bvrfis的单元信息参数类
 * @Date: 2019/12/10 11:08
 */
@Getter
@Setter
public class BvrfisCellParam {
	/**
	 * 楼幢编号
	 */
	private String bldNo;
	/**
	 * 单元编号
	 */
	private String cellNo;
	/**
	 * 单元名称
	 */
	private String cellName;
	/**
	 * 房屋性质
	 */
	private String houseProp;


	@Override
	public String toString() {
		return JSONObject.toJSONString(this);
	}

}
