package com.augmentum.ot.email;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.augmentum.ot.dao.PlanEmployeeMapDao;
import com.augmentum.ot.dataObject.constant.DateFormatConstants;
import com.augmentum.ot.dataObject.constant.FlagConstants;
import com.augmentum.ot.dataObject.constant.JsonKeyConstants;
import com.augmentum.ot.dataObject.constant.LogConstants;
import com.augmentum.ot.exception.DataWarningException;
import com.augmentum.ot.exception.ServerErrorException;
import com.augmentum.ot.model.ActualCourse;
import com.augmentum.ot.model.Employee;
import com.augmentum.ot.model.LeaveNote;
import com.augmentum.ot.model.Plan;
import com.augmentum.ot.model.PlanEmployeeMap;
import com.augmentum.ot.remoteService.IRemoteEmailService;
import com.augmentum.ot.remoteService.constant.RemoteProperyConstants;
import com.augmentum.ot.service.ActualCourseService;
import com.augmentum.ot.service.EmployeeService;
import com.augmentum.ot.service.LeaveNoteService;
import com.augmentum.ot.service.PlanService;
import com.augmentum.ot.util.BeanFactory;
import com.augmentum.ot.util.DateHandlerUtils;

public class EmailUtil {
    private static Logger logger = Logger.getLogger(EmailUtil.class);

    public static void sendEmailToRemovedEmployeesByUpdatePlan(Plan oldPlan, Plan newPlan, List<Employee> emailToEmployees,
            List<Employee> emailCcEmployees, List<Employee> emailToNotSendToManager, HttpServletRequest request, int isSendToManager)
            throws ServerErrorException, DataWarningException {
        if (emailToEmployees.isEmpty() && emailCcEmployees.isEmpty() && emailToNotSendToManager.isEmpty()) {
            // do nothing
        } else {
            Map<String, String> emailMap = getEmailReminder(EmailConstant.EMAIL_DELETE_EMPLOYEE_REMINDER_PATH);
            EmployeeService employeeService = BeanFactory.getEmployeeService();
            IRemoteEmailService remoteEmailService = BeanFactory.getIRemoteEmailService();
            Employee master = employeeService.findEmployeeByName(oldPlan.getPlanCreator());
            // Send email to trainee and his reportManger
            Set<String> emailToEmails = new HashSet<String>();
            Set<String> emailCcEmails = new HashSet<String>();
            Set<String> emailBccEmails = new HashSet<String>();
            // cc to master
            emailCcEmails.add(master.getAugEmail());
            // To list
            if (emailToEmployees != null) {
                for (Employee employee : emailToEmployees) {
                    emailToEmails.add(employee.getAugEmail());
                    if (isSendToManager == FlagConstants.SEND_TO_MANAGER) {
                        emailBccEmails.add(employee.getManagerEmail());
                    }
                }
            }
            // specific trainees didn't send email to manager
            if (emailToNotSendToManager != null) {
                for (Employee employee : emailToNotSendToManager) {
                    emailToEmails.add(employee.getAugEmail());
                }
            }
            // Cc list
            if (emailCcEmployees != null) {
                for (Employee employee : emailCcEmployees) {
                    emailCcEmails.add(employee.getAugEmail());
                }
            }

            makeDeleteEmployeeEmailConstent(emailMap, newPlan);
            generateEmailAddress(emailMap, emailToEmails, emailCcEmails, emailBccEmails);
            remoteEmailService.sendEmail(emailMap);
        }
    }

    public static void sendEmailByUpdatePlan(Plan oldPlan, Plan newPlan, List<Employee> emailToEmployees, List<Employee> emailCcEmployees,
            List<Employee> emailToNotSendToManager, Set<Employee> sendEmailBefore, HttpServletRequest request, int isSendToManager)
            throws ServerErrorException, DataWarningException {
        if (emailToEmployees.isEmpty() && emailCcEmployees.isEmpty() && emailToNotSendToManager.isEmpty()) {
            // do nothing
        } else {
            Map<String, String> emailMap = getEmailReminder(EmailConstant.EMAIL_UPDATE_PLAN_REMINDER_PATH);
            EmployeeService employeeService = BeanFactory.getEmployeeService();
            IRemoteEmailService remoteEmailService = BeanFactory.getIRemoteEmailService();
            Employee master = employeeService.findEmployeeByName(oldPlan.getPlanCreator());
            // Send email to trainee and his reportManger
            Set<String> emailToEmails = new HashSet<String>();
            Set<String> emailCcEmails = new HashSet<String>();
            Set<String> emailBccEmails = new HashSet<String>();
            // cc to master
            emailCcEmails.add(master.getAugEmail());
            if (emailCcEmployees != null) {
                for (Employee employeeCc : emailCcEmployees) {
                    if (sendEmailBefore == null || !sendEmailBefore.contains(employeeCc)) {
                        emailCcEmails.add(employeeCc.getAugEmail());
                    }
                }
            }
            if (emailToEmployees != null) {
                for (Employee employeeTo : emailToEmployees) {
                    if (sendEmailBefore == null || !sendEmailBefore.contains(employeeTo)) {
                        emailToEmails.add(employeeTo.getAugEmail());
                        if (isSendToManager == FlagConstants.SEND_TO_MANAGER) {
                            emailBccEmails.add(employeeTo.getManagerEmail());
                        }
                    }
                }
            }
            // specific trainees didn't send email to manager
            if (emailToNotSendToManager != null) {
                for (Employee employee : emailToNotSendToManager) {
                    emailToEmails.add(employee.getAugEmail());
                }
            }

            makeUpdatePlanEmailConstent(emailMap, oldPlan, newPlan);
            generateEmailAddress(emailMap, emailToEmails, emailCcEmails, emailBccEmails);
            remoteEmailService.sendEmail(emailMap);
        }
    }

    /**
     * If a trainee want to apply leave for a course or cancel his application,
     * this method can help him send email to his trainer, master and report
     * manager
     */
    public static void sendTraineeLeaveOrBackCourseEmail(HttpServletRequest request, Employee employee, ActualCourse actualCourse,
            String leaveOrBack, String reason) throws ServerErrorException, DataWarningException {
        logger.info("send email leave or back course");
        IRemoteEmailService remoteEmailService = BeanFactory.getIRemoteEmailService();
        Map<String, String> messageMap = new HashMap<String, String>();
        messageMap = getEmailReminder(EmailConstant.EMAIL_LEAVE_COURSE_REMINDER_PATH);
        List<ActualCourse> actualCourses = new ArrayList<ActualCourse>();
        actualCourses.add(actualCourse);
        makeApplyLeaveCourseContent(messageMap, actualCourse, employee, reason, leaveOrBack);
        makeApplyLeaveEmailAddress(request, employee, messageMap, actualCourses);
        remoteEmailService.sendEmail(messageMap);
    }
    
    /**
     * If a trainee want to apply leave a plan or apply back a plan, this method
     * help send email to the trainee's trainer, master and report manager
     * 
     */
    public static void sendTraineeLeaveOrBackPlanEmail(HttpServletRequest request, Employee employee, Plan plan, List<ActualCourse> actualCourses,
            String leaveOrBack, String reason) throws ServerErrorException, DataWarningException {
        logger.info("send email leave or back course");
        IRemoteEmailService remoteEmailService = BeanFactory.getIRemoteEmailService();
        Map<String, String> messageMap = new HashMap<String, String>();
        messageMap = getEmailReminder(EmailConstant.EMAIL_LEAVE_PLAN_REMINDER_PATH);
        makeApplyLeavePlanEmailContent(messageMap, plan, employee, reason, leaveOrBack);
        makeApplyLeaveEmailAddress(request, employee, messageMap, actualCourses);
        remoteEmailService.sendEmail(messageMap);
    }
    
    /**
     * 
     * Because publish plan, so send email to trainees and trainers and their
     * report managers
     * 
     * @param request
     * @param plan
     * @throws ServerErrorException
     * @throws DataWarningException
     */
    public static void sendEmailByPublishPlan(HttpServletRequest request, Plan plan, int isSendToManager) throws ServerErrorException,
            DataWarningException {
        IRemoteEmailService remoteEmailService = BeanFactory.getIRemoteEmailService();
        Map<String, String> emailMap = generateEmailContentByPlan(plan, EmailConstant.EMAIL_PUBLISH_PLAN_REMINDER_PATH);
        if (plan.getPlanType().getPlanTypeName().equals(FlagConstants.INVITED_PLAN)) {
            String content = emailMap.get(EmailConstant.EMAIL_CONTENT).replace(EmailConstant.INVITED_OR_PUBLIC_MEG_HOLDER,
                    EmailConstant.INVITED_PLAN_MSG).replace(EmailConstant.END_MEG_HOLDER, EmailConstant.END_MSG);
            if (content.contains(EmailConstant.TO_BE_DETERMINED)) {
                content = content.replace(EmailConstant.CONTACT_HOLDER, EmailConstant.EXPLAIN_TBD + EmailConstant.EMAIL_CONTACT_MSG);
            } else {
                content = content.replace(EmailConstant.CONTACT_HOLDER, EmailConstant.EMAIL_CONTACT_MSG);
            }

            emailMap.put(EmailConstant.EMAIL_CONTENT, content);
            String subject = emailMap.get(EmailConstant.EMAIL_SUBJECT).replace(EmailConstant.PLAN_TYPE_HOLDER,
                    FlagConstants.INVITED_PLAN.toLowerCase());
            emailMap.put(EmailConstant.EMAIL_SUBJECT, subject);
            makeInvitedPlanEmailAddress(emailMap, request, plan, isSendToManager);
            remoteEmailService.sendEmail(emailMap);
        } else {
            if (plan.getIsAllEmployee() == 1) {
                makePublicPlanTrainerAddress(emailMap, request, plan, isSendToManager, "publish");
                remoteEmailService.sendEmail(emailMap);
            } else {
                Map<String, String> tempEmailMap = new HashMap<String, String>();
                for (String string : emailMap.keySet()) {
                    tempEmailMap.put(string, emailMap.get(string));
                }
                if (plan.getRegisterNotice() == 1) {
                    makePublicPlanTrainerAddress(emailMap, request, plan, isSendToManager, "publish");
                    remoteEmailService.sendEmail(emailMap);
                    makePublicPlanTraineeAddress(tempEmailMap, request, plan, FlagConstants.DONT_SEND_TO_MANAGER);
                    remoteEmailService.sendEmail(tempEmailMap);
                } else {
                    makePublicPlanTrainerAddress(emailMap, request, plan, isSendToManager, "publish");
                    remoteEmailService.sendEmail(emailMap);
                }
            }
        }
    }
    
    /**
     * 
     * Because cancel plan, so send email to trainees and trainers and their
     * report managers
     * 
     * @param request
     * @param plan
     * @param isSendToManager
     * @throws ServerErrorException
     * @throws DataWarningException
     */
    public static void sendEmailByCancelPlan(HttpServletRequest request, Plan plan, int isSendToManager) throws ServerErrorException,
            DataWarningException {
        PlanEmployeeMapDao planEmployeeMapDao = BeanFactory.getPlanEmployeeMapDao();
        IRemoteEmailService remoteEmailService = BeanFactory.getIRemoteEmailService();
        Map<String, String> emailMap = getEmailReminder(EmailConstant.EMAIL_CANCEL_PLAN_REMINDER_PATH);
        makeCancelPlanEmailContent(emailMap, plan);
        if (plan.getPlanType().getPlanTypeName().equals(FlagConstants.INVITED_PLAN)) {
            makeInvitedPlanEmailAddress(emailMap, request, plan, isSendToManager);
        } else {
            makePublicPlanTrainerAddress(emailMap, request, plan, isSendToManager, "cancel");
            List<PlanEmployeeMap> planEmployeeMap = null;
            if (plan.getIsAllEmployee() == FlagConstants.IS_ALL_EMPLOYEES) {
                planEmployeeMap = planEmployeeMapDao.getPlanEmployeeMaps(plan.getPlanId(), FlagConstants.ATTEND_TYPE_JOIN, FlagConstants.UN_DELETED);
            } else {
                planEmployeeMap = planEmployeeMapDao.getPlanEmployeeMaps(plan.getPlanId(), FlagConstants.ATTEND_TYPE_SPECIFIC,
                        FlagConstants.UN_DELETED);
            }
            Set<String> managerEmails = new HashSet<String>();
            Set<String> traineeEmails = new HashSet<String>();
            for (PlanEmployeeMap traineeMap : planEmployeeMap) {
                getStaffEmailandManagerEmails(traineeMap.getEmployee().getAugUserName(), managerEmails, traineeEmails, request, isSendToManager);
            }
            String emailTo = emailMap.get(EmailConstant.EMAIL_TO);
            for (String traineeEmail : traineeEmails) {
                if (emailTo.indexOf(traineeEmail) == -1) {
                    emailTo += traineeEmail + ";";
                }
            }
            emailMap.put(EmailConstant.EMAIL_TO, emailTo);
        }
        remoteEmailService.sendEmail(emailMap);
    }
    
    private static void makeDeleteEmployeeEmailConstent(Map<String, String> emailMap, Plan plan) {
        String content = emailMap.get(EmailConstant.EMAIL_CONTENT).replace(EmailConstant.EMAIL_PLAN_CREATOR_REPLACE, plan.getPlanCreator()).replace(
                EmailConstant.CONTACT_HOLDER, EmailConstant.EMAIL_CONTACT_MSG).replace(EmailConstant.EMAIL_PLAN_NAME_REPLACE, plan.getPlanName())
                .replace(EmailConstant.EMAIL_PROJECT_NAME_REPLACE, RemoteProperyConstants.APP_NAME);
        emailMap.put(EmailConstant.EMAIL_CONTENT, content);
        String subject = emailMap.get(EmailConstant.EMAIL_SUBJECT).replace(EmailConstant.EMAIL_PLAN_NAME_REPLACE, plan.getPlanName());
        emailMap.put(EmailConstant.EMAIL_SUBJECT, subject);
    }

    private static void makeUpdatePlanEmailConstent(Map<String, String> emailMap, Plan oldPlan, Plan newPlan) {
        String content = emailMap.get(EmailConstant.EMAIL_CONTENT);

        // replace the plan url
        // %planDetailUrl%
        content = content.replace(EmailConstant.PLAN_URL_HOLDER, EmailConstant.VIEW_PLAN_URL + newPlan.getPlanId());

        // course Table highlight the change message
        String courseTable = "";
        // two courses
        List<ActualCourse> oldCourses = oldPlan.getActualCourses();
        List<ActualCourse> newCourses = newPlan.getActualCourses();
        List<Integer> oldCourseIds = new ArrayList<Integer>();
        for (ActualCourse oldCourse : oldCourses) {
            oldCourseIds.add(oldCourse.getActualCourseId());
        }
        for (ActualCourse newCourse : newCourses) {
            // messages
            // "<TR><TD>%courseName%</TD><TD>%room%</TD><TD>%time%</TD><TD>%trainer%</TD></TR>"
            String oneCourse = EmailConstant.ONE_COURSE_LINE;
            String courseName = replaceNullDataToTBD(newCourse.getCourseName());
            String room = replaceNullDataToTBD(newCourse.getCourseRoomNum());
            String startTime = DateHandlerUtils.dateToString(DateFormatConstants.YYYY_MM_DD_HH_MM, newCourse.getCourseStartTime());
            String endTime = DateHandlerUtils.dateToString(DateFormatConstants.HH_MM, newCourse.getCourseEndTime());
            String time = "";
            String trainer = replaceNullDataToTBD(newCourse.getCourseTrainer());
            // If the course is deleted show nothing
            // The course was not been deleted

            // add a course
            if (!oldCourseIds.contains(newCourse.getActualCourseId())) {
                courseName = EmailConstant.HIGHLIGHT_PREFIX + courseName + EmailConstant.HIGHLIGHT_SUFFIX;
                room = EmailConstant.HIGHLIGHT_PREFIX + room + EmailConstant.HIGHLIGHT_SUFFIX;
                time = EmailConstant.HIGHLIGHT_PREFIX + replaceNullDataToTBD(startTime) + EmailConstant.MIDDLE_LINE + replaceNullDataToTBD(endTime)
                        + EmailConstant.HIGHLIGHT_SUFFIX;
                trainer = EmailConstant.HIGHLIGHT_PREFIX + trainer + EmailConstant.HIGHLIGHT_SUFFIX;
            } else {
                // the course changed
                for (ActualCourse oldCourse : oldCourses) {
                    if (oldCourse.getActualCourseId().equals(newCourse.getActualCourseId())) {
                        courseName = highlightMsgIfChanged(oldCourse.getCourseName(), courseName);
                        room = highlightMsgIfChanged(oldCourse.getCourseRoomNum(), room);
                        startTime = highlightMsgIfChanged(DateHandlerUtils.dateToString(DateFormatConstants.YYYY_MM_DD_HH_MM, oldCourse
                                .getCourseStartTime()), startTime);
                        endTime = highlightMsgIfChanged(DateHandlerUtils.dateToString(DateFormatConstants.HH_MM, oldCourse.getCourseEndTime()),
                                endTime);
                        trainer = highlightMsgIfChanged(oldCourse.getCourseTrainer(), trainer);
                    }
                }
            }
            time = replaceNullDataToTBD(startTime + EmailConstant.MIDDLE_LINE + endTime);
            oneCourse = oneCourse.replace(EmailConstant.COURSE_NAME_HOLDER, courseName).replace(EmailConstant.COURSE_ROOM_HOLDER, room).replace(
                    EmailConstant.COURSE_TIME_HOLDER, time).replace(EmailConstant.COURSE_TRAINER_HOLDER, trainer);
            courseTable += oneCourse;
        }

        content = content.replace(EmailConstant.EMAIL_TRS_REPLACE, courseTable);
        if (content.contains(EmailConstant.TO_BE_DETERMINED)) {
            content = content.replace(EmailConstant.CONTACT_HOLDER, EmailConstant.EXPLAIN_TBD + EmailConstant.EMAIL_CONTACT_MSG);
        } else {
            content = content.replace(EmailConstant.CONTACT_HOLDER, EmailConstant.EMAIL_CONTACT_MSG);
        }
        // replace messages
        content = content.replace(EmailConstant.EMAIL_PLAN_CREATOR_REPLACE, newPlan.getPlanCreator()).replace(EmailConstant.EMAIL_PLAN_NAME_REPLACE,
                newPlan.getPlanName()).replace(EmailConstant.EMAIL_PROJECT_NAME_REPLACE, RemoteProperyConstants.APP_NAME);
        emailMap.put(EmailConstant.EMAIL_CONTENT, content);
        String subject = emailMap.get(EmailConstant.EMAIL_SUBJECT).replace(EmailConstant.EMAIL_PLAN_NAME_REPLACE, newPlan.getPlanName());
        emailMap.put(EmailConstant.EMAIL_SUBJECT, subject);
    }

    private static String highlightMsgIfChanged(String oldMsg, String newMsg) {
        oldMsg = replaceNullDataToTBD(oldMsg);
        newMsg = replaceNullDataToTBD(newMsg);
        if (!oldMsg.equalsIgnoreCase(newMsg)) {
            newMsg = replaceNullDataToTBD(EmailConstant.HIGHLIGHT_PREFIX + newMsg + EmailConstant.HIGHLIGHT_SUFFIX);
        }
        return newMsg;
    }
    
    private static String replaceNullDataToTBD(String data) {
        if (data == null || "".equals(data.trim()) || "-".equals(data.trim()) || "~".equals(data.trim())) {
            data = EmailConstant.TO_BE_DETERMINED;
        }
        // Change TDB-TDB to TDB
        if (EmailConstant.TBD_TIME.equals(data)) {
            data = EmailConstant.TO_BE_DETERMINED;
        }
        // Change HIGHLIGHT TDB-TDB to HIGHLIGHT TDB
        if (EmailConstant.HIGHLIGHT_TBD_TIME.equals(data) || EmailConstant.HIGHLIGHT_TBD_NEW_COURSE_TIME.equals(data)) {
            data = EmailConstant.HIGHLIGHT_TBD;
        }
        return data;
    }

    /**
     * get email message map
     * 
     * @param path
     * @return message map the message map for email, include from, subject
     *         content...
     */
    public static Map<String, String> getEmailReminder(String path) {
        Map<String, String> messageMap = new HashMap<String, String>();
        InputStream inputStream = null;
        try {
            SAXReader reader = new SAXReader();
            inputStream = EmailUtil.class.getClassLoader().getResourceAsStream(path);
            Document document = reader.read(inputStream);
            Element root = document.getRootElement();
            Element content = root.element(EmailConstant.EMAIL_CONTENT);
            String contentString = (String) content.getData();
            // Send Email App Name
            messageMap.put(EmailConstant.EMAIL_FORM, RemoteProperyConstants.APP_NAME);
            // Email Subject template
            messageMap.put(EmailConstant.EMAIL_SUBJECT, root.element(EmailConstant.EMAIL_SUBJECT).getText());
            // TR template
            if (null != root.element(EmailConstant.EMAIL_TR_TEMPLATE)) {
                messageMap.put(EmailConstant.EMAIL_TR_TEMPLATE, root.element(EmailConstant.EMAIL_TR_TEMPLATE).getText());
            }
            if (null != root.element(EmailConstant.EMAIL_PLAN_TR_TEMPLATE)) {
                messageMap.put(EmailConstant.EMAIL_PLAN_TR_TEMPLATE, root.element(EmailConstant.EMAIL_PLAN_TR_TEMPLATE).getText());
            }
            // The following JsonKeyConstants.PLAN_INFO and
            // JsonKeyConstants.COURSE_INFO is used in the
            // "Leave_Email_Reminder"
            if (null != root.element(JsonKeyConstants.PLAN_INFO)) {
                messageMap.put(JsonKeyConstants.PLAN_INFO, root.element(JsonKeyConstants.PLAN_INFO).getText());
            }
            if (null != root.element(JsonKeyConstants.COURSE_INFO)) {
                messageMap.put(JsonKeyConstants.COURSE_INFO, root.element(JsonKeyConstants.COURSE_INFO).getText());
            }
            messageMap.put(EmailConstant.EMAIL_CONTENT, contentString);
        } catch (DocumentException e) {
            logger.error(LogConstants.exceptionMessage("Parse the xml"), e);
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                logger.error(LogConstants.exceptionMessage("Close the inputStream"), e);
            }
        }
        return messageMap;
    }

    private static void generateEmailAddress(Map<String, String> emailMap, Set<String> emailToEmails, Set<String> emailCcEmails,
            Set<String> emailBccEmails) {
        String emailTo = "";
        String emailBcc = "";
        String emailCc = "";
        for (String email : emailToEmails) {
            if (emailTo.indexOf(email) == -1) {
                emailTo += email + ";";
            }
        }
        for (String email : emailBccEmails) {
            if (emailBcc.indexOf(email) == -1) {
                emailBcc += email + ";";
            }
        }
        for (String email : emailCcEmails) {
            if (emailCc.indexOf(email) == -1) {
                emailCc += email + ";";
            }
        }
        emailMap.put(EmailConstant.EMAIL_TO, emailTo);
        emailMap.put(EmailConstant.EMAIL_CC, emailCc);
        emailMap.put(EmailConstant.EMAIL_BCC, emailBcc);
    }

    /**
     * 
     * @param
     * @return
     */
    private static void makeCancelPlanEmailContent(Map<String, String> emailMap, Plan plan) {
        String content = emailMap.get(EmailConstant.EMAIL_CONTENT);
        content = content.replace(EmailConstant.EMAIL_PLAN_NAME_REPLACE, plan.getPlanName()).replace(EmailConstant.EMAIL_PLAN_CREATOR_REPLACE,
                plan.getPlanCreator()).replace(EmailConstant.CONTACT_HOLDER, EmailConstant.EMAIL_CONTACT_MSG).replace(
                EmailConstant.EMAIL_PROJECT_NAME_REPLACE, RemoteProperyConstants.APP_NAME);
        
        content = content.replace(EmailConstant.LEAVE_ACTION_HOLDER,EmailConstant.LEAVE_ACTION);
        if (plan.getCancelPlanReason().equals("")) {
            content = content.replace(EmailConstant.LEAVE_REASON_TIP, "").replace(EmailConstant.LEAVE_REASON, "");
        } else {
            content = content.replace(EmailConstant.LEAVE_REASON_TIP, EmailConstant.REASON_TIP_CONTENT).replace(EmailConstant.LEAVE_REASON, plan.getCancelPlanReason());
        }
        emailMap.put(EmailConstant.EMAIL_CONTENT, content);
        String subject = emailMap.get(EmailConstant.EMAIL_SUBJECT).replace(EmailConstant.EMAIL_PLAN_NAME_REPLACE, plan.getPlanName());
        emailMap.put(EmailConstant.EMAIL_SUBJECT, subject);
    }

    /**
     * @param
     * @return
     * @throws ServerErrorException
     * @throws DataWarningException
     */
    private static void makeInvitedPlanEmailAddress(Map<String, String> emailMap, HttpServletRequest request, Plan plan, int isSendToManager)
            throws ServerErrorException, DataWarningException {
        EmployeeService employeeService = BeanFactory.getEmployeeService();
        PlanEmployeeMapDao planEmployeeMapDao = BeanFactory.getPlanEmployeeMapDao();
        // get trainees' email and trainee managers' email
        Set<String> traineeOrTrainerEmails = new HashSet<String>();
        Set<String> managerEmails = new HashSet<String>();
        Set<String> masterEmails = new HashSet<String>();
        String trainingMasterEmail = employeeService.findEmployeeByName(plan.getPlanCreator()).getAugEmail();
        masterEmails.add(trainingMasterEmail);
        List<ActualCourse> actualCourses = plan.getActualCourses();
        for (ActualCourse actualCourse : actualCourses) {
            getStaffEmailandManagerEmails(actualCourse.getCourseTrainer(), managerEmails, traineeOrTrainerEmails, request, isSendToManager);
        }
        List<PlanEmployeeMap> pemInvited = planEmployeeMapDao.getPlanEmployeeMaps(plan.getPlanId(), FlagConstants.ATTEND_TYPE_INVITED,
                FlagConstants.UN_DELETED);
        for (PlanEmployeeMap planEmployeeMap : pemInvited) {
            getStaffEmailandManagerEmails(planEmployeeMap.getEmployee().getAugUserName(), managerEmails, traineeOrTrainerEmails, request,
                    isSendToManager);
        }
        List<PlanEmployeeMap> pemOptional = planEmployeeMapDao.getPlanEmployeeMaps(plan.getPlanId(), FlagConstants.ATTEND_TYPE_OPTIONAL,
                FlagConstants.UN_DELETED);
        for (PlanEmployeeMap planEmployeeMap : pemOptional) {
            masterEmails.add(planEmployeeMap.getEmployee().getAugEmail());
        }
        generateEmailAddress(emailMap, traineeOrTrainerEmails, masterEmails, managerEmails);
    }

    /**
     * Get my report manager's email and myself's email
     * 
     * @param userName
     * @param managerEmails
     * @param traineeEmails
     * @param request
     * @throws ServerErrorException
     * @throws DataWarningException
     */
    private static void getStaffEmailandManagerEmails(String userName, Set<String> managerEmails, Set<String> traineeEmails,
            HttpServletRequest request, int isSendToManager) throws ServerErrorException, DataWarningException {
        if (userName == null || "".equals(userName)) {
            return;
        }
        EmployeeService employeeService = BeanFactory.getEmployeeService();
        Employee employee = employeeService.findEmployeeByName(userName);
        if(employee == null){
            return;
        }
        // Get report manager for IAP
        if (!traineeEmails.contains(employee.getAugEmail())) {
            traineeEmails.add(employee.getAugEmail());
        }
        if (isSendToManager == FlagConstants.SEND_TO_MANAGER && !managerEmails.contains(employee.getManagerEmail())) {
            managerEmails.add(employee.getManagerEmail());
        }
    }

    /**
     * 
     * @param
     * @return
     * @throws DataWarningException
     * @throws ServerErrorException
     */
    private static void makePublicPlanTrainerAddress(Map<String, String> emailMap, HttpServletRequest request, Plan plan, int isSendToManager,
            String callFlag) throws ServerErrorException, DataWarningException {
        EmployeeService employeeService = BeanFactory.getEmployeeService();
        String content = emailMap.get(EmailConstant.EMAIL_CONTENT).replace(EmailConstant.INVITED_OR_PUBLIC_MEG_HOLDER,
                "Appreciate you all for providing the trainings. If there is any issue, please contact with training master.").replace(
                EmailConstant.END_MEG_HOLDER, "").replace(EmailConstant.CONTACT_HOLDER, EmailConstant.EMAIL_CONTACT_MSG);
        emailMap.put(EmailConstant.EMAIL_CONTENT, content);
        if (callFlag.equals("publish")) {
            String subject = "[Training] You are invited as trainer in public plan \"" + plan.getPlanName() + "\"";
            emailMap.put(EmailConstant.EMAIL_SUBJECT, subject);
        }
        Set<String> trainerEmails = new HashSet<String>();
        Set<String> managerEmails = new HashSet<String>();
        Set<String> masterEmails = new HashSet<String>();
        String trainingMasterEmail = employeeService.findEmployeeByName(plan.getPlanCreator()).getAugEmail();
        masterEmails.add(trainingMasterEmail);
        List<ActualCourse> actualCourses = plan.getActualCourses();
        for (ActualCourse actualCourse : actualCourses) {
            getStaffEmailandManagerEmails(actualCourse.getCourseTrainer(), managerEmails, trainerEmails, request, isSendToManager);
        }
        generateEmailAddress(emailMap, trainerEmails, masterEmails, managerEmails);
    }



    /**
     * 
     * @param
     * @return
     * @throws ServerErrorException
     * @throws DataWarningException
     */
    private static void makePublicPlanTraineeAddress(Map<String, String> tempEmailMap, HttpServletRequest request, Plan plan, int isSendToManager)
            throws ServerErrorException, DataWarningException {
        String content = tempEmailMap.get(EmailConstant.EMAIL_CONTENT).replace(EmailConstant.INVITED_OR_PUBLIC_MEG_HOLDER,
                EmailConstant.PUBLIC_TRAINEE_MEG.replace(EmailConstant.PLAN_URL_HOLDER, EmailConstant.VIEW_PLAN_URL + plan.getPlanId())).replace(
                EmailConstant.END_MEG_HOLDER, "").replace(EmailConstant.CONTACT_HOLDER, EmailConstant.EMAIL_CONTACT_MSG);
        tempEmailMap.put(EmailConstant.EMAIL_CONTENT, content);
        String subject = tempEmailMap.get(EmailConstant.EMAIL_SUBJECT).replace(EmailConstant.PLAN_TYPE_HOLDER,
                FlagConstants.PUBLIC_PLAN.toLowerCase());
        tempEmailMap.put(EmailConstant.EMAIL_SUBJECT, subject);
        EmployeeService employeeService = BeanFactory.getEmployeeService();
        PlanEmployeeMapDao planEmployeeMapDao = BeanFactory.getPlanEmployeeMapDao();
        List<PlanEmployeeMap> specificTrainees = planEmployeeMapDao.getPlanEmployeeMaps(plan.getPlanId(), FlagConstants.ATTEND_TYPE_SPECIFIC,
                FlagConstants.UN_DELETED);

        Set<String> traineeEmails = new HashSet<String>();
        Set<String> managerEmails = new HashSet<String>();
        Set<String> masterEmails = new HashSet<String>();
        String trainingMasterEmail = employeeService.findEmployeeByName(plan.getPlanCreator()).getAugEmail();
        masterEmails.add(trainingMasterEmail);
        for (PlanEmployeeMap planEmployeeMap : specificTrainees) {
            getStaffEmailandManagerEmails(planEmployeeMap.getEmployee().getAugUserName(), managerEmails, traineeEmails, request, isSendToManager);
        }
        generateEmailAddress(tempEmailMap, traineeEmails, masterEmails, managerEmails);
    }

    /**
     * 
     * Get plan course and plan session by the plan, and generate email content.
     * 
     * @param plan
     * @param path
     * @return
     */
    private static Map<String, String> generateEmailContentByPlan(Plan plan, String path) {
        // Get email template
        Map<String, String> emailMap = getEmailReminder(path);
        // Make some rows data by plan courses
        String planCourseTrData = generateTRsDataByCourse(emailMap, plan.getActualCourses());
        // Make all course and session data
        String trData = planCourseTrData;
        makeEmailContent(emailMap, trData, EmailConstant.EMAIL_TRS_REPLACE);

        fillInPlanData(emailMap, plan);
        emailMap.remove(EmailConstant.EMAIL_TR_TEMPLATE);
        emailMap.remove(EmailConstant.EMAIL_PLAN_TR_TEMPLATE);
        return emailMap;
    }

    private static Map<String, String> fillInPlanData(Map<String, String> messageMap, Plan plan) {
        String content = messageMap.get(EmailConstant.EMAIL_CONTENT);
        content = content.replace(EmailConstant.MASTER_NAME_HOLDER, plan.getPlanCreator());
        String startTime = replaceNullDataToTBD(DateHandlerUtils.getEmailPatternDate(plan.getPlanExecuteStartTime()));
        String endTime = replaceNullDataToTBD(DateHandlerUtils.getEmailPatternDate(plan.getPlanExecuteEndTime()));

        if (EmailConstant.TO_BE_DETERMINED.equals(startTime) && EmailConstant.TO_BE_DETERMINED.equals(endTime)) {
            content = content.replace(EmailConstant.START_TIME_HOLDER, "").replace(EmailConstant.TILDE_HOLDER, "").replace(
                    EmailConstant.END_TIME_HOLDER, EmailConstant.TO_BE_DETERMINED);
        } else {
            content = content.replace(EmailConstant.START_TIME_HOLDER, startTime).replace(EmailConstant.TILDE_HOLDER, EmailConstant.TILDE).replace(
                    EmailConstant.END_TIME_HOLDER, endTime);
        }

        String subject = messageMap.get(EmailConstant.EMAIL_SUBJECT);
        subject = subject.replace(EmailConstant.MASTER_NAME_HOLDER, plan.getPlanCreator()).replace(EmailConstant.EMAIL_PLAN_NAME_REPLACE,
                plan.getPlanName());
        messageMap.put(EmailConstant.EMAIL_CONTENT, content);
        messageMap.put(EmailConstant.EMAIL_SUBJECT, subject);
        return messageMap;
    }

    /**
     * 
     * Make the email information for plan course
     * 
     * @param planSession
     * @return
     */
    private static Map<String, String> generateEmailsContentByActualCourse(Collection<ActualCourse> actualCourses) {
        Map<String, String> emailMap = getEmailReminder(EmailConstant.EMAIL_PLAN_COURSE_REMINDER_PATH);
        // Make some rows data by plan courses
        String trData = generateTRsDataByCourse(emailMap, actualCourses);
        return makeEmailContent(emailMap, trData, EmailConstant.EMAIL_TRS_REPLACE);
    }

    /**
     * 
     * Generate TRs data from course list for email content
     * 
     * @param emailMap
     * @param planCourses
     * @return
     */
    private static String generateTRsDataByCourse(Map<String, String> emailMap, Collection<ActualCourse> actualCourses) {
        StringBuilder builder = new StringBuilder();
        for (ActualCourse actualCourse : actualCourses) {
            String[] data = getActualCoursesDetailInfo(actualCourse);
            builder.append(generateOneTRData(emailMap, data));
        }
        return builder.toString();
    }

    /**
     * 
     * Make the plan course information
     * 
     * @param planCourse
     * @return
     */
    private static String[] getActualCoursesDetailInfo(ActualCourse actualCourse) {
        String startTime = DateHandlerUtils.dateToString(DateFormatConstants.YYYY_MM_DD_HH_MM, actualCourse.getCourseStartTime());
        String endTime = DateHandlerUtils.dateToString(DateFormatConstants.HH_MM, actualCourse.getCourseEndTime());
        String time = startTime + EmailConstant.MIDDLE_LINE + endTime;
        String[] data = new String[] { replaceNullDataToTBD(actualCourse.getCourseName()), replaceNullDataToTBD(actualCourse.getCourseRoomNum()),
                replaceNullDataToTBD(time), replaceNullDataToTBD(actualCourse.getCourseTrainer()) };
        return data;
    }

    /**
     * 
     * Generate one TR data one course for email content
     * 
     * @param emailMap
     * @param datas
     * @return
     */
    private static String generateOneTRData(Map<String, String> emailMap, String[] datas) {
        // Replace TR template data
        String trTemplate = emailMap.get(EmailConstant.EMAIL_TR_TEMPLATE);
        trTemplate = trTemplate.replace(EmailConstant.EMAIL_COURSE_NAME_REPLACE, datas[0]).replace(EmailConstant.EMAIL_ROOM_REPLACE, datas[1])
                .replace(EmailConstant.EMAIL_TIME_REPLACE, datas[2]).replace(EmailConstant.EMAIL_TRAINER_REPLACE, datas[3]);
        return trTemplate;
    }

    /**
     * 
     * Make the email content
     * 
     * @param emailMap
     * @param data
     * @param replaceTrs
     * @return
     */
    private static Map<String, String> makeEmailContent(Map<String, String> emailMap, String data, String replaceTrs) {
        String emailBody = emailMap.get(EmailConstant.EMAIL_CONTENT);
        // Replace common data
        emailBody = emailBody.replace(EmailConstant.EMAIL_PROJECT_NAME_REPLACE, RemoteProperyConstants.APP_NAME).replace(replaceTrs, data);
        emailMap.put(EmailConstant.EMAIL_CONTENT, emailBody);
        return emailMap;
    }



    /**
     * 
     * @param
     * @return
     */
    private static void makeApplyLeaveCourseContent(Map<String, String> messageMap, ActualCourse actualCourse, Employee employee, String reason,
            String leaveOrBack) {
        String content = messageMap.get(EmailConstant.EMAIL_CONTENT);
        String subject = messageMap.get(EmailConstant.EMAIL_SUBJECT);
        content = content.replace(EmailConstant.EMAIL_PROJECT_NAME_REPLACE, RemoteProperyConstants.APP_NAME).replace(
                EmailConstant.LEAVE_EMPLOYEE_NAME, employee.getAugUserName()).replace(EmailConstant.COURSE_NAME_HOLDER, actualCourse.getCourseName())
                .replace(EmailConstant.EMAIL_PLAN_NAME_REPLACE, actualCourse.getPlan().getPlanName()).replace(EmailConstant.COURSE_URL_HOLDER,
                        EmailConstant.VIEW_COURSE_URL + actualCourse.getActualCourseId()).replace(EmailConstant.PLAN_URL_HOLDER,
                        EmailConstant.VIEW_PLAN_URL + actualCourse.getPlan().getPlanId()).replace(EmailConstant.CONTACT_HOLDER,
                        EmailConstant.EMAIL_CONTACT_MSG);
        if (leaveOrBack.equals(EmailConstant.LEAVE_FLAG_LEAVE)) {
            content = content.replace(EmailConstant.LEAVE_ACTION_HOLDER,EmailConstant.LEAVE_ACTION);
            if (reason.equals("")) {
                content = content.replace(EmailConstant.LEAVE_REASON_TIP, "").replace(EmailConstant.LEAVE_REASON, "<br>");
            } else {
                content = content.replace(EmailConstant.LEAVE_REASON_TIP, EmailConstant.REASON_TIP_CONTENT).replace(EmailConstant.LEAVE_REASON, reason);
            }
            subject = subject.replace(EmailConstant.LEAVE_ACTION_HOLDER, EmailConstant.LEAVE_ACTION);
        } else {
            content = content.replace(EmailConstant.LEAVE_REASON, "<br>").replace(EmailConstant.LEAVE_REASON_TIP, "").replace(
                    EmailConstant.LEAVE_ACTION_HOLDER, EmailConstant.BACK_ACTION);
            subject = subject.replace(EmailConstant.LEAVE_ACTION_HOLDER, EmailConstant.BACK_ACTION);
        }
        messageMap.put(EmailConstant.EMAIL_CONTENT, content);
        subject = subject.replace(EmailConstant.LEAVE_EMPLOYEE_NAME, employee.getAugUserName()).replace(EmailConstant.COURSE_NAME_HOLDER,
                actualCourse.getCourseName());
        messageMap.put(EmailConstant.EMAIL_SUBJECT, subject);
    }


    /**
     * 
     * @param
     * @return
     */
    private static void makeApplyLeavePlanEmailContent(Map<String, String> messageMap, Plan plan, Employee employee, String reason, String leaveOrBack) {
        String content = messageMap.get(EmailConstant.EMAIL_CONTENT);
        String subject = messageMap.get(EmailConstant.EMAIL_SUBJECT);
        content = content.replace(EmailConstant.EMAIL_PROJECT_NAME_REPLACE, RemoteProperyConstants.APP_NAME).replace(
                EmailConstant.LEAVE_EMPLOYEE_NAME, employee.getAugUserName()).replace(EmailConstant.EMAIL_PLAN_NAME_REPLACE, plan.getPlanName())
                .replace(EmailConstant.PLAN_URL_HOLDER, EmailConstant.VIEW_PLAN_URL + plan.getPlanId()).replace(EmailConstant.CONTACT_HOLDER,
                        EmailConstant.EMAIL_CONTACT_MSG);
        if (leaveOrBack.equals(EmailConstant.LEAVE_FLAG_LEAVE)) {
            content = content.replace(EmailConstant.LEAVE_ACTION_HOLDER,EmailConstant.LEAVE_ACTION);
            if (reason.equals("")) {
                content = content.replace(EmailConstant.LEAVE_REASON_TIP, "").replace(EmailConstant.LEAVE_REASON, "<br>");
            } else {
                content = content.replace(EmailConstant.LEAVE_REASON_TIP, EmailConstant.REASON_TIP_CONTENT).replace(EmailConstant.LEAVE_REASON, reason);
            }
            subject = subject.replace(EmailConstant.LEAVE_ACTION_HOLDER, EmailConstant.LEAVE_ACTION);
        } else {
            content = content.replace(EmailConstant.LEAVE_REASON, "<br>").replace(EmailConstant.LEAVE_REASON_TIP, "").replace(
                    EmailConstant.LEAVE_ACTION_HOLDER, EmailConstant.BACK_ACTION);
            subject = subject.replace(EmailConstant.LEAVE_ACTION_HOLDER, EmailConstant.BACK_ACTION);
        }
        messageMap.put(EmailConstant.EMAIL_CONTENT, content);
        subject = subject.replace(EmailConstant.LEAVE_EMPLOYEE_NAME, employee.getAugUserName()).replace(EmailConstant.EMAIL_PLAN_NAME_REPLACE,
                plan.getPlanName());
        messageMap.put(EmailConstant.EMAIL_SUBJECT, subject);

    }

    /**
     * get the trainer email and master email and report manager
     * 
     */
    private static void makeApplyLeaveEmailAddress(HttpServletRequest request, Employee employee, Map<String, String> messageMap,
            List<ActualCourse> actualCourses) throws ServerErrorException, DataWarningException {
        EmployeeService employeeService = BeanFactory.getEmployeeService();
        ActualCourseService actualCourseService = BeanFactory.getActualCourseService();
        String trainerEmail = "";
        String masterEmail = "";
        String managerEmail = "";
        String emailTo = "";
        String emailCc = "";
        for (ActualCourse actualCourse : actualCourses) {
            Employee trainee = employee;
            // Get report trainer for IAP
            if (null != actualCourse.getCourseTrainer() && "" != actualCourse.getCourseTrainer()) {
                Employee trainer = employeeService.findEmployeeByName(actualCourse.getCourseTrainer());
                trainerEmail = trainer.getAugEmail();
            }
            // Get report manager for IAP
            managerEmail = trainee.getManagerEmail();
            ActualCourse planCourse = actualCourseService.findActualCourseById(actualCourse.getActualCourseId());
            String masterName = planCourse.getPlan().getPlanCreator();
            masterEmail = employeeService.findEmployeeByName(masterName).getAugEmail();
            if (emailCc.indexOf(trainee.getAugEmail()) == -1) {
                emailCc = emailCc + trainee.getAugEmail() + ";";
            }
            if (emailTo.indexOf(trainerEmail) == -1) {
                emailTo = emailTo + trainerEmail + ";";
            }
            if (emailTo.indexOf(masterEmail) == -1) {
                emailTo = emailTo + masterEmail + ";";
            }
            if (emailTo.indexOf(managerEmail) == -1) {
                emailTo = emailTo + managerEmail + ";";
            }
        }
        messageMap.put(EmailConstant.EMAIL_TO, emailTo);
        messageMap.put(EmailConstant.EMAIL_CC, emailCc);
    }

    /*        ***************timer************************ */
    /**
     * 
     * Send email for trainees and trainer by plan course or session
     * 
     * @param id
     * @param type
     * @throws ServerErrorException
     * @throws DataWarningException
     */
    public static void sendEmailForTraineesAndTrainerByCourseId(int actualCourseId, int type) throws ServerErrorException, DataWarningException {
        Map<String, String> emailMap = null;
        List<Employee> trainees = null;
        ActualCourseService actualCourseService = BeanFactory.getActualCourseService();
//        PlanEmployeeMapService planEmployeeMapService = BeanFactory.getPlanEmployeeMapService();
        PlanService planService = BeanFactory.getPlanService();
        LeaveNoteService leaveNoteService = BeanFactory.getLeaveNoteService();
        EmployeeService employeeService = BeanFactory.getEmployeeService();
        IRemoteEmailService remoteEmailService = BeanFactory.getIRemoteEmailService();
        ActualCourse actualCourse = actualCourseService.getActualCourseById(actualCourseId);
        logger.info(" send reminder email for actual course name=[" + actualCourse.getCourseName()+"] id=[#"+actualCourseId+"]");
        // check whether the this email is necessary
        long remindInterval = actualCourse.getPlan().getReminderEmail() * 3600 * 1000;
        long timeBetweenPublishAndStart = DateHandlerUtils.getMillSecondsDiff2Date(actualCourse.getCourseStartTime(), actualCourse.getPlan()
                .getPlanPublishDate());
        if (remindInterval >= timeBetweenPublishAndStart) {
            logger.info("Do not send reminder email for actual course[#"+actualCourseId+"]because the publish time less than the reminder time");
            return;
        }
        List<ActualCourse> list = new ArrayList<ActualCourse>();
        list.add(actualCourse);
        // Generate course email content by session
        emailMap = generateEmailsContentByActualCourse(list);
        // Find all trainees by plan course id
        trainees = actualCourseService.getTraineesByActualCourseId(actualCourseId);

        Integer planId = actualCourse.getPlan().getPlanId();
        logger.info("send reminder email for plan course in plan[#" + planId +"]");
        Plan plan = planService.getPlanById(planId);
        Set<PlanEmployeeMap> planEmployeeMap = plan.getPlanEmployeeMapList();
        Set<PlanEmployeeMap> unDeletedEmployeeMapList = new HashSet<PlanEmployeeMap>();
        for (PlanEmployeeMap p : planEmployeeMap) {
            if (p.getPlanEmployeeIsDeleted() == FlagConstants.UN_DELETED) {
                unDeletedEmployeeMapList.add(p);
            }
        }
        /*   need the joined employees to record if someone is quit  */
        List<Employee> joinedEmployees = new ArrayList<Employee>();
        /*   need the optional employees to record if someone is in cc list  */
        List<Employee> optionalEmployees = new ArrayList<Employee>();
        /*   need the invited employees to record if someone is in to list  */
        List<Employee> invitedEmployees = new ArrayList<Employee>();
        for (PlanEmployeeMap p : unDeletedEmployeeMapList) {
            if (p.getPlanTraineeAttendType().equals(FlagConstants.ATTEND_TYPE_JOIN)) {
                joinedEmployees.add(p.getEmployee());
            }
            if (p.getPlanTraineeAttendType().equals(FlagConstants.ATTEND_TYPE_OPTIONAL)) {
                optionalEmployees.add(p.getEmployee());
            }
            if (p.getPlanTraineeAttendType().equals(FlagConstants.ATTEND_TYPE_INVITED)) {
                invitedEmployees.add(p.getEmployee());
            }
        }
        // check whether the trainee applies leave of this course
        List<Employee> leavedEmployees = new ArrayList<Employee>();
        List<Employee> quitedEmployees = new ArrayList<Employee>();
        List<Employee> onlyOptionalEmployees = new ArrayList<Employee>();
        for (Employee trainee : trainees) {
         // get the quit trainees
            if (plan.getPlanType().getPlanTypeId() == FlagConstants.PUBLIC_PLAN_ID && !joinedEmployees.contains(trainee)) {
                quitedEmployees.add(trainee);
            }else{
                //invited plan
                // check whether the trainee quit the course.
                LeaveNote leaveNote = leaveNoteService.getLeaveNoteByEmployeeIdAndCourseId(trainee.getEmployeeId(), actualCourse.getActualCourseId());
                if (leaveNote != null) {
                    leavedEmployees.add(trainee);
                }
                if(optionalEmployees.contains(trainee) && !invitedEmployees.contains(trainee)){
                    //add to cc list and remove from email to list
                    onlyOptionalEmployees.add(trainee);
                }
            }
        }
        trainees.removeAll(leavedEmployees);
        trainees.removeAll(quitedEmployees);
        trainees.removeAll(onlyOptionalEmployees);
        makeCourseTimerEmailConstent(emailMap, actualCourse);
        String emailTo = "";
        if(trainees != null && !trainees.isEmpty()){
            for (Employee employee : trainees) {
                if (emailTo.indexOf(employee.getAugEmail()) == -1) {
                    emailTo += employee.getAugEmail() + ";";
                }
            }
        }
        String emailCc = "";
        if(optionalEmployees != null && !optionalEmployees.isEmpty()){
            for (Employee employee : optionalEmployees) {
                if (emailCc.indexOf(employee.getAugEmail()) == -1) {
                    emailCc += employee.getAugEmail() + ";";
                }
            }
        }
        Employee trainer = employeeService.findEmployeeByName(actualCourse.getCourseTrainer());
        Employee master = employeeService.findEmployeeByName(actualCourse.getPlan().getPlanCreator());
        if (emailTo.indexOf(trainer.getAugEmail()) == -1) {
            emailTo += trainer.getAugEmail() + ";";
        }
        if(emailCc.indexOf(master.getAugEmail()) == -1){
            emailCc += master.getAugEmail();
        }
        emailMap.put(EmailConstant.EMAIL_TO, emailTo);
        emailMap.put(EmailConstant.EMAIL_CC, emailCc);
        emailMap.remove(EmailConstant.EMAIL_TR_TEMPLATE);
        remoteEmailService.sendEmail(emailMap);
    }

    /**
     * 
     * @param
     * @return
     */
    private static void makeCourseTimerEmailConstent(Map<String, String> emailMap, ActualCourse actualCourse) {
        Plan plan = actualCourse.getPlan();
        int remindTime = plan.getReminderEmail();
        String courseStartTime = DateHandlerUtils.dateToString(DateFormatConstants.HH_MM, actualCourse.getCourseStartTime());
        String subject = emailMap.get(EmailConstant.EMAIL_SUBJECT).replace(EmailConstant.COURSE_NAME_HOLDER, actualCourse.getCourseName());
        if (remindTime == 24) {
            subject = subject.replace(EmailConstant.COURSE_START_TIMR_HOLDER, courseStartTime + " tomorrow");
        } else {
            subject = subject.replace(EmailConstant.COURSE_START_TIMR_HOLDER, courseStartTime);
        }
        emailMap.put(EmailConstant.EMAIL_SUBJECT, subject);
        String content = emailMap.get(EmailConstant.EMAIL_CONTENT).replace(EmailConstant.PLAN_URL_HOLDER,
                EmailConstant.VIEW_PLAN_URL + plan.getPlanId());
        if (plan.getPlanType().getPlanTypeName().equals(FlagConstants.INVITED_PLAN)) {
            content = content.replace(EmailConstant.COURSE_ACTION_HOLDER, EmailConstant.COURSE_ACTION_LEAVE);
        } else {
            content = content.replace(EmailConstant.COURSE_ACTION_HOLDER, EmailConstant.COURSE_ACTION_QUIT);
        }
        if(content.contains(EmailConstant.TO_BE_DETERMINED)){
            content = content.replace(EmailConstant.CONTACT_HOLDER, EmailConstant.EXPLAIN_TBD + EmailConstant.EMAIL_CONTACT_MSG);
        }else{
            content = content.replace(EmailConstant.CONTACT_HOLDER, EmailConstant.EMAIL_CONTACT_MSG);
        }
        emailMap.put(EmailConstant.EMAIL_CONTENT, content);
    }
}
