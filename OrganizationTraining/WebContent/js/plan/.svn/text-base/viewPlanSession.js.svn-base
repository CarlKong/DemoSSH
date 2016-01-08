

$(document).ready(function(){
	
	//there is no necessary for planSession to show the action button
	$('#view_plan_id_right').css("display","none");
	
//	// action
//	$('#view_plan_id_right').mouseenter(function(){
//		$(this).find('#action_active_launch').css('visibility', 'hidden');
//		$(this).find('#action_active_shrink').css('visibility', 'visible');
//		$(this).find('#action_part').css('visibility', 'visible');
//    });
//    $('#view_plan_id_right').mouseleave(function(){
//    	$(this).find('#action_active_shrink').css('visibility','hidden');
//        $(this).find('#action_active_launch').css('visibility','visible');
//    	$(this).find('#action_part').css('visibility','hidden');
//    });
//    // button 
//    $('#applyLeaveButton').click(function(){
//    	alert("Apply leave");
//    });
//    $('#assessmentButton').click(function(){
//    	alert("Assessment plan session");
//    });
	
	// Deal with date and time.
	dealWithDateAndTime();  /**  @see viewPlanSession.js  */
	// Deal with plan session attachment.
	actualCourseViewAttachment();
	// Add a field named creator in model plan session.
});

/**
 * Deal with session date and time.
 * 
 * @author Michael.Ding
 * @return  Null
 */
function dealWithDateAndTime(){
	var sessionStartTime = $('#sessionStartTime').val();
	var sessionEndTime = $('#sessionEndTime').val();
	var date = '';
	var time = '';
	var startTime;
	var endTime;
	if (null != sessionStartTime && "" != sessionStartTime) {
		date = sessionStartTime.substr(0,4)+"-"+sessionStartTime.substr(5,2)+"-"+sessionStartTime.substr(8,2); 
		startTime = sessionStartTime.substr(11,5);
	}
	if (null != sessionEndTime && "" != sessionEndTime) {
		endTime = sessionEndTime.substr(11,5);
	}
	if (startTime == undefined) {
		startTime = "";
	}
	if (endTime == undefined) {
		endTime = "";
	}
	time = startTime+" - "+endTime;
	$('#pc_date').append(date);
	$('#pc_time').append(time);
}
