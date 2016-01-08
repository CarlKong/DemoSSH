package com.augmentum.ot.util;

import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONObject;

import com.augmentum.ot.dataObject.ErrorCode;
import com.augmentum.ot.dataObject.constant.FlagConstants;
import com.augmentum.ot.dataObject.constant.JsonKeyConstants;
import com.augmentum.ot.exception.DataWarningException;
import com.augmentum.ot.exception.ServerErrorException;

/**
 * Provide some methods about dealing exception.
 * 
 * @version 0.1 2012-10-22
 *
 */
public abstract class ExceptionUtils {

    /**
     * Deal with the server error exception.
     * 
     * @param e
     * @param isForAjax
     * @return  The map contains errorCodeResult and jsonObject.
     */
    public static Map<String, Object> getErrorCodeForServerErrorException(ServerErrorException e, boolean isForAjax) {
        JSONObject jsonObject = null;
        String errorCodeResult = null;
        String errorCode = e.getMessage();
        if (isForAjax) {
            // Make the JSON object.
            ErrorCode errorCodeEntity = ReaderXmlUtils.getErrorCodes().get("E0001");
            errorCodeEntity.setFlag("1");
            jsonObject = JSONObject.fromObject(errorCodeEntity);
        }
        if (errorCode == null || errorCode.isEmpty()) {
            errorCodeResult = FlagConstants.ERROR_500;
        } else if (FlagConstants.ERROR_SERVER.equals(errorCode)) {
            // This exception is because the server error.
            errorCodeResult = FlagConstants.ERROR_500;
        } else if (FlagConstants.VALIDATION_ERROR.equals(errorCode)) {
            // This exception is because the validation error of condition.
            errorCodeResult = FlagConstants.VALIDATION_ERROR;
        } else {
            errorCodeResult = FlagConstants.ERROR_500;
        }
        
        Map<String, Object> errorCodeMap = new HashMap<String, Object>();
        errorCodeMap.put(JsonKeyConstants.ERROR_CODE_RESULT, errorCodeResult);
        errorCodeMap.put(JsonKeyConstants.JSON_OBJECT, jsonObject);
        
        return errorCodeMap;
    }
    
    /**
     * Deal with the server error exception.
     * 
     * @param dataWarningException
     * @param isForAjax
     * @return  The map contains errorCodeResult and jsonObject.
     */
    public static Map<String, Object> getErrorCodeForDataWarningException(DataWarningException dataWarningException, boolean isForAjax) {
        JSONObject jsonObject = null;
        String errorCodeResult = null;
        
        Map<String, Object> errorCodeMap = new HashMap<String, Object>();
        errorCodeMap.put(JsonKeyConstants.ERROR_CODE_RESULT, errorCodeResult);
        errorCodeMap.put(JsonKeyConstants.JSON_OBJECT, jsonObject);
        
        return errorCodeMap;
    }
    
}
