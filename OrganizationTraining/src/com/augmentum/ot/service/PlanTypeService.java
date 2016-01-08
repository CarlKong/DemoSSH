package com.augmentum.ot.service;

import java.util.List;

import com.augmentum.ot.exception.ServerErrorException;
import com.augmentum.ot.model.PlanType;


/** 
 *   PlanTypeService interface provide methods about planType
 *
 * @version V1.0, 2012-7-17  
 */
public interface PlanTypeService {
 
	/** 
	 *  Search plan Type by id.
	 * 
	 * @param planTypeId
	 * @return
	 * @throws PlanServiceException  
	 */ 
	PlanType findPlanTypeById(Integer planTypeId) throws ServerErrorException;  
      
    /** 
     *  Search all type of plan.
     * 
     * @return
     * @throws ServerErrorException  
     */ 
    List<PlanType> findAllPlanTypes()throws ServerErrorException;
}
