package seung.java.common.excel;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.util.NumberToTextConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import seung.java.common.arguments.SMap;

/**
 * author       seung
 * description  excel util
 */
public class SExcelU {

	private static final Logger logger = LoggerFactory.getLogger(SExcelU.class);
	
	/**
	 * @param src
	 * @param sMap: sheetName, maxRowNum, maxCellNum
	 * @return
	 */
	public SMap readXls(byte[] src, SMap sMap) {
		
		SMap condition = null;
		
		ArrayList<SMap> sSheetList = null;
		SMap sSheetVO = null;
		ArrayList<SMap> sRowList = null;
		SMap sRowVO = null;
		ArrayList<SMap> sCellList = null;
		SMap sCellVO = null;
		
		Workbook workbook = null;
//		Sheet sheet = null;
		
		try {
			
			sSheetList = new ArrayList<SMap>();
			
			workbook = WorkbookFactory.create(new ByteArrayInputStream(src));
			
			sMap.put("numberOfSheets", workbook.getNumberOfSheets());
			logger.debug("numberOfSheets: " + sMap.getString("numberOfSheets"));
			
			boolean isContinue = false;
			for(Sheet sheet : workbook) {
				
				isContinue = false;
				condition = null;
				
				sSheetVO = new SSheetVO();
				sRowList = new ArrayList<SMap>();
				
				sSheetVO.put("sheetName", sheet.getSheetName());
				sSheetVO.put("physicalNumberOfRows", sheet.getPhysicalNumberOfRows());
				
				if(sMap.containsKey("sheetConditions") && sMap.get("sheetConditions") != null && sMap.getListSMap("sheetConditions").size() > 0) {
					isContinue = true;
					for(SMap sheetCondition : sMap.getListSMap("sheetConditions")) {
						if(sheetCondition.containsKey("sheetName") && sSheetVO.getString("sheetName").equals(sheetCondition.getString("sheetName"))) {
							condition = new SMap();
							condition.putAll(sheetCondition);
							break;
						}
					}
				}
				
				if(condition != null) {
					isContinue = true;
					if(condition.containsKey("sheetName") && sSheetVO.getString("sheetName").equals(condition.getString("sheetName"))) {
						isContinue = false;
					}
					if(isContinue) {
						sSheetList.add(sSheetVO);
						sMap.put("sheetList", sSheetList);
						continue;
					}
				}
				
				for(Row row : sheet) {
					
					sRowVO = new SRowVO();
					sCellList = new ArrayList<SMap>();
					
					sRowVO.put("rowNum", row.getRowNum());
					
					if(
						condition != null
						&& condition.containsKey("maxRowNum")
						&& row.getRowNum() > condition.getInt("maxRowNum")
						)
						break;
					
					if(sMap.containsKey("sheetConditions") && sMap.get("sheetConditions") != null && sMap.getListSMap("sheetConditions").size() > 0) {
						isContinue = true;
						for(SMap sheetCondition : sMap.getListSMap("sheetConditions")) {
							if(sheetCondition.containsKey("sheetName") && sSheetVO.getString("sheetName").equals(sheetCondition.getString("sheetName"))) {
								isContinue = false;
								break;
							}
						}
						if(isContinue) continue;
					}
					
					for(Cell cell : row) {
						
						isContinue = false;
						
						sCellVO = new SCellVO();
						
						sCellVO.put("rowIndex"   , cell.getRowIndex());
						sCellVO.put("columnIndex", cell.getColumnIndex());
						sCellVO.put("cellValue"  , getCellValue(cell));
						
						if(
							condition != null
							&& condition.containsKey("maxCellNum")
							&& cell.getColumnIndex() > condition.getInt("maxCellNum")
							)
							break;
						
						sCellList.add(sCellVO);
					}// end of cell
					
					sRowVO.put("cellList", sCellList);
					sRowList.add(sRowVO);
					
				}// end of row
				
				sSheetVO.put("rowList", sRowList);
				sSheetList.add(sSheetVO);
			}// end of sheet
			
			sMap.put("sheetList", sSheetList);
			
		} catch (Exception e) {
			
			sMap.put("exceptionMessage", e.getMessage());
			
		}
		
		return sMap;
	}
	
	private String getCellValue(Cell cell) {
		
		String cellValue = "";
		
		switch (cell.getCellTypeEnum()) {
			case BLANK:
				cellValue = "";
				break;
			case BOOLEAN:
				cellValue = "" + cell.getBooleanCellValue();
				break;
			case STRING:
				cellValue = "" + cell.getStringCellValue();
				break;
			case NUMERIC:
				if(DateUtil.isCellDateFormatted(cell)) cellValue = "" + cell.getDateCellValue();
				else                                   cellValue = NumberToTextConverter.toText(cell.getNumericCellValue());
				break;
			case FORMULA:
				cellValue = cell.getCellFormula();
				break;
			default:
				cellValue = null;
				break;
		}
		
		return cellValue;
	}
}
