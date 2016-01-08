package com.augmentum.ot.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.augmentum.ot.dao.PlanAttachmentDao;
import com.augmentum.ot.exception.ServerErrorException;
import com.augmentum.ot.model.PlanAttachment;
import com.augmentum.ot.service.PlanAttachmentService;

/** 
 * @ClassName: PlanAttachmentServiceImpl 
 * @date 2012-7-17 
 * @version V1.0 
 */
@Component("planAttachmentService")
public class PlanAttachmentServiceImpl implements PlanAttachmentService {
	
	private PlanAttachmentDao planAttachmentDao;
	
	public PlanAttachmentDao getPlanAttachmentDao() {
		return planAttachmentDao;
	}

	@Resource(name="planAttachmentDao")
	public void setPlanAttachmentDao(PlanAttachmentDao planAttachmentDao) {
		this.planAttachmentDao = planAttachmentDao;
	}

	@Override
	public PlanAttachment findPlanAttachmentById(Integer planAttachmentId)
			throws ServerErrorException {
		return planAttachmentDao.findByPrimaryKey(planAttachmentId);
	}
}
