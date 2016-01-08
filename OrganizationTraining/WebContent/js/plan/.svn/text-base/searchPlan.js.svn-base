$(document).ready(function(){
	initialCriteriaParam();
	initialKeywords();
    $(".dateInput").datepicker({
    	changeMonth: true,
    	changeYear: true,
    	dateFormat: "yy-mm-dd"
    });
    findCourseTypes();
    findDataListInfo(2);// 2 is plan
    $("#searchButton").click(function() {
    	setSearchPlanCriteria();
    	dataList.criteria = criteria;
		criteria.sortName = null;
        dataList.search();
    });
    $("#keyword").keyup(function(event) {
    	if(event.keyCode == "13"){
    		setSearchPlanCriteria();
    		dataList.criteria = criteria;
    		criteria.sortName = null;
	        dataList.search();
	     	$(this).blur();
    	}
    });
	// message bar
	var operationFlag = $('#operationFlag').val();
	var prefixIDValue = $('#plan_prefixIDValue').val();
	var messageBar_delete = $('#messageBar_delete').html();
	var messageBar_cancel = $('#messageBar_cancel').html();
	if (operationFlag == "delete") {
		$("#messageBar").messageBar({
			isPrepositionId: true,
			responseMessage: ": "+messageBar_delete,
    	    itemId: prefixIDValue,
    	    top: 100
		});
	}
	if (operationFlag == "cancel") {
		$("#messageBar").messageBar({
			isPrepositionId: true,
			responseMessage: ": "+messageBar_cancel,
			itemId: prefixIDValue,
    	    top: 100
		});
	}
	initialViewDetail();
	initialSearchPlanDownLoad();
});	

//A flag for complete info from view detail page to search page
var hasCondition = document.getElementById('_hasCondition').value;
/**
 * If select fields of filter box hasn'e been selected, the flag is 1.
 * Or the flag is 0.
 */
var isNoSelectedflag = 0;
var criteriaParam;
// search list
var dataList;
var criteria = {
    pageNum : 1,
    pageSize : 0
};
var dataListInfo = {
	columns:[
	         {EN:'ID', ZH:'编号', sortName:'prefix_id', width:70, isMustShow:true},
	         {EN:'Name', ZH:'名称', sortName:'plan_name', width:100, isMustShow:true, align : 'left'},
	         {EN:'Brief', ZH:'简介', width:200, autoWidth: true, align : 'left'},
	         {EN:'Type', ZH:'类别', sortName:getI18NSortField('planType_type_name_sort'), width:100},
	         {EN:'Tag', ZH:'标签', sortName:'plan_tag', width:100, align : 'left'},
	         {EN:'Date', ZH:'日期', width:150},
	         {EN:'Creator', ZH:'创建者', sortName:'plan_creator', width:90},
	],
	language: configI18n(),
	criteria: criteria,
	minHeight: 150,
	pageSizes: [],
	hasAttachmentIcon: true,
	url: $('#basePath').val()+'plan/searchPlan',
	updateShowField: {
		url: $('#basePath').val()+'searchCommon/searchCommon_updateShowFields?searchFlag=2',// 2 is plan
		callback: function(data){
			if(data && data.error){
				alert("save fail!");
			}
		}
	},
	updateShowSize: {
		url: $('#basePath').val()+'searchCommon/searchCommon_updateShowSize?searchFlag=2',// 2 is plan
		callback: function(data){
			if(data && data.error){
				alert("save fail!");
			}
		}
	},
	contentHandler : function(str){
		return resultContentHandle(str);
    }
};
function selectList(){
	dataList = $(".dataList").DataList(dataListInfo);
}
