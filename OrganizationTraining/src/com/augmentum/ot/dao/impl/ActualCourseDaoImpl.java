package com.augmentum.ot.dao.impl;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.apache.lucene.search.QueryWrapperFilter;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.search.FullTextQuery;
import org.hibernate.search.FullTextSession;
import org.hibernate.search.Search;
import org.springframework.stereotype.Component;

import com.augmentum.ot.dao.ActualCourseDao;
import com.augmentum.ot.dataObject.constant.DateFormatConstants;
import com.augmentum.ot.dataObject.constant.FlagConstants;
import com.augmentum.ot.model.ActualCourse;
import com.augmentum.ot.util.DateHandlerUtils;



/**
 * 
 * @version 0.1, 12/18/2012
 */
@Component("actualCourseDao")
public class ActualCourseDaoImpl extends BaseDaoImpl<ActualCourse> implements
        ActualCourseDao {

    @Override
    public Class<ActualCourse> getEntityClass() {
        return ActualCourse.class;
    }

	@SuppressWarnings("unchecked")
	@Override
	public List<ActualCourse> findActualCoursesByPlanId(Integer id) {
		Session session=this.getSession();
		Query query = session.createQuery("from "+getEntityClass().getSimpleName()+" where plan.planId=:planId");
		query.setInteger("planId", id);
		List<ActualCourse> actualCourses = query.list();
		return actualCourses;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ActualCourse> findActualCoursesByCourseId(Integer id) {
		Session session = this.getSession();
		String sql = "select * from actual_course as ac where ac.course_info_id in (select id from course_info as ci where ci.original_course_id = :courseId)";
		Query query = session.createSQLQuery(sql)
			.addEntity(getEntityClass())
			.setInteger("courseId", id);
		return query.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ActualCourse> queryActualCourseByTrainer(String trainerName) {
		return this.getSession().createQuery("from ActualCourse where courseTrainer=:trainer")
    	.setString("trainer", trainerName).list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ActualCourse> findEntityListByRemideTimer() {
	    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
	    String nowTime = simpleDateFormat.format(new Date());
	    nowTime = nowTime.substring(0, nowTime.length()-2);
		org.hibernate.Query query = this.getSession().createQuery("from ActualCourse as ac where " +
    			" ac.plan.planIsPublish = " + FlagConstants.IS_PUBLISHED +
    			" and ac.plan.planIsDeleted = " + FlagConstants.UN_DELETED +
    			" and ac.plan.planIsCanceled = " + FlagConstants.UN_CANCELED +
    			" and ac.plan.reminderEmail > 0" + 
    			" and ac.courseStartTime != null and ac.courseStartTime >= :nowTime" + 
    			" and ac.courseEndTime != null").setString("nowTime", nowTime);
    	List<ActualCourse> entityList = null; 
    	entityList = query.list();
    	return entityList;
	}

	@Override
	public int getNumByIndex(org.apache.lucene.search.Query query,
			QueryWrapperFilter filter) {
		FullTextSession ftSession = Search.getFullTextSession(getSession());
        FullTextQuery fullTextQuery = ftSession.createFullTextQuery(query, ActualCourse.class);
        if (null != filter) {
            fullTextQuery.setFilter(filter);
        }
        int resultSize = fullTextQuery.getResultSize();
        Transaction tr = ftSession.beginTransaction();
        tr.commit();
        ftSession.close();
        return resultSize;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ActualCourse> queryActualCourseFromIndex(org.apache.lucene.search.Query query, 
			String sortField, Boolean reverse, QueryWrapperFilter filter,
			int firstResult, int maxResult) {
		 FullTextSession ftSession = Search.getFullTextSession(this.getSession());
	     FullTextQuery fullTextQuery = ftSession.createFullTextQuery(query, ActualCourse.class);
	     if(null != filter) {
	         fullTextQuery.setFilter(filter);
	     }
	     // Sort
	     Sort sort = null;
	     if(null != sortField && !"".equals(sortField)) {
	            if (sortField.endsWith("_zh")) {
	                // This sort is by Chinese locale.
	                Locale locale = Locale.CHINESE;
	                sort = new Sort(new SortField(sortField, locale, reverse));
	            } else {
	                sort = new Sort(new SortField(sortField, SortField.STRING, reverse));
	            }
	        } else {
	        	sort = new Sort();
	     }
	     fullTextQuery.setSort(sort);
	     fullTextQuery.setFirstResult(firstResult);
	     fullTextQuery.setMaxResults(maxResult);
	     List<ActualCourse> actualCourseList = fullTextQuery.list();
	     for (ActualCourse actualCourse : actualCourseList) {
	    	 actualCourse.getTraineeNumber();
	     }
	     Transaction tr = ftSession.beginTransaction();
	     tr.commit();
	     ftSession.close();
	     return actualCourseList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ActualCourse> getActualCoursesInOnePlanByTrainer(
			String trainerName, int planId) {
		Query query = this.getSession().createQuery("from ActualCourse as ac where " +
    			" ac.plan.planId =:planId " +
    			" and ac.courseTrainer=:trainerName" +
    			" and ac.plan.planIsDeleted =:planIsDeleted " +
    			" and ac.courseInfo.courseInfoId is not null")
    			.setInteger("planId", planId)
    			.setString("trainerName", trainerName)
    			.setInteger("planIsDeleted", FlagConstants.UN_DELETED);
    	List<ActualCourse> entityList = null;
    	entityList = query.list();
    	return entityList;
	}
	

	/**
	 * 1. Trainer's name of this course is trainerName
	 * 2. Plan of this actualCourse is not deleted or canceled
	 *    Plan of this actualCourse is published
	 * 3. This actual course is a plan course not a session and need assessment
	 * 4. this actual course is ended and in 8 days.
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<ActualCourse> getActualCourseListOperateByTrainer(
			String trainerName, Date currentDate, Date validateDate) {
		List<ActualCourse> actualCourseList = new ArrayList<ActualCourse>();
		Query query=getSession().createQuery("from "+getEntityClass().getSimpleName()+ " as ac where " + 
				"ac.courseTrainer=:trainerName and " +
				"ac.plan.planIsDeleted=:planIsNotDeleted and " +
				"ac.plan.planIsCanceled=:planIsNotCanceled and " +
				"ac.plan.planIsPublish=:planIsPublish and " +
				"ac.courseInfo is not null and " +
				"ac.courseInfo.trainer2Trainee=:needAssessment and " +
				"ac.courseEndTime between :validateDate and :currentTime"
				);
		actualCourseList = query.setString("trainerName", trainerName)
			.setInteger("planIsNotDeleted", FlagConstants.UN_DELETED)
			.setInteger("planIsNotCanceled", FlagConstants.UN_CANCELED)
			.setInteger("planIsPublish", FlagConstants.IS_PUBLISHED)
			.setString("currentTime", DateHandlerUtils.dateToString(DateFormatConstants.TO_DATABASE, currentDate))
			.setString("validateDate", DateHandlerUtils.dateToString(DateFormatConstants.TO_DATABASE, validateDate))
			.setInteger("needAssessment", FlagConstants.NEED_ASSESSMENT)
			.list();
		return actualCourseList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ActualCourse> getActualCourseListOperateByTrainee(
			String traineeName, Date currentDate, Date validateDate) {
		List<ActualCourse> actualCourseList = new ArrayList<ActualCourse>();
		Query query = getSession().createQuery("select distinct ac from "+getEntityClass().getSimpleName()+ " ac" +
				" inner join ac.employeeList employee where employee.augUserName=:userName and " +
				"ac.plan.planIsDeleted=:planIsNotDeleted and " + 
				"ac.plan.planIsCanceled=:planIsNotCanceled and " +
				"ac.plan.planIsPublish=:planIsPublish and " +
				"ac.courseInfo.trainer2Trainee=:needAssessment and " +
				"ac.courseEndTime between :validateDate and :currentTime" );
		actualCourseList=query.setString("userName", traineeName)
			.setInteger("planIsNotDeleted", FlagConstants.UN_DELETED)
			.setInteger("planIsNotCanceled", FlagConstants.UN_CANCELED)
			.setInteger("planIsPublish", FlagConstants.IS_PUBLISHED)
			.setString("currentTime", DateHandlerUtils.dateToString(DateFormatConstants.TO_DATABASE, currentDate))
			.setString("validateDate", DateHandlerUtils.dateToString(DateFormatConstants.TO_DATABASE, validateDate))
			.setInteger("needAssessment", FlagConstants.NEED_ASSESSMENT)
			.list();
		return actualCourseList;
	}

}
