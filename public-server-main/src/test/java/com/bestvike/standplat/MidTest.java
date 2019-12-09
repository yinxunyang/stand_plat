package com.bestvike.standplat;

import com.bestvike.mid.dao.MidHouseDao;
import com.bestvike.mid.entity.MidHouseInfo;
import com.bestvike.mid.service.MidHouseService;
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
	@Autowired
	private MidHouseService midHouseService;


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
		BvdfHouseParam bvdfHouseParam = new BvdfHouseParam();
		bvdfHouseParam.setSysguid("42f089a6-18dd-4756-9c31-7aa6ed7210b6");
		MidHouseInfo midHouseInfo = midHouseService.queryMidHouseInfoById(bvdfHouseParam);
		int i = 0;
	}
	@Test
	public void test15() {
		BvdfHouseParam bvdfHouseParam = new BvdfHouseParam();
		bvdfHouseParam.setSysguid("42f089a6-18dd-4756-9c31-7aa6ed7210b6");
		bvdfHouseParam.setFloorname("测试");
		bvdfHouseParam.setRoomno("1");
		bvdfHouseParam.setRegionno("2");
		bvdfHouseParam.setBldno("2");
		bvdfHouseParam.setCellno("2");
		bvdfHouseParam.setBuynames("测试");
		bvdfHouseParam.setBuycertnos("测试");
		bvdfHouseParam.setHouseAddress("测试");
		int ss  = midHouseService.updateBvdfHouseInfoById(bvdfHouseParam);
		int i = 0;
	}
	@Test
	public void test14(){
		// 定时任务
		bvdfServiceImpl.bvdfHouseToEs();
	}
}
