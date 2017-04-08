package com.seawaf.filters;

import java.io.IOException;
import java.util.logging.Logger;
import java.util.regex.Pattern;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.seawaf.ExceptionInfo;
import com.seawaf.config.WafConfig;
import com.seawaf.config.mode.Quota;
import com.seawaf.utils.CacheUtils;
import com.seawaf.utils.ConfigReader;
import com.seawaf.utils.Const;
import com.seawaf.utils.Utils;

public class WafFilter implements Filter {
	public static final int CODE_EXCEPTION_DETECTED = 1001;
	public static final int CODE_URL_PROTECTED = 1002;
	public static final int CODE_SYSTEM_OVERLOADED = 1003;
	public static final int CODE_IP_DENIED = 1004;
	public static final int CODE_SPEED_LIMIT = 1005;
	private static Logger logger = Logger.getLogger(WafFilter.class.getCanonicalName());
	private String excluedUrls;
	private Pattern excluedUrlsPattern;
	private String redirectUrl;
	
	public void destroy() {
		CacheUtils.getInstance().shutdown();
	}

	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) servletRequest;
		HttpServletResponse response = (HttpServletResponse) servletResponse;
		WafRequest wafRequest = new WafRequest(request);
		String seawaf=request.getContextPath()+"/seawaf";
		if(seawaf.equals(request.getRequestURI())){
			chain.doFilter(servletRequest, servletResponse);
		}else{
			if(this.excluedUrlsPattern!=null && this.excluedUrlsPattern.matcher(request.getRequestURI()).find()){
				chain.doFilter(request, response);
			}else{
				String mergedUrl = Utils.mergeUrl(request.getRequestURL().toString());
				String ip=request.getRemoteAddr();
				this.updateCounters(request, response,mergedUrl);
				if(WafConfig.getInstance()!=null && WafConfig.getInstance().isPowerOn()){
					request.setAttribute("_waf_config_", WafConfig.getInstance());
					this.defenceClickJack(request,response);
					if(ipCheck(request,response,ip)){//is IP in deny list?
						if(quotaCheck(request, response,mergedUrl)){// is quota raise to max?
							if(WafConfig.getInstance().getActivatedMode().getExceptionCapture().isCapture()){
								this.handleExceptions(wafRequest, response, chain);
							}else{
								chain.doFilter(wafRequest, response);
							}
						}else{
							response.sendRedirect(this.redirectUrl);
						}
					}else{
						response.sendRedirect(this.redirectUrl);
					}
				}else{
					chain.doFilter(request, response);
				}
			}
		}
	}
	
	private void updateCounters(HttpServletRequest request,HttpServletResponse response,String mergedUrl){
		CacheUtils.getInstance().enc("global.counters", "global.hitcount");
		CacheUtils.getInstance().autoKeyId("urls", mergedUrl);
		String sid=request.getSession().getId();
		int uid=CacheUtils.getInstance().getIntValue("urls", mergedUrl);
		CacheUtils.getInstance().enc("ip.counters", request.getRemoteAddr());
		CacheUtils.getInstance().enc("url.counters", ""+uid);
		CacheUtils.getInstance().enc("session.counters", sid);
		CacheUtils.getInstance().enc("session.url.counters", sid+":"+uid);
	}
	
	private void defenceClickJack(HttpServletRequest request,HttpServletResponse response){
		if(!response.containsHeader("X-FRAME-OPTIONS")){
			response.addHeader("X-FRAME-OPTIONS", "SAMEORIGIN");
		}
	}

	private boolean ipCheck(HttpServletRequest request,HttpServletResponse response,String ip) throws IOException{
		int times = CacheUtils.getInstance().getIntValue("ip.logs", ip);
		if(times>-1){
			String reason = CacheUtils.getInstance().getStringValue("ip"+times, ip);
			if(reason!=null){
//				response.sendError(403, "[SEAWAF]Your IP is prohibited:"+reason+",you can try again in "+times+" min later");
				this.redirectUrl=request.getContextPath()+"/seawaf?c="+WafFilter.CODE_IP_DENIED+"&t="+times;
				return false;
			}
		}
		return true;
	}
	
	private boolean quotaCheck(HttpServletRequest request,HttpServletResponse response,String mergedUrl) throws IOException{
		Quota quota = WafConfig.getInstance().getActivatedMode().getQuota();
		String uid = CacheUtils.getInstance().getStringValue("urls", mergedUrl);
		String sid=request.getSession().getId();
		String ip=request.getRemoteAddr();
		//max exceptions check
		int exceptionCounter = CacheUtils.getInstance().getIntValue("exception.counters", uid);
		int maxExceptionPermit=quota.getMaxExceptionsPerUrl();
		if(exceptionCounter>0 && exceptionCounter>maxExceptionPermit){
//			response.sendError(403, "[SEAWAF]:Too many exceptions detected on url");
			this.redirectUrl=request.getContextPath()+"/seawaf?c="+WafFilter.CODE_URL_PROTECTED;
			return false;
		}
		//max sessions and online users check
		int sessionsCount = CacheUtils.getInstance().getCache("sessions").getSize();
		int onlineUsersCount = CacheUtils.getInstance().getCache("users").getSize();
		if(sessionsCount>=quota.getMaxSessions()||onlineUsersCount>=quota.getMaxOnlineUsers()){
//			response.sendError(403, "[SEAWAF]:Too many sessions or online users detected,MaxSessions:"+
//					quota.getMaxSessions()+",MaxUsers:"+quota.getMaxOnlineUsers());
			this.redirectUrl=request.getContextPath()+"/seawaf?c="+WafFilter.CODE_SYSTEM_OVERLOADED;
			return false;
		}
		//max single URL per session check
		int sessionUrlCount = CacheUtils.getInstance().getIntValue("session.url.counters", sid+":"+uid);
		if(sessionUrlCount>-1 && sessionUrlCount>quota.getMaxSameUrlOpenPerSession()){
			Utils.denyIp(ip,"Too many single url opened");
//			response.sendError(403, "[SEAWAF]:Too many single url opened in one session");
			this.redirectUrl=request.getContextPath()+"/seawaf?c="+WafFilter.CODE_SPEED_LIMIT;
			return false;
		}
		return true;
	}
	
	private void handleExceptions(HttpServletRequest request,HttpServletResponse response,FilterChain chain) throws IOException, ServletException{
		try{
			chain.doFilter(request, response);
		}catch(IOException e){
			this.logException(request,response, e);
		}catch(Exception e){
			this.logException(request,response, e);
		}
	}
	
	private void logException(HttpServletRequest request,HttpServletResponse response,Exception e) throws IOException{
		ExceptionInfo info = new ExceptionInfo(request, e);
		int uid = CacheUtils.getInstance().getIntValue("urls", info.getUrl());
		CacheUtils.getInstance().enc("total.counters", "global.exceptions");
		CacheUtils.getInstance().enc("exception.counters", uid+"");
		CacheUtils.getInstance().put("exceptions", info.getId(), info);
		this.redirectUrl=request.getContextPath()+"/seawaf?c="+WafFilter.CODE_EXCEPTION_DETECTED+"&d="+info.getId();
		response.sendRedirect(redirectUrl);
	}

	public void init(FilterConfig config) throws ServletException {
		this.excluedUrls = config.getInitParameter("excluded");
		if(this.excluedUrls!=null && this.excluedUrls.length()>0){
			this.excluedUrlsPattern=Pattern.compile(this.excluedUrls, Pattern.CASE_INSENSITIVE);
		}
		CacheUtils.getInstance();
		Const.root=config.getServletContext().getRealPath("/");
		try {
			WafConfig.setInstance(new ConfigReader().load());
		} catch (Exception e) {
			e.printStackTrace();
		}
		int timeUnit=WafConfig.getInstance().getActivatedMode().getQuota().getTimeUnit();
		CacheUtils.getInstance().getCache("session.url.counters").getCacheConfiguration().setTimeToLiveSeconds(timeUnit);
	}
}
