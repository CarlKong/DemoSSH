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
<title>Course Content</title>
<link type="text/css" href="<%=basePath %>css/course/courseContent.css" rel="stylesheet">
</head>
<body>
    <div class="courseContent" >
        <div class="content_layout_line" >
            <div class="line_content" >
                <div class="line_content_left" >
                    <span class="line_content_span" >
                        <s:text name="_course_name"></s:text>
                    </span>
                </div>
                <div class="line_content_right">
                    <input id="courseName" class="input_txt" name="course.courseName" type="text" value="${course.courseName}">
                </div>
            </div>
        </div>
        <div class="content_layout_line" >
            <div id="course_brief_height" class="line_content" >
                <div class="line_content_left" >
                    <span class="line_content_span" >
                        <s:text name="_course_brief"></s:text>
                    </span>
                </div>
                <div class="line_content_right" id="courseBriefDiv">
                    <textarea id="courseBrief" name="course.courseBrief">${course.courseBrief }</textarea>
                </div>
                <input type="hidden" id="courseBriefWithoutTag" name="course.courseBriefWithoutTag"></input>
            </div>
        </div>
        <div class="content_layout_line" >
            <div id="course_targetTrainee_height" class="line_content" >
                <div class="line_content_left" >
                    <span class="line_content_span" >
                        <s:text name="_target_people"></s:text>
                    </span>
                </div>
                <div class="line_content_right">
                    <textarea id="courseTargetPeople" name="course.courseTargetTrainee">${course.courseTargetTrainee }</textarea>
                </div>
            </div>
        </div>
        <div class="content_layout_line duration" >
            <div class="line_content" >
                <div class="line_content_left" >
                    <span class="line_content_span" >
                        <s:text name="_duration"></s:text>
                    </span>
                </div>
                <div class="line_content_right">
                    <input id="courseDuration" class="input_txt" type="text" maxlength="4" name="course.courseDuration" value="${course.courseDuration }">
                    <span><s:text name="_course_duration" /></span>
                </div>
            </div>
        </div>
        <div class="content_layout_line" >
            <div class="line_content" id="course_type_line" >
                <div class="line_content_left" >
                    <span class="line_content_span" >
                        <s:text name="_course_type"></s:text>
                    </span>
                </div>
                <div id="radio_courseTypeId" class="line_content_right">
                	<input type="hidden" id="courseTypeId" value="${course.courseType.courseTypeId}"/>
                </div>
            </div>
        </div>
        <div class="content_layout_line" >
            <div class="line_content" id="line_content_tag" >
                <div class="line_content_left" >
                    <span class="line_content_span" >
                        <s:text name="_course_tag"></s:text>
                    </span>
                </div>
                <div class="line_content_right">
                	<input type="hidden" id="courseTag_value" value="${course.courseCategoryTag }"/>
                	<div id="tagDiv">
                		<input id="courseCategoryTag" class="input_txt" type="hidden" name="course.courseCategoryTag" value="${course.courseCategoryTag }">
                	</div>
                    <span id="line_tag_notice" class="notice_font" ><s:text name="_common_tag_notice"></s:text></span>
                    <div id="common_tag_div" ><span id="common_tag_span"><s:text name="_common_tag"></s:text></span>
                        <span id="common_tag_content"></span>
                    </div>
                </div>
            </div>
        </div>
        <div class="content_layout_line author" >
            <div class="line_content" >
                <div class="line_content_left" >
                    <span class="line_content_span" >
                        <s:text name="_course_author"></s:text>
                    </span>
                </div>
                <div id="authorNameCheck" class="line_content_right ui-widget">
                    <input id="courseAuthorName_value" type="hidden" value="${course.courseAuthorName }">
                    <div id="authorNameDiv">
                    	<input id="courseAuthorName" class="input_txt" type="text" name="course.courseAuthorName" value="${course.courseAuthorName }">
                	</div>
                </div>
            </div>
        </div>
        <div class="content_layout_line" >
            <div class="line_content" >
                <div class="line_content_left" >
                    <span class="line_content_span" >
                        <s:text name="_course_attachment"></s:text>
                    </span>
                </div>
                <div class="line_content_right">
                    <a id="course_add_attachment" href="javascript:void(0);"><span><s:text name="_add_attachment"></s:text></span></a>
                </div>
            </div>
           <div id="upload_attachment_list" class="course_attachmentList">
           		<s:hidden id="attachmentList" name="attachmentsJson"></s:hidden>
           </div>
        </div>
    </div>
</body>

</html>
