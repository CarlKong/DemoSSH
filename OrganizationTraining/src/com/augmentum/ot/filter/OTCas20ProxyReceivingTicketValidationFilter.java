package com.augmentum.ot.filter;

import java.math.BigInteger;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.jasig.cas.client.validation.Assertion;
import org.jasig.cas.client.validation.Cas20ProxyReceivingTicketValidationFilter;

import com.augmentum.ot.dataObject.constant.JsonKeyConstants;
import com.augmentum.ot.exception.ServerErrorException;
import com.augmentum.ot.model.Employee;
import com.augmentum.ot.service.EmployeeService;
import com.augmentum.ot.service.PrivilegeService;
import com.augmentum.ot.util.BeanFactory;

public class OTCas20ProxyReceivingTicketValidationFilter extends Cas20ProxyReceivingTicketValidationFilter {

	@Override
	protected void onSuccessfulValidation(HttpServletRequest request,
			HttpServletResponse response, Assertion assertion) {
		EmployeeService employeeService = BeanFactory.getEmployeeService();
		String userName = assertion.getPrincipal().getName();
		Employee employee = null;
		List<String> roleNames = null;
        if (userName != null) {
        	userName = userName.replace(".", " ");
            try {
				employee = employeeService.saveAndInitLoginEmployee(userName, request);
				roleNames = employeeService.getRoleListNames(employee);
				
			} catch (ServerErrorException e) {
				//go 500Page
			}
        }
        BigInteger userPrivilegeValue = new BigInteger("0", 2);
        PrivilegeService privilegeService = BeanFactory.getPrivilegeService();
        try {
            userPrivilegeValue = privilegeService.createAuthorityValueForEmployee(userName);
          } catch (ServerErrorException e) {
        	  
        }
        HttpSession session=request.getSession();
        session.setAttribute(JsonKeyConstants.AUTHORITY_VALUE, userPrivilegeValue);
        session.setAttribute(JsonKeyConstants.LOGIN_EMPLOYEE, employee);
        session.setAttribute(JsonKeyConstants.HAS_LOGIN, true);
        session.setAttribute(JsonKeyConstants.EMPLOYEE_ROLE_NAMES, roleNames);
	}
	
	

}
