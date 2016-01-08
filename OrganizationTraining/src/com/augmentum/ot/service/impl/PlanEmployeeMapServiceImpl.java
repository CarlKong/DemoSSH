package com.augmentum.ot.service.impl;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.augmentum.ot.dao.PlanEmployeeMapDao;
import com.augmentum.ot.dataObject.constant.ErrorCodeConstants;
import com.augmentum.ot.dataObject.constant.FlagConstants;
import com.augmentum.ot.dataObject.constant.LogConstants;
import com.augmentum.ot.exception.ServerErrorException;
import com.augmentum.ot.model.PlanEmployeeMap;
import com.augmentum.ot.service.PlanEmployeeMapService;


/** 
* @ClassName: PlanEmployeeMapSeviceImpl 
* @date 2012-8-28 
* @version V1.0 
*/
@Component("planEmployeeMapService")
public class PlanEmployeeMapServiceImpl implements PlanEmployeeMapService {

    private static Logger logger = Logger.getLogger(PlanEmployeeMapServiceImpl.class);
    
    @Resource(name="planEmployeeMapDao")
    private PlanEmployeeMapDao planEmployeeMapDao;
    
    @Override
    public Boolean getIfEmployeeInvited(
            Integer planId, Integer employeeId) throws ServerErrorException {
        PlanEmployeeMap planEmployeeMap = null;
        try{
            planEmployeeMap = planEmployeeMapDao.getUndeletedPlanEmployeeMapByPlanAndEmployee(planId, employeeId, FlagConstants.ATTEND_TYPE_INVITED,FlagConstants.UN_DELETED);
        } catch (Exception e){
            logger.error(LogConstants.exceptionMessage("check whether the employee is invited error"), e);
            throw new ServerErrorException(ErrorCodeConstants.SERVER_ERROR);
        }
        if(planEmployeeMap != null){
            return true;
        } else {
            return false;
        }
    }
    
    public void setPlanEmployeeMapDao(PlanEmployeeMapDao planEmployeeMapDao) {
        this.planEmployeeMapDao = planEmployeeMapDao;
    }
}
