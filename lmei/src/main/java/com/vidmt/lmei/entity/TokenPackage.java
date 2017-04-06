package com.vidmt.lmei.entity;



public class TokenPackage {
    private Integer id;

    private Double money;//套餐价格

    private Integer give;//套餐赠送爱意

    private Integer token_count;//购买的爱意

    private Integer type;//1-andriod,2-apple

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Double getMoney() {
        return money;
    }

    public void setMoney(Double money) {
        this.money = money;
    }

    public Integer getGive() {
        return give;
    }

    public void setGive(Integer give) {
        this.give = give;
    }

    public Integer getToken_count() {
        return token_count;
    }

    public void setToken_count(Integer token_count) {
        this.token_count = token_count;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}