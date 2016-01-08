$(document).ready(function(){
	initialConfirmDialog();
	initialErrorAlert();
	initialFilterDiv();
	initialKeywords();
	initialSearchButton();
	initialSort();
	initialCheckBox();
	initialPageSize();
	initialGo();
	initialPageControl();
	initialReset();
	initialSave();
    
    getAllCriteria();
    drawDataList();
    drawPageControl();
	drawPageSize();
});
/**
 * Info for query to search action.
 */
var criteria={
	"queryString" :"",
	"searchFields" : "",
	"roleTypes" : "",
	"sortField" :"",
	"sortSign" :"",
	"firstResult" :0,
	"pageNum" :1,
	"pageSize" :0
};
/**
 * Info for return from search action.
 */
var returnDataList={
	Height: 300,
	pageSizes: [],
	
	returnRealDataList:{
		count:0,
		totalPage:0,
		fieldsData:[],
		pageNow:1
	}
};
/**
 * Info for page size input from search common action.
 * It is used only in first fresh page.
 */
var pageSizesArray=[10];
/**
 * Info for changed checked number.
 */
var readChangedCheckNum = 0;

var pageNumberItemSize = 5;
/**
 * Initial the confirm dialog which jquery supports.
 * It is hiddle now.
 * @return
 */
function initialConfirmDialog(){
	$("#confirmDialog").dialog({ autoOpen: false });
}
/**
 * Initial the alert dialog which jquery supports.
 * Only config it.
 * @return
 */
function initialErrorAlert(){
	$("#alertForError").dialog({
		autoOpen: false,
		width: 350,
		modal: true,
		resizable: false,
		draggable: false,
		title: getBtnNotice(),
		buttons: [
		{
			text: getBtnYes(),
			id : "grayButton",
			click: function() {
				$(this).dialog("close");
			}
		}
		]
	});
}
/**
 * Initial the temporary confirm  dialog.
 * 1. Set some config of it.
 * 2. Open the confirm dialog.
 * @param mainVar
 * @param doWhat
 * @return
 */
function initialTemporaryConfirmDialog(mainVar,doWhat){
	if(readChangedCheckNum>0){
		initialConfirmBar(mainVar, doWhat);
	}else{
		doWhat();
	}
}
/**
 * Initial filterDiv by using the filter box plugin.
 * @return
 */
function initialFilterDiv(){
	$(".filterDiv").filterBox({
	});
}
/**
 * Initial keywords by binding event.
 * @return
 */
function initialKeywords(){
	if($("#keyword").val() != ""){
		$("#searchInputTipDiv").hide();
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
/**
 * Initial search button by binding event.
 * @return
 */
function initialSearchButton(){
	$("#searchButton").click(function(){
		//No use the selfChangedCriteria parameter for ajaxJump method.
		var selfChangedCriteria;
		//ajax jump.
		ajaxJump(selfChangedCriteria);
	});
    $("#keyword").keyup(function(event) {
    	if(event.keyCode == "13"){
    		var selfChangedCriteria;
    		ajaxJump(selfChangedCriteria);	     	
    		$(this).blur();
    	}
    });
}
/**
 * Initial sort bar by binding event.
 * @return
 */
function initialSort(){
	$(".dataList-div-head div").each(function(){
		if($(this).css("cursor")=="pointer"){
			$(this).click(function(){
				//Ready for sort special param.
				var selfDiv=$(this);
				//Ready for content of the confirm dialog.
				var content=$("#role_list_tip_for_other").html();
				//Initial temporary confirm dialog, use the jquery ui.
				initialTemporaryConfirmDialog(content,function(){
					/**
					 * This is main code of initial sort.
					 */
					//Change the kind of sort bar.
					drawSortBar(selfDiv);
					//No use the selfChangedCriteria parameter for ajaxJump method.
					var selfChangedCriteria;
					//ajax jump.
					ajaxJump(selfChangedCriteria);
				});
			});
		}
	});

}
/**
 * Initial check box by binding event.
 * It is used in draw data list method.
 * @return
 */
function initialCheckBox(){
	//the flag should be refresh.
	readChangedCheckNum=0;
	$(".Trainer,.Master,.Admin").find("span").click(function(){
		//Change the kind of check box.
		if($(this).hasClass("checked")){
			$(this).removeClass("checked");
			$(this).addClass("unchecked");
			checkIsOperated(this);
			
		}else if($(this).hasClass("unchecked")){
			$(this).removeClass("unchecked");
			$(this).addClass("checked");
			checkIsOperated(this);
		}
	});
}

function checkIsOperated(_this) {
	if ($(_this).hasClass("operated")) {
		$(_this).removeClass("operated");
		readChangedCheckNum = readChangedCheckNum - 1;
		return false;
	}
	$(_this).addClass("operated");
	readChangedCheckNum = readChangedCheckNum + 1;
}
/**
 * Initial pageSize input by binding event and ready for related data.
 * @return
 */
function initialPageSize(){
	//Get necessary data by ajax mode.
	var basePath=$("#basePath").val();
	var thePath=basePath+"searchCommon/searchCommon_findDataListInfo?searchFlag=0";
	$.post(
			thePath,
			{},
			function(data,status){
				pageSizesArray=data.pageSizesJson;
				//Use the pageSizesArray to update the page size options.
				drawPageSize();
				//Bing open to show event.
				$(".dataList-div-pageSize").hide();
				$(".dataList-div-pageSize-wrapper").mouseleave(function(){
					$(".dataList-div-pageSize").slideUp();
				});
				$(".dataList-a-pageSize").click(function(){
					$(".dataList-div-pageSize").slideToggle(250);
				});
				//Bing select event.
				$(".dataList-div-pageSize div").click(function(){
					//Ready for page size special param.
					var pageSize = $(this).html();
					//Ready for content of the confirm dialog.
					var content=$("#role_list_tip_for_other").html();
					//Initial temporary confirm dialog, use the jquery ui.
					initialTemporaryConfirmDialog(content,function(){
						/**
						 * This is main code of initial page size.
						 */
						//Change the kind of sort bar.
						$(".dataList-div-pageSize").slideUp();
						$(".dataList-a-pageSize").html(pageSize);
						//No use the selfChangedCriteria parameter for ajaxJump method.
						var selfChangedCriteria;
						//ajax jump.
						ajaxJump(selfChangedCriteria);
					});
				});
			},
			"json"
	);

}
/**
 * Initial go button by binding event.
 * @return
 */
function initialGo(){
	var goButton = $(".dataList-button-go");
	goButton.click(function(){
		//Check info of the go input is right frame.
		var pageValue=$(".dataList-input-go").val();
		if(isNaN(pageValue) || !parseInt(pageValue)){
			//The data is not int.
			$(".dataList-input-go").attr("class","dataList-input-go w30-h24 dataList-error");
			return false;
		}
		if (!returnDataList.returnRealDataList.totalPage) {
			$(".dataList-input-go").attr("class","dataList-input-go w30-h24 dataList-error");
			return false;
		}
		if (parseInt(pageValue) >= returnDataList.returnRealDataList.totalPage) {
			pageValue = returnDataList.returnRealDataList.totalPage;
			$(".dataList-input-go").val(pageValue);
		}
		//The data is right frame.
		//recovery the page input.
		$(".dataList-input-go").attr("class","dataList-input-go w30-h24");
		//Ready for content of the confirm dialog.
		var content=$("#role_list_tip_for_other").html();
		//Initial temporary confirm dialog, use the jquery ui.
		initialTemporaryConfirmDialog(content,function(){
			/**
			 * This is main code of initial go button.
			 */
			//Should give a parameter named selfChangedCriteria to the ajaxJump method by yourself.
			var selfChangedCriteria={"pageNum":pageValue};
			//ajax jump.
			ajaxJump(selfChangedCriteria);
		});
	});
	$(".dataList-input-go", ".dataList-div-go").keyup(function(event) {
    	if(event.keyCode == "13"){
    		goButton.click();
    	}
    });
}
/**
 * Initial page control by binding event.
 * @return
 */
function initialPageControl(){
	$(".dataList-div-page a").each(function(){
		var theClass=$(this).attr("class");
		//get necessary pageNum
		var pageNum=0;
		if(theClass=="dataList-a-first w-h-24"){
			pageNum=1;
		}else if(theClass=="dataList-a-previous w-h-24"){
			pageNum=parseInt(criteria.pageNum)-1;
		}else if(theClass=="dataList-a-next w-h-24"){
			pageNum=parseInt(criteria.pageNum)+1;
		}else if(theClass=="dataList-a-last w-h-24"){
			if(returnDataList.returnRealDataList==undefined||returnDataList.returnRealDataList.totalPage==undefined){
				pageNum=0;
			}else{
				pageNum=parseInt(returnDataList.returnRealDataList.totalPage);
			}
		}else if(theClass=="dataList-a-page"){
			pageNum=$(this).html();
		}
		//If the pageNum is right frame, bind the click event.
		if(pageNum>=1&&returnDataList.returnRealDataList!=undefined&&returnDataList.returnRealDataList.totalPage!=undefined&&pageNum<=returnDataList.returnRealDataList.totalPage){
			$(this).click(function(){
				//Ready for content of the confirm dialog.
				var content=$("#role_list_tip_for_other").html();
				//Initial temporary confirm dialog, use the jquery ui.
				initialTemporaryConfirmDialog(content,function(){
					/**
					 * This is main code of initial page control.
					 */
					//Should give a parameter named selfChangedCriteria to the ajaxJump method by yourself.
					var selfChangedCriteria={"pageNum":pageNum};
					//ajax jump.
					ajaxJump(selfChangedCriteria);
				});
			});
		}
	});
}
/**
 * Initial reset button
 */
function initialReset(){
	$("#resetButton").click(function(){
		//Ready for content of the confirm dialog.
		var content=$("#role_list_tip_for_reset").html();
		//Initial temporary confirm dialog, use the jquery ui.
		initialTemporaryConfirmDialog(content,function(){
			if(readChangedCheckNum>0){
				/**
				 * This is main code of initial reset button.
				 */
				var basePath=$("#basePath").val();
				var thePath=basePath+"roleList/searchRoleList";
				//Don't update the criteria
				//When the data not come back, should use a image to change it.
				$(".dataList-div-loader").attr("style","display: block;");
				//Ajax request and update the data list and page control, the sort bar can't be changed.
				$.post(
					thePath, 
					{
						"criteria.queryString" :criteria.queryString,
						"criteria.searchFields" : criteria.searchFields,
						"criteria.employeeRoleNames" : criteria.roleTypes,
						"criteria.sortField" : criteria.sortField,
						"criteria.reverse" : criteria.sortSign,
						"criteria.nowPage" : criteria.pageNum,
						"criteria.pageSize" : criteria.pageSize
					},
					function(data,status){
						if (status == "success") {
							//check whether the return data is error info.
							if(utilMethodForCheckReturnData(data)){
								$(".dataList-div-loader").attr("style","display: none;");
								return;
							}
							returnDataList.returnRealDataList.count = data.totalRecords;
							returnDataList.returnRealDataList.totalPage = data.totalPage;
							returnDataList.returnRealDataList.fieldsData = data.list;
							returnDataList.returnRealDataList.pageNow = data.nowPager;
							returnDataList.pageSizes = data.pageSize;
							drawDataList();
							drawPageControl();
							$(".dataList-div-loader").attr("style","display: none;");
						}
					},
					"json"
				);
			}
		});
	});
}
/**
 * Initial save button
 */
function initialSave(){
	$("#saveButton").click(function(){
		//Ready for content of the confirm dialog.
		var content=$("#role_list_tip_for_save").html();
		//Initial temporary confirm dialog, use the jquery ui.
		initialTemporaryConfirmDialog(content,function(){
			if(readChangedCheckNum>0){
				/**
				 * This is main code of initial save button.
				 */
				var basePath=$("#basePath").val();
				var thePath=basePath+"roleList/saveRoleList";
				//When the data not come back, should use a image to change it.
				$(".dataList-div-loader").attr("style","display: block;");
				$.post(
					thePath,
					getTemporaryArrayJobNumberAndRole(),
					function(data,status){
						if (status == "success") {
							//check whether the return data is error info.
							if(utilMethodForCheckReturnData(data)){
								$(".dataList-div-loader").attr("style","display: none;");
								return;
							}
							//If save function is success, refresh the page.
							thePath=basePath+"roleList/searchRoleList";
							//Don't update the criteria
							//Ajax request and update the data list and page control, the sort bar can't be changed.
							$.post(
								thePath, 
								{
									"criteria.queryString" :criteria.queryString,
									"criteria.searchFields" : criteria.searchFields,
									"criteria.employeeRoleNames" : criteria.roleTypes,
									"criteria.sortField" : criteria.sortField,
									"criteria.reverse" : criteria.sortSign,
									"criteria.nowPage" : criteria.pageNum,
									"criteria.pageSize" : criteria.pageSize
								},
								function(data,status){
									if (status == "success") {
										//check whether the return data is error info.
										if(utilMethodForCheckReturnData(data)){
											$(".dataList-div-loader").attr("style","display: none;");
											return;
										}
										returnDataList.returnRealDataList.count = data.totalRecords;
										returnDataList.returnRealDataList.totalPage = data.totalPage;
										returnDataList.returnRealDataList.fieldsData = data.list;
										returnDataList.returnRealDataList.pageNow = data.nowPager;
										returnDataList.pageSizes = data.pageSize;
										drawDataList();
										drawPageControl();
										$(".dataList-div-loader").attr("style","display: none;");
										drawMessageBar();
									}
								},
								"json"
							);
						}
					},
					"json"
				);
			}
		});
	});
}
/**
 * Jump by ajax mode.
 * @param selfChangedCriteria: If you hope adjust any info of criteria after get criteria, 
 * you should use this parameter which has same form as criteria.
 * @return
 */
function ajaxJump(selfChangedCriteria){
	//Use the criteria as request data to the action
	var basePath=$("#basePath").val();
	var thePath=basePath+"roleList/searchRoleList";
	//Update the criteria
	getAllCriteria();
	//If selfChangedCriteria has any info, Use the info to replace the related info of criteria.
	if(selfChangedCriteria){
		if(selfChangedCriteria.queryString){
			criteria.queryString = selfChangedCriteria.queryString;
		}
		if(selfChangedCriteria.searchFields){
			criteria.searchFields = selfChangedCriteria.searchFields;
		}
		if(selfChangedCriteria.roleTypes){
			criteria.roleTypes = selfChangedCriteria.roleTypes;
		}
		if(selfChangedCriteria.sortField){
			criteria.sortField = selfChangedCriteria.sortField;
		}
		if(selfChangedCriteria.sortSign){
			criteria.sortSign = selfChangedCriteria.sortSign;
		}
		if(selfChangedCriteria.pageNum){
			criteria.pageNum = selfChangedCriteria.pageNum;
		}
		if(selfChangedCriteria.pageSize){
			criteria.pageSize = selfChangedCriteria.pageSize;
		}
	}
	//When the data not come back, should use a image to change it.
	$(".dataList-div-loader").attr("style","display: block;");
	//Ajax request and update the data list and page control, the sort bar can't be changed.
	$.post(
		thePath, 
		{
			"criteria.queryString" :criteria.queryString,
			"criteria.searchFields" : criteria.searchFields,
			"criteria.employeeRoleNames" : criteria.roleTypes,
			"criteria.sortField" : criteria.sortField,
			"criteria.reverse" : criteria.sortSign,
			"criteria.nowPage" : criteria.pageNum,
			"criteria.pageSize" : criteria.pageSize
		},
		function(data,status){
			if (status == "success") {
				//check whether the return data is error info.
				if(utilMethodForCheckReturnData(data)){
					$(".dataList-div-loader").attr("style","display: none;");
					return;
				}
				returnDataList.returnRealDataList.count = data.totalRecords;
				returnDataList.returnRealDataList.totalPage = data.totalPage;
				returnDataList.returnRealDataList.fieldsData = data.list;
				returnDataList.returnRealDataList.pageNow = data.nowPager;
				returnDataList.pageSizes = data.pageSize;
				drawDataList();
				drawPageControl();
				$(".dataList-div-loader").attr("style","display: none;");
			}
		},
		"json"
	);
}
/**
 * Get all necessary criteria.
 */
function getAllCriteria(){
	getSearchFields();
	getRoleTypes();
	getQueryString();
	getSortFieldSortSign();
	getPageNum();
	getPageSize();
}
/**
 * Get searchFields from filter box in page to criteria(a js Object).
 * @return
 */
function getSearchFields() {
	var searchFields = "";
	var fields = $("#searchFields").find(":checked");
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

/**
 * Get roleTypes from filter box in page to criteria.
 */
function getRoleTypes() {
	var roleType = "";
	var fields = $("#roleType").find(":checked");
	if (fields.length > 0) {
		fields.each(function() {
			if (this.value == "all") {
				roleType = roleType;
			} else {
				if (roleType == null || roleType == "") {
					roleType = this.value;
		        } else {
		        	roleType = roleType + "," + this.value;
		        }
			}
			});
	}
	criteria.roleTypes = roleType;
}
/**
 * Get queryString from input tag in page to criteria.
**/
function getQueryString(){
	var reference=$("#roleListTip").val();
	var queryString = $("#keyword").val();
	if (queryString == reference) {
		queryString = "";
	}
	criteria.queryString = queryString;
}
/**
 * Get sort field and sort sign from input tag in page to criteria.
 */
function getSortFieldSortSign(){
	var tableHeaderDoms = $(".dataList-div-data .dataList-div-head div");
	var sortHeaderName="";
	var sortHeaderSign = false;
	tableHeaderDoms.each(function(){
		var sortHeaderSpanDoms = $(this).children("span");
		if (sortHeaderSpanDoms.length == 2) {
			sortHeaderName = $(this).children(':eq(0)').attr("en");
			var sortHeaderSigns = $(this).children(':eq(1)').attr("class").split(" ");
			if(sortHeaderSigns.length>=2){
				sortHeaderSign=sortHeaderSigns[1];
			}
			if(sortHeaderSign=="dataList-asc"){
				sortHeaderSign=true;
			}else{
				sortHeaderSign=false;
			}
		}
	});
	criteria.sortField=sortHeaderName;
	criteria.sortSign=sortHeaderSign;
}
/**
 * Get page num from input tag in page to criteria.
 * @return
 */
function getPageNum(){
	var tablePageDoms = $(".currentPage");
	var pageNum=1;
	if(tablePageDoms.length>=1){
		pageNum=tablePageDoms.first().html();
	}
	criteria.pageNum=pageNum;
}
/**
 * Get page size from input tag in page to criteria.
 * @return
 */
function getPageSize(){
	var tablePageSizeDoms = $(".dataList-a-pageSize");
	var pageSize=0;
	if(tablePageSizeDoms.length>=1){
		pageSize=tablePageSizeDoms.html();
	}
	criteria.pageSize=pageSize;
}
/**
 * Get the temporary info about job number of employee and role.
 * @return : a array for the temporary info.
 */
function getTemporaryArrayJobNumberAndRole(){
	var temporaryArray=new Array();
	$(".dataList-div-body .dateList-div-record").each(function(){
		var temporaryRecord={"jobNum":"","trainer":"","master":"","admin":""};
		temporaryRecord.jobNum=$(this).attr('id');
		if($(this).find(".Trainer span").hasClass("checked")){
			temporaryRecord.trainer="1";
		}else{
			temporaryRecord.trainer="0";
		}
		if($(this).find(".Master span").hasClass("checked")){
			temporaryRecord.master="1";
		}else{
			temporaryRecord.master="0";
		}
		if($(this).find(".Admin span").hasClass("checked")){
			temporaryRecord.admin="1";
		}else{
			temporaryRecord.admin="0";
		}
		temporaryArray.push(temporaryRecord);
	});
	return transformArrayToProperJson(temporaryArray);
}
/**
 * Because the ognl form of struts is different from the json object form.
 * We should transform the array by ourselves.
 * This method is only used for getTemporaryArrayJobNumberAndRole method.
 * @param theArray
 * @return
 */
function transformArrayToProperJson(theArray){
	var result='{';
	for(var i=0;i<theArray.length;i++){
		var roleType="";
		if(theArray[i].trainer=="1"){
			roleType=roleType+"Trainer";
		}
		if(theArray[i].master=="1"){
			roleType=roleType+",Training Master";
		}
		if(theArray[i].admin=="1"){
			roleType=roleType+",Admin";
		}
		result=result+'"jobNumAndRoles['+i+'].jobNumber":"'+theArray[i].jobNum+'","jobNumAndRoles['+i+'].role":"'+roleType+'"';
		result=result+",";
	}
	result=result.substring(0, result.length-1);
	result=result+'}';
	return eval('('+ result +')');
}
/**
 * Refresh all elements of page.
 * @return
 */
function drawAllElement(){
}
/**
 * Update the dataList of page.
 */
function drawDataList(){
	var recordsParent=$(".dataList-div-body");
	recordsParent.empty();
	if(returnDataList.returnRealDataList.count){
		var count=returnDataList.returnRealDataList.count;
		if(count>=1){
			var fieldsData=returnDataList.returnRealDataList.fieldsData;
			for(var i=0;i<criteria.pageSize&&i<fieldsData.length;i++){
				var id = (returnDataList.returnRealDataList.pageNow - 1) * returnDataList.pageSizes;
				var theRecordHtml='<div class="dateList-div-record" id='+fieldsData[i].jobNumber+'>'+
				'<div class="w-30" style="width: 28px">'+(id + i + 1)+'</div>'+
				'<div class="ml-2 ID" style="width: 150px;text-align:center">'+fieldsData[i].jobNumber+'</div>'+
				'<div class="ml-2 Name" style="width: 200px;text-align:center">'+fieldsData[i].name+'</div>'+
				'<div class="ml-2 Department" style="width: 300px;text-align:center">'+fieldsData[i].email+'</div>';
				
				var trainerHtml="";
				var masterHtml="";
				var adminHtml="";
				var roleArray = fieldsData[i].role.split(",");
				if($.inArray("Trainer",roleArray) != -1){
					trainerHtml='<span class="checked"></span>';
				}else{
					trainerHtml='<span class="unchecked"></span>';
				}
				if($.inArray("Training Master",roleArray) != -1){
					masterHtml='<span class="checked"></span>';
				}else{
					masterHtml='<span class="unchecked"></span>';
				}
				if($.inArray("Admin",roleArray) != -1){
					adminHtml='<span class="checked"></span>';
				}else{
					adminHtml='<span class="unchecked"></span>';
				}
				theRecordHtml=theRecordHtml+'<div class="ml-2 Trainer" style="width: 68px;text-align:center">'+trainerHtml+'</div>'+
				'<div class="ml-2 Master" style="width: 68px;text-align:center">'+masterHtml+'</div>'+
				'<div class="ml-2 Admin" style="width: 70px;text-align:center">'+adminHtml+'</div>'+
				'</div>';
				recordsParent.append(theRecordHtml);
			}
		}
	}
	//note: need bind the click event for each check box.
	initialCheckBox();
	//note: need bind the blur event for each div which is low for this text.
	//If the div's content size is bigger than the div size, the div should use the poshytip and bind the mouseenter event.
	drawPoshytipForBeyond();
}
/**
 * Update the page control of page.
 * @return
 */
function drawPageControl(){	
	//Draw page show div, include total count number and the page size .
	var pageShowParent=$(".dataList-div-perPage");
	pageShowParent.empty();
	if(returnDataList.returnRealDataList.count){
		var allcount=returnDataList.returnRealDataList.count;
		var nowcount=returnDataList.returnRealDataList.fieldsData.length;
		var firstId= (returnDataList.returnRealDataList.pageNow - 1) * returnDataList.pageSizes + 1;
		lastId=parseInt(firstId)+parseInt(nowcount) - 1;
		pageShowParent.append('<span>'+firstId+'</span> - <span>'+lastId+'</span> of <span>'+allcount+'</span>');
	}else{
		pageShowParent.append('<span>0</span> - <span>0</span> of <span>0</span>');
	}
	
	//Draw page control <a> tag. 
	var pageControlParent=$(".dataList-div-page");
	pageControlParent.empty();
	if(returnDataList.returnRealDataList.count){
		var totalPage=parseInt(returnDataList.returnRealDataList.totalPage);
		var pageNum=parseInt(criteria.pageNum);
		var thePageControlHtml="";
		var middleItemSize = parseInt(pageNumberItemSize / 2);
		var startIndex = 1;
        var endIndex = totalPage;
        if (totalPage > pageNumberItemSize){
        	if (pageNum <= middleItemSize){
        		endIndex = pageNumberItemSize;
        	}else if (pageNum + middleItemSize >= totalPage){
        		startIndex = totalPage - pageNumberItemSize + 1;
        		endIndex = totalPage;
        	}else {
        		startIndex = pageNum - middleItemSize;
        		endIndex = pageNum + middleItemSize;
        	}
        }
        if (pageNum == 1){
    		thePageControlHtml='<a href="javascript:;" class="dataList-a-first-disable w-h-24"></a>'+
			'<a href="javascript:;" class="dataList-a-previous-disable w-h-24"></a>';
    	}else {
    		thePageControlHtml='<a href="javascript:;" class="dataList-a-first w-h-24"></a>'+
			'<a href="javascript:;" class="dataList-a-previous w-h-24"></a>';
    	}
        for (var i = startIndex; i <= endIndex; i++){
        	if(i==pageNum){
				thePageControlHtml=thePageControlHtml+'<a href="javascript:;" class="dataList-a-page currentPage">'+i+'</a>';
			}else{
				thePageControlHtml=thePageControlHtml+'<a href="javascript:;" class="dataList-a-page">'+i+'</a>';
			}
        }
        
        if(!totalPage || pageNum == totalPage){
			thePageControlHtml=thePageControlHtml+'<a href="javascript:;" class="dataList-a-next-disable w-h-24"></a>'+
			'<a href="javascript:;" class="dataList-a-last-disable w-h-24"></a>';
		}else{
			thePageControlHtml=thePageControlHtml+'<a href="javascript:;" class="dataList-a-next w-h-24"></a>'+
			'<a href="javascript:;" class="dataList-a-last w-h-24"></a>';
		}
		pageControlParent.append(thePageControlHtml);
	}
	
	//note: need bind the click event for each page number(except now page number).
	initialPageControl();
}
/**
 * Update the sort bar of page.
 * @param theSelfDiv
 * @return
 */
function drawSortBar(theSelfDiv){
	//The sort bar which is not clicked should be recovery.
	$(".dataList-div-head div").not(theSelfDiv).each(function(){
		var theOtherClass=$(this).attr("class");
		if(theOtherClass!=undefined&&theOtherClass.indexOf("dataList-div-sort-style")>=0&&$(this).css("cursor")=="pointer"){
			$(this).children("span:eq(1)").remove();
			theOtherClass=theOtherClass.replace("dataList-div-sort-style","");
			$(this).attr("class",theOtherClass);
		}
	});
	
	//The sort bar which is clicked should be marked.
	var thisClass=theSelfDiv.attr("class");
	if(thisClass!=undefined&&thisClass.indexOf("dataList-div-sort-style")>=0){
		var thisSpanClass=theSelfDiv.find("span:eq(1)").attr("class");
		if(thisSpanClass=="dataList-span-sort dataList-desc"){
			theSelfDiv.find("span:eq(1)").attr("class","dataList-span-sort dataList-asc");
			return "asc";
		}else{
			theSelfDiv.find("span:eq(1)").attr("class","dataList-span-sort dataList-desc");
			return "desc";
		}
	}else{
		if(thisClass==undefined){
			theSelfDiv.attr("class","dataList-div-sort-style");
		}else{
			theSelfDiv.attr("class",thisClass+" dataList-div-sort-style");
		}
		theSelfDiv.find("span").after('<span class="dataList-span-sort dataList-desc"></span>');
		return "desc";
	}
	return "desc";
}
/**
 * Update the page Size select options of page.
 * @return
 */
function drawPageSize(){
	var pageSizeParent=$(".dataList-div-pageSize");
	pageSizeParent.empty();
	for(var i=0;i<pageSizesArray.length;i++){
		pageSizeParent.append('<div>'+pageSizesArray[i]+'</div>');
	}
}
/**
 * Show the message bar.
 * Only be used after save role list.
 * @return
 */
function drawMessageBar(){
	$("#messageBar").messageBar({
		responseMessage:$("#messageBar_save").html(),
		top:100
	});
}
/**
 * Draw the tip for prompting beyond the mark in div.
 * Only be used after draw the data list.
 * @return
 */
function drawPoshytipForBeyond(){
	$(".dataList-div-body .dateList-div-record div").each(function(){
		//If <div>'s content size is bigger than <div>'s size(that is beyond the mark).
		if($(this)[0].scrollWidth>$(this).width()){
			//Use the poshytip made by jquery.
			$(this).poshytip({
				allowTipHover : true ,
				className: 'tip-green',
				content: $(this).html()
			});
		}
	});
}
/**
 * Use the function to check whether the return data is error information
 * @return
 */
function utilMethodForCheckReturnData(data){
	if(typeof(data.errorMessageKey)!="undefined"&&typeof(data.logMessage)!="undefined"){
		//data is error info to replace the normal info.
		if(data.flag=="1"){
			//Should jump to error page.
			var basePath=$("#basePath").val();
			var thePath=basePath+"error_500";
			window.location.href=thePath;
		}else{
			$("#alertForError").messageBar({
				responseMessage:data.errorMessageKey,
				top:100
			});
			$("#alertForError").dialog("open");
		}
		return true;
	}else{
		return false;
	}
}