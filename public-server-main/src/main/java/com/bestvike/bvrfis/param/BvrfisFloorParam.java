package com.bestvike.bvrfis.param;

import com.alibaba.fastjson.JSONObject;
import lombok.Getter;
import lombok.Setter;

/**
 * @Author: yinxunyang
 * @Description: bvrfis的楼层信息参数类
 * @Date: 2019/12/10 11:08
 */
@Getter
@Setter
public class BvrfisFloorParam {
	/**
	 * 楼幢编号
	 */
	private String bldNo;
	/**
	 * 楼层编号
	 */
	private String floorNo;
	/**
	 * 楼层名称
	 */
	private String floorName;
	/**
	 * 房屋性质
	 */
	private String houseProp;


	@Override
	public String toString() {
		return JSONObject.toJSONString(this);
	}

}
