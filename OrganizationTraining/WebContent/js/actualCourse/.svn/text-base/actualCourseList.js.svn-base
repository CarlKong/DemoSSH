var actualCount = 0;  //The total count of actual course list. (exclude deleted)
var maxCount = 0;     //The total count user has selected. (include deleted)
/**
 * Create actual course list in create/edit plan page.
 * @param jsonList
 * @param isForEdit
 * @param isEnterEdit
 */
function createActualCourseList(jsonList, currentTime) {
	var trCount = $('#planCourseList').find(".courseListDataBg").length;
	$.each(jsonList, function(index, actualCourseJson) {
		appendActualCourseData(actualCourseJson, currentTime);
	});
	validanguage.populate.call(validanguage);
	initSortable();//Sortable
}

/**
 * Append actual course data to course list.
 * @param actualCourseJson
 * 		  currentTime: Server time, only used for edit.
 */
function appendActualCourseData(actualCourseJson, currentTime) {
	actualCount++;
	maxCount++;
	var isEnable = true;
	$(".courseListHead").show();
	$("#orderButton").show();
	$("#planCourseList").show();
	var planCourseDate = "";
	var startTime = "";
	var endTime = "";
	//When edit plan, start time and end time are not null
	if (actualCourseJson.courseStartTime && actualCourseJson.courseStartTime.length >= 15) {
		planCourseDate = actualCourseJson.courseStartTime.substr(0,10);
		startTime = actualCourseJson.courseStartTime.substr(11,5);
	}
	if (actualCourseJson.courseEndTime && actualCourseJson.courseEndTime.length >= 15) {
		endTime = actualCourseJson.courseEndTime.substr(11,5);
		if (!(actualCourseJson.courseStartTime) || actualCourseJson.courseStartTime=="") {
			planCourseDate = actualCourseJson.courseEndTime.substr(0,10);
		}
	}
	//Order by time.
	if (actualCourseJson.actualCourseDate && actualCourseJson.actualCourseDate.length === 10) {
		planCourseDate = actualCourseJson.actualCourseDate;
	}
	if (actualCourseJson.courseStartTime && actualCourseJson.courseStartTime.length === 5) {
		startTime = actualCourseJson.courseStartTime;
	}
	if (actualCourseJson.courseEndTime && actualCourseJson.courseEndTime.length === 5) {
		endTime = actualCourseJson.courseEndTime;
	}
	//Edit
	if (startTime == "00:00") {
		startTime = "";
	}
	if (endTime == "00:00") {
		endTime = "";
	}
	if (currentTime && planCourseDate != "" && $("#hide-planIsPublish").val() === "1") {
		if (startTime == "") {
			var currentDate = currentTime.substr(0,10);
			if (currentDate > planCourseDate) {
				isEnable = false;
			}
		} else {
			var fullStartTime;
			var startTimeValues = startTime.split(':');
			if (startTimeValues[0].length == 1) {
				fullStartTime = planCourseDate + " 0" + startTime;
			} else if (startTimeValues[0].length == 2){
				fullStartTime = planCourseDate + " " + startTime;
			}
			if (currentTime > fullStartTime) {
				isEnable = false;
			}
		}
	}
	var $actualCourseRecord = $('<div class="courseListDataBg"></div>').appendTo($('#planCourseList'));
	$actualCourseRecord.attr("id", "courseListData_" + maxCount);
	$actualCourseRecord.data("courseListRecord", actualCourseJson);
	var $realNameDiv = $('<div class="realNameSpan"></div>').appendTo($actualCourseRecord);
	var $changeDiv = $('<div class="changeDiv"></div>').appendTo($actualCourseRecord);
	//Left border when drag.
	$('<div class="changeDivLeft"></div>').appendTo($changeDiv);
	var $changeDivMid = $('<div class="changeDivMid"></div>').appendTo($changeDiv);
	//Drag button
	var $dragButton = $('<span class="dragButtonSpan courseListData"><span class="dragButtonIcon"></span></span>').appendTo($changeDivMid);
	if (isEnable) {
		$dragButton.find(".dragButtonIcon").addClass("dragButton");
	} else {
		$dragButton.find(".dragButtonIcon").addClass("dragButtonDisable");
	}
	//Order Number
	var $coureNum = $('<span class="noShowLabel courseListData"></span>').appendTo($changeDivMid).text(actualCount);
	$actualCourseRecord.data("courseListRecord").courseOrder = actualCount;
	//Actual course name
	var $actualCourseNameSpan = $('<span class="nameLabel courseListData"></span>').appendTo($changeDivMid);
	//var $courseName = $('<span class="actualCourseName"></span>').appendTo($actualCourseNameSpan).appendTo($actualCourseNameSpan).html(actualCourseJson.courseName);
	var $courseName;
	//the following operation is to handle the special characters
	if(actualCourseJson.courseInfo){
		var $targetTraineeConverter = $('<span></span>');
		$targetTraineeConverter.html(actualCourseJson.courseInfo.courseTargetTrainee);
		actualCourseJson.courseInfo.courseTargetTraineeHasTag = $targetTraineeConverter.text();
		$courseName = $('<span class="actualCourseNameShow"></span>').appendTo($actualCourseNameSpan).appendTo($actualCourseNameSpan).html(actualCourseJson.courseName);
		$realNameDiv.html(actualCourseJson.courseName);
	} else {
		$courseName = $('<span></span>').appendTo($actualCourseNameSpan).appendTo($actualCourseNameSpan).text(actualCourseJson.courseName);
		$realNameDiv.text(actualCourseJson.courseName);
	}
	actualCourseJson.courseNameHasTag = $courseName.text();
	
	if (actualCourseJson.courseInfo && actualCourseJson.courseInfo.courseType) {
		$courseName.attr("courseType", "course");
		$actualCourseRecord.attr("courseType", "course");
	} else {
		$courseName.attr("courseType", "session");
		$actualCourseRecord.attr("courseType", "session");
	}
	$courseName.attr("courseNumber", maxCount);
	if (isEnable) {
		$courseName.addClass("actualCourseName");
		$courseName.bind("click", function(){
			if ($(this).attr("courseType") == "course") {
			    $("#course_save_btn").attr("targetCourse", $(this).attr("courseNumber"));
				prepareEditPlanCourse($(this).attr("courseNumber"));
			}
			if ($(this).attr("courseType") == "session") {
				$("#createPlanSessionBtn").attr("id", "editPlanSessionBtn");
				 $("#editPlanSessionBtn").attr("targetCourse", $(this).attr("courseNumber"));
				showSessionPopup($(this).attr("courseNumber"));
			}
		});
	} else {
		$courseName.addClass("courseNameDisable");
	}
	
	if ($realNameDiv.width()>340) {
		$courseName.poshytip({
			allowTipHover : true ,
			className: 'tip-green',
			content: $realNameDiv.html()
		});
	}
	//Room
	var $courseRoomSpan = $('<span class="roomLabel courseListData"></span>').appendTo($changeDivMid);
	var $courseRoomInput = $('<input>', {'class':'roomInput', 'type':'text'}).appendTo($courseRoomSpan);
	$courseRoomInput.val(actualCourseJson.courseRoomNum);
	$courseRoomInput.attr("id", "courseRoom_" + maxCount);
	var roomValidate = '<!--<validanguage target="courseRoom_'+maxCount+'" validations="validateRoom(text, '+maxCount+')" errorMsg="'+validateRoomValueEM()+'" onblur="true" />-->';
	$(roomValidate).appendTo($changeDivMid);
	//Date
	var $courseDateSpan = $('<span class="dateLabel courseListData"></span>').appendTo($changeDivMid);
	var $courseDateInput = $('<input>', {'class':'dateInputText', 'type':'text', 'readonly':'true'}).appendTo($courseDateSpan);
	$courseDateInput.val(planCourseDate);
	$courseDateInput.attr("id", "planCourseDate_" + maxCount);
	$actualCourseRecord.data("courseListRecord").actualCourseDate = planCourseDate;
	if (isEnable) {
		$courseDateInput.datepicker({
	    	changeMonth: true,
	    	changeYear: true,
	    	dateFormat: "yy-mm-dd",
	    	onSelect: function(dateText, inst) {
				$actualCourseRecord.data("courseListRecord").actualCourseDate = dateText;
			}
	    });
	}
	//Start Time
	var $startTimeSpan = $('<span class="startLabel courseListData"></span>').appendTo($changeDivMid);
	var $startTimeInput = $('<input>', {'class':'startInputText', 'type':'text'}).appendTo($startTimeSpan);
	$startTimeInput.attr("id", "planCourseStartTime_"+maxCount);
	$startTimeInput.val(startTime);
	$('<span class="toTag"></span>').appendTo($startTimeSpan);
	var planCourseStartTimeValidate = "";
	if (isEnable) {
		planCourseStartTimeValidate = '<!--<validanguage target="planCourseStartTime_'+maxCount+'" validations="compareTime(text,'+maxCount+')" errorMsg="'+validateStartTimeValueEM()+'" onblur="true" />-->'
			+ '<!--<validanguage target="planCourseStartTime_'+maxCount+'" validations="validateTimeStyle(text, '+maxCount+')" errorMsg="'+validateStartTimeStyleEM()+'" onblur="true" />-->'
			+ '<!--<validanguage target="planCourseStartTime_'+maxCount+'" validations="getDuration(text, '+maxCount+')" errorMsg="'+validateEndTimeValueEM()+'" onblur="true" />-->'
			+ '<!--<validanguage target="planCourseDate_'+maxCount+'" validations="compareDate(text, '+maxCount+')" errorMsg="'+validateDateValueEM()+'" onblur="true" />-->'
			+ '<!--<validanguage target="planCourseEndTime_'+maxCount+'" validations="durationValidate(text, '+maxCount+')" errorMsg="'+validateCourseDurationSmailarEM()+'" onblur="true" />-->';

	}
	$(planCourseStartTimeValidate).appendTo($changeDivMid);
    $actualCourseRecord.data("courseListRecord").courseStartTime = startTime;
	//End time
	var $endTimeSpan = $('<span class="finishLabel courseListData"></span>').appendTo($changeDivMid);
	var $endTimeInput = $('<input>', {'class':'finishInput', 'type':'text'}).appendTo($endTimeSpan);
	$endTimeInput.attr("id", "planCourseEndTime_"+maxCount);
	$endTimeInput.val(endTime);
	var planCourseEndTimeValueValidate = "";
	if (isEnable) {
		planCourseEndTimeValueValidate = '<!--<validanguage target="planCourseEndTime_'+maxCount+'" validations="getDuration(text, '+maxCount+')" errorMsg="'+validateEndTimeValueEM()+'" onblur="true" />-->'
        	+ '<!--<validanguage target="planCourseEndTime_'+maxCount+'" validations="validateTimeStyle(text, '+maxCount+')" errorMsg="'+validateEndTimeStyleEM()+'" onblur="true" />-->'
        	+ '<!--<validanguage target="planCourseEndTime_'+maxCount+'" validations="durationValidate(text, '+maxCount+')" errorMsg="'+validateCourseDurationSmailarEM()+'" onblur="true" />-->';


	}
	$(planCourseEndTimeValueValidate).appendTo($changeDivMid);
	$actualCourseRecord.data("courseListRecord").courseEndTime = endTime;
	//Duration
	var $durationSpan = $('<span>', {'id':'originalCourseDuration_' + maxCount, 'class':'durationLabel courseListData'}).appendTo($changeDivMid).text(actualCourseJson.courseDuration + "h");
	//Trainer
	var $trainerSpan = $('<span class="trainerLabel courseListData"></span>').appendTo($changeDivMid);
	var $trainerInput = $('<input>', {'class':'trainerInput', 'type':'text'}).appendTo($trainerSpan);
	if (actualCourseJson.courseTrainer) {
		$trainerInput.val(actualCourseJson.courseTrainer);
	}
	$trainerInput.bind("blur", function(){
		$actualCourseRecord.data("courseListRecord").courseTrainer = $(this).val();
	});
	configTrainerAutoComplete($trainerInput);
	//Operation
	var $actionDiv = $('<div>', {'class':'actionShowLabel courseListData'}).appendTo($changeDivMid);
	//Setting button, plan session does not have this button.
	var $settingButton = null;
	if (actualCourseJson.courseInfo && actualCourseJson.courseInfo.courseType) {
		$settingButton = $('<input>', {'type':'button', 'class':'settingButton', 'status':'fold'}).appendTo($actionDiv);
		//Setting div
		var $settingDiv = $('<div class="settingDiv"></div>').appendTo($actualCourseRecord);
		var $assessLable = $('<div class="assessLabel">'+getAssessmentLabel()+'</div>').appendTo($settingDiv);
		$assesmentCheck = $('<div class="assessment"></div>').appendTo($settingDiv);
		var $firseCheckBox = $('<span class="common_checkbox assessment_checkbox">').appendTo($assesmentCheck);
		$('<span class="checkLabel">'+getTrainee2CourseLabel()+'</span>').appendTo($assesmentCheck);
		setAssessmentCheckbox($firseCheckBox, actualCourseJson.courseInfo.trainee2Trainer, isEnable);
		$firseCheckBox.attr("courseNum", maxCount);
		
		var $secondCheckBox = $('<span class="common_checkbox assessment_checkbox">').appendTo($assesmentCheck);
		$('<span class="checkLabel">'+getTrainer2TraineeLabel()+'</span>').appendTo($assesmentCheck);
		$('<div class="clear"></div>').appendTo($settingDiv);
		setAssessmentCheckbox($secondCheckBox, actualCourseJson.courseInfo.trainer2Trainee, isEnable);
		$secondCheckBox.attr("courseNum", maxCount);
		$settingButton.poshytip({
			allowTipHover : true ,
			className: 'tip-green',
			content: getSetAssessmentToolTip()
		});
		if (isEnable) {
			$assessLable.addClass("assessLabelEn");
			$firseCheckBox.bind("click", function(){
				clickAssessmentCheckBox($firseCheckBox, "trainee2Trainer");
			});
			$secondCheckBox.bind("click", function(){
				clickAssessmentCheckBox($secondCheckBox, "trainer2Trainee");
			});
		} else {
			$assessLable.addClass("settingLabelDis");
			$settingDiv.find(".checkLabel").addClass("settingLabelDis");
			$settingDiv.find(".checkLabe2").addClass("settingLabelDis");
		}
		
		$settingButton.bind("click", function(){
			clickSettingButton($settingButton, $settingDiv);
		});
		
	}
	//Delete button
	var $deleteButton = $('<input>', {'type':'button'}).appendTo($actionDiv);
	$deleteButton.poshytip({
		allowTipHover : true ,
		className: 'tip-green',
		content: getDeleteCourseToolTip()
	});
	if (isEnable) {
		$deleteButton.addClass("removePlanCourse");
		$deleteButton.bind("click", function(){
			setConfirmBar($actualCourseRecord, true);
		});
	} else {
		$deleteButton.addClass("removeDisable");
	}
	//Right border when drag.
	$('<div class="changeDivRight"></div>').appendTo($changeDiv);
	if (!isEnable) {
		$coureNum.addClass("noShowLabelDisable");
		$courseRoomInput.attr("readonly", "readonly");
		$courseRoomInput.addClass("inputDisable");
		$courseDateInput.addClass("inputDisable");
		$startTimeInput.addClass("inputDisable");
		$startTimeInput.attr("readonly", "readonly");
		$startTimeInput.unbind("blur");
		$endTimeInput.addClass("inputDisable");
		$endTimeInput.attr("readonly", "readonly");
		$durationSpan.addClass("noShowLabelDisable");
		$trainerInput.addClass("inputDisable");
		$trainerInput.attr("readonly", "readonly");
	}
}

function setConfirmBar($removeNode, isSort) {
	var confirmContent = getCourseDeleteMessage();
	var funcParam = {
		node: $removeNode,
		isSort: isSort
	};
	initialConfirmBar(confirmContent, deleteRecord, undefined, funcParam);
}

function deleteRecord(funcParam) {
	var $actualCourseRecord = funcParam.node;
	var isSort = funcParam.isSort;
	$actualCourseRecord.remove();
	actualCount--;
	if (actualCount == 0) {
		$(".courseListHead").hide();
		$("#orderButton").hide();
	}
	if (isSort) {
		resortActualCourseList();
	}
}

function resortActualCourseList() {
	$(".noShowLabel").each(function(i, number){
		$(number).html(i+1);
	});
	$(".courseListDataBg").each(function(i,courseNode){
		$(courseNode).data("courseListRecord").courseOrder = (i + 1);
	});
}

/**
 * Set checkbox result
 * @param $checkBox
 * @param checkValue
 */
function setAssessmentCheckbox($checkBox, checkValue, isEnable) {
	$checkBox.attr("checkValue", checkValue);
	if (checkValue === 1) {
		if (isEnable) {
			$checkBox.addClass("common_checkbox_checked").removeClass("common_checkbox_unchecked");
		} else {
			$checkBox.addClass("selected_checkbox_disable");
		}
		
	}
	if (checkValue === 0) {
		if (isEnable) {
			$checkBox.addClass("common_checkbox_unchecked").removeClass("common_checkbox_checked");
		} else {
			$checkBox.addClass("unselected_checkbox_disable");
		}
	}
}

/**
 * Click checkbox.
 * @param $checkBox
 * @return
 */
function clickAssessmentCheckBox($checkBox, type) {
	if ($checkBox.attr("checkValue") === "0") {
		setAssessmentCheckbox($checkBox, 1, true);
		$("#courseListData_" + $checkBox.attr("courseNum")).data("courseListRecord").courseInfo[type] = 1;
		return;
	}
	if ($checkBox.attr("checkValue") === "1") {
		setAssessmentCheckbox($checkBox, 0, true);
		$("#courseListData_" + $checkBox.attr("courseNum")).data("courseListRecord").courseInfo[type] = 0;
		return;
	}
}

function clickSettingButton($setButton, $setPanal) {
	if ($setButton.attr("status") == "fold") {
		$setButton.attr("status", "expand");
		$setButton.addClass("settingActive").removeClass("settingButton");
		$setPanal.show();
		return;
	}
	if ($setButton.attr("status") == "expand") {
		$setButton.attr("status", "fold");
		$setButton.addClass("settingButton").removeClass("settingActive");
		$setPanal.hide();
		return;
	}
}

/**
 * Configure auto complete trainer input field
 * @param employeeList
 * @param $inputFiled
 * @return
 */
function configTrainerAutoComplete($inputFiled) {
	var employeeList = $(document).find("body").data("employeeNamesData");
	$inputFiled.autocomplete({
		source: function( request, response ) {
		var maxRow = 5;
		var matcher = new RegExp( "^"+$.ui.autocomplete.escapeRegex( request.term ), "i" );
		response( $.grep( employeeList, function( value ) {
			value = value.label || value.value || value;
			return matcher.test( value );
		}).slice(0, maxRow + 1));
		}
	});
}

/**
 * Initialize sortable plug-in for plan course list [$("#planCourseList")] in create/edit plan page.
 */
function initSortable(){
	$("#planCourseList").sortable({
		items:'.courseListDataBg',
		placeholder: "placeholder",
		containment:".createplan_content",
		start: function(event, ui) {
		    ui.helper.find(".changeDiv").addClass("dragDiv");
		    ui.helper.find(".changeDiv").find(".changeDivLeft").addClass("dragLeft");
		    ui.helper.find(".changeDiv").find(".changeDivMid").addClass("dragMid");
		    ui.helper.find(".changeDiv").find(".changeDivRight").addClass("dragRight");
		    ui.helper.find(".settingDiv").css("margin-left","4px");
		    $("#orderByTime").removeClass("orderByTimeActive");
			$("#orderByTime").addClass("orderByTime");
		},
		
		stop: function(event, ui) {
			$(ui.item).find(".changeDiv").find(".changeDivLeft").removeClass("dragLeft");
			$(ui.item).find(".changeDiv").find(".changeDivMid").removeClass("dragMid");
			$(ui.item).find(".changeDiv").find(".changeDivRight").removeClass("dragRight");
			$(ui.item).find(".changeDiv").removeClass("dragDiv");
			$(ui.item).find(".settingDiv").css("margin-left","0px");
			resortActualCourseList();
		}.bind(this)
	});
	$("#planCourseList").sortable('option', 'handle', '.dragButton'); 
	$( "#planCourseList" ).disableSelection();
}

/**
 * Validate actual course room
 * @param text
 * @param currentNo
 * @return
 */
function validateRoom(text,currentNo) {
	if (text.length>30) {
		return false;
	} 
	$("#courseListData_" + currentNo).data("courseListRecord").courseRoomNum = text;
	return true;
}

/***************** START validate date and time of actual course *****************************/
/**
 * validate start time and end time and calculate duration.
 * @param text
 * @param currentNo
 * @return
 */
function getDuration(text,currentNo) {
	return countDuration(currentNo, false);
}

function durationValidate(text, currentNo) {
	return countDuration(currentNo, true);
}

function countDuration(currentNo, isValidateDuration) {
	var startTime = $("#planCourseStartTime_"+currentNo).val();
	var endTime = $("#planCourseEndTime_"+currentNo).val();
	$("#courseListData_" + currentNo).data("courseListRecord").courseStartTime = startTime;
	$("#courseListData_" + currentNo).data("courseListRecord").courseEndTime = endTime;
	if (("" != startTime)&&("" != endTime)) {
		var durationTime = new Number(2);
		var startTimeValues = startTime.split(':');
		var endTimeValues = endTime.split(':');
		var startHour = parseInt(startTimeValues[0]);
		var endHour = parseInt(endTimeValues[0]);
		var startMinute = parseInt(startTimeValues[1]);
		var endMinute = parseInt(endTimeValues[1]);
		durationTime = endHour - startHour + (endMinute-startMinute)/60;
		if (isValidateDuration) {
			if (durationTime>8){
				$(this).attr("validation","1");
				return false;
			}
		} else {
			if (durationTime <=0){
				$(this).attr("validation","1");
				return false;
			}
		}
		
		$("#courseListData_" + currentNo).data("courseListRecord").courseDuration = durationTime.toFixed(2);
		$("#originalCourseDuration_"+currentNo).html(durationTime.toFixed(2)+"h");
		$(this).attr("validation","0");
		if ($(this).attr("id")==("planCourseEndTime_"+currentNo) && $("#planCourseStartTime_"+currentNo).attr("validation")
				&& $("#planCourseStartTime_"+currentNo).attr("validation") == "1") {
			$("#planCourseStartTime_"+currentNo).focus();
			$("#planCourseStartTime_"+currentNo).blur();
		} else if ($(this).attr("id")==("planCourseStartTime_"+currentNo) && $("#planCourseEndTime_"+currentNo).attr("validation")
				&& $("#planCourseEndTime_"+currentNo).attr("validation") == "1") {
			$("#planCourseEndTime_"+currentNo).focus();
			$("#planCourseEndTime_"+currentNo).blur();
		}
	}
	return true;
}

/**
 * Compare date with current date
 * @param text
 * @return
 */
function compareDate(text, currentNo) {

	var inputDateValues = text.split('-');
	var inputDate = new Date();
	var currentDate = new Date();
	inputDate.setYear(inputDateValues[0]);
	inputDate.setMonth(inputDateValues[1]-1);
	inputDate.setDate(inputDateValues[2]);
	if (inputDate < currentDate){
		return false;
	}
	return true;
}

/** compare input start time with current time **/
function compareTime(text,currentNo) {
	var curentDate = new Date();
	var startTime = $("#planCourseStartTime_"+currentNo).val();
	var courseDate = $("#planCourseDate_"+currentNo).val();
	if ((""!=startTime)&&(""!=courseDate)) {
		var planCourseDateValues = courseDate.split('-');
		var planCourseTimeValues = startTime.split(':');
		var planCourseDateTime = new Date();
		planCourseDateTime.setYear(planCourseDateValues[0]);
		planCourseDateTime.setMonth(planCourseDateValues[1]-1);
		planCourseDateTime.setDate(planCourseDateValues[2]);
		planCourseDateTime.setHours(planCourseTimeValues[0]);
		planCourseDateTime.setMinutes(planCourseTimeValues[1]);
		if (planCourseDateTime<curentDate) {
			return false;
		}
	}
	return true;
}

/** validate time style **/
function validateTimeStyle(text) {
	if (''!=text) {
		var regex = /^(([01]{0,1})[0-9]|2[0-3])\:[0-5][0-9]$/;
		if(!(text.match(regex))){
	        return false;
	    }
	}
	return true;
}
/***************** END validate date and time of actual course *****************************/

/**
 * collect all actual course json object from node, and join them to String.
 */
function makeActualCourseJson() {
	var actualCoursesJsonList = [];
	var actualCourseJsonStr ="{actualCourses:[";
	$("#planCourseList").find(".courseListDataBg").each(function(index, actualCourseNode){
		var actualJson = $(actualCourseNode).data("courseListRecord");
		actualCoursesJsonList.push(JSON.stringify(actualJson));
	});
	actualCourseJsonStr += actualCoursesJsonList.join(",")+"]}";
	$("#actualCourseJsonStr").val(actualCourseJsonStr);
	console.log(actualCourseJsonStr);
}

/**
 * Click order course list by time.
 */
function orderCourseListByTime() {
	actualCount = 0;  //The total count of actual course list. (exclude deleted)
	var actualCoursesJsonList = [];
	$(".courseListDataBg").each(function(index, actualCourseNode){
		var actualJson = $(actualCourseNode).data("courseListRecord");
		actualCoursesJsonList.push(actualJson);
	});
	if (actualCoursesJsonList.length <= 1) {
		return;
	}
	var jsonListAfterOrder = [];  //actual course list after order
	var courseListHaveST = [];    //actual course list which have start time
	var courseListNoST = [];      //actual course list which have no start time
	for (var i=0; i<actualCoursesJsonList.length; i++) {
		if (""==actualCoursesJsonList[i].actualCourseDate) {
			courseListNoST.push(actualCoursesJsonList[i]);
		} else {
			courseListHaveST.push(actualCoursesJsonList[i]);
		}
	}
	if (courseListHaveST.length>1) {
		for (var i=0; i<courseListHaveST.length-1; i++) {
			for (var j=i+1; j<courseListHaveST.length; j++) {
				if (getActualCourseStartTime(courseListHaveST[j]) < getActualCourseStartTime(courseListHaveST[i])) {
					var courseJson = courseListHaveST[j];
					courseListHaveST[j] = courseListHaveST[i];
					courseListHaveST[i] = courseJson;
				}
			}
		}
	}
	jsonListAfterOrder = jsonListAfterOrder.concat(courseListHaveST, courseListNoST);
	for (var i=0; i<jsonListAfterOrder.length; i++) {
		jsonListAfterOrder.courseOrder = i + 1;
	}
	$(".courseListDataBg").remove();
	createActualCourseList(jsonListAfterOrder);
}

function getActualCourseStartTime(json) {
	var startTimeValues = json.courseStartTime.split(':');
	if (startTimeValues[0].length == 1) {
		json.courseStartTime = "0" + json.courseStartTime;
	}
	if (json.courseEndTime && "" != json.courseEndTime) {
		var endTimeValues = json.courseEndTime.split(':');
		if (endTimeValues[0].length == 1) {
			json.courseEndTime = "0" + json.courseEndTime;
		}
	}
	return json.actualCourseDate + json.courseStartTime;
}

/**
 * get all actual courses json for edit page.
 */
function getActualCoursesJsonForEdit(){
	var actualCoursesJson = $("#actualCourseJsonStr").val();
	var actualCourses = eval("(" + actualCoursesJson + ")");
	var currentTime = getServerCurrentTime().currentTime;
	if ("" != actualCoursesJson){
		createActualCourseList(actualCourses, currentTime);
	}
}

/**
 * check if course list has session
 * used when change plan type
 * @return
 */
function checkHasSession(){
	var hasSession = false;
	$(".courseListDataBg").each(function(index, node){
		if ($(node).attr("courseType") === "session") {
			hasSession = true;
			return;
		}
	});
	return hasSession;
}