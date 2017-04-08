package com.seawaf.config.mode;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class InputValidator {
	private String exceptNames;
	private String exceptUrls;
	private List<InputValidatorPattern> patterns;
	private Pattern exceptNamesPattern;
	private Pattern exceptUrlsPattern;

	public InputValidator(){
		this.patterns=new ArrayList<InputValidatorPattern>();
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
			this.exceptUrlsPattern=Pattern.compile(exceptUrls,Pattern.CASE_INSENSITIVE);
		}
	}

	public List<InputValidatorPattern> getPatterns() {
		return patterns;
	}

	public void setPatterns(List<InputValidatorPattern> patterns) {
		this.patterns = patterns;
	}
	
	public void addPattern(InputValidatorPattern p){
		this.patterns.add(p);
	}
	
	public void removePattern(InputValidatorPattern p){
		this.patterns.remove(p);
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
		return "InputValidator [exceptNames=" + exceptNames + ", exceptUrls=" + exceptUrls + ", patterns=" + patterns
				+ "]";
	}
	
}
