package com.bestvike.dataCenter.param;

import com.alibaba.fastjson.JSONObject;
import lombok.Getter;
import lombok.Setter;

/**
 * @Author: yinxunyang
 * @Description: bvdf的自然幢信息参数类
 * @Date: 2019/12/10 11:08
 */
@Getter
@Setter
public class BvdfBldParam {
	/**
	 * dataCenterId
	 */
	private String dataCenterId;
	/**
	 * 自然幢编号
	 */
	private String bldNo;
	/**
	 * 自然幢名称
	 */
	private String bldName;
	/**
	 * 地址
	 */
	private String address;
	/**
	 * 占地面积
	 */
	private String totalArea;
	/**
	 * 开始日期
	 */
	private String startdate;
	/**
	 * 结束日期
	 */
	private String finishdate;
	/**
	 * 小区编号
	 */
	private String regionNo;
	/**
	 * 小区名称
	 */
	private String regionName;
	/**
	 * 开发企业编号
	 */
	private String corpNo;
	/**
	 * 开发企业名称
	 */
	private String corpName;
	/**
	 * 状态 0正常
	 */
	private String state;
	/**
	 * 版本号
	 */
	private String versionnumber;
	/**
	 * 行政区编号
	 */
	private String divisionCode;
	/**
	 * appCode
	 */
	private String appcode;
	/**
	 * 开始时间
	 */
	private String scopeBeginTime;
	/**
	 * 结束时间
	 */
	private String scopeEndTime;

	@Override
	public String toString() {
		return JSONObject.toJSONString(this);
	}

}
