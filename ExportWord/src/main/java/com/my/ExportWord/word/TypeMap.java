package com.my.ExportWord.word;


import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

public class TypeMap {

	private static Map<String, String> javaTypeMap = new HashMap<>();
	static {
		javaTypeMap.put("varchar"		, "String");
		javaTypeMap.put("char"			, "String");
		javaTypeMap.put("text"			, "String");
		javaTypeMap.put("text"			, "String");
		javaTypeMap.put("tinytext"		, "String");
		javaTypeMap.put("mediumtext"	, "String");
		javaTypeMap.put("longtext"		, "String");
		
		javaTypeMap.put("int"			, "Integer");
		javaTypeMap.put("tinyint"		, "Integer");
		javaTypeMap.put("smallint"		, "Integer");
		javaTypeMap.put("mediumint"		, "Integer");
		javaTypeMap.put("bigint"		, "Long");
		
		javaTypeMap.put("decimal"		, "BigDecimal");
		
		javaTypeMap.put("date"			, "Date");
		javaTypeMap.put("datetime"		, "Date");
		javaTypeMap.put("timestamp"		, "Date");
	}

	public static String getJavaType(String key){
		if(StringUtils.isBlank(key)){
			return null;
		}
		
		return javaTypeMap.get(key.toLowerCase());
	}

	// -------------------------------------------------------------------------
	private static Map<String, String> jdbcTypeMap = new HashMap<>();
	static {
		jdbcTypeMap.put("int", "integer");
		jdbcTypeMap.put("text", "longvarchar");
	}

	public static String getJdbcType(String key){
		if(StringUtils.isBlank(key)){
			return null;
		}
		
		String jdbcType = jdbcTypeMap.get(key.toLowerCase());
		
		return StringUtils.isBlank(jdbcType) ? key.toUpperCase() : jdbcType.toUpperCase();
	}

	// -------------------------------------------------------------------------
	public static String getJavaTypeImport(String key){
		
		if("Date".equals(key)){
			return "java.util.Date";
			
		}else if("BigDecimal".equals(key)){
			return "java.math.BigDecimal";
			
		}
		
		return null;
	}

}
