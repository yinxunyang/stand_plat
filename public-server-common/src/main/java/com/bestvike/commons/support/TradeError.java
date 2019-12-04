package com.bestvike.commons.support;

import java.io.Serializable;

public class TradeError implements Serializable {

	private static final long serialVersionUID = -5906499629136300947L;

	private String resultCode;
	private String resultMsg;
	private Object data;

	public TradeError() {
	}

	public static TradeError build(String resultCode, String resultMsg, Object data) {
		TradeError tradeError = new TradeError();
		tradeError.setData(data);
		tradeError.setResultCode(resultCode);
		tradeError.setResultMsg(resultMsg);
		return tradeError;
	}

	public String getResultCode() {
		return resultCode;
	}

	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}

	public String getResultMsg() {
		return resultMsg;
	}

	public void setResultMsg(String resultMsg) {
		this.resultMsg = resultMsg;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}
}
