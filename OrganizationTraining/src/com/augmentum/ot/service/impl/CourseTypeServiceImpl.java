package com.augmentum.ot.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.augmentum.ot.dao.CourseTypeDao;
import com.augmentum.ot.dataObject.constant.ErrorCodeConstants;
import com.augmentum.ot.dataObject.constant.LogConstants;
import com.augmentum.ot.exception.DataWarningException;
import com.augmentum.ot.exception.ServerErrorException;
import com.augmentum.ot.model.CourseType;
import com.augmentum.ot.service.CourseTypeService;
@Component("courseTypeService")
public class CourseTypeServiceImpl implements CourseTypeService {

	private Logger logger = Logger.getLogger(CourseTypeServiceImpl.class);
	private CourseTypeDao courseTypeDao;
	
	@Resource
	public void setCourseTypeDao(CourseTypeDao courseTypeDao) {
		this.courseTypeDao = courseTypeDao;
	}


	@Override
	public CourseType findCourseTypeById(int courseTypeId)
			throws DataWarningException, ServerErrorException {
		
		CourseType courseType = null;
		try {
			courseType = courseTypeDao.findByPrimaryKey(courseTypeId);
		} catch (Exception e) {
			logger.error(LogConstants.exceptionMessage("Find a course type[#"+courseTypeId+"]"), e);
			throw new ServerErrorException(ErrorCodeConstants.SERVER_ERROR);
		}
		if (null == courseType) {
			logger.warn("Don't Find the course type[#"+courseTypeId+"]");
			throw new DataWarningException(ErrorCodeConstants.NOT_EXISTED_COURSE_TYPE);
		}
		return courseType;
	}


	@Override
	public List<CourseType> findAllCourseType() {
		List<CourseType> listCourseType = new ArrayList<CourseType>();
		try {
			listCourseType = courseTypeDao.loadAll();
		} catch (Exception e) {
			logger.error(LogConstants.exceptionMessage("Load all course type"), e);
		}
		return listCourseType;
	}

}
