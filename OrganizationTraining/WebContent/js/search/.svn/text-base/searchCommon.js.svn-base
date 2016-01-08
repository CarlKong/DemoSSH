var keyword_content = $("#keyword_content").val();
var attachmentUI;
// initial keywords
function initialKeywords(){	
	if($("#keyword").val() != ""){
		$("#searchInputTipDiv").hide();
	} else {
		$("#searchInputTipDiv").show();
	}
	$("#keyword").focus(function() {
		$("#searchInputTipDiv").hide();
    });
    $("#keyword").blur(function() {
        if($(this).val() == "") {
    		$("#searchInputTipDiv").show();
        }
    });
    $("#searchInputTipDiv").click(function(){
    	$(this).hide();
    	$("#keyword").focus();
    });   	
}

// Prepare data for dataList
function findDataListInfo(searchFlag) {
	$.ajax({
        type: "POST",
        url: $('#basePath').val()+"searchCommon/searchCommon_findDataListInfo?searchFlag="+searchFlag,
        data: {},
        success: function(data) {
        	var jsonData = data;
        	criteria.pageSize = jsonData.pageSizeInt;
        	dataListInfo.pageSizes = jsonData.pageSizesJson;
        	selectList();
        	dataList.setShow(jsonData.fieldsJson);
        	if ("undefined" != typeof hasCondition && hasCondition == 'true') {
        		dataList.criteria = criteriaParam;
        		dataList.search();
        	}
        	if ("undefined" != typeof operation_searchMyPlan && operation_searchMyPlan == 'searchMyPlan') {
        		searchMyPlan();
        	}
        	if ("undefined" != typeof operation_searchMyCourse && operation_searchMyCourse == 'searchMyCourse') {
        		searchMyCourse();
        	}
        }
     });
}

// set filterBox course type
function findCourseTypes() {
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
            	$("#courseType").append("<p><input type='checkBox' name='field' content='"+typeName+"' value='"+typeId+"'/><label>"+typeName+"</label></p>");
            });
        	$(".filterDiv").filterBox({
        		//checkboxIcon: $('#basePath').val()+"filterBox/images/ICN_Checkbox_14x15.png",
        		//checkboxActiveIcon: $('#basePath').val()+"filterBox/images/ICN_Checkbox_Active_14x15.png"
        	});
        	if ("undefined" != typeof hasCondition && hasCondition == 'true') {
        		drawPage();
        	}
        }
     });
}

// set criteria.queryString
function setQueryString() {
	var queryString = $("#keyword").val();
	criteria.queryString = queryString;
}

// set criteria.searchFields
function setSearchFields() {
	var searchFields = "";
	var fields = $("#searchFields").find(":checked");
	if(fields.length == 0){
		isNoSelectedflag = 1; // hasn't been selected
	}else{
		isNoSelectedflag = 0;
	}
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
		$("#searchFields").find(":checkbox").each(function(){
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
	criteria.searchFields = searchFields;
}

// change pic in brief style and content handle
function resultContentHandle(str) {
	if(!str || 'null' == str) {
        return '';
    }
    var temp = str;
    if(str){
        temp = temp.replace(/<br>|<br \/>|<p>|<\/p>/gi,' ');
        if(/<img.*>/gi.test(temp)){
        	temp = temp.replace(/<img/g, "<img class='showImag'");
        }
    }
    return temp;
}

/**
 * Get the sort field according to the language of broswer.
 * 
 * @param sortName  Sort name.
 * @return  The sort field.
 */
function getI18NSortField(sortName) {
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

// configure download popup site in page
function initialPopup() {
	showLayer($("#downLoadPopup"));
	var v_top = ($(window).height() - $("#downLoadPopup").height())/2 + document.body.scrollTop;
	$("#downLoadPopup").css("top", v_top);
}

// configure close operation
function initialClose() {
	$("#downClosePic").live("click",function(){
		if (attachmentUI) {
			attachmentUI.destroyUploadUI("attachment_UI_Id");
		}
		closeLayer($("#downLoadPopup"));
	});
}

/**
 * initialSearchPlanDownLoad (used in search plan & my plan pages)
 * @return
 */
function initialSearchPlanDownLoad() {
	$(".downloadImag").live("click",function(){
		var planId = $(this).attr("planId");
		$.ajax({
	        type: "POST",
	        url: $('#basePath').val()+"plan/getPlanAttachmentsById?planId="+planId,
	        data: {},
	        success: function(data) {
	        	$("#attachmentList").val(JSON.stringify(data));
	        	searchPlanViewAttachment();
	        	initialPopup();
	        }
	     });
	});
	initialClose();
}

var planAttachment;
function searchPlanViewAttachment() {
	if(planAttachment) {
		planAttachment.reloadAttachment($('#attachmentList').val());
		return;
	}
	var	attachmentFields = [
		    {
				fieldType:"text",
				fieldName:attachmentNameMS(),
				fieldKey:["planAttachmentName", "size", "createDateTime"],
				uploadKey:["name", "fileSize", "uploadTime"],
				fieldWidth:390
			},
			{
				fieldType:"download",
				fieldName:attachmentDownloadMS(),
				fieldWidth:135,
				downloadSetting:{
					realNameKey:"fileRealName",
					downloadPathKey:"fileFileName",
					nameKeyInJson:"planAttachmentName",
					pathKeyInJson:"planAttachmentPath"
				}
			}
	];
	planAttachment = $("#upload_attachment_list").attachmentUI({
		fields : attachmentFields,
		$attachmentContent : $("#upload_attachment_list"),
		maxLine : 3,
		isNeedUpload : false,
		downloadUrl:$('#basePath').val() + "attachment/downLoadAttachment",
		attachmentsJsonstr : $('#attachmentList').val()
	});
}

/**
 * setSearchPlanCriteria (used in search plan & my plan pages)
 * @return
 */
function setSearchPlanCriteria() {
	setQueryString();
	setSearchFields();
	// set searchFlag
	criteria.searchOperationFlag = "search";
	// set planTypeIds
	var planTypeIds = "";
	$("#planType").find(":checked").each(function() {
		if (planTypeIds == null || planTypeIds == "") {
			planTypeIds = this.value;
     } else {
     	planTypeIds = planTypeIds + " " + this.value;
     }
	});
	criteria.planTypeIds = planTypeIds;
	// set planCourseTypeIds
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
	criteria.planCourseTypeIds = typeIds;
	// set time
	criteria.publishLowerDate = $("#publishLowerDate").val();
	criteria.publishUpperDate = $("#publishUpperDate").val();
	criteria.executeLowerDate = $("#executeLowerDate").val();
	criteria.executeUpperDate = $("#executeUpperDate").val();
	
	// for search my plan page, set search condition cache
	if("undefined" != typeof operation_searchMyPlan && operation_searchMyPlan == 'searchMyPlan'){
		setSearchMyPlanCache();
	}
}

/**
 * for search my plan page, set search condition cache
 * @return
 */
function setSearchMyPlanCache() {
	if(roleClickFlag == master_contant){
		masterSearchCache = {
				pageNum : criteria.pageNum,
				pageSize : criteria.pageSize,
				sortSign : criteria.sortSign,
				sortName : criteria.sortName,
				queryString : criteria.queryString,
				searchFields : criteria.searchFields,
				planTypeIds : criteria.planTypeIds,
				planCourseTypeIds : criteria.planCourseTypeIds,
				publishLowerDate : criteria.publishLowerDate,
				publishUpperDate : criteria.publishUpperDate,
				executeLowerDate : criteria.executeLowerDate,
				executeUpperDate : criteria.executeUpperDate,
				searchOperationFlag : criteria.searchOperationFlag,
				isNoSelectedflag : isNoSelectedflag
		};
	}
	if(roleClickFlag == trainer_contant){
		trainerSearchCache = {
				pageNum : criteria.pageNum,
				pageSize : criteria.pageSize,
				sortSign : criteria.sortSign,
				sortName : criteria.sortName,
				queryString : criteria.queryString,
				searchFields : criteria.searchFields,
				planTypeIds : criteria.planTypeIds,
				planCourseTypeIds : criteria.planCourseTypeIds,
				publishLowerDate : criteria.publishLowerDate,
				publishUpperDate : criteria.publishUpperDate,
				executeLowerDate : criteria.executeLowerDate,
				executeUpperDate : criteria.executeUpperDate,
				searchOperationFlag : criteria.searchOperationFlag,
				isNoSelectedflag : isNoSelectedflag
		};
	}
	if(roleClickFlag == trainee_contant){
		traineeSearchCache = {
				pageNum : criteria.pageNum,
				pageSize : criteria.pageSize,
				sortSign : criteria.sortSign,
				sortName : criteria.sortName,
				queryString : criteria.queryString,
				searchFields : criteria.searchFields,
				planTypeIds : criteria.planTypeIds,
				planCourseTypeIds : criteria.planCourseTypeIds,
				publishLowerDate : criteria.publishLowerDate,
				publishUpperDate : criteria.publishUpperDate,
				executeLowerDate : criteria.executeLowerDate,
				executeUpperDate : criteria.executeUpperDate,
				searchOperationFlag : criteria.searchOperationFlag,
				isNoSelectedflag : isNoSelectedflag
		};
	}
}

/**
 * set status tootip
 * @return
 */
function intialStatusTooTip() {
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
}