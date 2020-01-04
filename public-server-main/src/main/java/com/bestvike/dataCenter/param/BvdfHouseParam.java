package com.bestvike.dataCenter.param;

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
	 * 数据中心主键
	 */
	private String dataCenterId;
	/**
	 * 主键
	 */
	private String houseid;
	/**
	 * 房屋性质
	 */
	private String housetype;
	/**
	 * 小区名称
	 */
	private String regionName;
	/**
	 * 开发企业名称
	 */
	private String developName;
	/**
	 * 楼幢编号
	 */
	private String bldno;
	/**
	 * 楼幢名称
	 */
	private String bldName;
	/**
	 * 单元编号
	 */
	private String cellno;
	/**
	 * 单元编号
	 */
	private String cellName;
	/**
	 * 楼层编号
	 */
	private String floorno;
	/**
	 * 楼层名称
	 */
	private String floorname;
	/**
	 * 房屋显示名称
	 */
	private String showname;
	/**
	 * 室号
	 */
	private String roomno;
	/**
	 * 建筑面积
	 */
	private String constructArea;
	/**
	 * 房屋地址
	 */
	private String address;
	/**
	 * 版本号
	 */
	private String versionnumber;
	/**
	 * 状态 0正常
	 */
	private String state;
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
	/**
	 * 最多查询数量
	 */
	private Integer houseMaxNum;
	@Override
	public String toString() {
		return JSONObject.toJSONString(this);
	}

}
