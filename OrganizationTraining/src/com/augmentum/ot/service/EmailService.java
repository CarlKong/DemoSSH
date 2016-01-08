package com.augmentum.ot.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.augmentum.ot.dataObject.EmailSendCondition;
import com.augmentum.ot.exception.DataWarningException;
import com.augmentum.ot.exception.ServerErrorException;
import com.augmentum.ot.model.ActualCourse;
import com.augmentum.ot.model.Employee;
import com.augmentum.ot.model.Plan;

/**
 * @ClassName: EmailService
 * @date 2013-2-16
 * @version V1.0
 */
public interface EmailService {

    void sendEmailByCancelPlan(HttpServletRequest request, Plan plan, EmailSendCondition emailSendCondition)
            throws ServerErrorException, DataWarningException;

    void sendEmailByPublishPlan(HttpServletRequest request, Plan plan, EmailSendCondition emailSendCondition)
            throws ServerErrorException, DataWarningException;

    void sendEmailByTraineeLeaveOrBackCourse(HttpServletRequest request, Employee employee, ActualCourse actualCourse,
            String leaveFlag, String leaveCourseReason) throws ServerErrorException, DataWarningException;

    void sendEmailByTraineeLeaveOrBackPlan(HttpServletRequest request, Employee employee, Plan plan,
            List<ActualCourse> needSendEmailCourseList, String leaveFlag, String leavePlanReason)
            throws ServerErrorException, DataWarningException;

    void sendEmailByUpdatePlan(HttpServletRequest request, Plan oldPlan, Plan newPlan,
            EmailSendCondition emailSendCondition) throws ServerErrorException, DataWarningException;

}
