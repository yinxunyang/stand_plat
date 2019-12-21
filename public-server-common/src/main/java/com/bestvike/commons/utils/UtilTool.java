package com.bestvike.commons.utils;

import com.alibaba.fastjson.JSONObject;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public class UtilTool {
	private static final DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
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

	/**
	 * @Author: yinxunyang
	 * @Description: 获取系统当前时间 yyyy-MM-dd HH:mm:ss
	 * @Date: 2019/12/21 9:26
	 * @param:
	 * @return:
	 */
	public static String nowTime() {
		return df.format(LocalDateTime.now());
	}
}
