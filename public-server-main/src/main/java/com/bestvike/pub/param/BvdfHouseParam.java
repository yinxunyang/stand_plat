package com.bestvike.pub.param;

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
	 * 买受人身份证号
	 */
	private String buycertnos;
	/**
	 * 小区名称
	 */
	private String regionname;
	/**
	 * 楼幢名称
	 */
	private String bldname;
	/**
	 * 单元名称
	 */
	private String cellname;
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
	private String houseAddress;
}
