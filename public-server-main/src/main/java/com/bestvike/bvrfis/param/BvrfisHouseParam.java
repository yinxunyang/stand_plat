package com.bestvike.bvrfis.param;

import com.alibaba.fastjson.JSONObject;
import lombok.Getter;
import lombok.Setter;

/**
 * @Author: yinxunyang
 * @Description: bvrfis的房屋信息参数类
 * @Date: 2019/12/10 11:08
 */
@Getter
@Setter
public class BvrfisHouseParam {
	/**
	 * 主键
	 */
	private String sysGuid;
	/**
	 * 楼幢编号
	 */
	private String bldNo;
	/**
	 * 单元编号
	 */
	private String cellNo;
	/**
	 * 楼层编号
	 */
	private String floorNo;
	/**
	 * 室号
	 */
	private String roomNo;
	/**
	 * 房屋地址
	 */
	private String address;
	/**
	 * 房屋性质
	 */
	private String houseProp;

	@Override
	public String toString() {
		return JSONObject.toJSONString(this);
	}

}
