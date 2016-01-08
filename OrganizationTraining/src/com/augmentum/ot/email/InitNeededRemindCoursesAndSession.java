package com.augmentum.ot.email;

import java.util.Map;

import javax.servlet.ServletContext;

import com.augmentum.ot.dataObject.constant.JsonKeyConstants;
import com.augmentum.ot.exception.ServerErrorException;
import com.augmentum.ot.service.ActualCourseService;
import com.augmentum.ot.util.BeanFactory;

public class InitNeededRemindCoursesAndSession {

    private ServletContext servletContext;

    public InitNeededRemindCoursesAndSession(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    /**
     * Init needed remind plan course and session
     */
    public void InitNeededRemindData() {
        ActualCourseService actualCourseService = BeanFactory.getActualCourseService();
        try {
            Map<Integer, Long> planCourseMap = actualCourseService.getNeededRemideCourses();
            servletContext.setAttribute(JsonKeyConstants.PLAN_COURSE_REMIND, planCourseMap);
        } catch (ServerErrorException e) {
            // TODO
        }
    }

    /**
     * 
     * Add reminder of courses
     * 
     * @param newNeededRemindCourses
     */
    @SuppressWarnings("unchecked")
    public void addNeededRemindCourses(Map<Integer, Long> newNeededRemindCourses) {
        Map<Integer, Long> planCourseMap = (Map<Integer, Long>) servletContext.getAttribute(JsonKeyConstants.PLAN_COURSE_REMIND);
        for (Integer key : newNeededRemindCourses.keySet()) {
            planCourseMap.put(key, newNeededRemindCourses.get(key));
        }
    }

    /**
     * 
     * Remove reminder of course
     * 
     * @param key
     */
    @SuppressWarnings("unchecked")
    public void removePlanCoursesFromApplication(Integer key) {
        Map<Integer, Long> planCourseMap = (Map<Integer, Long>) servletContext.getAttribute(JsonKeyConstants.PLAN_COURSE_REMIND);
        planCourseMap.remove(key);
    }

    /**
     * 
     * Add reminder of session
     * 
     * @param newNeededRemindSessions
     */
    @SuppressWarnings("unchecked")
    public void addNeededRemindSession(Map<Integer, Long> newNeededRemindSessions) {
        Map<Integer, Long> planSessionMap = (Map<Integer, Long>) servletContext.getAttribute(JsonKeyConstants.PLAN_SESSION_REMIND);
        for (Integer key : newNeededRemindSessions.keySet()) {
            planSessionMap.put(key, newNeededRemindSessions.get(key));
        }
    }

    /**
     * 
     * Remove reminder of session
     * 
     * @param key
     */
    @SuppressWarnings("unchecked")
    public void removePlanSessionFromApplication(Integer key) {
        Map<Integer, Long> planSessionMap = (Map<Integer, Long>) servletContext.getAttribute(JsonKeyConstants.PLAN_SESSION_REMIND);
        planSessionMap.remove(key);
    }

}
