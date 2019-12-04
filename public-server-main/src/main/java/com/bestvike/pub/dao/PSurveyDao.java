package com.bestvike.pub.dao;

import com.bestvike.pub.data.table.PSurvey;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

@Repository
public interface PSurveyDao extends Mapper<PSurvey> {

}