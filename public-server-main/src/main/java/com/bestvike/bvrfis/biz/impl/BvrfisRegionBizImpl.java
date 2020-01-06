package com.bestvike.bvrfis.biz.impl;

import com.bestvike.bvrfis.biz.BvrfisRegionBiz;
import com.bestvike.bvrfis.entity.BDataRelation;
import com.bestvike.bvrfis.entity.BLogOper;
import com.bestvike.bvrfis.entity.BmatchAnResultInfo;
import com.bestvike.bvrfis.param.BDataRelationParam;
import com.bestvike.bvrfis.param.BvrfisRegionParam;
import com.bestvike.bvrfis.service.BDataRelationService;
import com.bestvike.bvrfis.service.BLogOperService;
import com.bestvike.bvrfis.service.BvrfisRegionService;
import com.bestvike.bvrfis.service.BvrfisService;
import com.bestvike.commons.enums.DataCenterEnum;
import com.bestvike.commons.enums.MatchTypeEnum;
import com.bestvike.commons.enums.RelStateEnum;
import com.bestvike.commons.enums.ReturnCode;
import com.bestvike.commons.exception.MsgException;
import com.bestvike.commons.utils.UtilTool;
import com.bestvike.dataCenter.param.BvdfRegionParam;
import com.bestvike.dataCenter.service.BvdfRegionService;
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
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

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
	private BvdfRegionService bvdfRegionService;
	@Autowired
	private BDataRelationService bDataRelationService;
	@Autowired
	private BvrfisService bvrfisService;
	@Autowired
	private BLogOperService bLogOperService;
	@Autowired
	private ElasticSearchService elasticSearchService;
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
		List<BvrfisRegionParam> bvrfisRegionExists = new ArrayList<>();
		bvrfisRegionParamList.forEach(bvrfisRegionParam -> {
			// 如果该条数据已经存在挂接关系，不再新增匹配结果表
			BDataRelationParam bDataRelationParam = new BDataRelationParam();
			bDataRelationParam.setWxBusiId(bvrfisRegionParam.getRegionNo());
			BDataRelation bDataRelation = bDataRelationService.selectBDataRelation(bDataRelationParam);
			if (null != bDataRelation) {
				bvrfisRegionExists.add(bvrfisRegionParam);
			}
		});
		bvrfisRegionParamList.removeAll(bvrfisRegionExists);
		// 新增操作日志
		BLogOper bLogOper = new BLogOper();
		String logId = UtilTool.UUID();
		bLogOper.setLogid(logId);
		// todo 待定
		bLogOper.setInUser("无");
		bLogOper.setInDate(UtilTool.nowTime());
		bLogOper.setMatchtype(MatchTypeEnum.DEVELOP.getCode());
		bLogOperService.insertBLogOper(bLogOper);
		// 完全匹配
		uniqueMatchCorp(bvrfisRegionParamList, httpSession, logId);
		try (TransportClient client = new PreBuiltTransportClient(Settings.builder().put("cluster.name", esClusterName).build())
				.addTransportAddress(new TransportAddress(InetAddress.getByName(esIP), Integer.parseInt(esPort)))) {
			// 疑似匹配
			unCertainMatchCorp(bvrfisRegionParamList, client, httpSession, logId);
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
	private void uniqueMatchCorp(List<BvrfisRegionParam> bvrfisRegionParamList, HttpSession httpSession, String logId) {
		// 匹配成功后需要从bvrfisRegionParamList移除的List
		List<BvrfisRegionParam> paramListForDel = new ArrayList<>();
		// 遍历开发企业信息和elasticsearch
		bvrfisRegionParamList.forEach(bvrfisRegionParam -> {
			try {
				// 查询bvdf的小区信息
				BvdfRegionParam queryParam = new BvdfRegionParam();
				queryParam.setCorpNo(DataCenterEnum.BVDF_APP_CODE_CAPITAL.getCode() + bvrfisRegionParam.getCorpNo());
				queryParam.setRegionNo(DataCenterEnum.BVDF_APP_CODE_CAPITAL.getCode() + bvrfisRegionParam.getRegionNo());
				//queryParam.setDivisionCode(bvrfisRegionParam.getDivisionCode());
				//queryParam.setFloorArea(bvrfisRegionParam.getFloorArea());
				List<BvdfRegionParam> bvdfRegionParamList = bvdfRegionService.queryBvdfRegionInfo(queryParam);
				if (bvdfRegionParamList.isEmpty() || bvdfRegionParamList.size() != 1) {
					return;
				}
				BvdfRegionParam bvdfRegionParam = bvdfRegionParamList.get(0);
				BmatchAnResultInfo bmatchAnResultInfo = new BmatchAnResultInfo();
				bmatchAnResultInfo.setMatchid(UtilTool.UUID());
				bmatchAnResultInfo.setLogid(logId);
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
				// 先删除再新增匹配结果表，同事务
				bvrfisService.delAndInsertBmatchAnResult(bmatchAnResultInfo);
				paramListForDel.add(bvrfisRegionParam);
			} catch (MsgException e) {
				log.error(e + "bvrfisRegionParam参数为：{}", bvrfisRegionParam);
			}
		});
		bvrfisRegionParamList.removeAll(paramListForDel);
	}
	/**
	 * @Author: yinxunyang
	 * @Description: 小区匹配
	 * @Date: 2019/12/19 19:15
	 * @param:
	 * @return:
	 */
	private void unCertainMatchCorp(List<BvrfisRegionParam> bvrfisRegionParamList, TransportClient client, HttpSession httpSession, String logId) {
		// 完全匹配开发企业信息
		String corpQueryEs = elasticSearchService.organizeQueryEsByJson("elasticSearch/region/unCertainRegion.json");
		// 匹配成功后需要从bvrfisRegionParamList移除的List
		List<BvrfisRegionParam> paramListForDel = new ArrayList<>();
		// 遍历开发企业信息和elasticsearch
		bvrfisRegionParamList.forEach(bvrfisRegionParam -> {
			try {
				String corpNo = bvrfisRegionParam.getCorpNo();
				// corpNo如果为空，跳过该笔查询
				if (StringUtils.isEmpty(corpNo)) {
					return;
				}
				BDataRelationParam bDataRelationParam = new BDataRelationParam();
				bDataRelationParam.setWxBusiId(corpNo);
				bDataRelationParam.setLinkType(MatchTypeEnum.DEVELOP.getCode());
				// 根据匹配结果表查询
				BDataRelation bDataRelation = bDataRelationService.selectBDataRelation(bDataRelationParam);
				if (null != bDataRelation) {
					corpNo = bDataRelation.getWqBusiId();
				}
				// corpNo如果为空，跳过该笔查询
				if (StringUtils.isEmpty(corpNo)) {
					return;
				}
				String regionName = bvrfisRegionParam.getRegionName();
				if (StringUtils.isEmpty(regionName)) {
					regionName = "无";
				}
				String divisionCode = bvrfisRegionParam.getDivisionCode();
				if (StringUtils.isEmpty(divisionCode)) {
					divisionCode = "无";
				}
				String address = bvrfisRegionParam.getAddress();
				if (StringUtils.isEmpty(address)) {
					address = "无";
				}
				String floorArea = bvrfisRegionParam.getFloorArea();
				if (StringUtils.isEmpty(floorArea)) {
					floorArea = "无";
				}
				String corpQueryParam = corpQueryEs
						.replace("corpNoValue", corpNo)
						//.replace("regionNoValue", regionNo)
						.replace("regionNameValue", regionName)
						.replace("divisionCodeValue", divisionCode)
						.replace("addressValue", address)
						.replace("floorAreaValue", floorArea);
				log.info(corpQueryParam);
				WrapperQueryBuilder wqb = QueryBuilders.wrapperQuery(corpQueryParam);
				SearchResponse searchResponse = client.prepareSearch(regionindex).setSize(Integer.parseInt(unCertainSize))
						.setTypes(regiontype).setQuery(wqb).get();
				SearchHit[] hits = searchResponse.getHits().getHits();
				for (SearchHit hit : hits) {
					// 返回内容
					String bvdfRegionJson = hit.getSourceAsString();
					BvdfRegionParam bvdfRegionParam = (BvdfRegionParam) UtilTool.jsonToObj(bvdfRegionJson, BvdfRegionParam.class);
					BmatchAnResultInfo bmatchAnResultInfo = new BmatchAnResultInfo();
					bmatchAnResultInfo.setMatchid(UtilTool.UUID());
					bmatchAnResultInfo.setLogid(logId);
					// 维修资金数据ID
					bmatchAnResultInfo.setWxbusiid(bvrfisRegionParam.getCorpNo());
					bmatchAnResultInfo.setCenterid(bvdfRegionParam.getDataCenterId());
					bmatchAnResultInfo.setWqbusiid(bvdfRegionParam.getCorpNo());
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
					bmatchAnResultInfo.setRemark("小区信息疑似匹配");
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
					// 先删除再新增匹配结果表，同事务
					bvrfisService.delAndInsertBmatchAnResult(bmatchAnResultInfo);
					paramListForDel.add(bvrfisRegionParam);
				}
			} catch (MsgException e) {
				log.error(e + "bvrfisRegionParam参数为：{}", bvrfisRegionParam);
			}
		});
		bvrfisRegionParamList.removeAll(paramListForDel);
	}
}
