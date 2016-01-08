package com.augmentum.ot.dao.impl;

import org.springframework.stereotype.Component;

import com.augmentum.ot.dao.ActualCourseAttachmentDao;
import com.augmentum.ot.model.ActualCourseAttachment;


/**
 * 
 * @version V1.0, 2012-12-20
 */
@Component("actualCourseAttachmentDao")
public class ActualCourseAttachmentDaoImpl extends BaseDaoImpl<ActualCourseAttachment> implements ActualCourseAttachmentDao {

	
    @Override
    public Class<ActualCourseAttachment> getEntityClass() {
        return ActualCourseAttachment.class;
    }
    
}
