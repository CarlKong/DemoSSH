  function commentsValidanguage(){
        validanguage.settings.commentDelimiter = "/>";
        validanguage.settings.loadCommentAPI = true;
        validanguage.settings.focusOnerror = false;
        validanguage.settings.onsuccess = 'validanguage.removeErrorTiTle';
        validanguage.settings.onerror = 'validanguage.addErrorTitle';
        validanguage.settings.errorListText = "<strong>"+"User-defined filed:"+"</strong>";
		validanguage.populate.call(validanguage);
    }
	
function validateScore(){
	var passFlag = true;
	var courseHasHomework = $("#courseHasHomework").val();
	var trainer2courseComment = $("#comment_trainer2course").val();
	if(trainer2courseComment.length > 1000){
		passFlag = false;
	}
	for (i = 0; i < homeworkInfoArray.length; i++) {
		var homeworkDivId = homeworkInfoArray[i][0];
		var behaviorDivId = homeworkDivId.replace("homework", "behavior");
		var homeworkScoreName = homeworkInfoArray[i][1];
		var homeworkScore = $("[name='" + homeworkScoreName + "']").val();
		var behaviorScoreName = homeworkScoreName.replace("assessScoreList[2]", "assessScoreList[1]");
		var behaviorScore = $("[name='" + behaviorScoreName + "']").val();
		var attendanceScore = $(behaviorDivId).prev().children("input:last").val();
		var comments = $(homeworkDivId).nextAll(".comment_item").children("textarea");
		var commentContent = $(comments).val();		
		if(commentContent.length > 1000){
			passFlag = false;
		}
		if (courseHasHomework == 1) {
			if((attendanceScore == 1 || attendanceScore == 2) && behaviorScore == "" && homeworkScore != ""){
				showValidateDiv1($(behaviorDivId).next(".disableBehavior").next(".validateDiv"));
				passFlag = false;
			}
			if((attendanceScore == 1 || attendanceScore == 2) && behaviorScore != "" && homeworkScore == ""){
				showValidateDiv1($(homeworkDivId).next(".disableHomework").next(".validateDiv"));
				passFlag = false;
			}
			if((attendanceScore == 1 || attendanceScore == 2) && behaviorScore == "" && homeworkScore == ""){
				showValidateDiv1($(behaviorDivId).next(".disableBehavior").next(".validateDiv"));
				showValidateDiv1($(homeworkDivId).next(".disableHomework").next(".validateDiv"));
				passFlag = false;
			}
		} else if (courseHasHomework == 0) {
			if((attendanceScore == 1 || attendanceScore == 2) && behaviorScore == ""){
				showValidateDiv1($(behaviorDivId).next(".disableBehavior").next(".validateDiv"));
				passFlag = false;
			}
		}
	}
		return  passFlag;
}

function showValidateDiv1($validateDiv) {
	$validateDiv.css("visibility", "visible");
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