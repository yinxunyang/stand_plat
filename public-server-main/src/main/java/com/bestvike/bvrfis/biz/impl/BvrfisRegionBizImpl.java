package com.bestvike.bvrfis.biz.impl;

import com.bestvike.bvrfis.biz.BvrfisRegionBiz;
import com.bestvike.bvrfis.entity.BmatchAnResultInfo;
import com.bestvike.bvrfis.param.BvrfisRegionParam;
import com.bestvike.bvrfis.service.BmatchAnResultService;
import com.bestvike.bvrfis.service.BvrfisRegionService;
import com.bestvike.commons.enums.MatchTypeEnum;
import com.bestvike.commons.enums.RelStateEnum;
import com.bestvike.commons.enums.ReturnCode;
import com.bestvike.commons.exception.MsgException;
import com.bestvike.commons.utils.UtilTool;
import com.bestvike.dataCenter.param.BvdfRegionParam;
import com.bestvike.dataCenter.service.BvdfRegionService;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: yinxunyang
 * @Description: 维修资金小区信息的biz实现类
 * @Date: 2019/12/10 17:06
 */
@Service
@Slf4j
public class BvrfisRegionBizImpl implements BvrfisRegionBiz {
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
	 * es小区的索引
	 */
	@Value("${esConfig.regionindex}")
	private String regionindex;
	/**
	 * es小区的映射
	 */
	@Value("${esConfig.regiontype}")
	private String regiontype;
	/**
	 * 疑似匹配的条数
	 */
	@Value("${standplatConfig.unCertainSize}")
	private String unCertainSize;
	@Autowired
	private BvrfisRegionService bvrfisRegionService;
	@Autowired
	private BmatchAnResultService bmatchAnResultService;
	@Autowired
	private BvdfRegionService bvdfRegionService;

	/**
	 * @Author: yinxunyang
	 * @Description: 将bvrfis小区信息跟es中的匹配
	 * @Date: 2019/12/19 15:51
	 * @param:
	 * @return:
	 */
	@Override
	public void bvrfisRegionMatchEs(HttpSession httpSession) throws MsgException {
		BvrfisRegionParam queryParam = new BvrfisRegionParam();
		// 0正常
		queryParam.setState("0");
		List<BvrfisRegionParam> bvrfisRegionParamList = bvrfisRegionService.queryBvrfisRegionInfo(queryParam);
		if (bvrfisRegionParamList.isEmpty()) {
			log.info("bvrfis没有需要跟elasticsearch匹配的小区数据");
			return;
		}
		// 完全匹配
		uniqueMatchCorp(bvrfisRegionParamList, httpSession);
		try (TransportClient client = new PreBuiltTransportClient(Settings.builder().put("cluster.name", esClusterName).build())
				.addTransportAddress(new TransportAddress(InetAddress.getByName(esIP), Integer.parseInt(esPort)))) {
			// 开发企业根据组织机构代码完全匹配
			//uniqueMatchCorp(bvrfisCorpInfoParamList, client, httpSession);
		} catch (UnknownHostException e) {
			log.error("创建elasticsearch客户端连接失败" + e);
			throw new MsgException(ReturnCode.sdp_sys_error, "创建elasticsearch客户端连接失败");
		}
	}

	/**
	 * @Author: yinxunyang
	 * @Description: 小区完全匹配
	 * @Date: 2019/12/19 19:15
	 * @param:
	 * @return:
	 */
	private void uniqueMatchCorp(List<BvrfisRegionParam> bvrfisRegionParamList, HttpSession httpSession) {
		// 匹配成功后需要从bvrfisRegionParamList移除的List
		List<BvrfisRegionParam> paramListForDel = new ArrayList<>();
		// 遍历开发企业信息和elasticsearch
		bvrfisRegionParamList.forEach(bvrfisRegionParam -> {
			try {
				// 查询bvdf的小区信息
				BvdfRegionParam queryParam = new BvdfRegionParam();
				queryParam.setCorpNo("BVDF" + bvrfisRegionParam.getCorpNo());
				queryParam.setRegionNo("BVDF" + bvrfisRegionParam.getRegionNo());
				//queryParam.setDivisionCode(bvrfisRegionParam.getDivisionCode());
				//queryParam.setFloorArea(bvrfisRegionParam.getFloorArea());
				List<BvdfRegionParam> bvdfRegionParamList = bvdfRegionService.queryBvdfRegionInfo(queryParam);
				if (bvdfRegionParamList.isEmpty() || bvdfRegionParamList.size() != 1) {
					return;
				}
				BvdfRegionParam bvdfRegionParam = bvdfRegionParamList.get(0);
				BmatchAnResultInfo bmatchAnResultInfo = new BmatchAnResultInfo();
				bmatchAnResultInfo.setMatchid(UtilTool.UUID());
				// todo 待确定
				bmatchAnResultInfo.setLogid(null);
				// 维修资金数据ID
				bmatchAnResultInfo.setWxbusiid(bvrfisRegionParam.getRegionNo());
				bmatchAnResultInfo.setCenterid(bvdfRegionParam.getDataCenterId());
				bmatchAnResultInfo.setWqbusiid(bvdfRegionParam.getRegionNo());
				// 该条数据匹配率 完全匹配是100
				bmatchAnResultInfo.setPercent(new BigDecimal("100.00"));
				// 匹配情况分析
				bmatchAnResultInfo.setResult("匹配度高");
				// 匹配状态 匹配成功
				bmatchAnResultInfo.setRelstate(RelStateEnum.MATCH_SUCCESS.getCode());
				// 匹配情况说明
				bmatchAnResultInfo.setDescribe(null);
				// 备注
				bmatchAnResultInfo.setRemark("小区信息完全匹配");
				// 单位信息表
				bmatchAnResultInfo.setMatchtype(MatchTypeEnum.REGION.getCode());
				// todo 创建人 待确定
				bmatchAnResultInfo.setInuser("无");
				//bmatchAnResultInfo.setInuser(httpSession.getAttribute(GCC.SESSION_KEY_USERNAME).toString());
				bmatchAnResultInfo.setIndate(UtilTool.nowTime());
				// 修改人
				bmatchAnResultInfo.setEdituser(null);
				bmatchAnResultInfo.setEditdate(null);
				bmatchAnResultInfo.setVersion(new BigDecimal(bvdfRegionParam.getVersionnumber()));
				bmatchAnResultService.insertBmatchAnResult(bmatchAnResultInfo);
				paramListForDel.add(bvrfisRegionParam);
			} catch (MsgException e) {
				log.error(e + "bvrfisRegionParam参数为：{}", bvrfisRegionParam);
			}
		});
		bvrfisRegionParamList.removeAll(paramListForDel);
	}
	/**
	 * @Author: yinxunyang
	 * @Description: 开发企业根据组织机构代码完全匹配
	 * @Date: 2019/12/19 19:15
	 * @param:
	 * @return:
	 *//*
	private void uniqueMatchCorp(List<BvrfisCorpInfoParam> bvrfisCorpInfoParamList, TransportClient client, HttpSession httpSession) {
		// 完全匹配开发企业信息
		ClassPathResource classPathResource = new ClassPathResource("elasticSearch/uniqueMatchCorpQuery.json");
		// 匹配成功后需要从bvrfisCorpInfoParamList移除的List
		List<BvrfisCorpInfoParam> paramListForDelByCertNo = new ArrayList<>();
		// 遍历开发企业信息和elasticsearch
		bvrfisCorpInfoParamList.forEach(bvrfisCorpInfoParam -> {
			try {
				InputStream inputStream = classPathResource.getInputStream();
				BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
				StringBuilder sb = new StringBuilder();
				String line;
				while ((line = br.readLine()) != null) {
					sb.append(line);
				}
				String licenseNo = bvrfisCorpInfoParam.getLicenseNo();
				// licenseNo如果为空，跳过该笔查询
				if (StringUtils.isEmpty(licenseNo)) {
					return;
				}
				String corpQueryParam = sb.toString().replace("certificateNoValue", licenseNo);
				log.info(corpQueryParam);
				WrapperQueryBuilder wqb = QueryBuilders.wrapperQuery(corpQueryParam);
				SearchResponse searchResponse = client.prepareSearch(regionindex)
						.setTypes(regiontype).setQuery(wqb).get();
				SearchHit[] hits = searchResponse.getHits().getHits();
				// 如果返回唯一一条数据，说明完全匹配
				if (hits.length == 1) {
					for (SearchHit hit : hits) {
						// 返回内容
						String bvdfCorpJson = hit.getSourceAsString();
						BvdfCorpParam bvdfCorpParam = (BvdfCorpParam) UtilTool.jsonToObj(bvdfCorpJson, BvdfCorpParam.class);
						BmatchAnResultInfo bmatchAnResultInfo = new BmatchAnResultInfo();
						bmatchAnResultInfo.setMatchid(UtilTool.UUID());
						// todo 待确定
						bmatchAnResultInfo.setLogid(null);
						// 维修资金数据ID
						bmatchAnResultInfo.setWxbusiid(bvrfisCorpInfoParam.getCorpNo());
						bmatchAnResultInfo.setCenterid(bvdfCorpParam.getDataCenterId());
						bmatchAnResultInfo.setWqbusiid(bvdfCorpParam.getCorpId());
						// 该条数据匹配率 完全匹配是100
						bmatchAnResultInfo.setPercent(new BigDecimal("100.00"));
						// 匹配情况分析
						bmatchAnResultInfo.setResult("匹配度高");
						// 匹配状态 匹配成功
						bmatchAnResultInfo.setRelstate(RelStateEnum.MATCH_SUCCESS.getCode());
						// 匹配情况说明
						bmatchAnResultInfo.setDescribe(null);
						// 备注
						bmatchAnResultInfo.setRemark("开发企业根据组织机构代码完全匹配");
						// 单位信息表
						bmatchAnResultInfo.setMatchtype(MatchTypeEnum.DEVELOP.getCode());
						// todo 创建人 待确定
						bmatchAnResultInfo.setInuser("无");
						//bmatchAnResultInfo.setInuser(httpSession.getAttribute(GCC.SESSION_KEY_USERNAME).toString());
						bmatchAnResultInfo.setIndate(UtilTool.nowTime());
						// 修改人
						bmatchAnResultInfo.setEdituser(null);
						bmatchAnResultInfo.setEditdate(null);
						bmatchAnResultInfo.setVersion(new BigDecimal(bvdfCorpParam.getVersionnumber()));
						bmatchAnResultService.insertBmatchAnResult(bmatchAnResultInfo);
						paramListForDelByCertNo.add(bvrfisCorpInfoParam);
					}
				}
			} catch (MsgException e) {
				log.error(e + "bvrfisCorpInfoParam参数为：{}", bvrfisCorpInfoParam);
			} catch (IOException e) {
				log.error(e + "bvrfisCorpInfoParam参数为：{}", bvrfisCorpInfoParam);
			}
		});
		bvrfisCorpInfoParamList.removeAll(paramListForDelByCertNo);
	}*/
}
