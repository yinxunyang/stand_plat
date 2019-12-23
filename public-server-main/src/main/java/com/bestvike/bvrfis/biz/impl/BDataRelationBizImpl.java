package com.bestvike.bvrfis.biz.impl;

import com.bestvike.bvrfis.biz.BDataRelationBiz;
import com.bestvike.bvrfis.entity.BDataRelation;
import com.bestvike.bvrfis.entity.BmatchAnResultInfo;
import com.bestvike.bvrfis.service.BDataRelationService;
import com.bestvike.bvrfis.service.BmatchAnResultService;
import com.bestvike.commons.exception.MsgException;
import com.bestvike.commons.utils.UtilTool;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author: yinxunyang
 * @Description: 维修资金挂接关系表biz实现类
 * @Date: 2019/12/10 17:06
 */
@Service
@Slf4j
public class BDataRelationBizImpl implements BDataRelationBiz {
	@Autowired
	private BmatchAnResultService bmatchAnResultService;
	@Autowired
	private BDataRelationService bDataRelationService;

	/**
	 * @Author: yinxunyang
	 * @Description: 建立挂接关系
	 * @Date: 2019/12/23 13:07
	 * @param:
	 * @return:
	 */
	@Override
	public void generateRelation(String matchType, String matchId) throws MsgException {
		// 查询b_matchAnResult
		BmatchAnResultInfo bmatchAnResultInfo = bmatchAnResultService.selectBmatchAnResultById(matchId);
		BDataRelation bDataRelation = new BDataRelation();
		bDataRelation.setRelationId(UtilTool.UUID());
		bDataRelation.setLinkType(bmatchAnResultInfo.getMatchtype());
		bDataRelation.setWxBusiId(bmatchAnResultInfo.getWxbusiid());
		bDataRelation.setCenterId(bmatchAnResultInfo.getCenterid());
		bDataRelation.setWqBusiId(bmatchAnResultInfo.getWqbusiid());
		// todo 待定
		bDataRelation.setBusId(UtilTool.UUID());
		bDataRelation.setVersion(bmatchAnResultInfo.getVersion());
		bDataRelation.setMatchId(bmatchAnResultInfo.getMatchid());
		// todo 待定
		bDataRelation.setInUser("无");
		bDataRelation.setInDate(UtilTool.nowTime());
		bDataRelation.setEditUser(null);
		bDataRelation.setEditDate(null);
		bDataRelationService.insertBDataRelation(bDataRelation);
		int i = 0;
	}
}
