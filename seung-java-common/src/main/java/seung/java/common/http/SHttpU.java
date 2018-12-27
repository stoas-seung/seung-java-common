package seung.java.common.http;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.commons.io.IOUtils;

/**
 * author       seung
 * description  http util
 */
public class SHttpU {

	public SHttpVO httpRequest(SHttpVO sHttpVO) {
		
		boolean           isPost            = "POST".equals(sHttpVO.getRequestMethod()) ? true : false;
		HttpURLConnection httpURLConnection = null;
		OutputStream      outputStream      = null;
		
		try {
			
			httpURLConnection = (HttpURLConnection) new URL(sHttpVO.getRequestUrl()).openConnection();
			
			for(String[] header : sHttpVO.getRequestHeaders()) {
				httpURLConnection.setRequestProperty(header[0], header[1]);
			}
			
			if(isPost) {
				httpURLConnection.setRequestProperty("Content-Length", "" + sHttpVO.getContentLength());
			}
			
			httpURLConnection.setRequestMethod(sHttpVO.getRequestMethod());
			httpURLConnection.setConnectTimeout(sHttpVO.getConnectionTimeout());
			httpURLConnection.setReadTimeout(sHttpVO.getReadTimeout());
			httpURLConnection.setDoInput(true);
			httpURLConnection.setDoOutput(true);
			
			outputStream = new BufferedOutputStream(httpURLConnection.getOutputStream());
			if(isPost) {
				outputStream.write(sHttpVO.getQueryString().getBytes(sHttpVO.getRequestEncoding()));
			}
			outputStream.flush();
			
			sHttpVO.setResponseCode(httpURLConnection.getResponseCode());
			sHttpVO.setContentType(httpURLConnection.getContentType());
			sHttpVO.setContentLength(httpURLConnection.getContentLength());
			sHttpVO.setContentDisposition(httpURLConnection.getHeaderField("Content-Disposition"));
			sHttpVO.setResponse(IOUtils.toByteArray(httpURLConnection.getInputStream()));
			
		} catch (IOException e) {
			sHttpVO.setExceptionMessage(e.getMessage());
		} finally {
			try {
				if(outputStream != null) outputStream.close();
				if(httpURLConnection != null) httpURLConnection.disconnect();
			} catch (IOException e) {
				sHttpVO.setExceptionMessage(e.getMessage());
			}
		}
		
		return sHttpVO;
	}
	
}
