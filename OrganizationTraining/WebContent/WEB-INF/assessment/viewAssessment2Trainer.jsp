<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
    
<%@ taglib prefix="s" uri="/struts-tags" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>View assessment to trainee</title>
<link type="text/css" href="<%=basePath%>css/assessment/viewAssessment2Trainer.css" rel="stylesheet">
</head>
<body>
    <div id="to_trainer_assessment_left" class="assessment_left">
        <div class="left_menu_div">
            <div id="" class="parent_menu">
                <s:text name="view_assessment_trainer_list" />
            </div>
            <div class = "child_menu_div" id = "view_assessment_to_trainer_trainer_list" >
                <!-- Here will display the trainer names -->
            </div>
        </div>
    </div>
    <div class="split_line">
        
    </div>
    <div id="to_trainer_assessment_content" class="assessment_content">
        <div class="assessed_object assessed_trainer">
            <span id="assessed__tainer_name" class="assessed_object_name">
            </span>
            <span id="assessed_trainer_id" class="assessed_object_id">
            </span>
        </div>
        <div class = "subtitle" >
            <s:text name="view_assessment_from_training_master" />
	        <div id = "master_to_trainer" class = "subtitle_fold" ></div>
        </div>
        <div id = "master_to_trainer_content" >
            
        </div>
        
        <div class = "subtitle" id = "to_trainer_from_trainee_subtitle" >
            <s:text name="view_assessment_from_trainee" />
            <div id = "trainee_to_trainer" class = "subtitle_fold" ></div>
        </div>
        <div id="trainee_to_trainer_content">
        
        </div>
    </div>
    
    <div class="clear"></div>
</body>
<script type="text/javascript" src="<%=basePath %>js/assessment/viewAssessmentData.js"></script>
<script type="text/javascript" src="<%=basePath %>js/assessment/viewAssessment2Trainer.js"></script>
</html>