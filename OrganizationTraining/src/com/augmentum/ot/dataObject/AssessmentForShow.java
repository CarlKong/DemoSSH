package com.augmentum.ot.dataObject;

import java.util.List;

public class AssessmentForShow {
	
    private Integer assessmentId;

	private String assessComment;
	
	private Integer employeeId;
	
	private String employeeName;
	
	private String planCourseName;
	
	private String attendenceLog;
	
	private String employeePrefixId;
	
	private Integer isIgnore;
	
	private List<ScoreForShow> scoreList;
	
	private Integer hasBeenAssessed;
	
	private Integer attendLogId;
	
	private String createDate;

	public String getAssessComment() {
		return assessComment;
	}

	public void setAssessComment(String assessComment) {
		this.assessComment = assessComment;
	}
	
	public String getPlanCourseName() {
		return planCourseName;
	}

	public void setPlanCourseName(String planCourseName) {
		this.planCourseName = planCourseName;
	}
	
	public String getEmployeeName() {
		return employeeName;
	}

	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}

	public List<ScoreForShow> getScoreList() {
		return scoreList;
	}

	public void setScoreList(List<ScoreForShow> scoreList) {
		this.scoreList = scoreList;
	}

	public String getAttendenceLog() {
		return attendenceLog;
	}

	public void setAttendenceLog(String attendenceLog) {
		this.attendenceLog = attendenceLog;
	}

    public String getEmployeePrefixId() {
        return employeePrefixId;
    }

    public void setEmployeePrefixId(String employeePrefixId) {
        this.employeePrefixId = employeePrefixId;
    }

    public Integer getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Integer employeeId) {
        this.employeeId = employeeId;
    }

    public Integer getAssessmentId() {
        return assessmentId;
    }

	public void setAssessmentId(Integer assessmentId) {
		this.assessmentId = assessmentId;
	}

	public Integer getHasBeenAssessed() {
		return hasBeenAssessed;
	}

	public void setHasBeenAssessed(Integer hasBeenAssessed) {
		this.hasBeenAssessed = hasBeenAssessed;
	}

	public Integer getIsIgnore() {
		return isIgnore;
	}

	public void setIsIgnore(Integer isIgnore) {
		this.isIgnore = isIgnore;
	}

	public Integer getAttendLogId() {
		return attendLogId;
	}

	public void setAttendLogId(Integer attendLogId) {
		this.attendLogId = attendLogId;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}
	
}
