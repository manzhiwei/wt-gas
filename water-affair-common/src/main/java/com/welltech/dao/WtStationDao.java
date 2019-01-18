package com.welltech.dao;

import java.util.List;
import java.util.Map;

import com.welltech.entity.WtCompany;
import com.welltech.entity.WtStation;

public interface WtStationDao {

	/**
	 * 通过站点名称查询
	 * @param point
	 * @return
	 */
	List<WtStation> findWtStationsBySearchingPoint(String point);
	
	/**
	 * 有站点的区域
	 * @return
	 */
	List<WtCompany> findHasStationsCompany();

	/**
	 * 根据id查找站点信息
	 * @param stationId
	 * @return
	 */
    WtStation findStationById(Integer stationId);
    
    /**
     * 所有站点
     * @return
     */
    List<WtStation> findAllStations();

    /**
    * @Author  Man Zhiwei
    * @Comment 查询所有站点信息，根据公司ID
    * @Param   [companyId]
    * @Date        2018-11-16 13:31
    */
    List<WtStation> findAllStationsByCompanyId(Integer companyId);
	/**
	 * 根据测点类型查找测点
	 * @param typeCode
	 * @return
	 */
	List<WtStation> findStationByType(String typeCode);
	
	/**
     * 24小时内活动的站点
     * @return
     */
    List<WtStation> findStationBy24Hour();
	/**
	* @Author  Man Zhiwei
	* @Comment 下面的方法只是简单的添加了一个公司的id,作为筛选条件
	* @Param   [companyId]
	* @Date        2018-11-16 9:33
	*/
    List<WtStation> findStationBy24HourAndCompanyId(Integer companyId);
	/**
	* @Author  Man Zhiwei    
	* @Comment 另一种需求，根据总公司的id,进行筛选
	* @Param   [companyId]
	* @Date        2018-12-17 14:56
	*/
	List<WtStation> findStationBy24HourAndParentId(Integer companyId);
	/**
	 * 查询时所有区域
	 */
	List<WtCompany> findAllCompanys();

	/**
	 * 通过所属区域id查询
	 * @param companyId
	 * @return
	 */
	List<WtStation> findStationsByCompanyId(Integer companyId);


	/**
	 * 获取评价类型
	 * @return
	 */
    String getJudgeType();

    WtStation findFirstStationByCompanyId(Integer companyId);

	List<WtCompany> findCompanyByCompanyId(Integer companyId);

	List<WtCompany> findCompanyByParentId(Integer companyId);


    List<WtStation> findAllStationsByParentId(Integer companyIdByUser);

	WtStation findFirstStationByParentId(Integer companyId);
}
