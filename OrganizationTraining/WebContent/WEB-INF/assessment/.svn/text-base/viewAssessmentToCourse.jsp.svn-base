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
</head>
<link type="text/css" href="<%=basePath%>css/assessment/assessmentToPlan.css" rel="stylesheet">
<link type="text/css" href="<%=basePath%>css/assessment/assessmentToCourse.css" rel="stylesheet">
<body>
	<div id="plan_course_assessment_left" class="assessment_left">
		<div class="left_menu_div">
			<div id="planCourse_courseList_menu" class="parent_menu">
				<s:text name="_course_list"></s:text>
			</div>
			<div id="planCourse_child_menu_div" class="child_menu_div">
				
			</div>
		</div>
	</div>
	<div class="split_line">
	</div>
	<div id="planCourse_assessment_content" class="assessment_content">
		<div class="assessed_object">
			<span id="assessed_planCourse_name" class="assessed_object_name"></span>
			<span id="assessed_planCourse_id" class="assessed_object_id"></span>
		</div>
		<div id="trainer_assess_planCourse_part">
			<div id="fromTrainer_subtitle" class="subtitle">
				<s:text name="_from_trainer"></s:text>
				<div id="trainer_to_course_expand" class="subtitle_fold"></div>
			</div>
			<div id="trainer_assess_planCourse_value"></div>
		</div>
	    <div id="trainees_assess_planCourse_part">
			<div id="fromTrainer_subtitle" class="subtitle">
				<s:text name="_from_trainee"></s:text>
				<div id="trainee_to_course_expand" class="subtitle_fold"></div>
			</div>
			<div id="trainees_assess_planCourse_value">
				<div id="planCourse_average_part" class="objectAndAverage">
					<div id="assess2course_assessorButton" class="have_not_assess_button"></div>
					<div id="show_trainee2course_name" class="show_employee_name_div">
						<div class="show_employee_name_head"></div>
						<div class="show_employee_name_content"></div>
					</div>
					<div id="planCourse_average_part_value"></div>
				</div>
				<div id="planCourse_assessment_detail"></div>
			</div>
		</div>
		
	</div>
	<div class="clear"></div>
</body>
<script type="text/javascript" src="<%=basePath %>js/assessment/assessmentToCourse.js"></script>
</html>