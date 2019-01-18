package com.welltech.entity;

import com.welltech.framework.dao.Column;

import java.util.Date;

public class WtGasAlarmStat {

    private Integer id;

    private String mcuId;

    private Date time;

    private Integer systemStat;

    private String systemStatComment;

    private Integer colorbookStat;

    private String colorbookStatComment;

    private  Integer machineStat;

    private String machineStatComment;

    private Integer autoRunStat;

    private Integer systemMaintainStat;

    private Integer systemBreakdownStat;

    private Integer gaowenheTempAlarm;

    private Integer sampleTempAlarm;

    private Integer banreguanTempAlarm;

    private Integer fanchuifaOpenStat;

    private Integer caiyangbengOpenStat;

    private Integer kongzhifaOpenStat;

    private Integer sample2tempAlarm;

    private Integer banreguan2tempAlarm;

    private Integer fanchuifa2openStat;

    private Integer caiyangbeng2openStat;

    private Integer kongzhifa2openStat;

    private Integer innerCommExcp;

    private Integer fid1ignitionFalse;

    private Integer fid2ignitionFalse;

    private Integer fid1fireBreak;

    private Integer fid2fireBreak;

    private Integer fid1fireTempExcp;

    private Integer fid2fireTempExcp;

    private Integer fid1boxTempExcp;

    private Integer fid2boxTempExcp;

    private Integer shitongfaTempExcp;

    private Integer zhuxiangTempExcp;

    private Integer zhuxiang2tempExcp;

    private Integer jixiangTempExcp;

    private Integer hydrogen1flowExcp;

    private Integer hydrogen2flowExcp;

    private Integer air1flowExcp;

    private Integer air2flowExcp;

    private Integer zhuqianya1pressExcp;

    private Integer zhuqianya2pressExcp;

    private Integer zhuqianya3pressExcp;





    private Integer so2concUpAlarm;

    private Integer so2concDownAlarm;

    private Integer noConcUpAlarm;

    private Integer noConcDownAlarm;

    private Integer noxConcUpAlarm;

    private Integer noxConcDownAlarm;

    private Integer o2concUpAlarm;

    private Integer o2concDownAlarm;

    private Integer so2overRange;

    private Integer noOverRange;

    private Integer noxOverRange;

    private Integer o2overRange;

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

    public Integer getSystemStat() {
        return systemStat;
    }

    public void setSystemStat(Integer systemStat) {
        this.systemStat = systemStat;
    }

    public String getSystemStatComment() {
        return systemStatComment;
    }

    public void setSystemStatComment(String systemStatComment) {
        this.systemStatComment = systemStatComment;
    }

    public Integer getMachineStat() {
        return machineStat;
    }

    public void setMachineStat(Integer machineStat) {
        this.machineStat = machineStat;
    }

    public String getMachineStatComment() {
        return machineStatComment;
    }

    public void setMachineStatComment(String machineStatComment) {
        this.machineStatComment = machineStatComment;
    }

    public Integer getInnerCommExcp() {
        return innerCommExcp;
    }

    public void setInnerCommExcp(Integer innerCommExcp) {
        this.innerCommExcp = innerCommExcp;
    }

    public Integer getFid1ignitionFalse() {
        return fid1ignitionFalse;
    }

    public void setFid1ignitionFalse(Integer fid1ignitionFalse) {
        this.fid1ignitionFalse = fid1ignitionFalse;
    }

    public Integer getFid2ignitionFalse() {
        return fid2ignitionFalse;
    }

    public void setFid2ignitionFalse(Integer fid2ignitionFalse) {
        this.fid2ignitionFalse = fid2ignitionFalse;
    }

    public Integer getFid1fireBreak() {
        return fid1fireBreak;
    }

    public void setFid1fireBreak(Integer fid1fireBreak) {
        this.fid1fireBreak = fid1fireBreak;
    }

    public Integer getFid2fireBreak() {
        return fid2fireBreak;
    }

    public void setFid2fireBreak(Integer fid2fireBreak) {
        this.fid2fireBreak = fid2fireBreak;
    }

    public Integer getFid1fireTempExcp() {
        return fid1fireTempExcp;
    }

    public void setFid1fireTempExcp(Integer fid1fireTempExcp) {
        this.fid1fireTempExcp = fid1fireTempExcp;
    }

    public Integer getFid2fireTempExcp() {
        return fid2fireTempExcp;
    }

    public void setFid2fireTempExcp(Integer fid2fireTempExcp) {
        this.fid2fireTempExcp = fid2fireTempExcp;
    }

    public Integer getFid1boxTempExcp() {
        return fid1boxTempExcp;
    }

    public void setFid1boxTempExcp(Integer fid1boxTempExcp) {
        this.fid1boxTempExcp = fid1boxTempExcp;
    }

    public Integer getFid2boxTempExcp() {
        return fid2boxTempExcp;
    }

    public void setFid2boxTempExcp(Integer fid2boxTempExcp) {
        this.fid2boxTempExcp = fid2boxTempExcp;
    }

    public Integer getShitongfaTempExcp() {
        return shitongfaTempExcp;
    }

    public void setShitongfaTempExcp(Integer shitongfaTempExcp) {
        this.shitongfaTempExcp = shitongfaTempExcp;
    }

    public Integer getZhuxiangTempExcp() {
        return zhuxiangTempExcp;
    }

    public void setZhuxiangTempExcp(Integer zhuxiangTempExcp) {
        this.zhuxiangTempExcp = zhuxiangTempExcp;
    }

    public Integer getJixiangTempExcp() {
        return jixiangTempExcp;
    }

    public void setJixiangTempExcp(Integer jixiangTempExcp) {
        this.jixiangTempExcp = jixiangTempExcp;
    }

    public Integer getHydrogen1flowExcp() {
        return hydrogen1flowExcp;
    }

    public void setHydrogen1flowExcp(Integer hydrogen1flowExcp) {
        this.hydrogen1flowExcp = hydrogen1flowExcp;
    }

    public Integer getHydrogen2flowExcp() {
        return hydrogen2flowExcp;
    }

    public void setHydrogen2flowExcp(Integer hydrogen2flowExcp) {
        this.hydrogen2flowExcp = hydrogen2flowExcp;
    }

    public Integer getAir1flowExcp() {
        return air1flowExcp;
    }

    public void setAir1flowExcp(Integer air1flowExcp) {
        this.air1flowExcp = air1flowExcp;
    }

    public Integer getAir2flowExcp() {
        return air2flowExcp;
    }

    public void setAir2flowExcp(Integer air2flowExcp) {
        this.air2flowExcp = air2flowExcp;
    }

    public Integer getZhuqianya1pressExcp() {
        return zhuqianya1pressExcp;
    }

    public void setZhuqianya1pressExcp(Integer zhuqianya1pressExcp) {
        this.zhuqianya1pressExcp = zhuqianya1pressExcp;
    }

    public Integer getZhuqianya2pressExcp() {
        return zhuqianya2pressExcp;
    }

    public void setZhuqianya2pressExcp(Integer zhuqianya2pressExcp) {
        this.zhuqianya2pressExcp = zhuqianya2pressExcp;
    }

    public Integer getZhuqianya3pressExcp() {
        return zhuqianya3pressExcp;
    }

    public void setZhuqianya3pressExcp(Integer zhuqianya3pressExcp) {
        this.zhuqianya3pressExcp = zhuqianya3pressExcp;
    }

    public Integer getSampleTempAlarm() {
        return sampleTempAlarm;
    }

    public void setSampleTempAlarm(Integer sampleTempAlarm) {
        this.sampleTempAlarm = sampleTempAlarm;
    }

    public Integer getBanreguanTempAlarm() {
        return banreguanTempAlarm;
    }

    public void setBanreguanTempAlarm(Integer banreguanTempAlarm) {
        this.banreguanTempAlarm = banreguanTempAlarm;
    }

    public Integer getFanchuifaOpenStat() {
        return fanchuifaOpenStat;
    }

    public void setFanchuifaOpenStat(Integer fanchuifaOpenStat) {
        this.fanchuifaOpenStat = fanchuifaOpenStat;
    }

    public Integer getCaiyangbengOpenStat() {
        return caiyangbengOpenStat;
    }

    public void setCaiyangbengOpenStat(Integer caiyangbengOpenStat) {
        this.caiyangbengOpenStat = caiyangbengOpenStat;
    }

    public Integer getKongzhifaOpenStat() {
        return kongzhifaOpenStat;
    }

    public void setKongzhifaOpenStat(Integer kongzhifaOpenStat) {
        this.kongzhifaOpenStat = kongzhifaOpenStat;
    }

    public Integer getAutoRunStat() {
        return autoRunStat;
    }

    public void setAutoRunStat(Integer autoRunStat) {
        this.autoRunStat = autoRunStat;
    }

    public Integer getSystemMaintainStat() {
        return systemMaintainStat;
    }

    public void setSystemMaintainStat(Integer systemMaintainStat) {
        this.systemMaintainStat = systemMaintainStat;
    }

    public Integer getSystemBreakdownStat() {
        return systemBreakdownStat;
    }

    public void setSystemBreakdownStat(Integer systemBreakdownStat) {
        this.systemBreakdownStat = systemBreakdownStat;
    }

    public Integer getSo2concUpAlarm() {
        return so2concUpAlarm;
    }

    public void setSo2concUpAlarm(Integer so2concUpAlarm) {
        this.so2concUpAlarm = so2concUpAlarm;
    }

    public Integer getSo2concDownAlarm() {
        return so2concDownAlarm;
    }

    public void setSo2concDownAlarm(Integer so2concDownAlarm) {
        this.so2concDownAlarm = so2concDownAlarm;
    }

    public Integer getNoConcUpAlarm() {
        return noConcUpAlarm;
    }

    public void setNoConcUpAlarm(Integer noConcUpAlarm) {
        this.noConcUpAlarm = noConcUpAlarm;
    }

    public Integer getNoConcDownAlarm() {
        return noConcDownAlarm;
    }

    public void setNoConcDownAlarm(Integer noConcDownAlarm) {
        this.noConcDownAlarm = noConcDownAlarm;
    }

    public Integer getNoxConcUpAlarm() {
        return noxConcUpAlarm;
    }

    public void setNoxConcUpAlarm(Integer noxConcUpAlarm) {
        this.noxConcUpAlarm = noxConcUpAlarm;
    }

    public Integer getNoxConcDownAlarm() {
        return noxConcDownAlarm;
    }

    public void setNoxConcDownAlarm(Integer noxConcDownAlarm) {
        this.noxConcDownAlarm = noxConcDownAlarm;
    }

    public Integer getO2concUpAlarm() {
        return o2concUpAlarm;
    }

    public void setO2concUpAlarm(Integer o2concUpAlarm) {
        this.o2concUpAlarm = o2concUpAlarm;
    }

    public Integer getO2concDownAlarm() {
        return o2concDownAlarm;
    }

    public void setO2concDownAlarm(Integer o2concDownAlarm) {
        this.o2concDownAlarm = o2concDownAlarm;
    }

    public Integer getSo2overRange() {
        return so2overRange;
    }

    public void setSo2overRange(Integer so2overRange) {
        this.so2overRange = so2overRange;
    }

    public Integer getNoOverRange() {
        return noOverRange;
    }

    public void setNoOverRange(Integer noOverRange) {
        this.noOverRange = noOverRange;
    }

    public Integer getNoxOverRange() {
        return noxOverRange;
    }

    public void setNoxOverRange(Integer noxOverRange) {
        this.noxOverRange = noxOverRange;
    }

    public Integer getO2overRange() {
        return o2overRange;
    }

    public void setO2overRange(Integer o2overRange) {
        this.o2overRange = o2overRange;
    }

    public Integer getColorbookStat() {
        return colorbookStat;
    }

    public void setColorbookStat(Integer colorbookStat) {
        this.colorbookStat = colorbookStat;
    }

    public String getColorbookStatComment() {
        return colorbookStatComment;
    }

    public void setColorbookStatComment(String colorbookStatComment) {
        this.colorbookStatComment = colorbookStatComment;
    }

    public Integer getGaowenheTempAlarm() {
        return gaowenheTempAlarm;
    }

    public void setGaowenheTempAlarm(Integer gaowenheTempAlarm) {
        this.gaowenheTempAlarm = gaowenheTempAlarm;
    }

    public Integer getSample2tempAlarm() {
        return sample2tempAlarm;
    }

    public void setSample2tempAlarm(Integer sample2tempAlarm) {
        this.sample2tempAlarm = sample2tempAlarm;
    }

    public Integer getBanreguan2tempAlarm() {
        return banreguan2tempAlarm;
    }

    public void setBanreguan2tempAlarm(Integer banreguan2tempAlarm) {
        this.banreguan2tempAlarm = banreguan2tempAlarm;
    }

    public Integer getFanchuifa2openStat() {
        return fanchuifa2openStat;
    }

    public void setFanchuifa2openStat(Integer fanchuifa2openStat) {
        this.fanchuifa2openStat = fanchuifa2openStat;
    }

    public Integer getCaiyangbeng2openStat() {
        return caiyangbeng2openStat;
    }

    public void setCaiyangbeng2openStat(Integer caiyangbeng2openStat) {
        this.caiyangbeng2openStat = caiyangbeng2openStat;
    }

    public Integer getKongzhifa2openStat() {
        return kongzhifa2openStat;
    }

    public void setKongzhifa2openStat(Integer kongzhifa2openStat) {
        this.kongzhifa2openStat = kongzhifa2openStat;
    }

    public Integer getZhuxiang2tempExcp() {
        return zhuxiang2tempExcp;
    }

    public void setZhuxiang2tempExcp(Integer zhuxiang2tempExcp) {
        this.zhuxiang2tempExcp = zhuxiang2tempExcp;
    }
}
