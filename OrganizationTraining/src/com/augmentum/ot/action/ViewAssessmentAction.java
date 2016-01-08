package com.augmentum.ot.action;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

import org.apache.log4j.Logger;
import org.apache.struts2.json.annotations.JSON;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.augmentum.ot.dataObject.AssessmentForShow;
import com.augmentum.ot.dataObject.AssessmentItemAverageForShow;
import com.augmentum.ot.dataObject.Page;
import com.augmentum.ot.dataObject.ScoreForShow;
import com.augmentum.ot.dataObject.ViewAssessmentCondition;
import com.augmentum.ot.dataObject.constant.FlagConstants;
import com.augmentum.ot.dataObject.constant.JsonKeyConstants;
import com.augmentum.ot.dataObject.constant.LogConstants;
import com.augmentum.ot.exception.DataWarningException;
import com.augmentum.ot.exception.ServerErrorException;
import com.augmentum.ot.model.ActualCourse;
import com.augmentum.ot.model.AssessmentItem;
import com.augmentum.ot.model.Employee;
import com.augmentum.ot.model.Plan;
import com.augmentum.ot.model.PlanEmployeeMap;
import com.augmentum.ot.service.ActualCourseService;
import com.augmentum.ot.service.AssessmentService;
import com.augmentum.ot.service.PlanService;
import com.augmentum.ot.util.BeanFactory;
import com.augmentum.ot.util.ObjectToJSONUtils;
import com.augmentum.ot.util.SessionObjectUtils;

/**
 * Provide the action layer methods for view assessments.
 * 
 * @version 0.1 2012-10-22
 *
 */

@Component("viewAssessmentAction")
@Scope("prototype")
public class ViewAssessmentAction extends BaseAction {

    private static final long serialVersionUID = -3474525527253119517L;
    private Logger logger = Logger.getLogger(ViewAssessmentAction.class);
    
    // The parameters from frontend.
    private Integer planId;
    private Integer actualCourseId;
    private Integer traineeId;
    private Integer trainerId;
    
    private ViewAssessmentCondition viewAssessmentCondition;
    
    /**  The all JSON results use this object.  */
    private JSONObject jsonObject;
    private JSONArray  jsonArray;
    
    @Resource(name = "planService")
    private PlanService planService;
    
    @Resource(name = "assessmentService")
    private AssessmentService assessmentService;
    
    /**
     * Skip to view all assessments page by this action.
     * prepare information of plan for view all assessments.
     * @return
     */
    public String prepareViewAllAssessment() {
    	Employee employee = SessionObjectUtils.getEmployeeFromSession();
    	logger.info(LogConstants.getInfo(employee.getAugUserName(), "prepareViewAllAssessment"));
    	if (planId <= 0) {
    		return FlagConstants.VALIDATION_ERROR;
    	}
    	try {
    		Plan plan = planService.findPlanById(planId);
    		if (!(plan.getPlanCreator().equals(employee.getAugUserName()))) {
    			return FlagConstants.NO_ACCESS;
    		}
    		this.request.put(JsonKeyConstants.PLAN, plan);
		} catch (DataWarningException e) {
			this.handleExceptionByDataWarningException(e, false);
		} catch (ServerErrorException e) {
			this.handleExceptionByServerErrorException(e, false);
		}
    	return SUCCESS;
    }

    /**
     * Get the course list and trainee list according to plan id.
     * For view assessments to Trainee
     * @return  The action deal with result.
     */
    public String viewActualCourseListAndTraineeList() {
        /*
         * 1. Get the Plan instance from service.
         * 2. Deal with the plan course list and employee list.
         * 3. Make the plan course list and employee list to JSONObject.
         * 4. Return result.
         */
    	Employee employee = SessionObjectUtils.getEmployeeFromSession();
    	logger.info(LogConstants.getInfo(employee.getAugUserName(), "viewActualCourseListAndTraineeList"));
        try {
            Plan plan = planService.findPlanByIdForViewAssessment(planId);
            if (plan != null) {
                List<ActualCourse> actualCourseList = plan.getActualCourses();
                Set<PlanEmployeeMap> planEmployeeMapList = plan.getPlanEmployeeMapList();
                Set<PlanEmployeeMap> invitedEmployeeMapSet = new HashSet<PlanEmployeeMap>();
                if (planEmployeeMapList != null && !(planEmployeeMapList.isEmpty())){
                	for (PlanEmployeeMap planEmployeeMap : planEmployeeMapList) {
                		if (planEmployeeMap.getPlanTraineeAttendType().equals(FlagConstants.ATTEND_TYPE_INVITED)) {
                			invitedEmployeeMapSet.add(planEmployeeMap);
                		}
                	}
                }
                jsonObject = ObjectToJSONUtils.getJSONObjectForViewAssessmentToTrainee(plan.getPrefixIDValue(), actualCourseList, invitedEmployeeMapSet);
            }
        } catch (DataWarningException e) {
            this.handleExceptionByDataWarningException(e, true);
        } catch (ServerErrorException e) {
            this.handleExceptionByServerErrorException(e, true);
        }
        return SUCCESS;
    }
    
    /**
     * This method will use planId and employeeId two parameters show assessments to trainee.
     * And the jsonObject will contains behaviorRate, behaviorAverageScore, homeworkRate, homeworkAverageScore
     * traineeId, traineeName, traineePrefixId, attendTimes, lateTimes, leaveEarlyTimes, absenceTimes
     * assessmentList. And each item of assessmentList contains planCourseName, attendenceStatus, behaviorScore, 
     * homeworkScore, CommentsAndSuggestions.
     * 
     * @return  The result is assessments to trainee based a trainee.
     */
    @SuppressWarnings("unchecked")
	public String viewAssessmentByPlanIdAndTraineeId() {
        /*
         * 1. Get the map from service.
         * 2. Deal with the map to a JSON object.
         */
    	Employee employee = SessionObjectUtils.getEmployeeFromSession();
    	logger.info(LogConstants.getInfo(employee.getAugUserName(), "viewAssessmentByPlanIdAndTraineeId"));
        if (assessmentService != null) {
            try {
                // Get the map from service.
                Map<String, Object> planTraineeAssessmentMap = assessmentService.getAssessmentByPlanIdAndTraineeId(planId, traineeId, viewAssessmentCondition);
                //i18n
                if (planTraineeAssessmentMap.get(JsonKeyConstants.NEED_ASSESSMENT).equals(JsonKeyConstants.NEED_ASSESSMENT_VALUE) &&
                		(Integer)planTraineeAssessmentMap.get(JsonKeyConstants.ASSESSMENT_NO_DATA) == FlagConstants.ASSESSMENT_HAVE_DATA_VALUE) {
                	List<AssessmentItemAverageForShow> assessmentItemAverageForShowList = 
                    	(List<AssessmentItemAverageForShow>)planTraineeAssessmentMap.get(JsonKeyConstants.ASSESSMENT_ITEM_AVERAGE_RATE_LIST);
                    for (AssessmentItemAverageForShow assessmentItemAverageForShow : assessmentItemAverageForShowList) {
                    	assessmentItemAverageForShow.setAssessItemName(getText(assessmentItemAverageForShow.getAssessItemName()));
                    }
                    List<AssessmentForShow> assessmentResultList = (List<AssessmentForShow>)planTraineeAssessmentMap.get(JsonKeyConstants.ASSESSMENT_LIST);
                    if (assessmentResultList != null && !(assessmentResultList.isEmpty())) {
                    	i18nDetailPart(assessmentResultList);
                    }
                }
                
                // Deal with the map to a JSON object.
                String[] str = { JsonKeyConstants.ASSESSMENT_EXCEPT_ITEMTYPE };
                JsonConfig jsonConfig = new JsonConfig();
                jsonConfig.setExcludes(str);
                jsonObject = JSONObject.fromObject(planTraineeAssessmentMap, jsonConfig);
            } catch (DataWarningException dataWarningException) {
               this.handleExceptionByDataWarningException(dataWarningException, true);
            } catch (ServerErrorException serverErrorException) {
               this.handleExceptionByServerErrorException(serverErrorException, true);
            }
        }
        return SUCCESS;
    }
    
    /**
     * This method will use planId and planCourseId two parameters.
     * And the jsonObject will contains behaviorRate, behaviorAverageScore, homeworkRate, homeworkAverageScore
     * planCourseId, planCourseName, planAndPlanCoursePrefixId, attendNumber, lateNumber, 
     * leaveEarlyNumber, absenceNumber, pageSize, nowPage, rowCount, pageCount and
     * assessmentList. And each item of assessmentList contains traineeName, attendenceStatus, behaviorScore, 
     * homeworkScore, CommentsAndSuggestions.
     * 
     * @return  The result is assessments to trainee in a course.
     */
    @SuppressWarnings("unchecked")
	public String viewAssessmentByPlanIdAndActualCourseId() {
        /*
         * 1. Get the map from service.
         * 2. Deal with the map to JSON object.
         */
    	Employee employee = SessionObjectUtils.getEmployeeFromSession();
    	logger.info(LogConstants.getInfo(employee.getAugUserName(), "viewAssessmentByPlanIdAndActualCourseId"));
        if (assessmentService != null) {
            try {
                // Get the map from service.
                Map<String, Object> planCourseAssessmentMap = assessmentService.getAssessmentByActualCourseId(planId, actualCourseId, viewAssessmentCondition);
                if (null != planCourseAssessmentMap.get(JsonKeyConstants.ASSESSMENT_LIST)) {
                	List<AssessmentItemAverageForShow> assessmentItemAverageList = 
                		(List<AssessmentItemAverageForShow>) planCourseAssessmentMap.get(JsonKeyConstants.ASSESSMENT_ITEM_AVERAGE_RATE_LIST);
                	if (assessmentItemAverageList != null && !(assessmentItemAverageList.isEmpty())) {
                		for (AssessmentItemAverageForShow assessmentItemAverageForShow : assessmentItemAverageList) {
                    		assessmentItemAverageForShow.setAssessItemName(getText(assessmentItemAverageForShow.getAssessItemName()));
                    	}
                	}
                	List<AssessmentForShow> assessmentResultList = (List<AssessmentForShow>) planCourseAssessmentMap.get(JsonKeyConstants.ASSESSMENT_LIST);
                	if (assessmentResultList != null && !(assessmentResultList.isEmpty())) {
                		i18nDetailPart(assessmentResultList);
                	}
                }
                // Deal with the map to a JSON object.
                jsonObject = ObjectToJSONUtils.transformMapToJson(planCourseAssessmentMap);
            } catch (ServerErrorException serverErrorException) {
                this.handleExceptionByServerErrorException(serverErrorException, true);
            } catch (DataWarningException dataWarningException) {
                this.handleExceptionByDataWarningException(dataWarningException, true);
            }
        }
        
        return SUCCESS;
    }
    
    /**
     * Get the trainer list by plan id. And the jsonObject will as below:
     * trainerList: [{traineeId: "1"; traineeName: "Michael"}, {traineeId: "2"; traineeName: "Michael2"}]
     * 
     * @return  The result.
     */
    public String viewTrainerAndActualCourseListByPlanId() {
    	Employee employee = SessionObjectUtils.getEmployeeFromSession();
    	logger.info(LogConstants.getInfo(employee.getAugUserName(), "viewTrainerAndActualCourseListByPlanId"));
        /*
         * 1. Get the trainer and plan course list from service.
         * 2. Deal with the employee list to JSON object.
         */
        
        try {
            // Get the employee list from service.
            Map<String, Object> trainerAndPlanCourseMap = planService.findTrainerAndActualCourseListByPlanIdForViewAssessment(planId);
            
            // Deal with the employee list to JSON object.
            String[] str = JsonKeyConstants.TRAINER_PLANCOURSE_EXCLUDES;
            JsonConfig jsonConfig = new JsonConfig();
            jsonConfig.setExcludes(str);
            jsonObject = JSONObject.fromObject(trainerAndPlanCourseMap, jsonConfig);
            
        } catch (ServerErrorException e) {
            this.handleExceptionByServerErrorException(e, true);
        } catch (DataWarningException e) {
            this.handleExceptionByDataWarningException(e, true);
        }
        
        return SUCCESS;
    }
    
    /**
     * Get the master to trainer assessment.
     * 
     * @return  The result.
     */
    @SuppressWarnings("unchecked")
	public String viewMasterToTrainerAssessmentByPlanIdAndTrainerId() {
    	Employee employee = SessionObjectUtils.getEmployeeFromSession();
    	logger.info(LogConstants.getInfo(employee.getAugUserName(), "viewTrainerAndActualCourseListByPlanId"));
        /*
         * 1. Get the assessment information with master name.
         * 2. Make up JSON object.
         */
        
        try {
            // 1. Get the assessment information with master name.
            Map<String, Object> masterToTrainerAssessmentMap = 
            	assessmentService.getMasterToTrainerAssessmentByPlanIdAndTrainerId(planId, trainerId, viewAssessmentCondition);
            if ((Integer)masterToTrainerAssessmentMap.get(JsonKeyConstants.ASSESSMENT_NO_DATA) == FlagConstants.ASSESSMENT_HAVE_DATA_VALUE) {
            	List<ScoreForShow> scoreForShowList = (List<ScoreForShow>) masterToTrainerAssessmentMap.get(JsonKeyConstants.SCORE_LIST);
            	for (ScoreForShow scoreForShow : scoreForShowList) {
            		scoreForShow.setItemName(getText(scoreForShow.getItemName()));
            	}
            }
            // 2. Make up JSON object.
            jsonObject = ObjectToJSONUtils.transformMapToJson(masterToTrainerAssessmentMap);
        } catch (ServerErrorException e) {
            this.handleExceptionByServerErrorException(e, true);
        } catch (DataWarningException e) {
           this.handleExceptionByDataWarningException(e, true);
        }
        
        return SUCCESS;
    }
    
    /**
     * View trainee to trainer assessment information. 
     * 
     * @return  The result.
     */
    @SuppressWarnings("unchecked")
	public String viewTraineesToTrainerAssessmentByActualCourseIdAndTrainerId() {
    	Employee employee = SessionObjectUtils.getEmployeeFromSession();
    	logger.info(LogConstants.getInfo(employee.getAugUserName(), "viewTraineesToTrainerAssessmentByActualCourseIdAndTrainerId"));
        Map<String, Object> traineesTotrainerAssessmentMap = null;
        try {
			traineesTotrainerAssessmentMap = assessmentService.getTraineesToTrainerAssessmentByCourseIdAndTranerId(actualCourseId, trainerId, 
					viewAssessmentCondition.getNowPage(), viewAssessmentCondition.getPageSize());
			if (traineesTotrainerAssessmentMap.get(JsonKeyConstants.NEED_ASSESSMENT).equals(JsonKeyConstants.NEED_ASSESSMENT_VALUE) &&
					(Integer)traineesTotrainerAssessmentMap.get(JsonKeyConstants.ASSESSMENT_NO_DATA) == FlagConstants.ASSESSMENT_HAVE_DATA_VALUE) {
				List<AssessmentItem> assessmentItems = 
					(List<AssessmentItem>)traineesTotrainerAssessmentMap.get(JsonKeyConstants.ASSESSMENT_ITEM_AVERAGE_RATE_LIST);
				if (assessmentItems != null && !(assessmentItems.isEmpty())) {
					i18nAveragePart(assessmentItems);
				}
				Page<AssessmentForShow> assessmentForShowPage = 
					(Page<AssessmentForShow>)traineesTotrainerAssessmentMap.get(JsonKeyConstants.ASSESSMENT_PAGE);
				if (assessmentForShowPage.getList() != null && !(assessmentForShowPage.getList().isEmpty())) {
					i18nDetailPart(assessmentForShowPage.getList());
				}
			}
		} catch (ServerErrorException e) {
			this.handleExceptionByServerErrorException(e, true);
		} catch (DataWarningException e) {
			this.handleExceptionByDataWarningException(e, true);
		}
		String[] str = { JsonKeyConstants.ASSESSMENT_EXCEPT_ITEMTYPE };
        JsonConfig jsonConfig = new JsonConfig();
        jsonConfig.setExcludes(str);
		jsonObject = JSONObject.fromObject(traineesTotrainerAssessmentMap, jsonConfig);
    	return SUCCESS;
    }
    
    /**
     * View trainees give assessments to plan 
     * @return
     */
    @SuppressWarnings("unchecked")
	public String viewTraineesToPlanInfo() {
    	Employee employee = SessionObjectUtils.getEmployeeFromSession();
    	logger.info(LogConstants.getInfo(employee.getAugUserName(), "viewTraineesToPlanInfo"));
    	Map<String, Object> traineeToPlanAssessmentMap = null;
    	try {
    		//traineeToPlanAssessmentMap include average score, assessmentPage
    		traineeToPlanAssessmentMap = assessmentService.getAssessmentsForPlanByTrainee(planId, 
    				viewAssessmentCondition.getNowPage(), viewAssessmentCondition.getPageSize());
    		if ((Integer)traineeToPlanAssessmentMap.get(JsonKeyConstants.ASSESSMENT_NO_DATA) == FlagConstants.ASSESSMENT_HAVE_DATA_VALUE) {
    			List <AssessmentItem> assessmentItems = 
    				(List<AssessmentItem>)traineeToPlanAssessmentMap.get(JsonKeyConstants.ASSESSMENT_ITEM_AVERAGE_RATE_LIST);
    			if (assessmentItems != null && !(assessmentItems.isEmpty())) {
    				i18nAveragePart(assessmentItems);
    			}
    			Page<AssessmentForShow> assessmentInfoPage = (Page<AssessmentForShow>)traineeToPlanAssessmentMap.get(JsonKeyConstants.ASSESSMENT_PAGE);
    			if (assessmentInfoPage.getList() != null && !(assessmentInfoPage.getList().isEmpty())) {
    				i18nDetailPart(assessmentInfoPage.getList());
    			}
    		}
    	} catch (ServerErrorException e) {
			this.handleExceptionByServerErrorException(e, true);
		} catch (DataWarningException e) {
			this.handleExceptionByDataWarningException(e, true);
		}
		String[] str = { JsonKeyConstants.ASSESSMENT_EXCEPT_ITEMTYPE };
        JsonConfig jsonConfig = new JsonConfig();
        jsonConfig.setExcludes(str);
		jsonObject = JSONObject.fromObject(traineeToPlanAssessmentMap, jsonConfig);
    	return SUCCESS;
    }
    
    /**
     * Get all assessments trainers to plan
     * @return
     */
    @SuppressWarnings("unchecked")
	public String viewTrainersToPlanInfo(){
    	Employee employee = SessionObjectUtils.getEmployeeFromSession();
    	logger.info(LogConstants.getInfo(employee.getAugUserName(), "viewTrainersToPlanInfo"));
    	Map<String, Object> trainerToPlanAssessmentMap = null;
    	try {
			trainerToPlanAssessmentMap = assessmentService.getAssessmentsForPlanByTrainer(planId, 
					viewAssessmentCondition.getNowPage(), viewAssessmentCondition.getPageSize());
			if ((Integer)trainerToPlanAssessmentMap.get(JsonKeyConstants.ASSESSMENT_NO_DATA) == FlagConstants.ASSESSMENT_HAVE_DATA_VALUE) {
    			List <AssessmentItem> assessmentItems = 
    				(List<AssessmentItem>)trainerToPlanAssessmentMap.get(JsonKeyConstants.ASSESSMENT_ITEM_AVERAGE_RATE_LIST);
    			if (assessmentItems != null && !(assessmentItems.isEmpty())) {
    				i18nAveragePart(assessmentItems);
    			}
    			Page<AssessmentForShow> assessmentInfoPage = (Page<AssessmentForShow>)trainerToPlanAssessmentMap.get(JsonKeyConstants.ASSESSMENT_PAGE);
    			if (assessmentInfoPage.getList() != null && !(assessmentInfoPage.getList().isEmpty())) {
    				i18nDetailPart(assessmentInfoPage.getList());
    			}
    		}
		} catch (ServerErrorException e) {
			this.handleExceptionByServerErrorException(e, true);
		} catch (DataWarningException e) {
			this.handleExceptionByDataWarningException(e, true);
		}
		jsonObject = new JSONObject();
		String[] str = { JsonKeyConstants.ASSESSMENT_EXCEPT_ITEMTYPE };
        JsonConfig jsonConfig = new JsonConfig();
        jsonConfig.setExcludes(str);
        jsonObject = JSONObject.fromObject(trainerToPlanAssessmentMap, jsonConfig);
    	return SUCCESS;
    }
    
    /**
     * Get plan course need assessment.
     * @return
     */
    public String getActualCourseNeedAssessment() {
    	Employee employee = SessionObjectUtils.getEmployeeFromSession();
    	logger.info(LogConstants.getInfo(employee.getAugUserName(), "getActualCourseNeedAssessment"));
    	ActualCourseService actualCourseService = BeanFactory.getActualCourseService();
    	List<ActualCourse> actualCourseList = null;
		try {
			actualCourseList = actualCourseService.findActualCoursesByPlanId(planId);
		} catch (ServerErrorException e) {
			this.handleExceptionByServerErrorException(e, true);
		} catch (DataWarningException e) {
			this.handleExceptionByDataWarningException(e, true);
		}
    	if (actualCourseList != null && actualCourseList.size() > 0) {
    		JsonConfig jsonConfig = new JsonConfig();
    		String[] excludeStr = JsonKeyConstants.ACTUAL_COURSE_NEED_ASSESSMENT_EXCLUDES;
    		jsonConfig.setExcludes(excludeStr);
    		jsonArray = JSONArray.fromObject(actualCourseList, jsonConfig);
    		return SUCCESS;
    	} else {
    		jsonObject = new JSONObject();
    		jsonObject.put(JsonKeyConstants.PLAN_NO_COURSE, FlagConstants.NO_COURSE);
    		return JsonKeyConstants.PLAN_NO_COURSE;
    	}
    	
    }
    
    @SuppressWarnings("unchecked")
	public String viewTraineesToCourseInfo() {
    	Employee employee = SessionObjectUtils.getEmployeeFromSession();
    	logger.info(LogConstants.getInfo(employee.getAugUserName(), "viewTraineesToCourseInfo"));
    	Map<String, Object> traineesToCourseAssessmentMap = null;
    	try {
    		traineesToCourseAssessmentMap = assessmentService.getAssessmentsForActualCourseByTrainee(planId, actualCourseId,
					viewAssessmentCondition.getNowPage(), viewAssessmentCondition.getPageSize());
    		if ((Integer)traineesToCourseAssessmentMap.get(JsonKeyConstants.ASSESSMENT_NO_DATA) == FlagConstants.ASSESSMENT_HAVE_DATA_VALUE){
    			List<AssessmentItem> assessmentItems = 
    				(List<AssessmentItem>) traineesToCourseAssessmentMap.get(JsonKeyConstants.ASSESSMENT_ITEM_AVERAGE_RATE_LIST);
    			if (assessmentItems != null && !(assessmentItems.isEmpty())) {
    				i18nAveragePart(assessmentItems);
    			}
    			Page<AssessmentForShow> assessmentPage = (Page<AssessmentForShow>) traineesToCourseAssessmentMap.get(JsonKeyConstants.ASSESSMENT_PAGE);
    			if (assessmentPage.getList() != null && !(assessmentPage.getList().isEmpty())) {
    				i18nDetailPart(assessmentPage.getList());
    			}
    		}
		} catch (ServerErrorException e) {
			this.handleExceptionByServerErrorException(e, true);
		} catch (DataWarningException e) {
			this.handleExceptionByDataWarningException(e, true);
		}
		jsonObject = new JSONObject();
		String[] str = { JsonKeyConstants.ASSESSMENT_EXCEPT_ITEMTYPE };
        JsonConfig jsonConfig = new JsonConfig();
        jsonConfig.setExcludes(str);
        jsonObject = JSONObject.fromObject(traineesToCourseAssessmentMap, jsonConfig);
    	return SUCCESS;
    }
    
    public String viewTrainerToCourseInfo() {
    	Employee employee = SessionObjectUtils.getEmployeeFromSession();
    	logger.info(LogConstants.getInfo(employee.getAugUserName(), "viewTrainerToCourseInfo"));
    	AssessmentForShow assessmentForShow = null;
    	try {
			assessmentForShow = assessmentService.getAssessmentsForActualCourseByTrainer(planId, actualCourseId);
		}  catch (ServerErrorException e) {
			this.handleExceptionByServerErrorException(e, true);
		} catch (DataWarningException e) {
			this.handleExceptionByDataWarningException(e, true);
		}
		if (null == assessmentForShow || "".equals(assessmentForShow.getAssessComment())){
			jsonObject = new JSONObject();
			jsonObject.put(JsonKeyConstants.ASSESSMENT_NO_DATA, FlagConstants.ASSESSMENT_NO_DATA_VALUE);
		} else {
			jsonObject = JSONObject.fromObject(assessmentForShow);
			jsonObject.put(JsonKeyConstants.ASSESSMENT_NO_DATA, FlagConstants.ASSESSMENT_HAVE_DATA_VALUE);
		}
    	
    	return SUCCESS;
    }
    
    @SuppressWarnings("unchecked")
	public String viewAverageScoreOfTrainer() {
    	Employee employee = SessionObjectUtils.getEmployeeFromSession();
    	logger.info(LogConstants.getInfo(employee.getAugUserName(), "viewAverageScoreOfTrainer"));
    	Map<String, Object> traineeToTrainerAverageMap = new HashMap<String, Object>();
    	try {
    		traineeToTrainerAverageMap = assessmentService.getAverageScoreToTrainerByCourseIdAndTranerId(actualCourseId, trainerId);
		}  catch (ServerErrorException e) {
			this.handleExceptionByServerErrorException(e, true);
		} catch (DataWarningException e) {
			this.handleExceptionByDataWarningException(e, true);
		}
		List<AssessmentItem> assessmentItemList = (List<AssessmentItem>) traineeToTrainerAverageMap.get(JsonKeyConstants.ASSESSMENT_ITEM_AVERAGE_RATE_LIST);
		if (null ==assessmentItemList || assessmentItemList.isEmpty()) {
			traineeToTrainerAverageMap.put(JsonKeyConstants.ASSESSMENT_NO_DATA, FlagConstants.ASSESSMENT_NO_DATA_VALUE);
		} else {
			traineeToTrainerAverageMap.put(JsonKeyConstants.ASSESSMENT_NO_DATA, FlagConstants.ASSESSMENT_HAVE_DATA_VALUE);
			i18nAveragePart(assessmentItemList);
		}
		jsonObject = JSONObject.fromObject(traineeToTrainerAverageMap);
    	return SUCCESS;
    }
    
    /**
     * i18n on average part
     * @param assessmentItems
     */
    private void i18nAveragePart(List<AssessmentItem> assessmentItems) {
    	for (AssessmentItem assessmentItem : assessmentItems) {
			assessmentItem.setAssessItemName(getText(assessmentItem.getAssessItemName()));
		}
    }
    
    /**
     * i18n on detail part.
     * @param assessmentForShowList
     */
    private void i18nDetailPart(List<AssessmentForShow> assessmentForShowList) {
    	for (AssessmentForShow assessmentForShow : assessmentForShowList) {
			for (ScoreForShow scoreForShow : assessmentForShow.getScoreList()) {
				if (null != scoreForShow && null != scoreForShow.getItemName() && !("".equals(scoreForShow.getItemName()))){
					scoreForShow.setItemName(getText(scoreForShow.getItemName()));
				}
			}
		}
    }


    @JSON(serialize = false)
    public Integer getPlanId() {
        return planId;
    }

    public void setPlanId(Integer planId) {
        this.planId = planId;
    }

    @JSON(serialize = false)
    public Integer getActualCourseId() {
        return actualCourseId;
    }

    public void setActualCourseId(Integer actualCourseId) {
        this.actualCourseId = actualCourseId;
    }

    @JSON(serialize = false)
    public Integer getTraineeId() {
        return traineeId;
    }

    public void setTraineeId(Integer traineeId) {
        this.traineeId = traineeId;
    }

    public JSONObject getJsonObject() {
        return jsonObject;
    }

    public void setJsonObject(JSONObject jsonObject) {
        this.jsonObject = jsonObject;
    }

    @JSON(serialize = false)
    public PlanService getPlanService() {
        return planService;
    }

    public void setPlanService(PlanService planService) {
        this.planService = planService;
    }

    @JSON(serialize = false)
    public AssessmentService getAssessmentService() {
        return assessmentService;
    }

    public void setAssessmentService(AssessmentService assessmentService) {
        this.assessmentService = assessmentService;
    }

    @JSON(serialize = false)
    public Integer getTrainerId() {
        return trainerId;
    }

    public void setTrainerId(Integer trainerId) {
        this.trainerId = trainerId;
    }

    @JSON(serialize = false)
    public ViewAssessmentCondition getViewAssessmentCondition() {
        return viewAssessmentCondition;
    }

    
	public void setViewAssessmentCondition(
            ViewAssessmentCondition viewAssessmentCondition) {
        this.viewAssessmentCondition = viewAssessmentCondition;
    }

    public JSONArray getJsonArray() {
		return jsonArray;
	}

	public void setJsonArray(JSONArray jsonArray) {
		this.jsonArray = jsonArray;
	}
}
