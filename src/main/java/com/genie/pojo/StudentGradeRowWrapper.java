package com.genie.pojo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.genie.model.User;

public class StudentGradeRowWrapper implements Serializable{

	private static final long serialVersionUID = 1314140378610572827L;

	private User student;

	private GradeWrapper attendanceGrade;
	
	private GradeWrapper finalAssignmentGrades;
	
	private GradeWrapper gamificationGrade;
	
	private List<GradeWrapper> examGrades;
	
	private List<GradeWrapper> otherGrades;

	private String finalGrade;
	
	private String type;
	
	public StudentGradeRowWrapper() {
		examGrades = new ArrayList<GradeWrapper>();
		otherGrades = new ArrayList<GradeWrapper>();
		attendanceGrade = new GradeWrapper();
		finalAssignmentGrades = new GradeWrapper();
		gamificationGrade = new GradeWrapper();
	}
	
	public void addToExamGrades(GradeWrapper gw) {
		examGrades.add(gw);
	}
	
	public void addToOtherGrades(GradeWrapper gw) {
		otherGrades.add(gw);
	}
	
	public User getStudent() {
		return student;
	}

	public void setStudent(User student) {
		this.student = student;
	}

	public GradeWrapper getAttendanceGrade() {
		return attendanceGrade;
	}

	public void setAttendanceGrade(GradeWrapper attendanceGrade) {
		this.attendanceGrade = attendanceGrade;
	}

	public GradeWrapper getFinalAssignmentGrades() {
		return finalAssignmentGrades;
	}

	public void setFinalAssignmentGrades(GradeWrapper finalAssignmentGrades) {
		this.finalAssignmentGrades = finalAssignmentGrades;
	}

	public GradeWrapper getGamificationGrade() {
		return gamificationGrade;
	}

	public void setGamificationGrade(GradeWrapper gamificationGrade) {
		this.gamificationGrade = gamificationGrade;
	}

	public List<GradeWrapper> getExamGrades() {
		return examGrades;
	}

	public void setExamGrades(List<GradeWrapper> examGrades) {
		this.examGrades = examGrades;
	}

	public List<GradeWrapper> getOtherGrades() {
		return otherGrades;
	}

	public void setOtherGrades(List<GradeWrapper> otherGrades) {
		this.otherGrades = otherGrades;
	}

	public String getFinalGrade() {
		return finalGrade;
	}

	public void setFinalGrade(String finalGrade) {
		this.finalGrade = finalGrade;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
