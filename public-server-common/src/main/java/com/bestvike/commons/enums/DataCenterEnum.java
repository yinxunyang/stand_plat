package com.bestvike.commons.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author: yinxunyang
 * @Description: dataCenter--枚举类
 * @Date: 2019/12/9 16:22
 */
@Getter
@AllArgsConstructor
public enum DataCenterEnum {
	NORMAL_STATE       ("normal",     "正常状态"),
	BVDF_APP_CODE      ("BVDF",       "BVDF的appCode");

	private String code;
	private String desc;
}
