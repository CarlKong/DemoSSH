package com.augmentum.ot.service;

import java.util.List;

import com.augmentum.ot.exception.ServerErrorException;
import com.augmentum.ot.model.LeaveNote;

/**
 * Handle the trainee apply leave service
 * @Project OT
 * @Modificaion_history 2012-12-20
 * @Version 2.0
 */
public interface LeaveNoteService {

    /**
     * save a leave
     * @param LeaveNote leave
     * @return the id of leave just be saved 
     * @throws ServerErrorException    
     */
    Integer saveLeave(LeaveNote leave) throws ServerErrorException; 
    
    /**
     * save a leave list
     * 
     * @param List<LeaveNote> leaveList
     * @return
     * @throws ServerErrorException   
     */
    void saveLeave(List<LeaveNote> leaveList) throws ServerErrorException;

    /**
     * delete a leaveNote 
     * @param LeaveNote traineeLeaveNote
     * @return  
     * @throws ServerErrorException 
     */
    void deleteLeaveNote(LeaveNote traineeLeaveNote) throws ServerErrorException;

    /**
     * get leaveNotes by the employeeId and planId
     * @param Integer employeeId,
            Integer planId
     * @return List<LeaveNote> 
     * @throws ServerErrorException 
     */
    List<LeaveNote> getLeaveNoteByEmployeeIdAndPlanId(Integer employeeId,
            Integer planId) throws ServerErrorException;

    /**
     * delete the LeaveNotes in a list
     * @param 
     * @return
     * @throws ServerErrorException 
     */ 
    void deleteLeaveNote(List<LeaveNote> needBackLeaveNoteList) throws ServerErrorException;

    LeaveNote getLeaveNoteByEmployeeIdAndCourseId(Integer employeeId, Integer actualCourseId) throws ServerErrorException; 

}
