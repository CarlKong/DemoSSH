/**
 * Get the plan course list and trainee list for plan and draw page.
 * 
 * @param url  The request url.
 * @param planId  The plan id.
 * @return  Null.
 */
function viewTrainerList(url, planId) {
	$.ajax({
        type: "POST",
        url: url,
        data: {
			"planId": planId
		},
        success: function(data) {
			if (handleException(data)) {
				trainerListCallBack(data, planId);
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
function trainerListCallBack(data, planId) {
	/*
	 * 1. Deal with the trainer list to a string and add it to $("#view_assessment_to_trainer_trainer_list").
	 * 2. Get the first JSON result for first trainer name.
	 */
	
	// 1. Deal with the trainer list to a string and add it to $("#view_assessment_to_trainer_trainer_list").
	var firstTrainerId = null;
	var firstTrainerName = null;
	var firstTrainerPrefixId = null;
	var trainerListString = "";
	$("#view_assessment_to_trainer_trainer_list").children().remove();
	$.each(data.trainerList, function(index, trainer) {
		var childMenu = $("<div>", {"class":"child_menu trainerMenu", "trainerId":trainer.employeeId, "trainerPrefixId":trainer.augEmpId}).text(trainer.augUserName).appendTo($("#view_assessment_to_trainer_trainer_list"));
		if (index == 0) {
			firstTrainerId = trainer.employeeId;
			firstTrainerName = trainer.augUserName;
			firstTrainerPrefixId = trainer.augEmpId;
			childMenu.addClass("selected_menu");
		}
		childMenu.bind("click", function(){
			$("#view_assessment_to_trainer_trainer_list").find(".trainerMenu").removeClass("selected_menu");
			childMenu.addClass("selected_menu");
			viewAssessmentsToTrainerForTrainer(planId, childMenu.attr("trainerId"), childMenu.text(), childMenu.attr("trainerPrefixId"), 
					viewAssessmentPageContent.firstPageNum, viewAssessmentPageContent.pageSize, data.actualCourseListOfTrainer); 
		});
	});
	
	// 2. Get the first JSON result for first trainer name.
	viewAssessmentsToTrainerForTrainer(planId, firstTrainerId, firstTrainerName, firstTrainerPrefixId, 
			viewAssessmentPageContent.firstPageNum, viewAssessmentPageContent.pageSize, data.actualCourseListOfTrainer);  
}

/**
 * Draw the assessment information to trainer.
 * 
 * @param planId  The plan id.
 * @param trainerId  The trainer id.
 * @param trainerName  The trainer name.
 * @param nowPage  The now page.
 * @param pageSize  The page size.
 * @return  Null
 */
function viewAssessmentsToTrainerForTrainer(planId, trainerId, trainerName, trainerPrefixId, nowPage, pageSize, planCourseList) {
	/*
	 * 1. Show trainer name and trainer prefix id.
	 * 2. Get the assessment result about master to trainer and draw the page.
	 * 3. Get the plan course list for trainer.
	 * 4. Iterate the plan course list and get the assessment result and draw each page.
	 */
	
	// 1. Show trainer name and trainer prefix id.
	$("#assessed__tainer_name").html(trainerName);
	$("#assessed_trainer_id").html("(" + trainerPrefixId + ")");
	
	// 2. Get the assessment result about master to trainer and draw the page.
	viewMasterToTrainerAssessment(planId, trainerId);
	
	// 3. Get the plan course list for trainer.
	$("#trainee_to_trainer_content").children().remove();
	var firstCourseId = 0;
	var planCourseIds = [];
	$.each(planCourseList, function(index, planCourse){
		if (planCourse.courseTrainer == trainerName) {
			var $courseContent = $("<div class='trainees_to_trainer_courseContent'></div>").appendTo($("#trainee_to_trainer_content"));
			$courseContent.attr("id", "trainees2trainer_courseContent_" + planCourse.actualCourseId);
			var $summaryPart = $("<div>", {'class':'trainee2Trainer_summary objectAndAverage'}).appendTo($courseContent);
			var $courseHead = $("<div></div>").appendTo($summaryPart);
			var $courseTitle = $("<div class='planCourse_title'></div>").appendTo($courseHead);
			$courseTitle.text(planCourse.courseName);
			var $courseFolder = $("<div class='plan_course_expand plan_course_fold_or_expand'></div>").appendTo($courseHead);
			$courseFolder.attr("courseId", planCourse.actualCourseId);
			$("<div class='clear'></div>").appendTo($courseHead);
			var $toTrainerAssessmentContent = $("<div class='trainees_to_trainer_assessment_courseContent'></div>").appendTo($courseContent);
			if (0 == firstCourseId) {
				firstCourseId = planCourse.actualCourseId;
				$courseFolder.addClass("plan_course_fold").removeClass("plan_course_expand");
				$courseFolder.attr("fold", "0");
			} else {
				planCourseIds.push(planCourse.actualCourseId);
				$courseFolder.attr("fold", "1");
			}
			$courseFolder.bind("click", function(){
				clickCourseFolder($(this), $("#trainees2trainer_courseContent_" + planCourse.actualCourseId), 
						planCourse.actualCourseId, trainerId, planCourseIds);
			});
			
		}
	});
	if (firstCourseId != 0) {
		viewTraineesToTrainerAssessment("assessment/viewTraineesToTrainerAssessmentByActualCourseIdAndTrainerId", 
				firstCourseId, trainerId, nowPage, pageSize, planCourseIds);
	}
	getHeightOfSplit($("#assess_to_trainer_part"), $("#to_trainer_assessment_left"), $("#to_trainer_assessment_content"));
	
}

function clickCourseFolder($button, $activePart, planCourseId, trainerId, planCourseIds) {
	if ($button.attr("fold") == "0") {
		$button.attr("fold", "1");
		$activePart.find(".trainees_to_trainer_assessment_courseContent").hide();
		$button.addClass("plan_course_expand").removeClass("plan_course_fold");
		getHeightOfSplit($("#assess_to_trainer_part"), $("#to_trainer_assessment_left"), $("#to_trainer_assessment_content"));
		return;
	}
	if ($button.attr("fold") == "1") {
		$(".trainees_to_trainer_assessment_courseContent").hide();
		$("#trainee_to_trainer_content .plan_course_fold_or_expand").attr("fold", "1");
		$("#trainee_to_trainer_content .plan_course_fold_or_expand").addClass("plan_course_expand").removeClass("plan_course_fold");
		$button.attr("fold", "0");
		if($activePart.find(".trainees_to_trainer_assessment_courseContent").children().length === 0) {
			viewTraineesToTrainerAssessment("assessment/viewTraineesToTrainerAssessmentByActualCourseIdAndTrainerId", 
					planCourseId, trainerId, viewAssessmentPageContent.firstPageNum, viewAssessmentPageContent.pageSize, planCourseIds);
		}
		$activePart.find(".trainees_to_trainer_assessment_courseContent").show();
		$button.addClass("plan_course_fold").removeClass("plan_course_expand");
		getHeightOfSplit($("#assess_to_trainer_part"), $("#to_trainer_assessment_left"), $("#to_trainer_assessment_content"));
		return;
	}
}

/**
 * Draw the master to trainer assessment.
 * 
 * @param planId  The plan id.
 * @param trainerId  The trainer id.
 * @return  Null
 */
function viewMasterToTrainerAssessment(planId, trainerId) {
	/*
	 * 1. Get the master to trainer assessment.
	 * 2. Draw the page.
	 */
	
	// 1. Get the master to trainer assessment.
	$.ajax({
        type: "POST",
        url: "assessment/viewMasterToTrainerAssessmentByPlanIdAndTrainerId",
        data: {
			"planId": planId,
			"trainerId": trainerId
		},
        success: function(data) {
			if (handleException(data)) {
				masterToTrainerAssessmentCallBack(data);
			}
		}
     });
}

/**
 * Draw the page about master to trainer.
 * 
 * @param data  The master to trainer data.
 * @return  Null
 */
function masterToTrainerAssessmentCallBack(data) {
	/*
	 * 1. Get the assessment data.
	 * 2. Draw the page about master to trainer.
	 */
	var assessment = data;
	$("#master_to_trainer_content").html("<div id = 'master_to_trainer_content_value' ></div>");
	if (assessment.scoreList) {
		$("#master_to_trainer_content_value").text("");
		// Append the master name dom to $("#master_to_trainer_content_value");
		var masterName = "<div></div>";
		var $masterName = $(masterName).appendTo($("#master_to_trainer_content_value"));
		$masterName.text(assessment.masterName);
		$masterName.addClass("to_trainer_master_name");
		
		var $master_to_trainer_content = $("<div></div>").appendTo($("#master_to_trainer_content_value")).css({"float":"left", "width":"620px"});
		if (data.isIgnore == 1) {
			$("<div>User ignored.</div>").appendTo($master_to_trainer_content).css("margin-top", "8px");
		} else {
			$.each(assessment.scoreList, function(index, score) {
				// Append the score item.
				var $scoreItemDIv = $("<div></div>").appendTo($master_to_trainer_content);
				if ((index + 1) % 2 == 1){
					$scoreItemDIv.css({"float" : "left"});
				} else {
					$scoreItemDIv.css({"float" : "right"});
				}
				var scoreItemLabelDiv = "<div><div>";
				var $scoreItemLabelDiv = $(scoreItemLabelDiv).appendTo($scoreItemDIv);
				$scoreItemLabelDiv.text(score.itemName + getColon());
				$scoreItemLabelDiv.addClass("to_trainer_master_item");
				var scoreItemScoreDiv = "<div>";
				var $scoreItemScoreDiv = $(scoreItemScoreDiv).appendTo($scoreItemDIv);
				$scoreItemScoreDiv.addClass("to_trainer_master_item_score");
				showAveragePoint(score.assessScore, $scoreItemScoreDiv);
			});
			$("#master_to_trainer_content_value").append($("<div>", {"class":"clear"}));
			var assessComment = assessment.commentsAndSuggestions;
			if (assessComment && ""!=assessComment) {
				$commentsDiv = $("<div></div>").appendTo($("#master_to_trainer_content"));
				$commentsDiv.attr("id", "master_to_trainer_comments").addClass("comments_div");
				$commentsDiv.text(getCommentsAndSuggestions()+ assessComment);
			}
		}
		clickFoldOrExpand($("#master_to_trainer"), $commentsDiv, "subtitle_expand", "subtitle_fold"
				, $("#assess_to_trainer_part"), $("#to_trainer_assessment_left"), $("#to_trainer_assessment_content"));
	} else {
		$("#master_to_trainer_content_value").text(getNoAssessmentMsg());
	}
}

function viewTraineesToTrainerAssessment(url, planCourseId, trainerId, nowPage, pageSize, planCourseIds) {
	var searchCritera = {
			"actualCourseId" : planCourseId,
			"trainerId" : trainerId,
			"viewAssessmentCondition.nowPage" : nowPage,
			"viewAssessmentCondition.pageSize" : pageSize
	};
	var otherParams = {
			"planCourseIds" : planCourseIds
	};
	getAssessmentInfo(url, searchCritera, traineesToTrainerCallBack, otherParams);
}

/**
 * draw trainees to trainer part
 * @param data
 * @param searchCritera
 * @param url
 * @return
 */
function traineesToTrainerCallBack(data, searchCritera, url, otherParams) {
	var $courseContent = $("#trainees2trainer_courseContent_" + searchCritera.actualCourseId).find(".trainees_to_trainer_assessment_courseContent");
	var $summaryPart = $("#trainees2trainer_courseContent_" + searchCritera.actualCourseId).find(".trainee2Trainer_summary");
	$courseContent.children().remove();
	if (data.needAssessment != '1') {
		$courseContent.append("<span class='no_assessed'>"+getNotNeedAssessmentMsg()+"</span>");
		drawOtherCourseAvgPart(otherParams, searchCritera);
		return;
	}
	drawTraineesNotFeedback($summaryPart, data);
	if (data.noData == 1) {
		$courseContent.append("<span class='no_assessed'>"+getNoAssessmentMsg()+"</span>");
	} else {
		$courseContent.find(".no_assessed").remove();
		drawAverageValuePart($summaryPart, data);
		$.each(data.assessmentPage.list, function(i, assessmentInfo){
			var $assessmentDetailPart = $("<div class='assessment_value_part'></div>").appendTo($courseContent);
			$("<div class='assessor_name'></div>").appendTo($assessmentDetailPart).text(assessmentInfo.employeeName);
			if (assessmentInfo.isIgnore == 1) {
				$("<div>User ignored.</div>").appendTo($assessmentDetailPart);
				$("<div class='clear'></div>").appendTo($assessmentDetailPart);
			} else {
				$.each(assessmentInfo.scoreList, function(j, scoreInfo){
					var itemScoreDiv = "<div></div>";
					var $itemScoreDiv = $(itemScoreDiv).appendTo($assessmentDetailPart);
					$itemScoreDiv.text(scoreInfo.itemName + ": " + scoreInfo.assessScore.toFixed(1) + getPointsTitle());
					$itemScoreDiv.addClass("item_score_value");
				});
				$("<div class='clear'></div>").appendTo($assessmentDetailPart);
				if (assessmentInfo.assessComment && ""!= assessmentInfo.assessComment) {
					$commentsDiv = $("<div></div>").appendTo($assessmentDetailPart);
					$commentsDiv.addClass("comments_div");
					$commentsDiv.text(getCommentsAndSuggestions()+assessmentInfo.assessComment);
				}
			}
			
		});
		if (data.assessmentPage.totalPage > 0) {
			pagination($courseContent, data.assessmentPage.nowPager, data.assessmentPage.totalPage, url, searchCritera, traineesToTrainerCallBack, otherParams);
		}
	}
	drawOtherCourseAvgPart(otherParams, searchCritera);
}

function drawOtherCourseAvgPart(otherParams, searchCritera){
	if (otherParams.planCourseIds.length == 0) {
		getHeightOfSplit($("#assess_to_trainer_part"), $("#to_trainer_assessment_left"), $("#to_trainer_assessment_content"));
	} else {
		for (i=0; i<otherParams.planCourseIds.length; i++) {
			if (otherParams.planCourseIds[i] != searchCritera.actualCourseId) {
				if (i == otherParams.planCourseIds.length-1) {
					$("#trainees2trainer_courseContent_" + otherParams.planCourseIds[i]).css({"margin-bottom":"20px"});
				}
				getTraineeToTrainerAvgScore("assessment/viewAverageScoreOfTrainer", otherParams.planCourseIds[i], searchCritera.trainerId);
			}
		}
		getHeightOfSplit($("#assess_to_trainer_part"), $("#to_trainer_assessment_left"), $("#to_trainer_assessment_content"));
	}
	
	clickFoldOrExpand($("#trainee_to_trainer"), $(".trainees_to_trainer_assessment_courseContent"), 
			"subtitle_expand", "subtitle_fold" , $("#assess_to_trainer_part"), $("#to_trainer_assessment_left"), $("#to_trainer_assessment_content"));

}

function getTraineeToTrainerAvgScore(url, planCourseId, trainerId) {
	$.ajax ({
		type: "POST",
        url: url,
        data: {
			"actualCourseId" : planCourseId,
		    "trainerId" : trainerId
		},
        success: function(data) {
			if (handleException(data)) {
				var $summaryPart = $("#trainees2trainer_courseContent_" + planCourseId).find(".trainee2Trainer_summary");
				drawTraineesNotFeedback($summaryPart, data);
				$summaryPart.find(".noAssessmentMsg").remove();
				if (data.noData == 1) {
					var $noDataMsg = $("<div class='noAssessmentMsg'></div>").appendTo($summaryPart);
					$noDataMsg.text(getNoAssessmentMsg());
				} else {
					$summaryPart.find(".noAssessmentMsg").remove();
					drawAverageValuePart($summaryPart, data);
				}
				
				getHeightOfSplit($("#assess_to_trainer_part"), $("#to_trainer_assessment_left"), $("#to_trainer_assessment_content"));
			}
		}
	});
}

/**
 * Draw average part of a plan course
 * @param $summaryPart
 * @param data
 * @return
 */
function drawAverageValuePart($summaryPart, data) {
	var $averageScorePart = $("<div ></div>").appendTo($summaryPart).addClass("traineeToTrainer_average_content");
	if (data.assessmentItemAverageScoreAndRateList) {
		$.each(data.assessmentItemAverageScoreAndRateList, function(i, averageItem) {
			var $averageItem = $("<div>",{"class":"average_item_div"}).appendTo($averageScorePart);
			if ((i+1)%2 == 1) {
				$averageItem.css({"float":"left"});
			} else {
				$averageItem.css({"float":"right"});
			}
			$("<div class='item_name_div'></div>").appendTo($averageItem).text(averageItem.assessItemName);
			var $itemScoreDiv = $("<div class='item_score_div'></div>").appendTo($averageItem);
			showAveragePoint(averageItem.avgScore, $itemScoreDiv);
			$("<div class='item_rate_div'></div>").appendTo($averageItem).text(averageItem.avgScore.toFixed(1)+getPointsTitle()+" ("+averageItem.assessmentItemRate+getRatedLabel()+")");
		});
	}
}

/**
 * Draw trainees who have not give assessments to trainer.
 * @param $summaryPart
 * @param data
 * @return
 */
function drawTraineesNotFeedback($summaryPart, data) {
	$summaryPart.find(".traineeToTrainer_average_content").remove();
	var $assessorButton = $("<div>", {"class":"have_not_assess_button"}).appendTo($summaryPart).attr("show","hide");
	var $showAssessorDiv = $("<div>", {"class":"show_employee_name_div"}).appendTo($summaryPart);
	$("<div>", {"class":"show_employee_name_head"}).appendTo($showAssessorDiv).text(
			getNotAssessTraineesMsg() + " ("+data.notAssessedEmployeeList.length+")");
	var $showAssessorContent = $("<div>", {"class":"show_employee_name_content"}).appendTo($showAssessorDiv);
	drawAssessorNotGiven(data.notAssessedEmployeeList, $showAssessorContent);
	$assessorButton.bind("click", function(){
		clickAssessorButton($(this), $showAssessorDiv);
	});
}