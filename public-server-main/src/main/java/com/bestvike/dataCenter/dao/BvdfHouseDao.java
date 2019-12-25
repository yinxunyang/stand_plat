package com.bestvike.dataCenter.dao;

import com.bestvike.dataCenter.param.BvdfBldParam;
import com.bestvike.dataCenter.param.BvdfCorpParam;
import com.bestvike.dataCenter.param.BvdfHouseParam;
import com.bestvike.dataCenter.param.BvdfRegionParam;
import org.apache.ibatis.annotations.Param;
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
	 * @Description: 查询企业信息
	 * @Date: 2019/12/5 11:34
	 * @param:
	 * @return:
	 */
	List<BvdfCorpParam> queryBvdfCorpInfo(@Param("param") BvdfCorpParam queryParam);

	/**
	 * @Author: yinxunyang
	 * @Description: 查询小区信息
	 * @Date: 2019/12/5 11:34
	 * @param:
	 * @return:
	 */
	List<BvdfRegionParam> queryBvdfRegionInfo(@Param("param") BvdfRegionParam queryParam);
	/**
	 * @Author: yinxunyang
	 * @Description: 查询自然幢信息
	 * @Date: 2019/12/5 11:34
	 * @param:
	 * @return:
	 */
	List<BvdfBldParam> queryBvdfBldInfo(@Param("param") BvdfBldParam queryParam);

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
	BvdfCorpParam selectCorpNameByCorpNo(String corpNo);

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
