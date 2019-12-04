package com.bestvike.pub.dao;

import com.bestvike.pub.data.table.PNote;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

@Repository
public interface PNoteDao extends Mapper<PNote> {

}