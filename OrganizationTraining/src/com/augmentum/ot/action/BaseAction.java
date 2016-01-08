package com.augmentum.ot.action;

import java.util.Map;
import javax.servlet.ServletContext;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.RequestAware;
import org.apache.struts2.interceptor.SessionAware;
import org.apache.struts2.json.annotations.JSON;

import com.augmentum.ot.dataObject.ErrorCode;
import com.augmentum.ot.dataObject.constant.ErrorCodeConstants;
import com.augmentum.ot.dataObject.constant.FlagConstants;
import com.augmentum.ot.dataObject.constant.LogConstants;
import com.augmentum.ot.exception.DataWarningException;
import com.augmentum.ot.exception.ServerErrorException;
import com.opensymphony.xwork2.ActionSupport;
/**
 * 
 * Base action
 *
 * @version V1.0, 2012-8-27
 */
public abstract class BaseAction extends ActionSupport implements 
		RequestAware, SessionAware {
	private Logger logger = Logger.getLogger(BaseAction.class);
	private static final long serialVersionUID = 1430814461569166462L;
	
	protected Map<String, Object> request;
	protected Map<String, Object> session;
	
	protected JSONObject jsonObject;
	protected JSONArray  jsonArray;
	
	/**
	 * 
	 * @Title: Return the error code according to ServerErrorException.
	 *
	 * @param e
	 * @param isForAjax
	 * @return
	 */
	@SuppressWarnings("unchecked")
    @JSON(serialize = false)
    public String handleExceptionByServerErrorException(ServerErrorException e, boolean isForAjax) {
        String errorCode = e.getMessage();
        if (null == errorCode || "".equals(errorCode)) {
        	errorCode = ErrorCodeConstants.SERVER_ERROR;
        }
        if (isForAjax) {
            // Make the JSON object.
            ServletContext context = ServletActionContext.getServletContext();
        	Map<String, ErrorCode> errorCodeMap = (Map<String, ErrorCode>) context.getAttribute(FlagConstants.ERROR_CODE_MAP);
            ErrorCode errorCodeEntity = errorCodeMap.get(errorCode);
            jsonObject = JSONObject.fromObject(errorCodeEntity);
            if (errorCode.equals(ErrorCodeConstants.SERVER_ERROR)){
            	return FlagConstants.ERROR_SERVER_JSON;
            }else {
            	return FlagConstants.VALIDATION_ERROR_JSON;
            }
            
        }else{
        	if (errorCode.equals(ErrorCodeConstants.SERVER_ERROR)) {
        		return FlagConstants.ERROR_SERVER;
        	}else {
        		return FlagConstants.VALIDATION_ERROR;
        	}
        }
    }

    /**
     * 
     * @Title: Return the error code according to ServerErrorException.
     *
     * @param e
     * @param isForAjax
     * @return
     */
	@SuppressWarnings("unchecked")
    public String handleExceptionByDataWarningException(DataWarningException e, boolean isForAjax) {
    	String errorCode = e.getMessage();
    	ServletContext context = ServletActionContext.getServletContext();
    	Map<String, ErrorCode> errorCodeMap = (Map<String, ErrorCode>) context.getAttribute(FlagConstants.ERROR_CODE_MAP);
        ErrorCode errorCodeEntity = errorCodeMap.get(errorCode);
        String message = errorCodeEntity.getErrorMessageKey();
        errorCodeEntity.setErrorMessage(getText(message));
    	if (isForAjax) {
            jsonObject = JSONObject.fromObject(errorCodeEntity);
            return FlagConstants.DATA_WARNING_JSON;
    	}else {
    		this.addActionMessage(errorCodeEntity.getErrorMessage());
    		return FlagConstants.DATA_WARNING;
    	}
    }
    
	@SuppressWarnings("unchecked")
    public String handleValidateExceptionInAction(String userName) {
    	ServletContext context = ServletActionContext.getServletContext();
    	Map<String, ErrorCode> errorCodeMap = (Map<String, ErrorCode>) context.getAttribute(FlagConstants.ERROR_CODE_MAP);
        ErrorCode errorCodeEntity = errorCodeMap.get(ErrorCodeConstants.SERVER_VALIDATION_ERROR);
    	jsonObject = JSONObject.fromObject(errorCodeEntity);
    	logger.error(LogConstants.getValidationMsg(userName));
    	return FlagConstants.VALIDATION_ERROR_JSON;
    }
    
	@Override
	public void setRequest(Map<String, Object> request) {
		this.request = request;
	}

	@Override
	public void setSession(Map<String, Object> session) {
		this.session = session;
	}

	public JSONObject getJsonObject() {
		return jsonObject;
	}

	public void setJsonObject(JSONObject jsonObject) {
		this.jsonObject = jsonObject;
	}

	public JSONArray getJsonArray() {
		return jsonArray;
	}

	public void setJsonArray(JSONArray jsonArray) {
		this.jsonArray = jsonArray;
	}
}
