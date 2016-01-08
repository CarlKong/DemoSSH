<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
    
<%@ taglib prefix="s" uri="/struts-tags" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>View assessment to trainee</title>
<link type="text/css" href="<%=basePath%>css/assessment/viewAssessment2Trainee.css" rel="stylesheet">
</head>
<body>
    <div id="to_trainee_assessment_left" class="assessment_left">
        <div class="left_menu_div">
            <div id="to_trainee_plan_course_menu" class="parent_menu">
                <s:text name="view_assessment_course_list" />
            </div>
            <div class = "child_menu_div" id = "view_assessment_to_trainee_course_list" >
                <!-- Here will display the plan course names -->
            </div>
            <div id="to_trainee_trainee_menu" class="parent_menu second_parent_menu">
                <s:text name="view_assessment_trainee_list" />
            </div>
            <div class = "child_menu_div" id = "view_assessment_to_trainee_trainee_list" >
                <!-- Here will display the trainee names -->
            </div>
        </div>
    </div>
    <div id = "to_trainee_split_line">
	    <div class="split_line">
	    </div>
    </div>
    <div id="to_trainee_assessment_content" class="assessment_content">
    	<div class="objectAndAverage">
        	<div id="assessed_trainee_or_course" class="assessed_object">
            	<span id="assessed_course_or_tainee_name" class="assessed_object_name">
            	</span>
            	<span id="assessed_plan_or_trainee_id" class="assessed_object_id">
            	</span>
        	</div>
        	<div id="plan_or_trainee_average_part">
            	<div id = "to_trainee_assessment_average_item_list">
                
            	</div>
	        	<div id="trainee_attendence_logs_count_content">
	            	<div class = "to_trainee_item_label to_trainee_attendence_logs_label" >
	               		<s:text name="view_assessment_attendence_logs" />
	            	</div>
	            	<div class = "attendence_logs_count_content" >
	               		<div class = "trainee_attended attendence_logs_log" ></div><div class = "attendence_logs_count " id = "attendence_logs_count_attend"></div>
	               		<div class = "trainee_late attendence_logs_log" ></div><div class = "attendence_logs_count" id = "attendence_logs_count_late"></div>
	               		<div class = "trainee_leave attendence_logs_log" ></div><div class = "attendence_logs_count" id = "attendence_logs_count_leave_early"></div>
	               		<div class = "trainee_absent attendence_logs_log" ></div><div class = "attendence_logs_count" id = "attendence_logs_count_absence"></div>
	            	</div>
	        	</div>
	        </div>
	        <div class = "clear"></div>
	        <div id="assess2trainee_assessorButton" class="have_not_assess_button"></div>
			<div id="assess2trainee_show_employee_name" class="show_employee_name_div">
				<div class="show_employee_name_head"></div>
				<div class="show_employee_name_content"></div>
			</div>
        </div>
        <div id = "to_trainee_assessment_item_list">
            
        </div>
        <div id = "to_trainee_assessment_page_info" ></div>
    </div>
    
    <div class="clear"></div>
</body>
<script type="text/javascript" src="<%=basePath %>js/assessment/viewAssessmentData.js"></script>
<script type="text/javascript" src="<%=basePath %>js/assessment/viewAssessment2Trainee.js"></script>
</html>