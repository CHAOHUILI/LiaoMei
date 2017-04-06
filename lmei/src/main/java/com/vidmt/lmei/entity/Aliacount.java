package com.vidmt.lmei.entity;


//支付宝账号表
public class Aliacount {
    private Integer id;

    private Integer persion_id;

    private String account;//支付宝号

    private String realname;//真实名字

    private Integer remark;//无用
    
    public Aliacount(){}
    
    public Aliacount(Integer id, Integer persionId, String account,
			String realname, Integer remark) {
		super();
		this.id = id;
		persion_id = persionId;
		this.account = account;
		this.realname = realname;
		this.remark = remark;
	}
    
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getPersion_id() {
		return persion_id;
	}

	public void setPersion_id(Integer persionId) {
		persion_id = persionId;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getRealname() {
		return realname;
	}

	public void setRealname(String realname) {
		this.realname = realname;
	}

	public Integer getRemark() {
		return remark;
	}

	public void setRemark(Integer remark) {
		this.remark = remark;
	}
}