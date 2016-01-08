package com.augmentum.ot.util;

import java.util.Date;
import java.util.List;
import java.util.Set;

import com.augmentum.ot.dataObject.constant.FlagConstants;
import com.augmentum.ot.model.ActualCourse;
import com.augmentum.ot.model.Plan;
import com.augmentum.ot.model.PlanEmployeeMap;


public class ValidationUtil {
	public static boolean isNotNull (String str) {
	    String string = str.trim();
		if (null == string || "".equals(string)) {
			return false;
		}
		return true;
	}
	
	/**
	 * Return the flag of whether the plan is published.
	 * 
	 * @param plan  The instance.
	 * @return  The flag.
	 */
	public static Integer isPlanPublished(Plan plan) {
	    Integer flag = FlagConstants.PLAN_VALIDATEION_RESULT_FALSE;
	    if (plan == null) {
	        flag = FlagConstants.PLAN_IS_NULL;
	    } else if (plan.getPlanIsPublish().equals(FlagConstants.IS_PUBLISHED) ){
	        flag = FlagConstants.PLAN_VALIDATEION_RESULT_TRUE;
	    }
	    return flag;
	}
	
	/**
     * Return the flag of whether the plan is canceled.
     * 
     * @param plan  The instance.
     * @return  The flag.
     */
	public static Integer isPlanCanceled(Plan plan) {
	    Integer flag = FlagConstants.PLAN_VALIDATEION_RESULT_FALSE;
	    if (plan == null || plan.getPlanIsCanceled() == null) {
            flag = FlagConstants.PLAN_IS_NULL;
        } else if (plan.getPlanIsCanceled().equals(FlagConstants.IS_CANCELED) ){
            flag = FlagConstants.PLAN_VALIDATEION_RESULT_TRUE;
        }
        return flag;
	}
	
	/**
     * Return the flag of whether the plan is deleted.
     * 
     * @param plan  The instance.
     * @return  The flag.
     */
    public static Integer isPlanDeleted(Plan plan) {
        Integer flag = FlagConstants.PLAN_VALIDATEION_RESULT_FALSE;
        if (plan == null || plan.getPlanIsDeleted() == null) {
            flag = FlagConstants.PLAN_IS_NULL;
        } else if (plan.getPlanIsDeleted().equals(FlagConstants.IS_DELETED) ){
            flag = FlagConstants.PLAN_VALIDATEION_RESULT_TRUE;
        }
        return flag;
    }
    
    /**
     * According to the end time about the plan course and plan session, judge 
     * whether the plan is completed.
     * 
     * @param plan  The instance.
     * @return  The flag.
     */
    public static Integer isPlanCompleted(Plan plan) {
        Integer flag = FlagConstants.PLAN_VALIDATEION_RESULT_TRUE;
        if (plan == null) {
            flag = FlagConstants.PLAN_IS_NULL;
        } else if (plan != null) {
            // If flag is true, iterate the plan course.
            if (flag.equals(FlagConstants.PLAN_VALIDATEION_RESULT_TRUE)) {
                List<ActualCourse> actualCourseList = plan.getActualCourses();
                Date nowTime = new Date();
                Date actualCourseEndTime = null;
                if (actualCourseList != null && !actualCourseList.isEmpty()) {
                    for (ActualCourse actualCourse: actualCourseList) {
                        actualCourseEndTime = actualCourse.getCourseEndTime();
                        /*
                         *  If the planCourseEndTime is behind now time or is null,
                         *  this plan is not completed.
                         */
                        if (actualCourseEndTime == null || nowTime.compareTo(actualCourseEndTime) < 0) {
                            flag = FlagConstants.PLAN_VALIDATEION_RESULT_FALSE;
                            break;
                        }
                    }
                }
            }
        }
        return flag;
    }
    
    /**
     * Judge whether the plan has no plan course.
     * 
     * @param plan
     * @return
     */
    public static Integer isPlanNoCourseAdded(Plan plan) {
        Integer flag = FlagConstants.PLAN_VALIDATEION_RESULT_FALSE;
        if (plan == null) {
            flag = FlagConstants.PLAN_IS_NULL;
        } else {
            if (plan.getActualCourses() == null || plan.getActualCourses().isEmpty()) {
                flag = FlagConstants.PLAN_VALIDATEION_RESULT_TRUE;
            }
        }
        return flag;
    }
    
    /**
     * 
     *  Judge whether the plan has trainees
     *  1. invited plan -- invited trainees
     *  2. public plan -- specific trainees
     * 
     * @param plan
     * @return
     */
    public static Integer isPlanNoTraineesAdded(Plan plan) {
    	Integer flag = FlagConstants.PLAN_VALIDATEION_RESULT_FALSE;
        if (plan == null) {
            flag = FlagConstants.PLAN_IS_NULL;
        } else {
        	Set<PlanEmployeeMap> pemList = plan.getPlanEmployeeMapList();
            if(plan.getPlanType().getPlanTypeName().equals(FlagConstants.INVITED_PLAN)) {
            	flag = getNoTraineesFlag(pemList, FlagConstants.ATTEND_TYPE_INVITED);
            } else {
            	if(plan.getIsAllEmployee().equals(FlagConstants.UN_ALL_EMPLOYEES)) {
            		flag = getNoTraineesFlag(pemList, FlagConstants.ATTEND_TYPE_SPECIFIC);
            	}
            }
        }
        return flag;
    }
    private static Integer getNoTraineesFlag(Set<PlanEmployeeMap> pemList, int attendType) {
    	Integer flag = FlagConstants.PLAN_VALIDATEION_RESULT_TRUE;
    	if(pemList == null | pemList.size() == 0) {
    		flag = FlagConstants.PLAN_VALIDATEION_RESULT_TRUE;
    	} else {
    		for(PlanEmployeeMap pem:pemList) {
    			if (pem.getPlanEmployeeIsDeleted() == FlagConstants.UN_DELETED && pem.getPlanTraineeAttendType() == attendType) {
            		flag = FlagConstants.PLAN_VALIDATEION_RESULT_FALSE;
            		break;
            	}
        	}
    	}
    	return flag;
    }
    
    /**
     * Judge whether has one or more plan courses started.
     * 
     * @param plan  The instance.
     * @return  The flag.
     */
    public static Integer isPlanOneOrMoreCourseStarted(Plan plan) {
        Integer flag = FlagConstants.PLAN_VALIDATEION_RESULT_FALSE;
        if (plan == null) {
            flag = FlagConstants.PLAN_IS_NULL;
        } else {
            List<ActualCourse> actualCourseList = plan.getActualCourses();
            if (actualCourseList != null && !actualCourseList.isEmpty()) {
                Date nowTime = new Date();
                Date actualCourseStartTime = null;
                for (ActualCourse actualCourse: actualCourseList) {
                    if (actualCourse != null) {
                        /*
                         * If planCourseStartTime is null or is before nowTime,
                         * the plan has one or more plan courses started.
                         */
                        actualCourseStartTime = actualCourse.getCourseStartTime();
                        if (actualCourseStartTime != null && actualCourseStartTime.compareTo(nowTime) < 0) {
                            flag = FlagConstants.PLAN_VALIDATEION_RESULT_TRUE;
                        }
                    }
                }
            }
        }
        return flag;
    }
}
