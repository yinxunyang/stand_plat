package com.bestvike.pub.controller;

import com.bestvike.portal.controller.BaseController;
import com.bestvike.pub.data.table.PPublish;
import com.bestvike.pub.service.PPublishService;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PPublishController extends BaseController {

	@Autowired
	private PPublishService pPublishService;

	@ApiOperation(value = "查询公开信息", notes = "根据输入公开信息进行查询")
	@GetMapping("/api/public/publishes")
	public PageInfo<PPublish> fetchPublishes(PPublish pPublish) {
		return pPublishService.fetchPublishes(pPublish);
	}

	@ApiOperation(value = "信息发布", notes = "通过传入的信息实现信息发布")
	@PostMapping("/api/public/publishes")
	public PPublish insert(@RequestBody PPublish pPublish) {
		String userIdAndUserName = getLoginUserId() + "_" + getLoginUser().getUserName();
		return pPublishService.insert(pPublish, userIdAndUserName);
	}

	@ApiOperation(value = "修改发布信息", notes = "通过新传进来的信息对象修改发布信息")
	@PutMapping("/api/public/publishes")
	public PPublish modify(@RequestBody PPublish pPublish) {
		return pPublishService.modify(pPublish);
	}

	@ApiOperation(value = "删除发布信息", notes = "根据id删除发布信息")
	@DeleteMapping("/api/public/publishes/{ids}")
	public int remove(@PathVariable String ids) {
		return pPublishService.remove(ids);
	}
}