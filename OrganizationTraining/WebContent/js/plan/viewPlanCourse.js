var planCourseId = $("#planCourseId").val();
var planId = $("#planCourse_planId").val();
 
$(document).ready(function(){
	// action button
	$('#view_plan_id_right').mouseenter(function(){
		$(this).find('#action_active_launch').css('visibility', 'hidden');
		$(this).find('#action_active_shrink').css('visibility', 'visible');
		$(this).find('#action_part').css('visibility', 'visible');
    });
    $('#view_plan_id_right').mouseleave(function(){
    	$(this).find('#action_active_shrink').css('visibility','hidden');
        $(this).find('#action_active_launch').css('visibility','visible');
    	$(this).find('#action_part').css('visibility','hidden');
    });
	//hide the action button firstly
	$('#view_plan_id_right').css('visibility', 'hidden');
	$('#action_active_launch').css('visibility', 'hidden');
	
    setDateAndTime();
    actualCourseViewAttachment();
    getPlanCourseInfo();
    bindTrainer2TraineeSubmit(function() {
    	getPlanCourseInfo();
    });
    bindTrainee2CourseSubmit(function(){
    	getPlanCourseInfo();
	});
});

/**
 * set date and time value as format style
 */
function setDateAndTime(){
	var startTimeDetail = $('#startTimeDetail').val();
	var endTimeDetail = $('#endTimeDetail').val();
	var date = '';
	var time = '';
	var startTime = '';
	var endTime = '';
	if (null != startTimeDetail && "" != startTimeDetail) {
		date = startTimeDetail.substr(0,4)+"-"+startTimeDetail.substr(5,2)+"-"+startTimeDetail.substr(8,2); 
		startTime = startTimeDetail.substr(11,5);
	}
	if (null != endTimeDetail && "" != endTimeDetail) {
		endTime = endTimeDetail.substr(11,5);
	}
	time = startTime+" - "+endTime;
	$('#pc_date').append(date);
	$('#pc_time').append(time);
}

function initialGiveAssessment(){
	$(".assessment_popup").each(function(){
		var width = $(this).width();
    	var height = $(this).height();
		$(this).css("left",($(window).width()-width)/2);
		$(this).css("top",($(window).height()-height)/2+document.documentElement.scrollTop);
	});
	$("#traineeAssessCourse").live('click', function() {
		trainee2courseAssessment('create');
	});
	$("#traineeViewCourseAssessment").live('click', function() {
		trainee2courseAssessment('edit');
	});
	$("#trainerAssessTrainee").click(trainer2traineeAssessment);
	$("#trainerViewTraineeAssessment").click(trainer2traineeAssessment);
}

function trainee2courseAssessment(methodName){
	if(methodName == 'create'){
		$("#form_trainee2course").attr("action",$('#basePath').val()+"assessment/create_createTrainee2courseAssessment");
	}
	if(methodName == 'edit'){
		$("#form_trainee2course").attr("action",$('#basePath').val()+"assessment/edit_editTrainee2courseAssessment");
	}
	showAssessmentpopup('trainee2course_popup',$("#planCourseName").html(),$("#planCoursePrefixId").html());
	$('#trainee2course_popup #assess_planCourseId').val(planCourseId);
	initTrainee2Course(planCourseId);
}

function trainer2traineeAssessment(){
	trainer2traineePopup(planCourseId, $("#planCoursePrefixId").html(), $("#planCourseName").html(), planId);
}

function getPlanCourseInfo() {
	$.ajax ({
		type: "POST",
        url: $('#basePath').val() + "plan/getActualCourseInfo",
        data: {"actualCourseId" : planCourseId},
        success: function(data) {
        	//hide the action button if there is no action
        	if(data.trim() == ""){
        		$('#view_plan_id_right').css('visibility', 'hidden');
        		$('#action_active_launch').css('visibility', 'hidden');
        	} else {
        		$('#view_plan_id_right').css('visibility', 'visible');
        		$('#action_active_launch').css('visibility', 'visible');
        	}
        	$("#action_part").html(data);
			var maxWidth = 93;
			$("#action_part").find('a').each(function(){
				if($(this).width() > maxWidth){
					maxWidth = $(this).width();
				}
			});
			$("#action_part").find('a').each(function(){
				$(this).width(maxWidth);
			});
            initialApplyLeaveCourse();
            initialGiveAssessment();
        }
	});
}
