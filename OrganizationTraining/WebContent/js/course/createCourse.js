$(document).ready(function(){
	// for course brief
	var i18nLan = $("#i18nLan").val();
    initXhEditor_setLanguange(initXHEditorCourse,i18nLan);
    // for course type
    courseTypeData();
    // for course tag
    configureTagToken();
    commonTagData();
    $("#tagDiv .taginput").bind('blur', function(e) {
    	if ($(this).parent().hasClass('stopInputBlur-tag')) {
    		return false;
    	}
    	checkCourseTag('blur');
    });
    // for author
    initEmployeeName('createCourse');
    // for attachment
    courseUploadAttachment('createCourse');
    // validation
    courseValid();
    // buttons function
    $("#course_create_btn a").bind("click",function(){
    	checkCourseBrief('submit');
    	checkTargetPeople();
    	checkCourseTag('submit');
    	if (!checkAutoComplete($("#authorNameDiv"))) {
    		return false;
    	}
    	$("#attachmentList").val(courseAttachment.getAttachmentsJsonStr());
    	validanguage.validateSubmit('createCourse_form');
    });
    $("#course_cancel_btn a").bind("click",function(){
    	window.location = $('#basePath').val()+"course/course_searchCourse";
    });
});

