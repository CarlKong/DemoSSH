var basePath = $("#basePath").val();
var attendType = ["attended", "late", "leave", "absent"];

$(document).ready(function(){
	
	bindCloseLayer($("#trainer_assess_trainee_close"));
	
	bindCloseLayer($("#cancel_trainer2Trainee"));
	
});

function bindCloseLayer($button) {
	$button.bind("click", function(){
		closeLayer($('#trainer2trainee_content'));
	});
}

function trainer2traineePopup(planCourseId, coursePrefixId, planCourseName, planId) {
	$("#trainer2planCourseId").val(planCourseId);
	$("#trainer2planCoursePlanId").val(planId);
	$("#a_course_prefixid").text("(" + coursePrefixId + ")");
	$("#a_course_name").text(planCourseName);
	var basePath = $("#basePath").val();
	var initPath = basePath + "assessment/searchData_searchTrainer2TraineeData";
	initTrainer2Trainee(initPath, planCourseId);
}

function initTrainer2Trainee(url, planCourseId){
	$.ajax ({
		type: "POST",
        url: url,
        data: {
			"actualCourseId" : planCourseId
		},
        success: function(data) {
			if (data.assessmentInfoMap) {
				showLayer($('#trainer2trainee_content'));
				$("#operationFlagId").val(data.operation);
				initHeadTitle(data.assessmentItemList, data.optionalItemMap);
				initTraineeList(data.assessmentInfoMap, data.assessmentItemList);
				initCourseComments(data.courseComments);
			} else {
				initialErrorMsgBar(getNoTraineeMsg());
			}
        }
	});
}
/**
 * initialize score items of head (include behavior and homework currently).
 * @param itemListData
 * @param optionalItemMap
 * @return
 */
function initHeadTitle(itemListData, optionalItemMap) {
	$("#a_score_field").children().remove();
	$.each(itemListData, function(index, item) {
		//TODO remove if and remove attend log from DB.
//		if (index > 0) {
			var $scoreItem = $("<div>", {"class" : "optional_item"}).appendTo($("#a_score_field"));
			if (item.isOptional == 1) {
				var $itemNameSpan = $("<span></span>").appendTo($scoreItem);
				$itemNameSpan.text(item.assessItemName);
				var $checkBox;
				if (!optionalItemMap || optionalItemMap[item.assessItemId] == "selected") {
					$checkBox = $("<span>", {"class" : "common_checkbox common_checkbox_checked"}).appendTo($scoreItem);
					$checkBox.attr("isselected", "selected");
				} else {
					$checkBox = $("<span>", {"class" : "common_checkbox common_checkbox_unchecked"}).appendTo($scoreItem);
					$checkBox.attr("isselected", "unselected");
				}
				$checkBox.css({"margin-bottom":"1px", "margin-left":"5px", "margin-right":"0px"});
				$checkBox.attr("itemId", item.assessItemId);
				$checkBox.attr("id", "checkBox_" + item.assessItemId);
				$checkBox.bind("click", function(){
					clickItemCheckBox($(this));
				});
				var $hiddenInput = $("<input type='hidden' name='opionalResult'>").appendTo($scoreItem);
				$hiddenInput.attr("id", "opionalResult" + item.assessItemId);
				$hiddenInput.val(item.assessItemId + ":" + optionalItemMap[item.assessItemId]);
			} else {
				$scoreItem.text(item.assessItemName);
			}
//		}
	});
}

/***
 * Click checkbox of head title, change its own style and this row. 
 * @param $checkBox
 * @return
 */
function clickItemCheckBox($checkBox){
	var itemId = $checkBox.attr("itemId");
	if ($checkBox.attr("isselected") == "selected") {
		$checkBox.attr("isselected", "unselected");
		$checkBox.addClass("common_checkbox_unchecked").removeClass("common_checkbox_checked");
		$("#real_trainees_div").find(".score_item_field" + itemId).hide();
		$(".scoredTrainee").find(".disable_star_div" + itemId).show();
		$(".scoredTrainee").find(".validate_star_div" + itemId).hide();
		$(".scoredTrainee").find(".validate_star_div" + itemId).attr("valiResult", "success");
		$("#opionalResult" + itemId).val(itemId + ":unselected");
		return;
	}
	if ($checkBox.attr("isselected") == "unselected")  {
		$checkBox.attr("isselected", "selected");
		$checkBox.addClass("common_checkbox_checked").removeClass("common_checkbox_unchecked");
		$(".scoredTrainee").find(".disable_star_div" + itemId).hide();
		$(".scoredTrainee").find(".validate_star_div" + itemId).hide();
		$(".scoredTrainee").find(".validate_star_div" + itemId).attr("valiResult", "success");
		$(".scoredTrainee").find(".score_item_field" + itemId).show();
		$("#opionalResult" + itemId).val(itemId + ":selected");
		return;
	}
}

/**
 * Get all of the trainees and the scores if he/she has been assessed.
 * @param assessmentInfoMap
 * @param itemListData
 */
function initTraineeList(assessmentInfoMap, itemListData) {
	$("#real_trainees_div").children().remove();
	var index = 0;
	$.each(assessmentInfoMap, function(i, assessmentInfo) {
		var $traineeItem = $("<div>", {"class":"trainee_item"}).appendTo($("#real_trainees_div"));
		$("<input type='hidden' name='assessmentList["+index+"].traineeId'>").appendTo($traineeItem).val(assessmentInfo.employeeId);
		if (assessmentInfo.assessmentId && assessmentInfo.assessmentId>0) {
			$("<input type='hidden' name='assessmentList["+index+"].assessId'>").appendTo($traineeItem).val(assessmentInfo.assessmentId);
			$("<input type='hidden' name='assessmentList["+index+"].createDate'>").appendTo($traineeItem).val(assessmentInfo.createDate);
		}
		$("<div class='trainee_num'></div>").appendTo($traineeItem).text(index + 1);
		$("<div class='trainee_preId'></div>").appendTo($traineeItem).text(assessmentInfo.employeePrefixId);
		$("<div class='trainee_name'></div>").appendTo($traineeItem).text(assessmentInfo.employeeName);
		var $hasBeenAssessed = $("<input type='hidden' name='assessmentList["+index+"].hasBeenAssessed'>").appendTo($traineeItem);
		$hasBeenAssessed.val(assessmentInfo.hasBeenAssessed);
		var $attendTypeInput = $("<input type='hidden' name='assessmentList["+index+"].assessmentAttendLog.attendLogKey'>").appendTo($traineeItem);
		var $attendTypeId = $("<input type='hidden' name='assessmentList["+index+"].assessmentAttendLog.attendLogId'>").appendTo($traineeItem);
		if (assessmentInfo.hasBeenAssessed && assessmentInfo.hasBeenAssessed == 1) {
			$attendTypeInput.val(assessmentInfo.attendenceLog);
			$attendTypeId.val(assessmentInfo.attendLogId);
		}
		
		var $attendField = $("<div class='attend_field'></div>").appendTo($traineeItem);
		initAttendLog($attendField, assessmentInfo.attendenceLog, $traineeItem, $hasBeenAssessed, $attendTypeInput);
		$attendField.attr("attendType", assessmentInfo.attendenceLog);
		
		if (assessmentInfo.scoreList && assessmentInfo.scoreList.length > 0) {
			$.each(assessmentInfo.scoreList, function (j, scoreItem){
				var $scoreField = $("<div class='score_item_field'></div>").appendTo($traineeItem);
				$scoreField.addClass('score_item_field' + scoreItem.itemId);
				$scoreField.attr("itemId", scoreItem.itemId);
				$("<input type='hidden' name='assessmentList["+index+"].assessScoreList["+j+"].assessmentItem.assessItemId'>")
					.appendTo($traineeItem).val(scoreItem.itemId);
				initTrainer2TraineeRaty($scoreField, 'assessmentList['+index+'].assessScoreList['+j+'].assessScore',scoreItem.assessScore);
				$scoreField.data("scoreValue", scoreItem.assessScore);
				$("<input type='hidden' name='assessmentList["+index+"].assessScoreList["+j+"].assessmentScoreId'>").appendTo($traineeItem).val(scoreItem.scoreId);
				if (scoreItem.assessScore > 0) {
					initFiveStar($traineeItem, "disable", scoreItem.itemId).hide();
				} else {
					$scoreField.hide();
					initFiveStar($traineeItem, "disable", scoreItem.itemId);
				}
				var $validateStraDiv = initFiveStar($traineeItem, "validate", scoreItem.itemId).hide();
				$validateStraDiv.bind("mouseover", function(){
					$validateStraDiv.hide();
					$validateStraDiv.attr("valiResult", "success");
					$scoreField.show();
				});
			});
		} else {
			$.each(itemListData, function (j, scoreItem){
					var $scoreField = $("<div class='score_item_field'></div>").appendTo($traineeItem);
					$scoreField.addClass('score_item_field' + scoreItem.assessItemId);
					$scoreField.attr("itemId", scoreItem.assessItemId);
					$scoreField.hide();
					$("<input type='hidden' name='assessmentList["+index+"].assessScoreList["+j+"].assessmentItem.assessItemId'>")
						.appendTo($traineeItem).val(scoreItem.assessItemId);
					initTrainer2TraineeRaty($scoreField, 'assessmentList['+index+'].assessScoreList['+j+'].assessScore',0);
					$scoreField.data("scoreValue", 0);
					initFiveStar($traineeItem, "disable", scoreItem.assessItemId);
					var $validateStraDiv = initFiveStar($traineeItem, "validate", scoreItem.assessItemId).hide();
					$validateStraDiv.bind("mouseover", function(){
						$validateStraDiv.attr("valiResult", "success");
						$validateStraDiv.hide();
						$scoreField.show();
					});
			});
		}
		
		var $commontsInputDiv = $("<div class='a_commonts_div'></div>").appendTo($traineeItem);
		var $commontsInput = $("<textarea name='assessmentList["+index+"].assessComment' class='a_commonts_input'></textarea >").appendTo($commontsInputDiv);
		$commontsInput.val(assessmentInfo.assessComment);
		
		changeCommentsInput($commontsInput, $attendField);
		if ($traineeItem.hasClass("unscoredTrainee")) {
			$commontsInput.val("");
			$commontsInput.attr("disabled","disabled");
		}
		$("<div class='clear'></div>").appendTo($traineeItem);
		index ++;
	});
	initScrollBar();
}

/**
 * Add scroll bar for trainer assess trainee pop-up
 * @return
 */
function initScrollBar() {
	if ($("#show_trainees_div").height() < $("#real_trainees_div").height()) {
		$("#vertical_scrollDiv").show();
		var ratio = $("#show_trainees_div").height() / $("#real_trainees_div").height();
		$("#vertical_block").css({"height" : $("#show_trainees_div").height()*ratio});
		$("#vertical_block").mousedown(function(e){
			var _$self = $(this);
			var x = e.clientX;
			var y = e.clientY;
			var top = this.offsetTop;
			$(document).bind({
				'mousemove.assessment' : function(e) {
					var _top = e.clientY - y + top;
					if (_top < 0) {
						_top = 0;
					}
					if( _top + _$self.height() > 300 ){
						_top = 300 - _$self.height();
					}
					_$self.css('top', _top);
					$("#real_trainees_div").css('top', (-1) *  _top / ratio);
				}
			});
			
			$(document).bind({
				'mouseup.assessment' : function(){
					$(document).unbind('mousemove.assessment').unbind('mouseup.assessment');
				}
			});
			return false;
		});
		
		$("#vertical_block").bind({
			'mouseout.assessment' : function(){
				$(document).unbind('mousemove.assessment').unbind('mouseup.assessment');
			}
		});
	} else {
		$("#vertical_scrollDiv").hide();
	}
	
}


/**
 * Change trainer to trainee comments input.
 * @param $input
 * @param $attendField
 */
function changeCommentsInput($input, $attendField) {
	$input.bind("focus", function(){
		$input.height(85);
		$input.css("z-index","102");
	});
	$input.bind("blur", function(){
		$input.height(20);
		$input.css("z-index","101");
	});
}

/**
 * Draw attend log.
 * @param $field
 * @param attendFlag
 */
function initAttendLog($field, attendFlag, $traineeItem, $hasBeenAssessed, $attendTypeInput) {
	setTraineeItemAttr(attendFlag, $traineeItem);
	for (i=0; i<attendType.length; i++) {
		var $attendType = $("<div class='attendType'></div>").appendTo($field);
		$attendType.attr("attendType", attendType[i]);
		if (attendFlag === attendType[i]) {
			$attendType.addClass(attendType[i]+"_active");
			$attendType.attr("isselected", "1");
		} else {
			$attendType.addClass(attendType[i]+"_disable");
			$attendType.attr("isselected", "0");
		}
		
		$attendType.bind("click", function(){
			clickAttendLog($(this), $field, $(this).attr("attendType"), $traineeItem, $hasBeenAssessed, $attendTypeInput);
		});
	}
	$("<div class='clear'></div>").appendTo($field);
}

/**
 * Change class of trainee item row by attend flag (one of ["attended", "late", "leave", "absent"]).
 * @param attendFlag
 * @param $traineeItem
 */
function setTraineeItemAttr(attendFlag, $traineeItem) {
	if (attendFlag == "attended" || attendFlag == "late") {
		$traineeItem.addClass("scoredTrainee").removeClass("unscoredTrainee");
	} else {
		$traineeItem.addClass("unscoredTrainee").removeClass("scoredTrainee");
	}
}

/**
 * Click attend log button (["attended", "late", "leave", "absent"])
 * @param $attendButton
 * @param $field
 * @param attendLogType (one of ["attended", "late", "leave", "absent"]).
 * @param $traineeItem
 */
function clickAttendLog($attendButton, $field, attendLogType, $traineeItem, $hasBeenAssessed, $attendTypeInput) {
	if ($attendButton.attr("isselected") == "1") {
//		var confirmContent = '<div>'+getGiveUpTrainerToTraineeMsg()+'</div>';
//		initialConfirmBar(confirmContent,  function(){
//			$attendButton.attr("isselected", "0");
//			$traineeItem.find(".score_item_field").hide();
//			$traineeItem.find(".validate_star_div").hide();
//			$traineeItem.find(".validate_star_div").attr("valiResult", "success");
//			$traineeItem.find(".disable_star_div").show();
//			$attendButton.addClass(attendLogType+"_disable").removeClass(attendLogType+"_active");
//			$traineeItem.addClass("unscoredTrainee").removeClass("scoredTrainee");
//			$traineeItem.find(".a_commonts_input").attr("disabled", "disabled");
//			$hasBeenAssessed.val(0);
//		});
	} else {
		$field.find(".attendType").each(function(i, button){
			$(button).addClass(attendType[i]+"_disable").removeClass(attendType[i]+"_active");
			$(button).attr("isselected", "0");
		});
		$attendButton.attr("isselected", "1");
		$attendButton.addClass(attendLogType+"_active")
			.removeClass(attendLogType+"_disable");
		setTraineeItemAttr(attendLogType, $traineeItem);
		//if click attended or late
		if ((attendLogType == "attended" || attendLogType == "late") &&
				$traineeItem.find(".disable_star_div").length >0) {
			$traineeItem.find(".disable_star_div").each(function(i, _disableStars){
				var itemId = $(_disableStars).attr("itemId");
				//If this item is selected or is not optional.
				if (($("#checkBox_" + itemId).length>0 && $("#checkBox_" + itemId).attr("isselected") == "selected")
						|| $("#checkBox_" + itemId).length===0) {
					$(_disableStars).hide();
					if ($traineeItem.find(".validate_star_div" + itemId).attr("valiResult") == null ||
							$traineeItem.find(".validate_star_div" + itemId).attr("valiResult") == "success") {
						$traineeItem.find(".score_item_field" + itemId).show();
					}
				} else { // This item is optional and not selected.
					$(_disableStars).show();
					$traineeItem.find(".score_item_field" + itemId).hide();
				}
			});
			$traineeItem.find(".a_commonts_input").removeAttr("disabled");
		} else { //Not click or selected leave or absent.
			$traineeItem.find(".score_item_field").hide();
			$traineeItem.find(".validate_star_div").hide();
			$traineeItem.find(".validate_star_div").attr("valiResult", "success");
			$traineeItem.find(".disable_star_div").show();
			$traineeItem.find(".a_commonts_input").attr("disabled", "disabled");
		}
		$hasBeenAssessed.val(1);
		$attendTypeInput.val(attendLogType);
	}
}

/**
 * Initialize disable stars or validation stars.
 * @param $field
 * @param type ("disable" or "validate")
 * @return $fiveStarDiv
 */
function initFiveStar($field, type, itemId) {
	var $fiveStarDiv = $("<div></div>").appendTo($field);
	$fiveStarDiv.addClass(type+"_star_div");
	$fiveStarDiv.attr("itemId", itemId);
	for (i=0; i<5; i++) {
		$("<div></div>").appendTo($fiveStarDiv).addClass(type+"_star");
	}
	$fiveStarDiv.addClass(type+"_star_div"+itemId);
	return $fiveStarDiv;
}

/**
 * initialize Raty (star control).
 * @param jQueryObject
 * @param scoreName
 * @param scoreValue
 */
function initTrainer2TraineeRaty(jQueryObject, scoreName,scoreValue){
	jQueryObject.raty({
		  half:      true,
		  size:      24,				
		  number: 	 5,
		  path:		 basePath+"image/assessment/",
		  starHalf:  "IMG_Half_Star_Active_16x16.png",
		  starOff:   "IMG_Star_16x16.png",
		  starOn:    "IMG_Star_Active_16x16.png",
		  scoreName: scoreName,
		  score:     scoreValue,
		  width:	 110,
		  space:     false,
		  click:     function(score, evt){
						jQueryObject.data("scoreValue", score);
		             }
	});
}

/**
 * Initialize trainer assess course comments.
 * @param comments
 * @return
 */
function initCourseComments(comments){
	if (comments) {
		$("#trainer2course_comments").val(comments);
	} else {
		$("#trainer2course_comments").val("");
	}
	initialCourseCommentsTip();
}

/**
 * Show or hide the tip of trainer to course comments.
 */
function initialCourseCommentsTip() {
	if($("#trainer2course_comments").val() != ""){
		$(".commentInputTips").hide();
	}
	$("#trainer2course_comments").focus(function(){
		$(".commentInputTips").hide();
	});
	$("#trainer2course_comments").blur(function(){
		if($("#trainer2course_comments").val() == ""){
			$(".commentInputTips").show();
		}
	});
	$(".commentInputTips").click(function(){
		$(this).hide();
		$("#trainer2course_comments").focus();
	});
}

/**
 * 
 * @param callBack
 * @return
 */
function bindTrainer2TraineeSubmit(callBack){
	$("#submit_trainer2Trainee").click(function(){
		if (validateTrainer2Trainees()) {
			$("#trainer2TraineeForm").submit();
		}
    });
    $('#trainer2TraineeForm').submit(function(){
        jQuery.ajax({
            url: $("#basePath").val() + "assessment/create_createTrainer2traineeAssessment",
            data: $('#trainer2TraineeForm').serialize(),
            type: "post",
            beforeSend: function(){
        		closeLayer($('#trainer2trainee_content'));
            },
            success: function(){
				if (callBack) {
					callBack.call();
				}
            }
        });
        return false;
    });
}

/**
 * Validate trainer assess trainees.
 * @return true: validate successfully, allow submit form;
 *         false: validate failure, cannot submit form.
 */
function validateTrainer2Trainees() {
	var validateResult = true;
//	if ($(".scoredTrainee").length === 0) {
//		validateResult = false;
//		//TODO
//	}
	$(".scoredTrainee").each(function(index, obj){
		$(obj).find($(".score_item_field")).each(function(i, scoreItem){
			var itemId = $(scoreItem).attr("itemId");
			if (($("#checkBox_" + itemId).length>0 && $("#checkBox_" + itemId).attr("isselected") == "selected")
					|| $("#checkBox_" + itemId).length===0) {
				if ($(scoreItem).data("scoreValue") <= 0) {
					$(scoreItem).hide();
					$(obj).find(".validate_star_div" + itemId).show();
					$(obj).find(".validate_star_div" + itemId).attr("valiResult", "falure");
					validateResult = false;
				}
			}
		});
	});
	return validateResult;
}