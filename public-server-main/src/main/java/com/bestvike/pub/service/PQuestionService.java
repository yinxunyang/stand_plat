package com.bestvike.pub.service;

import com.bestvike.pub.data.table.PQuestion;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

@Service
public interface PQuestionService {

	PageInfo<PQuestion> fetchQuestions(PQuestion pQuestion);

	PQuestion insert(PQuestion pQuestion, String userIdAndUserName);

	PQuestion modify(PQuestion pQuestion);

	int remove(String ids);
}
