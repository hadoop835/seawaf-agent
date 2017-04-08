package com.seawaf.listeners;


import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import com.seawaf.SessionInfo;
import com.seawaf.utils.CacheUtils;

public class WafSessionListener implements HttpSessionListener {

	public void sessionCreated(HttpSessionEvent event) {
		SessionInfo sessionInfo = new SessionInfo(event.getSession());
		CacheUtils.getInstance().put("sessions", event.getSession().getId(), sessionInfo);
	}

	public void sessionDestroyed(HttpSessionEvent event) {
		Object o = CacheUtils.getInstance().get("sessions", event.getSession().getId());
		if(o!=null && o instanceof SessionInfo){
			SessionInfo info = (SessionInfo)o;
			if(info.getUserId()!=null){
				CacheUtils.getInstance().remove("users", info.getUserId());
			}
		}
		CacheUtils.getInstance().remove("sessions", event.getSession().getId());
	}

}
