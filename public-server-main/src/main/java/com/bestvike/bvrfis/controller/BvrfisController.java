package com.bestvike.bvrfis.controller;

import com.bestvike.bvrfis.service.BvrfisService;
import com.bestvike.commons.exception.MsgException;
import com.bestvike.portal.controller.BaseController;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BvrfisController extends BaseController {
	@Autowired
	BvrfisService bvrfisService;

	@ApiOperation(value = "将bvrfis公司信息跟es中的匹配", notes = "将bvrfis公司信息跟es中的匹配")
	@GetMapping("/api/bvrfis/bvrfisCorpMatchEs")
	public void bvrfisCorpMatchEs() {
		try {
			bvrfisService.bvrfisCorpMatchEs();
		} catch (MsgException e) {
			logger.error("将bvrfis公司信息跟es中的匹配失败");
		}
	}
	@ApiOperation(value = "将bvrfis房屋信息跟es中的匹配", notes = "将bvrfis房屋信息跟es中的匹配")
	@GetMapping("/api/bvrfis/bvrfisHouseMatchEs")
	public void bvrfisHouseMatchEs() {
		try {
			bvrfisService.bvrfisHouseMatchEs();
		} catch (MsgException e) {
			logger.error("将bvrfis房屋信息跟es中的匹配失败");
		}
	}
}
