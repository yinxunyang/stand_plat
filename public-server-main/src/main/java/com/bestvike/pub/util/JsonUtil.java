package com.bestvike.pub.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;

public final class JsonUtil {
	public JsonUtil() {
	}

	public static String toString(Object object) throws RuntimeException {
		ObjectMapper mapper = new ObjectMapper();

		try {
			return mapper.writeValueAsString(object);
		} catch (JsonProcessingException var3) {
			throw new RuntimeException(var3);
		}
	}

	public static <T> T toBean(String str, Class<T> clazz) {
		ObjectMapper mapper = new ObjectMapper();

		try {
			return mapper.readValue(str, clazz);
		} catch (IOException var4) {
			throw new RuntimeException(var4);
		}
	}
}