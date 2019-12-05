package com.bestvike.pub.controller;

import com.bestvike.portal.controller.BaseController;
import com.bestvike.pub.param.BvdfHouseParam;
import com.bestvike.pub.service.BvdfService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class BvdfController extends BaseController {
	@Autowired
	BvdfService bvdfService;

	@ApiOperation(value = "查询房屋信息", notes = "查询房屋信息")
	@GetMapping("/api/bvdf/bvdfhouse")
	public List<BvdfHouseParam> queryBvdfHouseInfo() {
		return bvdfService.queryBvdfHouseInfo();
	}

}
