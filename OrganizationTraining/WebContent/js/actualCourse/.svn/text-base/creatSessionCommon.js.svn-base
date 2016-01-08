/**
 * Prepare a default session json object.
 * @return json style object, record all key of a session.
 */
function prepareDefaultSession() {
	var defaultSession = {
//		"actualCourseId" : 0,
		"prefixIdValue" : "",
		"courseName" : "",
		"courseBrief" : "",
		"courseBriefWithoutTag" : "",
		"courseDuration" : 0.0,
		"courseRoomNum" : "",
		"courseStartTime" : "",
		"courseEndTime" : "",
		"courseTrainer" : "",
		"courseHasAttachment" : 0, //0: has no attachment; 1: has attachemts.
		"courseOrder" : 0, //The order in a plan
		"statusForDashboard" : "",
		"sessionInfo" : {},
		"attachments" : [],
        "courseHasAttachment" : 0
	};
	return defaultSession;
}

/**
 * Get plan session data in pop up.
 * @return  A plan session JSON.
 */
function getPlanSessionData(planSession) {
	// Get the data in pop up.
	var planSessionName = $("#sessionNameInput").val();
	var planSessionBrief = $("#sessionBriefEditor").parent().find('iframe').contents().find('body').text();
	var planSessionAttachments = [];
	var attachmentJSON = eval("(" + sessionAttachmentUI.getAttachmentsJsonStr() + ")");
	if (attachmentJSON != null && attachmentJSON != undefined) {
		planSessionAttachments = attachmentJSON;
	}
	planSession.courseName = planSessionName;
	planSession.courseBrief = $("#sessionBriefEditor").val();
	planSession.courseBriefWithoutTag = planSessionBrief;
	planSession.attachments = planSessionAttachments;
	if (planSessionAttachments.length == 0) {
		// There are no attachments.
		planSession.courseHasAttachment = 0;
	} else {
		// There are some attachments.
		planSession.courseHasAttachment = 1;
	}
}

function showSessionPopup(courseNum) {
	var scrollHeight = 140 - (document.documentElement.clientHeight - 350) / 2;
	showLayer($("#addTemporarySessionDiv"));
	$("html,body").animate({scrollTop:scrollHeight},50);
	if (courseNum) {
		var sessionJson = $("#courseListData_"+courseNum).data("courseListRecord");
		$("#sessionNameInput").val(sessionJson.courseNameHasTag);
		var planSessionBriefDom = $("#sessionBriefEditor").parent().find('iframe').contents().find('body')[0];
		planSessionBriefDom.innerHTML = sessionJson.courseBrief;
		var attachmentsJsonList = sessionJson.attachments;
		sessionAttachmentUI.reloadAttachment(JSON.stringify(attachmentsJsonList));
	}
}

function destroyPlanSessionPopUp(){
	$("#sessionNameInput").removeClass(validanguage.settings.failedFieldClassName);
	$('#sessionBriefDiv').find("table").removeClass(validanguage.settings.failedFieldClassName);
	// Clear session name.
	$("#sessionNameInput").val("");
	$("#sessionBriefEditor").parent().find('iframe').contents().find('body').text("");
	sessionAttachmentUI.removeAllAttachments();
	closeLayer($("#addTemporarySessionDiv"));
}
