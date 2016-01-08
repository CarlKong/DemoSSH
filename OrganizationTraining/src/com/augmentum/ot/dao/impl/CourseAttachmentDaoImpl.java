package com.augmentum.ot.dao.impl;

import org.springframework.stereotype.Component;

import com.augmentum.ot.dao.CourseAttachmentDao;
import com.augmentum.ot.model.CourseAttachment;
@Component("courseAttachmentDao")
public class CourseAttachmentDaoImpl extends BaseDaoImpl<CourseAttachment>
		implements CourseAttachmentDao {

	@Override
	public Class<CourseAttachment> getEntityClass() {
		return CourseAttachment.class;
	}
}
