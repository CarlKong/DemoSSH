package com.augmentum.ot.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.augmentum.ot.dao.PlanTypeDao;
import com.augmentum.ot.dataObject.constant.ErrorCodeConstants;
import com.augmentum.ot.dataObject.constant.LogConstants;
import com.augmentum.ot.exception.ServerErrorException;
import com.augmentum.ot.model.PlanType;
import com.augmentum.ot.service.PlanTypeService;

/** 
 * @ClassName: PlanTypeServiceImpl 
 * @date 2012-7-17 
 * @version V1.0 
 */
@Component("planTypeService")
public class PlanTypeServiceImpl implements PlanTypeService{
	
    private Logger logger = Logger.getLogger(PlanTypeServiceImpl.class);

	private PlanTypeDao planTypeDao;
    
	@Resource
	public void setPlanTypeDao(PlanTypeDao planTypeDao) {
		this.planTypeDao = planTypeDao;
	}

	@Override
	public PlanType findPlanTypeById(Integer planTypeId)
			throws ServerErrorException {
		logger.debug(LogConstants.getDebugInput("find plan type by id",
				planTypeId));
		if (null == planTypeId || planTypeId <= 0) {
			logger.error(LogConstants.objectIsNULLOrEmpty("planTypeId"));
			throw new ServerErrorException(ErrorCodeConstants.SERVER_VALIDATION_ERROR);
		}
		PlanType planType = null;
		try{
			planType = planTypeDao.findByPrimaryKey(planTypeId);
		} catch(Exception e) {
			logger.error(LogConstants.exceptionMessage("find plan type "), e);
			throw new ServerErrorException(ErrorCodeConstants.SERVER_ERROR);
		}
		logger.debug(LogConstants.getDebugOutput("find plan type by id",
				planType));
		return planType;
	}

	@Override
	public List<PlanType> findAllPlanTypes() throws ServerErrorException {
		return planTypeDao.loadAll();
	}

}
