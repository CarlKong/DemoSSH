package com.augmentum.ot.dao.impl;


import org.springframework.stereotype.Component;

import com.augmentum.ot.dao.PlanTagDao;
import com.augmentum.ot.model.PlanTag;




@Component("planTagDao")
public class PlanTagDaoImpl extends BaseDaoImpl<PlanTag> implements
        PlanTagDao {


    @Override
    public Class<PlanTag> getEntityClass() {
        return PlanTag.class;
    }
}
