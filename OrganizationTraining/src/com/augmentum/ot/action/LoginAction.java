package com.augmentum.ot.action;

import java.math.BigInteger;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.augmentum.ot.dataObject.constant.JsonKeyConstants;
import com.augmentum.ot.exception.DataWarningException;
import com.augmentum.ot.exception.ServerErrorException;
import com.augmentum.ot.model.Employee;
import com.augmentum.ot.service.EmployeeService;
import com.augmentum.ot.service.PrivilegeService;
import com.augmentum.ot.util.BeanFactory;

@Component("loginAction")
@Scope("prototype")
public class LoginAction extends BaseAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7316035014763464024L;
	
    private static Logger loger = Logger.getLogger(LoginAction.class);
    
	private String employeeName;
	
	private String employeePassword;
	
	private EmployeeService employeeService;
	
	public String login() {
		String loginIp = ServletActionContext.getRequest().getRemoteAddr();
		loger.info("User: " + employeeName + "   IP: " + loginIp + "  try to login OT system.");
		BigInteger userPrivilegeValue = new BigInteger("0", 2);
        PrivilegeService privilegeService = BeanFactory.getPrivilegeService();
		try {
			Employee loginUser = employeeService.findEmployeeForLogin(employeeName, employeePassword);
			List<String> roleNames = null;
			if (loginUser != null) {
				userPrivilegeValue = privilegeService.createAuthorityValueForEmployee(employeeName);
				roleNames = employeeService.getRoleListNames(loginUser);
				HttpSession httpSession = ServletActionContext.getRequest().getSession();
				httpSession.setAttribute(JsonKeyConstants.AUTHORITY_VALUE, userPrivilegeValue);
				httpSession.setAttribute(JsonKeyConstants.LOGIN_EMPLOYEE, loginUser);
				httpSession.setAttribute(JsonKeyConstants.HAS_LOGIN, true);
				httpSession.setAttribute(JsonKeyConstants.EMPLOYEE_ROLE_NAMES, roleNames);
				loger.info("User: " + employeeName + "   IP: " + loginIp + "  login OT system successfully.");
			} else {
				request.put("loginMessage", getText("informationNotInDatabase"));
				loger.info("User: " + employeeName + "   IP: " + loginIp + "  login OT system failed.");
				return ERROR;
			}
		} catch (ServerErrorException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DataWarningException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return SUCCESS;
	}
	   public String logOut() {
	       HttpSession httpSession = ServletActionContext.getRequest().getSession();
	       if(httpSession != null){
	           httpSession.invalidate();
	       }
	        return SUCCESS;
	    }

	public String getEmployeeName() {
		return employeeName;
	}

	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}

	public String getEmployeePassword() {
		return employeePassword;
	}

	public void setEmployeePassword(String employeePassword) {
		this.employeePassword = employeePassword;
	}

	@Resource
	public void setEmployeeService(EmployeeService employeeService) {
		this.employeeService = employeeService;
	}
	
}
