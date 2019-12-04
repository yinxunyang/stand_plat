package com.bestvike.pub.service;

import com.bestvike.pub.data.table.PSurvey;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

@Service
public interface PSurveyService {

	PageInfo<PSurvey> fetchSurveys(PSurvey pSurvey);

	PSurvey insert(PSurvey pSurvey, String userIdAndUserName);

	PSurvey modify(PSurvey pSurvey);

	int remove(String ids);

	PSurvey publishSurvey(PSurvey pSurvey);
}
