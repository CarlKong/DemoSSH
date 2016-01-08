<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib  prefix="s" uri="/struts-tags"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Create Plan</title>
<link type="text/css" href="<%=basePath %>css/plan/planContent.css" rel="stylesheet">
<link type="text/css" href="<%=basePath %>autocomplete/css/autocomplete.css" rel="stylesheet">
</head>
<body>
	<div class="content_layout_line" id="title_plan_content">
            <div class="line_content" >
                <div class="line_content_left" id="plan_name_title">
                    <span class="line_content_span" >
                        <s:text name="_plan_name"></s:text>
                    </span>
                </div>
                <div class="line_content_right">
                    <input id="planName" class="input_txt" name="plan.planName" type="text" value="${plan.planName}">
                </div>
           </div>
    </div>
    <div class="content_layout_line" >
            <div id="plan_brief_height" class="line_content" >
                <div class="line_content_left" id="plan_brief_title"  >
                    <span class="line_content_span" >
                        <s:text name="_plan_brief"></s:text>
                    </span>
                </div>
                <div id="planBriefDiv" class="line_content_right">
                    <textarea id="planBriefEditor" name="plan.planBrief">${plan.planBrief}</textarea>
                </div>
                <input type="hidden" id="planBriefWithoutTag" name="plan.planBriefWithoutTag"></input>
          </div>
    </div>
   <div class="content_layout_line" >
            <div class="line_content" id="line_content_tag" >
                <div class="line_content_left" >
                    <span class="line_content_span" >
                        <s:text name="_course_tag"></s:text>
                    </span>
                </div>
                <div class="line_content_right">
                	<input type="hidden" id="planTag_value" value="${plan.planCategoryTag}"/>
                	<div id="planTagDiv">
                		<input id="planCategoryTag" class="input_txt" type="hidden" name="plan.planCategoryTag" value="${plan.planCategoryTag }">
                	</div>
                    <span id="line_tag_notice" class="notice_font" ><s:text name="_common_tag_notice"></s:text></span>
                    <div id="common_tag_div" ><span id="common_tag_span"><s:text name="_common_tag"></s:text></span>
                        <span id="categoryTextPlan"></span>
                    </div>
                </div>
            </div>
    </div>
	<div class="plan_type">
		<div class="plan_type_content">
			<span class="plan_tag"><s:text name="_plan_type"></s:text></span>
			<span class="plan_type_item" id="planTypeId">
			</span>
		</div>
	</div>
	<div id="plan_invited_trainees">
		<div class="trainee_title">
			<span class="invited_trainee_name"><s:text name="_plan_trainee"></s:text></span>
			<span class="invited_trainee_name_notice"><s:text name="_plan_notice_invited"></s:text></span>
			<span class="line_content_span"><s:text name="_colon"></s:text></span>
		</div>
		<div class="invited_trainee_input">
			<textarea id="invited_trainee" class="invited_trainee_input_blank" name="invitedTrainees" >${invitedTrainees}</textarea>
		</div>
		<div class="display_trainee_notice"><s:text name="_trainee_name_notice"></s:text> </div>
		<div class="trainee_title">
			<span class="option_trainee_name"><s:text name="_plan_trainee"></s:text></span>
			<span class="option_trainee_name_notice"><s:text name="_plan_notice_option"></s:text></span>
			<span class="line_content_span"><s:text name="_colon"></s:text></span>
		</div>
		<div class="option_trainee_input">
			<textarea id="option_trainee" class="option_trainee_input_blank" name="optionalTrainees">${optionalTrainees}</textarea>
		</div>
		<div class="display_trainee_notice"><s:text name="_trainee_name_notice"></s:text> </div>
		<div class="reminder_email"><s:text name="_plan_reminder_email"></s:text></div>
		<div class="reminder_time" id="invitedReminderTimeId">
			<span class="reminder_time1">
				<span class="reminder_time_image" id="invitedTimeOneDay"></span>
				<span class="reminder_time_title" id="reminderTimeTitle1">1 <s:text name="_plan_day"></s:text></span>
			</span>
			<span class="reminder_time1">
				<span class="reminder_time_image" id="invitedTimeFourHours"></span>
				<span class="reminder_time_title" id="reminderTimeTitle2">4 <s:text name="_plan_hours"></s:text></span>
			</span>
			<span class="reminder_time1">
				<span class="reminder_time_image" id="invitedTimeTwoHours"></span>
				<span class="reminder_time_title" id="reminderTimeTitle3" >2 <s:text name="_plan_hours"></s:text></span>
			</span>
			<span class="reminder_time1">
				<span class="reminder_time_image" id="invitedTimeOneHour"></span>
				<span class="reminder_time_title" id="reminderTimeTitle4">1 <s:text name="_plan_hour"></s:text></span>
			</span>
			<span class="reminder_time1">
				<span class="reminder_time_image" id="invitedTimeNo"></span>
				<span class="reminder_time_title" id="reminderTimeTitle5"><s:text name="_plan_hour_no"></s:text></span>
			</span>
		</div>
	</div>
	<div id="plan_public_trainees">
		<div class="trainee_title">
			<span class="trainee_name"><s:text name="_plan_public_trainee"></s:text> </span>
		</div>
		<div class="trainee_scop" id="traineeScopeId">
			<span>
				<span class="trainee_all_image" id="isallEmployeeId"></span>
				<span class="trainee_all_title"><s:text name="_plan_all_trainee"></s:text></span>
			</span>
			<span>
				<span class="trainee_all_image" id="sqecEmployeeId"></span>
				<span class="trainee_spec_title" ><s:text name="_plan_specific_trainee"></s:text></span>
			</span>
		</div>
		<div class="trainee_input">
			<textarea id="specific_trainee" class="trainee_input_blank" name="specificTrainees">${specificTrainees}</textarea>
		</div>
		<div class="trainee_notice"><s:text name="_trainee_name_notice"></s:text> </div>
		<div class="trainee_title">
			<span class="trainee_name"><s:text name="_plan_register_notice"></s:text></span>
		</div>
		<div class="register_notic" id="registerNoticeId">
			<span>
				<span class="register_notic_image" id="registerNoticeYes"></span>
				<span class="register_notic_title"><s:text name="_plan_yes"></s:text></span>
			</span>
			<span>
				<span class="register_notic_image" id="registerNoticeNo"></span>
				<span class="register_notic_no_title"><s:text name="_plan_hour_no"></s:text></span>
			</span>
		</div>
		<div class="reminder_email"><s:text name="_plan_reminder_email"></s:text></div>
		<div class="reminder_time" id="publicReminderTimeId">
			<span class="reminder_time1">
				<span class="reminder_time_image" id="publicTimeOneDay"></span>
				<span class="reminder_time_title" id="publicReminderTime1">1 <s:text name="_plan_day"></s:text></span>
			</span>
			<span class="reminder_time1">
				<span class="reminder_time_image" id="publicTimeFourHours"></span>
				<span class="reminder_time_title" id="publicReminderTime2">4 <s:text name="_plan_hours"></s:text></span>
			</span>
			<span class="reminder_time1">
				<span class="reminder_time_image" id="publicTimeTwoHours"></span>
				<span class="reminder_time_title" id="publicReminderTime3">2 <s:text name="_plan_hours"></s:text></span>
			</span>
			<span class="reminder_time1">
				<span class="reminder_time_image" id="publicTimeOneHours"></span>
				<span class="reminder_time_title" id="publicReminderTime4">1 <s:text name="_plan_hour"></s:text></span>
			</span>
			<span class="reminder_time1">
				<span class="reminder_time_image" id="publicTimeNo"></span>
				<span class="reminder_time_title" id="publicReminderTime5"><s:text name="_plan_hour_no"></s:text></span>
			</span>
		</div>
	</div>
	
	<div id="plan_assessment" class="content_layout_line">
            <div class="line_content" >
                <div class="line_content_left">
                    <span class="line_content_span" >
                        <s:text name="_plan_assessment"></s:text>
                    </span>
                </div>
                <div class="line_content_right">
                	<div id="plan_assessment_radios" class="common_radio">
                	    <input type="hidden" id="planNeedAssessmentFlag" value="${plan.needAssessment}"></input>
                		<input id="enableAssess" class="radio_type_input" type="radio" name="plan.needAssessment" value="1">
                		<span class="assessSpan"><s:text name="_plan_assess_enable"></s:text> </span>
                		<input id="disableAssess" class="radio_type_input" type="radio" name="plan.needAssessment" value="0">
                		<span class="assessSpan"><s:text name="_plan_assess_disable"></s:text></span>
                	</div>
                	<div id="assess_notice" class="notice_font"><s:text name="_plan_assess_notice"></s:text></div>
                </div>
           </div>
    </div>
    
	<div class="plan_course_list_title">
		<span class="plan_tag"><s:text name="_plan_course_list"></s:text></span>
		<span class="plan_course_list_url">
			<a id="selectCoursePlan" href="javascript:void(0)"><s:text name="_select_courses"></s:text> </a>
			<span id="sessionPart">
				<span>|</span>
				<a id = "addPlanSession" ><s:text name = "_add_temporary_session" /></a>
			</span>
		</span>
		<a id="orderButton" class="a_common_button"><s:text name="_sort_by_time_button" /></a>
	</div>
	<div class="plan_course_list">
		<div id="planCourseList">
            <div class="courseListHead">
    			<span class="headLeft"></span>
    			<span class="sortTitle titleBackGroud"></span>
    			<span class="noLabel titleBackGroud"></span>
    			<span class="nameLabel titleBackGroud"><s:text name="_courseList_title_name"></s:text></span>
    			<span class="roomLabel titleBackGroud"><s:text name="_courseList_title_room"></s:text></span>
    			<span class="dateTitleLabel titleBackGroud"><s:text name="_courseList_title_date"></s:text></span>
    			<span class="startLabel titleBackGroud"><s:text name="_courseList_title_start"></s:text></span>
    			<span class="finishLabel titleBackGroud"><s:text name="_courseList_title_finish"></s:text></span>
    			<span class="durationLabel titleBackGroud"><s:text name="_courseList_title_dur"></s:text></span>
    			<span class="trainerLabel titleBackGroud"><s:text name="_courseList_title_trainer"></s:text></span>
    			<span class="actionLabel titleBackGroud"><s:text name="_courseList_title_action"></s:text></span>
    			<span class="headRight"></span>
    			<div class="clear"></div>
			</div>
		</div>
	</div>
	
	<div class="plan_attachment_list_title">
		<span class="plan_tag"> <s:text name="_plan_attachment"></s:text></span>
		<span id="plan_attachment_list_url" class="plan_attachment_list_url"><s:text name="_add_attachment"></s:text> </span>
	</div>
	<div id="plan_attachment_list" class="plan_attachment_list">
		<s:hidden id="plan_attachmentsJson" name="attachmentsJson"/>
	</div>
	<input type="hidden" name="plan.reminderEmail" id="planReminderEmail" value="${plan.reminderEmail}"/>
	<input type="hidden" name="plan.planType.planTypeId" id="hidePlanTypeId" value="${plan.planType.planTypeId}"/>
	<input type="hidden" name="plan.isAllEmployee" id="hideIsAllEmployee" value="${plan.isAllEmployee}"/>
	<input type="hidden" name="plan.registerNotice" id="hideRegisterNoticeId" value="${plan.registerNotice}"/>
	<s:hidden name="actualCoursesJson" id="actualCourseJsonStr" ></s:hidden>
	<s:hidden name="delActualCourseIds" id="delActualCourseIds" ></s:hidden>
</body>
</html>
