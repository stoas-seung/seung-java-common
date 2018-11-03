package seung.java.common.arguments;

/**
 * author       seung
 * description  seung request parameter map
 */
public class SReqMap {

	private SMap network;
	private SMap session;
	private SMap header;
	private SMap parameter;
	
	public void putNetwork(String key, Object value) {
		if(network == null) network = new SMap();
		network.put(key, value);
	}
	public SMap getNetwork() {
		return network;
	}
	public void putSession(String key, Object value) {
		if(session == null) session = new SMap();
		session.put(key, value);
	}
	public SMap getSession() {
		return session;
	}
	public void putHeader(String key, Object value) {
		if(header == null) header = new SMap();
		header.put(key, value);
	}
	public SMap getHeader() {
		return header;
	}
	public void putParameter(String key, Object value) {
		if(parameter == null) parameter = new SMap();
		parameter.put(key, value);
	}
	public void putParameterAll(SMap sMap) {
		if(parameter == null) parameter = new SMap();
		parameter.putAll(sMap);
	}
	public SMap getParameter() {
		return parameter;
	}
	
}
