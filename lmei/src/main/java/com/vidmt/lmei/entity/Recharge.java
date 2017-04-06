package com.vidmt.lmei.entity;

import java.util.Date;

/**
 * 充值提现实体表
 * @author 马腾
 * @version 2015.9.22
 */

public class Recharge {
    private Integer id;//编号

    private String create_date;//充值时间

    private Integer persion_id;//用户编号，外键

    private Integer token_package;//套餐编号，外键

    private Double amount;//充值提现金额

    private Integer type;//类型，1.充值，2.提现
    
    private String alipayordersn;//支付宝订单号
    
    private String ordersn;//订单号
    
    private String batchNo;//提现批次号
    
    private Integer state;//状态 1-申请支付，2-支付成功，3-申请提现，4-提现成功,5-提现失败

    private Long credits;//兑换的积分
    
    private int recharge_type;//充值类型 1.支付宝2微信
    
    private int pversion;//充值人的乐观锁
    
    private int f_id;//一级分销商的id
    
    private Double f_rate;//一级分销商分的利润
    
    private int s_id;//二级分销商的id
    
    private Double s_rate;//二级分销商的id
    
    private int t_id;//三级分销商的id
    
    private Double t_rate;//三级分销商分的利润
  

	
	public int getF_id() {
		return f_id;
	}

	public void setF_id(int fId) {
		f_id = fId;
	}

	public Double getF_rate() {
		return f_rate;
	}

	public void setF_rate(Double fRate) {
		f_rate = fRate;
	}

	public int getS_id() {
		return s_id;
	}

	public void setS_id(int sId) {
		s_id = sId;
	}

	public Double getS_rate() {
		return s_rate;
	}

	public void setS_rate(Double sRate) {
		s_rate = sRate;
	}

	public int getT_id() {
		return t_id;
	}

	public void setT_id(int tId) {
		t_id = tId;
	}

	public Double getT_rate() {
		return t_rate;
	}

	public void setT_rate(Double tRate) {
		t_rate = tRate;
	}

	public int getPversion() {
		return pversion;
	}

	public void setPversion(int pversion) {
		this.pversion = pversion;
	}

	public Recharge() {
		super();
	}

	public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }


    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

	public String getCreate_date() {
		return create_date;
	}

	public void setCreate_date(String createDate) {
		create_date = createDate;
	}

	public Integer getPersion_id() {
		return persion_id;
	}

	public void setPersion_id(Integer persionId) {
		persion_id = persionId;
	}

	public Integer getToken_package() {
		return token_package;
	}

	public void setToken_package(Integer tokenPackage) {
		token_package = tokenPackage;
	}

	public String getAlipayordersn() {
		return alipayordersn;
	}

	public void setAlipayordersn(String alipayordersn) {
		this.alipayordersn = alipayordersn;
	}

	public String getOrdersn() {
		return ordersn;
	}

	public void setOrdersn(String ordersn) {
		this.ordersn = ordersn;
	}

	public String getBatchNo() {
		return batchNo;
	}

	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}

	
	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public Long getCredits() {
		return credits;
	}

	public void setCredits(Long credits) {
		this.credits = credits;
	}

	public int getRecharge_type() {
		return recharge_type;
	}

	public void setRecharge_type(int rechargeType) {
		recharge_type = rechargeType;
	}

	@Override
	public String toString() {
		return "Recharge [alipayordersn=" + alipayordersn + ", amount="
				+ amount + ", batchNo=" + batchNo + ", create_date="
				+ create_date + ", credits=" + credits + ", id=" + id
				+ ", ordersn=" + ordersn + ", persion_id=" + persion_id
				+ ", recharge_type=" + recharge_type + ", state=" + state
				+ ", token_package=" + token_package + ", type=" + type + "]";
	}
}