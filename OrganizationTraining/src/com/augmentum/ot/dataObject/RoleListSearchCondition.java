package com.augmentum.ot.dataObject;

public class RoleListSearchCondition {
    //All info from page
    //Info from query String input
    private String queryString = "";
    //Info from filter box
    private String searchFields = "";
    //Info from filter box
    private String roleTypes = "";
    //Info from sort field bar
    private String sortField = "";
    //Info from sort sign bar
    private String sortSign = "";
    //Info from data list
    private int firstResult = 0;
    //Info from page control
    private int pageNum = 1;
    //Info from page control
    private int pageSize = 0;

    public String getQueryString() {
        return queryString;
    }

    public void setQueryString(String queryString) {
        this.queryString = queryString;
    }

    public String getSearchFields() {
        return searchFields;
    }

    public void setSearchFields(String searchFields) {
        this.searchFields = searchFields;
    }

    public String getRoleTypes() {
        return roleTypes;
    }

    public void setRoleTypes(String roleTypes) {
        this.roleTypes = roleTypes;
    }

    public String getSortField() {
        return sortField;
    }

    public void setSortField(String sortField) {
        this.sortField = sortField;
    }

    public String getSortSign() {
        return sortSign;
    }

    public void setSortSign(String sortSign) {
        this.sortSign = sortSign;
    }

    public int getFirstResult() {
        return firstResult;
    }

    public void setFirstResult(int firstResult) {
        this.firstResult = firstResult;
    }

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
}
