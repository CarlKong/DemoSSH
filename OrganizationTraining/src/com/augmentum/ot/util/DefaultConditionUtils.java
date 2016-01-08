package com.augmentum.ot.util;

import com.augmentum.ot.dataObject.ActualCourseSearchCondition;
import com.augmentum.ot.dataObject.PlanSearchCondition;
import com.augmentum.ot.dataObject.constant.FlagConstants;
import com.augmentum.ot.dataObject.constant.IndexFieldConstants;

/**
 * Provide functions to return default search condition.
 * 
 * @version 0.1 2012-10-11
 */
public abstract class DefaultConditionUtils {

    /**
     * Get the default plan course search condition according to role and employeeName.
     * 
     * @param role
     * @param employeeName
     * @return  The default condition.
     */
    public static ActualCourseSearchCondition getDefaultPlanCourseSearchCondition(String role, String employeeName){
        ActualCourseSearchCondition planCourseSearchCondition = new ActualCourseSearchCondition();
        planCourseSearchCondition.setQueryString("");
        planCourseSearchCondition.setSearchFields(new String[]{"id"});
        planCourseSearchCondition.setSortField(IndexFieldConstants.ACTUAL_COURSE_END_TIME);
        planCourseSearchCondition.setReverse(false);
        planCourseSearchCondition.setPageSize(4);
        planCourseSearchCondition.setNowPage(1);
        planCourseSearchCondition.setDivideStauts(FlagConstants.ACTUAL_COURSE_SEARCH_STATUS_DIVIDED);
        planCourseSearchCondition.setRoleFlag(role);
        planCourseSearchCondition.setEmployeeName(employeeName);
        return planCourseSearchCondition;
    }
    
    public static PlanSearchCondition getDefaultPlanSearchCondition() {
    	PlanSearchCondition psc = new PlanSearchCondition();
    	psc.setQueryString("");
    	psc.setSearchFields(new String[]{"prefix_id"});
    	psc.setPageSize(4);
    	psc.setNowPage(1);
    	return psc;
    }
    
}
