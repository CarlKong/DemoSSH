package com.augmentum.ot.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.augmentum.ot.dao.ActualCourseDao;
import com.augmentum.ot.dao.AssessmentDao;
import com.augmentum.ot.dao.EmployeeDao;
import com.augmentum.ot.dao.PlanDao;
import com.augmentum.ot.dao.PlanEmployeeMapDao;
import com.augmentum.ot.dataObject.Page;
import com.augmentum.ot.dataObject.ToDoItem;
import com.augmentum.ot.dataObject.constant.DateFormatConstants;
import com.augmentum.ot.dataObject.constant.ErrorCodeConstants;
import com.augmentum.ot.dataObject.constant.FlagConstants;
import com.augmentum.ot.dataObject.constant.ItemTypeConstants;
import com.augmentum.ot.dataObject.constant.JsonKeyConstants;
import com.augmentum.ot.dataObject.constant.LogConstants;
import com.augmentum.ot.exception.DataWarningException;
import com.augmentum.ot.exception.ServerErrorException;
import com.augmentum.ot.model.ActualCourse;
import com.augmentum.ot.model.Assessment;
import com.augmentum.ot.model.Employee;
import com.augmentum.ot.model.Plan;
import com.augmentum.ot.model.PlanEmployeeMap;
import com.augmentum.ot.service.ToDoListService;
import com.augmentum.ot.util.BeanFactory;
import com.augmentum.ot.util.DateHandlerUtils;

@Component("toDoListService")
public class ToDoListServiceImpl implements ToDoListService {
	private static Logger logger = Logger.getLogger(ToDoListServiceImpl.class);
	
	@Resource(name = "planDao")
	private PlanDao planDao;
	@Resource(name = "assessmentDao")
	private AssessmentDao assessmentDao;
	@Resource(name = "actualCourseDao")
	private ActualCourseDao actualCourseDao;
	@Resource(name = "employeeDao")
	private EmployeeDao employeeDao;

	@Override
	public Page<ToDoItem> findToDoItemsForMaster(Employee employee, Integer pageNo, Integer pageSize)
			throws DataWarningException, ServerErrorException {
		List<ToDoItem> toDoItemList = new ArrayList<ToDoItem>();
		List<Assessment> assessmentList = null;
		List<Plan> planList = null;
		Date currentTime = new Date();
		try {
			//Get all assessments source.
			assessmentList = assessmentDao.findAssessmentsByProperty(
					JsonKeyConstants.MASTER_ID, employee.getEmployeeId(), 
					FlagConstants.TRAINING_MASTER_TO_TRAINERS, 
					DateHandlerUtils.getDateBefore(currentTime, DateFormatConstants.DAYS));
			//Get all plan source
			planList = planDao.getPlanListNeedOperate(employee.getAugUserName(), new Date(), DateHandlerUtils.getDateBefore(new Date(), DateFormatConstants.DAYS));
		} catch (Exception e) {
			logger.error(LogConstants.exceptionMessage("Find To-Do Items For Master["
						+ employee.getAugUserName() + "]"), e);
			throw new ServerErrorException(ErrorCodeConstants.SERVER_ERROR);
		}
		List<Plan> needPublishPlanList = new ArrayList<Plan>();
		List<Plan> needCompletePlanList = new ArrayList<Plan>();
		List<Plan> needAssessPlanList = new ArrayList<Plan>();	
		for (Plan plan : planList) {
			// Get unpublished plan.
			if (plan.getPlanIsPublish().equals(FlagConstants.UN_PUBLISHED)) {
				needPublishPlanList.add(plan);
				if (plan.getPlanIsCompleted().equals(FlagConstants.UN_COMPLETED)) {
					needCompletePlanList.add(plan);
				}
			} else {
				if (plan.getPlanExecuteEndTime() != null) {
					if (plan.getPlanExecuteEndTime().after(currentTime)) { //not end
						if (plan.getPlanIsCompleted().equals(FlagConstants.UN_COMPLETED) && isPlanNeedComplete(plan)) {
							needCompletePlanList.add(plan);
						}
					} else {
						if (needMasterAssessTrainer(assessmentList, plan, employee.getAugUserName(), currentTime)) {
							needAssessPlanList.add(plan);
						}
					}
				} else {
					needCompletePlanList.add(plan);
				}
			}
		}
		for (Plan plan : needPublishPlanList) {
			makeToDoListItem(toDoItemList, plan.getPlanName(), plan.getPlanCreateDate(), 
					plan.getPlanId(), FlagConstants.ID_BLANK, ItemTypeConstants.PUBLISH_PLAN, plan.getPrefixIDValue(), currentTime);
		}
		
		for (Plan plan : needCompletePlanList) {
			makeToDoListItem(toDoItemList, plan.getPlanName(), plan.getPlanCreateDate(), 
					plan.getPlanId(), FlagConstants.ID_BLANK, ItemTypeConstants.UNCOMPLETED_PLAN, plan.getPrefixIDValue(), currentTime);
		}
		
		for (Plan plan : needAssessPlanList) {
			makeToDoListItem(toDoItemList, plan.getPlanName(), plan.getPlanExecuteEndTime(), 
					plan.getPlanId(), FlagConstants.ID_BLANK, ItemTypeConstants.MASTER_ASSESS_TRAINERS, plan.getPrefixIDValue(), currentTime);
		}
		orderToDoList(toDoItemList);
		Page<ToDoItem> toDoPage = new Page<ToDoItem>();
		toDoPage = toDoPage.listToPage(toDoItemList, pageNo, pageSize);
		return toDoPage;
	} 
	
	/**
	 * Judge if courses of this plan need complete.
	 * @param plan
	 * @return
	 */
	private boolean isPlanNeedComplete(Plan plan) {
		Date currentTime = new Date();
		// If plan is ended.
		if (plan.getPlanExecuteEndTime() != null && plan.getPlanExecuteEndTime().before(currentTime)
				&& plan.getPlanIsPublish().equals(FlagConstants.IS_PUBLISHED)){
			return false;
		}
		// If plan is not ended.
		for (ActualCourse actualCourse : plan.getActualCourses()) {
			if (null == actualCourse.getCourseStartTime()) {
				return true;
			} else {
				// info of this course is not completed and this course is not started.
				if (!(isCourseCompleted(actualCourse)) && actualCourse.getCourseStartTime().after(currentTime)
						&& plan.getPlanIsPublish().equals(FlagConstants.IS_PUBLISHED)) {
					return true;
				}
				if (!(isCourseCompleted(actualCourse)) && plan.getPlanIsPublish().equals(FlagConstants.UN_PUBLISHED)) {
					return true;
				}
			}
		}
		return false;
	}
	
	
	/**
	 * check if plan course info is completed.
	 * @param planCourse
	 * @return
	 */
	private boolean isCourseCompleted(ActualCourse actualCourse) {
		if (null == actualCourse.getCourseRoomNum() 
				|| "".equals(actualCourse.getCourseRoomNum() )
				|| null == actualCourse.getCourseStartTime()
				|| null == actualCourse.getCourseEndTime()
				|| null == actualCourse.getCourseTrainer()
				|| "".equals(actualCourse.getCourseTrainer())
		) {
			return false;
		}
		return true;
	}
	
	/**
	 * Check if need Master assess Trainers.
	 * @param assessmentList
	 * @param planId
	 * @return
	 */
	private boolean needMasterAssessTrainer(List<Assessment> assessmentList, Plan plan, String userName, Date currentTime){
		// If plan does not need assessed
		if (plan.getNeedAssessment() == FlagConstants.UN_NEED_ASSESSMENT) {
			return false;
		}
		// If plan has courses is not end
		if (DateHandlerUtils.getDaysBetweenDate(plan.getPlanExecuteEndTime(), currentTime) > DateFormatConstants.EXPIRED_DEFAULT_DAY
		) {
			return false;
		}
		//get all trainers of this plan
		Set<String> trainersSet = new HashSet<String>();
		for (ActualCourse actualCourse : plan.getActualCourses()) {
			if (!(actualCourse.getCourseTrainer().equals(userName))) {
				trainersSet.add(actualCourse.getCourseTrainer());
			}
		}
		// If the master is the only trainer of this plan.
		if (trainersSet.isEmpty()) {
			return false;
		}
		// If the assessmentList is null or empty.
		if (assessmentList == null || assessmentList.isEmpty()) {
			return true;
		}
		// If one or more trainer of this plan has been assessed
		List<Assessment> assessmentListOfPlan = new ArrayList<Assessment>();
		for (Assessment assessment : assessmentList) {
			if (assessment.getPlanId().equals(plan.getPlanId())) {
				assessmentListOfPlan.add(assessment);
			}
		}
		if (assessmentListOfPlan.isEmpty()) {
			return true;
		}
		// If all trainers of this plan have been assessed.
		for (Assessment assessment : assessmentListOfPlan) {
			if (assessment.getHasBeenAssessed() == FlagConstants.ASSESSMENT_UNASSESSED) {
				return true;
			}
		}
		return false;
		
	}
	
	/**
	 * 
	 * @param toDoItemList
	 * @param planList
	 * @param itemType
	 */
	private void makeToDoListItem (List<ToDoItem> toDoItemList, String itemTitle, Date endTime,
			Integer planId, Integer actualCourseId, String itemType, String prefixId, Date currentTime) {
		ToDoItem toDoItem = new ToDoItem();
		toDoItem.setItemType(itemType);
		toDoItem.setItemTitle(itemTitle);
		toDoItem.setIsignored(FlagConstants.UN_IGNORE);
		toDoItem.setPlanCourseId(actualCourseId);
		toDoItem.setPlanId(planId);
		toDoItem.setDifferentDays(DateHandlerUtils.getDaysBetweenDate(endTime, currentTime));
		toDoItem.setDurationTime(DateHandlerUtils.makeDifferenceBetween2Date(endTime, currentTime));
		toDoItem.setPrefixId(prefixId);
		toDoItemList.add(toDoItem);
	}

	@Override
	public Page<ToDoItem> findToDoItemsForTrainer(Employee employee, Integer pageNo, Integer pageSize)
			throws DataWarningException, ServerErrorException {
		List<ToDoItem> toDoItemList = new ArrayList<ToDoItem>();
		List<ActualCourse> actualCourseList = null;
		List<Plan>planList = null;
		List<Assessment> assessmentToTraineesList = null;
		List<Assessment> assessmentToPlanList = null;
		Date currentTime = new Date();
		Date beforExpiredDayTime = DateHandlerUtils.getDateBefore(currentTime, DateFormatConstants.DAYS);
		try {
			//Get source of actual course list.
			actualCourseList = actualCourseDao.getActualCourseListOperateByTrainer(employee.getAugUserName(), currentTime, beforExpiredDayTime);
			planList = planDao.getPlanListOperateByTrainer(employee.getAugUserName(), currentTime, beforExpiredDayTime);
			assessmentToTraineesList = assessmentDao.findAssessmentsByProperty(
					JsonKeyConstants.TRAINER_ID, employee.getEmployeeId(), FlagConstants.TRAINER_TO_TRAINEES,
					beforExpiredDayTime);
			assessmentToPlanList = assessmentDao.findAssessmentsByProperty(
					JsonKeyConstants.TRAINER_ID, employee.getEmployeeId(), FlagConstants.TRAINER_TO_PLAN,
					beforExpiredDayTime);
		} catch (Exception e) {
			logger.error(LogConstants.exceptionMessage("Find To-Do Items For Trainer["
					+ employee.getAugUserName() + "]"), e);
			throw new ServerErrorException(ErrorCodeConstants.SERVER_ERROR);
		}
		List<ActualCourse> needAssessCourseList = new ArrayList<ActualCourse>();
		List<Plan> needAssessPlanList = new ArrayList<Plan>();
		
		if (null != actualCourseList && !actualCourseList.isEmpty()) {
			for (ActualCourse actualCourse : actualCourseList) {
				// if the plan course has ended more than 7 days
				if ( DateHandlerUtils.getDaysBetweenDate(actualCourse.getCourseEndTime(), currentTime) > DateFormatConstants.EXPIRED_DEFAULT_DAY) {
					continue;
				}
				// if all trainees of this plan course assessed.
				if(needTrainerAssessTrainee(assessmentToTraineesList, actualCourse)){
					needAssessCourseList.add(actualCourse);
				}
			}
			//make to do list to trainer for actual course 
			for (ActualCourse actualCourse : needAssessCourseList) {
				makeToDoListItem(toDoItemList, actualCourse.getCourseName(), actualCourse.getCourseEndTime(), 
						actualCourse.getPlan().getPlanId(), actualCourse.getActualCourseId(), ItemTypeConstants.ASSESS_TRAINEES, actualCourse.getPrefixIdValue(), currentTime);
			}
		}
		
		if (null != planList && !planList.isEmpty()) {
			for (Plan plan : planList) {
				List<String> trainerNameList = Arrays.asList(plan.getTrainers().split(FlagConstants.SPLIT_COMMA));
				//If current user is not a trainer of this plan, such as current user is "BC Wang", but the plan also include the trainer is "ABC Wang".
				if (!(trainerNameList.contains(employee.getAugUserName()))) {
					continue;
				}
				//If current user is master of this plan
				if (plan.getPlanCreator().equals(employee.getAugUserName())) {
					continue;
				}
				if (DateHandlerUtils.getDaysBetweenDate(plan.getPlanExecuteEndTime(), currentTime) <= DateFormatConstants.EXPIRED_DEFAULT_DAY &&
						isPlanNeedAssessByUser(assessmentToPlanList, plan.getPlanId())) {
					needAssessPlanList.add(plan);
				}
			}
			//make to do list to trainer for plan 
			for (Plan plan : needAssessPlanList) {
				makeToDoListItem(toDoItemList, plan.getPlanName(), plan.getPlanExecuteEndTime(), 
						plan.getPlanId(), FlagConstants.ID_BLANK, ItemTypeConstants.TRAINER_ASSESS_PLAN, plan.getPrefixIDValue(), currentTime);
			}
		}
		orderToDoList(toDoItemList);
		Page<ToDoItem> toDoPage = new Page<ToDoItem>();
		toDoPage = toDoPage.listToPage(toDoItemList, pageNo, pageSize);
		return toDoPage;
	}
	
	private boolean needTrainerAssessTrainee(List<Assessment> assessmentList, ActualCourse actualCourse) {
		List<Assessment> assessmentOfPlanCourse = new ArrayList<Assessment>();
		for (Assessment assessment : assessmentList) {
			if (assessment.getPlanCourseId().equals(actualCourse.getActualCourseId())) {
				assessmentOfPlanCourse.add(assessment);
			}
		}
		if (assessmentOfPlanCourse.isEmpty()) {
			return true;
		}
		for (Assessment assessment : assessmentOfPlanCourse) {
			if (assessment.getHasBeenAssessed() == FlagConstants.ASSESSMENT_UNASSESSED) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * check if this plan has been assessed 
	 * @param assessmentList
	 * @param planId
	 * @return
	 */
	private boolean isPlanNeedAssessByUser (List<Assessment> assessmentList, Integer planId) {
		for (Assessment assessment : assessmentList) {
			if (assessment.getPlanId().equals(planId)) {
				return false;
			}
		} 
		return true;
	}
	
	/**
	 * check if this plan course has been assessed
	 * @param assessmentList
	 * @param planCourseId
	 * @return
	 */
	private boolean isCourseNeedAssessByUser (List<Assessment> assessmentList, Integer planCourseId) {
		for (Assessment assessment : assessmentList) {
			if (assessment.getPlanCourseId().equals(planCourseId)) {
				return false;
			}
		}
		return true;
	}

	@Override
	public Page<ToDoItem> findToDoItemsForTrainee(Employee employee, Integer pageNo, Integer pageSize)
			throws DataWarningException, ServerErrorException {
		List<Assessment> assessmentToCourseList = null;
		List<Assessment> assessmentToPlanList = null;
		List<PlanEmployeeMap> pemList = null;
		List<ActualCourse> actualCourseList = null;
		Date currentTime = new Date();
		Date beforExpiredDayTime = DateHandlerUtils.getDateBefore(currentTime, DateFormatConstants.DAYS);
		try {
			assessmentToCourseList = assessmentDao.findAssessmentsByProperty(
					JsonKeyConstants.TRAINEE_ID, employee.getEmployeeId(), FlagConstants.TRAINEE_TO_COURSE,
					beforExpiredDayTime);
			assessmentToPlanList = assessmentDao.findAssessmentsByProperty(
					JsonKeyConstants.TRAINEE_ID, employee.getEmployeeId(), FlagConstants.TRAINEE_TO_PLAN,
					beforExpiredDayTime);
			PlanEmployeeMapDao pemDao = BeanFactory.getPlanEmployeeMapDao();
			pemList = pemDao.getPEMListByNeedAssessTrainee(employee.getAugUserName(), currentTime, beforExpiredDayTime);
			actualCourseList = actualCourseDao.getActualCourseListOperateByTrainee(employee.getAugUserName(), currentTime, beforExpiredDayTime);
		} catch (Exception e) {
			logger.error(LogConstants.exceptionMessage("Find To-Do Items For Trainee["
					+ employee.getAugUserName() + "]"), e);
			throw new ServerErrorException(ErrorCodeConstants.SERVER_ERROR);
		}
		List<ActualCourse> needAssessCourseList = new ArrayList<ActualCourse>();
		List<Plan> needAssessPlanList = new ArrayList<Plan>();
		
		for (ActualCourse actualCourse : actualCourseList) {
			//End time of course is longer than 7 days.
			if (DateHandlerUtils.getDaysBetweenDate(actualCourse.getCourseEndTime(), currentTime) > DateFormatConstants.EXPIRED_DEFAULT_DAY) {
				continue;
			}
			//Current trainee is both trainer and trainee of this course.
			if (employee.getAugUserName().equals(actualCourse.getCourseTrainer())) {
				continue;
			}
			if (isCourseNeedAssessByUser(assessmentToCourseList, actualCourse.getActualCourseId())) {
				needAssessCourseList.add(actualCourse);
			}
		}
		for (PlanEmployeeMap planEmployeeMap : pemList) {
			if (DateHandlerUtils.getDaysBetweenDate(planEmployeeMap.getPlan().getPlanExecuteEndTime(), currentTime) > DateFormatConstants.EXPIRED_DEFAULT_DAY) {
				continue;
			}
			//Current trainee is both master and trainee of this plan.
			if (planEmployeeMap.getPlan().getPlanCreator().equals(employee.getAugUserName())) {
				continue;
			}
			if (isPlanNeedAssessByUser(assessmentToPlanList, planEmployeeMap.getPlan().getPlanId())) {
				needAssessPlanList.add(planEmployeeMap.getPlan());
			}
		}
		List<ToDoItem> toDoItemList = new ArrayList<ToDoItem>();
		for (ActualCourse actualCourse : needAssessCourseList) {
			makeToDoListItem(toDoItemList, actualCourse.getCourseName(), 
					actualCourse.getCourseEndTime(), actualCourse.getPlan().getPlanId(), 
					actualCourse.getActualCourseId(), ItemTypeConstants.ASSESS_COURSE, actualCourse.getPrefixIdValue(), currentTime);
		}
		for (Plan plan : needAssessPlanList) {
			makeToDoListItem(toDoItemList, plan.getPlanName(), plan.getPlanExecuteEndTime(), 
					plan.getPlanId(), FlagConstants.ID_BLANK, ItemTypeConstants.TRAINEE_ASSESS_PLAN, plan.getPrefixIDValue(), currentTime);
		}
		orderToDoList(toDoItemList);
		Page<ToDoItem> toDoPage = new Page<ToDoItem>();
		toDoPage = toDoPage.listToPage(toDoItemList, pageNo, pageSize);
		return toDoPage;
	}
	
	/**
	 * sort the list by the different days, the days more small more front
	 * @param toDoList
	 */
	private void orderToDoList (List<ToDoItem> toDoList) {
		Collections.sort(toDoList, new Comparator<ToDoItem>() {
			@Override
			public int compare(ToDoItem o1, ToDoItem o2) {
				if (o1.getDifferentDays().compareTo(o2.getDifferentDays()) < 0) {
					return -1;
				} else {
					return 1;
				}
			}
		});
	}
	
	public PlanDao getPlanDao() {
		return planDao;
	}

	public void setPlanDao(PlanDao planDao) {
		this.planDao = planDao;
	}

	public AssessmentDao getAssessmentDao() {
		return assessmentDao;
	}

	public void setAssessmentDao(AssessmentDao assessmentDao) {
		this.assessmentDao = assessmentDao;
	}

	public EmployeeDao getEmployeeDao() {
		return employeeDao;
	}

	public void setEmployeeDao(EmployeeDao employeeDao) {
		this.employeeDao = employeeDao;
	}


	public ActualCourseDao getActualCourseDao() {
		return actualCourseDao;
	}


	public void setActualCourseDao(ActualCourseDao actualCourseDao) {
		this.actualCourseDao = actualCourseDao;
	}

}
