package com.welltech.entity;

import com.welltech.framework.dao.Column;

import java.util.Date;

public class WtGasVariables {

    private Integer id;

    private String mcuId;

    private Date time;

    private Float methaneConc;

    private Float methaneConcIn;

    private Float nonMethaneConc;

    private Float nonMethaneConcIn;

    private Float totalHydrocarbonConc;

    private Float totalHydrocarbonConcIn;

    private Float methylbenzeneConc;

    private Float methylbenzeneConcIn;

    private Float ethylbenzeneConc;

    private Float ethylbenzeneConcIn;

    private Float benzeneConc;

    private Float benzeneConcIn;

    private Float inXyleneConc;

    private Float inXyleneConcIn;

    private Float mXyleneConc;

    private Float mXyleneConcIn;

    private Float pXyleneConc;

    private Float pXyleneConcIn;

    private Float airFlow;

    private Float air2Flow;

    private Float hydrogenFlow;

    private Float hydrogen2Flow;

    private Float zhuqianya1;

    private Float zhuqianya2;

    private Float zhuqianya3;

    private Float fidBoxTemp;

    private Float fidFireTemp;

    private Float fid2FireTemp;

    private Float zhuxiangTemp;

    private Float zhuxiang2Temp;

    private Float faxiangTemp;

    private Float smokeTemp;

    private Float smokeTempIn;

    private Float smokePress;

    private Float smokePressIn;

    private Float smokeFlow;

    private Float smokeFlowIn;

    private Float smokeHumidity;

    private Float smokeHumidityIn;

    private Float smokeOxygen;

    private Float smokeOxygenIn;


    private Float workSmokeFlow;

    private Float workSmokeFlowIn;

    private Float standardSmokeFlow;

    private Float standardSmokeFlowIn;

    private Float nonMethaneRate;

    private Float nonMethaneRateIn;

    private Float so2Conc;

    private Float so2ConcIn;

    private Float noConc;

    private Float noConcIn;

    private Float noxConc;

    private Float noxConcIn;

    private Float methylAlcoholConc;

    private Float methylAlcoholConcIn;

    private Float styreneConc;

    private Float styreneConcIn;

    private Float o2Conc;

    private Float rtoEfficiency;

    public Float getRtoEfficiency() {
        return rtoEfficiency;
    }

    public void setRtoEfficiency(Float rtoEfficiency) {
        this.rtoEfficiency = rtoEfficiency;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMcuId() {
        return mcuId;
    }

    public void setMcuId(String mcuId) {
        this.mcuId = mcuId;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public Float getMethaneConc() {
        return methaneConc;
    }

    public void setMethaneConc(Float methaneConc) {
        this.methaneConc = methaneConc;
    }

    public Float getNonMethaneConc() {
        return nonMethaneConc;
    }

    public void setNonMethaneConc(Float nonMethaneConc) {
        this.nonMethaneConc = nonMethaneConc;
    }

    public Float getTotalHydrocarbonConc() {
        return totalHydrocarbonConc;
    }

    public void setTotalHydrocarbonConc(Float totalHydrocarbonConc) {
        this.totalHydrocarbonConc = totalHydrocarbonConc;
    }

    public Float getMethylbenzeneConc() {
        return methylbenzeneConc;
    }

    public void setMethylbenzeneConc(Float methylbenzeneConc) {
        this.methylbenzeneConc = methylbenzeneConc;
    }

    public Float getEthylbenzeneConc() {
        return ethylbenzeneConc;
    }

    public void setEthylbenzeneConc(Float ethylbenzeneConc) {
        this.ethylbenzeneConc = ethylbenzeneConc;
    }

    public Float getBenzeneConc() {
        return benzeneConc;
    }

    public void setBenzeneConc(Float benzeneConc) {
        this.benzeneConc = benzeneConc;
    }

    public Float getInXyleneConc() {
        return inXyleneConc;
    }

    public void setInXyleneConc(Float inXyleneConc) {
        this.inXyleneConc = inXyleneConc;
    }

    public Float getmXyleneConc() {
        return mXyleneConc;
    }

    public void setmXyleneConc(Float mXyleneConc) {
        this.mXyleneConc = mXyleneConc;
    }

    public Float getpXyleneConc() {
        return pXyleneConc;
    }

    public void setpXyleneConc(Float pXyleneConc) {
        this.pXyleneConc = pXyleneConc;
    }

    public Float getAirFlow() {
        return airFlow;
    }

    public void setAirFlow(Float airFlow) {
        this.airFlow = airFlow;
    }

    public Float getHydrogenFlow() {
        return hydrogenFlow;
    }

    public void setHydrogenFlow(Float hydrogenFlow) {
        this.hydrogenFlow = hydrogenFlow;
    }

    public Float getZhuqianya1() {
        return zhuqianya1;
    }

    public void setZhuqianya1(Float zhuqianya1) {
        this.zhuqianya1 = zhuqianya1;
    }

    public Float getZhuqianya2() {
        return zhuqianya2;
    }

    public void setZhuqianya2(Float zhuqianya2) {
        this.zhuqianya2 = zhuqianya2;
    }

    public Float getFidBoxTemp() {
        return fidBoxTemp;
    }

    public void setFidBoxTemp(Float fidBoxTemp) {
        this.fidBoxTemp = fidBoxTemp;
    }

    public Float getFidFireTemp() {
        return fidFireTemp;
    }

    public void setFidFireTemp(Float fidFireTemp) {
        this.fidFireTemp = fidFireTemp;
    }

    public Float getZhuxiangTemp() {
        return zhuxiangTemp;
    }

    public void setZhuxiangTemp(Float zhuxiangTemp) {
        this.zhuxiangTemp = zhuxiangTemp;
    }

    public Float getFaxiangTemp() {
        return faxiangTemp;
    }

    public void setFaxiangTemp(Float faxiangTemp) {
        this.faxiangTemp = faxiangTemp;
    }

    public Float getSmokeTemp() {
        return smokeTemp;
    }

    public void setSmokeTemp(Float smokeTemp) {
        this.smokeTemp = smokeTemp;
    }

    public Float getSmokePress() {
        return smokePress;
    }

    public void setSmokePress(Float smokePress) {
        this.smokePress = smokePress;
    }

    public Float getSmokeFlow() {
        return smokeFlow;
    }

    public void setSmokeFlow(Float smokeFlow) {
        this.smokeFlow = smokeFlow;
    }

    public Float getSmokeHumidity() {
        return smokeHumidity;
    }

    public void setSmokeHumidity(Float smokeHumidity) {
        this.smokeHumidity = smokeHumidity;
    }

    public Float getSmokeOxygen() {
        return smokeOxygen;
    }

    public void setSmokeOxygen(Float smokeOxygen) {
        this.smokeOxygen = smokeOxygen;
    }

    public Float getSo2Conc() {
        return so2Conc;
    }

    public void setSo2Conc(Float so2Conc) {
        this.so2Conc = so2Conc;
    }

    public Float getNoConc() {
        return noConc;
    }

    public void setNoConc(Float noConc) {
        this.noConc = noConc;
    }

    public Float getNoxConc() {
        return noxConc;
    }

    public void setNoxConc(Float noxConc) {
        this.noxConc = noxConc;
    }

    public Float getO2Conc() {
        return o2Conc;
    }

    public void setO2Conc(Float o2Conc) {
        this.o2Conc = o2Conc;
    }

    public Float getMethaneConcIn() {
        return methaneConcIn;
    }

    public void setMethaneConcIn(Float methaneConcIn) {
        this.methaneConcIn = methaneConcIn;
    }

    public Float getNonMethaneConcIn() {
        return nonMethaneConcIn;
    }

    public void setNonMethaneConcIn(Float nonMethaneConcIn) {
        this.nonMethaneConcIn = nonMethaneConcIn;
    }

    public Float getTotalHydrocarbonConcIn() {
        return totalHydrocarbonConcIn;
    }

    public void setTotalHydrocarbonConcIn(Float totalHydrocarbonConcIn) {
        this.totalHydrocarbonConcIn = totalHydrocarbonConcIn;
    }

    public Float getMethylbenzeneConcIn() {
        return methylbenzeneConcIn;
    }

    public void setMethylbenzeneConcIn(Float methylbenzeneConcIn) {
        this.methylbenzeneConcIn = methylbenzeneConcIn;
    }

    public Float getEthylbenzeneConcIn() {
        return ethylbenzeneConcIn;
    }

    public void setEthylbenzeneConcIn(Float ethylbenzeneConcIn) {
        this.ethylbenzeneConcIn = ethylbenzeneConcIn;
    }

    public Float getBenzeneConcIn() {
        return benzeneConcIn;
    }

    public void setBenzeneConcIn(Float benzeneConcIn) {
        this.benzeneConcIn = benzeneConcIn;
    }

    public Float getInXyleneConcIn() {
        return inXyleneConcIn;
    }

    public void setInXyleneConcIn(Float inXyleneConcIn) {
        this.inXyleneConcIn = inXyleneConcIn;
    }

    public Float getmXyleneConcIn() {
        return mXyleneConcIn;
    }

    public void setmXyleneConcIn(Float mXyleneConcIn) {
        this.mXyleneConcIn = mXyleneConcIn;
    }

    public Float getpXyleneConcIn() {
        return pXyleneConcIn;
    }

    public void setpXyleneConcIn(Float pXyleneConcIn) {
        this.pXyleneConcIn = pXyleneConcIn;
    }

    public Float getAir2Flow() {
        return air2Flow;
    }

    public void setAir2Flow(Float air2Flow) {
        this.air2Flow = air2Flow;
    }

    public Float getHydrogen2Flow() {
        return hydrogen2Flow;
    }

    public void setHydrogen2Flow(Float hydrogen2Flow) {
        this.hydrogen2Flow = hydrogen2Flow;
    }

    public Float getZhuqianya3() {
        return zhuqianya3;
    }

    public void setZhuqianya3(Float zhuqianya3) {
        this.zhuqianya3 = zhuqianya3;
    }

    public Float getFid2FireTemp() {
        return fid2FireTemp;
    }

    public void setFid2FireTemp(Float fid2FireTemp) {
        this.fid2FireTemp = fid2FireTemp;
    }

    public Float getZhuxiang2Temp() {
        return zhuxiang2Temp;
    }

    public void setZhuxiang2Temp(Float zhuxiang2Temp) {
        this.zhuxiang2Temp = zhuxiang2Temp;
    }

    public Float getSmokeTempIn() {
        return smokeTempIn;
    }

    public void setSmokeTempIn(Float smokeTempIn) {
        this.smokeTempIn = smokeTempIn;
    }

    public Float getSmokePressIn() {
        return smokePressIn;
    }

    public void setSmokePressIn(Float smokePressIn) {
        this.smokePressIn = smokePressIn;
    }

    public Float getSmokeFlowIn() {
        return smokeFlowIn;
    }

    public void setSmokeFlowIn(Float smokeFlowIn) {
        this.smokeFlowIn = smokeFlowIn;
    }

    public Float getSmokeHumidityIn() {
        return smokeHumidityIn;
    }

    public void setSmokeHumidityIn(Float smokeHumidityIn) {
        this.smokeHumidityIn = smokeHumidityIn;
    }

    public Float getSmokeOxygenIn() {
        return smokeOxygenIn;
    }

    public void setSmokeOxygenIn(Float smokeOxygenIn) {
        this.smokeOxygenIn = smokeOxygenIn;
    }

    public Float getWorkSmokeFlow() {
        return workSmokeFlow;
    }

    public void setWorkSmokeFlow(Float workSmokeFlow) {
        this.workSmokeFlow = workSmokeFlow;
    }

    public Float getWorkSmokeFlowIn() {
        return workSmokeFlowIn;
    }

    public void setWorkSmokeFlowIn(Float workSmokeFlowIn) {
        this.workSmokeFlowIn = workSmokeFlowIn;
    }

    public Float getStandardSmokeFlow() {
        return standardSmokeFlow;
    }

    public void setStandardSmokeFlow(Float standardSmokeFlow) {
        this.standardSmokeFlow = standardSmokeFlow;
    }

    public Float getStandardSmokeFlowIn() {
        return standardSmokeFlowIn;
    }

    public void setStandardSmokeFlowIn(Float standardSmokeFlowIn) {
        this.standardSmokeFlowIn = standardSmokeFlowIn;
    }

    public Float getNonMethaneRate() {
        return nonMethaneRate;
    }

    public void setNonMethaneRate(Float nonMethaneRate) {
        this.nonMethaneRate = nonMethaneRate;
    }

    public Float getNonMethaneRateIn() {
        return nonMethaneRateIn;
    }

    public void setNonMethaneRateIn(Float nonMethaneRateIn) {
        this.nonMethaneRateIn = nonMethaneRateIn;
    }

    public Float getSo2ConcIn() {
        return so2ConcIn;
    }

    public void setSo2ConcIn(Float so2ConcIn) {
        this.so2ConcIn = so2ConcIn;
    }

    public Float getNoConcIn() {
        return noConcIn;
    }

    public void setNoConcIn(Float noConcIn) {
        this.noConcIn = noConcIn;
    }

    public Float getNoxConcIn() {
        return noxConcIn;
    }

    public void setNoxConcIn(Float noxConcIn) {
        this.noxConcIn = noxConcIn;
    }

    public Float getMethylAlcoholConc() {
        return methylAlcoholConc;
    }

    public void setMethylAlcoholConc(Float methylAlcoholConc) {
        this.methylAlcoholConc = methylAlcoholConc;
    }

    public Float getMethylAlcoholConcIn() {
        return methylAlcoholConcIn;
    }

    public void setMethylAlcoholConcIn(Float methylAlcoholConcIn) {
        this.methylAlcoholConcIn = methylAlcoholConcIn;
    }

    public Float getStyreneConc() {
        return styreneConc;
    }

    public void setStyreneConc(Float styreneConc) {
        this.styreneConc = styreneConc;
    }

    public Float getStyreneConcIn() {
        return styreneConcIn;
    }

    public void setStyreneConcIn(Float styreneConcIn) {
        this.styreneConcIn = styreneConcIn;
    }
}
