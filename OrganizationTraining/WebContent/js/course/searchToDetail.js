/**
 * Initialize the a tag for viewing detail. 
 * Of course, it can bind a click event.
 * It need some data from page and js data(the data is defined in js) to delivery to view page.
 * Then use jquery to create dynamic form.
 **/
function initialViewDetail(){
	$(".viewDetail").live("click",function(){
		var criteriaParameters = "";
		setCriteria();
		
    	var courseId = $(this).attr("courseId");
    	courseId = parseInt(courseId);
    	var courseIds = setCourseIds();
    	var totalPage = findTotalPage();
    	var pageNum = findPageNum();
    	var sortCondition = findSortCondition();
    	var sortName = sortCondition.sortName;
    	var sortSign = sortCondition.sortSign;
    	var pageSize = criteria.pageSize;
    	var queryString = criteria.queryString;
    	var searchFields = criteria.searchFields;
    	var isCertificated = criteria.isCertificateds;
    	var typeIds = criteria.typeIds;
    	
    	var fromSearchToViewCondition = {
    			"fromSearchToViewCondition.nowId" : courseId,
    			"fromSearchToViewCondition.backupId" : courseIds,
    			"criteria.pageNum" : pageNum,
    			"criteria.pageSize" : pageSize,
    			"criteria.sortSign" : sortSign,
    			"criteria.sortName" : sortName,
    			"criteria.queryString" : queryString,
    			"criteria.searchFields" : searchFields,
    			"criteria.typeIds" : typeIds,
    			"criteria.isCertificateds" : isCertificated,
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
				typeIds : document.getElementById("_criteria_typeIds").value,
				isCertificateds : document.getElementById("_criteria_isCertificateds").value
		};
		isNoSelectedflag=document.getElementById("_fromSearchToViewCondition_isNoSelectedflag").value;
	}
}

/**
 * Not set but get the backup course Ids by using courseId in page.
 * @return
 */
function setCourseIds() {
	var courseIdDoms = $(".viewDetail");
	var courseIds = "";
	courseIdDoms.each(function() {
		if (courseIds == null || courseIds == "") {
			courseIds = $(this).attr("courseId");
		} else {
			courseIds = courseIds + "," + $(this).attr("courseId");
		}
	});
	return courseIds;
}
/**
 * Draw the search page when jump back from view page.
 * The next methods named draw* are all used for this method.
 * This method is used in searchcommon.js.
 * @return
 */
function drawPage() {
	var courseTypesFlag = drawCourseType();
	var searchFieldsFlag = drawSearchFields();
	var isCertificatedFlag = drawCertificated();
	if (courseTypesFlag || searchFieldsFlag || isCertificatedFlag) {
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
 * Draw course type field in filter box.
 * @return
 */
function drawCourseType(){
	// for search my course page, there is no need to set course type
	if("undefined" != typeof operation_searchMyCourse && operation_searchMyCourse == 'searchMyCourse'){
		return ;
	}
	var someoneChecked = false;
	var typeIds = criteriaParam.typeIds;
	var typeIdArr = typeIds.split(" ");
	
	var typeIdDom = $(".single_condition #courseType p");
	
	// Whether all course type is checked.
	var allFlag = true;
	typeIdDom.each(function(){
		var typeValue = $(this).children("input")[0].value;
		if (typeValue != "all") {
			var flag = isInArray(typeValue, typeIdArr);
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
		typeIdDom.each(function(){
			var typeValue = $(this).children("input")[0].value;
			if (typeValue == "all") {
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

/**
 * Draw search fields in filter box.
 * @return
 */
function drawSearchFields(){
	var someoneChecked = false;
	var searchFields = criteriaParam.searchFields;
	var searchFieldArr = searchFields.split(",");
	if(isNoSelectedflag!=1){
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

/**
 * Draw certificated field in filter box.
 * @return
 */
function drawCertificated(){
	// for search my course page, there is no need to set certificated
	if("undefined" != typeof operation_searchMyCourse && operation_searchMyCourse == 'searchMyCourse'){
		return ;
	}
	var someoneChecked = false;
	var isCertificated = criteriaParam.isCertificateds;
	var isCertificatedArray = isCertificated.split(" ");
	if (isCertificatedArray != null && isCertificatedArray.length > 0) {
		var isCertificatedDoms = $(".last_single_condition #isCertificateds p");
		isCertificatedDoms.each(function(){
			var isCertificatedValue = $(this).children("input")[0].value;
			var flag = isInArray(isCertificatedValue, isCertificatedArray);
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
 * Draw query string in page.
 * @return
 */
function drawQueryString(){
	var queryString = criteriaParam.queryString;
	if (queryString != null && queryString != "") {
		$("#keyword").val(queryString);
		$("#searchInputTipDiv").hide();
	}
}

/**
 * Draw sort condition in data list.
 * But now is useless.
 * @return
 */
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