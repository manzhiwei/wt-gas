package com.welltech.dto;

import com.welltech.entity.WtProtocolDay;

import java.util.Map;

public class WtProtocolDayDto extends WtProtocolDay {

    private Integer pointId;

    private String point;

    /**
     * 参数-数值键值
     */
    private Map<String, Object> paramValues;

    public Integer getPointId() {
        return pointId;
    }

    public void setPointId(Integer pointId) {
        this.pointId = pointId;
    }

    public String getPoint() {
        return point;
    }

    public void setPoint(String point) {
        this.point = point;
    }

    public Map<String, Object> getParamValues() {
        return paramValues;
    }

    public void setParamValues(Map<String, Object> paramValues) {
        this.paramValues = paramValues;
    }
}
