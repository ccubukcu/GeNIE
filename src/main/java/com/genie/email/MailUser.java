package com.genie.email;

import java.io.Serializable;

public class MailUser implements Serializable{
	
	private static final long serialVersionUID = 4080632064328918965L;

	private String email;
	private boolean isCc;
	
	public MailUser() {
	}

	public MailUser(String email) {
		this.email = email;
	}

	public MailUser(String email, boolean isCc) {
		this.email = email;
		this.isCc = isCc;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public boolean isCc() {
		return isCc;
	}

	public void setCc(boolean isCc) {
		this.isCc = isCc;
	}

}
