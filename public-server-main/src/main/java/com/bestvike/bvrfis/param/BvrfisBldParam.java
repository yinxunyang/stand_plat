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
	 * datacenterId
	 */
	private String datacenterId;
	/**
	 * 楼幢编号
	 */
	private String bldNo;
	/**
	 * 楼幢名称
	 */
	private String bldName;
	/**
	 * 自然幢地址
	 */
	private String address;
	/**
	 * 总面积
	 */
	private String totalArea;
	/**
	 * 开工日期
	 */
	private String startDate;
	/**
	 * 竣工验收日期
	 */
	private String finishDate;
	/**
	 * 小区编号
	 */
	private String regionNo;
	/**
	 * 楼幢名称
	 */
	private String developNo;
	/**
	 * 行政区编号
	 */
	private String divisionCode;
	/**
	 * 状态
	 */
	private String state;


	@Override
	public String toString() {
		return JSONObject.toJSONString(this);
	}

}
