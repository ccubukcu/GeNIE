package com.genie.utils;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFPalette;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.util.WorkbookUtil;

import com.genie.model.Exportable;
import com.genie.pojo.ExcelColumn;
import com.genie.pojo.ExcelData;

/**
 * @author ccubukcu
 *
 */
public class ExcelEngine {
	private static final int DEFAULT_ROW_HEIGHT_IN_TWIPS = 300;
	private static final int PIXEL_TO_TWIP_MULTIPLIER = 15;
	private static final Logger logger = Logger.getLogger(ExcelEngine.class);
	
	public static HSSFWorkbook createWorkbookWithSingleTablePerSheet(List<ExcelData> sheetDatas) {
		return createWorkbookWithSingleTablePerSheet(sheetDatas, null, null);
	}
	/**
	 * Her ExcelTable objesi icin bir excel sheet olusturup dondurur.
	 * */
	public static HSSFWorkbook createWorkbookWithSingleTablePerSheet(List<ExcelData> sheetDatas, HSSFCellStyle headerStyle, HSSFCellStyle bodyStyle) {
		HSSFWorkbook workbook = null;
		if(sheetDatas != null) {
			for (int i=0; i<sheetDatas.size(); i++) {
				ExcelData mySheet = sheetDatas.get(i);
				
				if(mySheet == null) {
					log("null sheet detected, skipping");
					continue;
				}
				
				if((mySheet.getHeaders() != null || mySheet.getExcelHeaders() != null) && mySheet.getSheetName() != null && mySheet.getData() != null) {
					if(workbook == null) {
						// null sheetData icin bombos excel olusturmasin diye burada
						workbook = new HSSFWorkbook();
					}

					HSSFFont headerFont = workbook.createFont();
					if(headerStyle == null) {
				        headerStyle = workbook.createCellStyle();
				        headerFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				        headerStyle.setFont(headerFont);
					}
					
					HSSFFont bodyFont = workbook.createFont();
					if(bodyStyle == null) {
				        bodyStyle = workbook.createCellStyle();
				        bodyStyle.setFont(bodyFont);
					}
					
			        String sheetName = WorkbookUtil.createSafeSheetName(mySheet.getSheetName(), '_');
			        
					fillSheetWithSingleTableData(workbook.createSheet(sheetName), headerStyle, bodyStyle, mySheet);
				} else {
					log("Headers, sheetname and data are all null, skipping");
				}
			}
		}
		return workbook;
	}
	
	/**
	 * Her 1. seviye liste elemani icin bir sheet olusturup, 2. seviyedeki listenin datasiyla doldurur
	 * */
	public static HSSFWorkbook createWorkbookWithMultipleTablesPerSheet(List<List<ExcelData>> sheetDatas) {
		return null;
	}
	
	/**
	 * @param sheet com.genie.model.Exportable interface'ini extend etmis objeleri iceren listeler
	 * */
	public static HSSFWorkbook createWorkbook(ExcelData sheet) {
		List<ExcelData> sheets = new ArrayList<ExcelData>();
		sheets.add(sheet);
		return createWorkbookWithSingleTablePerSheet(sheets);
	}
	
	private static HSSFSheet fillSheetWithSingleTableData(HSSFSheet emptySheet, HSSFCellStyle headerStyle, HSSFCellStyle bodyStyle, ExcelData tableData) {
		if(tableData.getHeaders() != null || tableData.getExcelHeaders() != null) {
			log("Creating first row of excel sheet (headers)");
			HSSFRow headerRow = emptySheet.createRow(0);
			if(tableData.getHeaders() == null) {
				for (int h=0; h<tableData.getExcelHeaders().size(); h++) {
					HSSFCell headerCell = headerRow.createCell(h, Cell.CELL_TYPE_STRING);
					ExcelColumn excelColumn = tableData.getExcelHeaders().get(h);

					emptySheet.setColumnWidth(h, excelColumn.getWidth());
					headerStyle.setWrapText(excelColumn.isWrapped());
					headerCell.setCellStyle(headerStyle);
					headerCell.setCellValue(excelColumn.getHeader());
				}
			} else {
				for (int h=0; h<tableData.getHeaders().size(); h++) {
					HSSFCell headerCell = headerRow.createCell(h, Cell.CELL_TYPE_STRING);
					headerCell.setCellValue(tableData.getHeaders().get(h));
				}
			}
			
			log("First row of excel sheet (headers) succesfully created");
		}
		
		boolean dataSuccess = true;
		if (tableData.getData() instanceof List<?>) {
			List<Object> sheetData = (List<Object>) tableData.getData();
			// ilk row'da headerlar oldugu icin data indexi 1den basliyor
			int rowNum = 1;
			for (int k = 0; k < sheetData.size(); k++) {
				log("Starting to fill data to the excel sheet.");
				if (sheetData.get(k) instanceof Exportable)
				{
					Exportable exp = (Exportable) sheetData.get(k);
					if(exp.isExportable()) {
						List<String> rowData = exp.getAsCellData();
						
						HSSFRow row = emptySheet.createRow(rowNum);
						for (int l = 0; l < rowData.size(); l++) {
							HSSFCell cell = row.createCell(l, Cell.CELL_TYPE_STRING);
							cell.setCellValue(rowData.get(l));
							
							if(tableData.getExcelHeaders() != null) {
								bodyStyle.setWrapText(tableData.getExcelHeaders().get(l).isWrapped());
							}
							cell.setCellStyle(bodyStyle);
						}
						++rowNum;
					}
				} else {
					dataSuccess = false;
					log("List<Object> data of ExcelSheet must consist of elements that implement Exportable interface.");
				}
			}
			
			if(dataSuccess) {
				log("Succesfully filled data to the excel sheet");
			} else {
				log("Failed to fill data to the excel sheet");
			}
		}
		
		if(emptySheet.getPhysicalNumberOfRows() > 0 && tableData.getExcelHeaders() == null) {
			log("Auto-sizing excel columns and assigning cell styles");
			HSSFRow header = emptySheet.getRow(0);
			for (int n=0; n<header.getLastCellNum()-1; n++) {
				emptySheet.autoSizeColumn(n);
				if(emptySheet.getColumnWidth(n) > 10000) {
					emptySheet.setColumnWidth(n, 10000);
				}
			}
	        
	        for (Cell cell : header) {
				cell.setCellStyle(headerStyle);
			}
		}
		
		return emptySheet;
	}
	
	public static HSSFSheet exportFilteredListFromBean(HSSFSheet emptySheet, HSSFCellStyle headerStyle, HSSFCellStyle bodyStyle, ExcelData tableData) {
		return fillSheetWithSingleTableData(emptySheet, headerStyle, bodyStyle, tableData);
	}
	
	public static  HSSFWorkbook applyStyles(HSSFWorkbook workbook, HSSFCellStyle headerStyle, HSSFCellStyle bodyStyle) {
		return applyStyles(workbook, headerStyle, bodyStyle, null, null);
	}
	
	public static  HSSFWorkbook applyStyles(HSSFWorkbook workbook, HSSFCellStyle headerStyle, HSSFCellStyle bodyStyle, Integer rowHeightInPixels) {
		return applyStyles(workbook, headerStyle, bodyStyle, null, rowHeightInPixels);
	}
	
	public static HSSFWorkbook applyStyles(HSSFWorkbook workbook, HSSFCellStyle headerStyle, HSSFCellStyle bodyStyle, List<ExcelColumn> columns, Integer rowHeightInPixels) {
		
		int rowHeightInTwips = (rowHeightInPixels != null && rowHeightInPixels > 0 ? rowHeightInPixels * PIXEL_TO_TWIP_MULTIPLIER : DEFAULT_ROW_HEIGHT_IN_TWIPS);
		
		for(int i=0; i < workbook.getNumberOfSheets(); i++) {
			HSSFSheet sheet = workbook.getSheetAt(i);
			
			if(sheet.getPhysicalNumberOfRows() > 0) {
				HSSFRow headerRow = sheet.getRow(0);
				if(columns != null) {
					for (int h=0; h<columns.size(); h++) {
						HSSFCell headerCell = headerRow.getCell(h);
						ExcelColumn excelColumn = columns.get(h);
						
						headerStyle.setWrapText(excelColumn.isWrapped());
						headerCell.setCellStyle(headerStyle);
	
						if(excelColumn.getWidth() != 0) 
							sheet.setColumnWidth(h, excelColumn.getWidth());
						if(excelColumn.getHeader() != null) {
							headerCell.setCellValue(excelColumn.getHeader());
						}
					}
				}
				
				if(sheet.getPhysicalNumberOfRows() > 1) {
					for(int r=1; r < sheet.getPhysicalNumberOfRows(); r++){
						HSSFRow row = sheet.getRow(r); 
						row.setHeight((short) rowHeightInTwips);
						
						for(int c=0; c < row.getPhysicalNumberOfCells(); c++) {
							row.getCell(c).setCellStyle(bodyStyle);
						}
			        }
				}
			}
		}
		return workbook;
	}
	
	public static HSSFCellStyle createHSSFCellStyle(HSSFWorkbook wb, HSSFFont font, short cellColor, short borderStyle, short verticalAlign, short horizontalAlign, boolean wrapText) {
		HSSFCellStyle style = wb.createCellStyle();
		style.setFillForegroundColor(cellColor);
		style = setCellStyle(style, font, borderStyle, verticalAlign, horizontalAlign, wrapText);
		return style;
	}
	
	/**
     * Sets the color at the given offset
     *
     * @param index the palette index, between 0x8 to 0x40 inclusive
     * @param red the RGB red component, between 0 and 255 inclusive
     * @param green the RGB green component, between 0 and 255 inclusive
     * @param blue the RGB blue component, between 0 and 255 inclusive
     */
	public static HSSFCellStyle createHSSFCellStyleWithCustomColor(HSSFWorkbook wb, HSSFFont font, short borderStyle, short verticalAlign, short horizontalAlign, boolean wrapText,
			short index, byte red, byte green, byte blue) {
		HSSFPalette palette = wb.getCustomPalette();
		HSSFColor hssfColor = null;
		palette.setColorAtIndex(index, red, green, blue);
	    hssfColor = palette.getColor(index);
	    
		HSSFCellStyle style = wb.createCellStyle();
		style.setFillForegroundColor(hssfColor.getIndex());
		
		style = setCellStyle(style, font, borderStyle, verticalAlign, horizontalAlign, wrapText);

		return style;
	}
	
	private static HSSFCellStyle setCellStyle(HSSFCellStyle style, HSSFFont font, short borderStyle, short verticalAlign, short horizontalAlign, boolean wrapText) {
		style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		style.setBorderBottom(borderStyle);
		style.setBorderLeft(borderStyle);
		style.setBorderRight(borderStyle);
		style.setBorderTop(borderStyle);
		style.setAlignment(horizontalAlign);
		style.setVerticalAlignment(verticalAlign);
		style.setWrapText(wrapText);
		style.setFont(font);
		return style;
	}
	
	private static void log(String log) {
		if (logger.isDebugEnabled())
			logger.debug(log);
	}
}
