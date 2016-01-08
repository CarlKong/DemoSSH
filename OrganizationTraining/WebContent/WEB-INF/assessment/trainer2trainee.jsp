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
<title>trainer assess trainees</title>
<link type="text/css" href="<%=basePath %>css/assessment/trainer2trainee.css" rel="stylesheet">
</head>
<body>
<form action="<%=basePath %>assessment/searchData_searchTrainer2TraineeData" id="trainer2TraineeForm" method="post">
	<input type="hidden" id="trainer2planCourseId" name="actualCourseId">
	<input type="hidden" id="trainer2planCoursePlanId" name="planId">
	<input type="hidden" id="operationFlagId" name="operationFlag">
	<div class="trainer_assess_trainee_content">
		<div class="trainer_to_trainee_head">
			<div class="feedback"><s:text name="_assessment_feedback_title"></s:text></div>
			<div id="trainer_assess_trainee_close" class="closeIcon"></div>
			<div class="clear"></div>
		</div>
		
		<div class="trainer_assess_trainee_body">
			<div class="title_part_content">
				<span id="a_course_name"></span>
				<span id="a_course_prefixid"></span>
			</div>
			<div class="init_trainee_list">
				<div class="init_trainee_list_title">
					<div class="assess_title_left"></div>
					<div id="trainee_list_title_mid" class="assess_title_middle">
						<div class="id_title"><s:text name="_by_id"></s:text></div>
						<div class="name_title"><s:text name="_by_name"></s:text></div>
						<div class="attendance_title"><s:text name="_assessment_course_attendance"></s:text></div>
						<div id="a_score_field" class="score_item">
							<!-- score items -->
						</div>
						<div class="comments_title">
							<s:text name="_assessment_trainee_comments"></s:text>
							<s:text name="_trainer2TraineeCommomTip"></s:text>	
						</div>
						<div class="clear"></div>
					</div>
					<div class="assess_title_right"></div>
					<div class="clear"></div>
				</div>
				<div id="show_trainees_div" class="init_trainee_list_body">
					<div id="real_trainees_div" class="real_trainees_div">
						<!-- trainee list -->
					</div>
					<div id="vertical_scrollDiv" class="vertical_scroll">
						<div id="vertical_block" class="vertical_slide_block"></div>
					</div>
				</div>
			</div>
			<div class="decoration_text">
				<div class="course_comments_label"><s:text name="_assessment_course_comments"></s:text></div>
				<div class="attend_decoration">
					<div class="attend_icon attended_active"></div>
					<div class="attend_discrib"><s:text name="_assessment_attended"></s:text></div>
					<div class="attend_icon late_active"></div>
					<div class="attend_discrib"><s:text name="_assessment_late"></s:text></div>
					<div class="attend_icon leave_active"></div>
					<div class="attend_discrib"><s:text name="_assessment_apply_leave"></s:text></div>
					<div class="attend_icon absent_active"></div>
					<div class="attend_discrib"><s:text name="_assessment_be_absent"></s:text></div>
					<div class="clear"></div>
				</div>
				<div class="clear"></div>
			</div>
			<div>
				<textarea id="trainer2course_comments" class="tr2c_comments" name="assessment.assessComment"></textarea>
				<div class="commentInputTips"><span><s:text name="_assessment_course_comments_notice"></s:text></span></div>
			</div>
			<div class="button_area">
				<a id="submit_trainer2Trainee" class="a_common_button a_create_button">
					<span><s:text name="_btn_submit"></s:text></span>
				</a>
				<a id="cancel_trainer2Trainee" class="a_common_button a_cancel_button">
					<span><s:text name="_btn_cancel"></s:text></span>
				</a>
			</div>
		</div>
	</div>
</form>
</body>
<script type="text/javascript" src="<%=basePath %>js/assessment/trainer2trainee.js"></script>
</html>