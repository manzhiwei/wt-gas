package com.welltech.entity;

import java.util.Date;

public class WtControl {

    private Integer id;

    private String mcuId;

    private Date  time;

    private String  channel;

    private Integer channelType;

    private Float measValue;

    private Integer measMode;

    private Date measTime;

    private String status;

    private Integer alarmRecord;

    private Date  systime;

    private Float  kValue;

    private Float  bValue;

    private Integer tValue;

    private Integer sValue;

    private String scheme;

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

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public Integer getChannelType() {
        return channelType;
    }

    public void setChannelType(Integer channelType) {
        this.channelType = channelType;
    }

    public Float getMeasValue() {
        return measValue;
    }

    public void setMeasValue(Float measValue) {
        this.measValue = measValue;
    }

    public Integer getMeasMode() {
        return measMode;
    }

    public void setMeasMode(Integer measMode) {
        this.measMode = measMode;
    }

    public Date getMeasTime() {
        return measTime;
    }

    public void setMeasTime(Date measTime) {
        this.measTime = measTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getAlarmRecord() {
        return alarmRecord;
    }

    public void setAlarmRecord(Integer alarmRecord) {
        this.alarmRecord = alarmRecord;
    }

    public Date getSystime() {
        return systime;
    }

    public void setSystime(Date systime) {
        this.systime = systime;
    }

    public Float getkValue() {
        return kValue;
    }

    public void setkValue(Float kValue) {
        this.kValue = kValue;
    }

    public Float getbValue() {
        return bValue;
    }

    public void setbValue(Float bValue) {
        this.bValue = bValue;
    }

    public Integer gettValue() {
        return tValue;
    }

    public void settValue(Integer tValue) {
        this.tValue = tValue;
    }

    public Integer getsValue() {
        return sValue;
    }

    public void setsValue(Integer sValue) {
        this.sValue = sValue;
    }

    public String getScheme() {
        return scheme;
    }

    public void setScheme(String scheme) {
        this.scheme = scheme;
    }
}
