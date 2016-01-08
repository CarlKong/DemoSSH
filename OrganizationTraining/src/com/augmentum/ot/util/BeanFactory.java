package com.augmentum.ot.util;

import java.sql.Connection;
import java.sql.SQLException;

import javax.servlet.ServletContext;

import org.hibernate.SessionFactory;
import org.hibernate.connection.ConnectionProvider;
import org.hibernate.engine.SessionFactoryImplementor;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.web.context.ContextLoader;

import com.augmentum.ot.dao.ActualCourseAttachmentDao;
import com.augmentum.ot.dao.ActualCourseDao;
import com.augmentum.ot.dao.AssessmentDao;
import com.augmentum.ot.dao.AssessmentItemDao;
import com.augmentum.ot.dao.AssessmentScoreDao;
import com.augmentum.ot.dao.CourseAttachmentDao;
import com.augmentum.ot.dao.CourseDao;
import com.augmentum.ot.dao.CourseInfoDao;
import com.augmentum.ot.dao.CourseTagDao;
import com.augmentum.ot.dao.CourseTypeDao;
import com.augmentum.ot.dao.EmployeeDao;
import com.augmentum.ot.dao.EmployeeRoleLevelMapDao;
import com.augmentum.ot.dao.ImageAttachmentDao;
import com.augmentum.ot.dao.PlanAttachmentDao;
import com.augmentum.ot.dao.PlanDao;
import com.augmentum.ot.dao.PlanEmployeeMapDao;
import com.augmentum.ot.dao.PlanTypeDao;
import com.augmentum.ot.dao.PrivilegeDao;
import com.augmentum.ot.dao.RoleDao;
import com.augmentum.ot.dao.SessionInfoDao;
import com.augmentum.ot.dataObject.constant.BeanNameConstants;
import com.augmentum.ot.remoteService.IRemoteEmailService;
import com.augmentum.ot.service.ActualCourseService;
import com.augmentum.ot.service.AssessmentItemService;
import com.augmentum.ot.service.AssessmentItemTypeService;
import com.augmentum.ot.service.AssessmentScoreService;
import com.augmentum.ot.service.AssessmentService;
import com.augmentum.ot.service.CourseAttachmentService;
import com.augmentum.ot.service.CourseService;
import com.augmentum.ot.service.CourseTagService;
import com.augmentum.ot.service.CourseTypeService;
import com.augmentum.ot.service.EmployeeRoleLevelMapService;
import com.augmentum.ot.service.EmployeeService;
import com.augmentum.ot.service.LeaveNoteService;
import com.augmentum.ot.service.PlanAttachmentService;
import com.augmentum.ot.service.PlanEmployeeMapService;
import com.augmentum.ot.service.PlanService;
import com.augmentum.ot.service.PlanTagService;
import com.augmentum.ot.service.PlanTypeService;
import com.augmentum.ot.service.PrivilegeService;
/**
 * 
 * Get each module instance
 *
 * @version V1.0, 2012-8-24
 */
public class BeanFactory {
	//--------------------------------DAO--------------------------
	public static AssessmentDao getAssessmentDao() {
		return SpringContextUtils.getBean(BeanNameConstants.ASSESSMENT_DAO);
	}
	
	public static AssessmentScoreDao getAssessmentScoreDao() {
		return SpringContextUtils.getBean(BeanNameConstants.ASSESSMENT_SCORE_DAO);
	}
	
	public static AssessmentItemDao getAssessmentItemDao() {
		return SpringContextUtils.getBean(BeanNameConstants.ASSESSMENT_ITEM_DAO);
	}
	
	public static CourseDao getCourseDao() {
		return SpringContextUtils.getBean(BeanNameConstants.COURSE_DAO);
	}
	
	public static CourseTagDao getCourseTagDao() {
		return SpringContextUtils.getBean(BeanNameConstants.COURSE_TAG_DAO);
	}
	
	public static CourseTypeDao getCourseTypeDao() {
		return SpringContextUtils.getBean(BeanNameConstants.COURSE_TYPE_DAO);
	}
	
	public static CourseAttachmentDao getCourseAttachmentDao() {
		return SpringContextUtils.getBean(BeanNameConstants.COURSE_ATTACHMENT_DAO);
	}
	
	public static EmployeeDao getEmployeeDao() {
		return SpringContextUtils.getBean(BeanNameConstants.EMPLOYEE_DAO);
	}
	
	public static EmployeeRoleLevelMapDao getEmployeeRoleLevelMapDao() {
		return SpringContextUtils.getBean(BeanNameConstants.EMPLOYEE_ROLE_LEVEL_MAP_DAO);
	}
	
	public static PlanAttachmentDao getPlanAttachmentDao() {
		return SpringContextUtils.getBean(BeanNameConstants.PLAN_ATTACHMENT_DAO);
	}
	
	public static PlanEmployeeMapDao getPlanEmployeeMapDao() {
		return SpringContextUtils.getBean(BeanNameConstants.PLAN_EMPLOYEE_MAP_DAO);
	}
	
	public static PlanTypeDao getPlanTypeDao() {
		return SpringContextUtils.getBean(BeanNameConstants.PLAN_TYPE_DAO);
	}
	
	public static PlanDao getPlanDao() {
		return SpringContextUtils.getBean(BeanNameConstants.PLAN_DAO);
	}
	
	public static PrivilegeDao getPrivilegeDao() {
		return SpringContextUtils.getBean(BeanNameConstants.PRIVILEGE_DAO);
	}
	
	public static RoleDao getRoleDao() {
		return SpringContextUtils.getBean(BeanNameConstants.ROLE_DAO);
	}
	
	public static ImageAttachmentDao getImageAttachmentDao() {
		return SpringContextUtils.getBean(BeanNameConstants.IMAGE_ATTACHMENT_DAO);
	}
	
	public static ActualCourseDao getActualCourseDao() {
	    return SpringContextUtils.getBean(BeanNameConstants.ACTUAL_COURSE_DAO);
	}
	
	public static CourseInfoDao getCourseInfoDao() {
	    return SpringContextUtils.getBean(BeanNameConstants.COURSE_INFO_DAO);
	}
	
	public static SessionInfoDao getSessionInfoDao() {
	    return SpringContextUtils.getBean(BeanNameConstants.SESSION_INFO_DAO);
	}
	
	public static ActualCourseAttachmentDao getActualCourseAttachmentDao() {
	    return SpringContextUtils.getBean(BeanNameConstants.ACTUAL_COURSE_ATTACHMENT_DAO);
	}
	
	//--------------------------------Service--------------------------
	public static CourseService getCourseService() {
		return SpringContextUtils.getBean(BeanNameConstants.COURSE_SERVICE);
	}
	
	public static CourseTagService getCourseTagService() {
		return SpringContextUtils.getBean(BeanNameConstants.COURSE_TAG_SERVICE);
	}
	
	public static CourseTypeService getCourseTypeService() {
		return SpringContextUtils.getBean(BeanNameConstants.COURSE_TYPE_SERVICE);
	}
	
	public static CourseAttachmentService getCourseAttachmentService() {
		return SpringContextUtils.getBean(BeanNameConstants.COURSE_ATTACHMENT_SERVICE);
	}
	
	public static EmployeeRoleLevelMapService getEmployeeRoleLevelMapService() {
		return SpringContextUtils.getBean(BeanNameConstants.EMPLOYEE_ROLE_LEVEL_MAP_SERVICE);
	}
	
	public static EmployeeService getEmployeeService() {
		return SpringContextUtils.getBean(BeanNameConstants.EMPLOYEE_SERVICE);
	}
	
	public static PlanAttachmentService getPlanAttachmentService() {
		return SpringContextUtils.getBean(BeanNameConstants.PLAN_ATTACHMENT_SERVICE);
	}
	
	public static PlanService getPlanService() {
		return SpringContextUtils.getBean(BeanNameConstants.PLAN_SERVICE);
	}
	
	public static PlanTagService getPlanTagService() {
		return SpringContextUtils.getBean(BeanNameConstants.PLAN_TAG_SERVICE);
	}
	
	public static PlanTypeService getPlanTypeService() {
		return SpringContextUtils.getBean(BeanNameConstants.PLAN_TYPE_SERVICE);
	}
	
	public static PlanEmployeeMapService getPlanEmployeeMapService() {
        return SpringContextUtils.getBean(BeanNameConstants.PLAN_EMPLOYEE_MAP_SERVICE);
    }
	
	public static PrivilegeService getPrivilegeService() {
		return SpringContextUtils.getBean(BeanNameConstants.PRIVILEGE_SERVICE);
	}
	
	public static IRemoteEmailService getIRemoteEmailService() {
		return SpringContextUtils.getBean(BeanNameConstants.REMOTE_EMIAL_SERVICE);
	}
	
	public static AssessmentService getAssessmentService() {
	    return SpringContextUtils.getBean(BeanNameConstants.ASSESSMENT_SERVICE);
	}
	
	public static AssessmentItemService getAssessmentItemService(){
	    return SpringContextUtils.getBean(BeanNameConstants.ASSESSMENT_ITEM_SERVICE);
	}
	
	public static AssessmentItemTypeService getAssessmentItemTypeService(){
	    return SpringContextUtils.getBean(BeanNameConstants.ASSESSMENT_ITEM_TYPE_SERVICE);
	}
	
	public static AssessmentScoreService getAssessmentScoreService(){
	    return SpringContextUtils.getBean(BeanNameConstants.ASSESSMENT_SCORE_SERVICE);
	}
	
	public static LeaveNoteService getLeaveNoteService(){ 
	    return SpringContextUtils.getBean(BeanNameConstants.LEAVE_SERVICE);
	}	

	public static ActualCourseService getActualCourseService(){
	    return SpringContextUtils.getBean(BeanNameConstants.ACTUAL_COURSE_SERVICE);
	}

	public static ServletContext getApplication(){
		return ContextLoader.getCurrentWebApplicationContext().getServletContext();
	}
	
	public static Connection getConnection() throws SQLException {
		SessionFactory sessionFactory = SpringContextUtils.
				getBean(BeanNameConstants.SESSION_FACTORY);
		ConnectionProvider cp = ((SessionFactoryImplementor) sessionFactory)
				.getConnectionProvider();
		Connection conn = cp.getConnection();
		return conn;
	}

    public static JavaMailSenderImpl getJavaMailSenderImpl() {
        return SpringContextUtils.getBean(BeanNameConstants.JAVA_MAIL_SENDER);
    }
}
