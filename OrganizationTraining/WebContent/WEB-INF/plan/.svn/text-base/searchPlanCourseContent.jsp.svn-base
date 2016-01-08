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
<title>Search Course Content</title>
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
						<div class="condition_title"><s:text name="_search_by"></s:text> </div>
						<div class="condition_optional" id="searchFields">
								<p><input type="checkBox" name="field" class="checked_all" value="all"/><label><s:text name="_all"></s:text></label></p>
								<p><input type="checkBox" name="field" content="<s:text name="_by_id"></s:text>" value="prefix_id"/><label><s:text name="_by_id"></s:text></label></p>
								<p><input type="checkBox" name="field" content="<s:text name="_by_name"></s:text>" value="name"/><label><s:text name="_by_name"></s:text></label></p>
				                <p><input type="checkBox" name="field" content="<s:text name="_by_brief"></s:text>" value="brief_without_tag"/><label><s:text name="_by_brief"></s:text></label></p>
				                <p><input type="checkBox" name="field" content="<s:text name="_by_target_attendees"></s:text>" value="target_trainee"/><label><s:text name="_by_target_attendees"></s:text></label></p>
			            </div>
			        </div>
			        <a class="reset" href="javascript:void(0);"><s:text name="_reset"></s:text></a>
			    </div>
			</div>
			<a id="searchButton" class="a_common_button a_create_button" onmouseup="className='a_common_button a_create_button'" onmousedown="className='a_common_button a_create_button_hover'">
	        	<span class="a_common_button_span" ><s:text name="_btn_search"></s:text> </span>
	        </a>
	    </div>
	</div>
</body>
</html>