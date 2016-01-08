package com.augmentum.ot.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * @version 0.2, 07/12/2012
 */
@Entity
@Table(name="custom_column")
public class CustomColumns implements Serializable {
	
	private static final long serialVersionUID = 1L;

	/**optional column id**/
	private Integer customColumnId;
	
	/**category flag, 0:'course', 1:program 2:'plan'**/
	private Integer categoryFlag;
	
	/**field name**/
	private String fieldName;
	
	/**sort the optional column**/
	private Integer customColumnOrder;
	
	/**default optional column**/
	private Integer customColumnDefault;
	
	/**set some column disabled**/
	private Integer customColumnDisabled;
	
	/**optional columns belong to employee list**/
	private List<Employee> employeeList = new ArrayList<Employee>();
	
	/**
 	 set get Methods
	*/
	@GenericGenerator(name="generator",strategy="increment")
	@Id
	@GeneratedValue(generator="generator")
	@Column(name="id", nullable=true, unique=true)
	public Integer getCustomColumnId() {
		return customColumnId;
	}

	public void setCustomColumnId(Integer customColumnId) {
		this.customColumnId = customColumnId;
	}

	@Column(name="category_flag", nullable=true)
	public Integer getCategoryFlag() {
		return categoryFlag;
	}

	public void setCategoryFlag(Integer categoryFlag) {
		this.categoryFlag = categoryFlag;
	}

	@Column(name="column_order")
	public Integer getCustomColumnOrder() {
		return customColumnOrder;
	}

	public void setCustomColumnOrder(Integer customColumnOrder) {
		this.customColumnOrder = customColumnOrder;
	}

	@Column(name="is_default", nullable=true)
	public Integer getCustomColumnDefault() {
		return customColumnDefault;
	}

	public void setCustomColumnDefault(Integer customColumnDefault) {
		this.customColumnDefault = customColumnDefault;
	}

	@Column(name="is_disabled", nullable=true)
	public Integer getCustomColumnDisabled() {
		return customColumnDisabled;
	}

	public void setCustomColumnDisabled(Integer customColumnDisabled) {
		this.customColumnDisabled = customColumnDisabled;
	}

	@Column(name="field_name",length=100,nullable=true)
	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	@ManyToMany(mappedBy="customColumnsList",fetch=FetchType.LAZY,cascade=CascadeType.ALL)
	public List<Employee> getEmployeeList() {
		return employeeList;
	}

	public void setEmployeeList(List<Employee> employeeList) {
		this.employeeList = employeeList;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((categoryFlag == null) ? 0 : categoryFlag.hashCode());
		result = prime
				* result
				+ ((customColumnDefault == null) ? 0 : customColumnDefault
						.hashCode());
		result = prime
				* result
				+ ((customColumnDisabled == null) ? 0 : customColumnDisabled
						.hashCode());
		result = prime * result
				+ ((customColumnId == null) ? 0 : customColumnId.hashCode());
		result = prime
				* result
				+ ((customColumnOrder == null) ? 0 : customColumnOrder
						.hashCode());
		result = prime * result
				+ ((fieldName == null) ? 0 : fieldName.hashCode());
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
		CustomColumns other = (CustomColumns) obj;
		if (categoryFlag == null) {
			if (other.categoryFlag != null)
				return false;
		} else if (!categoryFlag.equals(other.categoryFlag))
			return false;
		if (customColumnDefault == null) {
			if (other.customColumnDefault != null)
				return false;
		} else if (!customColumnDefault.equals(other.customColumnDefault))
			return false;
		if (customColumnDisabled == null) {
			if (other.customColumnDisabled != null)
				return false;
		} else if (!customColumnDisabled.equals(other.customColumnDisabled))
			return false;
		if (customColumnId == null) {
			if (other.customColumnId != null)
				return false;
		} else if (!customColumnId.equals(other.customColumnId))
			return false;
		if (customColumnOrder == null) {
			if (other.customColumnOrder != null)
				return false;
		} else if (!customColumnOrder.equals(other.customColumnOrder))
			return false;
		if (fieldName == null) {
			if (other.fieldName != null)
				return false;
		} else if (!fieldName.equals(other.fieldName))
			return false;
		return true;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("CustomColumns [categoryFlag=");
		builder.append(categoryFlag);
		builder.append(", customColumnDefault=");
		builder.append(customColumnDefault);
		builder.append(", customColumnDisabled=");
		builder.append(customColumnDisabled);
		builder.append(", customColumnId=");
		builder.append(customColumnId);
		builder.append(", customColumnOrder=");
		builder.append(customColumnOrder);
		//builder.append(", employeeList.size()=");
		//builder.append(employeeList.size());
		builder.append(", fieldName=");
		builder.append(fieldName);
		builder.append("]");
		return builder.toString();
	}

	


	
	

}
