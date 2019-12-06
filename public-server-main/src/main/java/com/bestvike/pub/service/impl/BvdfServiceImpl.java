package com.bestvike.pub.service.impl;

import com.bestvike.mid.dao.MidHouseDao;
import com.bestvike.portal.service.BaseService;
import com.bestvike.pub.dao.BvdfHouseDao;
import com.bestvike.pub.param.BvdfHouseParam;
import com.bestvike.pub.service.BvdfService;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class BvdfServiceImpl extends BaseService implements BvdfService {
	/**
	 * 房屋信息表(arc_houseinfo)最大查询条数
	 */
	public static final Integer HOUSE_MAX_NUM = 20;
	@Autowired
	private BvdfHouseDao bvdfHouseDao;
	private final TransportClient client = new PreBuiltTransportClient(Settings.builder().put("cluster.name", "docker-cluster").build())
			.addTransportAddress(new TransportAddress(InetAddress.getByName("192.168.237.131"), 9300));

	public BvdfServiceImpl() throws UnknownHostException {
	}
	@Autowired
	private MidHouseDao midHouseDao;
	/**
	 * @Author: yinxunyang
	 * @Description: 将bvdf房屋信息迁移至elasticsearch
	 * 定时5分钟一次, 开局自动执行
	 * @Date: 2019/12/5 13:41
	 * @param:
	 * @return:
	 */
	@Override
	@Scheduled(fixedRate = 1000 * 60 * 5)
	public void bvdfHouseToEs() {
		Map<String, Object> parameterMap = new HashMap<>();
		parameterMap.put("houseMaxNum", HOUSE_MAX_NUM);
		List<BvdfHouseParam> bvdfHouseParamList = bvdfHouseDao.queryBvdfHouseInfo(parameterMap);
		if (bvdfHouseParamList.isEmpty()) {
			log.info("bvdf没有需要往elasticsearch迁移的数据");
			return;
		}
		// todo 往es数据库迁移数据和其他用户传数据
		String index = "house_index";
		String type = "house_type";
		bvdfHouseParamList.stream().forEach(bvdfHouseParam -> {
			// 唯一编号
			String id = bvdfHouseParam.getSysguid();
			XContentBuilder doc = null;
			try {
				doc = XContentFactory.jsonBuilder()
						.startObject()
						.field("buycertnos", bvdfHouseParam.getBuycertnos())
						.field("regionno", bvdfHouseParam.getRegionno())
						.field("bldno", bvdfHouseParam.getBldno())
						.field("cellno", bvdfHouseParam.getCellno())
						.field("floorname", bvdfHouseParam.getFloorname())
						.field("roomno", bvdfHouseParam.getRoomno())
						.field("buynames", bvdfHouseParam.getBuynames())
						.field("houseAddress", bvdfHouseParam.getHouseAddress())
						.endObject();
			} catch (IOException e) {
				log.error("拼装elasticsearch的新增数据失败" + e);
			}
			IndexResponse response = client.prepareIndex(index, type, id).setSource(doc).get();
			log.info("=============" + response.status());
		});
	}
}
