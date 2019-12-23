package com.bestvike.dataCenter.param;

import com.alibaba.fastjson.JSONObject;
import lombok.Getter;
import lombok.Setter;

/**
 * @Author: yinxunyang
 * @Description: bvrfis的小区信息参数类
 * @Date: 2019/12/10 11:08
 */
@Getter
@Setter
public class BvdfRegionParam {
	/**
	 * dataCenterId
	 */
	private String dataCenterId;
	/**
	 * 开发企业编号
	 */
	private String corpNo;
	/**
	 * 小区编号
	 */
	private String regionNo;
	/**
	 * 小区名称
	 */
	private String regionName;
	/**
	 * 行政区编号
	 */
	private String divisionCode;
	/**
	 * 地址
	 */
	private String address;
	/**
	 * 状态 0正常
	 */
	private String state;
	/**
	 * 版本号
	 */
	private String versionnumber;

	@Override
	public String toString() {
		return JSONObject.toJSONString(this);
	}

}
