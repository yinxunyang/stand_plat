package com.bestvike.pub.dao;

import com.bestvike.pub.data.table.PSurveyItem;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

@Repository
public interface PSurveyItemDao extends Mapper<PSurveyItem> {

}