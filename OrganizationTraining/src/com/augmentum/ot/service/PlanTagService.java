package com.augmentum.ot.service;

import java.util.List;

import com.augmentum.ot.exception.ServerErrorException;
import com.augmentum.ot.model.PlanTag;


/** 
 *   PlanTagService interface provide methods about planTag
 *
 * @version V1.0, 2012-7-17  
 */
public interface PlanTagService {
	
	
	/** 
	 *  Search all Tag of plan.
	 * 
	 * @return
	 * @throws PlanServiceException  
	 */ 
	List<PlanTag> findAllPlanTags() throws ServerErrorException;


}
