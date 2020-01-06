package com.bestvike.commons.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

 /**
   * @Author: yinxunyang
   * @Description: 企业类型--枚举类
   * @Date: 2020/1/6 15:11
   */
@Getter
@AllArgsConstructor
public enum CorpTypeEnum {
	 HOUSE_DEVELOPER       ("1",     "房地产开发企业"),
	 HOUSE_AGENCY          ("2",     "房地产经纪机构"),
	 HOUSE_ESTIMATE        ("3",     "房地产评估机构"),
	 PROPERTY_COMPANY      ("4",     "物业服务企业"),
	 HOUSE_MAPPING         ("5",     "房屋测绘机构"),
	 OWNER_COMMITTEE       ("6",     "业主委员会"),
	 SALE_AGENCY           ("98",    "代理销售机构"),
	 ORTHER_CORP           ("99",    "其他企业");

	 private String code;
	 private String desc;
}
