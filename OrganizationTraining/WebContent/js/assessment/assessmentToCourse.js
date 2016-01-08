$(document).ready(function(){
	$("#assess2course_assessorButton").attr("show","hide").bind("click", function(){
		clickAssessorButton($(this), $("#show_trainee2course_name"));
	});
});

function getPlanCourseListNeedAssessed(url, planId) {
	$.ajax ({
		type: "POST",
        url: url,
        data: {
			"planId" : planId
		},
        success: function(data) {
			if (handleException(data)) {
				getPlanCourseListCallBack(data, planId);
			}
        }
	});
}

function getPlanCourseListCallBack(data, planId) {
	$("#planCourse_child_menu_div").children().remove();
	if (data.noCourse && data.noCourse==0) {
		$("#planCourse_child_menu_div").text(getPlanNoCourseMsg());
	}
	$.each(data, function(i, planCourseInfo) {
		var $childMenu = $("<div class='child_menu'></div>").appendTo($("#planCourse_child_menu_div"));
		$childMenu.html(planCourseInfo.courseName);
		$childMenu.attr("trainee2Trainer", planCourseInfo.courseInfo.trainee2Trainer);
		$childMenu.attr("trainer2Trainee", planCourseInfo.courseInfo.trainer2Trainee);
		//if the course don't need assessment.
		if (planCourseInfo.trainee2Trainer==0 && planCourseInfo.trainer2Trainee==0) {
			$childMenu.css("color", "#555");
		} 
		$childMenu.attr("planCourseId", planCourseInfo.actualCourseId);
		$childMenu.attr("prefixId", planCourseInfo.prefixIdValue);
		$childMenu.bind("click", function(){
			$("#planCourse_child_menu_div").children().removeClass("selected_menu");
			$(this).addClass("selected_menu");
			$("#assessed_planCourse_name").html($(this).html());
			$("#assessed_planCourse_id").text("("+$(this).attr("prefixid")+")");
			if ($childMenu.attr("trainer2Trainee") == 1) {
				getTrainerToCourseInfo($("#basePath").val()+"assessment/viewTrainerToCourseInfo", planId, $(this).attr("plancourseid"));
			} else {
				$("#trainer_assess_planCourse_value").text(getNotNeedAssessmentMsg());
			}
			if ($childMenu.attr("trainee2Trainer") == 1) {
				getTraineesToCourseInfo($("#basePath").val()+"assessment/viewTraineesToCourseInfo", planId, $(this).attr("plancourseid"),
					viewAssessmentPageContent.firstPageNum, viewAssessmentPageContent.pageSize);
			} else {
				$("#planCourse_average_part_value").text(getNotNeedAssessmentMsg());
			}
		});
	});
	$firstChildNode = $("#planCourse_child_menu_div").children().eq(0);
	$firstChildNode.addClass("selected_menu");
	$("#assessed_planCourse_name").html($firstChildNode.html());
	$("#assessed_planCourse_id").text("("+$firstChildNode.attr("prefixid")+")");
	if ($firstChildNode.attr("trainer2Trainee") == 1) {
		getTrainerToCourseInfo($("#basePath").val()+"assessment/viewTrainerToCourseInfo", planId, $firstChildNode.attr("plancourseid"));
	} else {
		$("#trainer_assess_planCourse_value").text(getNotNeedAssessmentMsg());
	}
	if ($firstChildNode.attr("trainee2Trainer") == 1) {
		getTraineesToCourseInfo($("#basePath").val()+"assessment/viewTraineesToCourseInfo", planId, $firstChildNode.attr("plancourseid"), 
			viewAssessmentPageContent.firstPageNum, viewAssessmentPageContent.pageSize);
	} else {
		$("#planCourse_average_part_value").text(getNotNeedAssessmentMsg());
	}
}

function getTrainerToCourseInfo(url, planId, planCourseId) {
	$.ajax({
		type: "POST",
        url: url,
        data: {
			"planId" : planId,
			"actualCourseId" : planCourseId
		},
        success: function(data) {
			if (handleException(data)) {
				getTrainerToCourseInfoCallBack(data);
			}
        }
	});
}

function getTrainerToCourseInfoCallBack(data) {
	$("#trainer_assess_planCourse_value").children().remove();
	if (data.noData == 1) {
		$("#trainer_assess_planCourse_value").text(getNoAssessmentMsg());
	} else {
		$("#trainer_assess_planCourse_value").text("");
		$trainerNameDiv = $("<div id='assess_course_trainer'></div>").appendTo($("#trainer_assess_planCourse_value"));
		$trainerNameDiv.text(data.employeeName);
		$trainerNameDiv.addClass("assessor_name");
		if (data.isIgnore == 1) {
			$("<div>User ignored.</div>").appendTo($("#trainer_assess_planCourse_value"));
			return;
		}
		$("<div class='clear'></div>").appendTo($("#trainer_assess_planCourse_value"));
		if (data.assessComment && "" != data.assessComment) {
			$commentsDiv = $("<div id='trainer_to_course_comments'></div").appendTo($("#trainer_assess_planCourse_value"));
			$commentsDiv.addClass("comments_div");
			$commentsDiv.text(getCommentsAndSuggestions() + data.assessComment);
		}
		
	}
}

function getTraineesToCourseInfo(url, planId, planCourseId, pageNow, pageSize) {
	var traineesToCourseCondition = {
		"planId" : planId,
		"actualCourseId" : planCourseId,
		"viewAssessmentCondition.nowPage" : pageNow,
		"viewAssessmentCondition.pageSize" : pageSize
	};
	getAssessmentInfo(url, traineesToCourseCondition, getTraineesToCourseInfoCallBack);
}

function getTraineesToCourseInfoCallBack(data, searchCritera, url) {
	$("#planCourse_average_part_value").children().remove();
	$("#planCourse_assessment_detail").children().remove();
	var $contentDiv = $("#show_trainee2course_name").find(".show_employee_name_content");
	$("#show_trainee2course_name").find(".show_employee_name_head").text(getNotAssessTraineesMsg()+" ("
			+data.notAssessedEmployeeList.length+")");
	drawAssessorNotGiven(data.notAssessedEmployeeList, $contentDiv);
	if (data.noData == 1) {
		$("#planCourse_average_part_value").text(getNoAssessmentMsg());
	} else {
		$("#planCourse_average_part_value").text("");
		$.each(data.assessmentItemAverageScoreAndRateList, function(i, averageItem){
			var $averageItemDiv = $("<div></div>").appendTo($("#planCourse_average_part_value"));
			if ((i+1)%2 == 1) {
				$averageItemDiv.css({"float":"left"});
			} else {
				$averageItemDiv.css({"float":"right"});
			}
			var itemNameDiv = "<div></div>";
			var $itemNameDiv = $(itemNameDiv).appendTo($averageItemDiv);
			$itemNameDiv.text(averageItem.assessItemName);
			$itemNameDiv.addClass("item_name_div");
			var itemScoreDiv = "<div></div>";
			var $itemScoreDiv = $(itemScoreDiv).appendTo($averageItemDiv);
			$itemScoreDiv.addClass("item_score_div");
			showAveragePoint(averageItem.avgScore, $itemScoreDiv);
			var rateDiv = "<div></div>";
			var $rateDiv = $(rateDiv).appendTo($averageItemDiv);
			$rateDiv.text(averageItem.avgScore.toFixed(1)+getPointsTitle()+" ("+averageItem.assessmentItemRate+getRatedLabel()+")");
			$rateDiv.addClass("item_rate_div");
		});
		$.each(data.assessmentPage.list, function(i, assessmentInfo){
			var assessmentInfoDiv = "<div class='assessment_value_part'></div>";
			var $assessmentInfoDiv = $(assessmentInfoDiv).appendTo($("#planCourse_assessment_detail"));
			var assessorName = "<div></div>";
			var $assessorName = $(assessorName).appendTo($assessmentInfoDiv);
			$assessorName.text(assessmentInfo.employeeName);
			$assessorName.addClass("assessor_name");
			if (assessmentInfo.isIgnore == 1) {
				$("<div>User ignored.</div>").appendTo($assessmentInfoDiv);
			} else {
				$.each(assessmentInfo.scoreList, function(j, scoreInfo){
					var itemScoreDiv = "<div></div>";
					var $itemScoreDiv = $(itemScoreDiv).appendTo($assessmentInfoDiv);
					$itemScoreDiv.text(scoreInfo.itemName + ": " + scoreInfo.assessScore + getPointsTitle());
					$itemScoreDiv.addClass("item_score_value");
				});
				$("<div class='clear'></div>").appendTo($assessmentInfoDiv);
				if (assessmentInfo.assessComment && "" != assessmentInfo.assessComment) {
					$commentsDiv = $("<div></div>").appendTo($assessmentInfoDiv);
					$commentsDiv.addClass("comments_div");
					$commentsDiv.text(getCommentsAndSuggestions()+assessmentInfo.assessComment);
				}
			}
		});
		if (data.assessmentPage.totalPage > 0) {
			pagination($("#planCourse_assessment_detail"), data.assessmentPage.nowPager, 
					data.assessmentPage.totalPage, url, searchCritera, getTraineesToCourseInfoCallBack);
		}
	}
	getHeightOfSplit($("#assess_to_course_part"), $("#plan_course_assessment_left"), $("#planCourse_assessment_content"));
}
