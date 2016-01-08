package com.augmentum.ot.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * @version 0.2, 07/12/2012
 */
@Entity
@Table(name="page_size")
public class PageSize implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer pageSizeId;
	
	/** Flag: flag the page size belong which module. 0:course 1:program 2:plan**/
	private Integer categoryFlag;
	
	private Integer pageSizeValue;
	
	/** Set the page size as the default value**/
	private Integer isDefaultValue;

	/**
	 set get Methods
	 */
	@GenericGenerator(name="generator",strategy="increment")
	@Id
	@GeneratedValue(generator="generator")
	@Column(name="id",nullable=true,unique=true)
	public Integer getPageSizeId() {
		return pageSizeId;
	}

	public void setPageSizeId(Integer pageSizeId) {
		this.pageSizeId = pageSizeId;
	}

	@Column(name="category_flag", nullable=false)
	public Integer getCategoryFlag() {
		return categoryFlag;
	}

	public void setCategoryFlag(Integer categoryFlag) {
		this.categoryFlag = categoryFlag;
	}

	@Column(name="page_value",nullable=false)
	public Integer getPageSizeValue() {
		return pageSizeValue;
	}

	public void setPageSizeValue(Integer pageSizeValue) {
		this.pageSizeValue = pageSizeValue;
	}

	@Column(name="is_default", nullable=false)
	public Integer getIsDefaultValue() {
		return isDefaultValue;
	}

	public void setIsDefaultValue(Integer isDefaultValue) {
		this.isDefaultValue = isDefaultValue;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((categoryFlag == null) ? 0 : categoryFlag.hashCode());
		result = prime * result
				+ ((isDefaultValue == null) ? 0 : isDefaultValue.hashCode());
		result = prime * result
				+ ((pageSizeId == null) ? 0 : pageSizeId.hashCode());
		result = prime * result
				+ ((pageSizeValue == null) ? 0 : pageSizeValue.hashCode());
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
		PageSize other = (PageSize) obj;
		if (categoryFlag == null) {
			if (other.categoryFlag != null)
				return false;
		} else if (!categoryFlag.equals(other.categoryFlag))
			return false;
		if (isDefaultValue == null) {
			if (other.isDefaultValue != null)
				return false;
		} else if (!isDefaultValue.equals(other.isDefaultValue))
			return false;
		if (pageSizeId == null) {
			if (other.pageSizeId != null)
				return false;
		} else if (!pageSizeId.equals(other.pageSizeId))
			return false;
		if (pageSizeValue == null) {
			if (other.pageSizeValue != null)
				return false;
		} else if (!pageSizeValue.equals(other.pageSizeValue))
			return false;
		return true;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("PageSize [categoryFlag=");
		builder.append(categoryFlag);
		builder.append(", isDefaultValue=");
		builder.append(isDefaultValue);
		builder.append(", pageSizeId=");
		builder.append(pageSizeId);
		builder.append(", pageSizeValue=");
		builder.append(pageSizeValue);
		builder.append("]");
		return builder.toString();
	}

}
