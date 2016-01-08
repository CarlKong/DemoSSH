
function clickEditPlanSession(actualCourseNodeId){
	$("#courseListData_"+actualCourseNodeId).data("courseListRecord").courseName = $("#sessionNameInput").val();
	$("#courseListData_"+actualCourseNodeId).data("courseListRecord").courseNameHasTag = $("#sessionNameInput").val();
	$("#courseListData_"+actualCourseNodeId).data("courseListRecord").courseBrief = $("#sessionBriefEditor").val();
	$("#courseListData_"+actualCourseNodeId).data("courseListRecord").courseBriefWithoutTag = 
		$("#sessionBriefEditor").parent().find('iframe').contents().find('body').text();
	var planSessionAttachments = [];
	var attachmentJSON = eval("(" + sessionAttachmentUI.getAttachmentsJsonStr() + ")");
	if (attachmentJSON != null && attachmentJSON != undefined) {
		planSessionAttachments = attachmentJSON;
	}
	$("#courseListData_"+actualCourseNodeId).data("courseListRecord").attachments = planSessionAttachments;
	if (planSessionAttachments.length == 0) {
		// There are no attachments.
		$("#courseListData_"+actualCourseNodeId).data("courseListRecord").courseHasAttachment = 0;
	} else {
		// There are some attachments.
		$("#courseListData_"+actualCourseNodeId).data("courseListRecord").courseHasAttachment = 1;
	}
	
	var $nameSpan = $("#courseListData_"+actualCourseNodeId).find(".actualCourseName");
	$nameSpan.text($("#sessionNameInput").val());
	$nameSpan.poshytip("destroy");
	var $realNameDiv = $("#courseListData_"+actualCourseNodeId).find(".realNameSpan");
	$realNameDiv.text($("#sessionNameInput").val());
	if ($realNameDiv.width()>340) {
		$nameSpan.poshytip({
			allowTipHover : true ,
			className: 'tip-green',
			content: $realNameDiv.text()
		});
	}
	$("#courseListData_"+actualCourseNodeId).data("courseListRecord").attachments = planSessionAttachments;
	destroyPlanSessionPopUp(); 
}