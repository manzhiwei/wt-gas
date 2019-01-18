package com.welltech.dto;

import com.welltech.entity.WtControl;

public class WtControlDto extends WtControl {

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
