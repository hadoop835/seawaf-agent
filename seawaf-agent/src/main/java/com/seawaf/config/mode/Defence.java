package com.seawaf.config.mode;


public class Defence {
	private boolean csrf;
	private boolean cc;
	private InputValidator inputValidator;
	
	public Defence(){
		
	}

	public boolean isCsrf() {
		return csrf;
	}

	public void setCsrf(boolean csrf) {
		this.csrf = csrf;
	}

	public boolean isCc() {
		return cc;
	}

	public void setCc(boolean cc) {
		this.cc = cc;
	}

	public InputValidator getInputValidator() {
		return inputValidator;
	}

	public void setInputValidator(InputValidator inputValidator) {
		this.inputValidator = inputValidator;
	}

	@Override
	public String toString() {
		return "Defence [csrf=" + csrf + ", cc=" + cc + ", inputValidator=" + inputValidator + "]";
	}
}
