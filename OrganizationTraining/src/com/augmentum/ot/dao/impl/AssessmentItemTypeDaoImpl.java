package com.augmentum.ot.dao.impl;

import org.springframework.stereotype.Component;

import com.augmentum.ot.dao.AssessmentItemTypeDao;
import com.augmentum.ot.model.AssessmentItemType;

/**
 * 
 * @Project OT
 * @Modificaion_history 2012-10-10
 * @Version 2.0
 */
@Component("assessmentItemTypeDao")
public class AssessmentItemTypeDaoImpl extends BaseDaoImpl<AssessmentItemType> implements AssessmentItemTypeDao{

    @Override
    public Class<AssessmentItemType> getEntityClass() {
        return AssessmentItemType.class;
    }

}
