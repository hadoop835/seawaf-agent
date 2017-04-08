package com.seawaf.config.mode;

public class Quota {
	private int maxSessions;
	private int maxSessionsPerUser;
	private int maxOnlineUsers;
	private int maxExceptionsPerUrl;
	private int maxSameUrlOpenPerSession;
	private int timeUnit;
	private String outOfServiceRedirectUrl;

	public int getMaxSessions() {
		return maxSessions;
	}

	public void setMaxSessions(int maxSessions) {
		this.maxSessions = maxSessions;
	}

	public int getMaxSessionsPerUser() {
		return maxSessionsPerUser;
	}

	public void setMaxSessionsPerUser(int maxSessionsPerUser) {
		this.maxSessionsPerUser = maxSessionsPerUser;
	}

	public int getMaxOnlineUsers() {
		return maxOnlineUsers;
	}

	public void setMaxOnlineUsers(int maxOnlineUsers) {
		this.maxOnlineUsers = maxOnlineUsers;
	}

	public int getMaxSameUrlOpenPerSession() {
		return maxSameUrlOpenPerSession;
	}

	public void setMaxSameUrlOpenPerSession(int maxSameUrlOpenPerSession) {
		this.maxSameUrlOpenPerSession = maxSameUrlOpenPerSession;
	}

	public String getOutOfServiceRedirectUrl() {
		return outOfServiceRedirectUrl;
	}

	public void setOutOfServiceRedirectUrl(String outOfServiceRedirectUrl) {
		this.outOfServiceRedirectUrl = outOfServiceRedirectUrl;
	}
	
	public int getMaxExceptionsPerUrl() {
		return maxExceptionsPerUrl;
	}

	public void setMaxExceptionsPerUrl(int maxExceptionsPerUrl) {
		this.maxExceptionsPerUrl = maxExceptionsPerUrl;
	}
	
	public int getTimeUnit() {
		return timeUnit;
	}

	public void setTimeUnit(int timeUnit) {
		this.timeUnit = timeUnit;
	}

	@Override
	public String toString() {
		return "Quota [maxSessions=" + maxSessions + ", maxSessionsPerUser=" + maxSessionsPerUser + ", maxOnlineUsers="
				+ maxOnlineUsers + ", maxExceptionsPerUrl=" + maxExceptionsPerUrl + ", maxSameUrlOpenPerSession=" + maxSameUrlOpenPerSession
				+ ", outOfServiceRedirectUrl=" + outOfServiceRedirectUrl + "]";
	}
	
}
