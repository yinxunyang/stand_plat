package com.bestvike.bvrfis.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;

/**
 * @Author: yinxunyang
 * @Description: 操作日志表
 * @Date: 2019/12/11 10:24
 * @param:
 * @return:
 */
@Entity
@Table(name = "B_LogOper")
@Getter
@Setter
public class BLogOper {
	/**
	 * 日志id
	 */
	private String logid;
	/**
	 * 当前匹配类型
	 */
	private String matchtype;
	/**
	 * 当前操作数量
	 */
	private BigDecimal opernum;
	/**
	 * 自动匹配数量
	 */
	private BigDecimal matchnum;
	/**
	 * 备注
	 */
	private String remark;
	/**
	 * 创建人
	 */
	private String inUser;
	/**
	 * 创建时间
	 */
	private String inDate;
	/**
	 * 编辑人
	 */
	private String editUser;
	/**
	 * 编辑时间
	 */
	private String editDate;
}
