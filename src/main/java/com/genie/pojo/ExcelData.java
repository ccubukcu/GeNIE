package com.genie.pojo;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ccubukcu
 *
 */
public class ExcelData {
	
	private Object data;
	private List<String> headers;
	private List<ExcelColumn> excelColumns; 
	private String sheetName;
	
	/**@param data List<com.genie.model.Exportable> cinsinden bir degisken olmali*/
	public ExcelData(String sheetName, Object data, List<String> headers) {
		this.sheetName = sheetName;
		this.data = data;
		this.headers = headers;
	}
	
	/**@param data List<com.genie.model.Exportable> cinsinden bir degisken olmali*/
	public ExcelData(String sheetName, Object data, ArrayList<ExcelColumn> excelColumns) {
		this.sheetName = sheetName;
		this.data = data;
		this.excelColumns = excelColumns;
	}
	
	public ExcelData() {
	}
	
	public Object getData() {
		return data;
	}
	
	/**@param data List<com.genie.model.Exportable> cinsinden bir degisken olmali*/
	public void setData(Object data) {
		this.data = data;
	}
	public List<String> getHeaders() {
		return headers;
	}
	public void setHeaders(List<String> headers) {
		this.headers = headers;
	}
	public String getSheetName() {
		return sheetName;
	}
	public void setSheetName(String sheetName) {
		this.sheetName = sheetName;
	}

	public List<ExcelColumn> getExcelHeaders() {
		return excelColumns;
	}

	public void setExcelHeaders(List<ExcelColumn> excelColumns) {
		this.excelColumns = excelColumns;
	}
}
