package com.my.ExportWord.word;

import java.io.Serializable;

import org.apache.commons.lang3.StringUtils;

public class TableDto implements Serializable {

	private static final long serialVersionUID = -3798944493017559973L;

	private String field;
	private String fieldJava;
	private String fieldPropertySet;
	private String fieldPropertyGet;
	private String type;
	private String typeJava;
	private String key;
	private boolean isPrimaryKey;
	private String null_;
	private String default_;
	private String comment;

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

	public String getFieldJava() {
		
		if(StringUtils.isBlank(field)){
			return field;
		}
		
		int index = field.indexOf("_");
		if(index < 1){
			return field;
		}
		
		StringBuilder sb = new StringBuilder();
		char[] array = field.toCharArray();
		int arrayLen = array.length;
		
		for(int i = 0; i < arrayLen; i++){
			
			char c = array[i];
			if(c == '_'){
				sb.append(Character.toUpperCase(array[++i]));
			}else{
				sb.append(c);
			}
			
		}
		
		return sb.toString();
	}

	public String getFieldPropertySet() {
		
		String fj = getFieldJava();
		if(StringUtils.isBlank(fj)){
			return null;
		}
		
		char[] array = fj.toCharArray();
        array[0] -= 32;
		
		return "set" + String.valueOf(array);
	}

	public String getFieldPropertyGet() {
		
		String fj = getFieldJava();
		if(StringUtils.isBlank(fj)){
			return null;
		}
		
		char[] array = fj.toCharArray();
        array[0] -= 32;
		
		return "get" + String.valueOf(array);
	}

	public String getType() {
		return TypeMap.getJdbcType(type);
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getTypeJava() {
		return TypeMap.getJavaType(type);
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public boolean isPrimaryKey() {
		return "PRI".equalsIgnoreCase(key);
	}

	public String getNull_() {
		return "YES".equalsIgnoreCase(null_) ? "" : "是";
	}

	public void setNull_(String null_) {
		this.null_ = null_;
	}

	public String getDefault_() {
		return default_;
	}

	public void setDefault_(String default_) {
		this.default_ = default_;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

}
