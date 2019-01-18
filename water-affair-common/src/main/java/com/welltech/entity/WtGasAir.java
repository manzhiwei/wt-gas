package com.welltech.entity;

import java.util.Date;

public class WtGasAir {
    private Integer id;

    private String mcuId;

    private Date time;

    private Float humidity;

    private Float temp;

    private Float currentI;

    private Integer currentForbidden;

    private Integer currentControlStat;

    private Integer currentRunStat;

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

    public Float getCurrentI() {
        return currentI;
    }

    public void setCurrentI(Float currentI) {
        this.currentI = currentI;
    }

    public Integer getCurrentForbidden() {
        return currentForbidden;
    }

    public void setCurrentForbidden(Integer currentForbidden) {
        this.currentForbidden = currentForbidden;
    }

    public Integer getCurrentControlStat() {
        return currentControlStat;
    }

    public void setCurrentControlStat(Integer currentControlStat) {
        this.currentControlStat = currentControlStat;
    }

    public Integer getCurrentRunStat() {
        return currentRunStat;
    }

    public void setCurrentRunStat(Integer currentRunStat) {
        this.currentRunStat = currentRunStat;
    }
}
