package com.augmentum.ot.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.augmentum.ot.dataObject.CourseSearchCondition;
import com.augmentum.ot.dataObject.FromSearchToViewCondition;
import com.augmentum.ot.dataObject.Page;
import com.augmentum.ot.exception.DataWarningException;
import com.augmentum.ot.exception.ServerErrorException;
import com.augmentum.ot.model.Course;
import com.augmentum.ot.model.CourseAttachment;


/**
 * @Description: CourseService interface provide methods about course
 * @version 0.1, 07/16/2012
 */
public interface CourseService {

    /**
     * @Description: create all course indexes
     * @throws CourseServiceException
     */
    void createAllCourseIndexes() throws ServerErrorException;
    
    /**
     * @Description: search course from index by keyword, type and so on.
     * @param CourseSearchCondition
     *                              csc
     * @return Page<Course>
     * @throws ServerErrorException
     */
    Page<Course> searchCoursesFromIndex(CourseSearchCondition csc) throws ServerErrorException;
    
     /** 
     * @Description: create course
     * @param course
     * @param attachments
     * @return int
     * @throws DataWarningException 
     * @throws ServerErrorException 
     */ 
    Integer createCourse(Course course, List<CourseAttachment> courseAttachments, HttpServletRequest request) throws DataWarningException, ServerErrorException;
    
     /** 
     * @Description: delete course by id
     * @param id
     * @return boolean
     *                 true or false
     * @throws DataWarningException 
     * @throws ServerErrorException 
     */ 
    Boolean deleteCourseById(Integer id) throws DataWarningException, ServerErrorException;
    
    
     /** 
     * @Description: find course by id. this needn't cascade.
     * @param id
     * @return Course
     *                course
     * @throws DataWarningException 
     * @throws ServerErrorException 
     */ 
    Course findCourseById(Integer id) throws DataWarningException, ServerErrorException;

    
     /** 
     * @Description: get course by id. this need cascade.
     * @param id
     * @return Course
     *                course
     * @throws DataWarningException 
     * @throws ServerErrorException 
     */ 
    Course getCourseById(Integer id) throws DataWarningException, ServerErrorException;
    
    
     /** 
     * @Description: update course
     * @param course
     * @param attachments
     * @return Boolean 
     *                 true or false
     * @throws CourseServiceException 
     */ 
    Course updateCourse(Course course, List<CourseAttachment> courseAttachments, HttpServletRequest request) throws DataWarningException, ServerErrorException;

    /**
     * Use the searchCoursesFromIndex method. According to the condition, get
     * related search results with only id.
     * @param csc: The course search condition, include such as page num, page size, search key and so on. 
     * @return The related search results with only id
     */
    List<Integer> searchIdByConditionInIndex(CourseSearchCondition csc) throws ServerErrorException;

    /**
     * If need calculate the next record info and condition, use the method.
     * @param fstvc: The additional condition, include the current chosen id, backup id collection and total page number.
     *               It should be changed and return the reference.
     * @param csc: The course search condition, include such as page number, page size, search key and so on.
     * @return If need a error, it is the error message. If no error, it is "".
     */
    String plusRecordId(FromSearchToViewCondition fstvc,
            CourseSearchCondition csc);

    /**
     * If need calculate the previous record info and condition, use the method.
     * @param fstvc: The additional condition, include the current chosen id, backup id collection and total page number.
     *               It should be changed and return the reference.
     * @param csc: The course search condition, include such as page number, page size, search key and so on.
     * @return If need a error, it is the error message. If no error, it is "".
     */
    String subtractRecordId(FromSearchToViewCondition fstvc,
            CourseSearchCondition csc);

    /**
     * Get a flag whether the record has previous record.
     * @param fstvc: The additional condition, include the current chosen id, backup id collection and total page number.
     * @return If the record has previous record, it is 0. If no previous record, it is -1.
     */
    int getPreviousRecordFlag(FromSearchToViewCondition fstvc);

    /**
     * Get a flag whether the record has next record.
     * @param fstvc: The additional condition, include the current chosen id, backup id collection and total page number.
     * @return If the record has next record, it is 0. If no next record, it is 1.
     */
    int getNextRecordFlag(FromSearchToViewCondition fstvc);

    /**
     * Find all the history trainer for the course by the way of scanning the plan_course table
     * @param 
     * @return
     * @throws ServerErrorException 
     */ 
    void updateHistoryTrainerForCourse() throws ServerErrorException;

}
