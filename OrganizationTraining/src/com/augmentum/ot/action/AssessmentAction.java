package com.augmentum.ot.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.augmentum.ot.dataObject.AssessmentForShow;
import com.augmentum.ot.dataObject.ScoreForShow;
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
import com.augmentum.ot.model.LeaveNote;
import com.augmentum.ot.model.Plan;
import com.augmentum.ot.service.ActualCourseService;
import com.augmentum.ot.service.AssessmentItemService;
import com.augmentum.ot.service.AssessmentService;
import com.augmentum.ot.service.EmployeeService;
import com.augmentum.ot.service.PlanService;
import com.augmentum.ot.util.BeanFactory;
import com.augmentum.ot.util.SessionObjectUtils;

/**
 * 
 * @Project OT
 * @Modificaion_history 2012-10-10
 * @Version 2.0
 */
@Component("assessmentAction")
@Scope("prototype")
public class AssessmentAction extends BaseAction {

    private static final long serialVersionUID = 7442392628878019179L;
    private Logger logger = Logger.getLogger(AssessmentAction.class);
    private AssessmentService assessmentService;
    private AssessmentItemService assessmentItemService;
    private int planId;
    private int actualCourseId;
    private Assessment assessment;
    private JSONObject assessmentsJson;
    private List<Assessment> assessmentList;
    private String jsonString;
    private Integer courseHasHomework;
    private String operationFlag;
    private List<String> opionalResult;
    
    /**
     * trainer2plan assessment part functions:
     * @return
     */
    public String searchTrainer2planData() {
    	Employee employee = SessionObjectUtils.getEmployeeFromSession();
    	logger.info(LogConstants.getInfo(employee.getAugUserName(), "searchTrainer2planData"));
    	assessmentsJson = new JSONObject();
    	AssessmentForShow assessmentForShow = new AssessmentForShow();
    	List<ScoreForShow> scoreForShowList = new ArrayList<ScoreForShow>();
		// get assessment form DB
		Assessment assessmmentInDB = null;
		try {
			assessmmentInDB = (Assessment) assessmentService.getOneTrainer2PlanAssessments(planId, employee.getEmployeeId());
		} catch (ServerErrorException e) {
			this.handleExceptionByServerErrorException(e, true);
		}
		List<AssessmentScore> scoreList = new ArrayList<AssessmentScore>();
		if (assessmmentInDB != null) {
			scoreList = assessmmentInDB.getAssessScoreList();
		}
		// assessment is given
		if (assessmmentInDB != null && scoreList != null && !(scoreList.isEmpty())) {
			assessmentForShow.setAssessmentId(assessmmentInDB.getAssessId());
			assessmentForShow.setAssessComment(assessmmentInDB.getAssessComment());
			for (AssessmentScore score:scoreList) {
				ScoreForShow scoreForShow = new ScoreForShow();
				scoreForShow.setAssessScore(score.getAssessScore());
				scoreForShow.setItemName(getText(score.getAssessmentItem().getAssessItemName()));
				scoreForShow.setAssessItemDescribe(getText(score.getAssessmentItem().getAssessItemDescribe()));
				scoreForShowList.add(scoreForShow);
			}
		}
		// assessment is ignored in dashboard
		// assessment has not been given
		if (scoreList == null || scoreList.isEmpty()){
			if (assessmmentInDB != null) {
				assessmentForShow.setAssessmentId(assessmmentInDB.getAssessId());
			}
			assessmentForShow.setAssessComment("");
			// get item description
			List<AssessmentItem> itemList = new ArrayList<AssessmentItem>();
			try {
				itemList = assessmentItemService.getAssessmentItemByTypeId(FlagConstants.TRAINER_TO_PLAN);
			} catch (ServerErrorException e) {
				this.handleExceptionByServerErrorException(e, true);
			}
			for(AssessmentItem item:itemList) {
				ScoreForShow scoreForShow = new ScoreForShow();
				scoreForShow.setAssessScore(FlagConstants.ASSESSMENT_DEFAULT_ZERO);
				scoreForShow.setItemName(getText(item.getAssessItemName()));
				scoreForShow.setAssessItemDescribe(getText(item.getAssessItemDescribe()));
				scoreForShowList.add(scoreForShow);
			}
		}
		assessmentForShow.setScoreList(scoreForShowList);
		assessmentsJson.put(JsonKeyConstants.ASSESSMENT_FOR_SHOW, JSONObject.fromObject(assessmentForShow));
        return SUCCESS;
    }
    
    private boolean validateTrainer2plan(){
        for(AssessmentScore assessmentScore:assessment.getAssessScoreList()){
            if(assessmentScore.getAssessScore() == null){
                return false;
            }
        }
        if(assessment.getAssessComment().length() > 1000){
            return false;
        }
        return true;
    }
    
	public String createTrainer2planAssessment() {
		Employee employee = SessionObjectUtils.getEmployeeFromSession();
    	logger.info(LogConstants.getInfo(employee.getAugUserName(), "createTrainer2planAssessment"));
		if(!validateTrainer2plan()) {
			this.handleValidateExceptionInAction(employee.getAugUserName());
		}
    	List<Assessment> assessmentList = new ArrayList<Assessment>();
    	assessment.setTypeFlag(FlagConstants.TRAINER_TO_PLAN);
		assessment.setTrainerId(employee.getEmployeeId());
		assessment.setAssessIsDeleted(FlagConstants.UN_DELETED);
		assessment.setIsIgnore(FlagConstants.UN_IGNORE);
		assessment.setCreateDate(new Date());
		assessment.setIsIgnore(FlagConstants.UN_IGNORE);
		assessment.setHasBeenAssessed(FlagConstants.ASSESSMENT_HAS_BEEN_ASSESSED);
		List<AssessmentItem> itemList = new ArrayList<AssessmentItem>();
		try {
			itemList = assessmentItemService.getAssessmentItemByTypeId(FlagConstants.TRAINER_TO_PLAN);
		} catch (ServerErrorException e) {
			this.handleExceptionByServerErrorException(e, true);
		}
		List<AssessmentScore> scoreList = assessment.getAssessScoreList();
		for (int i = 0; i < itemList.size(); i++) {
			scoreList.get(i).setAssessmentItem(itemList.get(i));
		}
		assessmentList.add(assessment);
		try {
			assessmentService.saveAssessments(assessmentList);
		} catch (ServerErrorException e) {
			this.handleExceptionByServerErrorException(e, true);
		}
		return SUCCESS;
	}
	
	public String editTrianer2planAssessment() {
		Employee employee = SessionObjectUtils.getEmployeeFromSession();
    	logger.info(LogConstants.getInfo(employee.getAugUserName(), "editTrianer2planAssessment"));
		if(!validateTrainer2plan()) {
			this.handleValidateExceptionInAction(employee.getAugUserName());
		}
		List<Assessment> assessmentList = new ArrayList<Assessment>();
		Assessment assessmentInDB = null;
		try {
			assessmentInDB = assessmentService.findAssessmentById(assessment.getAssessId());
		} catch (ServerErrorException e) {
			this.handleExceptionByServerErrorException(e, true);
		}
		List<AssessmentScore> scoreList = assessmentInDB.getAssessScoreList();
		if (scoreList != null && !(scoreList.isEmpty())) {
			for (int i = 0; i < scoreList.size(); i++) {
				scoreList.get(i).setAssessScore(assessment.getAssessScoreList().get(i).getAssessScore());
			}
		} else {
			List<AssessmentItem> itemList = new ArrayList<AssessmentItem>();
			try {
				itemList = assessmentItemService.getAssessmentItemByTypeId(FlagConstants.TRAINER_TO_PLAN);
			} catch (ServerErrorException e) {
				this.handleExceptionByServerErrorException(e, true);
			}
			List<AssessmentScore> newScoreList = assessment.getAssessScoreList();
			for (int i = 0; i < itemList.size(); i++) {
				newScoreList.get(i).setAssessmentItem(itemList.get(i));
			}
			assessmentInDB.setAssessScoreList(newScoreList);
		}
		assessmentInDB.setIsIgnore(FlagConstants.UN_IGNORE);
		assessmentInDB.setLastUpdateDate(new Date());
		assessmentInDB.setAssessComment(assessment.getAssessComment());
		assessmentList.add(assessmentInDB);
		try {
			assessmentService.updateAssessment(assessmentList);
		} catch (ServerErrorException e) {
			this.handleExceptionByServerErrorException(e, true);
		}
		return SUCCESS;
	}
    /**
     * Ignore trainer assess plan
     * @return
     */
    public String ignoreTrainer2planAssessment() {
    	//Check if this plan has been assessed by trainer.
        Employee employee = SessionObjectUtils.getEmployeeFromSession();
        logger.info(LogConstants.getInfo(employee.getAugUserName(), "ignoreTrainer2planAssessment"));
        Assessment assessmmentInDB = null;
        try {
			assessmmentInDB = (Assessment) assessmentService.getOneTrainer2PlanAssessments(planId, employee.getEmployeeId());
		} catch (ServerErrorException e) {
			this.handleExceptionByServerErrorException(e, true);
		}
        if (assessmmentInDB != null) {
        	return SUCCESS;
        }
        List<Assessment> assessmentList = new ArrayList<Assessment>();
		Assessment assessment = new Assessment();
		assessment.setTypeFlag(FlagConstants.TRAINER_TO_PLAN);
		assessment.setPlanId(planId);
		assessment.setTrainerId(employee.getEmployeeId());
		assessment.setIsIgnore(FlagConstants.IS_IGNORE);
		assessment.setAssessIsDeleted(FlagConstants.UN_DELETED);
		assessment.setCreateDate(new Date());
		assessment.setHasBeenAssessed(FlagConstants.ASSESSMENT_HAS_BEEN_ASSESSED);
		assessmentList.add(assessment);
        try {
            assessmentService.saveAssessments(assessmentList);
        } catch (ServerErrorException e) {
            this.handleExceptionByServerErrorException(e, true);
        }
        return SUCCESS;
    }

    /**
     * trainee2course assessment part functions:
     * @return
     */
    public String searchTrainee2courseData() {
    	Employee employee = SessionObjectUtils.getEmployeeFromSession();
    	logger.info(LogConstants.getInfo(employee.getAugUserName(), "searchTrainee2courseData"));
    	assessmentsJson = new JSONObject();
    	AssessmentForShow trainee2trainerAssessment = new AssessmentForShow();
    	AssessmentForShow trainee2courseAssessment = new AssessmentForShow();
    	// get assessment form DB
    	List<Assessment> assessmentInDB = null;
		try {
			assessmentInDB = assessmentService.getOneTrainee2CourseAssessments(employee.getEmployeeId(), actualCourseId);
		} catch (ServerErrorException e) {
			this.handleExceptionByServerErrorException(e, true);
		}
    	// get plan course trainer information
    	ActualCourseService actualcourseService = BeanFactory.getActualCourseService();
    	EmployeeService employeeService = BeanFactory.getEmployeeService();
    	int trainerId = 0;
    	try {
			trainerId = employeeService.findEmployeeByName(actualcourseService.findActualCourseById(actualCourseId).getCourseTrainer()).getEmployeeId();
		} catch (ServerErrorException e) {
			this.handleExceptionByServerErrorException(e, true);
		} catch (DataWarningException e) {
			this.handleExceptionByDataWarningException(e, true);
		}
		trainee2trainerAssessment.setEmployeeId(trainerId);
        if (assessmentInDB != null && assessmentInDB.size() > 0) {
        	// set trainee2trainerAssessment info
        	Assessment trainee2trainerInDB = assessmentInDB.get(0);
        	trainee2trainerAssessment.setAssessmentId(trainee2trainerInDB.getAssessId());
        	trainee2trainerAssessment.setAssessComment(trainee2trainerInDB.getAssessComment());
        	trainee2trainerAssessment.setScoreList(initscoreForShowList(trainee2trainerInDB, FlagConstants.TRAINEE_TO_TRAINER));
        	// set trainee2trainerAssessment info
        	Assessment trainee2courseInDB = assessmentInDB.get(1);
        	trainee2courseAssessment.setAssessmentId(trainee2courseInDB.getAssessId());
        	trainee2courseAssessment.setAssessComment(trainee2courseInDB.getAssessComment());
        	trainee2courseAssessment.setScoreList(initscoreForShowList(trainee2courseInDB, FlagConstants.TRAINEE_TO_COURSE));
        } else {
        	// get item description
        	trainee2trainerAssessment.setScoreList(initscoreForShowList(null, FlagConstants.TRAINEE_TO_TRAINER));
            trainee2courseAssessment.setScoreList(initscoreForShowList(null, FlagConstants.TRAINEE_TO_COURSE));
        }
        
        assessmentsJson.put(JsonKeyConstants.TRAINEE_2_TRAINER, JSONObject.fromObject(trainee2trainerAssessment));
        assessmentsJson.put(JsonKeyConstants.TRAINEE_2_COURSE, JSONObject.fromObject(trainee2courseAssessment));
        return SUCCESS;
    }
    
    /**
     * init score list for show in page from assessment in DB
     */
    private List<ScoreForShow> initscoreForShowList(Assessment assessmentInDB, int assessmentTypeID) {
    	List<ScoreForShow> scoreForShowList = new ArrayList<ScoreForShow>();
    	List<AssessmentScore> scoreList = new ArrayList<AssessmentScore>();
    	if (assessmentInDB != null) {
    		scoreList = assessmentInDB.getAssessScoreList();
    	}
    	if (scoreList != null && !(scoreList.isEmpty())) {
        	for(AssessmentScore score : scoreList) {
        		ScoreForShow scoreForShow = new ScoreForShow();
        		scoreForShow.setAssessScore(score.getAssessScore());
        		scoreForShow.setItemName(getText(score.getAssessmentItem().getAssessItemName()));
        		scoreForShow.setAssessItemDescribe(getText(score.getAssessmentItem().getAssessItemDescribe()));
        		scoreForShowList.add(scoreForShow);
        	}
    	} else {
			List<AssessmentItem> itemList = new ArrayList<AssessmentItem>();
			try {
				itemList = assessmentItemService.getAssessmentItemByTypeId(assessmentTypeID);
			} catch (ServerErrorException e) {
				this.handleExceptionByServerErrorException(e, true);
			}
			for (AssessmentItem item : itemList) {
				ScoreForShow scoreForShow = new ScoreForShow();
				scoreForShow.setAssessScore(FlagConstants.ASSESSMENT_DEFAULT_ZERO);
				scoreForShow.setItemName(getText(item.getAssessItemName()));
				scoreForShow.setAssessItemDescribe(getText(item.getAssessItemDescribe()));
				scoreForShowList.add(scoreForShow);
			}
    	}
    	return scoreForShowList;
    }
    
    private boolean validateTrainee2course(){
    	for(Assessment assessment:assessmentList){
    		for(AssessmentScore assessmentScore:assessment.getAssessScoreList()){
                if(assessmentScore.getAssessScore() == null){
                    return false;
                }
            }
    		if(assessment.getAssessComment().length() > 1000){
                return false;
            }
    	}
        return true;
    }

    public String createTrainee2courseAssessment() {
    	Employee employee = SessionObjectUtils.getEmployeeFromSession();
    	logger.info(LogConstants.getInfo(employee.getAugUserName(), "createTrainee2courseAssessment"));
    	Date currentDate = new Date();
    	if(!validateTrainee2course()) {
    		this.handleValidateExceptionInAction(employee.getAugUserName());
		}
    	// assessment trainee2trainer
    	assessmentList.get(0).setTypeFlag(FlagConstants.TRAINEE_TO_TRAINER);
    	assessmentList.get(0).setTraineeId(employee.getEmployeeId());
    	assessmentList.get(0).setAssessIsDeleted(FlagConstants.UN_DELETED);
    	assessmentList.get(0).setCreateDate(currentDate);
    	assessmentList.get(0).setIsIgnore(FlagConstants.UN_IGNORE);
    	assessmentList.get(0).setHasBeenAssessed(FlagConstants.ASSESSMENT_HAS_BEEN_ASSESSED);
    	List<AssessmentItem> toTrainerItemList = new ArrayList<AssessmentItem>();
		try {
			toTrainerItemList = assessmentItemService.getAssessmentItemByTypeId(FlagConstants.TRAINEE_TO_TRAINER);
		} catch (ServerErrorException e) {
			this.handleExceptionByServerErrorException(e, true);
		}
		List<AssessmentScore> toTrainerScoreList = assessmentList.get(0).getAssessScoreList();
		for (int i = 0; i < toTrainerItemList.size(); i++) {
			toTrainerScoreList.get(i).setAssessmentItem(toTrainerItemList.get(i));
		}
		assessmentList.get(0).setAssessScoreList(toTrainerScoreList);
    	// assessment trainer2course
    	assessmentList.get(1).setTypeFlag(FlagConstants.TRAINEE_TO_COURSE);
    	assessmentList.get(1).setPlanCourseId(assessmentList.get(0).getPlanCourseId());
    	assessmentList.get(1).setTraineeId(employee.getEmployeeId());
    	assessmentList.get(1).setAssessIsDeleted(FlagConstants.UN_DELETED);
    	assessmentList.get(1).setCreateDate(currentDate);
    	assessmentList.get(1).setIsIgnore(FlagConstants.UN_IGNORE);
    	assessmentList.get(1).setHasBeenAssessed(FlagConstants.ASSESSMENT_HAS_BEEN_ASSESSED);
    	List<AssessmentItem> toCourseItemList = new ArrayList<AssessmentItem>();
		try {
			toCourseItemList = assessmentItemService.getAssessmentItemByTypeId(FlagConstants.TRAINEE_TO_COURSE);
		} catch (ServerErrorException e) {
			this.handleExceptionByServerErrorException(e, true);
		}
		List<AssessmentScore> toCourseScoreList = assessmentList.get(1).getAssessScoreList();
		for (int i = 0; i < toCourseItemList.size(); i++) {
			toCourseScoreList.get(i).setAssessmentItem(toCourseItemList.get(i));
		}
		assessmentList.get(1).setAssessScoreList(toCourseScoreList);
    	
    	try {
			assessmentService.saveAssessments(assessmentList);
		} catch (ServerErrorException e) {
			this.handleExceptionByServerErrorException(e, true);
		}
    	return SUCCESS;
    }
    
    public String editTrainee2courseAssessment() {
    	Employee employee = SessionObjectUtils.getEmployeeFromSession();
    	logger.info(LogConstants.getInfo(employee.getAugUserName(), "editTrainee2courseAssessment"));
    	if(!validateTrainee2course()) {
			this.handleValidateExceptionInAction(employee.getAugUserName());
		}
		List<Assessment> assessments = new ArrayList<Assessment>();
		Assessment assessment2TrainerInDB = null;
		Assessment assessment2CourseInDB = null;
		Date currentDate = new Date();
		try {
			assessment2TrainerInDB = assessmentService.findAssessmentById(assessmentList.get(0).getAssessId());
			assessment2CourseInDB = assessmentService.findAssessmentById(assessmentList.get(1).getAssessId());
		} catch (ServerErrorException e) {
			this.handleExceptionByServerErrorException(e, false);
		}
		List<AssessmentScore> assessment2TrainerScoreList = assessment2TrainerInDB.getAssessScoreList();
		if (assessment2TrainerScoreList != null && !(assessment2TrainerScoreList.isEmpty())) {
			for (int i = 0; i < assessment2TrainerScoreList.size(); i++) {
				assessment2TrainerScoreList.get(i).setAssessScore(assessmentList.get(0).getAssessScoreList().get(i).getAssessScore());
			}
		} else {
			List<AssessmentItem> toTrainerItemList = new ArrayList<AssessmentItem>();
			try {
				toTrainerItemList = assessmentItemService.getAssessmentItemByTypeId(FlagConstants.TRAINEE_TO_TRAINER);
			} catch (ServerErrorException e) {
				this.handleExceptionByServerErrorException(e, true);
			}
			List<AssessmentScore> toTrainerScoreList = assessmentList.get(0).getAssessScoreList();
			for (int i = 0; i < toTrainerItemList.size(); i++) {
				toTrainerScoreList.get(i).setAssessmentItem(toTrainerItemList.get(i));
			}
			assessment2TrainerInDB.setAssessScoreList(toTrainerScoreList);
		}
		assessment2TrainerInDB.setIsIgnore(FlagConstants.UN_IGNORE);
		assessment2TrainerInDB.setLastUpdateDate(currentDate);
		assessment2TrainerInDB.setAssessComment(assessmentList.get(0).getAssessComment());
		assessments.add(assessment2TrainerInDB);
		
		List<AssessmentScore> assessment2CourseScoreList = assessment2CourseInDB.getAssessScoreList();
		if (assessment2CourseScoreList != null && !(assessment2CourseScoreList.isEmpty())) {
			for (int i = 0; i < assessment2CourseScoreList.size(); i++) {
				assessment2CourseScoreList.get(i).setAssessScore(assessmentList.get(1).getAssessScoreList().get(i).getAssessScore());
			}
		} else {
			List<AssessmentItem> toCourseItemList = new ArrayList<AssessmentItem>();
			try {
				toCourseItemList = assessmentItemService.getAssessmentItemByTypeId(FlagConstants.TRAINEE_TO_COURSE);
			} catch (ServerErrorException e) {
				this.handleExceptionByServerErrorException(e, true);
			}
			List<AssessmentScore> toCourseScoreList = assessmentList.get(1).getAssessScoreList();
			for (int i = 0; i < toCourseItemList.size(); i++) {
				toCourseScoreList.get(i).setAssessmentItem(toCourseItemList.get(i));
			}
			assessment2CourseInDB.setAssessScoreList(toCourseScoreList);
		}
		assessment2CourseInDB.setIsIgnore(FlagConstants.UN_IGNORE);
		assessment2CourseInDB.setLastUpdateDate(currentDate);
		assessment2CourseInDB.setAssessComment(assessmentList.get(1).getAssessComment());
		assessments.add(assessment2CourseInDB);
		try {
			assessmentService.updateAssessment(assessments);
		} catch (ServerErrorException e) {
			this.handleExceptionByServerErrorException(e, false);
		}
		return SUCCESS;
    }
    
    /**
     * Ignore trainee assess course.
     * @return
     */
    public String ignoreTrainee2courseAssessment() {
		Employee employee = SessionObjectUtils.getEmployeeFromSession();
		logger.info(LogConstants.getInfo(employee.getAugUserName(),"ignoreTrainee2courseAssessment"));
		//Check if this course has been assessed or ignored.
		List<Assessment> assessmentInDB = null;
		try {
			assessmentInDB = assessmentService.getOneTrainee2CourseAssessments(employee.getEmployeeId(), actualCourseId);
		} catch (ServerErrorException e) {
			this.handleExceptionByServerErrorException(e, true);
		}
		if (assessmentInDB != null && !(assessmentInDB.isEmpty())) {
			return SUCCESS;
		}
		Date currentDate = new Date();
		List<Assessment> assessmentList = new ArrayList<Assessment>();
		Assessment assessment2Trainer = new Assessment();
		assessment2Trainer.setTypeFlag(FlagConstants.TRAINEE_TO_TRAINER);
		assessment2Trainer.setPlanCourseId(actualCourseId);
		// get plan course trainer information
    	ActualCourseService actualcourseService = BeanFactory.getActualCourseService();
    	EmployeeService employeeService = BeanFactory.getEmployeeService();
    	int trainerId = 0;
    	try {
			trainerId = employeeService.findEmployeeByName(actualcourseService.findActualCourseById(actualCourseId).getCourseTrainer()).getEmployeeId();
		} catch (ServerErrorException e) {
			this.handleExceptionByServerErrorException(e, true);
		} catch (DataWarningException e) {
			this.handleExceptionByDataWarningException(e, true);
		}
		assessment2Trainer.setTrainerId(trainerId);
		assessment2Trainer.setTraineeId(employee.getEmployeeId());
		assessment2Trainer.setIsIgnore(FlagConstants.IS_IGNORE);
		assessment2Trainer.setAssessIsDeleted(FlagConstants.UN_DELETED);
		assessment2Trainer.setCreateDate(currentDate);
		assessment2Trainer.setHasBeenAssessed(FlagConstants.ASSESSMENT_HAS_BEEN_ASSESSED);
		assessmentList.add(assessment2Trainer);
		Assessment assessment2Course = new Assessment();
		assessment2Course.setTypeFlag(FlagConstants.TRAINEE_TO_COURSE);
		assessment2Course.setPlanCourseId(actualCourseId);
		assessment2Course.setTraineeId(employee.getEmployeeId());
		assessment2Course.setIsIgnore(FlagConstants.IS_IGNORE);
		assessment2Course.setAssessIsDeleted(FlagConstants.UN_DELETED);
		assessment2Course.setCreateDate(currentDate);
		assessment2Course.setHasBeenAssessed(FlagConstants.ASSESSMENT_HAS_BEEN_ASSESSED);
		assessmentList.add(assessment2Course);
		try {
			assessmentService.saveAssessments(assessmentList);
		} catch (ServerErrorException e) {
			this.handleExceptionByServerErrorException(e, true);
		}
		return SUCCESS;
    }
    
    /**
     * master2trainer assessment part functions:
     * @return
     */
    public String searchMaster2trainerData() {
    	Employee employee = SessionObjectUtils.getEmployeeFromSession();
    	logger.info(LogConstants.getInfo(employee.getAugUserName(), "searchMaster2trainerData"));
    	EmployeeService employeeService = BeanFactory.getEmployeeService();
    	ActualCourseService actualCourseService = BeanFactory.getActualCourseService();
    	PlanService planService = BeanFactory.getPlanService();
    	assessmentsJson = new JSONObject();
    	List<AssessmentForShow> assessmentForShowList = new ArrayList<AssessmentForShow>();
    	// get items
		List<AssessmentItem> itemList = new ArrayList<AssessmentItem>();
		List<Assessment> assessmentInDB = new ArrayList<Assessment>();
		try {
			itemList = assessmentItemService.getAssessmentItemByTypeId(FlagConstants.TRAINING_MASTER_TO_TRAINERS);
			assessmentInDB = assessmentService.getOneMaster2TrainerAssessments(employee.getEmployeeId(), planId);
		} catch (ServerErrorException e) {
			this.handleExceptionByServerErrorException(e, true);
		}
    	// get assessment for show list
    	if (assessmentInDB != null && assessmentInDB.size() > 0) {
    		for (Assessment assessment : assessmentInDB) {
    			AssessmentForShow showAssessment = new AssessmentForShow();
    			showAssessment.setAssessmentId(assessment.getAssessId());
    			List<ScoreForShow> scoreList = new ArrayList<ScoreForShow>();
    			Employee trainer = employeeService.findEmployeeByPrimaryKey(assessment.getTrainerId());
    			showAssessment.setEmployeeId(trainer.getEmployeeId());
    			showAssessment.setEmployeeName(trainer.getAugUserName());
    			// get trainer's plan course
    			try {
					showAssessment.setPlanCourseName(actualCourseService.getActualCoursesInOnePlanByTrainer(trainer.getAugUserName(), planId));
				} catch (ServerErrorException e) {
					this.handleExceptionByServerErrorException(e, true);
				}
    			// get score list
    			List<AssessmentScore> assessmentScores = assessment.getAssessScoreList();
    			if (assessment.getHasBeenAssessed().equals(FlagConstants.ASSESSMENT_HAS_BEEN_ASSESSED) 
    					&& assessmentScores != null && !(assessmentScores.isEmpty())) {
    				for (AssessmentScore score : assessmentScores) {
        				ScoreForShow scoreForShow = new ScoreForShow();
        				scoreForShow.setItemName(getText(score.getAssessmentItem().getAssessItemName()));
        				scoreForShow.setAssessItemDescribe(getText(score.getAssessmentItem().getAssessItemDescribe()));
        				scoreForShow.setAssessScore(score.getAssessScore());
        				scoreList.add(scoreForShow);
        			}
    			} else {
    				for (AssessmentItem item : itemList) {
    					ScoreForShow scoreForShow = new ScoreForShow();
        				scoreForShow.setItemName(getText(item.getAssessItemName()));
        				scoreForShow.setAssessItemDescribe(getText(item.getAssessItemDescribe()));
        				scoreList.add(scoreForShow);
    				}
    			}
    			showAssessment.setScoreList(scoreList);
    			showAssessment.setAssessComment(assessment.getAssessComment());
    			showAssessment.setHasBeenAssessed(assessment.getHasBeenAssessed());
    			showAssessment.setIsIgnore(assessment.getIsIgnore());
    			assessmentForShowList.add(showAssessment);
    		}
    	} else {
    		// get trainers
    		Plan plan = null;
			try {
				plan = planService.findPlanById(planId);
			} catch (DataWarningException e) {
				this.handleExceptionByDataWarningException(e, true);
			} catch (ServerErrorException e) {
				this.handleExceptionByServerErrorException(e, true);
			}
    		String trainers = plan.getTrainers();
    		if (trainers.equals(employee.getAugUserName())) {
    			this.handleValidateExceptionInAction(employee.getAugUserName());
    		}
    		String[] trainerList = trainers.split(FlagConstants.SPLIT_COMMA);
    		for (String trainer : trainerList) {
    			if (!(employee.getAugUserName().equals(trainer))) {
    				AssessmentForShow showAssessment = new AssessmentForShow();
        			List<ScoreForShow> scoreList = new ArrayList<ScoreForShow>();
        			try {
    					showAssessment.setEmployeeId(employeeService.findEmployeeByName(trainer).getEmployeeId());
    				} catch (ServerErrorException e) {
    					this.handleExceptionByServerErrorException(e, true);
    				}
        			showAssessment.setEmployeeName(trainer);
        			try {
    					showAssessment.setPlanCourseName(actualCourseService.getActualCoursesInOnePlanByTrainer(trainer, planId));
    				} catch (ServerErrorException e) {
    					this.handleExceptionByServerErrorException(e, true);
    				}
        			for (AssessmentItem item : itemList) {
        				ScoreForShow scoreForShow = new ScoreForShow();
        				scoreForShow.setItemName(getText(item.getAssessItemName()));
        				scoreForShow.setAssessItemDescribe(getText(item.getAssessItemDescribe()));
        				scoreList.add(scoreForShow);
        			}
        			showAssessment.setScoreList(scoreList);
        			showAssessment.setAssessComment("");
        			showAssessment.setHasBeenAssessed(FlagConstants.ASSESSMENT_UNASSESSED);
        			assessmentForShowList.add(showAssessment);
    			}
    		}
    	}
    	assessmentsJson.put(JsonKeyConstants.ASSESSMENT_FOR_SHOW, JSONArray.fromObject(assessmentForShowList)); // array
        return SUCCESS;
    }
    
    private boolean validateMaster2trainer(){
    	for(Assessment assessment:assessmentList) {
    		if(assessment.getHasBeenAssessed() == FlagConstants.ASSESSMENT_HAS_BEEN_ASSESSED) {
    			for(AssessmentScore assessmentScore:assessment.getAssessScoreList()){
                    if(assessmentScore.getAssessScore() == null){
                        return false;
                    }
                }
        		if(assessment.getAssessComment().length() > 1000){
                    return false;
                }
    		}
    	}
        return true;
    }

    public String createMaster2trainerAssessment() {
    	Employee employee = SessionObjectUtils.getEmployeeFromSession();
    	logger.info(LogConstants.getInfo(employee.getAugUserName(), "createMaster2trainerAssessment"));
    	if(!validateMaster2trainer()) {
    		this.handleValidateExceptionInAction(employee.getAugUserName());
    	}
    	Date currentDate = new Date();
    	for(Assessment assessment : assessmentList) {
    		assessment.setIsIgnore(FlagConstants.UN_IGNORE);
    		assessment.setAssessIsDeleted(FlagConstants.UN_DELETED);
    		assessment.setCreateDate(currentDate);
    		assessment.setMasterId(employee.getEmployeeId());
    		assessment.setPlanId(planId);
    		assessment.setTypeFlag(FlagConstants.TRAINING_MASTER_TO_TRAINERS);
    		List<AssessmentScore> scoreList = assessment.getAssessScoreList();
    		List<AssessmentItem> itemList = new ArrayList<AssessmentItem>();
			try {
				itemList = assessmentItemService.getAssessmentItemByTypeId(FlagConstants.TRAINING_MASTER_TO_TRAINERS);
			} catch (ServerErrorException e) {
				this.handleExceptionByServerErrorException(e, true);
			}
    		for (int i = 0; i < itemList.size(); i++) {
    			if (scoreList.get(i).getAssessScore() == null || scoreList.get(i).getAssessScore() == 0) {
    				assessment.setAssessScoreList(null);
    				break;
    			} else {
    				scoreList.get(i).setAssessmentItem(itemList.get(i));
    			}
    		}
    	}
    	try {
			assessmentService.saveAssessments(assessmentList);
		} catch (ServerErrorException e) {
			this.handleExceptionByServerErrorException(e, true);
		}
    	return SUCCESS;
    }
    
    public String editMaster2trainerAssessment() {
    	Employee employee = SessionObjectUtils.getEmployeeFromSession();
    	logger.info(LogConstants.getInfo(employee.getAugUserName(), "editMaster2trainerAssessment"));
    	if(!validateMaster2trainer()) {
    		this.handleValidateExceptionInAction(employee.getAugUserName());
    	}
    	List<Assessment> assessments = new ArrayList<Assessment>();
    	Date currentDate = new Date();
    	for(Assessment assessment : assessmentList) {
    		Assessment assessmentInDB = null;
    		try {
				assessmentInDB = assessmentService.findAssessmentById(assessment.getAssessId());
			} catch (ServerErrorException e) {
				this.handleExceptionByServerErrorException(e, true);
			}
			List<AssessmentScore> scoreListInDB = assessmentInDB.getAssessScoreList();
			List<AssessmentScore> assessmentScores = assessment.getAssessScoreList();
			if ((scoreListInDB == null || scoreListInDB.isEmpty())
					&& (!judgeScoreList(assessmentScores))) {
				assessmentInDB.setAssessScoreList(null);
			}
			if ((scoreListInDB == null || scoreListInDB.isEmpty())
					&& judgeScoreList(assessmentScores)) {
				List<AssessmentItem> itemList = new ArrayList<AssessmentItem>();
				try {
					itemList = assessmentItemService.getAssessmentItemByTypeId(FlagConstants.TRAINING_MASTER_TO_TRAINERS);
				} catch (ServerErrorException e) {
					this.handleExceptionByServerErrorException(e, true);
				}
	    		for (int i = 0; i < itemList.size(); i++) {
	    			if (assessmentScores.get(i).getAssessScore() == null || assessmentScores.get(i).getAssessScore() == 0) {
	    				assessment.setAssessScoreList(null);
	    				break;
	    			} else {
	    				assessmentScores.get(i).setAssessmentItem(itemList.get(i));
	    			}
	    		}
				assessmentInDB.setAssessScoreList(assessmentScores);
				assessmentInDB.setIsIgnore(FlagConstants.UN_IGNORE);
			}
			if (scoreListInDB != null && !(scoreListInDB.isEmpty())
					&& judgeScoreList(assessmentScores)) {
				for(int i=0; i<scoreListInDB.size(); i++) {
					scoreListInDB.get(i).setAssessScore(assessment.getAssessScoreList().get(i).getAssessScore());
				}
			}
			assessmentInDB.setLastUpdateDate(currentDate);
			assessmentInDB.setAssessComment(assessment.getAssessComment());
			assessmentInDB.setHasBeenAssessed(assessment.getHasBeenAssessed());
			assessments.add(assessmentInDB);
    	}
    	try {
			assessmentService.updateAssessment(assessments);
		} catch (ServerErrorException e) {
			this.handleExceptionByServerErrorException(e, true);
		}
    	return SUCCESS;
    }
    
    public String ignoreMaster2trainerAssessment() {
    	Employee employee = SessionObjectUtils.getEmployeeFromSession();
    	logger.info(LogConstants.getInfo(employee.getAugUserName(), "ignoreMaster2trainerAssessment"));
    	PlanService planService = BeanFactory.getPlanService();
    	EmployeeService employeeService = BeanFactory.getEmployeeService();
    	Date currentDate = new Date();
		try {
			List<Assessment> assessmentInDB = assessmentService.getOneMaster2TrainerAssessments(employee.getEmployeeId(), planId);
	    	Plan plan = null;
			plan = planService.findPlanById(planId);
	    	// have not given assessment at all
	    	if (assessmentInDB == null || assessmentInDB.isEmpty()) {
	    		String trainers = plan.getTrainers();
	    		if (trainers.equals(employee.getAugUserName())) {
	    			this.handleValidateExceptionInAction(employee.getAugUserName());
	    		}
	    		String[] trainerList = trainers.split(FlagConstants.SPLIT_COMMA);
	    		for (String trainer : trainerList) {
	    			if (!(employee.getAugUserName().equals(trainer))) {
	    				Assessment assessment = new Assessment();
	    		    	assessment.setTypeFlag(FlagConstants.TRAINING_MASTER_TO_TRAINERS);
	    		    	assessment.setPlanId(planId);
	    		    	assessment.setMasterId(employee.getEmployeeId());
	    		    	assessment.setTrainerId(employeeService.findEmployeeByName(trainer).getEmployeeId());
	    		    	assessment.setIsIgnore(FlagConstants.IS_IGNORE);
	    		    	assessment.setAssessIsDeleted(FlagConstants.UN_DELETED);
	    		    	assessment.setCreateDate(currentDate);
	    		    	assessment.setHasBeenAssessed(FlagConstants.ASSESSMENT_HAS_BEEN_ASSESSED);
	    		    	assessmentInDB.add(assessment);
	    			}
	    		}
	    		assessmentService.saveAssessments(assessmentInDB);
	    	} else {// have given part of assessment before
	    		List<Assessment> assessmentListForIgnore = new ArrayList<Assessment>();
				for (Assessment assessment : assessmentInDB) {
					if (assessment.getHasBeenAssessed().equals(FlagConstants.ASSESSMENT_UNASSESSED)) {
						assessment.setHasBeenAssessed(FlagConstants.ASSESSMENT_HAS_BEEN_ASSESSED);
						assessment.setIsIgnore(FlagConstants.IS_IGNORE);
						assessment.setAssessScoreList(null);
						assessment.setLastUpdateDate(currentDate);
					}
					assessmentListForIgnore.add(assessment);
				}
				assessmentService.updateAssessment(assessmentListForIgnore);
	    	}
		} catch (DataWarningException e) {
			this.handleExceptionByDataWarningException(e, true);
		} catch (ServerErrorException e) {
			this.handleExceptionByServerErrorException(e, true);
		}
        return SUCCESS;
    }
    
    /**
     * judge if scorelist is null, and score shouldn't be null
     * @param scores
     * @return
     */
    private Boolean judgeScoreList(List<AssessmentScore> scores) {
    	Boolean flag = true;
    	if (scores == null) {
    		flag = false;
    	} else {
    		for (AssessmentScore score:scores) {
				if (score.getAssessScore() == null) {
					flag = false;
					break;
				}
			}
    	}
    	return flag;
    }
    
    /**
     * trainee2plan assessment part functions:
     * @return
     */
    public String searchTrainee2planData() {
        Employee employee = SessionObjectUtils.getEmployeeFromSession();
        logger.info(LogConstants.getInfo(employee.getAugUserName(), "searchTrainee2planData"));
        assessmentsJson = new JSONObject();
        AssessmentForShow assessmentForShow = new AssessmentForShow();
        List<ScoreForShow> scoreForShowList = new ArrayList<ScoreForShow>();
        // get assessment form DB
        Assessment assessmmentInDB = null;
		try {
			assessmmentInDB = (Assessment) assessmentService.getOneTrainee2PlanAssessments(planId, employee.getEmployeeId());
		} catch (ServerErrorException e) {
			this.handleExceptionByServerErrorException(e, true);
		}
        List<AssessmentScore> scoreList = new ArrayList<AssessmentScore>();
		if (assessmmentInDB != null) {
			scoreList = assessmmentInDB.getAssessScoreList();
		}
		// assessment is given
		if (assessmmentInDB != null && scoreList != null && !(scoreList.isEmpty())) {
			operationFlag = FlagConstants.EDIT_OPERATION;
			assessmentForShow.setAssessmentId(assessmmentInDB.getAssessId());
			assessmentForShow.setAssessComment(assessmmentInDB.getAssessComment());
            for (AssessmentScore score:scoreList) {
                ScoreForShow scoreForShow = new ScoreForShow();
                scoreForShow.setAssessScore(score.getAssessScore());
                scoreForShow.setItemName(getText(score.getAssessmentItem().getAssessItemName()));
                scoreForShow.setAssessItemDescribe(getText(score.getAssessmentItem().getAssessItemDescribe()));
                scoreForShowList.add(scoreForShow);
            }
		}
		// assessment is ignored in dashboard
		// assessment has not been given
		if (scoreList == null || scoreList.isEmpty()) {
			if (assessmmentInDB != null) {
				assessmentForShow.setAssessmentId(assessmmentInDB.getAssessId());
				operationFlag = FlagConstants.EDIT_OPERATION;
			} else {
				operationFlag = FlagConstants.CREATE_OPERATION;
			}
            assessmentForShow.setAssessComment("");
            // get item description
            List<AssessmentItem> itemList = new ArrayList<AssessmentItem>();
            try {
                itemList = assessmentItemService.getAssessmentItemByTypeId(FlagConstants.TRAINEE_TO_PLAN);
            } catch (ServerErrorException e) {
                this.handleExceptionByServerErrorException(e, true);
            }
            for(AssessmentItem item:itemList) {
                ScoreForShow scoreForShow = new ScoreForShow();
                scoreForShow.setAssessScore(FlagConstants.ASSESSMENT_DEFAULT_ZERO);
                scoreForShow.setItemName(getText(item.getAssessItemName()));
                scoreForShow.setAssessItemDescribe(getText(item.getAssessItemDescribe()));
                scoreForShowList.add(scoreForShow);
            }
        }
        assessmentForShow.setScoreList(scoreForShowList);
        assessmentsJson.put(JsonKeyConstants.ASSESSMENT_FOR_SHOW, JSONObject.fromObject(assessmentForShow));
        assessmentsJson.put(JsonKeyConstants.OPERATION_FLAG, operationFlag);
        
        return SUCCESS;
    }
    
    private boolean validateTrainee2plan(){
        for(AssessmentScore assessmentScore:assessment.getAssessScoreList()){
            if(assessmentScore.getAssessScore() == null){
                return false;
            }
        }
        if(assessment.getAssessComment().length() > 1000){
            return false;
        }
        return true;
    }

    public String createTrainee2planAssessment() {
        Employee employee = SessionObjectUtils.getEmployeeFromSession();
        logger.info(LogConstants.getInfo(employee.getAugUserName(), "createTrainee2planAssessment"));
        if(!validateTrainee2plan()){
            this.handleValidateExceptionInAction(employee.getAugUserName());
        }
        Date currentDate = new Date();
        if(FlagConstants.CREATE_OPERATION.equals(operationFlag)){
            List<Assessment> assessmentList = new ArrayList<Assessment>();
            assessment.setTypeFlag(FlagConstants.TRAINEE_TO_PLAN);
            assessment.setTraineeId(employee.getEmployeeId());
            assessment.setAssessIsDeleted(FlagConstants.UN_DELETED);
            assessment.setIsIgnore(FlagConstants.UN_IGNORE);
            assessment.setPlanId(planId);
            assessment.setCreateDate(currentDate);
            assessment.setLastUpdateDate(currentDate);
            assessment.setHasBeenAssessed(FlagConstants.ASSESSMENT_HAS_BEEN_ASSESSED);
            List<AssessmentItem> itemList = new ArrayList<AssessmentItem>();
            try {
                itemList = assessmentItemService.getAssessmentItemByTypeId(FlagConstants.TRAINEE_TO_PLAN);
            } catch (ServerErrorException e) {
                this.handleExceptionByServerErrorException(e, true);
            }
            List<AssessmentScore> scoreList = assessment.getAssessScoreList();
            for (int i = 0; i < itemList.size(); i++) {
                scoreList.get(i).setAssessmentItem(itemList.get(i));
            }
            assessmentList.add(assessment);
            try {
                assessmentService.saveAssessments(assessmentList);
            } catch (ServerErrorException e) {
                this.handleExceptionByServerErrorException(e, true);
            }
        }else if(FlagConstants.EDIT_OPERATION .equals(operationFlag) ){
            List<Assessment> assessmentList = new ArrayList<Assessment>();
            Assessment assessmentInDB = null;
            try {
                assessmentInDB = assessmentService.findAssessmentById(assessment.getAssessId());
            } catch (ServerErrorException e) {
                this.handleExceptionByServerErrorException(e, true);
            }
            List<AssessmentScore> scoreList = assessmentInDB.getAssessScoreList();
            if (scoreList != null && !(scoreList.isEmpty())) {
            	for(int i = 0; i < scoreList.size();i++){
                    scoreList.get(i).setAssessScore(assessment.getAssessScoreList().get(i).getAssessScore());
                }
            } else {
            	List<AssessmentItem> itemList = new ArrayList<AssessmentItem>();
                try {
                    itemList = assessmentItemService.getAssessmentItemByTypeId(FlagConstants.TRAINEE_TO_PLAN);
                } catch (ServerErrorException e) {
                    this.handleExceptionByServerErrorException(e, true);
                }
                List<AssessmentScore> newScoreList  = assessment.getAssessScoreList();
                for (int i = 0; i < itemList.size(); i++) {
                	newScoreList .get(i).setAssessmentItem(itemList.get(i));
                }
                assessmentInDB.setAssessScoreList(newScoreList);
            }
            assessmentInDB.setIsIgnore(FlagConstants.UN_IGNORE);
            assessmentInDB.setLastUpdateDate(currentDate);
            assessmentInDB.setAssessComment(assessment.getAssessComment());
            assessmentList.add(assessmentInDB);
            try {
                assessmentService.updateAssessment(assessmentList);
            } catch (ServerErrorException e) {
                this.handleExceptionByServerErrorException(e, true);
            }
        }
        return SUCCESS;
    }
    /**
     * Ignore trainee assess plan.
     * @return
     */
    public String ignoreTrainee2planAssessment() {
        Employee employee = SessionObjectUtils.getEmployeeFromSession();
        logger.info(LogConstants.getInfo(employee.getAugUserName(), "ignoreTrainee2planAssessment"));
        // Check if this plan has been assessed or ignored.
        Assessment assessmmentInDB = null;
		try {
			assessmmentInDB = (Assessment) assessmentService.getOneTrainee2PlanAssessments(planId, employee.getEmployeeId());
		} catch (ServerErrorException e) {
			this.handleExceptionByServerErrorException(e, true);
		}
		if (assessmmentInDB != null) {
			return SUCCESS;
		}
        Date currentDate = new Date();
        List<Assessment> assessments = new ArrayList<Assessment>();
        Assessment assessment = new Assessment();
        assessment.setAssessIsDeleted(FlagConstants.UN_DELETED);
        assessment.setCreateDate(currentDate);
        assessment.setHasBeenAssessed(FlagConstants.ASSESSMENT_HAS_BEEN_ASSESSED);
        assessment.setIsIgnore(FlagConstants.IS_IGNORE);
        assessment.setPlanId(planId);
        assessment.setTraineeId(employee.getEmployeeId());
        assessment.setTypeFlag(FlagConstants.TRAINEE_TO_PLAN);
        assessments.add(assessment);
        try {
            assessmentService.saveAssessments(assessments);
        } catch (ServerErrorException e) {
            this.handleExceptionByServerErrorException(e, true);
        }
        return SUCCESS;
    }

    /**************************
     * re-write
     * @return
     */
    
	public String searchTrainer2TraineeData() {
    	Employee employee = SessionObjectUtils.getEmployeeFromSession();
        logger.info(LogConstants.getInfo(employee.getAugUserName(), "searchTrainer2TraineeData"));
        ActualCourseService actualCourseService = BeanFactory.getActualCourseService();
        List<Assessment> assessmentList = null;
        ActualCourse actualCourse = null;
        List<AssessmentItem> assessmentItemList = null;
        List<Employee> traineeList = null;
        List<LeaveNote> leaveNoteList = null;
        List<Employee> applyLeaveTrainees = new ArrayList<Employee>();
        Assessment trainerToCourse = null;
        Map<Integer, AssessmentForShow> assessmentInfoMap = new HashMap<Integer, AssessmentForShow>();
        
        try {
            assessmentItemList = assessmentItemService.getAssessmentItemByTypeId(FlagConstants.TRAINER_TO_TRAINEES);
            assessmentList = assessmentService.getAssessmentForTraineeByTrainer(employee.getEmployeeId(),actualCourseId);
            actualCourse = actualCourseService.getActualCourseById(actualCourseId);
            traineeList = actualCourse.getEmployeeList();
            leaveNoteList = actualCourse.getLeaveNoteList();
            trainerToCourse = assessmentService.getOneTrainer2CourseAssessment(actualCourseId, employee.getEmployeeId());
        } catch (DataWarningException e) {
            this.handleExceptionByDataWarningException(e, true);
        } catch (ServerErrorException e) {
            this.handleExceptionByServerErrorException(e, true);
        }
        for (AssessmentItem assessmentItem : assessmentItemList) {
        	assessmentItem.setAssessItemName(getText(assessmentItem.getAssessItemName()));
        }
        if (null != leaveNoteList) {
        	for (LeaveNote leaveNote : leaveNoteList) {
        		applyLeaveTrainees.add(leaveNote.getEmployee());
        	}
        }
        for (Employee trainee : traineeList) {
        	AssessmentForShow assessmentForShow = new AssessmentForShow();
        	assessmentForShow.setEmployeeId(trainee.getEmployeeId());
        	assessmentForShow.setEmployeeName(trainee.getAugUserName());
        	assessmentForShow.setEmployeePrefixId(trainee.getAugEmpId());
        	assessmentForShow.setHasBeenAssessed(FlagConstants.ASSESSMENT_UNASSESSED);
        	if (applyLeaveTrainees.contains(trainee)) {
        		assessmentForShow.setAttendenceLog(JsonKeyConstants.LEAVE);
        	}
        	assessmentInfoMap.put(trainee.getEmployeeId(), assessmentForShow);
        }
        
        for (Assessment assessment : assessmentList) {
        	AssessmentForShow assessmentForShow = assessmentInfoMap.get(assessment.getTraineeId());
            if (assessment.getHasBeenAssessed() == FlagConstants.ASSESSMENT_HAS_BEEN_ASSESSED 
            		&& assessment.getIsIgnore() == FlagConstants.UN_IGNORE) {
            	assessmentForShow.setAssessComment(assessment.getAssessComment());
            	assessmentForShow.setAttendenceLog(assessment.getAssessmentAttendLog().getAttendLogKey());
            	assessmentForShow.setAttendLogId(assessment.getAssessmentAttendLog().getAttendLogId());
            	assessmentForShow.setScoreList(createScoreListForShow(assessment.getAssessScoreList()));
            	assessmentForShow.setHasBeenAssessed(FlagConstants.ASSESSMENT_HAS_BEEN_ASSESSED);
            }
            assessmentForShow.setEmployeeId(assessment.getTraineeId());
            assessmentForShow.setAssessmentId(assessment.getAssessId());
            assessmentForShow.setIsIgnore(assessment.getIsIgnore());
            assessmentForShow.setCreateDate(assessment.getCreateDate().toString());
        }
        
        Map<Integer, String> optionalItemMap = getOptionalItemMap(assessmentList, assessmentItemList);
        Map<String, Object> prepareAssessInfo = new HashMap<String, Object>();
        if (assessmentList == null | assessmentList.isEmpty()) {
        	prepareAssessInfo.put(JsonKeyConstants.OPERATION, FlagConstants.CREATE_OPERATION);
        } else {
        	prepareAssessInfo.put(JsonKeyConstants.OPERATION, FlagConstants.EDIT_OPERATION);
        }
        if (assessmentItemList != null && !assessmentItemList.isEmpty()) {
        	prepareAssessInfo.put(JsonKeyConstants.ASSESSMENT_ITEM_LIST, assessmentItemList);
        }
        if (assessmentInfoMap != null && !assessmentInfoMap.isEmpty()) {
        	prepareAssessInfo.put(JsonKeyConstants.ASSESSMENT_INFO_MAP, assessmentInfoMap);
        } else {
        	//TODO no trainees.
        }
        if (optionalItemMap != null && !optionalItemMap.isEmpty()) {
        	prepareAssessInfo.put(JsonKeyConstants.OPTIONAL_ITEM_MAP, optionalItemMap);
        }
        if (trainerToCourse != null && trainerToCourse.getAssessComment() != null) {
        	prepareAssessInfo.put(JsonKeyConstants.COURSE_COMMENTS, trainerToCourse.getAssessComment());
        }
        assessmentsJson = JSONObject.fromObject(prepareAssessInfo);
		return SUCCESS;
	}
	
	//Judge optional item
    private Map<Integer, String> getOptionalItemMap(List<Assessment> assessmentList, List<AssessmentItem> assessmentItemList) {
    	Map<Integer, String> optionalItemMap = new HashMap<Integer, String>();
    	if (null != assessmentList && !assessmentList.isEmpty()) {
    		for (Assessment assessment : assessmentList) {
        		List<AssessmentScore> assessmentScoreList = assessment.getAssessScoreList();
        		if (assessmentScoreList != null && !(assessmentScoreList.isEmpty())) {
        			for (AssessmentScore assessmentScore : assessmentScoreList) {
            			AssessmentItem assessmentItem = assessmentScore.getAssessmentItem();
            			if (assessmentItem.getIsOptional().equals(FlagConstants.IS_OPTIONAL)) {
            				if (assessmentScore.getAssessScore().intValue() == FlagConstants.ITEM_UNSELECTED_SCORE){
            					optionalItemMap.put(assessmentItem.getAssessItemId(), "unselected");
            				} else {
            					optionalItemMap.put(assessmentItem.getAssessItemId(), "selected");
            				}
            				
            			}
            		}
            		return optionalItemMap;
        		}
        	}
    	} 
    	
    		if (null != assessmentItemList && !assessmentItemList.isEmpty()) {
    			for (AssessmentItem assessmentItem : assessmentItemList) {
    				if (assessmentItem.getIsOptional().equals(FlagConstants.IS_OPTIONAL)) {
    					optionalItemMap.put(assessmentItem.getAssessItemId(), "selected");
    				}
    			}
    			return optionalItemMap;
    		}
    	
    	
    	return optionalItemMap;
    }
    
    /**
     * Trainer assess trainee
     * @return
     */
    public String createTrainer2traineeAssessment() {
    	Employee employee = SessionObjectUtils.getEmployeeFromSession();
        logger.info(LogConstants.getInfo(employee.getAugUserName(), "createTrainee2traineeAssessment"));
        Date currentDate = new Date();
        //Get selected result and put them to map.
        Map<Integer, String> optionalResultMap = new HashMap<Integer, String>();
        for (String optionalStr : opionalResult) {
        	Integer itemId = Integer.valueOf(Integer.parseInt(optionalStr.split(":")[0]));
        	optionalResultMap.put(itemId, optionalStr.split(":")[1]);
        }
    	for (Assessment assessment : assessmentList) {
    		for (AssessmentScore assessmentScore : assessment.getAssessScoreList()) {
    			//Set score of unselected item -1.
    			if (optionalResultMap.get(assessmentScore.getAssessmentItem().getAssessItemId()) != null &&
    				optionalResultMap.get(assessmentScore.getAssessmentItem().getAssessItemId()).equals("unselected")) {
    					assessmentScore.setAssessScore(FlagConstants.ITEM_UNSELECTED_SCORE.doubleValue());
    			} else {
    				if (assessmentScore.getAssessScore() != null && 
    						assessment.getHasBeenAssessed().equals(FlagConstants.ASSESSMENT_HAS_BEEN_ASSESSED) &&
    						assessmentScore.getAssessScore().intValue() == FlagConstants.ASSESSMENT_UNASSESSED) {
    					return ""; //TODO
    				}
    			}
    		}
    		if (assessment.getAssessmentAttendLog().getAttendLogKey().equals(JsonKeyConstants.ABSENT) ||
    				assessment.getAssessmentAttendLog().getAttendLogKey().equals(JsonKeyConstants.LEAVE)) {
    			for(AssessmentScore assessmentScore : assessment.getAssessScoreList()) {
    				assessmentScore.setAssessScore(FlagConstants.ASSESSMENT_UNASSESSED.doubleValue());
    			}
    		}
    		assessment.setTypeFlag(FlagConstants.TRAINER_TO_TRAINEES);
    		assessment.setAssessIsDeleted(FlagConstants.UN_DELETED);
    		assessment.setTrainerId(employee.getEmployeeId());
    		assessment.setPlanCourseId(actualCourseId);
    		assessment.setPlanId(planId);
    		assessment.setIsIgnore(FlagConstants.UN_IGNORE);
    		assessment.setLastUpdateDate(currentDate);
    		if (null == assessment.getCreateDate()) {
    			assessment.setCreateDate(currentDate);
    		}
    		if (assessment.getHasBeenAssessed() == FlagConstants.ASSESSMENT_UNASSESSED) {
    			assessment.setAssessScoreList(null);
    			assessment.setAssessmentAttendLog(null);
    		}
    	}
    	if (null != assessment) {
    		assessment.setTrainerId(employee.getEmployeeId());
    		assessment.setTypeFlag(FlagConstants.TRAINER_TO_COURSE);
    		assessment.setPlanCourseId(actualCourseId);
    		assessment.setIsIgnore(FlagConstants.UN_IGNORE);
    		assessment.setAssessIsDeleted(FlagConstants.UN_DELETED);
    		assessment.setLastUpdateDate(currentDate);
    		assessment.setAssessScoreList(null);
    		assessment.setHasBeenAssessed(FlagConstants.ASSESSMENT_HAS_BEEN_ASSESSED);
    		assessmentList.add(assessment);
    	}
    	if(FlagConstants.CREATE_OPERATION.equals(operationFlag)){
    		saveTrainerToTraineeAssessment();
        } else if(FlagConstants.EDIT_OPERATION.equals(operationFlag)){
        	editTrainerToTraineeAssessment();
        }
    	return SUCCESS;
    }
    
    private void saveTrainerToTraineeAssessment() {
    	try {
    		assessmentService.saveAssessments(assessmentList);
    	} catch (ServerErrorException e) {
			this.handleExceptionByServerErrorException(e, true);
		}
    }
    
    private void editTrainerToTraineeAssessment() {
    	try {
			assessmentService.updateAssessment(assessmentList);
		} catch (ServerErrorException e) {
			this.handleExceptionByServerErrorException(e, true);
		}
    }
    
    
    private List<ScoreForShow> createScoreListForShow(List<AssessmentScore> assessmentScoreList) {
    	List<ScoreForShow> scoreForShowList = new ArrayList<ScoreForShow>();
    	for (AssessmentScore assessmentScore : assessmentScoreList) {
    		ScoreForShow scoreForShow = new ScoreForShow();
    		scoreForShow.setItemName(getText(assessmentScore.getAssessmentItem().getAssessItemName()));
    		if (assessmentScore.getAssessScore() != null) {
    			scoreForShow.setAssessScore(assessmentScore.getAssessScore());
    		}
    		scoreForShow.setItemId(assessmentScore.getAssessmentItem().getAssessItemId());
    		scoreForShow.setScoreId(assessmentScore.getAssessmentScoreId());
    		scoreForShowList.add(scoreForShow);
    	}
    	
    	return scoreForShowList;
    }
    
    public String ignoreTrainer2Trainee() {
    	Employee employee = SessionObjectUtils.getEmployeeFromSession();
        logger.info(LogConstants.getInfo(employee.getAugUserName(), "initTrainer2TraineesData"));
    	List<Assessment> assessmentList = null;
    	ActualCourse actualCourse = null;
    	ActualCourseService actualCourseService = BeanFactory.getActualCourseService();
    	Date currentDate = new Date();
    	try {
			assessmentList = assessmentService.getAssessmentForTraineeByTrainer(employee.getEmployeeId(),actualCourseId);
			if (assessmentList == null || assessmentList.isEmpty()) {
				actualCourse = actualCourseService.getActualCourseById(actualCourseId);
				List<Employee> employList = actualCourse.getEmployeeList();
				List<Assessment> assessmentListForIgnore = new ArrayList<Assessment>();
				for (Employee trainee : employList) {
					Assessment assessment = new Assessment();
					assessment.setAssessId(FlagConstants.UN_DELETED);
					assessment.setPlanId(actualCourse.getPlan().getPlanId());
					assessment.setPlanCourseId(actualCourseId);
					assessment.setIsIgnore(FlagConstants.IS_IGNORE);
					assessment.setCreateDate(currentDate);
					assessment.setHasBeenAssessed(FlagConstants.ASSESSMENT_HAS_BEEN_ASSESSED);
					assessment.setAssessScoreList(null);
					assessment.setTrainerId(employee.getEmployeeId());
					assessment.setTraineeId(trainee.getEmployeeId());
					assessment.setAssessIsDeleted(FlagConstants.UN_DELETED);
					assessment.setTypeFlag(FlagConstants.TRAINER_TO_TRAINEES);
					assessmentListForIgnore.add(assessment);
					
				}
				assessmentService.saveAssessments(assessmentListForIgnore);
			} else {
				List<Assessment> assessmentListForIgnore = new ArrayList<Assessment>();
				for (Assessment assessment : assessmentList) {
					if (assessment.getHasBeenAssessed().equals(FlagConstants.ASSESSMENT_UNASSESSED)) {
						assessment.setHasBeenAssessed(FlagConstants.ASSESSMENT_HAS_BEEN_ASSESSED);
						assessment.setIsIgnore(FlagConstants.IS_IGNORE);
						assessment.setAssessScoreList(null);
						assessment.setLastUpdateDate(currentDate);
					}
					assessmentListForIgnore.add(assessment);
				}
				assessmentService.updateAssessment(assessmentListForIgnore);
			}
		} catch (ServerErrorException e) {
			this.handleExceptionByServerErrorException(e, true);
		} catch (DataWarningException e) {
			this.handleExceptionByDataWarningException(e, true);
		}
		
    	return SUCCESS;
    }
    
    
	public Assessment getAssessment() {
		return assessment;
	}

	public void setAssessment(Assessment assessment) {
		this.assessment = assessment;
	}

	public JSONObject getAssessmentsJson() {
		return assessmentsJson;
	}

	public void setAssessmentsJson(JSONObject assessmentsJson) {
		this.assessmentsJson = assessmentsJson;
	}

	public int getPlanId() {
		return planId;
	}

	public void setPlanId(int planId) {
		this.planId = planId;
	}

    public int getActualCourseId() {
		return actualCourseId;
	}

	public void setActualCourseId(int actualCourseId) {
		this.actualCourseId = actualCourseId;
	}

	public List<Assessment> getAssessmentList() {
        return assessmentList;
    }

    public void setAssessmentList(List<Assessment> assessmentList) {
        this.assessmentList = assessmentList;
    }

	@Resource
	public void setAssessmentService(AssessmentService assessmentService) {
		this.assessmentService = assessmentService;
	}

	@Resource
	public void setAssessmentItemService(
			AssessmentItemService assessmentItemService) {
		this.assessmentItemService = assessmentItemService;
	}

    public String getJsonString() {
        return jsonString;
    }

    public void setJsonString(String jsonString) {
        this.jsonString = jsonString;
    }

    public Integer getCourseHasHomework() {
        return courseHasHomework;
    }

    public void setCourseHasHomework(Integer courseHasHomework) {
        this.courseHasHomework = courseHasHomework;
    }

    public String getOperationFlag() {
        return operationFlag;
    }

    public void setOperationFlag(String operationFlag) {
        this.operationFlag = operationFlag;
    }

	public List<String> getOpionalResult() {
		return opionalResult;
	}

	public void setOpionalResult(List<String> opionalResult) {
		this.opionalResult = opionalResult;
	}
    
}
