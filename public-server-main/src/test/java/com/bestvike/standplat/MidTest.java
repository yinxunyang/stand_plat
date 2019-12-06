package com.bestvike.standplat;

import com.bestvike.mid.dao.MidHouseDao;
import com.bestvike.mid.entity.MidHouseInfo;
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
public class MidTest extends BaseTest {
	@Autowired
	private MidHouseDao midHouseDao;


	@Test
	public void test12() {
		MidHouseInfo midHouseInfo = new MidHouseInfo();
		midHouseInfo.setBvdfhouseid("602628467");
		midHouseInfo.setBldname("123");
		midHouseDao.insertBvdfHouseInfo(midHouseInfo);
	}
	@Test
	public void test13() {

		String ss = midHouseDao.queryArcBuildInfoById();
	}
}
