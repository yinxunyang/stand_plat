package com.bestvike.bvrfis.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;

/**
 * @Author: yinxunyang
 * @Description: 挂接关系表
 * @Date: 2019/12/11 10:24
 * @param:
 * @return:
 */
@Entity
@Table(name = "b_dataRelation")
@Getter
@Setter
public class BDataRelation {
	/**
	 * 主键
	 */
	private String relationId;
	/**
	 * 类型
	 */
	private String linkType;
	/**
	 * 维修资金数据ID
	 */
	private String wxBusiId;
	/**
	 * 数据中心id
	 */
	private String centerId;
	/**
	 * 网签数据ID
	 */
	private String wqBusiId;
	/**
	 * 统一数据id
	 */
	private String busId;
	/**
	 * 版本号
	 */
	private BigDecimal version;
	/**
	 * 匹配id
	 */
	private String matchId;
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
