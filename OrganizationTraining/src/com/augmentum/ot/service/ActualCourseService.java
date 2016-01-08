package com.augmentum.ot.service;

import java.util.List;
import java.util.Map;

import com.augmentum.ot.dataObject.ActualCourseSearchCondition;
import com.augmentum.ot.dataObject.Page;
import com.augmentum.ot.exception.DataWarningException;
import com.augmentum.ot.exception.ServerErrorException;
import com.augmentum.ot.model.ActualCourse;
import com.augmentum.ot.model.Employee;



/**
 * 
 * @Description: ActualService interface provide methods about plan 
 *
 * @version V1.0, 2012-12-20
 */
public interface ActualCourseService {
	
	/**
	 * 
	 * @Title: getTemporaryActualCourseByIds  
	 * @Description: get temporary actualCourses for select course list when create or edit a plan
	 *
	 * @param ids
	 * 			1_C,1_PC,4_C,12_PC
	 * @return List<ActualCourse>
	 * @throws ServerErrorException
	 */
    List<ActualCourse> getTemporaryActualCoursesByIds(String ids) throws ServerErrorException;
    
    /**
     * 
     * @Title: getTraineesByPlanCourseId  
     * @Description: get trainees by actual course id
     *
     * @param planCourseId
     * @return
     * @throws ServerErrorException
     */
    List<Employee> getTraineesByActualCourseId(Integer actualCourseId) throws ServerErrorException, DataWarningException;

    /**
     * 
     * @Title: findPlanCourse  
     * @Description: search plan by id.this search is not cascade.
     *
     * @param planCourseId
     * @return
     * @throws ServerErrorException
     * @throws DataWarningException
     */
    ActualCourse findActualCourseById(Integer actualCourseId) throws ServerErrorException, DataWarningException; 
    
     
    /**
     * 
     * @Title: getPlanCourse  
     * @Description: get plan course detail information by plan course id
     *
     * @param planCourseId
     * @return
     * @throws DataWarningException
     * @throws ServerErrorException
     */
    ActualCourse getActualCourseById(Integer actualCourseId) throws DataWarningException, ServerErrorException ;
    
    /**
     * 
     * @Title: createAllActualCourseIndexes  
     * @Description: Create all actual course Indexes
     *
     * @throws ServerErrorException
     */
    void createAllActualCourseIndexes() throws ServerErrorException;
    
    /**
     * 
     * @Title: findActualCoursesByPlanId  
     * @Description: Search actual course related to plan that id is it.this search is cascade.
     *
     * @param planId
     * @return
     * @throws DataWarningException
     * @throws ServerErrorException
     */
    List<ActualCourse> findActualCoursesByPlanId(Integer planId) throws DataWarningException, ServerErrorException;
    
	/**
	 * 
	 * @Title: getNeededRemideCourses  
	 *
	 * @return
	 * @throws ServerErrorException
	 */
    Map<Integer, Long> getNeededRemideCourses() throws ServerErrorException;
    
    /**
     * 
     * @Title: searchActualCourseFromIndex  
     *
     * @param acsc
     * @return
     * @throws ServerErrorException
     * @throws DataWarningException
     */
    Page<ActualCourse> searchActualCourseFromIndex(ActualCourseSearchCondition acsc) throws ServerErrorException, DataWarningException ;
    
    void queryActualCourseInfo(Page<ActualCourse> actualCoursePage, ActualCourseSearchCondition actualCourseSearchCondition) throws ServerErrorException, DataWarningException ;
    
    /**
     * 
     * @Title: getPlanCoursesInOnePlanByTrainer  
     * @Description: get actual courses name from a  plan by trainer's id
     *
     * @param trainerName
     * @param planId
     * @return
     */
    String getActualCoursesInOnePlanByTrainer(String trainerName, int planId) throws ServerErrorException;
    
    /**
     * 
     * @Title: updatePlanCourse  
     * @Description: Update a actualCourse
     *
     * @param actualCourse
     */
    void updateActualCourse(ActualCourse actualCourse) throws ServerErrorException;
    
}
