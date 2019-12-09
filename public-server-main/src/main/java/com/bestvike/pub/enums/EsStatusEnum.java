package com.bestvike.pub.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author: yinxunyang
 * @Description: elasticsearch响应报文的状态--枚举类
 * @Date: 2019/12/9 16:22
 */
@Getter
@AllArgsConstructor
public enum EsStatusEnum {
	ES_INSERTED("CREATED", "往elasticsearch新增数据成功"),
	ES_UPDATED("OK", "往elasticsearch更新数据成功");

	private String code;
	private String desc;
}
