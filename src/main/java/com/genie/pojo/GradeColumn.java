package com.genie.pojo;

import java.io.Serializable;

public class GradeColumn implements Serializable{
	private static final long serialVersionUID = -3037339261789735505L;

	private String label;
	private int order;
	
	public GradeColumn() {
	}
	
	public GradeColumn(String label, int order) {
		this.label = label;
		this.order = order;
	}
	
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public int getOrder() {
		return order;
	}
	public void setOrder(int order) {
		this.order = order;
	}
}
