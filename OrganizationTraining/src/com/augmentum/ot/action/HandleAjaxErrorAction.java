package com.augmentum.ot.action;

import static com.augmentum.ot.util.ReaderXmlUtils.getErrorCodes;

import java.util.Map;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import net.sf.json.JSONObject;

import com.augmentum.ot.dataObject.ErrorCode;
import com.augmentum.ot.dataObject.constant.ErrorCodeConstants;

@Component("handleAjaxErrorAction")
@Scope("prototype")
public class HandleAjaxErrorAction extends BaseAction {
	private static final long serialVersionUID = 1646581118247496626L;
	
	@Override
	public String execute(){
		Map<String,ErrorCode> errorCodes=getErrorCodes();
		ErrorCode errorCode=errorCodes.get(ErrorCodeConstants.SERVER_ERROR);
		jsonObject=JSONObject.fromObject(errorCode);
		return SUCCESS;
	}

}
