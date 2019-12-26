package com.bestvike.bvrfis.biz.impl;

import com.bestvike.bvrfis.biz.BvrfisBldBiz;
import com.bestvike.bvrfis.entity.BDataRelation;
import com.bestvike.bvrfis.entity.BLogOper;
import com.bestvike.bvrfis.entity.BmatchAnResultInfo;
import com.bestvike.bvrfis.param.BDataRelationParam;
import com.bestvike.bvrfis.param.BvrfisBldParam;
import com.bestvike.bvrfis.param.BvrfisCorpInfoParam;
import com.bestvike.bvrfis.service.BDataRelationService;
import com.bestvike.bvrfis.service.BLogOperService;
import com.bestvike.bvrfis.service.BvrfisBldService;
import com.bestvike.bvrfis.service.BvrfisService;
import com.bestvike.commons.enums.MatchTypeEnum;
import com.bestvike.commons.enums.RelStateEnum;
import com.bestvike.commons.enums.ReturnCode;
import com.bestvike.commons.exception.MsgException;
import com.bestvike.commons.utils.UtilTool;
import com.bestvike.dataCenter.param.BvdfBldParam;
import com.bestvike.dataCenter.param.BvdfCorpParam;
import com.bestvike.dataCenter.service.BvdfBldService;
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
 * @Description: 维修资金自然幢信息的biz实现类
 * @Date: 2019/12/10 17:06
 */
@Service
@Slf4j
public class BvrfisBldBizImpl implements BvrfisBldBiz {
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
	private BvrfisBldService bvrfisBldService;
	@Autowired
	private BvrfisService bvrfisService;
	@Autowired
	private BLogOperService bLogOperService;
	@Autowired
	private BDataRelationService bDataRelationService;
	@Autowired
	private BvdfBldService bvdfBldService;

	/**
	 * @Author: yinxunyang
	 * @Description: 将bvrfis自然幢跟es中的匹配
	 * @Date: 2019/12/19 15:51
	 * @param:
	 * @return:
	 */
	@Override
	public void bvrfisBldMatchEs(HttpSession httpSession) throws MsgException {
		BvrfisBldParam queryParam = new BvrfisBldParam();
		// 0正常
		queryParam.setState("0");
		queryParam.setDatacenterId("isNUll");
		List<BvrfisBldParam> bvrfisBldParamList = bvrfisBldService.queryBvrfisBldInfo(queryParam);
		if (bvrfisBldParamList.isEmpty()) {
			log.info("bvrfis没有需要跟elasticsearch匹配的自然幢数据");
			return;
		}
		List<BvrfisBldParam> bvrfisBldExists = new ArrayList<>();
		bvrfisBldParamList.forEach(bvrfisBldParam -> {
			// 如果该条数据已经存在挂接关系，不再新增匹配结果表
			BDataRelationParam bDataRelationParam = new BDataRelationParam();
			bDataRelationParam.setWxBusiId(bvrfisBldParam.getBldNo());
			BDataRelation bDataRelation = bDataRelationService.selectBDataRelation(bDataRelationParam);
			if (null != bDataRelation) {
				bvrfisBldExists.add(bvrfisBldParam);
			}
		});
		bvrfisBldParamList.removeAll(bvrfisBldExists);
		// 新增操作日志
		BLogOper bLogOper = new BLogOper();
		String logId = UtilTool.UUID();
		bLogOper.setLogid(logId);
		// todo 待定
		bLogOper.setInUser("无");
		bLogOper.setInDate(UtilTool.nowTime());
		bLogOper.setMatchtype(MatchTypeEnum.BLD.getCode());
		bLogOperService.insertBLogOper(bLogOper);
		// 自然幢根据自然幢名称完全匹配 使用初始的bldName
		uniqueMatchBldByBldName(bvrfisBldParamList, httpSession, logId, "bldNameInit");
		// 自然幢根据自然幢名称完全匹配 使用bldName xx数字
		uniqueMatchBldByBldName(bvrfisBldParamList, httpSession, logId, "bldNameRepalceOne");
		// 自然幢根据自然幢名称完全匹配 使用bldName xx号楼
		uniqueMatchBldByBldName(bvrfisBldParamList, httpSession, logId, "bldNameRepalceTwo");
		try (TransportClient client = new PreBuiltTransportClient(Settings.builder().put("cluster.name", esClusterName).build())
				.addTransportAddress(new TransportAddress(InetAddress.getByName(esIP), Integer.parseInt(esPort)))) {

			// 开发企业根据单位名称疑似匹配
			//unCertainCorpByCorpName(bvrfisBldParamList, client, httpSession, logId);
		} catch (UnknownHostException e) {
			log.error("创建elasticsearch客户端连接失败" + e);
			throw new MsgException(ReturnCode.sdp_sys_error, "创建elasticsearch客户端连接失败");
		}
	}

	/**
	 * @Author: yinxunyang
	 * @Description: 自然幢根据自然幢名称完全匹配
	 * @Date: 2019/12/19 19:15
	 * @param:
	 * @return:
	 */
	private void uniqueMatchBldByBldName(List<BvrfisBldParam> bvrfisBldParamList, HttpSession httpSession, String logId, String bldNameType) {
		// 匹配成功后需要从bvrfisBldParamList移除的List
		List<BvrfisBldParam> paramListForDel = new ArrayList<>();
		// 遍历自然幢信息和elasticsearch
		bvrfisBldParamList.forEach(bvrfisBldParam -> {
			try {
				// 查询bvdf的小区信息
				BDataRelationParam bDataRelationParam = new BDataRelationParam();
				bDataRelationParam.setWxBusiId(bvrfisBldParam.getRegionNo());
				BDataRelation bDataRelation = bDataRelationService.selectBDataRelation(bDataRelationParam);
				if (null == bDataRelation) {
					return;
				}
				String bvdfRegionId = bDataRelation.getWqBusiId();
				// 根据小区编号和自然名称查询 bvdf数据
				BvdfBldParam queryParam = new BvdfBldParam();
				queryParam.setState("normal");
				queryParam.setRegionNo(bvdfRegionId);
				String bldName = bvrfisBldParam.getBldName();
				if ("bldNameRepalceOne".equals(bldNameType)) {
					bldName = bldName.replace("号楼", "").replace("栋", "");
				}
				if ("bldNameRepalceTwo".equals(bldNameType)) {
					bldName = bldName.replace("号楼", "").replace("栋", "") + "号楼";
				}
				queryParam.setBldName(bldName);
				List<BvdfBldParam> bvdfBldParamList = bvdfBldService.queryBvdfBldInfo(queryParam);
				// bvdfBldParamList返回唯一的数据，说明完全匹配
				if (bvdfBldParamList.isEmpty() || bvdfBldParamList.size() != 1) {
					return;
				}
				BvdfBldParam bvdfBldParam = bvdfBldParamList.get(0);
				if (null == bvdfBldParam) {
					return;
				}
				BmatchAnResultInfo bmatchAnResultInfo = new BmatchAnResultInfo();
				bmatchAnResultInfo.setMatchid(UtilTool.UUID());
				bmatchAnResultInfo.setLogid(logId);
				// 维修资金数据ID
				bmatchAnResultInfo.setWxbusiid(bvrfisBldParam.getBldNo());
				bmatchAnResultInfo.setCenterid(bvdfBldParam.getDataCenterId());
				bmatchAnResultInfo.setWqbusiid(bvdfBldParam.getBldNo());
				// 该条数据匹配率 完全匹配是100
				bmatchAnResultInfo.setPercent(new BigDecimal("100.00"));
				// 匹配情况分析
				bmatchAnResultInfo.setResult("匹配度高");
				// 匹配状态 匹配成功
				bmatchAnResultInfo.setRelstate(RelStateEnum.MATCH_SUCCESS.getCode());
				// 匹配情况说明
				bmatchAnResultInfo.setDescribe(null);
				// 备注
				bmatchAnResultInfo.setRemark("自然幢信息完全匹配");
				// 单位信息表
				bmatchAnResultInfo.setMatchtype(MatchTypeEnum.BLD.getCode());
				// todo 创建人 待确定
				bmatchAnResultInfo.setInuser("无");
				//bmatchAnResultInfo.setInuser(httpSession.getAttribute(GCC.SESSION_KEY_USERNAME).toString());
				bmatchAnResultInfo.setIndate(UtilTool.nowTime());
				// 修改人
				bmatchAnResultInfo.setEdituser(null);
				bmatchAnResultInfo.setEditdate(null);
				//bmatchAnResultInfo.setVersion(new BigDecimal(bvdfRegionParam.getVersionnumber()));
				// 先删除再新增匹配结果表，同事务
				bvrfisService.delAndInsertBmatchAnResult(bmatchAnResultInfo);
				paramListForDel.add(bvrfisBldParam);
			} catch (MsgException e) {
				log.error(e + "bvrfisBldParam参数为：{}", bvrfisBldParam);
			}
		});
		bvrfisBldParamList.removeAll(paramListForDel);
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
					// 先删除再新增匹配结果表，同事务
					bvrfisService.delAndInsertBmatchAnResult(bmatchAnResultInfo);
				}
			} catch (MsgException e) {
				log.error(e + "bvrfisCorpInfoParam参数为：{}", bvrfisCorpInfoParam);
			}
		});
	}
}
