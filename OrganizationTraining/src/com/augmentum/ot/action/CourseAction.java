package com.augmentum.ot.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

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

import com.augmentum.ot.dataObject.CourseSearchCondition;
import com.augmentum.ot.dataObject.ErrorCode;
import com.augmentum.ot.dataObject.FromSearchToViewCondition;
import com.augmentum.ot.dataObject.Page;
import com.augmentum.ot.dataObject.constant.DateFormatConstants;
import com.augmentum.ot.dataObject.constant.ErrorCodeConstants;
import com.augmentum.ot.dataObject.constant.FlagConstants;
import com.augmentum.ot.dataObject.constant.IndexFieldConstants;
import com.augmentum.ot.dataObject.constant.JsonKeyConstants;
import com.augmentum.ot.dataObject.constant.LogConstants;
import com.augmentum.ot.exception.DataWarningException;
import com.augmentum.ot.exception.ServerErrorException;
import com.augmentum.ot.model.Course;
import com.augmentum.ot.model.CourseAttachment;
import com.augmentum.ot.model.CourseType;
import com.augmentum.ot.model.Employee;
import com.augmentum.ot.service.CourseService;
import com.augmentum.ot.service.CourseTypeService;
import com.augmentum.ot.util.BeanFactory;
import com.augmentum.ot.util.DateJsonValueProcessor;
import com.augmentum.ot.util.ReaderXmlUtils;
import com.augmentum.ot.util.SessionObjectUtils;
import com.augmentum.ot.util.ValidationUtil;

@Component("courseAction")
@Scope("prototype")
public class CourseAction extends BaseAction {

	private static final long serialVersionUID = 996648652365249627L;
	private Logger logger = Logger.getLogger(CourseAction.class);
	private CourseService courseService;
	private CourseSearchCondition criteria;
	private String deletedAttachmentIds;
	private Course course;
	private Integer courseId;
	private String attachmentsJson;
	private JSONArray jsonArray;
	private String operationFlag;
	private FromSearchToViewCondition fromSearchToViewCondition;
	private int nextFlag=0;
	private int previousFlag=0;
	private boolean hasCondition = false;
	private int viewType=0;
	private Boolean forChangeLocaleFlag;
	
	private boolean validateSearchCondition() {
		if (null == criteria) {
			return false;
		}
		return true;
	}
	
	/**
	 *  Search Course
	 * @return
	 * @throws ServiceException
	 */
	public String searchCourse() {
		Employee employee = SessionObjectUtils.getEmployeeFromSession();
		logger.info(LogConstants.getInfo(employee.getAugUserName(), "searchCourse"));
		if (!validateSearchCondition()) {
			ErrorCode errorCode = ReaderXmlUtils.getErrorCodes().get(ErrorCodeConstants.SERVER_ERROR);
			jsonObject = JSONObject.fromObject(errorCode);
			return FlagConstants.VALIDATION_ERROR_JSON;
		}
		if (criteria.getQueryString() == null) {
			criteria.setQueryString("");
		}
		if (criteria.getSearchFields() == null) {
			criteria.setSearchFields(IndexFieldConstants.COURSE_SEARCH_FIELDS_ALL);
		} else {
			String fields = criteria.getSearchFields()[0];
			criteria.setSearchFields(fields.split(FlagConstants.SPLIT_COMMA));
		}
		Page<Course> coursePage = null;
		try {
			coursePage = courseService.searchCoursesFromIndex(criteria);
		} catch (ServerErrorException e) {
			ErrorCode errorCode = ReaderXmlUtils.getErrorCodes().get(ErrorCodeConstants.SERVER_ERROR);
			jsonObject = JSONObject.fromObject(errorCode);
			return FlagConstants.ERROR_SERVER_JSON;
		}
		
		jsonObject = new JSONObject();
		jsonObject.put(JsonKeyConstants.COUNT, coursePage.getTotalRecords());
        jsonObject.put(JsonKeyConstants.TOTAL_PAGE, coursePage.getTotalPage());
        jsonObject.put(JsonKeyConstants.FIELDS_DATA, coursesToJsonArray(coursePage.getList()));
        
        return SUCCESS;
	}
	
	/**
	 * Prepare for info from view to search.
	 * @return
	 */
	public String preSearchCourse(){
	    if(fromSearchToViewCondition!=null&&criteria!=null){
	        hasCondition=true;
	    }
	    return SUCCESS;
	}
	
	/**
	 * set search course result to json array, for searchList use
	 * @param courseList
	 * @return
	 */
	private JSONArray coursesToJsonArray(List<Course> courseList) {
		logger.debug(LogConstants.message("Input course list size", courseList.size()));
		JSONArray arrays = new JSONArray();
		if (courseList != null) {
			 for (Course course : courseList) {
		            JSONArray array = new JSONArray();
		            array.add(course.getCourseId());
		            if (course.getCourseHasAttachment() == FlagConstants.HAS_ATTACHMENT) {
		            	array.add("<img class='downloadImag' courseId="+course.getCourseId()+" src='" + ServletActionContext.getRequest().getContextPath() + "/searchList/images/ICN_Attachment_16x16.png'>");
		            } else {
		            	array.add("");
		            }
		            array.add("<a class='viewDetail' courseId="+course.getCourseId()+">"+course.getPrefixIDValue()+"</a>");
		            array.add(course.getCourseName());
		            array.add(course.getCourseBriefWithoutTag());
		            array.add(course.getCourseTargetTrainee());
		            array.add(course.getCourseDuration()+"h");
		            // Make sure the type name is from properties.
		            array.add(getText(course.getCourseType().getTypeName()));  
		            array.add(course.getCourseCategoryTag());
		            String ifCertificated = getText("No");
		            if (course.getCourseIsCertificated() == 1) {
		            	ifCertificated = getText("Yes");
		            }
		            array.add(ifCertificated);
		            array.add("ToDo");
		            array.add(course.getCourseAuthorName());
		            if (course.getHistoryTrainers() == null) {
		            	array.add("");
		            } else {
		            	array.add(course.getHistoryTrainers());
		            }
		            arrays.add(array);
			 }
		}
        return arrays;
	}
	
	private boolean validateCreateCourse(){
		if (null == course) {
			return false;
		}
		
		if (null == course.getCourseType()) {
			return false;
		}
		
		if (null == course.getCourseType().getCourseTypeId() || course.getCourseType().getCourseTypeId() <= 0) {
			return false;
		}
		
		if (!(ValidationUtil.isNotNull(course.getCourseName()))){
			return false;
		}
		if (course.getCourseName().length() > 200) {
			return false;
		}
		if (!(ValidationUtil.isNotNull(course.getCourseBrief()))) {
			return false;
		}
		if (course.getCourseBrief().length() > 100000) {
			return false;
		}
		if (course.getCourseBriefWithoutTag().length() > 10000){
		    return false;
		}
		if (!(ValidationUtil.isNotNull(course.getCourseTargetTrainee()))) {
			return false;
		}
		if (course.getCourseTargetTrainee().length() > 1000) {
			return false;
		}
		if (null == course.getCourseDuration()) {
			return false;
		}
		if (course.getCourseDuration() <= 0 || course.getCourseDuration() > 8) {
			return false;
		}
		String[] tags = course.getCourseCategoryTag().split(";");
		if (tags.length > 5) {
			return false;
		}
		HashSet<String> hashSet = new HashSet<String>();
		if (tags.length >0) {
			for (String courseTag : tags) {
				hashSet.add(courseTag);
			}
		}
		if (tags.length != hashSet.size()) {
			return false;
		}
		//the following code is to handle the special characters
		course.setCourseName(HtmlUtils.htmlEscape(course.getCourseName()));
		String targetTrainee = HtmlUtils.htmlEscape(course.getCourseTargetTrainee());
		course.setCourseTargetTrainee(targetTrainee.replace("&lt;br /&gt;", "<br />"));
		return true;
	}

	 /** 
	 * Create course
	 * @return
	 * @throws ServiceException 
	 */ 
	@SuppressWarnings("unchecked")
	public String createCourse() throws ServerErrorException {
		Employee employee = SessionObjectUtils.getEmployeeFromSession();
		logger.info(LogConstants.getInfo(employee.getAugUserName(), "createCourse"));
		HttpServletRequest httpRequest = ServletActionContext.getRequest();
		if (!validateCreateCourse()) {
			logger.warn(LogConstants.getValidationMsg(employee.getAugUserName()));
			return FlagConstants.VALIDATION_ERROR;
		}
		/**change attachment jsonArray to list*/
		List<CourseAttachment> courseAttachments = JSONArray.toList(JSONArray.fromObject(attachmentsJson), CourseAttachment.class);
		if (FlagConstants.SAVE_AS_OPERATION.equals(operationFlag)) {
			course.setCourseId(null);
			/**save as need to add new attachment in DB*/
			if (null != courseAttachments && courseAttachments.size() > 0) {
				for (CourseAttachment courseAttachment:courseAttachments) {
					if (courseAttachment.getCourseAttach() == null) {
						courseAttachment.setCourseAttachmentId(null);
					}
				}
        	}
		}
        CourseTypeService courseTypeService = BeanFactory.getCourseTypeService();
	    CourseType courseType = null;
	    try {
	        courseType = courseTypeService.findCourseTypeById(course.getCourseType().getCourseTypeId());
	        course.setCourseType(courseType);
	        course.setCourseCreator(employee.getAugUserName());
	        course.setCourseHasHomework(FlagConstants.HAS_HOMEWORK);
	        course.setCourseIsCertificated(FlagConstants.IS_CERTIFICATED);
	        course.setCourseIsDeleted(FlagConstants.UN_DELETED);
	        course.setCourseCreateDate(new Date());
	        course.setCourseLastUpdateDate(new Date());
	        courseService.createCourse(course, courseAttachments, httpRequest);
        } catch (DataWarningException e) {
        	ErrorCode errorCode = ReaderXmlUtils.getErrorCodes().get(e.getMessage());
        	addActionMessage(getText(errorCode.getErrorMessageKey()));
            return FlagConstants.DATA_WARNING;
        } catch (ServerErrorException e) {
            return FlagConstants.ERROR_SERVER;
        }
        
        session.put(JsonKeyConstants.OPERATION_FLAG, FlagConstants.CREATE_OPERATION);
	    return SUCCESS;
	}
	
	/**
	 * View course detail
	 * @return
	 */
	public String viewCourseDetail() {
		Employee employee = SessionObjectUtils.getEmployeeFromSession();
		logger.info(LogConstants.getInfo(employee.getAugUserName(), "viewCourseDetail"));
		if (null == courseId || courseId <= 0) {
			return FlagConstants.VALIDATION_ERROR;
		}
		try {
			course = courseService.getCourseById(courseId);
		} catch (DataWarningException e) {
			addActionMessage(getText(ReaderXmlUtils.getErrorCodes().get(e.getMessage()).getErrorMessageKey()));
			return FlagConstants.DATA_WARNING;
		} catch (ServerErrorException e) {
			return FlagConstants.ERROR_SERVER;
		}
		course.getCourseType().setTypeName(getText(course.getCourseType().getTypeName()));
		List<CourseAttachment> attachmentList = course.getCourseAttachments();
		JsonConfig jsonConfig = new JsonConfig();
		String[] str = { "courseAttach" };
		jsonConfig.setExcludes(str);
        jsonConfig.registerJsonValueProcessor(java.util.Date.class,
                new DateJsonValueProcessor(DateFormatConstants.YYYY_MM_DD_));
		attachmentsJson = JSONArray.fromObject(attachmentList, jsonConfig).toString();
		return SUCCESS;
	}
	
	/**
	 * Change to edit page
	 * @return
	 */
	public String toEditCourse() {
		try {
			course = courseService.getCourseById(courseId);
			//handle the special characters, if you don't do that, the '<' or '>' will be decoded by rich editor
			//and the <b>a<b> will be a bold 'a'
			course.setCourseBrief(HtmlUtils.htmlEscape(course.getCourseBrief()));
		} catch (DataWarningException e) {
			addActionMessage(getText(ReaderXmlUtils.getErrorCodes().get(e.getMessage()).getErrorMessageKey()));
			return FlagConstants.DATA_WARNING;
		} catch (ServerErrorException e) {
			return FlagConstants.ERROR_SERVER;
		}
		List<CourseAttachment> attachmentList = course.getCourseAttachments();
		JsonConfig jsonConfig = new JsonConfig();
		String[] str = { "courseAttach" };
		jsonConfig.setExcludes(str);
        jsonConfig.registerJsonValueProcessor(java.util.Date.class,
                new DateJsonValueProcessor(DateFormatConstants.YYYY_MM_DD_));
		attachmentsJson = JSONArray.fromObject(attachmentList, jsonConfig).toString();
		return SUCCESS;
	}
	
    /**
     * Used to view need record detail(includes the next record, the previous
     * record and the current ) according to replace viewCourseDetail method.
     * @return
     */
    public String viewNeedRecordDetail() {
        if (fromSearchToViewCondition == null) {
            return courseDetail();
        }
        /**
         * If viewType is 1, need view next record. If viewType is -1, need view
         * previous record. If viewType is 0(default), need view this record.
         */
        if (viewType == 1) {
            if(!forChangeLocaleFlag){
                String error = courseService.plusRecordId(
                        fromSearchToViewCondition, criteria);
                if (error == "conditionError") {
                    // The condition is wrong, so use normal view method.
                    return courseDetail();
                } else if (error == "dataError") {
                    // The data is wrong, so view this record to replace viewing the
                    // next record.
                }
            }
        } else if (viewType == -1) {
            if(!forChangeLocaleFlag){
                String error = courseService.subtractRecordId(
                        fromSearchToViewCondition, criteria);
                if (error == "conditionError") {
                    // The condition is wrong, so use normal view method.
                    return courseDetail();
                } else if (error == "dataError") {
                    // The data is wrong, so view this record to replace viewing the
                    // previous record.
                }
            }
        }
        courseId = fromSearchToViewCondition.getNowId();
        nextFlag = courseService.getNextRecordFlag(fromSearchToViewCondition);
        previousFlag = courseService
                .getPreviousRecordFlag(fromSearchToViewCondition);
        return courseDetail();
    }
    

    /**
     * Created by copy the viewCourseDetail method. Because need the treatment
     * process of the method.
     * @return
     */
    private String courseDetail() {
    	if (null == courseId || courseId <= 0) {
			return FlagConstants.VALIDATION_ERROR;
		}
        try {
            course = courseService.getCourseById(courseId);
        } catch (DataWarningException e) {
        	addActionMessage(getText(ReaderXmlUtils.getErrorCodes().get(e.getMessage()).getErrorMessageKey()));
			return FlagConstants.DATA_WARNING;
        } catch (ServerErrorException e) {
        	return FlagConstants.ERROR_SERVER;
        }
        if (null == course) {
            return FlagConstants.ERROR_SERVER;
        }
        course.getCourseType().setTypeName(getText(course.getCourseType().getTypeName()));
        List<CourseAttachment> attachmentList = new ArrayList<CourseAttachment>();
        if (course.getCourseHasAttachment().equals(FlagConstants.HAS_ATTACHMENT)) {
        	attachmentList = course.getCourseAttachments();
        }
        JsonConfig jsonConfig = new JsonConfig();
        String[] str = { "courseAttach" };
		jsonConfig.setExcludes(str);
        jsonConfig.registerJsonValueProcessor(java.util.Date.class,
                new DateJsonValueProcessor(DateFormatConstants.YYYY_MM_DD_));
		attachmentsJson = JSONArray.fromObject(attachmentList, jsonConfig).toString();
        if (null != operationFlag
                && FlagConstants.TO_EDIT_PAGE.equals(operationFlag)) {
            return FlagConstants.TO_EDIT_PAGE;
        }
        return SUCCESS;
    }
	
    /**
     * Delete course
     * @return
     */
	public String deleteCourse() {
		Employee employee = SessionObjectUtils.getEmployeeFromSession();
		logger.info(LogConstants.getInfo(employee.getAugUserName(), "deleteCourse"));
		if (null == courseId || courseId <= 0) {
			return FlagConstants.VALIDATION_ERROR;
		}
		try {
			courseService.deleteCourseById(courseId);
		} catch (DataWarningException e) {
			addActionMessage(getText(ReaderXmlUtils.getErrorCodes().get(e.getMessage()).getErrorMessageKey()));
			return FlagConstants.DATA_WARNING;
		} catch (ServerErrorException e) {
			return FlagConstants.ERROR_SERVER;
		}
		return SUCCESS;
	}
	
	/**
	 * Edit course
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String editCourse() {
		Employee employee = SessionObjectUtils.getEmployeeFromSession();
		logger.info(LogConstants.getInfo(employee.getAugUserName(), "editCourse"));
		HttpServletRequest httpRequest = ServletActionContext.getRequest();
		if (!validateCreateCourse()) {
			logger.warn(LogConstants.getValidationMsg(employee.getAugUserName()));
			return FlagConstants.VALIDATION_ERROR;
		}
	    CourseTypeService courseTypeService = BeanFactory.getCourseTypeService();
	    CourseType courseType = new CourseType();
	    try {
			courseType = courseTypeService.findCourseTypeById(course.getCourseType().getCourseTypeId());
		} catch (DataWarningException e) {
			addActionMessage(getText(ReaderXmlUtils.getErrorCodes().get(e.getMessage()).getErrorMessageKey()));
			return FlagConstants.DATA_WARNING;
		} catch (ServerErrorException e) {
			return FlagConstants.ERROR_SERVER;
		}
		courseType.setTypeName(getText(courseType.getTypeName()));
		course.setCourseType(courseType);
		/**change attachment jsonArray to list*/
		List<CourseAttachment> courseAttachments = JSONArray.toList(JSONArray.fromObject(attachmentsJson), CourseAttachment.class);
		JsonConfig jsonConfig = new JsonConfig();
	    String[] str = { "courseAttach" };
		jsonConfig.setExcludes(str);
        jsonConfig.registerJsonValueProcessor(java.util.Date.class,
                new DateJsonValueProcessor(DateFormatConstants.YYYY_MM_DD_));
		attachmentsJson = JSONArray.fromObject(courseAttachments, jsonConfig).toString();
	    try {
			course = courseService.updateCourse(course, courseAttachments, httpRequest);
		} catch (DataWarningException e) {
			addActionMessage(getText(ReaderXmlUtils.getErrorCodes().get(e.getMessage()).getErrorMessageKey()));
			return FlagConstants.DATA_WARNING;
		} catch (ServerErrorException e) {
			return FlagConstants.ERROR_SERVER;
		}
	    return SUCCESS;
	}
	
	/**
	 * Get course attachment used in search page download
	 * @return
	 */
	public String getCourseAttachmentsById() {
		Employee employee = SessionObjectUtils.getEmployeeFromSession();
		logger.info(LogConstants.getInfo(employee.getAugUserName(), "getCourseAttachmentsById"));
		try {
			course = courseService.getCourseById(courseId);
		} catch (DataWarningException e) {
			addActionMessage(getText(ReaderXmlUtils.getErrorCodes().get(e.getMessage()).getErrorMessageKey()));
			return FlagConstants.DATA_WARNING;
		} catch (ServerErrorException e) {
			return FlagConstants.ERROR_SERVER;
		}
		List<CourseAttachment> attachmentList = course.getCourseAttachments();
		jsonArray = new JSONArray();
		JsonConfig jsonConfig = new JsonConfig();
	    String[] str = { "courseAttach" };
		jsonConfig.setExcludes(str);
        jsonConfig.registerJsonValueProcessor(java.util.Date.class,
                new DateJsonValueProcessor(DateFormatConstants.YYYY_MM_DD_));
		jsonArray = JSONArray.fromObject(attachmentList, jsonConfig);
		return SUCCESS;
	}
	
	/**
	 * clear session, used for message bar
	 */
	public void clearOperation() {
		session.remove("operationFlag");
	}
	
	@Resource
	public void setCourseService(CourseService courseService) {
		this.courseService = courseService;
	}


	public CourseSearchCondition getCriteria() {
		return criteria;
	}

	public void setCriteria(CourseSearchCondition criteria) {
		this.criteria = criteria;
	}

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public Integer getCourseId() {
        return courseId;
    }

    public void setCourseId(Integer courseId) {
        this.courseId = courseId;
    }

    public String getAttachmentsJson() {
        return attachmentsJson;
	}

	public void setAttachmentsJson(String attachmentsJson) {
		this.attachmentsJson = attachmentsJson;
	}

	public String getOperationFlag() {
		return operationFlag;
	}

	public void setOperationFlag(String operationFlag) {
		this.operationFlag = operationFlag;
	}

	public String getDeletedAttachmentIds() {
		return deletedAttachmentIds;
	}

	public void setDeletedAttachmentIds(String deletedAttachmentIds) {
		this.deletedAttachmentIds = deletedAttachmentIds;
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

	public JSONArray getJsonArray() {
		return jsonArray;
	}

	public void setJsonArray(JSONArray jsonArray) {
		this.jsonArray = jsonArray;
	}

    public Boolean getForChangeLocaleFlag() {
        return forChangeLocaleFlag;
    }

    public void setForChangeLocaleFlag(Boolean forChangeLocaleFlag) {
        this.forChangeLocaleFlag = forChangeLocaleFlag;
    }

}
