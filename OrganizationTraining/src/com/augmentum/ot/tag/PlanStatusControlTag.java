package com.augmentum.ot.tag;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.log4j.Logger;

import com.augmentum.ot.dataObject.constant.DateFormatConstants;
import com.augmentum.ot.dataObject.constant.FlagConstants;
import com.augmentum.ot.dataObject.constant.JsonKeyConstants;
import com.augmentum.ot.dataObject.constant.RoleNameConstants;
import com.augmentum.ot.exception.ServerErrorException;
import com.augmentum.ot.model.ActualCourse;
import com.augmentum.ot.model.Assessment;
import com.augmentum.ot.model.Employee;
import com.augmentum.ot.model.EmployeeRoleLevelMap;
import com.augmentum.ot.model.LeaveNote;
import com.augmentum.ot.model.Plan;
import com.augmentum.ot.model.PlanEmployeeMap;
import com.augmentum.ot.service.AssessmentService;
import com.augmentum.ot.service.LeaveNoteService;
import com.augmentum.ot.service.PlanEmployeeMapService;
import com.augmentum.ot.util.BeanFactory;
import com.augmentum.ot.util.DateHandlerUtils;
import com.augmentum.ot.util.SessionObjectUtils;




/** 
* @ClassName: PlanStatusControlTag 
* @Description: control page element is visible or unvisible by plan status
* @date 2012-8-24 
* @version V1.0 
*/
public class PlanStatusControlTag extends TagSupport{

	private static final long serialVersionUID = -3183761581105316295L;

	private static Logger logger = Logger.getLogger(PlanStatusControlTag.class);
    
    private Plan plan;
    private ActualCourse actualCourse;
    private int controlObjectFlag;
    private Employee employee;
    private AssessmentService assessmentService;

    public int doStartTag() throws JspException {
		employee = SessionObjectUtils.getEmployeeFromSession();
		List<EmployeeRoleLevelMap> roleMap = employee.getRoleLevelsForEmployee();
		assessmentService = BeanFactory.getAssessmentService();
        List<String> employeeRoleNames = new ArrayList<String>();
        for (EmployeeRoleLevelMap role : roleMap) {
        	employeeRoleNames.add(role.getRole().getRoleName());
        }
	    //if plan is published, publish button is invisible.
	    if (controlObjectFlag == FlagConstants.PUBLISH_BTN) {
	        if(plan.getPlanIsPublish() == FlagConstants.UN_PUBLISHED){
	            List<String> rolesInPlan = getRelationWithPlan(employeeRoleNames);
	            if(employeeRoleNames.contains(RoleNameConstants.ADMIN) 
	                    || rolesInPlan.contains(RoleNameConstants.TRAINING_MASTER)){
	                return EVAL_PAGE;
	            }
	        }
	        return SKIP_BODY;
	    }
	    //if plan was published, but not finished, cancel button is visible.
	    if (controlObjectFlag == FlagConstants.CANCEL_BTN) {
	        if ((plan.getPlanIsCanceled() == FlagConstants.UN_CANCELED)
	                && (plan.getPlanIsPublish() == FlagConstants.IS_PUBLISHED)
	                && (plan.getPlanExecuteEndTime() == null || plan.getPlanExecuteEndTime().after(new Date()))) {
	               List<String> rolesInPlan = getRelationWithPlan(employeeRoleNames);
	                if(rolesInPlan.contains(RoleNameConstants.TRAINING_MASTER)){
	                    return EVAL_PAGE;
	                }
	        }
	        return SKIP_BODY;
        }
	    //trainee apply leave for a plan 
        if (controlObjectFlag == FlagConstants.TRAINEE_APPLY_LEAVE_PLAN_BTN) {
            PlanEmployeeMapService planEmployeeMapService = BeanFactory.getPlanEmployeeMapService();
            LeaveNoteService leaveNoteService = BeanFactory.getLeaveNoteService();
            List<String> relations = getRelationWithPlan(employeeRoleNames);
            if(relations.contains(RoleNameConstants.TRAINEE) 
                   && (plan.getPlanExecuteEndTime() != null && plan.getPlanExecuteEndTime().after(new Date()) ||
                		   plan.getPlanExecuteEndTime() == null) //end time is null or plan is not end.
                   && plan.getPlanIsPublish() == FlagConstants.IS_PUBLISHED
                   && plan.getPlanIsDeleted() == FlagConstants.UN_DELETED
                   && plan.getPlanIsCanceled() == FlagConstants.UN_CANCELED){
               boolean isInvited = false;
               try {
                   isInvited = planEmployeeMapService.getIfEmployeeInvited(plan.getPlanId(),employee.getEmployeeId());
               } catch (ServerErrorException e) {
                   logger.error(e);
               }
               if(isInvited){
                   List<ActualCourse> actualCourses = plan.getActualCourses();
                   List<Integer> unStartedCourseIds = new ArrayList<Integer>();
                   List<Integer> leavedCourseIds = new ArrayList<Integer>();
                   Date now = new Date();
                   for(ActualCourse actualCourse:actualCourses){
                       if((actualCourse.getCourseStartTime()== null || actualCourse.getCourseStartTime().after(now))){
                           unStartedCourseIds.add(actualCourse.getActualCourseId());
                       }
                   }
                   List<LeaveNote> leaveNotes = null;
                   try {
                       leaveNotes = leaveNoteService.getLeaveNoteByEmployeeIdAndPlanId(employee.getEmployeeId(), plan.getPlanId());
                   } catch (ServerErrorException e) {
                       e.printStackTrace();
                   }
                   for(LeaveNote leaveNote:leaveNotes){
                       leavedCourseIds.add(leaveNote.getActualCourse().getActualCourseId());
                   }
                   //at least one course that has not been applied leave
                   for(Integer courseId:unStartedCourseIds){
                       if(leavedCourseIds.indexOf(courseId) == -1){
                           return EVAL_PAGE;
                       }
                   }
               }
            } 
            return SKIP_BODY;
        }
        
        //trainee cancel leave and back to plan 
        if (controlObjectFlag == FlagConstants.TRAINEE_APPLY_BACK_PLAN_BTN) {
            PlanEmployeeMapService planEmployeeMapService = BeanFactory.getPlanEmployeeMapService();
            LeaveNoteService leaveNoteService = BeanFactory.getLeaveNoteService();
            List<String> relations = getRelationWithPlan(employeeRoleNames);
            if(relations.contains(RoleNameConstants.TRAINEE) 
                   && (plan.getPlanExecuteEndTime() != null && plan.getPlanExecuteEndTime().after(new Date()) ||
                		   plan.getPlanExecuteEndTime() == null) //end time is null or plan is not end.
                   && plan.getPlanIsPublish() == FlagConstants.IS_PUBLISHED
                   && plan.getPlanIsDeleted() == FlagConstants.UN_DELETED
                   && plan.getPlanIsCanceled() == FlagConstants.UN_CANCELED){
               boolean isInvited = false;
               try {
                   isInvited = planEmployeeMapService.getIfEmployeeInvited(plan.getPlanId(),employee.getEmployeeId());
               } catch (ServerErrorException e) {
                   logger.error(e);
               }
               if(isInvited){
                   List<ActualCourse> actualCourses = plan.getActualCourses();
                   List<Integer> unStartedCourseIds = new ArrayList<Integer>();
                   List<Integer> leavedCourseIds = new ArrayList<Integer>();
                   Date now = new Date();
                   for(ActualCourse actualCourse:actualCourses){
                       if(actualCourse.getCourseStartTime() == null ||
                    		   (actualCourse.getCourseStartTime()!= null && actualCourse.getCourseStartTime().after(now))){
                           unStartedCourseIds.add(actualCourse.getActualCourseId());
                       }
                   }
                   List<LeaveNote> leaveNotes = null;
                   try {
                       leaveNotes = leaveNoteService.getLeaveNoteByEmployeeIdAndPlanId(employee.getEmployeeId(), plan.getPlanId());
                   } catch (ServerErrorException e) {
                       e.printStackTrace();
                   }
                   for(LeaveNote leaveNote:leaveNotes){
                       leavedCourseIds.add(leaveNote.getActualCourse().getActualCourseId());
                   }
                   //at least one course that has not started been leaved
                   for(Integer courseId:unStartedCourseIds){
                       if(leavedCourseIds.indexOf(courseId) != -1){
                           return EVAL_PAGE;
                       }
                   }
               }
            } 
            return SKIP_BODY;
        }
	    //assess plan by trainee
        if (controlObjectFlag == FlagConstants.TRAINEE_ASSESS_PLAN_BTN) {
            try { 
                if(isPlanCanBeAssessed()){
                    List<String> relations = getRelationWithPlan(employeeRoleNames);
                    //if the employee is a trainer of the plan, ignore the situation that he is trainee of the plan
                    if(relations.contains(RoleNameConstants.TRAINER) || relations.contains(RoleNameConstants.TRAINING_MASTER)){
                        return SKIP_BODY;
                    }
                    if(relations.contains(RoleNameConstants.TRAINEE)){
                        Map<String,Boolean> beenAssessedMap = isPlanBeenAssessed(relations);
                        if(!beenAssessedMap.get(RoleNameConstants.TRAINEE)) {
                            return EVAL_PAGE;
                        }
                    }
                }
            } catch (ServerErrorException e) {
                logger.error(e);
            }
            return SKIP_BODY;
        }
        //trainee view plan assessment
        if (controlObjectFlag == FlagConstants.TRAINEE_VIEW_PLAN_ASSESSMENT_BTN) {
            List<String> relations = getRelationWithPlan(employeeRoleNames);
            //if the employee is a trainer of the plan, ignore the situation that he is trainee of the plan
            if(relations.contains(RoleNameConstants.TRAINER)){
                return SKIP_BODY;
            }
            if(relations.contains(RoleNameConstants.TRAINEE)){
                Map<String, Boolean> isBeenAssessedMap = null;
                try {
                    isBeenAssessedMap = isPlanBeenAssessed(relations);
                } catch (ServerErrorException e) {
                    logger.error(e);
                }
                if(isBeenAssessedMap.get(RoleNameConstants.TRAINEE)) {
                    try {
                        if(!DateHandlerUtils.isAssessmentExpired(plan.getPlanExecuteEndTime())){
                            return EVAL_PAGE;
                        }
                    } catch (ServerErrorException e) {
                        logger.error(e);
                    }
                }
            }
            return SKIP_BODY;
        }
        //assess plan by trainer
        if (controlObjectFlag == FlagConstants.TRAINER_ASSESS_PLAN_BTN) {
            try { 
                if(isPlanCanBeAssessed()){
                    List<String> relations = getRelationWithPlan(employeeRoleNames);
                    // if the trainer is also a training master of the plan, ignore the situation that he is a trainer of the plan
                    if(relations.contains(RoleNameConstants.TRAINING_MASTER)){
                        return SKIP_BODY;
                    }
                    if(relations.contains(RoleNameConstants.TRAINER)){
                        Map<String,Boolean> beenAssessedMap = isPlanBeenAssessed(relations);
                        if(!beenAssessedMap.get(RoleNameConstants.TRAINER)) {
                            return EVAL_PAGE;
                        }
                    }
                }
            } catch (ServerErrorException e) {
                logger.error(e);
            }
            return SKIP_BODY;
        }
        //trainer view plan assessment
        if (controlObjectFlag == FlagConstants.TRAINER_VIEW_PLAN_ASSESSMENT_BTN) {
            List<String> relations = getRelationWithPlan(employeeRoleNames);
            // if the trainer is also a training master of the plan, ignore the situation that he is a trainer of the plan
            if(relations.contains(RoleNameConstants.TRAINING_MASTER)){
                return SKIP_BODY;
            }
            if(relations.contains(RoleNameConstants.TRAINER)){
                Map<String, Boolean> isBeenAssessedMap = null;
                try {
                    isBeenAssessedMap = isPlanBeenAssessed(relations);
                } catch (ServerErrorException e) {
                    logger.error(e);
                }
                if(isBeenAssessedMap.get(RoleNameConstants.TRAINER)) {
                    try {
                        if(!DateHandlerUtils.isAssessmentExpired(plan.getPlanExecuteEndTime())){
                            return EVAL_PAGE;
                        }
                    } catch (ServerErrorException e) {
                        logger.error(e);
                    }
                }
            }
          return SKIP_BODY;
        }
        //master assess trainer
        if (controlObjectFlag == FlagConstants.MASTER_ASSESS_TRAINER_BTN) {
            try {
                if(isPlanCanBeAssessed()){ 
                    List<String> relations = getRelationWithPlan(employeeRoleNames);
                    if(relations.contains(RoleNameConstants.TRAINING_MASTER)){
                        Map<String,Boolean> beenAssessedMap = isPlanBeenAssessed(relations);
                        if (plan.getTrainers().equals(employee.getAugUserName())) {
                        	return SKIP_BODY;
                        }
                        if(!beenAssessedMap.get(RoleNameConstants.TRAINING_MASTER)) {
                            return EVAL_PAGE;
                        }
                    }
                }
            } catch (ServerErrorException e) {
                logger.error(e);
            }
          return SKIP_BODY;
        }
        //master view trainer assessment
        if (controlObjectFlag == FlagConstants.MASTER_VIEW_TRAINER_ASSESSMENT_BTN) {
            List<String> relations = getRelationWithPlan(employeeRoleNames);
            if(relations.contains(RoleNameConstants.TRAINING_MASTER)){
                Map<String, Boolean> isBeenAssessedMap = null;
                try {
                    isBeenAssessedMap = isPlanBeenAssessed(relations);
                } catch (ServerErrorException e) {
                    logger.error(e);
                }
                if(isBeenAssessedMap.get(RoleNameConstants.TRAINING_MASTER)) {
                    try {
                        if(!DateHandlerUtils.isAssessmentExpired(plan.getPlanExecuteEndTime())){
                            return EVAL_PAGE;
                        }
                    } catch (ServerErrorException e) {
                        logger.error(e);
                    }
                }
            }
          return SKIP_BODY;
        }
        
        //trainee apply leave for a course
        if(controlObjectFlag == FlagConstants.APPLY_LEAVE_BTN){
        	List<String> relations = getRelationWithPlanCourse();
        	if (((relations.contains(RoleNameConstants.TRAINEE) 
        	        && actualCourse.getCourseStartTime() != null
        	        && actualCourse.getCourseStartTime().after(new Date()))
        	        || (relations.contains(RoleNameConstants.TRAINEE) && actualCourse.getCourseStartTime() == null))
        	        && actualCourse.getPlan().getPlanIsPublish() == FlagConstants.IS_PUBLISHED) {
        	    List<LeaveNote> leaveNotes = actualCourse.getLeaveNoteList();
        	    boolean isLeaved = false;
                for(LeaveNote leaveNote:leaveNotes){
                    if(leaveNote.getEmployee().getEmployeeId().equals(employee.getEmployeeId())){ 
                        isLeaved = true;
                        break;
                    }
                }
                if(!isLeaved){
                    return EVAL_PAGE;
                }
        	}
            return SKIP_BODY;
        }
        
        //trainee back to course 
        if(controlObjectFlag == FlagConstants.TRAINEE_APPLY_BACK_COURSE_BTN){
            List<LeaveNote> leaveNotes = actualCourse.getLeaveNoteList();
            for(LeaveNote leaveNote:leaveNotes){
                if(leaveNote.getEmployee().getEmployeeId().equals(employee.getEmployeeId())){ 
                    return EVAL_PAGE;
                }
            }
            return SKIP_BODY;
        }
        //trainee assess course and trainer
        if(controlObjectFlag == FlagConstants.TRAINEE_ASSESS_COURSE_BTN){
            if(actualCourse.getCourseInfo().getTrainee2Trainer() == FlagConstants.UN_NEED_ASSESSMENT){
                return SKIP_BODY;
            }
            try {
                if(isPlanCourseCanBeAssessed()){
                    List<String> relations = getRelationWithPlanCourse();
                    if(relations.contains(RoleNameConstants.TRAINEE)){
                        Map<String,Boolean> hasBeenAssessed = isActualCourseBeenAssessed(relations);
                        if(!hasBeenAssessed.get(RoleNameConstants.TRAINEE)){
                            return EVAL_PAGE;
                        }
                    }
                }
            } catch (ServerErrorException e) {
                logger.error(e);
            }
            return SKIP_BODY;
        }
        //trainee view assessment of course
        if(controlObjectFlag == FlagConstants.TRAINEE_VIEW_COURSE_ASSESSMENT_BTN){
            List<String> relations = getRelationWithPlanCourse();
            if(relations.contains(RoleNameConstants.TRAINEE)){
                Map<String, Boolean> hasBeenAssessed = null;
                try {
                    hasBeenAssessed = isActualCourseBeenAssessed(relations);
                } catch (ServerErrorException e) {
                    logger.error(e);
                }
                if(hasBeenAssessed.get(RoleNameConstants.TRAINEE)){
                    try {
                        if(!DateHandlerUtils.isAssessmentExpired(actualCourse.getCourseEndTime())){
                            return EVAL_PAGE;
                        }
                    } catch (ServerErrorException e) {
                        logger.error(e);
                    }
                }
            }
            return SKIP_BODY;
        }
        //trainer assess trainee and planCourse
        if(controlObjectFlag == FlagConstants.TRAINER_ASSESS_TRAINEE_BTN){
            if(actualCourse.getCourseInfo().getTrainer2Trainee() == FlagConstants.UN_NEED_ASSESSMENT){
                return SKIP_BODY;
            }
            try {
                if(isPlanCourseCanBeAssessed()){
                    List<String> relations = getRelationWithPlanCourse();
                    if(relations.contains(RoleNameConstants.TRAINER)){
                       Map<String,Boolean> hasBeenAssessed = null;
                       hasBeenAssessed = isTraineeBeenAssessed(relations);
                       if(!hasBeenAssessed.get(RoleNameConstants.TRAINER)){
                           return EVAL_PAGE;
                       }
                    }
                }
            } catch (ServerErrorException e) {
                logger.error(e);
            }
            return SKIP_BODY;
        }
        //trainer view assessment of trainee
        if(controlObjectFlag == FlagConstants.TRAINER_VIEW_TRAINEE_ASSESSMENT_BTN){
            List<String> relations = getRelationWithPlanCourse();
            if(relations.contains(RoleNameConstants.TRAINER)){
                Map<String,Boolean> hasBeenAssessed = null;
                try {
                    hasBeenAssessed = isTraineeBeenAssessed(relations);
                } catch (ServerErrorException e) {
                    logger.error(e);
                }
                if(hasBeenAssessed.get(RoleNameConstants.TRAINER)){
                    try {
                        if(!DateHandlerUtils.isAssessmentExpired(actualCourse.getCourseEndTime())){
                            return EVAL_PAGE;
                        }
                    } catch (ServerErrorException e) {
                        logger.error(e);
                    }
                }
             }
            return SKIP_BODY;
        }
	    // For view all assessments button.
	    if (controlObjectFlag == FlagConstants.VIEW_ALL_ASSESSMENT_BTN) {
	    	if (plan.getPlanIsPublish().equals(FlagConstants.IS_PUBLISHED) 
	    			&& checkPlanNeedAssessment(plan) && employee.getAugUserName().equals(plan.getPlanCreator())) {
	    		return EVAL_PAGE;
	    	} else {
	    		return SKIP_BODY;
	    	}
	    }
        return EVAL_PAGE;
    }

    /**
     * check whether the plan can be assessed
     * 
     * @param
     * @return true: can be assessed
     */
    private boolean isPlanCanBeAssessed() throws ServerErrorException{
    	Date currentDate = new Date();
        if(plan.getPlanIsCanceled() == FlagConstants.UN_CANCELED
                && plan.getPlanIsPublish() == FlagConstants.IS_PUBLISHED
                && plan.getPlanExecuteEndTime() != null 
                && plan.getPlanExecuteEndTime().before(currentDate)
                && DateHandlerUtils.getDaysBetweenDate(plan.getPlanExecuteEndTime(), currentDate) <= DateFormatConstants.EXPIRED_DEFAULT_DAY
                && plan.getNeedAssessment() == FlagConstants.NEED_ASSESSMENT){
            return true;
        }
        return false;
    }
    
    /**
     * check the relationship of employee who login the system and the plan
     * 
     * @param List<String> employeeRoleNames : whether the user is Master
     * @return List<String> : the list containing relations
     */
    private List<String> getRelationWithPlan(List<String> employeeRoleNames){
        List<String> relations = new ArrayList<String>();
        //check whether the user is Master of this plan
        if(employeeRoleNames.contains(RoleNameConstants.TRAINING_MASTER) || employeeRoleNames.contains(RoleNameConstants.ADMIN)){
        	if(plan.getPlanCreator().equals(employee.getAugUserName())){
        		relations.add(RoleNameConstants.TRAINING_MASTER);
        	}
        }
        //check whether the user is a trainer of the plan
        for(ActualCourse actualCourse : plan.getActualCourses()){
            if ((actualCourse.getCourseTrainer()).equals(employee.getAugUserName())) {
                relations.add(RoleNameConstants.TRAINER);
                break;
            }
        }
        //check whether the user is a trainee of the plan
        for (PlanEmployeeMap planEmployeeMap : plan.getPlanEmployeeMapList()) {
        	if ((planEmployeeMap.getPlanTraineeAttendType().equals(FlagConstants.ATTEND_TYPE_INVITED) ||
        			planEmployeeMap.getPlanTraineeAttendType().equals(FlagConstants.ATTEND_TYPE_JOIN))
        		&& planEmployeeMap.getEmployee().getAugUserName().equals(employee.getAugUserName())
        	) {
        		relations.add(RoleNameConstants.TRAINEE);
                break;
        	}
        }
        return relations;
    }
    
    /**
     * check whether the plan has already been assessed
     * 
     * @param List<String> relations
     * @return Map<String,Boolean> : the key is the relation and the value is a Boolean type which implies whether the user has assessed the plan
     */
    private Map<String,Boolean> isPlanBeenAssessed(List<String> relations) throws ServerErrorException{
        List<Assessment> assessmentList = null;
        Map<String,Boolean> hasBeenAssessedMap = new HashMap<String,Boolean>();
        Map<String,Integer> assessmentFieldMap = new HashMap<String,Integer>();
        //check whether the training master has assessed the trainers
        if(relations.contains(RoleNameConstants.TRAINING_MASTER)){
            assessmentFieldMap.clear();
            assessmentFieldMap.put(JsonKeyConstants.PLAN_ID, plan.getPlanId());
            assessmentFieldMap.put(JsonKeyConstants.MASTER_ID, employee.getEmployeeId());
            assessmentList = assessmentService.getAssessmentByFields(assessmentFieldMap,FlagConstants.TRAINING_MASTER_TO_TRAINERS);
            if(assessmentList == null || assessmentList.isEmpty()){
                hasBeenAssessedMap.put(RoleNameConstants.TRAINING_MASTER, false);
            } else {
                hasBeenAssessedMap.put(RoleNameConstants.TRAINING_MASTER, true);
            }
        }
        //check whether the trainer has assessed the plan
        if(relations.contains(RoleNameConstants.TRAINER)){
            assessmentFieldMap.clear();
            assessmentFieldMap.put(JsonKeyConstants.PLAN_ID, plan.getPlanId());
            assessmentFieldMap.put(JsonKeyConstants.TRAINER_ID, employee.getEmployeeId());
            assessmentList = assessmentService.getAssessmentByFields(assessmentFieldMap,FlagConstants.TRAINER_TO_PLAN);
            if(assessmentList == null || assessmentList.isEmpty()){
                hasBeenAssessedMap.put(RoleNameConstants.TRAINER, false);
            } else {
                hasBeenAssessedMap.put(RoleNameConstants.TRAINER, true);
            }
        }
        //check whether the trainee has assessed the plan
        if(relations.contains(RoleNameConstants.TRAINEE)){
            assessmentFieldMap.clear();
            assessmentFieldMap.put(JsonKeyConstants.PLAN_ID, plan.getPlanId());
            assessmentFieldMap.put(JsonKeyConstants.TRAINEE_ID, employee.getEmployeeId());
            assessmentList = assessmentService.getAssessmentByFields(assessmentFieldMap,FlagConstants.TRAINEE_TO_PLAN);
            if(assessmentList == null || assessmentList.isEmpty()){
                hasBeenAssessedMap.put(RoleNameConstants.TRAINEE, false);
            } else {
                hasBeenAssessedMap.put(RoleNameConstants.TRAINEE, true);
            }
        }
        return hasBeenAssessedMap;
    }
    
    /**
     * check whether the planCourse can be assessed
     * 
     * @param
     * @return true : can be assessed
     */
    private boolean isPlanCourseCanBeAssessed() throws ServerErrorException{
    	Date currentTime = new Date();
        if(plan.getPlanIsDeleted() == FlagConstants.UN_DELETED
                && plan.getPlanIsCanceled() == FlagConstants.UN_CANCELED
                && plan.getPlanIsPublish() == FlagConstants.IS_PUBLISHED
                && actualCourse.getCourseEndTime() != null
                && actualCourse.getCourseEndTime().before(currentTime)
                && DateHandlerUtils.getDaysBetweenDate(actualCourse.getCourseEndTime(), currentTime) <= DateFormatConstants.EXPIRED_DEFAULT_DAY){
            return true;
        }
        return false;
    }
    
    /**
     * check the relation of the user who login in the system and the planCourse
     * 
     * @param
     * @return List<String>
     */
    private List<String> getRelationWithPlanCourse(){
        List<String> relations = new ArrayList<String>();
        //check whether the user is Master of this plan course
        if (actualCourse.getPlan().getPlanCreator().equals(employee.getAugUserName())) {
        	relations.add(RoleNameConstants.TRAINING_MASTER);
        }
        //check whether the user is Trainer of this plan course
        if (actualCourse.getCourseTrainer().equals(employee.getAugUserName())) {
            relations.add(RoleNameConstants.TRAINER);
        }
        //check whether the user is Trainee of this plan course
        List<Employee> actualCourseTrainees = new ArrayList<Employee>();
        actualCourseTrainees = actualCourse.getEmployeeList();
        for(Employee trainee : actualCourseTrainees) {
            if(trainee.getAugUserName().equals(employee.getAugUserName())) {
                relations.add(RoleNameConstants.TRAINEE);
                break;
            }
        } 
        return relations;
    }
    
    /**
     * check whether the planCourse has been assessed
     * 
     * @param List<String> relations
     * @return Map<String,Boolean> : the key is the relationship and the value is a Boolean type which implies whether the planCourse has been assessed
     */
    private Map<String,Boolean> isActualCourseBeenAssessed(List<String> relations) throws ServerErrorException{
        List<Assessment> assessmentList = null;
        Map<String,Boolean> hasBeenAssessedMap = new HashMap<String,Boolean>();
        Map<String,Integer> assessmentFieldMap = new HashMap<String,Integer>();
        if(relations.contains(RoleNameConstants.TRAINEE)){
            assessmentFieldMap.put(JsonKeyConstants.TRAINEE_ID, employee.getEmployeeId());
            assessmentFieldMap.put(JsonKeyConstants.PLAN_COURSE_ID, actualCourse.getActualCourseId());
            assessmentList = assessmentService.getAssessmentByFields(assessmentFieldMap,FlagConstants.TRAINEE_TO_COURSE);
            if(assessmentList == null || assessmentList.isEmpty()){
                hasBeenAssessedMap.put(RoleNameConstants.TRAINEE, false);
            } else {
                hasBeenAssessedMap.put(RoleNameConstants.TRAINEE, true);
            }
        }
        return hasBeenAssessedMap;
    }
    
    /**
     * check whether the trainee has been assessed by trainer 
     * 
     * @param List<String> relations
     * @return Map<String,Boolean> 
     */
    private Map<String,Boolean> isTraineeBeenAssessed(List<String> relations) throws ServerErrorException{
        List<Assessment> assessmentList = null;
        Map<String,Boolean> hasBeenAssessedMap = new HashMap<String,Boolean>();
        Map<String,Integer> assessmentFieldMap = new HashMap<String,Integer>();
        if(relations.contains(RoleNameConstants.TRAINER)){
            assessmentFieldMap.put(JsonKeyConstants.PLAN_ID, plan.getPlanId());
            assessmentFieldMap.put(JsonKeyConstants.TRAINER_ID, employee.getEmployeeId());
            assessmentFieldMap.put(JsonKeyConstants.PLAN_COURSE_ID, actualCourse.getActualCourseId());
            assessmentList = assessmentService.getAssessmentByFields(assessmentFieldMap,FlagConstants.TRAINER_TO_TRAINEES);
            if(assessmentList == null || assessmentList.isEmpty()){
                hasBeenAssessedMap.put(RoleNameConstants.TRAINER, false);
            } else {
                hasBeenAssessedMap.put(RoleNameConstants.TRAINER, true);
            }
        }
        return hasBeenAssessedMap;
    }
    
    private boolean checkPlanNeedAssessment(Plan plan) {
    	if (plan.getNeedAssessment() == FlagConstants.NEED_ASSESSMENT) {
    		return true;
    	}
    	for (ActualCourse actualCourse : plan.getActualCourses()) {
    		if (actualCourse.getCourseInfo().getTrainee2Trainer() == FlagConstants.NEED_ASSESSMENT 
    				|| actualCourse.getCourseInfo().getTrainer2Trainee() == FlagConstants.NEED_ASSESSMENT) {
    			return true;
    		}
    	}
    	return false;
    }
    
    public Plan getPlan() {
        return plan;
    }


    public void setPlan(Plan plan) {
        this.plan = plan;
    }


    public int getControlObjectFlag() {
        return controlObjectFlag;
    }

    public void setControlObjectFlag(int controlObjectFlag) {
        this.controlObjectFlag = controlObjectFlag;
    }

	public ActualCourse getActualCourse() {
		return actualCourse;
	}

	public void setActualCourse(ActualCourse actualCourse) {
		this.actualCourse = actualCourse;
	}


}
