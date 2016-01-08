<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<%@ taglib  prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>New public plan</title>
<link rel="stylesheet" href="<%=basePath%>css/dashboard/newPublicPlan.css">
</head>
<body>
<!-- main content -->
<div class="new_public_plan">
	<div class="dataList-div-loader"></div>
	<div class="new_publish_table_header">
		<div class="table_header_left new_publish_table_header_left">
			<div class="table_header_theme"><s:text name="new_public_plan_title"></s:text></div>
			<div id="new_publish_plan_record_total" class="table_header_record_total_num_for_long">
				<div class="record_total_num_for_long_left"></div>
				<div class="record_total_num_for_long_middle"></div>
				<div class="record_total_num_for_long_right"></div>
			</div>
		</div>
		<div class="table_header_right">
			<a id="new_publish_plan_refresh" class="refresh table_header_button"></a>
			<a id="new_publish_plan_previous" class="previous table_header_button" ></a>
			<a id="new_publish_plan_next" class="next table_header_button"></a>
		</div>
	</div>
	<div class="new_publish_table_header_left_corner"></div>
	<div class="new_publish_table_header_right_corner"></div>
	<div class="table_body" id="new_publish_plan_table">
		<!-- Plan detail item -->
		<div class="table_record_detail"></div>
		<div class="table_record_detail"></div>
		<div class="table_record_detail"></div>
		<div class="table_record_detail"></div>
	</div>
	<input type="hidden" id="new_publish_plan_total_pages" readonly/>
	<input type="hidden" id="new_publish_plan_page_now" readonly/>
</div>
<div class="table_record"  id="new_publish_plan_item_temp" style="display:none">
	<div class="table_record_detail">
		<div class="table_record_detail_left">
			<div class="table_record_detail_left_one_layer">
				<a class="table_record_detail_left_one_layer_blue"></a></div>
				<div class="table_record_detail_left_two_layer"></div>
			</div>
		<div class="table_record_detail_right">
			<a class="packUp"></a>
		</div>
	</div>
</div>
<!-- Course List Template-->
<div class="table_record_additional" id="new_publish_course_item_temp" style="display:none">
	<div class="table_record_main">
		<div class="table_record_course" style="display:none">
			<div class="table_record_course_left"><a class="table_record_left_one_layer_blue"></a></div>
			<div class="table_record_course_middle"></div>
			<div class="table_record_course_right"></div>
		</div>
	</div>
	<div class="table_record_course_last">
		<a class="join_all"><s:text name="join_all_button"></s:text></a>
		<a class="quit_all"><s:text name="quit_all_button"></s:text></a>
	</div>
</div>
</body>
<script type="text/javascript" src="<%=basePath %>js/dashboard/newPublicPlan.js"></script>
<script type="text/javascript" src="<%=basePath %>js/dashboard/joinOrQuit.js"></script>
</html>
