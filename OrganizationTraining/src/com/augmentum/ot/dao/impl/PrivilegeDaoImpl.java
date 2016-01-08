package com.augmentum.ot.dao.impl;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.augmentum.ot.dao.PrivilegeDao;
import com.augmentum.ot.model.Privilege;

@Component("privilegeDao")
public class PrivilegeDaoImpl extends BaseDaoImpl<Privilege> implements PrivilegeDao{

    @SuppressWarnings("unused")
	private static Logger logger = Logger.getLogger(PrivilegeDaoImpl.class);

    @Override
    public Class<Privilege> getEntityClass() {
        return Privilege.class;
    }

    public Privilege getPrivilegeByPrivilegeName(String privilegeName) {
        return (Privilege)getSession().createQuery(
                "from " + getEntityClass().getSimpleName()
                        + " where privilegeName=:privilegeName")
                .setString("privilegeName", privilegeName).uniqueResult();
    }

 
}
