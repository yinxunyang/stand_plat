package com.bestvike.pub.enums;

/**
 * 响应码规范:
 * 00000 成功
 * 99999 失败
 * @author bestvike
 */
public enum ReturnCode {
    success("00000", "成功"),
    fail("99999", "失败"),

    /* 数据库错误*/
    plat_insert_fail("platInsertFail", "数据库新增失败"),
    plat_update_fail("platUpdateFail", "数据库修改失败"),
    plat_delete_fail("platDeleteFail", "数据库删除失败"),
    plat_null_info_fail("platNullInfoFail", "数据库没有该用户信息"),

    /* 数据检查错误*/
    plat_request_is_null("platRequestIsNull", "请求对象不能为空"),
    plat_request_param_is_null("platRequestParamIsNull", "请求参数不能为空"),
    plat_request_param_not_valid("platRequestParamNotValid", "请求参数未通过校验"),
    plat_param_lack("platParamLack", "参数缺失"),
    plat_param_length("platParamLength", "参数长度超限"),

    /* 应用系统错误*/
    plat_sys_error("platSysError", "系统异常"),

    /* 加解密错误*/
    plat_decrypt_error("platDecryptError", "解密异常"),
    plat_download_error("platDownloadError", "下载文件失败"),

    /* 配置信息错误*/
    plat_not_support("platNotSupport", "不支持此业务"),
    plat_config_not_exists("platConfigNotExists", "配置不存在"),
    plat_config_disable("platConfigDisable", "配置未开启"),
    plat_config_error("platConfigError", "配置信息有误"),
    plat_dict_not_exists("platDictNotExists", "字典项未配置"),
    plat_dict_error("platDictError", "字典项配置错误");

    private String code;
    private String desc;


    ReturnCode(String code, String message) {
        this.code = code;
        this.desc = message;
    }

    public String toCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

}
