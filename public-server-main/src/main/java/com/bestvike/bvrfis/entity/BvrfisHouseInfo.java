package com.bestvike.bvrfis.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
 /**
   * @Author: yinxunyang
   * @Description: 维修资金的房屋信息表
   * @Date: 2019/12/11 10:24
   * @param: 
   * @return: 
   */
@Entity
@Table(name = "Arc_HouseInfo")
@Getter
@Setter
public class BvrfisHouseInfo implements Serializable{
	private static final long serialVersionUID = -899544870183278607L;
	/**
	 * 主键
	 */
	private String sysGuid;
	 /**
	  * 自然幢编号
	  */
	private String bldNo;
	 /**
	  * 单元编号
	  */
	private String cellNo;
	 /**
	  * 楼层编号
	  */
	private String floorNo;
	 /**
	  * 房间号
	  */
	private String roomNo;
	 /**
	  * 显示名称
	  */
	private String showName;
	 /**
	  * 房屋地址
	  */
	private String address;
	 /**
	  * 状态
	  */
	private String state;
	 /**
	  * 房屋性质
	  */
	private String houseProp;
	 /**
	  * 房屋类型
	  */
	private String houseType;
	 /**
	  * 建筑面积
	  */
	private String constructArea;
	 /**
	  * 交存模式
	  */
	private String depositNo;
	 /**
	  * 应交金额
	  */
	private String dueAmount;
	 /**
	  * 账户余额
	  */
	private String balance;
	 /**
	  * 账户冻结金额
	  */
	private String frozen;
	 /**
	  * 交存标志
	  */
	private String depositSign;
	 /**
	  * 主房号
	  */
	private String mainHouseGuid;
	 /**
	  * 本次结息日期
	  */
	private String allotDate;
	 /**
	  * 备案日期
	  */
	private String recordDate;
	 /**
	  * 备案时间
	  */
	private String recordTime;
	 /**
	  * 备案用户
	  */
	private String recordUser;
	 /**
	  * 数据来源
	  */
	private String dataFrom;
	 /**
	  * 数据原编号
	  */
	private String orginNo;
	 /**
	  * 数据说明
	  */
	private String dataExplain;
	 /**
	  * 备注
	  */
	private String remark;
	 /**
	  * 账户类别
	  */
	private String accountType;
	 /**
	  * 承办银行
	  */
	private String majorCode;
	 /**
	  * 交存累计金额
	  */
	private String depTotalAmount;
	 /**
	  * 续筹累计金额
	  */
	private String conTotalAmount;
	 /**
	  * 退款累计金额
	  */
	private String drwTotalAmount;
	 /**
	  * 使用累计金额
	  */
	private String expTotalAmount;
	 /**
	  * 利息累计金额
	  */
	private String accrTotalAmount;
	 /**
	  * 是否为历史数据
	  */
	private String isHisTorical;
	 /**
	  * 最后一次结息后账户余额
	  */
	private String lastYearBalance;
	 /**
	  * 用于房屋合并
	  */
	private String mergeNum;
	 /**
	  * 用于房屋合并
	  */
	private String skipNum;
	 /**
	  * 续交模式编号
	  */
	private String fillNo;
	 /**
	  * 续交应缴额
	  */
	private String fillDueAmount;
	 /**
	  * 续交状态
	  */
	private String fillState;
	 /**
	  * 共有房屋ID
	  */
	private String shareHouseId;
	 /**
	  * 是否垫付
	  */
	private String isPrepay;
	 /**
	  * 企业代交交存金额
	  */
	private String corpPayAmount;
	 /**
	  * 房屋售价
	  */
	private String price;
	 /**
	  * 在途状态
	  */
	private String transmitState;
	 /**
	  * 列编号
	  */
	private String colNo;
	private String incomeTotalAmount;
	private String subArea;
}
