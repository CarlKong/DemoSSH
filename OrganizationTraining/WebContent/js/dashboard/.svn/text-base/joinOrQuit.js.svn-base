var JOIN_COURSE = 1;
var QUIT_COURSE = 0;
/**
 * get value when click, and show confirm dialog
 * @param planId
 * @param courseId
 * @param obj
 * @return
 */
function bindJoinOrQuitOne(planId, courseId, obj){
	confirm_planId = planId;
	confirm_courseId = courseId;
	confirm_obj = obj;
	if ($(obj).hasClass('join')) {
		confirm_isJoin = JOIN_COURSE;
		joinOrQuitOne();
	}else {
		confirm_isJoin =  QUIT_COURSE;
		var mainVar = getQuitOneMessage();
		initialConfirmBar(mainVar, joinOrQuitOne);
	}
}

function joinOrQuitOne() {
	var basePath=$("#basePath").val();
	var thePath=basePath+"dashboard/joinOrQuitPlan";
	$.post(
			thePath,
			{"planId" : confirm_planId,
			 'courseId':confirm_courseId,
			 'isJoin':confirm_isJoin
			},
			function(data,status){
				if (status == "success") {
					if(handleException(data, refreshCourseList)){   //@see common.js
						if ($(confirm_obj).hasClass('join')) {
							$(confirm_obj).text(getQuitButton());
							$(confirm_obj).attr('class','quit');
						}else {
							$(confirm_obj).text(getJoinButton());
							$(confirm_obj).attr('class','join');
						}
						if($('#new_publish_plan_refresh').length > 0) {
							//Refresh My Plan & My Course of trainee
							initPlanAndCourseListByRole(trainee_contant);/*method of planAndCourseList.js*/
						}
				    }; 
				}
			},
			"json"
	);
}

function refreshCourseList() {
	if($('#new_publish_plan_refresh').length > 0) {
		// dashboard page need to refresh new public plan part
		loadNewPublishPlanList({},true);
	} else {
		// view plan page need to refresh this page
		location.reload();
	}
}

/**
 * get value when click, and show confirm dialog
 * @param planId
 * @param obj
 * @return
 */
function bindJoinOrQuitAll(planId,obj){
	confirm_planId = planId;
	confirm_obj = obj;
	if ($(obj).hasClass('join_all')) {
		confirm_isJoin = JOIN_COURSE;
		joinOrQuitAll();
	}else {
		confirm_isJoin =  QUIT_COURSE;
		var mainVar = getQuitAllMessage();
		initialConfirmBar(mainVar, joinOrQuitAll);
	}
}

function joinOrQuitAll() {
	var basePath=$("#basePath").val();
	var thePath=basePath+"dashboard/joinOrQuitPlan";
	$.post(
			thePath,
			{"planId" : confirm_planId,
			 'isJoin':confirm_isJoin
			},
			function(data,status){
				if (status == 'success') {
					if(handleException(data, refreshCourseList)){
						if ($(confirm_obj).hasClass('join_all')) {
							$('.table_record_course_right', $(confirm_obj).parent().prev())
							.find('a').attr('class','quit').text(getQuitButton());
							
						}else {
							$('.table_record_course_right', $(confirm_obj).parent().prev())
							.find('a').attr('class','join').text(getJoinButton());
						}
						refreshTraineePart();/*method of toDoList.js*/
					}
				}
			},
			"json"
	);
}