package com.vidmt.lmei.entity;

import java.math.BigDecimal;

public class Complaint {
	
    private Integer id;
    
    private Integer video_order;//视频订单号，给图谱的tag就是这个id/人为投诉不需要此字段

    private Integer buy_id;//购买方，即登录方

    private String reason;//投诉原因，固定的，插入即可

    private Integer state;//处理状态1-未处理 2-已处理 3-图谱自动封号,待处理
    

    private String create_date;//申请时间
    

    private String update_date;//处理时间

    private Integer sell_id;//服务方

    private String handle;//处理结果
    
    private String screenshot;//投诉截图图片
    
    private Integer type;//投诉类型0.视频截图自动投诉。1.普通投诉。
    
    private BigDecimal rate;//(鉴黄)图像被识别为某个分类的概率值 0.000
    
    private Integer label;//(鉴黄)分类： 0：色情； 1：性感； 2：正常； 
    
    private Integer review;//(鉴黄）是否需要人工复审该图片  1需要  2不需要
    
	public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason == null ? null : reason.trim();
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getHandle() {
        return handle;
    }

    public void setHandle(String handle) {
        this.handle = handle == null ? null : handle.trim();
    }

	public Integer getBuy_id() {
		return buy_id;
	}

	public void setBuy_id(Integer buyId) {
		buy_id = buyId;
	}

	public String getCreate_date() {
		return create_date;
	}

	public void setCreate_date(String createDate) {
		create_date = createDate;
	}

	public String getUpdate_date() {
		return update_date;
	}

	public void setUpdate_date(String updateDate) {
		update_date = updateDate;
	}

	public Integer getSell_id() {
		return sell_id;
	}

	public void setSell_id(Integer sellId) {
		sell_id = sellId;
	}

	public String getScreenshot() {
		return screenshot;
	}

	public void setScreenshot(String screenshot) {
		this.screenshot = screenshot;
	}
	
	public Integer getVideo_order() {
		return video_order;
	}

	public void setVideo_order(Integer videoOrder) {
		video_order = videoOrder;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public BigDecimal getRate() {
		return rate;
	}

	public void setRate(BigDecimal rate) {
		this.rate = rate;
	}

	public Integer getLabel() {
		return label;
	}

	public void setLabel(Integer label) {
		this.label = label;
	}

	public Integer getReview() {
		return review;
	}

	public void setReview(Integer review) {
		this.review = review;
	}

	public Complaint() {
		super();
	}

	
    
}