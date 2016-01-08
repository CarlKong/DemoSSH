$(document).ready(function() {
	//load xheditor resource
	var i18nLan = $("#i18nLan").val();
	initXhEditor_setLanguange(initXHEditorPlan,i18nLan);
	getPlanTypes();
	configuePlanTagToken();
	getAllCategoryPlan();
	$("#planTagDiv .taginput").bind('blur', function() {
		if ($(this).parent().hasClass('stopInputBlur-tag')) {
    		return false;
    	}
		checkPlanTag('blur');
    });
	planUploadAttachment('createPlan');
	planValid();
	setPlanNeedAssessmentFlag();
	changeRadioChecked("plan_assessment_radios");
	loadAllEmployeeNames($('#basePath').val()+
			"searchCommon/searchCommon_findAllEmployeeNames", addAutoComplete, $(document).find("body"));
	// operations
	bindSubmitAction();
	bindCancelAction();
});

function bindSubmitAction(){
	$("#createPlanBtn").click(function(){
		checkPlanBrief('submit');
		checkPlanTag('submit');
		if(!checkTrainee()){
			return false;
		};
        $("#plan_attachmentsJson").val(planAttachment.getAttachmentsJsonStr());
		prepareDataForSubmit();
		makeActualCourseJson();
		validanguage.validateSubmit('createPlanForm');
	});
}

function bindCancelAction(){
	$("#cancelPlanBtn").click(function(){
		window.location = "plan/plan_searchPlan";
	});
}
