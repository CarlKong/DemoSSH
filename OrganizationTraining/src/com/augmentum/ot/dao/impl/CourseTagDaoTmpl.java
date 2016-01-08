package com.augmentum.ot.dao.impl;

import org.springframework.stereotype.Component;

import com.augmentum.ot.dao.CourseTagDao;
import com.augmentum.ot.model.CourseTag;

@Component("courseTagDao")
public class CourseTagDaoTmpl extends BaseDaoImpl<CourseTag> implements CourseTagDao {

    
    @Override
    public Class<CourseTag> getEntityClass() {
        return CourseTag.class;
    }
    
}
