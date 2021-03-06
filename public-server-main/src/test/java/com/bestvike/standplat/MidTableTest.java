package com.bestvike.standplat;

import com.bestvike.bvrfis.dao.BmatchAnResultDao;
import com.bestvike.dataCenter.dao.BvdfHouseDao;
import com.bestvike.dataCenter.param.BvdfHouseParam;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.net.InetAddress;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: yinxunyang
 * @Description: 中间表单元测试类
 * @Date: 2019/9/9 17:06
 * @param:
 * @return:
 */
public class MidTableTest extends BaseTest {

	@Autowired
	private BvdfHouseDao bvdfHouseDao;
	@Autowired
	private BmatchAnResultDao bmatchAnResultDao;

	@Test
	public void test2() {
		/*System.setProperty("es.set.netty.runtime.available.processors", "false");
		Map<String, Object> parameterMap = new HashMap<>();
		parameterMap.put("houseMaxNum", 20);
		List<BvdfHouseParam> bvdfHouseParamList = bvdfHouseDao.queryBvdfHouseInfo(parameterMap);
		System.out.println("bvdfHouseParamList:" + bvdfHouseParamList);*/
	}
	@Test
	public void esSearchTest() throws Exception {
		String index = "fangwu_index";
		String type = "fangwu_type";
		// 唯一编号
		String id = "2";
		System.setProperty("es.set.netty.runtime.available.processors", "false");
		Settings settings=Settings.builder().put("cluster.name", "docker-cluster").build();
		TransportClient client=new PreBuiltTransportClient(settings)
				.addTransportAddress(new TransportAddress(InetAddress.getByName("192.168.237.131"), 9300));
		GetResponse response=client.prepareGet(index, type,id).execute().actionGet();
		System.out.println(response.getSourceAsString());
		client.close();
	}

	@Test
	public void esAddTest() throws Exception {
		String index = "fangwu_index";
		String type = "fangwu_type";
		// 唯一编号
		String id = "10";
		System.setProperty("es.set.netty.runtime.available.processors", "false");
		Settings settings = Settings.builder().put("cluster.name", "docker-cluster").build();
		TransportClient client = new PreBuiltTransportClient(settings)
				.addTransportAddress(new TransportAddress(InetAddress.getByName("192.168.237.131"), 9300));
		XContentBuilder doc = XContentFactory.jsonBuilder()
				.startObject()
				.field("roomname", "801室")
				.field("certno", "370102199805210012")
				.field("address", "工业南路61号")
				.field("bldname", "5#楼")
				.field("cellname", "1单元")
				.field("regionname", "海信慧园")
				.field("name", "李四")
				.endObject();
		IndexResponse response = client.prepareIndex(index, type, id).setSource(doc).get();
		System.out.println("=============" + response.status());
	}
	@Test
	public void esAddTestaa() throws Exception {
		String index = "house_index";
		String type = "house_type";
		// 唯一编号
		String id = "11";
		System.setProperty("es.set.netty.runtime.available.processors", "false");
		Settings settings = Settings.builder().put("cluster.name", "docker-cluster").build();
		IndexResponse response;
		try (TransportClient client = new PreBuiltTransportClient(settings)
				.addTransportAddress(new TransportAddress(InetAddress.getByName("192.168.237.131"), 9300))) {
			XContentBuilder doc = XContentFactory.jsonBuilder()
					.startObject()
					.field("buycertnos", "370102199805210012")
					.field("regionname", "保利华庭")
					.field("bldname", "1幢")
					.field("cellname", "2单元")
					.field("floorname", "5楼")
					.field("roomno", "301室")
					.field("buynames", "李五")
					.field("houseAddress", "奥体中路88号")
					.endObject();
			response = client.prepareIndex(index, type, id).setSource(doc).get();
		}
		System.out.println("=============" + response.status());
	}
/*@Test
	public void test12(){
	BmatchAnResultInfo bmatchAnResultInfo = new BmatchAnResultInfo();
	bmatchAnResultInfo.setMatchid("12");
	bmatchAnResultInfo.setWqbusiid("12");
	bmatchAnResultInfo.setCenterid("12");
	bmatchAnResultInfo.setWxbusiid("12");
	bmatchAnResultInfo.setPercent(new BigDecimal("40.5"));
	bmatchAnResultInfo.setResult("12");
	bmatchAnResultInfo.setRelstate("12");
	bmatchAnResultInfo.setDescribe("12");
	bmatchAnResultInfo.setRemark("12");
	bmatchAnResultInfo.setInuser("12");
	bmatchAnResultInfo.setIndate("12");
	bmatchAnResultInfo.setMatchtype("12");
	bmatchAnResultInfo.setVersion(new BigDecimal("2"));
	bmatchAnResultDao.insertBmatch(bmatchAnResultInfo);

}*/

}
