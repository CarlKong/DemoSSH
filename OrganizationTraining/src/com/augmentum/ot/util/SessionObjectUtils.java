package com.augmentum.ot.util;

import java.util.List;

import com.augmentum.ot.dataObject.constant.JsonKeyConstants;
import com.augmentum.ot.model.Employee;
import com.opensymphony.xwork2.ActionContext;

public abstract class SessionObjectUtils {

    public static Employee getEmployeeFromSession() {
        return (Employee) getObjectFromSession(JsonKeyConstants.LOGIN_EMPLOYEE);
    }
    
    @SuppressWarnings("unchecked")
	public static List<String> getRoleNamesFromSession() {
    	return (List<String>)getObjectFromSession(JsonKeyConstants.EMPLOYEE_ROLE_NAMES);
    }
    
    protected static Object getObjectFromSession(String key) {
    	return ActionContext.getContext().getSession().get(key);
    }
}
