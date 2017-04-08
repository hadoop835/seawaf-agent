package com.seawaf;

import java.io.Serializable;
import java.util.Date;

public class InputAttackInfo implements Serializable {

	private static final long serialVersionUID = -236617418631681435L;
	
	private String type;
	private String url;
	private String name;
	private String value;
	private String ip;
	private String session;
	private Date created;
	
	public InputAttackInfo(String type,String url){
		this.type=type;
		this.url=url;
		this.created=new Date();
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getSession() {
		return session;
	}

	public void setSession(String session) {
		this.session = session;
	}

	@Override
	public String toString() {
		return "InputAttackInfo [type=" + type + ", url=" + url + ", name=" + name + ", value=" + value + ", ip=" + ip
				+ ", session=" + session + ", created=" + created + "]";
	}
	
}
