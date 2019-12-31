package com.bestvike.dataCenter.param;

import com.alibaba.fastjson.JSONObject;
import lombok.Getter;
import lombok.Setter;

/**
 * @Author: yinxunyang
 * @Description: bvdf的单元信息参数类
 * @Date: 2019/12/5 11:08
 */
@Getter
@Setter
public class BvdfCellParam {
	/**
	 * 自然幢编号
	 */
	private String bldNo;
	/**
	 * 单元编号
	 */
	private String cellNo;
	/**
	 * 单元名称
	 */
	private String cellName;
	/**
	 * 房屋类型
	 */
	private String houseType;

	@Override
	public String toString() {
		return JSONObject.toJSONString(this);
	}

}
