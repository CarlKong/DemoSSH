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
<body>
	<div id="plan_assessment_left" class="assessment_left">
		<div class="left_menu_div">
			<div id="from_trainee_menu" class="parent_menu">
				<s:text name="_from_trainee"></s:text>
			</div>
			<div id="from_trainer_menu" class="parent_menu">
				<s:text name="_from_trainer"></s:text>
			</div>
		</div>
	</div>
	<div class="split_line">
		
	</div>
	<div id="plan_assessment_content" class="assessment_content">
		<div class="objectAndAverage">
			<div id="assessed_plan" class="assessed_object">
				<span id="assessed_plan_name" class="assessed_object_name"></span>
				<span id="assessed_plan_id" class="assessed_object_id"></span>
			</div>
			<div id="plan_average_part" class="assessment_avarage_value_part"></div>
			<div id="assess2plan_assessorButton" class="have_not_assess_button"></div>
			<div id="show_employee_name" class="show_employee_name_div">
				<div class="show_employee_name_head"></div>
				<div class="show_employee_name_content"></div>
			</div>
		</div>
		<div id="plan_assessment_detail">
		</div>
	</div>
	<div class="clear"></div>
</body>
<script type="text/javascript" src="<%=basePath %>js/assessment/assessmentToPlan.js"></script>
</html>