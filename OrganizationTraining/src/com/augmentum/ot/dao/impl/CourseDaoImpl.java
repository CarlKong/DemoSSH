package com.augmentum.ot.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Component;

import com.augmentum.ot.dao.CourseDao;
import com.augmentum.ot.model.Course;


/**
 * 
 * @version 0.1, 07/13/2012
 */
@Component("courseDao")
public class CourseDaoImpl extends BaseDaoImpl<Course> implements CourseDao {

    @Override
    public Class<Course> getEntityClass() {
        return Course.class;
    }
 
    @SuppressWarnings("unchecked")
    @Override
    public List<Course> findUndeletedCourse() {
        Query courseQuery = getSession().createQuery("from " + getEntityClass().getSimpleName()
                + " where courseIsDeleted=?");
        courseQuery.setInteger(0, 0);
        return courseQuery.list();
    }
    
}
