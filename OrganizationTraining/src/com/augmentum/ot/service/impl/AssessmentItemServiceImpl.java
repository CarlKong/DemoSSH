package com.augmentum.ot.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.augmentum.ot.dao.AssessmentItemDao;
import com.augmentum.ot.dataObject.constant.ErrorCodeConstants;
import com.augmentum.ot.dataObject.constant.LogConstants;
import com.augmentum.ot.exception.ServerErrorException;
import com.augmentum.ot.model.AssessmentItem;
import com.augmentum.ot.service.AssessmentItemService;

/**
 * AssessmentItemServiceImpl
 * @Project OT
 * @Modificaion_history 2012-10-10
 * @Version 1.0
 */
@Component("assessmentItemService")
public class AssessmentItemServiceImpl implements AssessmentItemService{
    private static Logger logger = Logger.getLogger(CourseServiceImpl.class);
    private AssessmentItemDao assessmentItemDao;
    
    @Override
    public List<AssessmentItem> getAssessmentItemByTypeId(
            Integer assessmentItemTypeId) throws ServerErrorException {
        if(assessmentItemTypeId == null || assessmentItemTypeId <= 0){
        	logger.error(LogConstants.objectIsNULLOrEmpty("Assessment Item Type Id"));
            throw new ServerErrorException(ErrorCodeConstants.SERVER_VALIDATION_ERROR);
        }
        List<AssessmentItem> assessmentItemsTemp = null;
        try{
            assessmentItemsTemp = assessmentItemDao.getAssessmentItemByTypeId(assessmentItemTypeId);
        }catch(Exception e){
            logger.error(LogConstants.exceptionMessage("Get assessment item by type[#"+assessmentItemTypeId+"]"), e);
            throw new ServerErrorException(ErrorCodeConstants.SERVER_ERROR);
        }
        if(assessmentItemsTemp == null){
        	logger.error(LogConstants.pureMessage("Find assessment items by type[#"+assessmentItemTypeId+"] fail."));
            throw new ServerErrorException(ErrorCodeConstants.SERVER_VALIDATION_ERROR);
        }
        return assessmentItemsTemp; 
    }
    
    @Resource(name="assessmentItemDao")
    public void setAssessmentItemDao(AssessmentItemDao assessmentItemDao) {
        this.assessmentItemDao = assessmentItemDao;
    }
}
