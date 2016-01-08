package com.augmentum.ot.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.augmentum.ot.dao.ActualCourseDao;
import com.augmentum.ot.dao.AssessmentDao;
import com.augmentum.ot.dao.AssessmentItemDao;
import com.augmentum.ot.dao.EmployeeDao;
import com.augmentum.ot.dao.PlanDao;
import com.augmentum.ot.dataObject.AssessmentForShow;
import com.augmentum.ot.dataObject.AssessmentItemAverageForShow;
import com.augmentum.ot.dataObject.Page;
import com.augmentum.ot.dataObject.ScoreForShow;
import com.augmentum.ot.dataObject.ViewAssessmentCondition;
import com.augmentum.ot.dataObject.constant.ErrorCodeConstants;
import com.augmentum.ot.dataObject.constant.FlagConstants;
import com.augmentum.ot.dataObject.constant.JsonKeyConstants;
import com.augmentum.ot.dataObject.constant.LogConstants;
import com.augmentum.ot.exception.DataWarningException;
import com.augmentum.ot.exception.ServerErrorException;
import com.augmentum.ot.model.ActualCourse;
import com.augmentum.ot.model.Assessment;
import com.augmentum.ot.model.AssessmentItem;
import com.augmentum.ot.model.AssessmentScore;
import com.augmentum.ot.model.Employee;
import com.augmentum.ot.model.Plan;
import com.augmentum.ot.model.PlanEmployeeMap;
import com.augmentum.ot.service.ActualCourseService;
import com.augmentum.ot.service.AssessmentService;
import com.augmentum.ot.util.BeanFactory;

/**
 * @ClassName: AssessmentServiceImpl
 * @date 2012-8-28
 * @version V1.0
 */
@Component("assessmentService")
public class AssessmentServiceImpl implements AssessmentService {

    private static Logger logger = Logger.getLogger(AssessmentServiceImpl.class);
    @Resource(name = "assessmentDao")
    private AssessmentDao assessmentDao;

    @Resource
    private ActualCourseDao actualCourseDao;

    @Resource
    private PlanDao planDao;

    @Resource
    private EmployeeDao employeeDao;

    @Override
    public boolean updateAssessment(List<Assessment> assessments) throws ServerErrorException {
        if (assessments == null || assessments.isEmpty()) {
        	logger.error(LogConstants.objectIsNULLOrEmpty("assessments"));
            throw new ServerErrorException(ErrorCodeConstants.SERVER_VALIDATION_ERROR);
        }
        
        for (Assessment assessment : assessments) {
            try{
            	if(assessment.getHasBeenAssessed().equals(FlagConstants.ASSESSMENT_HAS_BEEN_ASSESSED) && assessment.getAssessScoreList() != null &&
                		assessment.getTypeFlag() != FlagConstants.TRAINER_TO_COURSE){
                    for(AssessmentScore assessmentScore:assessment.getAssessScoreList()){
                    	assessmentScore.setAssessment(assessment);
                    }
                    if (assessment.getAssessmentAttendLog() != null) {
                		assessment.getAssessmentAttendLog().setAssessment(assessment);
                	}
                }
                assessmentDao.updateObject(assessment);
            }catch (Exception e) {
            	logger.error(LogConstants.exceptionMessage("Update Assessment"), e);
            	throw new ServerErrorException(ErrorCodeConstants.SERVER_ERROR);
            }
        }
        return true;
    }

    @Override
    public Assessment findAssessmentById(Integer assessmentId)
            throws ServerErrorException {
        if (assessmentId == null || assessmentId <= 0) {
        	logger.error(LogConstants.objectIsNULLOrEmpty("Assessment Id"));
            throw new ServerErrorException(ErrorCodeConstants.SERVER_VALIDATION_ERROR);
        }
        Assessment assessment = null;
        try {
            assessment = assessmentDao.findByPrimaryKey(assessmentId);
        } catch (Exception e) {
        	logger.error(LogConstants.exceptionMessage("Find a assessment By assessment[#" + assessmentId + "]"), e);
        	throw new ServerErrorException(ErrorCodeConstants.SERVER_ERROR);
        }
        if (assessment == null) {
        	logger.error(LogConstants.pureMessage("Don't find a assessment By assessment[#" + assessmentId + "]"));
            throw new ServerErrorException(ErrorCodeConstants.SERVER_VALIDATION_ERROR);
        }
        if (assessment.getHasBeenAssessed().equals(FlagConstants.ASSESSMENT_HAS_BEEN_ASSESSED)) {
        	// trainer2course doesn't have scorelist
        	if (!(assessment.getTypeFlag().equals(FlagConstants.TRAINER_TO_COURSE))
        			&& assessment.getAssessScoreList() != null && assessment.getAssessScoreList().size() != 0) {
        		assessment.getAssessScoreList().get(0);
        	}
        } else {
        	assessment.setAssessScoreList(null);
        }            
        return assessment;
    }

    @Override
    public void saveAssessments(List<Assessment> assessments)
            throws ServerErrorException {
        if (assessments == null) {
        	logger.error(LogConstants.objectIsNULLOrEmpty("Assessments"));
            throw new ServerErrorException(ErrorCodeConstants.SERVER_VALIDATION_ERROR);
        }
        try {
        	// check whether the assessment has null assessmentScore
        	for (Assessment assessment : assessments) {
        		if (assessment.getAssessScoreList() != null) {
        			for (AssessmentScore assessmentScore : assessment.getAssessScoreList()) {
        				if (assessmentScore.getAssessScore() == null) {
        					logger.error(LogConstants.objectIsNULLOrEmpty("Assessment Score"));
        					throw new ServerErrorException(ErrorCodeConstants.SERVER_VALIDATION_ERROR);
        				}
        			}
        		}
            
        		if (assessment.getHasBeenAssessed().equals(FlagConstants.ASSESSMENT_HAS_BEEN_ASSESSED) && assessment.getAssessScoreList() != null) {
                	for (AssessmentScore assessmentScore : assessment.getAssessScoreList()) {
                        assessmentScore.setAssessment(assessment);
                    }
                	if (assessment.getAssessmentAttendLog() != null) {
                		assessment.getAssessmentAttendLog().setAssessment(assessment);
                	}
                }
                assessmentDao.saveObject(assessment);
        	}
        } catch (Exception e) {
        	logger.error(LogConstants.exceptionMessage("Save  assessments"), e);
        	throw new ServerErrorException(ErrorCodeConstants.SERVER_ERROR);
        }

    }

    @Override
    public Assessment getOneTrainer2PlanAssessments(int planId, int trainerId) throws ServerErrorException { 
    	if (planId <= 0 || trainerId <= 0) {
    		logger.warn(LogConstants.objectIsNULLOrEmpty("planId or trainerId"));
    		throw new ServerErrorException(ErrorCodeConstants.SERVER_VALIDATION_ERROR);
    	}
    	Assessment assessment = new Assessment();
        Map<String, Integer> assessmentFieldMap = new HashMap<String, Integer>();
        assessmentFieldMap.put(JsonKeyConstants.PLAN_ID, planId);
        assessmentFieldMap.put(JsonKeyConstants.TRAINER_ID, trainerId);
        List<Integer> typeFlagList = new ArrayList<Integer>();
        typeFlagList.add(FlagConstants.TRAINER_TO_PLAN);
        List<Assessment> assessmentList = null;
        try{
        	 assessmentList = assessmentDao.getAssessmentByFields(assessmentFieldMap, typeFlagList);
        }catch (Exception e) {
        	logger.error(LogConstants.exceptionMessage("Get an assessment from trainer to plan [#"+planId+" #"+trainerId+"]"), e);
        	throw new ServerErrorException(ErrorCodeConstants.SERVER_ERROR);
		}
        if (assessmentList != null && assessmentList.size() != 0) {
        	assessment = assessmentList.get(0);
        	if (assessment.getAssessScoreList() != null && assessment.getAssessScoreList().size() != 0) {
        		assessment.getAssessScoreList().get(0);
        	}
        	return assessment;
		} else {
			return null;
		}
    }

	@Override
	public Map<String, Object> getAssessmentsForPlanByTrainee(int planId,
			int pageNow, int pageSize) throws DataWarningException,
			ServerErrorException {
		return getAssessmentInfoForPlan(planId, pageNow, pageSize, FlagConstants.TRAINEE_TO_PLAN);
	}
    
    @Override
    public Assessment getOneTrainee2PlanAssessments(int planId, int traineeId) throws ServerErrorException {
    	if (traineeId <= 0 || planId <= 0) {
    		logger.warn(LogConstants.objectIsNULLOrEmpty("traineeId or planId"));
    		throw new ServerErrorException(ErrorCodeConstants.SERVER_VALIDATION_ERROR);
    	}
    	Assessment assessment = new Assessment();
        Map<String, Integer> assessmentFieldMap = new HashMap<String, Integer>();
        assessmentFieldMap.put(JsonKeyConstants.PLAN_ID, planId);
        assessmentFieldMap.put(JsonKeyConstants.TRAINEE_ID, traineeId);
        List<Integer> typeFlagList = new ArrayList<Integer>();
        typeFlagList.add(FlagConstants.TRAINEE_TO_PLAN);
        List<Assessment> assessmentList = null;
		try {
			assessmentList = assessmentDao.getAssessmentByFields(
			        assessmentFieldMap, typeFlagList);
		} catch (Exception e) {
			throw new ServerErrorException(ErrorCodeConstants.SERVER_ERROR);
		}
        if(assessmentList.size() == 0){
            return null;
        }else{
        	assessment = assessmentList.get(0);
        	if (assessment.getAssessScoreList() != null && assessment.getAssessScoreList().size() != 0) {
        		assessment.getAssessScoreList().get(0);
        	}
            return assessment;
        }
    }
    
    @Override
    public List<Assessment> getOneTrainee2CourseAssessments(int traineeId,
            int planCourseId) throws ServerErrorException {
    	if (traineeId <= 0 || planCourseId <= 0) {
    		logger.warn(LogConstants.objectIsNULLOrEmpty("traineeId or planCourseId"));
    		throw new ServerErrorException(ErrorCodeConstants.SERVER_VALIDATION_ERROR);
    	}
        Map<String, Integer> assessmentFieldMap = new HashMap<String, Integer>();
        assessmentFieldMap.put(JsonKeyConstants.TRAINEE_ID, traineeId);
        assessmentFieldMap.put(JsonKeyConstants.PLAN_COURSE_ID, planCourseId);
        List<Integer> typeFlagList = new ArrayList<Integer>();
        typeFlagList.add(FlagConstants.TRAINEE_TO_TRAINER);
        typeFlagList.add(FlagConstants.TRAINEE_TO_COURSE);
        List<Assessment> assessmentList = null;
		try {
			assessmentList = assessmentDao.getAssessmentByFields(assessmentFieldMap, typeFlagList);
		} catch (Exception e) {
			logger.error(LogConstants.exceptionMessage("Get an assessment from trainee to course [#"+traineeId+" #"+planCourseId+"]"), e);
        	throw new ServerErrorException(ErrorCodeConstants.SERVER_ERROR);
		}
        for (Assessment assessment:assessmentList) {
        	if (assessment.getAssessScoreList() != null && assessment.getAssessScoreList().size() != 0) {
        		assessment.getAssessScoreList().get(0);
        	}
        }
        return assessmentList;
    }

	public List<Assessment> getOneMaster2TrainerAssessments(
			int masterId, int planId) throws ServerErrorException{
		if (masterId <= 0 || planId <= 0) {
    		logger.warn(LogConstants.objectIsNULLOrEmpty("master Id or plan Id"));
    		throw new ServerErrorException(ErrorCodeConstants.SERVER_VALIDATION_ERROR);
    	}
		List<Assessment> assessments = new ArrayList<Assessment>();
		Map<String, Integer> assessmentFieldMap = new HashMap<String, Integer>();
		assessmentFieldMap.put(JsonKeyConstants.MASTER_ID, masterId);
		assessmentFieldMap.put(JsonKeyConstants.PLAN_ID, planId);
		List<Integer> typeFlagList = new ArrayList<Integer>();
		typeFlagList.add(FlagConstants.TRAINING_MASTER_TO_TRAINERS);
		List<Assessment> assessmentList = null;
		try {
			assessmentList = assessmentDao.getAssessmentByFields(assessmentFieldMap, typeFlagList);
		} catch (Exception e) {
			logger.error(LogConstants.exceptionMessage("Get an assessment from master to trainer [#"+masterId+" #"+planId+"]"), e);
        	throw new ServerErrorException(ErrorCodeConstants.SERVER_ERROR);
		}
		for (Assessment assessment:assessmentList) {
			if (assessment.getHasBeenAssessed().equals(FlagConstants.ASSESSMENT_HAS_BEEN_ASSESSED)
					&& assessment.getAssessScoreList() != null && assessment.getAssessScoreList().size() != 0) {
				assessment.getAssessScoreList().get(0);
			}
        	assessments.add(assessment);
        }
		return assessments;
	}

	@Override
	public Map<String, Object> getAssessmentsForPlanByTrainer(int planId,
			int pageNow, int pageSize) throws DataWarningException,
			ServerErrorException {
		return getAssessmentInfoForPlan(planId, pageNow, pageSize, FlagConstants.TRAINER_TO_PLAN);
	}
	
	/**
	 * Get trainer or trainees assessed plan info
	 * @param planId
	 * @param pageNow
	 * @param pageSize
	 * @return
	 * @throws DataWarningException 
	 */
	private Map<String, Object> getAssessmentInfoForPlan(int planId,
			int pageNow, int pageSize, int typeFlag) throws ServerErrorException, DataWarningException {
		Map<String, Object> assessmentToPlanInfo = new HashMap<String, Object>();
		Plan plan = null;
		ArrayList<String> traineeNameList = new ArrayList<String>();
		Set<String> trainerNameSet = new HashSet<String>();
		try{
			plan = planDao.findByPrimaryKey(planId);
			for (PlanEmployeeMap planEmployeeMap : plan.getPlanEmployeeMapList()) {
				if (planEmployeeMap.getPlanTraineeAttendType().equals(FlagConstants.ATTEND_TYPE_INVITED) ||
						planEmployeeMap.getPlanTraineeAttendType().equals(FlagConstants.ATTEND_TYPE_JOIN)
				) {
					traineeNameList.add(planEmployeeMap.getEmployee().getAugUserName());
				}
			}
			for (ActualCourse actualCourse : plan.getActualCourses()) {
				trainerNameSet.add(actualCourse.getCourseTrainer());
			}
		} catch (Exception e) {
			logger.error(LogConstants.exceptionMessage("find plan by id "), e);
            throw new ServerErrorException(ErrorCodeConstants.SERVER_ERROR);
		}
		if (plan.getPlanIsDeleted() == FlagConstants.IS_DELETED) {
			//TODO
			throw new DataWarningException("");
		}
		
		//get average score and put it into assessmentItems
		List<AssessmentItem> assessmentItems = null;
		Map<String, Integer> assessmentFieldMap = new HashMap<String, Integer>();
		assessmentFieldMap.put(JsonKeyConstants.PLAN_ID, planId);
		assessmentFieldMap.put(JsonKeyConstants.TYPE_FLAG, typeFlag);
		assessmentFieldMap.put(JsonKeyConstants.ASSESS_IS_DELETED, FlagConstants.UN_DELETED);
		assessmentFieldMap.put(JsonKeyConstants.IS_IGNORE, FlagConstants.UN_IGNORE);
		assessmentItems = assessmentDao.countAssessment(assessmentFieldMap, typeFlag);
		//get rate
		Map<Integer, String> rateMap = null;
		if  (typeFlag == FlagConstants.TRAINEE_TO_PLAN) {
			rateMap = getAssessmentsRateForPlanByTrainee(planId);
		}
		if (typeFlag == FlagConstants.TRAINER_TO_PLAN) {
			rateMap = getAssessmentsRateForPlanByTrainer(planId);
		}
		if (null != assessmentItems && rateMap.size() == assessmentItems.size()) {
			for (AssessmentItem assessmentItem : assessmentItems) {
				assessmentItem.setAssessmentItemRate(rateMap.get(assessmentItem.getAssessItemId()));
			}
		}
		//get assessment page
		List<Assessment> assessments = null;
        int totalRecords = 0;
        try {
            assessments = assessmentDao.findAssessmentsByProperty(JsonKeyConstants.PLAN_ID, planId, typeFlag, pageNow, pageSize);
            totalRecords = assessmentDao.findAssessmentsCountByProperty(JsonKeyConstants.PLAN_ID, planId, typeFlag);
        } catch (Exception e) {
            logger.error(e);
            throw new ServerErrorException(e);
        }
		Page<AssessmentForShow> assessmentPage = new Page<AssessmentForShow>();
	       assessmentPage.setNowPager(pageNow);
	       assessmentPage.setPageSize(pageSize);
	       assessmentPage.setTotalRecords(totalRecords);
	       List<AssessmentForShow> assessmentInfoList = new ArrayList<AssessmentForShow>();
	       for (Assessment assessment : assessments) {
	    	   AssessmentForShow assessmentForShow = new AssessmentForShow();
	    	   assessmentForShow.setAssessComment(assessment.getAssessComment());
	    	   assessmentForShow.setIsIgnore(assessment.getIsIgnore());
	    	   EmployeeDao employeeDao = BeanFactory.getEmployeeDao();
	    	   Employee employee = null;
	        	try {
	        		if (null == assessment.getTrainerId() || assessment.getTrainerId() == 0) {
	        			employee = employeeDao.findByPrimaryKey(assessment.getTraineeId());
	        		} else {
	        			employee = employeeDao.findByPrimaryKey(assessment.getTrainerId());
	        		}
	        		
	        	} catch (Exception e) {
	                logger.error(e);
	                throw new ServerErrorException(e);
	            }
	        	assessmentForShow.setEmployeeName(employee.getAugUserName());
	        	List<ScoreForShow> scoreInfoList = new ArrayList<ScoreForShow>();
	        	for (AssessmentScore assessmentScore : assessment.getAssessScoreList()) {
	        		ScoreForShow scoreForShow = new ScoreForShow();
	        		scoreForShow.setItemName(assessmentScore.getAssessmentItem().getAssessItemName());
	        		scoreForShow.setAssessScore(assessmentScore.getAssessScore());
	        		scoreInfoList.add(scoreForShow);
	        	}
	        	assessmentForShow.setScoreList(scoreInfoList);
	        	assessmentInfoList.add(assessmentForShow);
	        }
	        assessmentPage.setList(assessmentInfoList);
	        int totalPage = (totalRecords%pageSize == 0) ? (totalRecords/pageSize)
	                : (totalRecords/pageSize + 1);
	        assessmentPage.setTotalPage(totalPage);
		//put all info to assessmentToPlanInfo map.
	    if  (typeFlag == FlagConstants.TRAINEE_TO_PLAN) {
	    	findTraineesNotAssessPlan(plan, traineeNameList);
	    	assessmentToPlanInfo.put(JsonKeyConstants.NOT_ASSESSED_EMPLOYEE_LIST, traineeNameList);
		}
		if (typeFlag == FlagConstants.TRAINER_TO_PLAN) {
			findTrainersNotAssessPlan(plan, trainerNameSet);
			assessmentToPlanInfo.put(JsonKeyConstants.NOT_ASSESSED_EMPLOYEE_LIST, trainerNameSet);
		}	    
	    if (assessmentPage.getList().isEmpty()) {
	    	assessmentToPlanInfo.put(JsonKeyConstants.ASSESSMENT_NO_DATA, FlagConstants.ASSESSMENT_NO_DATA_VALUE);
	    } else {
	    	assessmentToPlanInfo.put(JsonKeyConstants.ASSESSMENT_NO_DATA, FlagConstants.ASSESSMENT_HAVE_DATA_VALUE);
	    	assessmentToPlanInfo.put(JsonKeyConstants.ASSESSMENT_ITEM_AVERAGE_RATE_LIST, assessmentItems);
			assessmentToPlanInfo.put(JsonKeyConstants.ASSESSMENT_PAGE, assessmentPage);
	    }
		return assessmentToPlanInfo;
	}
	
	/**
	 * Find the trainees not give plan assessment.
	 * @return
	 */
	private void findTraineesNotAssessPlan(Plan plan, ArrayList<String> traineeNameList) throws ServerErrorException {
		List<Assessment> assessmentsToPlan = null;
		List<String> trainerNameList = new ArrayList<String>();
		if (plan.getTrainers() != null && (!("".equals(plan.getTrainers())))) {
			trainerNameList = Arrays.asList(plan.getTrainers().split(FlagConstants.SPLIT_COMMA));
		}
		try {
			assessmentsToPlan = assessmentDao.getAssessmentTraineeToPlan(plan.getPlanId());
			if (assessmentsToPlan != null && !assessmentsToPlan.isEmpty()) {
				for (Assessment assessment : assessmentsToPlan) {
					EmployeeDao employeeDao = BeanFactory.getEmployeeDao();
			    	Employee employee = employeeDao.findByPrimaryKey(assessment.getTraineeId());
			    	if(traineeNameList.contains(employee.getAugUserName()) && 
							(!(assessment.getIsIgnore().equals(FlagConstants.IS_IGNORE)))) {
						traineeNameList.remove(employee.getAugUserName());
					}
				}
			}
			//Trainee is also a trainer or master of this plan.
			List<String> removeNames = new ArrayList<String>();
			for (String traineeName : traineeNameList) {
				if (trainerNameList.contains(traineeName) || traineeName.equals(plan.getPlanCreator())) {
					removeNames.add(traineeName);
				}
			}
			traineeNameList.removeAll(removeNames);
		} catch (Exception e) {
			logger.error("findTraineesNotAssessPlan function error:", e);
			throw new ServerErrorException(ErrorCodeConstants.SERVER_ERROR);
		}
		
	}
	
	private void findTrainersNotAssessPlan(Plan plan, Set<String> trainerNames) throws ServerErrorException {
		List<Assessment> assessmentsToPlan = null;
		try {
			assessmentsToPlan = assessmentDao.getAssessmentTrainerToPlan(plan.getPlanId());
			if (assessmentsToPlan != null && !assessmentsToPlan.isEmpty()) {
				for (Assessment assessment : assessmentsToPlan) {
					EmployeeDao employeeDao = BeanFactory.getEmployeeDao();
			    	Employee employee = employeeDao.findByPrimaryKey(assessment.getTrainerId());
			    	if(trainerNames.contains(employee.getAugUserName()) && 
							(!(assessment.getIsIgnore().equals(FlagConstants.IS_IGNORE)))) {
			    		trainerNames.remove(employee.getAugUserName());
					}
				}
			}
			//Trainee is also the master of this plan.
			if (trainerNames.contains(plan.getPlanCreator())) {
				trainerNames.remove(plan.getPlanCreator());
			}
		} catch (Exception e) {
			logger.error("findTrainersNotAssessPlan function error:", e);
			throw new ServerErrorException(ErrorCodeConstants.SERVER_ERROR);
		}
	}

	@Override
	public Map<String, Object> getAssessmentsForActualCourseByTrainee(int planId,
			int actualCourseId, int pageNow, int pageSize)
			throws DataWarningException, ServerErrorException {
		ActualCourseService actualCourseService = BeanFactory.getActualCourseService();
		ActualCourse actualCourse = actualCourseService.getActualCourseById(actualCourseId);
		if (actualCourse.getPlan().getPlanId() != planId) {
			throw new DataWarningException("");
		}
		List<Employee> employeeList = actualCourse.getEmployeeList();
		List<String> traineeNameList = new ArrayList<String>();
		for (Employee employee : employeeList) {
			traineeNameList.add(employee.getAugUserName());
		}
		Map<String, Object> assessmentToPlanCourseInfo = new HashMap<String, Object>();
		//get average score and put it into assessmentItems
		List<AssessmentItem> assessmentItems = null;
		Map<String, Integer> assessmentFieldMap = new HashMap<String, Integer>();
		assessmentFieldMap.put(JsonKeyConstants.PLAN_COURSE_ID, actualCourseId);
		assessmentFieldMap.put(JsonKeyConstants.TYPE_FLAG, FlagConstants.TRAINEE_TO_COURSE);
		assessmentFieldMap.put(JsonKeyConstants.ASSESS_IS_DELETED, FlagConstants.UN_DELETED);
		assessmentFieldMap.put(JsonKeyConstants.IS_IGNORE, FlagConstants.UN_IGNORE);
		assessmentItems = assessmentDao.countAssessment(assessmentFieldMap, FlagConstants.TRAINEE_TO_COURSE);
		//get rate
		Map<Integer, String> rateMap = getAssessmentRateForActualCourseByTrainee(actualCourseId);
		if (assessmentItems != null && rateMap != null && rateMap.size() == assessmentItems.size()) {
			for (AssessmentItem assessmentItem : assessmentItems) {
				assessmentItem.setAssessmentItemRate(rateMap.get(assessmentItem.getAssessItemId()));
			}
		}
		//get assessment page
		List<Assessment> assessments = null;
        int totalRecords = 0;
        try {
            assessments = assessmentDao.findAssessmentsByProperty(JsonKeyConstants.PLAN_COURSE_ID, actualCourseId, FlagConstants.TRAINEE_TO_COURSE, pageNow, pageSize);
            totalRecords = assessmentDao.findAssessmentsCountByProperty(JsonKeyConstants.PLAN_COURSE_ID, actualCourseId, FlagConstants.TRAINEE_TO_COURSE);
        } catch (Exception e) {
            logger.error(LogConstants.exceptionMessage("find assessment by property "), e);
            throw new ServerErrorException(ErrorCodeConstants.SERVER_ERROR);
        }
		Page<AssessmentForShow> assessmentPage = new Page<AssessmentForShow>();
	       assessmentPage.setNowPager(pageNow);
	       assessmentPage.setPageSize(pageSize);
	       assessmentPage.setTotalRecords(totalRecords);
	       List<AssessmentForShow> assessmentInfoList = new ArrayList<AssessmentForShow>();
	       for (Assessment assessment : assessments) {
	    	   AssessmentForShow assessmentForShow = new AssessmentForShow();
	    	   assessmentForShow.setIsIgnore(assessment.getIsIgnore());
	    	   assessmentForShow.setAssessComment(assessment.getAssessComment());
	    	   EmployeeDao employeeDao = BeanFactory.getEmployeeDao();
	    	   Employee employee = null;
	        	try {
	        		employee = employeeDao.findByPrimaryKey(assessment.getTraineeId());
	        	} catch (Exception e) {
	        		logger.error(LogConstants.exceptionMessage("Find employee by trainee[#" 
	        				+ assessment.getTraineeId() + "]"), e);
	            	throw new ServerErrorException(ErrorCodeConstants.SERVER_ERROR);
	            }
	        	assessmentForShow.setEmployeeName(employee.getAugUserName());
	        	List<ScoreForShow> scoreInfoList = new ArrayList<ScoreForShow>();
	        	for (AssessmentScore assessmentScore : assessment.getAssessScoreList()) {
	        		ScoreForShow scoreForShow = new ScoreForShow();
		        	scoreForShow.setItemName(assessmentScore.getAssessmentItem().getAssessItemName());
		        	scoreForShow.setAssessScore(assessmentScore.getAssessScore());
		        	scoreInfoList.add(scoreForShow);
	        	}
	        	assessmentForShow.setScoreList(scoreInfoList);
	        	assessmentInfoList.add(assessmentForShow);
	        }
	        assessmentPage.setList(assessmentInfoList);
	        int totalPage = (totalRecords%pageSize == 0) ? (totalRecords/pageSize)
	                : (totalRecords/pageSize + 1);
	        assessmentPage.setTotalPage(totalPage);
		//put all info to assessmentToPlanInfo map.
	    findTraineesNotAssessCourse(actualCourse, traineeNameList);
	    assessmentToPlanCourseInfo.put(JsonKeyConstants.NOT_ASSESSED_EMPLOYEE_LIST, traineeNameList);
	    if (assessmentPage.getList().size() == 0) {
	    	assessmentToPlanCourseInfo.put(JsonKeyConstants.ASSESSMENT_NO_DATA, FlagConstants.ASSESSMENT_NO_DATA_VALUE);
	    } else {
	    	assessmentToPlanCourseInfo.put(JsonKeyConstants.ASSESSMENT_NO_DATA, FlagConstants.ASSESSMENT_HAVE_DATA_VALUE);
	    	assessmentToPlanCourseInfo.put(JsonKeyConstants.ASSESSMENT_ITEM_AVERAGE_RATE_LIST, assessmentItems);
		    assessmentToPlanCourseInfo.put(JsonKeyConstants.ASSESSMENT_PAGE, assessmentPage);
	    }
		return assessmentToPlanCourseInfo;
	}
	
	private void findTraineesNotAssessCourse(ActualCourse actualCourse, List<String> traineeNames)throws ServerErrorException {
		List<Assessment> assessmentsToCourse = null;
		String trainerName = "";
		if (null != actualCourse.getCourseTrainer()) {
			trainerName = actualCourse.getCourseTrainer();
		}
		try {
			assessmentsToCourse = assessmentDao.getAssessmentTraineeToPlanCourse(actualCourse.getActualCourseId());
			if (assessmentsToCourse != null && !assessmentsToCourse.isEmpty()) {
				for (Assessment assessment : assessmentsToCourse) {
					EmployeeDao employeeDao = BeanFactory.getEmployeeDao();
			    	Employee employee = employeeDao.findByPrimaryKey(assessment.getTraineeId());
			    	if(traineeNames.contains(employee.getAugUserName()) && 
							(!(assessment.getIsIgnore().equals(FlagConstants.IS_IGNORE)))) {
			    		traineeNames.remove(employee.getAugUserName());
					}
				}
			}
		} catch (Exception e) {
			logger.error("findTraineesNotAssessPlan function error:", e);
			throw new ServerErrorException(ErrorCodeConstants.SERVER_ERROR);
		}
		if (!("".equals(trainerName)) && traineeNames.contains(trainerName)) {
			traineeNames.remove(trainerName);
		}
	}
	@Override
	public AssessmentForShow getAssessmentsForActualCourseByTrainer(
			int planId, int actualCourseId) throws DataWarningException,
			ServerErrorException {
		ActualCourse actualCourse = actualCourseDao.findByPrimaryKey(actualCourseId);
		if (actualCourse.getPlan().getPlanId() != planId) {
			throw new DataWarningException("");
		}
		List<Assessment> assessmentInfoList = assessmentDao.getAssessmentTrainerToPlanCourse(actualCourseId);
		if (assessmentInfoList == null || assessmentInfoList.size() != 1) {
			return null;
		}
		AssessmentForShow assessmentForShow = new AssessmentForShow();
		assessmentForShow.setIsIgnore(assessmentInfoList.get(0).getIsIgnore());
		assessmentForShow.setEmployeeName(actualCourse.getCourseTrainer());
		assessmentForShow.setAssessComment(assessmentInfoList.get(0).getAssessComment());
		return assessmentForShow;
	}

    @Override
    public Map<String, Object> getAssessmentByPlanIdAndTraineeId(
            Integer planId, Integer traineeId,ViewAssessmentCondition viewAssessmentCondition)
            throws ServerErrorException, DataWarningException {
        Map<String, Object> planTraineeAssessmentMap = new HashMap<String, Object>();
        Plan plan = null;
        List<ActualCourse> actualCourseList = null;
        List<ActualCourse> actualCourseOfTrianeeList = new ArrayList<ActualCourse>();
        List<String> trainerNameNotAssessList = new ArrayList<String>();
        try {
        	plan = planDao.findByPrimaryKey(planId);
        } catch (Exception e) {
			logger.error(LogConstants.exceptionMessage("find plan by id "), e);
			throw new ServerErrorException(ErrorCodeConstants.SERVER_ERROR);
		}
        boolean needAssessment = false;
        if (plan != null) {
        	actualCourseList = plan.getActualCourses();
        	for (ActualCourse actualCourse : actualCourseList) {
        		if (!needAssessment && null != actualCourse.getCourseInfo() &&
        				actualCourse.getCourseInfo().getTrainer2Trainee() == FlagConstants.NEED_ASSESSMENT) {
        			needAssessment = true;
        		}
        		for(Employee employee : actualCourse.getEmployeeList()) {
        			if (employee.getEmployeeId().equals(traineeId) && null != actualCourse.getCourseInfo() &&
        					actualCourse.getCourseInfo().getTrainer2Trainee() == FlagConstants.NEED_ASSESSMENT) {
        				actualCourseOfTrianeeList.add(actualCourse);
        				trainerNameNotAssessList.add(actualCourse.getCourseTrainer());
        				break;
        			}
        		}
        	}
        }
       if (needAssessment) {   //To a trainee, if one course need assessment, this trainee need assessment.
    	   // 1. Get the behaviorAverageScore.
           List<AssessmentItem> assessmentItems = null;
           Map<String, Integer> assessmentFieldMapForAvg = new HashMap<String, Integer>();
           assessmentFieldMapForAvg.put(JsonKeyConstants.PLAN_ID, planId);
           assessmentFieldMapForAvg.put(JsonKeyConstants.TRAINEE_ID, traineeId);
           assessmentFieldMapForAvg.put(JsonKeyConstants.TYPE_FLAG, FlagConstants.TRAINER_TO_TRAINEES);
           assessmentFieldMapForAvg.put(JsonKeyConstants.ASSESS_IS_DELETED, FlagConstants.UN_DELETED);
           assessmentFieldMapForAvg.put(JsonKeyConstants.IS_IGNORE, FlagConstants.UN_IGNORE);
           assessmentItems = assessmentDao.countAssessment(assessmentFieldMapForAvg, FlagConstants.TRAINER_TO_TRAINEES);
           // 2. Get the assessment list according to planId and traineeId.
           List<AssessmentForShow> assessmentResultList = null;
           // 1. Get assessment list from database according to planId and
           // traineeId.
           Map<String, Integer> assessmentFieldMap = new HashMap<String, Integer>();
           assessmentFieldMap.put(JsonKeyConstants.PLAN_ID, planId);
           assessmentFieldMap.put(JsonKeyConstants.TRAINEE_ID,traineeId);
           List<Integer> typeFlagList = new ArrayList<Integer>();
           typeFlagList.add(FlagConstants.TRAINER_TO_TRAINEES);
           List<Assessment> assessmentList = assessmentDao.getAssessmentByFields(assessmentFieldMap, typeFlagList);
        // 2. Get behaviorRate
           Map<Integer, String> rateMap = getAssessmentRateForTraineeCenterByTrainee(actualCourseOfTrianeeList, assessmentList, traineeId);
           List<AssessmentItemAverageForShow> assessmentItemAverageForShowList = dealWithAssessmentAverageScoreAndRate(assessmentItems, rateMap);
           Integer attendCount = 0;
           Integer lateCount = 0;
           Integer leaveCount = 0;
           Integer absentCount = 0;
           if (assessmentList != null && !assessmentList.isEmpty()) {
               // 2. Get the planCourseId from each assessment and packing them to a collection.
               // and Put all assessment to a map and each key is the planCourseId.
               Map<Integer, Assessment> assessmentMap = new HashMap<Integer, Assessment>();
               List<ActualCourse> actualCourseRelativeList = new ArrayList<ActualCourse>();
               
               for (Assessment assessment : assessmentList) {
                   for (ActualCourse actualCourse : actualCourseOfTrianeeList) {
                   	if (assessment != null && assessment.getHasBeenAssessed() == FlagConstants.ASSESSMENT_HAS_BEEN_ASSESSED
                   			&& assessment.getPlanCourseId().equals(actualCourse.getActualCourseId())) {
                   		actualCourseRelativeList.add(actualCourse);
                   		assessmentMap.put(assessment.getPlanCourseId(), assessment);
                   		trainerNameNotAssessList.remove(actualCourse.getCourseTrainer());
                   		if (assessment.getAssessmentAttendLog() != null &&
                           		assessment.getAssessmentAttendLog().getAttendLogKey().equals(JsonKeyConstants.ATTEND)) {
                           	attendCount ++;
                           }
                           if (assessment.getAssessmentAttendLog() != null &&
                           		assessment.getAssessmentAttendLog().getAttendLogKey().equals(JsonKeyConstants.LATE)) {
                           	lateCount ++;
                           }
                           if (assessment.getAssessmentAttendLog() != null &&
                           		assessment.getAssessmentAttendLog().getAttendLogKey().equals(JsonKeyConstants.LEAVE)) {
                           	leaveCount ++;
                           }
                           if (assessment.getAssessmentAttendLog() != null &&
                           		assessment.getAssessmentAttendLog().getAttendLogKey().equals(JsonKeyConstants.ABSENT)) {
                           	absentCount ++;
                           }
                   	}
                   }
               }

               // 4. Packing the assessment information with plan course name.
               assessmentResultList = packingAssessmentInfoWithActualCourseName(actualCourseRelativeList, assessmentMap);
           }
           Set<String> trainerNotAssessNameSet = new HashSet<String>();
           for (String trainerName : trainerNameNotAssessList) {
           	trainerNotAssessNameSet.add(trainerName);
           }
           planTraineeAssessmentMap.put(JsonKeyConstants.NOT_ASSESSED_EMPLOYEE_LIST, trainerNotAssessNameSet);
           planTraineeAssessmentMap.put(JsonKeyConstants.ATTEND, attendCount);
           planTraineeAssessmentMap.put(JsonKeyConstants.LATE, lateCount);
           planTraineeAssessmentMap.put(JsonKeyConstants.LEAVE, leaveCount);
           planTraineeAssessmentMap.put(JsonKeyConstants.ABSENT, absentCount);
           if (null == assessmentResultList || assessmentResultList.isEmpty()) {
           	planTraineeAssessmentMap.put(JsonKeyConstants.ASSESSMENT_NO_DATA, FlagConstants.ASSESSMENT_NO_DATA_VALUE);
           } else {
           	planTraineeAssessmentMap.put(JsonKeyConstants.ASSESSMENT_NO_DATA, FlagConstants.ASSESSMENT_HAVE_DATA_VALUE);
           	planTraineeAssessmentMap.put(JsonKeyConstants.ASSESSMENT_LIST,assessmentResultList);
           	planTraineeAssessmentMap.put(JsonKeyConstants.ASSESSMENT_ITEM_AVERAGE_RATE_LIST, assessmentItemAverageForShowList);
           }
           planTraineeAssessmentMap.put(JsonKeyConstants.NEED_ASSESSMENT, JsonKeyConstants.NEED_ASSESSMENT_VALUE);
       } else {
    	   planTraineeAssessmentMap.put(JsonKeyConstants.NEED_ASSESSMENT, JsonKeyConstants.NOT_NEED_ASSESSMENT_VALUE);
       }
        return planTraineeAssessmentMap;
    }
    
    /**
     * Packing the assessment information.
     * 
     * @return The packing assessment info.
     */
    private List<AssessmentForShow> packingAssessmentInfoWithActualCourseName(
            List<ActualCourse> actualCourseList,
            Map<Integer, Assessment> assessmentMap) {
        List<AssessmentForShow> assessmentResultList = new ArrayList<AssessmentForShow>();

        if (actualCourseList != null && !actualCourseList.isEmpty()) {
            // The assessment item.
            AssessmentForShow assessmentForShow = null;
            String planCourseName = null;

            // The temp params.
            Integer actualCourseId = null;
            Assessment assessment = null;

            for (ActualCourse actualCourse : actualCourseList) {
                if (actualCourse != null && actualCourse.getActualCourseId() != null) {
                	actualCourseId = actualCourse.getActualCourseId();
                    assessment = assessmentMap.get(actualCourseId);
                    if (assessment != null) {

                        // Get the planCourseName.
                        planCourseName = actualCourse.getCourseName();
                        assessmentForShow = dealWithAssessmentOwnItemInformationByDTO(assessment);
                        assessmentForShow.setPlanCourseName(planCourseName);

                        assessmentResultList.add(assessmentForShow);
                    }
                }
            }
        }

        return assessmentResultList;
    }

    /**
     * Packing the assessment information.
     * 
     * @return The packing assessment info.
     */
    private List<AssessmentForShow> packingAssessmentInfoWithTraineeName(
            List<Employee> traineeList, Map<Integer, Assessment> assessmentMap) {
        List<AssessmentForShow> assessmentResultList = new ArrayList<AssessmentForShow>();

        if (traineeList != null && !traineeList.isEmpty()) {
            // The assessment item.
            AssessmentForShow assessmentForShow = null;
            String traineeName = null;

            // The temp params.
            Integer traineeId = null;
            Assessment assessment = null;

            for (Employee trainee : traineeList) {
                if (trainee != null && trainee.getEmployeeId() != null) {
                    traineeId = trainee.getEmployeeId();
                    assessment = assessmentMap.get(traineeId);
                    if (assessment != null && assessment.getHasBeenAssessed() == FlagConstants.ASSESSMENT_HAS_BEEN_ASSESSED) {

                        // Get the traineeName.
                        traineeName = trainee.getAugUserName();
                        assessmentForShow = dealWithAssessmentOwnItemInformationByDTO(assessment);
                        assessmentForShow.setEmployeeName(traineeName);

                        assessmentResultList.add(assessmentForShow);
                    }
                }
            }
        }

        return assessmentResultList;
    }

    /**
     * Package the assessment item by class AssessmentForShow.
     * 
     * @param assessment
     * @return  The instance of AssessmentForShow.
     */
    private AssessmentForShow dealWithAssessmentOwnItemInformationByDTO(Assessment assessment) {
        AssessmentForShow assessmentForShow = new AssessmentForShow();
        /*
         * 1. Get the attendenceLogs and commentsAndSuggestions.
         * 2. Initialize the score item for assessment.
         * 3. Set the attendenceLogs, assessComment and score item list into assessmentForShow.
         */
        
        // 1. Get the attendenceLogs and assessComment.
        String assessComment = null;
        ScoreForShow behaviorScore = null;
        ScoreForShow homeworkScore = null;
        
        assessComment = assessment.getAssessComment();
        
        Integer assessmentItemId = null;
         // Initialize the score item for assessment.
        List<AssessmentScore> assessmentScoreList = assessment.getAssessScoreList();
        if (assessmentScoreList != null && !assessmentScoreList.isEmpty()) {
            for (AssessmentScore assessmentScore : assessmentScoreList) {
                if (assessmentScore != null&& assessmentScore.getAssessmentItem() != null
                        && assessmentScore.getAssessmentItem().getAssessItemId() != null) {
                    // Get the attendenceLogs, behaviorScore and homeworkScore.
                    assessmentItemId = assessmentScore.getAssessmentItem().getAssessItemId();
                    if (FlagConstants.ASSESSMENT_ITEM_TYPE_BEHAVIOR.equals(assessmentItemId)) {
                        // This item is for behavior score.
                        behaviorScore = new ScoreForShow();
                        behaviorScore.setItemName(assessmentScore.getAssessmentItem().getAssessItemName());
                        behaviorScore.setAssessScore(assessmentScore.getAssessScore());
                    } else if (FlagConstants.ASSESSMENT_ITEM_TYPE_HOMEWORK.equals(assessmentItemId)) {
                        // This item is for homework score.
                        homeworkScore = new ScoreForShow();
                        homeworkScore.setItemName(assessmentScore.getAssessmentItem().getAssessItemName());
                        homeworkScore.setAssessScore(assessmentScore.getAssessScore());
                    }
                }
            }
        }
        
        // 3. Set the attendenceLogs, assessComment and score item list into assessmentForShow.
        if (null != assessment.getAssessmentAttendLog()) {
        	assessmentForShow.setAttendenceLog(assessment.getAssessmentAttendLog().getAttendLogKey());
        }
        assessmentForShow.setAssessComment(assessComment);
        List<ScoreForShow> scoreList = new ArrayList<ScoreForShow>();
        scoreList.add(behaviorScore);
        scoreList.add(homeworkScore);
        assessmentForShow.setScoreList(scoreList);
        
        return assessmentForShow;
    }


    @Override
    public Map<String, Object> getAssessmentByActualCourseId(Integer planId,
            Integer actualCourseId, ViewAssessmentCondition viewAssessmentCondition)
            throws ServerErrorException, DataWarningException {
        Map<String, Object> planCourseAssessmentMap = new HashMap<String, Object>();
        ActualCourse actualCourse = null;
        List<Employee> employeeList = null; // All trainees.
        /*
         * 2. Get the assessment list according to planId and planCourseId. 
         * 3. Get the behaviorAverageScore, behaviorRate, homeworkAverageScore, behaviorAverageScore and attendenceLogs.
         */

        if (actualCourseId == null || actualCourseId <= 0) {
            // The planCourseId is null or is smaller than 0.
            logger.error(LogConstants.objectIsNULLOrEmpty("Actual course id"));
            throw new ServerErrorException(ErrorCodeConstants.SERVER_VALIDATION_ERROR);
        } 
        if (planId == null || planId <= 0) {
            // The planId is null or is smaller than 0.
            logger.error(LogConstants.objectIsNULLOrEmpty("plan Id"));
            throw new ServerErrorException(ErrorCodeConstants.SERVER_VALIDATION_ERROR);
        }
        try {
			actualCourse = actualCourseDao.findByPrimaryKey(actualCourseId);
			employeeList = actualCourse.getEmployeeList(); // All trainees.
		} catch (Exception e) {
			logger.error(e);
			throw new ServerErrorException(ErrorCodeConstants.SERVER_ERROR);
		}
		if (null != actualCourse.getCourseInfo() && actualCourse.getCourseInfo().getTrainer2Trainee() == FlagConstants.NEED_ASSESSMENT) {
            // 2. Get the assessment page info according to planId and
            // planCourseId.
            Map<String, Object> assessmentPageMap = dealWithAssessmentPageInfoForActualCourse(
                    planId, actualCourseId, viewAssessmentCondition);

            // Put the assessmentList, nowPage, pageSize, rowCount and pageCount
            // to planCourseAssessmentMap.
            planCourseAssessmentMap.putAll(assessmentPageMap);

            // 3. Get the behaviorAverageScore, behaviorRate, homeworkAverageScore, behaviorAverageScore and attendenceLogs.
            // Get attendenceLogs.
            Map<String, Integer> assessmentFieldMap = new HashMap<String, Integer>();
            assessmentFieldMap.put(JsonKeyConstants.PLAN_ID, planId);
            assessmentFieldMap.put(JsonKeyConstants.PLAN_COURSE_ID, actualCourseId);
            assessmentFieldMap.put(JsonKeyConstants.IS_IGNORE, FlagConstants.UN_IGNORE);
            List<Integer> typeFlagList = new ArrayList<Integer>();
            typeFlagList.add(FlagConstants.TRAINER_TO_TRAINEES);
            
            // Get average score, rate for behavior and homework.
            List<AssessmentItem> assessmentAverageScoreList = assessmentDao.countAssessment(assessmentFieldMap, FlagConstants.TRAINER_TO_TRAINEES);
            
            // Assume the data about rate.
            Map<Integer, String> assessmentRateMap = getAssessmentRateForTraineeCenterByCourse(planId, actualCourseId, actualCourse.getCourseTrainer());
            
            List<AssessmentItemAverageForShow> assessmentItemAverageList = dealWithAssessmentAverageScoreAndRate(assessmentAverageScoreList, assessmentRateMap);
            planCourseAssessmentMap.put(JsonKeyConstants.ASSESSMENT_ITEM_AVERAGE_RATE_LIST, assessmentItemAverageList);
            //Get have not been assessed trainees
            assessmentFieldMap.put(JsonKeyConstants.HAS_BEEN_ASSESSED, FlagConstants.ASSESSMENT_UNASSESSED);
            List<Assessment> notAssessedAssessmentList = assessmentDao.getAssessmentByFields(assessmentFieldMap, typeFlagList);
            List<Object> traineeIdList = new ArrayList<Object>();
            List<String> traineeNameList = new ArrayList<String>();
            if (null == assessmentAverageScoreList || assessmentAverageScoreList.isEmpty()) {
            	for (Employee employee : employeeList) {
            		traineeNameList.add(employee.getAugUserName());
            	}
            } else {
            	if (null != notAssessedAssessmentList && !notAssessedAssessmentList.isEmpty()) {
                	for (Assessment assessment : notAssessedAssessmentList) {
                		traineeIdList.add(assessment.getTraineeId());
                	}
                } 
                if (!traineeIdList.isEmpty()) {
                	List<Employee> notAssessedEmployeeList = employeeDao
                		.findEntityListByPropertyList(FlagConstants.EMPLOYEE_FIELDS_EMPLOYEE_ID, traineeIdList);
                	for (Employee employee : notAssessedEmployeeList) {
                		traineeNameList.add(employee.getAugUserName());
                	}
                }
            }
            if (null != actualCourse.getCourseTrainer()){
            	String trainerName = actualCourse.getCourseTrainer();
            	if (!("".equals(trainerName)) && traineeNameList.contains(trainerName)) {
            		traineeNameList.remove(trainerName);
            	}
            }
            planCourseAssessmentMap.put(JsonKeyConstants.NOT_ASSESSED_EMPLOYEE_LIST, traineeNameList);
            planCourseAssessmentMap.put(JsonKeyConstants.NEED_ASSESSMENT, JsonKeyConstants.NEED_ASSESSMENT_VALUE);
		} else {
			planCourseAssessmentMap.put(JsonKeyConstants.NEED_ASSESSMENT, JsonKeyConstants.NOT_NEED_ASSESSMENT_VALUE);
		}

        return planCourseAssessmentMap;
    }

    /**
     * Deal with the assessment item average and rate.
     * 
     * @param assessmentAverageScoreList  The assessment item average score collection.
     * @param assessmentRateList  The assessment rate collection.
     * @return  The AssessmentItemAverageForShow instance collection.
     */
    private List<AssessmentItemAverageForShow> dealWithAssessmentAverageScoreAndRate(
            List<AssessmentItem> assessmentAverageScoreList, Map<Integer, String> assessmentRateMap) {
        /*
         * 1. Iterate the assessmentAverageScoreList and package the information
         */
        List<AssessmentItemAverageForShow> assessmentItemAverageList = null;
        if (assessmentAverageScoreList != null && !assessmentAverageScoreList.isEmpty() 
                && assessmentRateMap != null && !assessmentRateMap.isEmpty()) {
            assessmentItemAverageList = new ArrayList<AssessmentItemAverageForShow>();
            AssessmentItemAverageForShow assessmentItemAverageForShow = null;
            for (AssessmentItem assessmentItem: assessmentAverageScoreList) {
                if (assessmentItem != null && assessmentItem.getAssessItemId() != null
                        && (FlagConstants.ASSESSMENT_ITEM_TYPE_BEHAVIOR.equals(assessmentItem.getAssessItemId())  
                                || FlagConstants.ASSESSMENT_ITEM_TYPE_HOMEWORK.equals(assessmentItem.getAssessItemId()))) {
                    assessmentItemAverageForShow = new AssessmentItemAverageForShow();
                    // Set the assessment item name and average score which come from assessment item.
                    assessmentItemAverageForShow.setAssessItemName(assessmentItem.getAssessItemName());
                    assessmentItemAverageForShow.setAvgScore(assessmentItem.getAvgScore());
                    
                    // Set the assessment rate which come from assessmentRateMap.
                    assessmentItemAverageForShow.setAssessmentItemRate(assessmentRateMap.get(assessmentItem.getAssessItemId()));
                    
                    assessmentItemAverageList.add(assessmentItemAverageForShow);
                }
            }
        }
        return assessmentItemAverageList;
    }
    
    /**
     * Deal with the assessment page info, the map contains assessment list,
     * nowPage, pageSize, rowCount and pageCount. Each assessment is packed in a
     * map.
     * 
     * @return The assessment list.
     */
    private Map<String, Object> dealWithAssessmentPageInfoForActualCourse(
            Integer planId, Integer actualCourseId, ViewAssessmentCondition viewAssessmentCondition) {
        Map<String, Object> assessmentPageMap = new HashMap<String, Object>();
        /*
         * 1. Get assessment list from database according to planId and
         * planCourseId. 2. Get the traineeId from each assessment and packing
         * them to a collection. and Put all assessment to a map and each key is
         * the traineeId. 3. Get the trainee list according to the traineeId
         * collection. 4. Packing the assessment information with trainee name.
         * 5. Put the assessment list, nowPage, pageSize, rowCount and pageCount
         * into assessmentPageMap.
         */

        // 1. Get assessment list from database according to planId and traineeId.
        List<AssessmentForShow> assessmentResultList = null;
        Map<String, Integer> assessmentFieldMap = new HashMap<String, Integer>();
        assessmentFieldMap.put(JsonKeyConstants.PLAN_ID, planId);
        assessmentFieldMap.put(JsonKeyConstants.PLAN_COURSE_ID, actualCourseId);
        assessmentFieldMap.put(JsonKeyConstants.HAS_BEEN_ASSESSED, FlagConstants.ASSESSMENT_HAS_BEEN_ASSESSED);
        // The type flag is trainerToTrainee.
        List<Integer> typeFlagList = new ArrayList<Integer>();
        typeFlagList.add(FlagConstants.TRAINER_TO_TRAINEES);

        Page<Assessment> assessmentPage = assessmentDao.getAssessmentByFieldsAndPaging(assessmentFieldMap, typeFlagList, viewAssessmentCondition.getNowPage(), viewAssessmentCondition.getPageSize());
        
        List<Assessment> assessmentList = assessmentPage.getList();
        Integer attendCount = 0;
        Integer lateCount = 0;
        Integer leaveCount = 0;
        Integer absentCount = 0;
        if (assessmentList != null && !assessmentList.isEmpty()) {
            // 2. Get the traineeId from each assessment and packing them to a collection.
            // and Put all assessment to a map and each key is the traineeId.
            Map<Integer, Assessment> assessmentMap = new HashMap<Integer, Assessment>();
            List<Object> traineeIdList = new ArrayList<Object>();
            Integer traineeId = null;
            for (Assessment assessment : assessmentList) {
                if (assessment != null && assessment.getPlanCourseId() != null) {
                    traineeId = assessment.getTraineeId();
                    traineeIdList.add(traineeId);
                    assessmentMap.put(traineeId, assessment);
                    if (assessment.getAssessmentAttendLog() != null &&
                    		assessment.getAssessmentAttendLog().getAttendLogKey().equals(JsonKeyConstants.ATTEND)) {
                    	attendCount ++;
                    }
                    if (assessment.getAssessmentAttendLog() != null &&
                    		assessment.getAssessmentAttendLog().getAttendLogKey().equals(JsonKeyConstants.LATE)) {
                    	lateCount ++;
                    }
                    if (assessment.getAssessmentAttendLog() != null &&
                    		assessment.getAssessmentAttendLog().getAttendLogKey().equals(JsonKeyConstants.LEAVE)) {
                    	leaveCount ++;
                    }
                    if (assessment.getAssessmentAttendLog() != null &&
                    		assessment.getAssessmentAttendLog().getAttendLogKey().equals(JsonKeyConstants.ABSENT)) {
                    	absentCount ++;
                    }
                }
            }

            // 3. Get the trainee list according to the traineeId collection.
            List<Employee> traineeList = employeeDao.findEntityListByPropertyList(FlagConstants.EMPLOYEE_FIELDS_EMPLOYEE_ID, traineeIdList);

            // 4. Packing the assessment information with trainee name.
            assessmentResultList = packingAssessmentInfoWithTraineeName(traineeList, assessmentMap);
        }
        assessmentPageMap.put(JsonKeyConstants.ATTEND, attendCount);
        assessmentPageMap.put(JsonKeyConstants.LATE, lateCount);
        assessmentPageMap.put(JsonKeyConstants.LEAVE, leaveCount);
        assessmentPageMap.put(JsonKeyConstants.ABSENT, absentCount);
        // Put the assessment list, nowPage, pageSize, rowCount and pageCount into assessmentPageMap.
        assessmentPageMap.put(JsonKeyConstants.ASSESSMENT_LIST, assessmentResultList);
        assessmentPage.setList(null);
        assessmentPageMap.put(JsonKeyConstants.ASSESSMENT_PAGE, assessmentPage);
        return assessmentPageMap;
    }

    @Override
    public Map<String, Object> getMasterToTrainerAssessmentByPlanIdAndTrainerId(
            Integer planId, Integer trainerId,
            ViewAssessmentCondition viewAssessmentCondition)
            throws ServerErrorException, DataWarningException {
        /*
         * 1. Get the master to trainer assessment according to planId andtrainerId. 
         * 2. Get the master name according to masterId in assessment.
         * 3. Put the data in masterToTrainerAssessmentMap.
         */

        Map<String, Object> masterToTrainerAssessmentMap = new HashMap<String, Object>();

        if (planId == null || planId <= 0) {
            // The planId is null or is smaller than 0.
            logger.error(LogConstants.objectIsNULLOrEmpty("Plan id"));
            throw new ServerErrorException(ErrorCodeConstants.SERVER_ERROR);
        } else if (trainerId == null || trainerId <= 0) {
            // The trainerId is null or is smaller than 0.
            logger.error(LogConstants.objectIsNULLOrEmpty("Trainer id"));
            throw new ServerErrorException(ErrorCodeConstants.SERVER_ERROR);
        } else {
            // 1. Get the master to trainer assessment according to planId and trainerId.
            Map<String, Integer> masterToTrainerFieldsMap = new HashMap<String, Integer>();
            masterToTrainerFieldsMap.put(JsonKeyConstants.PLAN_ID, planId);
            masterToTrainerFieldsMap.put(JsonKeyConstants.TRAINER_ID, trainerId);
            masterToTrainerFieldsMap.put(JsonKeyConstants.IS_IGNORE, FlagConstants.UN_IGNORE);
            List<Integer> typeFlagList = new ArrayList<Integer>();
            typeFlagList.add(FlagConstants.TRAINING_MASTER_TO_TRAINERS);
            List<Assessment> masterToTrainerAssessmentList = assessmentDao.getAssessmentByFields(masterToTrainerFieldsMap,typeFlagList);

            if (masterToTrainerAssessmentList != null&& masterToTrainerAssessmentList.size() == 1) {
                // One trainer in plan just has one master to trainer
                // assessment.
                Assessment assessment = masterToTrainerAssessmentList.get(0);
                if (assessment != null) {
                    // Put the responsibilityScore, provideAssessmentScore,
                    // proactiveScore,
                    // trainingPreparationScore and commentsAndSuggestions into
                    // masterToTrainerAssessmentMap.
                    masterToTrainerAssessmentMap.putAll(dealWithTheAssessmentForMasterToTrainer(assessment));

                    // 2. Get the master name according to masterId in
                    // assessment.
                    Integer masterId = assessment.getMasterId();
                    if (masterId != null) {
                        Employee master = employeeDao.findByPrimaryKey(masterId);
                        String masterName = master.getAugUserName();
                        masterToTrainerAssessmentMap.put(JsonKeyConstants.MASTER_NAME, masterName);
                    }
                }
            } else {
                masterToTrainerAssessmentMap.put(JsonKeyConstants.ASSESSMENT_NO_DATA, FlagConstants.ASSESSMENT_NO_DATA_VALUE);
            }
        }
        return masterToTrainerAssessmentMap;
    }
    
    /**
     * Deal with the assessment for master to trainer. The result map contains
     * responsibilityScore, provideAssessmentScore, proactiveScore,
     * trainingPreparationScore and commentsAndSuggestions.
     * 
     * @param assessmentThe assessment instance.
     * @return The map.
     */
    private Map<String, Object> dealWithTheAssessmentForMasterToTrainer(
            Assessment assessment) {
        Map<String, Object> masterToTrainerAssessmentMap = new HashMap<String, Object>();

        if (assessment != null) {
            List<AssessmentScore> assessmentScoreList = assessment.getAssessScoreList();
            String commentsAndSuggestions = assessment.getAssessComment();
            
            if (assessmentScoreList != null && !assessmentScoreList.isEmpty()) {
                List<ScoreForShow> scoreForShowList = new ArrayList<ScoreForShow>();
                for (AssessmentScore assessmentScore : assessmentScoreList) {
                    if (assessmentScore != null&& assessmentScore.getAssessmentItem() != null) {
                        ScoreForShow scoreForShow = new ScoreForShow();
                        scoreForShow.setAssessScore(assessmentScore.getAssessScore());
                        scoreForShow.setItemName(assessmentScore.getAssessmentItem().getAssessItemName());
                        scoreForShowList.add(scoreForShow);
                    }
                }
                // Put the data into masterToTrainerAssessmentMap.
                masterToTrainerAssessmentMap.put(JsonKeyConstants.ASSESSMENT_NO_DATA, FlagConstants.ASSESSMENT_HAVE_DATA_VALUE);
                masterToTrainerAssessmentMap.put(JsonKeyConstants.SCORE_LIST,scoreForShowList);
            }
            // If the assessment score list is null, the assessment also can
            // contains comments.
            masterToTrainerAssessmentMap.put(JsonKeyConstants.IS_IGNORE,assessment.getIsIgnore());
            masterToTrainerAssessmentMap.put(JsonKeyConstants.COMMENTS_AND_SUGGESTIONS,commentsAndSuggestions);
        } else {
        	masterToTrainerAssessmentMap.put(JsonKeyConstants.ASSESSMENT_NO_DATA, FlagConstants.ASSESSMENT_NO_DATA_VALUE);
        }
        return masterToTrainerAssessmentMap;
    }

    @Override
    public List<Assessment> getAssessmentForTraineeByTrainer(Integer trainerId,
            Integer planCourseId) throws ServerErrorException,
            DataWarningException {
        if (trainerId == null || planCourseId == null) {
        	logger.error(LogConstants.objectIsNULLOrEmpty("TrainerId or planCourseId"));
            throw new ServerErrorException(ErrorCodeConstants.SERVER_ERROR);
        }
        List<Assessment> assessments = new ArrayList<Assessment>();
        try {
            assessments = assessmentDao.getAssessmentForTraineeByTrainer(trainerId, planCourseId);
        } catch (Exception e) {
        	logger.error(LogConstants.exceptionMessage("Get assessment for trainee by trainer"), e);
            throw new ServerErrorException(ErrorCodeConstants.SERVER_ERROR);
        }
        for (Assessment assessment:assessments) {
            if(assessment.getAssessScoreList() != null && assessment.getAssessScoreList().size() != 0){
                assessment.getAssessScoreList().get(0);
            }
            if (assessment.getAssessmentAttendLog() != null) {
            	assessment.getAssessmentAttendLog();
            }
        }
        return assessments;
    }

    /**
     * Get the trainer to trainee kind of assessment rate
     * 
     * @param Integer traineeId
     * @return Map<Integer, String>
     */
    private Map<Integer, String> getAssessmentRateForTraineeCenterByTrainee(List<ActualCourse> actualCourseListOfTrainee,
            List<Assessment> assessmentList, Integer traineeId) throws ServerErrorException,
            DataWarningException {
    	Map<Integer, String> rate = new HashMap<Integer, String>();
        if (traineeId == null) {
        	logger.error(LogConstants.objectIsNULLOrEmpty("Trainee Id"));
            throw new ServerErrorException(ErrorCodeConstants.SERVER_ERROR);
        }
        List<AssessmentItem> assessmentScoreItemList = null;
        AssessmentItemDao assessmentItemDao = BeanFactory.getAssessmentItemDao();
        try {
            assessmentScoreItemList = assessmentItemDao.getAssessmentItemByTypeId(FlagConstants.TRAINER_TO_TRAINEES);
            
        } catch (Exception e) {
            logger.error(LogConstants.exceptionMessage("Find employee"), e);
            throw new ServerErrorException(ErrorCodeConstants.SERVER_ERROR);
        }
        int totalCount = actualCourseListOfTrainee.size();
        int unOptionalItemCount = 0;
        if (assessmentList != null) {
			for (Assessment assessment : assessmentList) {
				if (assessment.getHasBeenAssessed().equals(FlagConstants.ASSESSMENT_HAS_BEEN_ASSESSED)) {
					unOptionalItemCount ++;
				}
			}
		}
        if (null != assessmentScoreItemList) {
        	for (AssessmentItem assessmentItem :assessmentScoreItemList) {
            	if (assessmentItem.getIsOptional().equals(FlagConstants.UN_OPTIONAL)) {
            		rate.put(assessmentItem.getAssessItemId(), unOptionalItemCount + "/" + totalCount);
            	} else {
            		int optionalItemCount = 0;
            		int ratedItemCount = 0;
            		for (ActualCourse actualCourse : actualCourseListOfTrainee) {
            			Employee trainer = employeeDao.findEmployeeByName(actualCourse.getCourseTrainer());
            			if (judgeItemSelected(trainer.getEmployeeId(), actualCourse.getActualCourseId(), assessmentItem)) {
            				optionalItemCount++;
            				for (Assessment assessment : assessmentList) {
            					if (assessment.getPlanCourseId().equals(actualCourse.getActualCourseId()) &&
            							assessment.getHasBeenAssessed().equals(FlagConstants.ASSESSMENT_HAS_BEEN_ASSESSED)) {
            						ratedItemCount++;
            					}
            				}
            			}
            		}
            		rate.put(assessmentItem.getAssessItemId(), ratedItemCount + "/" + optionalItemCount);
            	}
            }
        }
        return rate;
    }
    //Judge an item is selected.
    private boolean judgeItemSelected(Integer trainerId, Integer actualCourseId, AssessmentItem assessmentItem) throws ServerErrorException {
    	List<Assessment> assessments = new ArrayList<Assessment>();
        try {
            assessments = assessmentDao.getAssessmentForTraineeByTrainer(trainerId, actualCourseId);
        } catch (Exception e) {
        	logger.error(LogConstants.exceptionMessage("Get assessment for trainee by trainer"), e);
            throw new ServerErrorException(ErrorCodeConstants.SERVER_ERROR);
        }
        if (assessments == null || assessments.isEmpty()) {
        	return true;
        }
    	for (Assessment assessment : assessments) {
    		if (assessment.getHasBeenAssessed().equals(FlagConstants.ASSESSMENT_HAS_BEEN_ASSESSED)) {
    			List<AssessmentScore> assessmentScoreList = assessment.getAssessScoreList();
        		if (assessmentScoreList != null && !(assessmentScoreList.isEmpty())) {
        			for (AssessmentScore assessmentScore : assessmentScoreList) {
            			if (assessmentScore.getAssessmentItem().getAssessItemId().equals(assessmentItem.getAssessItemId())) {
            				if (assessmentScore.getAssessScore().intValue() == FlagConstants.ITEM_UNSELECTED_SCORE) {
            					return false;
            				} else {
            					return true;
            				}
            			}
            		}
        		}
    		}
    	}
    	return true;
    }

    /**
     * Get the trainer to trainee kind of assessment rate
     * 
     * @param Integer traineeId
     * @return Map<Integer, String>
     */
    private Map<Integer, String> getAssessmentRateForTraineeCenterByCourse(Integer planId,
            Integer actualCourseId, String trainer) throws ServerErrorException,
            DataWarningException {
    	Map<Integer, String> rate = new HashMap<Integer, String>();
        if (planId == null || actualCourseId == null) {
        	logger.error(LogConstants.objectIsNULLOrEmpty("Trainee Id or Plan Id"));
            throw new ServerErrorException(ErrorCodeConstants.SERVER_ERROR);
        }
        List<Assessment> assessments = null;  
        List<AssessmentItem> assessmentScoreItemList = null;
        Map<String,Integer> assessmentFieldMap = new HashMap<String,Integer>();
        List<Integer> typeFlagList = new ArrayList<Integer>();
        assessmentFieldMap.put(JsonKeyConstants.PLAN_ID, planId);
        assessmentFieldMap.put(JsonKeyConstants.PLAN_COURSE_ID, actualCourseId);
        assessmentFieldMap.put(JsonKeyConstants.IS_IGNORE, FlagConstants.UN_IGNORE);
        typeFlagList.add(FlagConstants.TRAINER_TO_TRAINEES);
        AssessmentItemDao assessmentItemDao = BeanFactory.getAssessmentItemDao();
        EmployeeDao employeeDao = BeanFactory.getEmployeeDao();
        Employee trainerEmployee = null;
        try{
        	assessmentScoreItemList = assessmentItemDao.getAssessmentItemByTypeId(FlagConstants.TRAINER_TO_TRAINEES);
            assessments = assessmentDao.getAssessmentByFields(assessmentFieldMap, typeFlagList);
            trainerEmployee = employeeDao.findEmployeeByName(trainer);
        } catch (Exception e){
            logger.error(e);
            throw new ServerErrorException(e);
        }
        //The amount of has been assessed assessment
        int beenAssessedAmount = 0;
        //The trainee amount is the size of assessments
        int traineeAmount = assessments.size();
        if (assessments != null) {
			for (Assessment assessment : assessments) {
				if (assessment.getHasBeenAssessed().equals(FlagConstants.ASSESSMENT_HAS_BEEN_ASSESSED)) {
					beenAssessedAmount ++;
				}
			}
		}
        if (null != assessmentScoreItemList) {
        	for (AssessmentItem assessmentItem :assessmentScoreItemList) {
            	if (assessmentItem.getIsOptional().equals(FlagConstants.UN_OPTIONAL)) {
            		rate.put(assessmentItem.getAssessItemId(), beenAssessedAmount + JsonKeyConstants.RATE_SPLIT + traineeAmount);
            	} else {
            		int ratedItemCount = 0;
            		if (judgeItemSelected(trainerEmployee.getEmployeeId(), actualCourseId, assessmentItem)) {
        				for (Assessment assessment : assessments) {
        					if (assessment.getHasBeenAssessed().equals(FlagConstants.ASSESSMENT_HAS_BEEN_ASSESSED)) {
        						ratedItemCount++;
        					}
        				}
        				rate.put(assessmentItem.getAssessItemId(), ratedItemCount + JsonKeyConstants.RATE_SPLIT + traineeAmount);
            		}
            		rate.put(assessmentItem.getAssessItemId(), JsonKeyConstants.NONE_AVG_SCORE);
            	}
            }
        }
        return rate;
    }
    
    
    /**
     * Get the trainee to planCourse kind of assessment.
     * 
     * @param Integer planCourseId
     * @return Map<Integer, String>
     */
    private Map<Integer, String> getAssessmentRateForActualCourseByTrainee(
            Integer actualCourseId) throws DataWarningException,
            ServerErrorException {
        if (actualCourseId == null) {
        	logger.error(LogConstants.objectIsNULLOrEmpty("Actual course Id"));
            throw new ServerErrorException(ErrorCodeConstants.SERVER_VALIDATION_ERROR);
        }
        ActualCourse actualCourse = null;
        try {
        	actualCourse = actualCourseDao.findByPrimaryKey(actualCourseId);
        } catch (Exception e) {
            logger.error(LogConstants.exceptionMessage("find actual course by id "), e);
            throw new ServerErrorException(ErrorCodeConstants.SERVER_ERROR);
        }
        // The deserve amount of assessment
        int traineeAmount = actualCourse.getEmployeeList().size();
        // The actual amount of assessment
        int assessmentAmount = 0;
        try {
            assessmentAmount = assessmentDao.findAssessmentsCountByProperty(JsonKeyConstants.PLAN_COURSE_ID, actualCourse.getActualCourseId(), FlagConstants.TRAINEE_TO_COURSE);
        } catch (Exception e) {
            logger.error(LogConstants.exceptionMessage("Find assessment count trainee2course"), e);
            throw new ServerErrorException(ErrorCodeConstants.SERVER_ERROR);
        }
        //put the assessmentItem ID and its value into a Map and return the map
        Map<Integer, String> rate = new HashMap<Integer, String>();
        rate.put(FlagConstants.ASSESSMENT_ITEM_TYPE_TRAINING_MATERIAL,
                assessmentAmount + "/" + traineeAmount);
        rate.put(
                FlagConstants.ASSESSMENT_ITEM_TYPE_DIFFICULT_APPROPRIATE_TO_ME,
                assessmentAmount + "/" + traineeAmount);
        rate.put(FlagConstants.ASSESSMENT_ITEM_TYPE_VALUABLE_TO_ME,
                assessmentAmount + "/" + traineeAmount);
        return rate;
    }

    /**
     * Get the trainee to plan kind of assessment rate
     * 
     * @param Integer planId
     * @return Map<Integer, String>
     */
    private Map<Integer, String> getAssessmentsRateForPlanByTrainee(
            Integer planId) throws DataWarningException, ServerErrorException {
        if (planId == null) {
        	logger.error(LogConstants.objectIsNULLOrEmpty("Plan Id"));
            throw new ServerErrorException(ErrorCodeConstants.SERVER_VALIDATION_ERROR);
        }
        Plan plan = null;
        try {
            plan = planDao.findByPrimaryKey(planId);
        } catch (Exception e) {
            logger.error(LogConstants.exceptionMessage("Find a plan by Id"), e);
            throw new ServerErrorException(ErrorCodeConstants.SERVER_ERROR);
        }
        // The deserve amount of assessment
        int traineeAmount = plan.getPlanEmployeeMapList().size();
        // The actual amount of assessment
        int assessmentAmount = 0;
        try {
            assessmentAmount = assessmentDao.findAssessmentsCountByProperty(JsonKeyConstants.PLAN_ID,planId,FlagConstants.TRAINEE_TO_PLAN);
        } catch (Exception e) {
            logger.error(LogConstants.exceptionMessage("find assessments count trainee2plan"), e);
            throw new ServerErrorException(ErrorCodeConstants.SERVER_ERROR);
        }
        //put the assessmentItem ID and its value into a Map and return the map
        Map<Integer, String> rate = new HashMap<Integer, String>();
        rate.put(FlagConstants.ASSESSMENT_ITEM_TYPE_TRAINING_ARRAGEMENT,
                assessmentAmount + "/" + traineeAmount);
        rate.put(FlagConstants.ASSESSMENT_ITEM_TYPE_TRAINING_COURSE,
                assessmentAmount + "/" + traineeAmount);
        return rate;
    }

    /**
     * Get the trainer to plan kind of assessment rate
     * 
     * @param Integer planId
     * @return Map<Integer, String>
     */
    private Map<Integer, String> getAssessmentsRateForPlanByTrainer(Integer planId)
            throws DataWarningException, ServerErrorException {
        if (planId == null) {
        	logger.error(LogConstants.objectIsNULLOrEmpty("Plan Id"));
            throw new ServerErrorException(ErrorCodeConstants.SERVER_ERROR);
        }
        List<ActualCourse> actualCourses = null;
        try {
        	actualCourses = actualCourseDao.findActualCoursesByPlanId(planId);
        } catch (Exception e) {
            logger.error(LogConstants.exceptionMessage("find actual courses by plan id "), e);
            throw new ServerErrorException(ErrorCodeConstants.SERVER_ERROR);
        }
        if (actualCourses == null) {
        	logger.error(LogConstants.objectIsNULLOrEmpty("Plan Courses"));
            throw new ServerErrorException(ErrorCodeConstants.SERVER_VALIDATION_ERROR);
        }
        Set<String> trainers = new HashSet<String>();
        for (int i = 0; i < actualCourses.size(); i++) {
            String actualCourseTrainer = actualCourses.get(i).getCourseTrainer();
            if (!trainers.contains(actualCourseTrainer)) {
                trainers.add(actualCourseTrainer);
            }
        }
        // The deserve amount of assessment
        int trainerAmonut = trainers.size();
        // The actual amount of assessment
        int assessmentAmount = 0; 
        try {
            assessmentAmount = assessmentDao.findAssessmentsCountByProperty(JsonKeyConstants.PLAN_ID,planId,FlagConstants.TRAINER_TO_PLAN);
        } catch (Exception e) {
        	logger.error(LogConstants.exceptionMessage("find assessments count trainer2plan"), e);
            throw new ServerErrorException(ErrorCodeConstants.SERVER_ERROR);
        }
        //put the assessmentItem ID and its value into a Map and return the map
        Map<Integer,String> rate = new HashMap<Integer,String>();
        rate.put(FlagConstants.ASSESSMENT_ITEM_TYPE_PLAN_OBJECTIVES,
                assessmentAmount + "/" + trainerAmonut);
        rate.put(FlagConstants.ASSESSMENT_ITEM_TYPE_PLAN_TRAINING_ARRAGEMENT,
                assessmentAmount + "/" + trainerAmonut);
        rate.put(FlagConstants.ASSESSMENT_ITEM_TYPE_PLAN_TRAINING_COURSE,
                assessmentAmount + "/" + trainerAmonut);
        return rate;
    }

    /**
     * Get the trainee to trainer kind of assessment rate
     * 
     * @param Integer planCourseId, Integer trainerId
     * @return Map<Integer, String>
     */
    private Map<String, Object> getAssessmentsRateForTrainerByTrainee(Integer actualCourseId, Integer trainerId) throws ServerErrorException{
    	if (actualCourseId == null || trainerId == null) {
        	logger.error(LogConstants.objectIsNULLOrEmpty("trainer Id or actual course Id"));
            throw new ServerErrorException(ErrorCodeConstants.SERVER_ERROR);
        }
        if (actualCourseId == null) {
        	logger.error(LogConstants.objectIsNULLOrEmpty("actualCourseId"));
            throw new ServerErrorException(ErrorCodeConstants.SERVER_VALIDATION_ERROR);
        }
        ActualCourse actualCourse = null;
        try { 
            actualCourse = actualCourseDao.findByPrimaryKey(actualCourseId);
        } catch (Exception e) {
        	logger.error(LogConstants.exceptionMessage("find actual course by id "), e);
            throw new ServerErrorException(ErrorCodeConstants.SERVER_ERROR);
        }
        List<Employee> trainees = actualCourse.getEmployeeList();
        List<String> traineeNameList = new ArrayList<String>();
        for (Employee employee : trainees) {
        	traineeNameList.add(employee.getAugUserName());
        }
        List<Assessment> assessments = null;
        // The deserve amount of assessment
        int traineeAmonut = trainees.size();
        // The actual amount of assessment
        int assessmentAmount = 0; 
        Map<String, Integer> assessmentFieldMap = new HashMap<String,Integer>();
        assessmentFieldMap.put(JsonKeyConstants.TRAINER_ID, trainerId);
        assessmentFieldMap.put(JsonKeyConstants.PLAN_COURSE_ID, actualCourseId);
        assessmentFieldMap.put(JsonKeyConstants.IS_IGNORE, FlagConstants.UN_IGNORE);
        List<Integer> typeFlagList = new ArrayList<Integer>();
        typeFlagList.add(FlagConstants.TRAINEE_TO_TRAINER);
        try {
            assessments = assessmentDao
                    .getAssessmentByFields(assessmentFieldMap, typeFlagList);
            for (Assessment assessment : assessments) {
            	Employee employee = employeeDao.findByPrimaryKey(assessment.getTraineeId());
            	if (traineeNameList.contains(employee.getAugUserName())) {
            		traineeNameList.remove(employee.getAugUserName());
            	}
            }
        } catch (Exception e) {
            logger.error("Get assessments wrong:", e);
            throw new ServerErrorException(e);
        }
        assessmentAmount = assessments.size();
        //put the assessmentItem ID and its value into a Map and return the map
        Map<Integer,String> rate = new HashMap<Integer,String>();
        rate.put(FlagConstants.ASSESSMENT_ITEM_TYPE_PREPARATION,
                assessmentAmount + "/" + traineeAmonut);
        rate.put(FlagConstants.ASSESSMENT_ITEM_TYPE_EXPRESSION,
                assessmentAmount + "/" + traineeAmonut);
        rate.put(FlagConstants.ASSESSMENT_ITEM_TYPE_INTERACTION,
                assessmentAmount + "/" + traineeAmonut);
        rate.put(FlagConstants.ASSESSMENT_ITEM_TYPE_TIME_MAGAGEMENT,
                assessmentAmount + "/" + traineeAmonut);
        Map<String, Object> needAssessorAndRate = new HashMap<String, Object>();
        needAssessorAndRate.put(JsonKeyConstants.TRAINEE_LIST, traineeNameList);
        needAssessorAndRate.put(JsonKeyConstants.RATE, rate);
        return needAssessorAndRate;
    }

    @Override 
    public Assessment getOneTrainer2CourseAssessment(int planCourseId,
            Integer trainerId) throws ServerErrorException {
    	
        List<Assessment> assessments = new ArrayList<Assessment>();
        Map<String,Integer> assessmentFieldMap = new HashMap<String,Integer>();
        List<Integer> typeFlagList = new ArrayList<Integer>();
        assessmentFieldMap.put(JsonKeyConstants.PLAN_COURSE_ID, planCourseId);
        assessmentFieldMap.put(JsonKeyConstants.TRAINER_ID, trainerId);
        typeFlagList.add(FlagConstants.TRAINER_TO_COURSE);
        try {
            assessments = assessmentDao.getAssessmentByFields(assessmentFieldMap, typeFlagList);
        } catch (Exception e) {
        	logger.error(LogConstants.exceptionMessage("Get assessment by fields"), e);
            throw new ServerErrorException(ErrorCodeConstants.SERVER_ERROR);
        }
        if(assessments.isEmpty()){
            return null;
        }else{
            return assessments.get(0);
        }
    }
    
    @Override
	public Map<String, Object> getTraineesToTrainerAssessmentByCourseIdAndTranerId(
			int actualCourseId, int trainerId, int pageNow, int pageSize) throws ServerErrorException,
			DataWarningException {
    	Map<String, Object> assessmentToTrainerInfo = new HashMap<String, Object>();
		//get average score and put it into assessmentItems
    	Map<String, Object> averageScoreAndTraineesMap = getAverageScoreToTrainer(actualCourseId, trainerId);
		//get assessment page
		Page<Assessment> assessmentsPage = null;
		ActualCourse actualCourse = null;
        try {
        	Map<String, Integer> assessmentFields = new HashMap<String, Integer>();
        	assessmentFields.put(JsonKeyConstants.PLAN_COURSE_ID, actualCourseId);
        	assessmentFields.put(JsonKeyConstants.TRAINER_ID, trainerId);
        	assessmentFields.put(JsonKeyConstants.ASSESS_IS_DELETED, FlagConstants.UN_DELETED);
        	assessmentFields.put(JsonKeyConstants.IS_IGNORE, FlagConstants.UN_IGNORE);
        	List<Integer> typeList = new ArrayList<Integer>();
        	typeList.add(FlagConstants.TRAINEE_TO_TRAINER);
        	assessmentsPage = assessmentDao.getAssessmentByFieldsAndPaging(assessmentFields, typeList, pageNow, pageSize);
        	actualCourse = actualCourseDao.findByPrimaryKey(actualCourseId);
        } catch (Exception e) {
            logger.error(e);
            throw new ServerErrorException(e);
        }
        if (actualCourse.getCourseInfo() != null && actualCourse.getCourseInfo().getTrainee2Trainer() == FlagConstants.NEED_ASSESSMENT) {
        	Page<AssessmentForShow> assessmentForShowPage = new Page<AssessmentForShow>();
    		assessmentForShowPage.setNowPager(pageNow);
    		assessmentForShowPage.setPageSize(pageSize);
    		assessmentForShowPage.setTotalRecords(assessmentsPage.getTotalRecords());
    	    List<AssessmentForShow> assessmentInfoList = new ArrayList<AssessmentForShow>();
    	    for (Assessment assessment : assessmentsPage.getList()) {
    	    	AssessmentForShow assessmentForShow = new AssessmentForShow();
    	    	assessmentForShow.setAssessComment(assessment.getAssessComment());
    	    	assessmentForShow.setIsIgnore(assessment.getIsIgnore());
    	    	EmployeeDao employeeDao = BeanFactory.getEmployeeDao();
    	    	Employee employee = null;
    	        try {
    	        	employee = employeeDao.findByPrimaryKey(assessment.getTraineeId());
    	        } catch (Exception e) {
    	            logger.error(e);
    	            throw new ServerErrorException(e);
    	        }
    	        assessmentForShow.setEmployeeName(employee.getAugUserName());
    	        List<ScoreForShow> scoreInfoList = new ArrayList<ScoreForShow>();
    	        for (AssessmentScore assessmentScore : assessment.getAssessScoreList()) {
    	        	ScoreForShow scoreForShow = new ScoreForShow();
    		        scoreForShow.setItemName(assessmentScore.getAssessmentItem().getAssessItemName());
    		        scoreForShow.setAssessScore(assessmentScore.getAssessScore());
    		        scoreInfoList.add(scoreForShow);
    	        }
    	        assessmentForShow.setScoreList(scoreInfoList);
    	        assessmentInfoList.add(assessmentForShow);
    	    }
    	    assessmentForShowPage.setList(assessmentInfoList);
    	    assessmentForShowPage.setTotalPage(assessmentsPage.getTotalPage());
    		//put all info to assessmentToPlanInfo map.
    	    assessmentToTrainerInfo.putAll(averageScoreAndTraineesMap);
    	    if (assessmentForShowPage.getList().isEmpty()) {
    	    	assessmentToTrainerInfo.put(JsonKeyConstants.ASSESSMENT_NO_DATA, FlagConstants.ASSESSMENT_NO_DATA_VALUE);
    	    } else {
    	    	assessmentToTrainerInfo.put(JsonKeyConstants.ASSESSMENT_NO_DATA, FlagConstants.ASSESSMENT_HAVE_DATA_VALUE);
    	    	assessmentToTrainerInfo.put(JsonKeyConstants.ASSESSMENT_PAGE, assessmentForShowPage);
    	    }
    	    assessmentToTrainerInfo.put(JsonKeyConstants.NEED_ASSESSMENT, JsonKeyConstants.NEED_ASSESSMENT_VALUE);
        } else {
        	assessmentToTrainerInfo.put(JsonKeyConstants.NEED_ASSESSMENT, JsonKeyConstants.NOT_NEED_ASSESSMENT_VALUE);
        }
		
		return assessmentToTrainerInfo;
	}
    
    
    /**
     * 
     * @param planCourseId
     * @param trainerId
     * @return
     * @throws ServerErrorException
     */
    @SuppressWarnings("unchecked")
	private Map<String, Object> getAverageScoreToTrainer (int actualCourseId, int trainerId) throws ServerErrorException {
    	logger.info("getAverageScoreToTrainer param: actualCourseId-" + actualCourseId + "   trainerId" + trainerId);
    	Map<String, Object> averageScoreAndTraineesMap = new HashMap<String, Object>();
    	Map<String, Object> rateAndTraineeMap = getAssessmentsRateForTrainerByTrainee(actualCourseId, trainerId);
    	List<String> traineeNameList = (List<String>) rateAndTraineeMap.get(JsonKeyConstants.TRAINEE_LIST);
    	averageScoreAndTraineesMap.put(JsonKeyConstants.NOT_ASSESSED_EMPLOYEE_LIST, traineeNameList);
    	List<AssessmentItem> assessmentItems = null;
		Map<String, Integer> assessmentFieldMap = new HashMap<String, Integer>();
		assessmentFieldMap.put(JsonKeyConstants.PLAN_COURSE_ID, actualCourseId);
		assessmentFieldMap.put(JsonKeyConstants.TRAINER_ID, trainerId);
		assessmentFieldMap.put(JsonKeyConstants.TYPE_FLAG, FlagConstants.TRAINEE_TO_TRAINER);
		assessmentFieldMap.put(JsonKeyConstants.ASSESS_IS_DELETED, FlagConstants.UN_DELETED);
		assessmentFieldMap.put(JsonKeyConstants.IS_IGNORE, FlagConstants.UN_IGNORE);
		assessmentItems = assessmentDao.countAssessment(assessmentFieldMap, FlagConstants.TRAINEE_TO_TRAINER);
		if (null == assessmentItems) {
			return averageScoreAndTraineesMap;
		}
		//get rate 
		Map<Integer, String> rateMap = (Map<Integer, String>) rateAndTraineeMap.get(JsonKeyConstants.RATE);
		if (rateMap.size() == assessmentItems.size()) {
			for (AssessmentItem assessmentItem : assessmentItems) {
				assessmentItem.setAssessmentItemRate(rateMap.get(assessmentItem.getAssessItemId()));
			}
		}
		averageScoreAndTraineesMap.put(JsonKeyConstants.ASSESSMENT_ITEM_AVERAGE_RATE_LIST, assessmentItems);
		logger.info("getAverageScoreToTrainer return result: " + averageScoreAndTraineesMap.toString());
    	return averageScoreAndTraineesMap;
    	
    }
    
    @Override
	public Map<String, Object> getAverageScoreToTrainerByCourseIdAndTranerId(
			int planCourseId, int trainerId) throws ServerErrorException,
			DataWarningException {
		return getAverageScoreToTrainer (planCourseId, trainerId);
	}

    
    public void setActualCourseDao(ActualCourseDao actualCourseDao) {
		this.actualCourseDao = actualCourseDao;
	}

	public void setPlanDao(PlanDao planDao) {
        this.planDao = planDao;
    }

    public void setEmployeeDao(EmployeeDao employeeDao) {
        this.employeeDao = employeeDao;
    }

    @Override
    public List<Assessment> getAssessmentByFields(
            Map<String, Integer> assessmentFieldMap, Integer asssessmentTypeId) throws ServerErrorException {
        List<Integer> assessmentTypes = new ArrayList<Integer>();
        assessmentTypes.add(asssessmentTypeId);
        List<Assessment> assessments = null;
        try{
            assessments = assessmentDao.getAssessmentByFields(assessmentFieldMap, assessmentTypes);
        }catch(Exception e){
            logger.error(e);
            throw new ServerErrorException(e.getMessage());
        }
        for (Assessment assessment:assessments) {
            if(assessment.getAssessScoreList() != null && assessment.getAssessScoreList().size() != 0){
                assessment.getAssessScoreList().get(0);
            }
        }
        return assessments;
    }

}
