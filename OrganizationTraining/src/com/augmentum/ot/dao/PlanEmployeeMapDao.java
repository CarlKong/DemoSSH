package com.augmentum.ot.dao;

import java.util.Date;
import java.util.List;
import java.util.Set;

import com.augmentum.ot.model.Employee;
import com.augmentum.ot.model.PlanEmployeeMap;

/**
 * A interface about planEmployeeMap,provide additional methods
 * 
 * @version 0.1, 07/30/2012
 */
public interface PlanEmployeeMapDao extends BaseDao<PlanEmployeeMap> {

     /** 
     * @Description: get planEmployeeMap list by planId, attendType
     * @param planId
     * @param planTraineeAttendType
     * @return List<PlanEmployeeMap>
     * @throws PlanServiceException 
     */ 
    List<PlanEmployeeMap> getPlanEmployeeMaps(Integer planId, Integer attendType, Integer isDeleted);
    
    
     /** 
     * @Description: get planEmployeeMap list by planId
     * @param planId
     * @return
     * @throws PlanServiceException 
     */ 
    List<PlanEmployeeMap> getPlanEmployeeMaps(Integer planId);
    
    void savePlanEmployeeMap(PlanEmployeeMap planEmployeeMap);

    Set<Employee> getRelatedEmployeesByPlan(Integer planId, Integer attendType,
            Integer isDeleted);
    
    PlanEmployeeMap getPlanEmployeeMapByPlanAndEmployee(int planId, int employeeId, int attendType);
    
    PlanEmployeeMap getUndeletedPlanEmployeeMapByPlanAndEmployee(int planId, int employeeId, int attendType,Integer isDeleted);

    List<PlanEmployeeMap> getPEMListByNeedAssessTrainee(String traineeName, Date currentTime, Date validateTime);
        
}
