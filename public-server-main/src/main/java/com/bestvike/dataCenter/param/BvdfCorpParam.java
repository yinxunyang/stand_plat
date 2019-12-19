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
	 * 企业名称
	 */
	private String corpName;
	/**
	 * 营业执照编号
	 */
	private String licenseNo;

	@Override
	public String toString() {
		return JSONObject.toJSONString(this);
	}

}
