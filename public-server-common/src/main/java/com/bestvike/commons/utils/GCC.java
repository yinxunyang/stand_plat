package com.bestvike.commons.utils;

/**
 * @author
 * @version V1.0
 * @ClassName: GCC
 * @Description: 全局静态变量 GlobalConstCode
 * @date
 */
public class GCC {
	/**************************需求变更记录state******************************/
	/**
	 * F:\excelFile
	 */
	public static final String PERSONINFO_EXCEL = "excelFile";
	/**
	 * 信息来源：系统录入
	 */
	public static final String CORPINFO_DATAFROM_ENTER = "10";
	/**
	 * 信息来源：excel导入
	 */
	public static final String CORPINFO_DATAFROM_EXCELIMP = "11";
	/**
	 * 信息来源：历史数据导入
	 */
	public static final String CORPINFO_DATAFROM_IMP = "21";
	/**
	 * 自然幢：挂接
	 */
	public static final String BUILDINFO_DATAFROM_GJ = "99";
	/**
	 * 信息来源：房屋的来源：陵城合同签订时补录附房（如：车库、储藏室等的附房不在系统录入，而是在签订合同时，在合同房屋信息页面，手动填写附房的房屋信息）
	 */
	public static final String HOUSEINFO_DATAFROM_SIGNCONTRACT = "31";
	/**
	 * 判断是否：是
	 */
	public static final String DIC_PUBLIST_YESORNO_YES = "Y";

	/**************************需求变更记录end******************************/
	/**
	 * 判断是否：否
	 */
	public static final String DIC_PUBLIST_YESORNO_NO = "N";
	/**
	 * 通过房屋的sysGuid查询密码
	 */
	public static final String VERIFY_CONTRACTPASSWORD_HOUSEGUID = "1";
	/**
	 * 通过合同号查询密码
	 */
	public static final String VERIFY_CONTRACTPASSWORD_CONTRACTNO = "2";
	/**
	 * 系统定时(name)
	 */
	public static final String SYSTEMTIMING = "系统定时";
	/**
	 * 系统定时(id)
	 */
	public static final String SYSTEMTIMINGID = "system";
	/******************************短信发送地址****************************************************/
	public static final String SENDMESSAGEURL = "http://192.168.0.2:83/sms_add.php";
	/**********************************数据中心地址******************************************************/
	public static final String DATACENTERURL = "http://127.0.0.1:81/";
	/**
	 * 需求变更记录状态
	 *
	 * @author 刘圣君
	 * @Date 2018-1-15
	 */
	public static String BVDF_LOGBUSINESSCHANGE_STATE_NORMAL = "00";//正常
	public static String BVDF_LOGBUSINESSCHANGE_STATE_DELETE = "90";//删除
	/**
	 * 字数验证
	 */
	public static int BVDF_LOGBUSINESSCHANGE_EXHIBITOR = 6;//提出人
	public static int BVDF_LOGBUSINESSCHANGE_CHANGEDES = 651;//需求变更描述
	public static int BVDF_LOGBUSINESSCHANGE_DBCHANGEDEC = 651;//数据库变更描述
	public static int BVDF_LOGBUSINESSCHANGE_CHANGEMAN = 6;//变更人
	public static int BVDF_LOGBUSINESSCHANGE_REMARK = 61;//备注
	/**
	 * 预售资金监管系统代码
	 */
	public static String BVYS_SYSCODE = "22";
	/**
	 * 预售房网签系统代码
	 */
	public static String BVDF_SYSCODE = "12";
	/**
	 * SESSION KEY 加密设备序列号  keySn
	 */
	public static String SESSION_KEY_KEYSN = "keySn";
	/**
	 * SESSION KEY 是否为超级管理员  keyAdmin
	 */
	public static String SESSION_KEY_KEYADMIN = "keyAdmin";
	/**
	 * SESSION KEY 用户id  userId
	 */
	public static String SESSION_KEY_USERID = "userId";
	/**
	 * SESSION KEY 用户别名  aliasName
	 */
	public static String SESSION_KEY_ALIASNAME = "aliasName";
	/**
	 * SESSION KEY 用户姓名  userName
	 */
	public static String SESSION_KEY_USERNAME = "userName";

	/********************流程日志表  环节类型 end**********************************/
	/**
	 * SESSION KEY 用户登录ip userIp
	 */
	public static String SESSION_KEY_USERIP = "userIp";
	/**
	 * SESSION KEY 城市代码  cityCode
	 */
	public static String SESSION_KEY_CITYCODE = "cityCode";

	/********************信息来源********************************/
	/**
	 * SESSION KEY 行政区代码  divisionCode
	 */
	public static String SESSION_KEY_DIVISIONCODE = "divisionCode";
	/**
	 * SESSION KEY 行政区代码  市 0
	 */
	public static String SESSION_KEY_DIVISIONCODE_CITY = "0";
	/**
	 * SESSION KEY 系统代码  sysCode
	 */
	public static String SESSION_KEY_SYSCODE = "sysCode";
	/**
	 * SESSION KEY 角色代码  roleCode
	 */
	public static String SESSION_KEY_ROLECODE = "roleCode";
	/**
	 * SESSION KEY 单位类别  corpType
	 */
	public static String SESSION_KEY_CORPTYPE = "corpType";

	/******************字典：是否***********************************/
	/**
	 * SESSION KEY 项目编号  branchNo
	 */
	public static String SESSION_KEY_BRANCHNO = "branchNo";
	/**
	 * SESSION KEY 单位类别  预售人  01
	 */
	public static String SESSION_KEY_CORPTYPE_COMPANY = "01";
	/***********************图档状态 start***********************/
	/**
	 * SESSION KEY 单位编号  corpNo
	 */
	public static String SESSION_KEY_CORPNO = "corpNo";
	/***********************图档状态 end*************************/
	/**
	 * SESSION KEY 单位名称  corpName
	 */
	public static String SESSION_KEY_CORPNAME = "corpName";
	/**
	 * SESSION KEY 角色名称  roleName
	 */
	public static String SESSION_KEY_ROLENAME = "roleName";
	/********************流程日志表  环节类型 start**********************************/
	public static String DIC_FLOWLOG_NODETYPE = "flowlog_nodetype";
	/**
	 * *****************流程日志表  动作类型  start **************************
	 */
	public static String DIC_FLOWLOG_ACTIONTYPE = "flowlog_actiontype";

	/****************************往数据集中平台发送数据begin*********************************/
	public static String DOC_FILE_BUSITYPE_COMP = "50";//用于demo
	/**
	 * 图档状态 正常 0
	 */
	public static String UPLOADFILE_STATE_NOMAL = "0";
	/**
	 * 备案方式：1_手动备案
	 */
	public static String RECORD_BY_USER = "1";
	/**
	 * 备案方式：2_自动备案
	 */
	public static String RECORD_BY_SELF = "2";
	/**
	 * 合同编号的生成方式：1_手动生成
	 */
	public static String CONTRACTNO_TYPE_USER = "1";
	/**
	 * 合同编号的生成方式：2_自动生成
	 */
	public static String CONTRACTNO_TYPE_SELF = "2";
	/**
	 * 发送数据类型：预售人（企业）
	 */
	public static String DATATYPE_CORP_CODE = "01";
	/**
	 * 发送路径类型：预售人（企业）
	 */
	public static String DATATYPE_CORP_DESCRIBE = "corp";
	/**
	 * 发送数据类型：项目
	 */
	public static String DATATYPE_PROJECT_CODE = "02";
	/**
	 * 发送路径类型：项目
	 */
	public static String DATATYPE_PROJECT_DESCRIBE = "project";
	/**
	 * 发送数据类型：自然幢
	 */
	public static String DATATYPE_BUILD_CODE = "03";
	/**
	 * 发送路径类型：自然幢
	 */
	public static String DATATYPE_BUILD_DESCRIBE = "build";
	/**
	 * 发送数据类型：房屋
	 */
	public static String DATATYPE_HOUSE_CODE = "04";
	/**
	 * 发送数据路径：房屋
	 */
	public static String DATATYPE_HOUSE_DESCRIBE = "house";
	/**
	 * 发送数据类型：买受人
	 */
	public static String DATATYPE_BUYER_CODE = "05";
	/**
	 * 发送路径类型：买受人
	 */
	public static String DATATYPE_BUYER_DESCRIBE = "buyer";
	/**
	 * 发送数据类型：企业用户
	 */
	public static String DATATYPE_PUBUSER_CODE = "06";
	/**
	 * 发送路径类型：企业用户
	 */
	public static String DATATYPE_PUBUSER_DESCRIBE = "pubUser";
	/**
	 * 发送数据类型：合同信息
	 */
	public static String DATATYPE_CONTRACT_CODE = "07";
	/**
	 * 发送路径类型：合同信息
	 */
	public static String DATATYPE_CONTRACT_DESCRIBE = "contract";
	/**
	 * 发送数据类型：合同房屋信息
	 */
	public static String DATATYPE_CONHOUSE_CODE = "08";
	/**
	 * 发送路径类型：合同房屋信息
	 */
	public static String DATATYPE_CONHOUSE_DESCRIBE = "conHouse";

	/****************************往数据集中平台发送数据end*********************************/

	/****************************验证合同密码的方式start*********************************/
	/**
	 * 发送数据动作：新增
	 */
	public static String OPERATE_INSERT_CODE = "01";
	/**
	 * 发送路径动作：新增
	 */
	public static String OPERATE_INSERT_DESCRIBE = "insert";
	/****************************验证合同密码的方式end*********************************/
	/**
	 * 发送数据动作：修改
	 */
	public static String OPERATE_UPDATE_CODE = "02";
	/**
	 * 发送路径动作：修改
	 */
	public static String OPERATE_UPDATE_DESCRIBE = "update";
	/**
	 * 发送数据动作：删除
	 */
	public static String OPERATE_DELETE_CODE = "03";
	/**
	 * 发送路径动作：删除
	 */
	public static String OPERATE_DELETE_DESCRIBE = "deleteByIds";


	//f_contractaccount表id生成后缀
	public static String CONTRACTACCOUNT_ID_PREFIX = "合同监管资金明细户";
}
