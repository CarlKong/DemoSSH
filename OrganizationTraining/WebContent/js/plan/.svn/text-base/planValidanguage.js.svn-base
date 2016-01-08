function planValid(){
	validanguage.settings.commentDelimiter = "/>";
	validanguage.settings.focusOnerror = false;
    validanguage.settings.onsuccess = 'validanguage.removeErrorTiTle';
    validanguage.settings.onerror = 'validanguage.addErrorTitle';
    validanguage.settings.errorListText = "<strong>"+showFailedFields()+"</strong>";
      
	    validanguage.el.planName = {
	    	field:'Plan Name',
	    	validations:[
	    	{
	    		name: 'validanguage.validateRequired',
	    		errorMsg: validatePlanNameRequiredEM(),
	    		onblur: true,
	    		onsubmit: true
	    	},
	    	{
	    		name: 'validanguage.validateMinlength(text,2)',
	    		errorMsg: validatePlanNameMinlengthEM(),
	    		onblur: true,
	    		onsubmit: true
	    	},
	    	{
	    		name: 'validanguage.validateMaxlength(text,200)',
	    		errorMsg: validatePlanNameMaxlengthEM(),
	    		onkeyup:true,
	    		onblur: true
	    	}
//	    	,
//	    	{
//	            name: "validanguage.validateRegex(text, {expression: /([a-zA-Z\.\-\_\+\(\)\@\#\"\"\?\u4e00-\u9fa5])/, modifiers: 'g'})",
//	            errorMsg: validateInvalidCharactersEM(),
//	            onblur: true,
//	            onsubmit: true
//	        }
	    	]
	    };
}

function checkPlanBrief(eventType){
    if(event == undefined || event == ' ') eventType = 'submit'; 
    var $planBriefDiv = $('#planBriefDiv');
    var ev = document.createEvent('HTMLEvents');
    ev.initEvent(eventType, false, false);
    var $planBriefVal = $("#planBriefEditor").parent().find('iframe').contents().find('body').text().replace(/(\s*$)/g, "");
    if ($planBriefVal == null || $planBriefVal.length == 0 ){
    	var errormMsg = validatePlanBriefRequiredEM();
        $planBriefDiv.find('table').removeClass(validanguage.settings.passedFieldClassName).addClass(validanguage.settings.failedFieldClassName).attr(validanguage.settings.errorMsgAttr,errormMsg);
        validanguage.validateTooTip($planBriefDiv.find('table')[0]);
        return false;
    }
    if ($planBriefVal.length > 10000 ) {
        var errormMsg = validatePlanBriefMaxlengthEM();
        $planBriefDiv.find('table').removeClass(validanguage.settings.passedFieldClassName).addClass(validanguage.settings.failedFieldClassName).attr(validanguage.settings.errorMsgAttr,errormMsg);
        validanguage.validateTooTip($planBriefDiv.find('table')[0]);
        return false;
    } else {
    	$("#planBriefWithoutTag").val($planBriefVal);
        $planBriefDiv.find('table').removeClass(validanguage.settings.failedFieldClassName).addClass(validanguage.settings.passedFieldClassName).attr(validanguage.settings.errorMsgAttr,'');
        return true;
    }
}

function checkPlanTag(eventType){
	if(event == undefined || event == ' ') eventType = 'submit'; 
	var ev = document.createEvent('HTMLEvents');
    ev.initEvent(eventType, false, false);
	var $planTagDiv = $("#planTagDiv");
	var val = $("#planCategoryTag").val();
	var tagArray = val.split(";");
	if(val == null || val == "" || tagArray.length == 0){
		var errormMsg = validateCourseTagRequiredEM();
		$planTagDiv.removeClass(validanguage.settings.passedFieldClassName).addClass(validanguage.settings.failedFieldClassName).attr(validanguage.settings.errorMsgAttr,errormMsg);
        validanguage.validateTooTip(planTagDiv);
        return false;
	} else {
		$planTagDiv.removeClass(validanguage.settings.failedFieldClassName).addClass(validanguage.settings.passedFieldClassName).attr(validanguage.settings.errorMsgAttr,'');
		return true;
	}
}

function checkTrainee() {
	if (checkAutoComplete($(".invited_trainee_input")) && checkAutoComplete($(".option_trainee_input")) && checkAutoComplete($("#plan_public_trainees"))) {
		return true;
	} else {
		return false;
	}
}