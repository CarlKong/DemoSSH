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
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.search.annotations.Analyzer;
import org.hibernate.search.annotations.ContainedIn;
import org.hibernate.search.annotations.DateBridge;
import org.hibernate.search.annotations.DocumentId;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.IndexedEmbedded;
import org.hibernate.search.annotations.Resolution;
import org.hibernate.search.annotations.Store;
import org.wltea.analyzer.lucene.IKAnalyzer;


@Entity
@Table(name = "assessment")
@Indexed(index="assessment_index")
@Analyzer(impl = IKAnalyzer.class)
public class Assessment implements Serializable {


	/**
	 * 
	 */
	private static final long serialVersionUID = 4074716192674320254L;

	/**the id of the assessment */
	private Integer assessId;
	
	private Integer typeFlag;
	
	private Integer planId;
	
	private Integer planCourseId;
	
	private Integer masterId;
	
	private Integer trainerId;
	
	private Integer traineeId;
	
	/**whether the employee ignore a to_do item, default:1,show not ignore */
	private Integer isIgnore;
	
	/**the comments of the assessment */
	private String assessComment;
	
	private Integer assessIsDeleted;
	
	private Date createDate;
	
	private Date lastUpdateDate;
	
	private Integer hasBeenAssessed;
	
	/**link to the assessment score*/
	private List<AssessmentScore> assessScoreList = new ArrayList<AssessmentScore>();
	
	private AssessmentAttendLog assessmentAttendLog;

	@GenericGenerator(name = "generator", strategy = "increment")
    @Id
    @GeneratedValue(generator = "generator")
    @Column(name = "id", unique = true, nullable = false)
    @DocumentId(name="id")
	public Integer getAssessId() {
		return assessId;
	}

	public void setAssessId(Integer assessId) {
		this.assessId = assessId;
	}

	@Column(name = "type_flag")
    @Field(name="type_flag", index=Index.UN_TOKENIZED, store=Store.YES)
	public Integer getTypeFlag() {
		return typeFlag;
	}

	public void setTypeFlag(Integer typeFlag) {
		this.typeFlag = typeFlag;
	}

	@Column(name = "plan_id")
	@Field(name="plan_id", index=Index.UN_TOKENIZED, store=Store.YES)
	public Integer getPlanId() {
		return planId;
	}

	public void setPlanId(Integer planId) {
		this.planId = planId;
	}

	@Column(name = "plan_course_id")
	@Field(name="plan_course_id", index=Index.UN_TOKENIZED, store=Store.YES)
	public Integer getPlanCourseId() {
		return planCourseId;
	}

	public void setPlanCourseId(Integer planCourseId) {
		this.planCourseId = planCourseId;
	}

	@Column(name = "master_id")
    @Field(name="master_id", index=Index.UN_TOKENIZED, store=Store.YES)
	public Integer getMasterId() {
		return masterId;
	}

	public void setMasterId(Integer masterId) {
		this.masterId = masterId;
	}

	@Column(name = "trainer_id")
	@Field(name="trainer_id", index=Index.UN_TOKENIZED, store=Store.YES)
	public Integer getTrainerId() {
		return trainerId;
	}

	public void setTrainerId(Integer trainerId) {
		this.trainerId = trainerId;
	}

	@Column(name = "trainee_id")
	@Field(name="trainee_id", index=Index.UN_TOKENIZED, store=Store.YES)
	public Integer getTraineeId() {
		return traineeId;
	}

	public void setTraineeId(Integer traineeId) {
		this.traineeId = traineeId;
	}

	@Column(name = "is_ignore")
	@Field(name="is_ignore", index=Index.UN_TOKENIZED, store=Store.YES)
	public Integer getIsIgnore() {
		return isIgnore;
	}

	public void setIsIgnore(Integer isIgnore) {
		this.isIgnore = isIgnore;
	}

	@Column(name = "assess_comment")
	@Field(name="assess_comment", index=Index.TOKENIZED, store=Store.NO)
	public String getAssessComment() {
		return assessComment;
	}

	public void setAssessComment(String assessComment) {
		this.assessComment = assessComment;
	}

	@Column(name = "is_deleted")
	@Field(name="is_deleted", index=Index.UN_TOKENIZED, store=Store.YES)
	public Integer getAssessIsDeleted() {
		return assessIsDeleted;
	}

	public void setAssessIsDeleted(Integer assessIsDeleted) {
		this.assessIsDeleted = assessIsDeleted;
	}

	@Column(name = "create_datetime")
	@Temporal(TemporalType.TIMESTAMP)
	@Field(name="create_datetime", index=Index.UN_TOKENIZED, store=Store.YES)
    @DateBridge(resolution = Resolution.SECOND)
	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	@Column(name = "update_datetime")
	@Temporal(TemporalType.TIMESTAMP)
	@Field(name="update_datetime", index=Index.UN_TOKENIZED, store=Store.YES)
    @DateBridge(resolution = Resolution.SECOND)
	public Date getLastUpdateDate() {
		return lastUpdateDate;
	}

	public void setLastUpdateDate(Date lastUpdateDate) {
		this.lastUpdateDate = lastUpdateDate;
	}

	@OneToMany(cascade = CascadeType.ALL, targetEntity=AssessmentScore.class, fetch=FetchType.LAZY, mappedBy="assessment")
	@IndexedEmbedded(depth=1, prefix="assessScores_")
    @ContainedIn
	public List<AssessmentScore> getAssessScoreList() {
		return assessScoreList;
	}

	public void setAssessScoreList(List<AssessmentScore> assessScoreList) {
		this.assessScoreList = assessScoreList;
	}
	
	@OneToOne(cascade = CascadeType.ALL, targetEntity=AssessmentAttendLog.class, fetch=FetchType.LAZY, mappedBy="assessment")
	public AssessmentAttendLog getAssessmentAttendLog() {
		return assessmentAttendLog;
	}

	public void setAssessmentAttendLog(AssessmentAttendLog assessmentAttendLog) {
		this.assessmentAttendLog = assessmentAttendLog;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((assessComment == null) ? 0 : assessComment.hashCode());
		result = prime * result
				+ ((assessId == null) ? 0 : assessId.hashCode());
		result = prime * result
				+ ((assessIsDeleted == null) ? 0 : assessIsDeleted.hashCode());
		result = prime * result
				+ ((createDate == null) ? 0 : createDate.hashCode());
		result = prime * result
				+ ((isIgnore == null) ? 0 : isIgnore.hashCode());
		result = prime * result
				+ ((lastUpdateDate == null) ? 0 : lastUpdateDate.hashCode());
		result = prime * result
				+ ((masterId == null) ? 0 : masterId.hashCode());
		result = prime * result
				+ ((planCourseId == null) ? 0 : planCourseId.hashCode());
		result = prime * result + ((planId == null) ? 0 : planId.hashCode());
		result = prime * result
				+ ((traineeId == null) ? 0 : traineeId.hashCode());
		result = prime * result
				+ ((trainerId == null) ? 0 : trainerId.hashCode());
		result = prime * result
				+ ((typeFlag == null) ? 0 : typeFlag.hashCode());
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
		Assessment other = (Assessment) obj;
		if (assessComment == null) {
			if (other.assessComment != null)
				return false;
		} else if (!assessComment.equals(other.assessComment))
			return false;
		if (assessId == null) {
			if (other.assessId != null)
				return false;
		} else if (!assessId.equals(other.assessId))
			return false;
		if (assessIsDeleted == null) {
			if (other.assessIsDeleted != null)
				return false;
		} else if (!assessIsDeleted.equals(other.assessIsDeleted))
			return false;
		if (createDate == null) {
			if (other.createDate != null)
				return false;
		} else if (!createDate.equals(other.createDate))
			return false;
		if (isIgnore == null) {
			if (other.isIgnore != null)
				return false;
		} else if (!isIgnore.equals(other.isIgnore))
			return false;
		if (lastUpdateDate == null) {
			if (other.lastUpdateDate != null)
				return false;
		} else if (!lastUpdateDate.equals(other.lastUpdateDate))
			return false;
		if (masterId == null) {
			if (other.masterId != null)
				return false;
		} else if (!masterId.equals(other.masterId))
			return false;
		if (planCourseId == null) {
			if (other.planCourseId != null)
				return false;
		} else if (!planCourseId.equals(other.planCourseId))
			return false;
		if (planId == null) {
			if (other.planId != null)
				return false;
		} else if (!planId.equals(other.planId))
			return false;
		if (traineeId == null) {
			if (other.traineeId != null)
				return false;
		} else if (!traineeId.equals(other.traineeId))
			return false;
		if (trainerId == null) {
			if (other.trainerId != null)
				return false;
		} else if (!trainerId.equals(other.trainerId))
			return false;
		if (typeFlag == null) {
			if (other.typeFlag != null)
				return false;
		} else if (!typeFlag.equals(other.typeFlag))
			return false;
		return true;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Assessment [assessComment=");
		builder.append(assessComment);
		builder.append(", assessId=");
		builder.append(assessId);
		builder.append(", assessIsDeleted=");
		builder.append(assessIsDeleted);
		builder.append(", assessScoreList=");
		builder.append(assessScoreList);
		builder.append(", createDate=");
		builder.append(createDate);
		builder.append(", isIgnore=");
		builder.append(isIgnore);
		builder.append(", lastUpdateDate=");
		builder.append(lastUpdateDate);
		builder.append(", masterId=");
		builder.append(masterId);
		builder.append(", planCourseId=");
		builder.append(planCourseId);
		builder.append(", planId=");
		builder.append(planId);
		builder.append(", traineeId=");
		builder.append(traineeId);
		builder.append(", trainerId=");
		builder.append(trainerId);
		builder.append(", typeFlag=");
		builder.append(typeFlag);
		builder.append("]");
		return builder.toString();
	}

	@Column(name = "has_been_assessed")
    @Field(name="has_been_assessed", index=Index.UN_TOKENIZED, store=Store.YES)
    public Integer getHasBeenAssessed() {
        return hasBeenAssessed;
    }

    public void setHasBeenAssessed(Integer hasBeenAssessed) {
        this.hasBeenAssessed = hasBeenAssessed;
    }

}
