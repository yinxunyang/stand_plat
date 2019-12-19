package com.bestvike.bvrfis.param;

import com.alibaba.fastjson.JSONObject;
import lombok.Getter;
import lombok.Setter;

/**
 * @Author: yinxunyang
 * @Description: bvrfis的公司信息参数类
 * @Date: 2019/12/10 11:08
 */
@Getter
@Setter
public class BvrfisCorpInfoParam {
	/**
	 * 主键
	 */
	private String corpNo;
	/**
	 * 单位名称
	 */
	private String corpName;
	/**
	 * 营业执照编号
	 */
	private String licenseNo;
	/**
	 * 状态 0正常
	 */
	private String state;
	/**
	 * 类型 01开发企业
	 */
	private String corptype;
	/**
	 * 数据中心Id
	 */
	private String datacenterid;

	@Override
	public String toString() {
		return JSONObject.toJSONString(this);
	}

}
