package com.bestvike.bvrfis.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;

/**
 * @Author: yinxunyang
 * @Description: 匹配分析表
 * @Date: 2019/12/11 10:24
 * @param:
 * @return:
 */
@Entity
@Table(name = "b_matchAnResult")
@Getter
@Setter
public class BmatchAnResultInfo {
	/**
	 * 主键
	 */
	private String matchid;
	/**
	 * 日志ID
	 */
	private String logid;
	/**
	 * 维修资金数据ID
	 */
	private String wxbusiid;
	/**
	 * 数据中心id
	 */
	private String centerid;
	/**
	 * 网签数据ID
	 */
	private String wqbusiid;
	/**
	 * 该条数据匹配率
	 */
	private BigDecimal percent;
	/**
	 * 匹配情况分析
	 */
	private String result;
	/**
	 * 匹配状态
	 */
	private String relstate;
	/**
	 * 匹配情况说明
	 */
	private String describe;
	/**
	 * 备注
	 */
	private String remark;
	/**
	 * 创建人
	 */
	private String inuser;
	/**
	 * 创建时间
	 */
	private String indate;
	/**
	 * 编辑人
	 */
	private String edituser;
	/**
	 * 编辑时间
	 */
	private String editdate;
	/**
	 * 匹配分析类型
	 */
	private String matchtype;
	/**
	 * 版本号
	 */
	private BigDecimal version;
}
