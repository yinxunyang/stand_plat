package com.bestvike.pub.dao;

import com.bestvike.pub.data.table.SysUser;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

@Repository
public interface SysUserDao extends Mapper<SysUser> {
	String selectId();
}
