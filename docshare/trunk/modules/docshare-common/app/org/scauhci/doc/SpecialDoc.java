package org.scauhci.doc;

import java.io.File;

/**
 * 抽象的文档类型,可以和models中的Document转换
 * @author shunai
 *
 */
public abstract class SpecialDoc {

	protected long id;
	
	protected String name;
	
	protected String location;
	
	protected String title;
	
	protected String summary;
	
	protected File file;
	
	public void addIndex() {
		
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}
}
