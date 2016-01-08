package com.augmentum.ot.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.search.annotations.DocumentId;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Fields;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.IndexedEmbedded;
import org.hibernate.search.annotations.Store;

@Entity
@Table(name = "course_info")
public class CourseInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /** id of courseInfo */
    private Integer courseInfoId; 

    /** id for course */
    private Integer courseId;

    /** target trainee of course */
    private String courseTargetTrainee;

    /** author name of course */
    private String courseAuthorName;
    
    private int courseHasHomework;

    /** the tag of course */
    private String courseCategoryTag;

    /** type of course,link to courType */
    private CourseType courseType;
    
    /** 1:trainer should assess trainee, 0:trainer cannot assess trainee ***/
    private int trainer2Trainee;
    
    /** 1:trainee should assess trainer, 0:trainee cannot assess trainer ***/
    private int trainee2Trainer;
    
    
	@GenericGenerator(name = "generator", strategy = "increment")
    @Id
    @GeneratedValue(generator = "generator")
    @Column(name = "id", nullable = false, unique = true)
    @DocumentId(name="id")
    public Integer getCourseInfoId() {
		return courseInfoId;
	}

	public void setCourseInfoId(Integer courseInfoId) {
		this.courseInfoId = courseInfoId;
	}

    @Column(name = "original_course_id", nullable = false)
    @Field(name="course_id", index=Index.UN_TOKENIZED, store=Store.YES)
    public Integer getCourseId() {
        return courseId;
    }

    public void setCourseId(Integer courseId) {
        this.courseId = courseId;
    }

    @Lob
    @Column(name = "target_trainee", columnDefinition = "TEXT", nullable = false)
    @Field(name="target_trainee", index=Index.TOKENIZED, store=Store.NO)
    public String getCourseTargetTrainee() {
        return courseTargetTrainee;
    }

    public void setCourseTargetTrainee(String courseTargetTrainee) {
        this.courseTargetTrainee = courseTargetTrainee;
    }

    @Column(name = "author_name", nullable = false, length = 100)
    @Fields({
        @Field(name="author_name", index=Index.TOKENIZED, store=Store.YES),
        @Field(name="author_name_sort", index=Index.UN_TOKENIZED, store=Store.YES)
    })
    public String getCourseAuthorName() {
        return courseAuthorName;
    }

    public void setCourseAuthorName(String courseAuthorName) {
        this.courseAuthorName = courseAuthorName;
    }

    @Column(name = "tag", length = 100, nullable = false)
    @Fields({
        @Field(name="tag", index=Index.TOKENIZED, store=Store.YES),
        @Field(name="tag_sort", index=Index.UN_TOKENIZED, store=Store.YES)
    })
    public String getCourseCategoryTag() {
        return courseCategoryTag;
    }

    public void setCourseCategoryTag(String courseCategoryTag) {
        this.courseCategoryTag = courseCategoryTag;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "type_id", referencedColumnName = "id", nullable = false)
    @IndexedEmbedded(depth=1, prefix="courseType_")
    public CourseType getCourseType() {
        return courseType;
    }

    public void setCourseType(CourseType courseType) {
        this.courseType = courseType;
    }

	@Column(name="trainer_to_trainee")
	@Field(name="trainer_to_trainee", index=Index.TOKENIZED, store=Store.YES)
	public int getTrainer2Trainee() {
		return trainer2Trainee;
	}

	public void setTrainer2Trainee(int trainer2Trainee) {
		this.trainer2Trainee = trainer2Trainee;
	}

	@Column(name="trainee_to_trainer")
	@Field(name="trainee_to_trainer", index=Index.TOKENIZED, store=Store.YES)
	public int getTrainee2Trainer() {
		return trainee2Trainer;
	}

	public void setTrainee2Trainer(int trainee2Trainer) {
		this.trainee2Trainer = trainee2Trainer;
	}
	
	@Column(name="course_has_homework")
	@Field(name="course_has_homework", index=Index.TOKENIZED, store=Store.YES)
	public int getCourseHasHomework() {
		return courseHasHomework;
	}

	public void setCourseHasHomework(int courseHasHomework) {
		this.courseHasHomework = courseHasHomework;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((courseAuthorName == null) ? 0 : courseAuthorName.hashCode());
		result = prime
				* result
				+ ((courseCategoryTag == null) ? 0 : courseCategoryTag
						.hashCode());
		result = prime * result
				+ ((courseId == null) ? 0 : courseId.hashCode());
		result = prime * result
				+ ((courseInfoId == null) ? 0 : courseInfoId.hashCode());
		result = prime
				* result
				+ ((courseTargetTrainee == null) ? 0 : courseTargetTrainee
						.hashCode());
		result = prime * result
				+ ((courseType == null) ? 0 : courseType.hashCode());
		result = prime * result + trainee2Trainer;
		result = prime * result + trainer2Trainee;
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
		CourseInfo other = (CourseInfo) obj;
		if (courseAuthorName == null) {
			if (other.courseAuthorName != null)
				return false;
		} else if (!courseAuthorName.equals(other.courseAuthorName))
			return false;
		if (courseCategoryTag == null) {
			if (other.courseCategoryTag != null)
				return false;
		} else if (!courseCategoryTag.equals(other.courseCategoryTag))
			return false;
		if (courseId == null) {
			if (other.courseId != null)
				return false;
		} else if (!courseId.equals(other.courseId))
			return false;
		if (courseInfoId == null) {
			if (other.courseInfoId != null)
				return false;
		} else if (!courseInfoId.equals(other.courseInfoId))
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
		if (trainee2Trainer != other.trainee2Trainer)
			return false;
		if (trainer2Trainee != other.trainer2Trainee)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "CourseInfo [courseAuthorName=" + courseAuthorName
				+ ", courseCategoryTag=" + courseCategoryTag + ", courseId="
				+ courseId + ", courseInfoId=" + courseInfoId
				+ ", courseTargetTrainee=" + courseTargetTrainee
				+ ", courseType=" + courseType + ", trainee2Trainer="
				+ trainee2Trainer + ", trainer2Trainee=" + trainer2Trainee
				+ "]";
	}

	
}
