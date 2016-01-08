package com.augmentum.ot.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.springframework.stereotype.Component;

import com.augmentum.ot.action.AssessmentAction;
import com.augmentum.ot.dao.EmployeeDao;
import com.augmentum.ot.dataObject.EmailSendCondition;
import com.augmentum.ot.dataObject.constant.FlagConstants;
import com.augmentum.ot.dataObject.constant.LogConstants;
import com.augmentum.ot.email.EmailUtil;
import com.augmentum.ot.email.InitNeededRemindCoursesAndSession;
import com.augmentum.ot.exception.DataWarningException;
import com.augmentum.ot.exception.ServerErrorException;
import com.augmentum.ot.model.ActualCourse;
import com.augmentum.ot.model.Employee;
import com.augmentum.ot.model.Plan;
import com.augmentum.ot.model.PlanEmployeeMap;
import com.augmentum.ot.service.EmailService;
import com.augmentum.ot.util.BeanFactory;

@Component("emailService")
public class EmailServiceImpl implements EmailService {
    private Logger logger = Logger.getLogger(AssessmentAction.class);

    @Override
    public void sendEmailByCancelPlan(HttpServletRequest request, Plan plan, EmailSendCondition emailSendCondition) throws ServerErrorException,
            DataWarningException {
        logger.info("Send Email by cancel plan [#" + plan.getPlanId() + "]");
        logger.info("Send Email by cancel plan. The reason is:" + plan.getCancelPlanReason());
        EmailUtil.sendEmailByCancelPlan(request, plan, emailSendCondition.getIsSendToManager());
        // ///////////////////////cancel plan
        if(FlagConstants.IS_PUBLISHED == plan.getPlanIsPublish()){
            for (ActualCourse actualCourse : plan.getActualCourses()) {
                if (null != actualCourse.getCourseStartTime() && actualCourse.getCourseStartTime().after(new Date())) {
                    removeNeededRemindCourse(actualCourse.getActualCourseId());
                }
            }
        }
    }

    @Override
    public void sendEmailByPublishPlan(HttpServletRequest request, Plan plan, EmailSendCondition emailSendCondition) throws ServerErrorException,
            DataWarningException {
        logger.info("Send Email by publish plan [#" + plan.getPlanId() + "]");
        EmailUtil.sendEmailByPublishPlan(request, plan, emailSendCondition.getIsSendToManager());
        // ///////////////publish plan
        if(FlagConstants.NO_REMIND_EMAIL != plan.getReminderEmail()){
            for (ActualCourse actualCourse : plan.getActualCourses()) {
                if (actualCourse.getCourseStartTime() != null) {
                    addOrUpdateNeededRemindCourse(actualCourse, plan);
                }
            }
        }
    }

    @Override
    public void sendEmailByTraineeLeaveOrBackCourse(HttpServletRequest request, Employee employee, ActualCourse actualCourse, String leaveFlag,
            String leaveCourseReason) throws ServerErrorException, DataWarningException {
        logger.info("Send Email by leave or back course [#" + actualCourse.getActualCourseId() + "]");
        EmailUtil.sendTraineeLeaveOrBackCourseEmail(ServletActionContext.getRequest(), employee, actualCourse, leaveFlag, leaveCourseReason);
    }

    @Override
    public void sendEmailByTraineeLeaveOrBackPlan(HttpServletRequest request, Employee employee, Plan plan,
            List<ActualCourse> needSendEmailCourseList, String leaveFlag, String leavePlanReason) throws ServerErrorException, DataWarningException {
        logger.info("Send Email by leave or back plan [#" + plan.getPlanId() + "]");
        EmailUtil.sendTraineeLeaveOrBackPlanEmail(request, employee, plan, needSendEmailCourseList, leaveFlag, leavePlanReason);
    }

    // 1. send Email for deleted or added trainees, by margaret
    /*
     * =====================requirement========================= trainee has
     * three type: invited , optional and specific plan type is invited 1. if a
     * invited or optional trainee had been removed should send email like that
     * Subject (case a): [Training] You are no longer in plan 2. if a trainee's
     * type had been changed should send email like that Subject (case b):
     * [Training] Training plan change notice
     * 
     * plan type is public (Specific Employees): 1. If several trainees /
     * trainers are removed from the plan, an email should be sent to them .
     * (All Employees): 2 .If several trainers are removed from the plan, an
     * email should be sent to them. Subject (case a): [Training] You are no
     * longer in plan Subject (case b): [Training] Training plan xxxxx change
     * notice
     */

    /* ******************************************************************** */
    /*
     * Total two type of email (in my words) 1.If there is someone
     * removed([Training] You are no longer in plan) Someone had been removed is
     * means that this one is no longer in this plan.(not trainer and trainees)
     * 1)Invited plan invited trainee and trainer had been removed. They should
     * in to list optional trainee had been removed. They should in cc list
     * 2)Public plan Trainer and specific trainee had been removed.Send email to
     * them
     * 
     * 2.If there is someone changed([Training] Training plan change notice)
     * 1)Invited plan if someone change to trainer or invited trainee send
     * email(in to list) if someone change to optional trainee send email(in cc
     * list) 2)Public plan Trainer change to specific trainee or specific
     * trainee change to trainer.send email to them
     */
    @Override
    public void sendEmailByUpdatePlan(HttpServletRequest request, Plan oldPlan, Plan newPlan, EmailSendCondition emailSendCondition)
            throws ServerErrorException, DataWarningException {
        logger.info("Send Email by update plan");
        updateNeedReminderActualCourse(oldPlan, newPlan);
        // if the plan is not published do nothing
        // someone change had send email before
        List<Employee> needEmailToChangedEmployees = new ArrayList<Employee>();
        List<Employee> needEmailCcChangedEmployees = new ArrayList<Employee>();

        List<Employee> needEmailToRemovedEmployees = new ArrayList<Employee>();
        List<Employee> needEmailCcRemovedEmployees = new ArrayList<Employee>();

        List<Employee> needEmailToAllEmployees = new ArrayList<Employee>();
        List<Employee> needEmailCcAllEmployees = new ArrayList<Employee>();
        Set<Employee> sendEmailBefore = new HashSet<Employee>();

        /* =================start of specific trainee list================= */
        /*
         * because specific trainees need not send email to manager,So it need
         * to in another list
         */
        List<Employee> needEmailToRemovedSpecificTrainees = new ArrayList<Employee>();
        List<Employee> needEmailToChangedSpecificTrainees = new ArrayList<Employee>();
        List<Employee> needEmailToAllSpecificTrainees = new ArrayList<Employee>();

        /* =================end of specific trainee list================= */
        // 1. Find the plan is public or invite
        // Neither the plan is invite or public the below variables were useful
        Map<Integer, List<Employee>> oldAttendPerson = getAttendPersonGroupByAttendType(oldPlan);
        Map<Integer, List<Employee>> newAttendPerson = getAttendPersonGroupByAttendType(newPlan);

        List<Employee> oldTrainerList = oldAttendPerson.get(FlagConstants.ATTEND_TYPE_TRAINER);
        List<Employee> newTrainerList = newAttendPerson.get(FlagConstants.ATTEND_TYPE_TRAINER);

        List<Employee> allAttendList = newAttendPerson.get(FlagConstants.ATTEND_TYPE_ALL);
        Map<Integer, List<Employee>> changedPersonList = getChangedPerson(oldAttendPerson, newAttendPerson);

        /* ===========================trainer handle start===================== */
        // handle trainer change and remove. Always in email to list
        // Neither the plan is public or invited ,It contains trainer.
        needEmailToAllEmployees.addAll(newTrainerList);
        // compile if trainer removed
        Map<Integer, List<Employee>> removedTrainers = getRemovedPerson(oldTrainerList, newTrainerList, allAttendList);
        // send mail to removed trainer
        // removed all role trainers
        needEmailToRemovedEmployees.addAll(removedTrainers.get(FlagConstants.REMOVE_ALL_ROLE));
        // removed one role trainers
        needEmailToChangedEmployees.addAll(removedTrainers.get(FlagConstants.REMOVE_ONE_ROLE));
        // compile if trainer changed
        List<Employee> changedTrainers = changedPersonList.get(FlagConstants.ATTEND_TYPE_TRAINER);
        // send mail to changed trainer
        needEmailToChangedEmployees.addAll(changedTrainers);
        /* ===========================trainer handle end===================== */

        // invited
        if (newPlan != null && FlagConstants.INVITED_PLAN_ID == newPlan.getPlanType().getPlanTypeId()) {
            logger.info("Send Email by update invited plan");
            // old plan
            List<Employee> oldInvitedTraineeList = oldAttendPerson.get(FlagConstants.ATTEND_TYPE_INVITED);
            List<Employee> oldOptionalTraineeList = oldAttendPerson.get(FlagConstants.ATTEND_TYPE_OPTIONAL);
            // newPlan
            List<Employee> newInvitedTraineeList = newAttendPerson.get(FlagConstants.ATTEND_TYPE_INVITED);
            List<Employee> newOptionalTraineeList = newAttendPerson.get(FlagConstants.ATTEND_TYPE_OPTIONAL);

            // compile if someone removed
            // invited trainee had been removed
            Map<Integer, List<Employee>> removedInvitedTrainees = getRemovedPerson(oldInvitedTraineeList, newInvitedTraineeList, allAttendList);
            // send email to InvitedTrainees(to list)
            // removed all role InvitedTrainees
            needEmailToRemovedEmployees.addAll(removedInvitedTrainees.get(FlagConstants.REMOVE_ALL_ROLE));
            // removed one role InvitedTrainees
            needEmailToChangedEmployees.addAll(removedInvitedTrainees.get(FlagConstants.REMOVE_ONE_ROLE));

            // optional trainee had been removed
            Map<Integer, List<Employee>> removedOptionalTrainees = getRemovedPerson(oldOptionalTraineeList, newOptionalTraineeList, allAttendList);
            // send email to removedOptionalTrainees(cc list)
            // removed all role optional trainees
            needEmailCcRemovedEmployees.addAll(removedOptionalTrainees.get(FlagConstants.REMOVE_ALL_ROLE));
            // removed one role optional trainees
            needEmailCcChangedEmployees.addAll(removedOptionalTrainees.get(FlagConstants.REMOVE_ONE_ROLE));

            // compile if someone changed
            List<Employee> changedToInvitedTraineeList = changedPersonList.get(FlagConstants.ATTEND_TYPE_INVITED);
            // send email to changedToInvitedTraineeList(to list )
            needEmailToChangedEmployees.addAll(changedToInvitedTraineeList);

            List<Employee> changedToOptionalTraineeList = changedPersonList.get(FlagConstants.ATTEND_TYPE_OPTIONAL);
            // send email to removedOptionalTrainees(cc list)
            needEmailCcChangedEmployees.addAll(changedToOptionalTraineeList);

            needEmailToAllEmployees.addAll(newInvitedTraineeList);
            needEmailCcAllEmployees.addAll(newOptionalTraineeList);

            // public plan

        } else {
            logger.info("Send Email by update public plan");
            // check if the plan isAllEmployee
            // If the public plan is AllEmployee only send email to trainer.
            // changed to trainer and removed from trainer had handled
            if (newPlan.getIsAllEmployee() == FlagConstants.IS_ALL_EMPLOYEES) {
                // send Email to all joined employee
                logger.info("Send Email to all joined employee by update a AllEmployee public plan");
                needEmailToAllSpecificTrainees.addAll(newAttendPerson.get(FlagConstants.ATTEND_TYPE_JOIN));
            } else {
                logger.info("Send Email to all joined employee by update a specific trainees public plan");
                // old plan SpecficTraineeList
                List<Employee> oldSpecificTraineeList = oldAttendPerson.get(FlagConstants.ATTEND_TYPE_SPECIFIC);
                // new plan SpecficTraineeList
                List<Employee> newSpecificTraineeList = newAttendPerson.get(FlagConstants.ATTEND_TYPE_SPECIFIC);
                // send email to trainer and specific trainee
                // compile if someone removed
                Map<Integer, List<Employee>> removedSpecificTrainees = getRemovedPerson(oldSpecificTraineeList, newSpecificTraineeList, allAttendList);

                // send email to removed person(to list)
                // removed all role specific trainees
                needEmailToRemovedSpecificTrainees.addAll(removedSpecificTrainees.get(FlagConstants.REMOVE_ALL_ROLE));
                // removed one role specific trainees
                needEmailToChangedSpecificTrainees.addAll(removedSpecificTrainees.get(FlagConstants.REMOVE_ONE_ROLE));

                // compile if someone changed
                List<Employee> changedSpecficTrainees = changedPersonList.get(FlagConstants.ATTEND_TYPE_SPECIFIC);
                // send email to removed person(to list)
                needEmailToChangedSpecificTrainees.addAll(changedSpecficTrainees);
                needEmailToAllSpecificTrainees.addAll(newSpecificTraineeList);
            }
        }
        logger.info("Send Email removed person");
        // send removed email
        EmailUtil.sendEmailToRemovedEmployeesByUpdatePlan(oldPlan, newPlan, needEmailToRemovedEmployees, needEmailCcRemovedEmployees,
                needEmailToRemovedSpecificTrainees, request, emailSendCondition.getIsSendToManager());

        // record the employees had send email
        sendEmailBefore.addAll(needEmailToRemovedEmployees);
        sendEmailBefore.addAll(needEmailCcRemovedEmployees);

        // send email to all to notice the plan is changed
        logger.info("Send Email to notice the plan is changed");
        if (emailSendCondition.getIsModifiedTrainee() == FlagConstants.SEND_MODIFY_TRAINEE) {
            // send email to changed Employees only
            logger.info("Send Email by update plan to changed Employees only");
            EmailUtil.sendEmailByUpdatePlan(oldPlan, newPlan, needEmailToChangedEmployees, needEmailCcChangedEmployees,
                    needEmailToChangedSpecificTrainees, null, request, emailSendCondition.getIsSendToManager());
        } else {
            if (emailSendCondition.getIsSendEmail() == FlagConstants.SEND_EMAIL
                    || emailSendCondition.getIsSendAllTrainee() == FlagConstants.SEND_ALL_TRAINEE) {
                logger.info("Send Email by update plan to all employee");
                EmailUtil.sendEmailByUpdatePlan(oldPlan, newPlan, needEmailToAllEmployees, needEmailCcAllEmployees, needEmailToAllSpecificTrainees,
                        sendEmailBefore, request, emailSendCondition.getIsSendToManager());
            }
        }
    }

    private Map<Integer, List<Employee>> getChangedPerson(Map<Integer, List<Employee>> oldAttendPerson, Map<Integer, List<Employee>> newAttendPerson) {
        Map<Integer, List<Employee>> changedPerson = new HashMap<Integer, List<Employee>>();
        List<Employee> changedToInvitedTraineeList = new ArrayList<Employee>();
        List<Employee> changedToOptionalTraineeList = new ArrayList<Employee>();
        List<Employee> changedToSpecificTraineeList = new ArrayList<Employee>();
        List<Employee> changedToTrainerList = new ArrayList<Employee>();

        for (Employee employee : newAttendPerson.get(FlagConstants.ATTEND_TYPE_INVITED)) {
            if (!oldAttendPerson.get(FlagConstants.ATTEND_TYPE_INVITED).contains(employee)) {
                changedToInvitedTraineeList.add(employee);
            }
        }
        for (Employee employee : newAttendPerson.get(FlagConstants.ATTEND_TYPE_OPTIONAL)) {
            if (!oldAttendPerson.get(FlagConstants.ATTEND_TYPE_OPTIONAL).contains(employee)) {
                changedToOptionalTraineeList.add(employee);
            }
        }
        for (Employee employee : newAttendPerson.get(FlagConstants.ATTEND_TYPE_SPECIFIC)) {
            if (!oldAttendPerson.get(FlagConstants.ATTEND_TYPE_SPECIFIC).contains(employee)) {
                changedToSpecificTraineeList.add(employee);
            }
        }
        // TODO Trainer is no a attend type in database
        for (Employee employee : newAttendPerson.get(FlagConstants.ATTEND_TYPE_TRAINER)) {
            if (!oldAttendPerson.get(FlagConstants.ATTEND_TYPE_TRAINER).contains(employee)) {
                changedToTrainerList.add(employee);
            }
        }
        changedPerson.put(FlagConstants.ATTEND_TYPE_INVITED, changedToInvitedTraineeList);
        changedPerson.put(FlagConstants.ATTEND_TYPE_OPTIONAL, changedToOptionalTraineeList);
        changedPerson.put(FlagConstants.ATTEND_TYPE_SPECIFIC, changedToSpecificTraineeList);
        changedPerson.put(FlagConstants.ATTEND_TYPE_TRAINER, changedToTrainerList);
        return changedPerson;
    }

    private Map<Integer, List<Employee>> getRemovedPerson(List<Employee> oldList, List<Employee> newList, List<Employee> allAttendList) {
        Map<Integer, List<Employee>> removedPerson = new HashMap<Integer, List<Employee>>();
        List<Employee> removedAllRolePerson = new ArrayList<Employee>();
        List<Employee> removedOneRolePerson = new ArrayList<Employee>();
        for (Employee employee : oldList) {
            if (!newList.contains(employee)) {
                if (allAttendList.contains(employee)) {
                    removedOneRolePerson.add(employee);
                } else {
                    removedAllRolePerson.add(employee);
                }
            }
        }
        removedPerson.put(FlagConstants.REMOVE_ONE_ROLE, removedOneRolePerson);
        removedPerson.put(FlagConstants.REMOVE_ALL_ROLE, removedAllRolePerson);
        return removedPerson;
    }

    private Map<Integer, List<Employee>> getAttendPersonGroupByAttendType(Plan plan) {
        Map<Integer, List<Employee>> attendPerson = new HashMap<Integer, List<Employee>>();

        List<Employee> invitedTraineeList = new ArrayList<Employee>();
        List<Employee> optionalTraineeList = new ArrayList<Employee>();
        List<Employee> specificTraineeList = new ArrayList<Employee>();
        List<Employee> joinTraineeList = new ArrayList<Employee>();
        List<Employee> trainerList = new ArrayList<Employee>();
        String trainerNameList = plan.getTrainers();
        if (trainerNameList != null) {
            for (String name : trainerNameList.split(FlagConstants.SPLIT_COMMA)) {
                EmployeeDao employeeDao = BeanFactory.getEmployeeDao();
                if (employeeDao.findEmployeeByName(name) != null) {
                    trainerList.add(employeeDao.findEmployeeByName(name));
                }
            }
        }

        List<Employee> allAttendList = new ArrayList<Employee>();

        allAttendList.addAll(trainerList);
        Set<PlanEmployeeMap> employeeMapList = plan.getPlanEmployeeMapList();

        // delete the removed employees
        Set<PlanEmployeeMap> unDeletedEmployeeMapList = new HashSet<PlanEmployeeMap>();
        for (PlanEmployeeMap p : employeeMapList) {
            if (p.getPlanEmployeeIsDeleted() == FlagConstants.UN_DELETED) {
                unDeletedEmployeeMapList.add(p);
            }
        }

        for (PlanEmployeeMap p : unDeletedEmployeeMapList) {
            if (p.getPlanTraineeAttendType().equals(FlagConstants.ATTEND_TYPE_INVITED)) {
                invitedTraineeList.add(p.getEmployee());
            }
            if (p.getPlanTraineeAttendType().equals(FlagConstants.ATTEND_TYPE_OPTIONAL)) {
                optionalTraineeList.add(p.getEmployee());
            }
            if (p.getPlanTraineeAttendType().equals(FlagConstants.ATTEND_TYPE_SPECIFIC)) {
                specificTraineeList.add(p.getEmployee());
            }
            if (p.getPlanTraineeAttendType().equals(FlagConstants.ATTEND_TYPE_JOIN)) {
                joinTraineeList.add(p.getEmployee());
            }
            allAttendList.add(p.getEmployee());
        }
        attendPerson.put(FlagConstants.ATTEND_TYPE_INVITED, invitedTraineeList);
        attendPerson.put(FlagConstants.ATTEND_TYPE_OPTIONAL, optionalTraineeList);
        attendPerson.put(FlagConstants.ATTEND_TYPE_SPECIFIC, specificTraineeList);
        attendPerson.put(FlagConstants.ATTEND_TYPE_TRAINER, trainerList);
        attendPerson.put(FlagConstants.ATTEND_TYPE_JOIN, joinTraineeList);
        attendPerson.put(FlagConstants.ATTEND_TYPE_ALL, allAttendList);
        return attendPerson;
    }

    private void updateNeedReminderActualCourse(Plan oldPlan, Plan newPlan) {
        if (newPlan.getPlanIsPublish().equals(FlagConstants.IS_PUBLISHED)) {
            for (ActualCourse actualCourse : oldPlan.getActualCourses()) {
                removeNeededRemindCourse(actualCourse.getActualCourseId());
            }
            // if plan is no need to send reminder email. remove all actual
            // course id in the email map
            if (newPlan.getReminderEmail().equals(FlagConstants.NO_REMIND_EMAIL)) {
                for (ActualCourse actualCourse : newPlan.getActualCourses()) {
                    removeNeededRemindCourse(actualCourse.getActualCourseId());
                }
            } else {
                // if plan is need to send reminder email. remove all old
                // actual course id in the email map
                // add New actual course
                for (ActualCourse actualCourse : newPlan.getActualCourses()) {
                    if (actualCourse.getCourseStartTime() != null) {
                        addOrUpdateNeededRemindCourse(actualCourse, newPlan);
                    }
                }
            }
        }
    }

    private void addOrUpdateNeededRemindCourse(ActualCourse actualCourse, Plan plan) {
        logger.debug(LogConstants.message("Add or Update remind plan course ", actualCourse.getActualCourseId()));
        Long whichTimeSended = actualCourse.getCourseStartTime().getTime() - plan.getReminderEmail() * 3600 * 1000;
        logger.debug(LogConstants.message("Add or Update remind plan course, send time ", whichTimeSended));
        Map<Integer, Long> newNeededRemindCourses = new HashMap<Integer, Long>();
        if(whichTimeSended > new Date().getTime()){
            newNeededRemindCourses.put(actualCourse.getActualCourseId(), whichTimeSended);
        }
        new InitNeededRemindCoursesAndSession(BeanFactory.getApplication()).addNeededRemindCourses(newNeededRemindCourses);
    }

    private void removeNeededRemindCourse(Integer actualCourseId) {
        logger.debug(LogConstants.message("Remove remind plan course ", actualCourseId));
        new InitNeededRemindCoursesAndSession(BeanFactory.getApplication()).removePlanCoursesFromApplication(actualCourseId);
    }
}
