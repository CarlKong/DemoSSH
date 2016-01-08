package com.augmentum.ot.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;


@Entity
@Table(name = "leave_note")
public class LeaveNote implements Serializable {

    private static final long serialVersionUID = 3374182878452617096L;

    /**the id of the leave */
    private Integer leaveId;
    
    /**the planCourse which employee apply leave for*/
    private ActualCourse actualCourse;
    
   // private PlanCourse planCourse;
    
    private Employee employee;
    
    private Integer planId;

    private String reason;
    
    private Date applyLeaveDate;

    @GenericGenerator(name = "generator", strategy = "native")
    @Id
    @GeneratedValue(generator = "generator")
    @Column(name = "id", unique = true, nullable = false)
    public Integer getLeaveId() {
        return leaveId;
    }

    public void setLeaveId(Integer leaveId) {
        this.leaveId = leaveId;
    }
    


    @Column(name = "reason", nullable = true)
    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
    
    @Column(name = "plan_id", nullable = false)
    public Integer getPlanId() {
        return planId;
    }

    public void setPlanId(Integer planId) {
        this.planId = planId;
    }

    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "apply_leave_date", nullable = false)
    public Date getApplyLeaveDate() {
        return applyLeaveDate;
    }

    public void setApplyLeaveDate(Date applyLeaveDate) {
        this.applyLeaveDate = applyLeaveDate;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "actual_course_id", referencedColumnName="id", nullable = true, updatable = false)
    public ActualCourse getActualCourse() {
        return actualCourse;
    }

    public void setActualCourse(ActualCourse actualCourse) {
        this.actualCourse = actualCourse;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", referencedColumnName="id", nullable = false, updatable = false)
    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "plan_course_id", referencedColumnName="id", nullable = false, updatable = false)
//    public PlanCourse getPlanCourse() {
//        return planCourse;
//    }
//
//    public void setPlanCourse(PlanCourse planCourse) {
//        this.planCourse = planCourse;
//    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((applyLeaveDate == null) ? 0 : applyLeaveDate.hashCode());
        result = prime * result
                + ((employee == null) ? 0 : employee.hashCode());
        result = prime * result + ((leaveId == null) ? 0 : leaveId.hashCode());
//        result = prime * result
//                + ((actualCourse == null) ? 0 : actualCourse.hashCode());
        result = prime * result + ((reason == null) ? 0 : reason.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        LeaveNote other = (LeaveNote) obj;
        if (applyLeaveDate == null) {
            if (other.applyLeaveDate != null)
                return false;
        } else if (!applyLeaveDate.equals(other.applyLeaveDate))
            return false;
        if (employee == null) {
            if (other.employee != null)
                return false;
        } else if (!employee.equals(other.employee))
            return false;
        if (leaveId == null) {
            if (other.leaveId != null)
                return false;
        } else if (!leaveId.equals(other.leaveId))
            return false;
//        if (actualCourse == null) {
//            if (other.actualCourse != null)
//                return false;
//        } else if (!actualCourse.equals(other.actualCourse))
//            return false;
        if (reason == null) {
            if (other.reason != null)
                return false;
        } else if (!reason.equals(other.reason))
            return false;
        return true;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Leave [applyLeaveDate=");
        builder.append(applyLeaveDate);
        builder.append(", employee=");
        builder.append(", leaveId=");
        builder.append(leaveId);
//        builder.append(actualCourse);
        builder.append(", reason=");
        builder.append(reason);
        builder.append("]");
        return builder.toString();
    }
    
}
