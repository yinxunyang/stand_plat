package com.bestvike.standplat;

import com.bestvike.bvdf.dao.BvdfHouseDao;
import com.bestvike.bvdf.param.BvdfCorpParam;
import com.bestvike.bvdf.service.impl.BvdfServiceImpl;
import com.bestvike.mid.dao.MidHouseDao;
import com.bestvike.mid.entity.MidHouseInfo;
import com.bestvike.mid.service.MidHouseService;
import com.bestvike.bvdf.param.BvdfHouseParam;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.WrapperQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: yinxunyang
 * @Description: 中间表单元测试类
 * @Date: 2019/9/9 17:06
 * @param:
 * @return:
 */
@Slf4j
public class MidTest extends BaseTest {
	@Autowired
	private MidHouseDao midHouseDao;
	@Autowired
	private BvdfServiceImpl bvdfServiceImpl;
	@Autowired
	private MidHouseService midHouseService;
	@Autowired
	private BvdfHouseDao bvdfHouseDao;
	/**
	 * es集群的名称
	 */
	@Value("${esConfig.esClusterName}")
	private String esClusterName;
	/**
	 * es的IP
	 */
	@Value("${esConfig.esIP}")
	private String esIP;
	/**
	 * es的esPort
	 */
	@Value("${esConfig.esPort}")
	private String esPort;


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
	@Test
	public void test20() {
		ClassPathResource classPathResource = new ClassPathResource("static/elasticSearch/elasticQuery.json");
		try {
			InputStream inputStream = classPathResource.getInputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
			StringBuffer sb = new StringBuffer();
			String line;
			while ((line=br.readLine())!=null){
				sb.append(line);
			}
			System.out.println(sb.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	@Test
	public void test21() {
		TransportClient client = null;
		try {
			client = new PreBuiltTransportClient(Settings.builder().put("cluster.name", esClusterName).build())
					.addTransportAddress(new TransportAddress(InetAddress.getByName(esIP), Integer.parseInt(esPort)));
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		ClassPathResource classPathResource = new ClassPathResource("static/elasticSearch/elasticQuery.json");
		try {
			InputStream inputStream = classPathResource.getInputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
			StringBuffer sb = new StringBuffer();
			String line;
			while ((line=br.readLine())!=null){
				sb.append(line);
			}
			System.out.println(sb.toString());
			WrapperQueryBuilder wqb = QueryBuilders.wrapperQuery(sb.toString());
			SearchResponse searchResponse = client.prepareSearch("house_index")
					.setTypes("house_type").setQuery(wqb).setSize(10).get();
			SearchHit[] hits = searchResponse.getHits().getHits();
			for(SearchHit hit : hits){
				String content = hit.getSourceAsString();
				// 房屋主键
				log.info(hit.getId());
				System.out.println(content);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
