$(document).ready(function (){
	//init plan tab is selected
	var tabObjs = [{id:"assess_plan_tab", name:getPlanLabel()}, {id:"assess_course_tab", name:getCourseLabel()}
		, {id:"assess_trainee_tab", name:getTraineeLabel()}, {id:"assess_trainer_tab", name:getTrainerLabel()}];
	initTab(960, 110, tabObjs, $("#assessment_type_menu"), 1);
	
	$("#assess_plan_tab").removeClass("unselectedTabBkg");
	$("#assess_to_plan_part").show();
	//bind click 
	$("#assess_plan_tab").bind("click", function(){
		clickOptionTab($(this), $("#assessment_type_menu"));
		$(".assessment_show_part").hide();
		$("#assess_to_plan_part").show();
	});
	$("#assess_course_tab").bind("click", function(){
		clickOptionTab($(this), $("#assessment_type_menu"));
		$(".assessment_show_part").hide();
		$("#assess_to_course_part").show();
		getPlanCourseListNeedAssessed($("#basePath").val()+"assessment/getActualCourseNeedAssessment", $("#planId").val());
	});
	$("#assess_trainee_tab").bind("click", function(){
		clickOptionTab($(this), $("#assessment_type_menu"));
		$(".assessment_show_part").hide();
		$("#assess_to_trainee_part").show();
		viewPlanCourseListAndTraineeList($("basePath") + "assessment/viewActualCourseListAndTraineeList", $("#planId").val());
	});
	$("#assess_trainer_tab").bind("click", function(){
		clickOptionTab($(this), $("#assessment_type_menu"));
		$(".assessment_show_part").hide();
		$("#assess_to_trainer_part").show();
		viewTrainerList($("basePath") + "assessment/viewTrainerAndActualCourseListByPlanId", $("#planId").val());
	});
	//bind fold and expand
	clickFoldOrExpand($("#trainer_to_course_expand"), $("#trainer_assess_planCourse_value"), "subtitle_expand", "subtitle_fold"
			, $("#assess_to_course_part"), $("#plan_course_assessment_left"), $("#planCourse_assessment_content"));
	clickFoldOrExpand($("#trainee_to_course_expand"), $("#planCourse_assessment_detail"), "subtitle_expand", "subtitle_fold"
			, $("#assess_to_course_part"), $("#plan_course_assessment_left"), $("#planCourse_assessment_content"));
	clickFoldOrExpand($("#planCourse_courseList_menu"), $("#planCourse_child_menu_div"), "", ""
			, $("#assess_to_course_part"), $("#plan_course_assessment_left"), $("#planCourse_assessment_content"));
});

/**
 * draw average point
 * @param point (double)
 * @param parentContent
 * @return
 */
function showAveragePoint(point, parentContent) {
	var integer_part = parseInt(point);
	var decimal_part = point - integer_part;
	var gray_count = parseInt(5-point);
	for (i=0; i<integer_part; i++) {
		var $lightStar = $("<span></span>").appendTo(parentContent);
		$lightStar.addClass("light_star");
	}
	if (0 < decimal_part && decimal_part < 0.25) {
		var $uncertaintyStar = $("<span></span>").appendTo(parentContent);
		$uncertaintyStar.addClass("gray_star");
	}
	if (0.25 <= decimal_part && decimal_part < 0.75) {
		var $uncertaintyStar = $("<span></span>").appendTo(parentContent);
		$uncertaintyStar.addClass("half_star");
	}
	if (0.75 <= decimal_part && decimal_part <1 ) {
		var $uncertaintyStar = $("<span></span>").appendTo(parentContent);
		$uncertaintyStar.addClass("light_star");
	}
	for (i=0; i<gray_count; i++) {
		var $grayStar = $("<span></span>").appendTo(parentContent);
		$grayStar.addClass("gray_star");
	}
}

/**
 * click Fold Or Expand
 * @param $button
 * @param $expandArea
 * @param expandClass
 * @param foldClass
 * @param $parentPart
 * @param $leftPart
 * @param $rightPart
 * @return
 */
function clickFoldOrExpand($button, $expandArea, expandClass, foldClass, $parentPart, $leftPart, $rightPart) {
	$button.attr("expand", "1");
	$button.unbind("click");
	$button.bind("click", function(){
		//if $expandArea is expand before click
		if ($button.attr("expand") == "1") {
			$button.attr("expand", "0");
			$expandArea.hide();
			$button.addClass(expandClass).removeClass(foldClass);
			getHeightOfSplit($parentPart, $leftPart, $rightPart);
			return;
		}
		//if $expandArea is fold before click
		if ($button.attr("expand") == "0") {
			$button.attr("expand", "1");
			$expandArea.show();
			$button.addClass(foldClass).removeClass(expandClass);
			getHeightOfSplit($parentPart, $leftPart, $rightPart);
			return;
		}
	});
}

/**
 * Send ajax request to get assessment info
 * @param url
 * @param searchCritera
 * @param callBack
 * @return
 */
function getAssessmentInfo(url, searchCritera, callBack, otherParam) {
	$.ajax ({
		type: "POST",
        url: url,
        data: searchCritera,
        success: function(data) {
			if (handleException(data)) {
				callBack(data, searchCritera, url, otherParam);
			}
        }
	});
}

/**
 * Pagination function of view all assessments
 * @param $parentContent 
 * @param currentPage int
 * @param totalPage int
 * @return
 */
function pagination($parentContent, currentPage, totalPage, url, searchCritera, callBack, otherParam) {
	$parentContent.find(".view_assessment_page").remove();
	var $paginationDiv = $("<div>",{"class":"view_assessment_page"}).appendTo($parentContent);
	//Go to first page and previous page.
	var $firstDiv;
	var $previousDiv;
	if (currentPage == 1) {
		$firstDiv = $("<a>", {"href":"javascript:;", "class":"first_page_disable"}).appendTo($paginationDiv);
		$previousDiv = $("<a>", {"href":"javascript:;", "class":"previous_page_disable"}).appendTo($paginationDiv);
	}
	if (currentPage > 1 && currentPage <= totalPage) {
		$firstDiv = $("<a>", {"href":"javascript:;", "class":"first_page_active"}).appendTo($paginationDiv);
		$previousDiv = $("<a>", {"href":"javascript:;", "class":"previous_page_active"}).appendTo($paginationDiv);
		$firstDiv.bind("click", function(){
			searchCritera["viewAssessmentCondition.nowPage"] = 1;
			getAssessmentInfo(url, searchCritera, callBack, otherParam);
		});
		$previousDiv.bind("click", function(){
			searchCritera["viewAssessmentCondition.nowPage"] = searchCritera["viewAssessmentCondition.nowPage"] - 1;
			getAssessmentInfo(url, searchCritera, callBack, otherParam);
		});
	}
	//show page number and current page.
	for (i=1; i<=totalPage; i++) {
		var $pageShowDiv = $("<a>", {"href":"javascript:;", "text": i}).appendTo($paginationDiv);
		if (i == currentPage) {
			$pageShowDiv.addClass("current_page");
		} else {
			$pageShowDiv.bind("click", function(){
				
			});
		}
	}
	//Go to next and last page.
	var $nextPageDiv;
	var $lastPageDiv;
	if (currentPage == totalPage) {
		$nextPageDiv = $("<a>", {"href":"javascript:;", "class":"next_page_disable"}).appendTo($paginationDiv);
		$lastPageDiv = $("<a>", {"href":"javascript:;", "class":"last_page_disable"}).appendTo($paginationDiv);
	}
	if (currentPage > 0 && currentPage < totalPage) {
		$nextPageDiv = $("<a>", {"href":"javascript:;", "class":"next_page_active"}).appendTo($paginationDiv);
		$lastPageDiv = $("<a>", {"href":"javascript:;", "class":"last_page_active"}).appendTo($paginationDiv);
		$nextPageDiv.bind("click", function(){
			searchCritera["viewAssessmentCondition.nowPage"] = searchCritera["viewAssessmentCondition.nowPage"] + 1;
			getAssessmentInfo(url, searchCritera, callBack, otherParam);
		});
		$lastPageDiv.bind("click", function(){
			searchCritera["viewAssessmentCondition.nowPage"] = totalPage;
			getAssessmentInfo(url, searchCritera, callBack, otherParam);
		});
	}
}

/**
 * get Height Of Split line.
 * @param $parentPart ->parent node of this split line.
 * @param $leftPart	  ->left part of split line.
 * @param $rightPart  ->right part of split line.
 * @return
 */
function getHeightOfSplit($parentPart, $leftPart, $rightPart) {
	var leftHeight = $leftPart.height();
	var rightHeight = $rightPart.height();
	if (leftHeight > rightHeight) {
		$parentPart.find(".split_line").height(leftHeight+"px");
	}
	else {
		$parentPart.find(".split_line").height(rightHeight+"px");
	}
}

function drawAssessorNotGiven(assessorList, $contentDiv) {
	$contentDiv.children().remove();
	$.each(assessorList, function(i, employeeName){
		var $nameDiv = $("<div></div>").appendTo($contentDiv);
		$("<span></span>").appendTo($nameDiv).addClass("prifixClass");
		$("<span></span>").appendTo($nameDiv).css({"color":"#555555"}).text(employeeName);
		
	});
}

function clickAssessorButton($button, $showDiv) {
	if($button.attr("show") == "hide") {
		$button.attr("show", "show");
		$showDiv.fadeIn("slow");
	}
	else if ($button.attr("show") == "show") {
		$button.attr("show", "hide");
		$showDiv.fadeOut("slow");
	}
}
