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
<title><s:text name="_view_plan_assessment"></s:text></title>
<link type="text/css" href="<%=basePath%>css/common/commonTab.css" rel="stylesheet">
<link type="text/css" href="<%=basePath%>css/assessment/viewAssessmentCommon.css" rel="stylesheet">
<link type="text/css" href="<%=basePath%>css/assessment/viewAllAssessment.css" rel="stylesheet">

</head>
<body>
	<jsp:include page="/WEB-INF/common/header.jsp"></jsp:include>
	<input type="hidden" id="planId" value="${request.plan.planId}" />
	<input type="hidden" id="planName" value="${request.plan.planName}" />
	<input type="hidden" id="prefixIDValue" value="${request.plan.prefixIDValue}" />
	<input type="hidden" id="needAssessment" value="${request.plan.needAssessment}">
	<div class="content">
		<div id="assessment_type_menu">	
		</div>
		<div id="assessment_show_content">
			<div id="assess_to_plan_part" class="assessment_show_part">
				<jsp:include page="/WEB-INF/assessment/viewAssessmentToPlan.jsp"></jsp:include>
			</div>
			<div id="assess_to_course_part" class="assessment_show_part">
				<jsp:include page="/WEB-INF/assessment/viewAssessmentToCourse.jsp"></jsp:include>
			</div>
			<div id="assess_to_trainee_part" class="assessment_show_part">
				<jsp:include page="/WEB-INF/assessment/viewAssessment2Trainee.jsp" />
			</div>
			<div id="assess_to_trainer_part" class="assessment_show_part">
				<jsp:include page="/WEB-INF/assessment/viewAssessment2Trainer.jsp" />
			</div>
		</div>
	</div>
	<jsp:include page="/WEB-INF/common/footer.jsp"></jsp:include>
	<script type="text/javascript" src="<%=basePath %>js/common/common.js"></script>
	<script type="text/javascript" src="<%=basePath %>js/common/commonTab.js"></script>
	<script type="text/javascript" src="<%=basePath %>js/assessment/viewAllAssessment.js"></script>
</body>
</html>