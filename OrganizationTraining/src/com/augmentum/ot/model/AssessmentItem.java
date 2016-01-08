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
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "assessment_item")
public class AssessmentItem implements Serializable {
	
	private static final long serialVersionUID = 1L;

	/**the assess item id */
	private Integer assessItemId;
	
	/**the assess item name*/
	private String assessItemName;
	
	/**the assess item describe */
	private String assessItemDescribe;
	
	/**the assess item type is belong to plan or plan_course */
	private AssessmentItemType assessItemType;
	
	private Double avgScore;
	
	private String assessmentItemRate;
	
	private Integer isOptional;
	
	@GenericGenerator(name = "generator", strategy = "increment")
	@Id
	@GeneratedValue(generator = "generator")
	@Column(name = "id", unique = true, nullable = false)
	public Integer getAssessItemId() {
		return assessItemId;
	}
	
	public void setAssessItemId(Integer assessItemId) {
		this.assessItemId = assessItemId;
	}
	
	@Column(name = "name", length = 100, nullable = false)
	public String getAssessItemName() {
		return assessItemName;
	}

	public void setAssessItemName(String assessItemName) {
		this.assessItemName = assessItemName;
	}

	@Column(name = "item_describe", length= 500)
	public String getAssessItemDescribe() {
		return assessItemDescribe;
	}

	public void setAssessItemDescribe(String assessItemDescribe) {
		this.assessItemDescribe = assessItemDescribe;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "type_id", referencedColumnName = "id", nullable = false)
	public AssessmentItemType getAssessItemType() {
		return assessItemType;
	}

	public void setAssessItemType(AssessmentItemType assessItemType) {
		this.assessItemType = assessItemType;
	}

	@Transient
	public Double getAvgScore() {
		return avgScore;
	}

	public void setAvgScore(Double avgScore) {
		this.avgScore = avgScore;
	}
	
	@Column(name="is_optional")
	public Integer getIsOptional() {
		return isOptional;
	}

	public void setIsOptional(Integer isOptional) {
		this.isOptional = isOptional;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((assessItemDescribe == null) ? 0 : assessItemDescribe
						.hashCode());
		result = prime * result
				+ ((assessItemId == null) ? 0 : assessItemId.hashCode());
		result = prime * result
				+ ((assessItemName == null) ? 0 : assessItemName.hashCode());
		
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
		AssessmentItem other = (AssessmentItem) obj;
		if (assessItemDescribe == null) {
			if (other.assessItemDescribe != null)
				return false;
		} else if (!assessItemDescribe.equals(other.assessItemDescribe))
			return false;
		if (assessItemId == null) {
			if (other.assessItemId != null)
				return false;
		} else if (!assessItemId.equals(other.assessItemId))
			return false;
		if (assessItemName == null) {
			if (other.assessItemName != null)
				return false;
		} else if (!assessItemName.equals(other.assessItemName))
			return false;
		if (assessItemType == null) {
			if (other.assessItemType != null)
				return false;
		} else if (!assessItemType.equals(other.assessItemType))
			return false;
		return true;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("AssessmentItem [assessItemDescribe=");
		builder.append(assessItemDescribe);
		builder.append(", assessItemId=");
		builder.append(assessItemId);
		builder.append(", assessItemName=");
		builder.append(assessItemName);
		builder.append("]");
		return builder.toString();
	}

	@Transient
    public String getAssessmentItemRate() {
        return assessmentItemRate;
    }

    public void setAssessmentItemRate(String assessmentItemRate) {
        this.assessmentItemRate = assessmentItemRate;
    }
	
}
