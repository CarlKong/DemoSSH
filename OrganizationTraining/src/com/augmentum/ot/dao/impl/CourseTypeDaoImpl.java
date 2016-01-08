package com.augmentum.ot.dao.impl;

import org.springframework.stereotype.Component;

import com.augmentum.ot.dao.CourseTypeDao;
import com.augmentum.ot.model.CourseType;

@Component("courseTypeDao")
public class CourseTypeDaoImpl extends BaseDaoImpl<CourseType> implements
		CourseTypeDao {
	@Override
	public Class<CourseType> getEntityClass() {
		return CourseType.class;
	}

	/**
	 * find courseType by name
	 */
	public CourseType findCourseTypeByName(String courseTypeName) {
		return (CourseType) getSession().createQuery(
				"from " + getEntityClass().getSimpleName()
						+ " where typeName=:courseTypeName").setString(
				"courseTypeName", courseTypeName).uniqueResult();
	}
}
