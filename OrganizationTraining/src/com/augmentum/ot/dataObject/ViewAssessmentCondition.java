package com.augmentum.ot.dataObject;

import java.io.Serializable;

/**
 * Packing the view assessment condition.
 * 
 * @version 0.1  2012-10-24
 */
public class ViewAssessmentCondition implements Serializable {

    private static final long serialVersionUID = 338206644569295035L;
    
    private Integer nowPage;
    private Integer pageSize;
    private Integer firstResult;
    
    public Integer getNowPage() {
        return nowPage;
    }

    public void setNowPage(Integer nowPage) {
        this.nowPage = nowPage;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getFirstResult() {
        return firstResult;
    }

    public void setFirstResult(Integer firstResult) {
        this.firstResult = firstResult;
    }
    
}
