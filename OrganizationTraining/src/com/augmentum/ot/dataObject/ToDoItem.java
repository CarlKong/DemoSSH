package com.augmentum.ot.dataObject;

public class ToDoItem {

	/**The title**/
	private String itemTitle;
	
	/**The duration time**/
	private String durationTime;
	
	private Integer differentDays;
	
	private Integer planId;
	
	private Integer planCourseId;
	
	private String itemType;
	
	private Integer Isignored;
	
	private String itemContent;
	
	private String prefixId;

	public String getItemTitle() {
		return itemTitle;
	}

	public void setItemTitle(String itemTitle) {
		this.itemTitle = itemTitle;
	}

	public String getDurationTime() {
		return durationTime;
	}

	public void setDurationTime(String durationTime) {
		this.durationTime = durationTime;
	}

	public Integer getDifferentDays() {
		return differentDays;
	}

	public void setDifferentDays(Integer differentDays) {
		this.differentDays = differentDays;
	}

	public String getItemType() {
		return itemType;
	}

	public void setItemType(String itemType) {
		this.itemType = itemType;
	}

	public Integer getIsignored() {
		return Isignored;
	}

	public void setIsignored(Integer isignored) {
		Isignored = isignored;
	}

	public Integer getPlanId() {
		return planId;
	}

	public void setPlanId(Integer planId) {
		this.planId = planId;
	}

	public Integer getPlanCourseId() {
		return planCourseId;
	}

	public void setPlanCourseId(Integer planCourseId) {
		this.planCourseId = planCourseId;
	}

	public String getItemContent() {
		return itemContent;
	}

	public void setItemContent(String itemContent) {
		this.itemContent = itemContent;
	}

	public String getPrefixId() {
		return prefixId;
	}

	public void setPrefixId(String prefixId) {
		this.prefixId = prefixId;
	}
	
}
