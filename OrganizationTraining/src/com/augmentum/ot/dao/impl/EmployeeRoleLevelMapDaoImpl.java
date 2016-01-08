package com.augmentum.ot.dao.impl;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.augmentum.ot.dao.EmployeeRoleLevelMapDao;
import com.augmentum.ot.model.EmployeeRoleLevelMap;

@Component("employeeRoleLevelMapDao")
public class EmployeeRoleLevelMapDaoImpl extends BaseDaoImpl<EmployeeRoleLevelMap> implements EmployeeRoleLevelMapDao{

    @SuppressWarnings("unused")
	private static Logger logger = Logger.getLogger(EmployeeRoleLevelMapDaoImpl.class);

    @Override
    public Class<EmployeeRoleLevelMap> getEntityClass() {
        return EmployeeRoleLevelMap.class;
    }

 
}
