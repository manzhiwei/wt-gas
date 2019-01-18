package com.welltech.service.station;

import com.welltech.dao.WtDataRawDao;
import com.welltech.dto.WaterLevelResult;
import com.welltech.dto.WtParamDataDto;
import com.welltech.entity.*;
import com.welltech.service.statistics.WaterLevelService;
import com.welltech.service.sysSetting.PageParamManageService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by myMac on 18/6/28.
 */
@Service
public class BossService {

    @Autowired
    private WaterLevelService waterLevelService;

    @Autowired
    private WtDataRawDao wtDataRawDao;

    //统计站点当前最严格的达标标准（取配置中最小值）
    public String getHardestJudgeName(WtStation station){

        List<WtParam> generalParams = waterLevelService.listWtParam();
        List<WtStationMonitor> monitors = waterLevelService.listMonitorByStationId(station.getId());
        List<WtWaterLevel> waterLevels = waterLevelService.listWaterLevel();

        int hardestLevel=station.getStationJudgeType().equals("1")?3:6;

        //第一步：判断站点类型：标准、非标准
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

        return calcLevelDescription(Integer.valueOf(station.getStationJudgeType()),hardestLevel);
    }

    //重新计算单个参数指标的评价结论
    public String judgeBoss(String type,String value,String paramName){
        List<WtWaterLevel> waterLevels = waterLevelService.listWaterLevel();
        int currentLevel=0;
        String currentLevelName="-";

        for(WtWaterLevel wtWaterLevel:waterLevels){
            //地表＋参数名字
            //依次判断 取出最差的评价结果
            if(wtWaterLevel.getParam().equals(paramName)
                    &&wtWaterLevel.getTypeCode().equals(type)){
                try {
                    //判断最大值和最小是否相反
                    if(wtWaterLevel.getUpperLimit()<wtWaterLevel.getLowerLimit()
                            &&
                            (
                                    (wtWaterLevel.getHasUpper().equals("1")
                                            ||wtWaterLevel.getContainUpper().equals("1"))

                                            &&

                                            (wtWaterLevel.getHasLower().equals("1")
                                                    ||wtWaterLevel.getContainLower().equals("1"))
                            )){
                        boolean pass=false;

                        //上限判断
                        if(wtWaterLevel.getHasUpper().equals("1")
                                ||wtWaterLevel.getContainUpper().equals("1")){
                            if(Double.valueOf(value)<=Double.valueOf(wtWaterLevel.getUpperLimit())){
                                pass=true;
                            }
                        }

                        //下限判断
                        if(wtWaterLevel.getHasLower().equals("1")
                                ||wtWaterLevel.getContainLower().equals("1")){
                            if(Double.valueOf(value)>=Double.valueOf(wtWaterLevel.getLowerLimit())){
                                pass=true;
                            }
                        }

                        //如果通过上下限判断
                        if(pass){
                            //判断当前等级是否可以覆盖之前的判断
                            if(calcLevelValue(wtWaterLevel.getLevel())>currentLevel){
                                currentLevel=calcLevelValue(wtWaterLevel.getLevel());
                                currentLevelName=wtWaterLevel.getLevel();
                            }
                        }

                    }else{
                        boolean upPass=false,lowerPass=false;

                        //上限判断
                        if(wtWaterLevel.getHasUpper().equals("1")
                                ||wtWaterLevel.getContainUpper().equals("1")){
                            if(Double.valueOf(value)<=Double.valueOf(wtWaterLevel.getUpperLimit())){
                                upPass=true;
                            }
                        }else{
                            upPass=true;
                        }

                        //下限判断
                        if(wtWaterLevel.getHasLower().equals("1")
                                ||wtWaterLevel.getContainLower().equals("1")){
                            if(Double.valueOf(value)>=Double.valueOf(wtWaterLevel.getLowerLimit())){
                                lowerPass=true;
                            }
                        }else{
                            lowerPass=true;
                        }


                        //如果通过上下限判断
                        if(upPass&&lowerPass){
                            //判断当前等级是否可以覆盖之前的判断
                            if(calcLevelValue(wtWaterLevel.getLevel())>currentLevel){
                                currentLevel=calcLevelValue(wtWaterLevel.getLevel());
                                currentLevelName=wtWaterLevel.getLevel();
                            }
                        }

                    }
                }catch (Exception e){

                }


            }
        }
        return currentLevelName;
    }

    //查询近24小时的评价结果
    public String find24HourAvgDataResult(WtStation station){
        try {
            List<WtParam> params = waterLevelService.listWtParam();
            List<String> searchParams = new ArrayList<String>();
            for(WtParam param: params){
                searchParams.add(param.getParam());//避免sql写重复内容
            }
            WtDataRaw hour24Data = wtDataRawDao.find24HourAvgWtDataRawDtoByStationId(searchParams, station.getId());
            List<WtStationMonitor> monitors = waterLevelService.listMonitorByStationId(station.getId());
            List<WtWaterLevel> waterLevels = waterLevelService.listWaterLevel();
            WaterLevelResult waterLevelResult=waterLevelService.calculateWaterLevel(station, hour24Data, monitors, params, waterLevels);
            return waterLevelResult.getResultName();
        }catch (Exception e){
            return "-";
        }

    }

    //处理某个站点、某条数据的评级结果
    public WaterLevelResult calcSingleWaterLevelResult(WtStation station,WtDataRaw wtDataRaw){
        try {
            List<WtParam> params = waterLevelService.listWtParam();
            List<String> searchParams = new ArrayList<String>();
            for(WtParam param: params){
                searchParams.add(param.getParam());//避免sql写重复内容
            }
            List<WtStationMonitor> monitors = waterLevelService.listMonitorByStationId(station.getId());
            List<WtWaterLevel> waterLevels = waterLevelService.listWaterLevel();
            WaterLevelResult waterLevelResult=waterLevelService.calculateWaterLevel(station, wtDataRaw, monitors, params, waterLevels);
            return waterLevelResult;
        }catch (Exception e){
            return null;
        }

    }

    //判断前后数据是否相同，相同的去除
    public List<WtDataRaw> filterWtDataRawList(List<WtDataRaw> input){
        List<WtDataRaw> result=new ArrayList<>();
        if(input.size()==0){
            return result;
        }
        WtDataRaw lastWtDataRaw=input.get(0);
        result.add(lastWtDataRaw);
        for(int i=1;i<input.size();i++){
            WtDataRaw wtDataRaw=input.get(i);

            if(isParamSame(lastWtDataRaw.getP1(),wtDataRaw.getP1())){
                wtDataRaw.setP1(null);
            }
            if(isParamSame(lastWtDataRaw.getP2(),wtDataRaw.getP2())){
                wtDataRaw.setP2(null);
            }
            if(isParamSame(lastWtDataRaw.getP3(),wtDataRaw.getP3())){
                wtDataRaw.setP3(null);
            }
            if(isParamSame(lastWtDataRaw.getP4(),wtDataRaw.getP4())){
                wtDataRaw.setP4(null);
            }
            if(isParamSame(lastWtDataRaw.getP5(),wtDataRaw.getP5())){
                wtDataRaw.setP5(null);
            }
            if(isParamSame(lastWtDataRaw.getP6(),wtDataRaw.getP6())){
                wtDataRaw.setP6(null);
            }
            if(isParamSame(lastWtDataRaw.getP7(),wtDataRaw.getP7())){
                wtDataRaw.setP7(null);
            }
            if(isParamSame(lastWtDataRaw.getP8(),wtDataRaw.getP8())){
                wtDataRaw.setP8(null);
            }
            if(isParamSame(lastWtDataRaw.getP9(),wtDataRaw.getP9())){
                wtDataRaw.setP9(null);
            }
            if(isParamSame(lastWtDataRaw.getP10(),wtDataRaw.getP10())){
                wtDataRaw.setP10(null);
            }
            if(isParamSame(lastWtDataRaw.getP11(),wtDataRaw.getP11())){
                wtDataRaw.setP11(null);
            }
            if(isParamSame(lastWtDataRaw.getP12(),wtDataRaw.getP12())){
                wtDataRaw.setP12(null);
            }
            if(isParamSame(lastWtDataRaw.getP13(),wtDataRaw.getP13())){
                wtDataRaw.setP13(null);
            }
            if(isParamSame(lastWtDataRaw.getP14(),wtDataRaw.getP14())){
                wtDataRaw.setP14(null);
            }
            if(isParamSame(lastWtDataRaw.getP15(),wtDataRaw.getP15())){
                wtDataRaw.setP15(null);
            }
            if(isParamSame(lastWtDataRaw.getP16(),wtDataRaw.getP16())){
                wtDataRaw.setP16(null);
            }
            if(isParamSame(lastWtDataRaw.getP17(),wtDataRaw.getP17())){
                wtDataRaw.setP17(null);
            }
            if(isParamSame(lastWtDataRaw.getP18(),wtDataRaw.getP18())){
                wtDataRaw.setP18(null);
            }
            if(isParamSame(lastWtDataRaw.getP19(),wtDataRaw.getP19())){
                wtDataRaw.setP19(null);
            }
            if(isParamSame(lastWtDataRaw.getP20(),wtDataRaw.getP20())){
                wtDataRaw.setP20(null);
            }
            if(isParamSame(lastWtDataRaw.getP21(),wtDataRaw.getP21())){
                wtDataRaw.setP21(null);
            }
            if(isParamSame(lastWtDataRaw.getP22(),wtDataRaw.getP22())){
                wtDataRaw.setP22(null);
            }
            if(isParamSame(lastWtDataRaw.getP23(),wtDataRaw.getP23())){
                wtDataRaw.setP23(null);
            }
            if(isParamSame(lastWtDataRaw.getP24(),wtDataRaw.getP24())){
                wtDataRaw.setP24(null);
            }
            if(isParamSame(lastWtDataRaw.getP25(),wtDataRaw.getP25())){
                wtDataRaw.setP25(null);
            }
            if(isParamSame(lastWtDataRaw.getP26(),wtDataRaw.getP26())){
                wtDataRaw.setP26(null);
            }
            if(isParamSame(lastWtDataRaw.getP27(),wtDataRaw.getP27())){
                wtDataRaw.setP27(null);
            }
            if(isParamSame(lastWtDataRaw.getP28(),wtDataRaw.getP28())){
                wtDataRaw.setP28(null);
            }
            if(isParamSame(lastWtDataRaw.getP29(),wtDataRaw.getP29())){
                wtDataRaw.setP29(null);
            }
            if(isParamSame(lastWtDataRaw.getP30(),wtDataRaw.getP30())){
                wtDataRaw.setP30(null);
            }
            if(isParamSame(lastWtDataRaw.getP31(),wtDataRaw.getP31())){
                wtDataRaw.setP31(null);
            }
            if(isParamSame(lastWtDataRaw.getP32(),wtDataRaw.getP32())){
                wtDataRaw.setP32(null);
            }

            lastWtDataRaw=wtDataRaw;
            result.add(wtDataRaw);

        }
        return result;
    }

    //判断前后两条水质数据的参数是否相同
    public boolean isParamSame(BigDecimal b1, BigDecimal b2){
        if(b1==null&&b2==null){
            return true;
        }else if(b1!=null){
            if(b1.equals(b2)){
                return true;
            }else{
                return false;
            }
        }else{
            return false;
        }
    }

    //不同类型下不同等级对应的标准显示
    public String calcLevelDescription(int type,int level){
        if(type==1){
            switch (level){
                case 1:
                    return "达标";
                case 2:
                    return "预警";
                case 3:
                    return "超标";
            }
        }else{
            switch (level){
                case 1:
                    return "Ⅰ类";
                case 2:
                    return  "Ⅱ类";
                case 3:
                    return "Ⅲ类";
                case 4:
                    return "Ⅳ类";
                case 5:
                    return "Ⅴ类";
                case 6:
                    return "劣Ⅴ类";
            }
        }
        return "-";
    }

    //显示的标准对应的等级值（1-3，1-6）
    public int calcLevelValue(String description){
        if(description==null){
            return 0;
        }
        switch (description){
            case "达标":
            case "Ⅰ类":
                return 1;
            case "预警":
            case "Ⅱ类":
                return 2;
            case "超标":
            case "Ⅲ类":
                return 3;
            case "Ⅳ类":
                return 4;
            case "Ⅴ类":
                return 5;
            case "劣Ⅴ类":
                return 6;
        }
        return 0;
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

        if(calcLevelValue(wtParamDataDto.getP1())>hardestLevel){
            result.add(1);
        }
        if(calcLevelValue(wtParamDataDto.getP2())>hardestLevel){
            result.add(2);
        }
        if(calcLevelValue(wtParamDataDto.getP3())>hardestLevel){
            result.add(3);
        }
        if(calcLevelValue(wtParamDataDto.getP4())>hardestLevel){
            result.add(4);
        }
        if(calcLevelValue(wtParamDataDto.getP5())>hardestLevel){
            result.add(5);
        }
        if(calcLevelValue(wtParamDataDto.getP6())>hardestLevel){
            result.add(6);
        }
        if(calcLevelValue(wtParamDataDto.getP7())>hardestLevel){
            result.add(7);
        }
        if(calcLevelValue(wtParamDataDto.getP8())>hardestLevel){
            result.add(8);
        }
        if(calcLevelValue(wtParamDataDto.getP9())>hardestLevel){
            result.add(9);
        }
        if(calcLevelValue(wtParamDataDto.getP10())>hardestLevel){
            result.add(10);
        }
        if(calcLevelValue(wtParamDataDto.getP11())>hardestLevel){
            result.add(11);
        }
        if(calcLevelValue(wtParamDataDto.getP12())>hardestLevel){
            result.add(12);
        }
        if(calcLevelValue(wtParamDataDto.getP13())>hardestLevel){
            result.add(13);
        }
        if(calcLevelValue(wtParamDataDto.getP14())>hardestLevel){
            result.add(14);
        }
        if(calcLevelValue(wtParamDataDto.getP15())>hardestLevel){
            result.add(15);
        }
        if(calcLevelValue(wtParamDataDto.getP16())>hardestLevel){
            result.add(16);
        }
        if(calcLevelValue(wtParamDataDto.getP17())>hardestLevel){
            result.add(17);
        }
        if(calcLevelValue(wtParamDataDto.getP18())>hardestLevel){
            result.add(18);
        }
        if(calcLevelValue(wtParamDataDto.getP19())>hardestLevel){
            result.add(19);
        }
        if(calcLevelValue(wtParamDataDto.getP20())>hardestLevel){
            result.add(20);
        }
        if(calcLevelValue(wtParamDataDto.getP21())>hardestLevel){
            result.add(21);
        }
        if(calcLevelValue(wtParamDataDto.getP22())>hardestLevel){
            result.add(22);
        }
        if(calcLevelValue(wtParamDataDto.getP23())>hardestLevel){
            result.add(23);
        }
        if(calcLevelValue(wtParamDataDto.getP24())>hardestLevel){
            result.add(24);
        }
        if(calcLevelValue(wtParamDataDto.getP25())>hardestLevel){
            result.add(25);
        }
        if(calcLevelValue(wtParamDataDto.getP26())>hardestLevel){
            result.add(26);
        }
        if(calcLevelValue(wtParamDataDto.getP27())>hardestLevel){
            result.add(27);
        }
        if(calcLevelValue(wtParamDataDto.getP28())>hardestLevel){
            result.add(28);
        }
        if(calcLevelValue(wtParamDataDto.getP29())>hardestLevel){
            result.add(29);
        }
        if(calcLevelValue(wtParamDataDto.getP30())>hardestLevel){
            result.add(30);
        }
        if(calcLevelValue(wtParamDataDto.getP31())>hardestLevel){
            result.add(31);
        }
        if(calcLevelValue(wtParamDataDto.getP31())>hardestLevel){
            result.add(32);
        }
        return result;
    }



}
