package com.bestvike.standplat;

import com.bestvike.mid.dao.MidHouseDao;
import com.bestvike.pub.param.BvdfHouseParam;
import com.bestvike.pub.service.impl.BvdfServiceImpl;
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
	@Autowired
	private BvdfServiceImpl bvdfServiceImpl;


	@Test
	public void test12() {
		BvdfHouseParam midHouseInfo = new BvdfHouseParam();
		midHouseInfo.setSysguid("602628467");
		midHouseInfo.setBldno("123");
		midHouseInfo.setCellno("12");
		midHouseInfo.setFloorname("123");
		midHouseInfo.setBuycertnos("456");
		midHouseInfo.setRegionno("123");
		midHouseInfo.setRoomno("77");
				midHouseInfo.setHouseAddress("123");
		midHouseDao.insertBvdfHouseInfo(midHouseInfo);
	}
	@Test
	public void test13() {

		String ss = midHouseDao.queryArcBuildInfoById();
	}
	@Test
	public void test14(){
		// 定时任务
		bvdfServiceImpl.bvdfHouseToEs();
	}
}
