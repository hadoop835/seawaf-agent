package com.seawaf;

import java.io.Serializable;
import java.util.Date;

import javax.servlet.http.HttpSession;

public class SessionInfo implements Serializable {

	private static final long serialVersionUID = 7132448897345327418L;

	private String ip;
	private String userName;
	private String userId;
	private HttpSession session;
	private Date logined;
	private Date created;
	private Date lastAccessed;
	private int count;
	
	public SessionInfo(HttpSession s){
		this.session=s;
		this.created=new Date();
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public HttpSession getSession() {
		return session;
	}

	public void setSession(HttpSession session) {
		this.session = session;
	}

	public Date getLogined() {
		return logined;
	}

	public void setLogined(Date logined) {
		this.logined = logined;
	}
	
	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}
	

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}
	
	public Date getLastAccessed() {
		return new Date(this.session.getLastAccessedTime());
	}

	public void setLastAccessed(Date lastAccessed) {
		this.lastAccessed = lastAccessed;
	}

	@Override
	public String toString() {
		return "SessionInfo [ip=" + ip + ", userName=" + userName + ", userId=" + userId + ", session=" + session
				+ ", logined=" + logined + "]";
	}
}
