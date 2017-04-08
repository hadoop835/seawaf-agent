package com.seawaf.filters;

import java.util.List;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import com.seawaf.InputAttackInfo;
import com.seawaf.config.WafConfig;
import com.seawaf.config.mode.InputValidator;
import com.seawaf.config.mode.InputValidatorPattern;
import com.seawaf.utils.CacheUtils;
import com.seawaf.utils.Utils;

public class WafRequest extends HttpServletRequestWrapper {
	private static Logger logger = Logger.getLogger(WafRequest.class.getCanonicalName());
	
	public WafRequest(HttpServletRequest request) {
		super(request);
	}

	public String getParameter(String name){
		InputValidator validator = WafConfig.getInstance().getActivatedMode().getDefence().getInputValidator();
		String url=super.getRequestURL().toString();
		String value = super.getParameter(name);
		String ip=super.getRemoteAddr();
		Pattern globalNamesExcept=validator.getExceptNamesPattern();
		if(value==null||globalNamesExcept!=null && globalNamesExcept.matcher(name).find()){
			//global name except
			return value;
		}else{
			List<InputValidatorPattern> patterns = validator.getPatterns();
			for(InputValidatorPattern pattern:patterns){
				Pattern p1=pattern.getExceptNamesPattern();
				Pattern p2=pattern.getExceptUrlsPattern();
				if(p1!=null && p1.matcher(name).find()||p2!=null && p2.matcher(url).find()){
					//pattern name or URL except
					logger.info("input check ignored because name or url is except");
					continue;
				}
				Matcher matcher = pattern.getExpressionPattern().matcher(value);
				if(matcher.find()){
					logger.warning("ATTACK DETECTED IN NAME["+name+"],FOR INPUT"+value);
					InputAttackInfo attack = new InputAttackInfo(pattern.getName(), url);
					attack.setName(name);
					attack.setValue(value);
					CacheUtils.getInstance().put("attacks",ip+":"+super.getSession().getId(), attack);
					CacheUtils.getInstance().enc("attack.counters", ip);
					int times=CacheUtils.getInstance().getIntValue("attack.counters", ip);
					if(times>100){
						Utils.denyIp(ip, "Too many attack detected");
						logger.warning("ip["+ip+"] added to denied list");
					}
					if(pattern.getAction()==InputValidatorPattern.ACTION_REPLACE){
						return matcher.replaceAll("");
					}
				}else{
					continue;
				}
			}
		}
		return value;
	}

}
