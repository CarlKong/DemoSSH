package com.augmentum.ot.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Fields;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Store;

import com.augmentum.ot.util.InternationalizationUtils;

/**
 * @version 0.2, 07/12/2012
 */
@Entity
@Table(name = "plan_type")
public class PlanType implements Serializable {

    private static final long serialVersionUID = 1L;

    /** id of playType */
    private Integer planTypeId;

    /** name of plan type */
    private String planTypeName;

    /** link to plan*/
//private List<Plan> planList = new ArrayList<Plan>();

    @GenericGenerator(name = "generator", strategy = "increment")
    @Id
    @GeneratedValue(generator = "generator")
    @Column(name = "id", nullable = false, unique = true)
    public Integer getPlanTypeId() {
        return planTypeId;
    }

    public void setPlanTypeId(Integer planTypeId) {
        this.planTypeId = planTypeId;
    }

    @Column(name = "name", length = 100, nullable = false)
    @Fields({
    	@Field(name="type_name", index=Index.TOKENIZED, store=Store.YES),
        @Field(name="type_name_sort", index=Index.UN_TOKENIZED, store=Store.YES)
    })
    public String getPlanTypeName() {
        return planTypeName;
    }

    public void setPlanTypeName(String planTypeName) {
        this.planTypeName = planTypeName;
    }
    
    @Transient
    @Fields({
        @Field(name="type_name_zh", index=Index.TOKENIZED, store=Store.YES),
        @Field(name="type_name_sort_zh", index=Index.UN_TOKENIZED, store=Store.YES)
    })
    public String getPlanTypeNameZH() {
        return InternationalizationUtils.getValueByKeyZH(this.getPlanTypeName());
    }
    
    @Transient
    @Fields({
        @Field(name="type_name_en", index=Index.TOKENIZED, store=Store.YES),
        @Field(name="type_name_sort_en", index=Index.UN_TOKENIZED, store=Store.YES)
    })
    public String getPlanTypeNameEN() {
        return InternationalizationUtils.getValueByKeyEN(this.getPlanTypeName());
    }

//    @OneToMany(cascade = CascadeType.ALL, mappedBy = "planType", fetch = FetchType.LAZY)
//    public List<Plan> getPlanList() {
//        return planList;
//    }
//
//    public void setPlanList(List<Plan> planList) {
//        this.planList = planList;
//    }

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((planTypeId == null) ? 0 : planTypeId.hashCode());
		result = prime * result
				+ ((planTypeName == null) ? 0 : planTypeName.hashCode());
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
		PlanType other = (PlanType) obj;
		if (planTypeId == null) {
			if (other.planTypeId != null)
				return false;
		} else if (!planTypeId.equals(other.planTypeId))
			return false;
		if (planTypeName == null) {
			if (other.planTypeName != null)
				return false;
		} else if (!planTypeName.equals(other.planTypeName))
			return false;
		return true;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("PlanType [planList.size()=");
//builder.append(planList.size());
		builder.append(", planTypeId=");
		builder.append(planTypeId);
		builder.append(", planTypeName=");
		builder.append(planTypeName);
		builder.append("]");
		return builder.toString();
	}

	
    
}
