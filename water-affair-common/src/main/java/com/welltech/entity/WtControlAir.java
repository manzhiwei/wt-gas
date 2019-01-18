package com.welltech.entity;

import java.util.Date;

public class WtControlAir {

    private Integer id;

    private String  mcuId;

    private Date    time;

    private Float   humidity;

    private Float   temp;

    private Float   electricity;

    private Float   high;

    private Float   flow;

    private Integer   mode;

    private Integer   waterStatus;

    private Integer   waterGround;

    private Integer   airStatus;

    private Integer   airRunStatus;

    private Integer   airForbidden;

    private Date startTime;

    private Date endTime;

    private Date samplingStartTime;

    private Date samplingEndTime;

    private Date overSampleTime;

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Date getSamplingStartTime() {
        return samplingStartTime;
    }

    public void setSamplingStartTime(Date samplingStartTime) {
        this.samplingStartTime = samplingStartTime;
    }

    public Date getSamplingEndTime() {
        return samplingEndTime;
    }

    public void setSamplingEndTime(Date samplingEndTime) {
        this.samplingEndTime = samplingEndTime;
    }

    public Date getOverSampleTime() {
        return overSampleTime;
    }

    public void setOverSampleTime(Date overSampleTime) {
        this.overSampleTime = overSampleTime;
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

    public Float getHumidity() {
        return humidity;
    }

    public void setHumidity(Float humidity) {
        this.humidity = humidity;
    }

    public Float getTemp() {
        return temp;
    }

    public void setTemp(Float temp) {
        this.temp = temp;
    }

    public Float getElectricity() {
        return electricity;
    }

    public void setElectricity(Float electricity) {
        this.electricity = electricity;
    }

    public Float getHigh() {
        return high;
    }

    public void setHigh(Float high) {
        this.high = high;
    }

    public Float getFlow() {
        return flow;
    }

    public void setFlow(Float flow) {
        this.flow = flow;
    }

    public Integer getMode() {
        return mode;
    }

    public void setMode(Integer mode) {
        this.mode = mode;
    }

    public Integer getWaterStatus() {
        return waterStatus;
    }

    public void setWaterStatus(Integer waterStatus) {
        this.waterStatus = waterStatus;
    }

    public Integer getWaterGround() {
        return waterGround;
    }

    public void setWaterGround(Integer waterGround) {
        this.waterGround = waterGround;
    }

    public Integer getAirStatus() {
        return airStatus;
    }

    public void setAirStatus(Integer airStatus) {
        this.airStatus = airStatus;
    }

    public Integer getAirRunStatus() {
        return airRunStatus;
    }

    public void setAirRunStatus(Integer airRunStatus) {
        this.airRunStatus = airRunStatus;
    }

    public Integer getAirForbidden() {
        return airForbidden;
    }

    public void setAirForbidden(Integer airForbidden) {
        this.airForbidden = airForbidden;
    }
}
