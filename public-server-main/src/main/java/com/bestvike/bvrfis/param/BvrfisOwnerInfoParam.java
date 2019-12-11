package com.bestvike.bvrfis.param;

import com.alibaba.fastjson.JSONObject;
import lombok.Getter;
import lombok.Setter;

/**
 * @Author: yinxunyang
 * @Description: bvrfis的业主信息参数类
 * @Date: 2019/12/10 11:08
 */
@Getter
@Setter
public class BvrfisOwnerInfoParam {
	/**
	 * 业主账户
	 */
	private String subAccount;
	/**
	 * 身份证号
	 */
	private String certNo;
	/**
	 * 业主姓名
	 */
	private String ownerName;

	@Override
	public String toString() {
		return JSONObject.toJSONString(this);
	}

}
