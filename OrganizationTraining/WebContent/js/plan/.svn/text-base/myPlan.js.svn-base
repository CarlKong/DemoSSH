var admin_contant = "Admin";
var master_contant = "Training Master";
var trainer_contant = "Trainer";
var trainee_contant = "Trainee";
var operation_searchMyPlan = 'searchMyPlan';
var roleClickFlag;
var masterSearchCache;
var trainerSearchCache;
var traineeSearchCache;
var isNoSelectedflag = 0;

$(document).ready(function(){
	// search part function
	initialKeywords();
	$(".dateInput").datepicker({
    	changeMonth: true,
    	changeYear: true,
    	dateFormat: "yy-mm-dd"
    });
	findCourseTypes();
    findDataListInfo(2);// 2 is plan
    $("#searchButton").click(function() {
	    criteria.sortName = null;
    	setSearchPlanCriteria();
        dataList.search();
    });
    $("#keyword").keyup(function(event) {
    	if(event.keyCode == "13"){
    		setSearchPlanCriteria();
    		criteria.sortName = null;
	        dataList.search();
	     	$(this).blur();
    	}
    });
    initialSearchPlanDownLoad();
    initialViewMyPlanDetail();
    // tab part function 
	initialTab();
});

//search list
var dataList;
var criteria = {
    pageNum : 1,
    pageSize : 0,
	sortName: ""
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
	hasStatus: true,
	url: $('#basePath').val()+'plan/searchMyPlan',
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

/**
 * initialization myplan header tab
 * 1. get roleFlag from session, and set tab
 * 2. from dashboard, get rolFlag, to choose tab
 * @return
 */
function initialTab() {
	var tabObjs = [ {
		id : "master_tab",
		name : $('#tabName_master').val()
	}, {
		id : "trainer_tab",
		name : $('#tabName_trainer').val()
	}, {
		id : "trainee_tab",
		name : $('#tabName_trainee').val()
	} ];
	var tabObjs2 = [ {
		id : "trainer_tab",
		name : $('#tabName_trainer').val()
	}, {
		id : "trainee_tab",
		name : $('#tabName_trainee').val()
	} ];
	//get session
	var roleNames=$('#roleNames').val();
	var hasMaster = false;
	if(roleNames.indexOf(master_contant) >= 0 || roleNames.indexOf(admin_contant) >= 0) {
		hasMaster = true;
	}
	// from dashboard, get url role
	var roleFlag = getUrlParam("roleFlag");
	if (roleFlag != null) {
		roleClickFlag = roleFlag;
		if (roleFlag == master_contant) {
			initTab(960, 110, tabObjs, $("#myPlan_tab"), 1);
			criteria.roleFlag = master_contant;
		}
		if (roleFlag == trainer_contant) {
			if (hasMaster) {
				initTab(960, 110, tabObjs, $("#myPlan_tab"), 2);
			} else {
				initTab(960, 110, tabObjs2, $("#myPlan_tab"), 1);
			}
			criteria.roleFlag = trainer_contant;
		}
		if (roleFlag == trainee_contant) {
			if (hasMaster) {
				initTab(960, 110, tabObjs, $("#myPlan_tab"), 3);
			} else {
				initTab(960, 110, tabObjs2, $("#myPlan_tab"), 2);
			}
			criteria.roleFlag = trainee_contant;
		}
	} else {
		if (hasMaster) {
			roleClickFlag = master_contant;
			initTab(960, 110, tabObjs, $("#myPlan_tab"), 1);
			criteria.roleFlag = master_contant;
		} else {
			roleClickFlag = trainer_contant;
			initTab(960, 110, tabObjs2, $("#myPlan_tab"), 1);
			criteria.roleFlag = trainer_contant;
		}
	}
	// click tab, if search condition cache is not null, set page's search condition
	$("#master_tab").bind("click", function() {
		roleClickFlag = master_contant;
		clickOptionTab($(this), $("#myPlan_tab"));
		clearSearchCondition();
		if(masterSearchCache != undefined){
			drawSearchCache();
		}
		criteria.roleFlag = master_contant;
		searchMyPlan();
	});
	$("#trainer_tab").bind("click", function() {
		roleClickFlag = trainer_contant;
		clickOptionTab($(this), $("#myPlan_tab"));
		clearSearchCondition();
		if(trainerSearchCache != undefined){
			drawSearchCache();
		}
		criteria.roleFlag = trainer_contant;
		searchMyPlan();
	});
	$("#trainee_tab").bind("click", function() {
		roleClickFlag = trainee_contant;
		clickOptionTab($(this), $("#myPlan_tab"));
		clearSearchCondition();
		if(traineeSearchCache != undefined){
			drawSearchCache();
		}
		criteria.roleFlag = trainee_contant;
		searchMyPlan();
	});
}

function searchMyPlan() {
	setSearchPlanCriteria();
    dataList.search();
}

function clearSearchCondition() {
	$('#keyword').val('');
	initialKeywords();
	$('.reset').click();
}

/**
 * get search cache and draw page search condition
 * @return
 */
function drawSearchCache() {
	if(roleClickFlag == master_contant){
		criteriaParam = masterSearchCache;
	}
	if(roleClickFlag == trainer_contant){
		criteriaParam = trainerSearchCache;
	}
	if(roleClickFlag == trainee_contant){
		criteriaParam = traineeSearchCache;
	}
	drawPage();
}

/**
 * to get param from url
 * @param name
 * @return
 */
function getUrlParam(name){      
    var reg = new RegExp("(^|&)"+name+"=([^&]*)(&|$)");      
    var r = window.location.search.substr(1).match(reg);      
    if (r!=null)
    	return unescape(r[2]);   
    return null;      
}

 
/**
 * view my plan detail, turn to plan detail page
 * @return
 */
function initialViewMyPlanDetail() {
	$('.viewDetail').live('click', function(){
		var planId = $(this).attr("planId");
		window.location.href = $("#basePath").val() + "plan/viewPlanDetail?planId=" + planId;
	});
}