package com.bestvike.standplat;

import com.bestvike.pub.dao.ArcBuildInfoMapper;
import com.bestvike.pub.dao.BvdfHouseDao;
import com.bestvike.pub.param.BvdfHouseParam;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

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
	@Autowired
	private BvdfHouseDao bvdfHouseDao;


	@Test
	public void test1() {
		String ss = arcBuildInfoDao.queryArcBuildInfoById();
		System.out.println("ss:" + ss);
	}

	@Test
	public void test2() {
		List<BvdfHouseParam> bvdfHouseParamList = bvdfHouseDao.queryBvdfHouseInfo();
		System.out.println("bvdfHouseParamList:" + bvdfHouseParamList);
	}
}
