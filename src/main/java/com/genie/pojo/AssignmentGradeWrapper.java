package com.genie.pojo;

import java.io.Serializable;
import java.util.List;

import com.genie.model.User;

public class AssignmentGradeWrapper implements Serializable {
	
	private static final long serialVersionUID = 280412789424186603L;

	private User u;
	private List<GradeWrapper> grades;
	
	public User getU() {
		return u;
	}
	public void setU(User u) {
		this.u = u;
	}
	public List<GradeWrapper> getGrades() {
		return grades;
	}
	public void setGrades(List<GradeWrapper> grades) {
		this.grades = grades;
	}
}
