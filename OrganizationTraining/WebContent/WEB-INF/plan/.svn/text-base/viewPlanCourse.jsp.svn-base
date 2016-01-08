<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<%@ taglib  prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title><s:text name="_view_plan_course_title"></s:text></title>

<link type="text/css" href="<%=basePath %>css/common/common.css" rel="stylesheet">
<link type="text/css" href="<%=basePath %>css/plan/viewPlan.css" rel="stylesheet">
<link type="text/css" href="<%=basePath %>css/plan/viewPlanCourse.css" rel="stylesheet">
<link type="text/css" href="<%=basePath %>attachmentUI/csslib/attachmentUI.css" rel="stylesheet" />
<link type="text/css" href="<%=basePath %>jquery.poshytip/css/tip-green.css" rel="stylesheet">
<link type="text/css" href="<%=basePath %>css/assessment/assessmentCommon.css" rel="stylesheet"/>
<link type="text/css" href="<%=basePath %>validanguage/css/validanguage.css" rel="stylesheet">
<link rel="stylesheet" href="<%=basePath%>jquery.ui/css/jquery-ui-1.8.18.custom.css">
<link rel="stylesheet" href="<%=basePath%>confirmBar/css/confirmDialog.css">
<link rel="stylesheet" href="<%=basePath%>scrollbar/css/custom-scrollbar.css">
</head>
<body>
<jsp:include page="/WEB-INF/common/header.jsp"></jsp:include>
<input type="hidden" id="startTimeDetail" value="${actualCourse.courseStartTime }"></input>
<input type="hidden" id="endTimeDetail" value="${actualCourse.courseEndTime }"></input>
<input type="hidden" id="planCourseId" value="${actualCourse.actualCourseId }"></input>
<input type="hidden" id="planCourse_planId" value="${actualCourse.plan.planId}"></input>
<!-- content -->
<div class="content">
	<div class="view_planCourse_details">
		<div id="view_plan_id">
			<div id="view_plan_id_left">
				<div class="line_content_left">
					<span class="line_content_span" >
	                        <s:text name="_course_id"></s:text>
	                </span>
				</div>
				<div class="line_content_right" id="view_plan_id_left_value">
					<span id="planCoursePrefixId">${actualCourse.prefixIdValue}</span>
					<input type="hidden" id="actualCoursePrefixId" value="${actualCourse.prefixIdValue}"></input>
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
		            <div id="action_part">
	            </div>   
	            </div>
			</div>
		</div>
		<div class="content_layout_line">
			<div class="line_content">
				<div class="line_content_left">
					<span class="line_content_span" >
	                        <s:text name="_course_name"></s:text>
	                </span>
				</div>
				<div class="line_content_right">
					<span id="planCourseName">${actualCourse.courseName}</span>
				</div>
			</div>
		</div>
		<div class="content_layout_line">
			<div class="line_content">
				<div class="line_content_left">
					<span class="line_content_span" >
	                        <s:text name="_course_brief"></s:text>
	                </span>
				</div>
				<div class="line_content_right">
					<span id="brief">${actualCourse.courseBrief}</span>
				</div>
			</div>
		</div>
		<div class="content_layout_line">
			<div class="line_content">
				<div class="line_content_left">
					<span class="line_content_span" >
	                        <s:text name="_target_people"></s:text>
	                </span>
				</div>
				<div class="line_content_right">
					<span>${actualCourse.courseInfo.courseTargetTrainee}</span>
				</div>
			</div>
		</div>
		<div class="content_layout_line">
			<div class="line_content">
				<div class="line_content_left">
					<span class="line_content_span" >
	                        <s:text name="_date"></s:text>
	                </span>
				</div>
				<div class="line_content_right">
					<span id="pc_date"></span>
				</div>
			</div>
		</div>
		<div class="content_layout_line">
			<div class="line_content">
				<div class="line_content_left">
					<span class="line_content_span" >
	                        <s:text name="_time"></s:text>
	                </span>
				</div>
				<div class="line_content_right">
					<span id="pc_time"></span>
				</div>
			</div>
		</div>
		<div class="content_layout_line">
			<div class="line_content">
				<div class="line_content_left">
					<span class="line_content_span" >
	                        <s:text name="_duration"></s:text>
	                </span>
				</div>
				<div class="line_content_right">
					<span>${actualCourse.courseDuration}
					<s:if test="%{1>actualCourse.courseDuration}">
						<s:text name="_plan_hour"></s:text>
					</s:if>
					<s:else>
						<s:text name="_plan_hours"></s:text>
					</s:else>
					</span>
				</div>
			</div>
		</div>
		<div class="content_layout_line">
			<div class="line_content">
				<div class="line_content_left">
					<span class="line_content_span" >
	                        <s:text name="_room"></s:text>
	                </span>
				</div>
				<div class="line_content_right">
					<span>${actualCourse.courseRoomNum}</span>
				</div>
			</div>
		</div>
		<div class="content_layout_line">
			<div class="line_content">
				<div class="line_content_left">
					<span class="line_content_span" >
	                        <s:text name="_course_type"></s:text>
	                </span>
				</div>
				<div class="line_content_right">
					<span>${actualCourse.courseInfo.courseType.typeName}</span>
				</div>
			</div>
		</div>
		<div class="content_layout_line">
			<div class="line_content">
				<div class="line_content_left">
					<span class="line_content_span" >
	                        <s:text name="_course_tag"></s:text>
	                </span>
				</div>
				<div class="line_content_right">
					<span>${actualCourse.courseInfo.courseCategoryTag}</span>
				</div>
			</div>
		</div>
		<div class="content_layout_line">
			<div class="line_content">
				<div class="line_content_left">
					<span class="line_content_span" >
	                        <s:text name="_course_author"></s:text>
	                </span>
				</div>
				<div class="line_content_right">
					<span><a class="a_type">${actualCourse.courseInfo.courseAuthorName}</a></span>
				</div>
			</div>
		</div>
		<div class="content_layout_line">
			<div class="line_content">
				<div class="line_content_left">
					<span class="line_content_span" >
	                        <s:text name="_course_attachment"></s:text>
	                </span>
				</div>
			</div>
		   	<div id="upload_attachment_list">
		        <s:hidden id="attachmentList" name="attachmentsJson"/>
		    </div>
		</div>
		<div class="content_layout_line">
			<div class="line_content">
				<div class="line_content_left">
					<span class="line_content_span" >
	                        <s:text name="_trainer"></s:text>
	                </span>
				</div>
				<div class="line_content_right">
					<span><a class="a_type">${actualCourse.courseTrainer}</a></span>
				</div>
			</div>
		</div>
		<div class="content_layout_line">
			<div class="line_content">
				<div class="line_content_left">
					<span class="line_content_span" >
	                        <s:text name="_plan_id"></s:text>
	                </span>
				</div>
				<div class="line_content_right">
					<span id="plan_prefixIDValue"><a class="a_type">${actualCourse.plan.prefixIDValue }</a></span>
				</div>
			</div>
		</div>
	</div>
	<div id="trainer2trainee_content" class="trainer2trainee_content_div">
   		<jsp:include page="/WEB-INF/assessment/trainer2trainee.jsp"></jsp:include>
	</div>
</div>
<jsp:include page="/WEB-INF/assessment/trainee2course.jsp"></jsp:include>
<jsp:include page="/WEB-INF/common/footer.jsp"></jsp:include>
</body>
<script type="text/javascript" src="<%=basePath %>/jquery.ui/js/jquery-ui-1.8.18.custom.min.js"></script>
<script type="text/javascript" src="<%=basePath %>confirmBar/js/commonConfirmBar.js"></script>
<script type="text/javascript" src="<%=basePath %>validanguage/js/validanguage_uncompressed.js"></script>
<script type="text/javascript" src="<%=basePath %>js/actualCourse/actualCourseCommon.js"></script>
<script type="text/javascript" src="<%=basePath %>/js/plan/viewPlanCourse.js"></script>
<script type="text/javascript" src="<%=basePath %>jquery.poshytip/js/jquery.poshytip.js"></script>
<script type="text/javascript" src="<%=basePath %>attachmentUI/script/attachmentUI.js"></script>
<script type="text/javascript" src="<%=basePath %>js/common/jquery.layer.js"></script>
<script type="text/javascript" src="<%=basePath %>js/assessment/jquery.raty.min.js"></script>
<script type="text/javascript" src="<%=basePath %>js/assessment/assessmentCommon.js"></script>
<script type="text/javascript" src="<%=basePath %>scrollbar/js/scrollbar.js"></script>
<script type="text/javascript" src="<%=basePath %>js/plan/applyLeaveOrBack.js"></script>
<script type="text/javascript" src="<%=basePath %>js/common/common.js"></script>
</html>