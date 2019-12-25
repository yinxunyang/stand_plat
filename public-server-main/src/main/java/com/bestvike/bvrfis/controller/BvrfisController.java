package com.bestvike.bvrfis.controller;

import com.bestvike.bvrfis.biz.BDataRelationBiz;
import com.bestvike.bvrfis.biz.BvrfisCorpBiz;
import com.bestvike.bvrfis.biz.BvrfisRegionBiz;
import com.bestvike.bvrfis.service.BvrfisService;
import com.bestvike.commons.enums.MatchTypeEnum;
import com.bestvike.commons.exception.MsgException;
import com.bestvike.portal.controller.BaseController;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
public class BvrfisController extends BaseController {
	@Autowired
	private BvrfisCorpBiz bvrfisCorpBiz;
	@Autowired
	private BvrfisService bvrfisService;
	@Autowired
	private BDataRelationBiz bDataRelationBiz;
	@Autowired
	private BvrfisRegionBiz bvrfisRegionBiz;

	@ApiOperation(value = "将bvrfis信息跟es中的匹配", notes = "将bvrfis信息跟es中的匹配")
	@GetMapping("/api/bvrfis/bvrfisMatchEs")
	public void bvrfisMatchEs(HttpSession httpSession) {
		try {
			// 匹配公司信息
			bvrfisCorpBiz.bvrfisCorpMatchEs(httpSession);
			// 匹配小区信息
			bvrfisRegionBiz.bvrfisRegionMatchEs(httpSession);
			// TODO 匹配自然幢信息

			// TODO 匹配房屋信息
		} catch (MsgException e) {
			logger.error("将bvrfis信息跟es中的匹配失败");
		}
	}

	@ApiOperation(value = "将bvrfis公司信息跟es中的匹配", notes = "将bvrfis公司信息跟es中的匹配")
	@GetMapping("/api/bvrfis/bvrfisCorpMatchEs")
	public void bvrfisCorpMatchEs(HttpSession httpSession) {
		try {
			bvrfisCorpBiz.bvrfisCorpMatchEs(httpSession);
		} catch (MsgException e) {
			logger.error("将bvrfis公司信息跟es中的匹配失败");
		}
	}

	@ApiOperation(value = "将bvrfis小区信息跟es中的匹配", notes = "将bvrfis小区信息跟es中的匹配")
	@GetMapping("/api/bvrfis/bvrfisRegionMatchEs")
	public void bvrfisRegionMatchEs(HttpSession httpSession) {
		try {
			bvrfisRegionBiz.bvrfisRegionMatchEs(httpSession);
		} catch (MsgException e) {
			logger.error("将bvrfis小区信息跟es中的匹配失败");
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

	@ApiOperation(value = "建立关联关系", notes = "建立关联关系")
	@GetMapping("/api/bvrfis/generateRelation")
	public void generateRelation() {
		try {
			String matchType = MatchTypeEnum.DEVELOP.getCode();
			String matchId = "123";
			bDataRelationBiz.generateRelation(matchType, matchId);
		} catch (MsgException e) {
			logger.error("建立关联关系失败");
		}
	}
}
