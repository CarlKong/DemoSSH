var JOIN_COURSE = 1;
var QUIT_COURSE = 0;
var confirm_planId;
var confirm_courseId;
var confirm_obj;
var confirm_isJoin;

$(document).ready(function(){
	loadNewPublishPlanList({},true);
	initialButtonEvent();
});

function loadNewPublishPlanList(criteria, isInitHeadData){
	var basePath=$("#basePath").val();
	var thePath=basePath+"dashboard/newPublicPlan";
	//When the data not come back, should use a image to change it.
	$(".dataList-div-loader").attr("style","display: block;");
	$.post(
			thePath,
			criteria,
			function(data,status){
				if (status == "success") {
					$(".dataList-div-loader").attr("style","display: none;");
					if (!data) {
						disablePreviousButton();
						disableNextButton();
						initalNewPlanHeadData(data.totalRecord, data.totalPage);
						return false;
					}
					if (!data.dataList.length) {
						disablePreviousButton();
						disableNextButton();
						initalNewPlanHeadData(data.totalRecord, data.totalPage);
						return false;
					}
					if (isInitHeadData){
						initalNewPlanHeadData(data.totalRecord, data.totalPage);
					}
					drawPlanListCallBack(data);
					drawPoshytipForPlanTitle();
				}
			},
			"json"
		);
}

function initalNewPlanHeadData(totalRecord, totalPage){
	$(".record_total_num_for_long_middle","#new_publish_plan_record_total").html(totalRecord);
	$("#new_publish_plan_total_pages").val(totalPage);
}

function drawPlanListCallBack(data){
	var $planContainer = $("#new_publish_plan_table");
	var planDetailRecords = $(".table_record_detail", $planContainer);
	var count = planDetailRecords.length - data.dataList.length;
	$planContainer.empty();
	$("#new_publish_plan_page_now").val(data.pageNow);
	initialButtonStatus(data.pageNow,data.totalPage);
	var planItems;
	for (var i = 0; i < data.dataList.length; i++) {
		var $planItemObj = $("#new_publish_plan_item_temp").clone(true).show();
		$planItemObj.removeAttr("id");
		var number = (data.pageNow -1) * data.pageSize + (i+1);
		var $leftDeatial = $planItemObj.find(".table_record_detail_left_one_layer");
		$leftDeatial.append(number + ". <span>" +data.dataList[i].creator+' '+getPublishedAPlan()+"&nbsp;"+ 
			"<a class='table_record_detail_left_one_layer_blue'>"+data.dataList[i].title+"</a></span>");
		$planItemObj.find(".table_record_detail_left_two_layer").html(getStartOn() +" "+data.dataList[i].startTime);
		$planItemObj.find(".table_record_detail_right").attr("planId", data.dataList[i].planId)
		.find("a").click(function(e){
			loadOrCloseCourseListContainer($(this).parent().attr("planId"), $(this));
		});
		var planid = data.dataList[i].planId;
		$(".table_record_detail_left_one_layer_blue", $leftDeatial)
		.data('planId', data.dataList[i].planId).bind('click', function(){
			bindClickForNewPublishPlan($(this).data('planId'));
		});
		$planContainer.append($planItemObj);
	}
	if (count) {
		var temp = "";
		for(var i = 0; i < count; i++) {
			temp += '<div class="table_record_detail"></div>';
		}
		$planContainer.append(temp);
	} 
}

function bindClickForNewPublishPlan(planId){
	window.open($("#basePath").val() + "plan/viewPlanDetail?planId=" + planId);
}

function initialButtonEvent(){
	$("#new_publish_plan_refresh").click(function(){
		loadNewPublishPlanList({},true);
	});
	//Previous
	$("#new_publish_plan_previous").click(function(){
		var pageNow = $("#new_publish_plan_page_now").val();
		if (isNaN(pageNow) || pageNow <= 1){
			return false;
		}
		pageNow = parseInt(pageNow) - 1;
		loadNewPublishPlanList({'pageNow' : pageNow});
	});
	//Next
	$("#new_publish_plan_next").click(function(){
		var pageNow = $("#new_publish_plan_page_now").val();
		if (!pageNow || pageNow >= $("#new_publish_plan_total_pages").val()){
			return false;
		}
		pageNow = parseInt(pageNow) + 1;
		loadNewPublishPlanList({'pageNow' : pageNow});
	});
}
function initialButtonStatus(pageNow, pages){
	if (pages == 1) {
		disablePreviousButton();
		disableNextButton();
		return false;
	}
	if (pageNow >= pages){
		ablePreviousButton();
		disableNextButton();
		return false;
	}
	if (pageNow <=1 ){
		disablePreviousButton();
		ableNextButton();
		return false;
	}
	ablePreviousButton();
	ableNextButton();
}

function loadOrCloseCourseListContainer(planId, $obj){
	var $container = $obj.parent().parent(); 
	if (!$container.attr("hasLoad")) {
		var basePath=$("#basePath").val();
		var thePath=basePath+"dashboard/getCourseListForPublishPlan";
		//When the data not come back, should use a image to change it.
		$(".dataList-div-loader").attr("style","display: block;");
		$.post(
				thePath,
				{"planId" : planId},
				function(data,status){
					if (status == "success") {
						drawCourseListCallBack(data, $obj);
						$(".dataList-div-loader").attr("style","display: none;");
						if ($(".table_record_main",$container).height() > 120) {
							$(".table_record_main",$container).scrollbar({
								viewPortWidth  : 400,
								viewPortHeight : 120
							 });
						}
						drawPoshytipForCourseTitle();
					}
				},
				"json"
		);
	}
	if ($obj.hasClass("packUp")) {
		$obj.attr("class","expand");
		$(".table_record_additional", $container).slideToggle(500);
		$(".table_record_additional","#new_publish_plan_table")
			.not($(".table_record_additional", $container)).slideUp();
		$(".expand","#new_publish_plan_table").not($obj).attr("class","packUp");
		return false;
	}else {
		$(".table_record_additional", $container).slideUp();
		$obj.attr("class","packUp");
		return false;
	}
}

function drawCourseListCallBack(data,  $obj){
	var $container = $obj.parent().parent();
	$container.attr("hasLoad",true);
	var planId = $(".table_record_detail_right", $container).attr("planId");
	$(".table_record_additional", $container).remove();
	var $courseDiv = $("#new_publish_course_item_temp").clone().show();
	$courseDiv.removeAttr("id");
	$('.table_record_course_last', $courseDiv).find('a').click(function(){
		bindJoinOrQuitAll(planId, this);
	});
	var $courseItemContainer = $courseDiv.find(".table_record_main");
	for (var i = 0; i < data.dataList.length; i++) {
		var $courseItem = $(".table_record_course", "#new_publish_course_item_temp").clone().show();
		$courseItem.find(".table_record_left_one_layer_blue").html(data.dataList[i].courseName);
		$courseItem.find(".table_record_left_one_layer_blue").data('planCourseId',data.dataList[i].courseId)
		.bind('click',function(){
			bindClickForPlanCourseInNewPlan($(this).data('planCourseId'));
		});
		$courseItem.find(".table_record_course_middle").html(data.dataList[i].time);
		if (data.dataList[i].isJoinCourse == JOIN_COURSE) {
			$courseItem.find(".table_record_course_right").empty()
			.append('<a class="quit" onclick="bindJoinOrQuitOne('+planId+','+
					''+data.dataList[i].courseId+', this)">'+getQuitButton()+'</a>');
		}else {
			$courseItem.find(".table_record_course_right").empty()
			.append('<a class="join" onclick="bindJoinOrQuitOne('+planId+','+
					''+data.dataList[i].courseId+', this)">'+getJoinButton()+'</a>');
		}
		
		$courseItemContainer.append($courseItem);
	}
	$courseItemContainer.appendTo($courseDiv.find(".jScrollbar_mask"));
	$container.append($courseDiv);
}

function bindClickForPlanCourseInNewPlan(planCourseId){
	window.open($("#basePath").val() + "plan/findActualCourseById?actualCourseId=" + planCourseId);
}

function disablePreviousButton(){
	$("#new_publish_plan_previous").removeClass("previous");
	$("#new_publish_plan_previous").addClass("previous_disable");
	$("#new_publish_plan_previous").css("cursor","default");
}
function ablePreviousButton(){
	$("#new_publish_plan_previous").removeClass("previous_disable");
	$("#new_publish_plan_previous").addClass("previous");
	$("#new_publish_plan_previous").css("cursor","pointer");
}

function disableNextButton(){
	$("#new_publish_plan_next").removeClass("next");
	$("#new_publish_plan_next").addClass("next_disable");
	$("#new_publish_plan_next").css("cursor","default");
}

function ableNextButton(){
	$("#new_publish_plan_next").removeClass("next_disable");
	$("#new_publish_plan_next").addClass("next");
	$("#new_publish_plan_next").css("cursor","pointer");
}

/**
 * Draw the tip for prompting beyond the mark in div.
 * Only be used after draw the data list.
 * @return
 */
function drawPoshytipForPlanTitle(){
	$(".table_record_detail_left_one_layer",'#new_publish_plan_table').each(function(){
		if(($(this)[0].scrollWidth)-20>$(this).width()){
			$(this).poshytip({
				allowTipHover : true ,
				className: 'tip-green',
				content: "<div style='font: 12px arial regular;word-break:break-all;'>"+$(this).find('span').html()+"</div>"
			});
		}
	});
}
function drawPoshytipForCourseTitle(){
	$(".table_record_left_one_layer_blue",'#new_publish_plan_table').each(function(){
		if($(this).width() > $(this).parent().width()){
			$(this).poshytip({
				allowTipHover : true ,
				className: 'tip-green',
				content: "<div style='font: 12px arial regular;word-break:break-all;'>"+$(this).html()+"</div>"
			});
		}
	});
}
