package com.bestvike.elastic.param;

import com.alibaba.fastjson.JSONObject;
import lombok.Getter;
import lombok.Setter;

/**
 * @Author: yinxunyang
 * @Description: elasticSearch的房屋信息参数类
 * @Date: 2019/12/5 11:08
 */
@Getter
@Setter
public class EsHouseParam {
	/*	*//**
	 * 买受人身份证号
	 *//*
	private String buyCertNos;
	*//**
	 * 房屋买受人
	 *//*
	private String buyNames;
	*/
	/**
	 * 小区名称
	 */
	private String regionName;
	/**
	 * 开发企业名称
	 */
	private String developName;
	/**
	 * 数据中心主键
	 */
	private String dataCenterId;
	/**
	 * 主键
	 */
	private String houseId;
	/**
	 * 房屋性质
	 */
	private String houseType;
	/**
	 * 楼幢编号
	 */
	private String bldNo;
	/**
	 * 楼幢名称
	 */
	private String bldName;
	/**
	 * 单元编号
	 */
	private String cellNo;
	/**
	 * 单元名称
	 */
	private String cellName;
	/**
	 * 楼层编号
	 */
	private String floorNo;
	/**
	 * 楼层名称
	 */
	private String floorName;
	/**
	 * 房屋显示名称
	 */
	private String showName;
	/**
	 * 室号
	 */
	private String roomNo;
	/**
	 * 建筑面积
	 */
	private String constructArea;
	/**
	 * 房屋地址
	 */
	private String houseAddress;
	/**
	 * 版本号
	 */
	private String versionnumber;

	@Override
	public String toString() {
		return JSONObject.toJSONString(this);
	}

}
