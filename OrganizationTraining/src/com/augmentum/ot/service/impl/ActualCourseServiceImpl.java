package com.augmentum.ot.service.impl;

/**
 * 
 * @Description: ActualService interface provide methods about plan 
 *
 * @version V1.0, 2012-12-20
 */
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.queryParser.MultiFieldQueryParser;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.QueryWrapperFilter;
import org.apache.lucene.search.TermRangeQuery;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.util.Version;
import org.springframework.stereotype.Component;
import org.wltea.analyzer.lucene.IKAnalyzer;

import com.augmentum.ot.dao.ActualCourseDao;
import com.augmentum.ot.dao.AssessmentDao;
import com.augmentum.ot.dao.CourseDao;
import com.augmentum.ot.dao.EmployeeDao;
import com.augmentum.ot.dataObject.ActualCourseSearchCondition;
import com.augmentum.ot.dataObject.Page;
import com.augmentum.ot.dataObject.constant.DateFormatConstants;
import com.augmentum.ot.dataObject.constant.ErrorCodeConstants;
import com.augmentum.ot.dataObject.constant.FlagConstants;
import com.augmentum.ot.dataObject.constant.IndexFieldConstants;
import com.augmentum.ot.dataObject.constant.JsonKeyConstants;
import com.augmentum.ot.dataObject.constant.LogConstants;
import com.augmentum.ot.dataObject.constant.RoleNameConstants;
import com.augmentum.ot.exception.DataWarningException;
import com.augmentum.ot.exception.ServerErrorException;
import com.augmentum.ot.model.ActualCourse;
import com.augmentum.ot.model.ActualCourseAttachment;
import com.augmentum.ot.model.Assessment;
import com.augmentum.ot.model.Course;
import com.augmentum.ot.model.CourseAttachment;
import com.augmentum.ot.model.CourseInfo;
import com.augmentum.ot.model.Employee;
import com.augmentum.ot.model.Plan;
import com.augmentum.ot.model.PlanEmployeeMap;
import com.augmentum.ot.service.ActualCourseService;
import com.augmentum.ot.util.BeanFactory;
import com.augmentum.ot.util.DateHandlerUtils;
import com.augmentum.ot.util.StringHandlerUtils;
/**
 * 
 * @Description: ActualService interface provide methods about plan 
 *
 * @version V1.0, 2012-12-20
 */
@Component("actualCourseService")
public class ActualCourseServiceImpl implements ActualCourseService{

	@Resource(name="actualCourseDao")
	private ActualCourseDao actualCourseDao;
	
	@Resource(name="assessmentDao")
	private AssessmentDao assessmentDao;
	
	@Resource(name="courseDao")
	private CourseDao courseDao;
	
	@Resource(name="employeeDao")
	private EmployeeDao employeeDao;
	
	private static Logger logger = Logger.getLogger(ActualCourseServiceImpl.class);
	
	public ActualCourseDao getActualCourseDao() {
		return actualCourseDao;
	}
	
	public void setActualCourseDao(ActualCourseDao actualCourseDao) {
		this.actualCourseDao = actualCourseDao;
	}

	public AssessmentDao getAssessmentDao() {
		return assessmentDao;
	}

	public void setAssessmentDao(AssessmentDao assessmentDao) {
		this.assessmentDao = assessmentDao;
	}

	
	public CourseDao getCourseDao() {
		return courseDao;
	}

	public void setCourseDao(CourseDao courseDao) {
		this.courseDao = courseDao;
	}

	public EmployeeDao getEmployeeDao() {
		return employeeDao;
	}

	public void setEmployeeDao(EmployeeDao employeeDao) {
		this.employeeDao = employeeDao;
	}

	/**
	 * 
	 * @Title: getTemporaryActualCourseByIds  
	 * @Description: get temporary actualCourses for select course list when create or edit a plan
	 *
	 * @param ids
	 * 			1_C,1_PC,4_C,12_PC
	 * @return list of ActualCourse
	 * @throws ServerErrorException
	 * @throws DataWarningException
	 */
	@Override
	public List<ActualCourse> getTemporaryActualCoursesByIds(String ids)
			throws ServerErrorException {
		
		logger.debug(LogConstants.getDebugInput("Get temporary actualCourses by ids", ids));
		String[] idInfoArray = null;
		if (null == ids || "".equals(ids)) {
			logger.error(LogConstants.objectIsNULLOrEmpty("ids"));
			throw new ServerErrorException(ErrorCodeConstants.SERVER_VALIDATION_ERROR);
		} else {
			idInfoArray = ids.split(",");
		}
		List<ActualCourse> actualCourses = new ArrayList<ActualCourse>();
		try {
			for (String idInfo : idInfoArray) {
				String[] idArray = idInfo.split("_");
				CourseInfo courseInfo = new CourseInfo();
				ActualCourse newActualCourse = new ActualCourse();
				//if course is from course -- "courseId_C"
				if (idArray[1].equals(FlagConstants.COURSE_NUMBER_PREFIX)) {
					CourseDao courseDao = BeanFactory.getCourseDao();
					Course course = courseDao.findByPrimaryKey(Integer.parseInt(idArray[0]));
					
					//set courseInfo 
					courseInfo.setCourseAuthorName(course.getCourseAuthorName());
					courseInfo.setCourseCategoryTag(course.getCourseCategoryTag());
					courseInfo.setCourseId(course.getCourseId());
					courseInfo.setCourseTargetTrainee(course.getCourseTargetTrainee());
					courseInfo.setCourseType(course.getCourseType());
					
					//set actualCourse
					newActualCourse.setCourseInfo(courseInfo);
					newActualCourse.setCourseName(course.getCourseName());
					newActualCourse.setCourseDuration(course.getCourseDuration());
					newActualCourse.setCourseBrief(course.getCourseBrief());
					newActualCourse.setCourseBriefWithoutTag(course.getCourseBriefWithoutTag());
				
					//set attachments
					if (course.getCourseHasAttachment().equals(FlagConstants.HAS_ATTACHMENT)) {
						newActualCourse.setCourseHasAttachment(FlagConstants.HAS_ATTACHMENT);
						for (CourseAttachment attac : course.getCourseAttachments()) {
							//if courseAttachments has deleted, we need not it.
							if (null != attac.getCourseAttachmentIsDeleted() 
									&& attac.getCourseAttachmentIsDeleted().equals(FlagConstants.UN_DELETED)){
								ActualCourseAttachment actualCourseAttachment = new ActualCourseAttachment();
								actualCourseAttachment.setActualCoursAttachmentName(attac.getCourseAttachmentName());
								actualCourseAttachment.setActualCourseAttachmentIsDeleted(FlagConstants.UN_DELETED);
								actualCourseAttachment.setActualCourseAttachmentPath(attac.getCourseAttachmentPath());
								actualCourseAttachment.setActualCourseAttachmentVisible(attac.getCourseAttachmentvisible());
								actualCourseAttachment.setSize(attac.getSize());
								actualCourseAttachment.setActualCourse(newActualCourse);
								actualCourseAttachment.setCreateDateTime(attac.getCreateDateTime());
								newActualCourse.getAttachments().add(actualCourseAttachment);
							}
						}
					}
				}
				if (idArray[1].equals(FlagConstants.PLAN_COURSE_NUMBER_PREFIX)) {
					ActualCourse actualCourse = actualCourseDao.findByPrimaryKey(Integer.parseInt(idArray[0]));
					//set courseInfo 
					courseInfo.setCourseAuthorName(actualCourse.getCourseInfo().getCourseAuthorName());
					courseInfo.setCourseCategoryTag(actualCourse.getCourseInfo().getCourseCategoryTag());
					courseInfo.setCourseId(actualCourse.getCourseInfo().getCourseId());
					courseInfo.setCourseTargetTrainee(actualCourse.getCourseInfo().getCourseTargetTrainee());
					courseInfo.setCourseType(actualCourse.getCourseInfo().getCourseType());
					//set actualCourse
					
					newActualCourse.setCourseInfo(courseInfo);
					newActualCourse.setCourseName(actualCourse.getCourseName());
					newActualCourse.setCourseDuration(actualCourse.getCourseDuration());
					newActualCourse.setCourseBrief(actualCourse.getCourseBrief());
					newActualCourse.setCourseBriefWithoutTag(actualCourse.getCourseBriefWithoutTag());
					
					//set attachments
					if (actualCourse.getCourseHasAttachment().equals(FlagConstants.HAS_ATTACHMENT)) {
						newActualCourse.setCourseHasAttachment(FlagConstants.HAS_ATTACHMENT);
						for (ActualCourseAttachment attac : actualCourse.getAttachments()) {
							//if courseAttachments has deleted, we need not it.
							if (null != attac.getActualCourseAttachmentIsDeleted()
									&& attac.getActualCourseAttachmentIsDeleted().equals(FlagConstants.UN_DELETED)){
								ActualCourseAttachment actualCourseAttachment = new ActualCourseAttachment();
								actualCourseAttachment.setActualCoursAttachmentName(attac.getActualCoursAttachmentName());
								actualCourseAttachment.setActualCourseAttachmentIsDeleted(FlagConstants.UN_DELETED);
								actualCourseAttachment.setActualCourseAttachmentPath(attac.getActualCourseAttachmentPath());
								actualCourseAttachment.setActualCourseAttachmentVisible(attac.getActualCourseAttachmentVisible());
								actualCourseAttachment.setSize(attac.getSize());
								actualCourseAttachment.setActualCourse(newActualCourse);
								actualCourseAttachment.setCreateDateTime(attac.getCreateDateTime());
								newActualCourse.getAttachments().add(actualCourseAttachment);
							}
						}
					}
				}
				actualCourses.add(newActualCourse);
			}
		} catch(Exception e){
			logger.error(LogConstants.exceptionMessage("[get temporary ActualCourses By Ids]" + LogConstants.getExceptionStackTrace(e)));
			throw new ServerErrorException(ErrorCodeConstants.SERVER_ERROR);
		}
		logger.debug(LogConstants.getDebugOutput("Get temporary actualCourses", actualCourses));
		return actualCourses;
	}

	@Override
	public List<Employee> getTraineesByActualCourseId(Integer actualCourseId)
			throws ServerErrorException, DataWarningException {
		logger.debug(LogConstants.getDebugInput("get trainees by actual course id",
				actualCourseId));
		if (null == actualCourseId || actualCourseId <= 0) {
			logger.error(LogConstants.objectIsNULLOrEmpty("actualCourseId"));
			throw new ServerErrorException(ErrorCodeConstants.SERVER_VALIDATION_ERROR);
		}
		ActualCourse actualCourse = null;
		try {
			actualCourse = actualCourseDao.findByPrimaryKey(actualCourseId);
		} catch (Exception e) {
			logger.error(LogConstants.exceptionMessage("find actual course "), e);
			throw new ServerErrorException(ErrorCodeConstants.SERVER_ERROR);
		}
		if (actualCourse == null) {
			logger.warn(LogConstants.getLogActualCourseByErrorCode(ErrorCodeConstants.GET_DELETED_ACTUAL_COURSE, actualCourseId));
			throw new DataWarningException(ErrorCodeConstants.GET_DELETED_ACTUAL_COURSE);
		}
		Plan plan = actualCourse.getPlan();
		Set<PlanEmployeeMap> employeeMap = plan.getPlanEmployeeMapList();
		List<Employee> employeeList = new ArrayList<Employee>();
		for (PlanEmployeeMap item : employeeMap) {
			employeeList.add(item.getEmployee());
		}
		logger.debug(LogConstants.getDebugOutput("get trainees by actual course id",
				employeeList));
		return employeeList;
	}

	@Override
	public ActualCourse findActualCourseById(Integer actualCourseId)
			throws ServerErrorException, DataWarningException {
		logger.debug(LogConstants.getDebugInput("Find actual course by id",
				actualCourseId));
		if (null == actualCourseId || actualCourseId <= 0) {
			logger.error(LogConstants.objectIsNULLOrEmpty("actualCourseId"));
			throw new ServerErrorException(ErrorCodeConstants.SERVER_VALIDATION_ERROR);
		}
		ActualCourse actualCourse = null;
		try {
			actualCourse = actualCourseDao.findByPrimaryKey(actualCourseId);
		} catch (Exception e) {
			logger.error(LogConstants.exceptionMessage("find actual course "), e);
			throw new ServerErrorException(ErrorCodeConstants.SERVER_ERROR);
		}
		if (actualCourse == null) {
			logger.warn(LogConstants.getLogActualCourseByErrorCode(ErrorCodeConstants.GET_DELETED_ACTUAL_COURSE, actualCourseId));
			throw new DataWarningException(ErrorCodeConstants.GET_DELETED_ACTUAL_COURSE);
		}
		logger.debug(LogConstants.getDebugOutput("Find a actual course",
				actualCourse));
		return actualCourse;
	}

	@Override
	public ActualCourse getActualCourseById(Integer actualCourseId) throws DataWarningException, ServerErrorException {
		logger.debug(LogConstants.getDebugInput("get actual course by id",
				actualCourseId));
		if (null == actualCourseId || actualCourseId <= 0) {
			logger.error(LogConstants.objectIsNULLOrEmpty("actualCourseId"));
			throw new ServerErrorException(ErrorCodeConstants.SERVER_VALIDATION_ERROR);
		}
		ActualCourse actualCourse = null;
		try {
			actualCourse = actualCourseDao.findByPrimaryKey(actualCourseId);
		} catch (Exception e) {
			logger.error(LogConstants.exceptionMessage("find actual course "), e);
			throw new ServerErrorException(ErrorCodeConstants.SERVER_ERROR);
		}
		for(Employee employee : actualCourse.getEmployeeList()){
	           employee.getEmployeeId();
	    }
		if (null == actualCourse) {
			logger.warn(LogConstants.getLogActualCourseByErrorCode(ErrorCodeConstants.GET_DELETED_ACTUAL_COURSE, actualCourseId));
			throw new DataWarningException(ErrorCodeConstants.GET_DELETED_ACTUAL_COURSE);
		}
		// get attachments
		List<ActualCourseAttachment> attachments = new ArrayList<ActualCourseAttachment>();
		for (ActualCourseAttachment attachment : actualCourse.getAttachments()) {
			if (attachment.getActualCourseAttachmentIsDeleted() == FlagConstants.UN_DELETED) {
				attachments.add(attachment);
			}
		}
		actualCourse.setAttachments(attachments);
		// get plan detail
		actualCourse.getPlan().getPrefixIDValue();
		if(actualCourse.getLeaveNoteList() != null && actualCourse.getLeaveNoteList().size() != 0){
		    actualCourse.getLeaveNoteList().get(0);
		}
		logger.debug(LogConstants.getDebugOutput("Find a actual course",
				actualCourse));
		return actualCourse;
	}

	@Override
	public void createAllActualCourseIndexes() throws ServerErrorException {
		try {
    		String hql = "from ActualCourse actualCourse";
            actualCourseDao.rebuildAllEntitiesIndexes(hql);
    	}catch(Exception e) {
    		logger.error(LogConstants.exceptionMessage("Create all actual course indexes"), e);
    		throw new ServerErrorException(ErrorCodeConstants.SERVER_ERROR);
    	}
	}

	@Override
	public List<ActualCourse> findActualCoursesByPlanId(Integer planId)
			throws DataWarningException, ServerErrorException {
		if (null == planId || planId <= 0) {
			logger.warn(LogConstants.objectIsNULLOrEmpty("planId"));
			throw new ServerErrorException(ErrorCodeConstants.SERVER_VALIDATION_ERROR);
		}
		List<ActualCourse> actualCourses=null;
		try{
			actualCourses = actualCourseDao.findActualCoursesByPlanId(planId);
		} catch (Exception e) {
			logger.error(LogConstants.exceptionMessage("find actual courses by planId"), e);
			throw new ServerErrorException(ErrorCodeConstants.SERVER_ERROR);
		}
		return actualCourses;
	}

	@Override
	public Map<Integer, Long> getNeededRemideCourses()
			throws ServerErrorException {
		List<ActualCourse> actualCourseList = actualCourseDao.findEntityListByRemideTimer();
		Map<Integer, Long> neededRemindMap = new ConcurrentHashMap<Integer, Long>();
		for (ActualCourse actualCourse : actualCourseList) {
			Long whichTimeSended = actualCourse.getCourseStartTime().getTime() - 
					actualCourse.getPlan().getReminderEmail() * 3600 * 1000;
			if(new Date().getTime() <= whichTimeSended) {
			    neededRemindMap.put(actualCourse.getActualCourseId(), whichTimeSended);
			}
		}
		return neededRemindMap;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Page<ActualCourse> searchActualCourseFromIndex(
			ActualCourseSearchCondition acsc) throws ServerErrorException,
			DataWarningException {
		Page<ActualCourse> actualCoursePage = new Page<ActualCourse>();
        /*
         *  1. Judge whether this search need to be divided by status.
         *  2. If divided:
         *       queryPlanCourseFromIndexDividedByStatus();
         *       getNumByIndex();
         *     If not divided:
         *       queryPlanCourseFromIndexNotDevidedByStatus();
         *       getNumByIndex();
         *  3. Put the information into planCoursePage.
         *  4. Return the planCoursePage.
         *  
         */
        
        if (acsc == null) {
            // The plan course search condition is null.
            logger.error(LogConstants.objectIsNULLOrEmpty("plan course search condition"));
            throw new ServerErrorException(ErrorCodeConstants.SERVER_VALIDATION_ERROR);
        } else {
            // Define the parameters.
            List<ActualCourse> actualCourseList = null;
            Integer actualCourseRowCount = 0;
            
            Integer divideByStatus = acsc.isDivideStauts();
            if (divideByStatus == null || divideByStatus < 0) {
                logger.error(LogConstants.objectIsNULLOrEmpty("Search condition's divede by status"));
                throw new ServerErrorException(ErrorCodeConstants.SERVER_ERROR);
            } else {
                Map<String, Object> actualCourseAndRowCountMap = null;
                
                if (FlagConstants.ACTUAL_COURSE_SEARCH_STATUS_DIVIDED.equals(divideByStatus)) {
                    // This search will be divided by status.
                	acsc.setSortName(IndexFieldConstants.ACTUAL_COURSE_END_TIME);
                    actualCourseAndRowCountMap = getActualCourseListDividedByStatus(acsc);
                    actualCourseList = (List<ActualCourse>) actualCourseAndRowCountMap.get(JsonKeyConstants.ACTUAL_COURSE_LIST);
                    actualCourseRowCount = (Integer) actualCourseAndRowCountMap.get(JsonKeyConstants.ACTUAL_COURSE_ROW_COUNT);
                } else if (FlagConstants.ACTUAL_COURSE_SEARCH_STATUS_NOT_DIVIDED.equals(divideByStatus)) {
                    // This search will not be divided by status.
                    actualCourseAndRowCountMap = getActualCourseListNotDividedByStatus(acsc);
                    actualCourseList = (List<ActualCourse>) actualCourseAndRowCountMap.get(JsonKeyConstants.ACTUAL_COURSE_LIST);
                    actualCourseRowCount = (Integer) actualCourseAndRowCountMap.get(JsonKeyConstants.ACTUAL_COURSE_ROW_COUNT);
                } else {
                    // The parameter error.
                    logger.error(LogConstants.pureMessage("Search condition's divide status is error"));
                    throw new ServerErrorException(ErrorCodeConstants.SERVER_ERROR);
                }
            }
            // Put the information into plan course page.
            actualCoursePage.setNowPager(acsc.getNowPage());
            actualCoursePage.setPageSize(acsc.getPageSize());
            actualCoursePage.setList(actualCourseList);
            actualCoursePage.setTotalRecords(actualCourseRowCount);
        }
        return actualCoursePage;
	}
	
	@Override
	public void queryActualCourseInfo(Page<ActualCourse> actualCoursePage, ActualCourseSearchCondition actualCourseSearchCondition)
			throws ServerErrorException, DataWarningException {
		setStatusForActualCourse(actualCourseSearchCondition, actualCoursePage.getList());
	}
	/**
     * Get the divided plan course list and row count. This map has two entries,
     * they are planCourseList-List<PlanCourse> and planCourseRowCount-Integer.
     * 
     * @param planCourseSearchCondition
     * @return  The plan course and row count map.
     * @throws ServerErrorException 
     */
    @SuppressWarnings("unchecked")
    private Map<String, Object> getActualCourseListDividedByStatus(ActualCourseSearchCondition actualourseSearchCondition) throws ServerErrorException {
        Map<String, Object> actualCourseAndRowCountMap = new HashMap<String, Object>();
        Analyzer analyzer = new IKAnalyzer();
        
        Query query = dealWithKeywordQuery(analyzer, actualourseSearchCondition);
        
        Integer actualCourseRowCount = null;
        
        // Define temp plan course and session list.
        List tempActualCourseList = new ArrayList();
        
         //Get the filter for not completed and completed plan course and plan session.
        QueryWrapperFilter notCompletedPlanCourseFilter = this.dealWithNotCompletedCourseFilter(actualourseSearchCondition, analyzer);
        QueryWrapperFilter completedPlanCourseFilter = this.dealWithCompletedCourseFilter(actualourseSearchCondition, analyzer);
        
        // Get not completed plan course and plan session.
        List notCompletedList = null;
        int notCompletedRowCount = actualCourseDao.getNumByIndex(query, notCompletedPlanCourseFilter);
        
        // Define the completedList and completedRowCount.
        List completedList = null;
        int completedRowCount = actualCourseDao.getNumByIndex(query, completedPlanCourseFilter);
        
        int currentPageFirstResult = actualourseSearchCondition.getFirstResult();
        int pageSize = actualourseSearchCondition.getPageSize();
        //All planCourse/session of current page are not completed.
        if ((currentPageFirstResult + pageSize) <= notCompletedRowCount) {
        	notCompletedList = actualCourseDao.queryActualCourseFromIndex(query, actualourseSearchCondition.getSortField(), 
        			false, notCompletedPlanCourseFilter, currentPageFirstResult, pageSize);
        }
        //Part of planCourse/session of current page are not completed, but not enough for fill current page.
        if (currentPageFirstResult < notCompletedRowCount && notCompletedRowCount < (currentPageFirstResult + pageSize)) {
        	notCompletedList = actualCourseDao.queryActualCourseFromIndex(query, actualourseSearchCondition.getSortField(), 
        			false, notCompletedPlanCourseFilter,currentPageFirstResult, pageSize);
        	completedList = actualCourseDao.queryActualCourseFromIndex(query, actualourseSearchCondition.getSortField(), 
        			true, completedPlanCourseFilter, 0, currentPageFirstResult + pageSize - notCompletedRowCount);
        }
        //All planCourse/session of current page are completed.
        if (currentPageFirstResult >= notCompletedRowCount) {
        	completedList = actualCourseDao.queryActualCourseFromIndex(query, actualourseSearchCondition.getSortField(), 
        			true, completedPlanCourseFilter, currentPageFirstResult - notCompletedRowCount, pageSize);
        }
        if (null != notCompletedList) {
        	tempActualCourseList.addAll(notCompletedList);
        }
        if (null != completedList) {
        	tempActualCourseList.addAll(completedList);
        }
        
        // Add notCompletedRowCount and completedRowCount to planCourseRowCount.
        actualCourseRowCount = notCompletedRowCount + completedRowCount;
        
        actualCourseAndRowCountMap.put(JsonKeyConstants.ACTUAL_COURSE_LIST, tempActualCourseList);
        actualCourseAndRowCountMap.put(JsonKeyConstants.ACTUAL_COURSE_ROW_COUNT, actualCourseRowCount);
        
        return actualCourseAndRowCountMap;
    }
    
    /**
     * Get the not divided plan course list and row count. This map has two entries,
     * they are planCourseList-List<PlanCourse> and planCourseRowCount-Integer.
     * 
     * @param planCourseSearchCondition
     * @return  The plan course and row count map.
     * @throws ServerErrorException 
     */
    private Map<String, Object> getActualCourseListNotDividedByStatus(ActualCourseSearchCondition actualCourseSearchCondition) throws ServerErrorException {
        Map<String, Object> actualCourseAndRowCountMap = new HashMap<String, Object>();
        /**
         * 1. Parser keyword.
         * 2. Deal with filter.
         * 3. Get the plan course and plan session list.
         * 4. Get the row count.
         * 5. Transform the plan course and plan session list to plan course list.
         * 6. Deal with the plan course status.
         * 7. Deal with the plan course trainee number if the role is trainer.
         * 8. Make up the map and return it.
         * 
         */
        
        List<ActualCourse> actualCourseList = null;
        Integer actualCourseRowCount = null;
        
        // keyword query.
        Analyzer analyzer = new IKAnalyzer();
        Query keywordQuery = dealWithKeywordQuery(analyzer, actualCourseSearchCondition);
        
        // Filter
        QueryWrapperFilter notDividedFilter = dealWithNotDividedFilter(actualCourseSearchCondition, analyzer);
        
        // Plan course and session list, row count.
        actualCourseList = actualCourseDao.queryActualCourseFromIndex(keywordQuery, actualCourseSearchCondition.getSortField(), 
        		actualCourseSearchCondition.getReverse(), notDividedFilter, actualCourseSearchCondition.getFirstResult(), actualCourseSearchCondition.getPageSize());
        actualCourseRowCount = actualCourseDao.getNumByIndex(keywordQuery, notDividedFilter);
        
        actualCourseAndRowCountMap.put(JsonKeyConstants.ACTUAL_COURSE_LIST, actualCourseList);
        actualCourseAndRowCountMap.put(JsonKeyConstants.ACTUAL_COURSE_ROW_COUNT, actualCourseRowCount);
        
        return actualCourseAndRowCountMap;
    }
    
    /**
     * Deal with the keyword query.
     * 
     * @param analyzer  The analyzer for query.
     * @param planCourseSearchCondition
     * @return  The keyword query.
     * @throws ServerErrorException  When parse keyword error.
     */
    private Query dealWithKeywordQuery(Analyzer analyzer, ActualCourseSearchCondition actualCourseSearchCondition) throws ServerErrorException {
        // Deal with keyword.
        String keyword = dealWithKeyWord(actualCourseSearchCondition.getQueryString());
        
        // Deal with keyword query parser.
        QueryParser queryParser = new MultiFieldQueryParser(Version.LUCENE_30, actualCourseSearchCondition.getSearchFields(), analyzer);
        queryParser.setAllowLeadingWildcard(true);
        
        Query query = null;
        try {
            query = queryParser.parse(keyword);
        } catch (ParseException e) {
            logger.error(LogConstants.exceptionMessage("Parsing keyword"), e);
            throw new ServerErrorException(ErrorCodeConstants.SERVER_ERROR);
        }
        return query;
    }
    
    /**
     * Deal with the key word. If the keyword is null or keyword is empty,
     * make the keyword become "*".
     * 
     * @param keyword
     * @return
     */
    private String dealWithKeyWord(String keyword) {
        // Deal with keyword.
       List<String> luceneSpecialOperator = Arrays.asList(IndexFieldConstants.LUCENE_OPEERATOR);
       if (keyword != null) {
           keyword = keyword.trim();
       }
       if (keyword == null || keyword.length() == 0
               || luceneSpecialOperator.contains(keyword)) {
           keyword = "*";
       }
       return keyword;
   }
    
    /**
     * Deal with the filter which is used to search the not completed plan course
     * and plan session.
     * 
     * @param planCourseSearchCondition  The search condition.
     * @param analyzer  The analyzer for search.
     * @return  The filter for not completed plan course.
     * @throws ServerErrorException 
     */
    private QueryWrapperFilter dealWithNotCompletedCourseFilter(ActualCourseSearchCondition actualCourseSearchCondition, Analyzer analyzer) throws ServerErrorException {
        QueryWrapperFilter filter = null;
        /*
         *  1. The trainee contains employeeName.
         *  2. The end time is between now time and the max time.
         */
        BooleanQuery booleanQuery = new BooleanQuery();
        // Role
        Query roleQuery = dealWithQueryForRole(actualCourseSearchCondition, analyzer);
        if (roleQuery != null) {
            booleanQuery.add(roleQuery, Occur.MUST);
        }
        // Plan is published
        String publishField = IndexFieldConstants.ACTUAL_COURSE_PLAN_PREFIX + IndexFieldConstants.PLAN_IS_PUBLISHED;
        QueryParser publishQueryParser = new QueryParser(Version.LUCENE_30, publishField, analyzer);
        String cancelField = IndexFieldConstants.ACTUAL_COURSE_PLAN_PREFIX + IndexFieldConstants.PLAN_IS_CANCELED;
        String deleteField = IndexFieldConstants.ACTUAL_COURSE_PLAN_PREFIX + IndexFieldConstants.PLAN_IS_DELETED;
        QueryParser cancelQueryParser = new QueryParser(Version.LUCENE_30, cancelField, analyzer);
        QueryParser deleteQueryParser = new QueryParser(Version.LUCENE_30, deleteField, analyzer);
        Query publishQuery = null;
        Query cancelQuery = null;
        Query deleteQuery = null;
        try {
			publishQuery = publishQueryParser.parse(FlagConstants.IS_PUBLISHED + "");
			cancelQuery = cancelQueryParser.parse(FlagConstants.UN_CANCELED + "");
			deleteQuery = deleteQueryParser.parse(FlagConstants.UN_DELETED + "");
		} catch (ParseException e) {
			logger.error(e);
			throw new ServerErrorException(e);
		}
		booleanQuery.add(publishQuery, Occur.MUST);
		booleanQuery.add(cancelQuery, Occur.MUST);
		booleanQuery.add(deleteQuery, Occur.MUST);
        // End time.
        Query endTimeQuery = new TermRangeQuery("end_datetime", DateHandlerUtils.dateToString(DateFormatConstants.FULL_TIME_STR, new Date()), 
                "99999999", false, true);
        booleanQuery.add(endTimeQuery, Occur.MUST);
        
        filter = new QueryWrapperFilter(booleanQuery);
        
        return filter;
    }
    
    /**
     * Deal with query for role.
     * 
     * @param planCourseSearchCondition
     * @param analyzer
     * @return
     * @throws ServerErrorException
     */
    private Query dealWithQueryForRole(ActualCourseSearchCondition actualCourseSearchCondition, Analyzer analyzer) throws ServerErrorException {
        Query query = null;
        String indexFiled = "";
        String employeeName = actualCourseSearchCondition.getEmployeeName();
        if (actualCourseSearchCondition != null) {
            if (RoleNameConstants.TRAINEE.equals(actualCourseSearchCondition.getRoleFlag())) {
                // Trainee
            	indexFiled = IndexFieldConstants.ACTUAL_COURSE_TRAINEE_PREFIX + IndexFieldConstants.EMPLOYEE_NAME;
                QueryParser traineeQueryParser = new QueryParser(Version.LUCENE_30, indexFiled, analyzer);
                try {
                    query = traineeQueryParser.parse(StringHandlerUtils.bulidPhraseQueryString(indexFiled, employeeName));
                } catch (ParseException e) {
                    logger.error(LogConstants.exceptionMessage("Parsing employee name for trainee"), e);
                    throw new ServerErrorException(ErrorCodeConstants.SERVER_ERROR);
                }
            } else if (RoleNameConstants.TRAINER.equals(actualCourseSearchCondition.getRoleFlag())) {
                // Trainer
            	indexFiled = IndexFieldConstants.ACTUAL_COURSE_TRAINER;
                QueryParser trainerQueryParser = new QueryParser(Version.LUCENE_30, indexFiled, analyzer);
                try {
                    query = trainerQueryParser.parse(StringHandlerUtils.bulidPhraseQueryString(indexFiled, employeeName));
                } catch (ParseException e) {
             	   logger.error(LogConstants.exceptionMessage("Parsing employee name for trainer"), e);
                    throw new ServerErrorException(ErrorCodeConstants.SERVER_ERROR);
                }
            }
        }
        return query;
     }
    
    /**
     * Deal with the filter which is used to search the completed plan course
     * and plan session.
     * 
     * 
     * @param planCourseSearchCondition  The search condition.
     * @param analyzer  The analyzer for search.
     * @return  The filter for completed plan course.
     * @throws ServerErrorException 
     */
    private QueryWrapperFilter dealWithCompletedCourseFilter(ActualCourseSearchCondition actualCourseSearchCondition, 
            Analyzer analyzer) throws ServerErrorException {
        QueryWrapperFilter filter = null;
        /*
         *  1. The trainer is employeeName.
         *  2. The end time is between the early time and now time. 
         */
        BooleanQuery booleanQuery = new BooleanQuery();
        
        // Role
        Query roleQuery = dealWithQueryForRole(actualCourseSearchCondition, analyzer);
        if (roleQuery != null) {
            booleanQuery.add(roleQuery, Occur.MUST);
        }
        //not canceled and not deleted
        String cancelField = IndexFieldConstants.ACTUAL_COURSE_PLAN_PREFIX + IndexFieldConstants.PLAN_IS_CANCELED;
        String deleteField = IndexFieldConstants.ACTUAL_COURSE_PLAN_PREFIX + IndexFieldConstants.PLAN_IS_DELETED;
        QueryParser cancelQueryParser = new QueryParser(Version.LUCENE_30, cancelField, analyzer);
        QueryParser deleteQueryParser = new QueryParser(Version.LUCENE_30, deleteField, analyzer);
        Query cancelQuery = null;
        Query deleteQuery = null;
        try {
			cancelQuery = cancelQueryParser.parse(FlagConstants.UN_CANCELED + "");
			deleteQuery = deleteQueryParser.parse(FlagConstants.UN_DELETED + "");
		} catch (ParseException e) {
			logger.error(e);
			throw new ServerErrorException(e);
		}
		booleanQuery.add(cancelQuery, Occur.MUST);
		booleanQuery.add(deleteQuery, Occur.MUST);
        // End time.
        Query endTimeQuery = new TermRangeQuery("end_datetime", "00000000", DateHandlerUtils.dateToString(DateFormatConstants.FULL_TIME_STR, new Date()), false, true);
        booleanQuery.add(endTimeQuery, Occur.MUST);
        
        filter = new QueryWrapperFilter(booleanQuery);
        
        return filter;
    }
    
    /**
     * Deal with not divided filter.
     * 
     * @param planCourseSearchCondition
     * @param analyzer
     * @return
     * @throws ServerErrorException
     */
    private QueryWrapperFilter dealWithNotDividedFilter(ActualCourseSearchCondition actualCourseSearchCondition, Analyzer analyzer) throws ServerErrorException {
        QueryWrapperFilter notDividedFilter = null;
        
        /*
         *  1. The role query.
         *  TODO Other queries.
         */
        BooleanQuery booleanQuery = new BooleanQuery();
        // Identity whether the boolean query has query.
        boolean flag = false;
        
        // Role query.
        Query roleQuery = dealWithQueryForRole(actualCourseSearchCondition, analyzer);
        if (roleQuery != null) {
            booleanQuery.add(roleQuery, Occur.MUST);
            flag = true;
        }
        
        //not canceled and not deleted
        String cancelField = IndexFieldConstants.ACTUAL_COURSE_PLAN_PREFIX + IndexFieldConstants.PLAN_IS_CANCELED;
        String deleteField = IndexFieldConstants.ACTUAL_COURSE_PLAN_PREFIX + IndexFieldConstants.PLAN_IS_DELETED;
        QueryParser cancelQueryParser = new QueryParser(Version.LUCENE_30, cancelField, analyzer);
        QueryParser deleteQueryParser = new QueryParser(Version.LUCENE_30, deleteField, analyzer);
        Query cancelQuery = null;
        Query deleteQuery = null;
        try {
			cancelQuery = cancelQueryParser.parse(FlagConstants.UN_CANCELED + "");
			deleteQuery = deleteQueryParser.parse(FlagConstants.UN_DELETED + "");
		} catch (ParseException e) {
			logger.error(e);
			throw new ServerErrorException(e);
		}
		booleanQuery.add(cancelQuery, Occur.MUST);
		booleanQuery.add(deleteQuery, Occur.MUST);
        
        if (flag) {
            notDividedFilter = new QueryWrapperFilter(booleanQuery);
        }
        
        return notDividedFilter;
    }
    
    private void setStatusForActualCourse(ActualCourseSearchCondition actualCourseSearchCondition, 
            List<ActualCourse> actualCourseList) throws ServerErrorException{
        if(null == actualCourseList){
            return ;
        }
        Date currentDate = new Date();
        for(ActualCourse actualCourse : actualCourseList){
        	Date endTime = actualCourse.getCourseEndTime();
            //the planCourse is not over
            if((endTime ==  null) || (endTime != null && endTime.after(currentDate))){
            	actualCourse.setStatus(FlagConstants.STATUS_GREEN);
            	continue;
            }
            if (DateHandlerUtils.getDaysBetweenDate(endTime, currentDate) > DateFormatConstants.EXPIRED_DEFAULT_DAY) {
            	actualCourse.setStatus(FlagConstants.STATUS_GRAY);
            	continue;
            }
            //the planCourse is over but not expired
            if(endTime.before(currentDate)){
                //the employee is the trainer of the planCourse
                if(actualCourseSearchCondition.getRoleFlag().equals(RoleNameConstants.TRAINER)){
                    //the planCourse does not need been assessed
                    if( actualCourse.getCourseInfo() == null ||
                    	actualCourse.getCourseInfo().getTrainer2Trainee() == FlagConstants.UN_NEED_ASSESSMENT){
                    	actualCourse.setStatus(FlagConstants.STATUS_GRAY);
                    	continue;
                    }
                    //the planCourse need been assessed
                    if(actualCourse.getCourseInfo().getTrainer2Trainee() == FlagConstants.NEED_ASSESSMENT){
                            Employee employee = employeeDao.findEmployeeByName(actualCourseSearchCondition.getEmployeeName());
                            Map<String,Integer> assessmentFields = new HashMap<String,Integer>();
                            assessmentFields.put(JsonKeyConstants.TRAINER_ID, employee.getEmployeeId());
                            assessmentFields.put(JsonKeyConstants.PLAN_COURSE_ID, actualCourse.getActualCourseId());
                            List<Integer> assessmentTypes = new ArrayList<Integer>();
                            assessmentTypes.add(FlagConstants.TRAINER_TO_TRAINEES);
                            List<Assessment> assessments = null;
                            try{
                                assessments = assessmentDao.getAssessmentByFields(assessmentFields, assessmentTypes);
                            }catch(Exception e){
                            	logger.error(LogConstants.exceptionMessage("Get assessment by field:"), e);
                    			throw new ServerErrorException(ErrorCodeConstants.SERVER_ERROR);
                            }
                          //the planCourse has not been assessed
                          if(!courseHasAssessedByTrainer(assessments)){
                        	  actualCourse.setStatus(FlagConstants.STATUS_YELLOW);
                          //the planCourse has been assessed
                          } else {
                              actualCourse.setStatus(FlagConstants.STATUS_GRAY);
                          }
                    }
                }
                //the employee is the trainee of the planCourse
                if(actualCourseSearchCondition.getRoleFlag().equals(RoleNameConstants.TRAINEE)){
                    //check whether the planCourse need been assessed by trainee
                    if(actualCourse.getCourseInfo() == null ||
                       actualCourse.getCourseInfo().getTrainee2Trainer() == FlagConstants.UN_NEED_ASSESSMENT){
                    	actualCourse.setStatus(FlagConstants.STATUS_GRAY);
                    	continue;
                    }
                    if(actualCourse.getCourseInfo().getTrainee2Trainer() == FlagConstants.NEED_ASSESSMENT){
                        Employee employee = null;
						try {
							employee = employeeDao.findEmployeeByName(actualCourseSearchCondition.getEmployeeName());
						} catch (Exception e) {
							logger.error(LogConstants.exceptionMessage("Get employee by name:"), e);
                			throw new ServerErrorException(ErrorCodeConstants.SERVER_ERROR);
						}
                        Map<String,Integer> assessmentFields = new HashMap<String,Integer>();
                        assessmentFields.put(JsonKeyConstants.TRAINEE_ID, employee.getEmployeeId());
                        assessmentFields.put(JsonKeyConstants.PLAN_COURSE_ID, actualCourse.getActualCourseId());
                        List<Integer> assessmentTypes = new ArrayList<Integer>();
                        assessmentTypes.add(FlagConstants.TRAINEE_TO_COURSE);
                        List<Assessment> assessments = null;
                        try{
                            assessments = assessmentDao.getAssessmentByFields(assessmentFields, assessmentTypes);
                        }catch(Exception e){
                        	logger.error(LogConstants.exceptionMessage("Get assessment by field:"), e);
                			throw new ServerErrorException(ErrorCodeConstants.SERVER_ERROR);
                        }
                      //the planCourse has not been assessed
                      if(assessments != null && assessments.size()==1){
                    	  Assessment assessmentToCourse = assessments.get(0);
                    	  if (assessmentToCourse.getIsIgnore() == FlagConstants.IS_IGNORE || 
                    			  assessmentToCourse.getHasBeenAssessed() == FlagConstants.ASSESSMENT_HAS_BEEN_ASSESSED) {
                    		  actualCourse.setStatus(FlagConstants.STATUS_GRAY);
                    	  } else {
                    		  actualCourse.setStatus(FlagConstants.STATUS_YELLOW);
                    	  }
                      } else {
                    	  actualCourse.setStatus(FlagConstants.STATUS_YELLOW);
                      }
                    }
                }
            }
        }
    }
    
    private boolean courseHasAssessedByTrainer(List<Assessment> assessments){
    	if (assessments == null || assessments.isEmpty()) {
    		return false;
    	}
    	for (Assessment assessment : assessments) {
    		if (assessment.getIsIgnore() == FlagConstants.IS_IGNORE) {
    			return true;
    		}
    		if (assessment.getHasBeenAssessed() == FlagConstants.ASSESSMENT_UNASSESSED) {
    			return false;
    		}
    	}
    	return true;
    } 
    
	@Override
	public String getActualCoursesInOnePlanByTrainer(String trainerName,
			int planId) throws ServerErrorException{
		List<ActualCourse> actualCourses = null;
		try {
			actualCourses = actualCourseDao.getActualCoursesInOnePlanByTrainer(trainerName, planId);
		} catch (Exception e) {
			logger.error(LogConstants.exceptionMessage("Get actual courses from a plan by trainer:"), e);
			throw new ServerErrorException(ErrorCodeConstants.SERVER_ERROR);
		}
		StringBuilder builer = new StringBuilder();
		if (actualCourses != null && !(actualCourses.isEmpty())){
			for (ActualCourse actualCourse : actualCourses) {
				builer.append("<br>");
				builer.append(actualCourse.getCourseName());
			}
		}
		return builer.toString();
	}

	@Override
	public void updateActualCourse(ActualCourse actualCourse) throws ServerErrorException{
		try{
			actualCourseDao.mergeObject(actualCourse);
		} catch(Exception e) {
			logger.error(LogConstants.exceptionMessage("update actualCourse "), e);
			throw new ServerErrorException(ErrorCodeConstants.SERVER_ERROR);
		}
	}

}
