package com.augmentum.ot.dao;

import java.util.List;

import com.augmentum.ot.model.AssessmentItem;

/**
 * @Project OT
 * @Modificaion_history 2012-10-10
 * @Version 1.0
 */
public interface AssessmentItemDao extends BaseDao<AssessmentItem> {

    /**
     * Get the assessmentItems by the assessmentType
     * 
     * @param AssessmentItemType
     *            assessmentItemType
     * @return List<AssessmentItem>
     */
    public List<AssessmentItem> getAssessmentItemByTypeId(
            Integer assessmentItemTypeId);

}
