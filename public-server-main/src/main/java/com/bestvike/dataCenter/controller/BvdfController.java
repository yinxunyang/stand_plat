package com.bestvike.dataCenter.controller;

import com.bestvike.dataCenter.biz.BvdfHouseBiz;
import com.bestvike.portal.controller.BaseController;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BvdfController extends BaseController {
	@Autowired
	BvdfHouseBiz bvdfHouseBiz;

	@ApiOperation(value = "将bvdf房屋信息迁移至elasticsearch", notes = "将bvdf房屋信息迁移至elasticsearch")
	@GetMapping("/api/bvdf/bvdfhouseToEs")
	public void bvdfHouseToEs() {
		bvdfHouseBiz.bvdfHouseToEs();
	}

}
