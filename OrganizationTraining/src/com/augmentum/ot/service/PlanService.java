package com.augmentum.ot.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import com.augmentum.ot.dataObject.CourseItem;
import com.augmentum.ot.dataObject.FromSearchToViewCondition;
import com.augmentum.ot.dataObject.Page;
import com.augmentum.ot.dataObject.PlanSearchCondition;
import com.augmentum.ot.exception.DataWarningException;
import com.augmentum.ot.exception.ServerErrorException;
import com.augmentum.ot.model.ActualCourse;
import com.augmentum.ot.model.Employee;
import com.augmentum.ot.model.Plan;
import com.augmentum.ot.model.PlanAttachment;
import com.augmentum.ot.model.PlanEmployeeMap;
import com.augmentum.ot.model.PlanType;


/** 
 *  PlanService interface provide methods about plan
 *
 * @version V1.0, 2012-7-17  
 */
public interface PlanService {

    
    /** 
     * create all plan indexes
     * 
     * @throws PlanServiceException  
     */ 
    void createAllPlanIndexes() throws ServerErrorException;
    
    /**
     * search Plan from index by keyword, type and so on.
     * 
     * @param PlanSearchCondition                               
     * @return Page<Plan>
     * @throws PlanServiceException
     */
    Page<Plan> searchPlansFromIndex(PlanSearchCondition psc) throws ServerErrorException;
      
    /** 
     *  Find plan by id, this search is not cascade.  
     * 
     * @param planId
     * @return
     * @throws PlanServiceException  
     */ 
    Plan findPlanById(Integer planId) throws DataWarningException, ServerErrorException;
    
  
    /** 
     *  Find plan by id, this search is cascade.  
     * 
     * @param planId
     * @return
     * @throws PlanServiceException  
     */ 
    Plan getPlanById(Integer planId) throws DataWarningException, ServerErrorException;
    
	 
	/** 
	 *  Create a plan use all the information.
	 * 
	 * @param plan
	 * @param planType
	 * @param planAttachments
	 * @param invitedTrainees
	 * @param optionalTrainees
	 * @param spectificTrainees
	 * @param isAllEmployeeAttend
	 * @param actualCourses
	 * @return
	 * @throws PlanServiceException  
	 */ 
	Integer createPlan(Plan plan, PlanType planType,
			List<PlanAttachment> planAttachments, String invitedTrainees,
			String optionalTrainees, String spectificTrainees,
			List<ActualCourse> actualCourses, HttpServletRequest request)
			throws DataWarningException, ServerErrorException;

	
	 
	/** 
	 *  update plan.
	 * 
	 * @param plan
	 * @param planType
	 * @param invitedTrainees
	 * @param optionalTrainees
	 * @param spectificTrainees
	 * @param isAllEmployeeAttend
	 * @param planCourses
	 * @param planAttachments
	 * @param delPlanCourses
	 * @return Boolean
	 *                 true or false
	 * @throws PlanServiceException  
	 */ 
	Boolean updatePlan(Plan plan, PlanType planType, String invitedTrainees,
			String optionalTrainees, String spectificTrainees,List<ActualCourse> actualCourses, List<PlanAttachment> planAttachments,
			List<ActualCourse> delActualCourses, HttpServletRequest request) throws DataWarningException, ServerErrorException;
	
	/** 
	 *  Just update status is 0, it mean that delete it on logic.
	 * 
	 * @param planId
	 * @return
	 * @throws PlanServiceException  
	 */ 
	Integer deletePlanById(Integer planId) throws DataWarningException, ServerErrorException;
  
	/** 
	 *  Publish Plan, update plan Status ispublish to 1.
	 * 
	 * @param planId
	 * @return
	 * @throws PlanServiceException  
	 */ 
	Integer updatePlanForPublishById(Integer planId) throws DataWarningException, ServerErrorException;
	 
	/** 
	 *  Cancel plan.
	 * 
	 * @param planId
	 * @param cancelPlanReason
	 * @return
	 * @throws PlanServiceException  
	 */ 
	Integer updatePlanForCancel(Integer planId, String cancelPlanReason) throws DataWarningException, ServerErrorException;
	
	/**
	 * Get plan's trainee list by plan employee map and attend type
	 * 
	 * @param pemList
	 * @param attend
	 * @return
	 */
	String getTraineeList(Set<PlanEmployeeMap> pemList, int attendType);
	
    /**
     * Use the searchPlanFromIndex method. According to the condition, get
     * related search results with only id.
     * @param csc: The plan search condition, include such as page num, page size, search key and so on. 
     * @return The related search results with only id
     */
    List<Integer> searchIdByConditionInIndex(PlanSearchCondition psc) throws ServerErrorException;

    /**
     * If need calculate the next record info and condition, use the method.
     * @param fstvc: The additional condition, include the current chosen id, backup id collection and total page number.
     *               It should be changed and return the reference.
     * @param psc: The plan search condition, include such as page number, page size, search key and so on.
     * @return If need a error, it is the error message. If no error, it is "".
     */
    String plusRecordId(FromSearchToViewCondition fstvc,
            PlanSearchCondition psc);

    /**
     * If need calculate the previous record info and condition, use the method.
     * @param fstvc: The additional condition, include the current chosen id, backup id collection and total page number.
     *               It should be changed and return the reference.
     * @param psc: The plan search condition, include such as page number, page size, search key and so on.
     * @return If need a error, it is the error message. If no error, it is "".
     */
    String subtractRecordId(FromSearchToViewCondition fstvc,
            PlanSearchCondition psc);

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
     * Employee join or quit plan
     * @param employee
     * @param planId
     * @param planCourseId
     * @param isJoin
     */
    void saveEmployeeJoinOrQuitPlan(Employee employee, int planId,
            int planCourseId, int isJoin) throws ServerErrorException, DataWarningException;
    
    
    public void queryPlanInfo(Page<Plan> planPage, PlanSearchCondition psc, Employee employee) throws ServerErrorException, DataWarningException;
    
    /**
     * Get plan informations by different roles, show on dashboard or my plan.
     * 
     * @param employee
     * @param psc
     * @return
     */
    Page<Plan> searchMyPlanFromIndex(Employee employee, PlanSearchCondition psc) throws DataWarningException, ServerErrorException;
    
    /**
     * Find the plan instance for view assessment page. The plan will contains 
     * plan course list and trainee list.
     * 
     * @param planId  The plan id.
     * @return  The instance of Plan.
     * @throws DataWarningException
     * @throws ServerErrorException
     */
    Plan findPlanByIdForViewAssessment(Integer planId) throws DataWarningException, ServerErrorException;
    
    /**
     * Find the trainer list for view assessment to trainer page.
     * Each trainer must contains employeeId and employeeName.
     * 
     * @param planId  The plan id.
     * @return  The employee collection
     * @throws DataWarningException
     * @throws ServerErrorException
     */
    Map<String, Object> findTrainerAndActualCourseListByPlanIdForViewAssessment(Integer planId) 
    		throws DataWarningException, ServerErrorException;
    
    /**
     * get new public plan
     * @param pageNow
     * @param pageSize
     * @param employee
     * @return
     * @throws ServerErrorException
     */
    Page<Plan> getNewPublishPlan(int pageNow, int pageSize, Employee employee) throws ServerErrorException;
    
    /**
     * getCourseListForPublishPlan
     * @param planId
     * @param employee
     * @return
     * @throws ServerErrorException
     * @throws DataWarningException
     */
    List<CourseItem> getCourseListForPublishPlan(int planId, Employee employee) 
    		throws ServerErrorException, DataWarningException;

}
