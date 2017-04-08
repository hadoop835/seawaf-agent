package com.seawaf.config;

import java.util.List;

public class WafConfig {
	private static WafConfig wafConfig;
	
	public WafConfig(){
		this.powerOn=true;
	}
	
	private Application application;
	private List<Mode> modes;
	private Mode activatedMode;
	private boolean powerOn;

	public Application getApplication() {
		return application;
	}

	public void setApplication(Application application) {
		this.application = application;
	}

	public List<Mode> getModes() {
		return modes;
	}

	public void setModes(List<Mode> modes) {
		this.modes = modes;
	}

	public void addMode(Mode mode) {
		this.modes.add(mode);
	}

	public void removeMode(Mode mode) {
		this.modes.remove(mode);
	}

	public boolean isPowerOn() {
		return powerOn;
	}

	public void setPowerOn(boolean powerOn) {
		this.powerOn = powerOn;
	}

	public Mode getActivatedMode() {
		return activatedMode;
	}

	public void setActivatedMode(Mode activatedMode) {
		this.activatedMode = activatedMode;
	}
	
	public static WafConfig getInstance(){
		if(wafConfig!=null){
			return wafConfig;
		}else{
			return new WafConfig();
		}
	}
	
	public static void setInstance(WafConfig config){
		WafConfig.wafConfig=config;
	}

	@Override
	public String toString() {
		return "WafConfig [application=" + application + ", modes=" + modes + "]";
	}
	
	
}
