package org.scauhci.doc;

public enum DocEnum {

	txt(1, "文本"), word(2, "word"), pdf(3, "pdf");
	
	public int value;
	
	public String name;
	
	DocEnum(int value, String name) {
		this.value = value;
		this.name = name;
	}
	
	public static DocEnum getType(int value) {
		if(txt.value == value) {
			return txt;
		}
		
		if(word.value == value) {
			return word;
		}
		
		if(pdf.value == value) {
			return pdf;
		}
		// 其他值为不支持格式
		return null;
	} 
}
