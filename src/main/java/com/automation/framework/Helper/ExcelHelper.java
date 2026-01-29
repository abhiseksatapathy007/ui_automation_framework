package com.automation.framework.Helper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelHelper {

	private String filePath;
	private Workbook workbook;
	private Sheet sheet;

	/**
	 * Constructor to initialize Excel file path
	 * 
	 * @param filePath Path to the Excel file
	 */
	public ExcelHelper(String filePath) {
		this.filePath = filePath;
	}

	/**
	 * Opens an existing Excel file
	 * 
	 * @throws IOException if file cannot be opened
	 */
	public void openWorkbook() throws IOException {
		FileInputStream fileInputStream = new FileInputStream(new File(filePath));
		workbook = WorkbookFactory.create(fileInputStream);
		fileInputStream.close();
	}

	/**
	 * Creates a new Excel workbook
	 */
	public void createWorkbook() {
		workbook = new XSSFWorkbook();
	}

	/**
	 * Sets the active sheet by name
	 * 
	 * @param sheetName Name of the sheet
	 */
	public void setSheet(String sheetName) {
		sheet = workbook.getSheet(sheetName);
		if (sheet == null) {
			sheet = workbook.createSheet(sheetName);
		}
	}

	/**
	 * Sets the active sheet by index
	 * 
	 * @param sheetIndex Index of the sheet (0-based)
	 */
	public void setSheet(int sheetIndex) {
		sheet = workbook.getSheetAt(sheetIndex);
	}

	/**
	 * Creates a new sheet
	 * 
	 * @param sheetName Name of the new sheet
	 * @return Created Sheet object
	 */
	public Sheet createSheet(String sheetName) {
		sheet = workbook.createSheet(sheetName);
		return sheet;
	}

	/**
	 * Reads a cell value as String
	 * 
	 * @param rowIndex    Row index (0-based)
	 * @param columnIndex Column index (0-based)
	 * @return Cell value as String
	 */
	public String readCell(int rowIndex, int columnIndex) {
		Row row = sheet.getRow(rowIndex);
		if (row == null) {
			return "";
		}
		Cell cell = row.getCell(columnIndex);
		if (cell == null) {
			return "";
		}
		return getCellValueAsString(cell);
	}

	/**
	 * Reads a cell value by row and column name (first row contains headers)
	 * 
	 * @param rowIndex  Row index (0-based, excluding header row)
	 * @param columnName Column name from header row
	 * @return Cell value as String
	 */
	public String readCellByColumnName(int rowIndex, String columnName) {
		int columnIndex = getColumnIndex(columnName);
		return readCell(rowIndex + 1, columnIndex);
	}

	/**
	 * Writes a value to a cell
	 * 
	 * @param rowIndex    Row index (0-based)
	 * @param columnIndex Column index (0-based)
	 * @param value       Value to write
	 */
	public void writeCell(int rowIndex, int columnIndex, Object value) {
		Row row = sheet.getRow(rowIndex);
		if (row == null) {
			row = sheet.createRow(rowIndex);
		}
		Cell cell = row.getCell(columnIndex);
		if (cell == null) {
			cell = row.createCell(columnIndex);
		}
		setCellValue(cell, value);
	}

	/**
	 * Reads all data from a sheet as List of Maps (first row as headers)
	 * 
	 * @return List of Maps where each Map represents a row with column names as keys
	 */
	public List<Map<String, String>> readAllData() {
		List<Map<String, String>> data = new ArrayList<>();
		if (sheet.getPhysicalNumberOfRows() == 0) {
			return data;
		}

		Row headerRow = sheet.getRow(0);
		List<String> headers = new ArrayList<>();
		for (Cell cell : headerRow) {
			headers.add(getCellValueAsString(cell));
		}

		for (int i = 1; i <= sheet.getLastRowNum(); i++) {
			Row row = sheet.getRow(i);
			if (row == null) {
				continue;
			}
			Map<String, String> rowData = new HashMap<>();
			for (int j = 0; j < headers.size(); j++) {
				Cell cell = row.getCell(j);
				String value = cell != null ? getCellValueAsString(cell) : "";
				rowData.put(headers.get(j), value);
			}
			data.add(rowData);
		}
		return data;
	}

	/**
	 * Reads data from a specific row as Map (first row as headers)
	 * 
	 * @param rowIndex Row index (0-based, excluding header row)
	 * @return Map with column names as keys and cell values as values
	 */
	public Map<String, String> readRow(int rowIndex) {
		Map<String, String> rowData = new HashMap<>();
		if (sheet.getPhysicalNumberOfRows() == 0) {
			return rowData;
		}

		Row headerRow = sheet.getRow(0);
		Row dataRow = sheet.getRow(rowIndex + 1);
		if (dataRow == null) {
			return rowData;
		}

		for (int i = 0; i < headerRow.getLastCellNum(); i++) {
			Cell headerCell = headerRow.getCell(i);
			Cell dataCell = dataRow.getCell(i);
			String header = headerCell != null ? getCellValueAsString(headerCell) : "Column" + i;
			String value = dataCell != null ? getCellValueAsString(dataCell) : "";
			rowData.put(header, value);
		}
		return rowData;
	}

	/**
	 * Writes data to a row
	 * 
	 * @param rowIndex Row index (0-based)
	 * @param data     Array of values to write
	 */
	public void writeRow(int rowIndex, Object... data) {
		Row row = sheet.getRow(rowIndex);
		if (row == null) {
			row = sheet.createRow(rowIndex);
		}
		for (int i = 0; i < data.length; i++) {
			Cell cell = row.createCell(i);
			setCellValue(cell, data[i]);
		}
	}

	/**
	 * Writes headers to the first row
	 * 
	 * @param headers Array of header names
	 */
	public void writeHeaders(String... headers) {
		writeRow(0, (Object[]) headers);
	}

	/**
	 * Gets the number of rows in the current sheet
	 * 
	 * @return Number of rows
	 */
	public int getRowCount() {
		return sheet.getPhysicalNumberOfRows();
	}

	/**
	 * Gets the number of columns in a specific row
	 * 
	 * @param rowIndex Row index (0-based)
	 * @return Number of columns
	 */
	public int getColumnCount(int rowIndex) {
		Row row = sheet.getRow(rowIndex);
		return row != null ? row.getLastCellNum() : 0;
	}

	/**
	 * Gets the column index by column name (searches in first row)
	 * 
	 * @param columnName Column name to search
	 * @return Column index (0-based), -1 if not found
	 */
	public int getColumnIndex(String columnName) {
		Row headerRow = sheet.getRow(0);
		if (headerRow == null) {
			return -1;
		}
		for (Cell cell : headerRow) {
			if (getCellValueAsString(cell).equalsIgnoreCase(columnName)) {
				return cell.getColumnIndex();
			}
		}
		return -1;
	}

	/**
	 * Saves the workbook to file
	 * 
	 * @throws IOException if file cannot be saved
	 */
	public void saveWorkbook() throws IOException {
		FileOutputStream fileOutputStream = new FileOutputStream(new File(filePath));
		workbook.write(fileOutputStream);
		fileOutputStream.close();
		workbook.close();
	}

	/**
	 * Saves the workbook to a new file path
	 * 
	 * @param newFilePath New file path
	 * @throws IOException if file cannot be saved
	 */
	public void saveWorkbookAs(String newFilePath) throws IOException {
		FileOutputStream fileOutputStream = new FileOutputStream(new File(newFilePath));
		workbook.write(fileOutputStream);
		fileOutputStream.close();
	}

	/**
	 * Closes the workbook without saving
	 * 
	 * @throws IOException if workbook cannot be closed
	 */
	public void closeWorkbook() throws IOException {
		if (workbook != null) {
			workbook.close();
		}
	}

	/**
	 * Gets cell value as String regardless of cell type
	 * 
	 * @param cell Cell object
	 * @return Cell value as String
	 */
	private String getCellValueAsString(Cell cell) {
		if (cell == null) {
			return "";
		}
		switch (cell.getCellType()) {
		case STRING:
			return cell.getStringCellValue();
		case NUMERIC:
			if (org.apache.poi.ss.usermodel.DateUtil.isCellDateFormatted(cell)) {
				return cell.getDateCellValue().toString();
			} else {
				double numericValue = cell.getNumericCellValue();
				if (numericValue == (long) numericValue) {
					return String.valueOf((long) numericValue);
				} else {
					return String.valueOf(numericValue);
				}
			}
		case BOOLEAN:
			return String.valueOf(cell.getBooleanCellValue());
		case FORMULA:
			return cell.getCellFormula();
		case BLANK:
			return "";
		default:
			return "";
		}
	}

	/**
	 * Sets cell value based on object type
	 * 
	 * @param cell  Cell object
	 * @param value Value to set
	 */
	private void setCellValue(Cell cell, Object value) {
		if (value == null) {
			cell.setBlank();
		} else if (value instanceof String) {
			cell.setCellValue((String) value);
		} else if (value instanceof Number) {
			cell.setCellValue(((Number) value).doubleValue());
		} else if (value instanceof Boolean) {
			cell.setCellValue((Boolean) value);
		} else {
			cell.setCellValue(value.toString());
		}
	}

	/**
	 * Gets all sheet names in the workbook
	 * 
	 * @return List of sheet names
	 */
	public List<String> getSheetNames() {
		List<String> sheetNames = new ArrayList<>();
		for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
			sheetNames.add(workbook.getSheetName(i));
		}
		return sheetNames;
	}

	/**
	 * Checks if a sheet exists
	 * 
	 * @param sheetName Name of the sheet
	 * @return true if sheet exists, false otherwise
	 */
	public boolean sheetExists(String sheetName) {
		return workbook.getSheet(sheetName) != null;
	}
}

