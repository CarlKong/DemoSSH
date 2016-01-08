<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="privilege" uri="/WEB-INF/tld/privilege" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title><s:text name="_view_course_title"/></title>
<link type="text/css" href="<%=basePath %>css/common/common.css" rel="stylesheet">
<link type="text/css" href="<%=basePath %>css/course/veiwCourse.css" rel="stylesheet">
<link type="text/css" href="<%=basePath %>attachmentUI/csslib/attachmentUI.css" rel="stylesheet" />
<link type="text/css" href="<%=basePath %>jquery.poshytip/css/tip-green.css" rel="stylesheet">
<link rel="stylesheet" href="<%=basePath%>messageBar/css/messagebar.css" type="text/css">
<link rel="stylesheet" href="<%=basePath%>jquery.ui/css/jquery-ui-1.8.18.custom.css">
<link rel="stylesheet" href="<%=basePath%>confirmBar/css/confirmDialog.css">
</head>
<body>
<!-- info from search page -->
<s:form action="" theme="simple" id="preornext">
<s:hidden name="fromSearchToViewCondition.nowId" value="%{course.courseId}"></s:hidden>
<s:hidden name="fromSearchToViewCondition.backupId"></s:hidden>
<s:hidden name="fromSearchToViewCondition.totalPageNum"></s:hidden>
<s:hidden name="fromSearchToViewCondition.isNoSelectedflag"></s:hidden>
<s:hidden name="criteria.pageNum"></s:hidden>
<s:hidden name="criteria.pageSize"></s:hidden>
<s:hidden name="criteria.sortSign"></s:hidden>
<s:hidden name="criteria.sortName"></s:hidden>
<s:hidden name="criteria.queryString"></s:hidden>
<s:hidden name="criteria.searchFields"></s:hidden>
<s:hidden name="criteria.typeIds"></s:hidden>
<s:hidden name="criteria.isCertificateds"></s:hidden>
<s:hidden name="forChangeLocaleFlag" id="forChangeLocaleFlag" ></s:hidden>
</s:form>
<jsp:include page="/WEB-INF/common/header.jsp"></jsp:include>
<input type="hidden" id="course_id" value="${course.courseId }"/>
<input type="hidden" id="course_prefixIDValue" value="${course.prefixIDValue }"/>
<input type="hidden" id="operationFlag" value="${operationFlag }"/>
<!-- message bar -->
<div id ="messageBar">
	<span id="messageBar_create" class="message_span"><s:text name="_course_messageBar_create"></s:text></span>
</div>

<div class="content" >
    <div class="viewCourseDetails" >
       <s:if test="fromSearchToViewCondition!=null">
       <div class="first_content_layout_line" >
           <div class="first_line_content" >
               <div class="first_line_content_left" >
                   <s:if test="%{criteria.pageNum<=1&&previousFlag==-1}">
                   <a class="first_other_a"><span class="line_span_details" >
                       <s:text name="_course_previous"></s:text>
                   </span></a>
                   </s:if><s:else>
                   <a class="first_a course_previous" href="javascript:void(0);"><span class="line_span_details" >
                       <s:text name="_course_previous"></s:text>
                   </span></a>
                   </s:else>
                   <span class="first_bar">|</span>
                   <s:if test="%{criteria.pageNum>=fromSearchToViewCondition.totalPageNum&&nextFlag==1}">
                   <a class="first_other_a"><span class="line_span_details" >
                       <s:text name="_course_next"></s:text>
                   </span></a>
                   </s:if><s:else>
                   <a class="first_a course_next" href="javascript:void(0);"><span class="line_span_details" >
                       <s:text name="_course_next"></s:text>
                   </span></a>
                   </s:else>
               </div>
               <div class="first_line_content_right">
                   <a class="first_a course_back_to_search" href="javascript:void(0);"><span class="line_span_details">
                       <s:text name="_course_back_to_search"></s:text>
                   </span></a>
               </div>
           </div>
           <div class="first_line"></div>
        </div>
        </s:if>
        <div class="content_layout_line" >
            <div class="line_content" >
                <div class="line_content_left" >
                    <span class="line_content_span" >
                        <s:text name="_course_id"></s:text>
                    </span>
                </div>
                <div class="line_content_right">
                    <span>${course.prefixIDValue }</span>
                </div>
            </div>
         </div>
         <div class="content_layout_line" >
            <div class="line_content" >
                <div class="line_content_left" >
                    <span class="line_content_span" >
                        <s:text name="_course_name"></s:text>
                    </span>
                </div>
                <div class="line_content_right">
                    <span>${course.courseName }</span>
                </div>
            </div>
        </div>
        <div class="content_layout_line" >
            <div class="line_content" >
                <div class="line_content_left" >
                    <span class="line_content_span" >
                        <s:text name="_course_brief"></s:text>
                    </span>
                </div>
                <div class="line_content_right">
                    <span id="brief">${course.courseBrief }</span>
                </div>
            </div>
        </div>
        <div class="content_layout_line" >
            <div class="line_content" >
                <div class="line_content_left" >
                    <span class="line_content_span" >
                        <s:text name="_target_people"></s:text>
                    </span>
                </div>
                <div class="line_content_right">
                    <span>${course.courseTargetTrainee }</span>
                </div>
            </div>
        </div>
        <div class="content_layout_line" >
            <div class="line_content" >
                <div class="line_content_left" >
                    <span class="line_content_span" >
                        <s:text name="_duration"></s:text>
                    </span>
                </div>
                <div class="line_content_right">
                    <span>${course.courseDuration }</span>&nbsp;<span><s:text name="_course_duration"/></span>
                </div>
            </div>
        </div>
        <div class="content_layout_line" >
            <div class="line_content" >
                <div class="line_content_left" >
                    <span class="line_content_span" >
                        <s:text name="_course_type"></s:text>
                    </span>
                </div>
                <div class="line_content_right">
                    <span>${course.courseType.typeName }</span>
                </div>
            </div>
        </div>
        <div class="content_layout_line" >
            <div class="line_content" >
                <div class="line_content_left" >
                    <span class="line_content_span" >
                        <s:text name="_course_tag"></s:text>
                    </span>
                </div>
                <div class="line_content_right">
                    <span>${course.courseCategoryTag }</span>
                </div>
            </div>
        </div>
        <div class="content_layout_line" >
            <div class="line_content" >
                <div class="line_content_left" >
                    <span class="line_content_span" >
                        <s:text name="_course_author"></s:text>
                    </span>
                </div>
                <div class="line_content_right">
                    <a href="javascript:void(0);" ><span>${course.courseAuthorName }</span></a>
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
            </div>
	   		<div id="upload_attachment_list">
	   			<s:hidden id="attachmentList" name="attachmentsJson"></s:hidden>
	    	</div>
        </div>
        <div class="content_layout_line" >
            <div class="line_content" >
                <div class="line_content_left" >
                    <span class="line_content_span" >
                        <s:text name="_history_trainer"></s:text>
                    </span>
                </div>
                <div class="line_content_right">
                    <a href="javascript:void(0);" ><span>${course.historyTrainers }</span></a>
                </div>
            </div>
        </div>
        <div class="content_layout_line" >
            <div class="line_content" >
                <div class="line_content_left" >
                    <span class="line_content_span" >
                        <s:text name="_course_crateor"></s:text>
                    </span>
                </div>
                <div class="line_content_right">
                    <a href="javascript:void(0);" ><span>${course.courseCreator }</span></a>
                </div>
            </div>
        </div>
    </div>
    <div class="view_course_btn" >
     <privilege:operate operateID="course[toEditCourse]" creator="${course.courseCreator}">
        <div id="course_edit_btn">
            <a id="editButton" class="a_common_button a_create_button" onmouseup="className='a_common_button a_create_button'" onmousedown="className='a_common_button a_create_button_hover'">
                <span class="a_common_button_span" ><s:text name="_btn_edit"></s:text> </span>
            </a>
        </div>
      </privilege:operate>
      
       <privilege:operate operateID="course[deleteCourse]" creator="${course.courseCreator}" special="true">
        <div id="course_delete_btn">
            <a id="deleteButton" class="a_common_button a_cancel_button" onmouseup="className='a_common_button a_cancel_button'" onmousedown="className='a_common_button a_cancel_button_hover'">
                <span class="a_common_button_span" ><s:text name="_btn_delete"></s:text> </span>
            </a>
        </div>
       </privilege:operate>
    </div>
</div>
<form id="viewCourse_form" action="" method="post">
    <input type="hidden" name="course.courseId" value="${course.courseId }">
</form>
<jsp:include page="/WEB-INF/common/footer.jsp"></jsp:include>
</body>
<script type="text/javascript" src="<%=basePath %>/jquery.ui/js/jquery-ui-1.8.18.custom.min.js"></script>
<script type="text/javascript" src="<%=basePath %>confirmBar/js/commonConfirmBar.js"></script>
<script type="text/javascript" src="<%=basePath %>attachmentUI/script/attachmentUI.js"></script>
<script type="text/javascript" src="<%=basePath %>jquery.poshytip/js/jquery.poshytip.js"></script>
<script type="text/javascript" src="<%=basePath %>js/common/common.js" ></script>
<script type="text/javascript" src="<%=basePath %>js/course/courseCommon.js"></script>
<script type="text/javascript" src="<%=basePath %>js/course/viewCourse.js"></script>
<script type="text/javascript" src="<%=basePath %>messageBar/js/messagebar.js"></script>
</html>