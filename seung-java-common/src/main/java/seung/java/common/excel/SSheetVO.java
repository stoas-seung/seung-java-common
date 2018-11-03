package seung.java.common.excel;

import seung.java.common.arguments.SMap;

/**
 * author       seung
 * description  sheet value object
 */
public class SSheetVO extends SMap {

	private static final long serialVersionUID = 1L;
	
	public SSheetVO() {
		this.put("sheetName"           , "");
		this.put("physicalNumberOfRows", "");
		this.put("rowList"             , null);
	}
}
