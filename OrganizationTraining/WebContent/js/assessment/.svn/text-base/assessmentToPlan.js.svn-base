$(document).ready(function (){
	$("#assessed_plan_name").text($("#planName").val());
	$("#assessed_plan_id").text("("+$("#prefixIDValue").val() + ")");
	var needAssessment = $("#needAssessment").val();
	if (needAssessment == 0) {
		$("#plan_average_part").text(getNotNeedAssessmentMsg());
	} else {
		$("#from_trainee_menu").addClass("selected_menu").attr("isSelected", "1");
		getAssessmentsToPlan($("#basePath").val()+"assessment/viewTraineesToPlanInfo", 
				$("#planId").val(), viewAssessmentPageContent.firstPageNum, viewAssessmentPageContent.pageSize);
		$("#from_trainer_menu").bind("click", function(){
			clickFromTainer();
		});
		$("#from_trainee_menu").bind("click", function(){
			clickFromTrainee();
		});
		$("#assess2plan_assessorButton").attr("show","hide").bind("click", function(){
			clickAssessorButton($(this), $("#show_employee_name"));
		});
	}
});

/**
 * Ajax request for assessments to plan .
 * @param url
 * @param planId
 * @param nowPage
 * @param pageSize
 * @return
 */
function getAssessmentsToPlan(url, planId, nowPage, pageSize) {
	var searchCritera = {
			"planId" : planId,
			"viewAssessmentCondition.nowPage" : nowPage,
			"viewAssessmentCondition.pageSize" : pageSize	
	};
	getAssessmentInfo(url, searchCritera, assessmentToPlanCallBack);
}

/**
 * get assessments info of plan and draw it on page.
 * @param data
 * @param searchCritera
 * @param url
 * @return
 */
function assessmentToPlanCallBack(data, searchCritera, url) {
	//clear last result.
	$("#plan_average_part").children().remove();
	$("#plan_assessment_detail").children().remove();
	
	if ($("#from_trainee_menu").attr("isselected") == "1") {
		$("#show_employee_name").find(".show_employee_name_head").text(getNotAssessTraineesMsg()+" ("
				+data.notAssessedEmployeeList.length+")");
	} else {
		$("#show_employee_name").find(".show_employee_name_head").text(getNotAssessTrainersMsg()+" ("
				+data.notAssessedEmployeeList.length+")");
	}
	
	var $contentDiv = $("#show_employee_name").find(".show_employee_name_content");
	drawAssessorNotGiven(data.notAssessedEmployeeList, $contentDiv);
	if (data.noData == 1) {
		$("#plan_average_part").text(getNoAssessmentMsg());
	} else {
		$("#plan_average_part").text("");
		//draw average score and rate on page.
		$.each(data.assessmentItemAverageScoreAndRateList, function(i, averageItem){
			var itemDiv = $("<div>",{"class":"average_item_div"}).appendTo($("#plan_average_part"));
			if ((i+1)%2 == 1) {
				itemDiv.css({"float":"left"});
			} else {
				itemDiv.css({"float":"right"});
			}
			var itemNameDiv = "<div></div>";
			var $itemNameDiv = $(itemNameDiv).appendTo(itemDiv);
			$itemNameDiv.text(averageItem.assessItemName + getColon());
			$itemNameDiv.addClass("averageItem_name");
			var itemScoreDiv = "<div></div>";
			var $itemScoreDiv = $(itemScoreDiv).appendTo(itemDiv);
			$itemScoreDiv.addClass("item_score_div");
			showAveragePoint(averageItem.avgScore, $itemScoreDiv);
			var rateDiv = "<div></div>";
			var $rateDiv = $(rateDiv).appendTo(itemDiv);
			$rateDiv.text(averageItem.avgScore.toFixed(1)+ getPointsTitle()+" ("+averageItem.assessmentItemRate+getRatedLabel()+")");
			$rateDiv.addClass("item_rate_div");
		});
		//draw assessment details
		$.each(data.assessmentPage.list, function(i, assessmentInfo){
			var assessmentInfoDiv = "<div class='assessment_value_part'></div>";
			var $assessmentInfoDiv = $(assessmentInfoDiv).appendTo($("#plan_assessment_detail"));
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
					$itemScoreDiv.text(scoreInfo.itemName + getColon() + " " + scoreInfo.assessScore.toFixed(1) + getPointsTitle());
					$itemScoreDiv.addClass("item_score_value");
				});
				$("<div class='clear'></div>").appendTo($assessmentInfoDiv);
				$commentsDiv = $("<div></div>").appendTo($assessmentInfoDiv);
				$commentsDiv.addClass("comments_div");
				if (assessmentInfo.assessComment && "" != assessmentInfo.assessComment) {
					$commentsDiv.text(getCommentsAndSuggestions()+assessmentInfo.assessComment);
				}
			}
		});
		//draw pagination part.
		if (data.assessmentPage.totalPage > 0) {
			pagination($("#plan_assessment_detail"), data.assessmentPage.nowPager, 
					data.assessmentPage.totalPage, url, searchCritera, assessmentToPlanCallBack);
		}
	}
	getHeightOfSplit($("#assess_to_plan_part"), $("#plan_assessment_left"), $("#plan_assessment_content"));
}

function clickFromTainer() {
	$("#from_trainee_menu").removeClass("selected_menu").attr("isselected", "0");
	$("#from_trainer_menu").addClass("selected_menu").attr("isselected", "1");
	getAssessmentsToPlan($("#basePath").val()+"assessment/viewTrainersToPlanInfo", $("#planId").val(), 
			viewAssessmentPageContent.firstPageNum, viewAssessmentPageContent.pageSize);
}

function clickFromTrainee(){
	$("#from_trainer_menu").removeClass("selected_menu").attr("isselected", "0");
	$("#from_trainee_menu").addClass("selected_menu").attr("isselected", "1");
	getAssessmentsToPlan($("#basePath").val()+"assessment/viewTraineesToPlanInfo", $("#planId").val(), 
			viewAssessmentPageContent.firstPageNum, viewAssessmentPageContent.pageSize);
}

