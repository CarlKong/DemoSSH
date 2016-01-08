$(document).ready(function() {
//	var tabObjs = [{id:"selectCourseTab", name:getTabCourseList()}, 
//                   {id:"selectProgramTab", name:getTabProgramList()}, 
//                   {id:"selectPlanTab", name:getTabPlanList()}];
	var tabObjs = [{id:"selectCourseTab", name:getTabCourseList()}, 
                   {id:"selectPlanTab", name:getTabPlanList()}];
    initTab(960, 110, tabObjs, $("#selectCourseMenu"), 1);
    $("#selectCourseTab").click(function(){
		$("#selectCourseList").show();
		$("#selectProgramList").hide();
		$("#selectPlanList").hide();
		initCourseTypeInfo("Course");
		initSearchList(3);
		clickOptionTab($(this), $("#selectCourseMenu"));
		$("#selectCoursePlan").attr("selectedResult", "Course");
	});
//	$("#selectProgramTab").click(function(){
//		$("#selectCourseList").hide();
//		$("#selectProgramList").show();
//		$("#selectPlanList").hide();
//		clickOptionTab($(this), $("#selectCourseMenu"));
//		$("#selectCoursePlan").attr("selectedResult", "Program");
//	});
	$("#selectPlanTab").click(function(){
		$("#selectCourseList").hide();
		$("#selectProgramList").hide();
		$("#selectPlanList").show();
		initCourseTypeInfo("Plan");
		initSearchList(4);
		clickOptionTab($(this), $("#selectCourseMenu"));
		$("#selectCoursePlan").attr("selectedResult", "Plan");
	});
	
	$("#selectCoursePlan").click(function(){
		var scrollHeight = 140 - (document.documentElement.clientHeight - 540) / 2;
		if ($("#slideCourseResult").children().length > 0) {
			$("#slideCourseResult").children().remove();
		}
		$("#slideCourseResult").css("margin-left","0");
		showLayer($("#selectCourseDiv"));
		initSelectedCourseTab();
		$("html,body").animate({scrollTop:scrollHeight},50);
		initSearchList(3);
	});
	$("#closePic, #cancelButton").click(function() {
		closeSelectPopup();
    });
	$("#confirmButton").click(function(){
		closeSelectPopup();
		confirmCourses();
	});
	$("#searchButton").click(function() {
    	setCourseCriteria();
    	courseDataList.search();
    });
	//initialize keyword of course
	initKeyword($("#keyword"), $("#searchInputTipDiv"));
	//initialize keyword of plan
	initKeyword($("#keywordOfPlan"), $("#searchPlanInputTipDiv"));

    $("#searchButtonOfPlan").click(function() {
    	setPlanCriteria();
    	planDataList.search();
    });
    $("#courseResultLeft").click(function() {
    	leftSlide();
    });
    $("#courseResultRight").click(function() {
    	rightSlide();
    });
    
	// to view 
    $(".viewDetail").live("click",function(){
    	var courseId = $(this).attr("courseId");
    	if (courseId === undefined || "" === courseId || "undefined" === courseId) {
    		var planId = $(this).attr("planId");
        	var plan_ID = parseInt(planId);
        	window.open($('#basePath').val()+'plan/viewPlanDetail?planId='+plan_ID+'&operationFlag=searchResult');
    	} else {
    		var course_ID = parseInt(courseId);
        	window.open($('#basePath').val()+'course/viewCourseDetail?courseId='+course_ID);
    	}
    });
    
    $("#orderButton").click(function(){
    	orderCourseListByTime();
    });
	
});

function closeSelectPopup() {
	removeAllMark();
	closeLayer($("#selectCourseDiv"));
	$("html,body").animate({scrollTop:$("#selectCoursePlan").offset().top},50);
	$(".selectOptionList").hide();
	$("#tiemCount").text(0);
	initSlideButton();
}

function initSelectedCourseTab() {
	if (!($("#selectCoursePlan").attr("selectedResult")) ||
			$("#selectCoursePlan").attr("selectedResult") == "Course" ||
			$("#selectCoursePlan").attr("selectedResult") == "") {
		initCourseTypeInfo("Course");
		$("#selectCourseList").show();
		return;
	}
	if ($("#selectCoursePlan").attr("selectedResult") == "Program") {
		$("#selectCourseList").show();
		return;
	}
	if ($("#selectCoursePlan").attr("selectedResult") == "Plan") {
		initCourseTypeInfo("Plan");
		$("#selectPlanList").show();
		return;
	}
}