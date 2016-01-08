package com.augmentum.ot.dataObject;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.augmentum.ot.dataObject.constant.IndexFieldConstants;

/**
 * 
 * @version 0.1, 07/13/2012
 */
public class PlanSearchCondition {
    
	/** All selected course type names MUST BE: "planCourseTypeNames1 planCourseTypeNames2" */
    private String planCourseTypeIds;
    
    /** All selected plan type names MUST BE: "planTypeNames1 planTypeNames2" */
    private String planTypeIds;

    /** Input keywords*/
    private String queryString;
    
    /** Selected search fields, MUST USE strings declared in class ModelColumnNameConstant*/
    private String[] searchFields;
    
    /** Field use to sort. MUST USE one string declared in class ModelColumnNameConstant*/
    private String sortField;
    private String sortName; // sortName for selectList plugin
    
    /** Sort order. Descending order use IndexUtil.DESC, else use IndexUtil.ASC*/
    private Boolean reverse;
    private String sortSign; // sortSign for selectList plugin, "asc" "desc"
    
    /**get nowPage.*/
    private int nowPage;
    private String pageNum; // pageNum for selectList plugin
    
    /** For all the search results, select from where to return results.*/
    private int firstResult;
    
    /** For all the search results, select return how many results*/
    private int pageSize;
    
    /** For date search, refer the lower Date, turned from lowerDateStr*/
    private Date publishLowerDate;
    
    /** For date search, refer the upper Date, turned from upperDateStr*/
    private Date publishUpperDate;
    
    /** For search by execute date, refer the execute lower date, turned from executeLowerDateStr */
    private Date executeLowerDate;
    
    /** For execute date search, refer the execute upper Date, turned from executeUpperDateStr*/
    private Date executeUpperDate;
    
    /** For Master, Trainer, Or Trainee*/
    private String roleFlag;
    
    private String employeeName;
    
    private String searchOperationFlag; // For distinguish search plan or my plan 
	    
    /**************** Set relevance weight **********************/
    private Map<String, Float> boosts = new HashMap<String, Float>();

	public String getPlanCourseTypeIds() {
		return planCourseTypeIds;
	}

	public void setPlanCourseTypeIds(String planCourseTypeIds) {
		this.planCourseTypeIds = planCourseTypeIds;
	}

	public String getPlanTypeIds() {
		return planTypeIds;
	}

	public void setPlanTypeIds(String planTypeIds) {
		this.planTypeIds = planTypeIds;
	}

	public String getQueryString() {
		return queryString;
	}

	public void setQueryString(String queryString) {
		this.queryString = queryString;
	}

	public String[] getSearchFields() {
		return searchFields;
	}

	public void setSearchFields(String[] searchFields) {
		this.searchFields = searchFields;
	}

	public String getSortField() {
		return sortField;
	}

	public void setSortField(String sortField) {
		this.sortField = sortField;
	}

	public Boolean getReverse() {
		return reverse;
	}

	public void setReverse(Boolean reverse) {
		this.reverse = reverse;
	}

	public int getNowPage() {
		return nowPage;
	}

	public void setNowPage(int nowPage) {
		this.nowPage = nowPage;
	}

	public int getFirstResult() {
		if (this.nowPage < 1) { 
    		this.nowPage = 1;
    	}
    	this.firstResult = (this.nowPage - 1) * this.getPageSize();
		return firstResult;
	}

	public void setFirstResult(int firstResult) {
		this.firstResult = firstResult;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public Date getPublishLowerDate() {
		return publishLowerDate;
	}

	public void setPublishLowerDate(Date publishLowerDate) {
		this.publishLowerDate = publishLowerDate;
	}

	public Date getPublishUpperDate() {
		return publishUpperDate;
	}

	public void setPublishUpperDate(Date publishUpperDate) {
		this.publishUpperDate = publishUpperDate;
	}

	public Date getExecuteLowerDate() {
		return executeLowerDate;
	}

	public void setExecuteLowerDate(Date executeLowerDate) {
		this.executeLowerDate = executeLowerDate;
	}

	public Date getExecuteUpperDate() {
		return executeUpperDate;
	}

	public void setExecuteUpperDate(Date executeUpperDate) {
		this.executeUpperDate = executeUpperDate;
	}

	public String getRoleFlag() {
		return roleFlag;
	}

	public void setRoleFlag(String roleFlag) {
		this.roleFlag = roleFlag;
	}

	public String getEmployeeName() {
		return employeeName;
	}

	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}

	public String getSortName() {
		return sortName;
	}

	public void setSortName(String sortName) {
		this.sortName = sortName;
		this.sortField = this.sortName;
	}

	public String getSortSign() {
		return sortSign;
	}

	public void setSortSign(String sortSign) {
		this.sortSign = sortSign;
		if (this.sortSign.equals("desc")) {
			this.reverse = true;
		} else {
			this.reverse = false;
		}
	}

	public String getPageNum() {
		return pageNum;
	}

	public void setPageNum(String pageNum) {
		this.pageNum = pageNum;
		this.nowPage = Integer.parseInt(this.pageNum);
	}

	public String getSearchOperationFlag() {
		return searchOperationFlag;
	}

	public void setSearchOperationFlag(String searchOperationFlag) {
		this.searchOperationFlag = searchOperationFlag;
	}
	
	public Map<String, Float> getBoosts() {
		if (searchFields != null && searchFields.length > 0) {
			for (String field : searchFields) {
				if (Arrays.asList(IndexFieldConstants.HIGH_LEVEL_FIELD).contains(field)) {
					boosts.put(field, 2f);
				} else {
					boosts.put(field, 1f);
				}
			}
		}
		return boosts;
	}

	public void setBoosts(Map<String, Float> boosts) {
		this.boosts = boosts;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("PlanSearchCondition [employeeName=");
		builder.append(employeeName);
		builder.append(", executeLowerDate=");
		builder.append(executeLowerDate);
		builder.append(", executeUpperDate=");
		builder.append(executeUpperDate);
		builder.append(", firstResult=");
		builder.append(firstResult);
		builder.append(", nowPage=");
		builder.append(nowPage);
		builder.append(", pageNum=");
		builder.append(pageNum);
		builder.append(", pageSize=");
		builder.append(pageSize);
		builder.append(", planCourseTypeIds=");
		builder.append(planCourseTypeIds);
		builder.append(", planTypeIds=");
		builder.append(planTypeIds);
		builder.append(", publishLowerDate=");
		builder.append(publishLowerDate);
		builder.append(", publishUpperDate=");
		builder.append(publishUpperDate);
		builder.append(", queryString=");
		builder.append(queryString);
		builder.append(", reverse=");
		builder.append(reverse);
		builder.append(", roleFlag=");
		builder.append(roleFlag);
		builder.append(", searchFields=");
		builder.append(Arrays.toString(searchFields));
		builder.append(", searchOperationFlag=");
		builder.append(searchOperationFlag);
		builder.append(", sortField=");
		builder.append(sortField);
		builder.append(", sortName=");
		builder.append(sortName);
		builder.append(", sortSign=");
		builder.append(sortSign);
		builder.append("]");
		return builder.toString();
	}
	
	
	
}
