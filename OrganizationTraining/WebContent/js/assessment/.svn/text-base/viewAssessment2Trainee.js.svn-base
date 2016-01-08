$(document).ready(function(){
	var planId = $("#planId").val();
	bindPlanCourseAndTraineeClickToTrainee(planId);
	bindToTraineeParentMenuClick();
	$("#assess2trainee_assessorButton").attr("show","hide").bind("click", function(){
		clickAssessorButton($(this), $("#assess2trainee_show_employee_name"));
	});
});

function bindToTraineeParentMenuClick() {
	$("#to_trainee_plan_course_menu").live("click", function() {
		$("#view_assessment_to_trainee_course_list").show();
		$("#view_assessment_to_trainee_trainee_list").hide();
	});
	$("#to_trainee_trainee_menu").live("click", function() {
		$("#view_assessment_to_trainee_course_list").hide();
		$("#view_assessment_to_trainee_trainee_list").show();
	});
}

function bindPlanCourseAndTraineeClickToTrainee (planId) {
	$(".to_trainee_plan_course").live("click", function() {
		$(".selected_menu").removeClass("selected_menu");
		$(this).addClass("selected_menu");
		var planCourseId = $(this).attr("id");
		var planAndPlanCoursePrefixId = $(this).attr("plan_and_plan_course_prefix_id");
		var planCourseName = $(this).html();
		var searchCriteria = {
				"planId": planId,
				"actualCourseId": planCourseId,
				"viewAssessmentCondition.nowPage": viewAssessmentPageContent.firstPageNum,
				"viewAssessmentCondition.pageSize": viewAssessmentPageContent.pageSize
		};
		
		var otherParam = {
				"planCourseName":planCourseName,
				"planAndPlanCoursePrefixId" : planAndPlanCoursePrefixId,
				"assessmentType" : "assessmentForPlanCourse"
		};
		var url = $("#basePath").val()+"assessment/viewAssessmentByPlanIdAndActualCourseId";
		viewAssessmentsToTraineeForPlanCourse(url, searchCriteria, assessmentByPlanIdAndPlanCourseIdCallBack, otherParam); 
	});
	$(".to_trainee_trainee").live("click", function() {
		$(".selected_menu").removeClass("selected_menu");
		$(this).addClass("selected_menu");
		var traineeId = $(this).attr("id");
		var traineePrefixId = $(this).attr("trainee_prefix_id");
		var traineeName = $(this).html();
		var searchCriteria = {
				"planId": planId,
				"traineeId": traineeId,
				"viewAssessmentCondition.nowPage": viewAssessmentPageContent.firstPageNum,
				"viewAssessmentCondition.pageSize": viewAssessmentPageContent.pageSize
		};
		
		var otherParam = {
				"traineeName":traineeName,
				"traineePrefixId" : traineePrefixId,
				"assessmentType" : "assessmentForTrainee"
		};
		var url = $("#basePath").val()+"assessment/viewAssessmentByPlanIdAndTraineeId";
		viewAssessmentsToTraineeForTrainee(url, searchCriteria,assessmentByPlanIdAndPlanCourseIdCallBack, otherParam );
	});
}

/**
 * Get the plan course list and trainee list for plan and draw page.
 * 
 * @param url  The request url.
 * @param planId  The plan id.
 * @return  Null.
 */
function viewPlanCourseListAndTraineeList(url, planId) {
	$.ajax({
        type: "POST",
        url: url,
        data: {
			"planId": planId
		},
        success: function(data) {
			if (handleException(data)) {
				planCourseAndTraineeListCallBack(data, planId);
			}
		}
     });
}

/**
 * Draw the plan course list and trainee list.
 * 
 * @param data  The data object from backend.
 * @param planId  The plan id.
 * @return  Null
 */
function planCourseAndTraineeListCallBack(data, planId) {
	/*
	 * 1. Deal with the plan course list to a string and add it to $("#view_assessment_to_trainee_course_list").
	 * 2. Deal with the trainee list to a string and add it to $("#view_assessment_to_trainee_trainee_list").
	 * 3. Get the first JSON result for first plan course.
	 */
	
	// 1. Deal with the plan course list to a string and add it to $("#view_assessment_to_trainee_course_list").
	var firstPlanCourseId = 0;
	var firstPlanCourseName = "";
	var firstPlanAndPlanCoursePrefixId = "";
	var planCourseListString = "";
	$.each(data.planCourseList, function(index, planCourse) {
		if (index == 0) {
			firstPlanCourseId = planCourse.actualCourseId;
			firstPlanCourseName = planCourse.actualCourseName;
			firstPlanAndPlanCoursePrefixId = planCourse.planAndActualCoursePrefixId;
			planCourseListString += '<div class = "child_menu to_trainee_plan_course selected_menu" id = "' + planCourse.actualCourseId +'" plan_and_plan_course_prefix_id = ' +  planCourse.planAndActualCoursePrefixId + ' >' + planCourse.actualCourseName + '</div>';
		} else {
			planCourseListString += '<div class = "child_menu to_trainee_plan_course" id = "' + planCourse.actualCourseId +'" plan_and_plan_course_prefix_id = ' +  planCourse.planAndActualCoursePrefixId + ' >' + planCourse.actualCourseName + '</div>';
		}
	});
	$("#view_assessment_to_trainee_course_list").html(planCourseListString);
	
	// 2. Deal with the trainee list to a string and add it to $("#view_assessment_to_trainee_trainee_list").
	var traineeListString = "";
	$.each(data.traineeList, function(index, trainee) {
		traineeListString += '<div class = "child_menu to_trainee_trainee" id = "' + trainee.traineeId + '" trainee_prefix_id = "'+ trainee.traineePrefixId +'" >' + trainee.traineeName + '</div>';;
	});
	$("#view_assessment_to_trainee_trainee_list").html(traineeListString);
	
	// 3. Get the first JSON result for first plan course.
	$("#view_assessment_to_trainee_trainee_list").hide();
	var searchCriteria = {
			"planId": planId,
			"actualCourseId": firstPlanCourseId,
			"viewAssessmentCondition.nowPage": viewAssessmentPageContent.firstPageNum,
			"viewAssessmentCondition.pageSize": viewAssessmentPageContent.pageSize
	};
	
	var otherParam = {
			"planCourseName":firstPlanCourseName,
			"planAndPlanCoursePrefixId" : firstPlanAndPlanCoursePrefixId,
			"assessmentType" : "assessmentForPlanCourse"
	};
	var url = $("#basePath").val()+"assessment/viewAssessmentByPlanIdAndActualCourseId";
	viewAssessmentsToTraineeForPlanCourse(url, searchCriteria, assessmentByPlanIdAndPlanCourseIdCallBack, otherParam);  
}


/**
 * Request the backend and display the data about assessment to trainee for plan course.
 * 
 * @param url  The request url.
 * @param planId  The plan id.
 * @param planCourseId  The plan course id.
 * @param nowPage  The now page number.
 * @param pageSize  The page size.
 * @return  Null
 */

function viewAssessmentsToTraineeForPlanCourse(url, searchCriteria, callBack, otherParam) {
	$.ajax({
        type: "POST",
        url: url,
        data: searchCriteria,
        success: function(data) {
			if (handleException(data)) {
				callBack(data, searchCriteria, url, otherParam);
			}
		}
     });
}

function viewAssessmentsToTraineeForTrainee(url, searchCriteria, callBack, otherParam) {
	$.ajax({
        type: "POST",
        url: url,
        data: searchCriteria,
        success: function(data) {
			if (handleException(data)) {
				callBack(data, searchCriteria, url, otherParam);
			}
		}
     });
}

/**
 * Draw the assessment information.
 * 
 * @param data  The assessment data.
 * @param planId  The plan id.
 * @param planCourseId  The plan course id.
 * @param traineeId  The trainee id.
 * @param objectName  The object name.
 * @param objectPrefixId  The object prefix id.
 * @param type  Identity this method is for plan course or is for trainee.
 * @return  Null
 */
function assessmentByPlanIdAndPlanCourseIdCallBack(data, searchCriteria, url, otherParam) {
	// 1. Deal with the plan course name and plan_planCourse prefix id.
	$("#assessed_course_or_tainee_name").html(otherParam.planCourseName);
	$("#assessed_plan_or_trainee_id").html("(" +otherParam.planAndPlanCoursePrefixId + ")");
	$("#to_trainee_assessment_item_list").find(".needNotAssessInfo").remove();
	if (data.needAssessment === "1") {	
		$("#plan_or_trainee_average_part").show();
		// 2. Deal with assessment average score and attendence logs.
		dealWithAssessmentAverageScoreAndRate(data.assessmentItemAverageScoreAndRateList);
		// Deal with the attendence logs.
		$("#attendence_logs_count_attend").html(data.attended);
		$("#attendence_logs_count_late").html(data.late);
		$("#attendence_logs_count_leave_early").html(data.leave);
		$("#attendence_logs_count_absence").html(data.absent);
		if (!otherParam.assessmentType) {
			otherParam.assessmentType = assessmentForPlanCourse;
		}
		// 3. Deal with the assessment list.
		var assessmentList = data.assessmentList;
		dealWithAssessmentToTraineeList(assessmentList, otherParam.assessmentType);
		var searchCriteria;
		
		// 4. Add the paging information.
		if (otherParam.assessmentType == assessmentForPlanCourse) {
			if (data.notAssessedEmployeeList) {
				$("#assess2trainee_show_employee_name").find(".show_employee_name_head").text(getNotAssessedTraineesMsg()+" ("
						+data.notAssessedEmployeeList.length+")");
				var $contentDiv = $("#assess2trainee_show_employee_name").find(".show_employee_name_content");
				drawAssessorNotGiven(data.notAssessedEmployeeList, $contentDiv);
			}
		} else if(otherParam.assessmentType == assessmentForTrainee) {
			if (data.notAssessedEmployeeList) {
				$("#assess2trainee_show_employee_name").find(".show_employee_name_head").text(getNotAssessTrainersMsg()+" ("
						+data.notAssessedEmployeeList.length+")");
				var $contentDiv = $("#assess2trainee_show_employee_name").find(".show_employee_name_content");
				drawAssessorNotGiven(data.notAssessedEmployeeList, $contentDiv);
			}
		}
		if (assessmentList && otherParam.assessmentType == assessmentForPlanCourse) {
			pagination($("#to_trainee_assessment_page_info"), data.assessmentPage.nowPager, data.assessmentPage.totalPage, "assessment/viewAssessmentByPlanIdAndActualCourseId", searchCriteria, assessmentByPlanIdAndPlanCourseIdCallBack, otherParam);
		} else if (otherParam.assessmentType == assessmentForTrainee) {
			$("#to_trainee_assessment_page_info").html("");
		}
	} 
	else {
		$("#plan_or_trainee_average_part").hide();
		$needNotAssessInfo = $("<span class='needNotAssessInfo'></span>").appendTo($("#to_trainee_assessment_item_list"));
		$needNotAssessInfo.text(getNotNeedAssessmentMsg());
		
	}
	
	getHeightOfSplit($("#to_trainee_split_line"), $("#to_trainee_assessment_left"), $("#to_trainee_assessment_content"));
}

/**
 * Deal with assessment list.
 * 
 * @param assessmentList  The assessment list.
 * @param type  For trainee or for plan course.
 * @return Null
 */
function dealWithAssessmentToTraineeList(assessmentList, type) {
	/*
	 * 1. Add one DIV whose id is to_trainee_assessment_item_list_value into $("#to_trainee_assessment_item_list").
	 * 2. Iterate the assessment list and add the DOM into $("#to_trainee_assessment_item_list_value").
	 */
	
	if (type != null && type != undefined) {
		$("#to_trainee_assessment_item_list").html("<div id = 'to_trainee_assessment_item_list_value' ></div>");
		if (assessmentList) {
			$("#trainee_attendence_logs_count_content").show();
			$.each(assessmentList, function(i, assessmentInfo){
				var assessmentInfoDiv = "<div class='assessment_value_part'></div>";
				var $assessmentInfoDiv = $(assessmentInfoDiv).appendTo($("#to_trainee_assessment_item_list_value"));
				if (type == assessmentForTrainee) {
					// This assessment is for trainee.
					var planCourseName = "<div></div>";
					var $planCourseName = $(planCourseName).appendTo($assessmentInfoDiv);
					$planCourseName.addClass("to_trainee_plan_course_name");
					$("<span></span>").appendTo($planCourseName).addClass("trainee_name_span").text(assessmentInfo.planCourseName);
				} else if (type == assessmentForPlanCourse) {
					// This assessment is for plan course.
					var assessorName = "<div></div>";
					var $assessorName = $(assessorName).appendTo($assessmentInfoDiv);
					$assessorName.addClass("assessor_name");
					$("<span></span>").appendTo($assessorName).addClass("trainee_name_span").text(assessmentInfo.employeeName);
				}
				if (assessmentInfo.isIgnore == 1) {
					$("<div>User ignored.</div>").appendTo($assessmentInfoDiv);
				} else {
					// Add the attendence log.
					var attendenceClass = dealWithAttendenceLog(assessmentInfo.attendenceLog);
					if (attendenceClass != null && attendenceClass != "") {
						var attendenceSpan = "<span></span>";
						var $attendenceSpan = null;
						if (type == assessmentForTrainee) {
							$attendenceSpan = $(attendenceSpan).appendTo($planCourseName);
						} else if(type == assessmentForPlanCourse) {
							$attendenceSpan = $(attendenceSpan).appendTo($assessorName);
						}
						$attendenceSpan.css({"margin-top":"3px"});
						$attendenceSpan.addClass(attendenceClass);
					}
					$.each(assessmentInfo.scoreList, function(j, scoreInfo){
						var itemScoreDiv = "<div></div>";
						var $itemScoreDiv = $(itemScoreDiv).appendTo($assessmentInfoDiv);
						var scoreValue;
						if (scoreInfo.assessScore == -1) {
							scoreValue = getNoneAssessmentItemMsg();
							$itemScoreDiv.text(scoreInfo.itemName + getColon() + " " + scoreValue);
						} else {
							scoreValue = scoreInfo.assessScore;
							$itemScoreDiv.text(scoreInfo.itemName + getColon() + " " + scoreValue + getPointsTitle());
						}
						$itemScoreDiv.addClass("to_trainee_item_score_value");
					});
					$("<div class='clear'></div>").appendTo($assessmentInfoDiv);
					var assessComment = assessmentInfo.assessComment;
					if (assessComment != null && assessComment != "") {
						$commentsDiv = $("<div></div>").appendTo($assessmentInfoDiv);
						$commentsDiv.addClass("comments_div");
						$commentsDiv.text(getCommentsAndSuggestions() + assessmentInfo.assessComment);
					}
				}
			});
		} else {
			$("#trainee_attendence_logs_count_content").hide();
		}
	}
}

/**
 * Deal with the attendence log from backend to adapt to the frontend css.
 * 
 * @param attendenceLog  The attendence log from backend.
 * @return  The attendence result to adapt the css.
 */
function dealWithAttendenceLog(attendenceLog) {
	var attendenceLogForCss = "";
	if (attendenceLog != null && attendenceLog != undefined) {
		if (attendenceLogAttend == attendenceLog) {
			// Attend
			attendenceLogForCss = attendenceLogForCssAttend;
		} else if (attendenceLogLate == attendenceLog) {
			// Late
			attendenceLogForCss = attendenceLogForCssLate;
		} else if (attendenceLogLeaveEarly == attendenceLog) {
			// Leave early
			attendenceLogForCss = attendenceLogForCssLeaveEarly;
		} else if (attendenceLogAbsence == attendenceLog) {
			// Absence
			attendenceLogForCss = attendenceLogForCssAbsence;
		}
	}
	return attendenceLogForCss;
}

/**
 * Deal with the assessment average score and rate.
 * 
 * @param assessmentItemList  The instance colleciton.
 * @return  Null
 */
function dealWithAssessmentAverageScoreAndRate(assessmentItemList) {
	$("#to_trainee_assessment_average_item_list").html("<div id = 'to_trainee_assessment_average_item_list_value' ></div>");
	if (!assessmentItemList) {
		$("#to_trainee_assessment_average_item_list_value").text(getNoAssessmentMsg());
	} else {
		$.each(assessmentItemList, function(i, averageItem){
			var $averageScoreItem = $("<div></div>").appendTo($("#to_trainee_assessment_average_item_list_value"));
			if ((i+1)%2 == 1) {
				$averageScoreItem.css({"float":"left"});
			} else {
				$averageScoreItem.css({"float":"right"});
			}
			var itemNameDiv = "<div></div>";
			var $itemNameDiv = $(itemNameDiv).appendTo($averageScoreItem);
			$itemNameDiv.text(averageItem.assessItemName);
			$itemNameDiv.addClass("item_name_div");
			var itemScoreDiv = "<div></div>";
			var $itemScoreDiv = $(itemScoreDiv).appendTo($averageScoreItem);
			$itemScoreDiv.addClass("item_score_div");
			showAveragePoint(averageItem.avgScore, $itemScoreDiv);
			var rateDiv = "<div></div>";
			var $rateDiv = $(rateDiv).appendTo($averageScoreItem);
			if (averageItem.assessmentItemRate == "none"){
				$rateDiv.text(averageItem.assessmentItemRate);
			} else {
				$rateDiv.text(averageItem.avgScore.toFixed(1)+getPointsTitle()+" ("+averageItem.assessmentItemRate+getRatedLabel()+")");
			}
			$rateDiv.addClass("item_rate_div");
		});
	}
}
