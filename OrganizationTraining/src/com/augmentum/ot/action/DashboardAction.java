package com.augmentum.ot.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

import org.apache.log4j.Logger;
import org.apache.struts2.json.annotations.JSON;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.augmentum.ot.dataObject.ActualCourseSearchCondition;
import com.augmentum.ot.dataObject.CourseItem;
import com.augmentum.ot.dataObject.Page;
import com.augmentum.ot.dataObject.PlanSearchCondition;
import com.augmentum.ot.dataObject.ToDoItem;
import com.augmentum.ot.dataObject.constant.DateFormatConstants;
import com.augmentum.ot.dataObject.constant.IndexFieldConstants;
import com.augmentum.ot.dataObject.constant.JsonKeyConstants;
import com.augmentum.ot.dataObject.constant.LogConstants;
import com.augmentum.ot.dataObject.constant.RoleNameConstants;
import com.augmentum.ot.exception.DataWarningException;
import com.augmentum.ot.exception.ServerErrorException;
import com.augmentum.ot.model.ActualCourse;
import com.augmentum.ot.model.Employee;
import com.augmentum.ot.model.Plan;
import com.augmentum.ot.service.ActualCourseService;
import com.augmentum.ot.service.PlanService;
import com.augmentum.ot.service.ToDoListService;
import com.augmentum.ot.util.DateHandlerUtils;
import com.augmentum.ot.util.DateJsonValueProcessor;
import com.augmentum.ot.util.DefaultConditionUtils;
import com.augmentum.ot.util.ObjectToJSONUtils;
import com.augmentum.ot.util.SessionObjectUtils;

@Component("dashboardAction")
@Scope("prototype")
public class DashboardAction extends BaseAction {

    private static final long serialVersionUID = 3472020343475446828L;
    private Logger logger = Logger.getLogger(DashboardAction.class);
    @Resource
    private PlanService planService;
    @Resource
    private ActualCourseService actualCourseService;
    @Resource
    private ToDoListService toDoListService;
    private int pageSize = 4;
    private int pageNow = 1;
    private int planId;
    private int courseId;
    private int isJoin;
    private int toDoListPageSize;
    private int toDoListPageNum;
    private String toDoListRole;
    private String role;
    
    public String showNewPublicPlan(){
    	Employee employee = SessionObjectUtils.getEmployeeFromSession();
    	logger.info(LogConstants.getInfo(employee.getAugUserName(), "showNewPublicPlan"));
        try {
			Page<Plan> plans = planService.getNewPublishPlan(pageNow, pageSize, employee);
			jsonObject = new JSONObject();
			jsonObject.put(JsonKeyConstants.TOTAL_RECORD, plans.getTotalRecords());
			jsonObject.put(JsonKeyConstants.TOTAL_PAGE, plans.getTotalPage());
			jsonObject.put(JsonKeyConstants.PAGE_NOW, plans.getNowPager());
			jsonObject.put(JsonKeyConstants.PAGE_SIZE, plans.getPageSize());
			JSONArray planList=new JSONArray();
			for (Plan plan : plans.getList()) {
				JSONObject json = new JSONObject();
				json.put(JsonKeyConstants.PLAN_ID, plan.getPlanId());
				json.put(JsonKeyConstants.TITLE, plan.getPlanName());
				json.put(JsonKeyConstants.CREATOR, plan.getPlanCreator());
				json.put(JsonKeyConstants.START_TIME, DateHandlerUtils.dateToString(DateFormatConstants.MM_DD_,
					plan.getPlanExecuteStartTime()));
				planList.add(json);
			}
			jsonObject.put(JsonKeyConstants.DATA_LIST, planList);
        } catch (ServerErrorException e) {
			this.handleExceptionByServerErrorException(e, true);
		}
        return SUCCESS;
    }
    
    public String joinOrQuitPlan(){
    	Employee employee = SessionObjectUtils.getEmployeeFromSession();
    	logger.info(LogConstants.getInfo(employee.getAugUserName(), "joinOrQuitPlan"));
        try {
			planService.saveEmployeeJoinOrQuitPlan(employee, planId, courseId, isJoin);
        } catch(DataWarningException e) {
        	this.handleExceptionByDataWarningException(e, true);
		} catch (ServerErrorException e) {
			this.handleExceptionByServerErrorException(e, true);
		}
		return SUCCESS;
    }
    
    public String getPlanAndCourseList() {
        // Initialize a default search condition.
        Employee employee = SessionObjectUtils.getEmployeeFromSession();
        logger.info(LogConstants.getInfo(employee.getAugUserName(), "getPlanAndCourseList"));
        if (employee != null) {
        	String actualCourseJson = "";
        	if (!(RoleNameConstants.TRAINING_MASTER.equals(role))) {
        		// Get default search condition.
                ActualCourseSearchCondition searchCondtion = DefaultConditionUtils.getDefaultPlanCourseSearchCondition(role, employee.getAugUserName());
                searchCondtion.setSortField(IndexFieldConstants.ACTUAL_COURSE_END_TIME);
                // Get the plan course search result.
                Page<ActualCourse> actualCoursePage = null;
				try {
					actualCoursePage = actualCourseService.searchActualCourseFromIndex(searchCondtion);
					actualCourseService.queryActualCourseInfo(actualCoursePage, searchCondtion);
				} catch (ServerErrorException e) {
					this.handleExceptionByServerErrorException(e, true);
				} catch (DataWarningException e) {
					this.handleExceptionByDataWarningException(e, true);
				}
                List<ActualCourse> actualCourseList = actualCoursePage.getList();
                actualCourseJson = ObjectToJSONUtils.getActualCourseJSONForDashBoard(actualCourseList, role);
        	}
            
            // Get the plan search result;
            String planListJson;
            PlanSearchCondition psc = DefaultConditionUtils.getDefaultPlanSearchCondition();
            psc.setRoleFlag(role);
            Page<Plan> planPage = null;
			try {
				planPage = planService.searchMyPlanFromIndex(employee, psc);
				planService.queryPlanInfo(planPage, psc, employee);
			} catch (DataWarningException e) {
				this.handleExceptionByDataWarningException(e, true);
			} catch (ServerErrorException e) {
				this.handleExceptionByServerErrorException(e, true);
			}
            List<Plan> planList = planPage.getList();
            if (planList == null || planList.size() <= 0) {
            	planListJson = "[]";
            } else {
	            String[] str = {  "actualCourses", "planEmployeeMapList", "planAttachments",  "courseTypeIds"};
	            JsonConfig jsonConfig = new JsonConfig();
	            jsonConfig.setExcludes(str);
	            jsonConfig.registerJsonValueProcessor(java.util.Date.class, new DateJsonValueProcessor(DateFormatConstants.YYYY_MM_DD_));
	            planListJson = JSONArray.fromObject(planList, jsonConfig).toString();
            }
            
            Map<String, Object> courseListMap = new HashMap<String, Object>();
            courseListMap.put(JsonKeyConstants.ACTUAL_COURSE_LIST, actualCourseJson);
            courseListMap.put(JsonKeyConstants.PLAN_LIST, planListJson);
            
            jsonObject = ObjectToJSONUtils.transformMapToJson(courseListMap);
        }
        return SUCCESS;
    }
    
    public String getToDoListForRole() {
    	Employee employee = SessionObjectUtils.getEmployeeFromSession();
    	logger.info(LogConstants.getInfo(employee.getAugUserName(), "getToDoListForRole"));
    	Page<ToDoItem> toDoPage = null;
    	try {
    		if (toDoListRole.equals(RoleNameConstants.TRAINING_MASTER)) {
				toDoPage = toDoListService.findToDoItemsForMaster(employee, toDoListPageNum, toDoListPageSize);
    		}
    		if (toDoListRole.equals(RoleNameConstants.TRAINER)) {
    			toDoPage = toDoListService.findToDoItemsForTrainer(employee, toDoListPageNum, toDoListPageSize);
    		}
    		if (toDoListRole.equals(RoleNameConstants.TRAINEE)) {
    			toDoPage = toDoListService.findToDoItemsForTrainee(employee, toDoListPageNum, toDoListPageSize);
    		}
		} catch (DataWarningException e) {
			this.handleExceptionByDataWarningException(e, true);
		} catch (ServerErrorException e) {
			this.handleExceptionByServerErrorException(e, true);
		}
		for (ToDoItem toDoItem : toDoPage.getList()) {
			toDoItem.setItemContent(getText(toDoItem.getItemType()));
		}
		jsonObject = JSONObject.fromObject(toDoPage);
    	return SUCCESS;
    }
    
    public String getCourseListForPublishPlan() {
    	Employee employee = SessionObjectUtils.getEmployeeFromSession();
    	logger.info(LogConstants.getInfo(employee.getAugUserName(), "getCourseListForPublishPlan"));
    	try {
			List<CourseItem> courseItems = planService.getCourseListForPublishPlan(planId, employee);
			jsonObject = new JSONObject();
			jsonObject.put(JsonKeyConstants.DATA_LIST, JSONArray.fromObject(courseItems));
		} catch (ServerErrorException e) {
			this.handleExceptionByServerErrorException(e, true);
		} catch (DataWarningException e) {
			this.handleExceptionByDataWarningException(e, true);
		}
    	return SUCCESS;
    }
    
    @JSON(serialize = false)
    public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	
	@JSON(serialize = false)
	public int getPageNow() {
		return pageNow;
	}
	public void setPageNow(int pageNow) {
		this.pageNow = pageNow;
	}
	
	public int getPlanId() {
        return planId;
    }
    public void setPlanId(int planId) {
        this.planId = planId;
    }
    
    public int getCourseId() {
        return courseId;
    }
    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }
    
    public int getIsJoin() {
        return isJoin;
    }
    public void setIsJoin(int isJoin) {
        this.isJoin = isJoin;
    }
    
    @JSON(serialize = false)
    public ActualCourseService getActualCourseService() {
        return actualCourseService;
    }

    public void setActualCourseService(ActualCourseService actualCourseService) {
        this.actualCourseService = actualCourseService;
    }

    @JSON(serialize = false)
    public PlanService getPlanService() {
        return planService;
    }

    public void setPlanService(PlanService planService) {
        this.planService = planService;
    }

    @JSON(serialize = false)
    public String getRole() {
        return role;
    }
    
    public void setRole(String role) {
        this.role = role;
    }
    
    @JSON(serialize = false)
    public ToDoListService getToDoListService() {
		return toDoListService;
	}
    
	public void setToDoListService(ToDoListService toDoListService) {
		this.toDoListService = toDoListService;
	}
	
	public int getToDoListPageSize() {
		return toDoListPageSize;
	}
	
	public void setToDoListPageSize(int toDoListPageSize) {
		this.toDoListPageSize = toDoListPageSize;
	}
	
	public int getToDoListPageNum() {
		return toDoListPageNum;
	}
	
	public void setToDoListPageNum(int toDoListPageNum) {
		this.toDoListPageNum = toDoListPageNum;
	}
	
	public String getToDoListRole() {
		return toDoListRole;
	}

	public void setToDoListRole(String toDoListRole) {
		this.toDoListRole = toDoListRole;
	}

}
