package com.augmentum.ot.dao.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Component;

import com.augmentum.ot.dao.PlanEmployeeMapDao;
import com.augmentum.ot.dataObject.constant.DateFormatConstants;
import com.augmentum.ot.dataObject.constant.FlagConstants;
import com.augmentum.ot.model.Employee;
import com.augmentum.ot.model.PlanEmployeeMap;
import com.augmentum.ot.util.DateHandlerUtils;

/**
 * 
 * @version 0.1, 07/30/2012
 */
@Component("planEmployeeMapDao")
public class PlanEmployeeMapDaoImpl extends BaseDaoImpl<PlanEmployeeMap>
		implements PlanEmployeeMapDao {

	@Override
	public Class<PlanEmployeeMap> getEntityClass() {
		return PlanEmployeeMap.class;
	}

    @SuppressWarnings("unchecked")
    @Override
    public List<PlanEmployeeMap> getPlanEmployeeMaps(Integer planId,
            Integer attendType, Integer isDeleted) {
        Query query = (Query)getSession().createSQLQuery(
                        "select * from plan_employee_map where attend_type =:attendType and plan_id =:planId and is_deleted =:isDeleted")
                        .addEntity(PlanEmployeeMap.class)
                        .setInteger("attendType", attendType)
                        .setInteger("planId", planId)
        				.setInteger("isDeleted", isDeleted);
        return query.list();
    }

	@Override
	public void savePlanEmployeeMap(PlanEmployeeMap planEmployeeMap) {
		Session session = getSession();
    	session.save(planEmployeeMap);
    	session.flush();
	}

    @SuppressWarnings("unchecked")
    @Override
    public List<PlanEmployeeMap> getPlanEmployeeMaps(Integer planId) {
        Query query = (Query)getSession().createSQLQuery(
                        "select * from plan_employee_map where plan_id =:planId")
                        .addEntity(PlanEmployeeMap.class)
                        .setInteger("planId", planId);
        return query.list();
    }

    @SuppressWarnings("unchecked")
    @Override
    public Set<Employee> getRelatedEmployeesByPlan(Integer planId, Integer attendType, Integer isDeleted){
        Set<Employee> employeeSet=new LinkedHashSet<Employee>();
        Query query = (Query)getSession().createSQLQuery(
                        "select * from plan_employee_map where attend_type =:attendType and plan_id =:planId and is_deleted=:isDeleted")
                        .addEntity(PlanEmployeeMap.class)
                        .setInteger("attendType", attendType)
                        .setInteger("planId", planId)
                        .setInteger("isDeleted", isDeleted);
        List<PlanEmployeeMap> planEmployeeMaps=query.list();
        for(PlanEmployeeMap planEmployeeMap:planEmployeeMaps){
            employeeSet.add(planEmployeeMap.getEmployee());
        }
        return employeeSet;
    }

	@Override
	public PlanEmployeeMap getPlanEmployeeMapByPlanAndEmployee(int planId,
			int employeeId, int attendType) {
		PlanEmployeeMap planEmployeeMap = (PlanEmployeeMap)getSession()
				.createQuery("from " + getEntityClass().getSimpleName() +
				" where employee.employeeId =:employeeId " +
				" and plan.planId =:planId" + 
				" and planTraineeAttendType =:attendType").setInteger("employeeId", employeeId)
				.setInteger("planId", planId).setInteger("attendType", attendType).uniqueResult();
		return planEmployeeMap;
	}
	
	@Override
    public PlanEmployeeMap getUndeletedPlanEmployeeMapByPlanAndEmployee(int planId,
            int employeeId, int attendType, Integer isDeleted) {
        PlanEmployeeMap planEmployeeMap = (PlanEmployeeMap)getSession()
                .createQuery("from " + getEntityClass().getSimpleName() +
                " where employee.employeeId =:employeeId " +
                " and plan.planId =:planId" + 
                " and planTraineeAttendType =:attendType and is_deleted=:isDeleted").setInteger("employeeId", employeeId)
                .setInteger("planId", planId).setInteger("attendType", attendType).setInteger("isDeleted", isDeleted).uniqueResult();
        return planEmployeeMap;
    }

	@SuppressWarnings("unchecked")
	@Override
	public List<PlanEmployeeMap> getPEMListByNeedAssessTrainee(
			String traineeName, Date currentTime, Date validateTime) {
		List<PlanEmployeeMap> pemList = new ArrayList<PlanEmployeeMap>();
		Query query=getSession().createQuery("from "+getEntityClass().getSimpleName()+
                " as pem where pem.employee.augUserName =:employeeName  and " +
                " pem.planEmployeeIsDeleted=:mapIsNotDeleted and " +
                " (pem.planTraineeAttendType=:invitedType or " +
                " pem.planTraineeAttendType=:joinType) and " +
                " pem.plan.planIsDeleted=:planIsNotDeleted and " +
                " pem.plan.planIsCanceled=:planIsNotCanceled and " +
                " pem.plan.planIsPublish=:planIsPublish and " +
                "pem.plan.planExecuteEndTime is not null and " +
                "pem.plan.planExecuteEndTime<=:currentTime and pem.plan.planExecuteEndTime>:validateDate" +
                " and pem.plan.needAssessment=:needAssessment)"
        );
		pemList = query.setString("employeeName", traineeName)
		    .setInteger("mapIsNotDeleted", FlagConstants.UN_DELETED)
		    .setInteger("invitedType", FlagConstants.ATTEND_TYPE_INVITED)
		    .setInteger("joinType", FlagConstants.ATTEND_TYPE_JOIN)
			.setInteger("planIsNotDeleted", FlagConstants.UN_DELETED)
			.setInteger("planIsNotCanceled", FlagConstants.UN_CANCELED)
			.setInteger("planIsPublish", FlagConstants.IS_PUBLISHED)
			.setString("currentTime", DateHandlerUtils.dateToString(DateFormatConstants.TO_DATABASE, currentTime))
			.setString("validateDate", DateHandlerUtils.dateToString(DateFormatConstants.TO_DATABASE, validateTime))
			.setInteger("needAssessment", FlagConstants.NEED_ASSESSMENT)
			.list();
		return pemList;
	}
	
}
