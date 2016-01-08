package com.augmentum.ot.action;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.util.HtmlUtils;

import com.augmentum.ot.dataObject.ActualCourseSearchCondition;
import com.augmentum.ot.dataObject.EmailSendCondition;
import com.augmentum.ot.dataObject.ErrorCode;
import com.augmentum.ot.dataObject.FromSearchToViewCondition;
import com.augmentum.ot.dataObject.Page;
import com.augmentum.ot.dataObject.PlanSearchCondition;
import com.augmentum.ot.dataObject.constant.DateFormatConstants;
import com.augmentum.ot.dataObject.constant.ErrorCodeConstants;
import com.augmentum.ot.dataObject.constant.FlagConstants;
import com.augmentum.ot.dataObject.constant.IndexFieldConstants;
import com.augmentum.ot.dataObject.constant.JsonKeyConstants;
import com.augmentum.ot.dataObject.constant.LogConstants;
import com.augmentum.ot.dataObject.constant.RoleNameConstants;
import com.augmentum.ot.email.EmailConstant;
import com.augmentum.ot.exception.DataWarningException;
import com.augmentum.ot.exception.ServerErrorException;
import com.augmentum.ot.model.ActualCourse;
import com.augmentum.ot.model.ActualCourseAttachment;
import com.augmentum.ot.model.CourseInfo;
import com.augmentum.ot.model.CourseType;
import com.augmentum.ot.model.Employee;
import com.augmentum.ot.model.LeaveNote;
import com.augmentum.ot.model.Plan;
import com.augmentum.ot.model.PlanAttachment;
import com.augmentum.ot.model.PlanEmployeeMap;
import com.augmentum.ot.model.PlanType;
import com.augmentum.ot.model.SessionInfo;
import com.augmentum.ot.service.ActualCourseService;
import com.augmentum.ot.service.CourseTypeService;
import com.augmentum.ot.service.EmailService;
import com.augmentum.ot.service.EmployeeService;
import com.augmentum.ot.service.LeaveNoteService;
import com.augmentum.ot.service.PlanService;
import com.augmentum.ot.service.PlanTypeService;
import com.augmentum.ot.util.BeanFactory;
import com.augmentum.ot.util.DateHandlerUtils;
import com.augmentum.ot.util.DateJsonValueProcessor;
import com.augmentum.ot.util.ReaderXmlUtils;
import com.augmentum.ot.util.SessionObjectUtils;
import com.augmentum.ot.util.StringHandlerUtils;
import com.augmentum.ot.util.ValidationUtil;

@Component("planAction")
@Scope("prototype")
public class PlanAction extends BaseAction {

    /**
	 * 
	 */
    private static final long serialVersionUID = -1197318917256254112L;
    private Logger logger = Logger.getLogger(PlanAction.class);
    private PlanService planService;
    private Plan plan;
    private Integer planId;
    private String invitedTrainees;
    private String optionalTrainees;
    private String specificTrainees;
    private String actualCourseIds;
    private String actualCoursesJson;
    private String attachmentsJson;
    private String delActualCourseIds;
    private JSONArray jsonArray;
    private String cancelPlanReason;
    private String leavePlanReason;
    private String leaveCourseReason;
    private PlanSearchCondition criteria;
    private ActualCourseSearchCondition actualCourseCriteria;
    private String operationFlag;
    private FromSearchToViewCondition fromSearchToViewCondition;
    private int nextFlag = 0;
    private int previousFlag = 0;
    private boolean hasCondition = false;
    private int viewType = 0;
    private Integer actualCourseId;
    private ActualCourse actualCourse;
    private Boolean forChangeLocaleFlag;
    
    @Resource(name="emailService")
    private EmailService emailService;
    /**
     * encapsulate email send condition, determine how to send email.
     */
    private EmailSendCondition esc;
    
    /**
     * 
     * @Title: createPlan  
     * @Description: create new plan, plan course and plan session
     * 				set attachment relationship with plan.
     *
     * @return
     */
    @SuppressWarnings("unchecked")
	public String createPlan() {
    	Employee employee = SessionObjectUtils.getEmployeeFromSession();
        logger.info(employee.getAugUserName() + " enters createPlan");
        HttpServletRequest request = ServletActionContext.getRequest();
        if (!validateCreatePlan()) {
			logger.warn(LogConstants.getValidationMsg(employee.getAugUserName()));
			return FlagConstants.VALIDATION_ERROR;
		}
        // creator
        plan.setPlanCreator(employee.getAugUserName());
        // the following code is to handle the special characters in plan name
        plan.setPlanName(HtmlUtils.htmlEscape(plan.getPlanName()));
        // plan type
        PlanTypeService planTypeService = BeanFactory.getPlanTypeService();
        PlanType planType = null;
        // actual course list
        List<ActualCourse> actualCourseList = null;
        // plan attachments
        List<PlanAttachment> planAttachmentList = JSONArray.toList(JSONArray.fromObject(attachmentsJson), PlanAttachment.class);
        
        try {
            planType = planTypeService.findPlanTypeById(plan.getPlanType().getPlanTypeId());
    		actualCourseList = makeActualCourseList(actualCoursesJson,request);
        } catch (ServerErrorException e) {
        	this.handleExceptionByServerErrorException(e, false);
        } catch (DataWarningException e) {
        	this.handleExceptionByDataWarningException(e, false);
        }
        
        /*
         * If the opeartionFlag is saveAs, set the id of plan, plan course, plan
         * course attachment, plan session and plan session attachment to null.
         */
        if (FlagConstants.SAVE_AS_OPERATION.equals(operationFlag)) {
            plan.setPlanId(null);
            if (actualCourseList != null) {
                // Set the planCourseId = null;
                for (ActualCourse actualCourse : actualCourseList) {
                	actualCourse.setActualCourseId(null);
                    List<ActualCourseAttachment> actualCourseAttachmentList = actualCourse.getAttachments();
                    if (actualCourseAttachmentList != null) {
                        for (ActualCourseAttachment actualCourseAttachment : actualCourseAttachmentList) {
                            // Set the planCourseAttachmentId = null.
                        	actualCourseAttachment.setActualCourseAttachmentId(null);
                        }
                    }
                }
            }
            if (planAttachmentList != null) {
                for (PlanAttachment planAttachment : planAttachmentList) {
                    // Set the planAttachmentId = null.
                    planAttachment.setPlanAttachmentId(null);
                }
            }
        }
        try {
        	HttpServletRequest httpRequest = ServletActionContext.getRequest();
			planService.createPlan(plan, planType, planAttachmentList,
					invitedTrainees, optionalTrainees, specificTrainees,
					actualCourseList, httpRequest);
        } catch (DataWarningException e) {
        	this.handleExceptionByDataWarningException(e, false);
        } catch (ServerErrorException e) {
        	this.handleExceptionByServerErrorException(e, false);
        }
        session.put(JsonKeyConstants.OPERATION_FLAG, FlagConstants.CREATE_OPERATION);
        return SUCCESS;
    }
    
    private boolean validateCreatePlan() {
    	if (plan == null) {
    		return false;
    	}
    	if (!(ValidationUtil.isNotNull(plan.getPlanName()))){
			return false;
		}
		if (plan.getPlanName().length() > 200) {
			return false;
		}
		if (!(ValidationUtil.isNotNull(plan.getPlanBrief()))) {
			return false;
		}
		if (plan.getPlanBrief().length() > 100000) {
			return false;
		}
		if (plan.getPlanBriefWithoutTag().length() > 10000){
		    return false;
		}
		String[] tags = plan.getPlanCategoryTag().split(";");
		if (tags.length > 5) {
			return false;
		}
		HashSet<String> hashSet = new HashSet<String>();
		if (tags.length >0) {
			for (String planTag : tags) {
				hashSet.add(planTag);
			}
		}
		if (tags.length != hashSet.size()) {
			return false;
		}
		if (plan.getPlanType() == null){
		    return false;
		}
		if (plan.getNeedAssessment() != FlagConstants.NEED_ASSESSMENT && plan.getNeedAssessment() != FlagConstants.UN_NEED_ASSESSMENT) {
			return false;
		}
		if (plan.getPlanType().getPlanTypeId() == FlagConstants.PUBLIC_PLAN_ID) {
			//public plan shouldn't have session
			if (null != actualCoursesJson && !("".equals(actualCoursesJson))) {
	            JSONArray jsons = JSONObject.fromObject(actualCoursesJson).getJSONArray(JsonKeyConstants.ACRUAL_COURSES);
	            for (int i = 0; i < jsons.size(); i++) {
	                JSONObject tempJson = JSONObject.fromObject(jsons.get(i));
	                if (!(tempJson.containsKey(JsonKeyConstants.COURSE_INFO)) 
	                		&& (tempJson.containsKey(JsonKeyConstants.SESSION_INFO) && null != JSONObject.toBean(tempJson.getJSONObject(JsonKeyConstants.SESSION_INFO)))){
	                	return false;
	                }
	            }
			}
		}
		if (plan.getPlanIsPublish() != null && plan.getPlanIsPublish() == FlagConstants.IS_PUBLISHED) {
			// cannot edit a published plan to no trainees
			if (plan.getPlanType().getPlanTypeId() == FlagConstants.INVITED_PLAN_ID){
				if (invitedTrainees.equals("")) {
					return false;
				}
			} else {
				if (plan.getIsAllEmployee() == FlagConstants.UN_ALL_EMPLOYEES && specificTrainees.equals("")) {
					return false;
				}
			}
		}
    	return true;
    }
	/**
	 * 
	 * @Title: makeActualCourseList  
	 * @Description: Get actual course list from json
	 *
	 * @param actualCoursesJson
	 * @return
	 * @throws ServerErrorException 
	 * @throws DataWarningException 
	 */
    private List<ActualCourse> makeActualCourseList(String actualCoursesJson,HttpServletRequest request) throws DataWarningException, ServerErrorException {
        List<ActualCourse> actualCourseList = new ArrayList<ActualCourse>();
        if (null != actualCoursesJson && !("".equals(actualCoursesJson))) {
            JSONObject jsonObject = JSONObject.fromObject(actualCoursesJson);
            JSONArray jsons = jsonObject.getJSONArray(JsonKeyConstants.ACRUAL_COURSES);
            for (int i = 0; i < jsons.size(); i++) {
                JSONObject tempJson = JSONObject.fromObject(jsons.get(i));
                //the following code is to handle the special characters, the "courseNameHasTag" field is created
                //in the front-end by the json object, which contains the course name has tags, so as the "courseTargetTrainee"
				tempJson.put(JsonKeyConstants.COURSE_NAME, HtmlUtils.htmlEscape(tempJson.getString(JsonKeyConstants.COURSE_NAME_HAS_TAG))); 
                ActualCourse actualCourse = null;
                List<ActualCourseAttachment> actualCourseAttachmentList = new ArrayList<ActualCourseAttachment>();
                JSONArray actualCourseAttachmentsJson = null;
                if (tempJson.containsKey("attachments")) {
                	actualCourseAttachmentsJson = tempJson.getJSONArray("attachments");
                }
                String courseTrainer = tempJson.getString("courseTrainer").trim();
                if(!courseTrainer.equals("")){
                    judgeActualCourseTrainer(tempJson.getString("courseTrainer"),request);
                }
                //set actualCourseAttachment 
                if (actualCourseAttachmentsJson != null
                        && actualCourseAttachmentsJson.size() > 0) {
                    for (int j = 0; j < actualCourseAttachmentsJson.size(); j++) {
                        JSONObject pcaJson = JSONObject
                                .fromObject(actualCourseAttachmentsJson.get(j));
                        Date createDate = DateHandlerUtils.formatDate(pcaJson.getString("createDateTime"), "yyyyMMdd" );
                        pcaJson.remove("createDateTime");
                        ActualCourseAttachment actualCourseAttachment = 
                        	(ActualCourseAttachment) JSONObject.toBean(pcaJson, ActualCourseAttachment.class);;
                        actualCourseAttachment.setCreateDateTime(createDate);
                        if (actualCourseAttachment.getActualCourseAttachmentId() <= 0) {
                        	actualCourseAttachment.setActualCourseAttachmentId(null);
                        };
                        actualCourseAttachmentList.add(actualCourseAttachment);
                    }
                }
                CourseInfo courseInfo = null;
                SessionInfo sessionInfo = null;
                // if current actual course contains courseInfo, then sessionInfo is null
                if (tempJson.containsKey(JsonKeyConstants.COURSE_INFO) && null != JSONObject.toBean(tempJson.getJSONObject(JsonKeyConstants.COURSE_INFO))) {
                	JSONObject courseInfoJson = tempJson.getJSONObject(JsonKeyConstants.COURSE_INFO);
                	courseInfoJson.put(JsonKeyConstants.COURSE_TARGET_TRAINEE, HtmlUtils.htmlEscape(courseInfoJson.getString("courseTargetTraineeHasTag")));
                	//as courseInfo, it has courseType, get courseType
                	CourseType courseType = null;
                	CourseTypeService courseTypeService = BeanFactory.getCourseTypeService();
                	JSONObject courseTypeJson = courseInfoJson.getJSONObject("courseType");
                	Integer courseTypeId = courseTypeJson.getInt("courseTypeId");
                	if (courseTypeId != null && courseTypeId > 0) {
                        courseType = courseTypeService.findCourseTypeById(courseTypeId);
                    }
                	courseInfoJson.remove("courseType");
                	courseInfo = (CourseInfo) JSONObject.toBean(courseInfoJson, CourseInfo.class);
                	courseInfo.setCourseType(courseType);
                	if (courseInfo.getCourseInfoId() !=null && courseInfo.getCourseInfoId() <= 0) {
                		courseInfo.setCourseInfoId(null);
                	}
                	tempJson.remove(JsonKeyConstants.COURSE_INFO);
                	
                } else if (tempJson.containsKey(JsonKeyConstants.SESSION_INFO) && null != JSONObject.toBean(tempJson.getJSONObject(JsonKeyConstants.SESSION_INFO))) {
                	JSONObject sessionInfoJson = tempJson.getJSONObject(JsonKeyConstants.SESSION_INFO);
                	sessionInfo = (SessionInfo) JSONObject.toBean(sessionInfoJson, SessionInfo.class);
                	if (sessionInfo.getSessionInfoId() != null && sessionInfo.getSessionInfoId() <= 0) {
                		sessionInfo.setSessionInfoId(null);
                	}
                	tempJson.remove(JsonKeyConstants.SESSION_INFO);
                }
                // calculate actualCourseStartTime and endTime.
                StringBuilder actualCourseStartTimeStr = new StringBuilder();
                StringBuilder actualCourseEndTimeStr = new StringBuilder();
                String actualCourseDateStr = tempJson.getString("actualCourseDate");
                String startTimeStr = tempJson.getString("courseStartTime");
                String endTimeStr = tempJson.getString("courseEndTime");
                if (null != actualCourseDateStr
                        && !("".equals(actualCourseDateStr))) {
                	actualCourseStartTimeStr.append(actualCourseDateStr);
                	actualCourseEndTimeStr.append(actualCourseDateStr);
                    if (null != startTimeStr && !("".equals(startTimeStr))) {
                    	actualCourseStartTimeStr.append(" ").append(startTimeStr).append(":00");
                    }
                    if (null != endTimeStr && !("".equals(endTimeStr))) {
                    	actualCourseEndTimeStr.append(" ").append(endTimeStr).append(":00");
                    }
                }
                Date actualCourseStartTime = null;
                Date actualCourseEndTime = null;
                actualCourseStartTime = DateHandlerUtils.formatDate(actualCourseStartTimeStr.toString(), DateFormatConstants.TO_DATABASE);
                actualCourseEndTime = DateHandlerUtils.formatDate(actualCourseEndTimeStr.toString(), DateFormatConstants.TO_DATABASE);
                
                tempJson.remove("attachments");
                tempJson.remove("actualCourseDate");
                tempJson.remove("courseStartTime");
                tempJson.remove("courseEndTime");

                actualCourse = (ActualCourse) JSONObject.toBean(tempJson,
                		ActualCourse.class);
                actualCourse.setAttachments(actualCourseAttachmentList);
                actualCourse.setCourseInfo(courseInfo);
                actualCourse.setSessionInfo(sessionInfo);
                actualCourse.setCourseStartTime(actualCourseStartTime);
                actualCourse.setCourseEndTime(actualCourseEndTime);
                if (actualCourse.getActualCourseId() != null && actualCourse.getActualCourseId() <= 0) {
                	actualCourse.setActualCourseId(null);
                }
                for (ActualCourseAttachment actualCourseAttachment : actualCourseAttachmentList) {
                    actualCourseAttachment.setActualCourse(actualCourse);
                }
                actualCourseList.add(actualCourse);
            }
        }
        return actualCourseList;
    }

    private void judgeActualCourseTrainer(String trainerName, HttpServletRequest request) throws ServerErrorException, DataWarningException{
        EmployeeService employeeService = BeanFactory.getEmployeeService();
        Employee employee = new Employee();
            try{
                employee = employeeService.findEmployeeByName(trainerName);
            } catch (Exception e) {
                logger.error(e); 
                throw new ServerErrorException(e);
            }
            if (employee == null) {
                throw new ServerErrorException(ErrorCodeConstants.SERVER_ERROR);
            }
    }

    @SuppressWarnings("unchecked")
	public String editPlan() {
    	Employee employee = SessionObjectUtils.getEmployeeFromSession();
        logger.info(LogConstants.getInfo(employee.getAugUserName(), "editPlan"));
        HttpServletRequest request = ServletActionContext.getRequest();
        // Validate the information of plan.
        if (!validateCreatePlan()) {
			logger.warn(LogConstants.getValidationMsg(employee.getAugUserName()));
			return FlagConstants.VALIDATION_ERROR;
		}
        List<ActualCourse> actualCourseList = null;
        List<PlanAttachment> planAttachmentList = JSONArray.toList(JSONArray.fromObject(attachmentsJson), PlanAttachment.class);
        List<ActualCourse> delActualCourseList = null;
        PlanType planType = null;
        Boolean editPlanFlag = false;
        PlanTypeService planTypeService = BeanFactory.getPlanTypeService();
        if (null != plan) {
            //handle the special characters in plan name
            plan.setPlanName(HtmlUtils.htmlEscape(plan.getPlanName()));
            planType = plan.getPlanType();
            try {
                planType = planTypeService.findPlanTypeById(planType
                        .getPlanTypeId());
            } catch (ServerErrorException e) {
            	this.handleExceptionByServerErrorException(e, false);
            }
        } else {
        	logger.error("plan is null from front-end!");
        	return FlagConstants.VALIDATION_ERROR;
        }
        try {
			actualCourseList = makeActualCourseList(actualCoursesJson,request);
		} catch (ServerErrorException e) {
        	this.handleExceptionByServerErrorException(e, false);
        } catch (DataWarningException e) {
        	this.handleExceptionByDataWarningException(e, false);
		}
        if (null != delActualCourseIds && !"".equals(delActualCourseIds)) {
            delActualCourseList = new ArrayList<ActualCourse>();
            List<String> delActualCourseId = StringHandlerUtils.strToArray(delActualCourseIds, FlagConstants.SPLIT_COMMA);
            for (String delId : delActualCourseId) {
                Integer id = Integer.parseInt(delId);
                ActualCourse actualCourse = new ActualCourse();
                actualCourse.setActualCourseId(id);
                delActualCourseList.add(actualCourse);
            }
        } else {
            delActualCourseList = new ArrayList<ActualCourse>();
        }
        Plan oldPlan = null;
        try {
            oldPlan = planService.getPlanById(plan.getPlanId());
			editPlanFlag = planService.updatePlan(plan, planType,
					invitedTrainees, optionalTrainees, specificTrainees,
					actualCourseList, planAttachmentList,
					delActualCourseList, ServletActionContext.getRequest());
        } catch (DataWarningException e) {
            return this.handleExceptionByDataWarningException(e, false);
        } catch (ServerErrorException e) {
            return this.handleExceptionByServerErrorException(e, false);
        }
        if (editPlanFlag) {
            try {
                plan = planService.getPlanById(plan.getPlanId());
                if(plan.getPlanIsPublish() == FlagConstants.IS_PUBLISHED){
                    emailService.sendEmailByUpdatePlan(request, oldPlan, plan, esc);
                }
            } catch (DataWarningException e) {
                this.handleExceptionByDataWarningException(e, false);
            } catch (ServerErrorException e) {
                this.handleExceptionByServerErrorException(e, false);
            }
            if (plan == null) {
                return ERROR;
            }
            getPlanDetail();
            return SUCCESS;
        } else {
            return ERROR;
        }
    }
    //need confirm whether the trainee has applied
    /**
     * 
     * @Title: courseListConfirm  
     * @Description: Get actual course list when confirm selected course.  
     *
     * @return
     */
    public String courseListConfirm() {
    	Employee employee = SessionObjectUtils.getEmployeeFromSession();
        logger.info(LogConstants.getInfo(employee.getAugUserName(), "courseListConfirm"));
        ActualCourseService actualCourseService = BeanFactory.getActualCourseService();
        String[] str = { "actualCourse", "plan", "employeeList" };
        JsonConfig jsonConfig = new JsonConfig();
        jsonConfig.setExcludes(str);
        jsonConfig.registerJsonValueProcessor(java.util.Date.class, new DateJsonValueProcessor("yyyy-MM-dd"));
        // PlanService planService = BeanFactory.getPlanService();
        try {
            if ("".equals(actualCourseIds) || null == actualCourseIds) {
                ErrorCode errorCode = ReaderXmlUtils.getErrorCodes().get(ErrorCodeConstants.SERVER_VALIDATION_ERROR);
                logger.warn("Server Data validation problem, please try again!");
                jsonObject = JSONObject.fromObject(errorCode);
                return FlagConstants.ERROR_SERVER_JSON;
            }
            List<ActualCourse> actualCourses = actualCourseService.getTemporaryActualCoursesByIds(actualCourseIds);
            if (null == actualCourses || actualCourses.isEmpty()) {
                ErrorCode errorCode = ReaderXmlUtils.getErrorCodes().get(ErrorCodeConstants.SERVER_ERROR);
                logger.warn("Server Data problem, please try again!");
                jsonObject = JSONObject.fromObject(errorCode);
                return FlagConstants.ERROR_SERVER_JSON;
            }
            jsonArray = JSONArray.fromObject(actualCourses, jsonConfig);
        } catch (ServerErrorException e) {
        	this.handleExceptionByServerErrorException(e, true);
        }
        return SUCCESS;
    }

    private boolean validatePlanSearchCondtion(PlanSearchCondition planSearchCondition) {
        boolean flag = true;
        if (null == planSearchCondition) {
            flag = false;
        }
        return flag;
    }
    
    /**
     * 
     * Search Plan
     * 
     * @throws ServerErrorException
     * @throws DataWarningException
     */
    public String searchPlan() {
    	Employee employee = SessionObjectUtils.getEmployeeFromSession();
        logger.info(LogConstants.getInfo(employee.getAugUserName(), "searchPlan"));

        if (!validatePlanSearchCondtion(criteria)) {
            ErrorCode errorCode = ReaderXmlUtils.getErrorCodes().get("E0001");
            jsonObject = JSONObject.fromObject(errorCode);
            return FlagConstants.VALIDATION_ERROR;
        }
        if (criteria.getQueryString() == null) {
        	criteria.setQueryString("");
        }
        if (criteria.getSearchFields() == null) {
        	criteria.setSearchFields(IndexFieldConstants.PLAN_SEARCH_FIELDS_ALL);
        } else {
        	String fields = criteria.getSearchFields()[0];
            criteria.setSearchFields(fields.split(","));
        }

        Page<Plan> planPage = null;
		try {
			planPage = planService.searchPlansFromIndex(criteria);
		} catch (ServerErrorException e) {
			this.handleExceptionByServerErrorException(e, true);
		}

        jsonObject = new JSONObject();
        jsonObject.put(JsonKeyConstants.COUNT, planPage.getTotalRecords());
        jsonObject.put(JsonKeyConstants.TOTAL_PAGE, planPage.getTotalPage());
        jsonObject.put(JsonKeyConstants.FIELDS_DATA, planToJsonArray(planPage.getList(), FlagConstants.SEARCH_PLAN));
        return SUCCESS;
    }
    
    /**
     * search my plan list
     * @return
     */
    public String searchMyPlan() {
    	Employee employee = SessionObjectUtils.getEmployeeFromSession();
    	logger.info(LogConstants.getInfo(employee.getAugUserName(), FlagConstants.SEARCH_MY_PLAN));
    	
    	if (!validatePlanSearchCondtion(criteria)) {
            ErrorCode errorCode = ReaderXmlUtils.getErrorCodes().get(ErrorCodeConstants.SERVER_VALIDATION_ERROR);
            jsonObject = JSONObject.fromObject(errorCode);
            return FlagConstants.VALIDATION_ERROR;
        }
        
        String fields = criteria.getSearchFields()[0];
        criteria.setSearchFields(fields.split(","));

        Page<Plan> planPage = null;
		try {
			planPage = planService.searchMyPlanFromIndex(employee, criteria);
			planService.queryPlanInfo(planPage, criteria, employee);
		} catch (DataWarningException e) {
			this.handleExceptionByDataWarningException(e, true);
		} catch (ServerErrorException e) {
			this.handleExceptionByServerErrorException(e, true);
		}

        jsonObject = new JSONObject();
        jsonObject.put(JsonKeyConstants.COUNT, planPage.getTotalRecords());
        jsonObject.put(JsonKeyConstants.TOTAL_PAGE, planPage.getTotalPage());
        jsonObject.put(JsonKeyConstants.FIELDS_DATA, planToJsonArray(planPage.getList(), FlagConstants.SEARCH_MY_PLAN));
        return SUCCESS;
    }
 
    /**
     * Prepare for info from view to search.
     * 
     * @return
     */
    public String preSearchPlan() {
    	Employee employee = SessionObjectUtils.getEmployeeFromSession();
        logger.info(LogConstants.getInfo(employee.getAugUserName(), "preSearchPlan"));
        
        if (fromSearchToViewCondition != null && criteria != null) {
            hasCondition = true;
        }
        return SUCCESS;
    }

    // get jasonArray of planList
    private JSONArray planToJsonArray(List<Plan> planList, String methodName) {
        JSONArray arrays = new JSONArray();
        if (planList != null) {
            for (int index = 0; index < planList.size(); index++) {
            	Plan plan = planList.get(index);
                JSONArray array = new JSONArray();
                if ("searchMyPlan".equals(methodName)) {
                	if (index == (planList.size()-1)) {
                		array.add("<div class='"+plan.getStatus()+"'>&nbsp;<script type='text/javascript'>intialStatusTooTip();</script></div>");
					} else {
						array.add("<div class='"+plan.getStatus()+"'>&nbsp;</div>");
					}
				}
                array.add(plan.getPlanId());
                if (plan.getPlanHasAttachment() != null && plan.getPlanHasAttachment() == FlagConstants.HAS_ATTACHMENT) {
                    array.add("<img class='downloadImag' planId=" + plan.getPlanId() + " src='"
                            + ServletActionContext.getRequest().getContextPath()
                            + "/searchList/images/ICN_Attachment_16x16.png'>");
                } else {
                    array.add("");
                }
                array.add("<a class='viewDetail' planId=" + plan.getPlanId() + ">"
                        + plan.getPrefixIDValue() + "</a>");
                array.add(plan.getPlanName());
                array.add(plan.getPlanBriefWithoutTag());
                array.add(getText(plan.getPlanType().getPlanTypeName()));
                array.add(plan.getPlanCategoryTag());
                String startTime = "";
                if (plan.getPlanExecuteStartTime() != null) {
                    startTime = DateHandlerUtils.dateToString(
                            DateFormatConstants.YY_MM_DD, plan
                                    .getPlanExecuteStartTime());
                }
                String endTime = "";
                if (plan.getPlanExecuteEndTime() != null) {
                    endTime = DateHandlerUtils.dateToString(
                            DateFormatConstants.YY_MM_DD, plan
                                    .getPlanExecuteEndTime());
                }
                array.add(startTime + "-" + endTime);
                array.add(plan.getPlanCreator());
                arrays.add(array);
            }
        }
        return arrays;
    }

    public String viewPlanDetail() {
    	Employee employee = SessionObjectUtils.getEmployeeFromSession();
        logger.info(LogConstants.getInfo(employee.getAugUserName(), "viewPlanDetail"));
        if (planId == null || planId <= 0) {
            ErrorCode errorCode = ReaderXmlUtils.getErrorCodes().get(ErrorCodeConstants.SERVER_VALIDATION_ERROR);
            jsonObject = JSONObject.fromObject(errorCode);
            return FlagConstants.VALIDATION_ERROR;
        }
        try {
            plan = planService.getPlanById(planId);
            if (plan != null) {
                List<ActualCourse> actualCourseList = plan.getActualCourses();
                //set trainee join or quit course flag
                setJoinFlagForTrainee(actualCourseList,employee);
                //set the trainee apply leave flag
                setLeaveFlagForTrainee(actualCourseList,employee);
            }
        } catch (DataWarningException e) {
            this.handleExceptionByDataWarningException(e, false);
        } catch (ServerErrorException e) {
            this.handleExceptionByServerErrorException(e, false);
        }
        if (null == plan) {
            return FlagConstants.VALIDATION_ERROR;
        }
        if (getHighestRoleOfEmployeeByPlan(plan, employee).equals(RoleNameConstants.NO_ACCESS)) {
        	return FlagConstants.NO_ACCESS;
        }
        getPlanDetail();
        return SUCCESS;
    }
    
    private String getHighestRoleOfEmployeeByPlan(Plan plan, Employee employee) {
    	List<String> roleNamelist = SessionObjectUtils.getRoleNamesFromSession();
        if (roleNamelist.contains(RoleNameConstants.ADMIN)) {
        	return RoleNameConstants.ADMIN;
        }
        if (roleNamelist.contains(RoleNameConstants.TRAINING_MASTER)) {
        	return RoleNameConstants.TRAINING_MASTER;
        }
        if (null != plan.getTrainers() && !("".equals(plan.getTrainers()))) {
        	String[] trainerNames = plan.getTrainers().split(FlagConstants.SPLIT_COMMA);
            if (Arrays.asList(trainerNames).contains(employee.getAugUserName())) {
            	return RoleNameConstants.TRAINER;
            }
        }
        List<Employee> traineeList = new ArrayList<Employee>();
        for (PlanEmployeeMap planEmployeeMap : plan.getPlanEmployeeMapList()) {
        	traineeList.add(planEmployeeMap.getEmployee());
        }
        if (traineeList.contains(employee)) {
        	return RoleNameConstants.TRAINEE;
        }
        // if a plan is to all employees, all people have access  to view it
        if (plan.getIsAllEmployee() == FlagConstants.IS_ALL_EMPLOYEES) {
        	return RoleNameConstants.HAVE_ACCESS;
        }
        return RoleNameConstants.NO_ACCESS;
    }
    
    /**to judge if employee has joined course, only when plan meet below requirements
     * 1. plan is public
     * 2. plan is published
     * 3. course is not started*/
    private void setJoinFlagForTrainee(List<ActualCourse> actualCourseList, Employee employee) {
    	if (plan.getPlanType().getPlanTypeName().equals(FlagConstants.PUBLIC_PLAN) 
    			&& plan.getPlanIsPublish().equals(FlagConstants.IS_PUBLISHED)) {
        	for (ActualCourse actualCourse : actualCourseList) {
        		if (actualCourse.getCourseInfo() != null) {
        			List<Employee> employees = actualCourse.getEmployeeList();
        			if (employees.contains(employee)) {
        				actualCourse.setIsJoinCourse(FlagConstants.JOIN);
        			} else {
        				actualCourse.setIsJoinCourse(FlagConstants.QUIT);
        			}
        		}
    		}
        }
    }
    
    /** Set the flag of trainee apply leave course 
     * 0: planCourse starts
     * 1: planCourse has not started, the trainee has not applied leave 
     * 2: planCourse has not started, the trainee has applied leave
     */
    private void setLeaveFlagForTrainee(List<ActualCourse> actualCourseList, Employee employee){
        if (plan.getPlanType().getPlanTypeName().equals(FlagConstants.INVITED_PLAN)) {
            for (ActualCourse actualCourse:actualCourseList) {
                 if(actualCourse.getCourseStartTime() == null || (actualCourse.getCourseStartTime() != null && actualCourse.getCourseStartTime().after(new Date()))){
                     List<LeaveNote> leaveNotes = actualCourse.getLeaveNoteList();
                     boolean isLeaved = false;
                     for(LeaveNote leaveNote: leaveNotes){
                         if(leaveNote.getEmployee().getEmployeeId().equals(employee.getEmployeeId())){
                            isLeaved = true;
                            break;
                         }
                     }
                     if(isLeaved){
                         actualCourse.setApplyLeaveFlag(FlagConstants.TRAINEE_LEAVED);
                     } else {
                         actualCourse.setApplyLeaveFlag(FlagConstants.TRAINEE_NOT_LEAVED);
                     }
                 } else if (actualCourse.getCourseStartTime() != null && actualCourse.getCourseStartTime().before(new Date())){
                     actualCourse.setApplyLeaveFlag(FlagConstants.COURSE_STARTS);
                 }
            }
        }
    }
    
    /**
     *  Search MyCourse
     * @return
     * @throws ServiceException
     */
    public String searchMyCourse() {
        Employee employee = SessionObjectUtils.getEmployeeFromSession();
        logger.info(LogConstants.getInfo(employee.getAugUserName(), "searchMyCourse"));
        
        if (!validateSearchCondition()) {
            ErrorCode errorCode = ReaderXmlUtils.getErrorCodes().get(ErrorCodeConstants.SERVER_ERROR);
            jsonObject = JSONObject.fromObject(errorCode);
            return FlagConstants.VALIDATION_ERROR_JSON;
        }
        ActualCourseService actualCourseService = BeanFactory.getActualCourseService();
        String fields = actualCourseCriteria.getSearchFields()[0];
        actualCourseCriteria.setSearchFields(fields.split(FlagConstants.SPLIT_COMMA));
        actualCourseCriteria.setEmployeeName(employee.getAugUserName());
        Page<ActualCourse> actualCoursePage = null;
        try {
        	actualCoursePage = actualCourseService.searchActualCourseFromIndex(actualCourseCriteria);
        	actualCourseService.queryActualCourseInfo(actualCoursePage, actualCourseCriteria);
        } catch (ServerErrorException e) {
            this.handleExceptionByServerErrorException(e, true);
        } catch (DataWarningException e) { 
            this.handleExceptionByDataWarningException(e, true);
        }
        
        jsonObject = new JSONObject();
        jsonObject.put(JsonKeyConstants.COUNT, actualCoursePage.getTotalRecords());
        jsonObject.put(JsonKeyConstants.TOTAL_PAGE, actualCoursePage.getTotalPage());
        jsonObject.put(JsonKeyConstants.FIELDS_DATA, actualCoursesToJsonArray(actualCoursePage.getList()));
        return SUCCESS;
    }
    
    /**
     * validate search condition
     * @return
     */
    private boolean validateSearchCondition() {
        if (null == actualCourseCriteria) {
            return false;
        }
        if (actualCourseCriteria.getSearchFields().length <= 0) {
            return false;
        }
        if (!ValidationUtil.isNotNull(actualCourseCriteria.getSearchFields()[0])) {
            return false;
        }
        return true;
    }
    
    // get jasonArray of planCourseList 
    private JSONArray actualCoursesToJsonArray(List<ActualCourse> actualCourseList) {
        logger.debug(LogConstants.message("Input actualCourse list size", actualCourseList.size()));
        JSONArray arrays = new JSONArray();
        SimpleDateFormat dateFormat= new SimpleDateFormat("yyyy/MM/dd");
        SimpleDateFormat timeFormat= new SimpleDateFormat("HH:mm");
        if (actualCourseList != null) {
            for (int index = 0; index < actualCourseList.size(); index++) {
                    JSONArray array = new JSONArray();
                    ActualCourse actualCourse = actualCourseList.get(index);
                    Date pcStartTime = actualCourse.getCourseStartTime();
                    String pcStartDateStr = "";
                    String pcStartTimeStr = "";
                    Date pcEndTime = actualCourse.getCourseEndTime();
                    String pcEndTimeStr = "";
                    if (index == (actualCourseList.size() - 1)) {
                        array.add("<div class='"+actualCourse.getStatus()+"'>&nbsp;<script type='text/javascript'>intialStatusTooTip();</script></div>");
                    } else {
                        array.add("<div class='"+actualCourse.getStatus()+"'>&nbsp;</div>");
                    }
                    array.add(index+1);
                    if (actualCourse.getCourseHasAttachment() != null && actualCourse.getCourseHasAttachment() == FlagConstants.HAS_ATTACHMENT) {
                        array.add("<img class='downloadImag' planCourseId="+actualCourse.getActualCourseId()+" src='" + ServletActionContext.getRequest().getContextPath() + "/searchList/images/ICN_Attachment_16x16.png'>");
                    } else {
                        array.add("");
                    }
                    array.add("<a class='viewDetail' planCourseId="+actualCourse.getActualCourseId()+">"+actualCourse.getPrefixIdValue()+"</a>");
                    array.add(actualCourse.getCourseName());
                    array.add(actualCourse.getCourseBriefWithoutTag());
                    if (pcStartTime != null) {
                    	pcStartDateStr = dateFormat.format(pcStartTime);
                    	pcStartTimeStr = timeFormat.format(pcStartTime);
                    }
                    if (pcEndTime != null) {
                    	pcEndTimeStr = timeFormat.format(pcEndTime);
                    }
                    array.add(pcStartDateStr);
                    array.add(pcStartTimeStr+"-"+pcEndTimeStr);
                    array.add(actualCourse.getCourseRoomNum());  
                    array.add(actualCourse.getPlan().getPrefixIDValue());
                    array.add(actualCourse.getPlan().getPlanCreator());
                    arrays.add(array);
             }
        }
        return arrays;
    }
    
    public String getActualCourseAttachmentsById() {
    	Employee employee = SessionObjectUtils.getEmployeeFromSession();
        logger.info(LogConstants.getInfo(employee.getAugUserName(), "getPlanCourseAttachmentsById"));

        ActualCourseService actualCourseService = BeanFactory.getActualCourseService();
        try {
            actualCourse = actualCourseService.getActualCourseById(actualCourseId);
        } catch (ServerErrorException e) {
            this.handleExceptionByServerErrorException(e, false);
        } catch (DataWarningException e) {
            this.handleExceptionByDataWarningException(e, false);
        }
        List<ActualCourseAttachment> acAttachments = actualCourse.getAttachments();
        List<ActualCourseAttachment> attachmentList = new ArrayList<ActualCourseAttachment>();
        if (null != acAttachments && !(acAttachments.isEmpty())){
        	if (getHighestRoleOfEmployeeByAc(actualCourse, employee).equals(RoleNameConstants.TRAINEE)) {
        		for (ActualCourseAttachment attachment : acAttachments) {
        			if (attachment.getActualCourseAttachmentVisible().equals(FlagConstants.VISIBLE)) {
        				attachmentList.add(attachment);
        			}
        		}
        	} else {
        		attachmentList.addAll(acAttachments);
        	}
        }
        jsonArray = new JSONArray();
        JsonConfig jsonConfig = new JsonConfig();
		String[] str = { "actualCourse" };
		jsonConfig.setExcludes(str);
        jsonConfig.registerJsonValueProcessor(java.util.Date.class,
                new DateJsonValueProcessor(DateFormatConstants.YYYY_MM_DD_));
        jsonArray = JSONArray.fromObject(attachmentList, jsonConfig);
        return SUCCESS;
    } 
    
    public String toEditPlan() {
    	Employee employee = SessionObjectUtils.getEmployeeFromSession();
        logger.info(LogConstants.getInfo(employee.getAugUserName(), "toEditPlan"));
        
        if (planId == null || planId <= 0) {
            ErrorCode errorCode = ReaderXmlUtils.getErrorCodes().get("E0001");
            jsonObject = JSONObject.fromObject(errorCode);
            return FlagConstants.VALIDATION_ERROR;
        }
        
    	try {
			plan = planService.getPlanById(planId);
			//handle the special characters, if you don't do that, the '<' or '>' will be decoded by rich editor
            //and the <b>a<b> will be a bold 'a'
			plan.setPlanBrief(HtmlUtils.htmlEscape(plan.getPlanBrief()));
		} catch (DataWarningException e) {
			this.handleExceptionByDataWarningException(e, false);
		} catch (ServerErrorException e) {
			this.handleExceptionByServerErrorException(e, false);
		}
		if (null == plan) {
            return ERROR;
        }
        getPlanDetail();
        return SUCCESS;
    }

    /**
     * Created by copy the viewPlanDetail method. Because need the treatment
     * process of the method.
     * 
     * @return
     */
    private String planDetail(Employee employee) {
        try {
            plan = planService.getPlanById(planId);
        } catch (DataWarningException e) {
        	this.handleExceptionByDataWarningException(e, false);
        } catch (ServerErrorException e) {
        	this.handleExceptionByServerErrorException(e, false);
        }
        if (null == plan) {
            return ERROR;
        }
        if (getHighestRoleOfEmployeeByPlan(plan, employee).equals(RoleNameConstants.NO_ACCESS)) {
        	return FlagConstants.NO_ACCESS;
        }
        List<ActualCourse> actualCourseList = plan.getActualCourses();
        //set trainee join or quit course flag
        setJoinFlagForTrainee(actualCourseList,employee);
        //set apply leave flag for trainee
        setLeaveFlagForTrainee(actualCourseList, employee);
        getPlanDetail();
        if (null != operationFlag
                && FlagConstants.TO_EDIT_PAGE.equals(operationFlag)) {
            return FlagConstants.TO_EDIT_PAGE;
        }
        return SUCCESS;
    }

    /**
     * Used to view need record detail(includes the next record, the previous
     * record and the current ) according to replace viewPlanDetail method.
     * 
     * @return
     */
    public String viewNeedRecordDetail() {
    	Employee employee = SessionObjectUtils.getEmployeeFromSession();
        logger.info(LogConstants.getInfo(employee.getAugUserName(), "viewNeedRecordDetail"));
        
        if (fromSearchToViewCondition == null) {
            return planDetail(employee);
        }
        /**
         * If viewType is 1, need view next record. If viewType is -1, need view
         * previous record. If viewType is 0(default), need view this record.
         */
        if (viewType == 1) {
            if(!forChangeLocaleFlag){
                String error = planService.plusRecordId(fromSearchToViewCondition,
                        criteria);
                if (error == "conditionError") {
                    // The condition is wrong, so use normal view method.
                    return planDetail(employee);
                } else if (error == "dataError") {
                    // The data is wrong, so view this record to replace viewing the
                    // next record.
                }
            }
        } else if (viewType == -1) {
            if(!forChangeLocaleFlag){
                String error = planService.subtractRecordId(
                        fromSearchToViewCondition, criteria);
                if (error == "conditionError") {
                    // The condition is wrong, so use normal view method.
                    return planDetail(employee);
                } else if (error == "dataError") {
                    // The data is wrong, so view this record to replace viewing the
                    // previous record.
                }
            }
        }

        planId = fromSearchToViewCondition.getNowId();
        nextFlag = planService.getNextRecordFlag(fromSearchToViewCondition);
        previousFlag = planService.getPreviousRecordFlag(fromSearchToViewCondition);
        return planDetail(employee);
    }

    /**
     * view plan detail by planId
     * 
     * @param plan
     */
    private void getPlanDetail() {
        // trainees
        Set<PlanEmployeeMap> pemList = plan.getPlanEmployeeMapList();
        if (plan.getPlanType().getPlanTypeName().equals(
                FlagConstants.INVITED_PLAN)) {
            invitedTrainees = planService.getTraineeList(pemList,
                    FlagConstants.ATTEND_TYPE_INVITED);
            optionalTrainees = planService.getTraineeList(pemList,
                    FlagConstants.ATTEND_TYPE_OPTIONAL);
        }
        if (plan.getPlanType().getPlanTypeName().equals(
                FlagConstants.PUBLIC_PLAN)) {
            specificTrainees = planService.getTraineeList(pemList,
                    FlagConstants.ATTEND_TYPE_SPECIFIC);
        }
        // course list
        List<ActualCourse> actualCourses = plan.getActualCourses();
        if (null == actualCourses || 0 == actualCourses.size()) {
        	actualCoursesJson = "";
        } else {
        	sortActualCourseList(actualCourses);
        }
        JsonConfig jsonConfig = new JsonConfig();
        String[] str = { "plan", "attachments", "employeeList","leaveNoteList" };
        jsonConfig.setExcludes(str);
        jsonConfig.registerJsonValueProcessor(Date.class,
                new DateJsonValueProcessor("yyyy-MM-dd HH:mm:ss"));
        JSONObject pcJsonObject;
        JSONArray pcJsonArray = new JSONArray();
        for (ActualCourse actualCourse : actualCourses) {
            pcJsonObject = JSONObject.fromObject(actualCourse, jsonConfig);
            if (FlagConstants.HAS_ATTACHMENT == actualCourse
                    .getCourseHasAttachment()) {
                List<ActualCourseAttachment> acAttachments = actualCourse
                        .getAttachments();
                JsonConfig pcaJsonConfig = new JsonConfig();
                String[] str2 = { "actualCourse" };
                pcaJsonConfig.setExcludes(str2);
                JSONArray pcAttachmentsJsonArray = JSONArray.fromObject(
                        acAttachments, pcaJsonConfig);
                pcJsonObject.put(JsonKeyConstants.ATTACHMENTS,
                        pcAttachmentsJsonArray);
            } else {
                pcJsonObject.put(JsonKeyConstants.ATTACHMENTS, "[]");
            }
            pcJsonArray.add(pcJsonObject);
        }
        actualCoursesJson = pcJsonArray.toString();
        // attachment
        List<PlanAttachment> planAttachments = plan.getPlanAttachments();
        String[] strPlan = { "plan" };
		jsonConfig.setExcludes(strPlan);
        jsonConfig.registerJsonValueProcessor(java.util.Date.class,
                new DateJsonValueProcessor(DateFormatConstants.YYYY_MM_DD_));
        attachmentsJson = JSONArray.fromObject(planAttachments, jsonConfig).toString();
    }
    
    private void sortActualCourseList (List<ActualCourse> actualCourseList) {
		Collections.sort(actualCourseList, new Comparator<ActualCourse>() {

			@Override
			public int compare(ActualCourse ac1, ActualCourse ac2) {
				if (ac1.getCourseOrder().compareTo(ac2.getCourseOrder()) < 0) {
					return -1;
				} else {
					return 1;
				}
			}
		});
	}

    public String deletePlan() {
    	Employee employee = SessionObjectUtils.getEmployeeFromSession();
        logger.info(LogConstants.getInfo(employee.getAugUserName(), "deletePlan"));
        try {
            planService.deletePlanById(planId);
        } catch (DataWarningException e) {
        	this.handleExceptionByDataWarningException(e, true);
        } catch (ServerErrorException e) {
        	this.handleExceptionByServerErrorException(e, true);
        }
        return SUCCESS;
    }

    public String publishPlan() {
    	Employee employee = SessionObjectUtils.getEmployeeFromSession();
        logger.info(LogConstants.getInfo(employee.getAugUserName(), "publishPlan"));
        try {
            planService.updatePlanForPublishById(planId);
            //TODO Margaret send email by publish plan
            plan = planService.getPlanById(planId);
            emailService.sendEmailByPublishPlan(ServletActionContext.getRequest(), plan, esc);
        } catch (DataWarningException e) {
        	this.handleExceptionByDataWarningException(e, true);
        } catch (ServerErrorException e) {
        	this.handleExceptionByServerErrorException(e, true);
        }
        return SUCCESS;
    }

    public String cancelPlan() {
    	Employee employee = SessionObjectUtils.getEmployeeFromSession();
        logger.info(LogConstants.getInfo(employee.getAugUserName(), "cancelPlan"));
        try {
            planService.updatePlanForCancel(planId, cancelPlanReason);
            //TODO Margaret send email by cancel plan
            plan = planService.getPlanById(planId);
            emailService.sendEmailByCancelPlan(ServletActionContext.getRequest(), plan, esc);
        } catch (DataWarningException e) {
        	this.handleExceptionByDataWarningException(e, true);
        } catch (ServerErrorException e) {
        	this.handleExceptionByServerErrorException(e, true);
        }
        return SUCCESS;
    }
    
    /**
     * Select plan course from a plan.
     * @return
     */
    public String findActualCoursesByPlanId() {
    	Employee employee = SessionObjectUtils.getEmployeeFromSession();
        logger.info(LogConstants.getInfo(employee.getAugUserName(), "findActualCoursesByPlanId"));
        
        if (planId == null || planId <= 0) {
            ErrorCode errorCode = ReaderXmlUtils.getErrorCodes().get("E0001");
            jsonObject = JSONObject.fromObject(errorCode);
            return FlagConstants.VALIDATION_ERROR;
        }
        ActualCourseService actualCourseService = BeanFactory
                .getActualCourseService();
        try {
            List<ActualCourse> actualCourseList = actualCourseService
                    .findActualCoursesByPlanId(planId);
            //remove plan sessions
            List<ActualCourse> planCourseList = new ArrayList<ActualCourse>();
            for (ActualCourse actualCourse : actualCourseList) {
            	if (actualCourse.getCourseInfo() != null) {
            		planCourseList.add(actualCourse);
            	}
            }
            sortActualCourseList(planCourseList);
            JsonConfig config = new JsonConfig();
            config.setExcludes(new String[] { "plan", "attachments",
                    "employeeList", "courseType", "leaveNoteList", "traineeNumber" });
           jsonArray = JSONArray.fromObject(planCourseList, config);
        } catch (DataWarningException e) {
        	this.handleExceptionByDataWarningException(e, false);
        } catch (ServerErrorException e) {
        	this.handleExceptionByServerErrorException(e, false);
        }
        return SUCCESS;
    }

    public String findActualCourseById() {
    	Employee employee = SessionObjectUtils.getEmployeeFromSession();
        logger.info(LogConstants.getInfo(employee.getAugUserName(), "findActualCourseById"));
        
        if (actualCourseId == null || actualCourseId <= 0) {
            ErrorCode errorCode = ReaderXmlUtils.getErrorCodes().get(ErrorCodeConstants.SERVER_VALIDATION_ERROR);
            jsonObject = JSONObject.fromObject(errorCode);
            return FlagConstants.VALIDATION_ERROR;
        }
        
        ActualCourseService actualCourseService = BeanFactory.getActualCourseService();
        try {
            actualCourse = actualCourseService.getActualCourseById(actualCourseId);
        } catch (DataWarningException e) {
            this.handleExceptionByDataWarningException(e, false);
        } catch (ServerErrorException e) {
            this.handleExceptionByServerErrorException(e, false);
        }
        if (null == actualCourse) {
            return FlagConstants.VALIDATION_ERROR;
        }
        if (getHighestRoleOfEmployeeByAc(actualCourse, employee).equals(RoleNameConstants.NO_ACCESS)) {
        	return FlagConstants.NO_ACCESS;
        }
        // get actual course attachments
        List<ActualCourseAttachment> acAttachments = actualCourse.getAttachments();
        List<ActualCourseAttachment> attachmentList = new ArrayList<ActualCourseAttachment>();
        if (null != acAttachments && !(acAttachments.isEmpty())){
        	if (getHighestRoleOfEmployeeByAc(actualCourse, employee).equals(RoleNameConstants.TRAINEE)) {
        		for (ActualCourseAttachment attachment : acAttachments) {
        			if (attachment.getActualCourseAttachmentVisible().equals(FlagConstants.VISIBLE)) {
        				attachmentList.add(attachment);
        			}
        		}
        	} else {
        		attachmentList.addAll(acAttachments);
        	}
        }
        JsonConfig jsonConfig = new JsonConfig();
		String[] str = { "actualCourse" };
		jsonConfig.setExcludes(str);
        jsonConfig.registerJsonValueProcessor(java.util.Date.class,
                new DateJsonValueProcessor(DateFormatConstants.YYYY_MM_DD_));
        attachmentsJson = JSONArray.fromObject(attachmentList, jsonConfig).toString();
        if (actualCourse.getCourseInfo() == null) {
        	// turn to viewplansession page
        	return FlagConstants.SESSION_SUCCESS;
        }
        // turn to viewplancourse page
        return SUCCESS;
    }
    
    private String getHighestRoleOfEmployeeByAc(ActualCourse actualCourse, Employee employee) {
    	List<String> roleNamelist = SessionObjectUtils.getRoleNamesFromSession();
        if (roleNamelist.contains(RoleNameConstants.ADMIN)) {
        	return RoleNameConstants.ADMIN;
        }
        if (roleNamelist.contains(RoleNameConstants.TRAINING_MASTER)) {
        	return RoleNameConstants.TRAINING_MASTER;
        }
        if (actualCourse.getCourseTrainer().equals(employee.getAugUserName())) {
        	return RoleNameConstants.TRAINER;
        }
        if (actualCourse.getEmployeeList().contains(employee)) {
        	return RoleNameConstants.TRAINEE;
        }
        // if a plan is to all employees, all people have access  to view it
        if (actualCourse.getPlan().getIsAllEmployee() == FlagConstants.IS_ALL_EMPLOYEES) {
        	return RoleNameConstants.HAVE_ACCESS;
        }
        return RoleNameConstants.NO_ACCESS;
    }

    public String getPlanAttachmentsById() {
    	Employee employee = SessionObjectUtils.getEmployeeFromSession();
        logger.info(LogConstants.getInfo(employee.getAugUserName(), "getPlanAttachmentsById"));
        
		try {
			plan = planService.getPlanById(planId);
		} catch (DataWarningException e) {
			logger.error(e);
		} catch (ServerErrorException e) {
			logger.error(e);
		}
		// attachment
		List<PlanAttachment> planAttachments = plan.getPlanAttachments();
		jsonArray = new JSONArray();
		JsonConfig jsonConfig = new JsonConfig();
		String[] str = { "plan" };
		jsonConfig.setExcludes(str);
        jsonConfig.registerJsonValueProcessor(java.util.Date.class,
                new DateJsonValueProcessor(DateFormatConstants.YYYY_MM_DD_));
		jsonArray = JSONArray.fromObject(planAttachments, jsonConfig);
		return SUCCESS;
	}

    public void clearOperation() {
        session.remove("operationFlag");
    }
    
    public String getPlanInfo() {
    	Employee employee = SessionObjectUtils.getEmployeeFromSession();
        logger.info(LogConstants.getInfo(employee.getAugUserName(), "getPlanInfo"));
        
    	try {
            plan = planService.getPlanById(planId);
        } catch (DataWarningException e) {
            logger.error(e);
        } catch (ServerErrorException e) {
            logger.error(e);
        }
        if (null == plan || plan.getPlanIsDeleted() == FlagConstants.IS_DELETED) {
        	jsonObject = new JSONObject();
        	jsonObject.put("", "");
        	return FlagConstants.VALIDATION_ERROR;
        }
    	return SUCCESS;
    }
    
    public String getActualCourseInfo() {
    	Employee employee = SessionObjectUtils.getEmployeeFromSession();
        logger.info(LogConstants.getInfo(employee.getAugUserName(), "getActualCourseInfo"));
        
    	if (actualCourseId == null || actualCourseId <= 0) {
            ErrorCode errorCode = ReaderXmlUtils.getErrorCodes().get(ErrorCodeConstants.SERVER_VALIDATION_ERROR);
            jsonObject = JSONObject.fromObject(errorCode);
            return FlagConstants.VALIDATION_ERROR;
        }
        
        ActualCourseService actualCourseService = BeanFactory
                .getActualCourseService();
        try {
            actualCourse = actualCourseService.getActualCourseById(actualCourseId);
            plan = actualCourse.getPlan();
        } catch (DataWarningException e) {
            this.handleExceptionByDataWarningException(e, false);
        } catch (ServerErrorException e) {
            this.handleExceptionByServerErrorException(e, false);
        }
    	return SUCCESS;
    }
    
    //trainee apply leave for plan 
    public String applyLeavePlan(){
        Employee employee = SessionObjectUtils.getEmployeeFromSession();
        logger.info(LogConstants.getInfo(employee.getAugUserName(), "applyLeavePlan"));
        if (planId == null || planId <= 0) {
            ErrorCode errorCode = ReaderXmlUtils.getErrorCodes().get("E0001");
            jsonObject = JSONObject.fromObject(errorCode);
            return FlagConstants.VALIDATION_ERROR;
        }
        EmployeeService employeeService = BeanFactory.getEmployeeService();
        LeaveNoteService leaveNoteService = BeanFactory.getLeaveNoteService();
        try {
            plan = planService.getPlanById(planId);
            employee = employeeService.findEmployeeByPrimaryKey(employee.getEmployeeId());
        } catch (DataWarningException dataWarningException) {
            return getErrorCodeForDataWarningException(dataWarningException);
        } catch (ServerErrorException serverErrorException) {
            return handleExceptionByServerErrorException(serverErrorException, true);
        }
        //all the planCourses in the plan 
        List<ActualCourse> actualCourseList = plan.getActualCourses();
        //the planCourses which already has applied for leave
        List<Integer> alreadyLeavedCourseIdList = new ArrayList<Integer>();
        //the planCourses which need applied for leave
        List<ActualCourse> needSendEmailCourseList = new ArrayList<ActualCourse>();
        List<LeaveNote> leaveNotes = new ArrayList<LeaveNote>();
        List<LeaveNote> alreadyLeaved = null;
        try {
            alreadyLeaved = leaveNoteService.getLeaveNoteByEmployeeIdAndPlanId(employee.getEmployeeId(), planId);
        } catch (ServerErrorException e1) {
            return handleExceptionByServerErrorException(e1, true);
        }
        for(LeaveNote leaveNote:alreadyLeaved){
           // alreadyLeavedCourseIdList.add(leaveNote.getPlanCourse().getPlanCourseId());
            alreadyLeavedCourseIdList.add(leaveNote.getActualCourse().getActualCourseId());
        }
        Date now = new Date();
        for(ActualCourse actualCourse:actualCourseList){
            if(alreadyLeavedCourseIdList.contains(actualCourse.getActualCourseId())){
                continue;
            }
            if(actualCourse.getCourseStartTime() == null || (actualCourse.getCourseStartTime() != null && actualCourse.getCourseStartTime().after(now))){
                needSendEmailCourseList.add(actualCourse);
                LeaveNote leaveNote = new LeaveNote(); 
                leaveNote.setApplyLeaveDate(now);
                leaveNote.setEmployee(employee);
                leaveNote.setPlanId(planId);
                leaveNote.setActualCourse(actualCourse);
                leaveNote.setReason(leavePlanReason);
                actualCourse.getLeaveNoteList().add(leaveNote);
                leaveNotes.add(leaveNote);
            }
        }
        try {
            leaveNoteService.saveLeave(leaveNotes);
            //TODO Margaret send Trainee leave plan email
            emailService.sendEmailByTraineeLeaveOrBackPlan(ServletActionContext.getRequest(),employee, plan,needSendEmailCourseList,EmailConstant.LEAVE_FLAG_LEAVE,leavePlanReason);
        } catch (DataWarningException dataWarningException) {
            return getErrorCodeForDataWarningException(dataWarningException);
        } catch (ServerErrorException serverErrorException) {
            return handleExceptionByServerErrorException(serverErrorException, true);
        }
        return SUCCESS;
    }
    
    public String applyBackPlan(){
        Employee employee = SessionObjectUtils.getEmployeeFromSession();
        logger.info(LogConstants.getInfo(employee.getAugUserName(), "applyLeavePlan"));
        if (planId == null || planId <= 0) {
            ErrorCode errorCode = ReaderXmlUtils.getErrorCodes().get("E0001");
            jsonObject = JSONObject.fromObject(errorCode);
            return FlagConstants.VALIDATION_ERROR;
        }
        LeaveNoteService leaveNoteService = BeanFactory.getLeaveNoteService();
        ActualCourseService actualCourseService = BeanFactory.getActualCourseService();
        try {
            plan = planService.getPlanById(planId);
        } catch (DataWarningException dataWarningException) {
            return getErrorCodeForDataWarningException(dataWarningException);
        } catch (ServerErrorException serverErrorException) {
            return handleExceptionByServerErrorException(serverErrorException, true);
        }
        List<LeaveNote> leaveNotes = null;
        List<LeaveNote> needBackLeaveNoteList = new ArrayList<LeaveNote>();
        List<ActualCourse> needCancelCourseList = new ArrayList<ActualCourse>();
        try {
            leaveNotes = leaveNoteService.getLeaveNoteByEmployeeIdAndPlanId(employee.getEmployeeId(), planId);
        } catch (ServerErrorException e) {
            return handleExceptionByServerErrorException(e, true);
        }
        Date now = new Date();
        for(LeaveNote leaveNote:leaveNotes){
            ActualCourse actualCourse = null;
            try {
                actualCourse = actualCourseService.getActualCourseById(leaveNote.getActualCourse().getActualCourseId());
            } catch (ServerErrorException e) {
                return handleExceptionByServerErrorException(e, true);
            } catch (DataWarningException e) {
                return getErrorCodeForDataWarningException(e);
            }
            if(actualCourse.getCourseStartTime() == null || actualCourse.getCourseStartTime().after(now)){
                needCancelCourseList.add(actualCourse);
                needBackLeaveNoteList.add(leaveNote);
            }
        }
        try {
            leaveNoteService.deleteLeaveNote(needBackLeaveNoteList);
            //TODO  Margaret send trainee  back plan email
            emailService.sendEmailByTraineeLeaveOrBackPlan(ServletActionContext.getRequest(),employee,plan,needCancelCourseList,EmailConstant.LEAVE_FLAG_BACK,EmailConstant.LEAVE_NULL_MESSAGE);
        } catch (ServerErrorException e) {
            return handleExceptionByServerErrorException(e, true);
        } catch (DataWarningException e) {
            return getErrorCodeForDataWarningException(e);
        }
        return SUCCESS;
    }
    
   /**
   * trainee apply leave for course 
   */
    public String applyLeaveCourse(){
        Employee employee = SessionObjectUtils.getEmployeeFromSession();
        logger.info(LogConstants.getInfo(employee.getAugUserName(), "applyLeavePlan"));
        if (actualCourseId == null || actualCourseId <= 0) {
            ErrorCode errorCode = ReaderXmlUtils.getErrorCodes().get("E0001");
            jsonObject = JSONObject.fromObject(errorCode);
            return FlagConstants.VALIDATION_ERROR;
        }
        ActualCourseService actualCourseService = BeanFactory.getActualCourseService();
        LeaveNoteService leaveNoteService = BeanFactory.getLeaveNoteService();
        EmployeeService employeeService = BeanFactory.getEmployeeService();
        try {
            actualCourse = actualCourseService.getActualCourseById(actualCourseId);
            employee = employeeService.findEmployeeByPrimaryKey(employee.getEmployeeId());
        } catch (DataWarningException dataWarningException) {
            return getErrorCodeForDataWarningException(dataWarningException);
        } catch (ServerErrorException serverErrorException) {
            return handleExceptionByServerErrorException(serverErrorException, true);
        }
        LeaveNote leave = new LeaveNote();
        leave.setEmployee(employee);
        leave.setActualCourse(actualCourse);
        leave.setPlanId(actualCourse.getPlan().getPlanId());
        leave.setReason(leaveCourseReason);
        leave.setApplyLeaveDate(new Date()); 
        try {
            leaveNoteService.saveLeave(leave); 
        } catch (ServerErrorException e) {
            return handleExceptionByServerErrorException(e, true);
        }
        actualCourse.getLeaveNoteList().add(leave);
        try { 
        	//TODO  Margaret send trainee leave course email
            emailService.sendEmailByTraineeLeaveOrBackCourse(ServletActionContext.getRequest(),employee, actualCourse, EmailConstant.LEAVE_FLAG_LEAVE, leaveCourseReason);
        } catch (ServerErrorException e) {
            return handleExceptionByServerErrorException(e, true);
        } catch (DataWarningException e) {
            return getErrorCodeForDataWarningException(e);
        }
        return SUCCESS;
    }
    
    /**
     * trainee cancel his apply leave for course
     * 
     * @param
     * @return
     */
    public String applyBackCourse(){
        Employee employee = SessionObjectUtils.getEmployeeFromSession();
        logger.info(LogConstants.getInfo(employee.getAugUserName(), "applyLeavePlan"));
        if (actualCourseId == null || actualCourseId <= 0) {
            ErrorCode errorCode = ReaderXmlUtils.getErrorCodes().get("E0001");
            jsonObject = JSONObject.fromObject(errorCode);
            return FlagConstants.VALIDATION_ERROR;
        }
        LeaveNoteService leaveNoteService = BeanFactory.getLeaveNoteService();
        ActualCourseService actualCourseService = BeanFactory.getActualCourseService();
        EmployeeService employeeService = BeanFactory.getEmployeeService();
        try {
            actualCourse = actualCourseService.getActualCourseById(actualCourseId);
            employee = employeeService.findEmployeeByPrimaryKey(employee.getEmployeeId());
        } catch (ServerErrorException e) {
            return handleExceptionByServerErrorException(e, true);
        } catch (DataWarningException e) {
            return getErrorCodeForDataWarningException(e);
        }
        List<LeaveNote> courseLeaveNotes = actualCourse.getLeaveNoteList();
        LeaveNote traineeLeaveNote = null; 
        for(LeaveNote leaveNote:courseLeaveNotes){
            if(leaveNote.getEmployee().getEmployeeId().equals(employee.getEmployeeId())){
                traineeLeaveNote = leaveNote;
                actualCourse.getLeaveNoteList().remove(leaveNote);
          //      employee.getLeaveList().remove(leaveNote);
                leaveNote.setEmployee(null);
                leaveNote.setActualCourse(null);
                break;
            }
        }
        try {
            leaveNoteService.deleteLeaveNote(traineeLeaveNote);
            //TODO  Margaret send trainee back course email
            emailService.sendEmailByTraineeLeaveOrBackCourse(ServletActionContext.getRequest(),employee, actualCourse, EmailConstant.LEAVE_FLAG_BACK,EmailConstant.LEAVE_NULL_MESSAGE);
        } catch (ServerErrorException e) {
            return handleExceptionByServerErrorException(e, true);
        } catch (DataWarningException e) {
            return getErrorCodeForDataWarningException(e);
        }
        return SUCCESS;
    }
    
    private String getErrorCodeForDataWarningException(DataWarningException dataWarningException) {
        String errorCodeString = dataWarningException.getMessage();
        if (errorCodeString == null || ("").equals(errorCodeString) ) {
            ErrorCode errorCodeEntity = ReaderXmlUtils.getErrorCodes().get("E0001");
            errorCodeEntity.setFlag("1");
            jsonObject = JSONObject.fromObject(errorCodeEntity);
        } else {
            ErrorCode errorCodeEntity = ReaderXmlUtils.getErrorCodes().get(errorCodeString);
            errorCodeEntity.setErrorMessage(getText(errorCodeEntity.getErrorMessageKey()));
            jsonObject = JSONObject.fromObject(errorCodeEntity);
        }
        return FlagConstants.DATA_WARNING;
    }
    
    @Resource
    public void setPlanService(PlanService planService) {
        this.planService = planService;
    }

    public Plan getPlan() {
        return plan;
    }

    public void setPlan(Plan plan) {
        this.plan = plan;
    }

    public String getActualCourseIds() {
        return actualCourseIds;
    }

    public void setActualCourseIds(String actualCourseIds) {
        this.actualCourseIds = actualCourseIds;
    }

    public JSONArray getJsonArray() {
        return jsonArray;
    }

    public void setJsonArray(JSONArray jsonArray) {
        this.jsonArray = jsonArray;
    }

    public PlanSearchCondition getCriteria() {
        return criteria;
    }

    public void setCriteria(PlanSearchCondition criteria) {
        this.criteria = criteria;
    }

    public String getOperationFlag() {
        return operationFlag;
    }

    public void setOperationFlag(String operationFlag) {
        this.operationFlag = operationFlag;
    }

    public String getInvitedTrainees() {
        return invitedTrainees;
    }

    public void setInvitedTrainees(String invitedTrainees) {
        this.invitedTrainees = invitedTrainees;
    }

    public String getOptionalTrainees() {
        return optionalTrainees;
    }

    public void setOptionalTrainees(String optionalTrainees) {
        this.optionalTrainees = optionalTrainees;
    }

    public String getSpecificTrainees() {
        return specificTrainees;
    }

    public void setSpecificTrainees(String specificTrainees) {
        this.specificTrainees = specificTrainees;
    }

    public String getActualCoursesJson() {
        return actualCoursesJson;
    }

    public void setActualCoursesJson(String actualCoursesJson) {
        this.actualCoursesJson = actualCoursesJson;
    }

    public void setPlanId(Integer planId) {
        this.planId = planId;
    }

    public Integer getPlanId() {
        return planId;
    }

    public String getAttachmentsJson() {
        return attachmentsJson;
    }

    public void setAttachmentsJson(String attachmentsJson) {
        this.attachmentsJson = attachmentsJson;
    }

    public String getCancelPlanReason() {
        return cancelPlanReason;
    }

    public void setCancelPlanReason(String cancelPlanReason) {
        this.cancelPlanReason = cancelPlanReason;
    }

    public FromSearchToViewCondition getFromSearchToViewCondition() {
        return fromSearchToViewCondition;
    }

    public void setFromSearchToViewCondition(
            FromSearchToViewCondition fromSearchToViewCondition) {
        this.fromSearchToViewCondition = fromSearchToViewCondition;
    }

    public int getNextFlag() {
        return nextFlag;
    }

    public void setNextFlag(int nextFlag) {
        this.nextFlag = nextFlag;
    }

    public int getPreviousFlag() {
        return previousFlag;
    }

    public void setPreviousFlag(int previousFlag) {
        this.previousFlag = previousFlag;
    }

    public boolean isHasCondition() {
        return hasCondition;
    }

    public void setHasCondition(boolean hasCondition) {
        this.hasCondition = hasCondition;
    }

    public int getViewType() {
        return viewType;
    }

    public void setViewType(int viewType) {
        this.viewType = viewType;
    }

	public ActualCourseSearchCondition getActualCourseCriteria() {
		return actualCourseCriteria;
	}

	public void setActualCourseCriteria(
			ActualCourseSearchCondition actualCourseCriteria) {
		this.actualCourseCriteria = actualCourseCriteria;
	}

	public EmailSendCondition getEsc() {
		return esc;
	}

	public void setEsc(EmailSendCondition esc) {
		this.esc = esc;
	}

    public String getLeavePlanReason() {
        return leavePlanReason;
    }

    public void setLeavePlanReason(String leavePlanReason) {
        this.leavePlanReason = leavePlanReason;
    }

    public String getLeaveCourseReason() {
        return leaveCourseReason;
    }

    public void setLeaveCourseReason(String leaveCourseReason) {
        this.leaveCourseReason = leaveCourseReason;
    }

	public String getDelActualCourseIds() {
		return delActualCourseIds;
	}

	public void setDelActualCourseIds(String delActualCourseIds) {
		this.delActualCourseIds = delActualCourseIds;
	}

	public Integer getActualCourseId() {
		return actualCourseId;
	}

	public void setActualCourseId(Integer actualCourseId) {
		this.actualCourseId = actualCourseId;
	}

	public ActualCourse getActualCourse() {
		return actualCourse;
	}

	public void setActualCourse(ActualCourse actualCourse) {
		this.actualCourse = actualCourse;
	}

	public PlanService getPlanService() {
		return planService;
	}

    public Boolean getForChangeLocaleFlag() {
        return forChangeLocaleFlag;
    }

    public void setForChangeLocaleFlag(Boolean forChangeLocaleFlag) {
        this.forChangeLocaleFlag = forChangeLocaleFlag;
    }
}
