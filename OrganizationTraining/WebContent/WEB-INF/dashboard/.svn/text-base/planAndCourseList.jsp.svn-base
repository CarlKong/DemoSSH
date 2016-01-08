<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<%@ taglib  prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>course list</title>
<link rel="stylesheet" href="<%=basePath%>css/dashboard/planAndCourseList.css">
</head>
<body>
<!-- get employeeRoleNames from session-->
<input type="hidden" id="roleNames" value="<%=session.getAttribute("employeeRoleNames")%>">
    <input type="hidden" id="red_message" value="<s:text name='red_message'></s:text>"></input>
    <input type="hidden" id="green_message" value="<s:text name='green_message'></s:text>"></input>
    <input type="hidden" id="yellow_message" value="<s:text name='yellow_message'></s:text>"></input>
    <input type="hidden" id="gray_message" value="<s:text name='gray_message'></s:text>"></input>
    <div class = "plan_and_course_list">
        
        <div class = "master" >
	        <div class="table_header_left_corner">&nbsp;</div>
	        <div class="table_header">
	            <div class="table_header_left">
	                <div class="table_header_theme"><s:text name="dashboard_list_master" /></div>
	                <div class="table_second_more_20" >
	                	<a id="master_plan_more" class="more_link"><s:text name = "dashboard_more"/></a>
	            	</div>
	            </div>
	        </div>
	        <div class="table_header_right_corner"></div>
	        <div class="clear"></div>
	        <div class = "table_content_table" >
	            <div class = "content_title content_row" >
	                <div class = "content_status" >&nbsp;</div>
	                <div class = "content_name_title"><s:text name = "dashboard_plan_name" /></div>
	                <div class = "content_type"><s:text name = "dashboard_type" /></div>
	                <div class = "content_publish_time"><s:text name = "dashboard_publish_time" /></div>
	                <div class = "content_trainees"><s:text name = "dashboard_trainees" /></div>
	            </div>
	            <div id = "table_plan_master_content" class="table_content_default">
	                <!-- There will show plan list for master -->
	            </div>
	       </div>
        </div>
        
        
        <div class = "trainer" >
	        <div class="table_header_left_corner">&nbsp;</div>
	        <div class="table_header">
	            <div class="table_header_left">
	                <div class="table_header_theme"><s:text name="dashboard_list_trainer_plan" /></div>
	                <div class = "table_second_more_20" >
		                <a id="trainer_plan_more" class="more_link"><s:text name = "dashboard_more"/></a>
		            </div>
	            </div>
	        </div>
	        <div class="table_header_right_corner"></div>
	        <div class="clear"></div>
	        <div class = "table_content_table" >
	            <div class = "content_title content_row" >
	                <div class = "content_status" >&nbsp;</div>
	                <div class = "content_name_title"><s:text name = "dashboard_plan_name" /></div>
	                <div class = "content_type"><s:text name = "dashboard_type" /></div>
	                <div class = "content_publish_time"><s:text name = "dashboard_publish_time" /></div>
	                <div class = "content_trainees"><s:text name = "dashboard_trainees" /></div>
	            </div>
	            <div id = "table_plan_trainer_content" class="table_content_default"> 
	                <!-- There will show plan list for trainer. -->
	            </div>
	        </div>
        </div>
        <div class = "trainer" >
	        <div class="table_header_left_corner">&nbsp;</div>
	        <div class="table_header">
	            <div class="table_header_left">
	                <div class="table_header_theme"><s:text name="dashboard_list_trainer_course" /></div>
	                <div class = "table_second_more_20" >
						<a id="trainer_course_more" class="more_link"><s:text name = "dashboard_more"/></a>
					</div>
	            </div>
	        </div>
	        <div class="table_header_right_corner"></div>
	        <div class="clear"></div>
			
	        <div class = "table_content_table" >
	            <div class = "content_title content_row" >
	                <div class = "content_status" >&nbsp;</div>
	                <div class = "content_name_title"><s:text name = "dashboard_course_name" /></div>
	                <div class = "content_date"><s:text name = "dashboard_date" /></div>
	                <div class = "content_time"><s:text name = "dashboard_time" /></div>
	                <div class = "content_room"><s:text name = "dashboard_room" /></div>
	                <div class = "content_trainees"><s:text name = "dashboard_trainees" /></div>
	            </div>
	            <div id = "table_course_trainer_content" class="table_content_default">
	                <!-- There will show course list for trainer. -->
	            </div>
	        </div>
        </div>
        
        <div class = "trainee" >
	        <div class="table_header_left_corner">&nbsp;</div>
	        <div class="table_header">
	            <div class="table_header_left">
	                <div class="table_header_theme"><s:text name="dashboard_list_trainee_plan" /></div>
	                <div class = "table_second_more_20" >
		                <a id="trainee_plan_more" class="more_link"><s:text name = "dashboard_more"/></a>
		            </div>
	            </div>
	        </div>
	        <div class="table_header_right_corner"></div>
	        <div class="clear"></div>
	        <div class = "table_content_table" >
	            <div class = "content_title content_row" >
	                <div class = "content_status" >&nbsp;</div>
	                <div class = "content_name_title"><s:text name = "dashboard_plan_name" /></div>
	                <div class = "content_type"><s:text name = "dashboard_type" /></div>
	                <div class = "content_publish_time"><s:text name = "dashboard_publish_time" /></div>
	                <div class = "content_trainer_tilte"><s:text name = "dashboard_trainer" /></div>
	            </div>
	            <div id = "table_plan_trainee_content" class="table_content_default">
	                <!-- There will show plan list for trainee. -->
	            </div>
	        </div>
    	</div>
    	<div class = "trainee" >
	        <div class="table_header_left_corner">&nbsp;</div>
	        <div class="table_header">
	            <div class="table_header_left">
	                <div class="table_header_theme"><s:text name="dashboard_list_trainee_course" /></div>
	                <div class = "table_second_more_20" >
						<a id="trainee_course_more" class="more_link"><s:text name = "dashboard_more"/></a>
					</div>
	            </div>
	        </div>
	        <div class="table_header_right_corner"></div>
	        <div class="clear"></div>
	        <div class = "table_content_table" >
	            <div class = "content_title content_row" >
	                <div class = "content_status" >&nbsp;</div>
	                <div class = "content_name_title"><s:text name = "dashboard_course_name" /></div>
	                <div class = "content_date"><s:text name = "dashboard_date" /></div>
	                <div class = "content_time"><s:text name = "dashboard_time" /></div>
	                <div class = "content_room"><s:text name = "dashboard_room" /></div>
	                <div class = "content_trainer_title"><s:text name = "dashboard_trainer" /></div>
	            </div>
	            <div id = "table_course_trainee_content" class="table_content_default">
	                <!-- There will show course list for trainee. -->
	            </div>
	        </div>
    	</div>
    	
    </div>
</body>
<script type="text/javascript" src="<%=basePath %>js/dashboard/planAndCourseList.js" ></script>
</html>