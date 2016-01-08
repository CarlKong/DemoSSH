package com.augmentum.ot.service;

import com.augmentum.ot.exception.ServerErrorException;
import com.augmentum.ot.model.PlanAttachment;

/** 
 *  Process all operations of Plan attachment. 
 *
 * @version V1.0, 2012-7-17  
 */
public interface PlanAttachmentService {
	
	/**
	 *  To search plan Attachment by attachment id, return a attachment object.   
	 * 
	 * @param planAttachmentId
	 * @return the result of search.
	 * @throws ServerErrorException
	 */
	PlanAttachment findPlanAttachmentById(Integer planAttachmentId) throws ServerErrorException;
	 


}
