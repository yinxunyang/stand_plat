package com.bestvike.dataCenter.entity;

import com.bestvike.portal.data.BaseData;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import java.io.Serializable;

/**
 * @Author: yinxunyang
 * @Description: bvdfToEs的时间记录表
 * @Date: 2019/12/19 10:09
 */
@Entity
@Getter
@Setter
public class BvdfToEsRecordTime extends BaseData implements Serializable {
	private static final long serialVersionUID = -8071679620384686241L;
	/**
	 * 主键
	 */
	private String id;
	/**
	 * 最后执行时间
	 */
	private String lastExcuteTime;
	/**
	 * 类型
	 */
	private String matchType;
	/**
	 * 描述
	 */
	private String describe;
}
