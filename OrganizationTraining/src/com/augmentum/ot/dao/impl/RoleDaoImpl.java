package com.augmentum.ot.dao.impl;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.augmentum.ot.dao.RoleDao;
import com.augmentum.ot.model.Role;

@Component("roleDao")
public class RoleDaoImpl extends BaseDaoImpl<Role> implements RoleDao{

    @SuppressWarnings("unused")
	private static Logger logger = Logger.getLogger(RoleDaoImpl.class);

    @Override
    public Class<Role> getEntityClass() {
        return Role.class;
    }

    public Role getRoleByRoleName(String roleName) {
        return (Role)getSession().createQuery(
                "from " + getEntityClass().getSimpleName()
                        + " where roleName=:roleName")
                .setString("roleName", roleName).uniqueResult();
    }

 
}
