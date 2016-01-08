var basePath = $('#basePath').val();
var rated = 0;
var maxNum = 0;
var changeToObj;
var nowi = 0;
var toSubmit = false;

$(document).ready(function() {
	bindMaster2TrainerClose();
	bindMaster2TrainerCancel();
	initialDropDown();
	$(".validateDiv_master2trainer").live("mouseover", cancel_starValidation);
});

function bindMaster2TrainerClose() {
	$('.closePic').live('click', function() {
		closeAssessmentPopup('master2trainer_popup');
	});
}

function bindMaster2TrainerSubmit(callBack){
	$('#form_master2trainer #give_assessment').live('click', function() {
		toSubmit = true;
		// before submit, validate all trainers
		if(!(validateMasterToTrainer('master2trainer_assess_'+$('#trainerNum').val()))){
			return false;
		}
		$('#form_master2trainer').submit();
	});
	$('#form_master2trainer').submit(
		function() {
			jQuery.ajax( {
				url : $('#form_master2trainer').attr('action'),
				data : $('#form_master2trainer').serialize(),
				type : "post",
				beforeSend: function(){
					closeAssessmentPopup('master2trainer_popup');
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

function bindMaster2TrainerCancel() {
	$('#form_master2trainer #cancel_assessment').live('click', function() {
		closeAssessmentPopup('master2trainer_popup');
	});
}

/**
 * get master2trainer assessment info
 * @param planId
 * @return
 */
function initMaster2Trainer(planId) {
	$.ajax({
        type: "POST",
        url:  basePath+ "assessment/searchData_searchMaster2trainerData?planId="+planId,
        data: {},
        success: function(data){
        	if (handleException(data)) {
        		// 1. remove default background
        		$('#master2trainer_item_default').css('display', 'none');
        		// 2. draw each item data(description and star info)
        		master2TrainerAsessmentCallBack(data);
        	}
        }
    });
}

/**
 * draw
 * @param data
 * @return
 */
function master2TrainerAsessmentCallBack(data) {
	var assessmentForShow = data.assessmentForShow; // list
	maxNum = assessmentForShow.length;
	$('#trainers_count').html(maxNum);
	var trainer_option = '';
	var trainer_assess_div = '';
	//reset the rate
	rated = 0;
	for (var i = 0; i < assessmentForShow.length; i++) {
		var assessmentId = assessmentForShow[i].assessmentId;
		var employeeId = assessmentForShow[i].employeeId;
		var employeeName = assessmentForShow[i].employeeName;
		var planCourseName = assessmentForShow[i].planCourseName;
		var scoreList = assessmentForShow[i].scoreList;
		var assessComment = assessmentForShow[i].assessComment;
		var hasBeenAssessed = assessmentForShow[i].hasBeenAssessed;
		// draw trainers
		trainer_option = trainer_option + 
						 '<div class="trainer_option" id="trainer_option_'+employeeId+'">'+
						 	'<div class="option_checked"></div>'+
						 	'<span class="option_name">'+employeeName+'</span>'+
						 	'<input type="hidden" id="employeeNum" name="assessmentList['+i+'].trainerId" value="'+employeeId+'"></input>'+
						 	'<input type="hidden" id="planCourseName" value="'+planCourseName+'"></input>'+
						 '</div>';
		// draw assessment div
		trainer_assess_div = trainer_assess_div + '<div id="master2trainer_assess_'+employeeId+'" class="master2trainer_assess">'+
													'<input type="hidden" id="i" value="'+i+'"></input>'+
													'<input type="hidden" id="hasBeenAssessed" name="assessmentList['+i+'].hasBeenAssessed" value="'+hasBeenAssessed+'"></input>'+
													'<input type="hidden" name="assessmentList['+i+'].assessId" value="'+assessmentId+'"></input>';
		for (var index = 0; index < scoreList.length; index++) {
			var item = '<div class="assessment_item">'+
							'<div class="assess_item_30">'+
								'<div class="item_description_10">'+
									'<div class="item_description_up">'+scoreList[index].itemName+'</div>'+
									'<div class="item_description_up_2">'+scoreList[index].assessItemDescribe+'</div>'+
								'</div>'+
								'<div class="star_div" id="star_div_'+index+'"></div>'+
								'<div name="disableArrange" class="validateDiv_30 validateDiv_master2trainer">' +
									'<div class="validateStar validateStar_30"></div>' +
								    '<div class="validateStar validateStar_30"></div>' +
								    '<div class="validateStar validateStar_30"></div>' +
								    '<div class="validateStar validateStar_30"></div>' +
								    '<div class="validateStar validateStar_30"></div>' +
								'</div>' +
							'</div>'+
						'</div>';
			trainer_assess_div = trainer_assess_div + item;
		}
		trainer_assess_div = trainer_assess_div +
								'<div class="assessment_textarea">'+
									'<textarea class="assess_comment" id="comment_master2trainer_'+i+'" name="assessmentList['+i+'].assessComment"></textarea>'+
									'<div class="tipDiv"><span>'+getAssessmentComment()+getAssessmentCommentNotice()+'</span></div>'+
								'</div>';
		trainer_assess_div = trainer_assess_div + '</div>';
	}
	// trainers
	$('#trainer_options').html(trainer_option);
	// assessments
	$('#master2trainer_popup .assessment_part').html(trainer_assess_div);
	for (var i = 0; i < assessmentForShow.length; i++) {
		var employeeId = assessmentForShow[i].employeeId;
		var scoreList = assessmentForShow[i].scoreList;
		var assessComment = assessmentForShow[i].assessComment;
		var hasBeenAssessed = assessmentForShow[i].hasBeenAssessed;
		var isIgnore = assessmentForShow[i].isIgnore;
		var reallyAssessed = false;
		if (isIgnore == 0 && hasBeenAssessed == 1) {
			reallyAssessed = true;
		}
		// comment
		$('#comment_master2trainer_'+i).val(assessComment);
		initialComment('comment_master2trainer_'+i);
		// star
		if (reallyAssessed) {
			rated = rated + 1;
			addTick($('#trainer_option_'+employeeId));
		}
		for (var index = 0; index < scoreList.length; index++) {
			initialRaty($('#master2trainer_assess_'+employeeId+' #star_div_'+index), 'assessmentList['+i+'].assessScoreList['+index+'].assessScore', scoreList[index].assessScore);
		}
		// poshytip
		$('#trainer_option_'+employeeId).poshytip({
			allowTipHover : true ,
			className: 'tip-green',
			content: getCourseConductedMessage()+$('#trainer_option_'+employeeId).find('#planCourseName').val()
		});
	}
	// show first employee assessment
	showAssessDiv(assessmentForShow[0].employeeId);
	$('#trainerNum').val(assessmentForShow[0].employeeId);
	$('#trainerCourses').val(assessmentForShow[0].planCourseName);
	initialCoursesTooltip($('#trainerCourses').val());
	$('#trainer_name').html(assessmentForShow[0].employeeName);
	$('#assessed_count').html(rated);
}

/*drop down part functions*/
function initialDropDown() {
	$('#dropDown_button').click(function(){
		var visibilityValue = $('#trainer_options').css('visibility');
		if(visibilityValue == 'visible') {
			$('#trainer_options').css('visibility','hidden');
			// remove scroll bar
		} else {
			$('#trainer_options').css('visibility','visible');
//			initialTrainersDivScrollbar();
		}
	});
	$('#trainers_dropDown').mouseleave(function(){
    	$(this).find('#trainer_options').css('visibility','hidden');
    });
    clickOptionDiv(); 
}

/**
 * trainers operation div show scrollbar
 * @return
 */
function initialTrainersDivScrollbar(){
	if ($("#trainer_options").height() > 43) {
		$("#trainer_options").scrollbar({
			viewPortWidth  : 182,
			viewPortHeight : 43// for test, normal should be 200px
		 });
	}
}

/**
 * when click option div show employeeName in header and show his assessment div
 * @return
 */
function clickOptionDiv() {
	$('.trainer_option').live('click', function(){
		changeToObj = $(this);
		// validate nowTrainer(if have started assess, show validation and must finish assessment; if havn't assessed, show notice popup)
		if(validateMasterToTrainer('master2trainer_assess_'+$('#trainerNum').val())){
			changeToAssessDiv(changeToObj);
		}
	});
}

/**
 * show this trainer's assessment div and set relevant infor
 * @return
 */
function changeToAssessDiv(changeToObj){
	var assessDivNum = changeToObj.find('#employeeNum').val();
	showAssessDiv(assessDivNum);
	$('#trainer_name').html(changeToObj.find('.option_name').html());
	$('#trainerNum').val(changeToObj.find('#employeeNum').val());
	$('#trainerCourses').val(changeToObj.find('#planCourseName').val());
	initialCoursesTooltip($('#trainerCourses').val());
}

/**
 * now div disappear and show new div
 * @param assessDivNum
 * @return
 */
function showAssessDiv(assessDivNum) {
	var nowAssessDivNum = $('#trainerNum').val();
	if (nowAssessDivNum != -1) {
		$('#master2trainer_assess_'+nowAssessDivNum).fadeOut("slow",function(){
			$('#master2trainer_assess_'+assessDivNum).fadeIn();
		}); 
	} else {
		$('#master2trainer_assess_'+assessDivNum).css('display', 'block');
	}
}

/**
 * validation of master2trainer
 * 
 * @return
 */
function validateMasterToTrainer(divId){
	var changeDivFlag = false;
    var oneFlag = false;
    var orFlag = false;
    var andFlag = true;
    nowi = $('#'+divId+' #i').val();
    for (index = 0; index < 4; index++) {
        if ($('#master2trainer_popup #'+divId+' [name="assessmentList['+nowi+'].assessScoreList[' + index + '].assessScore"]').val() == "") {
        	oneFlag = false;
        } else {
        	oneFlag = true;
        }
        orFlag = orFlag || oneFlag;
        andFlag = andFlag && oneFlag;
    }
	// if havn't assessed, show notice popup
    if(orFlag == false){
    	var ignoreContent = getIgnoreTrainerMessage();
    	initialConfirmBar(ignoreContent, ignoreOneTrainer);
    }
    // if have started assess, show validation and must finish assessment
    if(orFlag == true && andFlag == false){
    	for (index = 0; index < 4; index++) {
            if ($('#master2trainer_popup #'+divId+' [name="assessmentList['+nowi+'].assessScoreList[' + index + '].assessScore"]').val() == "") {
            	showValidateDiv($('#master2trainer_popup #'+divId+' #star_div_' + index).next(".validateDiv_master2trainer"));
            }
        }
    }
    if(andFlag == true){
    	// has been assessed
    	changeRate($('#trainerNum').val());
    	$('#master2trainer_popup [name="assessmentList['+nowi+'].hasBeenAssessed"]').val(1);
    	changeDivFlag = true;
    }
    return changeDivFlag;
}

/**
 * ignore assess one trainer, set has been assessed false
 * @return
 */
function ignoreOneTrainer(){
	if ($('#master2trainer_popup [name="assessmentList['+nowi+'].hasBeenAssessed"]').val() !=1) {
		$('#master2trainer_popup [name="assessmentList['+nowi+'].hasBeenAssessed"]').val(0);
	}
	if (toSubmit == true) {
		$('#form_master2trainer').submit();
	} else {
		changeToAssessDiv(changeToObj);
	}
}

/**
 * show trainer plan courses infor
 * @param courses
 * @return
 */
function initialCoursesTooltip(courses){
	$('#trainers_tooTip').poshytip('destroy');
	$('#trainers_tooTip').poshytip({
		allowTipHover : true ,
		className: 'tip-green',
		content: getCourseConductedMessage()+' '+courses
	});
}

/**
 * add tick mark before trainer name (when he is given assessment)
 * @param $trainerDiv
 * @return
 */
function addTick($trainerDiv) {
	$trainerDiv.find('.option_checked').css('display', 'inline-block');
	$trainerDiv.find('.option_name').css('margin-left', 0+'px');
}


/**
 * after assess, change employee's assessed icon and change rate
 * @return
 */
function changeRate(divNum) {
	if ($('#master2trainer_assess_'+divNum+' #hasBeenAssessed').val() == 0) {
		rated = rated + 1;
		$('#assessed_count').html(rated);
		addTick($('#trainer_option_'+divNum));
	}
}