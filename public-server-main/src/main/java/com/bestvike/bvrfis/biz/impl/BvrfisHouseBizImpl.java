package com.bestvike.bvrfis.biz.impl;

import com.bestvike.bvrfis.biz.BvrfisHouseBiz;
import com.bestvike.bvrfis.entity.BDataRelation;
import com.bestvike.bvrfis.entity.BLogOper;
import com.bestvike.bvrfis.entity.BmatchAnResultInfo;
import com.bestvike.bvrfis.param.BDataRelationParam;
import com.bestvike.bvrfis.param.BmatchAnResultParam;
import com.bestvike.bvrfis.param.BvrfisBldParam;
import com.bestvike.bvrfis.param.BvrfisHouseParam;
import com.bestvike.bvrfis.param.BvrfisRegionParam;
import com.bestvike.bvrfis.service.BDataRelationService;
import com.bestvike.bvrfis.service.BLogOperService;
import com.bestvike.bvrfis.service.BmatchAnResultService;
import com.bestvike.bvrfis.service.BvrfisBldService;
import com.bestvike.bvrfis.service.BvrfisHouseService;
import com.bestvike.bvrfis.service.BvrfisRegionService;
import com.bestvike.bvrfis.service.BvrfisService;
import com.bestvike.commons.enums.DataCenterEnum;
import com.bestvike.commons.enums.MatchTypeEnum;
import com.bestvike.commons.enums.RelStateEnum;
import com.bestvike.commons.enums.ReturnCode;
import com.bestvike.commons.exception.MsgException;
import com.bestvike.commons.utils.UtilTool;
import com.bestvike.dataCenter.param.BvdfBldParam;
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
 * @Description: 维修资金房屋信息的biz实现类
 * @Date: 2019/12/10 17:06
 */
@Service
@Slf4j
public class BvrfisHouseBizImpl implements BvrfisHouseBiz {
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
	 * es房屋的索引
	 */
	@Value("${esConfig.houseindex}")
	private String houseindex;
	/**
	 * es房屋的映射
	 */
	@Value("${esConfig.housetype}")
	private String housetype;
	/**
	 * 疑似匹配的条数
	 */
	@Value("${standplatConfig.unCertainSize}")
	private String unCertainSize;
	/**
	 * 房屋信息表(arc_houseinfo)最大查询条数,防止内存溢出
	 */
	@Value("${standplatConfig.bvdfToEsSchedule.houseMaxNum}")
	private String houseMaxNum;
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
	@Autowired
	private BmatchAnResultService bmatchAnResultService;
	@Autowired
	private BvrfisRegionService bvrfisRegionService;
	@Autowired
	private BvrfisHouseService bvrfisHouseService;

	/**
	 * @Author: yinxunyang
	 * @Description: 将bvrfis房屋跟es中的匹配
	 * @Date: 2019/12/19 15:51
	 * @param:
	 * @return:
	 */
	@Override
	public void bvrfisHouseMatchEs(HttpSession httpSession) throws MsgException {
		BvrfisHouseParam queryParam = new BvrfisHouseParam();
		// 0正常
		queryParam.setState("0");
		queryParam.setDatacenterId("isNUll");
		queryParam.setHouseMaxNum(Integer.parseInt(houseMaxNum));
		List<BvrfisHouseParam> bvrfisHouseParamList = bvrfisHouseService.queryBvrfisHouseInfo(queryParam);
		if (bvrfisHouseParamList.isEmpty()) {
			log.info("bvrfis没有需要跟elasticsearch匹配的房屋数据");
			return;
		}
		List<BvrfisBldParam> bvrfisBldExists = new ArrayList<>();
		/*bvrfisHouseParamList.forEach(bvrfisBldParam -> {
			// 如果该条数据已经存在挂接关系，不再新增匹配结果表
			BDataRelationParam bDataRelationParam = new BDataRelationParam();
			bDataRelationParam.setWxBusiId(bvrfisBldParam.getBldNo());
			BDataRelation bDataRelation = bDataRelationService.selectBDataRelation(bDataRelationParam);
			if (null != bDataRelation) {
				bvrfisBldExists.add(bvrfisBldParam);
			} else {
				// 添加小区名称
				BvrfisRegionParam regionParam = new BvrfisRegionParam();
				regionParam.setRegionNo(bvrfisBldParam.getRegionNo());
				// 正常
				regionParam.setState("0");
				BvrfisRegionParam bvrfisRegionParam = bvrfisRegionService.selectBvrfisRegionInfo(regionParam);
				if (null != bvrfisRegionParam) {
					bvrfisBldParam.setRegionName(bvrfisRegionParam.getRegionName());
				}
			}
		});*/
/*		bvrfisBldParamList.removeAll(bvrfisBldExists);
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

			// 自然幢根据小区疑似匹配
			unCertainBldByRegion(bvrfisBldParamList, client, httpSession, logId);
			// 自然幢根据开发企业疑似匹配
			unCertainBldByCorpNo(bvrfisBldParamList, client, httpSession, logId);
		} catch (UnknownHostException e) {
			log.error("创建elasticsearch客户端连接失败" + e);
			throw new MsgException(ReturnCode.sdp_sys_error, "创建elasticsearch客户端连接失败");
		}*/
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
				queryParam.setState(DataCenterEnum.NORMAL_STATE.getCode());
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
				bmatchAnResultInfo.setVersion(new BigDecimal(bvdfBldParam.getVersionnumber()));
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
	 * @Description: 自然幢根据小区疑似匹配
	 * @Date: 2019/12/19 19:15
	 * @param:
	 * @return:
	 */
	private void unCertainBldByRegion(List<BvrfisBldParam> bvrfisBldParamList, TransportClient client, HttpSession httpSession, String logId) {
		// 匹配成功后需要从bvrfisBldParamList移除的List
		List<BvrfisBldParam> paramListForDel = new ArrayList<>();
		// 自然幢根据小区疑似匹配
		String bldQueryJson = bvrfisService.organizeQueryEsByJson("elasticSearch/bld/unCertainBldByRegion.json");
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
				String bldQueryParam = bldQueryJson.replace("regionNoValue", bDataRelation.getWqBusiId())
						.replace("bldNameValue", bvrfisBldParam.getBldName())
						.replace("addressValue", bvrfisBldParam.getAddress());
				// 查询的json串
				log.info(bldQueryParam);
				WrapperQueryBuilder wqb = QueryBuilders.wrapperQuery(bldQueryParam);
				SearchResponse searchResponse = client.prepareSearch(houseindex)
						.setTypes(housetype).setSize(Integer.parseInt(unCertainSize)).setQuery(wqb).get();
				SearchHit[] hits = searchResponse.getHits().getHits();
				for (SearchHit hit : hits) {
					// 返回内容
					String bvdfBldJson = hit.getSourceAsString();
					BvdfBldParam bvdfBldParam = (BvdfBldParam) UtilTool.jsonToObj(bvdfBldJson, BvdfBldParam.class);
					// 完全匹配有匹配结果的话不再新增bvdfbldNo
					BmatchAnResultParam bmatchAnResultParam = new BmatchAnResultParam();
					bmatchAnResultParam.setWqbusiid(bvdfBldParam.getBldNo());
					bmatchAnResultParam.setPercent(new BigDecimal("100.00"));
					BmatchAnResultInfo bmatchIsExists = bmatchAnResultService.selectBmatchAnResult(bmatchAnResultParam);
					if (null != bmatchIsExists) {
						return;
					}
					BmatchAnResultInfo bmatchAnResultInfo = new BmatchAnResultInfo();
					bmatchAnResultInfo.setMatchid(UtilTool.UUID());
					bmatchAnResultInfo.setLogid(logId);
					// 维修资金数据ID
					bmatchAnResultInfo.setWxbusiid(bvrfisBldParam.getBldNo());
					bmatchAnResultInfo.setCenterid(bvdfBldParam.getDataCenterId());
					bmatchAnResultInfo.setWqbusiid(bvdfBldParam.getBldNo());
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
					bmatchAnResultInfo.setRemark("自然幢根据小区疑似匹配");
					// 单位信息表
					bmatchAnResultInfo.setMatchtype(MatchTypeEnum.BLD.getCode());
					// todo 创建人 待确定
					bmatchAnResultInfo.setInuser("无");
					//bmatchAnResultInfo.setInuser(httpSession.getAttribute(GCC.SESSION_KEY_USERNAME).toString());
					bmatchAnResultInfo.setIndate(UtilTool.nowTime());
					// 修改人
					bmatchAnResultInfo.setEdituser(null);
					bmatchAnResultInfo.setEditdate(null);
					bmatchAnResultInfo.setVersion(new BigDecimal(bvdfBldParam.getVersionnumber()));
					// 先删除再新增匹配结果表，同事务
					bvrfisService.delAndInsertBmatchAnResult(bmatchAnResultInfo);
					paramListForDel.add(bvrfisBldParam);
				}
			} catch (MsgException e) {
				log.error(e + "bvrfisBldParam参数为：{}", bvrfisBldParam);
			}
		});
		bvrfisBldParamList.removeAll(paramListForDel);
	}

	/**
	 * @Author: yinxunyang
	 * @Description: 自然幢根据开发企业疑似匹配
	 * @Date: 2019/12/19 19:15
	 * @param:
	 * @return:
	 */
	private void unCertainBldByCorpNo(List<BvrfisBldParam> bvrfisBldParamList, TransportClient client, HttpSession httpSession, String logId) {
		// 自然幢根据小区疑似匹配
		String bldQueryJson = bvrfisService.organizeQueryEsByJson("elasticSearch/bld/unCertainBldByCorp.json");
		// 遍历自然幢信息和elasticsearch
		bvrfisBldParamList.forEach(bvrfisBldParam -> {
			try {
				// 查询bvdf的开发企业信息
				BDataRelationParam bDataRelationParam = new BDataRelationParam();
				bDataRelationParam.setWxBusiId(bvrfisBldParam.getDevelopNo());
				BDataRelation bDataRelation = bDataRelationService.selectBDataRelation(bDataRelationParam);
				if (null == bDataRelation) {
					return;
				}
				String bldQueryParam = bldQueryJson.replace("corpNoValue", bDataRelation.getWqBusiId())
						.replace("regionNameValue", bvrfisBldParam.getRegionName())
						.replace("bldNameValue", bvrfisBldParam.getBldName())
						.replace("addressValue", bvrfisBldParam.getAddress());
				// 查询的json串
				log.info(bldQueryParam);
				WrapperQueryBuilder wqb = QueryBuilders.wrapperQuery(bldQueryParam);
				SearchResponse searchResponse = client.prepareSearch(houseindex)
						.setTypes(housetype).setSize(Integer.parseInt(unCertainSize)).setQuery(wqb).get();
				SearchHit[] hits = searchResponse.getHits().getHits();
				for (SearchHit hit : hits) {
					// 返回内容
					String bvdfBldJson = hit.getSourceAsString();
					BvdfBldParam bvdfBldParam = (BvdfBldParam) UtilTool.jsonToObj(bvdfBldJson, BvdfBldParam.class);
					// 完全匹配有匹配结果的话不再新增bvdfbldNo
					BmatchAnResultParam bmatchAnResultParam = new BmatchAnResultParam();
					bmatchAnResultParam.setWqbusiid(bvdfBldParam.getBldNo());
					bmatchAnResultParam.setPercent(new BigDecimal("100.00"));
					BmatchAnResultInfo bmatchIsExists = bmatchAnResultService.selectBmatchAnResult(bmatchAnResultParam);
					if (null != bmatchIsExists) {
						return;
					}
					BmatchAnResultInfo bmatchAnResultInfo = new BmatchAnResultInfo();
					bmatchAnResultInfo.setMatchid(UtilTool.UUID());
					bmatchAnResultInfo.setLogid(logId);
					// 维修资金数据ID
					bmatchAnResultInfo.setWxbusiid(bvrfisBldParam.getBldNo());
					bmatchAnResultInfo.setCenterid(bvdfBldParam.getDataCenterId());
					bmatchAnResultInfo.setWqbusiid(bvdfBldParam.getBldNo());
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
					bmatchAnResultInfo.setRemark("自然幢根据开发企业疑似匹配");
					// 单位信息表
					bmatchAnResultInfo.setMatchtype(MatchTypeEnum.BLD.getCode());
					// todo 创建人 待确定
					bmatchAnResultInfo.setInuser("无");
					//bmatchAnResultInfo.setInuser(httpSession.getAttribute(GCC.SESSION_KEY_USERNAME).toString());
					bmatchAnResultInfo.setIndate(UtilTool.nowTime());
					// 修改人
					bmatchAnResultInfo.setEdituser(null);
					bmatchAnResultInfo.setEditdate(null);
					bmatchAnResultInfo.setVersion(new BigDecimal(bvdfBldParam.getVersionnumber()));
					// 先删除再新增匹配结果表，同事务
					bvrfisService.delAndInsertBmatchAnResult(bmatchAnResultInfo);
				}
			} catch (MsgException e) {
				log.error(e + "bvrfisBldParam参数为：{}", bvrfisBldParam);
			}
		});
	}
}
