package com.bestvike.pub.service.impl;

import com.bestvike.mid.entity.MidHouseInfo;
import com.bestvike.mid.service.MidHouseService;
import com.bestvike.pub.dao.BvdfHouseDao;
import com.bestvike.pub.enums.ReturnCode;
import com.bestvike.pub.exception.MsgException;
import com.bestvike.pub.param.BvdfHouseParam;
import com.bestvike.pub.param.EsHouseParam;
import com.bestvike.pub.service.BvdfHouseService;
import com.bestvike.pub.service.BvdfService;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class BvdfServiceImpl implements BvdfService {
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
	 * 房屋信息表(arc_houseinfo)最大查询条数,防止内存溢出
	 */
	private static final Integer HOUSE_MAX_NUM = 20000;
	@Autowired
	private BvdfHouseService bvdfHouseService;
	@Autowired
	private MidHouseService midHouseService;
	@Autowired
	private BvdfHouseDao bvdfHouseDao;
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
		try (TransportClient client = new PreBuiltTransportClient(Settings.builder().put("cluster.name", esClusterName).build())
				.addTransportAddress(new TransportAddress(InetAddress.getByName(esIP), Integer.parseInt(esPort)))) {
			// 遍历新增房屋信息和elasticsearch
			bvdfHouseParamList.forEach(bvdfHouseParam -> {
				// 新增房屋信息和迁移elasticsearch
				try {
					// 根据主键查询中间库房屋信息
					MidHouseInfo midHouseInfo = midHouseService.queryMidHouseInfoById(bvdfHouseParam);
					// 组织往elasticSearch推送的数据
					EsHouseParam esHouseParam = organizeEsHouseParam(bvdfHouseParam);
					bvdfHouseService.insertCopyHouseAndEs(bvdfHouseParam, client, midHouseInfo, esHouseParam);
				} catch (MsgException e) {
					log.error(e + "bvdfHouseParam参数为：{}", bvdfHouseParam);
				}
			});
		} catch (UnknownHostException e) {
			log.error("创建elasticsearch客户端连接失败" + e);
			throw new MsgException(ReturnCode.sdp_sys_error, "创建elasticsearch客户端连接失败");
		}
	}

	/**
	 * @Author: yinxunyang
	 * @Description: 组织往elasticSearch推送的数据
	 * @Date: 2019/12/10 13:29
	 * @param:
	 * @return:
	 */
	private EsHouseParam organizeEsHouseParam(BvdfHouseParam bvdfHouseParam) {
		try {
			EsHouseParam esHouseParam = new EsHouseParam();
			esHouseParam.setId(bvdfHouseParam.getSysguid());
			String corpName = null;
			if (!StringUtils.isEmpty(bvdfHouseParam.getCorpno())) {
				corpName = bvdfHouseDao.selectCorpNameByCorpNo(bvdfHouseParam.getCorpno());
			}
			if (StringUtils.isEmpty(corpName)) {
				corpName = "无";
			}
			// 开发企业名称
			esHouseParam.setDevelopName(corpName);
			String bldName = null;
			if (!StringUtils.isEmpty(bvdfHouseParam.getBldno())) {
				bldName = bvdfHouseDao.selectBldNameByBldNo(bvdfHouseParam.getBldno());
			}
			if (StringUtils.isEmpty(bldName)) {
				bldName = "无";
			}
			// 楼幢名称
			esHouseParam.setBldName(bldName);
			String cellName = null;
			if (!StringUtils.isEmpty(bvdfHouseParam.getCellno())) {
				cellName = bvdfHouseDao.selectCellNameByCellNo(bvdfHouseParam.getCellno());
			}
			if (StringUtils.isEmpty(cellName)) {
				cellName = "无";
			}
			// 单元名称
			esHouseParam.setCellName(cellName);
			esHouseParam.setFloorName(bvdfHouseParam.getFloorname());
			esHouseParam.setRoomno(bvdfHouseParam.getRoomno());
			esHouseParam.setBuyCertNos(bvdfHouseParam.getBuycertnos());
			esHouseParam.setBuyNames(bvdfHouseParam.getBuynames());
			esHouseParam.setHouseAddress(bvdfHouseParam.getAddress());
			return esHouseParam;
		} catch (Exception e) {
			log.error("组织往elasticSearch推送的数据失败" + e);
			throw new MsgException(ReturnCode.sdp_select_fail, "组织往elasticSearch推送的数据失败");
		}
	}
}
