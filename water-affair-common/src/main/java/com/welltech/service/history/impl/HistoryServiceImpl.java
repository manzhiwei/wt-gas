package com.welltech.service.history.impl;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

import com.welltech.dao.history.*;
import com.welltech.dto.*;
import com.welltech.entity.WtControlLimit;
import com.welltech.entity.WtProtocolDay;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.welltech.dao.WtDataRawDao;
import com.welltech.dao.statistics.StationStatisticDao;
import com.welltech.entity.WtDataRaw;
import com.welltech.entity.WtStation;
import com.welltech.framework.aop.pagination.bean.MyPage;
import com.welltech.framework.aop.pagination.bean.Page;
import com.welltech.framework.aop.pagination.bean.Pagination;
import com.welltech.service.history.HistoryService;

@Service
public class HistoryServiceImpl implements HistoryService{
	
	@Autowired
	private WtDataRawDao wtDataRawDao;
	
	@Autowired
	private StationStatisticDao stationStatisticDao;

	@Autowired
	private WtControlAirDao wtControlAirDao;

	@Autowired
	private WtControlDao wtControlDao;

	@Autowired
	private WtControlLimitDao wtControlLimitDao;

	@Autowired
	private WtProtocolDayDao wtProtocolDayDao;

	@Autowired
	private WtGasAirDao wtGasAirDao;

	@Autowired
	private WtGasAlarmStatDao wtGasAlarmStatDao;

	@Autowired
	private WtGasVariablesDao wtGasVariablesDao;

	@Override
	public List<WtDataRawDto> listHistoryWtDataRawDto(
			MyPage myPage,
			Integer[] pointIds,
			Date startTime,
			Date endTime) {

		Map<String, Object> map = new LinkedHashMap<String, Object>();
		Page page = new Pagination();
		page.setCurrentPage(myPage.getCurrentPage());

		map.put("page", page);
		map.put("pointIds", pointIds);
		startTime = getToDay(startTime);
		map.put("startTime", startTime);
		map.put("endTime", endTime);
		List<WtDataRawDto> result = wtDataRawDao.findPageWtDataRaws(map);

		if(result != null){
			Field[] fields = WtDataRaw.class.getDeclaredFields();
			for (WtDataRawDto dto : result) {
				Map<String, Object> paramValues = new LinkedHashMap<>();
				
				for(Field f: fields){
					f.setAccessible(true);
					if(f.getName().matches("^[p][1-9]?\\d$")){
						//匹配p1~p32
						try {
							paramValues.put(f.getName(), f.get(dto));
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
				
				dto.setParamValues(paramValues);
			}
		}
		myPage.setCurrentPage(page.getCurrentPage());
		myPage.setTotalPages(page.getTotalPages());

		//myPage.setTotalPages(page.getTotalPages());


		return result;
	}

	@Override
	public List<WtDataRawDto> listHistoryWtDataRawDto(Integer[] pointIds, Date startTime, Date endTime) {
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		map.put("pointIds", pointIds);
		startTime = getToDay(startTime);
		map.put("startTime", startTime);
		map.put("endTime", endTime);
		List<WtDataRawDto> result = wtDataRawDao.listWtDataRaws(map);

		if(result != null){
			Field[] fields = WtDataRaw.class.getDeclaredFields();
			for (WtDataRawDto dto : result) {
				Map<String, Object> paramValues = new LinkedHashMap<>();

				for(Field f: fields){
					f.setAccessible(true);
					if(f.getName().matches("^[p][1-9]?\\d$")||f.getName().matches("^[p][1-9]?\\dFlag?$")){
						//匹配p1~p32
						try {
							paramValues.put(f.getName(), f.get(dto));
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}

				dto.setParamValues(paramValues);
			}
		}

		for (int i = 0; i < result.size(); i++) {
			Map<String,Object> paramValues = result.get(i).getParamValues();
			for (int j = 1; j <= 32 ; j++) {
				if(paramValues.get("p"+j)!=null){
					BigDecimal tempValue = (BigDecimal) paramValues.get("p"+j);
					String tempValue1 = (String) paramValues.get("p"+j+"Flag");
					paramValues.put("p"+j,tempValue.setScale(2,BigDecimal.ROUND_HALF_UP)+tempValue1);
				}
			}
		}

		return result;
	}

	/* (non-Javadoc)
         * @see com.welltech.service.history.HistoryService#listHistoryWtData(com.welltech.framework.aop.pagination.bean.MyPage, java.lang.Integer, java.util.Date, java.util.Date)
         */
	@Override
	public Object listHistoryWtData(MyPage myPage, Integer pointId, Date startTime, Date endTime, String dataType) {
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		Page page = new Pagination();
		page.setCurrentPage(myPage.getCurrentPage());
		map.put("page", page);
		startTime = getToDay(startTime);
		map.put("startTime", startTime);
		map.put("endTime", endTime);
		map.put("dataType", dataType);

		List<WtDataRawDto> result = null;
		if("5".equals(dataType)){
			map.put("pointIds", new Integer[]{pointId});
			result = wtDataRawDao.findPageWtDataRaws(map);
		} else{
			map.put("pointId", pointId);
			result = wtDataRawDao.findPageWtDataRaw(map);
		}

		if(result != null){
			Field[] fields = WtDataRaw.class.getDeclaredFields();
			for (WtDataRawDto dto : result) {
				Map<String, Object> paramValues = new LinkedHashMap<>();
				
				for(Field f: fields){
					f.setAccessible(true);
					if(f.getName().matches("^[p][1-9]?\\d$")){
						//匹配p1~p32
						try {
							paramValues.put(f.getName(), f.get(dto));
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
				
				dto.setParamValues(paramValues);
			}
		}
		myPage.setTotalPages(page.getTotalPages());
		return result;
	}

	@Override
	public WtStation getStation(Integer stationId) {
		return stationStatisticDao.findWtStationById(stationId);
	}


	@Override
	public List<WtControlDto> listHistoryWtControl(MyPage myPage, Integer[] pointIds, Date startTime, Date endTime) {
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		Page page = new Pagination();
		page.setCurrentPage(myPage.getCurrentPage());
		map.put("page", page);
		map.put("pointIds", pointIds);
		startTime = getToDay(startTime);
		map.put("startTime", startTime);
		map.put("endTime", endTime);
		List<WtControlDto> result = wtControlDao.findPageWtControl(map);
		myPage.setTotalPages(page.getTotalPages());
		return result;
	}

	@Override
	public List<WtControlAirDto> listHistoryWtControlAir(MyPage myPage, Integer[] pointIds, Date startTime, Date endTime) {
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		Page page = new Pagination();
		page.setCurrentPage(myPage.getCurrentPage());
		map.put("page", page);
		map.put("pointIds", pointIds);
		startTime = getToDay(startTime);
		map.put("startTime", startTime);
		map.put("endTime", endTime);
		List<WtControlAirDto> result = wtControlAirDao.findPageWtControlAir(map);
		myPage.setTotalPages(page.getTotalPages());
		return result;
	}

	@Override
	public List<WtControlLimitDto> listHistoryWtControlLimit(MyPage myPage, Integer[] pointIds, Date startTime, Date endTime) {
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		Page page = new Pagination();
		page.setCurrentPage(myPage.getCurrentPage());
		map.put("page", page);
		map.put("pointIds", pointIds);
		startTime = getToDay(startTime);
		map.put("startTime", startTime);
		map.put("endTime", endTime);
		List<WtControlLimitDto> result = wtControlLimitDao.findPageWtControlLimit(map);
		myPage.setTotalPages(page.getTotalPages());
		return result;
	}

	/*@Override
	public List<WtProtocolDayDto> listdataCollectionHistory(MyPage myPage, Integer[] pointIds, Date startTime, Date endTime) {
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		Page page = new Pagination();
		page.setCurrentPage(myPage.getCurrentPage());
		map.put("page", page);
		map.put("pointIds", pointIds);
		startTime = getToDay(startTime);
		map.put("startTime", startTime);
		map.put("endTime", endTime);
		List<WtProtocolDayDto> result = wtProtocolDayDao.findPageWtProtocolDay(map);
		if(result != null){
			Field[] fields = WtProtocolDay.class.getDeclaredFields();
			for (WtProtocolDayDto dto : result) {
				Map<String, Object> paramValues = new LinkedHashMap<>();

				for(Field f: fields){
					f.setAccessible(true);
					if(f.getName().matches("^[p][1-9]?\\d((Avg)?|(Min)?|(Max)?|(Cou)?)$")){
						//匹配p1~p32
						try {
							paramValues.put(f.getName(), f.get(dto));//p* - dto
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}

				dto.setParamValues(paramValues);
			}
		}
		myPage.setTotalPages(page.getTotalPages());
		return result;
	}*/

	@Override
	public List<WtProtocolDayDto> listdataCollectionHistory(Integer[] pointIds, Date startTime, Date endTime,String queryType) {
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		map.put("pointIds", pointIds);
		startTime = getToDay(startTime);
		map.put("startTime", startTime);
		map.put("endTime", endTime);
		List<WtProtocolDayDto> result = null;
		if("1".equals(queryType)){
			result = wtProtocolDayDao.listWtProtocolDay(map);
		}else if("0".equals(queryType)){
			result = wtProtocolDayDao.listWtProtocolHour(map);
		}else if("2".equals(queryType)){
			result = wtProtocolDayDao.listWtProtocolMinuter(map);
		}

		if(result != null){
			Field[] fields = WtProtocolDay.class.getDeclaredFields();
			for (WtProtocolDayDto dto : result) {
				Map<String, Object> paramValues = new LinkedHashMap<>();

				for(Field f: fields){
					f.setAccessible(true);
					if(f.getName().matches("^[p][1-9]?\\d((Avg)?|(Min)?|(Max)?|(Cou)?)$")){
						//匹配p1~p32
						try {
							paramValues.put(f.getName(), f.get(dto));//p* - dto
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}

				dto.setParamValues(paramValues);
			}
		}
		return result;
	}

	@Override
	public List<WtGasAirDto> listGasAirHistory(Integer[] pointIds, Date startTime, Date endTime) {
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		Page page = new Pagination();
		map.put("pointIds", pointIds);
		startTime = getToDay(startTime);
		map.put("startTime", startTime);
		map.put("endTime", endTime);
		List<WtGasAirDto> result = wtGasAirDao.listWtGasAir(map);
		return result;
	}

	private Date getToDay(Date startTime) {
		if(startTime == null){
			Calendar cal = Calendar.getInstance();
			cal.set(Calendar.HOUR_OF_DAY,0);
			cal.set(Calendar.MINUTE,0);
			cal.set(Calendar.SECOND,0);
			cal.set(Calendar.MILLISECOND,0);
			startTime = cal.getTime();
		}
		return startTime;
	}

	@Override
    public List<WtGasAlarmStatDto> listGasAlarmStatHistory(Integer[] pointIds, Date startTime, Date endTime) {
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		map.put("pointIds", pointIds);
		startTime = getToDay(startTime);
		map.put("startTime", startTime);
		map.put("endTime", endTime);
		List<WtGasAlarmStatDto> result = wtGasAlarmStatDao.listWtGasAlarmStat(map);
		return result;
    }

	@Override
	public List<WtGasVariablesDto> listGasVariablesHistory(Integer[] pointIds, Date startTime, Date endTime) {
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		map.put("pointIds", pointIds);
		startTime = getToDay(startTime);
		map.put("startTime", startTime);
		map.put("endTime", endTime);
		List<WtGasVariablesDto> result = wtGasVariablesDao.listWtGasVariables(map);
		return result;
	}
}
