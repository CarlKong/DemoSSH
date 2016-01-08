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
@Table(name = "actual_course_attachment")
public class ActualCourseAttachment implements Serializable {

    private static final long serialVersionUID = 1L;

    /** id of actualCourseAttachment */
    private Integer actualCourseAttachmentId;

    /** path of actualCourseAttachment */
    private String actualCourseAttachmentPath;

    /** if visible of actualCourseAttachment */
    private Integer actualCourseAttachmentVisible;

    /** name of actualCourseAttachment */
    private String actualCoursAttachmentName;

    /** whether the actualCourseattachment is existed */
    private Integer actualCourseAttachmentIsDeleted;

    /** link to actualCourse */
    private ActualCourse actualCourse;
    
    private String size;
    
    /** The attachment create time*/
    private Date createDateTime;

    @GenericGenerator(name = "generator", strategy = "increment")
    @Id
    @GeneratedValue(generator = "generator")
    @Column(name = "id", unique = true, nullable = false)
    public Integer getActualCourseAttachmentId() {
        return actualCourseAttachmentId;
    }

    public void setActualCourseAttachmentId(Integer actualCourseAttachmentId) {
        this.actualCourseAttachmentId = actualCourseAttachmentId;
    }

    @Column(name = "path", length = 100, nullable = false)
    public String getActualCourseAttachmentPath() {
        return actualCourseAttachmentPath;
    }

    public void setActualCourseAttachmentPath(String actualCourseAttachmentPath) {
        this.actualCourseAttachmentPath = actualCourseAttachmentPath;
    }

    @Column(name = "visible", length=1, nullable = false)
    public Integer getActualCourseAttachmentVisible() {
        return actualCourseAttachmentVisible;
    }

    public void setActualCourseAttachmentVisible(
            Integer actualCourseAttachmentVisible) {
        this.actualCourseAttachmentVisible = actualCourseAttachmentVisible;
    }

    @Column(name = "name", length = 100, nullable = false)
    public String getActualCoursAttachmentName() {
        return actualCoursAttachmentName;
    }

    public void setActualCoursAttachmentName(String actualCoursAttachmentName) {
        this.actualCoursAttachmentName = actualCoursAttachmentName;
    }

    @Column(name = "size", length = 10)
    public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}
    
    @Column(name = "is_deleted", nullable = false)
    public Integer getActualCourseAttachmentIsDeleted() {
		return actualCourseAttachmentIsDeleted;
	}

	public void setActualCourseAttachmentIsDeleted(
			Integer actualCourseAttachmentIsDeleted) {
		this.actualCourseAttachmentIsDeleted = actualCourseAttachmentIsDeleted;
	}


	@Column(name="create_datetime", nullable=false)
    @Temporal(TemporalType.TIMESTAMP)
    public Date getCreateDateTime() {
		return createDateTime;
	}

	public void setCreateDateTime(Date createDateTime) {
		this.createDateTime = createDateTime;
	}

	@ManyToOne(fetch = FetchType.LAZY, targetEntity = ActualCourse.class)
    @JoinColumn(name = "actual_course_id", referencedColumnName = "id")
    public ActualCourse getActualCourse() {
        return actualCourse;
    }

	public void setActualCourse(ActualCourse actualCourse) {
        this.actualCourse = actualCourse;
    }

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((actualCourseAttachmentId == null) ? 0
						: actualCourseAttachmentId.hashCode());
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
		ActualCourseAttachment other = (ActualCourseAttachment) obj;
		if (actualCourseAttachmentId == null) {
			if (other.actualCourseAttachmentId != null)
				return false;
		} else if (!actualCourseAttachmentId
				.equals(other.actualCourseAttachmentId))
			return false;
		return true;
	}
    
	
}
