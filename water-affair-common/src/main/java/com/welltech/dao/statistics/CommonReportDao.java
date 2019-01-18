/**
 * 
 */
package com.welltech.dao.statistics;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.welltech.dto.WtAreaStationDto;
import com.welltech.entity.WtStationMonitor;

/**
 * Created by Zhujia at 2017年8月15日 下午11:19:38
 */
public interface CommonReportDao {

	/**
	 * 节点列表
	 *
	 * @return
	 */
	List<WtAreaStationDto> getAreaStationNode();

	/**
	 * 节点列表，根据登录用户所属公司
	 *
	 * @return
	 * @Author Man Zhiwei add by 18-11-14
	 */
	List<WtAreaStationDto> getAreaStationNodeByCompanyId(Integer companyId);
	/**
	* @Author  Man Zhiwei    
	* @Comment 节点列表，根据登录用户所属公司
	* @Param   [companyId]   
	* @Date        2018-12-17 15:49
	*/
	List<WtAreaStationDto> getAreaStationNodeByParentId(Integer companyId);
	

	/**
	 * @param mcu
	 * @return
	 */
	List<WtStationMonitor> getHeichouDisplay(@Param("mcu") String mcu);

	/**
	 * @param result
	 * @return
	 */
	String isPollution(String result);

	/**
	 * @param paraMap
	 * @return
	 */
	boolean isPollution(Map<String, String> paraMap);

	/**
	 * @return
	 */
	List<WtAreaStationDto> getAreaNode();

	/**
	* @Author  Man Zhiwei    
	* @Comment 根据公司Id查找节点信息
	* @Param   [companyId]   
	* @Date        2018-11-14 14:42
	*/
	List<WtAreaStationDto> getAreaNodeByCompanyId(Integer companyId);
	/**
	* @Author  Man Zhiwei
	* @Comment 根据总公司Id查找节点信息
	* @Param   [companyId]
	* @Date        2018-12-17 15:44
	*/
    List<WtAreaStationDto> getAreaNodeByParentId(Integer companyId);
}
