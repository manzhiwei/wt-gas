package com.welltech.entity;

import java.util.Date;

public class WtControlLimit {

    private Integer id;

    private String  mcuId;

    private Integer address;

    private Integer swtich;

    private Float  limitValue;

    private Float  trueValue;

    private Float  lastValue;

    private Float  writeValue;

    private Date  receiveTime;

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

    public Integer getAddress() {
        return address;
    }

    public void setAddress(Integer address) {
        this.address = address;
    }

    public Integer getSwtich() {
        return swtich;
    }

    public void setSwtich(Integer swtich) {
        this.swtich = swtich;
    }

    public Float getLimitValue() {
        return limitValue;
    }

    public void setLimitValue(Float limitValue) {
        this.limitValue = limitValue;
    }

    public Float getTrueValue() {
        return trueValue;
    }

    public void setTrueValue(Float trueValue) {
        this.trueValue = trueValue;
    }

    public Float getLastValue() {
        return lastValue;
    }

    public void setLastValue(Float lastValue) {
        this.lastValue = lastValue;
    }

    public Float getWriteValue() {
        return writeValue;
    }

    public void setWriteValue(Float writeValue) {
        this.writeValue = writeValue;
    }

    public Date getReceiveTime() {
        return receiveTime;
    }

    public void setReceiveTime(Date receiveTime) {
        this.receiveTime = receiveTime;
    }
}
