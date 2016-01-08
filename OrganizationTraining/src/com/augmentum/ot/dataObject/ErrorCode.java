package com.augmentum.ot.dataObject;
import static com.augmentum.ot.util.ReaderXmlUtils.getErrorCodes;

import java.util.Map;
public class ErrorCode {
	private String errorMessageKey;
	private String logMessage;
	private String flag;
	private String errorCodeId;
	private String errorMessage;
	private static Map<String,ErrorCode> errorCodes;
	
	public String getErrorMessageKey() {
		return errorMessageKey;
	}
	public void setErrorMessageKey(String errorMessageKey) {
		this.errorMessageKey = errorMessageKey;
	}
	public String getLogMessage() {
		return logMessage;
	}
	public void setLogMessage(String logMessage) {
		this.logMessage = logMessage;
	}
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}
	public String getErrorCodeId() {
		return errorCodeId;
	}
	public void setErrorCodeId(String errorCodeId) {
		this.errorCodeId = errorCodeId;
	}
	
	public String getErrorMessage() {
		return errorMessage;
	}
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	public static String getLoggerInfo(String errorCodeId){
		if(errorCodes==null){
			errorCodes=getErrorCodes();
			ErrorCode errorCode=errorCodes.get(errorCodeId);
			return "<ErrorLevel:>"+errorCode.getFlag()+
			"<ErrorMessage:>"+errorCode.getErrorMessageKey()+
			"<ErrorCode:>"+errorCode.getErrorCodeId();
		}else{
			ErrorCode errorCode=errorCodes.get(errorCodeId);
			return "<ErrorLevel:>"+errorCode.getFlag()+
			"<ErrorMessage:>"+errorCode.getErrorMessageKey()+
			"<ErrorCode:>"+errorCode.getErrorCodeId();
		}
	}
	
}
