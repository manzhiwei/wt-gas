package com.welltech.dto;

import java.util.List;

public class WtDataVo {
    private int total;

    private int start;

    private int count;


    private List<WtDataDto> subjects;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<WtDataDto> getSubjects() {
        return subjects;
    }

    public void setSubjects(List<WtDataDto> subjects) {
        this.subjects = subjects;
    }
}
