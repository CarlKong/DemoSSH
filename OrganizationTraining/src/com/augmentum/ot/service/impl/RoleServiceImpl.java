package com.augmentum.ot.service.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.augmentum.ot.dao.RoleDao;
import com.augmentum.ot.exception.ServerErrorException;
import com.augmentum.ot.model.EmployeeRoleLevelMap;
import com.augmentum.ot.model.Role;
import com.augmentum.ot.service.RoleService;



/** 
* @ClassName: RoleServiceImpl 
* @date 2012-8-18 
* @version V1.0 
*/
@Component("roleService")
public class RoleServiceImpl implements RoleService{

    private RoleDao roleDao;
    

    public RoleDao getRoleDao() {
        return roleDao;
    }

    @Resource(name="roleDao")
    public void setRoleDao(RoleDao roleDao) {
        this.roleDao = roleDao;
    }

	@Override
	public Set<String> findEmployeeJobNumbersByRoleNames(String[] roleNames)
			throws ServerErrorException {
		Set<String> employeeJobNumbers = new HashSet<String>();
        for (String roleName:roleNames) {
            Role role=roleDao.getRoleByRoleName(roleName);
            if (role != null) {
                List<EmployeeRoleLevelMap> EmployeeRoleLevelMaps = role.getRoleLevelsForRole();
                for (EmployeeRoleLevelMap employeeRoleLevelMap : EmployeeRoleLevelMaps) {
                	employeeJobNumbers.add(employeeRoleLevelMap.getEmployee().getAugEmpId());
                }
            }
        }
		return employeeJobNumbers;
	}
}
