package com.augmentum.ot.service;

import java.util.List;
import java.util.Map;

import com.augmentum.ot.dataObject.AssessmentForShow;
import com.augmentum.ot.dataObject.ViewAssessmentCondition;
import com.augmentum.ot.exception.DataWarningException;
import com.augmentum.ot.exception.ServerErrorException;
import com.augmentum.ot.model.Assessment;

public interface AssessmentService {

    /**
     * 
     * Save the assessment
     * 
     * @param assessments
     * @throws ServerErrorException
     */
     void saveAssessments(List<Assessment> assessments) throws ServerErrorException;
     
	/**
	 * Update an assessment
	 * 
	 * @param Assessment
	 *            assessment
	 * @return Assessment
	 * @throws ServerErrorException
	 */
    boolean updateAssessment(List<Assessment> assessments) throws ServerErrorException;
    
    /**
     * Find an assessment by its id
     * 
     * @param Integer assessmentId
     * @return Assessment 
    * @throws ServerErrorException 
    * @throws DataWarningException 
     */
    Assessment findAssessmentById(Integer assessmentId) throws ServerErrorException;
    
     /**
      * 
      * Get assessment and summary information for plan course which trainer give it
      * 
      * @param planId
      * 			Validate whether the plan course exists at the plan
      * @param planCourseId
      * @return
      * @throws DataWarningException
      * @throws ServerErrorException
      */
     AssessmentForShow getAssessmentsForActualCourseByTrainer(int planId, int actualCourseId) throws DataWarningException, ServerErrorException;
     
     /**
      * 
      * Get assessments and summary information for plan which trainee give it
      * 
      * @param planId
      * @param pageNow
      * @param pageSize
      * @return
      * @throws DataWarningException
      * @throws ServerErrorException
      */
     Map<String, Object> getAssessmentsForPlanByTrainee(int planId, int pageNow, int pageSize) throws DataWarningException, ServerErrorException;
    
     /**
      * 
      * Get assessment and summary information for plan which trainer give it
      * 
      * @param planId
      * @param pageNow
      * @param pageSize
      * @return
      * @throws DataWarningException
      * @throws ServerErrorException
      */
     Map<String, Object> getAssessmentsForPlanByTrainer(int planId, int pageNow, int pageSize) throws DataWarningException, ServerErrorException;
    
     /**
      * 
      * Get assessments and summary information for plan courses' assessment which trainee give it.
      * 
      * @param planId
      * 			Validate whether the plan course exists at the plan
      * @param planCourseId
      * @param pageNow
      * @param pageSize
      * @return
      * 		The result contains assessmentList and assessments' summary
      * @throws DataWarningException
      * @throws ServerErrorException
      */
     Map<String, Object> getAssessmentsForActualCourseByTrainee(int planId, int actualCourseId,
    		 		int pageNow, int pageSize) throws DataWarningException, ServerErrorException;
     
    /**
     * Get the assessment information according to planId and employeeId.
     * The result map contains behaviorRate, behaviorAverageScore, homeworkRate, homeworkAverageScore
     * traineeId, traineeName, traineePrefixId, attendTimes, lateTimes, leaveEarlyTimes, absenceTimes
     * assessmentList. And each item of assessmentList contains planCourseName, attendenceStatus, behaviorScore, 
     * homeworkScore, CommentsAndSuggestions.
     * 
     * @param planId  The plan id.
     * @param traineeId  The trainee id.
     * @param viewAssessmentCondition  View assessment condition.
     * @return  The map contains the assessment information.
     * @throws ServerErrorException
     * @throws DataWarningException
     */
    Map<String, Object> getAssessmentByPlanIdAndTraineeId(Integer planId,
            Integer traineeId, ViewAssessmentCondition viewAssessmentCondition)
            throws ServerErrorException, DataWarningException;
    
    /**
     * Get the assessment information according to planCourseId and planId.
     * The result map contains behaviorRate, behaviorAverageScore, homeworkRate, homeworkAverageScore
     * planCourseId, planCourseName, planAndPlanCoursePrefixId, attendNubmer, lateNubmer, leaveEarlyNubmer, absenceNubmer,
     * pageSize, nowPage, rowCount, pageCount and
     * assessmentList. And each item of assessmentList contains planCourseName, attendenceStatus, behaviorScore, 
     * homeworkScore, CommentsAndSuggestions.
     * 
     * @param planId  The plan id.
     * @param planCourseId  The plan course id.
     * @param viewAssessmentCondition  View assessment condition.
     * @return  The map contains the assessment information.
     * @throws ServerErrorException
     * @throws DataWarningException
     */
    Map<String, Object> getAssessmentByActualCourseId(Integer planId, 
            Integer actualCourseId, ViewAssessmentCondition viewAssessmentCondition) 
            throws ServerErrorException, DataWarningException;
    
    /**
     * Get the master to trainer assessment information according to planId and trainerId.
     * The result map contains masterName, responsibilityScore, provideAssessmentScore,
     * proactiveScore, trainingPreparationScore and commentsAndSuggestions.
     * 
     * @param planId  The plan id.
     * @param trainerId  The trainer id.
     * @param viewAssessmentCondition  View assessment condition.
     * @return  The map contains the assessment information for trainer in plan.
     * @throws ServerErrorException
     * @throws DataWarningException
     */
    Map<String, Object> getMasterToTrainerAssessmentByPlanIdAndTrainerId(Integer planId, 
            Integer trainerId, ViewAssessmentCondition viewAssessmentCondition)
            throws ServerErrorException, DataWarningException;
    
    /**
     * getTraineesToTrainerAssessmentByCourseIdAndTranerId
     * @param planCourseId
     * @param trainerId
     * @return
     * @throws ServerErrorException, DataWarningException
     */
    Map<String, Object> getTraineesToTrainerAssessmentByCourseIdAndTranerId(int actualCourseId, int trainerId, 
    		int pageNow, int pageSize) throws ServerErrorException, DataWarningException;
    
    /**
     * getAverageScoreToTrainerByCourseIdAndTranerId
     * @param planCourseId
     * @param trainerId
     * @return
     * @throws ServerErrorException
     * @throws DataWarningException
     */
    Map<String, Object> getAverageScoreToTrainerByCourseIdAndTranerId (int actualCourseId, int trainerId) throws ServerErrorException, DataWarningException;
    
    /** 
     * Get assessment by certain fields and its value
     * @param Map<String, Integer> assessmentFieldMap,
             Integer asssessmentTypeId
     * @return List<Assessment>
     * @throws ServerErrorException 
     */
    List<Assessment> getAssessmentByFields(Map<String, Integer> assessmentFieldMap,
            Integer asssessmentTypeId) throws ServerErrorException;
    
    /**
     * get trainer2plan assessment
     * @param planId
     * @param trainerId
     * @return
     */
    Assessment getOneTrainer2PlanAssessments (int planId, int trainerId) throws ServerErrorException;
    
    /**
     * get trainee2course assessments, include(trainee2trainer)
     * @param traineeId
     * @param planCourseId
     * @return
     */
    List<Assessment> getOneTrainee2CourseAssessments (int traineeId, int actualCourseId) throws ServerErrorException;
    
    /**
     * get master2trainer assessments
     * @param masterId
     * @param planId
     * @return
     */
    List<Assessment> getOneMaster2TrainerAssessments (int masterId, int planId) throws ServerErrorException;

    /**
     * Get the trainer to trainee kind of assessment by the planCourse Id and trainerId 
     * @param Integer trainerId, int planCourseId
     * @return List<Assessment>  
     * @throws DataWarningException   
     * @throws ServerErrorException 
     */
    List<Assessment> getAssessmentForTraineeByTrainer(Integer trainerId,
            Integer actualCourseId) throws ServerErrorException, DataWarningException;

    /**
     * Get trainee to plan assessment
     * @param int planId, int traineeId
     * @return Assessment
     */
    Assessment getOneTrainee2PlanAssessments(int planId, int traineeId) throws ServerErrorException;

    /**
     * Get trainer to course assessment
     * @param int planCourseId, Integer trainerId
     * @return Assessment
     * @throws ServerErrorException 
     */
    Assessment getOneTrainer2CourseAssessment(int actualCourseId,
            Integer trainerId) throws ServerErrorException;
}
