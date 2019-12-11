package com.bestvike.bvdf.exception;


import com.bestvike.bvdf.enums.ReturnCode;
import org.apache.commons.lang3.StringUtils;

/**
 * @Author: yinxunyang
 * @Description: 自定义异常
 * @Date: 2019/12/9 14:43
 */
public class MsgException extends BaseException {

	public MsgException() {
		super();
	}

	public MsgException(String defineCode) {
		super(defineCode);
	}

	public MsgException(String defineCode, String defineMessage) {
		super(defineCode, defineMessage);
	}

	public MsgException(ReturnCode returnCode) {
		super(returnCode.toCode(), returnCode.getDesc());
	}

	public MsgException(ReturnCode returnCode, String defineMessage) {
		super(returnCode.toCode(), StringUtils.isBlank(defineMessage) ? returnCode.getDesc() : defineMessage);
	}

}
