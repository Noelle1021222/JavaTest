package test.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFCreationHelper;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelUtil {

	private Map<String, Object> styleMap = new HashMap<String, Object>();
	

	public HashSet getProductOwnersByReadExcel(String fileLocation, String sheetName, int indexProductOwner) throws IOException, InvalidFormatException {
		HashSet setProductOwners = new HashSet();
		String strProductOwnerEmail = "";
		OPCPackage pkg = OPCPackage.open(new FileInputStream(fileLocation));
		XSSFWorkbook wb = new XSSFWorkbook(pkg);
		XSSFSheet sheet = wb.getSheet(sheetName);
		for (int i = sheet.getFirstRowNum() + 1; i <= sheet.getLastRowNum(); i++) {
			// System.out.println(i);
			XSSFRow row = sheet.getRow(i);
			// System.out.println(row);
			XSSFCell cell = row.getCell((short) indexProductOwner);
			// System.out.println(cell);
			if (cell != null) {
				strProductOwnerEmail = cell.getStringCellValue() == null ? "" : cell.getStringCellValue();
			}
			if (!"".equals(strProductOwnerEmail)) {
				setProductOwners.add(strProductOwnerEmail);
			}
		}
		// System.out.println("Total rows: " + setProductOwners.size());
		pkg.close();
		return setProductOwners;
	}

	private boolean isCellDateFormatted(XSSFCell cell) {
		boolean bDate = false;
		double d = cell.getNumericCellValue();
		if (HSSFDateUtil.isValidExcelDate(d)) {
			XSSFCellStyle style = cell.getCellStyle();
			int i = style.getDataFormat();
			switch (i) {
			case 0xe:
			case 0xf:
			case 0x10:
			case 0x11:
			case 0x12:
			case 0x13:
			case 0x14:
			case 0x15:
			case 0x16:
			case 0x2d:
			case 0x2e:
			case 0x2f:
				bDate = true;
				break;
			default:
				bDate = false;
				break;
			}
		}
		return bDate;
	}

	public void outputHeader(String outputFile, String[] header) throws FileNotFoundException, IOException, InvalidFormatException {
		FileOutputStream fileOut = new FileOutputStream(outputFile, false);
		XSSFWorkbook wb = new XSSFWorkbook();
		XSSFSheet sh = wb.createSheet("New sheet");
		XSSFCreationHelper createHelper = wb.getCreationHelper();

		XSSFRow row = sh.createRow(0);
		for (int j = 0; j < header.length; j++) {
			XSSFCell cell = row.createCell(j);
			cell.setCellValue(createHelper.createRichTextString(header[j]));
		}

		sh.createFreezePane(0, 1, 0, 1);

		wb.write(fileOut);
		fileOut.close();
	}

	public void outputArrayList(String outputFile, ArrayList list) throws FileNotFoundException, IOException, InvalidFormatException {
		InputStream inp = new FileInputStream(outputFile);
		XSSFWorkbook wb = (XSSFWorkbook) WorkbookFactory.create(inp);
		XSSFSheet sh = wb.getSheetAt(0);
		XSSFCreationHelper createHelper = wb.getCreationHelper();

		// contents
		int i = 1;
		Iterator it = list.iterator();
		while (it.hasNext()) {
			String[] str = (String[]) it.next();
			XSSFRow row = sh.createRow(i);

			for (int j = 0; j < str.length; j++) {
				XSSFCell cell = row.createCell(j);
				cell.setCellValue(createHelper.createRichTextString(str[j]));
			}

			i++;
		}

		FileOutputStream fileOut = new FileOutputStream(outputFile, false);
		wb.write(fileOut);
		fileOut.close();
	}

	public XSSFWorkbook createWorkbook() {
		XSSFWorkbook wb = new XSSFWorkbook();
		return wb;
	}

	public XSSFWorkbook createWorkbook(String sheetName) {
		XSSFWorkbook wb = new XSSFWorkbook();
		XSSFSheet    sh = wb.createSheet(sheetName);

		return wb;
	}
	/**
	 * @param wb
	 * @param sheet
	 * @param indexOfRow
	 * @param header
	 * @param headerData
	 * @return
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws InvalidFormatException
	 */
	public XSSFSheet setVerticalHeader(XSSFWorkbook wb, String sheet, int indexOfRow,Object[] header,Object[] headerData) throws FileNotFoundException, IOException, InvalidFormatException {
		XSSFSheet 					 sh = wb.getSheet(sheet);
		XSSFCreationHelper createHelper = wb.getCreationHelper();

		for (int j = 0; j < header.length; j++) {
			XSSFRow    headerRow = sh.createRow(indexOfRow);
			XSSFCell   cellLabel = headerRow.createCell(0);			
			cellLabel.setCellValue(createHelper.createRichTextString(header[j].toString()));
			XSSFCell    cellData = headerRow.createCell(1);	
			cellData.setCellValue(createHelper.createRichTextString(headerData[j].toString()));
		}
		sh.createFreezePane(0, 1, 0, 1);
		return sh;
	}

	/**
	 * @param wb
	 * @param sheet
	 * @param indexOfRow
	 * @param header
	 * @return
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws InvalidFormatException
	 */
	public XSSFSheet setHorizontalHeader(XSSFWorkbook wb, String sheet, int indexOfRow,String[] header) throws FileNotFoundException, IOException, InvalidFormatException {
		XSSFSheet 					 sh = wb.getSheet(sheet);
		XSSFCreationHelper createHelper = wb.getCreationHelper();

		XSSFRow row = sh.createRow(indexOfRow);
		for (int j = 0; j < header.length; j++) {
			XSSFCell cell = row.createCell(j);
			cell.setCellValue(createHelper.createRichTextString(header[j]));
		}
		sh.createFreezePane(0, 1, 0, 1);
		return sh;
	}
	
	public XSSFSheet setHeader(XSSFWorkbook wb, String sheet, String[] header) throws FileNotFoundException, IOException, InvalidFormatException {
		XSSFSheet sh = wb.createSheet(sheet);
		XSSFCreationHelper createHelper = wb.getCreationHelper();

		XSSFRow row = sh.createRow(0);
		for (int j = 0; j < header.length; j++) {
			XSSFCell cell = row.createCell(j);
			cell.setCellValue(createHelper.createRichTextString(header[j]));
		}

		sh.createFreezePane(0, 1, 0, 1);

		return sh;
	}

	/**
	 * @param wb
	 * @param sh
	 * @param header
	 * @param from
	 * @param to
	 * @param foreGroundColor
	 * @param fontColor
	 * @throws IOException
	 * @throws InvalidFormatException
	 */
	public void setHeaderColor(XSSFWorkbook wb, XSSFSheet sh, String[] header, int from, int to, String foreGroundColor, String fontColor) throws IOException, InvalidFormatException {
		XSSFCreationHelper createHelper = wb.getCreationHelper();
		
		XSSFCellStyle style = null;
		JSONObject styleJsonObject =new JSONObject(styleMap);
		boolean flag = false;
		XSSFRow row = sh.getRow(0);
		for (int j = 0; j < header.length; j++) {
			// System.out.println(row.getCell(j).getStringCellValue());
			if (j >= from & j <= to) {
				XSSFCell cell = row.getCell(j);
				// System.out.println("Row: " + row.getRowNum() + "; Column: " +
				// j + "; " + cell.getStringCellValue());
				// Fore Ground
				
				if(this.styleMap.isEmpty()) {
					style = wb.createCellStyle();
					flag = true;
				}
				else {
					if(this.styleMap.containsKey(styleJsonObject.toString())) {
						style = (XSSFCellStyle) this.styleMap.get(styleJsonObject.toString());
					}else {
						style = wb.createCellStyle();
						flag = true;
					}
				}
				if ("LIGHT_BLUE".equals(foreGroundColor)) {
					style.setFillForegroundColor(IndexedColors.LIGHT_BLUE.index);
				} else if ("LIME".equals(foreGroundColor)) {
					style.setFillForegroundColor(IndexedColors.LIME.index);
				} else if ("LIGHT_ORANGE".equals(foreGroundColor)) {
					style.setFillForegroundColor(IndexedColors.LIGHT_ORANGE.index);
				} else if ("LIGHT_YELLOW".equals(foreGroundColor)) {
					style.setFillForegroundColor(IndexedColors.LIGHT_YELLOW.index);
				} else {
					style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.index);
				}
				style.setFillPattern(FillPatternType.SOLID_FOREGROUND);

				// Font
				XSSFFont font = wb.createFont();
				if ("WHITE".equals(fontColor)) {
					font.setColor(IndexedColors.WHITE.index);
				} else if ("RED".equals(fontColor)) {
					font.setColor(IndexedColors.RED.index);
				} else {
					font.setColor(IndexedColors.AUTOMATIC.index);
				}
				style.setFont(font);

				// Set style to cell
//				cell.setCellStyle(style);
			    cell.getCellStyle().cloneStyleFrom(style);

			    if(flag) {
			    	this.styleMap.put(styleJsonObject.toString(), style);
			    	flag =false;
			    }
				cell.setCellValue(createHelper.createRichTextString(header[j]));
			}
		}
	}

	

	
	public void setCellColor(XSSFWorkbook wb, XSSFSheet sh, int rowIndex,int from, int to, String foreGroundColor, String fontColor) throws IOException, InvalidFormatException {
		XSSFCreationHelper createHelper = wb.getCreationHelper();
		XSSFCellStyle style = null;
		boolean flag = false;
		XSSFRow row = sh.getRow(rowIndex);
		for (int j = from; j <= to ; j++) {
			// System.out.println(row.getCell(j).getStringCellValue());
			if (j >= from & j <= to) {
				XSSFCell cell = row.getCell(j);
				// System.out.println("Row: " + row.getRowNum() + "; Column: " +
				// j + "; " + cell.getStringCellValue());
				// Fore Ground
				
				if(this.styleMap.isEmpty()) {
					style = wb.createCellStyle();
					flag = true;
				}

				
				if(foreGroundColor != null)
				{
					if ("LIGHT_BLUE".equals(foreGroundColor)) {
						style.setFillForegroundColor(IndexedColors.LIGHT_BLUE.index);
					} else if ("LIME".equals(foreGroundColor)) {
						style.setFillForegroundColor(IndexedColors.LIME.index);
					} else if ("LIGHT_ORANGE".equals(foreGroundColor)) {
						style.setFillForegroundColor(IndexedColors.LIGHT_ORANGE.index);
					} else if ("LIGHT_YELLOW".equals(foreGroundColor)) {
						style.setFillForegroundColor(IndexedColors.LIGHT_YELLOW.index);
					} else {
						style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.index);
					}
					style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
				}
				// Font
			    XSSFFont font = wb.createFont();
				if(fontColor != null)
				{
				    if ("WHITE".equals(fontColor)) {
				    	font.setColor(IndexedColors.WHITE.index);
				    } else if ("RED".equals(fontColor)) {
				    	font.setColor(IndexedColors.RED.index);
				    } else {
				    	font.setColor(IndexedColors.AUTOMATIC.index);
				    }
				}
			    style.setFont(font);
				// Set style to cell
				//cell.setCellStyle(style);
			    cell.getCellStyle().cloneStyleFrom(style);
			}
		}
	}	
	
	
	/**
	 * @param wb
	 * @param sheet
	 * @param list
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public void outputArrayList(XSSFWorkbook wb, String sheet, ArrayList list) throws FileNotFoundException, IOException {
		XSSFSheet sh = wb.createSheet(sheet);
		XSSFCreationHelper createHelper = wb.getCreationHelper();
		// contents
		int i = 0;
		Iterator it = list.iterator();
		while (it.hasNext()) {
			String[] str = (String[]) it.next();
			XSSFRow row = sh.createRow(i);
			for (int j = 0; j < str.length; j++) {
				XSSFCell cell = row.createCell(j);
				cell.setCellValue(createHelper.createRichTextString(str[j]));
			}
			i++;
		}
	}

	
	/**
	 * @param wb
	 * @param sheet
	 * @param str
	 * @param rowIndex
	 * @return
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public XSSFRow outputExcelRow(XSSFWorkbook wb, String sheet, String[] str,int rowIndex) throws FileNotFoundException, IOException {
		XSSFSheet sh = wb.getSheet(sheet);
		if(sh == null )
			sh = wb.createSheet(sheet);
		XSSFCreationHelper createHelper = wb.getCreationHelper();
		// contents
		XSSFRow row = sh.createRow(rowIndex);
		for (int j = 0; j < str.length; j++) {
			XSSFCell cell = row.createCell(j);
			cell.setCellValue(createHelper.createRichTextString(str[j]));
		}
		return row;
	}	
	
	/**
	 * @param wb
	 * @param sh
	 * @param list
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public void outputArrayList(XSSFWorkbook wb, XSSFSheet sh, ArrayList list) throws FileNotFoundException, IOException {
		XSSFCreationHelper createHelper = wb.getCreationHelper();
		// contents
		int i = 1;
		Iterator it = list.iterator();
		while (it.hasNext()) {
			String[] str = (String[]) it.next();
			XSSFRow row = sh.createRow(i);
			for (int j = 0; j < str.length; j++) {
				XSSFCell cell = row.createCell(j);
				cell.setCellValue(createHelper.createRichTextString(str[j]));
			}
			i++;
		}
	}

	/**
	 * @param wb
	 * @param sh
	 * @param str
	 * @param rowIndex
	 * @return
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public XSSFRow outputExcelRow(XSSFWorkbook wb, XSSFSheet sh, String[] str,int rowIndex) throws FileNotFoundException, IOException {
		XSSFCreationHelper createHelper = wb.getCreationHelper();
		// contents
		XSSFRow row = sh.createRow(rowIndex);
		for (int j = 0; j < str.length; j++) {
				XSSFCell cell = row.createCell(j);
				cell.setCellValue(createHelper.createRichTextString(str[j]));
		}
		return row;
	}
	
	public void outputExcel(XSSFWorkbook wb, String outputFile) throws FileNotFoundException, IOException {
		FileOutputStream fileOut = new FileOutputStream(outputFile, false);
		wb.write(fileOut);
		fileOut.close();
	}



}
