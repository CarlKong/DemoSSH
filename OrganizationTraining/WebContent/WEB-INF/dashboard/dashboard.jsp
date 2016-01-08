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
<title><s:text name="_dashboard_title"></s:text></title>
<link type="text/css" href="<%=basePath %>css/common/common.css" rel="stylesheet">
<link type="text/css" href="<%=basePath %>css/dashboard/dashboard.css" rel="stylesheet">
<link type="text/css" href="<%=basePath %>jquery.poshytip/css/tip-green.css" rel="stylesheet">
<link rel="stylesheet" href="<%=basePath%>css/dashboard/dashboardCommon.css">
<link rel="stylesheet" href="<%=basePath%>messageBar/css/messagebar.css" type="text/css">
<link rel="stylesheet" href="<%=basePath%>jquery.ui/css/jquery-ui-1.8.18.custom.css">
<link rel="stylesheet" href="<%=basePath%>confirmBar/css/confirmDialog.css">
<link type="text/css" href="<%=basePath %>css/assessment/assessmentCommon.css" rel="stylesheet"/>
<link type="text/css" href="<%=basePath %>validanguage/css/validanguage.css" rel="stylesheet">
<link rel="stylesheet" href="<%=basePath%>scrollbar/css/custom-scrollbar.css">
<link rel="stylesheet" href="<%=basePath%>css/common/common.css">
</head>
<body>
<jsp:include page="/WEB-INF/common/header.jsp"></jsp:include>
<span id="pulblish_i18n" class="message_span" ><s:text name="_todo_publish"></s:text></span>
<span id="assessment_i18n" class="message_span" ><s:text name="_todo_assessment"></s:text></span>
<span id="ignore_i18n" class="message_span" ><s:text name="_todo_ignore"></s:text></span>
<span id="complete_i18n" class="message_span" ><s:text name="_todo_complete"></s:text></span>
<div class="content">
    <div class="dashboard_Contrainer">
    	<div class="dashboard_left_contrainer">
    	
    	<!-- to-do page -->
    		<div class="dashboard_to_do_container">
    			<jsp:include page="/WEB-INF/dashboard/toDoList.jsp"></jsp:include>
    		</div>
    		
    	<!-- new public plan page -->
    		<div class="dashboard_new_publish_container div_margin_top">
    			<jsp:include page="/WEB-INF/dashboard/newPublicPlan.jsp"></jsp:include>
    		</div>
    	</div>
    	<div class="dashboard_right_contrainer">
    		<div class="dashboard_manager_contrainer" ></div>
    		<div>
    			<jsp:include page="/WEB-INF/dashboard/planAndCourseList.jsp"></jsp:include>
    		</div>
    	</div>
    </div>
    <div id="trainer2trainee_content" class="trainer2trainee_content_div">
    	<jsp:include page="/WEB-INF/assessment/trainer2trainee.jsp"></jsp:include>
    </div>
</div>
<jsp:include page="/WEB-INF/assessment/trainee2course.jsp"></jsp:include>
<jsp:include page="/WEB-INF/assessment/trainer2plan.jsp"></jsp:include>
<jsp:include page="/WEB-INF/assessment/master2trainer.jsp"></jsp:include>
<jsp:include page="/WEB-INF/assessment/trainee2plan.jsp"></jsp:include>
<jsp:include page="/WEB-INF/common/footer.jsp"></jsp:include>
</body>
<script type="text/javascript" src="<%=basePath %>validanguage/js/validanguage_uncompressed.js"></script>
<script type="text/javascript" src="<%=basePath %>jquery.poshytip/js/jquery.poshytip.js"></script>
<script type="text/javascript" src="<%=basePath %>jquery.ui/js/jquery-ui-1.8.18.custom.min.js"></script>
<script type="text/javascript" src="<%=basePath %>confirmBar/js/commonConfirmBar.js"></script>
<script type="text/javascript" src="<%=basePath %>messageBar/js/messagebar.js"></script>
<script type="text/javascript" src="<%=basePath %>js/common/jquery.layer.js"></script>
<script type="text/javascript" src="<%=basePath %>js/assessment/jquery.raty.min.js"></script>
<script type="text/javascript" src="<%=basePath %>js/assessment/assessmentCommon.js"></script>
<script type="text/javascript" src="<%=basePath %>scrollbar/js/scrollbar.js"></script>
<script type="text/javascript" src="<%=basePath %>js/common/common.js"></script>
</html>
