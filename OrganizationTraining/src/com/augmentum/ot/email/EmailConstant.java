package com.augmentum.ot.email;

import com.augmentum.ot.dataObject.constant.ConfigureConstants;
import com.augmentum.ot.util.PropertyUtils;

public class EmailConstant {
    /** timer period to send email automatically, 1 minutes **/
    public static final long EMAIL_TIMER = 60000;

    public static final String EMAIL_FORM = "from";
    public static final String EMAIL_TO = "to";
    public static final String EMAIL_CC = "cc";
    public static final String EMAIL_BCC = "bcc";
    public static final String EMAIL_SUBJECT = "subject";
    public static final String EMAIL_CONTENT = "content";

    public static final String EMAIL_TR_TEMPLATE = "trTemplate";
    public static final String EMAIL_PLAN_TR_TEMPLATE = "planTrTemplate";
    public static final String EMAIL_PROJECT_NAME_REPLACE = "%projectName%";
    public static final String EMAIL_COURSE_NAME_REPLACE = "%CourseName%";
    public static final String EMAIL_ROOM_REPLACE = "%Room%";
    public static final String EMAIL_TIME_REPLACE = "%Time%";
    public static final String EMAIL_URL_REPLACE = "%URL%";
    public static final String EMAIL_TRAINER_REPLACE = "%Trainer%";
    public static final String EMAIL_MESSAGE_REPLACE = "%message%";
    public static final String EMAIL_TRS_REPLACE = "%TRs%";
    public static final String EMAIL_NEW_TRS_REPLACE = "%newTRs%";
    public static final String EMAIL_PLAN_TRS_REPLACE = "%plan_trs%";
    public static final String EMAIL_PLAN_NAME_REPLACE = "%planName%";
    public static final String EMAIL_PLAN_TAG_REPLACE = "%Tag%";
    public static final String EMAIL_PLAN_DATE_REPLACE = "%Date%";
    public static final String EMAIL_PLAN_CREATOR_REPLACE = "%planMaster%";
    public static final String EMAIL_CHANGED_TRS_STYLE_REPLACE = "%className%";
    public static final String EMAIL_ROLE_REPLACE = "%employeeRole%";
    public static final String EMAIL_CHANGED_TRS_STYLE = "changed";
    public static final String EMAIL_URL = PropertyUtils.readValue(ConfigureConstants.OT_CONF_FILE, "email.url");
    public static final String VIEW_PLAN_URL = PropertyUtils.readValue(ConfigureConstants.OT_CONF_FILE, "view.plan.url");
    public static final String VIEW_COURSE_URL = PropertyUtils.readValue(ConfigureConstants.OT_CONF_FILE, "view.course.url");
    public static final String EMAIL_PLAN_COURSE_REMINDER_PATH = "emailTemplate/Course_Timer_Email_Reminder.xml";
    public static final String EMAIL_UPDATE_PLAN_REMINDER_PATH = "emailTemplate/Update_Plan_Email_Reminder.xml";
    public static final String EMAIL_DELETE_EMPLOYEE_REMINDER_PATH = "emailTemplate/Delete_Employee_Email_Reminder.xml";
    public static final String EMAIL_CANCEL_PLAN_REMINDER_PATH = "emailTemplate/Cancel_Plan_Email_Reminder.xml";
    public static final String EMAIL_LEAVE_COURSE_REMINDER_PATH = "emailTemplate/Leave_Course_Email_Reminder.xml";
    public static final String EMAIL_LEAVE_PLAN_REMINDER_PATH = "emailTemplate/Leave_Plan_Email_Reminder.xml";
    public static final String EMAIL_PUBLISH_PLAN_REMINDER_PATH = "emailTemplate/Publish_Plan_Email_Reminder.xml";

    public static final String EMAIL_TRAINEE_MESSAGE = "The following training will happen soon, please attend on time";
    public static final String EMAIL_TRAINER_MESSAGE = "You are invited as a trainer, please prepare course training on time.";
    public static final String EMAIL_DELETE_MESSAGE = "Sorry, you needn't attend following training, please understanding.";
    public static final String EMAIL_CANCEL_MESSAGE = "Beacuse plan has been canceled, You needn't following training.";
    public static final String EMAIL_INVITED_TRAINEE_MESSAGE = "You are invited as a invited trainee, please attend following course on time.";
    public static final String EMAIL_OPTIONAL_TRAINEE_MESSAGE = "You are invited as a optional trainee, you can attend following course if you interested.";
    public static final String EMAIL_SPECIFIC_TRAINEE_MESSAGE = "You are invited as a specific trainee, you can join this public plan.";

    public static final String TEST_TRAINEE_EMAIL = "hectorhe@augmentum.com.cn";

    public static final String TEST_MANAGER_EMAIL = "jacksonmu@augmentum.com.cn";

    public static final String TEST_TRAINER_EMAIL = "sherryzhang@augmentum.com.cn";

    // apply leave constants
    public static final String LEAVE_COURSE_MESSAGE = " has applid leave for the following course, the course is in plan: ";
    public static final String BACK_COURSE_MESSAGE = " has canceled his leave for the following course, the course is in plan: ";
    public static final String LEAVE_PLAN_MESSAGE = " has applid leave for the following plan so as the courses in the plan";
    public static final String BACK_PLAN_MESSAGE = " has canceled his leave for the following plan so as the courses in the plan";
    public static final String LEAVE_PLAN_FLAG = "plan";
    public static final String LEAVE_COURSE_FLAG = "course";
    public static final String LEAVE_NULL_MESSAGE = "";
    public static final String LEAVE_PLAN_INFO = "planInfo";
    public static final String LEAVE_COURSE_INFO = "courseInfo";
    public static final String LEAVE_PLAN_INFO_HOLDER = "%planInfoHolder%";
    public static final String LEAVE_COURSE_INFO_HOLDER = "%courseInfoHolder%";
    public static final String LEAVE_EMAIL_TITLE = "%title%";
    public static final String LEAVE_EMAIL_MESSAGE = "%leaveMessage%";
    public static final String LEAVE_PLAN_NAME = "%planName%";
    public static final String LEAVE_EMPLOYEE_NAME = "%employeeName%";
    public static final String LEAVE_REASON_TIP = "%reasonTip%";
    public static final String LEAVE_REASON_TIP_MESSAGE = "And his(her) reason is: ";
    public static final String LEAVE_REASON = "%reason%";
    public static final String LEAVE_FLAG_LEAVE = "leave";
    public static final String LEAVE_FLAG_BACK = "back";
    public static final String LEAVE_SUBJECT = "[OrganizationTraining] leave application";
    public static final String BACK_SUBJECT = "[OrganizationTraining] cancel leave ";

    public static final String PUBLIC_TRAINEE_MEG = "You are welcome to join the plan. If any courses you are interested, please click <a href='%planDetailUrl%'>here</a> to register.";
    public static final String EMAIL_CONTACT_MSG = "If you find any issues with the Organization Training system, please feel free to contact with Eriksson Luo (eriksson.luo@augmentum.com ext 557) or Hansen Ouyang (hansenouyang@augmentum.com.cn).";
    public static final String REASON_TIP_CONTENT = "Here is the reason: ";
    public static final String CONTACT_HOLDER = "%contactMsg%";
    public static final String PLAN_URL_HOLDER = "%planDetailUrl%";
    public static final String COURSE_URL_HOLDER = "%courseDetailUrl%";
    public static final String INVITED_OR_PUBLIC_MEG_HOLDER = "%invitedOrPublicMsg%";
    public static final String END_MEG_HOLDER = "%endMsg%";
    public static final String INVITED_PLAN_MSG = "You are invited to attend the plan as trainers or trainees.";
    public static final String END_MSG = "<BR>To those in receiver list: if there is any course you cannot attend, please apply leave in system in advance.<BR>To those in cc list: you are optional to this plan, you may attend any course if you are interested.<BR>";
    public static final String PLAN_TYPE_HOLDER = "%planType%";
    public static final String LEAVE_ACTION_HOLDER = "%leaveAction%";
    public static final String COURSE_NAME_HOLDER = "%courseName%";
    public static final String LEAVE_ACTION = "applied leave";
    public static final String BACK_ACTION = "canceled leave application";
    public static final String NO_REASON = "<em>No reason provided.</em>";
    public static final String MASTER_NAME_HOLDER = "%masterName%";
    public static final String START_TIME_HOLDER = "%startTime%";
    public static final String END_TIME_HOLDER = "%endTime%";
    public static final String COURSE_ACTION_HOLDER = "%courseAction%";
    public static final String COURSE_ACTION_LEAVE = "apply leave";
    public static final String COURSE_ACTION_QUIT = "quit";
    public static final String COURSE_START_TIMR_HOLDER = "%startTime%";

    public static final String ONE_COURSE_LINE = "<TR><TD><FONT face=Arial size=2>%courseName%</FONT></TD>"
            + "<TD><FONT face=Arial size=2>%room%</FONT></TD><TD><FONT face=Arial size=2>%time%</FONT></TD>"
            + "<TD><FONT face=Arial size=2>%trainer%</FONT></TD></TR>";
    public static final String COURSE_ROOM_HOLDER = "%room%";
    public static final String MIDDLE_LINE = " - ";
    public static final String TILDE = "~";
    public static final String TILDE_HOLDER = "%tilde%";
    public static final String COURSE_TIME_HOLDER = "%time%";
    public static final String COURSE_TRAINER_HOLDER = "%trainer%";
    public static final String HIGHLIGHT_PREFIX = "<FONT face=Arial size=2 style='color:blue;'>";
    public static final String HIGHLIGHT_SUFFIX = "</FONT>";

    public static final String TO_BE_DETERMINED = "TBD";
    public static final String HIGHLIGHT_TBD = HIGHLIGHT_PREFIX + TO_BE_DETERMINED + HIGHLIGHT_SUFFIX;
    public static final String HIGHLIGHT_TBD_TIME = HIGHLIGHT_TBD + MIDDLE_LINE + MIDDLE_LINE;
    public static final String TBD_TIME = TO_BE_DETERMINED + MIDDLE_LINE + TO_BE_DETERMINED;
    public static final String HIGHLIGHT_TBD_NEW_COURSE_TIME = HIGHLIGHT_PREFIX + TBD_TIME + HIGHLIGHT_SUFFIX;

    public static final String EXPLAIN_TBD = "TBD : To be determined.<BR>";

}
