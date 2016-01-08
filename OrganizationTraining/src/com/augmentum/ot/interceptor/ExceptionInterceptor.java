package com.augmentum.ot.interceptor;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import com.augmentum.ot.dataObject.constant.FlagConstants;
import com.augmentum.ot.dataObject.constant.LogConstants;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.ExceptionMappingInterceptor;

public class ExceptionInterceptor extends ExceptionMappingInterceptor{

	private static final long serialVersionUID = -3837674457299306885L;
	private static final Logger logger = Logger.getLogger(ExceptionInterceptor.class);
	@Override
	public String intercept(ActionInvocation arg0) throws Exception {
		try {
			return arg0.invoke();
		}catch(Exception e) {
			HttpServletRequest request = ServletActionContext.getRequest();
			String requestType=request.getHeader("X-Requested-With");
			logger.error(LogConstants.exceptionMessage("Exception Interceptor, " +
					" and request type [" + requestType + "] "),e);
			if (requestType != null && requestType.equals("XMLHttpRequest")) {
				return "ajax_server_error";
			}else {
				return FlagConstants.ERROR_500;
			}
		}
		
	}
}
