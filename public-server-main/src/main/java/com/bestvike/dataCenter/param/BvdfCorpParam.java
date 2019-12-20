package com.bestvike.dataCenter.param;

import com.alibaba.fastjson.JSONObject;
import lombok.Getter;
import lombok.Setter;

/**
 * @Author: yinxunyang
 * @Description: bvdf的公司信息参数类
 * @Date: 2019/12/5 11:08
 */
@Getter
@Setter
public class BvdfCorpParam {
	/**
	 * id
	 */
	private String corpId;
	/**
	 * 企业名称
	 */
	private String corpName;
	/**
	 * 营业执照编号
	 */
	private String licenseNo;
	/**
	 * 组织机构代码
	 */
	private String certificateNo;
	/**
	 * 版本号
	 */
	private String versionnumber;
	/**
	 * 状态
	 */
	private String state;

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
