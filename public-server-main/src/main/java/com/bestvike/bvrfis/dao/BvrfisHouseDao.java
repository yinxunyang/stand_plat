package com.bestvike.bvrfis.dao;

import com.bestvike.bvrfis.param.BvrfisBldParam;
import com.bestvike.bvrfis.param.BvrfisHouseParam;
import com.bestvike.bvrfis.param.BvrfisOwnerInfoParam;
import com.bestvike.bvrfis.param.BvrfisShareOwnerInfoParam;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @Author: yinxunyang
 * @Description: 维修资金房屋信息Dao
 * @Date: 2019/12/5 11:22
 * @param:
 * @return:
 */
@Repository
public interface BvrfisHouseDao {
	/**
	 * @Author: yinxunyang
	 * @Description: 查询bvrfis房屋的数据
	 * @Date: 2019/12/10 17:25
	 * @param:
	 * @return:
	 */
	List<BvrfisHouseParam> queryBvrfisHouseInfo(Map<String, Object> parameterMap);

	/**
	 * @Author: yinxunyang
	 * @Description: 根据楼幢编号查询楼幢信息
	 * @Date: 2019/12/11 10:06
	 * @param:
	 * @return:
	 */
	BvrfisBldParam queryBldInfoByBldNo(String bldNo);
	/**
	 * @Author: yinxunyang
	 * @Description: 根据企业编号查询企业名称
	 * @Date: 2019/12/10 13:50
	 * @param:
	 * @return:
	 */
	String selectDevelopNameByDevelopNo(String developNo);
	/**
	 * @Author: yinxunyang
	 * @Description: 根据单元编号查询单元名称
	 * @Date: 2019/12/10 14:12
	 * @param:
	 * @return:
	 */
	String selectCellNameByNo(Map<String, Object> parameterMap);

	/**
	 * @Author: yinxunyang
	 * @Description: 根据楼层编号查询楼层名称
	 * @Date: 2019/12/11 13:08
	 * @param:
	 * @return:
	 */
	String selectFloorNameByFloorNo(Map<String, Object> parameterMap);

	/**
	 * @Author: yinxunyang
	 * @Description: 根据房屋Id查询业主信息
	 * @Date: 2019/12/11 13:50
	 * @param:
	 * @return:
	 */
	BvrfisOwnerInfoParam selectOwnerInfoByHouseId(Map<String, Object> parameterMap);

	/**
	 * @Author: yinxunyang
	 * @Description: 根据业主账户查询共有人信息
	 * @Date: 2019/12/11 14:06
	 * @param:
	 * @return:
	 */
	List<BvrfisShareOwnerInfoParam> selectShareOwnerInfoByHouseId(String subAccount);
}
