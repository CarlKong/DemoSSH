package com.augmentum.ot.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.augmentum.ot.dao.CourseTagDao;
import com.augmentum.ot.dataObject.constant.ErrorCodeConstants;
import com.augmentum.ot.dataObject.constant.LogConstants;
import com.augmentum.ot.exception.ServerErrorException;
import com.augmentum.ot.model.CourseTag;
import com.augmentum.ot.service.CourseTagService;

/**
 * @ClassName CourseTagServiceImpl
 * @version V1.0, 2012-08-15
 */

@Component("courseTagService")
public class CourseTagServiceImpl implements CourseTagService {
    
    private static Logger logger = Logger.getLogger(CourseTagServiceImpl.class);
    
    @Resource(name="courseTagDao")
    private CourseTagDao courseTagDao;
    
    @Override
    public List<CourseTag> findAllCourseTags() throws ServerErrorException {
        List<CourseTag> list = null;
        try {
            list = courseTagDao.loadAll();
        } catch (Exception e) {
        	logger.error(LogConstants.exceptionMessage("Find all course tags"), e);
			throw new ServerErrorException(ErrorCodeConstants.SERVER_ERROR);
        }
        return list;
    }
    
}
