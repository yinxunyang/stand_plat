package com.bestvike.standplat;

import com.bestvike.bvrfis.dao.BvrfisHouseDao;
import com.bestvike.bvrfis.param.BvrfisBldParam;
import com.bestvike.bvrfis.param.BvrfisHouseParam;
import com.bestvike.bvrfis.service.BvrfisHouseService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

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
		String developName = bvrfisHouseDao.selectDevelopNameByDevelopNo("01000087");
		int i = 0;
	}


}
