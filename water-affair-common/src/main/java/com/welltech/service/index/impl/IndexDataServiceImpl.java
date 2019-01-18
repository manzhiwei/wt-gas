package com.welltech.service.index.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.welltech.common.util.UserUtil;
import com.welltech.dao.WtCompanyDao;
import com.welltech.dto.WtDataRawDto;
import com.welltech.dto.WtParamDataDto;
import com.welltech.dto.WtParamQueryDto;
import com.welltech.service.station.BossService;
import com.welltech.service.sysSetting.PageParamManageService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.welltech.dao.WtDataRawDao;
import com.welltech.dao.WtParamDao;
import com.welltech.dao.WtStationDao;
import com.welltech.dao.indexData.IndexDataDao;
import com.welltech.dto.RealtimeNetworkDto;
import com.welltech.entity.WtParam;
import com.welltech.entity.WtStation;
import com.welltech.entity.WtWaterLevel;
import com.welltech.entity.WtWaterType;
import com.welltech.framework.aop.pagination.bean.Page;
import com.welltech.service.index.IndexDataService;
import com.welltech.service.statistics.WaterLevelService;

@Service
public class IndexDataServiceImpl implements IndexDataService {

	@Autowired
	private IndexDataDao indexDataDao;
	@Autowired
	private WtStationDao wtStationDao;
	@Autowired
	private WtDataRawDao wtDataRawDao;
	@Autowired
	private WtStationDao stationDao;

	@Autowired
	PageParamManageService pageParamManageService;

	@Autowired
	private WtParamDao wtParamDao;

	@Autowired
	private WaterLevelService waterLevelService;

	@Autowired
	private BossService bossService;

	@Autowired
	private WtCompanyDao wtCompanyDao;


	@Override
	public List<RealtimeNetworkDto> findRealtimeNetworkDto() {
		return indexDataDao.findRealtimeNetworkDto();
	}

	@Override
	public List<RealtimeNetworkDto> findRealtimeNetworkDtoByCompanyId(Integer companyId) {
		return indexDataDao.findRealtimeNetworkDtoByCompanyId(companyId);
	}

	@Override
	public List<Map<String, Object>> listSingleParam(Page page, String param, String type) {
		WtParam wtParam = wtParamDao.findByParam(param);
		Map<String, Object> map = new LinkedHashMap<>();
		map.put("page", page);
		if(param.matches("^[p][1-9]?\\d$")){//防止sql注入
			map.put("param", param);
		} else{
			map.put("param", "p1");
		}
		if(!"0".equals(type)) {//0是全部
			map.put("rtype", type);
		}
		List<Map<String, Object>> result = new ArrayList<>();;
		List<Map<String, Object>> datas = null;
		if(UserUtil.isAdmin()){
			datas = indexDataDao.findPageSingleParam(map);
		}else {
			map.put("companyId",UserUtil.getCompanyIdByUser());

			if(wtCompanyDao.findCompanyById(UserUtil.getCompanyIdByUser()).getId() !=
					wtCompanyDao.findCompanyById(UserUtil.getCompanyIdByUser()).getParentId())
				datas = indexDataDao.findPageSingleParamByCompanyId(map);
			else
				datas = indexDataDao.findPageSingleParamByParentId(map);
		}

		if(datas != null && !datas.isEmpty()){
			for( Map<String, Object> data: datas) {
				Map<String, Object> r = new LinkedHashMap<>();
				r.put("point", data.get("point"));
				r.put("paramName", wtParam.getParamName());
				r.put("type", data.get("rtype"));
				r.put("value", data.get("rvalue"));
				result.add(r);
			}
		}
		return result;
	}

	@Override
	public Map<String, Object> countWaterLevel(String typeCode) {
		Map<String,Object> data = new HashMap<>();
		Map<String,Object> result1 = new HashMap<>();
		Map<String,Object> result = new HashMap<>();

		List<WtStation> stations = null;
		if(UserUtil.isAdmin()){
			stations = wtStationDao.findStationBy24Hour();
		}else {
			if(wtCompanyDao.findCompanyById(UserUtil.getCompanyIdByUser()).getId() !=
					wtCompanyDao.findCompanyById(UserUtil.getCompanyIdByUser()).getParentId())
				stations = wtStationDao.findStationBy24HourAndCompanyId(UserUtil.getCompanyIdByUser());
			else
				stations = wtStationDao.findStationBy24HourAndParentId(UserUtil.getCompanyIdByUser());
		}

		if(stations!=null&&stations.size()>0){
			typeCode=stations.get(0).getStationJudgeType();
			if(StringUtils.isBlank(typeCode)){//如果为空默认1
				typeCode = "1";
			}
		}

		for(WtStation s:stations){
			String levelDescription=bossService.find24HourAvgDataResult(s);
			if(!"-".equals(levelDescription)){
				int count=result.get(levelDescription)==null?
						0:Integer.valueOf(result.get(levelDescription).toString());
				result.put(levelDescription,count+1);
			}
		}

		List<WtWaterType> typeCodes=indexDataDao.findWtWaterTypesByTypeCode(typeCode);
		for(WtWaterType type:typeCodes){
			Object obj = result.get(type.getLevel());
			if(obj==null){
				result.put(type.getLevel(), 0);
			}
			result1.put(type.getLevelCode(), type.getLevel());
		}
		data.put("data", result);
		data.put("data1", result1);
		data.put("typeCode", typeCode);
		return data;
	}

	@Override
	public Map<String, Object> getRealtimeMonitoring() {
		Map<String, Object> result = new HashMap<String, Object>();
		List<Integer> data = new ArrayList<>();


		List<Map<String, Object>> stationNum = null;
		if(UserUtil.isAdmin()){
			stationNum = indexDataDao.countStationNum2Map();//总数
		}else {
			if(wtCompanyDao.findCompanyById(UserUtil.getCompanyIdByUser()).getId() !=
					wtCompanyDao.findCompanyById(UserUtil.getCompanyIdByUser()).getParentId())
				stationNum = indexDataDao.countStationNum2MapByCompanyId(UserUtil.getCompanyIdByUser());
			else
				stationNum = indexDataDao.countStationNum2MapByParentId(UserUtil.getCompanyIdByUser());
		}

		result.put("stationNum", stationNum.size());


		List<Map<String, Object>> offlineNum=null;
		if(UserUtil.isAdmin()){
			offlineNum = indexDataDao.countOfflineStationNum2Map();//离线的设备
		}else{
			if(wtCompanyDao.findCompanyById(UserUtil.getCompanyIdByUser()).getId() !=
					wtCompanyDao.findCompanyById(UserUtil.getCompanyIdByUser()).getParentId())
				offlineNum = indexDataDao.countOfflineStationNum2MapByCompanyId(UserUtil.getCompanyIdByUser());
			else
				offlineNum = indexDataDao.countOfflineStationNum2MapByParentId(UserUtil.getCompanyIdByUser());
		}


		List<WtParam> wtParams = wtParamDao.findAllWtParams();
		List<String> params = new ArrayList<>();
		if(wtParams != null){
			for (WtParam param : wtParams) {
				params.add(param.getParam());
			}
		}
		List<Map<String, Object>> abnormalNum = null;
		if(UserUtil.isAdmin()){
			abnormalNum = indexDataDao.countAbnormalStationNum2Map(params);//异常站点，排除离线设备
		}else {
			if(wtCompanyDao.findCompanyById(UserUtil.getCompanyIdByUser()).getId() !=
					wtCompanyDao.findCompanyById(UserUtil.getCompanyIdByUser()).getParentId())
				abnormalNum = indexDataDao.countAbnormalStationNum2MapByCompanyId(params,UserUtil.getCompanyIdByUser());
			else
				abnormalNum = indexDataDao.countAbnormalStationNum2MapByParentId(params,UserUtil.getCompanyIdByUser());
		}


		//离线站点数量
		Map<String,String> quanbuIdMap=new HashMap<>();
		for(Map<String, Object> entry:stationNum){
			quanbuIdMap.put(""+entry.get("id"),""+entry.get("id"));
		}

		String zhengchang="0",chaobiao="0",yichang="0",lixian="0";
		//离线站点数量
		List<String> lixianIdList=new ArrayList<>();
		for(Map<String, Object> entry:offlineNum){
			lixianIdList.add(""+entry.get("id"));
			quanbuIdMap.remove(""+entry.get("id"));
		}
		lixian=""+lixianIdList.size();

		//异常站点数量
		List<String> yichangIdList=new ArrayList<>();
		for(Map<String, Object> entry:abnormalNum){
			if(!lixianIdList.contains(""+entry.get("id"))){
				yichangIdList.add(""+entry.get("id"));
				quanbuIdMap.remove(""+entry.get("id"));
			}
		}
		yichang=""+yichangIdList.size();

		//剩下判断每一个站点的状态
		int zhengcangCount=0,chaobiaoCount=0;
		for (Map.Entry<String, String> entry : quanbuIdMap.entrySet()) {
			//获取某个站点最新的实时数据
			WtDataRawDto wtDataRawDto=wtDataRawDao.findWtDataRawDtoByStationId(Integer.valueOf(""+entry.getKey()));

			if(wtDataRawDto!=null){

				if(wtDataRawDto.getMcu()!=null){
					WtStation station = stationDao.findStationById(Integer.valueOf(""+entry.getKey()));

					WtParamQueryDto queryDto =new WtParamQueryDto();
					queryDto.setReportType("6");
					queryDto.setType(wtDataRawDto.getMcu());
					queryDto.setStartTime(wtDataRawDto.getTime());
					queryDto.setEndTime(wtDataRawDto.getTime());
					queryDto.setPointId(""+station.getId());

					//region >评价结果
					Map judgeParamMap = new HashMap();
					List<WtParam> judgeParams = pageParamManageService.findAllJudgeParams(queryDto.getPointId());
					//解析出参与评价的参数
					for(WtParam para:judgeParams){
						judgeParamMap.put(para.getParam(),new String[]{"on"});
					}
					Map<String, Object> judgeMap = pageParamManageService.findAllJudgeData(station,queryDto,judgeParamMap);

					List<WtParamDataDto> heichouDatas4Judge = (List<WtParamDataDto>) judgeMap.get("heichouDatas");
					List<WtParamDataDto> dibiaoDatas4Judge = (List<WtParamDataDto>) judgeMap.get("dibiaoDatas");
					String pollutantResult=bossService.calcMainPollutant(station,judgeParams,heichouDatas4Judge,dibiaoDatas4Judge);
					if("".equals(pollutantResult)){
						//正常
						zhengcangCount++;
					}else{
						//超标
						chaobiaoCount++;
					}

				}

			}
		}

		data.add(Integer.valueOf(""+zhengcangCount));
		data.add(Integer.valueOf(""+chaobiaoCount));
		data.add(Integer.valueOf(yichang));
		data.add(Integer.valueOf(lixian));

		result.put("data", data);
		return result;
	}

	@Override
	public List<Map<String, Object>> listOverproof(Page page) {
		
		Map<String, Object> map = new LinkedHashMap<>();
		map.put("page", page);
		List<WtParam> wtParams = wtParamDao.findAllWtParams();
		List<String> params = new ArrayList<>();
		if(wtParams != null){
			for (WtParam param : wtParams) {
				params.add(param.getParam());
			}
		}
		map.put("params", params);
		
		List<Map<String, Object>> result = new ArrayList<>();;
		List<Map<String, Object>> datas = null;
		if(UserUtil.isAdmin()){
			datas = indexDataDao.findPageOverproof(map);
		}else {
			map.put("companyId",UserUtil.getCompanyIdByUser());
			if(wtCompanyDao.findCompanyById(UserUtil.getCompanyIdByUser()).getId() !=
					wtCompanyDao.findCompanyById(UserUtil.getCompanyIdByUser()).getParentId())
				datas = indexDataDao.findPageOverproofByCompanyId(map);
			else
				datas = indexDataDao.findPageOverproofByParentId(map);
		}
		if(datas != null && !datas.isEmpty()){
			for( Map<String, Object> data: datas) {
				Map<String, Object> r = new LinkedHashMap<>();
				r.put("point", data.get("point"));
				r.put("paramName", data.get("paramName"));
				r.put("value", data.get("val"));
				r.put("time", data.get("time"));
				r.put("typeCode", data.get("typeCode"));
				r.put("typeValue", data.get("typeValue"));
				result.add(r);
			}
		}
		return result;
	}

	@Override
	public List<Map<String, Object>> fault(Page page) {
		
		Map<String, Object> map = new LinkedHashMap<>();
		map.put("page", page);
		List<WtParam> wtParams = wtParamDao.findAllWtParams();
		List<String> params = new ArrayList<>();
		if(wtParams != null){
			for (WtParam param : wtParams) {
				params.add(param.getParam());
			}
		}
		map.put("params", params);
		
		List<Map<String, Object>> result = new ArrayList<>();;
		List<Map<String, Object>> datas = indexDataDao.findPageFault(map);
		if(datas != null && !datas.isEmpty()){
			for( Map<String, Object> data: datas) {
				Map<String, Object> r = new LinkedHashMap<>();
				r.put("point", data.get("point"));
				//* 1:设备修改 
				//* 2:电极维护
				Object obj=data.get("breakdown_type");
				if(obj!=null){
					int tmp=Integer.parseInt(obj.toString());
					switch (tmp)
					{
					case 1:
						r.put("breakdownType","设备修改");
						break;
					case 2:
						r.put("breakdownType","电极维护");
						break;
					default:
						r.put("breakdownType","");
						break;
					}
				}
				r.put("desc", data.get("desc"));
				result.add(r);
			}
		}
		return result;
	}
	public List<Map<String, Object>> listAbnormal(Page page) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Map<String, Object>> listFault(Page page) {
		// TODO Auto-generated method stub
		return null;
	}

}
