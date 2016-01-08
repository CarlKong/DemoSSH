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
<title>EditPlanCourse</title>
<link type="text/css" href="<%=basePath %>/css/plan/editPlanCourse.css" rel="stylesheet">
<link type="text/css" href="<%=basePath %>/css/course/editCourse.css" rel="stylesheet">
</head>
<body>
	<div class="editPlanCourseDiv">
		<div class="editPlanCourseTitle">
			<span class="editPlanCourseTitleName"><s:text name="_btn_edit_course"/></span>
			<span id="closeEditPlanCourse" class="editPlanCourseTitleClose"></span>
		</div>
		<div class="editPlanCourseContent">
			<jsp:include page="/WEB-INF/course/courseContent.jsp"></jsp:include>		
		</div>
		<div class="edit_course_btn" >
        <div id="course_save_btn">
            <a class="a_common_button a_create_button" onmouseup="className='a_common_button a_create_button'" onmousedown="className='a_common_button a_create_button_hover'">
                <span class="a_common_button_span" ><s:text name="_btn_confirm"/></span>
            </a>
        </div>
        <div id="course_saveAS_btn">
            <a id="cancelEditPlanCourseID" class="a_common_button a_cancel_button" onmouseup="className='a_common_button a_cancel_button'" onmousedown="className='a_common_button a_cancel_button_hover'">
                <span class="a_common_button_span" ><s:text name="_btn_cancel"/></span>
            </a>
        </div>
    </div>
	</div>
</body>

</html>