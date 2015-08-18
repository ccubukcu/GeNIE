package com.genie.pojo;

/**
 * @author ccubukcu
 *
 */
public class ExcelColumn {
	private String header;
	private int width;
	private boolean wrapped;
	
	public ExcelColumn(String header, int width, boolean wrapped) {
		this.header = header;
		this.width = width;
		this.wrapped = wrapped;
	}
	
	public ExcelColumn(String header) {
		this.header = header;
		width = 5000;
		wrapped = false;
	}
	
	public ExcelColumn(String header, int width) {
		this.header = header;
		this.width = width;
		wrapped = false;
	}
	
	public ExcelColumn(String header, boolean wrapped) {
		this.header = header;
		width = 5000;
		this.wrapped = wrapped;
	}
	
	public String getHeader() {
		return header;
	}
	public void setHeader(String header) {
		this.header = header;
	}
	public int getWidth() {
		return width;
	}
	public void setWidth(int width) {
		this.width = width;
	}
	public boolean isWrapped() {
		return wrapped;
	}
	public void setWrapped(boolean wrapped) {
		this.wrapped = wrapped;
	}
}
