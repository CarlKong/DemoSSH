package com.augmentum.ot.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.augmentum.ot.dao.EmployeeDao;
import com.augmentum.ot.dao.EmployeeRoleLevelMapDao;
import com.augmentum.ot.dao.RoleDao;
import com.augmentum.ot.dao.RoleLevelDao;
import com.augmentum.ot.dataObject.EmployeeRecord;
import com.augmentum.ot.dataObject.EmployeeSearchCondition;
import com.augmentum.ot.dataObject.Page;
import com.augmentum.ot.dataObject.constant.ErrorCodeConstants;
import com.augmentum.ot.dataObject.constant.FlagConstants;
import com.augmentum.ot.dataObject.constant.LogConstants;
import com.augmentum.ot.dataObject.constant.RoleNameConstants;
import com.augmentum.ot.exception.DataWarningException;
import com.augmentum.ot.exception.ServerErrorException;
import com.augmentum.ot.model.CustomColumns;
import com.augmentum.ot.model.Employee;
import com.augmentum.ot.model.EmployeeRoleLevelMap;
import com.augmentum.ot.model.PageSize;
import com.augmentum.ot.model.Role;
import com.augmentum.ot.model.RoleLevel;
import com.augmentum.ot.service.EmployeeService;
import com.augmentum.ot.util.CustomColumnUtils;

/**
 * @ClassName: EmployeeServiceImpl
 * @date 2012-8-18
 * @version V1.0
 */
@Component("employeeService")
public class EmployeeServiceImpl implements EmployeeService {

    private static Logger logger = Logger.getLogger(EmployeeServiceImpl.class);

    @Resource(name = "employeeDao")
    private EmployeeDao employeeDao;
    @Resource(name="roleDao")
    private RoleDao roleDao;
    @Resource(name="roleLevelDao")
    private RoleLevelDao roleLevelDao;
    @Resource(name="employeeRoleLevelMapDao")
    private EmployeeRoleLevelMapDao employeeRoleLevelMapDao;

    @Override
    public List<CustomColumns> findCustomColumnsByEmployeeId(Integer employeeId, Integer categoryFlag)
            throws ServerErrorException {
        if (employeeId == null || employeeId == 0 || categoryFlag == null) {
            logger.error(LogConstants.objectIsNULLOrEmpty("employeeId or categoryFlag"));
            throw new ServerErrorException(ErrorCodeConstants.SERVER_ERROR);
        }
        try {
            Employee employee = employeeDao.findByPrimaryKey(employeeId);
            if(employee == null){
                logger.error(LogConstants.message("Don't find the employee", employeeId));
                throw new ServerErrorException(ErrorCodeConstants.SERVER_ERROR);
            }
            List<CustomColumns> list = employee.getCustomColumnsList();
            List<CustomColumns> returnList = new ArrayList<CustomColumns>();
            if (list != null && list.size() > 0) {
                for (CustomColumns cc : list) {
                    if (cc.getCategoryFlag().equals(categoryFlag)) {
                        returnList.add(cc);
                    }
                }
            }
            if(returnList.size() <= 0){
                // Judge whether the custom has already modified the custom column. 
                Integer employeeCustomColumnIdentity = employee.getCustomColumnTypeIdentity();
                boolean flag = CustomColumnUtils.isCustomColumnModified(CustomColumnUtils.getIdentityByCategoryFlag(categoryFlag), employeeCustomColumnIdentity);
                if (!flag) {
                    returnList = employeeDao.findDefaultCustomColumns(categoryFlag);
                }
            }
            logger.debug(LogConstants.getDebugOutput("Find custom columns for employee[#"+employeeId+"]", returnList));
            return returnList;
        } catch (Exception e) {
            logger.error(LogConstants.exceptionMessage("Find custom columns for employee[#"+employeeId+"]"), e);
            throw new ServerErrorException(ErrorCodeConstants.SERVER_ERROR);
        }
    }

    @Override
    public void updateCustomColumnsOfEmployee(Integer employeeId,
            String customization, Integer categoryFlag) throws ServerErrorException {
        if (employeeId == null || employeeId == 0 || customization == null
                || customization == "" || categoryFlag == null) {
        	 logger.error(LogConstants.objectIsNULLOrEmpty("employeeId, customization or categoryFlag"));
             throw new ServerErrorException(ErrorCodeConstants.SERVER_ERROR);
        }
        try {
            Employee employee = employeeDao.findByPrimaryKey(employeeId);
            Integer customColumnIdentity = employee.getCustomColumnTypeIdentity();
            customColumnIdentity = CustomColumnUtils.getCustomColumnTypeIdentity(CustomColumnUtils.getIdentityByCategoryFlag(categoryFlag), customColumnIdentity);
            employee.setCustomColumnTypeIdentity(customColumnIdentity);
            if (employee == null) {
            	logger.error(LogConstants.message("Don't find the employee", employeeId));
                throw new ServerErrorException(ErrorCodeConstants.SERVER_ERROR);
            }
            List<CustomColumns> list = employee.getCustomColumnsList();
            if (list != null && list.size() > 0) {
                List<CustomColumns> tempList = new ArrayList<CustomColumns>();
                for (CustomColumns customColumn : list) {
                    if (customColumn.getCategoryFlag().equals(categoryFlag)) {
                        tempList.add(customColumn);
                    }
                }
                list.removeAll(tempList);
            }
            String[] fields = null;
            fields = customization.split(",");
            for (int i = 0; i < fields.length; i++) {
                CustomColumns cc = employeeDao.findCustomCloumnByFieldName(
                        categoryFlag, fields[i]);
                if (cc != null) {
                    employee.getCustomColumnsList().add(cc);
                }
            }
        } catch (Exception e) {
            logger.error(LogConstants.exceptionMessage("Update custom columns for employee[#"+employeeId+"]"), e);
            throw new ServerErrorException(ErrorCodeConstants.SERVER_ERROR);
        }

    }

    @Override
    public Integer findPageSizeByEmployeeId(Integer employeeId,
            Integer categoryFlag) throws ServerErrorException {
        if (employeeId == null || employeeId == 0 || categoryFlag == null) {
        	logger.error(LogConstants.objectIsNULLOrEmpty("employeeId or categoryFlag"));
            throw new ServerErrorException(ErrorCodeConstants.SERVER_ERROR);
        }
        try {
            Employee employee = employeeDao.findByPrimaryKey(employeeId);
            if (employee == null) {
            	logger.error(LogConstants.message("Don't find the employee", employeeId));
                throw new ServerErrorException(ErrorCodeConstants.SERVER_ERROR);
            }
            List<PageSize> pageSizeList = employee.getCustomPageSizeList();
            if (pageSizeList.isEmpty() || pageSizeList == null) {
                pageSizeList = employeeDao.findAllPageSizesByCategory(categoryFlag);
                logger.debug(LogConstants.pureMessage("Don't find custom page size for employee[#" 
                		+ employeeId + "], so find default page size list [" +pageSizeList+ "]" 
                		+" at Category Flag[" + categoryFlag + "]"));
                return pageSizeList.get(0).getPageSizeValue();
            }
            List<PageSize> returnList = new ArrayList<PageSize>();
            for (PageSize ps : pageSizeList) {
                if (ps.getCategoryFlag().equals(categoryFlag)) {
                    returnList.add(ps);
                }
            }
            if (returnList.size() <= 0) {
                returnList = employeeDao.findAllPageSizesByCategory(categoryFlag);
            }
            logger.debug(LogConstants.getDebugOutput("Find page size for employee[#"+employeeId+"]", returnList));
            return returnList.get(0).getPageSizeValue();
        } catch (Exception e) {
            logger.error(LogConstants.exceptionMessage("Find page size for employee[#"+employeeId+"]"), e);
            throw new ServerErrorException(ErrorCodeConstants.SERVER_ERROR);
        }
    }

    @Override
    public void updatePageSizeByEmployee(Integer employeeId, Integer categoryFlag,
    			Integer pageSizeValue) throws ServerErrorException {
        if (employeeId == null || employeeId == 0 || categoryFlag == null
        		|| pageSizeValue == null || pageSizeValue == 0) {
        	logger.error(LogConstants.objectIsNULLOrEmpty("employeeId, categoryFlag or pageSizeValue"));
            throw new ServerErrorException(ErrorCodeConstants.SERVER_ERROR);
        }
        try {
            Employee employee = employeeDao.findByPrimaryKey(employeeId);
            if (employee == null) {
            	logger.error(LogConstants.message("Don't find the employee", employeeId));
                throw new ServerErrorException(ErrorCodeConstants.SERVER_ERROR);
            }
            List<PageSize> list = employee.getCustomPageSizeList();
            PageSize optionalPageSize = null;
            for (PageSize pageSize : list) {
                if (pageSize.getCategoryFlag().equals(categoryFlag)) {
                    optionalPageSize = pageSize;
                    break;
                }
            }
            if (optionalPageSize != null) {
                list.remove(optionalPageSize);
            }
            PageSize customPageSize = employeeDao.findOneOptionalPageSize(
                    categoryFlag, pageSizeValue);
            employee.getCustomPageSizeList().add(customPageSize);
        } catch (Exception e) {
        	logger.error(LogConstants.exceptionMessage("Update page size for employee[#"+employeeId+"]"), e);
            throw new ServerErrorException(ErrorCodeConstants.SERVER_ERROR);
        }

    }

    @Override
    public List<PageSize> findDefaultPageSizesByCategory(Integer categoryFlag)
            throws ServerErrorException {
        if (categoryFlag == null) {
        	logger.error(LogConstants.objectIsNULLOrEmpty("categoryFlag"));
            throw new ServerErrorException(ErrorCodeConstants.SERVER_ERROR);
        }
        try {
            List<PageSize> list = employeeDao
                    .findAllPageSizesByCategory(categoryFlag);
            if (list == null || list.size() == 0) {
                logger.error(LogConstants.message("Don't find default page size by Category Flag", categoryFlag));
                throw new ServerErrorException(ErrorCodeConstants.SERVER_ERROR);
            }
            return list;
        } catch (Exception e) {
        	logger.error(LogConstants.exceptionMessage("Find default page size Category["+categoryFlag+"]"), e);
            throw new ServerErrorException(ErrorCodeConstants.SERVER_ERROR);
        }
    }

    @Override
    public Employee findEmployeeByName(String employeeName)
            throws ServerErrorException {
        Employee employee = null;
        employee = employeeDao.findEmployeeByName(employeeName);
        if (null != employee && !employee.getRoleLevelsForEmployee().isEmpty()) {
            employee.getRoleLevelsForEmployee().get(0);
        }
        return employee;
    }
    
    /**
     * Save the related role of employee by using jobNumAndRoles.
     * @param jobNumAndRoles
     * @throws ServerErrorException 
     */
    @Override
    public void saveEmployeeRole(List<EmployeeRecord> jobNumAndRoles) throws ServerErrorException{
        for(EmployeeRecord searchEmployeeRecord:jobNumAndRoles){
            String role = searchEmployeeRecord.getRole();
            if (role != null && (!"".equals(role))) {
                Set<String> roleNames=new HashSet<String>(Arrays.asList(role.split(",")));
                String jobNumber=searchEmployeeRecord.getJobNumber();
                Employee employee=employeeDao.findEmployeeByJobNumber(jobNumber);
                List<EmployeeRoleLevelMap> employeeRoleLevelMaps=employee.getRoleLevelsForEmployee();
                for(EmployeeRoleLevelMap employeeRoleLevelMap:employeeRoleLevelMaps){
                    employeeRoleLevelMapDao.deleteObject(employeeRoleLevelMap);
                }
                if(roleNames.contains(RoleNameConstants.ADMIN)){
                    Role roleObject=roleDao.getRoleByRoleName(RoleNameConstants.ADMIN);
                    RoleLevel roleLevel=roleLevelDao.getRoleLevelbyLevel(1);
                    EmployeeRoleLevelMap employeeRoleLevelMap=new EmployeeRoleLevelMap();
                    employeeRoleLevelMap.setEmployee(employee);
                    employeeRoleLevelMap.setRole(roleObject);
                    employeeRoleLevelMap.setRoleLevel(roleLevel);
                    try{
                        employeeRoleLevelMapDao.saveObject(employeeRoleLevelMap);
                    }catch(Exception e){
                        logger.error(e);
                        throw new ServerErrorException(e);
                    }
                }
                if(roleNames.contains(RoleNameConstants.TRAINING_MASTER)){
                    Role roleObject=roleDao.getRoleByRoleName(RoleNameConstants.TRAINING_MASTER);
                    RoleLevel roleLevel=roleLevelDao.getRoleLevelbyLevel(1);
                    EmployeeRoleLevelMap employeeRoleLevelMap=new EmployeeRoleLevelMap();
                    employeeRoleLevelMap.setEmployee(employee);
                    employeeRoleLevelMap.setRole(roleObject);
                    employeeRoleLevelMap.setRoleLevel(roleLevel);
                    try{
                        employeeRoleLevelMapDao.saveObject(employeeRoleLevelMap);
                    }catch(Exception e){
                        logger.error(e);
                        throw new ServerErrorException(e);
                    }
                }
                if(roleNames.contains(RoleNameConstants.TRAINER)){
                    Role roleObject=roleDao.getRoleByRoleName(RoleNameConstants.TRAINER);
                    RoleLevel roleLevel=roleLevelDao.getRoleLevelbyLevel(1);
                    EmployeeRoleLevelMap employeeRoleLevelMap=new EmployeeRoleLevelMap();
                    employeeRoleLevelMap.setEmployee(employee);
                    employeeRoleLevelMap.setRole(roleObject);
                    employeeRoleLevelMap.setRoleLevel(roleLevel);
                    try{
                        employeeRoleLevelMapDao.saveObject(employeeRoleLevelMap);
                    }catch(Exception e){
                        logger.error(e);
                        throw new ServerErrorException(e);
                    }
                }
            }
        }
    }

	@Override
	public Employee saveAndInitLoginEmployee(String userName, HttpServletRequest request) 
				throws ServerErrorException {
        Employee localEmployee = findEmployeeByName(userName);

		if (null == localEmployee) {
			logger.error(LogConstants.pureMessage("Don't find the user[" + userName + "] from local database"));
			throw new ServerErrorException(ErrorCodeConstants.SERVER_ERROR);
		}
        logger.info(LogConstants.message("[" + localEmployee.getAugUserName() + "] Log in the system at ", request.getRemoteAddr()));
        return localEmployee;
	}
	
	@Override
	public List<String> getRoleListNames(Employee employee) throws ServerErrorException {
		if (employee == null) {
			logger.error(LogConstants.objectIsNULLOrEmpty("Employee"));
			throw new ServerErrorException(ErrorCodeConstants.SERVER_ERROR);
		}
		try {
			employee = employeeDao.findByPrimaryKey(employee.getEmployeeId());
		}catch(Exception e) {
			logger.error(LogConstants.exceptionMessage("Get role list for employee[#" +employee.getEmployeeId()+ "]"), e);
			throw new ServerErrorException(ErrorCodeConstants.SERVER_ERROR);
		}
		List<String> roleNameList = new ArrayList<String>();
		List<EmployeeRoleLevelMap> roleLevelsForEmployee = employee.getRoleLevelsForEmployee();
		for (EmployeeRoleLevelMap roleMap : roleLevelsForEmployee) {
			roleNameList.add(roleMap.getRole().getRoleName());
		}
		return roleNameList;
	}

	@Override
	public Employee findEmployeeByPrimaryKey(int keyId) {
	    Employee employee = null;
	    employee = employeeDao.findByPrimaryKey(keyId);
	    if(employee.getLeaveList() != null && employee.getLeaveList().size() != 0){
	        employee.getLeaveList().get(0);
	    }
		return employee;
	}

	@Override
	public Employee saveAndJudgeExistedInDB(Employee employee)
			throws ServerErrorException {
		Employee localEmployee = employeeDao.findEmployeeByJobNumber(employee.getAugEmpId());
		if (localEmployee == null) {
			employeeDao.saveObject(employee);
			Role role=roleDao.getRoleByRoleName(RoleNameConstants.TRAINER);
			List<EmployeeRoleLevelMap> list = new ArrayList<EmployeeRoleLevelMap>();
            EmployeeRoleLevelMap employeeRoleLevelMap=new EmployeeRoleLevelMap();
            employeeRoleLevelMap.setEmployee(employee);
            employeeRoleLevelMap.setRole(role);
            list.add(employeeRoleLevelMap);
            employee.setRoleLevelsForEmployee(list);
            employeeRoleLevelMapDao.saveObject(employeeRoleLevelMap);
            localEmployee = employee;
		}
		List<EmployeeRoleLevelMap> roleList = localEmployee.getRoleLevelsForEmployee();
		for (EmployeeRoleLevelMap roleMap: roleList) {
			roleMap.getRole();
		}
		return localEmployee;
	}

	@Override
	public Employee findEmployeeForLogin(String userName, String password) throws ServerErrorException,
			DataWarningException {
		Employee employee = null;
		try {
			employee = employeeDao.findEmployeeByNameAndPwd(userName, password);
			if (employee != null) {
				employee.getRoleLevelsForEmployee().size();
			}
		} catch (Exception e) {
			logger.error("find employee by username and password faild: ", e);
			throw new ServerErrorException(ErrorCodeConstants.SERVER_ERROR);
		}
		return employee;
	}
	
	@Override
	public Page<EmployeeRecord> findEmployeesByCriteria(EmployeeSearchCondition condition) {
		// TODO
		Page<EmployeeRecord> employeePage = new Page<EmployeeRecord>();
		List<Employee> result = employeeDao.findEmployeesByConditions(condition);
		int count = employeeDao.findEmployeesByConditionsCount(condition);
		employeePage.setPageSize(condition.getPageSize());
		employeePage.setTotalRecords(count);
		employeePage.setNowPager(condition.getNowPage());
		
		List<EmployeeRecord> employeeRecordList = new ArrayList<EmployeeRecord>();
		for(Employee employee:result) {
			EmployeeRecord record = new EmployeeRecord();
			record.setId(employee.getEmployeeId());
			record.setJobNumber(employee.getAugEmpId());
			record.setName(employee.getAugUserName());
			record.setEmail(employee.getAugEmail());
			List<EmployeeRoleLevelMap> roleList = employee.getRoleLevelsForEmployee();
			if (roleList != null && !(roleList.isEmpty())) {
				StringBuilder builder = new StringBuilder();
				for (EmployeeRoleLevelMap roleLevelMap: roleList) {
					builder.append(roleLevelMap.getRole().getRoleName());
					builder.append(FlagConstants.SPLIT_COMMA);
				}
				record.setRole(builder.substring(0, builder.length() - 1));
			} else {
				record.setRole("");
			}
			employeeRecordList.add(record);
		}
		employeePage.setList(employeeRecordList);
		
		return employeePage;
	}

    @Override
    public List<String> findAllEmployeeNames() {
        List<Employee> employees = employeeDao.loadAll();
        List<String> employeeNames = new ArrayList<String>();
        if(employees != null) {
            for(Employee e: employees) {
                if(e != null){
                    employeeNames.add(e.getAugUserName());
                }
            }
        }
        return employeeNames;
    }
}
