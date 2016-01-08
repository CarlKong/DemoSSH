package com.augmentum.ot.dataObject.constant;

import java.util.Date;

import com.augmentum.ot.util.DateHandlerUtils;

/**
 * DateFormatConstants
 * includes:
 * 1/ date format
 * 2/ day constants
 * 3/ default large date
 */
public abstract class DateFormatConstants {

	/**date format*/
	public static final String DEFAULT_FORMAT_DATE = "yyyy/MM/dd HH:mm:ss";
	public static final String YY_MM_DD = "yyyy/MM/dd";
	public static final String TO_DATABASE = "yyyy-MM-dd HH:mm:ss";
	public static final String YYYY_MM_DD = "yyyyMMdd";
	public static final String FULL_TIME_STR = "yyyyMMddHHmmss";
	public static final String HH_MM = "HH:mm";
	public static final String YYYY_MM_DD_ = "yyyy-MM-dd";
	public static final String MM_DD_ = "MM dd";
	public static final String YYYY_MM_DD_HH_MM = "yyyy/MM/dd HH:mm";

	/**day constants*/
	public static int DAYS = 8;
	public static int EXPIRED_DEFAULT_DAY = 7;

	/**default large date*/
	public static final Date DEFAULT_LARGE_DATE = DateHandlerUtils.formatDate(
			"2999/12/31 01:01:01", DateFormatConstants.DEFAULT_FORMAT_DATE);
	
	public static final String LATETES_TIME_IN_ONE_DAY = "235959";
	
	public static final String LARGE_TIME = "99991230235959";
	
}
