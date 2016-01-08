package com.augmentum.ot.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "assessment_attend_log")

public class AssessmentAttendLog implements Serializable {
	
	private static final long serialVersionUID = 7090626734445919665L;

	private Integer attendLogId;
	
	private Assessment assessment;
	
	private String attendLogKey;
	
	@Id
	@GenericGenerator(name = "generator", strategy = "increment")
	@GeneratedValue(generator = "generator")
    @Column(name = "attend_log_id", unique = true, nullable = false)
	public Integer getAttendLogId() {
		return attendLogId;
	}

	public void setAttendLogId(Integer attendLogId) {
		this.attendLogId = attendLogId;
	}

	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "assessment_id", referencedColumnName="id", nullable = true)
	public Assessment getAssessment() {
		return assessment;
	}

	public void setAssessment(Assessment assessment) {
		this.assessment = assessment;
	}
	
	@Column(name = "attend_log_key")
	public String getAttendLogKey() {
		return attendLogKey;
	}

	public void setAttendLogKey(String attendLogKey) {
		this.attendLogKey = attendLogKey;
	}
	
	
}
