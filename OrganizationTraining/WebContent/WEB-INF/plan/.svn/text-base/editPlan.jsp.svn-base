<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib  prefix="s" uri="/struts-tags"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title><s:text name="_edit_plan_title"></s:text></title>
<link type="text/css" href="<%=basePath %>css/common/common.css" rel="stylesheet">
<link type="text/css" href="<%=basePath %>css/plan/selectCourse.css" rel="stylesheet">
<link type="text/css" href="<%=basePath %>css/plan/addTemporarySession.css" rel="stylesheet">
<link type="text/css" href="<%=basePath%>css/plan/planCourseList.css" rel="stylesheet">
<link type="text/css" href="<%=basePath %>attachmentUI/csslib/attachmentUI.css" rel="stylesheet" />
<link type="text/css" href="<%=basePath %>validanguage/css/validanguage.css" rel="stylesheet">
<link type="text/css" href="<%=basePath %>jquery.ui/css/jquery-ui-1.8.18.custom.css" rel="stylesheet">
<link rel="stylesheet" href="<%=basePath%>confirmBar/css/confirmDialog.css" type="text/css" >
<link type="text/css" href="<%=basePath %>datepicker/css/datepicker.css" rel="stylesheet">
<link type="text/css" href="<%=basePath %>css/plan/editPlan.css" rel="stylesheet">
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
    <form action="" id="editPlanForm" method="post">
        <input type="hidden" name="esc.isSendEmail" id="isSendEmail" value="0"/>
        <input type="hidden" name="esc.isSendToManager" id="isSendToManager" value="0"/>
        <input type="hidden" name="esc.isSendAllTrainee" id="isSendAllTrainee" value="0"/>
        <input type="hidden" name="esc.isModifiedTrainee" id="isModifiedTrainee" value="0"/>
    	<input type="hidden" name="plan.planId" id="hideplanId" value="${plan.planId}"/>
    	<input type="hidden" name="plan.planIsPublish" id="hide-planIsPublish" value="${plan.planIsPublish}"/>
    	<span id="hide-planPrefixId" style="display: none;">${plan.prefixIDValue}</span>
		<jsp:include page="/WEB-INF/plan/planContent.jsp"></jsp:include>
		<div id="selectCourseDiv">
           <jsp:include page="/WEB-INF/plan/selectCourse.jsp"></jsp:include>
        </div>
		<div class="execute_button">
			<div class="save_plan_button">
				<a id="savePlanBtn" class="a_common_button a_create_button" onmouseup="className='a_common_button a_create_button'" onmousedown="className='a_common_button a_create_button_hover'">
		             <span class="a_common_button_span"><s:text name="_btn_save"/></span>
		        </a>
			</div>
			<div class="saveAs_plan_button">
				 <a id="saveAsPlanBtn" class="a_common_button a_create_button" onmouseup="className='a_common_button a_create_button'" onmousedown="className='a_common_button a_create_button_hover'">
		              <span class="a_common_button_span"><s:text name="_btn_save_as"/></span>
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
<script type="text/javascript" src="<%=basePath %>js/common/common.js" ></script>
<script type="text/javascript" src="<%=basePath %>js/plan/planCommon.js" ></script>
<script type="text/javascript" src="<%=basePath %>js/plan/editPlan.js" ></script>
<script type="text/javascript" src="<%=basePath %>js/course/courseCommon.js" ></script>
<script type="text/javascript" src="<%=basePath %>js/actualCourse/editPlanCourse.js" ></script>
<script type="text/javascript" src="<%=basePath %>js/actualCourse/actualCourseList.js"></script>

</body>
</html>
