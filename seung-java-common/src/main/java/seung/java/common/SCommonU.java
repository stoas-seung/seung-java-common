package seung.java.common;

import com.google.gson.GsonBuilder;

/**
 * author       seung
 * description  common util class
 */
public class SCommonU {

	public String toJsonString(Object object) {
		return toJsonString(object, false);
	}
	public String toJsonString(Object object, boolean isPrettyPrinting) {
		GsonBuilder gsonBuilder = new GsonBuilder();
		if(isPrettyPrinting) gsonBuilder.setPrettyPrinting();
		return gsonBuilder.create().toJson(object);
	}
	
}
