package com.augmentum.ot.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;


/**
 * @version 0.2, 07/12/2012
 */
@Entity
@Table(name = "course_attachment")
public class CourseAttachment implements Serializable {

    private static final long serialVersionUID = 1L;

    /** id of courseAttachment */
    private Integer courseAttachmentId;

    /** address of courseAttachment */
    private String courseAttachmentPath;

    /** if visible of courseAttachment */
    private Integer courseAttachmentvisible;

    /** name of courseAttachment */
    private String courseAttachmentName;

    /** whether the attachment is deleted */
    private Integer courseAttachmentIsDeleted;

    /** link to course */
    private Course courseAttach;
    
    /** The attachment size**/
    private String size;
    
    /** The attachment create time*/
    private Date createDateTime;

    @GenericGenerator(name = "generator", strategy = "increment")
    @Id
    @GeneratedValue(generator = "generator")
    @Column(name = "id", unique = true, nullable = true)
    public Integer getCourseAttachmentId() {
        return courseAttachmentId;
    }

    public void setCourseAttachmentId(Integer courseAttachmentId) {
        this.courseAttachmentId = courseAttachmentId;
    }

    @Column(name = "path", length = 200, nullable = false)
    public String getCourseAttachmentPath() {
        return courseAttachmentPath;
    }

    public void setCourseAttachmentPath(String courseAttachmentPath) {
        this.courseAttachmentPath = courseAttachmentPath;
    }

    @Column(name="visible", nullable=false)
    public Integer getCourseAttachmentvisible() {
		return courseAttachmentvisible;
	}

	public void setCourseAttachmentvisible(Integer courseAttachmentvisible) {
		this.courseAttachmentvisible = courseAttachmentvisible;
	}

	@Column(name = "name", nullable = false, length=200)
    	public String getCourseAttachmentName() {
		return courseAttachmentName;
	}

	public void setCourseAttachmentName(String courseAttachmentName) {
		this.courseAttachmentName = courseAttachmentName;
	}
	
    @Column(name = "size", length = 10)
    public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	@Column(name="is_deleted", nullable=false)
    public Integer getCourseAttachmentIsDeleted() {
		return courseAttachmentIsDeleted;
	}

	public void setCourseAttachmentIsDeleted(Integer courseAttachmentIsDeleted) {
		this.courseAttachmentIsDeleted = courseAttachmentIsDeleted;
	}

	@ManyToOne(targetEntity = Course.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "course_id", updatable = true, referencedColumnName = "id")
    public Course getCourseAttach() {
        return courseAttach;
    }

    public void setCourseAttach(Course courseAttach) {
        this.courseAttach = courseAttach;
    }
    
    @Column(name="create_datetime", nullable=false)
    @Temporal(TemporalType.TIMESTAMP)
    public Date getCreateDateTime() {
		return createDateTime;
	}

	public void setCreateDateTime(Date createDateTime) {
		this.createDateTime = createDateTime;
	}

	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((courseAttachmentId == null) ? 0 : courseAttachmentId
						.hashCode());
		result = prime
				* result
				+ ((courseAttachmentIsDeleted == null) ? 0
						: courseAttachmentIsDeleted.hashCode());
		result = prime
				* result
				+ ((courseAttachmentPath == null) ? 0 : courseAttachmentPath
						.hashCode());
		result = prime
				* result
				+ ((courseAttachmentvisible == null) ? 0
						: courseAttachmentvisible.hashCode());
		result = prime
				* result
				+ ((courseAttachmentName == null) ? 0 : courseAttachmentName
						.hashCode());
		result = prime * result
				+ ((createDateTime == null) ? 0 : createDateTime.hashCode());
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
		CourseAttachment other = (CourseAttachment) obj;
		if (courseAttachmentId == null) {
			if (other.courseAttachmentId != null)
				return false;
		} else if (!courseAttachmentId.equals(other.courseAttachmentId)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("CourseAttachment [courseAttach=");
		builder.append(courseAttach);
		builder.append(", courseAttachmentId=");
		builder.append(courseAttachmentId);
		builder.append(", courseAttachmentIsDeleted=");
		builder.append(courseAttachmentIsDeleted);
		builder.append(", courseAttachmentPath=");
		builder.append(courseAttachmentPath);
		builder.append(", courseAttachmentvisible=");
		builder.append(courseAttachmentvisible);
		builder.append(", couserAttachmentName=");
		builder.append(courseAttachmentName);
		builder.append(", createDateTime=");
		builder.append(createDateTime);
		builder.append(", size=");
		builder.append(size);
		builder.append("]");
		return builder.toString();
	}

	

}
