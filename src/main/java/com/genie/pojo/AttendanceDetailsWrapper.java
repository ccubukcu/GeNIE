package com.genie.pojo;

import java.io.Serializable;
import java.util.List;

import com.genie.model.User;

public class AttendanceDetailsWrapper implements Serializable {
	
	private static final long serialVersionUID = -6485449178244367543L;

	private User u;
	private List<AttendanceWrapper> attendances;
	
	public User getU() {
		return u;
	}
	public void setU(User u) {
		this.u = u;
	}
	public List<AttendanceWrapper> getAttendances() {
		return attendances;
	}
	public void setAttendances(List<AttendanceWrapper> attendances) {
		this.attendances = attendances;
	}
}
