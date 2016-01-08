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
<title><s:text name="_create_plan"/></title>
<link type="text/css" href="<%=basePath %>css/common/common.css" rel="stylesheet">
<link type="text/css" href="<%=basePath %>css/plan/selectCourse.css" rel="stylesheet">
<link type="text/css" href="<%=basePath %>css/plan/addTemporarySession.css" rel="stylesheet">
<link type="text/css" href="<%=basePath%>css/plan/planCourseList.css" rel="stylesheet">
<link type="text/css" href="<%=basePath %>attachmentUI/csslib/attachmentUI.css" rel="stylesheet" />
<link type="text/css" href="<%=basePath %>validanguage/css/validanguage.css" rel="stylesheet">
<link type="text/css" href="<%=basePath %>jquery.ui/css/jquery-ui-1.8.18.custom.css" rel="stylesheet">
<link type="text/css" href="<%=basePath%>confirmBar/css/confirmDialog.css" rel="stylesheet" >
<link type="text/css" href="<%=basePath %>datepicker/css/datepicker.css" rel="stylesheet">
<link type="text/css" href="<%=basePath %>css/plan/createPlan.css" rel="stylesheet">
<link type="text/css" href="<%=basePath %>tagControl/css/tagControl.css" rel="stylesheet">
</head>
<body>
<jsp:include page="/WEB-INF/common/header.jsp"></jsp:include>
<input type="hidden" id="judgePage" value="creatPlan">
<div class="createplan_content">
	<form id="editPlanCourseForm">
    	<div class="editPlanCoursePopUp">
    		<jsp:include page="/WEB-INF/plan/editPlanCourse.jsp"></jsp:include>
    	</div>
    </form>
    <form action="../plan/createPlan.action" method="post" id="createPlanForm">
		<jsp:include page="/WEB-INF/plan/planContent.jsp"></jsp:include>
		<div id="selectCourseDiv">
	         <jsp:include page="/WEB-INF/plan/selectCourse.jsp"></jsp:include>
	    </div>
		<div class="execute_button">
			<div class="create_plan_button">
				<a id="createPlanBtn" class="a_common_button a_create_button" onmouseup="className='a_common_button a_create_button'" onmousedown="className='a_common_button a_create_button_hover'">
	                <span class="a_common_button_span"><s:text name="_btn_create"/></span>
	            </a>
			</div>
			<div class="cancel_plan_button">
				<a  id="cancelPlanBtn" class="a_common_button a_cancel_button" onmouseup="className='a_common_button a_cancel_button'" onmousedown="className='a_common_button a_cancel_button_hover'" href="<%=basePath %>plan/plan_searchPlan">
	                <span class="a_common_button_span"><s:text name="_btn_cancel"/></span>
	            </a>
			</div>
		</div>
	</form>
	<div id = "addTemporarySessionDiv" >
         <jsp:include page="/WEB-INF/plan/addTemporarySession.jsp" />
    </div>
</div>
<jsp:include page="/WEB-INF/common/footer.jsp"></jsp:include>

<script type="text/javascript" src="<%=basePath %>js/plan/planPropertyConstant.js"></script>
<script type="text/javascript" src="<%=basePath %>validanguage/js/validanguage_uncompressed.js"></script>
<script type="text/javascript" src="<%=basePath %>js/actualCourse/planCourseValidanguage.js"></script>
<script type="text/javascript" src="<%=basePath %>js/plan/planValidanguage.js"></script>
<script type="text/javascript" src="<%=basePath %>attachmentUI/script/attachmentUI.js"></script>
<script type="text/javascript" src="<%=basePath %>/js/common/jquery.layer.js" ></script>
<script type="text/javascript" src="<%=basePath%>jquery.ui/js/jquery-ui-1.8.18.custom.min.js"></script>
<script type="text/javascript" src="<%=basePath %>confirmBar/js/commonConfirmBar.js"></script>
<script type="text/javascript" src="<%=basePath %>jquery.form/jquery.form.js"></script>
<script type="text/javascript" src="<%=basePath %>autocomplete/js/autocomplete.js" ></script>
<script type="text/javascript" src="<%=basePath %>tagControl/js/tagControl.js"></script>
<script type="text/javascript" src="<%=basePath %>js/common/menu.js" ></script>
<script type="text/javascript" src="<%=basePath %>js/common/common.js" ></script>
<script type="text/javascript" src="<%=basePath %>js/course/courseCommon.js" ></script>
<script type="text/javascript" src="<%=basePath %>js/plan/planCommon.js" ></script>
<script type="text/javascript" src="<%=basePath %>js/plan/createPlan.js" ></script>
<script type="text/javascript" src="<%=basePath %>js/actualCourse/editPlanCourse.js" ></script>
<script type="text/javascript" src="<%=basePath %>js/actualCourse/actualCourseList.js"></script>
</body>
</html>
