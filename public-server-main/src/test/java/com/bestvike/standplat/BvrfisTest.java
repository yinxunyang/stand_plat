package com.bestvike.standplat;

import com.bestvike.bvrfis.dao.BvrfisHouseDao;
import com.bestvike.bvrfis.param.BvrfisBldParam;
import com.bestvike.bvrfis.param.BvrfisCorpInfoParam;
import com.bestvike.bvrfis.param.BvrfisHouseParam;
import com.bestvike.bvrfis.param.BvrfisOwnerInfoParam;
import com.bestvike.bvrfis.param.BvrfisShareOwnerInfoParam;
import com.bestvike.bvrfis.service.BvrfisHouseService;
import com.bestvike.bvrfis.service.BvrfisService;
import com.bestvike.commons.utils.UtilTool;
import com.bestvike.dataCenter.param.BvdfCorpParam;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionContext;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: yinxunyang
 * @Description: 维修资金表单元测试类
 * @Date: 2019/9/9 17:06
 * @param:
 * @return:
 */
public class BvrfisTest extends BaseTest {
	@Autowired
	private BvrfisHouseService bvrfisHouseService;
	@Autowired
	private BvrfisHouseDao bvrfisHouseDao;
	@Autowired
	private BvrfisService bvrfisService;

	@Test
	public void test1() {
		Map<String, Object> parameterMap = new HashMap<>();
		parameterMap.put("houseMaxNum", "20");
		List<BvrfisHouseParam> list = bvrfisHouseService.queryBvrfisHouseInfo(parameterMap);
		int i = 0;
	}

	@Test
	public void test2() {
		BvrfisBldParam bvrfisBldParam = bvrfisHouseDao.queryBldInfoByBldNo("04002582");
		int i = 0;
	}
	@Test
	public void test3() {
		BvrfisCorpInfoParam bvrfisCorpInfoParam = bvrfisHouseDao.selectDevelopNameByDevelopNo("01000087");
		int i = 0;
	}
	@Test
	public void test4() {
		Map<String, Object> parameterMap = new HashMap<>();
		parameterMap.put("cellNo", "003");
		parameterMap.put("bldNo", "04002256");
		parameterMap.put("houseProp", "01");
		String cellName = bvrfisHouseDao.selectCellNameByNo(parameterMap);
		int i = 0;
	}
	@Test
	public void test5() {
		Map<String, Object> parameterMap = new HashMap<>();
		parameterMap.put("floorNo", "014");
		parameterMap.put("bldNo", "04001653");
		parameterMap.put("houseProp", "01");
		String floorName = bvrfisHouseDao.selectFloorNameByFloorNo(parameterMap);
		int i = 0;
	}
	@Test
	public void test6() {
		Map<String, Object> parameterMap = new HashMap<>();
		parameterMap.put("houseGuid", "2afe07b2-0c24-4a5d-9345-8df345790b17");
		// 查询正常状态的业主
		parameterMap.put("state", "0");
		BvrfisOwnerInfoParam bvrfisOwnerInfoParam = bvrfisHouseDao.selectOwnerInfoByHouseId(parameterMap);
		int i = 0;
	}
	@Test
	public void test7() {
		// 查询共有人
		List<BvrfisShareOwnerInfoParam> bvrfisShareOwnerInfoList = bvrfisHouseDao.selectShareOwnerInfoByHouseId("190814001375");
		int i = 0;
	}
	@Test
	public void test8(){
		HttpSession httpSession = new HttpSession() {
			@Override
			public long getCreationTime() {
				return 0;
			}

			@Override
			public String getId() {
				return null;
			}

			@Override
			public long getLastAccessedTime() {
				return 0;
			}

			@Override
			public ServletContext getServletContext() {
				return null;
			}

			@Override
			public void setMaxInactiveInterval(int interval) {

			}

			@Override
			public int getMaxInactiveInterval() {
				return 0;
			}

			@Override
			public HttpSessionContext getSessionContext() {
				return null;
			}

			@Override
			public Object getAttribute(String name) {
				return null;
			}

			@Override
			public Object getValue(String name) {
				return null;
			}

			@Override
			public Enumeration<String> getAttributeNames() {
				return null;
			}

			@Override
			public String[] getValueNames() {
				return new String[0];
			}

			@Override
			public void setAttribute(String name, Object value) {

			}

			@Override
			public void putValue(String name, Object value) {

			}

			@Override
			public void removeAttribute(String name) {

			}

			@Override
			public void removeValue(String name) {

			}

			@Override
			public void invalidate() {

			}

			@Override
			public boolean isNew() {
				return false;
			}
		};

		 bvrfisService.bvrfisCorpMatchEs(httpSession);
	}

	@Test
	public void test9() {
		String json = "{\"corpId\":\"BVDF109569\",\"corpName\":\"乌苏市塞维斯房地产开发有限公司\",\"certificateNo\":\"654202050009030\"}";
		BvdfCorpParam bvdfCorpParam = (BvdfCorpParam) UtilTool.jsonToObj(json, BvdfCorpParam.class);
		int i = 0;

	}

}
