package com.seawaf.listeners;

import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionBindingEvent;

import com.seawaf.SessionInfo;
import com.seawaf.config.Application;
import com.seawaf.config.WafConfig;
import com.seawaf.utils.CacheUtils;

import ognl.Ognl;
import ognl.OgnlException;

public class WafSessionAttrListener implements HttpSessionAttributeListener {

	private static Logger logger = Logger.getLogger(WafSessionAttrListener.class.getCanonicalName());
	
	public void attributeAdded(HttpSessionBindingEvent event) {
		Application app = WafConfig.getInstance().getApplication();
		if(event.getName().equals(app.getSessionUserAttributeName())){
			try {
				Object tid = Ognl.getValue(app.getSessionUserIdPath(), event.getValue());
				Object tname = Ognl.getValue(app.getSessionUserNamePath(), event.getValue());
				if(tid!=null && tname!=null){
					CacheUtils.getInstance().put("users", tid.toString(), event.getSession().getId());
					Object o = CacheUtils.getInstance().get("sessions", event.getSession().getId());
					if(o!=null && o instanceof SessionInfo){
						SessionInfo info = (SessionInfo)o;
						info.setUserId(tid.toString());
						info.setUserName(tname.toString());
						info.setLogined(new Date());
						CacheUtils.getInstance().put("sessions", event.getSession().getId(), info);
						CacheUtils.getInstance().put("user.sessions", tid.toString()+":"+event.getSession().getId(), 1);
					}
				}
			} catch (OgnlException e) {
				logger.log(Level.SEVERE, e.getMessage(), e);
			}
		}
	}

	public void attributeRemoved(HttpSessionBindingEvent event) {
		Application app = WafConfig.getInstance().getApplication();
		if(event.getName().equals(app.getSessionUserAttributeName())){
			try {
				Object tid = Ognl.getValue(app.getSessionUserIdPath(), event.getValue());
				Object tname = Ognl.getValue(app.getSessionUserNamePath(), event.getValue());
				if(tid!=null && tname!=null){
					CacheUtils.getInstance().remove("users", tname.toString());
					CacheUtils.getInstance().remove("sessions", event.getSession().getId());
				}
			} catch (OgnlException e) {
				logger.log(Level.SEVERE, e.getMessage(), e);
			}
		}
	}

	public void attributeReplaced(HttpSessionBindingEvent event) {
		
	}

}
