package org.scauhci.doc;

/**
 * 纯文本文档
 * @author shunai
 *
 */
public class TextDoc extends SpecialDoc {

	/**
	 * 解析逻辑
	 * @return
	 */
	public TextDoc parser() {
		// 文本不用解析，直接返回
		return this;
	} 
	
	
}
