package com.augmentum.ot.interceptor;

import java.math.BigInteger;
import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.ServletContext;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import com.augmentum.ot.dataObject.constant.FlagConstants;
import com.augmentum.ot.dataObject.constant.JsonKeyConstants;
import com.augmentum.ot.dataObject.constant.LogConstants;
import com.augmentum.ot.util.MathOperationUtils;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;


/** 
* @ClassName: AuthorityInterceptor 
* @date 2012-8-7 
* @version V1.0 
*/
public class AuthorityInterceptor extends AbstractInterceptor{

    private static final long serialVersionUID = 1L;
    private Logger logger = Logger.getLogger(AuthorityInterceptor.class);
    private static Map<String, Integer> authorityValueMap;
    
    static{
    	authorityValueMap = Collections.synchronizedSortedMap(new TreeMap<String, Integer>());
    }

    /** 
    * 
    * @Description: intercept action
    * @date 2012-8-7 
    * @version V1.0 
    */
    @SuppressWarnings("unchecked")
    @Override
    public String intercept(ActionInvocation invocation) throws Exception {
	   	ServletContext context = ServletActionContext.getServletContext();
        Map session = invocation.getInvocationContext().getSession();
        if (session == null || session.get(JsonKeyConstants.LOGIN_EMPLOYEE) == null) {
            return FlagConstants.RELOGIN;
        }
        BigInteger employeeAuthorityValue = (BigInteger) session.get(JsonKeyConstants.AUTHORITY_VALUE);
        //get name of action from requested
        String nameSpace = invocation.getProxy().getNamespace();
        String actionName = invocation.getProxy().getActionName();
        String privilegeName = nameSpace.substring(1)+"["+actionName+"]";
        logger.info(LogConstants.message("Request current action ", privilegeName));
        Integer privilegeValue = 0;
        authorityValueMap = (Map<String, Integer>) context.getAttribute(FlagConstants.AUTHORITY_VALUE);
        privilegeValue = authorityValueMap.get(privilegeName.toLowerCase());
        BigInteger privilegeValue_bigInteger = MathOperationUtils.binaryToBigInteger(2, privilegeValue);
        if (employeeAuthorityValue.and(privilegeValue_bigInteger).equals(privilegeValue_bigInteger)) {
        	logger.info(LogConstants.message("Whether current user can assess", true));
            return invocation.invoke();
        }else {
        	logger.info(LogConstants.message("Whether current user can assess", false));
            return FlagConstants.NO_ACCESS;
        }
            
   }
    
}
