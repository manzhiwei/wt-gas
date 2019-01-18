package com.welltech.service.realtime.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.welltech.common.util.UserUtil;
import com.welltech.dao.WtCompanyDao;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.welltech.dao.WtDataRawDao;
import com.welltech.dao.WtStationDao;
import com.welltech.dao.indexData.IndexDataDao;
import com.welltech.dto.WaterLevelResult;
import com.welltech.dto.WtDataRawDto;
import com.welltech.entity.WtCompany;
import com.welltech.entity.WtDataRaw;
import com.welltech.entity.WtParam;
import com.welltech.entity.WtStation;
import com.welltech.entity.WtStationMonitor;
import com.welltech.entity.WtWaterLevel;
import com.welltech.entity.WtWaterType;
import com.welltech.service.index.UiElementService;
import com.welltech.service.realtime.MapService;
import com.welltech.service.statistics.WaterLevelService;

@Service
public class MapServiceImpl implements MapService {

	@Autowired
	private IndexDataDao indexDataDao;
	
	@Autowired
	private WtDataRawDao wtDataRawDao;
	
	@Autowired
	private UiElementService uiElementService;
	
	@Autowired
	private WtStationDao wtStationDao;
	
	@Autowired
	private WaterLevelService waterLevelService;

	@Autowired
	private WtCompanyDao wtCompanyDao;
	
	@Override
	public Map<String, Object> getDataOfMapButtons() {
		Map<String, Object> result = new HashMap<>();
		List<Map<String, Object>> levelNums = new ArrayList<>();
		List<Map<String, Object>> stationDetails = new ArrayList<>();	
		
		List<WtStation> stations = null;
		if(UserUtil.isAdmin()){
			stations = wtStationDao.findAllStations();
		}else {
			if(wtCompanyDao.findCompanyById(UserUtil.getCompanyIdByUser()).getParentId() !=
					wtCompanyDao.findCompanyById(UserUtil.getCompanyIdByUser()).getId())
				stations = wtStationDao.findAllStationsByCompanyId(UserUtil.getCompanyIdByUser());
			else
				stations = wtStationDao.findAllStationsByParentId(UserUtil.getCompanyIdByUser());
		}
		List<WtParam> params = waterLevelService.listWtParam();
		List<WtWaterLevel> waterLevels = waterLevelService.listWaterLevel();
		List<WtWaterType> types = indexDataDao.findWtWaterTypesByTypeCode(null);

		//离线站点数据
		List<Map<String, Object>> offlineNum= null ;
		if(UserUtil.isAdmin()){
			offlineNum = indexDataDao.countOfflineStationNum2Map();//离线的设备
		}else{
			if(wtCompanyDao.findCompanyById(UserUtil.getCompanyIdByUser()).getParentId() !=
					wtCompanyDao.findCompanyById(UserUtil.getCompanyIdByUser()).getId())
				offlineNum = indexDataDao.countOfflineStationNum2MapByCompanyId(UserUtil.getCompanyIdByUser());//离线设备
			else
				offlineNum = indexDataDao.countOfflineStationNum2MapByParentId(UserUtil.getCompanyIdByUser());//离线设备
		}

		List<String> lixianIdList=new ArrayList<>();
		for(Map<String, Object> entry:offlineNum){
			String id=entry.get("id").toString();
			lixianIdList.add(id);
		}


		Map<String, Integer> levelNum = new LinkedHashMap<>();//分类-数目
		Map<String, String> typeCode = new HashMap<>();//分类-数目
		Map<String, String> levelCode = new HashMap<>();//分类-数目
		for(WtWaterType type: types){
			levelNum.put(type.getLevel(), 0);
			typeCode.put(type.getLevel(), type.getTypeCode());
			levelCode.put(type.getLevel(), type.getLevelCode());
		}

		int judgeType=1;
		int sum = 0;
		if(stations != null){
			for(WtStation station : stations){

				Map<String, Object> stationDetail = new HashMap<>();
				if("1".equals(station.getStationJudgeType())){
					judgeType=1;
				}else{
					judgeType=2;
				}
				WtDataRaw data = null;
				if(StringUtils.isNotBlank(station.getGatewaySerial())){
					data = wtDataRawDao.findLatestWtDataRaw(station.getGatewaySerial());
				}

				//计算站点评价
				if(!lixianIdList.contains(""+station.getId())){
					List<WtStationMonitor> monitors = waterLevelService.listMonitorByStationId(station.getId());
					WaterLevelResult levelResult = waterLevelService.calculateWaterLevel(station, data, monitors, params, waterLevels);

					int num = levelNum.get(levelResult.getResultName()) + 1;
					levelNum.put(levelResult.getResultName(), num);

					stationDetail.put("result", levelResult.getResultName());
					stationDetail.put("typeCode", levelResult.getLevelType());
					stationDetail.put("levelCode", levelResult.getResultCode());
				}else{

					if(judgeType==1){
						stationDetail.put("result","离线");
						stationDetail.put("typeCode", "1");
						stationDetail.put("levelCode", "7");
					}else{
						stationDetail.put("result","离线");
						stationDetail.put("typeCode", "2");
						stationDetail.put("levelCode", "7");
					}

				}

				
				stationDetail.put("stationId", station.getId());
				stationDetail.put("longitude", station.getLongitude());
				stationDetail.put("latitude", station.getLatitude());
				stationDetail.put("point", station.getPoint());
				stationDetail.put("companyId", station.getCompanyId());

				stationDetails.add(stationDetail);
				
				sum ++;
			}
		}
		result.put("stations", stationDetails);

		if(judgeType==1){
			//离线
			levelNum.put("离线",lixianIdList.size());
			typeCode.put("离线", "1");
			levelCode.put("离线", "7");
		}else{
			//离线
			levelNum.put("离线", lixianIdList.size());
			typeCode.put("离线", "2");
			levelCode.put("离线", "7");
		}

		// 对数据结果进行运算和补全
		for(String level : levelNum.keySet()){
			Map<String, Object> map = new HashMap<>();
			map.put("level", level);
			map.put("levelCode", levelCode.get(level));
			map.put("typeCode", typeCode.get(level));
			map.put("num", levelNum.get(level));
			DecimalFormat df = new DecimalFormat("#.##");
			map.put("precent", sum <= 0 ? 0 : df.format(new BigDecimal(levelNum.get(level) * 100).divide(new BigDecimal(sum), 1, RoundingMode.HALF_UP)));
			levelNums.add(map);
		}
		result.put("levelNums", levelNums);//button上的数据
		
		return result;
	}

	@Override
	public Map<String, Object> getStationData(Integer stationId) {
		Map<String, Object> result = new HashMap<>();
		WtDataRawDto wtDataRawDto = wtDataRawDao.findWtDataRawDtoByStationId(stationId);
		Map<String, WtParam> param = uiElementService.getParamsByStationId(stationId);
		result.put("data", wtDataRawDto);
		result.put("params", param);
		return result;
	}

	@Override
	public List<WtCompany> getCompany() {
		if(UserUtil.isAdmin())
			return wtStationDao.findAllCompanys();
		else {
			if(wtCompanyDao.findCompanyById(UserUtil.getCompanyIdByUser()).getParentId() !=
					wtCompanyDao.findCompanyById(UserUtil.getCompanyIdByUser()).getId())
				return wtStationDao.findCompanyByCompanyId(UserUtil.getCompanyIdByUser());
			else
				return wtStationDao.findCompanyByParentId(UserUtil.getCompanyIdByUser());
		}
	}
	
}
