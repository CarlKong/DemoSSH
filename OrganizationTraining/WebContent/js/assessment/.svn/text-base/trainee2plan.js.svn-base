var basePath = $('#basePath').val();
var itemLength = 0;

$(document).ready(function(){
   
    bindTrainee2PlanClose();
    bindTrainee2PlanCancel();
    $(".validateDiv_trainee2plan").live("mouseover", cancel_trainee2planValidate);
});

function bindTrainee2PlanClose(){
    $('.closePic').live('click', function(){
        closeAssessmentPopup('trainee2plan_popup');
    });
}

function bindTrainee2PlanSubmit(callBack){
    $("#give_assessment_traineeToPlan").click(function(){
		if(!validateTraineeToPlan() || $("#comment_trainee2plan").val().length > 300){
			return false;
		}
		$("#form_trainee2plan").submit();
    });
    $('#form_trainee2plan').submit(function(){
        jQuery.ajax({
            url: $("#basePath").val() + "assessment/create_createTrainee2planAssessment",
            data: $('#form_trainee2plan').serialize(),
            type: "post",
            beforeSend: function(){
				closeAssessmentPopup('trainee2plan_popup');
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

function bindTrainee2PlanCancel(){
    $('#cancel_assessment').live('click', function(){
        closeAssessmentPopup('trainee2plan_popup');
    });
}

/**
 * get trainer to plan assessment each item description
 * @return
 */
function initTrainee2Plan(planId){
    $.ajax({
        type: "POST",
        url: basePath + "assessment/searchData_searchTrainee2planData?planId=" + planId,
        data: {},
        success: function(data){
            // 1. remove default background
            removeDefault('trainee2plan_item');
            // 2. draw each item data(description and star info)
            trainee2PlanAsessmentCallBack(data);
        }
    });
}

/**
 * draw each item data(description and star info)
 * @param data
 * @return
 */
function trainee2PlanAsessmentCallBack(data){
    var assessmentForShow = data.assessmentForShow;
    var assessComment = assessmentForShow.assessComment;
    var scoreList = assessmentForShow.scoreList;
    var assessmentId = assessmentForShow.assessmentId;
    var operationFlag = data.operationFlag;
    itemLength = scoreList.length;
    // description
    var assessment_item = $('#trainee2plan_item');
    assessment_item.empty();
    var items_div = "";
    
    if (assessComment != "") {
        $("#trainee2planTipDiv").hide();
    }
    
    for(j=0;j<scoreList.length;j++){   
	    var $assessmentItem = $("<div>",{"class":"assess_item_60"});
	    var $itemLeft = $("<div>",{"class":"item_description_20"});
	    var content = '<div class="item_description_up">' + scoreList[j].itemName +'</div>' +
	    			   '<div class="item_description_down">' + scoreList[j].assessItemDescribe +'</div>';
	    $itemLeft.html(content);
	    $itemLeft.appendTo($assessmentItem);
	    $("<div>",{"class" : "star_div","id":"star_div_"+j}).appendTo($assessmentItem);
	    initialValidateStar($assessmentItem,"validateDiv_trainee2plan","validateStar");
	    $assessmentItem.appendTo($('#trainee2plan_item'));
	    var top = ($assessmentItem.height()-$itemLeft.height())/2;
	    $itemLeft.css("margin-top",top+"px");
    }
    // star
    for (var index = 0; index < scoreList.length; index++) {
        initialRaty($('#trainee2plan_popup #star_div_' + index), 'assessment.assessScoreList[' + index + '].assessScore', scoreList[index].assessScore);
    }
    // comment
    $('#comment_trainee2plan').val(assessComment);
    $('#trainee2plan_assessmentId').val(assessmentId);
    $('#trainee2plan_operationFlag').val(operationFlag);
    var commentsTooltip = $("#commentsTooltip").val();
    var trainee2planCommentValidation = '<!--' +
    				'<validanguage target="comment_trainee2plan" onblur="true" validations="validanguage.validateMaxlength(text, 300)" ' +
    				'onsubmit="true" errorMsg="' +
    				commentsTooltip +'" />' +
				    '-->';
    $(trainee2planCommentValidation).insertAfter("#comment_trainee2plan");
    trainee2planCommentValidanguage();
	initialComment('comment_trainee2plan');
}

function validateTraineeToPlan(){
    var submitFlag = true;
    for (index = 0; index < itemLength; index++) {
        if ($('#trainee2plan_popup [name="assessment.assessScoreList[' + index + '].assessScore"]').val() == "") {
          //  $('#trainee2plan_popup #star_div_' + index).next().css("visibility", "visible").css("position", "relative");
        	showTraineeToPlanValidateDiv($('#trainee2plan_popup #star_div_' + index).next());
           // $('#trainee2plan_popup #star_div_' + index).hide();
            submitFlag = false;
        }
    }
    return submitFlag;
}

function cancel_trainee2planValidate(){
    $(this).prev().show();
    $(this).hide(800);
   // $(this).css("visibility", "hidden").css("position", "absolute");
}


