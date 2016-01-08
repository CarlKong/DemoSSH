package com.augmentum.ot.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

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

import com.augmentum.ot.dataObject.constant.DateFormatConstants;
import com.augmentum.ot.dataObject.constant.FlagConstants;
import com.augmentum.ot.dataObject.constant.IndexFieldConstants;
import com.augmentum.ot.util.DateHandlerUtils;
import com.augmentum.ot.util.IdToStringUtils;



@Entity
@Table(name = "plan")
@Indexed(index="plan_index")
@Analyzer(impl = IKAnalyzer.class)
public class Plan implements Serializable {

	private static final long serialVersionUID = 1L;

	/** id of plan */
	private Integer planId;

	/** name of plan */
	private String planName;

	/** brief of plan */
	private String planBrief;
	
	   /** brief of plan without tag */
    private String planBriefWithoutTag;

	/** category of plan */
	private String planCategoryTag;

	/** if completed of plan */
	private Integer planIsCompleted;

	/** creator of plan */
	private String planCreator;

	/** date of create plan */
	private Date planCreateDate;

	/** date of last update date */
	private Date planLastUpdateDate;

	/** if publish of plan */
	private Integer planIsPublish;

	/** if have attachment of plan */
	private Integer planHasAttachment;

	/** link to plan type */
	private PlanType planType;

	private Integer planIsDeleted;

	/** course execute start and end date of publishing plan */
	private Date planExecuteStartTime;

	private Date planExecuteEndTime;
	
	/** the register notice has two values in DB, "1" represents YES, "0" represents NO */
	private Integer registerNotice;
	
	/** reminder email has five values in DB, "24":24 hours, "4":4 hours, "2":2 hours, "1":1 hour, "0":NO */
	private Integer reminderEmail;
	
	private Date planPublishDate;
	
	private Integer isAllEmployee;
	
	private Integer planIsCanceled;
	
	private String cancelPlanReason;
	
	private String trainers;
	
	private Integer trainees;
	
	private String status;
	
	/** link to planAttachment */
	private List<PlanAttachment> planAttachments = new ArrayList<PlanAttachment>();

	private Set<PlanEmployeeMap> planEmployeeMapList = new HashSet<PlanEmployeeMap>();

	/** planCourse type ids that plan contains, this field just is used to create in */
	private String courseTypeIds;
	
	/** Store trainee names for index**/
	private String traineeNames;
	
	/** Prefix + Id Value**/
	private String prefixIDValue;
	
	/** 1:plan should be assessed, 0:plan cannot be assessed **/
	private int needAssessment;
	
	private List<ActualCourse> actualCourses = new ArrayList<ActualCourse>();
	
	@GenericGenerator(name = "generator", strategy = "increment")
	@Id
	@GeneratedValue(generator = "generator")
	@Column(name = "id", nullable = false, unique = true)
	@DocumentId(name=IndexFieldConstants.PLAN_ID)
	public Integer getPlanId() {
		return planId;
	}

	public void setPlanId(Integer planId) {
		this.planId = planId;
		if (null != planId) {
			this.prefixIDValue = IdToStringUtils.addPrefixForId
	        (FlagConstants.PLAN_NUMBER_PREFIX, FlagConstants.PLAN_ID_NUMBER, planId);
		}
	}

	@Column(name = "name", length = 200, nullable = false)
	@Fields({
	    @Field(name=IndexFieldConstants.PLAN_NAME, index=Index.TOKENIZED, store=Store.YES),
	    @Field(name=IndexFieldConstants.PLAN_NAME_SORT, index=Index.UN_TOKENIZED, store=Store.YES)
	})
	public String getPlanName() {
		return planName;
	}

	public void setPlanName(String planName) {
		this.planName = planName;
	}

	@Column(name = "brief", columnDefinition="text", nullable = false)
	@Field(name=IndexFieldConstants.PLAN_BRIEF, index=Index.TOKENIZED, store=Store.NO)
	public String getPlanBrief() {
		return planBrief;
	}

	public void setPlanBrief(String planBrief) {
		this.planBrief = planBrief;
	}

	@Column(name = "brief_without_tag", columnDefinition="text", nullable = false)
	@Field(name=IndexFieldConstants.PLAN_BRIEF_WITHOUT_TAG, index=Index.TOKENIZED, store=Store.NO)
	public String getPlanBriefWithoutTag() {
        return planBriefWithoutTag;
    }

    public void setPlanBriefWithoutTag(String planBriefWithoutTag) {
        this.planBriefWithoutTag = planBriefWithoutTag.replace("<", "&lt;").replace(">", "&gt;");
    }

    @Column(name = "tag", length = 100, nullable = false)
	@Fields({
        @Field(name=IndexFieldConstants.PLAN_TAG, index=Index.TOKENIZED, store=Store.YES),
        @Field(name=IndexFieldConstants.PLAN_TAG_SORT, index=Index.UN_TOKENIZED, store=Store.YES)
    })
	public String getPlanCategoryTag() {
		return planCategoryTag;
	}

	public void setPlanCategoryTag(String planCategoryTag) {
		this.planCategoryTag = planCategoryTag;
	}

	@Column(name = "is_completed", nullable = false)
	@Field(name=IndexFieldConstants.PLAN_IS_COMPLETED, index=Index.UN_TOKENIZED, store=Store.YES)
	public Integer getPlanIsCompleted() {
		return planIsCompleted;
	}

	public void setPlanIsCompleted(Integer planIsCompleted) {
		this.planIsCompleted = planIsCompleted;
	}

	@Column(name = "creator", length = 100, nullable = false)
	@Field(name=IndexFieldConstants.PLAN_CREATOR, index=Index.TOKENIZED, store=Store.YES)
	public String getPlanCreator() {
		return planCreator;
	}

	public void setPlanCreator(String planCreator) {
		this.planCreator = planCreator;
	}

	@Column(name = "create_datetime", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	@Field(name=IndexFieldConstants.PLAN_CREATE_DATETIME, index=Index.UN_TOKENIZED, store=Store.YES)
    @DateBridge(resolution = Resolution.SECOND)
	public Date getPlanCreateDate() {
		return planCreateDate;
	}

	public void setPlanCreateDate(Date planCreateDate) {
		this.planCreateDate = planCreateDate;
	}

	@Column(name = "update_datetime", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	@Field(name=IndexFieldConstants.PLAN_UPDATE_DATETIME, index=Index.UN_TOKENIZED, store=Store.YES)
    @DateBridge(resolution = Resolution.SECOND)
	public Date getPlanLastUpdateDate() {
		return planLastUpdateDate;
	}

	public void setPlanLastUpdateDate(Date planLastUpdateDate) {
		this.planLastUpdateDate = planLastUpdateDate;
	}

	@Column(name = "is_published", nullable = false)
	@Field(name=IndexFieldConstants.PLAN_IS_PUBLISHED, index=Index.UN_TOKENIZED, store=Store.YES)
	public Integer getPlanIsPublish() {
		return planIsPublish;
	}

	public void setPlanIsPublish(Integer planIsPublish) {
		this.planIsPublish = planIsPublish;
	}

	@Column(name = "has_attachments")
	@Field(name=IndexFieldConstants.PLAN_HAS_ATTACHMENTS, index=Index.UN_TOKENIZED, store=Store.YES)
	public Integer getPlanHasAttachment() {
		return planHasAttachment;
	}

	public void setPlanHasAttachment(Integer planHasAttachment) {
		this.planHasAttachment = planHasAttachment;
	}

	@Column(name = "is_deleted", nullable = false)
	@Field(name=IndexFieldConstants.PLAN_IS_DELETED, index=Index.UN_TOKENIZED, store=Store.YES)
	public Integer getPlanIsDeleted() {
		return planIsDeleted;
	}

	public void setPlanIsDeleted(Integer planIsDeleted) {
		this.planIsDeleted = planIsDeleted;
	}

	@Column(name = "execute_start_datetime")
	@Temporal(TemporalType.TIMESTAMP)
	public Date getPlanExecuteStartTime() {
		return planExecuteStartTime;
	}
	
	@Transient
	@Field(name=IndexFieldConstants.PLAN_EXECUTE_START_DATETIME, index=Index.UN_TOKENIZED, store=Store.YES)
	public String getPlanExecuteStartTimeForIndex() {
		String planExecuteStartTimeStr = "";
    	if (planExecuteStartTime != null) {
    		planExecuteStartTimeStr = DateHandlerUtils.dateToString(DateFormatConstants.FULL_TIME_STR, planExecuteStartTime);
        }
    	return planExecuteStartTimeStr;
	}

	public void setPlanExecuteStartTime(Date planExecuteStartTime) {
		this.planExecuteStartTime = planExecuteStartTime;
	}

	@Column(name = "execute_end_datetime")
	@Temporal(TemporalType.TIMESTAMP)
	public Date getPlanExecuteEndTime() {
		return planExecuteEndTime;
	}
	
	@Transient
	@Field(name=IndexFieldConstants.PLAN_EXECUTE_END_DATETIME, index=Index.UN_TOKENIZED, store=Store.YES)
	public String getPlanExecuteEndTimeForIndex() {
		String planExecuteEndTimeStr = "";
    	if (planExecuteEndTime != null) {
    		planExecuteEndTimeStr = DateHandlerUtils.dateToString(DateFormatConstants.FULL_TIME_STR, planExecuteEndTime);
        }
    	return planExecuteEndTimeStr;
	}

	public void setPlanExecuteEndTime(Date planExecuteEndTime) {
		this.planExecuteEndTime = planExecuteEndTime;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "type_id", referencedColumnName = "id", nullable = false)
	@IndexedEmbedded(depth=1, prefix=IndexFieldConstants.PREFIX_PLAN_TYPE)
	public PlanType getPlanType() {
		return planType;
	}

	public void setPlanType(PlanType planType) {
		this.planType = planType;
	}
	
	@Column(name = "register_notice")
	@Field(name=IndexFieldConstants.PLAN_REGISTER_NOTICE, index=Index.UN_TOKENIZED, store=Store.YES)
	public Integer getRegisterNotice() {
		return registerNotice;
	}

	public void setRegisterNotice(Integer registerNotice) {
		this.registerNotice = registerNotice;
	}

	@Column(name = "reminder_email")
	@Field(name=IndexFieldConstants.PLAN_REMINDER_EMAIL, index=Index.UN_TOKENIZED, store=Store.YES)
	public Integer getReminderEmail() {
		return reminderEmail;
	}

	public void setReminderEmail(Integer reminderEmail) {
		this.reminderEmail = reminderEmail;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "plan")
	public List<PlanAttachment> getPlanAttachments() {
		return planAttachments;
	}

	public void setPlanAttachments(List<PlanAttachment> planAttachments) {
		this.planAttachments = planAttachments;
	}


	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "plan")
	public Set<PlanEmployeeMap> getPlanEmployeeMapList() {
		return planEmployeeMapList;
	}

	public void setPlanEmployeeMapList(Set<PlanEmployeeMap> planEmployeeMapList) {
		this.planEmployeeMapList = planEmployeeMapList;
	}

    @Column(name = "publish_datetime")
	@Temporal(TemporalType.TIMESTAMP)
	public Date getPlanPublishDate() {
		return planPublishDate;
	}
    
    @Transient
    @Field(name=IndexFieldConstants.PLAN_PUBLISH_DATETIME, index=Index.UN_TOKENIZED, store=Store.YES)
    public String getPlanPublishDateForIndex() {
    	String planPublishDateStr = "";
    	if (planPublishDate != null) {
    		planPublishDateStr = DateHandlerUtils.dateToString(DateFormatConstants.FULL_TIME_STR, planPublishDate);
        }
    	return planPublishDateStr;
    }

	public void setPlanPublishDate(Date planPublishDate) {
		this.planPublishDate = planPublishDate;
	}

	@Column(name = "is_all_employees", nullable = false)
	@Field(name=IndexFieldConstants.PLAN_IS_ALL_EMPLOYEES, index=Index.UN_TOKENIZED, store=Store.YES)
	public Integer getIsAllEmployee() {
		return isAllEmployee;
	}

	public void setIsAllEmployee(Integer isAllEmployee) {
		this.isAllEmployee = isAllEmployee;
	}
	
	@Column(name="is_canceled", nullable = false)
	@Field(name=IndexFieldConstants.PLAN_IS_CANCELED, index=Index.UN_TOKENIZED, store=Store.YES)
    public Integer getPlanIsCanceled() {
        return planIsCanceled;
    }

    public void setPlanIsCanceled(Integer planIsCanceled) {
        this.planIsCanceled = planIsCanceled;
    }
    
	@Column(name="cancel_plan_reason", length=500)
	@Field(name=IndexFieldConstants.PLAN_CANCEL_PLAN_REASON, index=Index.TOKENIZED, store=Store.YES)
	public String getCancelPlanReason() {
		return cancelPlanReason;
	}

	public void setCancelPlanReason(String cancelPlanReason) {
		this.cancelPlanReason = cancelPlanReason;
	}

	@Column(name="trainers", length=3000)
	@Fields({
	    @Field(name=IndexFieldConstants.PLAN_TRAINERS, index=Index.TOKENIZED, store=Store.YES),
	    @Field(name=IndexFieldConstants.PLAN_TRAINERS_SORT, index=Index.UN_TOKENIZED, store=Store.YES)
	})
	public String getTrainers() {
        return trainers;
    }

    public void setTrainers(String trainers) {
        this.trainers = trainers;
    }

    @Column(name="trainees")
    @Field(name=IndexFieldConstants.PLAN_TRAINEES, index=Index.UN_TOKENIZED, store=Store.YES)
    public Integer getTrainees() {
        return trainees;
    }

    public void setTrainees(Integer trainees) {
        this.trainees = trainees;
    }

    @Transient
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    
    @Transient
    @Field(name=IndexFieldConstants.ACTUAL_COURSE_TYPE_IDS, index=Index.TOKENIZED, store=Store.YES)
	public String getCourseTypeIds() {
    	StringBuilder acTypeIds = new StringBuilder();
		if (null != actualCourses && 0 != actualCourses.size()) {
			for (ActualCourse ac:actualCourses) {
				if (null != ac.getCourseInfo()) {
					acTypeIds.append(ac.getCourseInfo().getCourseType().getCourseTypeId());
					acTypeIds.append(FlagConstants.SPLIT_COMMA);
				}
			}
		}
		this.courseTypeIds = acTypeIds.toString();
		
        return courseTypeIds;
    }

    public void setCourseTypeIds(String courseTypeIds) {
        this.courseTypeIds = courseTypeIds;
    }
    
    @Column(name="plan_trainee_names", length=6000)
    @Field(name=IndexFieldConstants.PLAN_TRAINEE_NAMES, index=Index.TOKENIZED, store=Store.YES)
    public String getTraineeNames() {
		return traineeNames;
	}

	public void setTraineeNames(String traineeNames) {
		this.traineeNames = traineeNames;
	}

	@Column(name="prefix_id_value")
	@Field(name=IndexFieldConstants.PREFIX_ID, index=Index.TOKENIZED, store=Store.YES)
	public String getPrefixIDValue() {
		return prefixIDValue;
	}

	public void setPrefixIDValue(String prefixIDValue) {
		this.prefixIDValue = prefixIDValue;
	}
	
	@Column(name = "need_assessment", nullable = false)
	@Field(name=IndexFieldConstants.PLAN_NEED_ASSESSMENT, index=Index.UN_TOKENIZED, store=Store.YES)
	public int getNeedAssessment() {
		return needAssessment;
	}

	public void setNeedAssessment(int needAssessment) {
		this.needAssessment = needAssessment;
	}
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "plan", targetEntity = ActualCourse.class)
	public List<ActualCourse> getActualCourses() {
		return actualCourses;
	}

	public void setActualCourses(List<ActualCourse> actualCourses) {
		this.actualCourses = actualCourses;
	}

	@Transient
	public boolean isInformationCorrect() {
	    boolean flag = true;
	    if (this.planName == null) {
	        flag = false;
	    }
	    if (this.planBrief == null) {
	        flag = false;
	    }
	    if (this.planBriefWithoutTag == null || this.planBriefWithoutTag.length() > 10000){
	        flag = false;
	    }
	    return flag;
	}

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime
                * result
                + ((cancelPlanReason == null) ? 0 : cancelPlanReason.hashCode());
        result = prime * result
                + ((courseTypeIds == null) ? 0 : courseTypeIds.hashCode());
        result = prime * result
                + ((isAllEmployee == null) ? 0 : isAllEmployee.hashCode());
        result = prime * result + needAssessment;
        result = prime * result
                + ((planBrief == null) ? 0 : planBrief.hashCode());
        result = prime
                * result
                + ((planBriefWithoutTag == null) ? 0 : planBriefWithoutTag
                        .hashCode());
        result = prime * result
                + ((planCategoryTag == null) ? 0 : planCategoryTag.hashCode());
        result = prime * result
                + ((planCreateDate == null) ? 0 : planCreateDate.hashCode());
        result = prime * result
                + ((planCreator == null) ? 0 : planCreator.hashCode());
        result = prime
                * result
                + ((planExecuteEndTime == null) ? 0 : planExecuteEndTime
                        .hashCode());
        result = prime
                * result
                + ((planExecuteStartTime == null) ? 0 : planExecuteStartTime
                        .hashCode());
        result = prime
                * result
                + ((planHasAttachment == null) ? 0 : planHasAttachment
                        .hashCode());
        result = prime * result + ((planId == null) ? 0 : planId.hashCode());
        result = prime * result
                + ((planIsCanceled == null) ? 0 : planIsCanceled.hashCode());
        result = prime * result
                + ((planIsCompleted == null) ? 0 : planIsCompleted.hashCode());
        result = prime * result
                + ((planIsDeleted == null) ? 0 : planIsDeleted.hashCode());
        result = prime * result
                + ((planIsPublish == null) ? 0 : planIsPublish.hashCode());
        result = prime
                * result
                + ((planLastUpdateDate == null) ? 0 : planLastUpdateDate
                        .hashCode());
        result = prime * result
                + ((planName == null) ? 0 : planName.hashCode());
        result = prime * result
                + ((planPublishDate == null) ? 0 : planPublishDate.hashCode());
        result = prime * result
                + ((planType == null) ? 0 : planType.hashCode());
        result = prime * result
                + ((prefixIDValue == null) ? 0 : prefixIDValue.hashCode());
        result = prime * result
                + ((registerNotice == null) ? 0 : registerNotice.hashCode());
        result = prime * result
                + ((reminderEmail == null) ? 0 : reminderEmail.hashCode());
        result = prime * result + ((status == null) ? 0 : status.hashCode());
        result = prime * result
                + ((traineeNames == null) ? 0 : traineeNames.hashCode());
        result = prime * result
                + ((trainees == null) ? 0 : trainees.hashCode());
        result = prime * result
                + ((trainers == null) ? 0 : trainers.hashCode());
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
        Plan other = (Plan) obj;
        if (cancelPlanReason == null) {
            if (other.cancelPlanReason != null)
                return false;
        } else if (!cancelPlanReason.equals(other.cancelPlanReason))
            return false;
        if (courseTypeIds == null) {
            if (other.courseTypeIds != null)
                return false;
        } else if (!courseTypeIds.equals(other.courseTypeIds))
            return false;
        if (isAllEmployee == null) {
            if (other.isAllEmployee != null)
                return false;
        } else if (!isAllEmployee.equals(other.isAllEmployee))
            return false;
        if (needAssessment != other.needAssessment)
            return false;
        if (planBrief == null) {
            if (other.planBrief != null)
                return false;
        } else if (!planBrief.equals(other.planBrief))
            return false;
        if (planBriefWithoutTag == null) {
            if (other.planBriefWithoutTag != null)
                return false;
        } else if (!planBriefWithoutTag.equals(other.planBriefWithoutTag))
            return false;
        if (planCategoryTag == null) {
            if (other.planCategoryTag != null)
                return false;
        } else if (!planCategoryTag.equals(other.planCategoryTag))
            return false;
        if (planCreateDate == null) {
            if (other.planCreateDate != null)
                return false;
        } else if (!planCreateDate.equals(other.planCreateDate))
            return false;
        if (planCreator == null) {
            if (other.planCreator != null)
                return false;
        } else if (!planCreator.equals(other.planCreator))
            return false;
        if (planExecuteEndTime == null) {
            if (other.planExecuteEndTime != null)
                return false;
        } else if (!planExecuteEndTime.equals(other.planExecuteEndTime))
            return false;
        if (planExecuteStartTime == null) {
            if (other.planExecuteStartTime != null)
                return false;
        } else if (!planExecuteStartTime.equals(other.planExecuteStartTime))
            return false;
        if (planHasAttachment == null) {
            if (other.planHasAttachment != null)
                return false;
        } else if (!planHasAttachment.equals(other.planHasAttachment))
            return false;
        if (planId == null) {
            if (other.planId != null)
                return false;
        } else if (!planId.equals(other.planId))
            return false;
        if (planIsCanceled == null) {
            if (other.planIsCanceled != null)
                return false;
        } else if (!planIsCanceled.equals(other.planIsCanceled))
            return false;
        if (planIsCompleted == null) {
            if (other.planIsCompleted != null)
                return false;
        } else if (!planIsCompleted.equals(other.planIsCompleted))
            return false;
        if (planIsDeleted == null) {
            if (other.planIsDeleted != null)
                return false;
        } else if (!planIsDeleted.equals(other.planIsDeleted))
            return false;
        if (planIsPublish == null) {
            if (other.planIsPublish != null)
                return false;
        } else if (!planIsPublish.equals(other.planIsPublish))
            return false;
        if (planLastUpdateDate == null) {
            if (other.planLastUpdateDate != null)
                return false;
        } else if (!planLastUpdateDate.equals(other.planLastUpdateDate))
            return false;
        if (planName == null) {
            if (other.planName != null)
                return false;
        } else if (!planName.equals(other.planName))
            return false;
        if (planPublishDate == null) {
            if (other.planPublishDate != null)
                return false;
        } else if (!planPublishDate.equals(other.planPublishDate))
            return false;
        if (planType == null) {
            if (other.planType != null)
                return false;
        } else if (!planType.equals(other.planType))
            return false;
        if (prefixIDValue == null) {
            if (other.prefixIDValue != null)
                return false;
        } else if (!prefixIDValue.equals(other.prefixIDValue))
            return false;
        if (registerNotice == null) {
            if (other.registerNotice != null)
                return false;
        } else if (!registerNotice.equals(other.registerNotice))
            return false;
        if (reminderEmail == null) {
            if (other.reminderEmail != null)
                return false;
        } else if (!reminderEmail.equals(other.reminderEmail))
            return false;
        if (status == null) {
            if (other.status != null)
                return false;
        } else if (!status.equals(other.status))
            return false;
        if (traineeNames == null) {
            if (other.traineeNames != null)
                return false;
        } else if (!traineeNames.equals(other.traineeNames))
            return false;
        if (trainees == null) {
            if (other.trainees != null)
                return false;
        } else if (!trainees.equals(other.trainees))
            return false;
        if (trainers == null) {
            if (other.trainers != null)
                return false;
        } else if (!trainers.equals(other.trainers))
            return false;
        return true;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Plan [cancelPlanReason=");
        builder.append(cancelPlanReason);
        builder.append(", courseTypeIds=");
        builder.append(courseTypeIds);
        builder.append(", isAllEmployee=");
        builder.append(isAllEmployee);
        builder.append(", needAssessment=");
        builder.append(needAssessment);
        builder.append(", planBrief=");
        builder.append(planBrief);
        builder.append(", planBriefWithoutTag=");
        builder.append(planBriefWithoutTag);
        builder.append(", planCategoryTag=");
        builder.append(planCategoryTag);
        builder.append(", actualCourseNames=");
        builder.append(", planCreateDate=");
        builder.append(planCreateDate);
        builder.append(", planCreator=");
        builder.append(planCreator);
        builder.append(", planExecuteEndTime=");
        builder.append(planExecuteEndTime);
        builder.append(", planExecuteStartTime=");
        builder.append(planExecuteStartTime);
        builder.append(", planHasAttachment=");
        builder.append(planHasAttachment);
        builder.append(", planId=");
        builder.append(planId);
        builder.append(", planIsCanceled=");
        builder.append(planIsCanceled);
        builder.append(", planIsCompleted=");
        builder.append(planIsCompleted);
        builder.append(", planIsDeleted=");
        builder.append(planIsDeleted);
        builder.append(", planIsPublish=");
        builder.append(planIsPublish);
        builder.append(", planLastUpdateDate=");
        builder.append(planLastUpdateDate);
        builder.append(", planName=");
        builder.append(planName);
        builder.append(", planPublishDate=");
        builder.append(planPublishDate);
        builder.append(", planType=");
        builder.append(planType);
        builder.append(", prefixIDValue=");
        builder.append(prefixIDValue);
        builder.append(", registerNotice=");
        builder.append(registerNotice);
        builder.append(", reminderEmail=");
        builder.append(reminderEmail);
        builder.append(", status=");
        builder.append(status);
        builder.append(", traineeNames=");
        builder.append(traineeNames);
        builder.append(", trainees=");
        builder.append(trainees);
        builder.append(", trainers=");
        builder.append(trainers);
        builder.append("]");
        return builder.toString();
    }
}
