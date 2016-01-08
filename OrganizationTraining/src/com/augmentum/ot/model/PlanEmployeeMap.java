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
@Table(name = "plan_employee_map")
public class PlanEmployeeMap implements Serializable {

    private static final long serialVersionUID = 1L;

    /** link to planEmployee */
    private Integer planEmployeeMapId;

    /** mark the attend type  ,only have function about Invited type*/
    private Integer planTraineeAttendType;

    /** whether the employee of plan is existed */
    private Integer planEmployeeIsDeleted;

    /** link to plan */
    private Plan plan;

    /** link to plan */
    private Employee employee;
    
    /** operation time */
    private Date operationTime;

    @GenericGenerator(name = "generator", strategy = "increment")
    @Id
    @GeneratedValue(generator = "generator")
    @Column(name = "id", unique = true, nullable = false)
    public Integer getPlanEmployeeMapId() {
		return planEmployeeMapId;
	}

	public void setPlanEmployeeMapId(Integer planEmployeeMapId) {
		this.planEmployeeMapId = planEmployeeMapId;
		 if (this.planEmployeeMapId != null && this.planEmployeeMapId > 0) {
	            this.operationTime = new Date();
	        }
	}

	@Column(name = "attend_type", nullable = false)
	public Integer getPlanTraineeAttendType() {
		return planTraineeAttendType;
	}

	public void setPlanTraineeAttendType(Integer planTraineeAttendType) {
		this.planTraineeAttendType = planTraineeAttendType;
	}

	@Column(name = "is_deleted", nullable = false)
	public Integer getPlanEmployeeIsDeleted() {
		return planEmployeeIsDeleted;
	}

	public void setPlanEmployeeIsDeleted(Integer planEmployeeIsDeleted) {
		this.planEmployeeIsDeleted = planEmployeeIsDeleted;
	}
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plan_id", referencedColumnName="id", nullable = false, updatable = false)
    public Plan getPlan() {
        return plan;
    }

	public void setPlan(Plan plan) {
        this.plan = plan;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "employee_id", referencedColumnName="id", nullable = false, updatable = false)
    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }
    
    @Column(name = "operation_time", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
	public Date getOperationTime() {
        return operationTime;
    }

    public void setOperationTime(Date operationTime) {
        this.operationTime = operationTime;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((employee == null) ? 0 : employee.hashCode());
        result = prime * result
                + ((operationTime == null) ? 0 : operationTime.hashCode());
        result = prime
                * result
                + ((planEmployeeIsDeleted == null) ? 0 : planEmployeeIsDeleted
                        .hashCode());
        result = prime
                * result
                + ((planEmployeeMapId == null) ? 0 : planEmployeeMapId
                        .hashCode());
        result = prime
                * result
                + ((planTraineeAttendType == null) ? 0 : planTraineeAttendType
                        .hashCode());
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
        PlanEmployeeMap other = (PlanEmployeeMap) obj;
        if (employee == null) {
            if (other.employee != null)
                return false;
        } else if (!employee.equals(other.employee))
            return false;
        if (operationTime == null) {
            if (other.operationTime != null)
                return false;
        } else if (!operationTime.equals(other.operationTime))
            return false;
        if (planEmployeeIsDeleted == null) {
            if (other.planEmployeeIsDeleted != null)
                return false;
        } else if (!planEmployeeIsDeleted.equals(other.planEmployeeIsDeleted))
            return false;
        if (planEmployeeMapId == null) {
            if (other.planEmployeeMapId != null)
                return false;
        } else if (!planEmployeeMapId.equals(other.planEmployeeMapId))
            return false;
        if (planTraineeAttendType == null) {
            if (other.planTraineeAttendType != null)
                return false;
        } else if (!planTraineeAttendType.equals(other.planTraineeAttendType))
            return false;
        return true;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("PlanEmployeeMap [employee=");
        builder.append(employee);
        builder.append(", planEmployeeIsDeleted=");
        builder.append(planEmployeeIsDeleted);
        builder.append(", planEmployeeMapId=");
        builder.append(planEmployeeMapId);
        builder.append(", planTraineeAttendType=");
        builder.append(planTraineeAttendType);
        builder.append("]");
        return builder.toString();
    }

}
