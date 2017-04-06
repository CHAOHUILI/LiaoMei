package com.vidmt.lmei.entity;


//点赞记录表
public class PasteRecord {
    private Integer id;

    private Integer persion_id;//登录用户

    private Integer accept_id;//被点赞对方id
    
    public PasteRecord() {
		super();
	}

	public PasteRecord(Integer id) {
		super();
		this.id = id;
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

	public Integer getAccept_id() {
		return accept_id;
	}

	public void setAccept_id(Integer acceptId) {
		accept_id = acceptId;
	}

}