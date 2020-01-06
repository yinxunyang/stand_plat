package com.bestvike.bvrfis.param;

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
public class BvrfisRegionParam {
	/**
	 * 数据中心Id
	 */
	private String dataCenterId;
	/**
	 * 主键
	 */
	private String corpNo;
	/**
	 * 单位编号
	 */
	private String regionNo;
	/**
	 * 单位名称
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
	 * 占地面积
	 */
	private String floorArea;
	/**
	 * 状态 0正常
	 */
	private String state;

	@Override
	public String toString() {
		return JSONObject.toJSONString(this);
	}

}
