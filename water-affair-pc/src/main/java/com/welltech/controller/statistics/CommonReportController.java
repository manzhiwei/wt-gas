/**
 * 
 */
package com.welltech.controller.statistics;

import java.util.*;

import javax.servlet.http.HttpServletRequest;

import com.welltech.common.util.SpringWebUtils;
import com.welltech.common.util.UserUtil;
import com.welltech.dao.WtCompanyDao;
import com.welltech.entity.*;
import com.welltech.security.entity.WtUser;
import com.welltech.service.station.BossService;
import com.welltech.service.statistics.StationStatisticService;
import com.welltech.service.statistics.WaterLevelService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.method.P;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.welltech.common.util.ReturnEntity;
import com.welltech.dto.WtAreaStationDto;
import com.welltech.dto.WtParamDataDto;
import com.welltech.dto.WtParamQueryDto;
import com.welltech.service.statistics.CommonReportService;
import com.welltech.service.sysSetting.PageParamManageService;
import com.welltech.service.sysSetting.PortManageService;

/**
 * Created by Zhujia at 2017年8月14日 下午8:52:22
 */
@Controller
public class CommonReportController {
	
	@Autowired
	CommonReportService commonReportService;

	@Autowired
	private WaterLevelService waterLevelService;

	@Autowired
	PageParamManageService pageParamManageService;
	
	@Autowired
	private PortManageService portManageService;

	@Autowired
	private BossService bossService;

	@Autowired
	private StationStatisticService stationStatisticService;

	@Autowired
	private WtCompanyDao wtCompanyDao;

	@RequestMapping(value = { "/commonReport" })
	public String commonReport(HttpServletRequest request,Map<String,Object> map,WtParamQueryDto queryDto) {
		if(StringUtils.isBlank(queryDto.getPointId())){
		    //如果为空，查询这个账户下的第一台表的数据
			Object obj = SpringWebUtils.getSession().getAttribute("user");
			WtStation station = null;
			if(obj instanceof com.welltech.security.entity.WtUser){
				int companyId = ((com.welltech.security.entity.WtUser) obj).getCompanyId();
				station = stationStatisticService.findFirstStationInCompany(companyId);
			}
			if(station != null){
				queryDto.setPointId(station.getId()+"");
				queryDto.setReportType("0");
				queryDto.setPointName(station.getPoint());
				Date now = new Date();
				queryDto.setStartTime(new Date(now.getTime()-1*60*60*1000));//查询当前间隔1小时的数据
				queryDto.setEndTime(now);//当前时间
				queryDto.setDate(now);
				queryDto.setMonth(now);
				queryDto.setYear(now);
			}
		}

		WtStation station = portManageService.findStationById(queryDto.getPointId());
		if(null!=station.getGatewaySerial()){
			queryDto.setType(station.getGatewaySerial());
		}

		Map paramMap = request.getParameterMap();
		Map<String, Object> resultMap = pageParamManageService.findAllDisplayData(station,queryDto,paramMap);
		List<WtParamDataDto> datas = (List<WtParamDataDto>) resultMap.get("datas");
		List<WtParamDataDto> minDatas = (List<WtParamDataDto>) resultMap.get("minDatas");
		List<WtParamDataDto> maxDatas = (List<WtParamDataDto>) resultMap.get("maxDatas");
		List<WtParamDataDto> avgDatas = (List<WtParamDataDto>) resultMap.get("avgDatas");
		List<WtParamDataDto> heichouDatas = (List<WtParamDataDto>) resultMap.get("heichouDatas");
		List<WtParamDataDto> dibiaoDatas = (List<WtParamDataDto>) resultMap.get("dibiaoDatas");

		List<WtParam> params = pageParamManageService.findAllDisplayParams(paramMap,queryDto.getPointId());
		map.put("columns", params);
		map.put("datas", datas);
		map.put("minDatas", minDatas);
		map.put("maxDatas", maxDatas);
		map.put("avgDatas", avgDatas);
		map.put("heichouDatas", heichouDatas);
		map.put("dibiaoDatas", dibiaoDatas);
		map.put("queryDto", queryDto);
		map.put("pointId",queryDto.getPointId());
		map.put("pointName",queryDto.getPointName());
		map.put("station", station);

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
		String pollutantResult=calcMainPollutant(station,judgeParams,heichouDatas4Judge,dibiaoDatas4Judge);
		if("".equals(pollutantResult)){
			map.put("heichou", "达标");
			map.put("dibiao", "达标");
			map.put("pollutant", "无");
		}else{
			map.put("heichou", "超标");
			map.put("dibiao", "超标");
			map.put("pollutant", pollutantResult);
		}

		//endregion

		return "statistics/commonReport";
	}

	//计算某个站点主要污染物结果
	public String calcMainPollutant(WtStation station,
									List<WtParam> params,
									List<WtParamDataDto> heichouDatas,
									List<WtParamDataDto> dibiaoDatas
	){
		List<WtParam> generalParams = waterLevelService.listWtParam();
		List<WtStationMonitor> monitors = waterLevelService.listMonitorByStationId(station.getId());

		//第一步：判断站点类型：标准、非标准
		int hardestLevel=6;
		//region > 计算出最严格的统计结果
		if("1".equals(station.getStationStandard())){
			for(WtParam wtParam:generalParams){
				//参与评价
				if("1".equals(wtParam.getInvolved())){
					try{
						//判断取哪个标准:固定污染源预警、地表水质
						if("1".equals(station.getStationJudgeType())){
							if(Integer.valueOf(wtParam.getHeichou())<=hardestLevel){
								hardestLevel=Integer.valueOf(wtParam.getHeichou());
							}
						}else{
							if(Integer.valueOf(wtParam.getDibiao())<=hardestLevel){
								hardestLevel=Integer.valueOf(wtParam.getDibiao());
							}
						}
					}catch (Exception e){

					}

				}
			}
		}else{

			for(WtStationMonitor wtStationMonitor:monitors){
				//参与评价
				if("1".equals(wtStationMonitor.getRoundType())){
					//判断取哪个标准:固定污染源预警、地表水质
					if("1".equals(station.getStationJudgeType())){
						if(wtStationMonitor.getHeichouDisplay().equals("1")&&
								Integer.valueOf(wtStationMonitor.getHeichouLevel())<=hardestLevel){
							hardestLevel=Integer.valueOf(wtStationMonitor.getHeichouLevel());
						}
					}else{
						if(wtStationMonitor.getDibiaoDisplay().equals("1")&&
								Integer.valueOf(wtStationMonitor.getDibiaoLevel())<=hardestLevel){
							hardestLevel=Integer.valueOf(wtStationMonitor.getDibiaoLevel());
						}
					}
				}
			}

		}
		//endregion

		List<Integer> pollutantParaIndexList=null;
		if(heichouDatas!=null){
			pollutantParaIndexList=calcStationPollutantPara(heichouDatas.get(0),hardestLevel);
		}
		if(dibiaoDatas!=null){
			pollutantParaIndexList=calcStationPollutantPara(dibiaoDatas.get(0),hardestLevel);
		}

		List<String> paraNameList=new ArrayList<>();

		for(WtParam wtParam:params){
			int paraIndex=Integer.valueOf(wtParam.getParam().replace("p",""));
			if(pollutantParaIndexList!=null){
				if(pollutantParaIndexList.contains(paraIndex)){
					paraNameList.add(wtParam.getParamName());
				}
			}
		}

		return StringUtils.join(paraNameList, ",");
	}

	//计算超标污染物的列号
	public List<Integer> calcStationPollutantPara(WtParamDataDto wtParamDataDto,int hardestLevel){

		List<Integer> result=new ArrayList<>();

		if(bossService.calcLevelValue(wtParamDataDto.getP1())>hardestLevel){
			result.add(1);
		}
		if(bossService.calcLevelValue(wtParamDataDto.getP2())>hardestLevel){
			result.add(2);
		}
		if(bossService.calcLevelValue(wtParamDataDto.getP3())>hardestLevel){
			result.add(3);
		}
		if(bossService.calcLevelValue(wtParamDataDto.getP4())>hardestLevel){
			result.add(4);
		}
		if(bossService.calcLevelValue(wtParamDataDto.getP5())>hardestLevel){
			result.add(5);
		}
		if(bossService.calcLevelValue(wtParamDataDto.getP6())>hardestLevel){
			result.add(6);
		}
		if(bossService.calcLevelValue(wtParamDataDto.getP7())>hardestLevel){
			result.add(7);
		}
		if(bossService.calcLevelValue(wtParamDataDto.getP8())>hardestLevel){
			result.add(8);
		}
		if(bossService.calcLevelValue(wtParamDataDto.getP9())>hardestLevel){
			result.add(9);
		}
		if(bossService.calcLevelValue(wtParamDataDto.getP10())>hardestLevel){
			result.add(10);
		}
		if(bossService.calcLevelValue(wtParamDataDto.getP11())>hardestLevel){
			result.add(11);
		}
		if(bossService.calcLevelValue(wtParamDataDto.getP12())>hardestLevel){
			result.add(12);
		}
		if(bossService.calcLevelValue(wtParamDataDto.getP13())>hardestLevel){
			result.add(13);
		}
		if(bossService.calcLevelValue(wtParamDataDto.getP14())>hardestLevel){
			result.add(14);
		}
		if(bossService.calcLevelValue(wtParamDataDto.getP15())>hardestLevel){
			result.add(15);
		}
		if(bossService.calcLevelValue(wtParamDataDto.getP16())>hardestLevel){
			result.add(16);
		}
		if(bossService.calcLevelValue(wtParamDataDto.getP17())>hardestLevel){
			result.add(17);
		}
		if(bossService.calcLevelValue(wtParamDataDto.getP18())>hardestLevel){
			result.add(18);
		}
		if(bossService.calcLevelValue(wtParamDataDto.getP19())>hardestLevel){
			result.add(19);
		}
		if(bossService.calcLevelValue(wtParamDataDto.getP20())>hardestLevel){
			result.add(20);
		}
		if(bossService.calcLevelValue(wtParamDataDto.getP21())>hardestLevel){
			result.add(21);
		}
		if(bossService.calcLevelValue(wtParamDataDto.getP22())>hardestLevel){
			result.add(22);
		}
		if(bossService.calcLevelValue(wtParamDataDto.getP23())>hardestLevel){
			result.add(23);
		}
		if(bossService.calcLevelValue(wtParamDataDto.getP24())>hardestLevel){
			result.add(24);
		}
		if(bossService.calcLevelValue(wtParamDataDto.getP25())>hardestLevel){
			result.add(25);
		}
		if(bossService.calcLevelValue(wtParamDataDto.getP26())>hardestLevel){
			result.add(26);
		}
		if(bossService.calcLevelValue(wtParamDataDto.getP27())>hardestLevel){
			result.add(27);
		}
		if(bossService.calcLevelValue(wtParamDataDto.getP28())>hardestLevel){
			result.add(28);
		}
		if(bossService.calcLevelValue(wtParamDataDto.getP29())>hardestLevel){
			result.add(29);
		}
		if(bossService.calcLevelValue(wtParamDataDto.getP30())>hardestLevel){
			result.add(30);
		}
		if(bossService.calcLevelValue(wtParamDataDto.getP31())>hardestLevel){
			result.add(31);
		}
		if(bossService.calcLevelValue(wtParamDataDto.getP31())>hardestLevel){
			result.add(32);
		}
		return result;
	}



	@RequestMapping(value = { "/getAreaStation"})
	@ResponseBody
	public ReturnEntity<List<WtAreaStationDto>> getAreaStationNode(){
		ReturnEntity<List<WtAreaStationDto>> re = new ReturnEntity<List<WtAreaStationDto>>();
		List<WtAreaStationDto> dto = commonReportService.getAreaStationNode();
		re.setReturnData(dto);
		return re;
	}

	@RequestMapping(value = { "/getAreaStationByCompanyId"})
	@ResponseBody
	public ReturnEntity<List<WtAreaStationDto>> getAreaStationNodeByCompanyId(){
		//查询这个账户下的所有站点信息
		Object obj = SpringWebUtils.getSession().getAttribute("user");
		int companyId = 1;
		String userName = "";
		if(obj instanceof com.welltech.security.entity.WtUser){
			companyId = ((com.welltech.security.entity.WtUser) obj).getCompanyId();
			userName  = ((com.welltech.security.entity.WtUser) obj).getUsername();
		}

		ReturnEntity<List<WtAreaStationDto>> re = new ReturnEntity<List<WtAreaStationDto>>();

		List<WtAreaStationDto> dto = null;
		if(userName.equals("admin")){
			dto = commonReportService.getAreaStationNode();
		}else{
			if(wtCompanyDao.findCompanyById(UserUtil.getCompanyIdByUser()).getParentId() !=
					wtCompanyDao.findCompanyById(UserUtil.getCompanyIdByUser()).getId())
				dto =commonReportService.getAreaStationNodeByCompanyId(companyId);
			else
				dto =commonReportService.getAreaStationNodeByParentId(companyId);
		}
		re.setReturnData(dto);
		return re;
	}

	@RequestMapping(value = { "/getAreaTree"})
	@ResponseBody
	public ReturnEntity<List<WtAreaStationDto>> getAreaTree(){
		ReturnEntity<List<WtAreaStationDto>> re = new ReturnEntity<List<WtAreaStationDto>>();
		List<WtAreaStationDto> dto = commonReportService.getAreaNode();
		re.setReturnData(dto);
		return re;
	}

}
