package com.genie.pojo;

import java.io.Serializable;

import com.genie.model.AssignmentSubmission;
import com.genie.model.StudentAttendance;
import com.genie.model.StudentGrade;
import com.genie.model.User;

public class StudentRowWrapper implements Serializable{
	private static final long serialVersionUID = 447936310894698597L;

	private User student;
	private StudentGrade grade;
	private StudentAttendance attendance;
	private AssignmentSubmission submission;
	
	public User getStudent() {
		return student;
	}
	public void setStudent(User student) {
		this.student = student;
	}
	public StudentGrade getGrade() {
		return grade;
	}
	public void setGrade(StudentGrade grade) {
		this.grade = grade;
	}
	public StudentAttendance getAttendance() {
		return attendance;
	}
	public void setAttendance(StudentAttendance attendance) {
		this.attendance = attendance;
	}
	public AssignmentSubmission getSubmission() {
		return submission;
	}
	public void setSubmission(AssignmentSubmission submission) {
		this.submission = submission;
	}
}
