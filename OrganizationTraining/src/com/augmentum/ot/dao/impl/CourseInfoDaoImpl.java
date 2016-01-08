package com.augmentum.ot.dao.impl;

import org.springframework.stereotype.Component;

import com.augmentum.ot.dao.CourseInfoDao;
import com.augmentum.ot.model.CourseInfo;


/**
 * 
 *
 * @version V1.0, 2012-12-20
 */
@Component("courseInfoDao")
public class CourseInfoDaoImpl extends BaseDaoImpl<CourseInfo> implements CourseInfoDao {

	
    @Override
    public Class<CourseInfo> getEntityClass() {
        return CourseInfo.class;
    }
    
}
