package com.augmentum.ot.dao;


import java.util.List;

import com.augmentum.ot.dataObject.EmployeeSearchCondition;
import com.augmentum.ot.dataObject.RoleListSearchCondition;
import com.augmentum.ot.model.CustomColumns;
import com.augmentum.ot.model.Employee;
import com.augmentum.ot.model.PageSize;



/**
 * A interface about employee,provide additional methods
 * 
 * @version 0.1, 07/13/2012
 */
public interface EmployeeDao extends BaseDao<Employee> {
	
	/** 
	 *  Find default custom column by category.
	 * 
	 * @param categoryFlag
	 * @return  
	 */ 
	List<CustomColumns> findDefaultCustomColumns(Integer categoryFlag);

	/** 
	 *  Find custom column by id.
	 * 
	 * @param id
	 * @return  
	 */ 
	CustomColumns findCustomColumnById(Integer id);
	
	/**
	 * 
	 *  Find Custom Column by field name
	 * 
	 * @param fieldName
	 * @return
	 */
	CustomColumns findCustomCloumnByFieldName(int categoryFlag, String fieldName);

	/** 
	 *  Find all page size of category.
	 * 
	 * @param categoryFlag
	 * @return  
	 */ 
	List<PageSize> findAllPageSizesByCategory(Integer categoryFlag);

	/** 
	 *  Find out the page size object of the employee.
	 * 
	 * @param categoryFlag
	 * @param pageSizeValue
	 * @return  
	 */ 
	PageSize findOneOptionalPageSize(Integer categoryFlag, Integer pageSizeValue);
	
	/**
	 * Find out empoyee by name.
	 * 
	 * @param employeeName
	 * @return
	 */
	Employee findEmployeeByName(String employeeName);

    String spellRemoteSqlForEmployeeList(RoleListSearchCondition criteria,
            List<String> employeeIds);

    Employee findEmployeeByJobNumber(String augEmpId);
    
    Employee findEmployeeByNameAndPwd(String userName, String password);
	
	List<Employee> findEmployeesByConditions(EmployeeSearchCondition condition);
    
   	int findEmployeesByConditionsCount(EmployeeSearchCondition condition);
    
}
