package com.augmentum.ot.dao;

import java.util.Date;
import java.util.List;

import org.apache.lucene.search.QueryWrapperFilter;

import com.augmentum.ot.model.ActualCourse;



/**
 * A interface about ActualCourse,provide additional methods
 * 
 * @version 0.1, 12/18/2012
 */
public interface ActualCourseDao extends BaseDao<ActualCourse> {

	/**
	 * 
	 * @Title: findActualCoursesByPlanId  
	 * @Description: find actual courses by plan id.
	 *
	 * @param id
	 * @return
	 */
	List<ActualCourse> findActualCoursesByPlanId(Integer id);

	/**
	 * 
	 * @Title: findActualCoursesByCourseId  
	 * @Description: find actual courses by course id.
	 *
	 * @param id
	 * @return
	 */
	List<ActualCourse> findActualCoursesByCourseId(Integer id);
	
	/**
	 * 
	 * @Title: queryActualCourseByTrainer  
	 *
	 * @param trainerName
	 * @return
	 */
	List<ActualCourse> queryActualCourseByTrainer(String trainerName);
	
	/**
	 * 
	 * @Title: findEntityListByRemideTimer  
	 *
	 * @return
	 */
	List<ActualCourse> findEntityListByRemideTimer();
	
	/**
	 * 
	 * @Title: queryActualCourseFromIndex  
	 *
	 * @param query
	 * @param sortField
	 * @param reverse
	 * @param filter
	 * @param firstResult
	 * @param maxResult
	 * @return
	 */
	List<ActualCourse> queryActualCourseFromIndex(org.apache.lucene.search.Query query, String sortField,
            Boolean reverse, QueryWrapperFilter filter, int firstResult,
            int maxResult);
    
	/**
	 * 
	 * @Title: getNumByIndex  
	 *
	 * @param query
	 * @param filter
	 * @return
	 */
    int getNumByIndex(org.apache.lucene.search.Query query, QueryWrapperFilter filter);
    
    /**
     * 
     * @Title: getPlanCoursesInOnePlanByTrainer  
     * @Description: get trainer's plan course in a plan by his name and plan id
     *
     * @param trainerName
     * @param planId
     * @return
     */
    List<ActualCourse> getActualCoursesInOnePlanByTrainer (String trainerName, int planId);
	
	    
    List<ActualCourse> getActualCourseListOperateByTrainer (String trainerName, Date currentDate, Date validateDate);
    
    List<ActualCourse> getActualCourseListOperateByTrainee (String traineeName, Date currentDate, Date validateDate);
}
