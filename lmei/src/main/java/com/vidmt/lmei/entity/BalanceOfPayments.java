package com.vidmt.lmei.entity;

import java.util.Date;



//收支表
public class BalanceOfPayments{
    private Integer id;

    private Integer persion_id;//登陆方

    private Integer type;//类型。1-文字，2-语音，3-视频，4-礼物`

    private Integer token;//爱意	支出爱意(爱意)

    private String create_date;

    private String finish_date;

    private Integer state;//1-收入，2-支出     暂无作用
    
    private Integer present;//礼物外键
    
    private Integer chat;//订单外键		 暂无作用
    
    private String present_name;//礼物名字
    
    
    private Integer other_id;//对方id
    private String other_name;//对方名
    private Double capital;//资金
    
    private Integer charm;//送礼物的魅力值	暂无作用
    
    private String rong_order_id;//容联云视频语音通话订单id		暂无作用
    
    private Double adminget;//平台所挣的利润
    
    private int f_id;//一级分销商的id
    
    private Double f_rate;//一级分销商分的利润
    
    private int s_id;//二级分销商的id
    
    private Double s_rate;//二级分销商的id
    
    private int t_id;//三级分销商的id
    
    private Double t_rate;//三级分销商分的利润
    
    private Persion buyPersion;//消费的用户
    
    private Persion sellPersion;//收入的用户
    
    private int fversion;//f的乐观锁
    
    private int sversion;//s的乐观锁
    
    public BalanceOfPayments() {
		super();
	}

	public BalanceOfPayments(Integer persionId, Integer type, Integer token,
			Integer otherId, Double capital) {
		super();
		persion_id = persionId;
		this.type = type;
		this.token = token;
		other_id = otherId;
		this.capital = capital;
	}
	
	public BalanceOfPayments(Integer persionId, Integer type, String createDate,
			Integer otherId,Integer f_id,Integer s_id) {
		super();
		persion_id = persionId;
		this.type = type;
		create_date = createDate;
		other_id = otherId;
		this.f_id = f_id;
		this.s_id = s_id;
	}

	public BalanceOfPayments(Integer persionId, Integer type, Integer state) {
		super();
		persion_id = persionId;
		this.type = type;
	}

	public BalanceOfPayments(Integer persionId, Integer type, Integer token,
			Integer state) {
		super();
		persion_id = persionId;
		this.type = type;
		this.token = token;
	}
	public BalanceOfPayments(Integer persionId, Integer type, Integer token,
			Integer state,Integer present) {
		super();
		persion_id = persionId;
		this.type = type;
		this.token = token;
		this.present=present;
	}
	public BalanceOfPayments(Integer persionId, Integer type, Integer token,
			Integer present, Integer chat, Integer otherId, Double capital,Integer charm) {
		super();
		persion_id = persionId;
		this.type = type;
		this.token = token;
		this.present = present;
		this.chat = chat;
		other_id = otherId;
		this.capital = capital;
		this.charm=charm;
	}
	public BalanceOfPayments(Integer persionId, Integer type, Integer token,
			Integer present, Integer chat, Integer otherId, Double capital) {
		super();
		persion_id = persionId;
		this.type = type;
		this.token = token;
		this.present = present;
		this.chat = chat;
		other_id = otherId;
		this.capital = capital;
	}
	/**
	 * 聊天专用
	 * @param persionId
	 * @param type
	 * @param token
	 * @param createDate
	 * @param finishDate
	 * @param present
	 * @param otherId
	 * @param capital
	 * @param adminget
	 * @param fId
	 * @param fRate
	 * @param sId
	 * @param sRate
	 * @param tId
	 * @param tRate
	 */
	public BalanceOfPayments(Integer persionId, Integer type, Integer token,
			String createDate, String finishDate, Integer present, Integer otherId,
			Double capital, Double adminget, int fId, Double fRate, int sId,
			Double sRate, int tId, Double tRate) {
		super();
		persion_id = persionId;
		this.type = type;
		this.token = token;
		create_date = createDate;
		finish_date = finishDate;
		this.present = present;
		other_id = otherId;
		this.capital = capital;
		this.adminget = adminget;
		f_id = fId;
		f_rate = fRate;
		s_id = sId;
		s_rate = sRate;
		t_id = tId;
		t_rate = tRate;
	}

	public BalanceOfPayments(Integer id, Integer persionId, Integer type,
			Integer token, String createDate, String finishDate, Integer state,
			Integer present, Integer chat, String presentName, Integer otherId,
			String otherName, Double capital, Integer charm,
			String rongOrderId, Double adminget, int fId, Double fRate,
			int sId, Double sRate, int tId, Double tRate) {
		super();
		this.id = id;
		persion_id = persionId;
		this.type = type;
		this.token = token;
		create_date = createDate;
		finish_date = finishDate;
		this.state = state;
		this.present = present;
		this.chat = chat;
		present_name = presentName;
		other_id = otherId;
		other_name = otherName;
		this.capital = capital;
		this.charm = charm;
		rong_order_id = rongOrderId;
		this.adminget = adminget;
		f_id = fId;
		f_rate = fRate;
		s_id = sId;
		s_rate = sRate;
		t_id = tId;
		t_rate = tRate;
	}
	
	public BalanceOfPayments(Integer id, Integer persionId, Integer type,
			Integer token, String createDate, String finishDate, Integer state,
			Integer present, Integer chat, String presentName, Integer otherId,
			String otherName, Double capital, Integer charm,
			String rongOrderId, Double adminget, int fId, Double fRate,
			int sId, Double sRate, int tId, Double tRate, Persion buyPersion,
			Persion sellPersion) {
		super();
		this.id = id;
		persion_id = persionId;
		this.type = type;
		this.token = token;
		create_date = createDate;
		finish_date = finishDate;
		this.state = state;
		this.present = present;
		this.chat = chat;
		present_name = presentName;
		other_id = otherId;
		other_name = otherName;
		this.capital = capital;
		this.charm = charm;
		rong_order_id = rongOrderId;
		this.adminget = adminget;
		f_id = fId;
		f_rate = fRate;
		s_id = sId;
		s_rate = sRate;
		t_id = tId;
		t_rate = tRate;
		this.buyPersion = buyPersion;
		this.sellPersion = sellPersion;
	}
	
	public BalanceOfPayments(Integer id, Integer persionId, Integer type,
			Integer token, String createDate, String finishDate, Integer state,
			Integer present, Integer chat, String presentName, Integer otherId,
			String otherName, Double capital, Integer charm,
			String rongOrderId, Double adminget, int fId, Double fRate,
			int sId, Double sRate, int tId, Double tRate, Persion buyPersion,
			Persion sellPersion, int fversion, int sversion) {
		super();
		this.id = id;
		persion_id = persionId;
		this.type = type;
		this.token = token;
		create_date = createDate;
		finish_date = finishDate;
		this.state = state;
		this.present = present;
		this.chat = chat;
		present_name = presentName;
		other_id = otherId;
		other_name = otherName;
		this.capital = capital;
		this.charm = charm;
		rong_order_id = rongOrderId;
		this.adminget = adminget;
		f_id = fId;
		f_rate = fRate;
		s_id = sId;
		s_rate = sRate;
		t_id = tId;
		t_rate = tRate;
		this.buyPersion = buyPersion;
		this.sellPersion = sellPersion;
		this.fversion = fversion;
		this.sversion = sversion;
	}

	public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getToken() {
        return token;
    }

    public void setToken(Integer token) {
        this.token = token;
    }


	public Integer getPersion_id() {
		return persion_id;
	}

	public void setPersion_id(Integer persionId) {
		persion_id = persionId;
	}

	public String getCreate_date() {
		return create_date;
	}

	public void setCreate_date(String createDate) {
		create_date = createDate;
	}

	
	public Integer getPresent() {
		return present;
	}

	public void setPresent(Integer present) {
		this.present = present;
	}

	public Integer getChat() {
		return chat;
	}

	public void setChat(Integer chat) {
		this.chat = chat;
	}

	public String getPresent_name() {
		return present_name;
	}

	public void setPresent_name(String presentName) {
		present_name = presentName;
	}

	public Integer getOther_id() {
		return other_id;
	}

	public void setOther_id(Integer otherId) {
		other_id = otherId;
	}

	public String getOther_name() {
		return other_name;
	}

	public void setOther_name(String otherName) {
		other_name = otherName;
	}

	public Double getCapital() {
		return capital;
	}

	public void setCapital(Double capital) {
		this.capital = capital;
	}

	public Integer getCharm() {
		return charm;
	}

	public void setCharm(Integer charm) {
		this.charm = charm;
	}

	public String getRong_order_id() {
		return rong_order_id;
	}

	public void setRong_order_id(String rongOrderId) {
		rong_order_id = rongOrderId;
	}
	
	public String getFinish_date() {
		return finish_date;
	}

	public void setFinish_date(String finishDate) {
		finish_date = finishDate;
	}
	
	public Double getAdminget() {
		return adminget;
	}

	public void setAdminget(Double adminget) {
		this.adminget = adminget;
	}

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
	
	public Persion getBuyPersion() {
		return buyPersion;
	}

	public void setBuyPersion(Persion buyPersion) {
		this.buyPersion = buyPersion;
	}

	public Persion getSellPersion() {
		return sellPersion;
	}

	public void setSellPersion(Persion sellPersion) {
		this.sellPersion = sellPersion;
	}
	
	public int getFversion() {
		return fversion;
	}

	public void setFversion(int fversion) {
		this.fversion = fversion;
	}

	public int getSversion() {
		return sversion;
	}

	public void setSversion(int sversion) {
		this.sversion = sversion;
	}

	@Override
	public String toString() {
		return "BalanceOfPayments [capital=" + capital + ", charm=" + charm
				+ ", chat=" + chat + ", create_date=" + create_date + ", id="
				+ id + ", other_id=" + other_id + ", other_name=" + other_name
				+ ", persion_id=" + persion_id + ", present=" + present
				+ ", present_name=" + present_name + ", rong_order_id="
				+ rong_order_id + ", token=" + token
				+ ", type=" + type + "]";
	}

}