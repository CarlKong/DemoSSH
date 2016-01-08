<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<%@ taglib  prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>master2trainer</title>
<link href="<%=basePath %>css/assessment/master2trainer.css" rel="stylesheet" type="text/css" />
</head>
<body>
<input type="hidden" id="commentsTooltip" name="commentsTooltip" value="<s:text name='_assessment_trainee_comments_notice'></s:text>"></input>
<!-- assessment popup -->
<div class="assessment_popup" id="master2trainer_popup">
<div class="assessment_popup_div content">
<form action="#" method="post" id="form_master2trainer">
	<div class="assessment_popup_titile">
		<span class="feedback"><s:text name="master2trainer_assessment_feedback_title"></s:text> </span>
		<div class="closePic"></div>
	</div>
	<div class="assessment_popup_content">
		<div class="title_part">
			<div class="title_part_content">
				<span class="title_name"></span>
				<span class="title_id"></span>
				<input type="hidden" name="planId" id="assess_planId"></input>
			</div>
		</div>
		<div class="trainers_div">
			<div id="trainers_span"><s:text name="_trainers"></s:text> </div>
			<div id="trainers_dropDown">
				<span id="assessed_icon"></span>
				<input type="hidden" id="trainerNum" value="-1"></input>
				<input type="hidden" id="trainerCourses"></input>
				<span id="trainer_name"></span>
				<a id="dropDown_button"></a>
				<div id="trainer_options">
				</div>
			</div>
			<div id="trainers_tooTip"></div>
			<div id="trainers_rate">
				<span>( </span>
				<span id="assessed_count"></span>
				<span> / </span>
				<span id="trainers_count"></span>
				<span><s:text name="_assessed"></s:text>)</span>
			</div>
		</div>
		<div class="assessment_part">
			<div id="master2trainer_item_default">
				<div class="assessment_item">
					<!-- default height = 30 * 4 -->
					<div class="data_default item_default_30"></div>
					<!-- draw each item data -->
				</div>
				<div class="assessment_textarea">
					<textarea class="assess_comment" id="comment_master2trainer" name=""></textarea>
				</div>
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
<script type="text/javascript" src="<%=basePath %>js/assessment/master2trainer.js"></script>
</html>