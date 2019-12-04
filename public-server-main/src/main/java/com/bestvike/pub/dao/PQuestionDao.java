package com.bestvike.pub.dao;

import com.bestvike.pub.data.table.PQuestion;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

@Repository
public interface PQuestionDao extends Mapper<PQuestion> {

}