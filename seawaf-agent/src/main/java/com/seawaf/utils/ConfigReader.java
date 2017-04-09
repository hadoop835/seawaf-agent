package com.seawaf.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.seawaf.config.Application;
import com.seawaf.config.Mode;
import com.seawaf.config.User;
import com.seawaf.config.WafConfig;
import com.seawaf.config.mode.Defence;
import com.seawaf.config.mode.ExceptionCapture;
import com.seawaf.config.mode.InputValidator;
import com.seawaf.config.mode.InputValidatorPattern;
import com.seawaf.config.mode.Quota;

/**
 * 
 * @author zhugl
 *
 */
public class ConfigReader {
	private String path;
	private Document document;
	private static Logger logger = Logger.getLogger("WafConfigReader");
	
	public ConfigReader(){
		if(Const.root!=null){
			//user defined configuration file detect 
			File webapp = new File(Const.root+"/WEB-INF","waf.xml");
			if(webapp.exists()){
				this.path=Const.root+"/WEB-INF";
				logger.info("user defined waf config file found,waf.xml in /WEB-INF will be used");
			}else{
				logger.log(Level.WARNING, "waf.xml not found in /WEB-INF,default config file will be used");
			}
		}
	}
	
	private void init() throws DocumentException{
		File configFile = null;
		SAXReader reader = new SAXReader(); 
		if(this.path!=null){
			configFile = new File(this.path,"waf.xml");
			if(configFile!=null && configFile.exists()){
				this.document = reader.read(configFile);
			}
		}else{
			this.document=reader.read(this.getClass().getResourceAsStream("/waf.xml"));
		}
	}
	
	public WafConfig load() throws Exception{
		WafConfig config = new WafConfig();
		this.init();
		config.setApplication(this.loadAppInfo());
		config.setModes(this.loadModes());
		for(int i=0;i<config.getModes().size();i++){
			Mode m = config.getModes().get(i);
			if(m.getId().equals(config.getApplication().getActiveMode())){
				config.setActivatedMode(m);
				break;
			}
		}
		if(config.getActivatedMode()==null){
			throw new Exception("CAN NOT FIND MODE BY ID ["+config.getActivatedMode()+"] IN CONFIG FILE");
		}
		return config;
	}
	
	public Application loadAppInfo(){
		Application sysInfo = null;
		if(this.document!=null){
			Element system = this.document.getRootElement().element("application");
			sysInfo = system==null?null:new Application();
			sysInfo.setId(system.elementText("id"));
			sysInfo.setName(system.elementText("name"));
			sysInfo.setIp(system.elementText("ip"));
			sysInfo.setPort(Integer.parseInt(system.elementText("port")));
			sysInfo.setActiveMode(system.elementText("active-mode"));
			sysInfo.setSessionUserAttributeName(system.elementText("session-user-attribute-name"));
			sysInfo.setSessionUserIdPath(system.elementText("session-user-id-path"));
			sysInfo.setSessionUserNamePath(system.elementText("session-user-name-path"));
			String uidAdmin = system.element("administrator").attributeValue("id");
			String unameAdmin = system.element("administrator").attributeValue("name");
			String uemailAdmin = system.element("administrator").attributeValue("email");
			String uidAudit = system.element("auditor").attributeValue("id");
			String unameAudit = system.element("auditor").attributeValue("name");
			String uemailAudit = system.element("auditor").attributeValue("email");
			sysInfo.setAdministrator(new User(uidAdmin,unameAdmin,uemailAdmin,User.ROLE_ADMIN));
			sysInfo.setAuditor(new User(uidAudit,unameAudit,uemailAudit,User.ROLE_AUDIT));
		}
		return sysInfo;
	}
	
	public List<Mode> loadModes(){
		List<Mode> modes = null;
		if(this.document!=null){
			@SuppressWarnings("unchecked")
			List<Element> elements = this.document.getRootElement().elements("mode");
			modes=new ArrayList<Mode>();
			for(int i=0;i<elements.size();i++){
				Element element = elements.get(i);
				Mode mode = this.loadMode(element);
				if(mode!=null){
					modes.add(mode);
				}
			}
		}
		return modes;
	}
	
	private Mode loadMode(Element element){
		Mode mode = new Mode();
		mode.setId(element.attributeValue("id"));
		ExceptionCapture exceptionCapture = this.loadExceptionCapture(element.element("exceptions"));
		mode.setExceptionCapture(exceptionCapture);
		Quota quota = this.loadQuota(element.element("quotas"));
		mode.setQuota(quota);
		Defence defence = this.loadDefence(element.element("defences"));
		mode.setDefence(defence);
		return mode;
	}
	
	private ExceptionCapture loadExceptionCapture(Element element){
		ExceptionCapture exceptionCapture = new ExceptionCapture();
		exceptionCapture.setCapture(Boolean.parseBoolean(element.elementText("capture")));
		exceptionCapture.setMailto(element.elementText("mailto"));
		return exceptionCapture;
	}
	
	private Quota loadQuota(Element element){
		Quota quota = new Quota();
		quota.setMaxSessions(Integer.parseInt(element.elementText("max-sessions")));
		quota.setMaxSessionsPerUser(Integer.parseInt(element.elementText("max-sessions-per-user")));
		quota.setMaxOnlineUsers(Integer.parseInt(element.elementText("max-online-users")));
		String expression=element.elementText("max-same-url-open-per-session");
		Pattern pattern = Pattern.compile("(\\d{1,4})/(\\d{1,4})([s|m|h])");
		Matcher m = pattern.matcher(expression);
		if(m.matches()){
			int maxSameUrlOpenPerSession=Integer.parseInt(m.group(1));
			int timeUint=Integer.parseInt(m.group(2));
			quota.setMaxSameUrlOpenPerSession(maxSameUrlOpenPerSession);
			String x=m.group(3);
			if("m".equals(x)){
				quota.setTimeUnit(timeUint*60);
			}else if("h".equals(x)){
				quota.setTimeUnit(timeUint*3600);
			}else{
				quota.setTimeUnit(timeUint);
			}
		}
		quota.setMaxExceptionsPerUrl(Integer.parseInt(element.elementText("max-exceptions-per-url")));
		quota.setOutOfServiceRedirectUrl(element.elementText("out-of-service-redirect-url"));
		return quota;
	}
	
	private Defence loadDefence(Element element){
		Defence defence = new Defence();
		defence.setCsrf(Boolean.parseBoolean(element.elementText("csrf")));
		defence.setCc(Boolean.parseBoolean(element.elementText("cc")));
		defence.setInputValidator(this.loadInputValidator(element.element("input-validator")));
		return defence;
	}
	
	private InputValidator loadInputValidator(Element element){
		InputValidator validator = new InputValidator();
		validator.setExceptNames(element.elementText("except-names"));
		validator.setExceptUrls(element.elementText("except-urls"));
		@SuppressWarnings("unchecked")
		List<Element> list = element.elements("pattern");
		for(int index=0;index<list.size();index++){
			Element e = list.get(index);
			InputValidatorPattern pattern = this.loadInputCheckPattern(e);
			validator.addPattern(pattern);
		}
		return validator;
	}
	
	private InputValidatorPattern loadInputCheckPattern(Element element){
		InputValidatorPattern pattern = null;
		String name = element.elementText("name");
		String expression = element.elementTextTrim("expression");
		String action = element.elementText("action");
		pattern = new InputValidatorPattern(name, expression,InputValidatorPattern.nameValueMaps.get(action));
		pattern.setExceptUrls(element.elementText("except-urls"));
		pattern.setExceptNames(element.elementText("except-names"));
		pattern.setDescription(element.elementText("description"));
		return pattern;
	}
}
