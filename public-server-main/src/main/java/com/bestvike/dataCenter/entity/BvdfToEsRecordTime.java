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
	 * 公司信息的最后执行时间
	 */
	private String corpLastExcuteTime;
	/**
	 * 小区信息的最后执行时间
	 */
	private String regionLastExcuteTime;
	/**
	 * 楼幢信息的最后执行时间
	 */
	private String bldLastExcuteTime;
	/**
	 * 单元信息的最后执行时间
	 */
	private String cellLastExcuteTime;
	/**
	 * 楼层信息的最后执行时间
	 */
	private String floorLastExcuteTime;
	/**
	 * 房屋信息的最后执行时间
	 */
	private String houseLastExcuteTime;
}
