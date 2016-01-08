package com.augmentum.ot.service;

import java.util.List;

import com.augmentum.ot.exception.DataWarningException;
import com.augmentum.ot.exception.ServerErrorException;
import com.augmentum.ot.model.CourseType;

public interface CourseTypeService {
	
	/**
	 * 
	 *  find CourseType by Id
	 * 
	 * @param courseTypeId
	 * @return
	 * @throws DataWarningException
	 * @throws ServerErrorException
	 */
	CourseType findCourseTypeById(int courseTypeId)throws DataWarningException, ServerErrorException;
	
	/**
	 * 
	 *  find All CourseType
	 * 
	 * @return
	 */
	List<CourseType> findAllCourseType();

}
