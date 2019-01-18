package com.welltech.dto;

import com.welltech.entity.WtGasAir;

public class WtGasAirDto extends WtGasAir {

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
