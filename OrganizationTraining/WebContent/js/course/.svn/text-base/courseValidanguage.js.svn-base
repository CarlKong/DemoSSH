function courseValid(){
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
    validanguage.el.courseDuration = {
            field:'Duration',
            validations:[
            {
                name: 'validanguage.validateRequired',
                errorMsg: validateCourseDurationRequiredEM(),
                onblur: true,
                onsubmit: true
            },
            {
                name: 'validanguage.validateMaxlength(text,4)',
                errorMsg: validateCourseDurationMaxlengthEM(),
                onblur: true,
                onsubmit: true
            },
            {
                name: "validanguage.validateRegex(text, {expression: /(^[1-8]$)|(^([1-8][\.][0]{1,2})$)|(^[1-7][\.][0-9]{1,2}$)|(^[0][\.][1-9][0-9]{0,1}$)|(^[0][\.][0-9][1-9]$)|([a-z]+)|([A-Z]+)|([\u4e00-\u9fa5]+)/, modifiers: 'g'})",
                errorMsg: validateCourseDurationSmailarEM(),
                onblur: true,
                onsubmit: true
            }
           ],
            characters: {
                mode:'allow',
                expression: 'numeric.',
                suppress: true,
                errorMsg: validateInvalidCharactersEM(),
                onblur: true,
                onsubmit: true
            },
            transformations: [
            {
                name: "validanguage.format('x.x', '.')",
                onkeyup: true,
                ontyping: true,
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
    var $courseBriefVal = $("#courseBrief").parent().find('iframe').contents().find('body').text().replace(/(\s*$)/g, "");
    var imgs = $("#courseBrief").parent().find('iframe').contents().find('img');
    if(imgs.length > 0){
        $courseBriefDiv.find('table').removeClass(validanguage.settings.failedFieldClassName).addClass(validanguage.settings.passedFieldClassName).attr(validanguage.settings.errorMsgAttr,'');
    	return true;
    }
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

