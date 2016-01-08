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
<title><s:text name="_assign_role"></s:text></title>
<link type="text/css" href="<%=basePath %>searchList/css/dataList.css" rel="stylesheet">
<link type="text/css" href="<%=basePath %>css/search/searchCommon.css" rel="stylesheet">
<link type="text/css" href="<%=basePath %>filterBox/css/filterBox.css" rel="stylesheet">
<link type="text/css" href="<%=basePath %>jquery.poshytip/css/tip-green.css" rel="stylesheet">
<link rel="stylesheet" href="<%=basePath%>messageBar/css/messagebar.css" type="text/css">
<link rel="stylesheet" href="<%=basePath%>jquery.ui/css/jquery-ui-1.8.18.custom.css">
<link rel="stylesheet" href="<%=basePath%>confirmBar/css/confirmDialog.css">
<link type="text/css" href="<%=basePath %>css/roleList/roleList.css" rel="stylesheet">
</head>
<body>
<input type="hidden" id="roleListTip" name="roleListTip" value="<s:text name="_role_list_tip"></s:text>"></input>
<jsp:include page="/WEB-INF/common/header.jsp"></jsp:include>
<!-- message bar -->
<div id ="messageBar"></div>
<span id="messageBar_save" class="message_span"><s:text name="_role_list_message_for_save"></s:text></span>
<!-- alert for error -->
<div id ="alertForError"></div>
<span id="role_list_tip_for_save" class="message_span"><s:text name="_role_list_tip_for_save"></s:text></span>
<span id="role_list_tip_for_reset" class="message_span"><s:text name="_role_list_tip_for_reset"></s:text></span>
<span id="role_list_tip_for_other" class="message_span"><s:text name="_role_list_tip_for_other"></s:text></span>
<!-- main content -->
<div class="content">
	<div class="dataList">
		<div id="searchCondition">
			<div id="searchInputTipDiv" class="inputTipDiv"><span><s:text name="_keyword"></s:text></span></div>
			<input id="keyword" class="input_txt" name="" type="text" value="">
			<div class="filterDiv filterDiv_common">
				<button class="filterBtn filter_no_dropDown"></button>
				<span class="existedFlag"></span>
				<div class="filterBox">
					<div class="first_single_condition">
						<div class="condition_title"><s:text name="_role_list_search_field"></s:text></div>
						<div class="condition_optional" id="searchFields">
								<p><input type="checkBox" name="field" class="checked_all" value="all"><label><s:text name="_role_list_all"></s:text></label></p>
								<p><input type="checkBox" name="field" content="ID" value="employeeId"><label><s:text name="_role_list_job_number"></s:text></label></p>
								<p><input type="checkBox" name="field" content="Name" value="employeeName"><label><s:text name="_role_list_name"></s:text></label></p>
								
						</div>
					</div>
					<div class="last_single_condition">
						<div class="condition_title"><s:text name="_role_list_role"></s:text></div>
						<div class="condition_optional" id="roleType">
								<p><input type="checkBox" name="field" class="checked_all" value="all" ><label><s:text name="_role_list_all"></s:text></label></p>
								<p><input type="checkBox" name="field" content="Trainer" value="Trainer" ><label><s:text name="_role_list_trainer"></s:text></label></p>
								<p><input type="checkBox" name="field" content="Master" value="Training Master" ><label><s:text name="_role_list_master"></s:text></label></p>
								<p><input type="checkBox" name="field" content="Admin" value="Admin" ><label><s:text name="_role_list_admin"></s:text></label></p>
						</div>
					</div>
					<a class="reset" href="javascript:void(0);"><s:text name="_role_list_reset"></s:text></a>
				</div>
			</div>
			<a id="searchButton" class="a_common_button a_create_button" onmouseup="className='a_common_button a_create_button'" onmousedown="className='a_common_button a_create_button_hover'">
				<span class="a_common_button_span"><s:text name="_role_list_search"></s:text></span>
			</a>
		</div>
		<div class="dataList-child">
			<div class="dataList-div-loader">
			</div>
		</div>
		<div class="dataList-child">
			<div class="clear"></div>
		</div>
		<div class="dataList-child dataList-div-data">
			<div class="dataList-div-head">
				<div class="w-30"></div>
				<div class="dataList-div-listLine w-2"></div>
				<div class="w-58"><span en="employeeId"><s:text name="_role_list_job_number"></s:text></span></div>
				<div class="dataList-div-listLine w-2"></div>
				<div class="w-118"><span en="employeeName"><s:text name="_role_list_name"></s:text></span></div>
				<div class="dataList-div-listLine w-2"></div>
				<div class="w-300"><span en="undefined"><s:text name="_role_list_email"></s:text></span></div>
				<div class="dataList-div-listLine w-2"></div>
				<div class="w-68"><span en="undefined"><s:text name="_role_list_trainer"></s:text></span></div>
				<div class="dataList-div-listLine w-2"></div>
				<div class="w-68"><span en="undefined"><s:text name="_role_list_master"></s:text></span></div>
				<div class="dataList-div-listLine w-2"></div>
				<div class="w-70"><span en="undefined"><s:text name="_role_list_admin"></s:text></span></div>
			</div>
			<div class="dataList-div-body-left-corner"></div>
			<div class="dataList-div-body-right-corner"></div>
			<div class="dataList-div-body">
				<div class="dateList-div-record">
					
				</div>
			</div>
		</div>
		<div class="dateList-child dataList-div-button">
			<a id="saveButton" class="a_common_button a_create_button a_adjust_right">
				<span class="a_common_button_span"><s:text name="_role_list_save"></s:text></span>
			</a>
			<a id="resetButton" class="a_common_button a_cancel_button a_adjust_left">
				<span class="a_common_button_span"><s:text name="_role_list_reset"></s:text></span>
			</a>
		</div>
		<div class="dataList-child dataList-div-pagination">
			<div class="dataList-div-perPage"><span>1</span> - <span>10</span> <s:text name="_role_list_total_page"></s:text> <span>11</span></div>
			<div class="dataList-div-pageSize-wrapper">
				<div class="dataList-div-pageSize"><div>10</div><div>15</div><div>20</div></div>
				<a class="dataList-a-pageSize w40-h24" href="javascript:;">10</a> <s:text name="_role_list_each_page"></s:text> 
			</div>
			<div class="dataList-div-page">
				<a href="javascript:;" class="dataList-a-first-disable w-h-24"></a>
				<a href="javascript:;" class="dataList-a-previous-disable w-h-24"></a>
				<a href="javascript:;" class="dataList-a-page currentPage"></a>
				<a href="javascript:;" class="dataList-a-page">2</a>
				<a href="javascript:;" class="dataList-a-next w-h-24"></a>
				<a href="javascript:;" class="dataList-a-last w-h-24"></a>
			</div>
			<div class="dataList-div-go">
				<input type="text" class="dataList-input-go w30-h24"/>
				<a href="javascript:;" class="dataList-button-go w-h-24"></a>
			</div>
		</div>
		<div class="clear"></div>
	</div>
</div>
<jsp:include page="/WEB-INF/common/footer.jsp"></jsp:include>
</body>
<script type="text/javascript" src="<%=basePath %>/jquery.ui/js/jquery-ui-1.8.18.custom.min.js"></script>
<script type="text/javascript" src="<%=basePath %>confirmBar/js/commonConfirmBar.js"></script>
<script type="text/javascript" src="<%=basePath %>jquery.poshytip/js/jquery.poshytip.js"></script>
<script type="text/javascript" src="<%=basePath%>filterBox/js/filterBox.js"></script>
<script type="text/javascript" src="<%=basePath%>js/roleList/roleList.js"></script>
<script type="text/javascript" src="<%=basePath %>messageBar/js/messagebar.js"></script>
</html>
