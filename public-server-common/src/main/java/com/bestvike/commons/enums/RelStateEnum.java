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
public enum RelStateEnum {

	MATCH_SUCCESS   ("00",     "匹配成功"),
	MATCH_FAIL      ("01",     "匹配失败");

	private String code;
	private String desc;
}
