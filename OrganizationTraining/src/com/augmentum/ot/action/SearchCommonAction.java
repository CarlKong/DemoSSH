package com.augmentum.ot.action;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.augmentum.ot.dataObject.CourseSearchCondition;
import com.augmentum.ot.dataObject.constant.FlagConstants;
import com.augmentum.ot.dataObject.constant.JsonKeyConstants;
import com.augmentum.ot.exception.ServerErrorException;
import com.augmentum.ot.model.CourseTag;
import com.augmentum.ot.model.CourseType;
import com.augmentum.ot.model.CustomColumns;
import com.augmentum.ot.model.Employee;
import com.augmentum.ot.model.PageSize;
import com.augmentum.ot.model.PlanTag;
import com.augmentum.ot.model.PlanType;
import com.augmentum.ot.service.CourseTagService;
import com.augmentum.ot.service.CourseTypeService;
import com.augmentum.ot.service.EmployeeService;
import com.augmentum.ot.service.PlanTagService;
import com.augmentum.ot.service.PlanTypeService;
import com.augmentum.ot.util.BeanFactory;
import com.augmentum.ot.util.SessionObjectUtils;

@Component("searchCommonAction")
@Scope("prototype")
public class SearchCommonAction extends BaseAction {

	private static final long serialVersionUID = -5231153593527077662L;
	private Logger logger = Logger.getLogger(SearchCommonAction.class);
	private EmployeeService employeeService;
	private String customization;
	private int searchFlag; // different search flag
	private CourseSearchCondition criteria;
	
	
	/**
	 * 
	 *  Prepare data for dataList
	 * 
	 * @throws ServiceException 
	 */
	public String findDataListInfo() throws ServerErrorException {
		String fieldsJson = findShowFieldsByEmployee();
		String pageSizesJson = findPageSizes();
		int pageSizeInt = findPageSizeByEmployee();
		
		jsonObject = new JSONObject();
		jsonObject.put(JsonKeyConstants.FIELDS_JSON, fieldsJson);
		jsonObject.put(JsonKeyConstants.PAGE_SIZES_JSON, pageSizesJson);
		jsonObject.put(JsonKeyConstants.PAGE_SIZE_INT, pageSizeInt);
		return SUCCESS;
	}
	
	// set showFields 
	private String findShowFieldsByEmployee() throws ServerErrorException {
		Employee employee = SessionObjectUtils.getEmployeeFromSession();
		List<CustomColumns> customColumnList = new ArrayList<CustomColumns>();
		try {
			customColumnList = employeeService.findCustomColumnsByEmployeeId(employee.getEmployeeId(), searchFlag);
		} catch (Exception e) {
			logger.error(e);
		}
		StringBuilder jsonStrBuilder = new StringBuilder();
		for (CustomColumns column:customColumnList) {
			if (0 == jsonStrBuilder.length()) {
				jsonStrBuilder.append(column.getFieldName());
			} else {
				jsonStrBuilder.append(FlagConstants.SPLIT_COMMA);
				jsonStrBuilder.append(column.getFieldName());
			}
		}
		String str = jsonStrBuilder.toString();
		return str;
	}

	// set pageSizes
	private String findPageSizes() throws ServerErrorException {
		List<PageSize> pageSizeList = new ArrayList<PageSize>();
		try {
			pageSizeList = employeeService.findDefaultPageSizesByCategory(searchFlag);
		} catch (Exception e) {
			logger.error(e);
		}
		List<Integer> valueList = new ArrayList<Integer>();
		for (PageSize pageSize:pageSizeList) {
			valueList.add(pageSize.getPageSizeValue());
		}
		JSONArray json = JSONArray.fromObject(valueList);
		String jsonStr = json.toString();
		return jsonStr;
	}
	
	// set pageSize
	private int findPageSizeByEmployee() throws ServerErrorException {
		Employee employee = SessionObjectUtils.getEmployeeFromSession();
		int pageSize = 0;
		try {
			pageSize = employeeService.findPageSizeByEmployeeId(employee.getEmployeeId(), searchFlag);
		} catch (Exception e) {
			logger.error(e);
		}
		return pageSize;
	}
	
	// update show fields
	public void updateShowFields() throws ServerErrorException {
		Employee employee = SessionObjectUtils.getEmployeeFromSession();
		try {
			employeeService.updateCustomColumnsOfEmployee(employee.getEmployeeId(), customization, searchFlag);
		} catch (Exception e) {
			logger.error(e);
		}
	}

	// update show size
	public void updateShowSize() throws ServerErrorException {
		Employee employee = SessionObjectUtils.getEmployeeFromSession();
		try {
			employeeService.updatePageSizeByEmployee(employee.getEmployeeId(), searchFlag, criteria.getPageSize());
		} catch (Exception e) {
			logger.error(e);
		}
	}
	
	// set search filterBox Course Type
	public String findCourseTypes() throws ServerErrorException {
		CourseTypeService courseTypeService = BeanFactory.getCourseTypeService();
		List<CourseType> listCourseType = new ArrayList<CourseType>();
		try {
			listCourseType = courseTypeService.findAllCourseType();
		} catch (Exception e) {
			logger.error(e);
		}
		for (CourseType type : listCourseType) {
			type.setTypeName(getText(type.getTypeName()));
		}
		JsonConfig config = new JsonConfig();
		config.setExcludes(new String[]{ "courses" });
		jsonObject = new JSONObject();
		jsonObject.put(JsonKeyConstants.COURSE_TYPES, JSONArray.fromObject(listCourseType, config));
		return SUCCESS;
	}
	
	public String findCourseTags() throws ServerErrorException{
	    CourseTagService courseTagService = BeanFactory.getCourseTagService();
	    List<CourseTag> listCourseTags = new ArrayList<CourseTag>();
	    try {
	        listCourseTags = courseTagService.findAllCourseTags();
        } catch (Exception e) {
            logger.error(e);
        }
        jsonObject = new JSONObject();
        jsonObject.put(JsonKeyConstants.COURSE_TAGS, JSONArray.fromObject(listCourseTags));
		return SUCCESS;
	}
	
	public String findPlanTypes() throws ServerErrorException{
		PlanTypeService planTypeService = BeanFactory.getPlanTypeService();
		List<PlanType> planTypes = new ArrayList<PlanType>();
		planTypes = planTypeService.findAllPlanTypes();
		for (PlanType type : planTypes) {
			type.setPlanTypeName(getText(type.getPlanTypeName()));
		}
		JsonConfig config = new JsonConfig();
		config.setExcludes(new String[]{ "planList" });
		jsonObject = new JSONObject();
		jsonObject.put(JsonKeyConstants.PLAN_TYPES, JSONArray.fromObject(planTypes,config));
		return SUCCESS;
	}
	
	public String findPlanTags() {
		PlanTagService planTagService = BeanFactory.getPlanTagService();
		List<PlanTag> planTags = new ArrayList<PlanTag>();
		try {
			planTags = planTagService.findAllPlanTags();
		} catch (ServerErrorException e) {
			logger.error(e);
		}
		jsonObject = new JSONObject();
		jsonObject.put(JsonKeyConstants.PLAN_TAGS, JSONArray.fromObject(planTags));
		return SUCCESS;
	}
	
	/**
	 * 
	 * Find all Employees' names
	 * 
	 * @throws Exception
	 */
	public String findAllEmployeeNames () throws Exception {
		Employee employee = SessionObjectUtils.getEmployeeFromSession();
		String result = "";
		if (null == employee) {
			return SUCCESS;
		}
		List<String> nameList = employeeService.findAllEmployeeNames();
		StringBuilder builder = new StringBuilder();
		for (String string : nameList) {
			builder.append(string);
			builder.append(FlagConstants.SPLIT_COMMA);
		}
		if (builder.length() > 0) {
			result = builder.substring(0,builder.length() - 1);
		}
		jsonObject = new JSONObject();
		jsonObject.put(JsonKeyConstants.NAMES, result);
		return SUCCESS;
		
	}
	
	@Resource
	public void setEmployeeService(EmployeeService employeeService) {
		this.employeeService = employeeService;
	}

	public String getCustomization() {
		return customization;
	}

	public void setCustomization(String customization) {
		this.customization = customization;
	}
	
	public int getSearchFlag() {
		return searchFlag;
	}

	public void setSearchFlag(int searchFlag) {
		this.searchFlag = searchFlag;
	}

	public CourseSearchCondition getCriteria() {
		return criteria;
	}

	public void setCriteria(CourseSearchCondition criteria) {
		this.criteria = criteria;
	}
}
