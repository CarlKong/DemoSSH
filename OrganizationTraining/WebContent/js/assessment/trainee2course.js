var basePath = $('#basePath').val();

$(document).ready(function() {
	bindTrainee2CourseClose();
	bindTrainee2CourseCancel();
	$(".validateDiv_trainee2course").live("mouseover", cancel_starValidation);
});

function bindTrainee2CourseClose() {
	$('.closePic').live('click', function() {
		closeAssessmentPopup('trainee2course_popup');
	});
}

function bindTrainee2CourseSubmit(callBack) {
	$('#form_trainee2course #give_assessment').live('click', function() {
		if(!validateTraineeToCourse() || $("#comment2Trainer").val().length > 1000 || $("#comment2Course").val().length > 1000){
			return false;
		}
		$('#form_trainee2course').submit();
	});
	$('#form_trainee2course').submit(
		function() {
			jQuery.ajax( {
				url : $('#form_trainee2course').attr('action'),
				data : $('#form_trainee2course').serialize(),
				type : "post",
				beforeSend: function(){
					closeAssessmentPopup('trainee2course_popup');
	            },
				success : function(data) {
	            	if (handleException(data)) {
	            		if (callBack) {
	            			callBack.call();
	            		}
	            	}
				}
			});
			return false;
	});
}

function bindTrainee2CourseCancel() {
	$('#form_trainee2course #cancel_assessment').live('click', function() {
		closeAssessmentPopup('trainee2course_popup');
	});
}

/**
 * get trainee2course item description and assessment info
 * @param actualCourseId
 * @return
 */
function initTrainee2Course(actualCourseId) {
	$.ajax({
        type: "POST",
        url:  basePath+ "assessment/searchData_searchTrainee2courseData?actualCourseId="+actualCourseId,
        data: {},
        success: function(data){
        	if (handleException(data)) {
        		// 1. remove default background
        		removeDefault('trainee2course_item_trainer');
        		removeDefault('trainee2course_item_course');
        		// 2. draw each item data(description and star info)
        		trainee2CourseAsessmentCallBack(data);
        	}
        }
    });
}

/**
 * draw
 * @param data
 * @return
 */
function trainee2CourseAsessmentCallBack(data) {
	/**trainee2Trainer part*/
	var trainee2TrainerAssessment = data.trainee2Trainer;
	var assessment2TrainerId = trainee2TrainerAssessment.assessmentId;
	var trainerId = trainee2TrainerAssessment.employeeId;
	var trainee2TrainerScoreList = trainee2TrainerAssessment.scoreList;
	var comment2Trainer = trainee2TrainerAssessment.assessComment;
	// id
	$('#trainee2course_popup #assessment2TrainerId').val(assessment2TrainerId);
	$('#trainee2course_popup #trainerId').val(trainerId);
	// description
	var assessment_item_trainer = $('#trainee2course_item_trainer');
	assessment_item_trainer.empty();
	for(j=0;j<trainee2TrainerScoreList.length;j++){   
		var $assessmentItem = $("<div>",{"class":"assess_item_30"});
    	var $itemLeft = $("<div>",{"class":"item_description_10"});
	    var content = '<div class="item_description_up">' + trainee2TrainerScoreList[j].itemName +'</div>';
	    $itemLeft.html(content);
	    $itemLeft.appendTo($assessmentItem);
	    $("<div>",{"class" : "star_div","id":"star_div_2trainer_"+j}).appendTo($assessmentItem);
	    initialValidateStar($assessmentItem,"validateDiv_30 validateDiv_trainee2course","validateStar validateStar_30");
	    $assessmentItem.appendTo(assessment_item_trainer);
	    var top = ($assessmentItem.height()-$itemLeft.height())/2;
	    $itemLeft.css("margin-top",top+"px");
    }	
	// star
	for (var index = 0; index < trainee2TrainerScoreList.length; index++ ) {
		initialRaty($('#trainee2course_popup #star_div_2trainer_'+index), 'assessmentList[0].assessScoreList['+index+'].assessScore', trainee2TrainerScoreList[index].assessScore);
	}
	// comment
	$('#comment2Trainer').val(comment2Trainer);
	initialComment('comment2Trainer');
	var commentsTooltip = $('#commentsTooltip').val();
	var comment2TrainerValidation = '<!--'
			+ '<validanguage target="comment2Trainer" onblur="true" validations="validanguage.validateMaxlength(text, 1000)" '
			+ 'onsubmit="true" errorMsg="' + commentsTooltip + '" />' + '-->';
	$(comment2TrainerValidation).insertAfter("#comment2Trainer");
	
	/**trainee2Course part*/
	var trainee2CourseAssessment = data.trainee2Course;
	var trainee2CourseScoreList = trainee2CourseAssessment.scoreList;
	var comment2Course = trainee2CourseAssessment.assessComment;
	var assessment2CourseId = trainee2CourseAssessment.assessmentId;
	$('#trainee2course_popup #assessment2CourseId').val(assessment2CourseId);
	var assessment_item_course = $('#trainee2course_item_course');
	assessment_item_course.empty();
	for(j=0;j<trainee2CourseScoreList.length;j++){
		var $assessmentItem;
		var $itemLeft;
		var content;
		if(j == 0){
			$assessmentItem = $("<div>",{"class":"assess_item_60"});
	    	$itemLeft = $("<div>",{"class":"item_description_20"});
		    content = '<div class="item_description_up">'+trainee2CourseScoreList[j].itemName+'</div>'+
					  '<div class="item_description_down">'+trainee2CourseScoreList[j].assessItemDescribe+'</div>';
		    $itemLeft.html(content);
		    $itemLeft.appendTo($assessmentItem);
		    $("<div>",{"class" : "star_div","id":"star_div_2course_"+j}).appendTo($assessmentItem);
		    initialValidateStar($assessmentItem,"validateDiv_60 validateDiv_trainee2course","validateStar validateStar_60");
		} else {
			$assessmentItem = $("<div>",{"class":"assess_item_30"});
	    	$itemLeft = $("<div>",{"class":"item_description_10"});
		    content = '<div class="item_description_up">' + trainee2CourseScoreList[j].itemName +'</div>';
		    $itemLeft.html(content);
		    $itemLeft.appendTo($assessmentItem);
		    $("<div>",{"class" : "star_div","id":"star_div_2course_"+j}).appendTo($assessmentItem);
		    initialValidateStar($assessmentItem,"validateDiv_30 validateDiv_trainee2course","validateStar validateStar_30");
		}
	    $assessmentItem.appendTo(assessment_item_course);
	    var top = ($assessmentItem.height()-$itemLeft.height())/2;
	    $itemLeft.css("margin-top",top+"px");
    }
	for (var index = 0; index < trainee2CourseScoreList.length; index++ ) {
		initialRaty($('#trainee2course_popup #star_div_2course_'+index), 'assessmentList[1].assessScoreList['+index+'].assessScore', trainee2CourseScoreList[index].assessScore);
	}
	$('#comment2Course').val(comment2Course);
	initialComment('comment2Course');
	var comment2CourseValidation = '<!--'
			+ '<validanguage target="comment2Course" onblur="true" validations="validanguage.validateMaxlength(text, 1000)" '
			+ 'onsubmit="true" errorMsg="' + commentsTooltip + '" />' + '-->';
	$(comment2CourseValidation).insertAfter("#comment2Course");
	trainee2courseCommentValidanguage();
}

/**
 * validation
 * @return
 */
function validateTraineeToCourse(){
    var submitFlag = true;
    for (index = 0; index < 4; index++) {
        if ($('#trainee2course_popup [name="assessmentList[0].assessScoreList[' + index + '].assessScore"]').val() == "") {
        	showValidateDiv($('#trainee2course_popup #star_div_2trainer_' + index).next(".validateDiv_trainee2course"));
            submitFlag = false;
        }
    }
    for (index = 0; index < 3; index++) {
        if ($('#trainee2course_popup [name="assessmentList[1].assessScoreList[' + index + '].assessScore"]').val() == "") {
        	showValidateDiv($('#trainee2course_popup #star_div_2course_' + index).next(".validateDiv_trainee2course"));
            submitFlag = false;
        }
    }
    return submitFlag;
}

function trainee2courseCommentValidanguage() {
    /** It important if you use CommentAPI*/
    validanguage.settings.commentDelimiter = "/>";
    validanguage.settings.loadCommentAPI = true;
    validanguage.settings.focusOnerror = false;
    validanguage.settings.onsuccess = 'validanguage.removeErrorTiTle';
    validanguage.settings.onerror = 'validanguage.addErrorTitle';
    validanguage.settings.errorListText = "<strong>"+"User-defined filed:"+"</strong>";
	validanguage.populate.call(validanguage);
}

