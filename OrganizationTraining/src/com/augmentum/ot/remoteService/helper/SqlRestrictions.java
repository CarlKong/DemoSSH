package com.augmentum.ot.remoteService.helper;

import java.util.Collection;

/**
 * 
 * Make up the SQL statement
 *
 * @version V1.0, 2012-7-26
 */
public abstract class SqlRestrictions {
	
	public static String eq(String propertyName, Object value) {
		String eqSql = " " + propertyName + "=" + "'" + value + "'";
		return eqSql;
	}
	
	public static String ne(String propertyName, Object value) {
		String neSql = " " + propertyName + "<>" + "'" + value + "'";
		return neSql;
	}
	
	public static String gt(String propertyName, Object value) {
		String gtSql = " " + propertyName + ">" + value;
		return gtSql;
	}
	
	public static String lt(String propertyName, Object value) {
		String ltSql = " " + propertyName + "<" + value;
		return ltSql;
	}
	
	public static String le(String propertyName, Object value) {
		String leSql = " " + propertyName + "<=" + value;
		return leSql;
	}
	
	public static String ge(String propertyName, Object value) {
		String geSql = " " + propertyName + ">=" + value;
		return geSql;
	}
	
	public static String like(String propertyName, Object value) {
		String likeSql = " " + propertyName + " like " + "'%" + value + "%'";
		return likeSql;
	}
	
	public String bettween(String propertyName, Object lo, Object hi) {
		String bettweenSql = " " + propertyName + " bettween " + lo + " and " + hi;
		return bettweenSql;
	}
	
	public static String and() {
		return " and ";
	}
	
	public static String or() {
		return " or ";
	}
	
	public static String in(String field, Collection<?> arrays) {
		String inSql = field + " in(";
		StringBuilder builder = new StringBuilder();
		for (Object obj : arrays) {
			if (obj instanceof String) {
				builder.append("'"+obj+"'");
				builder.append(",");
			}else {
				builder.append(obj);
				builder.append(",");
			}
		}
		inSql += builder.substring(0, builder.length() - 1) + ")";
		return inSql;
	}
}
