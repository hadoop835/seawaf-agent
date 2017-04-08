package com.seawaf.utils;

import javax.servlet.http.HttpSession;

import com.seawaf.SessionInfo;
import com.seawaf.config.WafConfig;

import ognl.Ognl;

public class Utils {
	
	public static String mergeUrl(String url){
		return url.replaceAll("=\\d+", "=#{number}");
	}
	
	public static String getUserNameFromSession(HttpSession session){
		try{
			String nameAttr = WafConfig.getInstance().getApplication().getSessionUserAttributeName();
			String namePath = WafConfig.getInstance().getApplication().getSessionUserNamePath();
			String idPath=WafConfig.getInstance().getApplication().getSessionUserIdPath();
			Object name = Ognl.getValue(namePath, session.getAttribute(nameAttr));
			Object id=Ognl.getValue(idPath, session);
			if(name!=null && id!=null){
				return id.toString()+"("+name.toString()+")";
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	public static String getUserInfoFromCache(String sessionId){
		Object sessionInfo = CacheUtils.getInstance().get("sessions", sessionId);
		if(sessionInfo!=null && sessionInfo instanceof SessionInfo){
			SessionInfo info = (SessionInfo)sessionInfo;
			return info.getUserId()+"("+info.getUserName()+")";
		}
		return null;
	}
	
	public static void denyIp(String ip,String reason){
		int times = CacheUtils.getInstance().getIntValue("ip.logs", ip);
		switch(times){
			case -1:
				CacheUtils.getInstance().put("ip5", ip, reason);
				CacheUtils.getInstance().put("ip.logs", ip, 5);
				times=5;
				break;
			case 5:
				CacheUtils.getInstance().put("ip10", ip, reason);
				CacheUtils.getInstance().put("ip.logs", ip, 10);
				times=10;
				break;
			case 10:
				CacheUtils.getInstance().put("ip30", ip, reason);
				CacheUtils.getInstance().put("ip.logs", ip, 30);
				times=30;
				break;
			case 30:
				CacheUtils.getInstance().put("ip99", ip, reason);
				CacheUtils.getInstance().put("ip.logs", ip, 99);
				times=99;
				break;
			default:
				CacheUtils.getInstance().put("ip5", ip, reason);
				CacheUtils.getInstance().put("ip.logs", ip, 5);
				times=5;
		}
	}
}
