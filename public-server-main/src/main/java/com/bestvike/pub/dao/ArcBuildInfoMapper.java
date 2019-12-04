package com.bestvike.pub.dao;

import com.bestvike.pub.data.table.ArcBuildInfo;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

@Repository
public interface ArcBuildInfoMapper extends Mapper<ArcBuildInfo> {

	String queryArcBuildInfoById();
}
