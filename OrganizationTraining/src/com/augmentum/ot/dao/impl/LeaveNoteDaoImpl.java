package com.augmentum.ot.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Component;

import com.augmentum.ot.dao.LeaveNoteDao;
import com.augmentum.ot.model.LeaveNote;

@Component("leaveDao") 
public class LeaveNoteDaoImpl extends BaseDaoImpl<LeaveNote> implements LeaveNoteDao {

    @Override
    public Class<LeaveNote> getEntityClass() {
        return LeaveNote.class;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<LeaveNote> findLeaveNoteByEmployeeIdAndPlanId(
            Integer employeeId, Integer planId) {
        Query query=getSession().createQuery("from "+getEntityClass().getSimpleName()+
                    " as leaveNote where leaveNote.employee.employeeId =:employeeId and leaveNote.planId =:planId")
                    .setInteger("employeeId", employeeId).setInteger("planId", planId);
        return query.list();
    }

    @Override
    public LeaveNote findLeaveNoteByEmployeeIdAndCourseId(Integer employeeId,
            Integer courseId) {
        LeaveNote leaveNote= (LeaveNote)getSession().createQuery("from "+getEntityClass().getSimpleName()+
                    " as leaveNote where leaveNote.employee.employeeId =:employeeId and leaveNote.actualCourse.actualCourseId =:actualCourseId")
                    .setInteger("employeeId", employeeId).setInteger("actualCourseId", courseId).uniqueResult();
        return leaveNote;
    }
}
