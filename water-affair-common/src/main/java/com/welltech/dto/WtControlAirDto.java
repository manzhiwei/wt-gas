package com.welltech.dto;

import com.welltech.entity.WtControlAir;

public class WtControlAirDto extends WtControlAir {

    private Integer pointId;

    private String point;

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
}
