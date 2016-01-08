package com.augmentum.ot.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.augmentum.ot.dataObject.Page;
import com.augmentum.ot.model.Assessment;
import com.augmentum.ot.model.AssessmentItem;

public interface AssessmentDao extends BaseDao<Assessment> {
	
	/**
	 * Check the assessment has assessed or not
	 * 
	 * @param Integer planCourseId,Integer assessmentType
	 * @return boolean
	 */
    boolean isPlanCourseAssessed(Integer planCourseId,Integer assessmentType);
    
	/**
	 * Find the assessment count by certain property and its value
	 * 
	 * @param String property, Object value, int assessmentType
	 * @return int
	 */
	int findAssessmentsCountByProperty(String property, Object value, int assessmentType);
	
    /**
     * Find the assessment by certain property and its value
     * 
     * @param String property, Object value, int assessmentType
     * @return List<Assessment>
     */
	List<Assessment> findAssessmentsByProperty(String property, Object value, int assessmentType, Date startDate);
	
	/**
	 * Find the assessment by certain property and its value, also the paging information is required
	 * 
	 * @param String property, Object value, int assessmentType,int pageNow,int pageSize
	 * @return List<Assessment>
	 */
	List<Assessment> findAssessmentsByProperty(String property, Object value, int assessmentType,int pageNow,int pageSize);
	
    /**
     * Count the assessment, calculate the averageScore for the assessment
     * 
     * @param Map<String,Integer> assessmentFieldMap, Integer typeFlag
     * @return  List<AssessmentItem>
     */
    List<AssessmentItem> countAssessment(Map<String,Integer> assessmentFieldMap, Integer typeFlag);
    
    /**
     * Get the assessment list by assessmentFieldMap and the typeFlag is in the 
     * collection typeFlagList.
     * 
     * @param assessmentFieldMap  The key-value means the field-value.
     * @param typeFlagList  The typeFlag is in this collection.
     * @return  The assessment list.
     */
    List<Assessment> getAssessmentByFields(Map<String, Integer> assessmentFieldMap, List<Integer> typeFlagList);
    
    /**
     * Get the assessment page, the page contains an assessment list, the rowCount,
     * the nowPage and the pageSize.
     * 
     * @param assessmentFieldMap  The key-value means the field-value.
     * @param typeFlagList  The typeFlag is in this collection.
     * @param nowPage  The now page.
     * @param pageSize  The page size.
     * @return  The assessment page.
     */
    Page<Assessment> getAssessmentByFieldsAndPaging(Map<String, Integer> assessmentFieldMap, 
            List<Integer> typeFlagList, Integer nowPage, Integer pageSize);

	/**
	 * Get all assessments by the plan id.
	 * 
	 * @param Integer planId
	 * @return List<Assessment>
	 */
	List<Assessment> getAllAssessmentsByPlanId(Integer planId);
	
	/**
	 * Get all assessments to plan from trainers.
	 * @param planId
	 * @return List<Assessment>
	 */
	List<Assessment> getAssessmentTrainerToPlan(Integer planId);
	
	/**
	 * Get all assessments to trainers from master.
	 * @param planId
	 * @return List<Assessment>
	 */
	List<Assessment> getAssessmentMasterToTrainer(Integer planId);
	
	/**
	 * Get all assessments to plan from trainees.
	 * @param planId
	 * @return List<Assessment>
	 */
	List<Assessment> getAssessmentTraineeToPlan(Integer planId);
	
    /**
     * Get the trainer to planCourse type assessment.
     * @param Integer planCourseId
     * @return List<Assessment>
     */
    List<Assessment> getAssessmentTrainerToPlanCourse(Integer planCourseId);
    
    List<Assessment> getAssessmentTraineeToPlanCourse(Integer planCourseId);
	
    /**
     * Get the trainer to trainee kind of assessment by the trainerId and planCourseId;
     * @param Integer trainerId,Integer planCourseId
     * @return  List<Assessment>
     */
    List<Assessment> getAssessmentForTraineeByTrainer(Integer trainerId, Integer planCourseId);
    
    /**
     * findTraineeCountByStatusAndCourse
     * @param planCourseId
     * @param attendStatus
     * @return
     */
    int findTraineeCountByStatusAndCourse(int planCourseId, Double attendStatus);
    
}
