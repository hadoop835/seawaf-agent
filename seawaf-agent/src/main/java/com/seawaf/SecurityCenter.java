package com.seawaf;

import java.io.IOException;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.seawaf.config.WafConfig;
import com.seawaf.db.Urls;
import com.seawaf.utils.CacheUtils;

import freemarker.cache.ClassTemplateLoader;
import freemarker.cache.TemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import net.sf.ehcache.Element;
import net.sf.ehcache.search.Result;

public class SecurityCenter extends HttpServlet {

	private static final long serialVersionUID = 8873865660157721620L;
	private static Map<String, ErrorCode> codesMap = new HashMap<String, ErrorCode>();
	private static Map<String, String> pagesMap = new HashMap<String, String>();
	private static Configuration configuration;
	private ResourceBundle bundle;

	static {
		ErrorCode code1 = new ErrorCode("1001","EXCEPTION DETECTED","code.error.500.title","code.error.500.content","code.error.500.des");
		ErrorCode code2 = new ErrorCode("1002","URL PROTECTED","code.error.url.protected.title","code.error.url.protected.content","code.error.url.protected.des");
		ErrorCode code3 = new ErrorCode("1003","SYSTEM OVERLOADED","code.error.overloaded.title","code.error.overloaded.content","code.error.overloaded.des");
		ErrorCode code4 = new ErrorCode("1004","IP DENIED","code.error.ip.denied.title","code.error.ip.denied.content","code.error.ip.denied.des");
		ErrorCode code5 = new ErrorCode("1005","REQUEST QUOTA","code.error.dangerous.detected.title","code.error.dangerous.detected.content","code.error.dangerous.detected.des");
		codesMap.put("1001", code1);
		codesMap.put("1002", code2);
		codesMap.put("1003", code3);
		codesMap.put("1004", code4);
		codesMap.put("1005", code5);
		pagesMap.put("s", "sessions.ftl");
		pagesMap.put("u", "users.ftl");
		pagesMap.put("i", "ips.ftl");
		pagesMap.put("l", "urls.ftl");
		pagesMap.put("e", "exceptions.ftl");
		pagesMap.put("ed", "ed.ftl");
		pagesMap.put("d", "denied-ips.ftl");
		configuration = new Configuration(Configuration.VERSION_2_3_23);
		TemplateLoader ctl = new ClassTemplateLoader(SecurityCenter.class,"/template");
		configuration.setTemplateLoader(ctl);
	}

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		this.doPost(request, response);
	}

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
		bundle=ResourceBundle.getBundle("message", request.getLocale());
		String code = request.getParameter("c");
		String action = request.getParameter("a");
		response.setHeader("Content-type", "text/html;charset=UTF-8"); 
		if(code!=null){
			ErrorCode error = codesMap.get(code);
			String content=request.getParameter("d");
			String times=request.getParameter("t");
			if(content!=null){
				error.setContent(content);
			}
			error.locale(bundle);
			if(times!=null){
				Object[] params = {times};
				
				error.setTips(MessageFormat.format(bundle.getString("code.error.unlock.tips"),params));
			}
			if(error!=null){
				response.getOutputStream().write(show(error).getBytes("utf-8"));
			}
		}else if(action!=null){
			String query=request.getParameter("q");
			String page = "index.ftl";
			if(pagesMap.get(action)!=null){
				page=pagesMap.get(action);
			}
			Template template = configuration.getTemplate(page,"utf-8");
			Map<String,Object> data = this.getData(bundle,action, query);
			data.put("path", request.getContextPath());
			try {
				template.process(data,response.getWriter());
			} catch (TemplateException e) {
				e.printStackTrace();
			}
		}
	}
	
	private Map<String,Object> getData(ResourceBundle bundle,String action,String query){
		if("s".equals(action)){
			return this.getSessionData(query);
		}else if("u".equals(action)){
			return this.getUserData(query);
		}else if("i".equals(action)){
			return this.getIpData(query);
		}else if("l".equals(action)){
			return this.getUrlData(query);
		}else if("e".equals(action)){
			return this.getExceptionData(query);
		}else if("d".equals(action)){
			return this.getDenyIpData(query);
		}else if("ed".equals(action)){
			return this.getException(query);
		}
		return this.getIndexData(bundle);
	}
	
	private Map<String,Object> getIndexData(ResourceBundle bundle){
		Map<String,Object> data = new HashMap<String,Object>();
		int ips = CacheUtils.getInstance().getCache("ip.counters").getSize();
		int urls = CacheUtils.getInstance().getCache("urls").getSize();
		int sessions = CacheUtils.getInstance().getCache("sessions").getSize();
		int users = CacheUtils.getInstance().getCache("users").getSize();
		int exceptions = CacheUtils.getInstance().getCache("exceptions").getSize();
		int deniedIps = CacheUtils.getInstance().getCache("ip5").getSize()
				+CacheUtils.getInstance().getCache("ip10").getSize()
				+CacheUtils.getInstance().getCache("ip30").getSize()
				+CacheUtils.getInstance().getCache("ip99").getSize();
		data.put("sessions", sessions);
		data.put("sessionsLabel", bundle.getString("label.sessions"));
		data.put("onlineUsers", users);
		data.put("onlineUsersLabel",  bundle.getString("label.users"));
		data.put("ips", ips);
		data.put("ipsLabel",  bundle.getString("label.ip.total"));
		data.put("urls", urls);
		data.put("urlsLabel",  bundle.getString("label.url.total"));
		data.put("exceptions", exceptions);
		data.put("exceptionsLabel",  bundle.getString("label.exceptions"));
		data.put("ipDenied", deniedIps);
		data.put("ipDeniedLabel",  bundle.getString("label.ip.denied"));
		return data;
	}
	
	private Map<String,Object> getExceptionData(String q){
		Map<String,Object> data = new HashMap<String,Object>();
		List<String> keys = CacheUtils.getInstance().keys("exceptions");
		List<ExceptionInfo> exceptions = new ArrayList<ExceptionInfo>();
		for(String key:keys){
			Object o = CacheUtils.getInstance().get("exceptions", key);
			if(o!=null && o instanceof ExceptionInfo){
				exceptions.add((ExceptionInfo)o);
			}
		}
		data.put("exceptions", exceptions);
		return data;
	}
	
	private Map<String,Object> getUrlData(String q){
		Map<String,Object> data = new HashMap<String,Object>();
		List<Result> results = CacheUtils.getInstance().keysOrderByIntValue("url.counters","*");
		List<String> xx = new ArrayList<String>();
		for(Result r:results){
			String key = r.getKey().toString();
			Integer uid=Integer.parseInt(key);
			String count = r.getValue().toString();
			String u = Urls.findUrlById(uid);
			xx.add(uid+"|"+u+"|"+count);
		}
		data.put("results", xx);
		return data;
	}
	
	private Map<String,Object> getDenyIpData(String q){
		Map<String,Object> data = new HashMap<String,Object>();
		data.put("ip5", this.getIpList("5"));
		data.put("ip10", this.getIpList("10"));
		data.put("ip30", this.getIpList("30"));
		data.put("ip99", this.getIpList("99"));
		return data;
	}
	
	private List<String> getIpList(String time){
		List<String> ip = new ArrayList<String>();
		String cacheName = "ip"+time;
		List<String> keys = CacheUtils.getInstance().keys(cacheName);
		if(keys!=null && !keys.isEmpty()){
			SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			for(String key:keys){
				Element element = CacheUtils.getInstance().getCache(cacheName).get(key);
				Date created = new Date(element.getCreationTime());
				String reason = element.getObjectValue().toString();
				ip.add(key+"|"+fmt.format(created)+"|"+time+"|"+reason);
			}
		}
		return ip;
	}
	
	private Map<String,Object> getIpData(String q){
		Map<String,Object> data = new HashMap<String,Object>();
		List<Result> results = CacheUtils.getInstance().keysOrderByIntValue("ip.counters","*");
		data.put("results", results);
		return data;
	}
	
	private Map<String,Object> getSessionData(String q){
		Map<String,Object> data = new HashMap<String,Object>();
		List<Result> results = CacheUtils.getInstance().keysOrderByIntValue("session.counters","*");
		List<SessionInfo> sessions = new ArrayList<SessionInfo>();
		for(Result r:results){
			String sessionId = r.getKey().toString();
			int count = Integer.parseInt(r.getValue().toString());
			SessionInfo info = (SessionInfo)CacheUtils.getInstance().get("sessions", sessionId);
			if(info!=null){
				info.setCount(count);
				sessions.add(info);
			}
		}
		data.put("sessions", sessions);
		return data;
	}
	
	private Map<String,Object> getUserData(String q){
		Map<String,Object> data = new HashMap<String,Object>();
		
		return data;
	}
	
	private Map<String,Object> getException(String q){
		Map<String,Object> data = new HashMap<String,Object>();
		data.put("app", WafConfig.getInstance().getApplication());
		if(q!=null && CacheUtils.getInstance().exists("exceptions", q)){
			ExceptionInfo info = (ExceptionInfo)CacheUtils.getInstance().get("exceptions", q);
			data.put("e", info);
		}
		return data;
	}
	
	public static String show(ErrorCode code){
		StringBuffer buffer = new StringBuffer();
		buffer.append("<!DOCTYPE html><html><head><meta charset=\"UTF-8\"><title>"+code.getName()+"</title>");
		buffer.append("<style type=\"text/css\">body{font-family:\"Microsoft YaHei\",Arial,Helvetica,sans-serif,\"SimSun\";margin: 0px;padding:0px;}h1.logo{color:#333;margin:0px;padding:5px 10px;font-size: 18px;border-bottom: 2px solid #ff3300;}h1.logo span{float:right;color:#666;}.info{padding-top:100px;height: 400px;}.info h2{text-align:center;color:#333;}.info h2.content{font-size:3em;}.info h2.tips{color: #999;}.foot{font-size: 10px;}.foot p{text-align:center;}</style>");
		buffer.append("<body><div class=\"head\"><h1 class=\"logo\">SEAWAF</h1></div><div class=\"info\"><h2>"+code.getTitle()+"</h2><h2 class=\"content\">"+code.getContent()+"</h2><h2 class=\"tips\">"+code.getTips()+"</h2></div><div class=\"foot\"><p>This application is protected by SEAWAF</p></div></body></html>");
		return buffer.toString();
	}
	
	public static Configuration getConfiguration(){
		return configuration;
	}
}

class ErrorCode {
	private String code;
	private String name;
	private String title;
	private String content;
	private String tips;
	private int times=5;

	public ErrorCode(String code,String name,String title,String content,String tips){
		this.code=code;
		this.name=name;
		this.title=title;
		this.content=content;
		this.tips=tips;
	}
	
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getTips() {
		return tips;
	}

	public void setTips(String tips) {
		this.tips = tips;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getTimes() {
		return times;
	}

	public void setTimes(int times) {
		this.times = times;
	}
	
	public void locale(ResourceBundle bundle){
		try{
			this.title=bundle.getString(this.title);
		}catch(Exception e){
			
		}
		try{
			this.content=bundle.getString(this.content);
		}catch(Exception e){
			
		}
		try{
			this.tips=bundle.getString(this.tips);
		}catch(Exception e){
			
		}
	}
	
}
