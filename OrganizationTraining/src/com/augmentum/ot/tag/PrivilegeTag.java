package com.augmentum.ot.tag;

import java.math.BigInteger;
import java.util.Arrays;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import com.augmentum.ot.dataObject.constant.FlagConstants;
import com.augmentum.ot.dataObject.constant.JsonKeyConstants;
import com.augmentum.ot.dataObject.constant.LogConstants;
import com.augmentum.ot.dataObject.constant.RoleNameConstants;
import com.augmentum.ot.model.Employee;
import com.augmentum.ot.util.DateHandlerUtils;
import com.augmentum.ot.util.MathOperationUtils;
import com.augmentum.ot.util.SessionObjectUtils;




/** 
* @ClassName: PrivilegeTag 
* @Description: control page element is visible or unvisible by privilege
* @date 2012-8-7 
* @version V1.0 
*/
public class PrivilegeTag extends TagSupport{

    private static final long serialVersionUID = -532517444654109642L;

    private static Logger logger = Logger.getLogger(PrivilegeTag.class);
    
    private static final String VISIBLE_ROLE_LIST_SPLIT = ",";

    /**
     * operate method
     */
    private String operateID; 
    
    /**
     * creator for course or plan is created by current employee
     */
    private String creator;
    
    /**
     * if  has special privilege, admin can delete all plan or course
     * for delete button
     */
    private Boolean special = false;
    
    /**
     * this button could is visible for these roles
     */
    private String visibleRoles;
    
    private Date planEndTime;
	@SuppressWarnings("unchecked")
	public int doStartTag() throws JspException {
	    HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        HttpSession session = request.getSession();
        Employee employee = SessionObjectUtils.getEmployeeFromSession();
		List<String> employeeRoleNames = SessionObjectUtils.getRoleNamesFromSession();
		if (null == employeeRoleNames) {
			return handleException(request, response);
		}
        // just control page element by visible roles
        if (null == operateID || "".equals(operateID)){
            if (null == visibleRoles || "".equals(visibleRoles)) {
                return EVAL_PAGE;
            }else {
                String[] visibleRolesArray = visibleRoles.split(VISIBLE_ROLE_LIST_SPLIT);
                List<String> visibleRoleList = Arrays.asList(visibleRolesArray);
                for(String roleName : employeeRoleNames) {
                    if (visibleRoleList.contains(roleName)) {
                        return EVAL_PAGE;
                    }
                }
                return SKIP_BODY;
            }
        }else {
            // control page element by employeeAuthorityValue
            BigInteger employeeAuthorityValue = (BigInteger) session.getAttribute(JsonKeyConstants.AUTHORITY_VALUE);
            ServletContext context = ServletActionContext.getServletContext();
            Map<String, Integer> authorityValueMap = (Map<String, Integer>) context.getAttribute(FlagConstants.AUTHORITY_VALUE);
            Integer privilegeValue = authorityValueMap.get(operateID.toLowerCase());
            BigInteger privilegeValue_bigInteger = MathOperationUtils.binaryToBigInteger(2, privilegeValue);
            if (employeeAuthorityValue.and(privilegeValue_bigInteger).equals(privilegeValue_bigInteger)){
                //if employee has privilege to operate this method, we need judge page element by creator 
                if ((null == creator || "".equals(creator)) && handleEditPlan()) {
                    return EVAL_PAGE;
                }else if ((creator.equals(employee.getAugUserName())) && handleEditPlan()) {
                	return EVAL_PAGE;
                }else {
                	return (special && employeeRoleNames.contains(RoleNameConstants.ADMIN)) ?
                 		   EVAL_PAGE : SKIP_BODY;
                }
            }
            return SKIP_BODY;
           
        }
    }
	
	private int handleException(HttpServletRequest request, HttpServletResponse response) {
		RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/error/error500.jsp"); 
        try {
            dispatcher.forward(request, response);
            return TagSupport.SKIP_BODY;
        } catch (Exception e) {
            logger.error(LogConstants.exceptionMessage("Handle Exception in Privilege Tag"), e);
            return TagSupport.SKIP_BODY;
        }
	}
	
	private boolean handleEditPlan(){
		if (!(operateID.equals(FlagConstants.EDIT_PLAN_ACTION))) {
			return true;
		} 
		if (null == planEndTime) {
			return true;
		}
		if (DateHandlerUtils.getMillSecondsDiff2Date(planEndTime, new Date()) > 0) {
			return true;
		}
		return false;
	}
    
    public String getOperateID() {
        return operateID;
    }

    public void setOperateID(String operateID) {
        this.operateID = operateID;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public Boolean getSpecial() {
        return special;
    }

    public void setSpecial(Boolean special) {
        this.special = special;
    }

    public String getVisibleRoles() {
        return visibleRoles;
    }

    public void setVisibleRoles(String visibleRoles) {
        this.visibleRoles = visibleRoles;
    }

	public Date getPlanEndTime() {
		return planEndTime;
	}

	public void setPlanEndTime(Date planEndTime) {
		this.planEndTime = planEndTime;
	}
    
}
