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
<title>Insert title here</title>
<link rel="stylesheet" href="<%=basePath%>css/dashboard/toDoList.css">
</head>
<body>
<div class="to_do_list">
	<div id="to_do_loader" class="todo-div-loader"></div>
	<div class="table_header_left_corner"></div>
    <div id="toDoList_header" class="table_header">
        <div class="toDoList_title">
            <s:text name="to_do_list" />
        </div>
        <div class="toDoList_count">
			<div class="count_left"></div>
			<div id="toDoList_count_num" class="count_mid"></div>
			<div class="count_right"></div>
			<div class="clear"></div>
		</div>
        <div id="todoList_refresh" class="toDoList_refresh"></div>
    </div>
    <div class="table_header_right_corner"></div>
    <div class="clear"></div>
	<div class="training_master_part">
		<div id="master_todo_loading" class="todo-div-loader"></div>
		<div id="master_part_title" class="role_part_title">
			<span><s:text name="dashboard_training_master"></s:text></span>
			<div id="master_page_button" class="page_button">
				<div id="master_page_left" class="toDo_page_left"></div>
				<div id="master_page_right" class="toDo_page_right"></div>
				<div class="clear"></div>
			</div>
		</div>
		<div id="training_master_content" class="prepare_content">
		
		</div>
	</div>
	<div class="trainer_part">
		<div id="trainer_todo_loading" class="todo-div-loader"></div>
		<div id="trainer_part_title" class="role_part_title">
			<span><s:text name="dashboard_trainer"></s:text></span>
			<div id="trainer_page_button" class="page_button">
				<div id="trainer_page_left" class="toDo_page_left"></div>
				<div id="trainer_page_right" class="toDo_page_right"></div>
				<div class="clear"></div>
			</div>
		</div>
		<div id="trainer_content"  class="prepare_content"></div>
	</div>
	<div class="trainee_part">
		<div id="trainee_todo_loading" class="todo-div-loader"></div>
		<div id="trainee_part_title" class="role_part_title">
			<span><s:text name="dashboard_trainee"></s:text></span>
			<div id="trainee_page_button" class="page_button">
				<div id="trainee_page_left" class="toDo_page_left"></div>
				<div id="trainee_page_right" class="toDo_page_right"></div>
				<div class="clear"></div>
			</div>
		</div>
		<div id="trainee_content"  class="prepare_content"></div>
	</div>
</div>

<script type="text/javascript" src="<%=basePath %>js/dashboard/toDoList.js"></script>
</body>
</html>