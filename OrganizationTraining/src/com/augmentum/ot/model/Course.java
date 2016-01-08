package com.augmentum.ot.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.search.annotations.Analyzer;
import org.hibernate.search.annotations.DateBridge;
import org.hibernate.search.annotations.DocumentId;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Fields;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.IndexedEmbedded;
import org.hibernate.search.annotations.Resolution;
import org.hibernate.search.annotations.Store;
import org.wltea.analyzer.lucene.IKAnalyzer;

import com.augmentum.ot.dataObject.constant.FlagConstants;
import com.augmentum.ot.dataObject.constant.IndexFieldConstants;
import com.augmentum.ot.util.IdToStringUtils;



/**
 * @version 0.2, 07/12/2012
 */
@Entity
@Table(name = "course")
@Indexed(index="course_index")
@Analyzer(impl = IKAnalyzer.class)
public class Course implements Serializable {

    private static final long serialVersionUID = 1L;

    /** id of course */
    private Integer courseId;

    /** name of course */
    private String courseName;

    /** brief or course */
    private String courseBrief;
    
    /** brief or course without the tag*/
    private String courseBriefWithoutTag;

    /** target trainee of course */
    private String courseTargetTrainee;

    /** duration course */
    private Double courseDuration;

    /** type of course,link to courType */
    private CourseType courseType;

    /** if certificated of course */
    private Integer courseIsCertificated;

    /** if have homework of course */
    private Integer courseHasHomework;

    /** author name of course */
    private String courseAuthorName;

    /** the create course time */
    private Date courseCreateDate;

    /** the last update time */
    private Date courseLastUpdateDate;

    /** the name creator of course */
    private String courseCreator;

    /** the tag of category */
    private String courseCategoryTag;

    /** whether the course is existed */
    private Integer courseIsDeleted;

    /** whether the course has attachments */
    private Integer courseHasAttachment;
    
    /** Prefix + Id Value**/
    private String prefixIDValue;
    
    /** the trainers of this course**/
    private String historyTrainers;

    /** link courseAttachment */
    private List<CourseAttachment> courseAttachments = new ArrayList<CourseAttachment>();
    
    @GenericGenerator(name = "generator", strategy = "increment")
    @Id
    @GeneratedValue(generator = "generator")
    @Column(name = "id", unique = true, nullable = false)
    @DocumentId(name=IndexFieldConstants.COURSE_ID)
    public Integer getCourseId() {
        return courseId;
    }

    public void setCourseId(Integer courseId) {
        this.courseId = courseId;
        if (null != courseId) {
        	this.prefixIDValue = IdToStringUtils.addPrefixForId
             (FlagConstants.COURSE_NUMBER_PREFIX, FlagConstants.COURSE_ID_NUMBER, courseId);
        }
    }

    @Column(name = "name", length = 200, nullable = false)
    @Fields({
        @Field(name=IndexFieldConstants.COURSE_NAME, index=Index.TOKENIZED, store=Store.YES),
        @Field(name=IndexFieldConstants.COURSE_NAME_SORT, index=Index.UN_TOKENIZED, store=Store.YES)
    })
    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    @Column(name = "brief", columnDefinition="TEXT", nullable = false)
    @Field(name=IndexFieldConstants.COURSE_BRIEF, index=Index.TOKENIZED, store=Store.NO)
    public String getCourseBrief() {
        return courseBrief;
    }

    public void setCourseBrief(String courseBrief) {
        this.courseBrief = courseBrief;
    }
    
    @Column(name = "brief_without_tag", columnDefinition="TEXT", nullable = false)
    @Field(name=IndexFieldConstants.COURSE_BRIEF_WITHOUT_TAG, index=Index.TOKENIZED, store=Store.NO)
    public String getCourseBriefWithoutTag() {
        return courseBriefWithoutTag;
    }

    public void setCourseBriefWithoutTag(String courseBriefWithoutTag) {
        this.courseBriefWithoutTag = courseBriefWithoutTag.replace("<", "&lt;").replace(">", "&gt;");
    }

    @Lob
    @Column(name = "target_trainee", columnDefinition = "TEXT", nullable = false)
    @Field(name=IndexFieldConstants.COURSE_TARGET_TRAINEE, index=Index.TOKENIZED, store=Store.NO)
    public String getCourseTargetTrainee() {
        return courseTargetTrainee;
    }

    public void setCourseTargetTrainee(String courseTargetTrainee) {
        this.courseTargetTrainee = courseTargetTrainee;
    }

    @Column(name = "duration", precision = 2, scale = 4, nullable = false)
    @Field(name=IndexFieldConstants.COURSE_DURATION, index=Index.UN_TOKENIZED, store=Store.YES)
    public Double getCourseDuration() {
        return courseDuration;
    }

    public void setCourseDuration(Double courseDuration) {
        this.courseDuration = courseDuration;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "type_id", referencedColumnName = "id", nullable = false)
    @IndexedEmbedded(depth=1, prefix=IndexFieldConstants.PREFIX_COURSE_TYPE)
    public CourseType getCourseType() {
        return courseType;
    }

    public void setCourseType(CourseType courseType) {
        this.courseType = courseType;
    }

  
    @Column(name = "is_certificated", nullable = false)
    @Field(name=IndexFieldConstants.COURSE_IS_CERTIFICATED, index=Index.UN_TOKENIZED, store=Store.YES)
    public Integer getCourseIsCertificated() {
		return courseIsCertificated;
	}

	public void setCourseIsCertificated(Integer courseIsCertificated) {
		this.courseIsCertificated = courseIsCertificated;
	}

	@Column(name = "has_homework", nullable = false)
	@Field(name=IndexFieldConstants.COURSE_HAS_HOMEWORK, index=Index.UN_TOKENIZED, store=Store.YES)
	public Integer getCourseHasHomework() {
		return courseHasHomework;
	}

	public void setCourseHasHomework(Integer courseHasHomework) {
		this.courseHasHomework = courseHasHomework;
	}

	@Column(name = "author_name", length = 50, nullable = false)
	@Fields({
	    @Field(name=IndexFieldConstants.COURSE_AUTHOR_NAME, index=Index.TOKENIZED, store=Store.YES),
	    @Field(name=IndexFieldConstants.COURSE_AUTHOR_NAME_SORT, index=Index.UN_TOKENIZED, store=Store.YES)
	})
    public String getCourseAuthorName() {
        return courseAuthorName;
    }

    public void setCourseAuthorName(String courseAuthorName) {
        this.courseAuthorName = courseAuthorName;
    }

    @Column(name = "create_datetime", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @Field(name=IndexFieldConstants.COURSE_CREATE_DATETIME, index=Index.UN_TOKENIZED, store=Store.YES)
    @DateBridge(resolution = Resolution.SECOND)
    public Date getCourseCreateDate() {
        return courseCreateDate;
    }

    public void setCourseCreateDate(Date courseCreateDate) {
        this.courseCreateDate = courseCreateDate;
    }

    @Column(name = "last_update_datetime", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @Field(name=IndexFieldConstants.COURSE_LAST_UPDATE_DATETIME, index=Index.UN_TOKENIZED, store=Store.YES)
    @DateBridge(resolution = Resolution.SECOND)
    public Date getCourseLastUpdateDate() {
        return courseLastUpdateDate;
    }

    public void setCourseLastUpdateDate(Date courseLastUpdateDate) {
        this.courseLastUpdateDate = courseLastUpdateDate;
    }

    @Column(name = "creator", length = 50, nullable = false)
    @Field(name=IndexFieldConstants.COURSE_CREATOR, index=Index.UN_TOKENIZED, store=Store.YES)
    public String getCourseCreator() {
        return courseCreator;
    }

    public void setCourseCreator(String courseCreator) {
        this.courseCreator = courseCreator;
    }

    @Column(name = "tag", length = 100, nullable = false)
    @Fields({
        @Field(name=IndexFieldConstants.COURSE_TAG, index=Index.TOKENIZED, store=Store.YES),
        @Field(name=IndexFieldConstants.COURSE_TAG_SORT, index=Index.UN_TOKENIZED, store=Store.YES)
    })
    public String getCourseCategoryTag() {
        return courseCategoryTag;
    }

    public void setCourseCategoryTag(String courseCategoryTag) {
        this.courseCategoryTag = courseCategoryTag;
    }

    @Column(name="is_deleted", nullable=false)
    @Field(name=IndexFieldConstants.COURSE_IS_DELETED, index=Index.UN_TOKENIZED, store=Store.YES)
    public Integer getCourseIsDeleted() {
		return courseIsDeleted;
	}

	public void setCourseIsDeleted(Integer courseIsDeleted) {
		this.courseIsDeleted = courseIsDeleted;
	}

	@Column(name="has_attachments", nullable=false)
	@Field(name=IndexFieldConstants.COURSE_HAS_ATTACHMENTS, index=Index.UN_TOKENIZED, store=Store.YES)
	public Integer getCourseHasAttachment() {
		return courseHasAttachment;
	}

	public void setCourseHasAttachment(Integer courseHasAttachment) {
		this.courseHasAttachment = courseHasAttachment;
	}

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "courseAttach", fetch = FetchType.LAZY)
    public List<CourseAttachment> getCourseAttachments() {
        return courseAttachments;
    }

    public void setCourseAttachments(List<CourseAttachment> courseAttachments) {
        this.courseAttachments = courseAttachments;
    }
    
    @Column(name="prefix_id_value")
	@Field(name=IndexFieldConstants.PREFIX_ID, index=Index.TOKENIZED, store=Store.YES)
	public String getPrefixIDValue() {
		return prefixIDValue;
	}

	public void setPrefixIDValue(String prefixIDValue) {
		this.prefixIDValue = prefixIDValue;
	}

	@Column(name="historyTrainers", length=1000, nullable=true)
	@Field(name=IndexFieldConstants.COURSE_HISTORY_TRAINERS, index=Index.TOKENIZED, store=Store.YES)
	public String getHistoryTrainers() {
        return historyTrainers;
    }

    public void setHistoryTrainers(String historyTrainers) {
        this.historyTrainers = historyTrainers;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime
                * result
                + ((courseAttachments == null) ? 0 : courseAttachments
                        .hashCode());
        result = prime
                * result
                + ((courseAuthorName == null) ? 0 : courseAuthorName.hashCode());
        result = prime * result
                + ((courseBrief == null) ? 0 : courseBrief.hashCode());
        result = prime
                * result
                + ((courseBriefWithoutTag == null) ? 0 : courseBriefWithoutTag
                        .hashCode());
        result = prime
                * result
                + ((courseCategoryTag == null) ? 0 : courseCategoryTag
                        .hashCode());
        result = prime
                * result
                + ((courseCreateDate == null) ? 0 : courseCreateDate.hashCode());
        result = prime * result
                + ((courseCreator == null) ? 0 : courseCreator.hashCode());
        result = prime * result
                + ((courseDuration == null) ? 0 : courseDuration.hashCode());
        result = prime
                * result
                + ((courseHasAttachment == null) ? 0 : courseHasAttachment
                        .hashCode());
        result = prime
                * result
                + ((courseHasHomework == null) ? 0 : courseHasHomework
                        .hashCode());
        result = prime * result
                + ((courseId == null) ? 0 : courseId.hashCode());
        result = prime
                * result
                + ((courseIsCertificated == null) ? 0 : courseIsCertificated
                        .hashCode());
        result = prime * result
                + ((courseIsDeleted == null) ? 0 : courseIsDeleted.hashCode());
        result = prime
                * result
                + ((courseLastUpdateDate == null) ? 0 : courseLastUpdateDate
                        .hashCode());
        result = prime * result
                + ((courseName == null) ? 0 : courseName.hashCode());
        result = prime
                * result
                + ((courseTargetTrainee == null) ? 0 : courseTargetTrainee
                        .hashCode());
        result = prime * result
                + ((courseType == null) ? 0 : courseType.hashCode());
        result = prime * result
                + ((historyTrainers == null) ? 0 : historyTrainers.hashCode());
        result = prime * result
                + ((prefixIDValue == null) ? 0 : prefixIDValue.hashCode());
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
        Course other = (Course) obj;
        if (courseAttachments == null) {
            if (other.courseAttachments != null)
                return false;
        } else if (!courseAttachments.equals(other.courseAttachments))
            return false;
        if (courseAuthorName == null) {
            if (other.courseAuthorName != null)
                return false;
        } else if (!courseAuthorName.equals(other.courseAuthorName))
            return false;
        if (courseBrief == null) {
            if (other.courseBrief != null)
                return false;
        } else if (!courseBrief.equals(other.courseBrief))
            return false;
        if (courseBriefWithoutTag == null) {
            if (other.courseBriefWithoutTag != null)
                return false;
        } else if (!courseBriefWithoutTag.equals(other.courseBriefWithoutTag))
            return false;
        if (courseCategoryTag == null) {
            if (other.courseCategoryTag != null)
                return false;
        } else if (!courseCategoryTag.equals(other.courseCategoryTag))
            return false;
        if (courseCreateDate == null) {
            if (other.courseCreateDate != null)
                return false;
        } else if (!courseCreateDate.equals(other.courseCreateDate))
            return false;
        if (courseCreator == null) {
            if (other.courseCreator != null)
                return false;
        } else if (!courseCreator.equals(other.courseCreator))
            return false;
        if (courseDuration == null) {
            if (other.courseDuration != null)
                return false;
        } else if (!courseDuration.equals(other.courseDuration))
            return false;
        if (courseHasAttachment == null) {
            if (other.courseHasAttachment != null)
                return false;
        } else if (!courseHasAttachment.equals(other.courseHasAttachment))
            return false;
        if (courseHasHomework == null) {
            if (other.courseHasHomework != null)
                return false;
        } else if (!courseHasHomework.equals(other.courseHasHomework))
            return false;
        if (courseId == null) {
            if (other.courseId != null)
                return false;
        } else if (!courseId.equals(other.courseId))
            return false;
        if (courseIsCertificated == null) {
            if (other.courseIsCertificated != null)
                return false;
        } else if (!courseIsCertificated.equals(other.courseIsCertificated))
            return false;
        if (courseIsDeleted == null) {
            if (other.courseIsDeleted != null)
                return false;
        } else if (!courseIsDeleted.equals(other.courseIsDeleted))
            return false;
        if (courseLastUpdateDate == null) {
            if (other.courseLastUpdateDate != null)
                return false;
        } else if (!courseLastUpdateDate.equals(other.courseLastUpdateDate))
            return false;
        if (courseName == null) {
            if (other.courseName != null)
                return false;
        } else if (!courseName.equals(other.courseName))
            return false;
        if (courseTargetTrainee == null) {
            if (other.courseTargetTrainee != null)
                return false;
        } else if (!courseTargetTrainee.equals(other.courseTargetTrainee))
            return false;
        if (courseType == null) {
            if (other.courseType != null)
                return false;
        } else if (!courseType.equals(other.courseType))
            return false;
        if (historyTrainers == null) {
            if (other.historyTrainers != null)
                return false;
        } else if (!historyTrainers.equals(other.historyTrainers))
            return false;
        if (prefixIDValue == null) {
            if (other.prefixIDValue != null)
                return false;
        } else if (!prefixIDValue.equals(other.prefixIDValue))
            return false;
        return true;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Course [courseAuthorName=");
        builder.append(courseAuthorName);
        builder.append(", courseBrief=");
        builder.append(courseBrief);
        builder.append(", courseBriefWithoutTag=");
        builder.append(courseBriefWithoutTag);
        builder.append(", courseCategoryTag=");
        builder.append(courseCategoryTag);
        builder.append(", courseCreateDate=");
        builder.append(courseCreateDate);
        builder.append(", courseCreator=");
        builder.append(courseCreator);
        builder.append(", courseDuration=");
        builder.append(courseDuration);
        builder.append(", courseHasAttachment=");
        builder.append(courseHasAttachment);
        builder.append(", courseHasHomework=");
        builder.append(courseHasHomework);
        builder.append(", courseId=");
        builder.append(courseId);
        builder.append(", courseIsCertificated=");
        builder.append(courseIsCertificated);
        builder.append(", courseIsDeleted=");
        builder.append(courseIsDeleted);
        builder.append(", courseLastUpdateDate=");
        builder.append(courseLastUpdateDate);
        builder.append(", courseName=");
        builder.append(courseName);
        builder.append(", courseTargetTrainee=");
        builder.append(courseTargetTrainee);
        builder.append(", courseType=");
        builder.append(courseType);
        builder.append(", historyTrainers=");
        builder.append(historyTrainers);
        builder.append(", prefixIDValue=");
        builder.append(prefixIDValue);
        builder.append("]");
        return builder.toString();
    }
    
}
