function planSessionValid(){
	validanguage.settings.commentDelimiter = "/>";
	validanguage.settings.focusOnerror = false;
    validanguage.settings.onsuccess = 'validanguage.removeErrorTiTle';
    validanguage.settings.onerror = 'validanguage.addErrorTitle';
    validanguage.settings.errorListText = "<strong>"+showFailedFields()+"</strong>";
	    validanguage.el.sessionNameInput = {
	    	field:'Session Name',
	    	validations:[
	    	{
	    		name: 'validanguage.validateRequired',
	    		errorMsg: validateSessionNameRequiredEM(),
	    		onblur: true,
	    		onsubmit: true
	    	},
	    	{
	    		name: 'validanguage.validateMinlength(text,2)',
	    		errorMsg: validateSessionNameMinlengthEM(),
	    		onblur: true,
	    		onsubmit: true
	    	},
	    	{
	    		name: 'validanguage.validateMaxlength(text,200)',
	    		errorMsg: validateSessionNameMaxlengthEM(),
	    		onkeyup:true,
	    		onblur: true
	    	}
	    	]
	    };
	    
}


/**
 * Check the plan session brief.
 * 
 * @param eventType
 * @return
 */
function checkPlanSessionBrief(eventType){
    if(event == undefined || event == ' ') eventType = 'submit'; 
    var $planSessionBriefDiv = $('#sessionBriefDiv');
    var ev = document.createEvent('HTMLEvents');
    ev.initEvent(eventType, false, false);
    var $planSessionBriefVal = $("#sessionBriefEditor").parent().find('iframe').contents().find('body').text().replace(/(\s*$)/g, "");
    if ($planSessionBriefVal == null || $planSessionBriefVal.length == 0 ){
    	var errormMsg = validatePlanSessionBriefRequiredEM();
        $planSessionBriefDiv.find("table").removeClass(validanguage.settings.passedFieldClassName).addClass(validanguage.settings.failedFieldClassName).attr(validanguage.settings.errorMsgAttr,errormMsg);
        validanguage.validateTooTip(sessionBriefDiv);
        return false;
    }
    if ($planSessionBriefVal.length > 10000 ) {
        var errormMsg = validatePlanBriefSessionMaxlengthEM();
        $planSessionBriefDiv.find("table").removeClass(validanguage.settings.passedFieldClassName).addClass(validanguage.settings.failedFieldClassName).attr(validanguage.settings.errorMsgAttr,errormMsg);
        validanguage.validateTooTip(sessionBriefDiv);
        return false;
    } else {
        $planSessionBriefDiv.find("table").removeClass(validanguage.settings.failedFieldClassName).addClass(validanguage.settings.passedFieldClassName).attr(validanguage.settings.errorMsgAttr,'');
        return true;
    }
}