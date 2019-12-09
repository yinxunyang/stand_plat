package com.bestvike.pub.exception;


import org.apache.commons.lang3.ArrayUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@SuppressWarnings("serial")
public class BaseException extends RuntimeException {

	/**
	 * 异常ID，用于表示某一异常实例，每一个异常实例都有一个唯一的异常ID
	 */
	protected String id;

	/**
	 * 异常信息，包含必要的上下文业务信息，用于打印日志
	 */
	protected String message;

	/**
	 * 具体异常码，即异常码code的后3位，由各具体异常实例化时自己定义
	 */
	protected String defineCode;

	protected String defineMessage;

	protected String realClassName;

	protected BaseException() {
		super();
		initId();
	}

	protected BaseException(String defineCode) {
		super();
		initId();
		this.defineCode = defineCode;
	}

	public BaseException(String defineCode, String defineMessage) {
		super();
		initId();
		this.defineCode = defineCode;
		this.defineMessage = defineMessage;
	}

	private void initId() {
		this.id = UUID.randomUUID().toString().toUpperCase().replaceAll("-", "");
	}

	public String getId() {
		return id;
	}

	public String getMessage() {
		String mes = "";
		Field[] fields = this.getClass().getDeclaredFields();
		for (Field field : fields) {
			try {
				field.setAccessible(true);
				Object obj = field.get(this);
				if (obj instanceof BaseException) {
					BaseException jps = (BaseException) obj;
					if (jps.getDefineCode().equals(this.getDefineCode())) {
						mes = field.getName();
						break;
					}
				}
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		return mes + " " + defineCode + " " + defineMessage + " " + message;
	}

	public void setMessage(String message, Object... args) {
		this.message = MessageFormat.format(message, args);
	}


	public String getDefineCode() {
		return defineCode;
	}

	public void setDefineCode(String defineCode) {
		this.defineCode = defineCode;
	}

	public String getDefineMessage() {
		return defineMessage;
	}

	public void setDefineMessage(String defineMessage) {
		this.defineMessage = defineMessage;
	}

	public static <T extends BaseException> T newException(T exception, String message, Object... args) {
		if (exception == null) {
			throw new RuntimeException("no exception instance specified");
		}
		try {
			Constructor constructor = exception.getClass().getDeclaredConstructor(String.class);
			constructor.setAccessible(true);
			T newException = (T) constructor.newInstance(exception.getDefineCode());
			newException.setMessage(message, args);
			return newException;
		} catch (Throwable e) {
			throw new RuntimeException("create exception instance fail : " + e.getMessage(), e);
		}
	}

	/**
	 * 比较异常的class和defineCode是否相同
	 *
	 * @param e
	 * @return
	 */
	public boolean codeEquals(BaseException e) {
		if (e == null) {
			return false;
		}
		if (!e.getClass().equals(this.getClass())) {
			return false;
		}
		return e.getDefineCode().equals(getDefineCode());
	}

	public BaseException upcasting() {
		if (this.getClass().equals(BaseException.class)) {
			return this;
		}
		BaseException superexception = new BaseException(this.defineCode);
		superexception.message = this.message;
		superexception.realClassName = this.getClass().getName();
		superexception.id = this.id;
		superexception.setStackTrace(this.getStackTrace());
		return superexception;
	}

	public BaseException downcasting() {
		if (this.realClassName == null || BaseException.class.getName().equals(this.realClassName)) {
			return this;
		}
		Class clz = null;
		try {
			clz = Class.forName(this.realClassName);
		} catch (Exception e) {
		}
		if (clz == null) {
			return this;
		}
		try {
			Constructor constructor = clz.getDeclaredConstructor(String.class);
			constructor.setAccessible(true);
			BaseException newException = (BaseException) constructor.newInstance(this.defineCode);
			newException.message = this.message;
			newException.id = this.id;
			newException.setStackTrace(this.getStackTrace());
			return newException;
		} catch (Throwable e) {
			throw new RuntimeException("create exception instance fail : " + e.getMessage(), e);
		}
	}

	public String getRealClassName() {
		if (realClassName == null) {
			return this.getClass().getName();
		}
		return realClassName;
	}

	public void mergeStackTrace(StackTraceElement[] stackTrace) {
		this.setStackTrace(ArrayUtils.addAll(this.getStackTrace(), stackTrace));
	}

	public StackTraceElement[] getCoreStackTrace() {
		List<StackTraceElement> list = new ArrayList<StackTraceElement>();
		for (StackTraceElement traceEle : getStackTrace()) {
			if (traceEle.getClassName().startsWith("com.bestvike")) {
				list.add(traceEle);
			}
		}
		StackTraceElement[] stackTrace = new StackTraceElement[list.size()];
		return list.toArray(stackTrace);
	}

	public String getCoreStackTraceStr() {
		StringBuffer sb = new StringBuffer();
		for (StackTraceElement traceEle : getCoreStackTrace()) {
			sb.append("\n" + traceEle.toString());
		}
		return sb.toString();
	}

}
