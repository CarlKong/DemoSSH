var planIsPublishFlag = $("#hide-planIsPublish").val();
var planIsPublishVal = "1";
var noInvitedTraineees = "noInvitedTraineees";
var noSpecificTrainees = "noSpecificTrainees";

$(document).ready(function() {
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
	planTokenInputObject[0].addTags($("#planTag_value").val());
	loadAllEmployeeNames($('#basePath').val()+
			"searchCommon/searchCommon_findAllEmployeeNames", addAutoComplete, $(document).find("body"));
	planValid();
	planUploadAttachment('editPlan');
	saveAsPlan();
	editPlan();
	setPlanNeedAssessmentFlag();
	changeRadioChecked("plan_assessment_radios");
});

function editPlan(){
	$("#savePlanBtn").click(function(){
		checkPlanBrief('submit');
		checkPlanTag('submit');
		if(!checkTrainee()){
			return false;
		};
		$("#plan_attachmentsJson").val(planAttachment.getAttachmentsJsonStr());
		prepareDataForSubmit();
		makeActualCourseJson();
		$("#plan_attachmentsJson").val(planAttachment.getAttachmentsJsonStr());
		
		if(planIsPublishFlag === planIsPublishVal) {
			// if trainees is empty, show pop-up to warn creator cannot do this
			var judgeTrainees = checkTraineeIsEmpty();
			if(judgeTrainees != "") {
				if(judgeTrainees == noInvitedTraineees) {
					initialErrorMsgBar(getEditPlanNoInvitedTraineesMs());
				}
				if(judgeTrainees == noSpecificTrainees) {
					initialErrorMsgBar(getEditPlanNoSpecificTraineesMs());
				}
			} else {
				// if trainees is modified, show pop-up if  need send email
				var isModified = checkTraineeIsModified();
				if(isModified){
					setEditPlanConfirmBarByModifyTrainee($("#hide-planPrefixId").text());
				}else{
					setEditPlanConfirmBar($("#hide-planPrefixId").text());
				}
			}
		} else {
			$("#editPlanForm").attr("action","../plan/editPlan");
//			$("#editPlanForm").submit();
			validanguage.validateSubmit('editPlanForm');
		}
	});
}

function saveAsPlan(){
	$("#saveAsPlanBtn").click(function(){
		$('#hide-planIsPublish').val(null);
		checkPlanBrief('submit');
		checkPlanTag('submit');
		if(!checkTrainee()){
			return false;
		};
		$("#plan_attachmentsJson").val(planAttachment.getAttachmentsJsonStr());
		prepareDataForSubmit();
		makeActualCourseJson();
		$("#plan_attachmentsJson").val(planAttachment.getAttachmentsJsonStr());
		$("#editPlanForm").attr("action","../plan/createPlan?operationFlag=saveAs");
//		$("#editPlanForm").submit();
		validanguage.validateSubmit('editPlanForm');
	});
}

function loadPlanTagData(sourceObj, showObj){
	var sourceValue = sourceObj.val();
	if(sourceValue != null && sourceValue != "" && sourceValue != undefined){
	    if(sourceValue.indexOf(";") == -1){
	    	 sourceValue = {'label':sourceValue,'value':sourceValue};
	    	 showObj.getAutoCompleteInstance().add(sourceValue);
	    }else{
	    	 showObj.getAutoCompleteInstance().addItemsByString(sourceValue);
	    }
	}
}

/**
 * judge if trainees is empty
 * @return
 */
function checkTraineeIsEmpty(){
	var flag = "";
	var invited_trainees_new = getEmployeeNamesFromAutoComplete($("#invited_trainee"));
	var specific_trainees_new = getEmployeeNamesFromAutoComplete($("#specific_trainee"));
	if( $("#hidePlanTypeId").val() == planProperty.invitedType && invited_trainees_new == ""){
		flag = noInvitedTraineees;
	}
	if( $("#hidePlanTypeId").val() == planProperty.publicType && $("#hideIsAllEmployee").val() != planProperty.isAllEmployee  && specific_trainees_new == ""){
		flag = noSpecificTrainees;
	}
	return flag;
}

function getAllTrainers() {
    var trainerNames = "";
    $(".courseListDataBg").each(function(index, node){
        if($(node).find(".trainerInput").val() != ""){
            trainerNames += $(node).find(".trainerInput").val() + "; ";
        }
    });
    console.log(trainerNames);
    return trainerNames;
}

function checkTraineeIsModified(){
	var option_trainees_old = $("#option_trainee").text().split('; ');
	var invited_trainees_old = $("#invited_trainee").text().split('; ');
	var specific_trainees_old = $("#specific_trainee").text().split('; ');
	
	var option_trainees_new = getEmployeeNamesFromAutoComplete($("#option_trainee"));
	var invited_trainees_new = getEmployeeNamesFromAutoComplete($("#invited_trainee"));
	var specific_trainees_new = getEmployeeNamesFromAutoComplete($("#specific_trainee"));
	
	//trainer
	var all_trainers_old = allTrainersOld.split("; ");
	var all_trainers_new = getAllTrainers().split("; ");
	
	var flags = [];
	flags.push(ArrayIsEqual(all_trainers_old, all_trainers_new));
	flags.push(ArrayIsEqual(option_trainees_old, option_trainees_new));
	flags.push(ArrayIsEqual(invited_trainees_old, invited_trainees_new));
	flags.push(ArrayIsEqual(specific_trainees_old, specific_trainees_new));
	var isModified = false;
	for(var i = 0; i < flags.length; i++) {
		if(!flags[i]){
			isModified = true;
			break;
		}
	}
	return isModified;
}

//set confirm bar operation
function setEditPlanConfirmBar(planId) {
	var publishContent = getPlanEditMessage()+' "<span class="id_span">'+planId+'</span>" ?'+
						 '<div class="option_div"><p class="confirmBar_option"><span class="common_checkbox common_checkbox_unchecked" onclick="commonCheckBox(this)" id="checkBox_isSendEmail"></span>'+getSendEmailMessage()+'</p>'+
						 '<p class="confirmBar_option"><span class="common_checkbox common_checkbox_unchecked" onclick="commonCheckBox(this)" id="checkBox_isSendToManager"></span>'+getPublishSendEmailMessage()+'</p></div>';
	initialConfirmBar(publishContent, submitForm);
}

function setEditPlanConfirmBarByModifyTrainee(planId){
	var publishContent = getPlanEditMessage()+' "<span class="id_span">'+planId+'</span>" ?'+
	 '<div class="option_div radio_div"><p class="confirmBar_option"><span class="common_radio_comfirmBar common_radio_checked" radioName="sendToWho" check="checked" onclick="commonRadio(this)" id="radio_isModifiedTrainee"></span>'+getSendEmailToModifyTraineeMessage()+'</p>'+
	 '<p class="confirmBar_option"><span class="common_radio_comfirmBar common_radio_unchecked" radioName="sendToWho" onclick="commonRadio(this)" id="radio_isSendAllTrainee"></span>'+getSendEmailToEveryoneMessage()+'</p>'+
	 '</div><div class="option_div"><p class="confirmBar_option"><span class="common_checkbox common_checkbox_unchecked" onclick="commonCheckBox(this)" id="checkBox_isSendToManager"></span>'+getPublishSendEmailMessage()+'</p></div>';
	initialConfirmBar(publishContent, submitForm);
}

function submitForm(){
	prepareSendEmailCondition();
	$("#editPlanForm").attr("action","../plan/editPlan");
//	$("#editPlanForm").submit();
	validanguage.validateSubmit('editPlanForm');
}

function prepareSendEmailCondition(){
	if($("#checkBox_isSendEmail").length > 0 && $("#checkBox_isSendEmail").attr("check") === 'checked') {
		$("#isSendEmail").val(1);
	}
	if($("#checkBox_isSendToManager").length > 0 && $("#checkBox_isSendToManager").attr("check") === 'checked') {
		$("#isSendToManager").val(1);
	}
	if($("#radio_isModifiedTrainee").length > 0 && $("#radio_isModifiedTrainee").attr("check") === 'checked') {
		$("#isModifiedTrainee").val(1);
	}
	if($("#radio_isSendAllTrainee").length > 0 && $("#radio_isSendAllTrainee").attr("check") === 'checked') {
		$("#isSendAllTrainee").val(1);
	}
}
