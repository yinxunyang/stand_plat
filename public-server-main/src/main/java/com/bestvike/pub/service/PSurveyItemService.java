package com.bestvike.pub.service;

import com.bestvike.pub.data.table.PSurveyItem;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

@Service
public interface PSurveyItemService {

	PageInfo<PSurveyItem> fetchSurveyItems(PSurveyItem pSurveyItem);

	PSurveyItem insert(PSurveyItem pSurveyItem, String userIdAndUserName);

	PSurveyItem modify(PSurveyItem pSurveyItem);

	int remove(String ids);
}
