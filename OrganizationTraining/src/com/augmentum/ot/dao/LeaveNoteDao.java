package com.augmentum.ot.dao;

import java.util.List;

import com.augmentum.ot.model.LeaveNote;

/**
 * 
 * @Project OT
 * @Modificaion_history 2012-12-20
 * @Version 2.0
 */
public interface LeaveNoteDao extends BaseDao<LeaveNote>{

    /**
     * find leaveNote by employeeId and planId
     * @param Integer employeeId,
            Integer planId
     * @return  List<LeaveNote>
     */
    List<LeaveNote> findLeaveNoteByEmployeeIdAndPlanId(Integer employeeId,
            Integer planId);

    LeaveNote findLeaveNoteByEmployeeIdAndCourseId(Integer employeeId, Integer courseId);

}
