var admin_contant = "Admin";
var master_contant = "Training Master";
var trainer_contant = "Trainer";
var trainee_contant = "Trainee";
var hasMaster = false;

$(document).ready(function(){
	setRoleFlag();
	bindClickForMore();
	bindClickForActualCourse();
	bindClickForPlan();
	if(hasMaster) {
		initPlanAndCourseListByRole(master_contant);
	}
	initPlanAndCourseListByRole(trainer_contant);
	initPlanAndCourseListByRole(trainee_contant);
});

/**
 * get roleFlag from session and judge if show master part
 * @return
 */
function setRoleFlag() {
	var roleNames=$('#roleNames').val();
	if(roleNames.indexOf(master_contant) < 0 && roleNames.indexOf(admin_contant) < 0) {
		hasMaster = false;
		$('.master').css('display', 'none');
	} else {
		hasMaster = true;
	}
}

/**
 * Initialize the plan and course list according to role.
 * 
 * @param role
 * @return  Null
 */
function initPlanAndCourseListByRole(role) {
	var basePath = $("#basePath").val();
	$.ajax({
        type: "POST",
        url: basePath + "/dashboard/getPlanAndCourseList",
        data: {"role":role},
        success: function(data) {
        	planAndCourseCallBack(data, role);
        }
     });
}
 
/**
 * The call back to draw page about plan and course list.
 * 
 * @param data
 * @param role
 * @return  Null
 */
function planAndCourseCallBack(data, role) {
	if (role != null && role != undefined) {
		if (data != null && data != undefined) {
			if (role == master_contant) {
				// Draw plan list for master.
				var planForMasterDivContent = getPlanJsonForDashBoard(data.planList, role);
				$("#table_plan_master_content").removeClass("table_content_default");
				$("#table_plan_master_content").html(planForMasterDivContent);
			} else if (role == trainer_contant) {
				// Draw plan list for trainer.
				var planForTrainerDivContent = getPlanJsonForDashBoard(data.planList, role);
				$("#table_plan_trainer_content").removeClass("table_content_default");
				$("#table_plan_trainer_content").html(planForTrainerDivContent);
				
				// Draw course list for trainer.
				var courseForTrainerDivContent = getPlanCourseJSONForDashBoard(data.actualCourseList, false);
				$("#table_course_trainer_content").removeClass("table_content_default");
				$("#table_course_trainer_content").html(courseForTrainerDivContent);
			} else if (role == trainee_contant) {
				// Draw plan list for trainee.
				var planForTraineeDivContent = getPlanJsonForDashBoard(data.planList, role);
				$("#table_plan_trainee_content").removeClass("table_content_default");
				$("#table_plan_trainee_content").html(planForTraineeDivContent);
				
				// Draw course list for trainee.
				var courseForTraineeDivContent = getPlanCourseJSONForDashBoard(data.actualCourseList, true);
				$("#table_course_trainee_content").removeClass("table_content_default");
				$("#table_course_trainee_content").html(courseForTraineeDivContent);
			}
		}
	initTooTip();
	}
}

/**
 * Return the plan list content String.
 * 
 * @param planList
 * @param role
 * @return  Null
 */
function getPlanJsonForDashBoard(planList, role) {
	var planInfosDiv = "";
	// If the plan list's length is smaller than 4, add empty row.
	var i;
	if (planList == null) {
		i = 0;
	} else {
		i = planList.length;
		$.each(planList, function(index, plan){
			planInfosDiv += drawPlanRow(plan, role, index);
		});
	}
	for (i; i < 4; i++) {
		planInfosDiv += drawPlanRow(null, role, i);
	}
	return planInfosDiv;
}

function drawPlanRow(plan, role, i) {
	var content_class = judgeBackgroundColor(i);
	var status = "";
	var planName = "";
	var planNameSpan = "";
	var planTypeName = "";
	var planPublishDate = "";
	var trainers = "";
	var trainees = "";
	if (null != plan) {
		status = plan.status;
		planName = plan.planName;
		planNameSpan = "<span class = 'plan' id = '" + plan.planId + "' >" + plan.planName + "</span>";
		planTypeName = plan.planType.planTypeName;
		trainers = plan.trainers;
		trainees = plan.trainees;
		if ( -1 == trainees) {
			trainees = 0; 
		}
		planPublishDate = plan.planPublishDate;
		if (null == planPublishDate || "" == planPublishDate || "2999-12-31" == planPublishDate) {
			planPublishDate = "-";
		}
	}
	var planItem = "";
	planItem = '<div class = "content_row '+content_class+'" >'
				+'<div class = '+status+' >&nbsp;</div>'
				+'<div class = "content_realName">' + planName + '</div>'
				+'<div class = "content_name">'+planNameSpan+'</div>'
				+'<div class = "content_type">'+planTypeName+'</div>'
				+'<div class = "content_publish_time">'+planPublishDate+'</div>'
				;
	if (trainee_contant == role) {
		planItem = planItem 
				   +'<div class = "content_realTrainers">'+trainers+'</div>'
   		   		   +'<div class = "content_trainer">'+trainers+'</div>'
   		   		   +'</div>';
	} else {
		planItem = planItem 
		   		   +'<div class = "content_trainees">'+trainees+'</div>'
		   		   +'</div>';
	}
	return planItem;
}

/**
 * Return plan course list content String.
 * 
 * @param planCourseList
 * @param isForTrainee
 * @return  Null
 */
function getPlanCourseJSONForDashBoard(actualCourseList, isForTrainee) {
	var actualCourseForTraineeDivContent = "";
	var actualCourseForTraineeItem;
	var actualCourseNameSpan = "";
	var actualCoursePrefixId;
	var actualCourseId;
	var actualCourseLength = 0;
	if(actualCourseList != null) {
		actualCourseLength = actualCourseList.length;
		$.each(actualCourseList, function(index, actualCourse){
			var content_class = judgeBackgroundColor(index);
			actualCoursePrefixId = actualCourse.actualCoursePrefixId;
			actualCourseId = actualCourse.actualCourseId;
			if (actualCoursePrefixId != null && actualCoursePrefixId != undefined) {
				if (actualCoursePrefixId.indexOf("PC") == 0) {
					actualCourseNameSpan = "<span class = 'planCourse actualCourse' id = '" + actualCourseId + "' >" + actualCourse.actualCourseName + "</span>";
				} else if (actualCoursePrefixId.indexOf("PS") == 0) {
					actualCourseNameSpan = "<span class = 'planSession actualCourse' id = '" + actualCourseId + "' >" + actualCourse.actualCourseName + "</span>";
				}
			}
			
			actualCourseForTraineeItem = '<div class = "content_row '+content_class+'" >' 
				+ '<div class = "' + actualCourse.actualCourseStatus + '" >&nbsp;</div>'
				+ '<div class = "content_realName">' + actualCourse.actualCourseName + '</div>'
				+ '<div class = "content_name">' + actualCourseNameSpan + '</div>'
				+ '<div class = "content_date">' + actualCourse.actualCourseDate + '</div>'
				+ '<div class = "content_time">' + actualCourse.actualCourseTime + '</div>'
				+ '<div class = "content_room">' + actualCourse.actualCourseRoomNumber + '</div>';
			if (isForTrainee) {
				actualCourseForTraineeItem += '<div class = "content_realTrainers">'+actualCourse.actualCourseTrainer+'</div>'
											+ '<div class = "content_trainer">' + actualCourse.actualCourseTrainer + '</div>'
			} else {
				actualCourseForTraineeItem += '<div class = "content_trainees">' + actualCourse.actualCourseTraineeNumber + '</div>'
			}
			actualCourseForTraineeItem += '</div>';
			actualCourseForTraineeDivContent += actualCourseForTraineeItem;
		});
	} 
	
	// If the plan course list's length is smaller than 4, add empty row.
	for (var index = actualCourseLength; index < 4; index++) {
		var content_class = judgeBackgroundColor(index);
		actualCourseForTraineeItem = '<div class = "content_row '+content_class+'" >' 
			+ '<div class = "" >&nbsp;</div>'
			+ '<div class = "content_name"></div>'
			+ '<div class = "content_date"></div>'
			+ '<div class = "content_time"></div>'
			+ '<div class = "content_room"></div>'
		if (isForTrainee) {
			actualCourseForTraineeItem += '<div class = "content_trainer"></div>';
		} else {
			actualCourseForTraineeItem += '<div class = "content_trainees"></div>';
		}
		actualCourseForTraineeItem += '</div>';
		
		actualCourseForTraineeDivContent += actualCourseForTraineeItem;
	}
	return actualCourseForTraineeDivContent;
}

/**
 * set status and name tootip
 */
function initTooTip() {
	// status
	var red_message = $("#red_message").val();
	$(".red").poshytip({
		allowTipHover : true ,
		className: 'tip-green',
		content: red_message
	});
	var green_message = $("#green_message").val();
	$(".green").poshytip({
		allowTipHover : true ,
		className: 'tip-green',
		content: green_message
	});
	var yellow_message = $("#yellow_message").val();
	$(".yellow").poshytip({
		allowTipHover : true ,
		className: 'tip-green',
		content: yellow_message
	});
	var gray_message = $("#gray_message").val();
	$(".gray").poshytip({
		allowTipHover : true ,
		className: 'tip-green',
		content: gray_message
	});
	// name
	$(".content_realName").each(function(i, node){
		if ($(node).width()>200) {
			$(node).parent().find(".content_name").poshytip({
				allowTipHover : true ,
				className: 'tip-green',
				content: $(node).html()
			});
		}
	});
	// trainers
	$(".content_realTrainers").each(function(i, node){
		if ($(node).width()>80) {
			$(node).parent().find(".content_trainer").poshytip({
				allowTipHover : true ,
				className: 'tip-green',
				content: $(node).html()
			});
		}
	});
}

/**
 * Bind the click for actual course.
 * 
 * @return  Null
 */
function bindClickForActualCourse() {
	$(".actualCourse").live("click", function(){
		var actualCourseId = $(this).attr("id");
		window.open($("#basePath").val() + "plan/findActualCourseById?actualCourseId=" + actualCourseId);
	});
}

/**
 * click to view plan detail
 * @return
 */
function bindClickForPlan() {
	$(".plan").live("click", function(){
		var planId = $(this).attr("id");
		window.open($("#basePath").val() + "plan/viewPlanDetail?planId=" + planId);
	});
}

/**
 * click to view myPlan or myCourse
 * @return
 */
function bindClickForMore() {
	$('#master_plan_more').live('click', function() {
		window.open($("#basePath").val() + "plan/plan_myPlan?roleFlag=" + master_contant);
	});
	$('#trainer_plan_more').live('click', function() {
		window.open($("#basePath").val() + "plan/plan_myPlan?roleFlag=" + trainer_contant);
	});
	$('#trainer_course_more').live('click', function() {
		window.open($("#basePath").val() + "plan/plan_myCourse?roleFlag=" + trainer_contant);
	});
	$('#trainee_plan_more').live('click', function() {
		window.open($("#basePath").val() + "plan/plan_myPlan?roleFlag=" + trainee_contant);
	});
	$('#trainee_course_more').live('click', function() {
		window.open($("#basePath").val() + "plan/plan_myCourse?roleFlag=" + trainee_contant);
	});
}

/**
 * to judge background color
 * @param i
 * @return
 */
function judgeBackgroundColor(i) {
	var content_class = "";
	if (i%2) {
		content_class = "content_row_gray";
	} else {
		content_class = "content_row_white";
	}
	return content_class;
}