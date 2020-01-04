package com.bestvike.bvrfis.param;

import com.alibaba.fastjson.JSONObject;
import lombok.Getter;
import lombok.Setter;

/**
 * @Author: yinxunyang
 * @Description: bvrfis的房屋信息参数类
 * @Date: 2019/12/10 11:08
 */
@Getter
@Setter
public class BvrfisHouseParam {
	/**
	 * 数据中心Id
	 */
	private String datacenterId;
	/**
	 * 主键
	 */
	private String sysGuid;
	/**
	 * 状态 0正常
	 */
	private String state;
	/**
	 * 楼幢编号
	 */
	private String bldNo;
	/**
	 * 小区编号
	 */
	private String regionNo;
	/**
	 * 小区名称
	 */
	private String regionName;
	/**
	 * 开发企业名称
	 */
	private String developName;
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
	 * 室号
	 */
	private String roomNo;
	/**
	 * 显示名称
	 */
	private String showName;
	/**
	 * 房屋地址
	 */
	private String address;
	/**
	 * 房屋性质
	 */
	private String houseProp;
	/**
	 * 建筑面积
	 */
	private String constructArea;
	/**
	 * 最多查询数量
	 */
	private Integer houseMaxNum;
	@Override
	public String toString() {
		return JSONObject.toJSONString(this);
	}

}
