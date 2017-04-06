package com.vidmt.lmei.entity;



/*
 * 系统设置实体
 */

public class SystemConfig {
    private Integer id;

    private String system_name;//系统参数名

    private Integer system_desc;//系统参数值

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

	public String getSystem_name() {
		return system_name;
	}

	public void setSystem_name(String system_name) {
		this.system_name = system_name;
	}

	public Integer getSystem_desc() {
		return system_desc;
	}

	public void setSystem_desc(Integer system_desc) {
		this.system_desc = system_desc;
	}

	@Override
	public String toString() {
		return "System [id=" + id + ", system_desc=" + system_desc
				+ ", system_name=" + system_name + "]";
	}


}