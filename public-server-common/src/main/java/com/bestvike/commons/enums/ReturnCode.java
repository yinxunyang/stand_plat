package com.bestvike.commons.enums;

/**
 * sdp:standplat
 * 响应码规范:
 * 00000 成功
 * 第一位,第二位:
 * 00 特殊码  不记录异常，只是提示
 * 01 数据库错误
 * 02 网络错误
 * 03 操作系统错误
 * 04 应用系统错误
 * 05 加解密异常
 * 06 业务逻辑错误
 * 10 数据检查错误
 * 20 配置信息错误
 * 30 工作流错误
 * 40 银行返回错误映射
 * 50 文件读取错误
 * <p>
 * 第三位,系统标识:
 * 7 房产信息系统历史数据标准化平台
 */
public enum ReturnCode {
	success("00000", "成功"),
	fail("99998", "失败"),
	process("00002","处理中"),

	/*01 数据库错误*/
	sdp_insert_fail             ("SDP0170001", "orcle数据库新增失败"),
	sdp_update_fail             ("SDP0170002", "orcle数据库修改失败"),
	sdp_delete_fail             ("SDP0170003", "orcle数据库删除失败"),
	sdp_select_fail             ("SDP0170004", "orcle数据库查询失败"),
	sdp_es_insert_fail          ("SDP0170005", "es数据库新增失败"),
	sdp_es_delete_fail          ("SDP0170006", "es数据库删除失败"),
	sdp_es_select_fail          ("SDP0170007", "es数据库查询失败"),

	/*10 数据检查错误*/
	sdp_request_is_null         ("SDP1070001", "请求对象不能为空"),
	sdp_request_param_is_null   ("SDP1070002", "请求参数不能为空"),
	sdp_request_param_not_valid ("SDP1070003", "请求参数未通过校验"),
	sdp_request_repeat          ("SDP1070004", "请求重复"),
	sdp_response_is_null        ("SDP1070005", "返回结果为空"),

	/*04 应用系统错误*/
	sdp_sys_error               ("SDP0470001", "系统异常"),
	sdp_extsys_error            ("SDP0470002", "外部系统返回错误"),

	/*05 加解密错误*/

	/* 06 业务逻辑错误*/
	sdp_not_recorded            ("SDP0670001", "无此记录"),
	sdp_inaccuracy_status       ("SDP0670002", "操作状态不正确"),

	/*20 配置信息错误*/
	sdp_not_support             ("SDP2070001", "不支持此业务"),
	sdp_config_not_exists       ("SDP2070002", "配置不存在"),
	sdp_config_disable          ("SDP2070003", "配置未开启"),
	sdp_config_error            ("SDP2070004", "配置信息有误"),
	sdp_dict_not_exists         ("SDP2070005", "字典项未配置"),
	sdp_dict_error              ("SDP2070006", "字典项配置错误"),

	/* 50 文件读取错误 */
	sdp_file_not_exist_error    ("SDP5070001", "文件不存在"),
	sdp_file_empty_error        ("SDP5070002", "文件内容为空"),
	sdp_file_format_error       ("SDP5070003", "文件格式错误"),

	/* 30 工作流错误*/
	sdp_approve_role_not_exists ("SDP3070001", "下一级审批角色无相关负责人"),
	sdp_workflow_fail           ("SDP3070002", "开启工作流失败");


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
