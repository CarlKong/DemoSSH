package com.augmentum.ot.dataObject;

/**
 * 
 * @version 0.1, 07/13/2012
 */
public class ActualCourseSearchCondition extends SearchCondition {
    
    /** Employee name  */
    private String employeeName;
    
    private String roleFlag;
   
    /** 
     * Judge whether the search result is divided according status. 
     * 0: divide; 1: not divide. 
     * */
    private Integer divideByStauts;

    private String sortField;
    private String sortName; // sortName for selectList plugin
    
    private String sortSign; // sortSign for selectList plugin, "asc" "desc"
    
    private String pageNum; // pageNum for selectList plugin

    public Integer isDivideStauts() {
        return divideByStauts;
    }

    public void setDivideStauts(Integer divideByStauts) {
        this.divideByStauts = divideByStauts;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getRoleFlag() {
        return roleFlag;
    }

    public void setRoleFlag(String roleFlag) {
        this.roleFlag = roleFlag;
    }

    public boolean isForTrainee() {
        if (this.roleFlag != null && "trainee".equals(this.roleFlag)) {
            return true;
        }
        return false;
    }

    public String getSortName() {
        return sortName;
    }

    public void setSortName(String sortName) {
        this.sortName = sortName;
        this.sortField = sortName;
    }

    public String getSortSign() {
        return sortSign;
    }

    public void setSortSign(String sortSign) {
        this.sortSign = sortSign;
        if(sortSign.equals("asc")){
            this.setReverse(false);
        }else if(sortSign.equals("desc")){
            this.setReverse(true);
        }
    }

    public String getPageNum() {
        return pageNum;
    }

    public void setPageNum(String pageNum) {
        this.pageNum = pageNum;
        int nowPage = Integer.parseInt(pageNum);
        this.setNowPage(nowPage);
    }

    public Integer getDivideByStauts() {
        return divideByStauts;
    }

    public void setDivideByStauts(Integer divideByStauts) {
        this.divideByStauts = divideByStauts;
    }

    public String getSortField() {
        return sortField;
    }

    public void setSortField(String sortField) {
        this.sortField = sortField;
    }
}
