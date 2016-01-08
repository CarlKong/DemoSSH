package com.augmentum.ot.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryParser.MultiFieldQueryParser;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.QueryWrapperFilter;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TermRangeQuery;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.util.Version;
import org.springframework.stereotype.Component;
import org.wltea.analyzer.lucene.IKAnalyzer;

import com.augmentum.ot.dao.ActualCourseAttachmentDao;
import com.augmentum.ot.dao.ActualCourseDao;
import com.augmentum.ot.dao.AssessmentDao;
import com.augmentum.ot.dao.CourseDao;
import com.augmentum.ot.dao.EmployeeDao;
import com.augmentum.ot.dao.PlanAttachmentDao;
import com.augmentum.ot.dao.PlanDao;
import com.augmentum.ot.dao.PlanEmployeeMapDao;
import com.augmentum.ot.dao.PlanTypeDao;
import com.augmentum.ot.dataObject.CourseItem;
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
import com.augmentum.ot.exception.DataWarningException;
import com.augmentum.ot.exception.ServerErrorException;
import com.augmentum.ot.model.ActualCourse;
import com.augmentum.ot.model.ActualCourseAttachment;
import com.augmentum.ot.model.Assessment;
import com.augmentum.ot.model.Employee;
import com.augmentum.ot.model.EmployeeRoleLevelMap;
import com.augmentum.ot.model.LeaveNote;
import com.augmentum.ot.model.Plan;
import com.augmentum.ot.model.PlanAttachment;
import com.augmentum.ot.model.PlanEmployeeMap;
import com.augmentum.ot.model.PlanType;
import com.augmentum.ot.service.PlanService;
import com.augmentum.ot.util.BeanFactory;
import com.augmentum.ot.util.DateHandlerUtils;
import com.augmentum.ot.util.StringHandlerUtils;
import com.augmentum.ot.util.ValidationUtil;
/**
 * @ClassName: PlanServiceImpl
 * @date 2012-7-17
 * @version V1.0
 */
@Component("planService")
public class PlanServiceImpl implements PlanService {

    private static Logger logger = Logger.getLogger(PlanServiceImpl.class);
    
    private PlanDao planDao;
    
    private CourseDao courseDao;
    
    private EmployeeDao employeeDao;
    
    private ActualCourseDao actualCourseDao;
    
    
    @Override
    public void createAllPlanIndexes() throws ServerErrorException {
        String hql = "from Plan plan";
        try {
            planDao.rebuildAllEntitiesIndexes(hql);
        }catch(Exception e){
            logger.error(LogConstants.exceptionMessage("Create plan indexes"), e);
            throw new ServerErrorException(ErrorCodeConstants.SERVER_ERROR);
        }
    }

    @Override
    public Page<Plan> searchPlansFromIndex(PlanSearchCondition psc)
            throws ServerErrorException {
        logger.debug(LogConstants.getDebugInput("search plans from index", psc));
        if (null == psc) {
            logger.warn(LogConstants.objectIsNULLOrEmpty("psc"));
            throw new ServerErrorException(ErrorCodeConstants.SERVER_VALIDATION_ERROR);
        }
        Analyzer analyzer = new IKAnalyzer();
        Page<Plan> page = new Page<Plan>();
        List<String> luceneSpecialOperator = Arrays.asList(IndexFieldConstants.LUCENE_OPEERATOR);
        if (psc.getQueryString() == null || "".equals(psc.getQueryString().trim())
                || luceneSpecialOperator.contains(psc.getQueryString().trim())) {
            psc.setQueryString("*");
        }
        QueryParser queryParser = new MultiFieldQueryParser(Version.LUCENE_30, psc.getSearchFields(), analyzer, psc.getBoosts());
        //* or ? are allowed as the first character of a PrefixQuery and WildcardQuery.
        queryParser.setAllowLeadingWildcard(true);
        Query queryAllFields = null;
        try {
            queryAllFields = queryParser.parse(psc.getQueryString());
        } catch (ParseException e) {
            logger.error(LogConstants.exceptionMessage("Parse query string to query type"), e);
            throw new ServerErrorException(ErrorCodeConstants.SERVER_ERROR);
        }
        //Build query filter
        try {
            BooleanQuery filterBooleanQuery = new BooleanQuery();
            //Add unDeleted filter
            buildFilterByField(""+FlagConstants.UN_DELETED, IndexFieldConstants.PLAN_IS_DELETED, filterBooleanQuery, analyzer);
            //Add unCancel filter
            buildFilterByField(""+FlagConstants.UN_CANCELED, IndexFieldConstants.PLAN_IS_CANCELED, filterBooleanQuery, analyzer);
            
            if (null == psc.getRoleFlag() || !(RoleNameConstants.TRAINING_MASTER.equals(psc.getRoleFlag()))) {
                //Add publish filter
                buildFilterByField(""+FlagConstants.IS_PUBLISHED, IndexFieldConstants.PLAN_IS_PUBLISHED, filterBooleanQuery, analyzer);
            }
            
            //Add plan type filter
            buildFilterByField(psc.getPlanTypeIds(), 
                    IndexFieldConstants.PREFIX_PLAN_TYPE + IndexFieldConstants.PLAN_TYPE_ID,
                    filterBooleanQuery, analyzer);
            //Add plan course type filter
            buildFilterByField(psc.getPlanCourseTypeIds(), 
                    IndexFieldConstants.ACTUAL_COURSE_TYPE_IDS,
                    filterBooleanQuery, analyzer);
            //Add plan publish time filter
            buildRangeFilterByDate(psc.getPublishLowerDate(), psc.getPublishUpperDate(),
                    IndexFieldConstants.PLAN_PUBLISH_DATETIME, filterBooleanQuery);
            //Add plan execute start time filter
            buildRangeFilterByDate(psc.getExecuteLowerDate(), null,
                    IndexFieldConstants.PLAN_EXECUTE_START_DATETIME, filterBooleanQuery);
            buildRangeFilterByDate(null, psc.getExecuteUpperDate(),
                    IndexFieldConstants.PLAN_EXECUTE_END_DATETIME, filterBooleanQuery);
            //Add role filter for my plans
            buildFilterByRole(psc.getRoleFlag(), psc.getEmployeeName(), filterBooleanQuery, analyzer);
            QueryWrapperFilter filter = null;
            if (filterBooleanQuery.getClauses().length > 0) {
                filter = new QueryWrapperFilter(filterBooleanQuery);
            }
            int totalRecords = planDao.queryTotalRecordsFromIndex(queryAllFields, filter);
                        
            String sortField = psc.getSortField();
        	if (null == psc.getSortField() || psc.getSortField().equals("null") || "".equals(psc.getSortField())) {
        		sortField = null;
        	}
        	if (sortField == null && psc.getQueryString().equals("*")) {
        		sortField = IndexFieldConstants.PREFIX_ID;
        	}
            
            List<Plan> planList = planDao.queryObjectFromIndex(queryAllFields, 
            		sortField, psc.getReverse(), filter, psc.getFirstResult(), psc.getPageSize());
            page.setNowPager(psc.getNowPage());
            page.setPageSize(psc.getPageSize());
            page.setTotalRecords(totalRecords);
            page.setList(planList);
        } catch(Exception e) {
            logger.error(LogConstants.exceptionMessage("Parse object to Lucene query type Or search index"), e);
            throw new ServerErrorException(ErrorCodeConstants.SERVER_ERROR);
        }
        logger.debug(LogConstants.getDebugOutput("search plans from index", page));
        return page;
    }

    
    public Plan findPlanById(Integer planId) throws DataWarningException, ServerErrorException {
        Plan plan = null;
        if (planId == null || planId <= 0) {
            logger.error(LogConstants.objectIsNULLOrEmpty("planId"));
            throw new ServerErrorException(ErrorCodeConstants.SERVER_VALIDATION_ERROR);
        }
        try {
            plan = planDao.findByPrimaryKey(planId);
        } catch (Exception e) {
            logger.error(LogConstants.exceptionMessage("Find plan by [#"+planId+"]"), e);
            throw new ServerErrorException(ErrorCodeConstants.SERVER_ERROR);
        }
        if (null == plan || FlagConstants.IS_DELETED == plan.getPlanIsDeleted()
                || FlagConstants.IS_CANCELED == plan.getPlanIsCanceled()) {
            logger.debug(LogConstants.pureMessage("Plan is not exist [#"+planId+"]"));
            return null;
        }
        return plan;
    }

    @Override
    public Plan getPlanById(Integer planId) throws DataWarningException, ServerErrorException {
        if (planId == null || planId == 0) {
            logger.error(LogConstants.objectIsNULLOrEmpty("planId"));
            throw new ServerErrorException(ErrorCodeConstants.SERVER_VALIDATION_ERROR);
        }
        logger.debug(LogConstants.pureMessage("Get a Plan by [#"+planId+"]"));
        return getPlanInfoById(planId);
    }

    @Override
    public Integer createPlan(Plan plan, PlanType planType,
            List<PlanAttachment> planAttachments, String invitedTrainees,
            String optionalTrainees, String spectificTrainees,
            List<ActualCourse> actualCourses, HttpServletRequest request)
            throws DataWarningException, ServerErrorException {
        
        // The not null field.
        if (plan == null || !plan.isInformationCorrect()) {
            logger.error(LogConstants.pureMessage("Plan basic information is not correct or Plan is null"));
            throw new ServerErrorException(ErrorCodeConstants.SERVER_ERROR);
        }
        
        // One or more courses are already started.
        if (FlagConstants.PLAN_VALIDATEION_RESULT_TRUE.equals(ValidationUtil.isPlanOneOrMoreCourseStarted(plan))) {
            dealWithDataWarningException(ErrorCodeConstants.EDIT_STARTED_PLAN, plan.getPlanId());
        }
        
        if (plan == null) {
            logger.error(LogConstants.objectIsNULLOrEmpty("Plan"));
            return null;
        }
        // plan's default information
        plan.setPlanIsCompleted(FlagConstants.UN_COMPLETED);
        plan.setPlanCreateDate(new Date());
        plan.setPlanLastUpdateDate(new Date());
        plan.setPlanIsPublish(FlagConstants.UN_PUBLISHED);
        plan.setPlanIsDeleted(FlagConstants.UN_DELETED);
        plan.setPlanIsCanceled(FlagConstants.UN_CANCELED);
        plan.setPlanPublishDate(DateFormatConstants.DEFAULT_LARGE_DATE);
        // connect plan with planType
        plan.setPlanType(planType);
        
        // connect plan with planCourses.
        if (actualCourses == null || actualCourses.isEmpty()) {
            logger.warn(LogConstants.objectIsNULLOrEmpty("actual course list"));
        } else {
            for (ActualCourse actualCourse : actualCourses) {
                actualCourse.setPlan(plan);
                /*
                 * When actualCourse is save as another plan's course the course info is the same id.
                 * When actualCourse is save as another plan's course the session info is the same id.
                 * It's should be the new one. Clean the id.
                 */
                if(actualCourse.getSessionInfo() != null){
                    actualCourse.getSessionInfo().setSessionInfoId(null);
                }
                if(actualCourse.getCourseInfo() != null){
                    actualCourse.getCourseInfo().setCourseInfoId(null);
                }
                if (!actualCourse.getAttachments().isEmpty()) {
                    actualCourse.setCourseHasAttachment(FlagConstants.HAS_ATTACHMENT);
                    for (ActualCourseAttachment attachment : actualCourse.getAttachments()) {
                        attachment.setCreateDateTime(new Date());
                    }
                }
                try {
                    actualCourseDao.saveOrUpdateObject(actualCourse);
                } catch (Exception e) {
                    logger.error(LogConstants.exceptionMessage("save or update actualCourse"), e);
                    throw new ServerErrorException(ErrorCodeConstants.SERVER_ERROR);
                }
            }
            plan.setActualCourses(actualCourses);
        }
        if (checkIfPlanIsComplete(actualCourses)) {
        	plan.setPlanIsCompleted(FlagConstants.IS_COMPLETED);
        } else {
        	plan.setPlanIsCompleted(FlagConstants.UN_COMPLETED);
        }
        plan.setTrainers(findTrainersForPlan(actualCourses));
        
        // isAllEmployeeAttend
        Boolean isAllEmpoyee = false;
        if (plan.getIsAllEmployee() == FlagConstants.IS_ALL_EMPLOYEES) {
            plan.setIsAllEmployee(FlagConstants.IS_ALL_EMPLOYEES);
            plan.setTrainees(FlagConstants.ALL_TRAINEES);
            isAllEmpoyee = true;
        } else {
            plan.setIsAllEmployee(FlagConstants.UN_ALL_EMPLOYEES);
        }
        // connect plan with trainees
        List<String> invitedTraineeList = StringHandlerUtils.strToArray(invitedTrainees, FlagConstants.SPLIT_COMMA);
        List<String> optionalTraineeList = StringHandlerUtils.strToArray(optionalTrainees, FlagConstants.SPLIT_COMMA);
        List<String> spectificTraineeList = StringHandlerUtils.strToArray(spectificTrainees, FlagConstants.SPLIT_COMMA);
        Integer traineesCount = 0;
        if (!isAllEmpoyee) {
            if (invitedTraineeList == null || invitedTraineeList.isEmpty()) {
                logger.warn(LogConstants.objectIsNULLOrEmpty("Invited trainees list"));
            } else {
                traineesCount = invitedTraineeList.size();
            }
            plan.setTrainees(traineesCount);
        }
        if (FlagConstants.INVITED_PLAN.equals(planType.getPlanTypeName())) {
            plan.setTraineeNames(invitedTrainees +FlagConstants.SPLIT_COMMA+ optionalTrainees);
        }
        // save plan
        try {
            planDao.saveObject(plan);
        } catch (Exception e) {
            logger.error(LogConstants.exceptionMessage("save plan"), e);
            throw new ServerErrorException(ErrorCodeConstants.SERVER_ERROR);
        }
        // setPlanEmployeeMap and setPlanCourseEmployeeMap
        try {
            setPlanEmployeeMap(plan, invitedTraineeList,
                    FlagConstants.ATTEND_TYPE_INVITED,
                    FlagConstants.IS_SAVETO_PLAN_COURSE_EMPLOYEE_MAP, request);
            setPlanEmployeeMap(plan, optionalTraineeList,
                    FlagConstants.ATTEND_TYPE_OPTIONAL,
                    FlagConstants.UN_SAVETO_PLAN_COURSE_EMPLOYEE_MAP, request);
            setPlanEmployeeMap(plan, spectificTraineeList,
                    FlagConstants.ATTEND_TYPE_SPECIFIC,
                    FlagConstants.UN_SAVETO_PLAN_COURSE_EMPLOYEE_MAP, request);
        } catch (ServerErrorException e) {
            logger.error(LogConstants.exceptionMessage("Set plan employee map"), e);
            throw new ServerErrorException(ErrorCodeConstants.SERVER_ERROR);
        }
        
        // connect plan with planAttachments
        if (planAttachments.isEmpty()) {
            logger.warn(LogConstants.objectIsNULLOrEmpty("Plan attachment list"));
            plan.setPlanHasAttachment(FlagConstants.NO_ATTACHMENT);
        } else {
            for (PlanAttachment planAttachment : planAttachments) {
                planAttachment.setPlan(plan);
            }
            plan.setPlanHasAttachment(FlagConstants.HAS_ATTACHMENT);
            plan.setPlanAttachments(planAttachments);
        }
        return plan.getPlanId();
    }
    
    private boolean checkIfPlanIsComplete(List<ActualCourse> actualCourseList) {
    	for (ActualCourse actualCourse : actualCourseList) {
    		if (null == actualCourse.getCourseRoomNum() 
    				|| "".equals(actualCourse.getCourseRoomNum() )
    				|| null == actualCourse.getCourseStartTime()
    				|| null == actualCourse.getCourseEndTime()
    				|| null == actualCourse.getCourseTrainer()
    				|| "".equals(actualCourse.getCourseTrainer())
    		) {
    			return false;
    		}
    	}
    	return true;
    }

    @Override
    public Boolean updatePlan(Plan plan, PlanType planType, String invitedTrainees,
            String optionalTrainees, String spectificTrainees,List<ActualCourse> actualCourses, List<PlanAttachment> planAttachments,
            List<ActualCourse> delActualCourses, HttpServletRequest request) throws DataWarningException, ServerErrorException {
        
        // The not null field.
        if (plan == null || !plan.isInformationCorrect()) {
            logger.error(LogConstants.pureMessage("Plan basic information is not correct or Plan is null"));
            throw new ServerErrorException(ErrorCodeConstants.SERVER_ERROR);
        }
        
        // Plan is canceled.
        /* if (FlagConstants.PLAN_VALIDATEION_RESULT_TRUE.equals(ValidationUtil.isPlanCanceled(plan))) {
            dealWithDataWarningException(ErrorCodeConstants.EDIT_CANCELED_PLAN, plan.getPlanId());
        }*/
        
        // Plan is completed.
        /*if (FlagConstants.PLAN_VALIDATEION_RESULT_TRUE.equals(ValidationUtil.isPlanCompleted(plan))) {
            dealWithDataWarningException(ErrorCodeConstants.EDIT_COMPLETED_PLAN, plan.getPlanId());
        }*/
        
        // Plan is deleted.
        /*if (FlagConstants.PLAN_VALIDATEION_RESULT_TRUE.equals(ValidationUtil.isPlanDeleted(plan))) {
            dealWithDataWarningException(ErrorCodeConstants.EDIT_DELETED_PLAN, plan.getPlanId());
        }
        
        // One or more courses are already started.
        if (FlagConstants.PLAN_VALIDATEION_RESULT_TRUE.equals(ValidationUtil.isPlanOneOrMoreCourseStarted(plan))) {
            dealWithDataWarningException(ErrorCodeConstants.EDIT_STARTED_PLAN, plan.getPlanId());
        }*/
        
        PlanEmployeeMapDao planEmployeeMapDao = BeanFactory.getPlanEmployeeMapDao();
        
        if (planType == null) {// not right parameter
            logger.error(LogConstants.objectIsNULLOrEmpty("Plan type"));
            throw new ServerErrorException(ErrorCodeConstants.SERVER_ERROR);
        }
        PlanTypeDao planTypeDao = BeanFactory.getPlanTypeDao();
        PlanAttachmentDao planAttachmentDao = BeanFactory.getPlanAttachmentDao();
        
        planType = planTypeDao.findByPrimaryKey(planType.getPlanTypeId());
        // find the plan from database
        Plan realPlan = planDao.findByPrimaryKey(plan.getPlanId());
        realPlan.setPlanName(plan.getPlanName());
        realPlan.setPlanBrief(plan.getPlanBrief());
        realPlan.setPlanBriefWithoutTag(plan.getPlanBriefWithoutTag());
        realPlan.setPlanCategoryTag(plan.getPlanCategoryTag());
        realPlan.setPlanLastUpdateDate(new Date());
        realPlan.setPlanType(planType);
        realPlan.setRegisterNotice(plan.getRegisterNotice());
        realPlan.setReminderEmail(plan.getReminderEmail());
        realPlan.setNeedAssessment(plan.getNeedAssessment());
        realPlan.setTrainers(findTrainersForPlan(actualCourses));
        //Set planIsCompleted.
        if (checkIfPlanIsComplete(actualCourses)) {
        	realPlan.setPlanIsCompleted(FlagConstants.IS_COMPLETED);
        } else {
        	realPlan.setPlanIsCompleted(FlagConstants.UN_COMPLETED);
        }
        List<Integer> exsitedActualCourseId = new ArrayList<Integer>();
        List<ActualCourse> notExsitedActualCourseList = new ArrayList<ActualCourse>();
        if (actualCourses != null) {
            for(ActualCourse actualCourse : actualCourses) {
                if (actualCourse != null && actualCourse.getActualCourseId() != null) {
                    exsitedActualCourseId.add(actualCourse.getActualCourseId());
                } else {
                    // This plan course is not exited in database.
                    notExsitedActualCourseList.add(actualCourse);
                }
            }
        }
        List<ActualCourse> actualCourseInDBList = realPlan.getActualCourses();
        List<ActualCourse> oldActualCourseList = new ArrayList<ActualCourse>();
        for(ActualCourse actualCourse : actualCourseInDBList) {
            ActualCourse oldActualCourse = new ActualCourse();
            oldActualCourse.setCourseName(actualCourse.getCourseName());
            oldActualCourse.setCourseRoomNum(actualCourse.getCourseRoomNum());
            oldActualCourse.setCourseStartTime(actualCourse.getCourseStartTime());
            oldActualCourse.setCourseEndTime(actualCourse.getCourseEndTime());
            oldActualCourse.setCourseTrainer(actualCourse.getCourseTrainer());
            oldActualCourseList.add(oldActualCourse);
        }
        List<ActualCourse> modifyActualCourseList = new ArrayList<ActualCourse>();
        List<ActualCourse> deletedActualCourseInDBList = new ArrayList<ActualCourse>();
        
        if (actualCourseInDBList != null && !actualCourseInDBList.isEmpty()) {
            for (ActualCourse actualCourseInDB: actualCourseInDBList) {
                Integer actualCourseIdInDB = actualCourseInDB.getActualCourseId();
                
                boolean isExist = exsitedActualCourseId.contains(actualCourseIdInDB);
                if (!isExist) {
                    // This object is deleted in page.
                    deletedActualCourseInDBList.add(actualCourseInDB);
                    List<Employee> employees = actualCourseInDB.getEmployeeList();
                    for (Employee employee : employees) {
                        employee.getActualCourseList().remove(actualCourseInDB);
                    }
                    actualCourseInDB.getEmployeeList().clear();
                    actualCourseDao.deleteObject(actualCourseInDB);
                } else {
                    // Copy the field into actualCourseInDB;
                    ActualCourse actualCourseInPage = findActualCourseByIdInList(actualCourseIdInDB, actualCourses);
                    if(realPlan.getPlanIsPublish().equals(FlagConstants.IS_PUBLISHED) &&
                            realPlan.getPlanType().getPlanTypeName().equals(FlagConstants.INVITED_PLAN)) {
                        //Modify the plan course
                        if (checkIsModifyActualCourse(actualCourseInDB, actualCourseInPage)){
                            modifyActualCourseList.add(actualCourseInPage);
                        }
                    }
                    actualCourseInDB.setCourseName(actualCourseInPage.getCourseName());
                    actualCourseInDB.setCourseRoomNum(actualCourseInPage.getCourseRoomNum());
                    actualCourseInDB.setCourseStartTime(actualCourseInPage.getCourseStartTime());
                    actualCourseInDB.setCourseEndTime(actualCourseInPage.getCourseEndTime());
                    actualCourseInDB.setCourseDuration(actualCourseInPage.getCourseDuration());
                    actualCourseInDB.setCourseTrainer(actualCourseInPage.getCourseTrainer());
                    actualCourseInDB.setCourseBrief(actualCourseInPage.getCourseBrief());
                    actualCourseInDB.setCourseBriefWithoutTag(actualCourseInPage.getCourseBriefWithoutTag());
                    actualCourseInDB.setCourseOrder(actualCourseInPage.getCourseOrder());
                    //copy courseInfo
                    if (null != actualCourseInDB.getCourseInfo()) {
                        actualCourseInDB.getCourseInfo().setCourseAuthorName(actualCourseInPage.getCourseInfo().getCourseAuthorName());
                        actualCourseInDB.getCourseInfo().setCourseCategoryTag(actualCourseInPage.getCourseInfo().getCourseCategoryTag());
                        actualCourseInDB.getCourseInfo().setCourseHasHomework(actualCourseInPage.getCourseInfo().getCourseHasHomework());
                        actualCourseInDB.getCourseInfo().setCourseId(actualCourseInPage.getCourseInfo().getCourseId());
                        actualCourseInDB.getCourseInfo().setCourseTargetTrainee(actualCourseInPage.getCourseInfo().getCourseTargetTrainee());
                        actualCourseInDB.getCourseInfo().setCourseType(actualCourseInPage.getCourseInfo().getCourseType());
                        actualCourseInDB.getCourseInfo().setTrainee2Trainer(actualCourseInPage.getCourseInfo().getTrainee2Trainer());
                        actualCourseInDB.getCourseInfo().setTrainer2Trainee(actualCourseInPage.getCourseInfo().getTrainer2Trainee());
                    } else {
                        // copy session
                    }
                    // set actual course attachments
                    ActualCourseAttachmentDao actualCourseAttachmentDao = BeanFactory.getActualCourseAttachmentDao();
                    if (!actualCourseInPage.getAttachments().isEmpty()) {
                        actualCourseInDB.setCourseHasAttachment(FlagConstants.HAS_ATTACHMENT);
                        for (ActualCourseAttachment attachment : actualCourseInPage.getAttachments()) {
                            if (!actualCourseInDB.getAttachments().contains(attachment)) {
                                attachment.setActualCourse(actualCourseInDB);
                                attachment.setCreateDateTime(new Date());
                                actualCourseInDB.getAttachments().add(attachment);
                            }
                        }
                        for (ActualCourseAttachment attachment : actualCourseInDB.getAttachments()) {
                            if (!actualCourseInPage.getAttachments().contains(attachment) && attachment.getActualCourseAttachmentIsDeleted().equals(FlagConstants.UN_DELETED)) {
                                attachment.setActualCourseAttachmentIsDeleted(FlagConstants.IS_DELETED);
                            } else {
                                for (ActualCourseAttachment attachmentInPage : actualCourseInPage.getAttachments()) {
                                    if (attachmentInPage.equals(attachment)) {
                                        attachment.setActualCourseAttachmentVisible(attachmentInPage.getActualCourseAttachmentVisible());
                                    }
                                }
                            }
                            actualCourseAttachmentDao.saveOrUpdateObject(attachment);
                        }
                        
                    } else {
                        actualCourseInDB.setCourseHasAttachment(FlagConstants.NO_ATTACHMENT);
                        for (ActualCourseAttachment attachment : actualCourseInDB.getAttachments()) {
                            if (attachment.getActualCourseAttachmentIsDeleted().equals(FlagConstants.UN_DELETED)) {
                                attachment.setActualCourseAttachmentIsDeleted(FlagConstants.IS_DELETED);
                                actualCourseAttachmentDao.saveOrUpdateObject(attachment);
                            }
                        }
                    }
                    
                }
            }
        }
        
        actualCourseInDBList.addAll(notExsitedActualCourseList);
        actualCourseInDBList.removeAll(deletedActualCourseInDBList);
        realPlan.setActualCourses(actualCourseInDBList);
        modifyActualCourseList.addAll(notExsitedActualCourseList);
        
        // Set the plan into each actual course collection.
        if (notExsitedActualCourseList != null && !notExsitedActualCourseList.isEmpty()) {
            for (ActualCourse actualCourse: notExsitedActualCourseList) {
                actualCourse.setPlan(realPlan);
                List<PlanEmployeeMap> planEmployeeMaps = planEmployeeMapDao.getPlanEmployeeMaps(realPlan.getPlanId(), 
                        FlagConstants.ATTEND_TYPE_INVITED, FlagConstants.UN_DELETED);
                for (PlanEmployeeMap pem :planEmployeeMaps) {
                    pem.getEmployee().getActualCourseList().add(actualCourse);
                }
            }
        }
        
        //find the end time and start time for plan
        Boolean actualCourseEndTimeHasEmpty = false;
        if(realPlan.getPlanIsPublish().equals(FlagConstants.IS_PUBLISHED)) {
            Date minStartTime = actualCourses.get(0).getCourseStartTime();
            Date maxEndTime = actualCourses.get(0).getCourseEndTime();
            for(ActualCourse actualCourse : actualCourses){
                if(minStartTime == null || 
                	(actualCourse.getCourseStartTime() != null && actualCourse.getCourseStartTime().before(minStartTime))){
                    minStartTime = actualCourse.getCourseStartTime();
                }
                if(actualCourse.getCourseEndTime() == null){
                    actualCourseEndTimeHasEmpty = true;
                    continue;
                }else{
                    if(maxEndTime != null && actualCourse.getCourseEndTime().after(maxEndTime)){
                        maxEndTime = actualCourse.getCourseEndTime();
                    }
                }
            }
            if(actualCourseEndTimeHasEmpty){
                realPlan.setPlanExecuteEndTime(null);
            }else{
                realPlan.setPlanExecuteEndTime(maxEndTime);
            }
            realPlan.setPlanExecuteStartTime(minStartTime);
            
        };
        // update the plan attachment
        if (!planAttachments.isEmpty()) {
            realPlan.setPlanHasAttachment(FlagConstants.HAS_ATTACHMENT);
            for (PlanAttachment planAttachment : planAttachments) {
                planAttachment.setPlan(realPlan);
                planAttachmentDao.saveOrUpdateObject(planAttachment);
            }
            for (PlanAttachment attachment : realPlan.getPlanAttachments()) {
                if (!planAttachments.contains(attachment)) {
                    PlanAttachment planAttachment = planAttachmentDao
                            .findByPrimaryKey(attachment
                                    .getPlanAttachmentId());
                    planAttachment.setPlanAttachmentIsDeleted(FlagConstants.IS_DELETED);
                }
            }
            logger.debug(LogConstants.pureMessage("Handle the plan attachment success"));
        } else {
            realPlan.setPlanHasAttachment(FlagConstants.NO_ATTACHMENT);
            for (PlanAttachment planAttachment : realPlan.getPlanAttachments()) {
                PlanAttachment pAttachment = planAttachmentDao.findByPrimaryKey(planAttachment
                        .getPlanAttachmentId());
                pAttachment.setPlanAttachmentIsDeleted(FlagConstants.IS_DELETED);
            }
        }
        
        // isAllEmployeeAttend
        Boolean isAllEmpoyee = false;
        if (plan.getIsAllEmployee().equals(FlagConstants.IS_ALL_EMPLOYEES)) {
            realPlan.setIsAllEmployee(FlagConstants.IS_ALL_EMPLOYEES);
            realPlan.setTrainees(FlagConstants.ALL_TRAINEES);
            for(PlanEmployeeMap pem : realPlan.getPlanEmployeeMapList()) {
                pem.setPlanEmployeeIsDeleted(FlagConstants.IS_DELETED);
                planEmployeeMapDao.updateObject(pem);
            }
            isAllEmpoyee = true;
        } else {
            realPlan.setIsAllEmployee(FlagConstants.UN_ALL_EMPLOYEES);
        }
        // connect plan with trainees
        List<String> invitedTraineeList = StringHandlerUtils.strToArray(invitedTrainees, FlagConstants.SPLIT_COMMA);
        List<String> optionalTraineeList = StringHandlerUtils.strToArray(optionalTrainees, FlagConstants.SPLIT_COMMA);
        List<String> spectificTraineeList = StringHandlerUtils.strToArray(spectificTrainees, FlagConstants.SPLIT_COMMA);
        Integer traineesCount = 0;
        Set<Employee> recordNewAddEmployees = new HashSet<Employee>();
        if (!isAllEmpoyee) {
            if (invitedTraineeList == null || invitedTraineeList.isEmpty()) {
                logger.warn(LogConstants.objectIsNULLOrEmpty("invitedTrainees"));
            } else {
                traineesCount = invitedTraineeList.size();
            }
            realPlan.setTrainees(traineesCount);
            if (FlagConstants.INVITED_PLAN.equals(planType.getPlanTypeName())) {
                realPlan.setTraineeNames(invitedTrainees +FlagConstants.SPLIT_COMMA+ optionalTrainees);
            }
            planDao.updateObject(realPlan);
            // updatePlanEmployeeMap and updatePlanCourseEmployeeMap
            try {
                updatePlanEmployeeMap(realPlan, invitedTraineeList, FlagConstants.ATTEND_TYPE_INVITED, 
                        FlagConstants.IS_SAVETO_PLAN_COURSE_EMPLOYEE_MAP, request, recordNewAddEmployees);
                updatePlanEmployeeMap(realPlan, optionalTraineeList, FlagConstants.ATTEND_TYPE_OPTIONAL, 
                        FlagConstants.UN_SAVETO_PLAN_COURSE_EMPLOYEE_MAP, request, recordNewAddEmployees);
                updatePlanEmployeeMap(realPlan, spectificTraineeList, FlagConstants.ATTEND_TYPE_SPECIFIC, 
                        FlagConstants.UN_SAVETO_PLAN_COURSE_EMPLOYEE_MAP, request, recordNewAddEmployees);
            } catch (ServerErrorException e) {
                logger.error(LogConstants.exceptionMessage("Update the plan employee map failure!"), e);
                throw new ServerErrorException(ErrorCodeConstants.SERVER_ERROR);
            }
        }
        return true;
    }
    
    
    /**
     * find trainer of plan for create or edit plan.
     * @param trainersSet
     * @return
     */
    private String findTrainersForPlan(List<ActualCourse> actualCourseList) {
        Set<String> trainersSet = new HashSet<String>();
        if (actualCourseList != null) {
            for (ActualCourse actualCourse : actualCourseList) {
                if (ValidationUtil.isNotNull(actualCourse.getCourseTrainer())) {
                    trainersSet.add(actualCourse.getCourseTrainer());
                }
            }
        }
        if (trainersSet.size() > 0) {
            StringBuilder trainersBuilder = new StringBuilder();
            for (String trainer : trainersSet) {
                trainersBuilder = trainersBuilder.append(trainer).append(FlagConstants.SPLIT_COMMA);
            }
            String trainers = trainersBuilder.substring(0, trainersBuilder.length() - 1).toString();
            return trainers;
        } else {
            return "";
        }
    }
    
    private ActualCourse findActualCourseByIdInList(Integer actualCourseId, List<ActualCourse> actualCourseList) {
        ActualCourse actualCourse = null;
        if (actualCourseId != null && actualCourseList != null && !actualCourseList.isEmpty()) {
            for (ActualCourse tempActualCourse: actualCourseList) {
                if (tempActualCourse != null && tempActualCourse.getActualCourseId() != null) {
                    if (actualCourseId.equals(tempActualCourse.getActualCourseId())) {
                        actualCourse = tempActualCourse;
                        break;
                    }
                }
            }
        }
        return actualCourse;
    }
    
    private boolean checkIsModifyActualCourse(ActualCourse oldCourse, ActualCourse newCourse) {
        if (!oldCourse.getCourseRoomNum().equals(newCourse.getCourseRoomNum())) {
            return true;
        } else if (oldCourse.getCourseStartTime() != null && newCourse.getCourseStartTime() == null) {
            return false;
        } else if (oldCourse.getCourseEndTime() != null && newCourse.getCourseEndTime() == null) {
            return false;
        } else if (!DateHandlerUtils.isEqual(oldCourse.getCourseStartTime(), newCourse.getCourseStartTime())) {
            return true;
        }else if (!DateHandlerUtils.isEqual(oldCourse.getCourseEndTime(), newCourse.getCourseEndTime())) {
            return true;
        }else if (!oldCourse.getCourseTrainer().equals(newCourse.getCourseTrainer())) {
            return true;
        }else {
            return false;
        }
    }
    
    @Override
    public Integer deletePlanById(Integer planId) throws DataWarningException, ServerErrorException {
        if (planId == null || planId <= 0) {
            logger.error(LogConstants.objectIsNULLOrEmpty("Plan Id"));
            throw new ServerErrorException(ErrorCodeConstants.SERVER_ERROR);
        }
        Plan plan = getPlanInfoById(planId);
        // plan is not exist
        if (null == plan) {
            logger.error(LogConstants.pureMessage("Plan[#"+planId+"] is not existed"));
            throw new DataWarningException(ErrorCodeConstants.DELETE_DELETED_PLAN);
        }
        
        // Plan is canceled.
        if (FlagConstants.PLAN_VALIDATEION_RESULT_TRUE.equals(ValidationUtil.isPlanCanceled(plan))) {
            dealWithDataWarningException(ErrorCodeConstants.DELETE_CANCELED_PLAN, plan.getPlanId());
        }
        
        // Plan is deleted.
        if (FlagConstants.PLAN_VALIDATEION_RESULT_TRUE.equals(ValidationUtil.isPlanDeleted(plan))) {
            dealWithDataWarningException(ErrorCodeConstants.DELETE_DELETED_PLAN, plan.getPlanId());
        }
        
        // plan has been published.
        if (plan.getPlanIsPublish() == FlagConstants.IS_PUBLISHED) {
            //planCourse field has null value
            if (!checkActualCourseIsCompleted(plan.getActualCourses())) {
                dealWithDataWarningException(ErrorCodeConstants.DELETE_NOT_COMPLETE_PLAN, plan.getPlanId());
            }
            //plan is not finished
            if (checkActualCourseIsCompleted(plan.getActualCourses())
                    && plan.getPlanExecuteEndTime().after(new Date())) {
                dealWithDataWarningException(ErrorCodeConstants.DELETE_ON_GOING_PLAN, plan.getPlanId());
            }
        }
        
        // Delete index of assessments about plan and mark as IS_DELETED
        AssessmentDao assessmentDao = (AssessmentDao) BeanFactory.getAssessmentDao();
        List<Assessment> assessmentList = null;
        try {
            assessmentList = assessmentDao.getAllAssessmentsByPlanId(planId);
            for (Assessment assessment : assessmentList) {
                assessment.setAssessIsDeleted(FlagConstants.IS_DELETED);
                assessmentDao.updateObject(assessment);
            }
        } catch (Exception e) {
            logger.error(LogConstants.exceptionMessage("get all assessment by plan id or update assessment "), e);
            throw new ServerErrorException(ErrorCodeConstants.SERVER_ERROR);
        }
        // Delete index of plan course and relationship of planCourse and
        // employee in plan_course_employee_map table.
        for (ActualCourse actualCourse : plan.getActualCourses()) {
            try {
                actualCourseDao.deleteObjectIndex(actualCourse.getActualCourseId());
            } catch (Exception e) {
                logger.error(LogConstants.exceptionMessage("Delete plan course index"), e);
                throw new ServerErrorException(ErrorCodeConstants.SERVER_ERROR);
            }
            
            actualCourse.getEmployeeList().clear();
            if (actualCourse.getCourseHasAttachment() == FlagConstants.HAS_ATTACHMENT) {
                // Delete attachment of plan course.
                ActualCourseAttachmentDao actualCourseAttachmentDao = BeanFactory.getActualCourseAttachmentDao();
                try {
                    for (ActualCourseAttachment actualCourseAttachment : actualCourse.getAttachments()) {
                        actualCourseAttachment.setActualCourseAttachmentIsDeleted(FlagConstants.IS_DELETED);
                        actualCourseAttachmentDao.updateObject(actualCourseAttachment);
                    }
                } catch (Exception e) {
                    logger.error(LogConstants.exceptionMessage("Update actual course attachment"), e);
                    throw new ServerErrorException(ErrorCodeConstants.SERVER_ERROR);
                }
            }
        }
        // Delete attachment of plan.
        if (plan.getPlanHasAttachment() == FlagConstants.HAS_ATTACHMENT) {
            PlanAttachmentDao planAttachmentDao = BeanFactory.getPlanAttachmentDao();
            try {
                for (PlanAttachment planAttachment : plan.getPlanAttachments()) {
                    planAttachment.setPlanAttachmentIsDeleted(FlagConstants.IS_DELETED);
                    planAttachmentDao.updateObject(planAttachment);
                }
            } catch (Exception e) {
                logger.error(LogConstants.exceptionMessage("Update plan attachment"), e);
                throw new ServerErrorException(ErrorCodeConstants.SERVER_ERROR);
            }
        }
        // Delete plan
        plan.setPlanIsDeleted(FlagConstants.IS_DELETED);
        plan.setPlanLastUpdateDate(new Date());
        return FlagConstants.OPERATE_SUCCESS;
    }

    @Override
    public Integer updatePlanForPublishById(Integer planId) throws DataWarningException, ServerErrorException {
        logger.debug(LogConstants.getDebugInput("update plan for publish By Id", planId));
        if (planId == null || planId <= 0) {
            logger.error(LogConstants.objectIsNULLOrEmpty("Plan Id"));
            throw new ServerErrorException(ErrorCodeConstants.SERVER_ERROR);
        }
        Plan plan = getPlanInfoById(planId);
        // plan is not exist
        if (null == plan) {
            logger.error(LogConstants.pureMessage("Plan[#"+planId+"] is not existed"));
            throw new ServerErrorException(ErrorCodeConstants.SERVER_ERROR);
        }
        
        // Plan is deleted.
        if (FlagConstants.PLAN_VALIDATEION_RESULT_TRUE.equals(ValidationUtil.isPlanDeleted(plan))) {
            dealWithDataWarningException(ErrorCodeConstants.PUBLISH_DELETED_PLAN, plan.getPlanId());
        }
        
        // Plan is canceled.
        if (FlagConstants.PLAN_VALIDATEION_RESULT_TRUE.equals(ValidationUtil.isPlanCanceled(plan))) {
            dealWithDataWarningException(ErrorCodeConstants.PUBLISH_CANCELED_PLAN, plan.getPlanId());
        }
        
        // Plan has no course.
        if (FlagConstants.PLAN_VALIDATEION_RESULT_TRUE.equals(ValidationUtil.isPlanNoCourseAdded(plan))) {
            dealWithDataWarningException(ErrorCodeConstants.PUBLISH_NO_COURSE_ADDED_PLAN, plan.getPlanId());
        }
        
        // plan has no trainees
        if (FlagConstants.PLAN_VALIDATEION_RESULT_TRUE.equals(ValidationUtil.isPlanNoTraineesAdded(plan))) {
            if(plan.getPlanType().getPlanTypeName().equals(FlagConstants.INVITED_PLAN)) {
                dealWithDataWarningException(ErrorCodeConstants.PUBLISH_NO_INVITED_TRAINEES_ADDED_PLAN, plan.getPlanId());
            } else {
                dealWithDataWarningException(ErrorCodeConstants.PUBLISH_NO_SPECIFIC_TRAINEES_ADDED_PLAN, plan.getPlanId());
            }
        }
        
        // A course of plan is started.
        if (FlagConstants.PLAN_VALIDATEION_RESULT_TRUE.equals(ValidationUtil.isPlanOneOrMoreCourseStarted(plan))) {
            dealWithDataWarningException(ErrorCodeConstants.PUBLISH_COURSE_STARTED_PLAN, plan.getPlanId());
        }
        
        // Plan is published.
        if (ValidationUtil.isPlanPublished(plan).equals(FlagConstants.PLAN_VALIDATEION_RESULT_TRUE)) {
            dealWithDataWarningException(ErrorCodeConstants.PUBLISH_PUBLISHED_PLAN, plan.getPlanId());
        }
        
        // Plan is completed.
        if (FlagConstants.PLAN_VALIDATEION_RESULT_TRUE.equals(ValidationUtil.isPlanCompleted(plan))) {
            dealWithDataWarningException(ErrorCodeConstants.PUBLISH_COMPLETED_PLAN, plan.getPlanId());
        }
        
        Date currentDate = new Date();
        plan.setPlanExecuteStartTime(getPlanStartTime(plan));
        plan.setPlanExecuteEndTime(getPlanEndTime(plan));
        plan.setPlanPublishDate(currentDate);
        plan.setPlanIsPublish(FlagConstants.IS_PUBLISHED);
        plan.setPlanLastUpdateDate(currentDate);
        logger.debug(LogConstants.pureMessage("Plan[#"+planId+"] has been published"));
        return FlagConstants.OPERATE_SUCCESS;
    }

    @Override
    public Integer updatePlanForCancel(Integer planId, String cancelPlanReason) throws DataWarningException, ServerErrorException {
        logger.debug(LogConstants.getDebugInput("update plan for cancel", planId));
        if (planId == null || planId <= 0) {
            logger.error(LogConstants.objectIsNULLOrEmpty("planId"));
            throw new ServerErrorException(ErrorCodeConstants.SERVER_ERROR);
        }
        Plan plan = getPlanInfoById(planId);
        // plan is not exist
        if (null == plan) {
            logger.error(LogConstants.pureMessage("Plan[#"+planId+"] is not existed"));
            throw new ServerErrorException(ErrorCodeConstants.SERVER_ERROR);
        }
        
        // Plan is not published.
        if (ValidationUtil.isPlanPublished(plan).equals(FlagConstants.PLAN_VALIDATEION_RESULT_FALSE)) {
            dealWithDataWarningException(ErrorCodeConstants.CANCEL_PLAN_NO_PUBLISHED, plan.getPlanId());
        }
        
        // Plan is canceled.
        if (FlagConstants.PLAN_VALIDATEION_RESULT_TRUE.equals(ValidationUtil.isPlanCanceled(plan))) {
            dealWithDataWarningException(ErrorCodeConstants.CANCEL_CANCELED_PLAN, plan.getPlanId());
        }
        
        // Plan is completed.
        if (FlagConstants.PLAN_VALIDATEION_RESULT_TRUE.equals(ValidationUtil.isPlanCompleted(plan))) {
            dealWithDataWarningException(ErrorCodeConstants.CANCEL_COMPLETED_PLAN, plan.getPlanId());
        }
        
        // Plan is deleted.
        if (FlagConstants.PLAN_VALIDATEION_RESULT_TRUE.equals(ValidationUtil.isPlanDeleted(plan))) {
            dealWithDataWarningException(ErrorCodeConstants.CANCEL_DELETED_PLAN, plan.getPlanId());
        }
        
        Date currentDate = new Date();
        
        plan.setPlanIsCanceled(FlagConstants.IS_CANCELED);
        plan.setCancelPlanReason(cancelPlanReason);
        plan.setPlanLastUpdateDate(currentDate);
        for (ActualCourse actualCourse : plan.getActualCourses()) {
            if (null != actualCourse.getCourseStartTime() && null != actualCourse.getCourseEndTime() && 
                    actualCourse.getCourseStartTime().after(currentDate)
                    && actualCourse.getCourseEndTime().after(currentDate)) {
                actualCourse.setCourseStartTime(null);
                actualCourse.setCourseEndTime(null);
            }
        }
        
        return FlagConstants.OPERATE_SUCCESS;
    }

    

    /**
     * Get all information of plan: include attachments, courses, course attachments.
     * @param planId
     * @return
     */
    private Plan getPlanInfoById(Integer planId) throws ServerErrorException {
        Plan plan = null;
        try {
            plan = planDao.findByPrimaryKey(planId);
        } catch (Exception e) {
            logger.error(LogConstants.exceptionMessage("Get plan by [#"+planId+"]"), e);
            throw new ServerErrorException(ErrorCodeConstants.SERVER_ERROR);
        }
        
        if (null == plan || FlagConstants.IS_DELETED == plan.getPlanIsDeleted()) {
            logger.warn(LogConstants.pureMessage("Plan[#"+planId+"] is not existed"));
            return null;
        }
        // Get plan attachment.
        List<PlanAttachment> planAttachments = new ArrayList<PlanAttachment>();
        for (PlanAttachment planAttachment : plan.getPlanAttachments()) {
             if (planAttachment.getPlanAttachmentIsDeleted() == FlagConstants.UN_DELETED) {
                 planAttachments.add(planAttachment);
             }
        }
        plan.setPlanAttachments(planAttachments);
        // Get plan course
        for (ActualCourse actualCourse : plan.getActualCourses()) {
            List<ActualCourseAttachment> attachments = new ArrayList<ActualCourseAttachment>();
            for (ActualCourseAttachment attachment : actualCourse.getAttachments()) {
                if (attachment.getActualCourseAttachmentIsDeleted() == FlagConstants.UN_DELETED) {
                    attachments.add(attachment);
                }
            }
            actualCourse.setAttachments(attachments);
            List<Employee> employees = new ArrayList<Employee>();
            for (Employee employee : actualCourse.getEmployeeList()) {
                employees.add(employee);
            }
            actualCourse.setEmployeeList(employees);
            List<LeaveNote> leaveNotes = new ArrayList<LeaveNote>();
            for (LeaveNote leaveNote : actualCourse.getLeaveNoteList()){
                    leaveNotes.add(leaveNote);
            }
            actualCourse.setLeaveNoteList(leaveNotes);
        }
        // Get trainee list
        for (PlanEmployeeMap planEmployeeMap : plan.getPlanEmployeeMapList()) {
            planEmployeeMap.getPlanEmployeeMapId();
        }
        return plan;
    }
    
    /**
     * Check if all fields of plan courses is completed.
     * 
     * @param planCourses
     * @return
     */
    private Boolean checkActualCourseIsCompleted(List<ActualCourse> actualCourses) {
        if (actualCourses.size() < 1) {
            return false;
        }
        for (ActualCourse actualCourse : actualCourses) {
            if (actualCourse.getCourseRoomNum() == null
                    || actualCourse.getCourseStartTime() == null
                    || actualCourse.getCourseEndTime() == null
                    || actualCourse.getCourseTrainer() == null) {
                return false;
            }
        }
        return true;
    }
    
    
    
    /**
     * Get plan start time by planCourses' start time.
     * @param planCourseList
     * @return null:Start time of one course is null;
     *         startTime:The earliest time of all courses.
     */
    private Date getPlanStartTime(Plan plan) {
        List<ActualCourse> actualCourses = plan.getActualCourses();
        Date startTime = null;
        if (null != actualCourses) {
            if (actualCourses.size() > 0) {
                startTime = actualCourses.get(0).getCourseStartTime();
                if (null == startTime) {
                    return null;
                }
            }
            for (ActualCourse actualCourse : actualCourses) {
                if (null == actualCourse.getCourseStartTime()) {
                    return null;
                }
                if (startTime.after(actualCourse.getCourseStartTime())) {
                    startTime = actualCourse.getCourseStartTime();
                }
            }
        }
        return startTime;
    }
    
    /**
     * Get plan end time by planCourses' end time.
     * @param planCourseList
     * @return null:End time of one course is null;
     *         endTime:The latest time of all courses.
     */
    private Date getPlanEndTime(Plan plan) {
        List<ActualCourse> actualCourses = plan.getActualCourses();
        Date endTime = null;
        if (null != actualCourses) {
            if (actualCourses.size() > 0) {
                endTime = actualCourses.get(0).getCourseEndTime();
                if (null == endTime) {
                    return null;
                }
            }
            for (ActualCourse actualCourse : actualCourses) {
                if (null == actualCourse.getCourseEndTime()) {
                    return null;
                }
                if (endTime.before(actualCourse.getCourseEndTime())) {
                    endTime = actualCourse.getCourseEndTime();
                }
            }
        }
        return endTime;
    }

     /**
     * Save different trainee to planEmpoyeeMap
     * 
     * @param plan                               
     * @param trainees (trainees list)
     * @param attendType (0 or 1)
     * @param isSaveToPlanCourseTrainee (0 or 1)
     * @throws PlanServiceException
     */
    private void setPlanEmployeeMap(Plan plan, List<String> trainees,
            Integer attendType, Boolean isSaveToActualCourseTrainee, HttpServletRequest request)
            throws ServerErrorException {
        PlanEmployeeMapDao planEmployeeMapDao = BeanFactory.getPlanEmployeeMapDao();
        Employee employee = null;
        try {
            if (trainees != null && !(trainees.isEmpty())) {
                for (String trainee : trainees) {
                    employee = employeeDao.findEmployeeByName(trainee);
                    if (employee == null) {
                       throw new ServerErrorException(ErrorCodeConstants.SERVER_ERROR);
                    }
                    PlanEmployeeMap planEmployeeMap = new PlanEmployeeMap();
                    planEmployeeMap.setEmployee(employee);
                    planEmployeeMap.setPlan(plan);
                    planEmployeeMap.setPlanTraineeAttendType(attendType);
                    planEmployeeMap.setPlanEmployeeIsDeleted(FlagConstants.UN_DELETED);
                    planEmployeeMapDao.savePlanEmployeeMap(planEmployeeMap);
                    if (isSaveToActualCourseTrainee) {
                        setActualCourseEmployeeMap(plan.getActualCourses(), employee);
                    }
                }
            }
        } catch (Exception e) {
            logger.error(LogConstants.exceptionMessage("Save planEmployeeMap"), e);
            throw new ServerErrorException(ErrorCodeConstants.SERVER_ERROR);
        }
    }
    
     /**
     * Save different trainee to planCourseEmpoyeeMap
     * 
     * @param planCourses
     * @param planSessionList
     * @param employeeId
     * @throws PlanServiceException
     */
    private void setActualCourseEmployeeMap(List<ActualCourse> actualCourses, Employee employee) throws ServerErrorException {
        try {
            //Employee employee = employeeDao.findByPrimaryKey(employeeId);
            List<ActualCourse> existActualCourses = employee.getActualCourseList();
            if (actualCourses == null || actualCourses.isEmpty()) {
                logger.warn(LogConstants.objectIsNULLOrEmpty("Plan course list"));
            } else {
                for (ActualCourse actualCourse : actualCourses) {
                    if (existActualCourses == null || !(existActualCourses.contains(actualCourse))) {
                        if (actualCourse.getCourseStartTime() == null || actualCourse.getCourseStartTime().after(new Date())) {
                             employee.getActualCourseList().add(actualCourse);
                        }
                    }
                }
            }
            employeeDao.saveOrUpdateObject(employee);
        } catch (Exception e) {
            logger.error(LogConstants.exceptionMessage("Save actualCourseEmployeeMap! ") + LogConstants.getExceptionStackTrace(e));
            throw new ServerErrorException(ErrorCodeConstants.SERVER_ERROR);
        }
    }
    
    
     /** 
     * @Description: update planEmployeeMap
     * @param plan
     * @param trainees
     * @param attendType
     * @param request
     * @param isUpdateToActualCourseTrainee
     * @throws DataWarningException 
     * @throws PlanServiceException 
     */ 
    private void updatePlanEmployeeMap(Plan plan, List<String> trainees,Integer attendType,
            Boolean isUpdateToActualCourseTrainee, HttpServletRequest request,
            Collection<Employee> recordAddEmployees) throws ServerErrorException, DataWarningException {
        PlanEmployeeMapDao planEmployeeMapDao = BeanFactory.getPlanEmployeeMapDao();
        
        List<PlanEmployeeMap> oldPlanEmployeeMaps = planEmployeeMapDao.getPlanEmployeeMaps(plan.getPlanId(), attendType, FlagConstants.UN_DELETED);
        List<String> oldEmployees = new ArrayList<String>();
        List<Employee> deleteEmployees = new ArrayList<Employee>();
        List<Employee> addEmployees = new ArrayList<Employee>();
        Employee employee = null;
        Date currentDate = new Date();
        for(PlanEmployeeMap pem : oldPlanEmployeeMaps) {
            String pemName = pem.getEmployee().getAugUserName();
            oldEmployees.add(pemName);
            if(trainees == null || !trainees.contains(pemName)) {
                // For specific trainees
                if (attendType == FlagConstants.ATTEND_TYPE_SPECIFIC) {
                    PlanEmployeeMap joinPem = planEmployeeMapDao.getPlanEmployeeMapByPlanAndEmployee(plan.getPlanId(), pem.getEmployee().getEmployeeId(), FlagConstants.ATTEND_TYPE_JOIN);
                    if (joinPem != null) {
                        joinPem.setPlanEmployeeIsDeleted(FlagConstants.IS_DELETED);
                        joinPem.setOperationTime(currentDate);
                        planEmployeeMapDao.updateObject(joinPem);
                        plan.setTraineeNames(plan.getTraineeNames().replace(pemName, ""));
                        plan.setTrainees(plan.getTrainees() - 1);
                    }
                }
                pem.setPlanEmployeeIsDeleted(FlagConstants.IS_DELETED);
                planEmployeeMapDao.updateObject(pem);
                for (ActualCourse actualCourse : plan.getActualCourses()) {
                    if (null == actualCourse.getCourseStartTime() || actualCourse.getCourseStartTime().after(currentDate)) {
                        pem.getEmployee().getActualCourseList().remove(actualCourse);
                    }
                }
                deleteEmployees.add(pem.getEmployee());
                if (isUpdateToActualCourseTrainee) {
                    List<ActualCourse> needDeleteActualCourses = new ArrayList<ActualCourse>();
                    for (ActualCourse ac : pem.getEmployee().getActualCourseList()) {
                        if (ac.getPlan().getPlanId().equals(plan.getPlanId()) && ac.getCourseStartTime().after(currentDate)) {
                            needDeleteActualCourses.add(ac);
                        }
                    }
                    pem.getEmployee().getActualCourseList().removeAll(needDeleteActualCourses);
                }
            }
         }
        try {
            if (trainees != null) {
                for (String trainee : trainees) {
                    employee = employeeDao.findEmployeeByName(trainee);
                    if (employee == null) {
                        throw new ServerErrorException(ErrorCodeConstants.SERVER_ERROR);
                    }
                    if(!oldEmployees.contains(trainee)) {
                        PlanEmployeeMap planEmployeeMap = new PlanEmployeeMap();
                        planEmployeeMap.setEmployee(employee);
                        planEmployeeMap.setPlan(plan);
                        planEmployeeMap.setPlanTraineeAttendType(attendType);
                        planEmployeeMap.setPlanEmployeeIsDeleted(FlagConstants.UN_DELETED);
                        planEmployeeMapDao.saveObject(planEmployeeMap);
                        addEmployees.add(employee);
                        if (isUpdateToActualCourseTrainee) {
                            List<ActualCourse> notStartActualCourses = new ArrayList<ActualCourse>();
                            for (ActualCourse ac : plan.getActualCourses()) {
                                if (ac.getCourseStartTime() == null || ac.getCourseStartTime().after(currentDate)) {
                                    notStartActualCourses.add(ac);
                                }
                            }
                            setActualCourseEmployeeMap(notStartActualCourses, employee);
                        }
                    }
                }
            }
        } catch (Exception e) {
            logger.error(LogConstants.exceptionMessage("Save planEmployeeMap"), e);
            throw new ServerErrorException(ErrorCodeConstants.SERVER_ERROR);
        }
        if (!addEmployees.isEmpty()) {
            recordAddEmployees.addAll(addEmployees);
        }
    }

    
    /**
     * 
     * Build boolean query as a item of the filter by field
     * 
     * @param value   
     *          The object value
     * @param field
     *          Need to search field
     * @param filterBooleanQuery
     * @param analyzer
     * @throws ParseException
     */
    private void buildFilterByField(String value, String field, BooleanQuery filterBooleanQuery,
            Analyzer analyzer) throws ParseException {
        if (value == null || "".equals(value)) {
            return;
        }
        QueryParser queryParser = new QueryParser(Version.LUCENE_30, field, analyzer);
        Query query = queryParser.parse(value.trim());
        filterBooleanQuery.add(query, Occur.MUST);
    }
    
    /**
     * 
     * Build a range filter by date
     * 
     * @param lower
     *          The lower date
     * @param upper
     *          The upper date
     * @param field
     *          Search field
     * @param filterBooleanQuery
     * @throws ParseException
     */
    private void buildRangeFilterByDate(Date lower, Date upper, String field,
            BooleanQuery filterBooleanQuery) throws ParseException {
        if (lower == null && upper == null) {
            return ;
        }
        String lowerStr = null;
        String upperStr = null;
        if (lower != null) {
        	lowerStr = DateHandlerUtils.dateToString(DateFormatConstants.FULL_TIME_STR, lower);
        }
        if (upper != null) {
        	upperStr = DateHandlerUtils.dateToString(DateFormatConstants.YYYY_MM_DD, upper) + DateFormatConstants.LATETES_TIME_IN_ONE_DAY;
        }
        TermRangeQuery rangeQuery = new TermRangeQuery(field, lowerStr, upperStr, true, true);
        filterBooleanQuery.add(rangeQuery, Occur.MUST);
    }
    
    /**
     * 
     * Build a filter by role
     * 
     * @param role
     *          The role name
     * @param employeeName
     * @param filterBooleanQuery
     * @param analyzer
     * @throws ParseException
     */
    private void buildFilterByRole(String role, String employeeName,BooleanQuery filterBooleanQuery,
            Analyzer analyzer) throws ParseException {
        if (role == null || "".equals(role)) {
            return ;
        }
        String queryField = (role.equals(RoleNameConstants.TRAINING_MASTER)) ? 
                IndexFieldConstants.PLAN_CREATOR : ((role.equals(RoleNameConstants.TRAINER)) ? 
                IndexFieldConstants.PLAN_TRAINERS : IndexFieldConstants.PLAN_TRAINEE_NAMES);
        QueryParser queryParserByRole = new QueryParser(Version.LUCENE_30, queryField, analyzer);
        Query queryByRole = queryParserByRole.parse(StringHandlerUtils.bulidPhraseQueryString(queryField, employeeName));
        filterBooleanQuery.add(queryByRole, Occur.MUST);
        if (!role.equals(RoleNameConstants.TRAINING_MASTER)) {
            filterBooleanQuery.add(new TermQuery(new Term(IndexFieldConstants.PLAN_IS_PUBLISHED,
                    FlagConstants.IS_PUBLISHED + "")), Occur.MUST);
        }
        
    }

    @Override
    public String getTraineeList(Set<PlanEmployeeMap> pemList, int attendType) {
        String traineeList = "";
        List<String> aList = new ArrayList<String>();
            for (PlanEmployeeMap pem : pemList) {
                if (pem.getPlanTraineeAttendType() == attendType && pem.getPlanEmployeeIsDeleted() == FlagConstants.UN_DELETED) {
                    aList.add(pem.getEmployee().getAugUserName());
                }
            }
            if (aList.size() == 1) {
                return aList.get(0);
            } else if (aList.size() > 1) {
                for (int i = 0; i < aList.size(); i++) {
                    if (i < aList.size() - 1) {
                        traineeList += aList.get(i) + "; ";
                    } else {
                        traineeList += aList.get(i);
                    }
                }
            }
        return traineeList;
    }

    @Override
    public int getNextRecordFlag(FromSearchToViewCondition fstvc) {
        String[] backupIds = fstvc.getBackupId().split(FlagConstants.SPLIT_COMMA);
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

    @Override
    public int getPreviousRecordFlag(FromSearchToViewCondition fstvc) {
        String[] backupIds = fstvc.getBackupId().split(FlagConstants.SPLIT_COMMA);
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
    public List<Integer> searchIdByConditionInIndex(PlanSearchCondition psc) throws ServerErrorException {
        String[] searchFieldsStore=psc.getSearchFields();
        String queryStringStore=psc.getQueryString();
        List<Integer> ids = null;
        if (psc != null) {
            psc.setSearchFields(new String[] { IndexFieldConstants.PLAN_ID });
        }
        Page<Plan> planPage = null;
        try {
            planPage = searchPlansFromIndex(psc);
        } catch (ServerErrorException e) {
            logger.error(LogConstants.exceptionMessage("Search plans from index"), e);
            throw new ServerErrorException(ErrorCodeConstants.SERVER_ERROR);
        }
        List<Plan> plans = planPage.getList();
        if (plans != null && plans.size() > 0) {
            ids = new ArrayList<Integer>();
            for (Plan plan: plans) {
                ids.add(plan.getPlanId());
            }
        }
        psc.setQueryString(queryStringStore);
        psc.setSearchFields(searchFieldsStore);
        return ids;
    }

    @Override
    public String subtractRecordId(FromSearchToViewCondition fstvc,
            PlanSearchCondition psc) {
        String error = "";
        if (fstvc == null || fstvc.getBackupId() == null
                || fstvc.getBackupId().equals("")) {
            error = "conditionError";
            return error;
        }
        String[] backupIds = fstvc.getBackupId().split(FlagConstants.SPLIT_COMMA);
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
            int pageNum = Integer.parseInt(psc.getPageNum());
            pageNum -= 1;
            psc.setPageNum("" + pageNum);
            List<Integer> ids = null;
            try {
                ids = this.searchIdByConditionInIndex(psc);
            } catch (ServerErrorException e) {
                logger.error(LogConstants.exceptionMessage("Search plans id by ["+psc+"]"), e);
            }
            if (ids == null || ids.size() == 0) {
                error = "dataError";
                return error;
            }
            String newbackupId = "" + ids.get(0);
            for (int j = 1; j < ids.size(); j++) {
                newbackupId = newbackupId + FlagConstants.SPLIT_COMMA + ids.get(j);
            }
            fstvc.setBackupId(newbackupId);
            fstvc.setNowId(ids.get(ids.size() - 1));
        } else {
            fstvc.setNowId(Integer.parseInt(backupIds[i]));
        }
        return "";
    }
    
    @Override
    public String plusRecordId(FromSearchToViewCondition fstvc,
            PlanSearchCondition psc) {
        String error = "";
        if (fstvc == null || fstvc.getBackupId() == null
                || fstvc.getBackupId().equals("")) {
            error = "conditionError";
            return error;
        }
        String[] backupIds = fstvc.getBackupId().split(FlagConstants.SPLIT_COMMA);
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
            int pageNum = Integer.parseInt(psc.getPageNum());
            pageNum += 1;
            psc.setPageNum("" + pageNum);
            List<Integer> ids = null;
            try {
                ids = this.searchIdByConditionInIndex(psc);
            } catch (ServerErrorException e) {
                logger.error(LogConstants.exceptionMessage("Search plans id by ["+psc+"]"), e);
            }
            if (ids == null || ids.size() == 0) {
                error = "dataError";
                return error;
            }
            String newbackupId = "" + ids.get(0);
            for (int j = 1; j < ids.size(); j++) {
                newbackupId = newbackupId + FlagConstants.SPLIT_COMMA + ids.get(j);
            }
            fstvc.setBackupId(newbackupId);
            fstvc.setNowId(ids.get(0));
        } else {
            fstvc.setNowId(Integer.parseInt(backupIds[i]));
        }
        return error;
    }
    
    @Override
	public Page<Plan> searchMyPlanFromIndex(Employee employee,
			PlanSearchCondition psc) throws DataWarningException,
			ServerErrorException {
    	Page<Plan> plansFromIndexPage = new Page<Plan>();
        String roleNameForSearch = psc.getRoleFlag();
        // setEmployeeName
        psc.setEmployeeName(employee.getAugUserName());
        // setRoleFlag
        List<EmployeeRoleLevelMap> employeeRoles = employee.getRoleLevelsForEmployee();
        Boolean isMaster = false;
        Boolean isTrainer = false;
        Boolean isTrainee = true;
        for(EmployeeRoleLevelMap employeeRole:employeeRoles) {
            String roleName = employeeRole.getRole().getRoleName();
            if(RoleNameConstants.TRAINING_MASTER.equals(roleName)) {
                isMaster = true;
            }
            if(RoleNameConstants.TRAINER.equals(roleName)) {
                isTrainer = true;
            }
        }
        if(RoleNameConstants.TRAINING_MASTER.equals(roleNameForSearch) && isMaster) {
            psc.setRoleFlag(RoleNameConstants.TRAINING_MASTER);
        }
        if(RoleNameConstants.TRAINER.equals(roleNameForSearch) && isTrainer) {
            psc.setRoleFlag(RoleNameConstants.TRAINER);
        }
        if(RoleNameConstants.TRAINEE.equals(roleNameForSearch) && isTrainee) {
            psc.setRoleFlag(RoleNameConstants.TRAINEE); 
        }
        if(psc.getSortField() == null) {
            // sort plan by publish date, desc
            psc.setSortField(IndexFieldConstants.PLAN_PUBLISH_DATETIME);
            psc.setReverse(true);
        }
        // search plan
        plansFromIndexPage = searchPlansFromIndex(psc);
        return plansFromIndexPage;
	}
    
        /**
     * set status for every plan.
     */
    @Override
	public void queryPlanInfo(Page<Plan> planPage, PlanSearchCondition psc, Employee employee)
			throws ServerErrorException, DataWarningException {
		if (null != planPage.getList() && !(planPage.getList().isEmpty())) {
			if (psc.getRoleFlag().equals(RoleNameConstants.TRAINING_MASTER)) {
				getPlanStatusForMaster(planPage.getList(), employee);
			}
			if (psc.getRoleFlag().equals(RoleNameConstants.TRAINER)) {
				getPlanStatusForTrainerAndTrainee(planPage.getList(), employee, RoleNameConstants.TRAINER);
			}
			if (psc.getRoleFlag().equals(RoleNameConstants.TRAINEE)) {
				getPlanStatusForTrainerAndTrainee(planPage.getList(), employee, RoleNameConstants.TRAINEE);
			}
		}
	}
    
    /**
     * Get plan status for master-plan-list
     * @param planList
     * @param employee
     * @throws ServerErrorException
     */
    private void getPlanStatusForMaster(List<Plan> planList, Employee employee) throws ServerErrorException {
    	Date currentDate = new Date();
    	List<Assessment> assessmentList = null;
    	AssessmentDao assessmentDao = BeanFactory.getAssessmentDao();
    	try {
			//Get all assessments source.
			assessmentList = assessmentDao.findAssessmentsByProperty(
					JsonKeyConstants.MASTER_ID, employee.getEmployeeId(), 
					FlagConstants.TRAINING_MASTER_TO_TRAINERS, 
					DateHandlerUtils.getDateBefore(currentDate, DateFormatConstants.DAYS));
    	} catch (Exception e) {
			logger.error(LogConstants.exceptionMessage("Find To-Do Items For Master["
						+ employee.getAugUserName() + "]"), e);
			throw new ServerErrorException(ErrorCodeConstants.SERVER_ERROR);
		}	
    	for (Plan plan : planList) {
    		if (plan.getPlanIsPublish().equals(FlagConstants.UN_PUBLISHED)) {
    			plan.setStatus(FlagConstants.STATUS_RED);
    			continue;
    		}
    		if (plan.getPlanExecuteEndTime() == null) {
    			plan.setStatus(FlagConstants.STATUS_RED);
    			continue;
    		} 
    		
    		// If plan is not ended.
    		if (plan.getPlanExecuteEndTime().after(currentDate)) {
    			if (plan.getPlanIsCompleted().equals(FlagConstants.UN_COMPLETED)) {
    				if (isPlanNeedComplete(plan, currentDate)) {
        				plan.setStatus(FlagConstants.STATUS_RED);
        			} else {
        				plan.setStatus(FlagConstants.STATUS_GREEN);
        			}
    			} else {
    				plan.setStatus(FlagConstants.STATUS_GREEN);
    			}

    		} else {  // If plan is ended.
    			if (isNeedMasteAssessTrainer(assessmentList, plan, employee.getAugUserName(), currentDate)) {
    				plan.setStatus(FlagConstants.STATUS_YELLOW);
    			} else {
    				plan.setStatus(FlagConstants.STATUS_GRAY);
    			}
    		}
    	}
    }
    
    private boolean isPlanNeedComplete(Plan plan, Date currentTime) throws ServerErrorException {
    	try {
			plan = planDao.findByPrimaryKey(plan.getPlanId());
		} catch (Exception e) {
			logger.error("find plan by id wrong:", e);
			throw new ServerErrorException(ErrorCodeConstants.SERVER_ERROR);
		}
		// If plan is not ended.
		for (ActualCourse actualCourse : plan.getActualCourses()) {
			if (null == actualCourse.getCourseStartTime()) {
				return true;
			} else {
				// info of this course is not completed and this course is not started.
				if (!(isCourseCompleted(actualCourse)) && actualCourse.getCourseStartTime().after(currentTime)) {
					return true;
				}
			}
		}
		return false;
    }
    
    private boolean isCourseCompleted(ActualCourse actualCourse) {
    	if (null == actualCourse.getCourseRoomNum() 
				|| "".equals(actualCourse.getCourseRoomNum() )
				|| null == actualCourse.getCourseStartTime()
				|| null == actualCourse.getCourseEndTime()
				|| null == actualCourse.getCourseTrainer()
				|| "".equals(actualCourse.getCourseTrainer())
		) {
			return false;
		}
		return true;
    }
    
    private boolean isNeedMasteAssessTrainer(List<Assessment> assessmentList, Plan plan, String userName, Date currentTime) throws ServerErrorException  {
    	try {
			plan = planDao.findByPrimaryKey(plan.getPlanId());
		} catch (Exception e) {
			logger.error("find plan by id wrong:", e);
			throw new ServerErrorException(ErrorCodeConstants.SERVER_ERROR);
		}
    	// If plan does not need assessed
		if (plan.getNeedAssessment() == FlagConstants.UN_NEED_ASSESSMENT) {
			return false;
		}
		// If plan has courses is not end or ended 7 days already.
		if (plan.getPlanExecuteEndTime().after(currentTime)||
				DateHandlerUtils.getDaysBetweenDate(plan.getPlanExecuteEndTime(), currentTime) > DateFormatConstants.EXPIRED_DEFAULT_DAY
		) {
			return false;
		}
		//get all trainers of this plan
		Set<String> trainersSet = new HashSet<String>();
		for (ActualCourse actualCourse : plan.getActualCourses()) {
			if (!(actualCourse.getCourseTrainer().equals(userName))) {
				trainersSet.add(actualCourse.getCourseTrainer());
			}
		}
		// If the master is the only trainer of this plan.
		if (trainersSet.isEmpty()) {
			return false;
		}
		// If the assessmentList is null or empty.
		if (assessmentList == null || assessmentList.isEmpty()) {
			return true;
		}
		// If one or more trainer of this plan has been assessed
		List<Assessment> assessmentListOfPlan = new ArrayList<Assessment>();
		for (Assessment assessment : assessmentList) {
			if (assessment.getPlanId().equals(plan.getPlanId())) {
				assessmentListOfPlan.add(assessment);
			}
		}
		if (assessmentListOfPlan.isEmpty()) {
			return true;
		}
		// If all trainers of this plan have been assessed.
		for (Assessment assessment : assessmentListOfPlan) {
			if (assessment.getHasBeenAssessed() == FlagConstants.ASSESSMENT_UNASSESSED) {
				return true;
			}
		}
		return false;
    }
    
    private void getPlanStatusForTrainerAndTrainee(List<Plan> planList, Employee employee, String roleFlag) throws ServerErrorException {
    	Date currentDate = new Date();
    	Date beforExpiredDayTime = DateHandlerUtils.getDateBefore(currentDate, DateFormatConstants.DAYS);
    	List<Assessment> assessmentList = null;
    	AssessmentDao assessmentDao = BeanFactory.getAssessmentDao();
    	try {
			//Get all assessments source.
			if (roleFlag.equals(RoleNameConstants.TRAINER)) {
				assessmentList = assessmentDao.findAssessmentsByProperty(
						JsonKeyConstants.TRAINER_ID, employee.getEmployeeId(), FlagConstants.TRAINER_TO_PLAN,
						beforExpiredDayTime);
			} else if (roleFlag.equals(RoleNameConstants.TRAINEE)) {
				assessmentList = assessmentDao.findAssessmentsByProperty(
						JsonKeyConstants.TRAINEE_ID, employee.getEmployeeId(), FlagConstants.TRAINEE_TO_PLAN,
						beforExpiredDayTime);
			}
    	} catch (Exception e) {
			logger.error(LogConstants.exceptionMessage("Find To-Do Items For Master["
						+ employee.getAugUserName() + "]"), e);
			throw new ServerErrorException(ErrorCodeConstants.SERVER_ERROR);
		}
    	for (Plan plan : planList) {
    		if (plan.getPlanExecuteEndTime() == null || plan.getPlanExecuteEndTime().after(currentDate)) {
    			plan.setStatus(FlagConstants.STATUS_GREEN);
    			continue;
    		}
    		if (plan.getNeedAssessment() == FlagConstants.UN_NEED_ASSESSMENT) {
    			plan.setStatus(FlagConstants.STATUS_GRAY);
    			continue;
    		} 
    		if (DateHandlerUtils.getDaysBetweenDate(plan.getPlanExecuteEndTime(), currentDate) > DateFormatConstants.EXPIRED_DEFAULT_DAY) {
    			plan.setStatus(FlagConstants.STATUS_GRAY);
				continue;
			}
			//Current trainee is both master and trainee of this plan.
			if (plan.getPlanCreator().equals(employee.getAugUserName())) {
				plan.setStatus(FlagConstants.STATUS_GRAY);
				continue;
			}
			if (isPlanNeedAssessByUser(assessmentList, plan.getPlanId())) {
				plan.setStatus(FlagConstants.STATUS_YELLOW);
			} else {
				plan.setStatus(FlagConstants.STATUS_GRAY);
			}
			
    	}
    }
    
    /**
	 * check if this plan has been assessed 
	 * @param assessmentList
	 * @param planId
	 * @return
	 */
	private boolean isPlanNeedAssessByUser (List<Assessment> assessmentList, Integer planId) {
		for (Assessment assessment : assessmentList) {
			if (assessment.getPlanId().equals(planId)) {
				return false;
			}
		} 
		return true;
	}
	
        
    /**
     * Check whether the employee join the course of the plan.
     * @param employee
     * @param planCourse
     * @return
     */
    private boolean isEmployeeJoinActualCourse(Employee employee, ActualCourse actualCourse){
        boolean resultFromCourse=false;
        //Check whether the employee join the plan course.
        employee = employeeDao.findByPrimaryKey(employee.getEmployeeId());
        List<ActualCourse> courseList = employee.getActualCourseList();
        if (courseList.contains(actualCourse)) {
            resultFromCourse = true;
        }
        return resultFromCourse;
    }
    
    
    @Override
    public void saveEmployeeJoinOrQuitPlan(Employee employee, int planId, int actualCourseId, int isJoin)
            throws ServerErrorException, DataWarningException {
        Plan plan=planDao.findByPrimaryKey(planId);
        if (plan == null || plan.getPlanId() <= 0) {
            throw new ServerErrorException(ErrorCodeConstants.SERVER_ERROR);
        }
        employee = employeeDao.findByPrimaryKey(employee.getEmployeeId());
        List<ActualCourse> employeeHaveActualCourses = employee.getActualCourseList();
        if (actualCourseId == 0) {
            /**join all courses or quit all courses in the plan.*/
            if (isJoin == FlagConstants.JOIN) {
                //Join all
                //1.Try to get and update the related planEmployeeMap
                if (!checkIsJoinedPlan(employeeHaveActualCourses, plan)) {
                    tryToUpdateThePlanEmployee(employee, plan, true);
                }
                //2.This is a cycle.Try to get and update the related planCourseEmployeeMap
                for (ActualCourse actualCourse: plan.getActualCourses()) {
                    //Check whether the plan course is started
                    if(checkeActualCourseIsStarted(actualCourse)) {
                        String errorCode = (isJoin == FlagConstants.JOIN) ? 
                                ErrorCodeConstants.JOIN_ALL_PLAN_COURSE_STARTED : 
                                ErrorCodeConstants.QUIT_ALL_PLAN_COURSE_STARTED;
                        logger.warn(LogConstants.getLogPlanCourseByErrorCode(errorCode, actualCourse.getActualCourseId()));
                        throw new DataWarningException(errorCode);
                    }
                    if (actualCourse.getCourseEndTime().compareTo(new Date()) > 0) {
                        tryToUpdateActualCourse(employee, actualCourse, true);
                    }
                }
               return ; 
            }
            //Quit all
            //1.Try to get and update the related planEmployeeMap
            tryToUpdateThePlanEmployee(employee, plan, false);
            //2.This is a cycle.Try to get and update the related planCourseEmployeeMap
            for(ActualCourse actualCourse: plan.getActualCourses()){
                 if(actualCourse.getCourseEndTime().compareTo(new Date())>0){
                     tryToUpdateActualCourse(employee, actualCourse, false);
                  }
            }
            return ;
        }
        ActualCourseDao actualCourseDao = BeanFactory.getActualCourseDao();
        /**join one course or quit one course in the plan.*/
        ActualCourse actualCourse = actualCourseDao.findByPrimaryKey(actualCourseId);
        if (actualCourse == null || actualCourse.getActualCourseId() <= 0) {
            throw new ServerErrorException(ErrorCodeConstants.SERVER_VALIDATION_ERROR);
        }
        if(checkeActualCourseIsStarted(actualCourse)) {
            String errorCode = (isJoin == FlagConstants.JOIN) ? 
                    ErrorCodeConstants.JOIN_PLAN_COURSE_STARTED : ErrorCodeConstants.QUIT_PLAN_COURSE_STARTED;
            logger.warn(LogConstants.getLogPlanCourseByErrorCode(errorCode, actualCourseId));
            throw new DataWarningException(errorCode);
        }
        if (isJoin == FlagConstants.JOIN) {
            //Join one course
            if (!checkIsJoinedPlan(employeeHaveActualCourses, plan)) {
                //Try to get and update the related planEmployeeMap
                tryToUpdateThePlanEmployee(employee, plan, true);
            }
            //Update the employee and plan course relationship, add
            tryToUpdateActualCourse(employee, actualCourse, true);
            return ;
        }
        //Quit one course
        boolean quitFlag = employeeHaveActualCourses.remove(actualCourse);
        if (quitFlag) {
            //planEmployeeMap
            if (!checkIsJoinedPlan(employeeHaveActualCourses, plan)) {
                tryToUpdateThePlanEmployee(employee, plan, false);
            }
            //remove plan course
            tryToUpdateActualCourse(employee, actualCourse, false);
        }
    }
    
    /**
     * 
     * Check one employee whether join one course in the plan
     * 
     * @param planCourses 
     *       Current employee has courses
     * @param plan
     * @return
     */
    private boolean checkIsJoinedPlan(List<ActualCourse> actualCourses, Plan plan) {
        boolean result = false;
        for (ActualCourse course : actualCourses) {
            if (course.getPlan().equals(plan)) {
                result = true;
                break;
            }
        }
        return result;
    }
    
    /**
     * 
     * Check whether the plan course is started
     * 
     * @param planCourse
     * @return
     */
    private boolean checkeActualCourseIsStarted(ActualCourse actualCourse){
        if (actualCourse == null || actualCourse.getCourseStartTime() == null) {
            return false;
        }else if (actualCourse.getCourseStartTime().compareTo(new Date()) < 0){
            return true;
        }else {
            return false;
        }
    }
    
    /**
     * 
     * Update the plan course and index
     * 
     * @param employee
     * @param planCourse
     * @param isAdd
     */
    private void tryToUpdateActualCourse(Employee employee, ActualCourse actualCourse, boolean isAdd){
        ActualCourseDao actualCourseDao = BeanFactory.getActualCourseDao();
        if (isAdd){
            employee.getActualCourseList().add(actualCourse);
            actualCourse.getEmployeeList().add(employee);
            actualCourseDao.saveOrUpdateObjectIndex(actualCourse);
            return ;
        }
        employee.getActualCourseList().remove(actualCourse);
        actualCourse.getEmployeeList().remove(employee);
        actualCourseDao.saveOrUpdateObjectIndex(actualCourse);
    }
    
    /**
     * Try to get and update the related planEmployeeMap.
     * 
     * @param employee
     * @param plan
     * @param isJoin
     */
    private void tryToUpdateThePlanEmployee(Employee employee, Plan plan, boolean isJoin){
        // get plan trainees info
        String traineeNames = plan.getTraineeNames();
        // get this plan, this employee all planEmployeeMap
        PlanEmployeeMap planEmployeeMap = planEmployeeMapDao.
                    getPlanEmployeeMapByPlanAndEmployee(plan.getPlanId(),
                    employee.getEmployeeId(), FlagConstants.ATTEND_TYPE_JOIN);
        if (isJoin) {
            // Join
            if (planEmployeeMap == null) {
                PlanEmployeeMap newplanEmployeeMap=new PlanEmployeeMap();
                newplanEmployeeMap.setPlan(plan);
                newplanEmployeeMap.setEmployee(employee);
                newplanEmployeeMap.setPlanEmployeeIsDeleted(0);
                newplanEmployeeMap.setOperationTime(new Date());
                newplanEmployeeMap.setPlanTraineeAttendType(FlagConstants.ATTEND_TYPE_JOIN);
                plan.getPlanEmployeeMapList().add(newplanEmployeeMap);
            }else if (planEmployeeMap.getPlanEmployeeIsDeleted().equals(FlagConstants.IS_DELETED)) {
                planEmployeeMap.setPlanEmployeeIsDeleted(FlagConstants.UN_DELETED);
                planEmployeeMap.setOperationTime(new Date());
                plan.getPlanEmployeeMapList().add(planEmployeeMap);
            }
            traineeNames = (traineeNames == null || "".equals(traineeNames)) ? employee.getAugUserName() 
                    : (traineeNames + FlagConstants.SPLIT_COMMA + employee.getAugUserName());
            plan.setTraineeNames(traineeNames);
            plan.setTrainees(plan.getTrainees() + 1);
        }else {
            // Quit
            if (planEmployeeMap != null) {
                planEmployeeMap.setPlanEmployeeIsDeleted(FlagConstants.IS_DELETED);
                planEmployeeMap.setOperationTime(new Date());
                plan.getPlanEmployeeMapList().add(planEmployeeMap);
            }
            if (traineeNames != null && !"".equals(traineeNames)) {
                plan.setTraineeNames(traineeNames.replace(employee.getAugUserName(), ""));
                plan.setTrainees(plan.getTrainees() - 1);
            }
        }
        logger.info("The plan[#"+plan.getPlanId()+"] trainee names :" + plan.getTraineeNames());
        planDao.saveOrUpdateObjectIndex(plan);
    }
    
    private void dealWithDataWarningException(String code, Object value) throws DataWarningException {
        logger.warn(LogConstants.getLogPlanByErrorCode(code, value));
        throw new DataWarningException(code);
    }

    @Override
    public Plan findPlanByIdForViewAssessment(Integer planId)
            throws DataWarningException, ServerErrorException {
        /*
         * 1. Get the plan by planId.
         * 2. Get the planCourseList from database.
         * 3. Get the traineeList from database.
         * 4. Return the instance of Plan.
         * 
         */
        Plan plan = null;
        if (planId == null || planId <= 0) {
            // The planId is null or is smaller than 0.
            logger.error(LogConstants.objectIsNULLOrEmpty("Plan Id"));
            throw new ServerErrorException(ErrorCodeConstants.SERVER_VALIDATION_ERROR);
        } else {
            
            // Get the plan by planId.
            plan = planDao.findByPrimaryKey(planId);
            if (plan == null) {
                logger.error(LogConstants.objectIsNULLOrEmpty("Plan[id == " + planId + "]"));
                throw new ServerErrorException(FlagConstants.ERROR_SERVER);
            } else {
             // Get the planCourseList from database.
                List<ActualCourse> actualCourseList = plan.getActualCourses();
                if (null != actualCourseList && !actualCourseList.isEmpty()) {
                    actualCourseList.get(0).getActualCourseId();
                }
                // Get the traineeList from database. plan-->planEmployeeMapList-->employee.
                Set<PlanEmployeeMap> planEmployeeMapList = plan.getPlanEmployeeMapList();
                if (planEmployeeMapList != null && !planEmployeeMapList.isEmpty()) {
                    for (PlanEmployeeMap planEmployeeMap: planEmployeeMapList) {
                        if (planEmployeeMap != null && 
                                new Integer(FlagConstants.UN_DELETED).equals(planEmployeeMap.getPlanEmployeeIsDeleted())) {
                            // If the planEmployeeMap is null or is deleted, don't get it's information from database.
                            planEmployeeMap.getEmployee();
                        } 
                    }
                }
            }
        }
        return plan;
    }

    @Override
    public Map<String, Object> findTrainerAndActualCourseListByPlanIdForViewAssessment(
            Integer planId) throws DataWarningException, ServerErrorException {
        Map<String, Object> trainerAndPlanCourseMap = new HashMap<String, Object>();
        /*
         * 1. Get the plan by planId.
         * 2. Get all trainer name from planCourse of plan and put them to a collection.
         * 3. Get the trainer list by trainer name collection.
         * 4. Put the trainerList and planCourseList into trainerAndPlanCourseMap.
         */
        
        if (planId == null || planId <= 0) {
            // The planId is null or is smaller than 0.
            logger.error(LogConstants.objectIsNULLOrEmpty("Plan Id"));
            throw new ServerErrorException(ErrorCodeConstants.SERVER_ERROR);
        } else {
            List<Employee> trainerList = null;
            // Get the plan by planId.
            Plan plan = planDao.findByPrimaryKey(planId);
            if (plan == null) {
                logger.error(LogConstants.objectIsNULLOrEmpty("Plan[#" + planId + "]"));
                throw new ServerErrorException(FlagConstants.ERROR_SERVER);
            } else {
                // Get all trainer name from planCourse of plan and put them to a collection.
                Set<Object> trainerSet = getTrainerSetFromPlan(plan);
                // Get the trainer list by trainer name collection.
                trainerList = employeeDao.findEntityListByPropertyList(FlagConstants.EMPLOYEE_NAME, trainerSet);
                
                // 4. Put the trainerList and planCourseList into trainerAndPlanCourseMap.
                trainerAndPlanCourseMap.put(JsonKeyConstants.TRAINER_LIST, trainerList);
                trainerAndPlanCourseMap.put(JsonKeyConstants.ACTUAL_COURSE_LIST_OF_TRAINER, plan.getActualCourses());
            }
            
        }
        return trainerAndPlanCourseMap;
    }
    
    /**
     * Get all trainer name from planCourse of plan and put them to a collection.
     * 
     * @param plan
     * @return  The trainer collection.
     */
    private Set<Object> getTrainerSetFromPlan(Plan plan) {
        Set<Object> trainerSet = null;
        // Get all trainer name from planCourse of plan and put them to a collection.
        List<ActualCourse> actualCourseList = plan.getActualCourses();
        if (actualCourseList != null && !actualCourseList.isEmpty()) {
            // Use Set because the set can not contains the same object.
            trainerSet = new HashSet<Object>();
            String trainer = null;
            for (ActualCourse actualCourse: actualCourseList) {
                trainer = actualCourse.getCourseTrainer();
                if (trainer != null && !trainer.isEmpty()) {
                    trainerSet.add(trainer);
                }
            }
        }
        return trainerSet;
    }

    @Override
    public Page<Plan> getNewPublishPlan(int pageNow, int pageSize, Employee employee)
            throws ServerErrorException {
        if (pageNow == 0 || pageSize == 0) {
            logger.error(LogConstants.objectIsNULLOrEmpty("PageNow or pageSize"));
            throw new ServerErrorException(ErrorCodeConstants.SERVER_ERROR);
        }
        List<Plan> planList = null;
        List<Plan> newPlanList = new ArrayList<Plan>();
        int counts;
        try {
            planList = planDao.getNewPlanList();
            counts = planDao.getNewPlanNumber();
            
            // add specific condition
            for (Plan plan:planList) {
                if (plan.getIsAllEmployee().equals(FlagConstants.UN_ALL_EMPLOYEES)) {
                    boolean isSpecific = false;
                    PlanEmployeeMapDao planEmployeeMapDao = BeanFactory.getPlanEmployeeMapDao();
                    List<PlanEmployeeMap> pems = planEmployeeMapDao.getPlanEmployeeMaps(plan.getPlanId(), 
                            FlagConstants.ATTEND_TYPE_SPECIFIC, FlagConstants.UN_DELETED);
                    for (PlanEmployeeMap pem : pems) {
                        if (pem.getEmployee().equals(employee)) {
                            isSpecific = true;
                            newPlanList.add(plan);
                            break;
                        }
                    }
                    if (!isSpecific) {
                        counts--;
                    }
                } else{
                    newPlanList.add(plan);
                }
                
            }
        }catch(Exception e) {
            logger.error(LogConstants.exceptionMessage("Get new publish plan or get new publish count"), e);
            throw new ServerErrorException(ErrorCodeConstants.SERVER_ERROR);
        }
        // paging
        Page<Plan> plans = new Page<Plan>();
        try {
            plans = plans.listToPage(newPlanList, pageNow, pageSize);
            plans.setTotalRecords(counts);
        } catch (DataWarningException e) {
            logger.error(e);
        }
        return plans;
    }

    /**
     * Get course list for new publish plan
     */
    @Override
    public List<CourseItem> getCourseListForPublishPlan(int planId, Employee employee)
            throws ServerErrorException, DataWarningException {
        Plan plan = planDao.findByPrimaryKey(planId);
        List<CourseItem> itemList = new ArrayList<CourseItem>();
        for(ActualCourse actualCourse : plan.getActualCourses()){
            /**
             *  1. actual course must be course
             *  2. course time can be null or after current time
             */
            if (actualCourse.getPrefixIdValue().indexOf(FlagConstants.PLAN_COURSE_NUMBER_PREFIX) >=0 
                    && (actualCourse.getCourseStartTime() == null || actualCourse.getCourseStartTime().compareTo(new Date()) > 0)) {
                CourseItem item = new CourseItem();
                item.setCourseId(actualCourse.getActualCourseId());
                item.setCourseName( actualCourse.getCourseName());
                String startTime = DateHandlerUtils.dateToString(DateFormatConstants.YYYY_MM_DD_HH_MM,
                        actualCourse.getCourseStartTime());
                String endTime = DateHandlerUtils.dateToString(DateFormatConstants.HH_MM,
                        actualCourse.getCourseEndTime());
                item.setTime(startTime + "-" + endTime);
                if (isEmployeeJoinActualCourse(employee, actualCourse)) {
                    item.setIsJoinCourse(FlagConstants.JOIN);
                }else {
                    item.setIsJoinCourse(FlagConstants.QUIT);
                }
                itemList.add(item);
            }
         }
        return itemList;
    }
    
    public ActualCourseDao getActualCourseDao() {
        return actualCourseDao;
    }

    @Resource
    public void setActualCourseDao(ActualCourseDao actualCourseDao) {
        this.actualCourseDao = actualCourseDao;
    }

    @Resource
    private PlanEmployeeMapDao planEmployeeMapDao;
    
    public PlanDao getPlanDao() {
        return planDao;
    }
    
    @Resource
    public void setPlanDao(PlanDao planDao) {
        this.planDao = planDao;
    }
    
    public EmployeeDao getEmployeeDao() {
         return employeeDao;
    }
       
    @Resource
    public void setEmployeeDao(EmployeeDao employeeDao) {
        this.employeeDao = employeeDao;
    }
    

    public CourseDao getCourseDao() {
        return courseDao;
    }

    @Resource
    public void setCourseDao(CourseDao courseDao) {
        this.courseDao = courseDao;
    }

}
