package com.bestvike.bvdf.param;

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
	/**
	 * 主键
	 */
	private String id;
	/**
	 * 开发企业名称
	 */
	private String developName;
	/**
	 * 营业执照编号
	 */
	private String licenseNo;
	/**
	 * 楼幢名称
	 */
	private String bldName;
	/**
	 * 单元名称
	 */
	private String cellName;
	/**
	 * 楼层名称
	 */
	private String floorName;
	/**
	 * 室号
	 */
	private String roomno;
	/**
	 * 买受人身份证号
	 */
	private String buyCertNos;
	/**
	 * 房屋买受人
	 */
	private String buyNames;
	/**
	 * 房屋地址
	 */
	private String houseAddress;

	@Override
	public String toString() {
		return JSONObject.toJSONString(this);
	}

}
