<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>select course for plan</title>
<link type="text/css" href="<%=basePath %>css/assessment/trainee2plan.css" rel="stylesheet">
</head>
<body>
<div class="assessment_popup" id="trainee2plan_popup">
<div class="assessment_popup_div content">
<input type="hidden" id="commentsTooltip" name="commentsTooltip" value="<s:text name='_assessment_trainee_comments_notice'></s:text>"></input>
	
  <form id="form_trainee2plan" action="/">
    <input type="hidden" id="trainee2plan_planId" name="planId"></input>
    <input id="trainee2plan_operationFlag" type="hidden" name="operationFlag"></input>
    <input type="hidden" name="assessment.planId" id="assess_planId"></input>
    <input id="trainee2plan_assessmentId" type="hidden" name="assessment.assessId"></input>
	<div class="assessment_popup_titile">
		<span class="feedback"><s:text name="trainee2plan_assessment_feedback_title"></s:text> </span>
		<div class="closePic"></div>
	</div>
	<div class="assessment_popup_content">
		<div class="title_part">
			<div class="title_part_content">
				<span class="title_name"></span>
				<span class="title_id"></span>
			</div>
		</div>
		<div class="assessment_part">
			<div class="assessment_title">
				<div class="assess_title_left"></div>
				<div class="assess_title_middle"><s:text name="dashboard_plan"></s:text></div>
				<div class="assess_title_right"></div>
			</div>
			<div class="assessment_item" id="trainee2plan_item">
				<!-- default height = 60 * 3 -->
				<div class="data_default item_default_60"></div>
				<!-- draw each item data -->
			</div>
			<div class="assessment_textarea">
				<textarea class="assess_comment" id="comment_trainee2plan" name="assessment.assessComment"></textarea>
				<div class="tipDiv"><span><s:text name="_assessment_comment"></s:text><s:text name='_assessment_trainee_comments_notice'></s:text></span></div>
			</div>
		</div>
		<div class="button_part">
			<a id="give_assessment_traineeToPlan" class="a_common_button a_create_button" onmouseup="className='a_common_button a_create_button'" onmousedown="className='a_common_button a_create_button_hover'">
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
<script type="text/javascript" src="<%=basePath %>js/assessment/trainee2planValidation.js"></script>
<script type="text/javascript" src="<%=basePath %>js/assessment/trainee2plan.js"></script>
</html>