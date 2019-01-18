package com.welltech.service.history;

import java.util.Date;
import java.util.List;

import com.welltech.dto.*;
import com.welltech.entity.WtControlLimit;
import com.welltech.entity.WtGasVariables;
import com.welltech.entity.WtStation;
import com.welltech.framework.aop.pagination.bean.MyPage;

public interface HistoryService {
	
	/**
	 * 历史查询
	 * @param myPage
	 * @param pointIds
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	List<WtDataRawDto> listHistoryWtDataRawDto(MyPage myPage,
											   Integer[] pointIds,
											   Date startTime, Date endTime);

	/**
	 * @param myPage
	 * @param pointId
	 * @param startTime
	 * @param endTime
	 * @param dataType 数据类型
	 * @return
	 */
	Object listHistoryWtData(MyPage myPage, Integer pointId, Date startTime, Date endTime, String dataType);
	
	/**
	 * 获取站点
	 * @param stationId
	 * @return
	 */
	WtStation getStation(Integer stationId);

	List<WtControlAirDto> listHistoryWtControlAir(MyPage myPage, Integer[] pointIds, Date startTime, Date endTime);

	List<WtControlDto> listHistoryWtControl(MyPage myPage, Integer[] pointIds, Date startTime, Date endTime);

	List<WtControlLimitDto> listHistoryWtControlLimit(MyPage myPage, Integer[] pointIds, Date startTime, Date endTime);

	List<WtProtocolDayDto> listdataCollectionHistory(MyPage myPage, Integer[] pointIds, Date startTime, Date endTime);

	List<WtProtocolDayDto> listdataCollectionHistory(MyPage myPage, Integer[] pointIds, Date startTime, Date endTime,String queryType);

	List<WtGasAirDto> listGasAirHistory(MyPage myPage, Integer[] pointIds, Date startTime, Date endTime);

	List<WtGasAlarmStatDto> listGasAlarmStatHistory(MyPage myPage, Integer[] pointIds, Date startTime, Date endTime);

	List<WtGasVariablesDto> listGasVariablesHistory(MyPage myPage, Integer[] pointIds, Date startTime, Date endTime);
}
