package com.welltech.controller.history;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.welltech.common.util.SpringWebUtils;
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
	
    @RequestMapping(value = {"historyData"})
    public String realtimeData(Model model, MyPage myPage,
    		@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss") Date startTime,
    		@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss") Date endTime,
							   String pointName, String pointId){
    	if(myPage == null){
    		myPage = new MyPage();
    	}

		Integer[] pointIds ;//站点ID集合
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
			pointIds =
		}

		model.addAttribute("points", uiElementService.findAllPointDtos());
		model.addAttribute("pointIds", pointIds);
		model.addAttribute("pointId", pointId);
		model.addAttribute("pointName", pointName);

    	model.addAttribute("params", uiElementService.getParams());
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
    /*@RequestMapping(value = {"historyData"})
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
    }*/

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
