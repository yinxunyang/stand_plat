package com.bestvike.bvrfis.service.impl;

import com.bestvike.bvrfis.dao.BvrfisHouseDao;
import com.bestvike.bvrfis.param.BvrfisBldParam;
import com.bestvike.bvrfis.param.BvrfisCorpInfoParam;
import com.bestvike.bvrfis.param.BvrfisHouseParam;
import com.bestvike.bvrfis.param.BvrfisOwnerInfoParam;
import com.bestvike.bvrfis.param.BvrfisShareOwnerInfoParam;
import com.bestvike.bvrfis.service.BvrfisCorpService;
import com.bestvike.bvrfis.service.BvrfisHouseService;
import com.bestvike.bvrfis.service.BvrfisService;
import com.bestvike.commons.enums.ReturnCode;
import com.bestvike.commons.exception.MsgException;
import com.bestvike.elastic.param.EsHouseParam;
import com.bestvike.elastic.service.ElasticSearchService;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.WrapperQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: yinxunyang
 * @Description: 维修资金的service实现类
 * @Date: 2019/12/11 17:06
 */
@Service
@Slf4j
public class BvrfisServiceImpl implements BvrfisService {

	/**
	 * 房屋信息表(arc_houseinfo)最大查询条数,防止内存溢出
	 */
	private static final Integer HOUSE_MAX_NUM = 20000;
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
	 * es开发企业的索引
	 */
	@Value("${esConfig.corpindex}")
	private String corpindex;
	/**
	 * es开发企业的映射
	 */
	@Value("${esConfig.corptype}")
	private String corptype;
	@Autowired
	private BvrfisHouseService bvrfisHouseService;
	@Autowired
	private BvrfisHouseDao bvrfisHouseDao;
	@Autowired
	private ElasticSearchService elasticSearchService;
	@Autowired
	private BvrfisCorpService bvrfisCorpService;

	/**
	 * @Author: yinxunyang
	 * @Description: 将bvrfis公司信息跟es中的匹配
	 * @Date: 2019/12/19 15:51
	 * @param:
	 * @return:
	 */
	@Override
	public void bvrfisCorpMatchEs() throws MsgException {
		BvrfisCorpInfoParam queryParam = new BvrfisCorpInfoParam();
		// 0正常
		queryParam.setState("0");
		// 开发企业
		queryParam.setCorptype("01");
		queryParam.setDatacenterid("isNUll");
		List<BvrfisCorpInfoParam> bvrfisCorpInfoParamList = bvrfisCorpService.queryBvrfisCorpInfo(queryParam);
		if (bvrfisCorpInfoParamList.isEmpty()) {
			log.info("bvrfis没有需要跟elasticsearch匹配的开发企业数据");
			return;
		}
		// 跟es匹配和跟开发企业建立关联关系
		matchEsAndRelationCorp(bvrfisCorpInfoParamList);
	}

	/**
	 * @Author: yinxunyang
	 * @Description: 跟es匹配和跟开发企业建立关联关系
	 * @Date: 2019/12/19 16:47
	 * @param:
	 * @return:
	 */
	private void matchEsAndRelationCorp(List<BvrfisCorpInfoParam> bvrfisCorpInfoParamList) {
		try (TransportClient client = new PreBuiltTransportClient(Settings.builder().put("cluster.name", esClusterName).build())
				.addTransportAddress(new TransportAddress(InetAddress.getByName(esIP), Integer.parseInt(esPort)))) {
			ClassPathResource classPathResource = new ClassPathResource("elasticSearch/uniqueMatchCorpQuery.json");
			// 遍历开发企业信息和elasticsearch
			bvrfisCorpInfoParamList.forEach(bvrfisCorpInfoParam -> {
				try {
					InputStream inputStream = classPathResource.getInputStream();
					BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
					StringBuffer sb = new StringBuffer();
					String line;
					while ((line = br.readLine()) != null) {
						sb.append(line);
					}
					String corpQueryParam = sb.toString().replace("certificateNoValue", bvrfisCorpInfoParam.getLicenseNo());
					log.info(corpQueryParam);
					WrapperQueryBuilder wqb = QueryBuilders.wrapperQuery(corpQueryParam);
					SearchResponse searchResponse = client.prepareSearch(corpindex)
							.setTypes(corptype).setQuery(wqb).setSize(1).get();
					SearchHit[] hits = searchResponse.getHits().getHits();
					for (SearchHit hit : hits) {
						String content = hit.getSourceAsString();
						// 房屋主键
						log.info(hit.getId());
						log.info(content);
					}
					// todo 替换es查询的值str.replace
					// todo 1 完全匹配
					// todo 2 疑似匹配


				} catch (MsgException e) {
					log.error(e + "bvrfisCorpInfoParam参数为：{}", bvrfisCorpInfoParam);
				} catch (IOException e) {
					e.printStackTrace();
				}
			});
		} catch (UnknownHostException e) {
			log.error("创建elasticsearch客户端连接失败" + e);
			throw new MsgException(ReturnCode.sdp_sys_error, "创建elasticsearch客户端连接失败");
		}
	}

	/**
	 * @Author: yinxunyang
	 * @Description: 将bvrfis房屋信息跟es中的匹配
	 * @Date: 2019/12/10 13:15
	 * @param:
	 * @return:
	 */
	@Override
	public void bvrfisHouseMatchEs() throws MsgException {
		Map<String, Object> parameterMap = new HashMap<>();
		parameterMap.put("houseMaxNum", HOUSE_MAX_NUM);
		List<BvrfisHouseParam> bvrfisHouseParamList = bvrfisHouseService.queryBvrfisHouseInfo(parameterMap);
		if (bvrfisHouseParamList.isEmpty()) {
			log.info("bvrfis没有需要跟elasticsearch匹配的房屋数据");
			return;
		}
		// 跟es匹配和往中间库新增数据
		matchEsAndInsertMid(bvrfisHouseParamList);
	}

	/**
	 * @Author: yinxunyang
	 * @Description: 跟es匹配和往中间库新增数据
	 * @Date: 2019/12/11 9:40
	 * @param:
	 * @return:
	 */
	private void matchEsAndInsertMid(List<BvrfisHouseParam> bvrfisHouseParamList) {
		try (TransportClient client = new PreBuiltTransportClient(Settings.builder().put("cluster.name", esClusterName).build())
				.addTransportAddress(new TransportAddress(InetAddress.getByName(esIP), Integer.parseInt(esPort)))) {
			// todo 从配置文件读取es查询语句
			// 遍历新增房屋信息和elasticsearch
			bvrfisHouseParamList.forEach(bvrfisHouseParam -> {
				// 新增房屋信息和迁移elasticsearch
				try {
					// 组织跟elasticSearch匹配的数据
					EsHouseParam esHouseParam = organizeMatchEsParam(bvrfisHouseParam);
					// 标准化处理跟es交互的数据
					elasticSearchService.bvdfHouseParamFormat(esHouseParam);
					// todo 替换es查询的值str.replace



				} catch (MsgException e) {
					log.error(e + "bvrfisHouseParam参数为：{}", bvrfisHouseParam);
				}
			});
		} catch (UnknownHostException e) {
			log.error("创建elasticsearch客户端连接失败" + e);
			throw new MsgException(ReturnCode.sdp_sys_error, "创建elasticsearch客户端连接失败");
		}
	}

	/**
	 * @Author: yinxunyang
	 * @Description: 组织跟elasticSearch匹配的数据
	 * @Date: 2019/12/11 9:43
	 * @param:
	 * @return:
	 */
	private EsHouseParam organizeMatchEsParam(BvrfisHouseParam bvrfisHouseParam) {
		EsHouseParam esHouseParam = new EsHouseParam();
		// 楼幢名称
		String bldName = null;
		// 开发企业编号
		String developNo = null;
		String bldNo = bvrfisHouseParam.getBldNo();
		if (!StringUtils.isEmpty(bldNo)) {
			BvrfisBldParam bvrfisBldParam = bvrfisHouseDao.queryBldInfoByBldNo(bldNo);
			if (null != bvrfisBldParam) {
				bldName = bvrfisBldParam.getBldName();
				developNo = bvrfisBldParam.getDevelopNo();
			}
		}
		if (StringUtils.isEmpty(bldName)) {
			bldName = "无";
		}
		esHouseParam.setBldName(bldName);
		String developName = null;
		String licenseNo = null;
		if (!StringUtils.isEmpty(developNo)) {
			BvrfisCorpInfoParam bvrfisCorpInfoParam = bvrfisHouseDao.selectDevelopNameByDevelopNo(developNo);
			if (null != bvrfisCorpInfoParam) {
				developName = bvrfisCorpInfoParam.getCorpName();
				licenseNo = bvrfisCorpInfoParam.getLicenseNo();
			}
		}
		if (StringUtils.isEmpty(developName)) {
			developName = "无";
		}
		if (StringUtils.isEmpty(licenseNo)) {
			licenseNo = "无";
		}
		esHouseParam.setDevelopName(developName);
		esHouseParam.setLicenseNo(licenseNo);
		String cellNo = bvrfisHouseParam.getCellNo();
		String cellName = null;
		String houseProp = bvrfisHouseParam.getHouseProp();
		if (!StringUtils.isEmpty(cellNo) && !StringUtils.isEmpty(bldNo)) {
			Map<String, Object> parameterMap = new HashMap<>();
			parameterMap.put("cellNo", cellNo);
			parameterMap.put("bldNo", bldNo);
			parameterMap.put("houseProp", houseProp);
			cellName = bvrfisHouseDao.selectCellNameByNo(parameterMap);
		}
		if (StringUtils.isEmpty(cellName)) {
			cellName = "无";
		}
		esHouseParam.setCellName(cellName);
		String floorName = null;
		String floorNo = bvrfisHouseParam.getFloorNo();
		if (!StringUtils.isEmpty(floorNo)) {
			Map<String, Object> parameterMap = new HashMap<>();
			parameterMap.put("floorNo", floorNo);
			parameterMap.put("bldNo", bldNo);
			parameterMap.put("houseProp", houseProp);
			floorName = bvrfisHouseDao.selectFloorNameByFloorNo(parameterMap);
		}
		if(StringUtils.isEmpty(floorName)){
			floorName = "无";
		}
		esHouseParam.setFloorName(floorName);
		esHouseParam.setRoomno(bvrfisHouseParam.getRoomNo());
		String houseGuid = bvrfisHouseParam.getSysGuid();
		StringBuilder buyCertNos = new StringBuilder();
		StringBuilder buyNames = new StringBuilder();
		if (!StringUtils.isEmpty(houseGuid)) {
			Map<String, Object> parameterMap = new HashMap<>();
			parameterMap.put("houseGuid", houseGuid);
			// 查询正常状态的业主
			parameterMap.put("state", "0");
			BvrfisOwnerInfoParam bvrfisOwnerInfoParam = bvrfisHouseDao.selectOwnerInfoByHouseId(parameterMap);
			String certNo = null;
			String ownerName = null;
			String subAccount = null;
			if (null != bvrfisOwnerInfoParam) {
				certNo = bvrfisOwnerInfoParam.getCertNo();
				ownerName = bvrfisOwnerInfoParam.getOwnerName();
				subAccount = bvrfisOwnerInfoParam.getSubAccount();
			}
			List<BvrfisShareOwnerInfoParam> bvrfisShareOwnerInfoList = new ArrayList<>();
			if (!StringUtils.isEmpty(subAccount)) {
				// 查询共有人
				bvrfisShareOwnerInfoList = bvrfisHouseDao.selectShareOwnerInfoByHouseId(subAccount);
			}
			if (!StringUtils.isEmpty(certNo)) {
				buyCertNos.append(certNo);
			}
			if (!StringUtils.isEmpty(ownerName)) {
				buyNames.append(ownerName);
			}
			if (!bvrfisShareOwnerInfoList.isEmpty()) {
				for (BvrfisShareOwnerInfoParam bvrfisShareOwnerInfoParam : bvrfisShareOwnerInfoList) {
					String shareCertNo = bvrfisShareOwnerInfoParam.getCertNo();
					if (!StringUtils.isEmpty(shareCertNo)) {
						// 英文状态下逗号
						buyCertNos.append(",").append(shareCertNo);
					}
					String shareOwnerName = bvrfisShareOwnerInfoParam.getOwnerName();
					if (!StringUtils.isEmpty(shareOwnerName)) {
						buyNames.append(",").append(shareOwnerName);
					}
				}
			}
		}
		if (StringUtils.isEmpty(buyCertNos.toString())) {
			buyCertNos.append("无");
		}
		if (StringUtils.isEmpty(buyNames.toString())) {
			buyNames.append("无");
		}
		esHouseParam.setBuyCertNos(buyCertNos.toString());
		esHouseParam.setBuyNames(buyNames.toString());
		esHouseParam.setHouseAddress(bvrfisHouseParam.getAddress());
		return esHouseParam;
	}
}