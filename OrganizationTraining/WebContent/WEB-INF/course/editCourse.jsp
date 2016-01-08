<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title><s:text name="_edit_course_title"/></title>
<link type="text/css" href="<%=basePath %>css/common/common.css" rel="stylesheet">
<link type="text/css" href="<%=basePath %>css/course/editCourse.css" rel="stylesheet">
<link type="text/css" href="<%=basePath %>validanguage/css/validanguage.css" rel="stylesheet">
<link type="text/css" href="<%=basePath %>jquery.ui/css/jquery-ui-1.8.18.custom.css" rel="stylesheet">
<link type="text/css" href="<%=basePath %>attachmentUI/csslib/attachmentUI.css" rel="stylesheet" />
<link type="text/css" href="<%=basePath %>jquery.poshytip/css/tip-green.css" rel="stylesheet">
<link type="text/css" href="<%=basePath %>autocomplete/css/autocomplete.css" rel="stylesheet">
<link type="text/css" href="<%=basePath %>tagControl/css/tagControl.css" rel="stylesheet">
</head>
<body>
<jsp:include page="/WEB-INF/common/header.jsp"></jsp:include>
<div class="content">
    <form id="editCourse_form" action="" method="post">
        <s:token></s:token>
        <input  id="courseId" type="hidden" readonly="readonly" name="course.courseId" value="${course.courseId }">
        <jsp:include page="/WEB-INF/course/courseContent.jsp"></jsp:include>
	    <div class="edit_course_btn" >
	        <div id="course_save_btn">
	            <a class="a_common_button a_create_button" onmouseup="className='a_common_button a_create_button'" onmousedown="className='a_common_button a_create_button_hover'">
	                <span class="a_common_button_span" ><s:text name="_btn_save"/></span>
	            </a>
	        </div>
	        <div id="course_saveAS_btn">
	            <a class="a_common_button a_create_button" onmouseup="className='a_common_button a_create_button'" onmousedown="className='a_common_button a_create_button_hover'">
	                <span class="a_common_button_span" ><s:text name="_btn_save_as"/></span>
	            </a>
	        </div>
	    </div>
    </form>
</div>
<jsp:include page="/WEB-INF/common/footer.jsp"></jsp:include>
</body>
<script type="text/javascript" src="<%=basePath %>validanguage/js/validanguage_uncompressed.js"></script>
<script type="text/javascript" src="<%=basePath %>jquery.ui/js/jquery-ui-1.8.18.custom.min.js"></script>
<script type="text/javascript" src="<%=basePath %>js/common/common.js" ></script>
<script type="text/javascript" src="<%=basePath %>js/course/courseCommon.js"></script>
<script type="text/javascript" src="<%=basePath %>js/course/editCourse.js"></script>
<script type="text/javascript" src="<%=basePath %>js/course/courseValidanguage.js"></script>
<script type="text/javascript" src="<%=basePath %>jquery.form/jquery.form.js"></script>
<script type="text/javascript" src="<%=basePath %>attachmentUI/script/attachmentUI.js"></script>
<script type="text/javascript" src="<%=basePath %>jquery.poshytip/js/jquery.poshytip.js"></script>
<script type="text/javascript" src="<%=basePath %>autocomplete/js/autocomplete.js"></script>
<script type="text/javascript" src="<%=basePath %>tagControl/js/tagControl.js"></script>

</html>