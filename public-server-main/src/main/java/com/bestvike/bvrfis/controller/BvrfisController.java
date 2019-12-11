package com.bestvike.bvrfis.controller;

import com.bestvike.bvdf.service.BvdfService;
import com.bestvike.portal.controller.BaseController;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BvrfisController extends BaseController {
	@Autowired
	BvdfService bvdfService;

	@ApiOperation(value = "将bvdf房屋信息迁移至elasticsearch", notes = "将bvdf房屋信息迁移至elasticsearch")
	@GetMapping("/api/bvrfis/bvrfisHouseMatchEs")
	public void bvdfHouseToEs() {
		bvdfService.bvdfHouseToEs();
	}

}
