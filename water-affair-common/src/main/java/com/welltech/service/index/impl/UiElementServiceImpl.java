package com.welltech.service.index.impl;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.welltech.common.util.SpringWebUtils;
import com.welltech.common.util.UserUtil;
import com.welltech.dao.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.welltech.dto.PointDto;
import com.welltech.entity.WtParam;
import com.welltech.entity.WtStation;
import com.welltech.entity.WtStationMonitor;
import com.welltech.service.index.UiElementService;

@Service("uiElementService")
public class UiElementServiceImpl implements UiElementService {

	@Autowired
	private UiElementDao uiElementDao;
	
	@Autowired
	private WtParamDao wtParamDao;
	
	@Autowired
	private WtStationMonitorDao wtStationMonitorDao;
	
	@Autowired
	private WtStationDao wtStationDao;

	@Autowired
	private WtCompanyDao wtCompanyDao;
	
	@Override
	public List<PointDto> findAllPointDtos() {

		List<PointDto> result = null;
		if(UserUtil.isAdmin()){
			result = uiElementDao.findAllPointDtos();
		}else{
			//如果站点的公司ID 和 站点的父公司ID 不相等，则代表这个站点是独立站点，即本公司的站点
			if(wtCompanyDao.findCompanyById(UserUtil.getCompanyIdByUser()).getId() !=
					wtCompanyDao.findCompanyById(UserUtil.getCompanyIdByUser()).getParentId())
				result = uiElementDao.findAllPointOnCompany(UserUtil.getCompanyIdByUser());
			else
				result = uiElementDao.findAllPointOnParentId(UserUtil.getCompanyIdByUser());
		}
		if(result == null){
			result = new ArrayList<PointDto>();
		}
		return result;
	}

    @Override
	public Map<String, WtParam> getParams() {
		Map<String, WtParam> result = new LinkedHashMap<>();
		List<WtParam> params = wtParamDao.findAllWtParams();
		if(params != null){
			for (WtParam param : params) {
				result.put(param.getParam(), param);
			}
		}
		return result;
	}

	@Override
	public Map<String, WtParam> getParams2() {
		Map<String, WtParam> result = new LinkedHashMap<>();
		List<WtParam> params = wtParamDao.findAllWtParams();
		if(params != null){
			for (WtParam param : params) {
				result.put(param.getParam(), param);
			}
			for(int i= 0 ; i<params.size();i++){
				int temp = i+1;

				WtParam wt =(WtParam) params.get(i).clone();
				wt.setParam("p"+temp+"Min");
				wt.setParamName(params.get(i).getParamName()+"最小值");

				result.put("p"+temp+"Min",wt);

				WtParam wt1 = (WtParam) params.get(i).clone();
				wt1.setParam("p"+temp+"Max");
				wt1.setParamName(params.get(i).getParamName()+"最大值");

				result.put("p"+temp+"Max",wt1);

				WtParam wt2 = (WtParam) params.get(i).clone();
				wt2.setParam("p"+temp+"Avg");
				wt2.setParamName(params.get(i).getParamName()+"平均值");

				result.put("p"+temp+"Avg",wt2);

				WtParam wt3 = (WtParam) params.get(i).clone();
				wt3.setParam("p"+temp+"Cou");
				wt3.setParamName(params.get(i).getParamName()+"累积量");

				result.put("p"+temp+"Cou",wt3);
			}
		}
		return result;
	}

	@Override
	public List<WtParam> getDisplayParams() {
		//List<WtParam> result = wtParamDao.findDisplayWtParams();
		List<WtParam> result = wtParamDao.findJudgeWtParams();

		if(result == null){
			result = new ArrayList<>();
		}
		return result;
	}

	@Override
	public Map<String, WtParam> getParamsByStationId(Integer stationId) {
		Map<String, WtParam> params = this.getParams();
		WtStation station = wtStationDao.findStationById(stationId);
		if(station != null
				&& "2".equals(station.getStationStandard())){
			// 个性站点
			List<WtStationMonitor> monitors = wtStationMonitorDao.findSetAliasByStationId(stationId);
			params = this.getPersonalAliasName(monitors, params);
		}
		return params;
	}

	@Override
	public Map<String, WtParam> getParamsByStationId2(Integer stationId) {
		Map<String, WtParam> params = this.getParams();
		WtStation station = wtStationDao.findStationById(stationId);
		if(station != null
				&& "2".equals(station.getStationStandard())){
			// 个性站点
			List<WtStationMonitor> monitors = wtStationMonitorDao.findSetAliasByStationId(stationId);
			params = this.getPersonalAliasName2(monitors, params);
		}
		return params;
	}

	@Override
	public Map<String, WtParam> getDisplayParamsByStationId(WtStation wtStation) {
		Map<String, WtParam> params = new LinkedHashMap<>();
		if("1".equals(wtStation.getStationStandard())){	//标准测点
			List<WtParam> paramsList = wtParamDao.findDisplayWtParams();
			if(paramsList != null){
				for (WtParam param : paramsList) {
					params.put(param.getParam(), param);
				}
			}
		}else{	//个性化测点
			List<WtStationMonitor> monitors = wtStationMonitorDao.findDisplaySetAliasByStationId(wtStation.getId());
			List<WtParam> paramsList = wtParamDao.findStatisticsDisplayWtParams(wtStation.getId()+"");
			if(paramsList != null){
				for (WtParam param : paramsList) {
					params.put(param.getParam(), param);
				}
			}
			params = this.getAliasName(monitors,params);
		}
		return params;


	}

	private Map<String, WtParam> getAliasName(List<WtStationMonitor> monitors, Map<String, WtParam> params){
		if(monitors != null){
			for(WtStationMonitor monitor: monitors) {
				/**if(StringUtils.isNotBlank(monitor.getAliasUnit())){
				 params.get(monitor.getParam()).setUnit(monitor.getAliasUnit());
				 }
				 if(StringUtils.isNotBlank(monitor.getAliasParamName())){
				 params.get(monitor.getParam()).setParamName(monitor.getAliasParamName());
				 params.get(monitor.getParam()).setUnit(monitor.getAliasUnit());
				 }*/

				params.get(monitor.getParamAdjust()).setParamName(monitor.getAliasParamName());
				params.get(monitor.getParamAdjust()).setUnit(monitor.getAliasUnit());
				//此行代码待测试 如有bug请删除
				params.get(monitor.getParamAdjust()).setParam(monitor.getParam());
				params.get(monitor.getParamAdjust()).setDisplay(monitor.getDisplay());
			}
		}
		return params;
	}

	private Map<String, WtParam> getPersonalAliasName(List<WtStationMonitor> monitors, Map<String, WtParam> params){
		//个性站点全部以设置的为准，不再取通用部分
		Map<String, WtParam> result = new LinkedHashMap<>();
		Set<String> keys = params.keySet();
		for(String key: keys){
			WtParam param = new WtParam();
			param.setParam(key);
			param.setParamName(key);
			param.setDisplay("0");
			result.put(key, param);
		}
		/*for(int i =1;i<=32;i++){
			WtParam param = new WtParam();
			param.setParam("p"+i+"Flag");
			param.setParamName("p"+i+"Flag");
			param.setDisplay("0");
			result.put("p"+i+"Flag",param);
		}*/

		if(monitors != null){
			for(WtStationMonitor monitor: monitors) {
				WtParam param = result.get(monitor.getParam());
				//重新对参数个性赋值
				param.setParamName(monitor.getAliasParamName());
				param.setUnit(monitor.getAliasUnit());
				param.setDisplay(monitor.getDisplay());
				param.setRoundType(monitor.getRoundType());
				param.setDibiao(monitor.getDibiaoLevel());
				param.setHeichou(monitor.getHeichouLevel());


			}
			/*for(WtStationMonitor monitor: monitors){
				WtParam param = result.get(monitor.getParam()+"Flag");
				param.setParamName(monitor.getAliasParamName()+"标志");
				param.setDisplay(monitor.getDisplay());
			}*/
		}


		return result;
	}
	private Map<String, WtParam> getPersonalAliasName2(List<WtStationMonitor> monitors, Map<String, WtParam> params){
		//个性站点全部以设置的为准，不再取通用部分
		Map<String, WtParam> result = new LinkedHashMap<>();
		Set<String> keys = params.keySet();
		for(String key: keys){
			WtParam param = new WtParam();
			param.setParam(key);
			param.setParamName(key);
			param.setDisplay("0");
			result.put(key, param);
		}
		for(int i =1;i<=32;i++){
			WtParam param = new WtParam();
			param.setParam("p"+i+"Min");
			param.setParamName("p"+i+"Min");
			param.setDisplay("0");
			result.put("p"+i+"Min",param);

			WtParam param2 = new WtParam();
			param.setParam("p"+i+"Max");
			param.setParamName("p"+i+"Max");
			param.setDisplay("0");
			result.put("p"+i+"Max",param2);

			WtParam param3 = new WtParam();
			param.setParam("p"+i+"Avg");
			param.setParamName("p"+i+"Avg");
			param.setDisplay("0");
			result.put("p"+i+"Avg",param3);

			WtParam param4 = new WtParam();
			param.setParam("p"+i+"Cou");
			param.setParamName("p"+i+"Cou");
			param.setDisplay("0");
			result.put("p"+i+"Cou",param4);
		}


		if(monitors != null){
			for(WtStationMonitor monitor: monitors){
				WtParam param = result.get(monitor.getParam()+"Min");
				param.setParamName(monitor.getAliasParamName()+"最小值");
				param.setDisplay(monitor.getDisplay());
			}
			for(WtStationMonitor monitor: monitors){
				WtParam param = result.get(monitor.getParam()+"Max");
				param.setParamName(monitor.getAliasParamName()+"最大值");
				param.setDisplay(monitor.getDisplay());
			}
			for(WtStationMonitor monitor: monitors){
				WtParam param = result.get(monitor.getParam()+"Avg");
				param.setParamName(monitor.getAliasParamName()+"平均值");
				param.setDisplay(monitor.getDisplay());
			}
			for(WtStationMonitor monitor: monitors){
				WtParam param = result.get(monitor.getParam()+"Cou");
				param.setParamName(monitor.getAliasParamName()+"排放量");
				param.setDisplay(monitor.getDisplay());
			}
		}
		return result;
	}

	@Override
	public String getJudgeType() {
		return wtStationDao.getJudgeType();
	}
}
