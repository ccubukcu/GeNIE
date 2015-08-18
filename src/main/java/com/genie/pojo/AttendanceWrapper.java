package com.genie.pojo;

import java.io.Serializable;

import com.genie.utils.ResourceUtil;

public class AttendanceWrapper implements Serializable{

	private static final long serialVersionUID = -4309779303933967869L;

	private int week;
	private boolean attendance;
	
	public AttendanceWrapper() {
	}
	
	public AttendanceWrapper(int week) {
		this.week = week;
		attendance = false;
	}
	
	public int getWeek() {
		return week;
	}
	public void setWeek(int week) {
		this.week = week;
	}
	public boolean isAttendance() {
		return attendance;
	}
	public void setAttendance(boolean attendance) {
		this.attendance = attendance;
	}
	
	public String getAttendanceString() {
		return attendance ? ResourceUtil.getLabel("general.labels.yes") : ResourceUtil.getLabel("general.labels.no");
	}
}
