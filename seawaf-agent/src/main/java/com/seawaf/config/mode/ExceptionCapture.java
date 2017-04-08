package com.seawaf.config.mode;

public class ExceptionCapture {

	private boolean capture;
	private String mailto;

	public boolean isCapture() {
		return capture;
	}

	public void setCapture(boolean capture) {
		this.capture = capture;
	}

	public String getMailto() {
		return mailto;
	}

	public void setMailto(String mailto) {
		this.mailto = mailto;
	}

	@Override
	public String toString() {
		return "ExceptionCapture [capture=" + capture + ", mailto=" + mailto + "]";
	}
	
	
}
