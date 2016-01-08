var master_contant = "Training Master";
var trainer_contant = "Trainer";
var trainee_contant = "Trainee";
var admin_contant = "Admin";
var trainingMasterCount = 0;
var trainerCount = 0;
var traineeCount = 0;
var publishPlanId;
var prefixIDValue;
var ignoreContent = getIgnoreMessage();
var toDoPlanId;
var toDoPlanCourseId;

//judge 
var masterFinished = false;
var trainerFinished = false;
var traineeFinished = false;

$(document).ready(function(){
	addLoading();
	if($('#roleNames').val().indexOf(master_contant)>0 || $('#roleNames').val().indexOf(admin_contant)>0) {
		toDoListByRoleByPage("dashboard/getToDoListForRole", master_contant, 1, 2, true);
	} else {
		masterFinished = true;
	}
	toDoListByRoleByPage("dashboard/getToDoListForRole", trainer_contant, 1, 2, true);
	toDoListByRoleByPage("dashboard/getToDoListForRole", trainee_contant, 1, 3, true);
	$("#master_page_left").bind("click", function(){
		var pageNo = parseInt($("#master_part_title").attr("pageNo"));
		if (pageNo > 1) {
			$("#master_todo_loading").show();
			$("#training_master_content").children().remove();
			$("#training_master_content").addClass("prepare_content");
			toDoListByRoleByPage("dashboard/getToDoListForRole", master_contant, pageNo - 1, 2);
			$("#master_page_right").addClass("toDo_page_right").removeClass("toDo_page_right_disable");
		}
	});
	$("#master_page_right").bind("click", function(){
		var totalPage = parseInt($("#master_part_title").attr("totalPage"));
		var pageNo = parseInt($("#master_part_title").attr("pageNo"));
		if (totalPage>pageNo && pageNo>0) {
			$("#master_todo_loading").show();
			$("#training_master_content").children().remove();
			$("#training_master_content").addClass("prepare_content");
			toDoListByRoleByPage("dashboard/getToDoListForRole", master_contant, pageNo + 1, 2);
			$("#master_page_left").addClass("toDo_page_left").removeClass("toDo_page_left_disable");
		}
	});
	$("#trainer_page_left").bind("click", function(){
		var pageNo = parseInt($("#trainer_part_title").attr("pageNo"));
		if (pageNo > 1) {
			$("#trainer_todo_loading").show();
			$("#trainer_content").children().remove();
			$("#trainer_content").addClass("prepare_content");
			toDoListByRoleByPage("dashboard/getToDoListForRole", trainer_contant, pageNo - 1, 2);
			$("#trainer_page_right").addClass("toDo_page_right").removeClass("toDo_page_right_disable");
		}
	});
	$("#trainer_page_right").bind("click", function(){
		var totalPage = parseInt($("#trainer_part_title").attr("totalPage"));
		var pageNo = parseInt($("#trainer_part_title").attr("pageNo"));
		if (totalPage>pageNo && pageNo>0) {
			$("#trainer_todo_loading").show();
			$("#trainer_content").children().remove();
			$("#trainer_content").addClass("prepare_content");
			toDoListByRoleByPage("dashboard/getToDoListForRole", trainer_contant, pageNo + 1, 2);
			$("#trainer_page_left").addClass("toDo_page_left").removeClass("toDo_page_left_disable");
		}
	});
	$("#trainee_page_left").bind("click", function(){
		var pageNo = parseInt($("#trainee_part_title").attr("pageNo"));
		if (pageNo > 1) {
			$("#trainee_todo_loading").show();
			$("#trainee_content").children().remove();
			$("#trainee_content").addClass("prepare_content");
			toDoListByRoleByPage("dashboard/getToDoListForRole", trainee_contant, pageNo - 1, 3);
			$("#trainee_page_right").addClass("toDo_page_right").removeClass("toDo_page_right_disable");
		}
	});
	$("#trainee_page_right").bind("click", function(){
		var totalPage = parseInt($("#trainee_part_title").attr("totalPage"));
		var pageNo = parseInt($("#trainee_part_title").attr("pageNo"));
		if (totalPage>pageNo && pageNo>0) {
			$("#trainee_todo_loading").show();
			$("#trainee_content").children().remove();
			$("#trainee_content").addClass("prepare_content");
			toDoListByRoleByPage("dashboard/getToDoListForRole", trainee_contant, pageNo + 1, 3);
			$("#trainee_page_left").addClass("toDo_page_left").removeClass("toDo_page_left_disable");
		}
	});
	$("#todoList_refresh").bind("click", function(){
		$("#training_master_content").find(".toDo_content").remove();
		$("#trainer_content").find(".toDo_content").remove();
		$("#trainee_content").find(".toDo_content").remove();
		addLoading();
		if($('#roleNames').val().indexOf(master_contant)>0 || $('#roleNames').val().indexOf(admin_contant)>0) {
			toDoListByRoleByPage("dashboard/getToDoListForRole", master_contant, 1, 2, true);
		} else {
			masterFinished = true;
		}
		toDoListByRoleByPage("dashboard/getToDoListForRole", trainer_contant, 1, 2, true);
		toDoListByRoleByPage("dashboard/getToDoListForRole", trainee_contant, 1, 3, true);
	});
	
	bindTrainee2CourseSubmit(function(){
		refreshTraineePart();
	});
	bindTrainee2PlanSubmit(function(){
		refreshTraineePart();
	});
	bindTrainer2TraineeSubmit(function(){
		refreshTrainerPart();
	});
	bindTrainer2PlanSubmit(function(){
		refreshTrainerPart();
	});
	bindMaster2TrainerSubmit(function(){
		refreshMasterPart();
	});
	
});

function refreshMasterPart() {
	//Refresh To Do List
	$("#training_master_content").find(".toDo_content").remove();
	toDoListByRoleByPage("dashboard/getToDoListForRole", master_contant, 1, 2);
	//Refresh My Plan & My Course
	initPlanAndCourseListByRole(master_contant);
}

function refreshTrainerPart() {
	//Refresh To Do List
	$("#trainer_content").find(".toDo_content").remove();
	toDoListByRoleByPage("dashboard/getToDoListForRole", trainer_contant, 1, 2);
	//Refresh My Plan & My Course
	initPlanAndCourseListByRole(trainer_contant);
}

function refreshTraineePart() {
	//Refresh To Do List
	$("#trainee_content").find(".toDo_content").remove();
	toDoListByRoleByPage("dashboard/getToDoListForRole", trainee_contant, 1, 3);
	//Refresh My Plan & My Course
	initPlanAndCourseListByRole(trainee_contant);
}

/**
 * get different roles' to do list info by page, and set total to do list records
 * @param actionUrl
 * @param role
 * @param pageNo
 */
function toDoListByRoleByPage(actionUrl, role, pageNo, pageSize, isRefresh){
	// ajax request
	// draw to do list info (after ajax)
	
	$.ajax ({
		type: "POST",
        url: actionUrl,
        data: {
			"toDoListPageSize" : pageSize,
			"toDoListPageNum" : pageNo,
			"toDoListRole" : role
		},
        success: function(data) {
			toDodataCallBack(role, data, pageSize, isRefresh);
        }
	});
}

/**
 * draw different roles' to do list data on dashboard, and bind onclick operation of each part
 * @param role
 * @param data
 */
function toDodataCallBack(role, toDoPageJson, pageSize, isRefresh){
	// draw info
	// set total records (after draw info)
	var basePath = $("#basePath").val();
	var totalNum = toDoPageJson.totalPage;//Total page number.
	var isHasPrePager = toDoPageJson.isHasPrePager;
	var isHasNextPager = toDoPageJson.isHasNextPager;
	var toDoListJson = toDoPageJson.list;
	if (role === master_contant) {
		if (toDoPageJson && toDoPageJson.totalRecords) {
			trainingMasterCount = toDoPageJson.totalRecords;
		} else {
			trainingMasterCount = 0;
		}
		$("#training_master_content").removeClass("prepare_content");
		var masterPart = $("#master_part_title").text();
		if (masterPart.indexOf("(") >= 0){
			masterPart = masterPart.substring(0,masterPart.indexOf("("));
		}
		var masterPartTitle = masterPart + "(" + trainingMasterCount + ")";
		$("#master_part_title").find("span").text(masterPartTitle);
		$("#master_part_title").attr("pageNo", toDoPageJson.nowPager);
		$("#master_part_title").attr("totalPage", totalNum);
		if (totalNum > 1) {
			$("#master_page_button").show();
			if (toDoPageJson.nowPager <= 1) {
				$("#master_page_left").addClass("toDo_page_left_disable").removeClass("toDo_page_left");
				$("#master_page_right").addClass("toDo_page_right").removeClass("toDo_page_right_disable");
			}
			if (toDoPageJson.nowPager == totalNum) {
				$("#master_page_right").addClass("toDo_page_right_disable").removeClass("toDo_page_right");
			}
		} else {
			$("#master_page_button").hide();
		}
		getTotalCount();
		if (isRefresh) {
			masterFinished = true;
		}
		removeLoading();
		$("#master_todo_loading").hide();
	}
	if (role === trainer_contant) {
		if (toDoPageJson && toDoPageJson.totalRecords) {
			trainerCount = toDoPageJson.totalRecords;
		} else {
			trainerCount = 0;
		}
		$("#trainer_content").removeClass("prepare_content");
		var trainerPart = $("#trainer_part_title").text();
		if (trainerPart.indexOf("(") >= 0){
			trainerPart = trainerPart.substring(0,trainerPart.indexOf("("));
		}
		var trainerPartTitle = trainerPart + "(" + trainerCount + ")";
		$("#trainer_part_title").find("span").text(trainerPartTitle);
		$("#trainer_part_title").attr("pageNo", toDoPageJson.nowPager);
		$("#trainer_part_title").attr("totalPage", totalNum);
		if (totalNum > 1) {
			$("#trainer_page_button").show();
			if (toDoPageJson.nowPager <= 1) {
				$("#trainer_page_left").addClass("toDo_page_left_disable").removeClass("toDo_page_left");
				$("#trainer_page_right").addClass("toDo_page_right").removeClass("toDo_page_right_disable");
			}
			if (toDoPageJson.nowPager == totalNum) {
				$("#trainer_page_right").addClass("toDo_page_right_disable").removeClass("toDo_page_right");
			}
		} else {
			$("#trainer_page_button").hide();
		}
		getTotalCount();
		if (isRefresh) {
			trainerFinished = true;
		}
		removeLoading();
		$("#trainer_todo_loading").hide();
	} 
	if (role === trainee_contant) {
		if (toDoPageJson && toDoPageJson.totalRecords) {
			traineeCount = toDoPageJson.totalRecords;
		} else {
			traineeCount = 0;
		}
		$("#trainee_content").removeClass("prepare_content");
		var traineePart = $("#trainee_part_title").text();
		if (traineePart.indexOf("(") >= 0){
			traineePart = traineePart.substring(0,traineePart.indexOf("("));
		}
		var traineePartTitle = traineePart + "(" + traineeCount + ")";
		$("#trainee_part_title").find("span").text(traineePartTitle);
		$("#trainee_part_title").attr("pageNo", toDoPageJson.nowPager);
		$("#trainee_part_title").attr("totalPage", totalNum);
		if (totalNum > 1) {
			$("#trainee_page_button").show();
			if (toDoPageJson.nowPager <= 1) {
				$("#trainee_page_left").addClass("toDo_page_left_disable").removeClass("toDo_page_left");
				$("#trainee_page_right").addClass("toDo_page_right").removeClass("toDo_page_right_disable");
			}
			if (toDoPageJson.nowPager == totalNum) {
				$("#trainee_page_right").addClass("toDo_page_right_disable").removeClass("toDo_page_right");
			}
		} else {
			$("#trainee_page_button ").hide();
		}
		getTotalCount();
		if (isRefresh) {
			traineeFinished = true;
		}
		removeLoading();
		$("#trainee_todo_loading").hide();
	}
	if (toDoListJson && toDoListJson.length > 0) {
		$.each(toDoListJson, function(i, toDoItemJson) {
			var toDoNode = "";
			var toDoNode = "<div></div>";
			var $toDoNode;
			if (role === master_contant) {
				$toDoNode = $(toDoNode).appendTo($("#training_master_content"));
				$toDoNode.attr("id", "master_toDo_data_" + i);
			} 
			if (role === trainer_contant) {
				$toDoNode = $(toDoNode).appendTo($("#trainer_content"));
				$toDoNode.attr("id", "trainer_toDo_data_" + i);
			}
			if (role === trainee_contant) {
				$toDoNode = $(toDoNode).appendTo($("#trainee_content"));
				$toDoNode.attr("id", "trainee_toDo_data_" + i);
			}
			$toDoNode.addClass("toDo_content");
			
			var toDoTopLine = "<div><span class='toDo_num'>{0}</span></div>";
			var showContentSpan = "<span class='show_data_span'>{1}</span>";
			var toDoNum = toDoPageJson.firstResult + i+1+ ". ";
			var showContent = "{2} <a class='viewPlanOrCourse'>{4}</a>";
			toDoTopLine = toDoTopLine.replace("{0}", toDoNum);
			showContent = showContent.replace("{2}", toDoItemJson.itemContent);
			showContent = showContent.replace("{4}", toDoItemJson.itemTitle);
			showContentSpan = showContentSpan.replace("{1}", showContent);
			
			var $toDoTopLine = $(toDoTopLine).appendTo($toDoNode);
			$toDoTopLine.addClass("toDo_top_line");
			var $showContentSpan = $(showContentSpan).appendTo($toDoTopLine);
			if (toDoItemJson.itemType === "assessCourse" || toDoItemJson.itemType === "assessTrainees") {
				$showContentSpan.find(".viewPlanOrCourse").attr("actualCourseId", toDoItemJson.planCourseId);
				$showContentSpan.find(".viewPlanOrCourse").bind("click", function(){
					var actualCourseId = $(this).attr("actualCourseId");
					window.open(basePath + "plan/findActualCourseById?actualCourseId=" + actualCourseId);
				});
			} else {
				$showContentSpan.find(".viewPlanOrCourse").attr("planId", toDoItemJson.planId);
				$showContentSpan.find(".viewPlanOrCourse").bind("click", function(){
					var planId = $(this).attr("planId");
					window.open(basePath + "plan/viewPlanDetail?planId=" + planId);
				});
			}
			
			
			var durationTimeSpan = "<span>{0}</span>";
			durationTimeSpan = durationTimeSpan.replace("{0}", toDoItemJson.durationTime);
			var $durationTimeSpan = $(durationTimeSpan).appendTo($toDoNode);
			$durationTimeSpan.addClass("duration_time_span");
			if (toDoItemJson.itemType==="assessCourse" || toDoItemJson.itemType==="assessTrainees" 
					||toDoItemJson.itemType==="masterAssessTrainers" ||toDoItemJson.itemType==="assessPlan") {
				var assessButton = "<div></div>";
				var $assessButton = $(assessButton).appendTo($toDoNode);
				$assessButton.addClass("assess_button_div");
				var assessButtonOK = "<span></span>";
				var assessButtonNO = "<span></span>";
				$assessButtonOK = $(assessButtonOK).appendTo($assessButton);
				$assessButtonNO = $(assessButtonNO).appendTo($assessButton);
				$assessButtonOK.addClass("assess_button_ok");
				$assessButtonNO.addClass("assess_button_no");
				
				//trainee give assessment to course and trainer 
				if (toDoItemJson.itemType==="assessCourse") {
					$assessButtonOK.bind("click",function(){
						$("#form_trainee2course").attr("action",$('#basePath').val()+"assessment/create_createTrainee2courseAssessment");
						showAssessmentpopup('trainee2course_popup',toDoItemJson.itemTitle,toDoItemJson.prefixId);
						$('#trainee2course_popup #assess_planCourseId').val(toDoItemJson.planCourseId);
						initTrainee2Course(toDoItemJson.planCourseId);
					});
					$assessButtonNO.data("assessedId", toDoItemJson.planCourseId);
					$assessButtonNO.bind("click",function(){
						toDoPlanCourseId = $(this).data("assessedId");
						initialConfirmBar(ignoreContent, ignoreTrainee2courseAssessment);
					});
				}
				//trainer assess trainees and course
				if (toDoItemJson.itemType==="assessTrainees") {
					$assessButtonOK.bind("click",function(){
//						showAssessmentpopup('trainer2trainee_popup',toDoItemJson.itemTitle,toDoItemJson.prefixId);
//						$("#planCourseId_trainer2trainee").val(toDoItemJson.planCourseId);
//						$("#planId_trainer2trainee").val(toDoItemJson.planId);
//						initTrainer2Trainee(toDoItemJson.planCourseId);	
						trainer2traineePopup(toDoItemJson.planCourseId, toDoItemJson.prefixId, toDoItemJson.itemTitle, toDoItemJson.planId);
					});
					$assessButtonNO.data("assessedId", toDoItemJson.planCourseId);
					$assessButtonNO.bind("click",function(){
						toDoPlanCourseId = $(this).data("assessedId");
						initialConfirmBar(ignoreContent, ignoreTrainer2traineeAssessment);
					});
				}
				//master assess trainer
				if (toDoItemJson.itemType==="masterAssessTrainers") {
					$assessButtonOK.bind("click",function(){
						$("#form_master2trainer").attr("action",$('#basePath').val()+"assessment/create_createMaster2trainerAssessment");
						showAssessmentpopup('master2trainer_popup', toDoItemJson.itemTitle, toDoItemJson.prefixId);
						$('#master2trainer_popup #assess_planId').val(toDoItemJson.planId);
						initMaster2Trainer(toDoItemJson.planId);
					});
					$assessButtonNO.data("assessedId", toDoItemJson.planId);
					$assessButtonNO.bind("click",function(){
						toDoPlanId = $(this).data("assessedId");
						initialConfirmBar(ignoreContent, ignoreMaster2trainerAssessment );
					});
				}
				//trainee assess plan
				if (toDoItemJson.itemType==="assessPlan" && role === trainee_contant) {
					$assessButtonOK.bind("click",function(){
					showAssessmentpopup('trainee2plan_popup', toDoItemJson.itemTitle, toDoItemJson.prefixId);
					$('#trainee2plan_planId').val(toDoItemJson.planId);
					initTrainee2Plan(toDoItemJson.planId);
					});
					$assessButtonNO.data("assessedId", toDoItemJson.planId);
					$assessButtonNO.bind("click",function(){
						toDoPlanId = $(this).data("assessedId");
						initialConfirmBar(ignoreContent, ignoreTrainee2planAssessment);
					});
				}
				//trainer assess plan
				if (toDoItemJson.itemType==="assessPlan" && role === trainer_contant) {
					$assessButtonOK.bind("click",function(){
						$("#form_trainer2plan").attr("action",$('#basePath').val()+"assessment/create_createTrainer2planAssessment");
						showAssessmentpopup('trainer2plan_popup', toDoItemJson.itemTitle, toDoItemJson.prefixId);
						$('#trainer2plan_popup #assess_planId').val(toDoItemJson.planId);
						initTrainer2Plan(toDoItemJson.planId);
					});
					$assessButtonNO.data("assessedId", toDoItemJson.planId);
					$assessButtonNO.bind("click",function(){
						toDoPlanId = $(this).data("assessedId");
						initialConfirmBar(ignoreContent, ignoreTrainer2planAssessment);
					});
				}
				$assessButtonOK.poshytip({
					allowTipHover : true ,
					className: 'tip-green',
					content: $("#assessment_i18n").html()
				});
				
				$assessButtonNO.poshytip({
					allowTipHover : true ,
					className: 'tip-green',
					content: $("#ignore_i18n").html()
				});
				
			} else {
				var handlePlanButton = "<span></span>";
				var $handlePlanButton = $(handlePlanButton).appendTo($toDoNode);
				$handlePlanButton.addClass("handle_plan_button");
				
			}
			
			if (toDoItemJson.itemType === "publishPlan" ) {
				$handlePlanButton.bind("click", function(){
					publishPlanId = toDoItemJson.planId;
					prefixIDValue = toDoItemJson.prefixId;
					setConfirmBar(toDoItemJson.prefixId);
					return false;
				});
				$handlePlanButton.poshytip({
					allowTipHover : true ,
					className: 'tip-green',
					content: $("#pulblish_i18n").html()
				});
			}
			
			if (toDoItemJson.itemType === "uncompletedPlan") {
				$handlePlanButton.bind("click", function(){
					window.open(basePath + "plan/viewPlanDetail?planId=" + toDoItemJson.planId);
				});
				$handlePlanButton.poshytip({
					allowTipHover : true ,
					className: 'tip-green',
					content: $("#complete_i18n").html()
				});
			}
			
			//Add toolTip
			var showValueDiv = "<div class='show_value_div'></div>";
			var $showValueDiv = $(showValueDiv).appendTo($toDoNode);
			$showValueDiv.html(showContent);
			if ($showValueDiv.width()>330) {
				$toDoNode.find(".show_data_span").poshytip({
					allowTipHover : true ,
					className: 'tip-green',
					content: $showValueDiv.html()
				});
			}
		});
	}
	var currentLength = 0;
	if (toDoListJson) {
		currentLength = toDoListJson.length;
	}
	for (i=0; i< pageSize - currentLength; i++) {
		var toDoNode = "<div class='toDo_content'></div>";
		if (role === master_contant && $("#training_master_content").find(".toDo_content").length<pageSize) {
			$(toDoNode).appendTo($("#training_master_content"));
		}
		if (role === trainer_contant && $("#trainer_content").find(".toDo_content").length<pageSize) {
			$(toDoNode).appendTo($("#trainer_content"));
		}
		if (role === trainee_contant && $("#trainee_content").find(".toDo_content").length<pageSize) {
			$(toDoNode).appendTo($("#trainee_content"));
		}
	}
}

function getTotalCount() {
	var totalCount = trainingMasterCount + traineeCount + trainerCount;
	$("#toDoList_count_num").text(totalCount);
}

//set confirm bar operation
function setConfirmBar(publishPlanId) {
	var publishContent = getPlanPublishMrssage()+' "<span class="id_span">'+publishPlanId+'</span>" ?'+
	 '<div class="option_div">'+
	 '<p class="confirmBar_option"><span class="common_checkbox common_checkbox_unchecked" onclick="commonCheckBox(this)" id="checkBox_isSendToManager"></span>'+getPublishSendEmailMessage()+'</p></div>';
	initialConfirmBar(publishContent, toPublish);
}
function toPublish() {
	var isSendToManager;
	if ($("#checkBox_isSendToManager").attr("check") === "checked") {
		isSendToManager = 1;
	} else {
		isSendToManager = 0;
	}
	$.ajax({
		type: "POST",
        url: $('#basePath').val()+"plan/publishPlan",
        data: {
			"planId" : publishPlanId,
			"esc.isSendToManager" : isSendToManager
		},
        success: function(errorCode) {
			if (errorCode) {
				initialErrorMsgBar(errorCode.errorMessage, operationAfterPublish);
			} else {
				$("#messageBar").messageBar({
					isPrepositionId: true,
					responseMessage: ": "+$('#messageBar_publish').html(),
					itemId: prefixIDValue,
		    	    top: 100
				});
				operationAfterPublish();
			}
        }
	});
}
function operationAfterPublish() { 
	refreshMasterPart();
	refreshTrainerPart();
	refreshTraineePart();
	loadNewPublishPlanList({},true); /*method of newPublicPlan.js*/
}

function ignoreTrainee2courseAssessment(){
	$.ajax({
		type: "POST",
		url:  basePath+ "assessment/ignore_ignoreTrainee2courseAssessment",
		data: {actualCourseId:toDoPlanCourseId},
		success: function(data){
			refreshTraineePart();
		}
	});
}

function ignoreTrainer2traineeAssessment(){
	$.ajax({
		type: "POST",
		url:  basePath+ "assessment/ignore_ignoreTrainer2Trainee",
		data: {actualCourseId:toDoPlanCourseId,
			   planId:toDoPlanId},
		success: function(data){
			refreshTrainerPart();
		}
	});
}

function ignoreMaster2trainerAssessment(){
	$.ajax({
		type: "POST",
		url:  basePath+ "assessment/ignore_ignoreMaster2trainerAssessment",
		data: {planId:toDoPlanId},
		success: function(data){
			refreshMasterPart();
		}
	});
}

function ignoreTrainee2planAssessment(){
	$.ajax({
		type: "POST",
		url:  basePath+ "assessment/ignore_ignoreTrainee2planAssessment",
		data: {planId:toDoPlanId},
		success: function(data){
			refreshTraineePart();
		}
	});
}

function ignoreTrainer2planAssessment(){
	$.ajax({
		type: "POST",
		url:  basePath+ "assessment/ignore_ignoreTrainer2planAssessment",
		data: {planId:toDoPlanId},
		success: function(data){
			refreshTrainerPart();
		}
	});
}

/**
 * operation of loading
 * @return
 */
function removeLoading() {
	if (masterFinished && trainerFinished && traineeFinished) {
		$("#to_do_loader").hide();
		masterFinished = false;
		trainerFinished = false;
		traineeFinished = false;
	}
}

function addLoading() {
	$('.to_do_list').addClass('to_do_loading');
	$("#to_do_loader").show();
}
