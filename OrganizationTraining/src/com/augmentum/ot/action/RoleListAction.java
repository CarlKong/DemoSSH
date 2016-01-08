package com.augmentum.ot.action;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.augmentum.ot.dataObject.EmployeeRecord;
import com.augmentum.ot.dataObject.EmployeeSearchCondition;
import com.augmentum.ot.dataObject.ErrorCode;
import com.augmentum.ot.dataObject.Page;
import com.augmentum.ot.dataObject.constant.ErrorCodeConstants;
import com.augmentum.ot.dataObject.constant.FlagConstants;
import com.augmentum.ot.dataObject.constant.LogConstants;
import com.augmentum.ot.exception.ServerErrorException;
import com.augmentum.ot.model.Employee;
import com.augmentum.ot.service.EmployeeService;
import com.augmentum.ot.util.BeanFactory;
import com.augmentum.ot.util.ReaderXmlUtils;
import com.augmentum.ot.util.SessionObjectUtils;

@Component("roleListAction")
@Scope("prototype")
public class RoleListAction extends BaseAction {
    
    private static final long serialVersionUID = -3176720371277886496L;
    private Logger logger = Logger.getLogger(RoleListAction.class);
    private EmployeeSearchCondition criteria;
    private List<EmployeeRecord> jobNumAndRoles=new ArrayList<EmployeeRecord>();
    @Resource
    private EmployeeService employeeService;

    public String searchRoleList() throws ServerErrorException {
		Employee employee = SessionObjectUtils.getEmployeeFromSession();
		logger.info(LogConstants.getInfo(employee.getAugUserName(), "searchRoleList"));
		EmployeeService employeeService = BeanFactory.getEmployeeService();
        try {
        	if (criteria.getSearchFields() != null && criteria.getSearchFields().length > 0) {
        		String searchFields = criteria.getSearchFields()[0];
        		criteria.setSearchFields(searchFields.split(FlagConstants.SPLIT_COMMA));
        	}
//        	Page<EmployeeRecord> employeeRecords = remoteService.findEmployeesByCriteria(criteria, httpRequest);
        	// TODO for package
        	Page<EmployeeRecord> employeeRecords = employeeService.findEmployeesByCriteria(criteria);
        	jsonObject = JSONObject.fromObject(employeeRecords);
        } catch (ServerErrorException e) {
            ErrorCode errorCode = ReaderXmlUtils.getErrorCodes().get(ErrorCodeConstants.SERVER_ERROR);
            jsonObject = JSONObject.fromObject(errorCode);
            return FlagConstants.ERROR_SERVER;
        }
        return SUCCESS;
    }
    
    public String saveRoleList(){
        try {
            employeeService.saveEmployeeRole(jobNumAndRoles);
        } catch (ServerErrorException e) {
            ErrorCode errorCode = ReaderXmlUtils.getErrorCodes().get(ErrorCodeConstants.SERVER_ERROR);
            jsonObject = JSONObject.fromObject(errorCode);
            return FlagConstants.ERROR_SERVER;
        }
        return SUCCESS;
    }
    

	public EmployeeSearchCondition getCriteria() {
		return criteria;
	}

	public void setCriteria(EmployeeSearchCondition criteria) {
		this.criteria = criteria;
	}

	@Override
    public String execute(){
        return SUCCESS;
    }

    public List<EmployeeRecord> getJobNumAndRoles() {
        return jobNumAndRoles;
    }

    public void setJobNumAndRoles(List<EmployeeRecord> jobNumAndRoles) {
        this.jobNumAndRoles = jobNumAndRoles;
    }
}
