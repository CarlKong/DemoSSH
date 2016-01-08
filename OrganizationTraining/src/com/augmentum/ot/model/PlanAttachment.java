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
@Table(name = "plan_attachment")
public class PlanAttachment implements Serializable {

    private static final long serialVersionUID = 1L;

    /** id of planAttachment */
    private Integer planAttachmentId;

    /** path of planAttachment */
    private String planAttachmentPath;

    /** if visible of planAttachment */
    private Integer planAttachmentIsVisible;

    /** name of planAttachment */
    private String planAttachmentName;

    /** whether the planAttachment is existed */
    private Integer planAttachmentIsDeleted;
    
    /** The attachment create time*/
    private Date createDateTime;

    /** link to plan */
    private Plan plan;
    
    private String size;

    @GenericGenerator(name = "generator", strategy = "increment")
    @Id
    @GeneratedValue(generator = "generator")
    @Column(name = "id", nullable = false, unique = true)
    public Integer getPlanAttachmentId() {
        return planAttachmentId;
    }

    public void setPlanAttachmentId(Integer planAttachmentId) {
        this.planAttachmentId = planAttachmentId;
    }

    @Column(name = "path", length = 100, nullable = false)
    public String getPlanAttachmentPath() {
        return planAttachmentPath;
    }

    public void setPlanAttachmentPath(String planAttachmentPath) {
        this.planAttachmentPath = planAttachmentPath;
    }

    @Column(name = "visible", nullable = true)
    public Integer getPlanAttachmentIsVisible() {
        return planAttachmentIsVisible;
    }

    public void setPlanAttachmentIsVisible(Integer planAttachmentIsVisible) {
		this.planAttachmentIsVisible = planAttachmentIsVisible;
	}

    @Column(name = "name", length = 100, nullable = false)
    public String getPlanAttachmentName() {
        return planAttachmentName;
    }

    public void setPlanAttachmentName(String planAttachmentName) {
        this.planAttachmentName = planAttachmentName;
    }
    
    @Column(name = "size", length = 10)
    public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

    @Column(name = "is_deleted", nullable = false)
    public Integer getPlanAttachmentIsDeleted() {
		return planAttachmentIsDeleted;
	}

	public void setPlanAttachmentIsDeleted(Integer planAttachmentIsDeleted) {
		this.planAttachmentIsDeleted = planAttachmentIsDeleted;
	}

	@Column(name="create_datetime", nullable=false)
    @Temporal(TemporalType.TIMESTAMP)
    public Date getCreateDateTime() {
		return createDateTime;
	}

	public void setCreateDateTime(Date createDateTime) {
		this.createDateTime = createDateTime;
	}
	
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plan_id", referencedColumnName = "id")
    public Plan getPlan() {
        return plan;
    }


	public void setPlan(Plan plan) {
        this.plan = plan;
    }

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((planAttachmentId == null) ? 0 : planAttachmentId.hashCode());
		result = prime
				* result
				+ ((planAttachmentIsDeleted == null) ? 0
						: planAttachmentIsDeleted.hashCode());
		result = prime
				* result
				+ ((planAttachmentIsVisible == null) ? 0
						: planAttachmentIsVisible.hashCode());
		result = prime
				* result
				+ ((planAttachmentName == null) ? 0 : planAttachmentName
						.hashCode());
		result = prime
				* result
				+ ((planAttachmentPath == null) ? 0 : planAttachmentPath
						.hashCode());
		result = prime * result + ((size == null) ? 0 : size.hashCode());
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
		PlanAttachment other = (PlanAttachment) obj;
		if (planAttachmentId == null) {
			if (other.planAttachmentId != null)
				return false;
		} else if (!planAttachmentId.equals(other.planAttachmentId))
			return false;
		return true;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("PlanAttachment [plan=");
		builder.append(plan);
		builder.append(", planAttachmentId=");
		builder.append(planAttachmentId);
		builder.append(", planAttachmentIsDeleted=");
		builder.append(planAttachmentIsDeleted);
		builder.append(", planAttachmentIsVisible=");
		builder.append(planAttachmentIsVisible);
		builder.append(", planAttachmentName=");
		builder.append(planAttachmentName);
		builder.append(", planAttachmentPath=");
		builder.append(planAttachmentPath);
		builder.append(", size=");
		builder.append(size);
		builder.append("]");
		return builder.toString();
	}

}
