package com.augmentum.ot.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.augmentum.ot.dao.EmployeeRoleLevelMapDao;
import com.augmentum.ot.service.EmployeeRoleLevelMapService;


@Component("employeeRoleLevelMapService")
public class EmployeeRoleLevelMapServiceImpl implements EmployeeRoleLevelMapService{

    private EmployeeRoleLevelMapDao employeeRoleLevelMapDao;

    public EmployeeRoleLevelMapDao getEmployeeRoleLevelMapDao() {
        return employeeRoleLevelMapDao;
    }

    @Resource
    public void setEmployeeRoleLevelMapDao(
            EmployeeRoleLevelMapDao employeeRoleLevelMapDao) {
        this.employeeRoleLevelMapDao = employeeRoleLevelMapDao;
    }

}
