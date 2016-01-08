package com.augmentum.ot.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.search.annotations.Analyzer;
import org.hibernate.search.annotations.DocumentId;
import org.hibernate.search.annotations.Indexed;
import org.wltea.analyzer.lucene.IKAnalyzer;

@Entity
@Indexed(index="assessmentScore_index")
@Table(name = "assessment_item_score")
@Analyzer(impl = IKAnalyzer.class)
public class AssessmentScore implements Serializable {

	private static final long serialVersionUID = 1L;

	/** the assessment score id*/
	private Integer assessmentScoreId;
	
	/**the assessment which is assessed*/
	private Assessment assessment;
	
	/**the assessment score of assessment item */
	private AssessmentItem assessmentItem;
	
	/**the assess score */
	private Double assessScore;

	
	@GenericGenerator(name = "generator", strategy = "increment")
	@Id
	@GeneratedValue(generator = "generator")
	@Column(name = "id", unique = true, nullable = false)
	@DocumentId(name="id")
	public Integer getAssessmentScoreId() {
		return assessmentScoreId;
	}

	public void setAssessmentScoreId(Integer assessmentScoreId) {
		this.assessmentScoreId = assessmentScoreId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "assessment_id", referencedColumnName="id", nullable = true)
	public Assessment getAssessment() {
		return assessment;
	}
	
	public void setAssessment(Assessment assessment) {
		this.assessment = assessment;
	}
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "item_id", referencedColumnName="id", nullable = false)
	public AssessmentItem getAssessmentItem() {
		return assessmentItem;
	}
	
	public void setAssessmentItem(AssessmentItem assessmentItem) {
		this.assessmentItem = assessmentItem;
	}
	
	@Column(name = "score" , nullable = true)
	public Double getAssessScore() {
		return assessScore;
	}
	public void setAssessScore(Double assessScore) {
		this.assessScore = assessScore;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((assessScore == null) ? 0 : assessScore.hashCode());
		result = prime * result
				+ ((assessment == null) ? 0 : assessment.hashCode());
		result = prime * result
				+ ((assessmentItem == null) ? 0 : assessmentItem.hashCode());
		result = prime
				* result
				+ ((assessmentScoreId == null) ? 0 : assessmentScoreId
						.hashCode());
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
		AssessmentScore other = (AssessmentScore) obj;
		if (assessScore == null) {
			if (other.assessScore != null)
				return false;
		} else if (!assessScore.equals(other.assessScore))
			return false;
		if (assessment == null) {
			if (other.assessment != null)
				return false;
		} else if (!assessment.equals(other.assessment))
			return false;
		if (assessmentItem == null) {
			if (other.assessmentItem != null)
				return false;
		} else if (!assessmentItem.equals(other.assessmentItem))
			return false;
		if (assessmentScoreId == null) {
			if (other.assessmentScoreId != null)
				return false;
		} else if (!assessmentScoreId.equals(other.assessmentScoreId))
			return false;
		return true;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("AssessmentScore [assessScore=");
		builder.append(assessScore);
		builder.append(", assessmentScoreId=");
		builder.append(assessmentScoreId);
		builder.append("]");
		return builder.toString();
	}

	

}
