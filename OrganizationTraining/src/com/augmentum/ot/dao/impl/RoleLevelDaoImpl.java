package com.augmentum.ot.dao.impl;

import org.springframework.stereotype.Component;
import com.augmentum.ot.dao.RoleLevelDao;
import com.augmentum.ot.model.RoleLevel;

@Component("roleLevelDao")
public class RoleLevelDaoImpl extends BaseDaoImpl<RoleLevel> implements
        RoleLevelDao {

    @Override
    public Class<RoleLevel> getEntityClass() {
        return RoleLevel.class;
    }

    @Override
    public RoleLevel getRoleLevelbyLevel(int level) {
        return (RoleLevel) getSession().createQuery(
                "from " + getEntityClass().getSimpleName()
                        + " where level=:level").setInteger("level", level)
                .uniqueResult();
    }
}
