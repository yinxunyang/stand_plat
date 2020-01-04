package com.bestvike.bvrfis.biz.impl;

import com.bestvike.bvrfis.biz.BvrfisHouseBiz;
import com.bestvike.bvrfis.entity.BDataRelation;
import com.bestvike.bvrfis.entity.BLogOper;
import com.bestvike.bvrfis.entity.BmatchAnResultInfo;
import com.bestvike.bvrfis.param.BDataRelationParam;
import com.bestvike.bvrfis.param.BmatchAnResultParam;
import com.bestvike.bvrfis.param.BvrfisBldParam;
import com.bestvike.bvrfis.param.BvrfisCellParam;
import com.bestvike.bvrfis.param.BvrfisCorpInfoParam;
import com.bestvike.bvrfis.param.BvrfisFloorParam;
import com.bestvike.bvrfis.param.BvrfisHouseParam;
import com.bestvike.bvrfis.param.BvrfisRegionParam;
import com.bestvike.bvrfis.service.BDataRelationService;
import com.bestvike.bvrfis.service.BLogOperService;
import com.bestvike.bvrfis.service.BmatchAnResultService;
import com.bestvike.bvrfis.service.BvrfisBldService;
import com.bestvike.bvrfis.service.BvrfisCellService;
import com.bestvike.bvrfis.service.BvrfisCorpService;
import com.bestvike.bvrfis.service.BvrfisFloorService;
import com.bestvike.bvrfis.service.BvrfisHouseService;
import com.bestvike.bvrfis.service.BvrfisRegionService;
import com.bestvike.bvrfis.service.BvrfisService;
import com.bestvike.commons.enums.DataCenterEnum;
import com.bestvike.commons.enums.MatchTypeEnum;
import com.bestvike.commons.enums.RelStateEnum;
import com.bestvike.commons.enums.ReturnCode;
import com.bestvike.commons.exception.MsgException;
import com.bestvike.commons.utils.UtilTool;
import com.bestvike.dataCenter.param.BvdfHouseParam;
import com.bestvike.dataCenter.service.BvdfBldService;
import com.bestvike.dataCenter.service.BvdfHouseService;
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
	private BvdfHouseService bvdfHouseService;
	@Autowired
	private BmatchAnResultService bmatchAnResultService;
	@Autowired
	private BvrfisRegionService bvrfisRegionService;
	@Autowired
	private BvrfisHouseService bvrfisHouseService;
	@Autowired
	private BvrfisCellService bvrfisCellService;
	@Autowired
	private BvrfisFloorService bvrfisFloorService;
	@Autowired
	private BvrfisCorpService bvrfisCorpService;
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
		// 新增操作日志
		BLogOper bLogOper = new BLogOper();
		String logId = UtilTool.UUID();
		bLogOper.setLogid(logId);
		// todo 待定
		bLogOper.setInUser("无");
		bLogOper.setInDate(UtilTool.nowTime());
		bLogOper.setMatchtype(MatchTypeEnum.HOUSE.getCode());
		bLogOperService.insertBLogOper(bLogOper);
		// 房屋根据房屋名称完全匹配
		uniqueMatchHouseByHouseName(bvrfisHouseParamList, httpSession, logId);
		// 组织跟es匹配的数据
		organizeEsHouseList(bvrfisHouseParamList);
		try (TransportClient client = new PreBuiltTransportClient(Settings.builder().put("cluster.name", esClusterName).build())
				.addTransportAddress(new TransportAddress(InetAddress.getByName(esIP), Integer.parseInt(esPort)))) {
			// 房屋根据自然幢疑似匹配
			unCertainHouseByBld(bvrfisHouseParamList, client, httpSession, logId);
			// 房屋根据小区疑似匹配
			unCertainHouseByRegion(bvrfisHouseParamList, client, httpSession, logId);
			// 房屋根据开发企业疑似匹配
			//unCertainBldByCorpNo(bvrfisHouseParamList, client, httpSession, logId);*/
		} catch (UnknownHostException e) {
			log.error("创建elasticsearch客户端连接失败" + e);
			throw new MsgException(ReturnCode.sdp_sys_error, "创建elasticsearch客户端连接失败");
		}
	}

	/**
	 * @Author: yinxunyang
	 * @Description: 组织跟es匹配的数据
	 * @Date: 2019/12/31 17:01
	 * @param:
	 * @return:
	 */
	private void organizeEsHouseList(List<BvrfisHouseParam> bvrfisHouseParamList) {
		bvrfisHouseParamList.forEach(bvrfisHouseParam -> {
			BvrfisBldParam bldQueryParam = new BvrfisBldParam();
			bldQueryParam.setBldNo(bvrfisHouseParam.getBldNo());
			// 查询楼幢名称
			BvrfisBldParam bvrfisBldParam = bvrfisBldService.selectBvrfisBldInfo(bldQueryParam);
			String bldName = "无";
			String developNo = "无";
			String regionNo = "无";
			if (null != bvrfisBldParam) {
				bldName = bvrfisBldParam.getBldName();
				developNo = bvrfisBldParam.getDevelopNo();
				regionNo = bvrfisBldParam.getRegionNo();
			}
			bvrfisHouseParam.setBldName(bldName);
			BvrfisCorpInfoParam corpInfoQuery = new BvrfisCorpInfoParam();
			corpInfoQuery.setCorpNo(developNo);
			// 查询开发企业名称
			BvrfisCorpInfoParam bvrfisCorpInfoParam = bvrfisCorpService.selectBvrfisCorpInfo(corpInfoQuery);
			String developName = "无";
			if (null != bvrfisCorpInfoParam) {
				developName = bvrfisCorpInfoParam.getCorpName();
			}
			bvrfisHouseParam.setDevelopName(developName);
			BvrfisRegionParam regionQuery = new BvrfisRegionParam();
			regionQuery.setRegionNo(regionNo);
			// 查询小区名称
			BvrfisRegionParam bvrfisRegionParam = bvrfisRegionService.selectBvrfisRegionInfo(regionQuery);
			String regionName = "无";
			if (null != bvrfisRegionParam) {
				regionName = bvrfisRegionParam.getRegionName();
			}
			bvrfisHouseParam.setRegionNo(regionNo);
			bvrfisHouseParam.setRegionName(regionName);
			BvrfisCellParam cellQuery = new BvrfisCellParam();
			cellQuery.setBldNo(bvrfisHouseParam.getBldNo());
			cellQuery.setCellNo(bvrfisHouseParam.getCellNo());
			cellQuery.setHouseProp(bvrfisHouseParam.getHouseProp());
			// 查询单元名称
			BvrfisCellParam bvrfisCellParam = bvrfisCellService.selectBvrfisCellInfo(cellQuery);
			String cellName = "无";
			if (null != bvrfisCellParam) {
				cellName = bvrfisCellParam.getCellName();
			}
			bvrfisHouseParam.setCellName(cellName);
			BvrfisFloorParam floorQuery = new BvrfisFloorParam();
			floorQuery.setBldNo(bvrfisHouseParam.getBldNo());
			floorQuery.setFloorNo(bvrfisHouseParam.getFloorNo());
			floorQuery.setHouseProp(bvrfisHouseParam.getHouseProp());
			// 查询楼层名称
			BvrfisFloorParam bvrfisFloorParam = bvrfisFloorService.selectBvrfisFloorInfo(floorQuery);
			String floorName = "无";
			if (null != bvrfisFloorParam) {
				floorName = bvrfisFloorParam.getFloorName();
			}
			bvrfisHouseParam.setFloorName(floorName);
		});
	}
	/**
	 * @Author: yinxunyang
	 * @Description: 房屋根据房屋名称完全匹配
	 * @Date: 2019/12/19 19:15
	 * @param:
	 * @return:
	 */
	private void uniqueMatchHouseByHouseName(List<BvrfisHouseParam> bvrfisHouseParamList, HttpSession httpSession, String logId) {
		// 匹配成功后需要从bvrfisHouseParamList移除的List
		List<BvrfisHouseParam> paramListForDel = new ArrayList<>();
		// 遍历房屋信息和elasticsearch
		bvrfisHouseParamList.forEach(bvrfisHouseParam -> {
			try {
				// 查询bvdf的自然幢信息
				BDataRelationParam bDataRelationParam = new BDataRelationParam();
				bDataRelationParam.setWxBusiId(bvrfisHouseParam.getBldNo());
				BDataRelation bDataRelation = bDataRelationService.selectBDataRelation(bDataRelationParam);
				if (null == bDataRelation) {
					return;
				}
				String bvdfBldId = bDataRelation.getWqBusiId();
				// 根据自然幢编号和单元编号、楼层编号、室号查询 bvdf数据
				BvdfHouseParam queryParam = new BvdfHouseParam();
				queryParam.setState(DataCenterEnum.NORMAL_STATE.getCode());
				queryParam.setBldno(bvdfBldId);
				queryParam.setCellno(bvrfisHouseParam.getCellNo());
				queryParam.setFloorno(bvrfisHouseParam.getFloorNo());
				queryParam.setRoomno("0" + bvrfisHouseParam.getRoomNo());
				BvdfHouseParam bvdfHouseParam = bvdfHouseService.selectBvdfHouseInfo(queryParam);
				// bvdfHouseParam不为空，说明完全匹配
				if (null == bvdfHouseParam) {
					return;
				}
				BmatchAnResultInfo bmatchAnResultInfo = new BmatchAnResultInfo();
				bmatchAnResultInfo.setMatchid(UtilTool.UUID());
				bmatchAnResultInfo.setLogid(logId);
				// 维修资金数据ID
				bmatchAnResultInfo.setWxbusiid(bvrfisHouseParam.getSysGuid());
				bmatchAnResultInfo.setCenterid(bvdfHouseParam.getDataCenterId());
				bmatchAnResultInfo.setWqbusiid(bvdfHouseParam.getHouseid());
				// 该条数据匹配率 完全匹配是100
				bmatchAnResultInfo.setPercent(new BigDecimal("100.00"));
				// 匹配情况分析
				bmatchAnResultInfo.setResult("匹配度高");
				// 匹配状态 匹配成功
				bmatchAnResultInfo.setRelstate(RelStateEnum.MATCH_SUCCESS.getCode());
				// 匹配情况说明
				bmatchAnResultInfo.setDescribe(null);
				// 备注
				bmatchAnResultInfo.setRemark("房屋信息完全匹配");
				// 单位信息表
				bmatchAnResultInfo.setMatchtype(MatchTypeEnum.BLD.getCode());
				// todo 创建人 待确定
				bmatchAnResultInfo.setInuser("无");
				//bmatchAnResultInfo.setInuser(httpSession.getAttribute(GCC.SESSION_KEY_USERNAME).toString());
				bmatchAnResultInfo.setIndate(UtilTool.nowTime());
				// 修改人
				bmatchAnResultInfo.setEdituser(null);
				bmatchAnResultInfo.setEditdate(null);
				bmatchAnResultInfo.setVersion(new BigDecimal(bvdfHouseParam.getVersionnumber()));
				// 先删除再新增匹配结果表，同事务
				bvrfisService.delAndInsertBmatchAnResult(bmatchAnResultInfo);
				paramListForDel.add(bvrfisHouseParam);
			} catch (MsgException e) {
				log.error(e + "bvrfisHouseParam参数为：{}", bvrfisHouseParam);
			}
		});
		bvrfisHouseParamList.removeAll(paramListForDel);
	}

	/**
	 * @Author: yinxunyang
	 * @Description: 房屋根据自然幢疑似匹配
	 * @Date: 2019/12/19 19:15
	 * @param:
	 * @return:
	 */
	private void unCertainHouseByBld(List<BvrfisHouseParam> bvrfisHouseParamList, TransportClient client, HttpSession httpSession, String logId) {
		// 匹配成功后需要从bvrfisHouseParamList移除的List
		List<BvrfisHouseParam> paramListForDel = new ArrayList<>();
		// 房屋根据自然幢疑似匹配
		String houseQueryJson = bvrfisService.organizeQueryEsByJson("elasticSearch/house/unCertainHouseByBld.json");
		// 遍历房屋信息和elasticsearch
		bvrfisHouseParamList.forEach(bvrfisHouseParam -> {
			try {
				// 查询bvdf的自然幢信息
				BDataRelationParam bDataRelationParam = new BDataRelationParam();
				bDataRelationParam.setWxBusiId(bvrfisHouseParam.getBldNo());
				BDataRelation bDataRelation = bDataRelationService.selectBDataRelation(bDataRelationParam);
				if (null == bDataRelation) {
					return;
				}
				String houseQueryParam = houseQueryJson.replace("bldNoValue", bDataRelation.getWqBusiId())
						.replace("cellNoValue", bvrfisHouseParam.getCellNo())
						.replace("floorNoValue", bvrfisHouseParam.getFloorNo())
						.replace("constructAreaValue", bvrfisHouseParam.getConstructArea())
						.replace("houseTypeValue", bvrfisHouseParam.getHouseProp())
						.replace("showNameValue", bvrfisHouseParam.getShowName())
						.replace("houseAddressValue", bvrfisHouseParam.getAddress());
				// 查询的json串
				log.info(houseQueryParam);
				WrapperQueryBuilder wqb = QueryBuilders.wrapperQuery(houseQueryParam);
				SearchResponse searchResponse = client.prepareSearch(houseindex)
						.setTypes(housetype).setSize(Integer.parseInt(unCertainSize)).setQuery(wqb).get();
				SearchHit[] hits = searchResponse.getHits().getHits();
				for (SearchHit hit : hits) {
					// 返回内容
					String bvdfHouseJson = hit.getSourceAsString();
					BvdfHouseParam bvdfHouseParam = (BvdfHouseParam) UtilTool.jsonToObj(bvdfHouseJson, BvdfHouseParam.class);
					// 完全匹配有匹配结果的话不再新增
					BmatchAnResultParam bmatchAnResultParam = new BmatchAnResultParam();
					bmatchAnResultParam.setWqbusiid(bvdfHouseParam.getHouseid());
					bmatchAnResultParam.setPercent(new BigDecimal("100.00"));
					BmatchAnResultInfo bmatchIsExists = bmatchAnResultService.selectBmatchAnResult(bmatchAnResultParam);
					if (null != bmatchIsExists) {
						return;
					}
					BmatchAnResultInfo bmatchAnResultInfo = new BmatchAnResultInfo();
					bmatchAnResultInfo.setMatchid(UtilTool.UUID());
					bmatchAnResultInfo.setLogid(logId);
					// 维修资金数据ID
					bmatchAnResultInfo.setWxbusiid(bvrfisHouseParam.getSysGuid());
					bmatchAnResultInfo.setCenterid(bvdfHouseParam.getDataCenterId());
					bmatchAnResultInfo.setWqbusiid(bvdfHouseParam.getHouseid());
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
					bmatchAnResultInfo.setRemark("房屋根据自然幢疑似匹配");
					// 单位信息表
					bmatchAnResultInfo.setMatchtype(MatchTypeEnum.HOUSE.getCode());
					// todo 创建人 待确定
					bmatchAnResultInfo.setInuser("无");
					//bmatchAnResultInfo.setInuser(httpSession.getAttribute(GCC.SESSION_KEY_USERNAME).toString());
					bmatchAnResultInfo.setIndate(UtilTool.nowTime());
					// 修改人
					bmatchAnResultInfo.setEdituser(null);
					bmatchAnResultInfo.setEditdate(null);
					bmatchAnResultInfo.setVersion(new BigDecimal(bvdfHouseParam.getVersionnumber()));
					// 先删除再新增匹配结果表，同事务
					bvrfisService.delAndInsertBmatchAnResult(bmatchAnResultInfo);
					paramListForDel.add(bvrfisHouseParam);
				}
			} catch (MsgException e) {
				log.error(e + "bvrfisHouseParam参数为：{}", bvrfisHouseParam);
			}
		});
		bvrfisHouseParamList.removeAll(paramListForDel);
	}
	/**
	 * @Author: yinxunyang
	 * @Description: 房屋根据小区疑似匹配
	 * @Date: 2019/12/19 19:15
	 * @param:
	 * @return:
	 */
	private void unCertainHouseByRegion(List<BvrfisHouseParam> bvrfisHouseParamList, TransportClient client, HttpSession httpSession, String logId) {
		// 匹配成功后需要从bvrfisHouseParamList移除的List
		List<BvrfisHouseParam> paramListForDel = new ArrayList<>();
		// 房屋根据小区疑似匹配
		String houseQueryJson = bvrfisService.organizeQueryEsByJson("elasticSearch/house/unCertainHouseByRegion.json");
		// 遍历房屋信息和elasticsearch
		bvrfisHouseParamList.forEach(bvrfisHouseParam -> {
			try {
				// 查询bvdf的小区信息
				BDataRelationParam bDataRelationParam = new BDataRelationParam();
				bDataRelationParam.setWxBusiId(bvrfisHouseParam.getRegionNo());
				BDataRelation bDataRelation = bDataRelationService.selectBDataRelation(bDataRelationParam);
				if (null == bDataRelation) {
					return;
				}
				String houseQueryParam = houseQueryJson.replace("regionNoValue", bDataRelation.getWqBusiId())
						.replace("cellNoValue", bvrfisHouseParam.getCellNo())
						.replace("floorNoValue", bvrfisHouseParam.getFloorNo())
						.replace("constructAreaValue", bvrfisHouseParam.getConstructArea())
						.replace("houseTypeValue", bvrfisHouseParam.getHouseProp())
						.replace("showNameValue", bvrfisHouseParam.getShowName())
						.replace("houseAddressValue", bvrfisHouseParam.getAddress());
				// 查询的json串
				log.info(houseQueryParam);
				WrapperQueryBuilder wqb = QueryBuilders.wrapperQuery(houseQueryParam);
				SearchResponse searchResponse = client.prepareSearch(houseindex)
						.setTypes(housetype).setSize(Integer.parseInt(unCertainSize)).setQuery(wqb).get();
				SearchHit[] hits = searchResponse.getHits().getHits();
				for (SearchHit hit : hits) {
					// 返回内容
					String bvdfHouseJson = hit.getSourceAsString();
					BvdfHouseParam bvdfHouseParam = (BvdfHouseParam) UtilTool.jsonToObj(bvdfHouseJson, BvdfHouseParam.class);
					// 完全匹配有匹配结果的话不再新增
					BmatchAnResultParam bmatchAnResultParam = new BmatchAnResultParam();
					bmatchAnResultParam.setWqbusiid(bvdfHouseParam.getHouseid());
					bmatchAnResultParam.setPercent(new BigDecimal("100.00"));
					BmatchAnResultInfo bmatchIsExists = bmatchAnResultService.selectBmatchAnResult(bmatchAnResultParam);
					if (null != bmatchIsExists) {
						return;
					}
					BmatchAnResultInfo bmatchAnResultInfo = new BmatchAnResultInfo();
					bmatchAnResultInfo.setMatchid(UtilTool.UUID());
					bmatchAnResultInfo.setLogid(logId);
					// 维修资金数据ID
					bmatchAnResultInfo.setWxbusiid(bvrfisHouseParam.getSysGuid());
					bmatchAnResultInfo.setCenterid(bvdfHouseParam.getDataCenterId());
					bmatchAnResultInfo.setWqbusiid(bvdfHouseParam.getHouseid());
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
					bmatchAnResultInfo.setRemark("房屋根据小区疑似匹配");
					// 单位信息表
					bmatchAnResultInfo.setMatchtype(MatchTypeEnum.HOUSE.getCode());
					// todo 创建人 待确定
					bmatchAnResultInfo.setInuser("无");
					//bmatchAnResultInfo.setInuser(httpSession.getAttribute(GCC.SESSION_KEY_USERNAME).toString());
					bmatchAnResultInfo.setIndate(UtilTool.nowTime());
					// 修改人
					bmatchAnResultInfo.setEdituser(null);
					bmatchAnResultInfo.setEditdate(null);
					bmatchAnResultInfo.setVersion(new BigDecimal(bvdfHouseParam.getVersionnumber()));
					// 先删除再新增匹配结果表，同事务
					bvrfisService.delAndInsertBmatchAnResult(bmatchAnResultInfo);
					paramListForDel.add(bvrfisHouseParam);
				}
			} catch (MsgException e) {
				log.error(e + "bvrfisHouseParam参数为：{}", bvrfisHouseParam);
			}
		});
		bvrfisHouseParamList.removeAll(paramListForDel);
	}


}
