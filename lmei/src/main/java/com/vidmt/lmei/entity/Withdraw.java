package com.vidmt.lmei.entity;


import java.math.BigDecimal;
import java.util.Date;



/**
 * 提现表
 * @author style
 *
 */
public class Withdraw {
	
	private Integer id;
	private String onceid;//唯一标识
	private Integer pid;//用户编号
	private BigDecimal money;//提现金额
	private String createdate;//提现时间
	private String handledate;//处理时间
	private Integer state;//提现状态：1提现申请  2同意提现  3 提现成功  4 提现失败
	private String batchNo;//流水账号
	private Integer adminid;//处理人id
	private BigDecimal platform_cost;//手续费
	
	public Withdraw() {
		super();
	}
	

	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getOnceid() {
		return onceid;
	}
	public void setOnceid(String onceid) {
		this.onceid = onceid;
	}
	public Integer getPid() {
		return pid;
	}
	public void setPid(Integer pid) {
		this.pid = pid;
	}
	public BigDecimal getMoney() {
		return money;
	}
	public void setMoney(BigDecimal money) {
		this.money = money;
	}
	public String getCreatedate() {
		return createdate;
	}
	public void setCreatedate(String createdate) {
		this.createdate = createdate;
	}
	public String getHandledate() {
		return handledate;
	}
	public void setHandledate(String handledate) {
		this.handledate = handledate;
	}
	public Integer getState() {
		return state;
	}
	public void setState(Integer state) {
		this.state = state;
	}
	public String getBatchNo() {
		return batchNo;
	}
	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}
	public Integer getAdminid() {
		return adminid;
	}
	public void setAdminid(Integer adminid) {
		this.adminid = adminid;
	}
	
	public BigDecimal getPlatform_cost() {
		return platform_cost;
	}

	public void setPlatform_cost(BigDecimal platformCost) {
		platform_cost = platformCost;
	}

	@Override
	public String toString() {
		return "Withdraw [adminid=" + adminid + ", batchNo=" + batchNo
				+ ", createdate=" + createdate + ", handledate=" + handledate
				+ ", id=" + id + ", money=" + money + ", onceid=" + onceid
				+ ", pid=" + pid + ", platform_cost=" + platform_cost
				+ ", state=" + state + "]";
	}
}
