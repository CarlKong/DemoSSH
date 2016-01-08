function planCourseValid(){
    validanguage.settings.commentDelimiter = "/>";
    validanguage.settings.focusOnerror = false;
    validanguage.settings.onsuccess = 'validanguage.removeErrorTiTle';
    validanguage.settings.onerror = 'validanguage.addErrorTitle';
    validanguage.settings.errorListText = "<strong>"+showFailedFields()+"</strong>";
    
    validanguage.el.courseName = {
    		field:'Course Name',
	        validations:[
	        {
	            name: 'validanguage.validateRequired',
	            errorMsg: validateCourseNameRequiredEM(),
	            onblur: true,
	            onsubmit: true
	        },
	        {
	            name: 'validanguage.validateMinlength(text,2)',
	            errorMsg: validateCoursenameMinlengthEM(),
	            onblur: true,
	            onsubmit: true
	        },
	        {
	            name: 'validanguage.validateMaxlength(text,200)',
	            errorMsg: validateCoursenameMaxlengthEM(),
	            onblur: true,
	            onsubmit: true
	        }
//	        ,
//	        {
//	            name: "validanguage.validateRegex(text, {expression: /([a-zA-Z\.\-\_\+\(\)\@\#\"\"\?\u4e00-\u9fa5])/, modifiers: 'g'})",
//	            errorMsg: validateInvalidCharactersEM(),
//	            onblur: true,
//	            onsubmit: true
//	        }
	    	]
    };
    validanguage.el.courseTargetPeople = {
            field:'Target People',
            validations:[
            {
                name: 'validanguage.validateRequired',
                errorMsg: validateCourseTagPeopleRequiredEM(),
                onblur: true,
                onsubmit: true
            },
            {
                name: 'validanguage.validateMaxlength(text,1000)',
                errorMsg: validateCourseTagPeopleMaxlengthEM(),
                onblur: true,
                onsubmit: true
            }
            ]
    };
}

function checkCourseBrief(eventType){
    if(event == undefined || event == ' ') eventType = 'submit'; 
    var $courseBriefDiv = $('#courseBriefDiv');
    var ev = document.createEvent('HTMLEvents');
    ev.initEvent(eventType, false, false);
    var $courseBriefVal = $("#courseBrief").parent().find('iframe').contents().find('body').text();
    if ($courseBriefVal == null || $courseBriefVal.length == 0 ){
    	var errormMsg = validateCourseBriefRequiredEM();
        $courseBriefDiv.find('table').removeClass(validanguage.settings.passedFieldClassName).addClass(validanguage.settings.failedFieldClassName).attr(validanguage.settings.errorMsgAttr,errormMsg);
        validanguage.validateTooTip($courseBriefDiv.find('table')[0]);
    	return false;
    }
    if ($courseBriefVal.length > 10000 ) {
        var errormMsg = validateCourseBriefMaxlengthEM();
        $courseBriefDiv.find('table').removeClass(validanguage.settings.passedFieldClassName).addClass(validanguage.settings.failedFieldClassName).attr(validanguage.settings.errorMsgAttr,errormMsg);
        validanguage.validateTooTip($courseBriefDiv.find('table')[0]);
        return false;
    } else {
    	//set the course brief without the brief
    	$("#courseBriefWithoutTag").val($courseBriefVal);
        $courseBriefDiv.find('table').removeClass(validanguage.settings.failedFieldClassName).addClass(validanguage.settings.passedFieldClassName).attr(validanguage.settings.errorMsgAttr,'');
        return true;
    }
}

function checkTargetPeople() {
	var targetPeople = $("#courseTargetPeople").val();
	var realStr = targetPeople.replace(/\n/g, "<br />");
	$("#courseTargetPeople").val(realStr);
}

function checkCourseTag(eventType){
	if(event == undefined || event == ' ') eventType = 'submit'; 
	var ev = document.createEvent('HTMLEvents');
    ev.initEvent(eventType, false, false);
	var $tagDiv = $("#tagDiv");
	var val = $("#courseCategoryTag").val();
	var tagArray = val.split(";");
	if(val == null || val == "" || tagArray.length == 0){
		var errormMsg = validateCourseTagRequiredEM();
		$tagDiv.removeClass(validanguage.settings.passedFieldClassName).addClass(validanguage.settings.failedFieldClassName).attr(validanguage.settings.errorMsgAttr,errormMsg);
		validanguage.validateTooTip(tagDiv);
        return false;
	} else {
		$tagDiv.removeClass(validanguage.settings.failedFieldClassName).addClass(validanguage.settings.passedFieldClassName).attr(validanguage.settings.errorMsgAttr,'');
		return true;
	}
}

