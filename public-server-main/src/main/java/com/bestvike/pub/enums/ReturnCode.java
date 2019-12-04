package com.bestvike.pub.enums;

/**
 * 响应码规范:
 * 00000 成功
 * 99999 失败
 * @author bestvike
 */
public enum ReturnCode implements RespCode {
    success("0000", "成功"),
    fail("9999","失败");

    private String code;
    private String message;

    ReturnCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
