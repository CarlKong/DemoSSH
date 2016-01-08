package com.augmentum.ot.dao.impl;


import org.springframework.stereotype.Component;

import com.augmentum.ot.dao.PlanTypeDao;
import com.augmentum.ot.model.PlanType;




/**
 * 
 * @version 0.1, 07/30/2012
 */
@Component("planTypeDao")
public class PlanTypeDaoImpl extends BaseDaoImpl<PlanType> implements
        PlanTypeDao {

    @Override
    public Class<PlanType> getEntityClass() {
        return PlanType.class;
    }
}
