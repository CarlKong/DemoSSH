var COURSE_STARTED = 0;
var TRAINEE_NOT_LEAVED = 1;
var TRAINEE_LEAVED = 2;
var $planCourseActionPart;
var inViewPlanPage = false;
var planCourseId;

//initial the toolTip for apply leave course or back course
function initialToolTipForApplyLeave(){
	var leaveClassArray = $('.actionLabel&.courseListDate');
	leaveClassArray.each(function(){
		$(this).children('span').each(function(){
			if($(this).hasClass('leaveCourse')){
				$(this).poshytip({
					allowTipHover : true ,
					className: 'tip-green',
					content: getLeaveCourseToolTipI18n()
				});
			} else if($(this).hasClass('backCourse')){
				$(this).poshytip({
					allowTipHover : true ,
					className: 'tip-green',
					content: getBackCourseToolTipI18n()
				});
			}
		});
	});
}

//used in view planCourse detail page
function initialApplyLeaveCourse() {
	// trainee apply leave for a course
    $('#applyLeaveButton').live("click",function(){
    	setLeaveCourseConfirmBar();
    }); 
    //trainee cancel his leave
    $("#backToCourse").live("click",function(){
    	setBackCourseConfirmBar();
    });
}


function applyLeaveOrBackCourse(aLabel){
	inViewPlanPage = true;
	$planCourseActionPart = $(aLabel);
	planCourseId = $planCourseActionPart.parent('span').attr("courseId");
	if($planCourseActionPart.hasClass("leaveCourse")){
		setLeaveCourseConfirmBar($planCourseActionPart.parent('span').children('span:first').html());
	} else if ($planCourseActionPart.hasClass("backCourse")){
		setBackCourseConfirmBar();
	}
}

//trainee apply leave plan and cancel his leave 
function initialApplyLeavePlan(){
	$("#applyLeavePlan").live("click",function(){
		setLeavePlanConfirmBar();
	}); 
	$("#backToPlan").live("click",function(){
		setBackPlanConfirmBar();
	});
}

function setLeavePlanConfirmBar() {
	var planName = $("#planName").html();
	var leaveContent = '<div id="leaveContent">'+
							getAskLeavePlanI18n()+' "<span class="leave_name_span">'+planName+'</span>" ?'+
							'<div style="position:relative">'+
								'<textarea id="leaveReason" class="leaveReason" name="leavePlanReason"></textarea>'+
								'<div class="inputTipDiv_leave"><span>'+getInputLeaveReasonI18n()+'</span></div>'+
							'</div>'+
						'</div>';
    initialConfirmBar(leaveContent, leavePlan);
    initialLeaveReasonInput();
}

function setBackPlanConfirmBar(){
	var leaveContent = '<div>'+getBackPlanI18n()+'</div>';
	initialConfirmBar(leaveContent, backPlan);
}

//trainee apply leave for plan
function leavePlan() {
	var leavePlanReason = $('#leaveReason').val();
	var leaveClassArray = $('.actionLabel&.courseListDate');
	for(i=0;i<leaveClassArray.length;i++){
		var courseOverSpan = $(leaveClassArray[i]).children("span:first");
		if(courseOverSpan.attr("style") == "display:inline"){
			continue;
		} else {
			$(leaveClassArray[i]).children('span').each(function(){
				if($(this).hasClass('leaveCourse')){
					$(this).attr("style","display:none");
				} else if($(this).hasClass('backCourse')){
					$(this).attr("style","display:inline");
				}
			});
		}
	}
	$.ajax({
        type: "POST",
        url: $('#basePath').val()+"plan/applyLeavePlan", 
        data: {"leavePlanReason":leavePlanReason,"planId":planId},
        success: function(data) {
        	handleException(data);
        	var splitString = "<!-- split -->";
        	var splitIndex = data.indexOf(splitString);
        	var planStatusHtml = data.substring(0, splitIndex);
			$("#action_part").html(planStatusHtml);
        }
     });
}

//trainee cancel his application for plan
function backPlan(){
	var backClassArray = $('.actionLabel&.courseListDate');
	for(i=0;i<backClassArray.length;i++){
		var courseOverSpan = $(backClassArray[i]).children("span:first");
		if(courseOverSpan.attr("style") == "display:inline"){
			continue;
		} else {
			$(backClassArray[i]).children('span').each(function(){
				if($(this).hasClass('leaveCourse')){
					$(this).attr("style","display:inline");
				} else if($(this).hasClass('backCourse')){
					$(this).attr("style","display:none");
				}
			});
		}
	}
	$.ajax({
        type: "POST",
        url: $('#basePath').val()+"plan/applyBackPlan", 
        data: {"planId":planId},
        success: function(data) {
        	handleException(data);
        	var splitString = "<!-- split -->";
        	var splitIndex = data.indexOf(splitString);
        	var planStatusHtml = data.substring(0, splitIndex);
			$("#action_part").html(planStatusHtml);
        }
     });
}

function initialLeaveReasonInput() {
	if($("#leaveReason").val() != ""){
		$(".inputTipDiv_leave").hide();
	}	
	$("#leaveReason").focus(function() {
		$(".inputTipDiv_leave").hide();
    });
	$("#leaveReason").keydown(function() {
		$(".inputTipDiv_leave").hide();
    });
	$("#leaveReason").blur(function() {
        if($(this).val() == "") {
    		$(".inputTipDiv_leave").show();
        }
    });
    $(".inputTipDiv_leave").click(function(){
    	$(this).hide();
    	$("#leaveReason").focus();
    });   
}

//The following is the apply leave for course js
function setLeaveCourseConfirmBar(planCourseName) {
	if(!inViewPlanPage){
		planCourseName = $("#planCourseName").html();
	}
	var cancelContent = '<div id="leaveContent">'+
							getAskLeaveCourseI18n()+' "<span class="leave_name_span">'+planCourseName+'</span>" ?'+
							'<div style="position:relative">'+
								'<textarea id="leaveReason" class="leaveReason" name="leaveCourseReason"></textarea>'+
								'<div class="inputTipDiv_leave"><span>'+getInputLeaveReasonI18n()+'</span></div>'+
							'</div>'+
						'</div>';
    initialConfirmBar(cancelContent, leaveCourse);
    initialLeaveReasonInput();
}

function setBackCourseConfirmBar(){
	var leaveContent = '<div>'+getAskBackCourseI18n()+'</div>';
	initialConfirmBar(leaveContent, backCourse);
}

//trainee apply leave course
function leaveCourse() {
	var leaveCourseReason = $('#leaveReason').val();
	if(inViewPlanPage){
		$planCourseActionPart.css("display","none");
		$planCourseActionPart.parent('span').children('span.backCourse').css("display","inline");
	}
	$.ajax({    
        type: "POST",
        url: $('#basePath').val()+"plan/applyLeaveCourse", 
        data: {"leaveCourseReason":leaveCourseReason,"actualCourseId":planCourseId},
        success: function(data) {
        	handleException(data);
        	if(inViewPlanPage){
            	getPlanInfo();
        	} else {
        		$("#action_part").html(data);
        	}
        	inViewPlanPage = false;
        }
     });
}

//trainee apply to cancel his application for course
function backCourse(){
	if(inViewPlanPage){
		$planCourseActionPart.css("display","none");
		$planCourseActionPart.parent('span').children('span.leaveCourse').css("display","inline");
	}
	$.ajax({
        type: "POST",
        url: $('#basePath').val()+"plan/applyBackCourse", 
        data: {"actualCourseId":planCourseId},
        success: function(data) {
        	handleException(data);
        	if(inViewPlanPage){
            	getPlanInfo();
        	} else {
        		$("#action_part").html(data);
        	}
        	inViewPlanPage = false;
        }
     });
}