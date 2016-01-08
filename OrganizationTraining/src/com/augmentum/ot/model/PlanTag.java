package com.augmentum.ot.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "plan_tag")
public class PlanTag implements Serializable {

    private static final long serialVersionUID = 1L;

    /** id of category */
    private Integer planCategoryId;

    /** name of category */
    private String planCategoryName;

    @GenericGenerator(name = "generator", strategy = "increment")
    @Id
    @GeneratedValue(generator = "generator")
    @Column(name = "id", unique = true, nullable = false)
    public Integer getPlanCategoryId() {
		return planCategoryId;
	}

	public void setPlanCategoryId(Integer planCategoryId) {
		this.planCategoryId = planCategoryId;
	}
	
	@Column(name = "name", length = 100, nullable = false, unique = true)
	public String getPlanCategoryName() {
		return planCategoryName;
	}

	public void setPlanCategoryName(String planCategoryName) {
		this.planCategoryName = planCategoryName;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("PlanCategory [planCategoryId=");
		builder.append(planCategoryId);
		builder.append(", planCategoryName=");
		builder.append(planCategoryName);
		builder.append("]");
		return builder.toString();
	}

}
