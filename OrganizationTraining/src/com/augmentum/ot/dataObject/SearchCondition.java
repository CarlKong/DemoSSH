package com.augmentum.ot.dataObject;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.augmentum.ot.dataObject.constant.IndexFieldConstants;

import edu.emory.mathcs.backport.java.util.Arrays;


public class SearchCondition {
	 /** Input keywords*/
    private String queryString;
    
    /** Selected search fields, MUST USE strings declared in class ModelColumnNameConstant*/
    private String[] searchFields;
    
    /** Field use to sort. MUST USE one string declared in class ModelColumnNameConstant*/
    private String sortField;
    
    /** Sort order. Descending order use IndexUtil.DESC, else use IndexUtil.ASC*/
    private Boolean reverse;
    
    /**get nowPage.*/
    private int nowPage;
    
    /** For all the search results, select from where to return results.*/
    private int firstResult;
    
    /** For all the search results, select return how many results*/
    private int pageSize;
    
    private Date lowerDate;
    
    /** For date search, refer the upper Date*/
    private Date upperDate;
        
    /**************** Set relevance weight **********************/
    private Map<String, Float> boosts = new HashMap<String, Float>();
    
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

	public Date getLowerDate() {
		return lowerDate;
	}

	public void setLowerDate(Date lowerDate) {
		this.lowerDate = lowerDate;
	}

	public Date getUpperDate() {
		return upperDate;
	}

	public void setUpperDate(Date upperDate) {
		this.upperDate = upperDate;
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
}
