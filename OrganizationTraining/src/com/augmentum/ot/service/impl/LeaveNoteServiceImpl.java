package com.augmentum.ot.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.augmentum.ot.dao.LeaveNoteDao;
import com.augmentum.ot.dataObject.constant.ErrorCodeConstants;
import com.augmentum.ot.dataObject.constant.LogConstants;
import com.augmentum.ot.exception.ServerErrorException;
import com.augmentum.ot.model.LeaveNote;
import com.augmentum.ot.service.LeaveNoteService;

/**
 * 
 * @Project OT
 * @Modificaion_history 2012-12-20
 * @Version 2.0
 */
@Component("leaveService")
public class LeaveNoteServiceImpl implements LeaveNoteService {

    private static Logger logger = Logger.getLogger(LeaveNoteServiceImpl.class);

    @Resource(name="leaveDao")
    private LeaveNoteDao leaveDao;

    @Override
    public Integer saveLeave(LeaveNote leave) throws ServerErrorException {
        logger.debug(LogConstants.getDebugInput("save LeaveNote object",leave));
        if(leave == null){
            logger.error(LogConstants.objectIsNULLOrEmpty("saveLeave method: LeaveNote is null"));
            throw new ServerErrorException(ErrorCodeConstants.SERVER_VALIDATION_ERROR); 
        }
        try{
            leaveDao.saveObject(leave);
        } catch (Exception e) {
            logger.error(LogConstants.exceptionMessage("save a LeaveNote operation occurs exception "), e);
            throw new ServerErrorException(ErrorCodeConstants.SERVER_ERROR);
        }
        logger.debug(LogConstants.getDebugOutput("Save a LeaveNote",leave));
        return leave.getLeaveId();
    }

    @Override
    public void deleteLeaveNote(LeaveNote traineeLeaveNote) throws ServerErrorException {
        logger.debug(LogConstants.getDebugInput("delete a LeaveNote object",traineeLeaveNote));
        if(traineeLeaveNote == null){
            logger.error(LogConstants.objectIsNULLOrEmpty("deleteLeaveNote method: traineeLeaveNote is null"));
            throw new ServerErrorException(ErrorCodeConstants.SERVER_VALIDATION_ERROR);
        }
        try{
            leaveDao.deleteObject(traineeLeaveNote);
        } catch (Exception e){
            logger.error(LogConstants.exceptionMessage("save a LeaveNote operation occurs exception "), e);
            throw new ServerErrorException(ErrorCodeConstants.SERVER_ERROR);
        }
        logger.debug(LogConstants.getDebugOutput("delete a LeaveNote",traineeLeaveNote));
    }

    @Override
    public void saveLeave(List<LeaveNote> leaveList) throws ServerErrorException {
        logger.debug(LogConstants.getDebugInput("save a LeaveNote List",leaveList));
        for(LeaveNote leaveNote:leaveList){
            try{
                leaveDao.saveObject(leaveNote);
            } catch (Exception e) {
                logger.error(LogConstants.exceptionMessage("save a LeaveNote operation occurs exception "), e);
                throw new ServerErrorException(ErrorCodeConstants.SERVER_ERROR);
            }
        }
        logger.debug(LogConstants.getDebugOutput("save a LeaveNote list",leaveList));
    }

    @Override
    public List<LeaveNote> getLeaveNoteByEmployeeIdAndPlanId(
            Integer employeeId, Integer planId) throws ServerErrorException {
        logger.debug(LogConstants.getDebugInput("getLeaveNoteByEmployeeIdAndPlanId ",planId));
        if(employeeId == null || planId == null){
            logger.error(LogConstants.objectIsNULLOrEmpty("getLeaveNoteByEmployeeIdAndPlanId: employeeId or planId"));
            throw new ServerErrorException(ErrorCodeConstants.SERVER_VALIDATION_ERROR);
        }
        List<LeaveNote> leaveNotes = null;
        try {
            leaveNotes = leaveDao.findLeaveNoteByEmployeeIdAndPlanId(employeeId,planId);
        } catch (Exception e){
            logger.error(LogConstants.exceptionMessage("save a LeaveNote operation occurs exception "), e);
            throw new ServerErrorException(ErrorCodeConstants.SERVER_ERROR);
        }
        if(leaveNotes != null && leaveNotes.size() != 0){
            leaveNotes.get(0).getActualCourse();
        }
        logger.debug(LogConstants.getDebugOutput("getLeaveNoteByEmployeeIdAndPlanId ",leaveNotes));
        return leaveNotes;
    }

    @Override
    public void deleteLeaveNote(List<LeaveNote> needBackLeaveNoteList) throws ServerErrorException {
        if(needBackLeaveNoteList == null){
            logger.error(LogConstants.objectIsNULLOrEmpty("leaveNote List is null, can't not delete it"));
            throw new ServerErrorException(ErrorCodeConstants.SERVER_VALIDATION_ERROR);
        }
        for(LeaveNote leaveNote:needBackLeaveNoteList){
            deleteLeaveNote(leaveNote);
        }
    }
    
    public void setLeaveDao(LeaveNoteDao leaveDao) {
        this.leaveDao = leaveDao;
    }

    @Override
    public LeaveNote getLeaveNoteByEmployeeIdAndCourseId(Integer employeeId,
            Integer courseId) throws ServerErrorException {
        logger.debug(LogConstants.getDebugInput("getLeaveNoteByEmployeeIdAndPlanId ",courseId));
        if(employeeId == null || courseId == null){
            logger.error(LogConstants.objectIsNULLOrEmpty("getLeaveNoteByEmployeeIdAndPlanId: employeeId or courseId"));
            throw new ServerErrorException(ErrorCodeConstants.SERVER_VALIDATION_ERROR);
        }
        LeaveNote leaveNote = null;
        try {
            leaveNote = leaveDao.findLeaveNoteByEmployeeIdAndCourseId(employeeId,courseId);
        } catch (Exception e){
            logger.error(LogConstants.exceptionMessage("save a LeaveNote operation occurs exception "), e);
            throw new ServerErrorException(ErrorCodeConstants.SERVER_ERROR);
        }
        if(leaveNote != null){
            leaveNote.getActualCourse();
        }
        return leaveNote;
    }
}
