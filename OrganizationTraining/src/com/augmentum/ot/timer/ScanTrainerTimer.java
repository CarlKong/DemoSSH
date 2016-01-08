package com.augmentum.ot.timer;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.augmentum.ot.service.CourseService;
import com.augmentum.ot.util.BeanFactory;

/**
 * update the course's history trainers at a certain time.
 * @author  MarcoQiu
 * @Project OT DEMO
 * @Modificaion_history 2012-9-24
 * @Version 1.0
 */
public class ScanTrainerTimer implements org.quartz.Job{

    private CourseService courseService;
    
    public void performTimer() throws Exception{ 
        courseService = BeanFactory.getCourseService();
        courseService.updateHistoryTrainerForCourse();
    }
    
    @Override 
    public void execute(JobExecutionContext arg0) throws JobExecutionException {
    }
}
