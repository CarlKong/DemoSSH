var planCourseAttachmentUI;
$(document).ready(function(){
	courseTypeData();            //in courseCommon.js 
	configureTagToken();        //in courseCommon.js 
	commonTagData();            //in courseCommon.js 
	planCourseValid();
	//Click confirm edit button.
	$("#course_save_btn").bind("click", function(){
		clickEditPlanCourse($(this));
	});
	$("#closeEditPlanCourse, #cancelEditPlanCourseID").live("click", function(){
		disdroyEditCoursePopup();
	});
	planCourseAttachmentUI = actualCourseUploadAttachment($("#course_add_attachment"), $("#upload_attachment_list"));
});

/**
 * When click plan course name (in create plan page plan course list).
 * Show basic information of this plan course.
 */
function prepareEditPlanCourse(courseNumber){
	var actualCourseJson = $("#courseListData_"+courseNumber).data("courseListRecord");
	//Set value for every item except attachments.
	$("#courseName").val(actualCourseJson.courseNameHasTag);
	$("#courseBrief").val(actualCourseJson.courseBrief);
	$("#courseBriefWithoutTag").val(actualCourseJson.courseBriefWithoutTag);
	$("#courseTargetPeople").val(actualCourseJson.courseInfo.courseTargetTraineeHasTag);
	var courseTypeId = actualCourseJson.courseInfo.courseType.courseTypeId;
	$('#courseAuthorName_value').val(actualCourseJson.courseInfo.courseAuthorName);
	$("#courseTypeId").val(courseTypeId);
	$('#courseAuthorName').getAutoCompleteInstance().addItemsByString(actualCourseJson.courseInfo.courseAuthorName);
	$("#courseDuration").val(actualCourseJson.courseDuration);
	$('#courseDuration').attr("readOnly","true");// duration cannot be changed on edit plan course page
	var attachmentsJsonList = actualCourseJson.attachments;
	planCourseAttachmentUI.reloadAttachment(JSON.stringify(attachmentsJsonList));
	
	var sourceValue = actualCourseJson.courseInfo.courseCategoryTag;
	if(sourceValue != null && sourceValue != "" && sourceValue != undefined){
//	    if(sourceValue.indexOf(";") == -1){
//	    	 sourceValue = {'label':sourceValue,'value':sourceValue};
//	    	 $("#courseCategoryTag").getAutoCompleteInstance().add(sourceValue);
//	    }else{
//	    	$("#courseCategoryTag").getAutoCompleteInstance().addItemsByString(sourceValue);
//	    }
		tokenInputObject[0].clearData();
		tokenInputObject[0].addTags(sourceValue);
	}
	if (undefined != courseTypeId){
    	$("label[for=courseTypeId_"+courseTypeId+"]").trigger("click");
    }
	showLayer($(".editPlanCoursePopUp"));
	var scrollHeight = 140 - (document.documentElement.clientHeight - 540) / 2;
	$("html,body").animate({scrollTop:scrollHeight},50);
	configCourseCategoryTag();
}

function clickEditPlanCourse($button){
	var domForm = document.getElementById("editPlanCourseForm");
	var eventObj = document.createEvent('HTMLEvents'); 
    eventObj.initEvent('submit', false, true);
    domForm.dispatchEvent(eventObj);
    checkPlanSessionBrief("submit");
    var failObject = validanguage.getElementByClassName(domForm, validanguage.settings.failedFieldClassName, false);
    if( typeof failObject == 'object' && failObject[0] == undefined ) {
    	var actualCourseNodeId = $button.attr("targetCourse");
    	$("#courseListData_"+actualCourseNodeId).data("courseListRecord").courseName  = $("#courseName").val();
    	$("#courseListData_"+actualCourseNodeId).data("courseListRecord").courseBrief  = $("#courseBrief").val();
    	$("#courseListData_"+actualCourseNodeId).data("courseListRecord").courseBriefWithoutTag = $("#courseBriefWithoutTag").val();
    	$("#courseListData_"+actualCourseNodeId).data("courseListRecord").courseInfo.courseTargetTrainee = $("#courseTargetPeople").val();
    	$("#courseListData_"+actualCourseNodeId).data("courseListRecord").courseInfo.courseType.courseTypeId = $("#radio_courseTypeId").find(".checked").prev().val();
    	$("#courseListData_"+actualCourseNodeId).data("courseListRecord").courseInfo.courseCategoryTag = $("#courseCategoryTag").val();
    	var authors = $("#courseAuthorName").val().split(",");
    	var authorNames = "";
    	if (authors && authors.length>0) {
    		for (i=0; i<authors.length; i++) {
    			authorNames += authors[i]+";";
    		}
    	}
    	$("#courseListData_"+actualCourseNodeId).data("courseListRecord").courseInfo.courseAuthorName = authorNames;
    	$("#courseListData_"+actualCourseNodeId).data("courseListRecord").attachments = eval("("+planCourseAttachmentUI.getAttachmentsJsonStr()+")");
    	//the following code is to handle the special characters, using the two fields to display the course name and course target trainee
    	$("#courseListData_"+actualCourseNodeId).data("courseListRecord").courseInfo.courseTargetTraineeHasTag = $("#courseTargetPeople").val();
    	$("#courseListData_"+actualCourseNodeId).data("courseListRecord").courseNameHasTag = $("#courseName").val();
    	
    	var $nameSpan = $("#courseListData_"+actualCourseNodeId).find(".actualCourseName");
    	$nameSpan.text($("#courseName").val());
    	$nameSpan.poshytip("destroy");
    	var $realNameDiv = $("#courseListData_"+actualCourseNodeId).find(".realNameSpan");
    	$realNameDiv.text($("#courseName").val());
    	if ($realNameDiv.width()>340) {
    		$nameSpan.poshytip({
    			allowTipHover : true ,
    			className: 'tip-green',
    			content: $realNameDiv.html()
    		});
    	}
    	disdroyEditCoursePopup();
    }
}

function configCourseCategoryTag(){
    if($("#courseCategoryTag").val() == "") {
            $(this).val("(Separate each tag with a semicolon, and maximum number of tag is 5)");
    }
	$("#courseCategoryTag").focus(function() {
		if($("#courseCategoryTag").val() == "(Separate each tag with a semicolon, and maximum number of tag is 5)") {
            $(this).val("");
		}
    });
}

function disdroyEditCoursePopup() {
	$("#courseName").removeClass(validanguage.settings.failedFieldClassName);
	$('#courseBriefDiv').find("table").removeClass(validanguage.settings.failedFieldClassName);
	$("#courseTargetPeople").removeClass(validanguage.settings.failedFieldClassName);
	$('#courseDuration').removeClass(validanguage.settings.failedFieldClassName);
	$("#courseAuthorName").removeClass(validanguage.settings.failedFieldClassName);
	$("#courseAuthorName_value").val("");
	$("#courseAuthorName").getAutoCompleteInstance().clear();
	$('#courseCategoryTag').removeClass(validanguage.settings.failedFieldClassName);
	closeLayer($(".editPlanCoursePopUp"));
}
