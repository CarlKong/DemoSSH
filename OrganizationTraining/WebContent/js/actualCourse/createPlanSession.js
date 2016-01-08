var sessionAttachmentUI;
$(document).ready(function() {
	planSessionValid();
	$("#addPlanSession").bind("click", function(){
		$("#editPlanSessionBtn").attr("id", "createPlanSessionBtn");
		showSessionPopup(null);
	});
	
	$("#createPlanSessionBtn").live("click", function(){
		var domForm = document.getElementById("planSessionForm");
		var eventObj = document.createEvent('HTMLEvents'); 
	    eventObj.initEvent('submit', false, true);
	    domForm.dispatchEvent(eventObj);
	    checkPlanSessionBrief("submit");
	    var failObject = validanguage.getElementByClassName(domForm, validanguage.settings.failedFieldClassName, false);
	    if( typeof failObject == 'object' && failObject[0] == undefined ) {
	    	var defaultSession = prepareDefaultSession();
			getPlanSessionData(defaultSession);
			destroyPlanSessionPopUp(); 
			var actualCourseList = [];
			actualCourseList.push(defaultSession);
			createActualCourseList(actualCourseList, false);
	    }
	});
	
	/**  Bind the click function to $("#closeSessionPic, #cancelPlanSessionBtn")  */
	$("#closeSessionPic, #cancelPlanSessionBtn").live("click", function(){
		destroyPlanSessionPopUp(); 
	});
	$("#editPlanSessionBtn").live("click", function(){
		checkPlanSessionBrief("submit"); 
		clickEditPlanSession($(this).attr("targetCourse"));
	});
	sessionAttachmentUI = actualCourseUploadAttachment($("#sessionAddAttachmentUrl"), $("#sessionAttachmentList"));
});

