package com.augmentum.ot.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.augmentum.ot.dao.PlanTagDao;
import com.augmentum.ot.exception.ServerErrorException;
import com.augmentum.ot.model.PlanTag;
import com.augmentum.ot.service.PlanTagService;

/** 
 * @ClassName: PlanTagSeviceImpl 
 * @date 2012-7-17 
 * @version V1.0 
 */
@Component("planTagService")
public class PlanTagSeviceImpl implements PlanTagService {
	    
	@Resource(name="planTagDao")
	private PlanTagDao planTagDao;
	private Logger logger = Logger.getLogger(PlanTagSeviceImpl.class);
	
	@Override
	public List<PlanTag> findAllPlanTags() throws ServerErrorException {
	    List<PlanTag> list = null;
		try {
			list = planTagDao.loadAll();
		} catch (Exception e) {
			logger.error(e);
		}
		return list;
	}

}
