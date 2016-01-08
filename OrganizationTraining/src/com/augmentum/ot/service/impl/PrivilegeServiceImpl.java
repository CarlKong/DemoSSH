package com.augmentum.ot.service.impl;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.augmentum.ot.dao.EmployeeDao;
import com.augmentum.ot.dao.EmployeeRoleLevelMapDao;
import com.augmentum.ot.dao.PrivilegeDao;
import com.augmentum.ot.dao.RoleDao;
import com.augmentum.ot.dataObject.constant.ErrorCodeConstants;
import com.augmentum.ot.dataObject.constant.LogConstants;
import com.augmentum.ot.dataObject.constant.RoleNameConstants;
import com.augmentum.ot.exception.ServerErrorException;
import com.augmentum.ot.model.Employee;
import com.augmentum.ot.model.EmployeeRoleLevelMap;
import com.augmentum.ot.model.Privilege;
import com.augmentum.ot.model.Role;
import com.augmentum.ot.service.PrivilegeService;
import com.augmentum.ot.util.BeanFactory;
import com.augmentum.ot.util.MathOperationUtils;


/** 
* @ClassName: PrivilegeServiceImpl 
* @date 2012-8-7 
* @version V1.0 
*/
@Component("privilegeService")
public class PrivilegeServiceImpl implements PrivilegeService{

    private static Logger logger = Logger.getLogger(PrivilegeServiceImpl.class);
    
    private PrivilegeDao privilegeDao;
    
    public PrivilegeDao getPrivilegeDao() {
        return privilegeDao;
    }

    @Resource(name="privilegeDao")
    public void setPrivilegeDao(PrivilegeDao privilegeDao) {
        this.privilegeDao = privilegeDao;
    }

    @Override
    public Integer getPrivilegeValueByPrivilegeName(String privilegeName)
            throws ServerErrorException {
        try {
        	Privilege privilege = privilegeDao.getPrivilegeByPrivilegeName(privilegeName);
            return privilege.getPrivilegeValue();
        }catch(Exception e) {
        	logger.error(LogConstants.exceptionMessage("Get Privilege Value By PrivilegeName["
        				+ privilegeName + "] ") + e);
        	throw new ServerErrorException(ErrorCodeConstants.SERVER_ERROR);
        }
    }

    @Override
    public BigInteger createAuthorityValueForEmployee(String augUserName)
            throws ServerErrorException {
        EmployeeDao employeeDao = (EmployeeDao) BeanFactory.getEmployeeDao();
        RoleDao roleDao = BeanFactory.getRoleDao();
        Employee employee = null;
        employee = employeeDao.findEmployeeByName(augUserName);
        List<Role> employeeRoleList = this.getEmployeeRoleList(employee);
        BigInteger employeePrivilegeValue = new BigInteger("0",2);
        if(employeeRoleList.size() > 0) {
            for(Role role : employeeRoleList){
                //we should calculate privilege value for single role
                BigInteger rolePrivilegeValue = new BigInteger("0", 2);
                for(Privilege privilege : role.getPrivilegeList()) {
                    Integer privilegeValue = privilege.getPrivilegeValue();
                    BigInteger privilegeValue_bigInteger = MathOperationUtils.binaryToBigInteger(2, privilegeValue);
                    rolePrivilegeValue = rolePrivilegeValue.add(privilegeValue_bigInteger);
                }
                //set rolePrivilegeValue
                role.setRolePrivilegeValue(rolePrivilegeValue.toString());
                employeePrivilegeValue = employeePrivilegeValue.or(rolePrivilegeValue);
            }
        } else {//if roles don't exsit, get default roles from externalRole table with externalRoleName ,and then calculate privilege value for employee
                EmployeeRoleLevelMapDao employeeRoleLevelMapDao = BeanFactory.getEmployeeRoleLevelMapDao();
                Role defaultRole = null;
                defaultRole = roleDao.getRoleByRoleName(RoleNameConstants.TRAINER);
                EmployeeRoleLevelMap employeeRoleLevelMap = new EmployeeRoleLevelMap();
                if(null != defaultRole){
                    employeeRoleLevelMap.setEmployee(employee);
                    employeeRoleLevelMap.setRole(defaultRole);
                    employeeRoleLevelMapDao.saveObject(employeeRoleLevelMap);
                }
                BigInteger rolePrivilegeValue = new BigInteger("0", 2);
                for(Privilege privilege : defaultRole.getPrivilegeList()) {
                    Integer privilegeValue = privilege.getPrivilegeValue();
                    BigInteger privilegeValue_bigInteger = MathOperationUtils.binaryToBigInteger(2, privilegeValue);
                    rolePrivilegeValue = rolePrivilegeValue.add(privilegeValue_bigInteger);
                }
                //set rolePrivilegeValue
                defaultRole.setRolePrivilegeValue(rolePrivilegeValue.toString());
                employeePrivilegeValue = employeePrivilegeValue.or(rolePrivilegeValue);
        };
        return employeePrivilegeValue;
    }

    /** 
     * @Description: get employee all role
     * @param employee
     * @return List<String>
     *           employeeRoleList
     */ 
    private List<Role> getEmployeeRoleList(Employee employee) {
        List<Role> employeeRoleList = new ArrayList<Role>();
        List<EmployeeRoleLevelMap> employeeRoleLevelMaps = employee.getRoleLevelsForEmployee();
        for(EmployeeRoleLevelMap employeeRoleLevelMap : employeeRoleLevelMaps) {
            if(!employeeRoleList.contains(employeeRoleLevelMap.getRole())) {
                employeeRoleList.add(employeeRoleLevelMap.getRole());
            }
        }
        return employeeRoleList;
    }
	
    @Override
	public List<Privilege> listAllPrivilige() throws ServerErrorException {
		try {
			List<Privilege> privilegeList = privilegeDao.loadAll();
			logger.info("Privilege Size: " + privilegeList.size());
            return privilegeList;
        }catch(Exception e) {
        	logger.error(LogConstants.exceptionMessage("Load all privilege failure:") + e);
        	throw new ServerErrorException(ErrorCodeConstants.SERVER_ERROR);
        }
	}
}
