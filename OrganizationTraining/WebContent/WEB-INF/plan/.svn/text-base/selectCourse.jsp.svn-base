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
<title>select course for plan</title>
<link type="text/css" href="<%=basePath %>filterBox/css/filterBox.css" rel="stylesheet">
<link type="text/css" href="<%=basePath %>searchList/css/dataList.css" rel="stylesheet">
<link type="text/css" href="<%=basePath %>jquery.poshytip/css/tip-green.css" rel="stylesheet">
<link type="text/css" href="<%=basePath %>css/search/searchCommon.css" rel="stylesheet">
<link type="text/css" href="<%=basePath%>css/common/commonTab.css" rel="stylesheet">
</head>
<body>
    <div id="selectCourse" class="content">
        <div class="selectCourseTitle">
            <span id="topTitle" class="line_content_span"><s:text name="_plan_select_course_title"></s:text></span>
            <img id="closePic" src="<%=basePath %>image/buttonIMG/ICN_Close_11x11.png">
            <span class="clear"></span>
        </div> 
        
        <div id="selectCourseMenu"></div>
        <div class="courseDataList">
            <div id="selectCourseList" class="selectOptionList">
                <div class="dataListOfCourse">
		            <div id="searchCondition">
		            <input type="hidden" id="keyword_content" value="<s:text name="_keyword"></s:text>"></input>
		            <div id="searchInputTipDiv" class="inputTipDiv"><span><s:text name="_keyword"></s:text></span></div>
					<input id="keyword" class="input_txt" name="" type="text" value="">
			        <div class="filterDiv filterDiv_common">
				    <span class="filterBtn filter_no_dropDown" href="javascript:;">
				    </span>
				    <span class="existedFlag"></span>
			        <div class='filterBox'>
			    	<div class="single_condition">
						<div class="condition_title"><s:text name="_search_by"></s:text></div>
						<div class="condition_optional" id="searchFields">
						        <p><input type="checkBox" name="field" class="checked_all" value="all"/><label><s:text name="_all"></s:text></label></p>
								<p><input type="checkBox" name="field" content="<s:text name="_by_id"></s:text>" value="prefix_id"/><label><s:text name="_by_id"></s:text></label></p>
								<p><input type="checkBox" name="field" content="<s:text name="_by_name"></s:text>" value="course_name"/><label><s:text name="_by_name"></s:text></label></p>
				                <p><input type="checkBox" name="field" content="<s:text name="_by_brief"></s:text>" value="course_brief"/><label><s:text name="_by_brief"></s:text></label></p>
				                <p><input type="checkBox" name="field" content="<s:text name="_by_target_attendees"></s:text>" value="course_target_trainee"/><label><s:text name="_by_target_attendees"></s:text></label></p>
				                <p><input type="checkBox" name="field" content="<s:text name="_by_tag"></s:text>" value="course_tag"/><label><s:text name="_by_tag"></s:text></label></p>
				                <p><input type="checkBox" name="field" content="<s:text name="_by_author"></s:text>" value="course_author_name"/><label><s:text name="_by_author"></s:text></label></p>
				                <p><input type="checkBox" name="field" content="<s:text name="_by_history_trainer"></s:text>" value="course_history_trainer"/><label><s:text name="_by_history_trainer"></s:text></label></p>
			            </div>
			        </div>
			        <div class="single_condition">
						<div class="condition_title"><s:text name="_search_course_type"></s:text></div>
						<div class="condition_optional" id="courseTypeCourse">
								<p><input type="checkBox" name="field" class="checked_all" value="all"/><label><s:text name="_all"></s:text></label></p>
			            </div>
			        </div>
			        <div class="single_condition">
						<div class="condition_title"><s:text name="_if_certificated"></s:text></div>
						<div class="condition_optional" id="isCertificateds">
								<p><input type="checkBox" name="field" value="1" content="<s:text name='_if_certificated'></s:text>: <s:text name="_btn_yes"></s:text>" /><label><s:text name="_btn_yes"></s:text></label></p>
								<p><input type="checkBox" name="field" value="0" content="<s:text name='_if_certificated'></s:text>: <s:text name="_btn_no"></s:text>" /><label><s:text name="_btn_no"></s:text></label></p>
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
            </div>
            
            <div id="selectProgramList" class="selectOptionList">program list</div>
            
            <div id="selectPlanList" class="selectOptionList">
				<div class="dataListOfPlan">
					<div id="searchConditionOfPlan">
						<div id="searchPlanInputTipDiv" class="inputTipDiv"><span><s:text name="_keyword"></s:text></span></div>
						<input id="keywordOfPlan" class="input_txt" name="" type="text" value="">
						<div class="filterDivPlan filterDiv_common">
							<span class="filterBtn filter_no_dropDown">
							</span>
							<span class="existedFlag"></span>
						    <div class='filterBox'>
						    	<div class="single_condition">
									<div class="condition_title"><s:text name="_search_by"></s:text></div>
									<div class="condition_optional" id="searchFieldsOfPlan">
											<p><input type="checkBox" name="field" class="checked_all" value="all"/><label><s:text name="_all"></s:text></label></p>
											<p><input type="checkBox" name="field" content="<s:text name="_by_plan_id"></s:text>" value="prefix_id"/><label><s:text name="_by_plan_id"></s:text></label></p>
											<p><input type="checkBox" name="field" content="<s:text name="_by_plan_name"></s:text>" value="plan_name"/><label><s:text name="_by_plan_name"></s:text></label></p>
							                <p><input type="checkBox" name="field" content="<s:text name="_by_plan_tag"></s:text>" value="plan_tag"/><label><s:text name="_by_plan_tag"></s:text></label></p>
							                <p><input type="checkBox" name="field" content="<s:text name="_by_course_name"></s:text>" value=""/><label><s:text name="_by_course_name"></s:text></label></p>
							                <p><input type="checkBox" name="field" content="<s:text name="_by_attendee"></s:text>" value="plan_trainers"/><label><s:text name="_by_attendee"></s:text></label></p>
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
									<div class="condition_optional" id="courseTypePlan">
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
						<a id="searchButtonOfPlan" class="a_common_button a_create_button" onmouseup="className='a_common_button a_create_button'" onmousedown="className='a_common_button a_create_button_hover'">
				        	<span class="a_common_button_span" ><s:text name="_btn_search"></s:text></span>
				        </a>
				    </div>
				</div>
            </div>
        </div>
        <div id="selectedCourseLabel" class="line_content_span">
        	<span id="tiemCount">0</span><s:text name="_plan_selected_result"></s:text>
		</div>
        <div class="selectCourseResult">
                
                <div id="selectCourseResultSpan">
                    <div id="courseResultLeft" class="resultLeftDisable"></div>
                    <div class="resultMidContent">
                        <div id="slideCourseResult"></div>
                    </div>
                    <div id="courseResultRight" class="resultRightDisable"></div>
                    <div class="clear"></div>
                </div>
                <div class="clear"></div>
        </div>
        <div class="selectCourseOperates">
            <a class="a_common_button a_create_button" id="confirmButton">
                <span class="a_common_button_span"><s:text name="_btn_confirm"></s:text> </span>
            </a>
            <a class="a_common_button a_cancel_button" id="cancelButton">
                <span class="a_common_button_span"><s:text name="_btn_cancel"></s:text> </span>
            </a>
        </div>
    </div>
</body>
    <script type="text/javascript" src="<%=basePath %>jquery.poshytip/js/jquery.poshytip.js"></script>
    <script type="text/javascript" src="<%=basePath %>js/common/commonTab.js"></script>
    <script type="text/javascript" src="<%=basePath %>searchList/js/DataList.js"></script>
    <script type="text/javascript" src="<%=basePath%>filterBox/js/filterBox.js"></script>
    <!-- 
    <script type="text/javascript" src="<%=basePath%>js/search/searchCommon.js"></script>
    <script type="text/javascript" src="<%=basePath %>js/plan/selectCourseCommon.js" ></script>
    <script type="text/javascript" src="<%=basePath %>js/plan/selectCourse.js" ></script>
     -->
    <script type="text/javascript" src="<%=basePath %>js/actualCourse/selectPlanCoure.js" ></script>
    <script type="text/javascript" src="<%=basePath %>js/actualCourse/selectPlanCourseCommon.js" ></script>
</html>