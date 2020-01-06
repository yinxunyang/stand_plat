package com.bestvike.elastic.service.impl;

import com.bestvike.commons.enums.EsStatusEnum;
import com.bestvike.commons.enums.ReturnCode;
import com.bestvike.commons.exception.MsgException;
import com.bestvike.dataCenter.param.BvdfHouseParam;
import com.bestvike.elastic.service.ElasticSearchService;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @Author: yinxunyang
 * @Description: elasticSearch实现类
 * @Date: 2019/12/6 16:57
 */
@Slf4j
@Service
public class ElasticSearchServiceImpl implements ElasticSearchService {
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
	/**
	 * @Author: yinxunyang
	 * @Description: 往elasticsearch迁移一条数据
	 * @Date: 2019/12/9 15:26
	 * @param:
	 * @return:
	 */
	@Override
	public void insertElasticSearch(TransportClient client, XContentBuilder doc, String index, String type, String id) throws MsgException {
		// es的返回状态
		String esStatus;
		IndexResponse response;
		try {
			// 往es新增数据及获取返回报文
			response = client.prepareIndex(index, type, id).setSource(doc).get();
			esStatus = response.status().toString();
			log.info("esStatus状态为：{}", esStatus);
		} catch (Exception e) {
			log.error("往ElasticSearch迁移失败" + e);
			throw new MsgException(ReturnCode.sdp_es_insert_fail, "往ElasticSearch迁移失败");
		}
		if (EsStatusEnum.ES_INSERTED.getCode().equals(esStatus)) {
			log.info("往ElasticSearch新增成功一条数据");
		} else if (EsStatusEnum.ES_UPDATED.getCode().equals(esStatus)) {
			log.info("往ElasticSearch更新成功一条数据");
		} else {
			log.error("往ElasticSearch迁移失败，返回状态不是CREATED或OK，response为：{}", response);
			throw new MsgException(ReturnCode.sdp_es_insert_fail, "往ElasticSearch迁移失败，返回状态不是CREATED或OK");
		}
	}


	/**
	 * @Author: yinxunyang
	 * @Description: 标准化处理跟es交互的数据
	 * @Date: 2019/12/12 17:25
	 * @param:
	 * @return:
	 */
	@Override
	public void bvdfHouseParamFormat(BvdfHouseParam bvdfHouseParam) {
		// 将#替换成号
		String bldName = bvdfHouseParam.getBldName().replaceAll("#", "号");
		bvdfHouseParam.setBldName(numberToChinese(bldName));
		String cellName = bvdfHouseParam.getCellName();
		bvdfHouseParam.setCellName(numberToChinese(cellName));
		String floorName = bvdfHouseParam.getFloorname();
		bvdfHouseParam.setFloorname(numberToChinese(floorName));
		String houseAddress = bvdfHouseParam.getAddress().replaceAll("#", "号");
		bvdfHouseParam.setAddress(numberToChinese(houseAddress));
	}

	/**
	 * @Author: yinxunyang
	 * @Description: 将字符串中的阿拉伯数字转成汉字
	 * @Date: 2019/12/12 18:44
	 * @param:
	 * @return:
	 */
	private String numberToChinese(String str) {
		for (int i = 0; i < 10; i++) {
			str = str.replace((char) ('0' + i), "零一二三四五六七八九".charAt(i));
		}
		return str;
	}

	/**
	 * @Author: yinxunyang
	 * @Description: 根据json文件组织查询Es的语句
	 * @Date: 2019/12/24 15:16
	 * @param:
	 * @return:
	 */
	@Override
	public String organizeQueryEsByJson(String jsonPath) {
		StringBuilder sb = new StringBuilder();
		try {
			ClassPathResource classPathResource = new ClassPathResource(jsonPath);
			InputStream inputStream = classPathResource.getInputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));

			String line;
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}
		} catch (Exception e) {
			log.error("根据json文件组织查询Es的语句异常" + e);
		}
		return sb.toString();
	}

	/**
	 * @Author: yinxunyang
	 * @Description: 创建es的客户端
	 * @Date: 2020/1/6 16:57
	 * @param:
	 * @return:
	 */
	@Override
	public TransportClient createEsClient() {
		try (TransportClient client = new PreBuiltTransportClient(Settings.builder().put("cluster.name", esClusterName).build())
				.addTransportAddress(new TransportAddress(InetAddress.getByName(esIP), Integer.parseInt(esPort)))) {
			return client;
		} catch (UnknownHostException e) {
			log.error("创建elasticsearch客户端连接失败", e);
			throw new MsgException(ReturnCode.sdp_sys_error, "创建elasticsearch客户端连接失败");
		}
	}
}
