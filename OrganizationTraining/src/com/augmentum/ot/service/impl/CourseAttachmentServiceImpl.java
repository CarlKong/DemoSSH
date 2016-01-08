package com.augmentum.ot.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.augmentum.ot.dao.CourseAttachmentDao;
import com.augmentum.ot.service.CourseAttachmentService;



@Component("courseAttachmentService")
public class CourseAttachmentServiceImpl implements CourseAttachmentService{

    private CourseAttachmentDao courseAttachmentDao;
    
    
    public CourseAttachmentDao getCourseAttachmentDao() {
        return courseAttachmentDao;
    }
    
    @Resource(name="courseAttachmentDao")
    public void setCourseAttachmentDao(CourseAttachmentDao courseAttachmentDao) {
        this.courseAttachmentDao = courseAttachmentDao;
    }

}
