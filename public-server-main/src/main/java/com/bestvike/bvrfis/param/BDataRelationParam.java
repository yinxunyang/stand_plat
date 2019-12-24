package com.bestvike.bvrfis.param;

import com.alibaba.fastjson.JSONObject;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * @Author: yinxunyang
 * @Description: bvrfis的公司信息参数类
 * @Date: 2019/12/10 11:08
 */
@Getter
@Setter
public class BDataRelationParam {
	/**
	 * 主键
	 */
	private String relationId;
	/**
	 * 类型
	 */
	private String linkType;
	/**
	 * 维修资金数据ID
	 */
	private String wxBusiId;
	/**
	 * 数据中心id
	 */
	private String centerId;
	/**
	 * 网签数据ID
	 */
	private String wqBusiId;
	/**
	 * 统一数据id
	 */
	private String busId;
	/**
	 * 版本号
	 */
	private BigDecimal version;
	/**
	 * 匹配id
	 */
	private String matchId;
	/**
	 * 创建人
	 */
	private String inUser;
	/**
	 * 创建时间
	 */
	private String inDate;
	/**
	 * 编辑人
	 */
	private String editUser;
	/**
	 * 编辑时间
	 */
	private String editDate;
	@Override
	public String toString() {
		return JSONObject.toJSONString(this);
	}

}
