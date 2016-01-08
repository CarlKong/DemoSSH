var courseCriteria = {
		pageNum : 1,
		pageSize : 8
};
var planCriteria = {
		pageNum : 1,
		pageSize : 8
};

var filterBoxOfCourse;

var filterBoxOfPlan;

var courseDataList;

var planDataList;

/**
 * Create course DataList setting.
 */
function makeCourseDataListSetting(){
	var courseDataListSetting = {
        columns:[
            {EN:'ID', ZH:'编号', sortName:'prefix_id', width:70, isMustShow:true},
            {EN:'Name', ZH:'名称', sortName:'course_name_sort', width:100, isMustShow:true, align : 'left'},
            {EN:'Brief', ZH:'简介', width:180, autoWidth: true, align : 'left'},
            {EN:'Target Attendees', ZH:'目标人群', width:150, align : 'left'},
            {EN:'Duration', ZH:'持续时间', sortName:'course_duration', width:100},
            {EN:'Type', ZH:'类别', sortName:getListI18NSortField('courseType_type_name_sort'), width:100},
            {EN:'Tag', ZH:'标签', sortName:'course_tag', width:80, align : 'left'},
            {EN:'If-Certificated', ZH:'是否认证', sortName:'course_is_certificated', width:130},
            {EN:'Update History', ZH:'更新历史', width:150},
            {EN:'Author', ZH:'作者', sortName:'course_author_name_sort', width:100},
            {EN:'History Trainer', ZH:'历史培训者', width:150}
        ],
        language: configI18n(),
        criteria: courseCriteria,
        hasPageSize:false,
        minHeight: 240,
        pageSizes: [],
        hasAttachmentIcon: true,
        hasAddIcon : true,
        addIconHandler : function(row){
			var courseId = row.find(".dataList-div-addIcon").attr("pk");
		    selectCourseBlock(row.find(".ID").text(), row.find(".Name").text(), "C", courseId);
		},
		isRepeat : true,
        url: $('#basePath').val()+'course/searchCourse',
        updateShowField: {
        	url: $('#basePath').val()+'searchCommon/searchCommon_updateShowFields?searchFlag=3',// 0 is course
            callback: function(data){
            	if(data && data.error){
            		alert("save fail!");
                }
            }
        },
        updateShowSize: {
            url: $('#basePath').val()+'searchCommon/searchCommon_updateShowSize?searchFlag=3',// 0 is course
            callback: function(data){
                if(data && data.error){
                    alert("save fail!");
                }
            }
        }
//        ,
//        contentHandler : function(str){
//    		return resultContentHandle(str);
//        }
        
	};
	return courseDataListSetting;
}
/**
 * Create plan DataList setting. 
 */
function makePlanDataListSetting(){
	var planDataListSetting = {
			columns:[
			         {EN:'ID', ZH:'编号', sortName:'prefix_id', width:70, isMustShow:true},
			         {EN:'Name', ZH:'名称', sortName:'plan_name', width:100, isMustShow:true, align : 'left'},
			         {EN:'Brief', ZH:'简介', width:200, autoWidth: true, align : 'left'},
			         {EN:'Type', ZH:'类别', sortName:getListI18NSortField('planType_type_name_sort'), width:100},
			         {EN:'Tag', ZH:'标签', sortName:'plan_tag', width:100, align : 'left'},
			         {EN:'Date', ZH:'日期', width:150},
			         {EN:'Creator', ZH:'创建者', sortName:'plan_creator', width:90},
			],
			language: configI18n(),
			criteria: planCriteria,
			minHeight: 240,
			pageSizes: [],
			hasPageSize:false,
			hasAttachmentIcon: true,
			hasAddIcon : true,
	        addIconHandler : function(row){
				var planId = row.find(".ID").text();
				findPlanCoursesFromPlan(planId.substr(2));
			},
			isRepeat : true,
			url: $('#basePath').val()+'plan/searchPlan',
			updateShowField: {
				url: $('#basePath').val()+'searchCommon/searchCommon_updateShowFields?searchFlag=4',// 4 is select course from plan
				callback: function(data){
					if(data && data.error){
						alert("save fail!");
					}
				}
			},
			updateShowSize: {
				url: $('#basePath').val()+'searchCommon/searchCommon_updateShowSize?searchFlag=4',// 4 is select course from plan
				callback: function(data){
					if(data && data.error){
						alert("save fail!");
					}
				}
			}
	};
	return planDataListSetting;
}

/**
 * Get course types and initialize filter box
 */
function initCourseTypeInfo(initPart) {
	if (initPart === "Course") {
		if (filterBoxOfCourse != undefined) {
			return false;
		}
	}
	if (initPart === "Plan") {
		if (filterBoxOfPlan != undefined) {
			return false;
		}
	}
	$.ajax({
        type: "POST",
        url: $('#basePath').val()+"searchCommon/searchCommon_findCourseTypes",
        data: {},
        success: function(data) {
        	var listData = data.courseTypes;
        	var typeName = "";
            $.each(listData, function(i, obj) {
            	typeName = obj.typeName;
            	typeId = obj.courseTypeId;
            	$("#courseType" + initPart).append("<p><input type='checkBox' name='field' content='"+typeName+"' value='"+typeId+"'/><label>"+typeName+"</label></p>");
            });
            var filterButton = $(".filterBtn");
            
            if ("Plan" === initPart) {
            	filterBoxOfPlan = $(".filterDiv" + initPart).filterBox();
            	
            } else if ("Course" === initPart) {
            	filterBoxOfCourse = $(".filterDiv").filterBox();
            }
            
        }
     });
}

/***
 * Initialize search list in select course page.
 * @param _searchFlag  3:Course; 4:Plan
 */
function initSearchList(_searchFlag) {
	$.ajax({
        type: "POST",
        url: $('#basePath').val()+"searchCommon/searchCommon_findDataListInfo?searchFlag="+_searchFlag,
        data: {},
        success: function(jsonData) {
        	if (3 ===_searchFlag) {
        		var courseDataListInfo = makeCourseDataListSetting();
        		courseDataListInfo.criteria = courseCriteria;
        		courseDataListInfo.pageSizes = jsonData.pageSizesJson;
        		if (undefined === courseDataList){
        			courseDataList = $(".dataListOfCourse").DataList(courseDataListInfo);
        		}
        		courseDataList.setShow(jsonData.fieldsJson);
        	} else if (4 === _searchFlag) {
        		planDataListInfo = makePlanDataListSetting();
        		planDataListInfo.criteria = planCriteria;
        		planDataListInfo.pageSizes = jsonData.pageSizesJson;
        		if (undefined === planDataList){
        			planDataList = $(".dataListOfPlan").DataList(planDataListInfo);
        		}
        		planDataList.setShow(jsonData.fieldsJson);
        	}
        }
     });
}
/**
 * Select course or plan course from course or plan.
 * @param prifixId
 * @param courseName
 * @param prifixType
 * @param courseId
 * @param planId
 */
function selectCourseBlock(prifixId, courseName, prifixType, courseId, planId) {
	var $selectedClassDiv = $('<div class="selectedCourseDiv"><div>').appendTo($("#slideCourseResult"));
	$selectedClassDiv.data("courseId", courseId+"_"+prifixType);
	if (planId) {
		$selectedClassDiv.data("planId", planId);
	}
	var $courseNameValue = $('<div class="courseNameValue"></div>').appendTo($selectedClassDiv).text(courseName);//For popup.
	$('<span class="selectedCourseLeft"></span>').appendTo($selectedClassDiv);
	var $selectedCourseMid = $('<span class="selectedCourseMid"></span>').appendTo($selectedClassDiv);
	$('<span class="idSpan"></span>').appendTo($selectedCourseMid).text(prifixId);
	var $nameSpan = $('<span class="nameSpan"></span>').appendTo($selectedCourseMid);
	$nameSpan.text(courseName);
	var $closeSpan = $('<span class="closeSpan"></span>').appendTo($selectedCourseMid);
	$('<span class="selectedCourseRight"></span>').appendTo($selectedClassDiv);
	var totalCount = parseInt($("#tiemCount").text()) + 1;
	$selectedClassDiv.css( {
		'top' : totalCount % 2 ? '0px':'24px',
        'left' : parseInt((totalCount-1) / 2) * 300
    });
	$closeSpan.bind("click", function(){
		removeSelectedFromPopup($selectedClassDiv);
	});
	if (totalCount % 2 && totalCount > 6) {
		$('#courseResultLeft').removeClass('resultLeftDisable').addClass('resultLeft');
    }
	//slide to head after add new node.
	var totalRow = (parseInt(totalCount / 2) + totalCount % 2);
	var newMargin = (totalRow - 3) * 300 + parseInt($("#slideCourseResult").css('margin-left'));
	if (newMargin > 0 && (totalCount % 2)) {
		$("#slideCourseResult").animate( {
            'margin-left' : '-=' + newMargin.toString()
        }, 50, function(){
        	$('#courseResultRight').removeClass('resultRight').addClass('resultRightDisable');
        });
	}
	$("#tiemCount").html(totalCount);
	//Set toolTip.
	if ($courseNameValue.width()>208) {
		$nameSpan.poshytip({
			allowTipHover : true ,
			className: 'tip-green',
			content: $courseNameValue.text()
		});
	}
}

/**
 * delete selected plan course from pop-up.
 */
function removeSelectedFromPopup($courseItem) {
	var totalCount = parseInt($("#tiemCount").text()) -1;
	$("#tiemCount").text(totalCount);
	removeMarks($courseItem);
	$(".selectedCourseDiv").each(function(i, node){
		$(node).animate({
			'top' : i % 2 * 24,
	        'left' : parseInt(i / 2) * 300
        }, 50);
	});
	var marginLeft = parseInt($("#slideCourseResult").css('margin-left'));
	if (marginLeft < 0 && totalCount%2===0) {
		$("#slideCourseResult").animate( {
            'margin-left' : '+=300'
        }, 50, function(){
        	judgeSlideStatus();
        });
	} else {
		judgeSlideStatus();
	}
}

/**
 * Check if Add Icon should be remove in course search list.
 * @param courseIdPrefix
 * @return true: remove; false: not remove.
 */
function checkRemovedCourse(courseIdPrefix) {
	var needRemove = true;
	$("#slideCourseResult").children().each(function(index, selectedNode){
		if ($(selectedNode).data("courseId") == courseIdPrefix) {
			needRemove = false;
			return false;
		}
	});
	return needRemove;
}

/**
 * Check if Add Icon should be remove in plan search list.
 * @param currentPlanId
 * @return true: remove; false: not remove.
 */
function checkRemovedPlan(currentPlanId) {
    var needRemove = true;	
	$("#slideCourseResult").children().each(function(index, selectedNode){
		if ($(selectedNode).data("planId") && $(selectedNode).data("planId") == currentPlanId) {
			needRemove = false;
			return false;
		}
	});
	return needRemove;
}


/**
 * Remove selected marks from search list.
 * @param courseIdPrefix
 */
function removeMarks($courseItem) {
	var courseIdPrefix = $courseItem.data("courseId");
	var idStrs = courseIdPrefix.split("_");
	if (idStrs[1] == "C") {
		$courseItem.remove();
		if (checkRemovedCourse(courseIdPrefix)) {
			var courseId = parseInt(idStrs[0], 10);
			courseDataList.cleanAddIcon(courseId);
		}
		
	}
	if (idStrs[1] == "PC") {
		var currentPlanId = $courseItem.data("planId");
		$courseItem.remove();
		if (checkRemovedPlan(currentPlanId)) {
			var planId = parseInt(currentPlanId, 10);
			planDataList.cleanAddIcon(planId);
		}
	}
}

/**
 * slide to left
 */
function leftSlide() {
	var marginLeft = parseInt($("#slideCourseResult").css('margin-left'));
	if (marginLeft < 0) {
		if (marginLeft == -300) {
			$('#courseResultLeft').removeClass('resultLeft').addClass('resultLeftDisable');
		}
		$("#slideCourseResult").animate( {
            'margin-left' : '+=300'
        }, 50);
		
		$('#courseResultRight').removeClass('resultRightDisable').addClass('resultRight');
	}
}

/**
 * slide to right
 */
function rightSlide() {
	var totalCount = parseInt($("#tiemCount").text());
	var marginLeft = parseInt($("#slideCourseResult").css('margin-left'));
	var totalLength = (parseInt(totalCount / 2) + totalCount % 2)*300;
	if (900 - marginLeft < totalLength) {
		$("#slideCourseResult").animate( {
            'margin-left' : '-=300'
        }, 50, function () {
        	judgeSlideStatus();
		});
	}
}

/**
 * Judge if slide is usable
 */
function judgeSlideStatus() {
	var totalCount = parseInt($("#tiemCount").text());
	var marginLeft = parseInt($("#slideCourseResult").css('margin-left'));
	var totalLength = (parseInt(totalCount / 2) + totalCount % 2)*300;
	if (900 - marginLeft >= totalLength) {
		$('#courseResultRight').removeClass('resultRight').addClass('resultRightDisable');
	}
	if (marginLeft == 0) {
		$('#courseResultLeft').removeClass('resultLeft').addClass('resultLeftDisable');
	} else if (marginLeft < 0) {
		$('#courseResultLeft').removeClass('resultLeftDisable').addClass('resultLeft');
	}
}

/**
 * click confirm button on select course part.
 */
function confirmCourses(){
	var ids = "";
	$("#slideCourseResult").children().each(function(index, courseNode){
		ids = ids + $(courseNode).data("courseId") + ",";
	});
	if (ids != "") {
		$.ajax( {
			type : "POST",
	        url : "plan/courseListConfirm.action",
	        dataType:"json",
	        data :{
			    "actualCourseIds":ids
		    },
		    success : function(data) {
		    	createActualCourseList(data);
		    }
		});
	}
}

//set Criteria of course
function setCourseCriteria() {
	var queryString = $("#keyword").val();
	courseCriteria.queryString = queryString;
	courseCriteria.searchFields = initSearchFields($("#searchFields"));
	
	// set typeIds
	var typeIds = "";
	$("#courseType").find(":checked").each(function() {
		if (this.value == "all") {
			typeIds = typeIds;
		} else {
			if (typeIds == null || typeIds == "") {
				typeIds = this.value;
        	} else {
        		typeIds = typeIds + " " + this.value;
        	}
		}
	});
	courseCriteria.typeIds = typeIds;
	
	// set isCertificateds
	var isCertificateds = "";
	$("#isCertificateds").find(":checked").each(function() {
		if (isCertificateds == null || isCertificateds == "") {
			isCertificateds = this.value;
		} else {
			isCertificateds = isCertificateds + " " + this.value;
		}
	});
	courseCriteria.isCertificateds = isCertificateds;
}

////select from plan part
function setPlanCriteria() {
	var queryString = $("#keywordOfPlan").val();
	planCriteria.queryString = queryString;
	planCriteria.searchFields = initSearchFields($("#searchFieldsOfPlan"));
	
	// set searchFlag
	planCriteria.searchOperationFlag = "search";
	
	// set planTypeIds
	var planTypeIds = "";
	$("#planType").find(":checked").each(function() {
		if (planTypeIds == null || planTypeIds == "") {
			planTypeIds = this.value;
        } else {
        	planTypeIds = planTypeIds + " " + this.value;
        }
	});
	planCriteria.planTypeIds = planTypeIds;
	
	// set planCourseTypeIds
	var typeIds = "";
	$("#courseTypeOfPlan").find(":checked").each(function() {
		if (this.value == "all") {
			typeIds = typeIds;
		} else {
			if (typeIds == null || typeIds == "") {
				typeIds = this.value;
        	} else {
        		typeIds = typeIds + " " + this.value;
        	}
		}
	});
	planCriteria.planCourseTypeIds = typeIds;
	
	// set time
	planCriteria.publishLowerDate = $("#publishLowerDate").val();
	planCriteria.publishUpperDate = $("#publishUpperDate").val();
	planCriteria.executeLowerDate = $("#executeLowerDate").val();
	planCriteria.executeUpperDate = $("#executeUpperDate").val();
}

function initSearchFields($fieldsContent) {
	var searchFields = "";
	var fields = $fieldsContent.find(":checked");
	if (fields.length > 0) {
		fields.each(function() {
			if (this.value == "all") {
				searchFields = searchFields;
			} else {
				if (searchFields == null || searchFields == "") {
					searchFields = this.value;
		        } else {
		        	searchFields = searchFields + "," + this.value;
		        }
			}
			});
	} else {
		$fieldsContent.find(":checkbox").each(function(){
			if (this.value == "all") {
				searchFields = searchFields;
			} else {
				if (searchFields == null || searchFields == "") {
					searchFields = this.value;
				} else {
					searchFields = searchFields + "," + this.value;
				}
			}
		});
	}
	return searchFields;
}

/**
 * Get the sort field according to the language of broswer.
 * 
 * @param sortName  Sort name.
 * @return  The sort field.
 */
function getListI18NSortField(sortName) {
	var language = getCustomLanguage(); // in message.js.jsp
	if (language == null || language == "") {
		language = getLanguageByBrowser();
	}
	if (language == "zh_CN" || language == "zh-CN" ) {
		return sortName + "_" + "zh";
	} else if (language == "en_US" || language == "en-US" ) {
		return sortName + "_" + "en";
	}
	return sortName;
}

function removeAllMark(){
	if (courseDataList) {
		courseDataList.cleanAllAddIcon();
	}
	if (planDataList) {
		planDataList.cleanAllAddIcon();
	}
}

//initialKeywords(); 
function initKeyword($keywordInput, $keywordTipDiv) {
    $keywordInput.focus(function() {
		$keywordTipDiv.hide();
    });
    $keywordInput.blur(function() {
      if($(this).val() == "") {
      	$keywordTipDiv.show();
      }
    });
    $keywordInput.keyup(function(event) {
  	    if(event.keyCode == "13"){
  	    	var keywordId = $keywordInput.attr("id");
  	    	if (keywordId === "keyword") {
  	    		setCourseCriteria();
  	      	    courseDataList.search();
  	      	    return;
  	    	}
            if (keywordId === "keywordOfPlan") {
  	    		setPlanCriteria();
  	      	    planDataList.search();
  	      	    return;
  	    	}
  	    }
    });
    if($keywordInput.val() != ""){
		$keywordTipDiv.hide();
	};	
    $keywordTipDiv.click(function(){
  	    $(this).hide();
  	    $keywordInput.focus();
    });  
}
/**
 * get all plan courses from a plan
 * @param planId
 * @return
 */
function findPlanCoursesFromPlan(planId) {
	$.ajax( {
		type : "POST",
        url : $('#basePath').val() + "plan/findActualCoursesByPlanId",
        dataType:"json",
        data :{
		    "planId":planId
	    },
	    success : function(data) {
	    	$.each(data, function(i, planCourse) {
	    		selectCourseBlock(planCourse.prefixIdValue, planCourse.courseName,"PC", planCourse.actualCourseId, planId);
	    	});
	    }
	});
}

function initSlideButton() {
	$("#courseResultLeft").addClass("resultLeftDisable").removeClass("resultLeft");
	$("#courseResultRight").addClass("resultRightDisable").removeClass("resultRight");
}
