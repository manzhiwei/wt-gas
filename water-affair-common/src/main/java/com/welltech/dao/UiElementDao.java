package com.welltech.dao;

import java.util.List;

import com.welltech.dto.PointDto;
import com.welltech.security.entity.WtUser;

/**
 * 页面元素持久层接口
 * @author wangxin
 *
 */
public interface UiElementDao {
	/**
	 * 所有测点
	 * @return
	 */
	List<PointDto> findAllPointDtos();

	/**
	 * 查询一个公司的所有测点
	 * @Author  Man Zhiwei add by 18-11-14
	 */
	List<PointDto> findAllPointOnCompany(Integer companyId);
	/**
	* @Author  Man Zhiwei    
	* @Comment 查询总公司的所有测点
	* @Param   [companyIdByUser]   
	* @Date        2018-12-17 15:29
	*/
    List<PointDto> findAllPointOnParentId(Integer companyId);
}
