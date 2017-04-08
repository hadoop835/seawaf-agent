package com.seawaf.config;

public class Application {
	private String id;
	private String name;
	private String activeMode;
	private String ip;
	private int port;
	private User administrator;
	private User auditor;
	private String sessionUserAttributeName;
	private String sessionUserNamePath;
	private String sessionUserIdPath;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getActiveMode() {
		return activeMode;
	}

	public void setActiveMode(String activeMode) {
		this.activeMode = activeMode;
	}

	public User getAdministrator() {
		return administrator;
	}

	public void setAdministrator(User administrator) {
		this.administrator = administrator;
	}

	public User getAuditor() {
		return auditor;
	}

	public void setAuditor(User auditor) {
		this.auditor = auditor;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}
	

	public String getSessionUserAttributeName() {
		return sessionUserAttributeName;
	}

	public void setSessionUserAttributeName(String sessionUserAttributeName) {
		this.sessionUserAttributeName = sessionUserAttributeName;
	}

	public String getSessionUserNamePath() {
		return sessionUserNamePath;
	}

	public void setSessionUserNamePath(String sessionUserNamePath) {
		this.sessionUserNamePath = sessionUserNamePath;
	}

	public String getSessionUserIdPath() {
		return sessionUserIdPath;
	}

	public void setSessionUserIdPath(String sessionUserIdPath) {
		this.sessionUserIdPath = sessionUserIdPath;
	}

	@Override
	public String toString() {
		return "Application [id=" + id + ", name=" + name + ", activeMode=" + activeMode + ", ip=" + ip + ", port="
				+ port + ", administrator=" + administrator + ", auditor=" + auditor + ", sessionUserAttributeName="
				+ sessionUserAttributeName + ", sessionUserNamePath=" + sessionUserNamePath + ", sessionUserIdPath="
				+ sessionUserIdPath + "]";
	}
	
}
