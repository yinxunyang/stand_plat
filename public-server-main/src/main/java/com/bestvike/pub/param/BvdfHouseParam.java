package com.bestvike.pub.param;

/**
 * @Author: yinxunyang
 * @Description: bvdf的房屋信息参数类
 * @Date: 2019/12/5 11:08
 * @param:
 * @return:
 */
public class BvdfHouseParam {
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

	public String getBuycertnos() {
		return buycertnos;
	}

	public void setBuycertnos(String buycertnos) {
		this.buycertnos = buycertnos;
	}

	public String getRegionname() {
		return regionname;
	}

	public void setRegionname(String regionname) {
		this.regionname = regionname;
	}

	public String getBldname() {
		return bldname;
	}

	public void setBldname(String bldname) {
		this.bldname = bldname;
	}

	public String getCellname() {
		return cellname;
	}

	public void setCellname(String cellname) {
		this.cellname = cellname;
	}

	public String getFloorname() {
		return floorname;
	}

	public void setFloorname(String floorname) {
		this.floorname = floorname;
	}

	public String getRoomno() {
		return roomno;
	}

	public void setRoomno(String roomno) {
		this.roomno = roomno;
	}

	public String getBuynames() {
		return buynames;
	}

	public void setBuynames(String buynames) {
		this.buynames = buynames;
	}

	public String getHouseAddress() {
		return houseAddress;
	}

	public void setHouseAddress(String houseAddress) {
		this.houseAddress = houseAddress;
	}
}
