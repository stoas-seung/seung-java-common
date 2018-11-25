package seung.java.common.file;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributeView;
import java.text.SimpleDateFormat;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import seung.java.common.arguments.SConstants;

/**
 * author       seung
 * description  file value object
 */
public class SFileVO implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String fileType;
	private String filePath;
	private String fileParent;
	private String fileName;
	private String fileExtension;
	private String fileSize;
	private String fileLastModified;
	private String fileCreationTime;
	
	private String canonicalPath;
	private String path;
	private String parent;
	private String name;
	private long   lastModified;
	private long   creationTime;
	private long   length;
	
	public SFileVO() {}
	public SFileVO(String path) throws IOException {
		setFile(path);
	}
	public SFileVO(File file) throws IOException {
		setFile(file);
	}
	
	private void setFile(String path) throws IOException {
		setFile(new File(path));
	}
	private void setFile(File file) throws IOException {
		
		if(!file.exists()) throw new FileNotFoundException();
		
		setCanonicalPath(file.getCanonicalPath());
		setPath(file.getPath());
		setParent(file.getParent());
		setName(file.getName());
		setLastModified(file.lastModified());
		setCreationTime(Files.getFileAttributeView(Paths.get(file.getCanonicalPath()), BasicFileAttributeView.class).readAttributes().creationTime().toMillis());
		setLength(file.length());
		
		if(file.isDirectory()) setFileType(SConstants._S_DIRECTORY);
		else if(file.isFile()) setFileType(SConstants._S_FILE);
		else                   setFileType("");
		
		setFilePath(getCanonicalPath().replace("\\", "/"));
		setFileParent(getParent().replace("\\", "/"));
		
		if(file.isFile() && getName().indexOf(".") > -1) {
			setFileExtension(getName().substring(getName().lastIndexOf(".") + 1));
			setFileName(getName().replace(getName().substring(getName().lastIndexOf(".")), ""));
		} else {
			setFileExtension("");
			setFileName(getName());
		}
		
		
		setFileSize(Long.toString(getLength()));
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		setFileLastModified(simpleDateFormat.format(getLastModified()));
		setFileCreationTime(simpleDateFormat.format(getCreationTime()));
	}
	
	public String getFileType() {
		return fileType;
	}
	public void setFileType(String fileType) {
		this.fileType = fileType;
	}
	public String getFilePath() {
		return filePath;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	public String getFileParent() {
		return fileParent;
	}
	public void setFileParent(String fileParent) {
		this.fileParent = fileParent;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getFileExtension() {
		return fileExtension;
	}
	public void setFileExtension(String fileExtension) {
		this.fileExtension = fileExtension;
	}
	public String getFileSize() {
		return fileSize;
	}
	public void setFileSize(String fileSize) {
		this.fileSize = fileSize;
	}
	public String getFileLastModified() {
		return fileLastModified;
	}
	public void setFileLastModified(String fileLastModified) {
		this.fileLastModified = fileLastModified;
	}
	public String getFileCreationTime() {
		return fileCreationTime;
	}
	public void setFileCreationTime(String fileCreationTime) {
		this.fileCreationTime = fileCreationTime;
	}
	public String getCanonicalPath() {
		return canonicalPath;
	}
	public void setCanonicalPath(String canonicalPath) {
		this.canonicalPath = canonicalPath;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public String getParent() {
		return parent;
	}
	public void setParent(String parent) {
		this.parent = parent;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public long getLastModified() {
		return lastModified;
	}
	public void setLastModified(long lastModified) {
		this.lastModified = lastModified;
	}
	public long getCreationTime() {
		return creationTime;
	}
	public void setCreationTime(long creationTime) {
		this.creationTime = creationTime;
	}
	public long getLength() {
		return length;
	}
	public void setLength(long length) {
		this.length = length;
	}
	
	public String toString(String type) {
		return ToStringBuilder.reflectionToString(this, "JSON".equals(type) ? ToStringStyle.JSON_STYLE : ToStringStyle.MULTI_LINE_STYLE);
	}
}
