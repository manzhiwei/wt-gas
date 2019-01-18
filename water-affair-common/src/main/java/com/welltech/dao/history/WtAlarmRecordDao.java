package com.welltech.dao.history;

import java.util.List;
import java.util.Map;

import com.welltech.entity.WtAlarmRecord;

/**
 * 报警记录持久化接口
 * @author WangXin
 *
 */
public interface WtAlarmRecordDao {

	/**
	 * 分页查询报警记录
	 * @param map
	 * @return
	 */
	List<WtAlarmRecord> findPageWtAlarmRecord(Map<String, Object> map);

	/**
	* @Author  Man Zhiwei
	* @Comment 分页查询报警记录，只是添加一个限定条件，公司ID
	* @Param   [map]
	* @Date        2018-11-16 11:09
	*/
	List<WtAlarmRecord> findPageWtAlarmRecordByCompanyId(Map<String,Object> map);
	/**
	 * 假删除
	 * @param id
	 * @return
	 */
	int updateDeleteFlag(Integer id);
	
}
