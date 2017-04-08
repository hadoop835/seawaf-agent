package com.seawaf.config;

import com.seawaf.config.mode.Defence;
import com.seawaf.config.mode.ExceptionCapture;
import com.seawaf.config.mode.Quota;

public class Mode {

	private String id;
	private ExceptionCapture exceptionCapture;
	private Quota quota;
	private Defence defence;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public ExceptionCapture getExceptionCapture() {
		return exceptionCapture;
	}

	public void setExceptionCapture(ExceptionCapture exceptionCapture) {
		this.exceptionCapture = exceptionCapture;
	}

	public Quota getQuota() {
		return quota;
	}

	public void setQuota(Quota quota) {
		this.quota = quota;
	}

	public Defence getDefence() {
		return defence;
	}

	public void setDefence(Defence defence) {
		this.defence = defence;
	}

	@Override
	public String toString() {
		return "Mode [id=" + id + ", exceptionCapture=" + exceptionCapture + ", quota=" + quota + ", defence=" + defence
				+ "]";
	}
	
	
}
