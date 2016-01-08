package com.augmentum.ot.dao;

import com.augmentum.ot.model.CourseType;

/**
 * A interface about course,provide additional methods
 * 
 * @version 0.1, 07/25/2012
 */
public interface CourseTypeDao extends BaseDao<CourseType>{
	
	/**
     * find courseType by name
     */
    public CourseType findCourseTypeByName(String courseTypeName);
}
