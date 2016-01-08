<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title><s:text name="_view_plan_title"></s:text></title>
<link type="text/css" href="<%=basePath %>css/common/common.css" rel="stylesheet">
<link type="text/css" href="<%=basePath %>css/plan/viewPlan.css" rel="stylesheet">
<link type="text/css" href="<%=basePath %>jquery.poshytip/css/tip-green.css" rel="stylesheet">
<link type="text/css" href="<%=basePath %>attachmentUI/csslib/attachmentUI.css" rel="stylesheet" />
<link rel="stylesheet" href="<%=basePath%>messageBar/css/messagebar.css" type="text/css">
<link rel="stylesheet" href="<%=basePath%>jquery.ui/css/jquery-ui-1.8.18.custom.css" type="text/css">
<link rel="stylesheet" href="<%=basePath%>confirmBar/css/confirmDialog.css" type="text/css" >
<link type="text/css"  href="<%=basePath %>css/assessment/assessmentCommon.css" rel="stylesheet"/>
<link type="text/css" href="<%=basePath %>validanguage/css/validanguage.css" rel="stylesheet">
<link rel="stylesheet" href="<%=basePath%>scrollbar/css/custom-scrollbar.css">
</head>
<body>
<!-- info from search page -->
<s:form action="" theme="simple" id="preornext">
	<s:hidden name="fromSearchToViewCondition.nowId" value="%{plan.planId}"></s:hidden>
	<s:hidden name="fromSearchToViewCondition.backupId"></s:hidden>
	<s:hidden name="fromSearchToViewCondition.totalPageNum"></s:hidden>
	<s:hidden name="fromSearchToViewCondition.isNoSelectedflag"></s:hidden>
	<s:hidden name="criteria.pageNum"></s:hidden>
	<s:hidden name="criteria.pageSize"></s:hidden>
	<s:hidden name="criteria.sortSign"></s:hidden>
	<s:hidden name="criteria.sortName"></s:hidden>
	<s:hidden name="criteria.queryString"></s:hidden>
	<s:hidden name="criteria.searchFields"></s:hidden>
	<s:hidden name="criteria.planTypeIds"></s:hidden>
	<s:hidden name="criteria.planCourseTypeIds"></s:hidden>
	<s:hidden name="criteria.publishLowerDate"></s:hidden>
	<s:hidden name="criteria.publishUpperDate"></s:hidden>
	<s:hidden name="criteria.executeLowerDate"></s:hidden>
	<s:hidden name="criteria.executeUpperDate"></s:hidden>
	<s:hidden name="criteria.searchOperationFlag"></s:hidden>
	<s:hidden name="forChangeLocaleFlag" id="forChangeLocaleFlag" ></s:hidden>
</s:form>
<jsp:include page="/WEB-INF/common/header.jsp"></jsp:include>
<input type="hidden" id="plan_id" value="${plan.planId}"></input>
<input type="hidden" id="plan_prefixIDValue" value="${plan.prefixIDValue}"></input>
<input type="hidden" id="operationFlag" value="${operationFlag}"></input>
<input type="hidden" id="planIsPublished" value="${plan.planIsPublish}"></input>
<!-- message bar -->
<div id ="messageBar">
	<span id="messageBar_create" class="message_span"><s:text name="_plan_messageBar_create"></s:text></span>
	<span id="messageBar_publish" class="message_span"><s:text name="_plan_messageBar_publish"></s:text></span>
</div>
<!-- content -->
<div class="content">
<div class="view_plan_details">
   <s:if test="fromSearchToViewCondition!=null">
   <div class="first_content_layout_line" >
       <div class="first_line_content" >
           <div class="first_line_content_left" >
               <s:if test="%{criteria.pageNum<=1&&previousFlag==-1}">
               <a class="first_other_a"><span class="line_span_details" >
                   <s:text name="_course_previous"></s:text>
               </span></a>
               </s:if><s:else> 
               <a class="first_a plan_previous" href="javascript:void(0);" ><span class="line_span_details" >
                   <s:text name="_course_previous"></s:text>
               </span></a>
               </s:else>
               <span class="first_bar">|</span>
               <s:if test="%{criteria.pageNum>=fromSearchToViewCondition.totalPageNum&&nextFlag==1}">
               <a class="first_other_a"><span class="line_span_details" >
                   <s:text name="_course_next"></s:text>
               </span></a>
               </s:if><s:else>
               <a class="first_a plan_next" href="javascript:void(0);" ><span class="line_span_details" >
                   <s:text name="_course_next"></s:text>
               </span></a>
               </s:else>
           </div>
           <div class="first_line_content_right">
               <a class="first_a plan_back_to_search" href="javascript:void(0);" ><span class="line_span_details">
                   <s:text name="_course_back_to_search"></s:text>
               </span></a>
           </div>
       </div>
       <div class="first_line"></div>
    </div>
    </s:if>
	<div id="view_plan_id">
		<div id="view_plan_id_left">
			<div class="line_content_left">
				<span class="line_content_span" >
                        <s:text name="_plan_id"></s:text>
                </span>
			</div>
			<div class="line_content_right" id="view_plan_id_left_value">
				<span>${plan.prefixIDValue}</span>
			</div>
		</div>
		<div id="view_plan_id_right">
			<a class="a_common_button" id="action">
                <span id="action_span"><s:text name="_btn_action"></s:text></span>
            </a>
            <div id="arrow_tip">
	            <a class="a_common_button" id="action_active_launch">
	            </a>
	            <a class="a_common_button" id="action_active_shrink">
	            </a>
            </div>
            <div id="action_part">
                
            </div>
		</div>
	</div>
	<div class="content_layout_line">
		<div class="line_content">
			<div class="line_content_left">
				<span class="line_content_span" >
                        <s:text name="_plan_name"></s:text>
                </span>
			</div>
			<div class="line_content_right">
				<span id="planName">${plan.planName}</span>
			</div>
		</div>
	</div>
	<div class="content_layout_line">
		<div class="line_content">
			<div class="line_content_left">
				<span class="line_content_span" >
                        <s:text name="_plan_brief"></s:text>
                </span>
			</div>
			<div class="line_content_right">
				<span id="brief">${plan.planBrief}</span>
			</div>
		</div>
	</div>
	<div class="content_layout_line">
		<div class="line_content">
			<div class="line_content_left">
				<span class="line_content_span" >
                        <s:text name="_plan_tag"></s:text>
                </span>
			</div>
			<div class="line_content_right">
				<span>${plan.planCategoryTag}</span>
			</div>
		</div>
	</div>
	<div class="content_layout_line">
		<div class="line_content">
			<div class="line_content_left">
				<span class="line_content_span" >
                        <s:text name="_plan_type"></s:text>
                </span>
			</div>
			<div class="line_content_right">
				<input type="hidden"  id="plan_type" value="${plan.planType.planTypeName}">
				<span>${plan.planType.planTypeName}</span>
			</div>
		</div>
	</div>
	<div id="invitedContent">
		<div class="content_layout_line">
			<div class="line_content">
				<div class="line_content_left">
	                <span class="line_content_span"><s:text name="_plan_trainee"></s:text></span>
					<span class="trainee_type"><s:text name="_plan_notice_invited"></s:text></span>
					<span class="line_content_span"><s:text name="_colon"></s:text></span>
				</div>
				<div class="line_content_right">
					<input type="hidden" id="plan_invited_trainees" value="${invitedTrainees}"></input>
					<span>${invitedTrainees}</span>
				</div>
			</div>
		</div>
		<div class="content_layout_line">
			<div class="line_content">
				<div class="line_content_left">
	                <span class="line_content_span"><s:text name="_plan_trainee"></s:text></span>
					<span class="trainee_type"><s:text name="_plan_notice_option"></s:text></span>
					<span class="line_content_span"><s:text name="_colon"></s:text></span>
				</div>
				<div class="line_content_right">
					<span>${optionalTrainees}</span>
				</div>
			</div>
		</div>
	</div>
	<div id="publichContent">
		<div class="content_layout_line">
			<div class="line_content">
			<s:if test="plan.isAllEmployee == 1">
				<input type="hidden" class="isAllEmployee" value="1"></input>
				<div class="line_content_left">
	                <span class="line_content_span"><s:text name="_plan_public_trainee"></s:text></span>
				</div>
				<div class="line_content_right">
					<span><s:text name="_plan_all_trainee"/></span>
				</div>
			</s:if>
			<s:if test="plan.isAllEmployee == 0">
				<input type="hidden" class="isAllEmployee" value="0"></input>
				<div class="line_content_left">
	                <span class="line_content_span"><s:text name="_plan_trainee"></s:text></span>
	                <span class="trainee_type"><s:text name="_plan_notice_specific"></s:text></span>
	                <span class="line_content_span"><s:text name="_colon"></s:text></span>
				</div>
				<div class="line_content_right">
					<input type="hidden" id="plan_specific_trainees" value="${specificTrainees}"></input>
					<span>${specificTrainees}</span>
				</div>
			</s:if>
			</div>
		</div>
		<div class="content_layout_line">
			<div class="line_content">
				<div class="line_content_left">
	                <span class="line_content_span"><s:text name="_plan_register_notice"></s:text></span>
				</div>
				<div class="line_content_right">
					<s:if test="plan.registerNotice == 0">
						<span><s:text name="_plan_hour_no"/></span>
					</s:if>
					<s:if test="plan.registerNotice == 1">
						<span><s:text name="_plan_yes"/></span>
					</s:if>
				</div>
			</div>
		</div>
	</div>
	<div class="content_layout_line">
		<div class="line_content">
			<div class="line_content_left">
				<span class="line_content_span" >
                        <s:text name="_plan_remind_time"></s:text>
                </span>
			</div>
			<div class="line_content_right">
				<s:if test="plan.reminderEmail == 0">
					<span><s:text name="_plan_hour_no"/></span>
				</s:if>
				<s:if test="plan.reminderEmail == 1">
					<span>1 <s:text name="_plan_hour"/></span>
				</s:if>
				<s:if test="plan.reminderEmail > 1 && plan.reminderEmail < 24">
					<span>${plan.reminderEmail} <s:text name="_plan_hours"/></span>
				</s:if>
				<s:if test="plan.reminderEmail == 24">
					<span>1 <s:text name="_plan_day"/></span>
				</s:if>
			</div>
		</div>
	</div>
	<div class="content_layout_line">
		<div class="line_content">
			<div class="line_content_left">
				<span class="line_content_span">
					<s:text name="_plan_assessment"></s:text>
				</span>
			</div>
			<div class="line_content_right">
				<s:if test="plan.needAssessment == 0">
					<span><s:text name="_plan_hour_no"/></span>
				</s:if>
				<s:if test="plan.needAssessment == 1">
					<span><s:text name="_plan_yes"/></span>
				</s:if>
			</div>
		</div>
	</div>
	<div class="content_layout_line">
		<div class="line_content">
			<div class="line_content_left">
				<span class="line_content_span" >
                    <s:text name="_plan_course_list"></s:text>
                </span>
			</div>
		</div>
		<div id="planCourseList">
	    	<div class="courseListHead">
	    			<span class="headLeft"></span>
	    			<span class="noLabel titleBackGroud"></span>
	    			<span class="nameLabel titleBackGroud"><s:text name="_courseList_title_name"></s:text></span>
	    			<span class="roomLabel titleBackGroud"><s:text name="_courseList_title_room"></s:text></span>
	    			<span class="dateLabel titleBackGroud"><s:text name="_courseList_title_date"></s:text></span>
	    			<span class="timeLabel titleBackGroud"><s:text name="_courseList_title_time"></s:text></span>
	    			<span class="durationLabel titleBackGroud"><s:text name="_courseList_title_dur"></s:text></span>
	    			<span class="trainerLabel titleBackGroud"><s:text name="_courseList_title_trainer"></s:text></span>
	    			<span class="actionLabel titleBackGroud"><s:text name="_courseList_title_action"></s:text></span>
	    			<span class="headRight"></span>
	    			<div class="clear"></div>
			</div>
				<s:hidden id="coursesList" name="actualCoursesJson"/>
			<div id="courseListContent">
			</div>
		</div>
	</div>
	<div class="content_layout_line">
		<div class="line_content">
			<div class="line_content_left">
				<span class="line_content_span" >
                        <s:text name="_plan_attachment"></s:text>
                </span>
			</div>
		</div>
   		<div id="upload_attachment_list">
        	<s:hidden id="plan_attachmentsJson" name="attachmentsJson"/>
    	</div>
	</div>
	<div class="content_layout_line">
		<div class="line_content">
			<div class="line_content_left">
				<span class="line_content_span" >
                        <s:text name="_plan_creator"></s:text>
                </span>
			</div>
			<div class="line_content_right">
				<span><a class="a_type">${plan.planCreator}</a></span>
			</div>
		</div>
	</div>
	<div id="plan_excute_button" class="execute_button">
	</div>
</div>
</div>
<jsp:include page="/WEB-INF/assessment/trainer2plan.jsp"></jsp:include>
<jsp:include page="/WEB-INF/assessment/master2trainer.jsp"></jsp:include>
<jsp:include page="/WEB-INF/assessment/trainee2plan.jsp"></jsp:include>
<jsp:include page="/WEB-INF/common/footer.jsp"></jsp:include>
</body>
<script type="text/javascript" src="<%=basePath %>validanguage/js/validanguage_uncompressed.js"></script>
<script type="text/javascript" src="<%=basePath %>jquery.poshytip/js/jquery.poshytip.js"></script>
<script type="text/javascript" src="<%=basePath %>/jquery.ui/js/jquery-ui-1.8.18.custom.min.js"></script>
<script type="text/javascript" src="<%=basePath %>confirmBar/js/commonConfirmBar.js"></script>
<script type="text/javascript" src="<%=basePath %>js/plan/planCommon.js" ></script>
<script type="text/javascript" src="<%=basePath %>/js/plan/viewPlan.js"></script>
<script type="text/javascript" src="<%=basePath %>/js/common/common.js"></script>
<script type="text/javascript" src="<%=basePath %>attachmentUI/script/attachmentUI.js"></script>
<script type="text/javascript" src="<%=basePath %>messageBar/js/messagebar.js"></script>
<script type="text/javascript" src="<%=basePath %>js/common/jquery.layer.js"></script>
<script type="text/javascript" src="<%=basePath %>js/assessment/jquery.raty.min.js"></script>
<script type="text/javascript" src="<%=basePath %>js/assessment/assessmentCommon.js"></script>
<script type="text/javascript" src="<%=basePath %>scrollbar/js/scrollbar.js"></script>
<script type="text/javascript" src="<%=basePath %>js/dashboard/joinOrQuit.js"></script>
<script type="text/javascript" src="<%=basePath %>js/plan/applyLeaveOrBack.js"></script>
</html>