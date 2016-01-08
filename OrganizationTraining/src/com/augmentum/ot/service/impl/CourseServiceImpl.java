package com.augmentum.ot.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.queryParser.MultiFieldQueryParser;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.QueryWrapperFilter;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.util.Version;
import org.springframework.stereotype.Component;
import org.wltea.analyzer.lucene.IKAnalyzer;

import com.augmentum.ot.dao.ActualCourseDao;
import com.augmentum.ot.dao.CourseAttachmentDao;
import com.augmentum.ot.dao.CourseDao;
import com.augmentum.ot.dao.EmployeeDao;
import com.augmentum.ot.dataObject.CourseSearchCondition;
import com.augmentum.ot.dataObject.FromSearchToViewCondition;
import com.augmentum.ot.dataObject.Page;
import com.augmentum.ot.dataObject.constant.ErrorCodeConstants;
import com.augmentum.ot.dataObject.constant.FlagConstants;
import com.augmentum.ot.dataObject.constant.IndexFieldConstants;
import com.augmentum.ot.dataObject.constant.LogConstants;
import com.augmentum.ot.exception.DataWarningException;
import com.augmentum.ot.exception.ServerErrorException;
import com.augmentum.ot.model.ActualCourse;
import com.augmentum.ot.model.Course;
import com.augmentum.ot.model.CourseAttachment;
import com.augmentum.ot.model.Employee;
import com.augmentum.ot.model.Plan;
import com.augmentum.ot.service.CourseService;
import com.augmentum.ot.util.BeanFactory;

/**
 * 
 * @version 0.1, 07/16/2012
 */
@Component("courseService")
public class CourseServiceImpl implements CourseService {

    private static Logger logger = Logger.getLogger(CourseServiceImpl.class);
    @Resource(name = "courseDao")
    private CourseDao courseDao;

    @Resource(name = "employeeDao")
    private EmployeeDao employeeDao;

    @Resource(name = "actualCourseDao")
    private ActualCourseDao actualCourseDao;
    
    public void setActualCourseDao(ActualCourseDao actualCourseDao) {
		this.actualCourseDao = actualCourseDao;
	}

    public CourseDao getCourseDao() {
        return courseDao;
    }

    public void setCourseDao(CourseDao courseDao) {
        this.courseDao = courseDao;
    }

    public void setEmployeeDao(EmployeeDao employeeDao) {
        this.employeeDao = employeeDao;
    }

    /**
     * create all course indexes
     * 
     * @throws CourseServiceException
     */
    @Override
    public void createAllCourseIndexes() throws ServerErrorException {
        String hql = "from Course course";
        try {
        	courseDao.rebuildAllEntitiesIndexes(hql);
        }catch(Exception e) {
        	logger.error(LogConstants.exceptionMessage("Create course indexes"), e);
			throw new ServerErrorException(ErrorCodeConstants.SERVER_ERROR);
        }
    }

    /**
     * search course from index by keyword, type and so on.
     * 
     * @param CourseSearchCondition
     *            csc
     * @return Page<Course>
     * @throws CourseServiceException
     */
    @Override
    public Page<Course> searchCoursesFromIndex(CourseSearchCondition csc)
            throws ServerErrorException {
    	logger.debug(LogConstants.getDebugInput("Course search condition", csc));
        Analyzer analyzer = new IKAnalyzer();
        List<Course> courses = new ArrayList<Course>();
        Page<Course> page = new Page<Course>();

        if (null != csc) {
            csc.setQueryString(csc.getQueryString().trim());
            List<String> luceneSpecialOperator = Arrays.asList(IndexFieldConstants.LUCENE_OPEERATOR);
            if (null == csc.getQueryString() || csc.getQueryString().equals("") 
                    || luceneSpecialOperator.contains(csc.getQueryString().trim())) {
                csc.setQueryString("*");
            }
            // parse queryString to Query type.
            QueryParser queryParser = new MultiFieldQueryParser(
			        Version.LUCENE_30, csc.getSearchFields(), analyzer, csc.getBoosts());

            // * or ? are allowed as the first character of a PrefixQuery and
            // WildcardQuery.
            queryParser.setAllowLeadingWildcard(true);
            Boolean flag = false;
            // booleanQuery as a filter
            BooleanQuery booleanQuery = new BooleanQuery();
            
            // Query existQuery = new TermQuery(new
            // Term(IndexFieldConstants.COURSE_IS_DELETED,
            // FlagConstants.UNDELETED + ""));
            Query query = null;
            try { 
                query = queryParser.parse(csc.getQueryString());
                
                if (csc.getTypeIds() != null && !csc.getTypeIds().isEmpty()) {
                    QueryParser typeQueryParser = new QueryParser(
                            Version.LUCENE_30,
                            IndexFieldConstants.PREFIX_COURSE_TYPE
                                    + IndexFieldConstants.TYPE_ID, analyzer);
                    Query typeQuery = null;
                    typeQuery = typeQueryParser.parse(csc.getTypeIds().trim());
                    booleanQuery.add(typeQuery, Occur.MUST);
                    flag = true;
                }
                if (csc.getIsCertificateds() != null
                        && !csc.getIsCertificateds().isEmpty()) {
                    QueryParser certificatedQueryParser = new QueryParser(
                            Version.LUCENE_30,
                            IndexFieldConstants.COURSE_IS_CERTIFICATED,
                            analyzer);
                    Query certificateedQuery = null;
                    certificateedQuery = certificatedQueryParser.parse(csc
                            .getIsCertificateds().trim());
                    booleanQuery.add(certificateedQuery, Occur.MUST);
                    flag = true;
                }
                // undeleted course
                QueryParser deleteQueryParser = new QueryParser(
                        Version.LUCENE_30,
                        IndexFieldConstants.COURSE_IS_DELETED, analyzer);
                Query deleteQuery = deleteQueryParser.parse(""
                        + FlagConstants.UN_DELETED);
                booleanQuery.add(deleteQuery, Occur.MUST);
                flag = true;
            } catch (ParseException e) {
                logger.error(LogConstants.exceptionMessage("Parse query"), e);
                throw new ServerErrorException(ErrorCodeConstants.SERVER_ERROR);
            }
            QueryWrapperFilter filter = null;
            if (flag) {
                filter = new QueryWrapperFilter(booleanQuery);
            }
            int totalRecords = courseDao.queryTotalRecordsFromIndex(query,
                    filter);
            try {
            	String sortField = csc.getSortField();
            	if (null == csc.getSortField() || csc.getSortField().equals("null") || "".equals(csc.getSortField())) {
            		sortField = null;
            	}
            	if (sortField == null && csc.getQueryString().equals("*")) {
            		sortField = IndexFieldConstants.PREFIX_ID;
            	}
            	courses = courseDao.queryObjectFromIndex(query, sortField,
                        csc.getReverse(), filter, csc.getFirstResult(), csc
                                .getPageSize());
            } catch (Exception e) {
            	logger.error(LogConstants.exceptionMessage("Query Object from index"), e);
            	throw new ServerErrorException(ErrorCodeConstants.SERVER_ERROR);
			}
            
            page.setNowPager(csc.getNowPage());
            page.setPageSize(csc.getPageSize());
            page.setTotalRecords(totalRecords);
            page.setList(courses);
        }
        logger.debug(LogConstants.getDebugOutput("Search courses", page));
        return page;
    }

    @Override
    public Boolean deleteCourseById(Integer id) throws DataWarningException,
            ServerErrorException {
        if (id == null) {
            logger.warn("errorCodes.getErrorCode(Wxxx).getMessage()");
            throw new DataWarningException(ErrorCodeConstants.DELETE_COURSE_DELETED);
        }
        logger.debug("input: id " + id);
        Course course;
        try {
            course = courseDao.findByPrimaryKey(id);
        } catch (Exception e) {
        	logger.error(LogConstants.exceptionMessage("Delete course"), e);
            throw new ServerErrorException(ErrorCodeConstants.SERVER_ERROR);
        }
        if (course == null) {
            return false;
        }
        // delete course
        course.setCourseIsDeleted(FlagConstants.IS_DELETED);
        course.setCourseLastUpdateDate(new Date());
        return true;
    }

    @Override
    public Course findCourseById(Integer id) throws DataWarningException,
            ServerErrorException {
        logger.debug(LogConstants.getDebugInput("Find course by id", id));
        Course course;
        try {
            course = courseDao.findByPrimaryKey(id);
        } catch (Exception e) {
            logger.error(e);
            throw new ServerErrorException(e);
        }
        if (course == null || course.getCourseIsDeleted() == FlagConstants.IS_DELETED) {
        	logger.warn(LogConstants.getLogCourseByErrorCode(ErrorCodeConstants.VIEW_COURSE_DELETED, id));
            throw new DataWarningException(ErrorCodeConstants.VIEW_COURSE_DELETED);
        }
        logger.debug(LogConstants.getDebugOutput("Find a course", course));
        return course;
    }

    @Override
    public Course getCourseById(Integer id) throws DataWarningException,
            ServerErrorException {
        logger.debug(LogConstants.getDebugInput("Get course by id", id));
        Course course;
        try {
            course = courseDao.findByPrimaryKey(id);
        } catch (Exception e) {
            logger.error(e);
            throw new ServerErrorException(e);
        }
        if (course == null || course.getCourseIsDeleted() == FlagConstants.IS_DELETED) {
        	logger.warn(LogConstants.getLogCourseByErrorCode(ErrorCodeConstants.VIEW_COURSE_DELETED, id));
            throw new DataWarningException(ErrorCodeConstants.VIEW_COURSE_DELETED);
        }
        // get attachments
        List<CourseAttachment> attachmentList = new ArrayList<CourseAttachment>();
        if (course.getCourseHasAttachment() == FlagConstants.HAS_ATTACHMENT) {
        	if (null == course.getCourseAttachments() || course.getCourseAttachments().size() <= 0) {
            	logger.warn(LogConstants.getLogCourseByErrorCode(
            				ErrorCodeConstants.VIEW_COURSE_DELETED_NOT_ATTACHMENT, id));
            	throw new DataWarningException(ErrorCodeConstants.VIEW_COURSE_DELETED_NOT_ATTACHMENT);
        	}
        	List<CourseAttachment> attachmentAll = course.getCourseAttachments();
        	for (CourseAttachment courseAttachment:attachmentAll) {
    			if (courseAttachment.getCourseAttachmentIsDeleted() == FlagConstants.UN_DELETED) {
    				attachmentList.add(courseAttachment);
    			}
    		}
        }
        course.setCourseAttachments(attachmentList);
        logger.debug(LogConstants.getDebugOutput("Get a course", course));
        return course;
    }

    @Override
    public Integer createCourse(Course course, List<CourseAttachment> courseAttachments, HttpServletRequest request) throws ServerErrorException, DataWarningException {
        try {
            // deal with attachment
            CourseAttachmentDao caDAO = BeanFactory.getCourseAttachmentDao();
    		if (null != courseAttachments && courseAttachments.size() > 0) {
    			course.setCourseHasAttachment(FlagConstants.HAS_ATTACHMENT);
    		} else {
    			course.setCourseHasAttachment(FlagConstants.NO_ATTACHMENT);
    		}
    		// save course, return courseId
            courseDao.saveObject(course);
            if (null != courseAttachments && courseAttachments.size() > 0) {
	            for (CourseAttachment courseAttachment : courseAttachments) {
					courseAttachment.setCourseAttach(course);
					caDAO.saveOrUpdateObject(courseAttachment);
				}
            }
        }catch(Exception e){
            logger.error(LogConstants.exceptionMessage("Save a course"), e);
            throw new ServerErrorException(ErrorCodeConstants.SERVER_ERROR);
        }
        // save course for the author
        String courseAuthorNames = course.getCourseAuthorName();
        if (courseAuthorNames != null && !(courseAuthorNames.equals(""))) {
        	judgeAuthor(courseAuthorNames, request);
        }
        logger.debug(LogConstants.getDebugOutput("Create a course", course.getCourseId()));
        return course.getCourseId();
    }
    
    /**
     * judge author, if author isn't in employee table, save it
     * @param courseAuthorNames
     * @param request
     * @throws ServerErrorException
     * @throws DataWarningException
     */
    private void judgeAuthor(String courseAuthorNames, HttpServletRequest request) throws ServerErrorException, DataWarningException {
    	String[] authorNames = courseAuthorNames.split(";");
    	Employee employee = new Employee();
        for (int i = 0; i < authorNames.length; i++) {
            try{
                employee = employeeDao.findEmployeeByName(authorNames[i]);
            } catch (Exception e) {
                logger.error(e);
                throw new ServerErrorException(e);
            }
            if (employee == null) {
                //just for package1.0
                throw new ServerErrorException(ErrorCodeConstants.SERVER_ERROR);
            	//IRemoteService remoteService = BeanFactory.getIRemoteService();
            	//Employee employeeFromIAP = remoteService.findEmployeeByName(authorNames[i], RoleNameConstants.ADMIN, request);
//                Employee employeeFromIAP = iRemoteService.findEmployeeByName(authorNames[i], RoleNameConstants.ADMIN, request);
//                employeeDao.saveObject(employeeFromIAP);
//                Role role=roleDao.getRoleByRoleName(RoleNameConstants.TRAINER);
//    			List<EmployeeRoleLevelMap> list = new ArrayList<EmployeeRoleLevelMap>();
//                EmployeeRoleLevelMap employeeRoleLevelMap=new EmployeeRoleLevelMap();
//                employeeRoleLevelMap.setEmployee(employeeFromIAP);
//                employeeRoleLevelMap.setRole(role);
//                list.add(employeeRoleLevelMap);
//                employeeFromIAP.setRoleLevelsForEmployee(list);
//                employeeRoleLevelMapDao.saveObject(employeeRoleLevelMap);
            }
        }
    }

    @Override
    public Course updateCourse(Course course, List<CourseAttachment> courseAttachments, HttpServletRequest request) throws DataWarningException, ServerErrorException{
        if (course == null) {
            return null;
        }
        Course realCourse = new Course();
        try {
        	realCourse = courseDao.findByPrimaryKey(course.getCourseId());
        } catch (Exception e) {
			logger.error(e);
			throw new ServerErrorException(e);
		}
        if (null == realCourse || realCourse.getCourseIsDeleted() == FlagConstants.IS_DELETED) {
        	logger.warn(LogConstants.getLogCourseByErrorCode(
        			ErrorCodeConstants.EDIT_COURSE_DELETED, course.getCourseId()));
        	throw new DataWarningException(ErrorCodeConstants.EDIT_COURSE_DELETED);
        }
        realCourse.setCourseName(course.getCourseName());
        realCourse.setCourseBrief(course.getCourseBrief());
        realCourse.setCourseBriefWithoutTag(course.getCourseBriefWithoutTag());
        realCourse.setCourseTargetTrainee(course.getCourseTargetTrainee());
        realCourse.setCourseDuration(course.getCourseDuration());
        realCourse.setCourseType(course.getCourseType());
        realCourse.setCourseAuthorName(course.getCourseAuthorName());
        realCourse.setCourseLastUpdateDate(new Date());
        realCourse.setCourseCategoryTag(course.getCourseCategoryTag());
        realCourse.setCourseHasAttachment(course.getCourseHasAttachment());
        
    	// deal with attachment
        CourseAttachmentDao caDAO = BeanFactory.getCourseAttachmentDao();
		if (null != courseAttachments && courseAttachments.size() > 0) {
			realCourse.setCourseHasAttachment(FlagConstants.HAS_ATTACHMENT);
			for (CourseAttachment courseAttachment : realCourse.getCourseAttachments()) {
                if (!courseAttachments.contains(courseAttachment)) {
                    CourseAttachment cAttachment = caDAO.findByPrimaryKey(courseAttachment.getCourseAttachmentId());
                    cAttachment.setCourseAttachmentIsDeleted(FlagConstants.IS_DELETED);
                    caDAO.updateObject(cAttachment);
                }
            }
			for (CourseAttachment courseAttachment : courseAttachments) {
				if (courseAttachment.getCourseAttach() == null) {
					int nowIsVisible = courseAttachment.getCourseAttachmentvisible();
					courseAttachment = caDAO.findByPrimaryKey(courseAttachment.getCourseAttachmentId());
					courseAttachment.setCourseAttachmentvisible(nowIsVisible);
				}
				if (!(realCourse.getCourseAttachments().contains(courseAttachment))) {
					courseAttachment.setCourseAttach(realCourse);
				}
				caDAO.saveOrUpdateObject(courseAttachment);
			}
		} else {
			realCourse.setCourseHasAttachment(FlagConstants.NO_ATTACHMENT);
			if (realCourse.getCourseAttachments() != null) {
				for (CourseAttachment courseAttachment : realCourse.getCourseAttachments()) {
					courseAttachment.setCourseAttachmentIsDeleted(FlagConstants.IS_DELETED);
                    caDAO.updateObject(courseAttachment);
				}
			}
		}
		
        courseDao.updateObject(realCourse);
		// save course for the author
		String courseAuthorNames = course.getCourseAuthorName();
		if (courseAuthorNames != null && !(courseAuthorNames.equals(""))) {
			judgeAuthor(courseAuthorNames, request);
		}
        return realCourse;
    }

    @Override
    public List<Integer> searchIdByConditionInIndex(CourseSearchCondition csc) throws ServerErrorException {
        String[] searchFieldsStore = csc.getSearchFields();
        String queryStringStore = csc.getQueryString();
        List<Integer> ids = null;
        if (csc != null) {
            csc.setSearchFields(new String[] { IndexFieldConstants.COURSE_ID });
        }
        Page<Course> coursePage = null;
        try {
            coursePage = searchCoursesFromIndex(csc);
        } catch (ServerErrorException e) {
            logger.error(LogConstants.exceptionMessage("Search Course From Index"), e);
            throw new ServerErrorException(ErrorCodeConstants.SERVER_ERROR);
        }
        List<Course> courses = coursePage.getList();
        if (courses != null && courses.size() > 0) {
            ids = new ArrayList<Integer>();
            for (Course course : courses) {
                ids.add(course.getCourseId());
            }
        }
        csc.setQueryString(queryStringStore);
        csc.setSearchFields(searchFieldsStore);
        return ids;
    }

    @Override
    public String plusRecordId(FromSearchToViewCondition fstvc,
            CourseSearchCondition csc) {
        String error = "";
        if (fstvc == null || fstvc.getBackupId() == null
                || fstvc.getBackupId().equals("")) {
            error = "conditionError";
            return error;
        }
        String[] backupIds = fstvc.getBackupId().split(",");
        int i = 0;
        for (; i < backupIds.length; i++) {
            if (Integer.parseInt(backupIds[i]) == fstvc.getNowId()) {
                break;
            }
        }
        if (i == backupIds.length) {
            error = "conditionError";
            return error;
        }
        i++;
        if (i == backupIds.length) {
            int pageNum = Integer.parseInt(csc.getPageNum());
            pageNum += 1;
            csc.setPageNum("" + pageNum);
            List<Integer> ids = null;
			try {
				ids = this.searchIdByConditionInIndex(csc);
			} catch (ServerErrorException e) {
				logger.error(LogConstants.exceptionMessage("Search course"), e);
			}
            if (ids == null || ids.size() == 0) {
                error = "dataError";
                return error;
            }
            String newbackupId = "" + ids.get(0);
            for (int j = 1; j < ids.size(); j++) {
                newbackupId = newbackupId + "," + ids.get(j);
            }
            fstvc.setBackupId(newbackupId);
            fstvc.setNowId(ids.get(0));
        } else {
            fstvc.setNowId(Integer.parseInt(backupIds[i]));
        }
        return error;
    }

    @Override
    public String subtractRecordId(FromSearchToViewCondition fstvc,
            CourseSearchCondition csc) {
        String error = "";
        if (fstvc == null || fstvc.getBackupId() == null
                || fstvc.getBackupId().equals("")) {
            error = "conditionError";
            return error;
        }
        String[] backupIds = fstvc.getBackupId().split(",");
        int i = 0;
        for (; i < backupIds.length; i++) {
            if (Integer.parseInt(backupIds[i]) == fstvc.getNowId()) {
                break;
            }
        }
        if (i == backupIds.length) {
            error = "conditionError";
            return error;
        }
        i--;
        if (i < 0) {
            int pageNum = Integer.parseInt(csc.getPageNum());
            pageNum -= 1;
            csc.setPageNum("" + pageNum);
            List<Integer> ids = null;
			try {
				ids = this.searchIdByConditionInIndex(csc);
			} catch (ServerErrorException e) {
				logger.error(LogConstants.exceptionMessage("Search course"), e);
			}
            if (ids == null || ids.size() == 0) {
                error = "dataError";
                return error;
            }
            String newbackupId = "" + ids.get(0);
            for (int j = 1; j < ids.size(); j++) {
                newbackupId = newbackupId + "," + ids.get(j);
            }
            fstvc.setBackupId(newbackupId);
            fstvc.setNowId(ids.get(ids.size() - 1));
        } else {
            fstvc.setNowId(Integer.parseInt(backupIds[i]));
        }
        return "";
    }

    @Override
    public int getPreviousRecordFlag(FromSearchToViewCondition fstvc) {
        String[] backupIds = fstvc.getBackupId().split(",");
        int i = 0;
        for (; i < backupIds.length; i++) {
            if (Integer.parseInt(backupIds[i]) == fstvc.getNowId()) {
                break;
            }
        }
        if (i == 0) {
            return -1;
        }
        return 0;
    }

    @Override
    public int getNextRecordFlag(FromSearchToViewCondition fstvc) {
        String[] backupIds = fstvc.getBackupId().split(",");
        int i = 0;
        for (; i < backupIds.length; i++) {
            if (Integer.parseInt(backupIds[i]) == fstvc.getNowId()) {
                break;
            }
        }
        if (i == (backupIds.length - 1)) {
            return 1;
        }
        return 0;
    }

    /**
     * spring timer use this method update the history trainer for course.
     */ 
    @Override
    public void updateHistoryTrainerForCourse() throws ServerErrorException {
        logger.debug(LogConstants.pureMessage("Update the history trainer for course begin..."));
        //Get all courses in the database.
        List<Course> courses = null;
        try {
            courses = courseDao.findUndeletedCourse();
        } catch (Exception e) {
            logger.error(LogConstants.exceptionMessage("Loading courses"), e);
            throw new ServerErrorException(ErrorCodeConstants.SERVER_ERROR);
        }
        for (Course course : courses) {
            //Before update the history trainer for the course set the history trainer to null.
            course.setHistoryTrainers(null);
            //Get all the planCourses of the course.
            List<ActualCourse> actualCourses = null;
            try {
                actualCourses = actualCourseDao.findActualCoursesByCourseId(course.getCourseId());
            } catch (Exception e) {
                logger.error(LogConstants.exceptionMessage("Loading planCourses"), e);
                throw new ServerErrorException(ErrorCodeConstants.SERVER_ERROR);
            }
            if(null != actualCourses){
                for (ActualCourse actualCourse : actualCourses) {
                    Plan plan = actualCourse.getPlan();
                    //The condition of plan
                    if((plan.getPlanIsDeleted() != 1) && (plan.getPlanIsCanceled() != 1) && (plan.getPlanIsPublish() == 1)){
                        //The condition of planCourse
                        if(actualCourse.getCourseEndTime() != null && actualCourse.getCourseEndTime().before(new Date()) && actualCourse.getCourseTrainer() != null){
                            String trainer = actualCourse.getCourseTrainer();
                                if (null == course.getHistoryTrainers()) {
                                    course.setHistoryTrainers(trainer);
                                } else if(course.getHistoryTrainers().indexOf(trainer) == -1){
                                    course.setHistoryTrainers(course.getHistoryTrainers() + ", " + trainer);
                                }
                        }
                        try {
                            courseDao.updateObject(course);
                        } catch (Exception e){
                        	logger.error(LogConstants.exceptionMessage("Update Courses"), e);
                            throw new ServerErrorException(ErrorCodeConstants.SERVER_ERROR);
                        }
                    }
                }
            } else {
                logger.debug(LogConstants.message("Course has no planCourse", "#"+course.getCourseId()));
            }
        }
        logger.debug(LogConstants.pureMessage("Update the history trainer for course end..."));
    }
}
