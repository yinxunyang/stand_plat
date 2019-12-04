package com.bestvike.pub.controller;

import com.bestvike.portal.controller.BaseController;
import com.bestvike.pub.data.table.PQuestion;
import com.bestvike.pub.data.table.PSurvey;
import com.bestvike.pub.service.PQuestionService;
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
public class PQuestionController extends BaseController {

	@Autowired
	private PQuestionService pQuestionService;

	@ApiOperation(value = "查询问题库", notes = "根据输入查询条件查询")
	@GetMapping("/api/public/questions")
	public PageInfo<PQuestion> fetchQuestions(PQuestion pQuestion) {
		return pQuestionService.fetchQuestions(pQuestion);
	}

	@ApiOperation(value = "新增问题", notes = "通过传入的信息新增问题")
	@PostMapping("/api/public/questions")
	public PQuestion insert(@RequestBody PQuestion pQuestion) {
		String userIdAndUserName = getLoginUserId() + "_" + getLoginUser().getUserName();
		return pQuestionService.insert(pQuestion, userIdAndUserName);
	}

	@ApiOperation(value = "修改问题", notes = "通过传入的信息修改问题")
	@PutMapping("/api/public/questions")
	public PQuestion modify(@RequestBody PQuestion pQuestion) {
		return pQuestionService.modify(pQuestion);
	}

	@ApiOperation(value = "删除问题", notes = "根据id删除问题")
	@DeleteMapping("/api/public/questions/{ids}")
	public int remove(@PathVariable String ids) {
		return pQuestionService.remove(ids);
	}
}