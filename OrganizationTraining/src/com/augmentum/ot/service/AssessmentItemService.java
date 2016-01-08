package com.augmentum.ot.service;

import java.util.List;

import com.augmentum.ot.exception.ServerErrorException;
import com.augmentum.ot.model.AssessmentItem;

/**
 * AssessmentItemService
 * @Project OT
 * @Modificaion_history 2012-10-10
 * @Version 1.0 
 */
public interface AssessmentItemService {
    /**
     * Get the AssessmentItem by the AssessmentItemType id.
     * 
     * @param Integer assessmentItemTypeId
     * @return List<AssessmentItem>
     * @throws ServerErrorException 
     */
    List<AssessmentItem> getAssessmentItemByTypeId(Integer assessmentItemTypeId) throws ServerErrorException;
}
