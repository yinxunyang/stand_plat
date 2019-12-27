package com.bestvike.commons.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author: yinxunyang
 * @Description: bvdfToEs的时间记录表--枚举类
 * @Date: 2019/12/9 16:22
 */
@Getter
@AllArgsConstructor
public enum RecordTimeEnum {
	BVDF_CORP_ID       ("bvdfCorp",         "bvdf开发企业的ID"),
	BVDF_REGION_ID     ("bvdfRegion",       "bvdf小区信息的ID"),
	BVDF_BLD_ID        ("bvdfBld",          "bvdf自然幢的ID"),
	BVDF_HOUSE_ID      ("bvdfHouse",        "bvdf房屋信息的ID"),
	LAST_EXCUTE_TIME   ("lastExcuteTime",   "最后执行时间");

	private String code;
	private String desc;
}
