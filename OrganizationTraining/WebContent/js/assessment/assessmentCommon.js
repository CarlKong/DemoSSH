/**
 * click button to show popup
 * @param popupID
 * @param titleName
 * @param titleID
 * @return
 */
function showAssessmentpopup(popupID, titleName, titleID) {
	showLayer($('#'+popupID));
	var v_top = ($(window).height() - $('#'+popupID).height())/2 + document.body.scrollTop;
	$('#'+popupID).css("top", v_top);
	$('.title_name').html(titleName+' ');
	$('.title_id').html('('+titleID+')');
}

function closeAssessmentPopup(popupID) {
	closeLayer($('#'+popupID));
}

/**
 * remove default background
 * @return
 */
function removeDefault(divId) {
	$('#'+divId+' .data_default').css('display', 'none');
}

/**
 * draw star data
 * @param jQueryObject : div
 * @param scoreName
 * @param score : score data
 * @return
 */
function initialRaty(jQueryObject, scoreName, score) {
	jQueryObject.raty({
		  cancel:    false,
		  half:      true,
		  size:      24,					
		  number: 	 5,
		  path:		 '../image/assessment/',
		  starHalf:  'IMG_Half_Star_Active_20x20.png',
		  starOff:   'IMG_Star_20x20.png',
		  starOn:    'IMG_Star_Active_20x20.png',
		  width : 	 140,
		  scoreName: scoreName,
		  score:     score,
		  space:     false,
		  click : function(score, scoreName) {
			// validation
		  }
	});
}

/**
 * function to judge and set comment textarea
 * @param comment_default
 * @param textareaId
 * @return
 */
function initialComment(textareaId){
	if($('#'+textareaId).val() == ""){
		$('#'+textareaId).siblings(".tipDiv").show();
	}
	if($('#'+textareaId).val() != ""){
		$('#'+textareaId).siblings(".tipDiv").hide();
	}
	$(".tipDiv").click(function(){
		$(this).hide();
		$(this).siblings(".assess_comment").focus();
	});
	
	$('#'+textareaId).focus(function() {
		$(this).siblings(".tipDiv").hide();
    });
    $('#'+textareaId).blur(function() {
        if ($(this).val() == "") {
        	$(this).siblings(".tipDiv").show();
        }
    });
}

/**
 * add star validation
 * @param $validateDiv
 * @return
 */
function showValidateDiv($validateDiv) {
	$validateDiv.show();
	$validateDiv.children(".validateStar").each(function(){
		$(this).css("opacity",0);
	});
	var children = $validateDiv.children(".validateStar");
	$(children[0]).animate({opacity:1},100,function(){
		$(children[1]).animate({opacity:1},100,function(){
			$(children[2]).animate({opacity:1},100,function(){
				$(children[3]).animate({opacity:1},100,function(){
					$(children[4]).animate({opacity:1},100);
				});
			});
		});
	});
}

/**
 * remove validation of star
 * @return
 */
function cancel_starValidation(){
	$(this).hide(800);
    $(this).prev().show();
}

/**
 * Initialize validation stars.
 * @param $field
 * @return
 */
function initialValidateStar($field, parentClass, childClass) {
	var $fiveStarDiv = $("<div></div>").appendTo($field);
	$fiveStarDiv.addClass(parentClass);
	for (i=0; i<5; i++) {
		$("<div></div>").appendTo($fiveStarDiv).addClass(childClass);
	}
	return $fiveStarDiv;
}
