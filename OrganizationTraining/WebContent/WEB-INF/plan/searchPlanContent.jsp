<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<%@ taglib  prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Search Plan</title>
</head>
<body>
<!-- Search part -->
	<div class="dataList">
		<div id="searchCondition">
			<input type="hidden" id="keyword_content" value="<s:text name="_keyword"></s:text>"></input>
			<div id="searchInputTipDiv" class="inputTipDiv"><span><s:text name="_keyword"></s:text></span></div>
			<input id="keyword" class="input_txt" name="" type="text" value="">
			<div class="filterDiv filterDiv_common">
				<button class="filterBtn filter_no_dropDown">
				</button>
				<span class="existedFlag"></span>
			    <div class='filterBox'>
			    	<div class="single_condition">
						<div class="condition_title"><s:text name="_search_by"></s:text></div>
						<div class="condition_optional" id="searchFields">
								<p><input type="checkBox" name="field" class="checked_all" value="all"/><label><s:text name="_all"></s:text></label></p>
								<p><input type="checkBox" name="field" content="<s:text name="_by_plan_id"></s:text>" value="prefix_id"/><label><s:text name="_by_plan_id"></s:text></label></p>
								<p><input type="checkBox" name="field" content="<s:text name="_by_plan_name"></s:text>" value="plan_name"/><label><s:text name="_by_plan_name"></s:text></label></p>
				                <p><input type="checkBox" name="field" content="<s:text name="_by_plan_tag"></s:text>" value="plan_tag"/><label><s:text name="_by_plan_tag"></s:text></label></p>
				                <p><input type="checkBox" name="field" content="<s:text name="_by_attendee"></s:text>" value="plan_trainers,plan_trainees"/><label><s:text name="_by_attendee"></s:text></label></p>
			            </div>
			        </div>
			        <div class="single_condition">
						<div class="condition_title"><s:text name="_search_plan_type"></s:text></div>
						<div class="condition_optional" id="planType">
								<p><input type="checkBox" name="field" value="1" content="<s:text name="Invited"></s:text>"/><label><s:text name="Invited"></s:text></label></p>
								<p><input type="checkBox" name="field" value="2" content="<s:text name="Public"></s:text>"/><label><s:text name="Public"></s:text></label></p>
			            </div>
			        </div>
			        <div class="single_condition">
						<div class="condition_title"><s:text name="_search_course_type"></s:text></div>
						<div class="condition_optional" id="courseType">
								<p><input type="checkBox" name="field" class="checked_all" value="all"/><label><s:text name="_all"></s:text></label></p>
			            </div>
			        </div>
			        <div class="single_condition">
				        <div class="left_div">
				            <div class="condition_title"><s:text name="_by_publish_time"></s:text></div>
				            <div class="condition_optional">
				                <p class="dateP"><input id="publishLowerDate" class="dateInput" type="text" name="field" content="<s:text name='_by_publishLowerDate'></s:text>: "/></p>
				                <span class="dateLine">-</span>
				                <p class="dateP"><input id="publishUpperDate" class="dateInput" type="text" name="field" content="<s:text name='_by_publishUpperDate'></s:text>: "/></p>
				            </div>
				        </div>
				        <div class="right_div">
				            <div class="condition_title"><s:text name="_by_execution_time"></s:text></div>
				            <div class="condition_optional">
				                <p class="dateP"><input id="executeLowerDate" class="dateInput" type="text" name="field" content="<s:text name='_by_executeLowerDate'></s:text>: "/></p>
				                <span class="dateLine">-</span>
				                <p class="dateP"><input id="executeUpperDate" class="dateInput" type="text" name="field" content="<s:text name='_by_executeUpperDate'></s:text>: "/></p>
				            </div>
			            </div>
			        </div>
			        <a class="reset" href="javascript:void(0);"><s:text name="_reset"></s:text></a>
			    </div>
			</div>
			<a id="searchButton" class="a_common_button a_create_button" onmouseup="className='a_common_button a_create_button'" onmousedown="className='a_common_button a_create_button_hover'">
	        	<span class="a_common_button_span" ><s:text name="_btn_search"></s:text></span>
	        </a>
	    </div>
	</div>
	<form action="<%=basePath %>plan/viewTheRecordDetail" method="post" id="viewDetailForm" ></form>
	<s:form action="" theme="simple">
	<s:hidden name="hasCondition"></s:hidden>
	<s:hidden name="fromSearchToViewCondition.nowId"></s:hidden>
	<s:hidden name="fromSearchToViewCondition.backupId"></s:hidden>
	<s:hidden name="fromSearchToViewCondition.totalPageNum"></s:hidden>
	<s:hidden name="fromSearchToViewCondition.isNoSelectedflag"></s:hidden>
	<s:hidden name="criteria.pageNum"></s:hidden>
	<s:hidden name="criteria.pageSize"></s:hidden>
	<s:hidden name="criteria.sortSign"></s:hidden>
	<s:hidden name="criteria.sortName"></s:hidden>
	<s:hidden name="criteria.queryString"></s:hidden>
	<s:hidden name="criteria.searchFields"></s:hidden>
	<s:hidden name="criteria.planTypeIds"></s:hidden>
	<s:hidden name="criteria.planCourseTypeIds"></s:hidden>
	<s:hidden name="criteria.publishLowerDate"></s:hidden>
	<s:hidden name="criteria.publishUpperDate"></s:hidden>
	<s:hidden name="criteria.executeLowerDate"></s:hidden>
	<s:hidden name="criteria.executeUpperDate"></s:hidden>
	<s:hidden name="criteria.searchOperationFlag"></s:hidden>
	</s:form>
</body>
</html>