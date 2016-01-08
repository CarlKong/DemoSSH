/**
 * Initialize the a tag for viewing detail. 
 * Of course, it can bind a click event.
 * It need some data from page and js data(the data is defined in js) to delivery to view page.
 * Then use jquery to create dynamic form.
 **/
function initialViewDetail(){
	$(".viewDetail").live("click",function(){
		var criteriaParameters = "";
		setSearchPlanCriteria();
	    
    	var planId = $(this).attr("planId");
    	planId = parseInt(planId);
    	var planIds = setPlanIds();
    	var totalPage = findTotalPage();
    	var pageNum = findPageNum();
    	var sortCondition = findSortCondition();
    	var sortName = sortCondition.sortName;
    	var sortSign = sortCondition.sortSign;
    	var pageSize = criteria.pageSize;
    	var queryString = criteria.queryString;
    	var searchFields = criteria.searchFields;
    	var planTypeIds = criteria.planTypeIds;
    	
    	var fromSearchToViewCondition = {
    			"fromSearchToViewCondition.nowId" : planId,
    			"fromSearchToViewCondition.backupId" : planIds,
    			"criteria.pageNum" : pageNum,
    			"criteria.pageSize" : pageSize,
    			"criteria.sortSign" : sortSign,
    			"criteria.sortName" : sortName,
    			"criteria.queryString" : queryString,
    			"criteria.searchFields" : searchFields,
    			"criteria.planTypeIds" : planTypeIds,
    			"criteria.planCourseTypeIds":criteria.planCourseTypeIds,
    			"criteria.publishLowerDate":criteria.publishLowerDate,
    			"criteria.publishUpperDate":criteria.publishUpperDate,
    			"criteria.executeLowerDate":criteria.executeLowerDate,
    			"criteria.executeUpperDate":criteria.executeUpperDate,
    			"criteria.searchOperationFlag":criteria.searchOperationFlag,
    			"fromSearchToViewCondition.totalPageNum" : totalPage,
    			"fromSearchToViewCondition.isNoSelectedflag" : isNoSelectedflag
    	};
    	
    	for (var key in fromSearchToViewCondition ) {
    		criteriaParameters += "<input type = 'hidden' name = '" + key + "' value =  '" + fromSearchToViewCondition[key] + "' />"
    	}
    	
    	$("#viewDetailForm").append(criteriaParameters);
    	
    	$("#viewDetailForm").submit();
    	
    });
}
/**
 * Initialize the crieriaParam data which come from view page.
 **/
function initialCriteriaParam(){
	if (hasCondition == 'true') {
		criteriaParam = {
				pageNum : document.getElementById("_criteria_pageNum").value,
				pageSize : document.getElementById("_criteria_pageSize").value,
				sortSign : document.getElementById("_criteria_sortSign").value,
				sortName : document.getElementById("_criteria_sortName").value,
				queryString : document.getElementById("_criteria_queryString").value,
				searchFields : document.getElementById("_criteria_searchFields").value,
				planTypeIds : document.getElementById("_criteria_planTypeIds").value,
				planCourseTypeIds : document.getElementById("_criteria_planCourseTypeIds").value,
				publishLowerDate : document.getElementById("_criteria_publishLowerDate").value,
				publishUpperDate : document.getElementById("_criteria_publishUpperDate").value,
				executeLowerDate : document.getElementById("_criteria_executeLowerDate").value,
				executeUpperDate : document.getElementById("_criteria_executeUpperDate").value,
				searchOperationFlag : document.getElementById("_criteria_searchOperationFlag").value
		};
		isNoSelectedflag = document.getElementById("_fromSearchToViewCondition_isNoSelectedflag").value;
	}
}
/**
 * Not set but get the backup plan Ids by using planId in page.
 * @return
 */
function setPlanIds() {
	var planIdDoms = $(".viewDetail");
	var planIds = "";
	planIdDoms.each(function() {
		if (planIds == null || planIds == "") {
			planIds = $(this).attr("planId");
		} else {
			planIds = planIds + "," + $(this).attr("planId");
		}
	});
	return planIds;
}
/**
 * Draw the search page when jump back from view page.
 * The next methods named draw* are all used for this method.
 * This method is used in searchcommon.js.
 * @return
 */
function drawPage() {
	PlanTypeFlag=drawPlanType();
	SearchFieldsFlag=drawSearchFields();
	CourseTypeFlag=drawCourseType();
	PublishLowerDateFlag=drawPublishLowerDate();
	PublishUpperDateFlag=drawPublishUpperDate();
	ExecuteLowerDateFlag=drawExecuteLowerDate();
	ExecuteUpperDateFlag=drawExecuteUpperDate();
	if (PlanTypeFlag||SearchFieldsFlag||CourseTypeFlag||PublishLowerDateFlag||PublishUpperDateFlag||ExecuteLowerDateFlag||ExecuteUpperDateFlag) {
		$(".existedFlag").css("display", "inline-block");
	}
	drawQueryString();
}

function isInArray(value, valueArray) {
	var flag = false;
	for (var index = 0; index < valueArray.length; index++) {
		if (value.trim() == valueArray[index].trim()) {
			flag = true;
			break;
		}
	}
	return flag;
}
/**
 * Draw plan type field in filter box.
 * @return
 */
function drawPlanType(){
	var someoneChecked = false;
	var typeIds = criteriaParam.planTypeIds;
	var typeIdArr = typeIds.split(" ");
	
	var typeIdDom = $(".single_condition #planType p");
	
	if (typeIdArr != null && typeIdArr.length > 0) {
		typeIdDom.each(function(){
			var typeIdValue = $(this).children("input")[0].value;
			var flag = isInArray(typeIdValue, typeIdArr);
			if (flag) {
				$(this).children("input").each(function(){
					$(this).attr("checked", "checked");
				});
				$(this).children("span").each(function(){
					$(this).addClass("span_checked");
					someoneChecked = true;
				});
			}
		});
	}
	return someoneChecked;
}

/**
 * Draw search fields in filter box.
 * @return
 */
function drawSearchFields(){
	var someoneChecked = false;
	var searchFields = criteriaParam.searchFields;
	var searchFieldArr = searchFields.split(",");
	if("undefined" != typeof operation_searchMyPlan && operation_searchMyPlan == 'searchMyPlan'){
		if(roleClickFlag == master_contant){
			isNoSelectedflag = masterSearchCache.isNoSelectedflag;
		}
		if(roleClickFlag == trainer_contant){
			isNoSelectedflag = trainerSearchCache.isNoSelectedflag;
		}
		if(roleClickFlag == trainee_contant){
			isNoSelectedflag = traineeSearchCache.isNoSelectedflag;
		}
	}
	if(isNoSelectedflag != 1){
		var searchFieldDoms = $(".first_single_condition #searchFields p");
		var allFlag = true;
		searchFieldDoms.each(function(){
			var searchFieldValue = $(this).children("input")[0].value;
			if (searchFieldValue != "all") {
				
				var flag = isInArray(searchFieldValue, searchFieldArr);
				if (flag) {
					$(this).children("input").each(function(){
						$(this).attr("checked", "checked");
					});
					$(this).children("span").each(function(){
						$(this).addClass("span_checked");
						someoneChecked = true;
					});
				}else {
					allFlag = false;
				}
			}
		});
		if (allFlag) {
			searchFieldDoms.each(function(){
				var searchFieldValue = $(this).children("input")[0].value;
				if (searchFieldValue == "all") {
					$(this).children("input").each(function(){
						$(this).attr("checked", "checked");
					});
					$(this).children("span").each(function(){
						$(this).addClass("span_checked");
					});
				}
			});
		}
	}
	return someoneChecked;
}


function drawCourseType(){
	var someoneChecked = false;
	var searchFields = criteriaParam.planCourseTypeIds;
	var searchFieldArr = searchFields.split(" ");
	
	var searchFieldDoms = $(".single_condition #courseType p");
	
	var allFlag = true;
	searchFieldDoms.each(function(){
		var searchFieldValue = $(this).children("input")[0].value;
		if (searchFieldValue != "all") {
			
			var flag = isInArray(searchFieldValue, searchFieldArr);
			if (flag) {
				$(this).children("input").each(function(){
					$(this).attr("checked", "checked");
				});
				$(this).children("span").each(function(){
					$(this).addClass("span_checked");
					someoneChecked = true;
				});
			}else {
				allFlag = false;
			}
		}
	});
	if (allFlag) {
		searchFieldDoms.each(function(){
			var searchFieldValue = $(this).children("input")[0].value;
			if (searchFieldValue == "all") {
				$(this).children("input").each(function(){
					$(this).attr("checked", "checked");
				});
				$(this).children("span").each(function(){
					$(this).addClass("span_checked");
				});
			}
		});
	}
	return someoneChecked;	
}

function drawPublishLowerDate(){
	var someoneChecked = false;
	var publishLowerDate = criteriaParam.publishLowerDate;
	if (publishLowerDate != null && publishLowerDate != "") {
		$("#publishLowerDate").val(publishLowerDate);
		someoneChecked = true;
	}
	return someoneChecked;	
}


function drawPublishUpperDate(){
	var someoneChecked = false;
	var publishUpperDate = criteriaParam.publishUpperDate;
	if (publishUpperDate != null && publishUpperDate != "") {
		$("#publishUpperDate").val(publishUpperDate);
		someoneChecked = true;
	}
	return someoneChecked;	
}

function drawExecuteLowerDate(){
	var someoneChecked = false;
	var executeLowerDate = criteriaParam.executeLowerDate;
	if (executeLowerDate != null && executeLowerDate != "") {
		$("#executeLowerDate").val(executeLowerDate);
		someoneChecked = true;
	}
	return someoneChecked;	
}

function drawExecuteUpperDate(){
	var someoneChecked = false;
	var executeUpperDate = criteriaParam.executeUpperDate;
	if (executeUpperDate != null && executeUpperDate != "") {
		$("#executeUpperDate").val(executeUpperDate);
		someoneChecked = true;
	}
	return someoneChecked;	
}

function drawQueryString(){
	var queryString = criteriaParam.queryString;
	if (queryString != null && queryString != "") {
		$("#keyword").val(queryString);
		$("#searchInputTipDiv").hide();
	}
}
function drawSort() {
	var sortName = criteriaParam.sortName;
	var sortSign = criteriaParam.sortSign;
	var tableHeaderDoms = $(".dataList-div-data .dataList-div-head div");
	tableHeaderDoms.each(function(){
		var sortHeaderSpanDoms = $(this).children("span");
		if (sortHeaderSpanDoms.length == 2) {
			var sortHeaderSpan = $(this).children(':eq(0)');
			var sortHeaderName = sortHeaderSpan.attr("en");
			
			if (sortName == sortHeaderName) {
//				alert(sortHeaderName);
			}
		}
	});
}
/**
 * The next methods named find* are all used for finding necessary data to initialViewDetail method.
 * This method is used to find total page data from page.
 * @return
 */
function findTotalPage() {
	var dataListPageDoms = $(".dataList-div-page .dataList-a-page");
	var totalPage = 1;
	dataListPageDoms.each(function(){
		var nowValue = this.innerHTML;
		if (parseInt(nowValue) > parseInt(totalPage)) {
			totalPage = nowValue;
		}
	});
	return totalPage;
}
/**
 * Find page num from data list.
 * @return
 */
function findPageNum() {
	var pageNumDoms = $(".dataList-div-page .currentPage");
	var pageNum = 1;
	pageNumDoms.each(function(){
		pageNum = this.innerHTML;
	});
	return pageNum;
}
/**
 * Find sort condition from data list.
 * @return
 */
function findSortCondition() {
	var sortCondition = {
			sortName : "",
			sortSign : "desc"
	};
	var sortDoms = $(".dataList-div-head .dataList-div-sort-style");
	sortDoms.each(function(){
		var sortHeaderSpanDoms = $(this).children("span");
		if (sortHeaderSpanDoms.length == 2) {
			var sortHeaderSpan = $(this).children(':eq(0)');
			var sortSignSpan = $(this).children(':eq(1)');
			
			var sortHeaderName = sortHeaderSpan.attr("en");
			var sortHeaderSign = "desc";
			
			if (sortSignSpan.hasClass("dataList-desc")) {
				sortHeaderSign = "desc";
			} else if(sortSignSpan.hasClass("dataList-asc")){
				sortHeaderSign = "asc";
			}
			
			sortCondition.sortName = sortHeaderName;
			sortCondition.sortSign = sortHeaderSign;
		}
	});
	return sortCondition;
}