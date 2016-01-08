var basePath = $('#basePath').val();

$(document).ready(function() {
	bindTrainer2PlanClose();
	bindTrainer2PlanCancel();
	$(".validateDiv_trainer2plan").live("mouseover", cancel_starValidation);
});

function bindTrainer2PlanClose() {
	$('.closePic').live('click', function() {
		closeAssessmentPopup('trainer2plan_popup');
	});
}

function bindTrainer2PlanSubmit(callBack){
	$('#form_trainer2plan #give_assessment').live('click', function() {
		if(!validateTrainerToPlan() || $("#comment_trainer2plan").val().length > 1000){
			return false;
		}
		$('#form_trainer2plan').submit();
	});
	$('#form_trainer2plan').submit(
		function() {
			jQuery.ajax( {
				url : $('#form_trainer2plan').attr('action'),
				data : $('#form_trainer2plan').serialize(),
				type : "post",
				beforeSend: function(){
					closeAssessmentPopup('trainer2plan_popup');
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

function bindTrainer2PlanCancel() {
	$('#form_trainer2plan #cancel_assessment').live('click', function() {
		closeAssessmentPopup('trainer2plan_popup');
	});
}

/**
 * get trainer to plan assessment each item description
 * @return
 */
function initTrainer2Plan(planId) {
	$.ajax({
        type: "POST",
        url:  basePath+ "assessment/searchData_searchTrainer2planData?planId="+planId,
        data: {},
        success: function(data){
        	if (handleException(data)) {
        		// 1. remove default background
        		removeDefault('trainer2plan_item');
        		// 2. draw each item data(description and star info)
        		trainer2PlanAsessmentCallBack(data);
        	}
        }
    });
}

/**
 * draw each item data(description and star info)
 * @param data
 * @return
 */
function trainer2PlanAsessmentCallBack(data) {
	var assessmentForShow = data.assessmentForShow;
	var assessmentId = assessmentForShow.assessmentId;
	var assessComment = assessmentForShow.assessComment;
	var scoreList = assessmentForShow.scoreList;
	// id
	$('#trainer2planAssessId').val(assessmentId);
	// description
	var assessment_item = $('#trainer2plan_item');
	assessment_item.empty();
	var items_div = "";
	
    for(j=0;j<scoreList.length;j++){   
	    var $assessmentItem = $("<div>",{"class":"assess_item_60"});
	    var $itemLeft;
	    if(j == 0){
	    	$itemLeft = $("<div>",{"class":"item_description_20"});
	    } else {
	    	$itemLeft = $("<div>",{"class":"item_description_10"});
	    }
	    var content = '<div class="item_description_up">' + scoreList[j].itemName +'</div>' +
	    			  '<div class="item_description_down">' + scoreList[j].assessItemDescribe +'</div>';
	    $itemLeft.html(content);
	    $itemLeft.appendTo($assessmentItem);
	    $("<div>",{"class" : "star_div","id":"star_div_"+j}).appendTo($assessmentItem);
	    initialValidateStar($assessmentItem,"validateDiv_60 validateDiv_trainer2plan","validateStar validateStar_60");
	    $assessmentItem.appendTo(assessment_item);
	    var top = ($assessmentItem.height()-$itemLeft.height())/2;
	    $itemLeft.css("margin-top",top+"px");
    }

	// star
	for (var index = 0; index < scoreList.length; index++ ) {
		initialRaty($('#trainer2plan_popup #star_div_'+index), 'assessment.assessScoreList['+index+'].assessScore', scoreList[index].assessScore);
	}
	// comment
	$('#comment_trainer2plan').val(assessComment);
	initialComment('comment_trainer2plan');
	var commentsTooltip = $('#commentsTooltip').val();
    var trainer2planCommentValidation = '<!--'
			+ '<validanguage target="comment_trainer2plan" onblur="true" validations="validanguage.validateMaxlength(text, 1000)" '
			+ 'onsubmit="true" errorMsg="' + commentsTooltip + '" />' + '-->';
	$(trainer2planCommentValidation).insertAfter("#comment_trainer2plan");
	trainer2planCommentValidanguage();
}

// validation 
function validateTrainerToPlan(){
    var submitFlag = true;
    for (index = 0; index < 3; index++) {
        if ($('#trainer2plan_popup [name="assessment.assessScoreList[' + index + '].assessScore"]').val() == "") {
        	showValidateDiv($('#trainer2plan_popup #star_div_' + index).next(".validateDiv_trainer2plan"));
            submitFlag = false;
        }
    }
    return submitFlag;
}

function trainer2planCommentValidanguage(){
    /** It important if you use CommentAPI*/
    validanguage.settings.commentDelimiter = "/>";
    validanguage.settings.loadCommentAPI = true;
    validanguage.settings.focusOnerror = false;
    validanguage.settings.onsuccess = 'validanguage.removeErrorTiTle';
    validanguage.settings.onerror = 'validanguage.addErrorTitle';
    validanguage.settings.errorListText = "<strong>"+"User-defined filed:"+"</strong>";
	validanguage.populate.call(validanguage);
}
