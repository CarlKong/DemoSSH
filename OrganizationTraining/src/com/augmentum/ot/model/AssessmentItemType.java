package com.augmentum.ot.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "assessment_item_type")
public class AssessmentItemType implements Serializable {
	
	private static final long serialVersionUID = 1L;

	/** the assessment item type id*/
	private Integer assessItemTypeId;
	
	/** the assessment item type name*/
	private String assessItemTypeName;
	
	@GenericGenerator(name = "generator", strategy = "increment")
	@Id
	@GeneratedValue(generator = "generator")
	@Column(name = "id", unique = true, nullable = false)
	public Integer getAssessItemTypeId() {
		return assessItemTypeId;
	}

	public void setAssessItemTypeId(Integer assessItemTypeId) {
		this.assessItemTypeId = assessItemTypeId;
	}

	@Column(name = "name", length = 100, nullable = false)
	public String getAssessItemTypeName() {
		return assessItemTypeName;
	}

	public void setAssessItemTypeName(String assessItemTypeName) {
		this.assessItemTypeName = assessItemTypeName;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((assessItemTypeId == null) ? 0 : assessItemTypeId.hashCode());
		result = prime
				* result
				+ ((assessItemTypeName == null) ? 0 : assessItemTypeName
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
		AssessmentItemType other = (AssessmentItemType) obj;
		if (assessItemTypeId == null) {
			if (other.assessItemTypeId != null)
				return false;
		} else if (!assessItemTypeId.equals(other.assessItemTypeId))
			return false;
		if (assessItemTypeName == null) {
			if (other.assessItemTypeName != null)
				return false;
		} else if (!assessItemTypeName.equals(other.assessItemTypeName))
			return false;
		return true;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("AssessmentItemType [assessItemTypeId=");
		builder.append(assessItemTypeId);
		builder.append(", assessItemTypeName=");
		builder.append(assessItemTypeName);
		builder.append("]");
		return builder.toString();
	}

}
