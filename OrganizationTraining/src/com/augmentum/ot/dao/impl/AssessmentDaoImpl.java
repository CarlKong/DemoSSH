package com.augmentum.ot.dao.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Component;

import com.augmentum.ot.dao.AssessmentDao;
import com.augmentum.ot.dataObject.Page;
import com.augmentum.ot.dataObject.constant.FlagConstants;
import com.augmentum.ot.model.Assessment;
import com.augmentum.ot.model.AssessmentItem;
import com.augmentum.ot.model.AssessmentScore;

@Component("assessmentDao")
public class AssessmentDaoImpl extends BaseDaoImpl<Assessment> implements
		AssessmentDao {

	@Override
	public Class<Assessment> getEntityClass() {
		return Assessment.class;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Assessment> getAssessmentMasterToTrainer(Integer planId) {
		Query assessmentQuery = getSession().createQuery("from " + getEntityClass().getSimpleName()
				+ " where planId=? and typeFlag=? and assessIsDeleted=?");
		assessmentQuery.setInteger(0, planId);
		assessmentQuery.setInteger(1, FlagConstants.TRAINING_MASTER_TO_TRAINERS);
		assessmentQuery.setInteger(2, FlagConstants.UN_DELETED);
		return assessmentQuery.list();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Assessment> getAssessmentTraineeToPlan(Integer planId) {
		Query assessmentQuery = getSession().createQuery("from " + getEntityClass().getSimpleName()
				+ " where planId=? and typeFlag=? and assessIsDeleted=?");
		assessmentQuery.setInteger(0, planId);
		assessmentQuery.setInteger(1, FlagConstants.TRAINEE_TO_PLAN);
		assessmentQuery.setInteger(2, FlagConstants.UN_DELETED);
		return assessmentQuery.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Assessment> getAssessmentTrainerToPlan(Integer planId) {
		Query assessmentQuery = getSession().createQuery("from " + getEntityClass().getSimpleName()
				+ " where planId=? and typeFlag=? and assessIsDeleted=?");
		assessmentQuery.setInteger(0, planId);
		assessmentQuery.setInteger(1, FlagConstants.TRAINER_TO_PLAN);
		assessmentQuery.setInteger(2, FlagConstants.UN_DELETED);
		return assessmentQuery.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Assessment> getAllAssessmentsByPlanId(Integer planId) {
		Query assessmentQuery = getSession().createQuery("from " + getEntityClass().getSimpleName()
				+ " where planId=? and assessIsDeleted=?");
		assessmentQuery.setInteger(0, planId);
		assessmentQuery.setInteger(1, FlagConstants.UN_DELETED);
		return assessmentQuery.list();
	}

    @SuppressWarnings("unchecked")
    @Override
    public List<Assessment> findAssessmentsByProperty(String property,
            Object value, int assessmentType,int pageNow,int pageSize) {
        Query assessmentQuery = getSession().createQuery("from " + getEntityClass().getSimpleName()
                + " where "+property+"=? and typeFlag=? and assessIsDeleted=?");
        assessmentQuery.setParameter(0, value);
        assessmentQuery.setInteger(1, assessmentType);
        assessmentQuery.setInteger(2, FlagConstants.UN_DELETED);
        assessmentQuery.setMaxResults(pageSize);
        assessmentQuery.setFirstResult(pageSize*(pageNow-1));
        return assessmentQuery.list();
    }

    @Override
    public int findAssessmentsCountByProperty(String property, Object value,
            int assessmentType) {
        int count = 0;
        Query assessmentQuery = getSession().createQuery("select count(*) from " + getEntityClass().getSimpleName()
                + " where "+property+"=? and typeFlag=? and assessIsDeleted=?");
        assessmentQuery.setParameter(0, value);
        assessmentQuery.setInteger(1, assessmentType);
        assessmentQuery.setInteger(2, FlagConstants.UN_DELETED);
        count = ((Long)assessmentQuery.uniqueResult()).intValue();
        return count;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Assessment> getAssessmentTrainerToPlanCourse(
            Integer planCourseId) {
        Query assessmentQuery = getSession().createQuery("from " + getEntityClass().getSimpleName()
                + " where planCourseId=? and typeFlag=? and assessIsDeleted=?");
        assessmentQuery.setInteger(0, planCourseId);
        assessmentQuery.setInteger(1, FlagConstants.TRAINER_TO_COURSE);
        assessmentQuery.setInteger(2, FlagConstants.UN_DELETED);
        return assessmentQuery.list();
    }
    
    @SuppressWarnings("unchecked")
	@Override
	public List<Assessment> getAssessmentTraineeToPlanCourse(
			Integer planCourseId) {
    	Query assessmentQuery = getSession().createQuery("from " + getEntityClass().getSimpleName()
                + " where planCourseId=? and typeFlag=? and assessIsDeleted=?");
        assessmentQuery.setInteger(0, planCourseId);
        assessmentQuery.setInteger(1, FlagConstants.TRAINEE_TO_COURSE);
        assessmentQuery.setInteger(2, FlagConstants.UN_DELETED);
        return assessmentQuery.list();
	}
	
    
    @SuppressWarnings("unchecked")
    @Override
    public List<Assessment> findAssessmentsByProperty(String property,
            Object value, int assessmentType, Date startDate) {
                Query assessmentQuery = getSession().createQuery("from " + getEntityClass().getSimpleName()
                + " where "+ property +"=? and typeFlag=? and assessIsDeleted=? and create_datetime>=?");
        assessmentQuery.setParameter(0, value);
        assessmentQuery.setInteger(1, assessmentType);
        assessmentQuery.setInteger(2, FlagConstants.UN_DELETED);
        assessmentQuery.setDate(3, startDate);
        return assessmentQuery.list();
    }

    @Override
    public boolean isPlanCourseAssessed(Integer planCourseId,
            Integer assessmentType) {
        int count = 0;
        Query assessmentQuery = getSession().createQuery("select count(*) from " + getEntityClass().getSimpleName()
                + " where planCourseId=? and typeFlag=? and hasBeenAssessed=? and assessIsDeleted=?");
        assessmentQuery.setInteger(0, planCourseId);
        assessmentQuery.setInteger(1, assessmentType);
        assessmentQuery.setInteger(2, FlagConstants.ASSESSMENT_HAS_BEEN_ASSESSED);
        assessmentQuery.setInteger(3, FlagConstants.UN_DELETED);
        count = ((Long)assessmentQuery.uniqueResult()).intValue();
        if(count == 0){
            return false;
        } else {
            return true;
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Assessment> getAssessmentForTraineeByTrainer(Integer trainerId,
            Integer planCourseId) {
        Query assessmentQuery = getSession().createQuery("from " + getEntityClass().getSimpleName()
                + " where trainerId=? and planCourseId=? and typeFlag=? and assessIsDeleted=?");
        assessmentQuery.setInteger(0, trainerId);
        assessmentQuery.setInteger(1, planCourseId);
        assessmentQuery.setInteger(2, FlagConstants.TRAINER_TO_TRAINEES);
        assessmentQuery.setInteger(3, FlagConstants.UN_DELETED);
        List<Assessment> assessments = assessmentQuery.list();
        return assessments;
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public List<Assessment> getAssessmentByFields(
            Map<String, Integer> assessmentFieldMap, List<Integer> typeFlagList) {
        List<Assessment> assessmentList = null;
        /*
         * 1. Deal with the criteria.
         * 2. Search the data.
         */
        
        // Deal with the criteria.
        Criteria criteria = dealWithCriteriaByFields(assessmentFieldMap, typeFlagList);
        // Search the data.
        assessmentList = criteria.list();
        
        return assessmentList;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Page<Assessment> getAssessmentByFieldsAndPaging(
            Map<String, Integer> assessmentFieldMap,
            List<Integer> typeFlagList, Integer nowPage, Integer pageSize) {
        Page<Assessment> assessmentPage = new Page<Assessment>();
        /*
         * 1. Add the nowPage and pageSize to Page instance in order to firstResult.
         * 2. Deal with the criteria.
         * 3. Get the rowCount by criteria.
         * 4. Add the pageSize and firseResult to criteria.
         * 5. Search the data.
         * 6. Add the rowCount and data list to page.
         */
        
        // 1. Add the nowPage and pageSize to Page instance in order to firstResult.
        assessmentPage.setNowPager(nowPage);
        assessmentPage.setPageSize(pageSize);
        
        // 2. Deal with the criteria.
        Criteria criteria = dealWithCriteriaByFields(assessmentFieldMap, typeFlagList);
        
        // 3. Get the rowCount by criteria.
        criteria.setProjection(Projections.rowCount());
        Integer rowCount = Integer.parseInt(criteria.uniqueResult().toString());
        criteria.setProjection(null);
        // 4. Add the pageSize and firseResult to criteria.
        criteria.setMaxResults(assessmentPage.getPageSize());
        criteria.setFirstResult(assessmentPage.getFirstResult());
        
        // 5. Search the data.
        List<Assessment> assessmentList = criteria.list();
        
        //6. Add the rowCount and data list to page.
        assessmentPage.setList(assessmentList);
        assessmentPage.setTotalRecords(rowCount);
        
        return assessmentPage;
    }
    
    /**
     * Deal with the criteria and add some filter.
     * 
     * @param assessmentFieldMap  The assessment field map.
     * @param typeFlagList  The type flag list.
     * @return  The criteria.
     */
    @SuppressWarnings("static-access")
	private Criteria dealWithCriteriaByFields(Map<String, Integer> assessmentFieldMap, List<Integer> typeFlagList) {
        Criteria criteria = this.getSession().createCriteria(getEntityClass());
        /*
         * 1. Deal with assessmentFieldMap.
         * 2. Deal with typeFlagList.
         * 3. Add the un_deleted.
         */
        
        // Deal with assessmentFieldMap.
        if (assessmentFieldMap != null && !assessmentFieldMap.isEmpty()) {
            // Add the field-value in criteria.
            criteria.add(Restrictions.allEq(assessmentFieldMap));
        }
        // Deal with typeFlagList.
        if (typeFlagList != null && !typeFlagList.isEmpty()) {
            criteria.add(Restrictions.in(FlagConstants.TYPE_FLAG, typeFlagList));
        }
        // Add the un_deleted.
        criteria.add(Restrictions.eq(FlagConstants.ASSESS_IS_DELETED, FlagConstants.UN_DELETED));
        criteria.setResultTransformer(criteria.DISTINCT_ROOT_ENTITY);   
        
        return criteria;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<AssessmentItem> countAssessment(
            Map<String, Integer> assessmentFieldMap, Integer typeFlag) {
        Query assessmentItemQuery = getSession().createQuery("from AssessmentItem where assessItemType.assessItemTypeId=?");
        assessmentItemQuery.setInteger(0, typeFlag);
        List<AssessmentItem> assessmentItems = assessmentItemQuery.list();
        
        //initialize the itemValues to save the item names and their scores.
        Map<String,Double> itemValues = new HashMap<String,Double>();
        for(int i = 0;i < assessmentItems.size(); i++){
            itemValues.put(assessmentItems.get(i).getAssessItemName(), 0.0);
        }
        //get the assessment
        List<Integer> typeFlagList = new ArrayList<Integer>();
        typeFlagList.add(typeFlag);
        Criteria criteria = dealWithCriteriaByFields(assessmentFieldMap, typeFlagList);
        List<Assessment> assessments = criteria.list();
        
        if (assessments.isEmpty()) {
        	return null;
        }
        
        //set the item names and their scores
        if(typeFlag != FlagConstants.TRAINER_TO_TRAINEES){
            int scoreAmount = 0;
            for(int i = 0; i < assessments.size(); i++){
                if(assessments.get(i).getIsIgnore() == FlagConstants.UN_IGNORE){
                    scoreAmount++;
                    List<AssessmentScore> assessmentScores = assessments.get(i).getAssessScoreList();
                    for(int j = 0; j < assessmentScores.size(); j++){
                        String itemName = assessmentScores.get(j).getAssessmentItem().getAssessItemName();
                        double score = assessmentScores.get(j).getAssessScore() + itemValues.get(itemName); 
                        itemValues.put(itemName, score);
                    }
                }
            }
            //calculate the average
            for(int i = 0; i < assessmentItems.size(); i++){
                String itemName = assessmentItems.get(i).getAssessItemName();
                double score = itemValues.get(itemName);
                assessmentItems.get(i).setAvgScore(score/scoreAmount);
            }
        } else {
            //handle the trainer to trainee kind of assessment.
            int scoreAmount = 0;
            int hasNoHomeWorkAmount = 0;
            for(int i = 0; i < assessments.size(); i++){
                if(assessments.get(i).getHasBeenAssessed() == FlagConstants.ASSESSMENT_UNASSESSED || assessments.get(i).getIsIgnore() == FlagConstants.IS_IGNORE){
                    continue;
                }
                scoreAmount++;
                List<AssessmentScore> assessmentScores = assessments.get(i).getAssessScoreList();
                for(int j = 0; j < assessmentScores.size(); j++){
                    String itemName = assessmentScores.get(j).getAssessmentItem().getAssessItemName();
                    double score = assessmentScores.get(j).getAssessScore(); 
                    if(itemName.equals("_assessment_course_homework") && score == FlagConstants.ASSESSMENT_NO_HOMEWORK_FLAG){
                        hasNoHomeWorkAmount++;
                    } else {
                        score = score + itemValues.get(itemName); 
                        itemValues.put(itemName, score);
                    }
                }
            }
          //calculate the average
            for(int i = 0; i < assessmentItems.size(); i++){
                String itemName = assessmentItems.get(i).getAssessItemName();
                double score = itemValues.get(itemName);
                if(itemName.equals("HomeWork")){
                    assessmentItems.get(i).setAvgScore(score/(scoreAmount-hasNoHomeWorkAmount));
                } else {
                    assessmentItems.get(i).setAvgScore(score/scoreAmount);
                }
            }
        }
        return assessmentItems;
    }

	@Override
	public int findTraineeCountByStatusAndCourse(int planCourseId,
			Double attendStatus) {
		int count = 0;
        Query assessmentQuery = getSession().createQuery("select count(*) from AssessmentScore where " +
        		"assessment.planCourseId=? and assessmentItem.assessItemId=? and assessment.typeFlag=? and assessment.assessIsDeleted=? and assessScore=?");
        assessmentQuery.setInteger(0, planCourseId);
        assessmentQuery.setInteger(1, 17);
        assessmentQuery.setInteger(2, FlagConstants.TRAINER_TO_TRAINEES);
        assessmentQuery.setInteger(3, FlagConstants.UN_DELETED);
        assessmentQuery.setDouble(4, attendStatus);
        count = ((Long)assessmentQuery.uniqueResult()).intValue();
		return count;
	}

}
