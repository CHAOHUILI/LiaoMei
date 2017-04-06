package com.vidmt.lmei.entity;

import java.util.Date;




//礼物列表
public class Present{
	private Integer id;

	private String present_name;//礼物名

	private String present_img;//礼物图片

	private int price;//礼物价格

	private String create_date;//创建时间
	private String create_date1;//接收时间
	private String nick_name;//来自",赠送人的名称
	private String photo;//赠送人的头像
	private int buy_count;//被购买数量
	/** 
	 * 栏目是否选中
	 *  */
	public int selected;//是否选中1 0为未选中 1为选

	private String result_img;//礼物发送图片


/*	private Persion persion;


	public Persion getPersion() {
		return persion;
	}

	public void setPersion(Persion persion) {
		this.persion = persion;
	}*/

	public String getCreate_date1() {
		return create_date1;
	}

	public void setCreate_date1(String create_date1) {
		this.create_date1 = create_date1;
	}

	public String getNick_name() {
		return nick_name;
	}

	public void setNick_name(String nick_name) {
		this.nick_name = nick_name;
	}

	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getResult_img() {
		return result_img;
	}

	public void setResult_img(String result_img) {
		this.result_img = result_img;
	}

	public Present() {
		super();
	}

	public Present(String present_name, int price) {
		super();
		this.present_name = present_name;
		this.price = price;
	}

	public int getSelected() {
		return selected;
	}

	public void setSelected(int selected) {
		this.selected = selected;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public String getPresent_name() {
		return present_name;
	}

	public void setPresent_name(String presentName) {
		present_name = presentName;
	}

	public String getPresent_img() {
		return present_img;
	}

	public void setPresent_img(String presentImg) {
		this.present_img = presentImg ;
	}

	public String getCreate_date() {
		return create_date;
	}

	public void setCreate_date(String createDate) {
		create_date = createDate;
	}

	public int getBuy_count() {
		return buy_count;
	}

	public void setBuy_count(int buyCount) {
		buy_count = buyCount;
	}

	@Override
	public String toString() {
		return "Present [buy_count=" + buy_count + ", create_date="
				+ create_date + ", id=" + id + ", present_img=" + present_img
				+ ", present_name=" + present_name + ", price=" + price + "]";
	}

}