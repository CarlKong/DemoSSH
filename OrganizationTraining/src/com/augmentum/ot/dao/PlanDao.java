package com.augmentum.ot.dao;


import java.util.Date;
import java.util.List;

import com.augmentum.ot.model.Plan;


/**
 * A interface about plan,provide additional methods
 * 
 * @version 0.1, 07/13/2012
 */
public interface PlanDao extends BaseDao<Plan> {
	
	List<Plan> findPlanByCreator(String creatorName);

	/**
	 * get all new public plans (without specific condition)
	 * @return
	 */
    List<Plan> getNewPlanList();

    /**
     * get all new public plans number
     * @return
     */
    int getNewPlanNumber();
    
    /**
	 * All conditions:
	 * 1. Creator
	 * 2. Not canceled and not deleted.
	 * 3. Published : 
	 *    1) Not published: All.
	 *    2) Is published: Plan end time is null:
	 *       a. Is null : All.
	 *       b. Not null : Finished: 
	 *          a). Not finished: not complete.
	 *          b). Is finished: in 8 days.
	 */
    List<Plan> getPlanListNeedOperate(String creatorName, Date currentDate, Date validateDate);
    
    List<Plan> getPlanListOperateByTrainer(String trainerName, Date currentDate, Date validateDate);
}
