/**
 * 
 */
package com.welltech.dao.repair;

import java.util.List;
import java.util.Map;

import com.welltech.dto.WtStationRepairDto;

/**
 * Created by Zhujia at 2017年8月27日 下午2:27:53
 */
public interface RepairRecordDao {

	/**
	 * @param map
	 * @return
	 */
	List<WtStationRepairDto> findPageRepairList(Map<String, Object> map);

    List<WtStationRepairDto> findRepairList(Map<String, Object> map);
    
    /**
    * @Author  Man Zhiwei    
    * @Comment 根据公司ID 查找维修记录
    * @Param   [map]   
    * @Date        2018-11-16 15:52
    */
    List<WtStationRepairDto> findRepairListByCompanyId(Map<String,Object> map);
    
    int deleteWtStationRepair(String id);
}
