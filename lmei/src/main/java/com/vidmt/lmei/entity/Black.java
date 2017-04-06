package com.vidmt.lmei.entity;




//黑名单
public class Black {
    private Integer id;

    private Integer persion_id;//登陆方

    private Integer other_id;//拉入谁进的黑名单

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPersion_id() {
        return persion_id;
    }

    public void setPersion_id(Integer persion_id) {
        this.persion_id = persion_id;
    }

    public Integer getOther_id() {
        return other_id;
    }

    public void setOther_id(Integer other_id) {
        this.other_id = other_id;
    }
}