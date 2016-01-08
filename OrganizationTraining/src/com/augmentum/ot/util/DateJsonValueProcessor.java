package com.augmentum.ot.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import net.sf.json.JsonConfig;
import net.sf.json.processors.JsonValueProcessor;

public class DateJsonValueProcessor implements JsonValueProcessor {
	public static final String DEFAULT_DATE_PATTERN = "yyyy-MM-dd";   
    private DateFormat dateFormat;
    public DateJsonValueProcessor(String datePattern) {   
        try {   
            dateFormat = new SimpleDateFormat(datePattern);   
        } catch (Exception ex) {   
            dateFormat = new SimpleDateFormat(DEFAULT_DATE_PATTERN);   
        }   
    }   

	public Object processArrayValue(Object arg0, JsonConfig arg1) {
		return process(arg0);   
	}

	public Object processObjectValue(String arg0, Object arg1, JsonConfig arg2) {
		return process(arg1);
	}
	
	private Object process(Object value) {   
	    if(null == value) {
	        value = "";
	        return value;
	    }
        return dateFormat.format((Date) value);
    }   

}
