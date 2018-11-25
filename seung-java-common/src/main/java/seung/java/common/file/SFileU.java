package seung.java.common.file;

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import seung.java.common.arguments.SConstants;
import seung.java.common.arguments.SMap;

/**
 * author       seung
 * description  file util
 */
public class SFileU {

	private static final Logger logger = LoggerFactory.getLogger(SFileU.class);
	
	public byte[] readFile(File file) {
		
		byte[] byteArray = null;
		
		InputStream inputStream = null;
		try {
			
			if(file.exists()) {
				inputStream = new FileInputStream(file);
				byteArray = IOUtils.toByteArray(inputStream);
			} else {
				throw new IOException("file [" + file + "] is not exists.");
			}
		} catch (IOException e) {
			logger.error(e.getMessage());
		} finally {
			closeCloseable(inputStream);
		}
		
		return byteArray;
	}
	
	public void writeFile(File file, byte[] data) {
		
		OutputStream outputStream = null;
		
		try {
			outputStream = openFileOutputStream(file);
			if(data != null) outputStream.write(data);
		} catch (IOException e) {
			logger.error(e.getMessage());
		} finally {
			closeCloseable(outputStream);
		}
	}
	
	public FileOutputStream openFileOutputStream(File file) throws IOException {
		
		if(file.exists()) {
			if(file.isDirectory()) throw new IOException("file [" + file + "] is directory.");
			if(!file.canWrite()) throw new IOException("file [" + file + "] can not write.");
		} else {
			File parentFile = file.getParentFile();
			if(parentFile != null && !parentFile.exists()) {
				if(!parentFile.mkdirs()) throw new IOException("file [" + file + "] is not created.");
			}
		}
		
		return new FileOutputStream(file);
	}
	
	public void closeCloseable(Closeable closeable) {
		try {
			if(closeable != null) closeable.close();
		} catch (IOException e) {
			logger.error(e.getMessage());
		}
	}
	
	/**
	 * @param rootFile
	 * @param fileNameMatch
	 * @param fileType     : FILE, DIR
	 * @param depth
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public ArrayList<SFileVO> getFileListInfo(
			File rootFile
			, String fileNameMatch
			, String fileType
			, int depth
			) throws IOException {
		
		if(!rootFile.exists())  throw new IOException("file is not exists.");
		if(!rootFile.canRead()) throw new IOException("file is not readable.");
		
		if(fileType == null || fileType.length() == 0) fileType = "FILEDIR";
		
		ArrayList fileList = new ArrayList<SMap>();
		
		if(rootFile.isFile()) {
			if(fileType.contains(SConstants._S_FILE) && (fileNameMatch == null || fileNameMatch.length() == 0 || rootFile.getPath().toLowerCase().contains(fileNameMatch.toLowerCase()))) {
				fileList.add(new SFileVO(rootFile));
			}
		} else if(rootFile.isDirectory()) {
			if(fileType.contains(SConstants._S_DIRECTORY) && (fileNameMatch == null || fileNameMatch.length() == 0 || rootFile.getPath().toLowerCase().contains(fileNameMatch.toLowerCase()))) fileList.add(new SFileVO(rootFile));
			for(File file : rootFile.listFiles()) {
				if(depth > -1) fileList.addAll(getFileListInfo(file, fileNameMatch, fileType, depth - 1));
			}
		}
		
		return fileList;
	}
	
	/**
	 * @param path
	 * @return
	 * desc extract directory path
	 */
	public String getDirPath(String path) {
		return getDirPath(path, "");
	}
	public String getDirPath(String path, String suf) {
		return path.substring(0, path.lastIndexOf("/")) + suf;
	}
}
