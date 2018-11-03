package seung.java.common.arguments;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * author       seung
 * description  seung linked hash map
 */
@SuppressWarnings("rawtypes")
public class SMap extends LinkedHashMap {

	private static final long serialVersionUID = 1L;
	
	public SMap() {}
	
	public SMap(Map map) {
		this.putAll(map);
	}
	public SMap(Object object) {
		convertObjectToSMap(object);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void putAll(Map map) {
		if(map != null && !map.isEmpty()) super.putAll(map);
	}
	
	public SMap putMap(Map map) {
		this.putAll(map);
		return this;
	}
	
	@SuppressWarnings("unchecked")
	public SMap put(String key, Object val) {
		super.put(key, val);
		return this;
	}
	
	public void appendString(String key, String val) {
		put(key, getString(key) + val);
	}
	
	public String getString(String key) {
		String val = "";
		if(containsKey(key)) {
			if(get(key).getClass().isArray()) {
				if(get(key) != null) {
					String[] vals = (String[]) get(key);
					val = vals[0];
				}
			} else {
				val = "" + String.valueOf(get(key));
			}
		}
		return val;
	}
	
	public String[] getStringArray(String key) {
		String[] stringArray = null;
		if(containsKey(key)) {
			if(get(key).getClass().isArray()) {
				if(get(key) != null) {
					stringArray = (String[]) get(key);
				}
			} else {
				stringArray = new String[1];
				stringArray[0] = getString(key);
			}
		}
		return stringArray;
	}
	
	public boolean getBoolean(String key) {
		return containsKey(key) ? Boolean.valueOf(getString(key)) : null;
	}
	
	public int getInt(String key) {
		return containsKey(key) ? Integer.parseInt(getString(key)) : null;
	}
	
	public double getDouble(String key) {
		return containsKey(key) ? Double.valueOf(getString(key)) : null;
	}
	
	public long getLong(String key) {
		return containsKey(key) ? Long.valueOf(getString(key)) : null;
	}
	
	public SMap getSMap(String key) {
		return (SMap) get(key);
	}
	
	public List getList(String key) {
		return (List) get(key);
	}
	
	@SuppressWarnings("unchecked")
	public List<SMap> getListSMap(String key) {
		return (List<SMap>) get(key);
	}
	
	@SuppressWarnings("unchecked")
	public ArrayList<SMap> getArrayListSMap(String key) {
		return (ArrayList<SMap>) get(key);
	}
	
	public SMap convertObjectToSMap(Object object) {
		Field[] fields = object.getClass().getDeclaredFields();
		try {
			for(Field field : fields) {
				field.setAccessible(true);
				this.put(field.getName(), field.get(object));
			}
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			return null;
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			return null;
		}
		return this;
	}
	
	@SuppressWarnings("unchecked")
	public Object convertSMapToObject(Object object) {
		
		String key = "";
		String methodName = "";
		
		Iterator<String> iterator = this.keySet().iterator();
		Method[] methods = null;
		while(iterator.hasNext()) {
			
			key = (String) iterator.next();
			methodName = "set" + key.substring(0, 1).toUpperCase() + key.substring(1);
			
			methods = object.getClass().getDeclaredMethods();
			try {
				for(Method method : methods) {
					if(methodName.equals(method.getName())) {
						if(this.get(key) != null) method.invoke(object, this.get(key));
						else method.invoke(object, "");
					}
				}
			} catch (IllegalAccessException e) {
				e.printStackTrace();
				return null;
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
				return null;
			} catch (InvocationTargetException e) {
				e.printStackTrace();
				return null;
			}
		}
		
		return object;
	}
	
	public String toString() {
		return toString(false);
	}
	
	public String toString(boolean isPrettyPrinting) {
		GsonBuilder gsonBuilder = new GsonBuilder();
		if(isPrettyPrinting) gsonBuilder.setPrettyPrinting();
		Gson gson = gsonBuilder.disableHtmlEscaping().create();
		return gson.toJson(this);
	}
}
