package com.bestvike.commons.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author: yinxunyang
 * @Description: 匹配分析类型--枚举类
 * @Date: 2019/12/9 16:22
 */
@Getter
@AllArgsConstructor
public enum MatchTypeEnum {
	REGION    ("00",         "小区表"),
	BLD       ("01",         "楼幢表"),
	CELL      ("02",         "单元表"),
	FLOOR     ("03",         "楼层表"),
	HOUSE     ("04",         "房屋表"),
	OWNER     ("05",         "业主信息表"),
	DEVELOP   ("06",         "单位信息表");

	private String code;
	private String desc;
}
