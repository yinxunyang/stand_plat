package com.bestvike.pub.exception;

import com.bestvike.pub.enums.ReturnCode;

/**
 * @Author: yinxunyang
 * @Description: 业务异常
 * @Date: 2019/12/9 10:57
 */
public class BusinessException extends RuntimeException {
	private static final long serialVersionUID = 2909756086213684900L;
	private String retFlag;
	private String retMsg;

	public BusinessException(String retFlag, String retMsg) {
		this.retFlag = retFlag;
		this.retMsg = retMsg;
	}

	public BusinessException(ReturnCode returnCode) {
		this.retFlag = returnCode.toCode();
		this.retMsg = returnCode.getDesc();
	}

	public BusinessException(String retFlag) {
		this.retFlag = retFlag;
	}

	public String getRetFlag() {
		return retFlag;
	}

	public void setRetFlag(String retFlag) {
		this.retFlag = retFlag;
	}

	public String getRetMsg() {
		return retMsg;
	}

	public void setRetMsg(String retMsg) {
		this.retMsg = retMsg;
	}
}
