package com.seawaf;

import java.io.Serializable;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import com.seawaf.config.WafConfig;
import com.seawaf.utils.Utils;

public class ExceptionInfo implements Serializable{
	private static final long serialVersionUID = -2630364650280389011L;
	private String id;
	private String ip;
	private String session;
	private String userName;
	private Date created;
	private String url;
	private String userAgent;
	private String message;
	private String stacktrace;
	
	public ExceptionInfo(HttpServletRequest request,Exception e){
		this.id=WafConfig.getInstance().getApplication().getId()+"-"+new Date().getTime();
		this.ip=request.getRemoteAddr();
		this.session=request.getSession().getId();
		this.created=new Date();
		this.url=Utils.mergeUrl(request.getRequestURL().toString());
		this.userAgent=request.getHeader("user-agent");
		this.message=e.getMessage();
		this.stacktrace=e.toString();
		this.userName=Utils.getUserInfoFromCache(request.getSession().getId());
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

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUserAgent() {
		return userAgent;
	}

	public void setUserAgent(String userAgent) {
		this.userAgent = userAgent;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getStacktrace() {
		return stacktrace;
	}

	public void setStacktrace(String stacktrace) {
		this.stacktrace = stacktrace;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "ExceptionInfo [id=" + id + ", ip=" + ip + ", session=" + session + ", userName=" + userName
				+ ", created=" + created + ", url=" + url + ", userAgent=" + userAgent + ", message=" + message
				+ ", stacktrace=" + stacktrace + "]";
	}
}
