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
<title>trainer2plan</title>
<link href="<%=basePath %>css/assessment/trainer2plan.css" rel="stylesheet" type="text/css" />
</head>
<body>
<input type="hidden" id="commentsTooltip" name="commentsTooltip" value="<s:text name='_assessment_trainee_comments_notice'></s:text>"></input>
<!-- assessment popup -->
<div class="assessment_popup" id="trainer2plan_popup">
<div class="assessment_popup_div content">
<form action="#" method="post" id="form_trainer2plan">
	<input id="trainer2planAssessId" type="hidden" name="assessment.assessId"></input>
	<div class="assessment_popup_titile">
		<span class="feedback"><s:text name="trainer2plan_assessment_feedback_title"></s:text> </span>
		<div class="closePic"></div>
	</div>
	<div class="assessment_popup_content">
		<div class="title_part">
			<div class="title_part_content">
				<span class="title_name"></span>
				<span class="title_id"></span>
				<input type="hidden" name="assessment.planId" id="assess_planId"></input>
			</div>
		</div>
		<div class="assessment_part">
			<div class="assessment_title">
				<div class="assess_title_left"></div>
				<div class="assess_title_middle"><s:text name="dashboard_plan"></s:text></div>
				<div class="assess_title_right"></div>
			</div>
			<div class="assessment_item" id="trainer2plan_item">
				<!-- default height = 60 * 3 -->
				<div class="data_default item_default_60"></div>
				<!-- draw each item data -->
			</div>
			<div class="assessment_textarea">
				<textarea class="assess_comment" id="comment_trainer2plan" name="assessment.assessComment"></textarea>
				<div class="tipDiv"><span><s:text name="_assessment_comment"></s:text><s:text name='_assessment_trainee_comments_notice'></s:text></span></div>
			</div>
		</div>
		<div class="button_part">
			<a id="give_assessment" class="a_common_button a_create_button" onmouseup="className='a_common_button a_create_button'" onmousedown="className='a_common_button a_create_button_hover'">
				<span class="a_common_button_span"><s:text name="_btn_submit"></s:text></span>
			</a>
			<a id="cancel_assessment" class="a_common_button a_cancel_button" onmouseup="className='a_common_button a_cancel_button'" onmousedown="className='a_common_button a_cancel_button_hover'">
				<span class="a_common_button_span" ><s:text name="_btn_cancel"></s:text></span>
			</a>
		</div>
	</div>
</form>
</div>
</div>
</body>
<script type="text/javascript" src="<%=basePath %>js/assessment/trainer2plan.js"></script>
</html>