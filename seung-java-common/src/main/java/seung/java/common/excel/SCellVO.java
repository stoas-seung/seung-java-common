package seung.java.common.excel;

import seung.java.common.arguments.SMap;

/**
 * author       seung
 * description  cell value object
 */
public class SCellVO extends SMap {

	private static final long serialVersionUID = 1L;
	
	public SCellVO() {
		this.put("rowIndex"   , -1);
		this.put("columnIndex", -1);
		this.put("cellValue"  , "");
	}
}
