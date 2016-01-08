package com.augmentum.ot.service;

import com.augmentum.ot.exception.ServerErrorException;




/** 
* @ClassName: PlanEmployeeMapService 
* @date 2012-8-28 
* @version V1.0 
*/
public interface PlanEmployeeMapService {

    /**
     * Find the planEmployeeMap according the planId, employeeId 
     * @param Integer planId,
             Integer employeeId
     * @return true if the employee is invited  
     * @throws ServerErrorException  
     */ 
    Boolean getIfEmployeeInvited(Integer planId, Integer employeeId) throws ServerErrorException;
}
