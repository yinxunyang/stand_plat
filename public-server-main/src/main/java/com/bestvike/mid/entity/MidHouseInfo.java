package com.bestvike.mid.entity;

import com.bestvike.portal.data.BaseData;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "mid_houseinfo")
@Getter
@Setter
public class MidHouseInfo extends BaseData implements Serializable {

	private static final long serialVersionUID = 7324602608457006679L;
	/**
	 * 买受人身份证号
	 */
	private String buycertnos;
	/**
	 * 小区名称
	 */
	private String regionname;
	/**
	 * 楼幢名称
	 */
	private String bldname;
	/**
	 * 单元名称
	 */
	private String cellname;
	/**
	 * 楼层名称
	 */
	private String floorname;
	/**
	 * 室号
	 */
	private String roomno;
	/**
	 * 房屋买受人
	 */
	private String buynames;
	/**
	 * 房屋地址
	 */
	private String houseAddress;
}
