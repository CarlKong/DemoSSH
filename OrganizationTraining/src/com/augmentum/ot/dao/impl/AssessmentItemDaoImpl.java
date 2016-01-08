package com.augmentum.ot.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Component;

import com.augmentum.ot.dao.AssessmentItemDao;
import com.augmentum.ot.model.AssessmentItem;

/**
 * 
 * @Project OT 
 * @Modificaion_history 2012-10-10
 * @Version 1.0
 */
@Component("assessmentItemDao")
public class AssessmentItemDaoImpl extends BaseDaoImpl<AssessmentItem> implements AssessmentItemDao{

    @Override
    public Class<AssessmentItem> getEntityClass() {
        return AssessmentItem.class;
    }
 
    @SuppressWarnings("unchecked")
    @Override
    public List<AssessmentItem> getAssessmentItemByTypeId(
            Integer assessmentItemTypeId) {
        Query assessmentItemQuery = getSession().createQuery("from " + getEntityClass().getSimpleName()
                + " where assessItemType.assessItemTypeId=?");
        assessmentItemQuery.setInteger(0, assessmentItemTypeId);
        return assessmentItemQuery.list();
    }

}
