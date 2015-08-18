package com.genie.pojo;

import java.io.Serializable;

public class GradeWrapper implements Serializable {
	private static final long serialVersionUID = 1196597178432068703L;

	private String label;
	private String grade;
	
	public GradeWrapper() {
	}
	
	public GradeWrapper(String grade, String label) {
		this.grade = grade;
		this.label = label;
	}
	
	public String getLabel() {
		return label;
	}
	
	public void setLabel(String label) {
		this.label = label;
	}

	public String getGrade() {
		return grade;
	}

	public void setGrade(String grade) {
		this.grade = grade;
	}
}
