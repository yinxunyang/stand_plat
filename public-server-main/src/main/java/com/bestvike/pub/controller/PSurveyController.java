package com.bestvike.pub.controller;

import com.bestvike.portal.controller.BaseController;
import com.bestvike.pub.data.table.PSurvey;
import com.bestvike.pub.data.table.PSurveyItem;
import com.bestvike.pub.service.PSurveyItemService;
import com.bestvike.pub.service.PSurveyService;
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
public class PSurveyController extends BaseController {

	@Autowired
	private PSurveyService pSurveyService;
	@Autowired
	private PSurveyItemService pSurveyItemService;

	@ApiOperation(value = "查询调查问卷", notes = "根据输入查询条件查询")
	@GetMapping("/api/public/surveys")
	public PageInfo<PSurvey> fetchSurveys(PSurvey pSurvey) {
		return pSurveyService.fetchSurveys(pSurvey);
	}

	@ApiOperation(value = "新增调查问卷", notes = "通过传入的信息新增调查问卷")
	@PostMapping("/api/public/surveys")
	public PSurvey insert(@RequestBody PSurvey pSurvey) {
		String userIdAndUserName = getLoginUserId() + "_" + getLoginUser().getUserName();
		return pSurveyService.insert(pSurvey, userIdAndUserName);
	}

	@ApiOperation(value = "修改调查问卷", notes = "通过传入的信息修改调查问卷")
	@PutMapping("/api/public/surveys")
	public PSurvey modify(@RequestBody PSurvey pSurvey) {
		return pSurveyService.modify(pSurvey);
	}

	@ApiOperation(value = "删除调查问卷", notes = "根据id删除调查问卷")
	@DeleteMapping("/api/public/surveys/{ids}")
	public int remove(@PathVariable String ids) {
		return pSurveyService.remove(ids);
	}

	@ApiOperation(value = "查询问卷题目列表", notes = "根据问卷编号查询问卷题目列表")
	@GetMapping("/api/public/surveyItems")
	public PageInfo<PSurveyItem> fetchSurveyItem(PSurveyItem pSurveyItem) {
		return pSurveyItemService.fetchSurveyItems(pSurveyItem);
	}

	@ApiOperation(value = "新增调查问卷题目", notes = "通过传入的信息新增调查问卷题目")
	@PostMapping("/api/public/surveyItems")
	public PSurveyItem insert(@RequestBody PSurveyItem pSurveyItem) {
		String userIdAndUserName = getLoginUserId() + "_" + getLoginUser().getUserName();
		return pSurveyItemService.insert(pSurveyItem, userIdAndUserName);
	}

	@ApiOperation(value = "修改调查问卷题目", notes = "通过传入的信息修改调查问卷题目")
	@PutMapping("/api/public/surveyItems")
	public PSurveyItem modify(@RequestBody PSurveyItem pSurveyItem) {
		return pSurveyItemService.modify(pSurveyItem);
	}

	@ApiOperation(value = "删除调查问卷题目", notes = "根据id删除调查问卷题目")
	@DeleteMapping("/api/public/surveyItems/{ids}")
	public int removeSurveyItems(@PathVariable String ids) {
		return pSurveyItemService.remove(ids);
	}

	@ApiOperation(value = "发布调查问卷", notes = "根据传入问卷信息发布调查问卷")
	@PutMapping("/api/public/publishSurvey")
	public PSurvey publish(@RequestBody PSurvey pSurvey) {
		return pSurveyService.publishSurvey(pSurvey);
	}
}