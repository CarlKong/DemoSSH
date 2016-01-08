package com.augmentum.ot.dao.impl;

import org.springframework.stereotype.Component;

import com.augmentum.ot.dao.PlanAttachmentDao;
import com.augmentum.ot.model.PlanAttachment;
@Component("planAttachmentDao")
public class PlanAttachmentDaoImpl extends BaseDaoImpl<PlanAttachment>
		implements PlanAttachmentDao {

	@Override
	public Class<PlanAttachment> getEntityClass() {
		return PlanAttachment.class;
	}

}
