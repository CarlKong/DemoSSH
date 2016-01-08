package com.augmentum.ot.action;

import java.util.Date;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import net.sf.json.JSONObject;

import com.augmentum.ot.dataObject.constant.DateFormatConstants;
import com.augmentum.ot.dataObject.constant.JsonKeyConstants;
import com.augmentum.ot.util.DateHandlerUtils;
@Component("serverTimeAction")
@Scope("prototype")
public class ServerTimeAction extends BaseAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = -952769725428707913L;
	public String getCurrentServerTime() {
		Date currentTime = new Date();
		String currentTimeStr = DateHandlerUtils.dateToString(DateFormatConstants.TO_DATABASE, currentTime);
		this.jsonObject = new JSONObject();
		this.jsonObject.put(JsonKeyConstants.CURRENT_TIME, currentTimeStr);
		return SUCCESS;
	}
}
