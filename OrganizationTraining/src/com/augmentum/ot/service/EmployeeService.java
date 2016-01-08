package com.augmentum.ot.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.augmentum.ot.dataObject.EmployeeRecord;
import com.augmentum.ot.dataObject.EmployeeSearchCondition;
import com.augmentum.ot.dataObject.Page;
import com.augmentum.ot.exception.DataWarningException;
import com.augmentum.ot.exception.ServerErrorException;
import com.augmentum.ot.model.CustomColumns;
import com.augmentum.ot.model.Employee;
import com.augmentum.ot.model.PageSize;

/**
 * EmployeeService interface provide methods about employee
 * @version 0.1, 07/16/2012
 */
public interface EmployeeService {

    
    /**
     * saveAndInitLoginEmployee
     * @param userName
     * @param request
     * @return
     * @throws ServerErrorException
     */
    Employee saveAndInitLoginEmployee(String userName, HttpServletRequest request) throws ServerErrorException;
    
    /**
     * saveAndJudgeExistedInDB
     * @param employee
     * @return
     * @throws ServerErrorException
     */
    Employee saveAndJudgeExistedInDB(Employee employee) throws ServerErrorException;

    /**
     * findEmployeeByPrimaryKey
     * @param keyId
     * @return
     */
    Employee findEmployeeByPrimaryKey(int keyId);
    
	 /** 
	 * @Description: find employee by employee name
	 * @param employeeName
	 * @return
	 * @throws ServerErrorException 
	 */ 
	Employee findEmployeeByName(String employeeName) throws ServerErrorException;
	
    /** 
     *  Find the customer columns use employeeId.
     *  If a custom set the show column before search, this will find from relationship table.
     *  If the custom is login first time, system will find all column by default.   
     * 
     * @param employeeId
     * @return List<CustomColumns>
     * @throws ServiceException  
     */ 
    List<CustomColumns> findCustomColumnsByEmployeeId(Integer employeeId, Integer categoryFlag) throws ServerErrorException;
     
	/** 
	 * If the custom set custom column, use this to do it.
	 * 
	 * @param employeeId employeeId is flag of the custom.
	 * @param customization 
	 * @param categoryFlag  the scope of the column.
	 * @throws ServiceException  
	 */ 
	void updateCustomColumnsOfEmployee(Integer employeeId,
			String customization, Integer categoryFlag ) throws ServerErrorException;
	
	/** 
	 *  Find page size by employee id.
	 * 
	 * @param employeeId
	 * @param categoryFlag
	 * @return Integer
	 * @throws ServerErrorException  
	 */ 
	Integer findPageSizeByEmployeeId(Integer employeeId, Integer categoryFlag)
			throws ServerErrorException;
	
	/** 
	 *  Save or update the custom page size for one employee in the category
	 * 
	 * @param employeeId
	 * @param categoryFlag
	 * @param pageSizeValue
	 * @throws ServerErrorException  
	 */ 
	void updatePageSizeByEmployee(Integer employeeId, Integer categoryFlag,
			Integer pageSizeValue) throws ServerErrorException;
	
	/** 
	 *  Find all page sizes by default.
	 * 
	 * @param categoryFlag
	 * @return List<PageSize>
	 * @throws ServerErrorException  
	 */ 
	List<PageSize> findDefaultPageSizesByCategory(Integer categoryFlag) throws ServerErrorException;
	
    /**
     * Save the related role of employee by using jobNumAndRoles.
     * @param jobNumAndRoles
     * @throws ServerErrorException
     */
    void saveEmployeeRole(List<EmployeeRecord> jobNumAndRoles) throws ServerErrorException;
    
    /**
     * getRoleListNames
     * @param employee
     * @return
     * @throws ServerErrorException
     */
    List<String> getRoleListNames(Employee employee) throws ServerErrorException;
    
    Employee findEmployeeForLogin(String userName, String password) throws ServerErrorException, DataWarningException;
	
	/**
     * this method is only for package.
     * @param condition
     * @param selectFields
     * @return
     */
    Page<EmployeeRecord> findEmployeesByCriteria(EmployeeSearchCondition condition) throws ServerErrorException;

    /**
     * for package1.0
     * @return
     */
    List<String> findAllEmployeeNames();
    
}
