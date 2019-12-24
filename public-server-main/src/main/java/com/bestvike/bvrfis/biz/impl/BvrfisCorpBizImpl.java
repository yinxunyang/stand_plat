package com.bestvike.bvrfis.biz.impl;

import com.bestvike.bvrfis.biz.BvrfisCorpBiz;
import com.bestvike.bvrfis.entity.BLogOper;
import com.bestvike.bvrfis.entity.BmatchAnResultInfo;
import com.bestvike.bvrfis.param.BvrfisCorpInfoParam;
import com.bestvike.bvrfis.service.BLogOperService;
import com.bestvike.bvrfis.service.BmatchAnResultService;
import com.bestvike.bvrfis.service.BvrfisCorpService;
import com.bestvike.bvrfis.service.BvrfisService;
import com.bestvike.commons.enums.MatchTypeEnum;
import com.bestvike.commons.enums.RelStateEnum;
import com.bestvike.commons.enums.ReturnCode;
import com.bestvike.commons.exception.MsgException;
import com.bestvike.commons.utils.StringUtils;
import com.bestvike.commons.utils.UtilTool;
import com.bestvike.dataCenter.param.BvdfCorpParam;
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
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: yinxunyang
 * @Description: 维修资金房屋信息的biz实现类
 * @Date: 2019/12/10 17:06
 */
@Service
@Slf4j
public class BvrfisCorpBizImpl implements BvrfisCorpBiz {
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
	/**
	 * 疑似匹配的条数
	 */
	@Value("${standplatConfig.unCertainSize}")
	private String unCertainSize;
	@Autowired
	private BvrfisCorpService bvrfisCorpService;
	@Autowired
	private BmatchAnResultService bmatchAnResultService;
	@Autowired
	private BvrfisService bvrfisService;
	@Autowired
	private BLogOperService bLogOperService;

	/**
	 * @Author: yinxunyang
	 * @Description: 将bvrfis公司信息跟es中的匹配
	 * @Date: 2019/12/19 15:51
	 * @param:
	 * @return:
	 */
	@Override
	public void bvrfisCorpMatchEs(HttpSession httpSession) throws MsgException {
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
		// 新增操作日志
		BLogOper bLogOper = new BLogOper();
		String logId = UtilTool.UUID();
		bLogOper.setLogid(logId);
		// todo 待定
		bLogOper.setInUser("无");
		bLogOper.setInDate(UtilTool.nowTime());
		bLogOper.setMatchtype(MatchTypeEnum.DEVELOP.getCode());
		bLogOperService.insertBLogOper(bLogOper);
		try (TransportClient client = new PreBuiltTransportClient(Settings.builder().put("cluster.name", esClusterName).build())
				.addTransportAddress(new TransportAddress(InetAddress.getByName(esIP), Integer.parseInt(esPort)))) {
			// 开发企业根据组织机构代码完全匹配
			uniqueMatchCorp(bvrfisCorpInfoParamList, client, httpSession, logId);
			// 开发企业根据单位名称完全匹配
			uniqueMatchCorpByCorpName(bvrfisCorpInfoParamList, client, httpSession, logId);
			// 开发企业根据单位名称疑似匹配
			unCertainCorpByCorpName(bvrfisCorpInfoParamList, client, httpSession, logId);
		} catch (UnknownHostException e) {
			log.error("创建elasticsearch客户端连接失败" + e);
			throw new MsgException(ReturnCode.sdp_sys_error, "创建elasticsearch客户端连接失败");
		}
	}

	/**
	 * @Author: yinxunyang
	 * @Description: 开发企业根据组织机构代码完全匹配
	 * @Date: 2019/12/19 19:15
	 * @param:
	 * @return:
	 */
	private void uniqueMatchCorp(List<BvrfisCorpInfoParam> bvrfisCorpInfoParamList, TransportClient client, HttpSession httpSession, String logId) {
		// 完全匹配开发企业信息
		String corpQueryEs = bvrfisService.organizeQueryEsByJson("elasticSearch/uniqueMatchCorpQuery.json");
		// 匹配成功后需要从bvrfisCorpInfoParamList移除的List
		List<BvrfisCorpInfoParam> paramListForDelByCertNo = new ArrayList<>();
		// 遍历开发企业信息和elasticsearch
		bvrfisCorpInfoParamList.forEach(bvrfisCorpInfoParam -> {
			try {
				String licenseNo = bvrfisCorpInfoParam.getLicenseNo();
				// licenseNo如果为空，跳过该笔查询
				if (StringUtils.isEmpty(licenseNo)) {
					return;
				}
				String corpQueryParam = corpQueryEs.replace("certificateNoValue", licenseNo);
				log.info(corpQueryParam);
				WrapperQueryBuilder wqb = QueryBuilders.wrapperQuery(corpQueryParam);
				SearchResponse searchResponse = client.prepareSearch(corpindex)
						.setTypes(corptype).setQuery(wqb).get();
				SearchHit[] hits = searchResponse.getHits().getHits();
				// 如果返回唯一一条数据，说明完全匹配
				if (hits.length == 1) {
					for (SearchHit hit : hits) {
						// 返回内容
						String bvdfCorpJson = hit.getSourceAsString();
						BvdfCorpParam bvdfCorpParam = (BvdfCorpParam) UtilTool.jsonToObj(bvdfCorpJson, BvdfCorpParam.class);
						BmatchAnResultInfo bmatchAnResultInfo = new BmatchAnResultInfo();
						bmatchAnResultInfo.setMatchid(UtilTool.UUID());
						bmatchAnResultInfo.setLogid(logId);
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
			}
		});
		bvrfisCorpInfoParamList.removeAll(paramListForDelByCertNo);
	}

	/**
	 * @Author: yinxunyang
	 * @Description: 开发企业根据企业名称完全匹配
	 * @Date: 2019/12/19 19:15
	 * @param:
	 * @return:
	 */
	private void uniqueMatchCorpByCorpName(List<BvrfisCorpInfoParam> bvrfisCorpInfoParamList, TransportClient client, HttpSession httpSession, String logId) {
		// 完全匹配开发企业信息
		String corpQueryEs = bvrfisService.organizeQueryEsByJson("elasticSearch/uniqueMatchbyCorpName.json");
		// 匹配成功后需要从bvrfisCorpInfoParamList移除的List
		List<BvrfisCorpInfoParam> paramListForDelByCorpName = new ArrayList<>();
		// 遍历开发企业信息和elasticsearch
		bvrfisCorpInfoParamList.forEach(bvrfisCorpInfoParam -> {
			try {
				String corpQueryParam = corpQueryEs.replace("corpNameValue", bvrfisCorpInfoParam.getCorpName());
				log.info(corpQueryParam);
				WrapperQueryBuilder wqb = QueryBuilders.wrapperQuery(corpQueryParam);
				SearchResponse searchResponse = client.prepareSearch(corpindex)
						.setTypes(corptype).setQuery(wqb).get();
				SearchHit[] hits = searchResponse.getHits().getHits();
				// 如果返回唯一一条数据，说明完全匹配
				if (hits.length == 1) {
					for (SearchHit hit : hits) {
						// 返回内容
						String bvdfCorpJson = hit.getSourceAsString();
						BvdfCorpParam bvdfCorpParam = (BvdfCorpParam) UtilTool.jsonToObj(bvdfCorpJson, BvdfCorpParam.class);
						BmatchAnResultInfo bmatchAnResultInfo = new BmatchAnResultInfo();
						bmatchAnResultInfo.setMatchid(UtilTool.UUID());
						bmatchAnResultInfo.setLogid(logId);
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
						bmatchAnResultInfo.setRemark("开发企业根据企业名称完全匹配");
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
						paramListForDelByCorpName.add(bvrfisCorpInfoParam);
					}
				}
			} catch (MsgException e) {
				log.error(e + "bvrfisCorpInfoParam参数为：{}", bvrfisCorpInfoParam);
			}
		});
		bvrfisCorpInfoParamList.removeAll(paramListForDelByCorpName);
	}

	/**
	 * @Author: yinxunyang
	 * @Description: 开发企业根据单位名称疑似匹配
	 * @Date: 2019/12/19 19:15
	 * @param:
	 * @return:
	 */
	private void unCertainCorpByCorpName(List<BvrfisCorpInfoParam> bvrfisCorpInfoParamList, TransportClient client, HttpSession httpSession, String logId) {
		// 完全匹配开发企业信息
		String corpQueryEs = bvrfisService.organizeQueryEsByJson("elasticSearch/unCertainbyCorpName.json");
		// 遍历开发企业信息和elasticsearch
		bvrfisCorpInfoParamList.forEach(bvrfisCorpInfoParam -> {
			try {
				String corpQueryParam = corpQueryEs.replace("corpNameValue", bvrfisCorpInfoParam.getCorpName());
				log.info(corpQueryParam);
				WrapperQueryBuilder wqb = QueryBuilders.wrapperQuery(corpQueryParam);
				SearchResponse searchResponse = client.prepareSearch(corpindex)
						.setTypes(corptype).setSize(Integer.parseInt(unCertainSize)).setQuery(wqb).get();
				SearchHit[] hits = searchResponse.getHits().getHits();
				// 如果返回唯一一条数据，说明完全匹配
				for (SearchHit hit : hits) {
					// 返回内容
					String bvdfCorpJson = hit.getSourceAsString();
					BvdfCorpParam bvdfCorpParam = (BvdfCorpParam) UtilTool.jsonToObj(bvdfCorpJson, BvdfCorpParam.class);
					BmatchAnResultInfo bmatchAnResultInfo = new BmatchAnResultInfo();
					bmatchAnResultInfo.setMatchid(UtilTool.UUID());
					bmatchAnResultInfo.setLogid(logId);
					// 维修资金数据ID
					bmatchAnResultInfo.setWxbusiid(bvrfisCorpInfoParam.getCorpNo());
					bmatchAnResultInfo.setCenterid(bvdfCorpParam.getDataCenterId());
					bmatchAnResultInfo.setWqbusiid(bvdfCorpParam.getCorpId());
					String score = Float.toString(hit.getScore());
					// 该条数据匹配率
					bmatchAnResultInfo.setPercent(new BigDecimal(score));
					// 匹配情况分析
					bmatchAnResultInfo.setResult("匹配度低");
					// 匹配状态 匹配成功
					bmatchAnResultInfo.setRelstate(RelStateEnum.MATCH_SUCCESS.getCode());
					// 匹配情况说明
					bmatchAnResultInfo.setDescribe(null);
					// 备注
					bmatchAnResultInfo.setRemark("开发企业根据单位名称疑似匹配");
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
				}
			} catch (MsgException e) {
				log.error(e + "bvrfisCorpInfoParam参数为：{}", bvrfisCorpInfoParam);
			}
		});
	}
}
