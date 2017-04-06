package com.vidmt.lmei.entity;

public class Model {
	public String name;
	public int iconRes;


	public Model(String name, int iconRes) {
		this.name = name;
		this.iconRes = iconRes;
	}
	public Model(String name) {
		this.name = name;
	}


	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getIconRes() {
		return iconRes;
	}

	public void setIconRes(int iconRes) {
		this.iconRes = iconRes;
	}
}
