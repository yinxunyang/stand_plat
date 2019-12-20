package com.bestvike.commons.utils;

import com.alibaba.fastjson.JSONObject;

import java.util.UUID;

public class UtilTool {
	/**
	 * 生成32位的UUID
	 *
	 * @return
	 */
	public static String UUID() {
		return UUID.randomUUID().toString().replace("-", "").toUpperCase();
	}

	/**
	 * @Author: yinxunyang
	 * @Description: 将json串转换成bean
	 * @Date: 2019/12/20 17:13
	 * @param:
	 * @return:
	 */
	public static Object jsonToObj(String json, Class className) {
		return JSONObject.parseObject(json, className);
	}
}
