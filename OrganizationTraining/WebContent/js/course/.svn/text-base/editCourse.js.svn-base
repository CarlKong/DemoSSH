var basePath = $("#basePath").val();

$(document).ready(function(){
	// brief
	var i18nLan = $("#i18nLan").val();
    initXhEditor_setLanguange(initXHEditorCourse,i18nLan);
    // type
    courseTypeData();
    configureTagToken();
    commonTagData();
    // tag
    $("#tagDiv .taginput").bind('blur', function() {
    	if ($(this).parent().hasClass('stopInputBlur-tag')) {
    		return false;
    	}
    	checkCourseTag('blur');
    });
    tokenInputObject[0].addTags($("#courseTag_value").val());
    // author
    initEmployeeName('editCourse');
    // attachment
    courseUploadAttachment('editCourse');
    // validation
    courseValid();
    // button functions
    editCourse();
    saveAsCourse();
});

function editCourse() {
	$("#course_save_btn a").bind("click",function(){
		checkCourseBrief('submit');
		checkTargetPeople();
		checkCourseTag('submit');
		if (!checkAutoComplete($("#authorNameDiv"))) {
    		return false;
    	}
		$("#attachmentList").val(courseAttachment.getAttachmentsJsonStr());
        $("#editCourse_form").attr("action",basePath+"course/editCourse");
        validanguage.validateSubmit('editCourse_form');
    });
}

function saveAsCourse() {
	$("#course_saveAS_btn a").bind("click",function(){
		checkCourseBrief('submit');
		checkTargetPeople();
		checkCourseTag('submit');
		if (!checkAutoComplete($("#authorNameDiv"))) {
    		return false;
    	}
		$("#attachmentList").val(courseAttachment.getAttachmentsJsonStr());
    	$("#editCourse_form").attr("action",basePath+"course/createCourse?operationFlag=saveAs");
    	validanguage.validateSubmit('editCourse_form');
    });
}