package com.bestvike.standplat;

import com.bestvike.pub.dao.ArcBuildInfoMapper;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Author: yinxunyang
 * @Description: 中间表单元测试类
 * @Date: 2019/9/9 17:06
 * @param:
 * @return:
 */
public class MidTableTest extends BaseTest {

	@Autowired
	private ArcBuildInfoMapper arcBuildInfoDao;


	@Test
	public void test10() {
		String ss = arcBuildInfoDao.queryArcBuildInfoById();
		System.out.println("ss:" + ss);
	}

}
