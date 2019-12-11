package com.bestvike.bvrfis.service.impl;

import com.bestvike.bvdf.param.EsHouseParam;
import com.bestvike.bvrfis.dao.BvrfisHouseDao;
import com.bestvike.bvrfis.param.BvrfisBldParam;
import com.bestvike.bvrfis.param.BvrfisHouseParam;
import com.bestvike.bvrfis.param.BvrfisOwnerInfoParam;
import com.bestvike.bvrfis.service.BvrfisHouseService;
import com.bestvike.bvrfis.service.BvrfisService;
import com.bestvike.commons.exception.MsgException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

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
	@Autowired
	private BvrfisHouseService bvrfisHouseService;
	@Autowired
	private BvrfisHouseDao bvrfisHouseDao;

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
		bvrfisHouseParamList.forEach(bvrfisHouseParam -> {
			// 组织跟elasticSearch匹配的数据
			EsHouseParam esHouseParam = organizeMatchEsParam(bvrfisHouseParam);
			// todo 组织查询es打分的语句
		});
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
		if (!StringUtils.isEmpty(developNo)) {
			developName = bvrfisHouseDao.selectDevelopNameByDevelopNo(developNo);
		}
		if (StringUtils.isEmpty(developName)) {
			developName = "无";
		}
		esHouseParam.setDevelopName(developName);
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
		String buyCertNos = null;
		String buyNames = null;
		if (!StringUtils.isEmpty(houseGuid)) {
			Map<String, Object> parameterMap = new HashMap<>();
			parameterMap.put("houseGuid", houseGuid);
			// 查询正常状态的业主
			parameterMap.put("state", "0");
			BvrfisOwnerInfoParam bvrfisOwnerInfoParam = bvrfisHouseDao.selectOwnerInfoByHouseId(parameterMap);
			String certNo = bvrfisOwnerInfoParam.getCertNo();
			String ownerName = bvrfisOwnerInfoParam.getOwnerName();
			// 查询共有人

		}
		if (StringUtils.isEmpty(buyCertNos)) {
			buyCertNos = "无";
		}
		if (StringUtils.isEmpty(buyNames)) {
			buyNames = "无";
		}
		esHouseParam.setBuyCertNos(buyCertNos);
		esHouseParam.setBuyNames(buyNames);
		esHouseParam.setHouseAddress(bvrfisHouseParam.getAddress());
		return esHouseParam;
	}
}