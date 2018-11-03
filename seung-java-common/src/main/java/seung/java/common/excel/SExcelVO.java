package seung.java.common.excel;

import seung.java.common.arguments.SMap;

/**
 * author       seung
 * description  excel value object
 */
public class SExcelVO extends SMap {

	private static final long serialVersionUID = 1L;

	public SExcelVO() {
		this.put("numberOfSheets"  , -1);
		this.put("sheetList"       , null);
		this.put("exceptionMessage", "");
	}
}
