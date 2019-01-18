package com.welltech.service;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.welltech.common.QuartzManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.welltech.dao.AlarmDao;
import com.welltech.dto.WaterLevelResult;
import com.welltech.dto.WaterLevelResultDetail;
import com.welltech.entity.WtAlarmRecord;
import com.welltech.entity.WtCompany;
import com.welltech.entity.WtDataRaw;
import com.welltech.entity.WtParam;
import com.welltech.entity.WtStation;
import com.welltech.entity.WtStationMonitor;
import com.welltech.entity.WtWaterLevel;
import com.welltech.service.statistics.WaterLevelService;
import com.welltech.common.MessageContants;

@Service
public class AlarmService {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private AlarmDao alarmDao;
	
	@Autowired
	private WaterLevelService waterLevelService;

	@Autowired
	private AlarmManageService alarmManageService;

	/**
	 * 保存网络断开告警
	 */
	public void saveShutdownAlarm(Integer stationId){
		WtStation station = alarmDao.findWtStationById(stationId);
		if(station == null){
			//查询不到测点，移除任务
			alarmManageService.removeShutdownJob(stationId);
			return;
		}
		WtCompany company = alarmDao.findWtCompanyById(station.getCompanyId());
		WtAlarmRecord record = new WtAlarmRecord();
		record.setAlarmTime(new Date());
		record.setCompanyId(company.getId());
		record.setCompanyName(company.getCompanyName());
		record.setStationId(station.getId());
		record.setPoint(station.getPoint());
		record.setProjectCode(station.getProjectCode());
		record.setTypeCode("1");
		record.setTypeValue("设备故障");
		record.setDescription(MessageContants.TMP1);
		alarmDao.insertAlarmRecord(record);
		//报警完毕，移除任务
		alarmManageService.removeShutdownJob(stationId);
	}

	/**
	 * 处理量程和参数报警
	 * @param data
	 */
	public void doAlarm(WtDataRaw data) {
		List<WtStation> stations = alarmDao.findWtStationByMcuId(data.getMcu());
		if(stations != null && stations.size() > 0){
			List<WtParam> params = waterLevelService.listWtParam();
			List<WtWaterLevel> waterLevels = waterLevelService.listWaterLevel();
			for(WtStation station : stations){
				try {
					//查询所属区域
					WtCompany company = alarmDao.findWtCompanyById(station.getCompanyId());

					List<WtStationMonitor> monitors = waterLevelService.listMonitorByStationId(station.getId());
					//参数告警
					WaterLevelResult levelResult = waterLevelService.calculateWaterLevel(station, data, monitors, params, waterLevels);
					this.handleLevelResult(levelResult, data, company, station, waterLevels);

					//处理量程告警
					this.handleRange(monitors, data, company, station, params);

					//网络断开告警任务
					alarmManageService.saveShutodownJob(station, data);

				} catch (Exception e){
					logger.error("处理报警，发生异常", e);
				}
			}
		}
	}

	/**
	 * 量程告警
	 * @param monitors
	 * @param data
	 * @param company
	 * @param station
	 * @param params 
	 */
	private void handleRange(List<WtStationMonitor> monitors, WtDataRaw data, WtCompany company, WtStation station, List<WtParam> params) {
		if(monitors != null){
			//通用参数设置为map供方便使用
			Map<String, WtParam> paramMap = new HashMap<>();
			if(params != null){
				for(WtParam param: params){
					paramMap.put(param.getParam(), param);
				}
			}
			for(WtStationMonitor monitor: monitors){
				//判断标准，全部以设置的为准monitor
				String param = monitor.getParam();
				WtParam wtParam = paramMap.get(param);
				boolean isDisplay = true;
				if("1".equals(station.getStationStandard())){
					isDisplay = "1".equals(wtParam.getDisplay());
				} else{
					isDisplay = "1".equals(monitor.getDisplay());
				}
				if(isDisplay){
					//显示参数
					String paramName =null;
					Field field;
					BigDecimal value = null;
					try {
						field = WtDataRaw.class.getDeclaredField(param);
						field.setAccessible(true);
						value = (BigDecimal) field.get(data);
					} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
						e.printStackTrace();
					}
					if("2".equals(station.getStationStandard())){
						//个性站点
						paramName = monitor.getAliasParamName();
					} else{
						paramName = wtParam.getParamName();
					}
					BigDecimal max;
					BigDecimal min;
					if(value != null){
						// 更新标志
						boolean isUpdate = false;

						max = new BigDecimal(monitor.getRangeMax());
						min = new BigDecimal(monitor.getRangeMin());
						if(value.compareTo(max) > 0){
							WtAlarmRecord record = this.newInstance(data, company, station, max, min,
									3, monitor.getParam(), paramName, paramName, value);
							alarmDao.insertAlarmRecord(record);
							isUpdate = true;
						}
						if(value.compareTo(min) < 0){
							WtAlarmRecord record = this.newInstance(data, company, station, max, min,
									4, monitor.getParam(), paramName, paramName, value);
							alarmDao.insertAlarmRecord(record);
							isUpdate = true;
						}
						
						max = new BigDecimal(monitor.getAlertMax());
						min = new BigDecimal(monitor.getAlertMin());
						if(value.compareTo(max) > 0){
							WtAlarmRecord record = this.newInstance(data, company, station, max, min,
									5, monitor.getParam(), paramName, paramName, value);
							alarmDao.insertAlarmRecord(record);
                            isUpdate = true;
						}
						if(value.compareTo(min) < 0){
							WtAlarmRecord record = this.newInstance(data, company, station, max, min,
									6, monitor.getParam(), paramName, paramName, value);
							alarmDao.insertAlarmRecord(record);
                            isUpdate = true;
						}

						if(isUpdate) {
							alarmDao.updateParamToNull(data.getId(), param);
						}
					}
				}
			}
			
		}
	}

	/**
	 * 参数告警
	 * @param levelResult
	 * @param data
	 * @param company
	 * @param station
	 * @param waterLevels
	 */
	private void handleLevelResult(WaterLevelResult levelResult, WtDataRaw data, WtCompany company, WtStation station, List<WtWaterLevel> waterLevels) {
		List<WaterLevelResultDetail> details = levelResult.getDetails();
		if(details != null){
			//参数超标
			for (WaterLevelResultDetail detail : details) {
				if(detail.isDisplay() 
						&& detail.isInvolved()
						&& !detail.isFinalResult()){
					for(WtWaterLevel waterLevel : waterLevels) {
						if(levelResult.getLevelType().equals(waterLevel.getTypeCode())
								&& detail.getStandardLevel().equals(waterLevel.getLevelCode())
								&& detail.getStandardParam().equals(waterLevel.getParam())) {
							// 同种类型的评价标准
							Double standardLowerLimit = waterLevel.getLowerLimit();
							Double standardUpperLimit = waterLevel.getUpperLimit();
							BigDecimal upperLimit = ("1".equals(waterLevel.getHasLower()) && standardLowerLimit != null) ?
									new BigDecimal(standardLowerLimit) : null;
							BigDecimal lowerLimit = ("1".equals(waterLevel.getHasUpper()) && standardUpperLimit != null) ?
									new BigDecimal(standardUpperLimit) : null;
							WtAlarmRecord record = this.newInstance(data, company, station, upperLimit, lowerLimit,
									2, detail.getParam(), detail.getParamName(), detail.getParamName(), detail.getValue());
							alarmDao.insertAlarmRecord(record);
						}
					}
				}
			}
		}
	}

	/**
	 * 创建实体类
	 * @param data
	 * @param company
	 * @param station
	 * @param alarmMax
	 * @param alarmMin
	 * @param templateCode
	 * @param alarmParam
	 * @param alarmParamName
	 * @param arguments
	 * @return
	 */
	private WtAlarmRecord newInstance(WtDataRaw data, WtCompany company, WtStation station, 
			BigDecimal alarmMax, BigDecimal alarmMin, int templateCode, String alarmParam, String alarmParamName,
			Object...arguments){
		WtAlarmRecord record = new WtAlarmRecord();
		record.setAlarmTime(data.getTime());
		record.setCompanyId(company.getId());
		record.setCompanyName(company.getCompanyName());
		record.setStationId(station.getId());
		record.setPoint(station.getPoint());
		record.setProjectCode(station.getProjectCode());
		record.setAlarmMax(alarmMax);
		record.setAlarmMin(alarmMin);
		record.setAlarmParam(alarmParam);
		record.setAlarmParamName(alarmParamName);
		
		//1：设备故障 2：量程告警 3：参数告警
		String typeCode = null;
		String typeValue = null;
		String description = null;
		if(templateCode == 1){
			typeCode = "1";
			typeValue = "设备故障";
			description = MessageContants.TMP1;
		} else if(templateCode == 2){
			typeCode = "3";
			typeValue = "参数告警";
			description = MessageFormat.format(MessageContants.TMP2, arguments);
		} else if(templateCode == 3){
			typeCode = "2";
			typeValue = "量程告警";
			description = MessageFormat.format(MessageContants.TMP3, arguments);
		} else if(templateCode == 4){
			typeCode = "2";
			typeValue = "量程告警";
			description = MessageFormat.format(MessageContants.TMP4, arguments);
		} else if(templateCode == 5){
			typeCode = "2";
			typeValue = "量程告警";
			description = MessageFormat.format(MessageContants.TMP5, arguments);
		} else if(templateCode == 6){
			typeCode = "2";
			typeValue = "量程告警";
			description = MessageFormat.format(MessageContants.TMP6, arguments);
		}
		record.setTypeCode(typeCode);
		record.setTypeValue(typeValue);
		record.setDescription(description);
		
		return record;
	} 
}
