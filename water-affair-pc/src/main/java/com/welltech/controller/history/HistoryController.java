package com.welltech.controller.history;

import java.util.*;

import com.welltech.common.util.DateUtil;
import com.welltech.common.util.SpringWebUtils;
import com.welltech.dto.PointDto;
import com.welltech.entity.WtStation;
import com.welltech.service.statistics.StationStatisticService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.method.P;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.welltech.dto.WtDataRawDto;
import com.welltech.framework.aop.pagination.bean.MyPage;
import com.welltech.service.history.HistoryService;
import com.welltech.service.index.UiElementService;
import com.welltech.service.realtime.RealtimeService;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thymeleaf.util.DateUtils;

@Controller
public class HistoryController {

	@Autowired
	private HistoryService historyService;
	
	@Autowired
	private RealtimeService realtimeService;
	
	@Autowired
	private UiElementService uiElementService;

	@Autowired
	private StationStatisticService stationStatisticService;
	
  /*  @RequestMapping(value = {"historyData"})
    public String realtimeData(Model model, MyPage myPage,
    		String startTime,
    		String endTime,
			String pointName, String pointId){
    	if(myPage == null){
    		myPage = new MyPage();
    	}

		Integer[] pointIds;//站点ID集合
		List<PointDto> pointDtoList = new ArrayList<>();
		if(pointName!=null && StringUtils.isNotBlank(pointId)){
			String[] pointNames = pointName.split(",");
			if(pointNames.length>1){
				model.addAttribute("pointName", pointNames[0]);
			}
			String[] stationIds = pointId.split(",");
			pointIds = new Integer[stationIds.length];
			for(int i = 0; i < stationIds.length; i ++) {
				pointIds[i] = Integer.parseInt(stationIds[i]);
			}
		}else {
			//当前账户下的所有站点
			pointDtoList = uiElementService.findAllPointDtos();
			model.addAttribute("points", pointDtoList);
			pointIds = new Integer[pointDtoList.size()];
			if(pointDtoList != null){
				for (int i = 0;i<pointDtoList.size();i++ ){
					pointIds[i] = pointDtoList.get(i).getId();
				}
			}
		}


		model.addAttribute("pointIds", pointIds);
		model.addAttribute("pointId", pointId);
		model.addAttribute("pointName", pointName);

		//处理表头
    	model.addAttribute("params", uiElementService.getParamList());
    	List<WtDataRawDto> data=null;
		Date startTime1;
		Date endTime1;
		if(startTime == null){
			startTime1 = DateUtil.getTodayDate();
		}else{
			startTime1 = DateUtil.stringToDate(startTime);
		}
		model.addAttribute("startTime",startTime1 );
		if(endTime == null){
			endTime1 = new Date();
		}else{
			endTime1 = DateUtil.stringToDate(endTime);
		}
		model.addAttribute("endTime",endTime1 );

    	if(pointIds!=null&&pointIds.length>=1) {
    		data=historyService.listHistoryWtDataRawDto(
    				myPage,
					new Integer[] {pointIds[0]},
					startTime1,
					endTime1);
    	}else {
    		data=new ArrayList<WtDataRawDto>();
    	}

    	model.addAttribute("datas", data);
    	model.addAttribute("myPage", myPage);

        return "history/historyData";
    }*/
    @RequestMapping(value = "historyDataAjax")
	@ResponseBody
    public Map<String,Object> historyData(String startTime, String endTime,
										  String pointName, String pointId){
    	Map<String,Object> map = new HashMap<String,Object>();
		Integer[] pointIds;//站点ID集合
		List<PointDto> pointDtoList = new ArrayList<>();
		if(pointName!=null && StringUtils.isNotBlank(pointId)){
			String[] stationIds = pointId.split(",");
			pointIds = new Integer[stationIds.length];
			for(int i = 0; i < stationIds.length; i ++) {
				pointIds[i] = Integer.parseInt(stationIds[i]);
			}
		}else {
			//当前账户下的所有站点
			pointDtoList = uiElementService.findAllPointDtos();
			pointIds = new Integer[pointDtoList.size()];
			if(pointDtoList != null){
				for (int i = 0;i<pointDtoList.size();i++ ){
					pointIds[i] = pointDtoList.get(i).getId();
				}
			}
		}

		//处理表头
		map.put("params",uiElementService.getParamList());

		//处理表数据
    	List<WtDataRawDto> data=null;
		Date startTime1;
		Date endTime1;
		if("".equals(startTime)||startTime == null){
			startTime1 = DateUtil.getTodayDate();
		}else{
			startTime1 = DateUtil.stringToDate(startTime);
		}
		if("".equals(endTime)||endTime == null){
			endTime1 = new Date();
		}else{
			endTime1 = DateUtil.stringToDate(endTime);
		}
    	if(pointIds!=null&&pointIds.length>=1) {
    		//按照多个站点选取来进行 后台不进行分页，分页由前台处理
			data = historyService.listHistoryWtDataRawDto(pointIds,startTime1,endTime1);
    	}else {
    		data=new ArrayList<WtDataRawDto>();
    	}
		map.put("datas",data);
        return map;
    }

    @RequestMapping(value = {"historyData"})
    public String realtimeData(Model model, MyPage myPage, Integer[] pointIds,
    		@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss") Date startTime,
    		@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss") Date endTime,
							   String pointName, String pointId){
    	if(myPage == null){
    		myPage = new MyPage();
    	}
    	model.addAttribute("points", uiElementService.findAllPointDtos());
    	model.addAttribute("pointIds", pointIds);
        model.addAttribute("pointId", pointId);
		model.addAttribute("pointName", pointName);

		if(pointName!=null){
			String[] pointNames = pointName.split(",");
			if(pointNames.length>1){
				model.addAttribute("pointName", pointNames[0]);
			}
		}

		if(pointIds == null) {
			if(StringUtils.isNotBlank(pointId)){
				String[] stationIds = pointId.split(",");
				pointIds = new Integer[stationIds.length];
				for(int i = 0; i < stationIds.length; i ++) {
					pointIds[i] = Integer.parseInt(stationIds[i]);
				}
			}
		}

    	if(pointIds == null || pointIds.length == 0){
    		pointIds = realtimeService.getDefaultStations();//为1的标准
    	}
    	//当取1的标准没有的时候需要取为2的值进来存入pointIds
    	if(pointIds == null || pointIds.length == 0){
    		pointIds = realtimeService.getStations2();//为2的标准
    	}
    	boolean alias = false;
    	if(pointIds.length >= 1){
    		//单站点查询，有可能设置过别名
    		List<Integer> unpass = realtimeService.checkStation(pointIds[0]);
    		if(!unpass.isEmpty()){
    			//设置过别名
    			alias = true;
    		}
    	}
    	model.addAttribute("alias", alias);
    	if(alias){
    		model.addAttribute("params", uiElementService.getParamsByStationId(pointIds[0]));
    	} else{
    		model.addAttribute("params", uiElementService.getParams());
    	}
    	List<WtDataRawDto> data=null;

    	if(pointIds!=null&&pointIds.length>=1) {
    		data=historyService.listHistoryWtDataRawDto(
    				myPage,
					new Integer[] {pointIds[0]},
					startTime,
					endTime);
    	}else {
    		data=new ArrayList<WtDataRawDto>();
    	}

    	model.addAttribute("datas", data);
    	model.addAttribute("myPage", myPage);
    	model.addAttribute("startTime", startTime);
    	model.addAttribute("endTime", endTime);
        return "history/historyData";
    }

	@RequestMapping(value = {"analyHistoryData"})
	public String realtime1Data(Model model, MyPage myPage, Integer[] pointIds,
								@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss") Date startTime,
								@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss") Date endTime, String pointName, String pointId){
		if(myPage == null){
			myPage = new MyPage();
		}
		model.addAttribute("points", uiElementService.findAllPointDtos());
		model.addAttribute("pointIds", pointIds);
		model.addAttribute("pointId", pointId);
		model.addAttribute("pointName", pointName);
		if(pointIds == null || pointIds.length == 0) {
			if(StringUtils.isNotBlank(pointId)){
				String[] stationIds = pointId.split(",");
				pointIds = new Integer[stationIds.length];
				for(int i = 0; i < stationIds.length; i ++) {
					pointIds[i] = Integer.parseInt(stationIds[i]);
				}
			}
		}
		if(pointIds == null || pointIds.length == 0){
			pointIds = realtimeService.getDefaultStations();
		}
		model.addAttribute("datas", historyService.listHistoryWtControlAir(myPage, pointIds, startTime, endTime));
		model.addAttribute("myPage", myPage);
		model.addAttribute("startTime", startTime);
		model.addAttribute("endTime", endTime);
		return "history/analyHistoryData";
	}

	@RequestMapping(value = {"monitorHistoryData"})
	public String realtime2Data(Model model, MyPage myPage, Integer[] pointIds,
								@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss") Date startTime,
								@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss") Date endTime, String pointName, String pointId){
		if(myPage == null){
			myPage = new MyPage();
		}
		model.addAttribute("points", uiElementService.findAllPointDtos());
		model.addAttribute("pointIds", pointIds);
		model.addAttribute("pointId", pointId);
		model.addAttribute("pointName", pointName);
		if(pointIds == null || pointIds.length == 0) {
			if(StringUtils.isNotBlank(pointId)){
				String[] stationIds = pointId.split(",");
				pointIds = new Integer[stationIds.length];
				for(int i = 0; i < stationIds.length; i ++) {
					pointIds[i] = Integer.parseInt(stationIds[i]);
				}
			}
		}
		if(pointIds == null || pointIds.length == 0){
			pointIds = realtimeService.getDefaultStations();
		}
		model.addAttribute("datas", historyService.listHistoryWtControl(myPage, pointIds, startTime, endTime));
		model.addAttribute("myPage", myPage);
		model.addAttribute("startTime", startTime);
		model.addAttribute("endTime", endTime);
		return "history/monitorHistoryData";
	}

//	@RequestMapping(value = {"testHistoryData"})
//	public String realtime3Data(Model model, MyPage myPage, Integer[] pointIds,
//						 @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss") Date startTime,
//						 @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss") Date endTime, String pointName, String pointId){
//		if(myPage == null){
//			myPage = new MyPage();
//		}
//		model.addAttribute("points", uiElementService.findAllPointDtos());
//		model.addAttribute("pointIds", pointIds);
//		model.addAttribute("pointId", pointId);
//		model.addAttribute("pointName", pointName);
//		if(pointIds == null || pointIds.length == 0) {
//			if(StringUtils.isNotBlank(pointId)){
//				String[] stationIds = pointId.split(",");
//				pointIds = new Integer[stationIds.length];
//				for(int i = 0; i < stationIds.length; i ++) {
//					pointIds[i] = Integer.parseInt(stationIds[i]);
//				}
//			}
//		}
//		if(pointIds == null || pointIds.length == 0){
//			pointIds = realtimeService.getDefaultStations();
//		}
//		model.addAttribute("datas", historyService.listHistoryWtControlLimit(myPage, pointIds, startTime, endTime));
//		model.addAttribute("myPage", myPage);
//		model.addAttribute("startTime", startTime);
//		model.addAttribute("endTime", endTime);
//		return "history/testHistoryData";
//	}

	@RequestMapping(value = {"dataCollectionHistory"})
	public String realtime4Data(Model model, MyPage myPage, Integer[] pointIds,
								@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss") Date startTime,
								@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss") Date endTime, String pointName, String pointId,String queryType){
		if(myPage == null){
			myPage = new MyPage();
		}

		model.addAttribute("points", uiElementService.findAllPointDtos());
		model.addAttribute("pointIds", pointIds);
		model.addAttribute("pointId", pointId);
		model.addAttribute("pointName", pointName);
		if(pointIds == null || pointIds.length == 0) {
			if(StringUtils.isNotBlank(pointId)){
				String[] stationIds = pointId.split(",");
				pointIds = new Integer[stationIds.length];
				for(int i = 0; i < stationIds.length; i ++) {
					pointIds[i] = Integer.parseInt(stationIds[i]);
				}
			}
		}
		if(pointIds == null || pointIds.length == 0){
			pointIds = realtimeService.getDefaultStations();
		}
		boolean alias = false;
		if(pointIds.length == 1){
			//单站点查询，有可能设置过别名
			List<Integer> unpass = realtimeService.checkStations(pointIds);
			if(!unpass.isEmpty()){
				//设置过别名
				alias = true;
			}
		}
		model.addAttribute("alias", alias);
		if(alias){
			model.addAttribute("params", uiElementService.getParamsByStationId2(pointIds[0]));
		} else{
			model.addAttribute("params", uiElementService.getParams2());
		}
		if(pointId == null){
			model.addAttribute("datas", null);
		}else{
			if(queryType == null || "".equals(queryType)){
				model.addAttribute("datas", historyService.listdataCollectionHistory(myPage, pointIds, startTime, endTime,"1"));
			}else{
				model.addAttribute("datas", historyService.listdataCollectionHistory(myPage, pointIds, startTime, endTime,queryType));
			}

		}
//		model.addAttribute("datas", historyService.listdataCollectionHistory(myPage, pointIds, startTime, endTime));
		model.addAttribute("myPage", myPage);
		model.addAttribute("startTime", startTime);
		model.addAttribute("endTime", endTime);
		return "history/dataCollectionHistory";
	}

	@RequestMapping(value = {"airHistoryData"})
	public String airHistoryData(Model model, MyPage myPage, Integer[] pointIds,
								@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss") Date startTime,
								@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss") Date endTime, String pointName, String pointId,String queryType){
		if(myPage == null){
			myPage = new MyPage();
		}

		model.addAttribute("points", uiElementService.findAllPointDtos());
		model.addAttribute("pointIds", pointIds);
		model.addAttribute("pointId", pointId);
		model.addAttribute("pointName", pointName);
		if(pointIds == null || pointIds.length == 0) {
			if(StringUtils.isNotBlank(pointId)){
				String[] stationIds = pointId.split(",");
				pointIds = new Integer[stationIds.length];
				for(int i = 0; i < stationIds.length; i ++) {
					pointIds[i] = Integer.parseInt(stationIds[i]);
				}
			}
		}
		if(pointIds == null || pointIds.length == 0){
			pointIds = realtimeService.getDefaultStations();
		}

		model.addAttribute("datas", historyService.listGasAirHistory(myPage, pointIds, startTime, endTime));
		model.addAttribute("myPage", myPage);
		model.addAttribute("startTime", startTime);
		model.addAttribute("endTime", endTime);
		return "history/airHistoryData";
	}


	@RequestMapping(value = {"/alarmStatHistoryData"})
	public String alarmStatHistoryData(Model model, MyPage myPage, Integer[] pointIds,
								 @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss") Date startTime,
								 @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss") Date endTime, String pointName, String pointId,String queryType){
		if(myPage == null){
			myPage = new MyPage();
		}

		model.addAttribute("points", uiElementService.findAllPointDtos());
		model.addAttribute("pointIds", pointIds);
		model.addAttribute("pointId", pointId);
		model.addAttribute("pointName", pointName);
		if(pointIds == null || pointIds.length == 0) {
			if(StringUtils.isNotBlank(pointId)){
				String[] stationIds = pointId.split(",");
				pointIds = new Integer[stationIds.length];
				for(int i = 0; i < stationIds.length; i ++) {
					pointIds[i] = Integer.parseInt(stationIds[i]);
				}
			}
		}
		if(pointIds == null || pointIds.length == 0){
			pointIds = realtimeService.getDefaultStations();
		}

		model.addAttribute("datas", historyService.listGasAlarmStatHistory(myPage, pointIds, startTime, endTime));
		model.addAttribute("myPage", myPage);
		model.addAttribute("startTime", startTime);
		model.addAttribute("endTime", endTime);
		return "history/alarmStatHistoryData";
	}

	@RequestMapping(value = {"/paraHistoryData"})
	public String paraHistoryData(Model model, MyPage myPage, Integer[] pointIds,
									   @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss") Date startTime,
									   @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss") Date endTime, String pointName, String pointId,String queryType){
		if(myPage == null){
			myPage = new MyPage();
		}

		model.addAttribute("points", uiElementService.findAllPointDtos());
		model.addAttribute("pointIds", pointIds);
		model.addAttribute("pointId", pointId);
		model.addAttribute("pointName", pointName);
		if(pointIds == null || pointIds.length == 0) {
			if(StringUtils.isNotBlank(pointId)){
				String[] stationIds = pointId.split(",");
				pointIds = new Integer[stationIds.length];
				for(int i = 0; i < stationIds.length; i ++) {
					pointIds[i] = Integer.parseInt(stationIds[i]);
				}
			}
		}
		if(pointIds == null || pointIds.length == 0){
			pointIds = realtimeService.getDefaultStations();
		}

		model.addAttribute("datas", historyService.listGasVariablesHistory(myPage, pointIds, startTime, endTime));
		model.addAttribute("myPage", myPage);
		model.addAttribute("startTime", startTime);
		model.addAttribute("endTime", endTime);
		return "history/paraHistoryData";
	}
}
