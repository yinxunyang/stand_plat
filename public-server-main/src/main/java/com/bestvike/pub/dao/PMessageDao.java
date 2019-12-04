package com.bestvike.pub.dao;

import com.bestvike.pub.data.table.PMessage;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

@Repository
public interface PMessageDao extends Mapper<PMessage> {

}