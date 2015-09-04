package com.genie.enums;

public enum AttendanceStatus {
	UNSPECIFIED(0),
	ATTENDED(1),
	NA(2);
	
	private int index;
	
	private AttendanceStatus(int index) {
		this.index = index;
	}

	public int getIndex() {
		return index;
	}
}
