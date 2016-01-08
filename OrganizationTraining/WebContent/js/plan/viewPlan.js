/**info of plan*/
var planId = $('#plan_id').val();
var prefixIDValue = $('#plan_prefixIDValue').val();
var planType = $('#plan_type').val();
var operationFlag = $('#operationFlag').val();
var loginEmployeeName = $('#loginEmployeeName').val();
var plan_invited_trainees = $('#plan_invited_trainees').val();
var plan_specific_trainees = $('#plan_specific_trainees').val();
var isAllEmployee = $('.isAllEmployee').val();
/**flag*/
var isInvited = false;
var isPublic = false;
var isAllEmployeeFlag = false;
var isPlanSession = false;
/**constant*/
var HAVE_JOIN = 1;
var planIsPublished = $("#planIsPublished").val();

$(document).ready(function(){
	// plan type
	if("Invited" == planType) {
		isInvited = true;
		$('#invitedContent').css('display','inline');
	}
	if("Public" == planType) {
		isPublic = true;
		$('#publichContent').css('display','inline');
		if(1 == isAllEmployee) {
			isAllEmployeeFlag = true;
		}
	}
	//hide the action button firstly
	$("#view_plan_id_right").css('visibility','hidden');
	$("#action_active_launch").css('visibility','hidden');
	// message bar
	setMessageBar();
	// action button
	$('#view_plan_id_right').mouseenter(function(){
        $(this).find('#action_active_launch').css('visibility','hidden');
        $(this).find('#action_active_shrink').css('visibility','visible');
        $(this).find('#action_part').css('visibility','visible');
    });
    $('#view_plan_id_right').mouseleave(function(){
    	$(this).find('#action_active_shrink').css('visibility','hidden');
        $(this).find('#action_active_launch').css('visibility','visible');
    	$(this).find('#action_part').css('visibility','hidden');
    });
    
    // plan course list
//	var coursesList = eval($("#coursesList").val());
//	if (null == coursesList || "" == coursesList) {
//		$('.courseListHead').css('display','none');
//	}
//	courseListCallBack(coursesList);
	refreshOrGetCourseList();
    // attachment
	planViewAttachment();
    $('.plan_previous').each(function(){
    	$(this).click(function(){
    		goPrevious();
    	});
    });
    $('.plan_next').each(function(){
    	$(this).click(function(){
    		goNext();
    	});
    });
    $('.plan_back_to_search').each(function(){
    	$(this).click(function(){
    		backSearch();
    	});
    });
	getPlanInfo();
	bindTrainee2PlanSubmit(function(){
		getPlanInfo();
	});
	bindTrainer2PlanSubmit(function(){
		getPlanInfo();
	});
	bindMaster2TrainerSubmit(function(){
		getPlanInfo();
	});
});

//set Previous and Next
function goPrevious(){
    var form = $('#preornext');
    $("#forChangeLocaleFlag").val(false);
    form.attr("action", "viewPreviousRecordDetail");  
    form.submit();
}

function goNext(){
    var form = $('#preornext');
    $("#forChangeLocaleFlag").val(false);
    form.attr("action", "viewNextRecordDetail");
    form.submit();
}

// set Back Search
function backSearch(){
    var form = $('#preornext');
    form.attr("action", "preSearchPlan");
    form.submit();
}

// sort plan course by course order 
function sortCourseOrder(a, b) {
	return a.courseOrder - b.courseOrder;
}

function courseListCallBack(data) {
	var tableData = "";// append table data
	var planCourseNo = 1;
	sortData = data.sort(sortCourseOrder);
	$.each(sortData, function(i, n) {
		tableData = makePlanCourseData(n, planCourseNo);
		$('#courseListContent').append(tableData);
		planCourseNo++;
	});
	/**name tooltip*/
	$(".realNameSpan").each(function(i, node){
		if ($(node).width()>150) {
			$(node).parent().find(".nameDetail").poshytip({
				allowTipHover : true ,
				className: 'tip-green',
				content: $(node).html()
			});
		}
	});
	/**trainer name tooltip*/
	$(".realPlanCourseTranerSpan").each(function(i, node){
		if ($(node).width()>90) {
			$(node).parent().find(".planCourseTraner").poshytip({
				allowTipHover : true ,
				className: 'tip-green',
				content: $(node).html()
			});
		}
	});
	/**judge action*/
	if(isInvited) {
		/**when plan is invited, only trainee of this plan can do action*/
		if(plan_invited_trainees.indexOf(loginEmployeeName) < 0) {
			hiddenAction();
		}
	}
	if(isPublic) {
		/**if this public plan is not for all employee, and he is not this plan's specific trainee, so he can't see action*/
		if((isAllEmployeeFlag == false) && (plan_specific_trainees.indexOf(loginEmployeeName) < 0)) {
			hiddenAction();
		}
	}
}

function makePlanCourseData(n, num) {
	var startTimeDetail = n.courseStartTime;
	var endTimeDetail = n.courseEndTime;
	var date = "";
	var startTime = "";
	var endTime = "";
	if (null != startTimeDetail && "" != startTimeDetail) {
		date = startTimeDetail.substr(0,4)+"-"+startTimeDetail.substr(5,2)+"-"+startTimeDetail.substr(8,2); 
		startTime = startTimeDetail.substr(11,5);
	}
	if (null != endTimeDetail && "" != endTimeDetail) {
		endTime = endTimeDetail.substr(11,5);
	}
	var realNameDiv = "<div class='realNameSpan'>"+n.courseName+"</div>";
	var realPlanCourseTranerDiv = "<div class='realPlanCourseTranerSpan'>"+n.courseTrainer+"</div>";
    var courseListDateBg = "<div id='courseList_"+num+"' class='courseListDateBg'>";
    var tdNum = "<span class='noShowLabel courseListDate'>"+num+"</span>";
    var prefixIDValue = n.prefixIdValue;
    var planCourseNameTd = "";
    if (prefixIDValue.indexOf("PS") == 0 ) {
    	// The prefixIDValue is start with "PS", means this object is a plan session.
    	isPlanSession = true;
    	planCourseNameTd = "<span class='nameLabel courseListDate'><a class='a_type' href='"+$('#basePath').val()+"plan/findActualCourseById?actualCourseId="+n.actualCourseId+"' target='_blank' id='courseName_"+num+"'><span class='nameDetail'>"+n.courseName+"</span></a></span>";
    } else {
    	isPlanSession = false;
    	// This object is a plan course.
    	planCourseNameTd = "<span class='nameLabel courseListDate'><a class='a_type' href='"+$('#basePath').val()+"plan/findActualCourseById?actualCourseId="+n.actualCourseId+"' target='_blank' id='courseName_"+num+"'><span class='nameDetail'>"+n.courseName+"</span></a></span>";
    }
    var planRoomTd = "<span class='roomLabel courseListDate'>"+n.courseRoomNum+"</span>";
    var planCourseDate = "<span class='dateLabel courseListDate'>"+date+"</span>";
    var planCourseTime = "<span class='timeLabel courseListDate'>"+startTime+"-"+endTime+"</span>";
    var originalCourseDuration = "<span class='durationLabel courseListDate'>"+n.courseDuration+" h</span>";
    var planCourseTrainer = "<span class='trainerLabel courseListDate'><a class='a_type' href='javascript:void(0)'><span class='planCourseTraner'>"+n.courseTrainer+"<span></a></span>";
    var actionSpan = "<span class='actionLabel courseListDate'></span>";
    /**public plan join/quit action*/
    if(isPublic) {
    	// only plan is published and course is not started can to join/quit
    	var canJoinFlag = true;
    	var nowTime = (new Date()).getTime();
    	var startTime = Date.parse(n.courseStartTime);
    	if (n.courseStartTime != "" && startTime <= nowTime) {
    		canJoinFlag = false;
    	}
    	if(planIsPublished == 1 && canJoinFlag) {
    		/*use method of newPublicPlan.js*/
    		if(HAVE_JOIN == n.isJoinCourse) {
    			actionSpan = "<span class='actionLabel courseListDate'><a class='quit' onclick='bindJoinOrQuitOne("+planId+", "+n.actualCourseId+", this)'>"+getQuitButton()+"</a></span>";
    		} else {
    			actionSpan = "<span class='actionLabel courseListDate'><a class='join' onclick='bindJoinOrQuitOne("+planId+", "+n.actualCourseId+", this)'>"+getJoinButton()+"</a></span>";
    		}
    	}
    }
    /**invited plan apply leave/back action*/
    if(isInvited) {
    	actionSpan = getApplyLeaveActionSpan(n,realNameDiv);
    }
    var trEnd = "<span class='courseListDate' style='width: 5px;'></span><div class='clear'></div></div>";
    
    var trData = courseListDateBg +realNameDiv+realPlanCourseTranerDiv+ tdNum + planCourseNameTd + planRoomTd + planCourseDate + 
    			 planCourseTime + originalCourseDuration + planCourseTrainer + actionSpan + trEnd ;
    return trData;
} 

// set confirm bar operation
function setDeleteConfirmBar() {
	var deleteContent = getPlanDeleteMessage;
	initialConfirmBar(deleteContent, toDeletePlan);
}
function toDeletePlan() {
	$.ajax({
		type: "POST",
        url: $('#basePath').val()+"plan/deletePlan",
        data: {
			"planId" : planId
		},
        success: function(errorCode) {
			if(handleException(errorCode)){
				window.location = $('#basePath').val()+'plan/plan_searchPlan?operationFlag=delete&prefixIDValue='+prefixIDValue;
			};
//			if (errorCode) {
//				initialErrorMsgBar(errorCode.errorMessage);
//			} else {
//				window.location = $('#basePath').val()+'plan/plan_searchPlan?operationFlag=delete&prefixIDValue='+prefixIDValue;
//			}
			//getPlanInfo();
        }
	});
}

function setPublishConfirmBar() {
	var publishContent = getPlanPublishMrssage()+' "<span class="id_span">'+prefixIDValue+'</span>" ?'+
	 '<div class="option_div">'+
	 '<p class="confirmBar_option"><span class="common_checkbox common_checkbox_unchecked" onclick="commonCheckBox(this)" id="checkBox_isSendToManager"></span>'+getPublishSendEmailMessage()+'</p></div>';
    initialConfirmBar(publishContent, toPublishPlan);
}
function toPublishPlan() {
	// before publish judge if plan has trainees (invited plan must have invited trainees; public plan must have specific trainees)
	var hasTrainees = true;
	if(isPublic) {
		if(!isAllEmployeeFlag) {
			if(plan_specific_trainees == null || plan_specific_trainees == '') {
				hasTrainees = false;
			}
		}
	} else {
		if(plan_invited_trainees == null || plan_invited_trainees == '') {
			hasTrainees = false;
		}
	}
	
	var isSendToManager;
	if ($("#checkBox_isSendToManager").attr("check") === "checked") {
		isSendToManager = 1;
	} else {
		isSendToManager = 0;
	}
	if(hasTrainees) {
		// publish plan
		$.ajax({
			type: "POST",
	        url: $('#basePath').val()+"plan/publishPlan",
	        data: {
				"planId" : planId,
				"esc.isSendToManager" : isSendToManager
			},
	        success: function(errorCode) {
				if(handleException(errorCode)){
					$("#messageBar").messageBar({
						isPrepositionId: true,
						responseMessage: getPublishPlanMessage(),
						itemId: prefixIDValue,
			    	    top: 100
					});			
				};
				getPlanInfo();
			    // set the plan is published flag
				planIsPublished = 1;
				$("#planIsPublished").val(1);
				refreshOrGetCourseList();
	        }
		});
	} else {
		// show error message pop-up
		if(isPublic){
			initialErrorMsgBar(getPublishPlanNoSpecificTraineesMs());
		} else {
			initialErrorMsgBar(getPublishPlanNoInvitedTraineesMs());
		}
	}

}

function setCancelConfirmBar() {
	var cancelContent = '<div id="cancelContent">'+
							getPlanCancelMessgage()+' "<span class="id_span">'+prefixIDValue+'</span>" ?'+
							'<div class="option_div">'+
							'<p class="confirmBar_option"><span class="common_checkbox common_checkbox_unchecked" onclick="commonCheckBox(this)" id="checkBox_isSendToManager"></span>'+getPublishSendEmailMessage()+'</p></div>'+
							'<textarea id="cancelPlanReason" name="cancelPlanReason"></textarea>'+
							'<div id="inputTipDiv"><span>'+getPlanCancelReason()+'</span></div>'+
						'</div>';
    initialConfirmBar(cancelContent, toCancelPlan);
    initialCancelInput();
}
function toCancelPlan() {
	var cancelPlanReason = $('#cancelPlanReason').val();
	var isSendToManager = 0;
	if ($("#checkBox_isSendToManager").attr("check") === "checked") {
		isSendToManager = 1;
	}
	$.ajax({
		type: "POST",
        url: $('#basePath').val()+"plan/cancelPlan",
        data: {
			"planId" : planId,
			"esc.isSendToManager" : isSendToManager,
			"cancelPlanReason" : cancelPlanReason
		},
        success: function(errorCode) {
			if(handleException(errorCode)){
				window.location = $('#basePath').val()+'plan/plan_searchPlan?operationFlag=cancel&prefixIDValue='+prefixIDValue;
			};
//			if (errorCode) {
//				initialErrorMsgBar(errorCode.errorMessage);
//			} else {
//				window.location = $('#basePath').val()+'plan/plan_searchPlan?operationFlag=cancel&prefixIDValue='+prefixIDValue;
//			}
			getPlanInfo();
			planIsPublished = 0;
			$("#planIsPublished").val(0);
			refreshOrGetCourseList();
        }
	});
}
function initialCancelInput() {
	if($("#cancelPlanReason").val() != ""){
		$("#inputTipDiv").hide();
	}	
	$("#cancelPlanReason").focus(function() {
		$("#inputTipDiv").hide();
    });
	$("#cancelPlanReason").keydown(function() {
		$("#inputTipDiv").hide();
    });
	$("#cancelPlanReason").blur(function() {
        if($(this).val() == "") {
    		$("#inputTipDiv").show();
        }
    });
    $("#inputTipDiv").click(function(){
    	$(this).hide();
    	$("#cancelPlanReason").focus();
    });   
}

// set message bar information
function setMessageBar() {
	var messageBar_create = $('#messageBar_create').html();
	var messageBar_publish = $('#messageBar_publish').html();
	if ("create" == operationFlag) {
		$("#messageBar").messageBar({
			responseMessage: messageBar_create+" ",
    	    itemId: prefixIDValue,
    	    top: 100
		});
	}
	if ("publish" == operationFlag) {
		$("#messageBar").messageBar({
			isPrepositionId: true,
			responseMessage: ": "+messageBar_publish,
			itemId: prefixIDValue,
    	    top: 100
		});
	}
	clearOperation();
}

function clearOperation() {
	$.ajax({
        type: "POST",
        url: $('#basePath').val()+"plan/clearOperation",
        data: {},
        success: function() {
        }
     });
}
//give assessment part
function initialAssessmentEvent(){
	$("#traineeAssessPlan").click(trainee2planAssessment);
    $("#traineeViewPlanAssessment").click(trainee2planAssessment);
    $("#trainerAssessPlan").live('click', function() {
		trainer2planAssessment('create');
	});
    $("#trainerViewPlanAssessment").live('click', function() {
		trainer2planAssessment('edit');
	});
    $("#masterAssessTrainer").live('click', function() {
    	master2trainerAssessment('create');
	});
    $("#masterViewTrainerAssessment").live('click', function() {
    	master2trainerAssessment('edit');
	});

}

//trainee assess plan, or trainee view the assessment
function trainee2planAssessment() {
	showAssessmentpopup('trainee2plan_popup', $("#planName").html(), $("#plan_prefixIDValue").val());
	$('#trainee2plan_planId').val(planId);
	initTrainee2Plan(planId);
}

function trainer2planAssessment(methodName) {
	if(methodName == 'create'){
		$("#form_trainer2plan").attr("action",$('#basePath').val()+"assessment/create_createTrainer2planAssessment");
	}
	if(methodName == 'edit'){
		$("#form_trainer2plan").attr("action",$('#basePath').val()+"assessment/edit_editTrianer2planAssessment");
	}
	showAssessmentpopup('trainer2plan_popup', $("#planName").html(), $("#plan_prefixIDValue").val());
	$('#trainer2plan_popup #assess_planId').val(planId);
	initTrainer2Plan(planId);
}

function master2trainerAssessment(methodName) {
	if(methodName == 'create'){
		$("#form_master2trainer").attr("action",$('#basePath').val()+"assessment/create_createMaster2trainerAssessment");
	}
	if(methodName == 'edit'){
		$("#form_master2trainer").attr("action",$('#basePath').val()+"assessment/edit_editMaster2trainerAssessment");
	}
	showAssessmentpopup('master2trainer_popup', $("#planName").html(), $("#plan_prefixIDValue").val());
	$('#master2trainer_popup #assess_planId').val(planId);
	initMaster2Trainer(planId);
}

function getPlanInfo() {
	$.ajax ({
		type: "POST",
        url: $('#basePath').val() + "plan/getPlanInfo",
        data: {"planId" : planId},
        success: function(data) {
        	var splitString = "<!-- split -->";
        	var splitIndex = data.indexOf(splitString);
        	var planStatusHtml = data.substring(0, splitIndex);
        	//if the action part has no action, hide the action button
        	if(planStatusHtml.trim() == ""){ 
        		$("#view_plan_id_right").css('visibility','hidden');
        		$("#action_active_launch").css('visibility','hidden');
        	} else {
        		$("#view_plan_id_right").css('visibility','visible');
        		$("#action_active_launch").css('visibility','visible');
        	}
			$("#action_part").html(planStatusHtml);
			
			//change the width dynamic
			var maxWidth = 93;
			$("#action_part").find('a').each(function(){
				if($(this).width() > maxWidth){
					maxWidth = $(this).width();
				}
			});
			$("#action_part").find('a').each(function(){
				$(this).width(maxWidth);
			});
			var planOperateHtml = data.substring(splitIndex + splitString.length, data.length);
			$("#plan_excute_button").html(planOperateHtml);
			$('#publishButton').click(function(){
		    	setPublishConfirmBar();
		    });
		    $('#cancelButton').click(function(){
		    	setCancelConfirmBar();
				return false;
		    });
		    $('#viewAllAssessment').click(function(){
		    	window.location = $('#basePath').val()+"assessment/prepareViewAllAssessment?planId="+planId;
		    });
		    initialAssessmentEvent();
		    // button operation
		    $('#editButton').click(function(){
		    	window.location = $('#basePath').val()+"plan/toEditPlan?planId="+planId;
		    });
		    $('#deleteButton').click(function(){
		    	setDeleteConfirmBar();
				return false;
		    });
		    initialApplyLeavePlan();
        }
	});
}

/**
 * remove action lable and modify other's css
 * @return
 */
function hiddenAction() {
	$('.actionLabel').css('display', 'none');
	$('.timeLabel').css('width', '100px');
	$('.durationLabel').css('width', '70px');
	$('.trainerLabel').css('width', '115px');
}

function getApplyLeaveActionSpan(n,realNameDiv){
	var actionSpan = "<span class='actionLabel courseListDate'></span>";
	if(!isPlanSession && (planIsPublished == 1)) {
    	if(n.applyLeaveFlag == COURSE_STARTED){
    		actionSpan = "<span class='actionLabel courseListDate' courseId='"+n.actualCourseId+"'>" +
    						"<span style='display:none'>"+$(realNameDiv).html()+"</span>"+
							"<span class='courseOverFlag' style='display:inline'>"+getCourseOverFlag()+"</span>" +
							"<span class='backCourse' onclick='applyLeaveOrBackCourse(this)' style='display:none'>"+getBackIconI18n()+"</span>" +
							"<span class='leaveCourse' onclick='applyLeaveOrBackCourse(this)' style='display:none'>"+getLeaveIconI18n()+"</span>" +
						"</span>";
    	} else if (n.applyLeaveFlag == TRAINEE_LEAVED){
    		actionSpan = "<span class='actionLabel courseListDate' courseId='"+n.actualCourseId+"'>" +
    						"<span style='display:none'>"+$(realNameDiv).html()+"</span>"+
						 	"<span class='courseOverFlag' style='display:none'>"+getCourseOverFlag()+"</span>" +
						 	"<span class='backCourse' onclick='applyLeaveOrBackCourse(this)' style='display:inline'>"+getBackIconI18n()+"</span>" +
						 	"<span class='leaveCourse' onclick='applyLeaveOrBackCourse(this)' style='display:none'>"+getLeaveIconI18n()+"</span>" +
						 "</span>";
    	} else if (n.applyLeaveFlag == TRAINEE_NOT_LEAVED) {
    		actionSpan = "<span class='actionLabel courseListDate' courseId='"+n.actualCourseId+"'>" +
    						"<span style='display:none'>"+$(realNameDiv).html()+"</span>"+
							"<span class='courseOverFlag' style='display:none'>"+getCourseOverFlag()+"</span>" +
							"<span class='backCourse' onclick='applyLeaveOrBackCourse(this)' style='display:none'>"+getBackIconI18n()+"</span>" +
							"<span class='leaveCourse' onclick='applyLeaveOrBackCourse(this)' style='display:inline'>"+getLeaveIconI18n()+"</span>" +
						 "</span>";
    	}
	} 
	return actionSpan;
}

function refreshOrGetCourseList(){
	$("#courseListContent").empty();
	var coursesList = eval($("#coursesList").val());
	if (null == coursesList || "" == coursesList) {
		$('.courseListHead').css('display','none');
	}
	courseListCallBack(coursesList);
	initialToolTipForApplyLeave();
}