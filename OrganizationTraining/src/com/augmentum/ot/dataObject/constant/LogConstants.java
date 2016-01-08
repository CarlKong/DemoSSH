package com.augmentum.ot.dataObject.constant;

import java.io.PrintWriter;
import java.io.StringWriter;

import com.augmentum.ot.util.ReaderXmlUtils;

public class LogConstants {
	// Replace constant.
	private static final String USER_NAME = "_user_name";
	private static final String METHOD_NAME = "_method_name";
	
	private static final String REPLACE_OBJECT = "_object";
	private static final String REPLACE_DO = "_do";
	private static final String REPLACE_PLAN_ID = "_plan_id";
	private static final String REPLACE_COURSE_ID = "_course_id";
	private static final String REPLACE_PLAN_COURSE_ID = "_plan_course_id";
	private static final String REPLACE_ACTUAL_COURSE_ID = "_actual_course";
	private static final String REPLACE_AUTHOR = "_author_name";
	
	private static String INFO_MESSAGE = "_user_name enters _method_name";
	private static String WARN_MESSAGE_VALIDATION = "_user_name input invalidate data.";
	private static String DEBUG_INPUT = "_do Input  [_object]";
	private static String DEBUG_OUTPUT = "_do Output [_object]";
	private static String EXCEPTION_MESSAGE = "_do appeard exception: ";
	
	private static final String OBJECT_IS_NULL_OR_EMPTY = "_object is null or is empty";
	    
	
	/**
     * get log info message
     * 
     * @param userName
     * @param methodName
     * @return
     */
    public static String getInfo(String userName, String methodName) {
        String message = INFO_MESSAGE.replaceAll(USER_NAME, userName);
        message = message.replaceAll(METHOD_NAME, methodName);
        return message;
    }
    
    /**
     * get validation warn massage.
     * @param userName
     * @return
     */
    public static String getValidationMsg(String userName) {
    	String message = WARN_MESSAGE_VALIDATION.replaceAll(USER_NAME, userName);
    	return message;
    }
    
    /**
     * 
     * @param objectName
     * @param object
     * @return
     */
    public static String getDebugInput(String doWhat, Object object) {
    	return DEBUG_INPUT.replace(REPLACE_DO, doWhat).replace(REPLACE_OBJECT, object.toString());
    }
    
    public static String getDebugOutput(String doWhat, Object object) {
    	return DEBUG_OUTPUT.replace(REPLACE_DO, doWhat).replace(REPLACE_OBJECT, object.toString());
    }
    
    public static String exceptionMessage(String dowhat){
    	return EXCEPTION_MESSAGE.replace(REPLACE_DO, dowhat);
    }
    
    public static String objectIsNULLOrEmpty(String name) {
    	return OBJECT_IS_NULL_OR_EMPTY.replace(REPLACE_OBJECT, name);
    }
    
    public static String getLogPlanByErrorCode(String errorCode, Object value){
    	return getLogByErrorCode(errorCode, REPLACE_PLAN_ID, value);
    }
    
    public static String getLogCourseByErrorCode(String errorCode, Object value){
    	return getLogByErrorCode(errorCode, REPLACE_COURSE_ID, value);
    }
    
    public static String getLogPlanCourseByErrorCode(String errorCode, Object value){
    	return getLogByErrorCode(errorCode, REPLACE_PLAN_COURSE_ID, value);
    }
    
    public static String getLogAuthorByErrorCode(String errorCode, Object value){
    	return getLogByErrorCode(errorCode, REPLACE_AUTHOR, value);
    }
    
    public static String getLogActualCourseByErrorCode(String errorCode, Object value){
    	return getLogByErrorCode(errorCode, REPLACE_ACTUAL_COURSE_ID, value);
    }
    
    public static String getLogByErrorCode(String errorCode, String replaceField, Object value) {
    	String message = ReaderXmlUtils.getErrorCodes().get(errorCode).getLogMessage();
    	if (replaceField != null && value != null) {
    		return message.replace(replaceField, value.toString());
    	}else {
    		return message;
    	}
    }
    
    public static String pureMessage(String message) {
    	return message;
    }
    
    public static String message(String message, Object parameter) {
    	return message + " [" + (parameter == null ? null : parameter.toString()) +"] ";
    }
    
    /**
     * 
     * @Title: getExceptionStackTrace  
     * @Description: print staclTrace to log file
     *
     * @param e
     * @return
     */
    public static String getExceptionStackTrace(Exception e) {
    	 StringWriter sw = new StringWriter();
         e.printStackTrace(new PrintWriter(sw, true));
         return sw.toString();
    }
}
