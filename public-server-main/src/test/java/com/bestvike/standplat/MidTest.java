package com.bestvike.standplat;

import com.bestvike.bvdf.dao.BvdfHouseDao;
import com.bestvike.bvdf.param.BvdfCorpParam;
import com.bestvike.bvdf.service.impl.BvdfServiceImpl;
import com.bestvike.mid.dao.MidHouseDao;
import com.bestvike.mid.entity.MidHouseInfo;
import com.bestvike.mid.service.MidHouseService;
import com.bestvike.bvdf.param.BvdfHouseParam;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;

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
	@Autowired
	private BvdfHouseDao bvdfHouseDao;


	@Test
	public void test12() {
		BvdfHouseParam midHouseInfo = new BvdfHouseParam();
		midHouseInfo.setSysguid("602628467");
		midHouseInfo.setBldno("123");
		midHouseInfo.setCellno("12");
		midHouseInfo.setFloorname("123");
		midHouseInfo.setBuycertnos("456");
		midHouseInfo.setProjectno("123");
		midHouseInfo.setRoomno("77");
		midHouseInfo.setAddress("123");
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
		bvdfHouseParam.setProjectno("2");
		bvdfHouseParam.setBldno("2");
		bvdfHouseParam.setCellno("2");
		bvdfHouseParam.setBuynames("测试");
		bvdfHouseParam.setBuycertnos("测试");
		bvdfHouseParam.setAddress("测试");
		int ss  = midHouseService.updateBvdfHouseInfoById(bvdfHouseParam);
		int i = 0;
	}
	@Test
	public void test14(){
		// 定时任务
		bvdfServiceImpl.bvdfHouseToEs();
	}

	@Test
	public void test16() {
		BvdfCorpParam  bvdfCorpParam= bvdfHouseDao.selectCorpNameByCorpNo("3181041084");
		int i = 0;
	}
	@Test
	public void test17() {
		String bldName = bvdfHouseDao.selectBldNameByBldNo("602609797");
		int i = 0;
	}
	@Test
	public void test18() {
		Map<String, Object> parameterMap = new HashMap<>();
		parameterMap.put("cellNo", "002");
		parameterMap.put("bldNo", "600551057");
		String cellName = bvdfHouseDao.selectCellNameByCellNo(parameterMap);
		int i = 0;
	}
	@Test
	public void test19() {
		String ss = "1#楼";
		String sssdd = "2#楼5#单元8#";
		System.out.println(ss.replaceAll("#","号"));
		System.out.println(sssdd.replaceAll("#","号"));
		int i = 0;
	}

}
