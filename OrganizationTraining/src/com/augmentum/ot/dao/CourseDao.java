package com.augmentum.ot.dao;


import java.util.List;

import com.augmentum.ot.model.Course;



/**
 * A interface about course,provide additional methods
 * 
 * @version 0.1, 07/13/2012
 */
public interface CourseDao extends BaseDao<Course> {

    /**
     * Find undeleted Courses
     * @param
     * @return List<Course>
     */
    public List<Course> findUndeletedCourse();

}
