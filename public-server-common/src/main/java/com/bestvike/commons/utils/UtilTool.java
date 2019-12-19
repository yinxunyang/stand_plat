package com.bestvike.commons.utils;

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
}
