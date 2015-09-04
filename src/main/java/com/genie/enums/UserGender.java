package com.genie.enums;

public enum UserGender {
	
	UNSPECIFIED(0),
	MALE(1),
	FEMALE(2);
	
	private int index;
	
	private UserGender(int index) {
		this.index = index;
	}

	public int getIndex() {
		return index;
	}
}
