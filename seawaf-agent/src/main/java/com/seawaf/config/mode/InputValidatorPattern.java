package com.seawaf.config.mode;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class InputValidatorPattern {
	public static int ACTION_WARN = 1;
	public static int ACTION_INTERCEPT = 2;
	public static int ACTION_REPLACE = 3;
	public static Map<String,Integer> nameValueMaps = new HashMap<String,Integer>();
	
	static{
		nameValueMaps.put("warn", ACTION_WARN);
		nameValueMaps.put("intercept",ACTION_INTERCEPT);
		nameValueMaps.put("replace", ACTION_REPLACE);
	}
	
	private String name;
	private String description;
	private String expression;
	private Pattern expressionPattern;
	private int action;
	private String exceptNames;
	private Pattern exceptNamesPattern;
	private String exceptUrls;
	private Pattern exceptUrlsPattern;
	
	
	public InputValidatorPattern(String name,String expression){
		this.name=name;
		this.expression=expression;
		this.action=ACTION_WARN;
		this.expressionPattern=Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
	}
	
	public InputValidatorPattern(String name,String expression,int action){
		this.name=name;
		this.expression=expression;
		this.action=action;
		this.expressionPattern=Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getExpression() {
		return expression;
	}

	public void setExpression(String expression) {
		this.expression = expression;
		this.expressionPattern=Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
	}

	public int getAction() {
		return action;
	}

	public void setAction(int action) {
		this.action = action;
	}

	public String getExceptNames() {
		return exceptNames;
	}

	public void setExceptNames(String exceptNames) {
		this.exceptNames = exceptNames;
		if(this.exceptNames!=null && this.exceptNames.length()>0){
			this.exceptNamesPattern=Pattern.compile(exceptNames,Pattern.CASE_INSENSITIVE);
		}
	}

	public String getExceptUrls() {
		return exceptUrls;
	}

	public void setExceptUrls(String exceptUrls) {
		this.exceptUrls = exceptUrls;
		if(this.exceptUrls!=null && this.exceptUrls.length()>0){
			this.exceptUrlsPattern=Pattern.compile(exceptUrls, Pattern.CASE_INSENSITIVE);
		}
	}
	
	public Pattern getExpressionPattern() {
		return expressionPattern;
	}

	public void setExpressionPattern(Pattern expressionPattern) {
		this.expressionPattern = expressionPattern;
	}
	
	public Pattern getExceptNamesPattern() {
		return exceptNamesPattern;
	}

	public void setExceptNamesPattern(Pattern exceptNamesPattern) {
		this.exceptNamesPattern = exceptNamesPattern;
	}

	public Pattern getExceptUrlsPattern() {
		return exceptUrlsPattern;
	}

	public void setExceptUrlsPattern(Pattern exceptUrlsPattern) {
		this.exceptUrlsPattern = exceptUrlsPattern;
	}

	@Override
	public String toString() {
		return "InputCheckPattern [name=" + name + ", description=" + description + ", expression=" + expression
				+ ", action=" + action + ", exceptNames=" + exceptNames + ", exceptUrls=" + exceptUrls + "]";
	}
}
