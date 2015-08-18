package com.genie.email;

/**
 * @author ccubukcu
 *
 */
public enum MailingPoint {
	
	SAMPLE_POINT(1L);
	
	private long value;
	
	private MailingPoint(long value) {
		this.value = value;
	}
	
	public long getValue() {
		return value;
	}
	
	public String getLabel() {
		return getLabel(this.value);
	}
	
	public static String getLabel(MailingPoint item) {
		return "";
	}
	
	public static String getLabel(long roleValue) {
		for (MailingPoint item : values()) {
			if (item.value == roleValue) {
				return getLabel(item);
			}
		}
		return null;
	}
	
	public static MailingPoint getObject(long roleValue) {
		for (MailingPoint item : values()) {
			if (item.value == roleValue) {
				return item;
			}
		}
		return null;
	}
}
