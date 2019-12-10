package com.bestvike.pub.param;

import com.alibaba.fastjson.JSONObject;
import lombok.Getter;
import lombok.Setter;

/**
 * @Author: yinxunyang
 * @Description: bvdf的房屋信息参数类
 * @Date: 2019/12/5 11:08
 */
@Getter
@Setter
public class BvdfHouseParam {
	/**
	 * 主键
	 */
	private String sysguid;
	/**
	 * 买受人身份证号
	 */
	private String buycertnos;
	/**
	 * 企业编号
	 */
	private String corpno;
	/**
	 * 小区编号
	 */
	private String projectno;
	/**
	 * 楼幢编号
	 */
	private String bldno;
	/**
	 * 单元编号
	 */
	private String cellno;
	/**
	 * 楼层名称
	 */
	private String floorname;
	/**
	 * 室号
	 */
	private String roomno;
	/**
	 * 房屋买受人
	 */
	private String buynames;
	/**
	 * 房屋地址
	 */
	private String address;

	@Override
	public String toString() {
		return JSONObject.toJSONString(this);
	}

}
