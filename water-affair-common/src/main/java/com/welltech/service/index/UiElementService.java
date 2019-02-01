package com.welltech.service.index;

import java.util.List;
import java.util.Map;

import com.welltech.dto.PointDto;
import com.welltech.entity.WtParam;
import com.welltech.entity.WtStation;

/**
 * 页面元素服务接口
 * @author wangxin
 *
 */
public interface UiElementService {
	
	/**
	 * 查询所有测点与id对应关系
	 * @return
	 */
	List<PointDto> findAllPointDtos();

	
	/**
	 * 参数与实体对应map
	 * @return
	 */
	Map<String, WtParam> getParams();

	/**
	* @Author  Man Zhiwei
	* @Comment 32路参数表头
	* @Param
	* @Date        2019-01-22 14:48
	*/
	List<WtParam> getParamList();

	/**
	* @Author  Man Zhiwei
	* @Comment 历史数据，日，时，分
	* @Param
	* @Date        2019-01-31 10:33
	*/
	List<WtParam> getParamListByShuCaiYi();
	/**
	 * 标准站点，分析仪数据菜单信息
	 * @return
	 */
	Map<String, WtParam> getParams2();
	
	/**
	 * 要显示的参数
	 * @return
	 */
	List<WtParam> getDisplayParams();

	/**
	 * 站点别名参数
	 * @param integer
	 * @return
	 */
	Map<String, WtParam> getParamsByStationId(Integer integer);

	/**
	 * 站点别名参数，历史数据参数别名，包括最小值，最大值，平均值，总累计
	 * @param integer
	 * @return
	 */
	Map<String, WtParam> getParamsByStationId2(Integer integer);

	/**
	 * 显示参与统计的参数
	 * @param station
	 * @return
	 */
	Map<String, WtParam> getDisplayParamsByStationId(WtStation station);

	/**
	 * 获取评价标准
	 * @return
	 */
	String getJudgeType();
}
