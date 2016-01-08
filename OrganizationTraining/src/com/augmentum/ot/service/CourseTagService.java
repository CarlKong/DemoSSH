package com.augmentum.ot.service;

import java.util.List;

import com.augmentum.ot.exception.ServerErrorException;
import com.augmentum.ot.model.CourseTag;

/**
 * CourseTagService interface provide methods about courseTag
 * 
 * @version V1.0, 2012-08-15
 */
public interface CourseTagService {
    
    /**
     * Search all Tag of course.
     * 
     * @return
     * @throws CourseServiceException
     */
    List<CourseTag> findAllCourseTags() throws ServerErrorException;
}
