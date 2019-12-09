package com.bestvike.pub.service.impl;

import com.bestvike.pub.enums.ReturnCode;
import com.bestvike.pub.exception.MsgException;
import com.bestvike.pub.param.BvdfHouseParam;
import com.bestvike.pub.service.BvdfHouseService;
import com.bestvike.pub.service.BvdfService;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class BvdfServiceImpl implements BvdfService {
	/**
	 * 房屋信息表(arc_houseinfo)最大查询条数,防止内存溢出
	 */
	private static final Integer HOUSE_MAX_NUM = 20000;
	@Autowired
	private BvdfHouseService bvdfHouseService;
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
		try {
			Map<String, Object> parameterMap = new HashMap<>();
			parameterMap.put("houseMaxNum", HOUSE_MAX_NUM);
			// 查询bvdf房屋的数据
			List<BvdfHouseParam> bvdfHouseParamList = bvdfHouseService.queryBvdfHouseInfo(parameterMap);
			if (bvdfHouseParamList.isEmpty()) {
				log.info("bvdf没有需要往elasticsearch迁移的房屋数据");
				return;
			}
			// 遍历新增房屋信息和迁移elasticsearch
			addCopyHouseAndEs(bvdfHouseParamList);
		} catch (Exception e) {
			log.error("定时任务：往ES迁移bvdf数据失败！" + e);
		}
	}

	/**
	 * @Author: yinxunyang
	 * @Description: 遍历新增房屋信息和迁移elasticsearch
	 * @Date: 2019/12/9 13:26
	 * @param:
	 * @return:
	 */
	private void addCopyHouseAndEs(List<BvdfHouseParam> bvdfHouseParamList) {
		try (TransportClient client = new PreBuiltTransportClient(Settings.builder().put("cluster.name", "docker-cluster").build())
				.addTransportAddress(new TransportAddress(InetAddress.getByName("192.168.237.132"), 9300))) {
			// 遍历新增房屋信息和elasticsearch
			bvdfHouseParamList.forEach(bvdfHouseParam -> {
				// 新增房屋信息和迁移elasticsearch
				try {
					bvdfHouseService.insertCopyHouseAndEs(bvdfHouseParam, client);
				} catch (MsgException e) {
					log.error(e + "bvdfHouseParam参数为：" + bvdfHouseParam);
				}
			});
		} catch (UnknownHostException e) {
			log.error("创建elasticsearch客户端连接失败" + e);
			throw new MsgException(ReturnCode.sdp_sys_error, "创建elasticsearch客户端连接失败");
		}
	}
}
