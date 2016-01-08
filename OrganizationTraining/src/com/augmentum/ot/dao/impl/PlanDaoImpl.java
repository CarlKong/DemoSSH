package com.augmentum.ot.dao.impl;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Component;

import com.augmentum.ot.dao.PlanDao;
import com.augmentum.ot.dataObject.constant.DateFormatConstants;
import com.augmentum.ot.dataObject.constant.FlagConstants;
import com.augmentum.ot.model.Plan;
import com.augmentum.ot.util.DateHandlerUtils;

/**
 * 
 * @version 0.1, 07/13/2012
 */
@Component("planDao")
public class PlanDaoImpl extends BaseDaoImpl<Plan> implements PlanDao {

    @Override
    public Class<Plan> getEntityClass() {
        return Plan.class;
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public List<Plan> getNewPlanList(){
        List<Plan> planList=new ArrayList<Plan>();
        Query query=getSession().createQuery("from "+getEntityClass().getSimpleName()+
                " as plan where plan.planType.planTypeName=:planTypeName and " +
                " plan.planIsPublish=:planIsPublish and " +
                " plan.planIsDeleted=:planIsNotDeleted and" +
                " plan.planIsCanceled=:planIsNotCanceled and" +
                " ( " +
                "(plan.planExecuteEndTime is null)" +
                "or" +
                "((select max(actualCourse.courseStartTime) from ActualCourse as actualCourse " +
                " where actualCourse.plan=plan)>current_timestamp())" +
                " ) " +
                "order by plan.planExecuteStartTime desc");
        planList=query.setString("planTypeName",FlagConstants.PUBLIC_PLAN)
        		.setInteger("planIsPublish", FlagConstants.IS_PUBLISHED).setInteger("planIsNotDeleted", FlagConstants.UN_DELETED).setInteger("planIsNotCanceled", FlagConstants.UN_CANCELED).list();
        return planList;
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public int getNewPlanNumber(){
        int num=0;
        List<Plan> planList=new ArrayList<Plan>();
        Query query=getSession().createQuery("from "+getEntityClass().getSimpleName()+
                " as plan where plan.planType.planTypeName=:planTypeName and " +
                " plan.planIsPublish=:planIsPublish and " +
                " plan.planIsDeleted=:planIsNotDeleted and" +
                " plan.planIsCanceled=:planIsNotCanceled and" +
                " ( " +
                "(plan.planExecuteEndTime is null)" +
                "or" +
                "((select max(actualCourse.courseStartTime) from ActualCourse as actualCourse " +
                " where actualCourse.plan=plan)>current_timestamp())" +
                " ) " );
        planList=query.setString("planTypeName",FlagConstants.PUBLIC_PLAN)
        			.setInteger("planIsPublish", FlagConstants.IS_PUBLISHED).setInteger("planIsNotDeleted", FlagConstants.UN_DELETED).setInteger("planIsNotCanceled", FlagConstants.UN_CANCELED).list();
        num=planList.size();
        return num;
    }
    
	@SuppressWarnings("unchecked")
	@Override
	public List<Plan> findPlanByCreator(String creatorName) {
		Query query = getSession().createQuery("from Plan as plan where plan.planCreator=:creator " +
				" and plan.planIsDeleted=:planIsDeleted and plan.planIsCanceled=:planIsCanceled");
		query.setString("creator", creatorName).setInteger("planIsDeleted", FlagConstants.UN_DELETED)
			 .setInteger("planIsCanceled", FlagConstants.UN_CANCELED);
		return query.list();
	}
	
	/**
	 * All conditions:
	 * 1. Creator
	 * 2. Not canceled and not deleted.
	 * 3. Published : 
	 *    1) Not published: All.
	 *    2) Is published: Plan end time is null:
	 *       a. Is null : All.
	 *       b. Not null : Finished: 
	 *          a). Not finished: not complete.
	 *          b). Is finished: in 8 days.
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Plan> getPlanListNeedOperate(String creator, Date currentDate, Date validateDate) {
		List<Plan> planList=new ArrayList<Plan>();
        Query query = getSession().createQuery("from "+getEntityClass().getSimpleName()+
                " as plan where plan.planCreator=:creator and " +
                " plan.planIsDeleted=:planIsNotDeleted and " +
                " plan.planIsCanceled=:planIsNotCanceled and " +
                " ( " +
                    " plan.planIsPublish=:planIsNotPublish " +
                    "or" +
                    " plan.planIsPublish=:planIsPublish and " +
                    "(plan.planExecuteEndTime is null or " +
                       "plan.planExecuteEndTime is not null and " +
                       "(plan.planExecuteEndTime>:currentTime and plan.planIsCompleted=:notCompleted or " +
                       "plan.planExecuteEndTime<=:currentTime and plan.planExecuteEndTime>:validateDate" +
                       " and plan.needAssessment=:needAssessment)" +
                   ")" +
                " ) ");
        planList = query.setString("creator", creator)
        		.setInteger("planIsNotDeleted", FlagConstants.UN_DELETED)
        		.setInteger("planIsNotCanceled", FlagConstants.UN_CANCELED)
        		.setInteger("planIsNotPublish", FlagConstants.UN_PUBLISHED)
        		.setInteger("planIsPublish", FlagConstants.IS_PUBLISHED)
        		.setInteger("notCompleted", FlagConstants.UN_COMPLETED)
        		.setString("currentTime", DateHandlerUtils.dateToString(DateFormatConstants.TO_DATABASE, currentDate))
        		.setString("validateDate", DateHandlerUtils.dateToString(DateFormatConstants.TO_DATABASE, validateDate))
        		.setInteger("needAssessment", FlagConstants.NEED_ASSESSMENT)
        		.list();
        return planList;
	}
	
	/**
	 * All conditions
	 * 1. In trainerNames field include trainerName.
	 * 2. Not canceled and not deleted.
	 * 3. Already finished and finished in 8 days.
	 * 4. Need Assessment
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Plan> getPlanListOperateByTrainer(String trainerName,
			Date currentDate, Date validateDate) {
		List<Plan> planList=new ArrayList<Plan>();
        Query query = getSession().createQuery("from "+getEntityClass().getSimpleName()+
                " as plan where plan.trainers like :trainerName and " +
                " plan.planIsDeleted=:planIsNotDeleted and " +
                " plan.planIsCanceled=:planIsNotCanceled and " +
                " plan.planIsPublish=:planIsPublish and " +
                "plan.planExecuteEndTime is not null and " +
                "plan.planExecuteEndTime<=:currentTime and plan.planExecuteEndTime>:validateDate" +
                " and plan.needAssessment=:needAssessment)"
        );
        planList = query.setString("trainerName", "%" + trainerName + "%")
        		.setInteger("planIsNotDeleted", FlagConstants.UN_DELETED)
        		.setInteger("planIsNotCanceled", FlagConstants.UN_CANCELED)
        		.setInteger("planIsPublish", FlagConstants.IS_PUBLISHED)
        		.setString("currentTime", DateHandlerUtils.dateToString(DateFormatConstants.TO_DATABASE, currentDate))
        		.setString("validateDate", DateHandlerUtils.dateToString(DateFormatConstants.TO_DATABASE, validateDate))
        		.setInteger("needAssessment", FlagConstants.NEED_ASSESSMENT)
        		.list();
        return planList;
	}
}
