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
<title>add temporary session for plan</title>
</head>
<body>
	<div id="addTemporarySession" class="content">
		<div class="addTemporarySessionTitle">
            <span id="topTitle" class="line_content_span"><s:text name="_add_temporary_session_title"></s:text></span>
            <img id="closeSessionPic" src="<%=basePath %>image/buttonIMG/ICN_Close_11x11.png">
            <span class="clear"></span>
        </div>
        <div id = "addTemporarySessionContent" >
	        <form id = "planSessionForm" action = "" >
	        	<div class = "addTemporarySessionContentClass" ></div>
	        	<div id = "sessionName" class = "temporarySessionList"  >
	        		<span id = "sessionNameLabel" class="sessionContentLabel" >
	        			<s:text name="_temporary_session_name" />
	        		</span>
	        		<span class = "sessionContentInput" >
	        			<input id="sessionNameInput" class="input_txt" type="text" value = "" />
	        		</span>
	        	</div>
	        	<div class = "addTemporarySessionContentClass" ></div>
	        	<div id = "sessionBrief" class = "temporarySessionList" >
	        		<span class="sessionContentLabel" >
	        			<s:text name="_temporary_session_brief" />
	        		</span>
	        		<span class = "sessionContentInput" id = "sessionBriefDiv" >
	        			<textarea id="sessionBriefEditor"  ></textarea>
	        		</span>
	        	</div>
	        	<div class = "addTemporarySessionContentClass" ></div>
	        	<div id = "sessionAttachment" class = "temporarySessionList" >
	        		<span class = "sessionContentLabel" >
	        			<s:text name="_temporary_session_attachment"></s:text>
	        		</span>
	        		<span class = "sessionContentInput"  >
	        			<a id = "sessionAddAttachmentUrl" href="javascript:void(0);" ><s:text name="_temporary_session_add_attachment"></s:text></a>
	        		</span>
	        	</div>
	        	<div id="sessionAttachmentList" class="sessionAttachmentList">
					
				</div>
				<input type = "hidden" id = "sessionAttachmentListDataSource" class = "sessionAttachmentListDataSource" />
	        	<div class = "addTemporarySessionContentClass" ></div>
	        	<div class = "session_execute_button" >
	        		<div class="session_confirm_button">
						<a id="createPlanSessionBtn" number = "" class="a_common_button a_create_button" href="javascript:void(0);">
			                <span class="a_common_button_span"><s:text name="_temporary_session_confirm"/></span>
			            </a>
					</div>
					<div class="session_cancel_button">
						<a  id="cancelPlanSessionBtn" class="a_common_button a_cancel_button" href="javascript:void(0);">
		             	   <span class="a_common_button_span"><s:text name="_temporary_session_cancel"/></span>
		           		</a>
					</div>
	        	</div>
	        </form>
        </div>
	</div>
</body>
<script type="text/javascript" src="<%=basePath %>js/actualCourse/actualCourseCommon.js"></script>
<script type="text/javascript" src="<%=basePath %>js/actualCourse/createPlanSession.js"></script>
<script type="text/javascript" src="<%=basePath %>js/actualCourse/creatSessionCommon.js"></script>
<script type="text/javascript" src="<%=basePath %>js/actualCourse/editPlanSession.js"></script>
<script type="text/javascript" src="<%=basePath %>js/actualCourse/planSessionValidanguage.js"></script>
</html>