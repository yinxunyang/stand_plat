package com.bestvike.bvdf.dao;

import com.bestvike.bvdf.param.BvdfHouseParam;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @Author: yinxunyang
 * @Description: bvdf房屋信息Dao
 * @Date: 2019/12/5 11:22
 * @param:
 * @return:
 */
@Repository
public interface BvdfHouseDao {
	/**
	 * @Author: yinxunyang
	 * @Description: 查询房屋信息
	 * @Date: 2019/12/5 11:34
	 * @param:
	 * @return:
	 */
	List<BvdfHouseParam> queryBvdfHouseInfo(Map<String, Object> parameterMap);

	/**
	 * @Author: yinxunyang
	 * @Description: 根据企业编号查询企业名称
	 * @Date: 2019/12/10 13:50
	 * @param:
	 * @return:
	 */
	String selectCorpNameByCorpNo(String corpNo);

	/**
	 * @Author: yinxunyang
	 * @Description: 根据楼幢编号查询楼幢名称
	 * @Date: 2019/12/10 14:02
	 * @param:
	 * @return:
	 */
	String selectBldNameByBldNo(String bldNo);

	/**
	 * @Author: yinxunyang
	 * @Description: 根据单元编号查询单元名称
	 * @Date: 2019/12/10 14:12
	 * @param:
	 * @return:
	 */
	String selectCellNameByCellNo(Map<String, Object> parameterMap);
}
