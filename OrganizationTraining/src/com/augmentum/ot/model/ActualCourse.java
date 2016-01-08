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
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.search.annotations.Analyzer;
import org.hibernate.search.annotations.ContainedIn;
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
import com.augmentum.ot.exception.ServerErrorException;
import com.augmentum.ot.util.DateHandlerUtils;
import com.augmentum.ot.util.IdToStringUtils;

@Entity
@Table(name = "actual_course")
@Indexed(index="actualCourse_index")
@Analyzer(impl = IKAnalyzer.class)
public class ActualCourse implements Serializable{

	/**  
	 * @Fields: serialVersionUID 
	 */ 
	private static final long serialVersionUID = -8248818873252749423L;

	/** id of actualCourse */
	private Integer actualCourseId;
	
	/** prefixIdValue */
	private String prefixIdValue;
	
	/** name of actualCourse */
	private String courseName;
	
	/** courseBrief of actualCourse */
	private String courseBrief;
	
	/** courseBrief of actualCourse without tag, such as: '<' '>' */
	private String courseBriefWithoutTag;
	
	/** duration course */
    private Double courseDuration;
    
    /** room number of trainning */
    private String courseRoomNum;

    /** start time of coruse */
    private Date courseStartTime;

    /** end time of course */
    private Date courseEndTime;

    /** trainer of courseo */
    private String courseTrainer;
    
    private Integer courseHasAttachment;

    /** Course order in a plan **/
    private Integer courseOrder;
    
    /** link to plan */
    private Plan plan;
    
    /** Program */
    // private Program program;
    
    /** link to courseInfo */
    private CourseInfo courseInfo;
    
    /** link to sessionInfo */
    private SessionInfo sessionInfo;
    
    /**  The trainee number  */
    private int traineeNumber;
    
    /** actual course status
     * 1.green: Plan/Course on-going while nothing to do with them, the status  of plan/course is green.
     * 2.yellow: Assessments not provided, the status of plan/course is yellow.
     * 3.gray: Plan/Course completed, the status of plan/course is gray.   
    */
    private String status;
    
    /** only used to judge if trainee join this plancourse*/
    private int isJoinCourse;
    
    private List<Employee> employeeList = new ArrayList<Employee>();
    
    private List<ActualCourseAttachment> attachments = new ArrayList<ActualCourseAttachment>();
    
    /** contains the trainee who apply leave for the course */
    private List<LeaveNote> leaveNoteList = new ArrayList<LeaveNote>();
    
    /** get the flag of trainee apply leave course
     * 0: planCourse starts
     * 1: planCourse has not started, the trainee has not applied leave 
     * 2: planCourse has not started, the trainee has applied leave
     */
    private int applyLeaveFlag;
    
//    /** contains the trainee who apply leave for the course */
//    private List<Leave> leaveList = new ArrayList<Leave>();

    @GenericGenerator(name = "generator", strategy = "increment")
    @Id
    @GeneratedValue(generator = "generator")
    @Column(name = "id", nullable = false, unique = true)
    @DocumentId(name="id")
	public Integer getActualCourseId() {
		return actualCourseId;
	}

	public void setActualCourseId(Integer actualCourseId) {
		this.actualCourseId = actualCourseId;
		if (this.actualCourseId != null && this.actualCourseId > 0) {
			String prefix = "";
			if (null != this.courseInfo){
				prefix = FlagConstants.PLAN_COURSE_NUMBER_PREFIX;
			}else if(null != this.sessionInfo) {
				prefix = FlagConstants.PLAN_SESSION_NUMBER_PREFIX;
			}
            this.prefixIdValue = IdToStringUtils.addPrefixForId
                (prefix, FlagConstants.ACTUAL_COURSE_ID_NUMBER, actualCourseId);
        }
	}

	@Column(name="prefix_id_value")
	@Field(name=IndexFieldConstants.PREFIX_ID, index=Index.TOKENIZED, store=Store.YES)
	public String getPrefixIdValue() {
		return prefixIdValue;
	}

	public void setPrefixIdValue(String prefixIdValue) {
		this.prefixIdValue = prefixIdValue;
	}

	@Column(name = "name", length = 100, nullable = false)
    @Fields({
        @Field(name="name", index=Index.TOKENIZED, store=Store.YES),
        @Field(name="name_sort", index=Index.UN_TOKENIZED, store=Store.YES)
    })
	public String getCourseName() {
		return courseName;
	}

	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}

	@Column(name = "brief", columnDefinition="text", nullable = false)
    @Field(name="brief", index=Index.TOKENIZED, store=Store.NO)
	public String getCourseBrief() {
		return courseBrief;
	}

	public void setCourseBrief(String courseBrief) {
		this.courseBrief = courseBrief;
	}

	@Column(name = "brief_without_tag", columnDefinition="text", nullable = false)
	@Field(name="brief_without_tag", index=Index.TOKENIZED, store=Store.NO)
	public String getCourseBriefWithoutTag() {
		return courseBriefWithoutTag;
	}

	public void setCourseBriefWithoutTag(String courseBriefWithoutTag) {
		this.courseBriefWithoutTag = courseBriefWithoutTag;
	}

	@Column(name = "duration", precision = 2, scale = 4, nullable = false)
    @Field(name="duration", index=Index.UN_TOKENIZED, store=Store.YES)
	public Double getCourseDuration() {
		return courseDuration;
	}

	public void setCourseDuration(Double courseDuration) {
		this.courseDuration = courseDuration;
	}

	@Column(name = "room_num", length = 50, nullable = true)
    @Field(name="room_num", index=Index.UN_TOKENIZED, store=Store.YES)
	public String getCourseRoomNum() {
		return courseRoomNum;
	}

	public void setCourseRoomNum(String courseRoomNum) {
		this.courseRoomNum = courseRoomNum;
	}

	@Column(name = "start_datetime", nullable = true)
    @Temporal(TemporalType.TIMESTAMP)
    @Field(name="start_datetime", index=Index.UN_TOKENIZED, store=Store.YES)
    @DateBridge(resolution = Resolution.SECOND)
	public Date getCourseStartTime() {
		return courseStartTime;
	}

	public void setCourseStartTime(Date courseStartTime) {
		this.courseStartTime = courseStartTime;
	}

	@Column(name = "end_datetime", nullable = true)
    @Temporal(TemporalType.TIMESTAMP)
	public Date getCourseEndTime() {
		return courseEndTime;
	}

	@Transient
    @Field(name="end_datetime", index=Index.UN_TOKENIZED, store=Store.YES)
    public String getCouresEndTimeForSearch() throws ServerErrorException {
        if (courseEndTime == null) {
            return DateFormatConstants.LARGE_TIME;
        }
        return DateHandlerUtils.dateToString(DateFormatConstants.FULL_TIME_STR, courseEndTime);
    }
	
	public void setCourseEndTime(Date courseEndTime) {
		this.courseEndTime = courseEndTime;
	}

	@Column(name = "trainer", length = 100, nullable = true)
    @Fields({
        @Field(name="trainer", index=Index.TOKENIZED, store=Store.YES),
        @Field(name="trainer_sort", index=Index.UN_TOKENIZED, store=Store.YES)
    })
	public String getCourseTrainer() {
		return courseTrainer;
	}

	public void setCourseTrainer(String courseTrainer) {
		this.courseTrainer = courseTrainer;
	}

	@Column(name="has_attachment", length=1)
    @Field(name="has_attachments", index=Index.UN_TOKENIZED, store=Store.YES)
	public Integer getCourseHasAttachment() {
		return courseHasAttachment;
	}

	public void setCourseHasAttachment(Integer courseHasAttachment) {
		this.courseHasAttachment = courseHasAttachment;
	}

	@Column(name="course_order")
	@Field(name="course_order", index=Index.TOKENIZED, store=Store.YES)
	public Integer getCourseOrder() {
		return courseOrder;
	}

	public void setCourseOrder(Integer courseOrder) {
		this.courseOrder = courseOrder;
	}

	@ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "plan_id", referencedColumnName = "id", nullable = true)
    @IndexedEmbedded(depth=1, prefix="plan.")
    @ContainedIn
	public Plan getPlan() {
		return plan;
	}

	public void setPlan(Plan plan) {
		this.plan = plan;
	}

	@OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(name = "course_info_id", referencedColumnName = "id", nullable = true)
	@IndexedEmbedded(depth=2, prefix="courseInfo.")
	public CourseInfo getCourseInfo() {
		return courseInfo;
	}

	public void setCourseInfo(CourseInfo courseInfo) {
		this.courseInfo = courseInfo;
	}

	@OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(name = "session_info_id", referencedColumnName = "id", nullable = true)
	@IndexedEmbedded(depth=1, prefix="sessionInfo.")
	public SessionInfo getSessionInfo() {
		return sessionInfo;
	}

	public void setSessionInfo(SessionInfo sessionInfo) {
		this.sessionInfo = sessionInfo;
	}

	@Transient
	public int getTraineeNumber() {
	    traineeNumber = this.employeeList.size();
		return traineeNumber;
	}

	public void setTraineeNumber(int traineeNumber) {
		this.traineeNumber = traineeNumber;
	}

	@ManyToMany(fetch=FetchType.LAZY, cascade=CascadeType.ALL, mappedBy="actualCourseList")
	@IndexedEmbedded(depth = 1, prefix = IndexFieldConstants.ACTUAL_COURSE_TRAINEE_PREFIX)
    @ContainedIn
	public List<Employee> getEmployeeList() {
		return employeeList;
	}

	public void setEmployeeList(List<Employee> employeeList) {
		this.employeeList = employeeList;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "actualCourse")
	public List<ActualCourseAttachment> getAttachments() {
		return attachments;
	}

	public void setAttachments(List<ActualCourseAttachment> attachments) {
		this.attachments = attachments;
	}

	 @Transient
     public String getStatus() {
         return status;
     }

     public void setStatus(String status) {
         this.status = status;
     }
    
     @Transient
     public int getIsJoinCourse() {
 		return isJoinCourse;
 	 }

     public void setIsJoinCourse(int isJoinCourse) {
 		 this.isJoinCourse = isJoinCourse;
 	 }

	@Transient
    public int getApplyLeaveFlag() {
        return applyLeaveFlag;
    }

    public void setApplyLeaveFlag(int applyLeaveFlag) {
        this.applyLeaveFlag = applyLeaveFlag;
    }
    
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "actualCourse", targetEntity = LeaveNote.class)
    public List<LeaveNote> getLeaveNoteList() {
        return leaveNoteList;
    }

    public void setLeaveNoteList(List<LeaveNote> leaveNoteList) {
        this.leaveNoteList = leaveNoteList;
    }

    @Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((actualCourseId == null) ? 0 : actualCourseId.hashCode());
		result = prime * result
				+ ((courseBrief == null) ? 0 : courseBrief.hashCode());
		result = prime
				* result
				+ ((courseBriefWithoutTag == null) ? 0 : courseBriefWithoutTag
						.hashCode());
		result = prime * result
				+ ((courseDuration == null) ? 0 : courseDuration.hashCode());
		result = prime * result
				+ ((courseEndTime == null) ? 0 : courseEndTime.hashCode());
		result = prime
				* result
				+ ((courseHasAttachment == null) ? 0 : courseHasAttachment
						.hashCode());
		result = prime * result
				+ ((courseInfo == null) ? 0 : courseInfo.hashCode());
		result = prime * result
				+ ((courseName == null) ? 0 : courseName.hashCode());
		result = prime * result
				+ ((courseOrder == null) ? 0 : courseOrder.hashCode());
		result = prime * result
				+ ((courseRoomNum == null) ? 0 : courseRoomNum.hashCode());
		result = prime * result
				+ ((courseStartTime == null) ? 0 : courseStartTime.hashCode());
		result = prime * result
				+ ((courseTrainer == null) ? 0 : courseTrainer.hashCode());
		result = prime * result + ((plan == null) ? 0 : plan.hashCode());
		result = prime * result
				+ ((prefixIdValue == null) ? 0 : prefixIdValue.hashCode());
		result = prime * result
				+ ((sessionInfo == null) ? 0 : sessionInfo.hashCode());
		result = prime * result + traineeNumber;
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
		ActualCourse other = (ActualCourse) obj;
		if (actualCourseId == null) {
			if (other.actualCourseId != null)
				return false;
		} else if (!actualCourseId.equals(other.actualCourseId))
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
		if (courseDuration == null) {
			if (other.courseDuration != null)
				return false;
		} else if (!courseDuration.equals(other.courseDuration))
			return false;
		if (courseEndTime == null) {
			if (other.courseEndTime != null)
				return false;
		} else if (!courseEndTime.equals(other.courseEndTime))
			return false;
		if (courseHasAttachment == null) {
			if (other.courseHasAttachment != null)
				return false;
		} else if (!courseHasAttachment.equals(other.courseHasAttachment))
			return false;
		if (courseInfo == null) {
			if (other.courseInfo != null)
				return false;
		} else if (!courseInfo.equals(other.courseInfo))
			return false;
		if (courseName == null) {
			if (other.courseName != null)
				return false;
		} else if (!courseName.equals(other.courseName))
			return false;
		if (courseOrder == null) {
			if (other.courseOrder != null)
				return false;
		} else if (!courseOrder.equals(other.courseOrder))
			return false;
		if (courseRoomNum == null) {
			if (other.courseRoomNum != null)
				return false;
		} else if (!courseRoomNum.equals(other.courseRoomNum))
			return false;
		if (courseStartTime == null) {
			if (other.courseStartTime != null)
				return false;
		} else if (!courseStartTime.equals(other.courseStartTime))
			return false;
		if (courseTrainer == null) {
			if (other.courseTrainer != null)
				return false;
		} else if (!courseTrainer.equals(other.courseTrainer))
			return false;
		if (plan == null) {
			if (other.plan != null)
				return false;
		} else if (!plan.equals(other.plan))
			return false;
		if (prefixIdValue == null) {
			if (other.prefixIdValue != null)
				return false;
		} else if (!prefixIdValue.equals(other.prefixIdValue))
			return false;
		if (sessionInfo == null) {
			if (other.sessionInfo != null)
				return false;
		} else if (!sessionInfo.equals(other.sessionInfo))
			return false;
		if (traineeNumber != other.traineeNumber)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ActualCourse [actualCourseId=" + actualCourseId
				+ ", courseBrief=" + courseBrief + ", courseBriefWithoutTag="
				+ courseBriefWithoutTag + ", courseDuration=" + courseDuration
				+ ", courseEndTime=" + courseEndTime + ", courseHasAttachment="
				+ courseHasAttachment + ", courseInfo=" + courseInfo
				+ ", courseName=" + courseName + ", courseOrder=" + courseOrder
				+ ", courseRoomNum=" + courseRoomNum + ", courseStartTime="
				+ courseStartTime + ", courseTrainer=" + courseTrainer
				+ ", plan=" + plan + ", prefixIdValue=" + prefixIdValue
				+ ", sessionInfo=" + sessionInfo + ", traineeNumber=" + traineeNumber + "]";
	}

	
}
