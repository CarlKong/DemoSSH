var trainer_contant = "Trainer";
var trainee_contant = "Trainee";
var operation_searchMyCourse = 'searchMyCourse';
var roleClickFlag;
var trainerSearchCache;
var traineeSearchCache;
var isNoSelectedflag = 0;
var lastClicked = "Trainer"; 

$(document).ready(function(){
	// search part function
	initialKeywords();
	$(".dateInput").datepicker({
    	changeMonth: true,
    	changeYear: true,
    	dateFormat: "yy-mm-dd"
    });
	findCourseTypes();
    findDataListInfo(0);// 0 is course
    $("#searchButton").click(function() {
    	setSearchMyCourseCriteria();
    	criteria.sortName = null;
        dataList.search();
    });
    $("#keyword").keyup(function(event) {
    	if(event.keyCode == "13"){
    		setSearchMyCourseCriteria();
    		criteria.sortName = null;
	        dataList.search();
	     	$(this).blur();
    	}
    });
    initialSearchMyCourseDownLoad();
    initialViewMyCourseDetail();
    // tab part function 
	initialTab();
});

//search list
var dataList;
var criteria = {
    pageNum : 1,
    pageSize : 0,
    divideByStauts : 0
};
var dataListInfo = {
	columns:[
			 {EN:'ID', ZH:'编号', sortName:'prefix_id', width:70, isMustShow:true},
             {EN:'Name', ZH:'名称', sortName:'name_sort', width:140, isMustShow:true, align : 'left'},
             {EN:'Brief', ZH:'简介', width:170, autoWidth: true, isMustShow:true, align : 'left'},
             {EN:'Date', ZH:'日期', sortName:'start_datetime',width:90, isMustShow:true},
             {EN:'Time', ZH:'时间', isMustShow:true, width:90},
             {EN:'Room', ZH:'房间', isMustShow:true,width:80},
             {EN:'Plan ID', ZH:'计划编号',isMustShow:true, width:80},
             {EN:'Plan Master', ZH:'培训主管', sortName:'',isMustShow:true, width:100},
         ],
	backendFieldName : 'actualCourseCriteria',
	language: configI18n(),
	criteria: criteria,
	minHeight: 150,
	pageSizes: [],
	hasAttachmentIcon: true,
	hasStatus: true,
	sortHander: function(){criteria.divideByStauts = 1;},
	url: $('#basePath').val()+'plan/searchMyCourse',
	updateShowField: {
		url: $('#basePath').val()+'searchCommon/searchCommon_updateShowFields?searchFlag=0',// 0 is course
		callback: function(data){
			if(data && data.error){
				alert("save fail!");
			}
		}
	},
	updateShowSize: {
		url: $('#basePath').val()+'searchCommon/searchCommon_updateShowSize?searchFlag=0',// 0 is course
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
		id : "trainer_tab",
		name : $('#tabName_trainer').val()
	}, {
		id : "trainee_tab",
		name : $('#tabName_trainee').val()
	} ];

	// from dashboard, get url role
	var roleFlag = getUrlParam("roleFlag");
	if (roleFlag != null) {
		roleClickFlag = roleFlag;
		if (roleFlag == trainer_contant) {
				initTab(960, 110, tabObjs, $("#myCourse_tab"), 1);
			criteria.roleFlag = trainer_contant;
		}
		if (roleFlag == trainee_contant) {
				initTab(960, 110, tabObjs, $("#myCourse_tab"), 2);
			criteria.roleFlag = trainee_contant;
		}
	} else {
			roleClickFlag = trainer_contant;
			initTab(960, 110, tabObjs, $("#myCourse_tab"), 1);
			criteria.roleFlag = trainer_contant;
	}
	// click tab, if search condition cache is not null, set page's search condition
	$("#trainer_tab").bind("click", function() {
		roleClickFlag = trainer_contant;
		if(lastClicked == "Trainee"){
			setSearchMyCourseCache();
		}
		clickOptionTab($(this), $("#myCourse_tab"));
		clearSearchCondition();
		if(trainerSearchCache != undefined){
			drawSearchCache();
		}
		criteria.roleFlag = trainer_contant;
		searchMyCourse();
		lastClicked = "Trainer";
	});
	$("#trainee_tab").bind("click", function() {
		roleClickFlag = trainee_contant;
		if(lastClicked == "Trainer"){
			setSearchMyCourseCache();
		}
		clickOptionTab($(this), $("#myCourse_tab"));
		clearSearchCondition();
		if(traineeSearchCache != undefined){
			drawSearchCache();
		} else {
			criteria.pageNum = 1;
			criteria.divideByStauts = 0;
			criteria.sortSign = "desc";
			criteria.sortName = "prefix_id";
		}
		criteria.roleFlag = trainee_contant;
		searchMyCourse();
		lastClicked = "Trainee";
	});
}

function searchMyCourse() {
	setSearchMyCourseCriteria();
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
	if(roleClickFlag == trainer_contant){
		criteriaParam = trainerSearchCache;
		criteria.pageNum = trainerSearchCache.pageNum;
		criteria.sortSign = trainerSearchCache.sortSign;
		criteria.sortName = trainerSearchCache.sortName;
	}
	if(roleClickFlag == trainee_contant){
		criteriaParam = traineeSearchCache;
		criteria.pageNum = traineeSearchCache.pageNum;
		criteria.sortSign = traineeSearchCache.sortSign;
		criteria.sortName = traineeSearchCache.sortName;
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
 * view my course detail, turn to course detail page
 * @return
 */
function initialViewMyCourseDetail() {
	$('.viewDetail').live('click', function(){
		var planCourseId = $(this).attr("planCourseId");
		window.location.href = $("#basePath").val() + "plan/findActualCourseById?actualCourseId=" + planCourseId;
	});
}

function initialSearchMyCourseDownLoad() {
		$(".downloadImag").live("click",function(){
			var planCourseId = $(this).attr("planCourseId");
			$.ajax({
		        type: "POST",
		        url: $('#basePath').val()+"plan/getActualCourseAttachmentsById?actualCourseId="+planCourseId,
		        data: {},
		        success: function(data) {
		        	$("#attachmentList").val(JSON.stringify(data));
		        	viewMyCourseAttachment();
		        	initialPopup();
		        }
		     });
		});
		initialClose();
}
var myCourseAttachment;
function viewMyCourseAttachment() {
	if(myCourseAttachment) {
		myCourseAttachment.reloadAttachment($('#attachmentList').val());
		return;
	}
	attachmentFields = [
		{
			fieldType:"text",
			fieldName:attachmentNameMS(),
			fieldKey:["actualCoursAttachmentName", "size", "createDateTime"],
			uploadKey:["name", "fileSize", "uploadTime"],
			fieldWidth:360
		},
		{
			fieldType:"selectResult",
			fieldName:attachmentPublicMS(),
			fieldKey:["actualCourseAttachmentVisible"],
			fieldWidth:60
		},
		{
			fieldType:"download",
			fieldName:attachmentDownloadMS(),
			fieldWidth:105,
			downloadSetting:{
				realNameKey:"fileRealName",
				downloadPathKey:"fileFileName",
				nameKeyInJson:"actualCoursAttachmentName",
				pathKeyInJson:"actualCourseAttachmentPath"
			}
		}	                    
	];	        	
	myCourseAttachment = $("#upload_attachment_list").attachmentUI({
		fields:attachmentFields,
		$attachmentContent : $("#upload_attachment_list"),
		downloadUrl:$('#basePath').val() + "attachment/downLoadAttachment",
		attachmentsJsonstr : $('#attachmentList').val()
	});
}

function setSearchMyCourseCriteria() {
	setQueryString();
	setSearchFields();
	//setSearchMyCourseCache();
}

/**
 * for search my plan page, set search condition cache
 * @return
 */
function setSearchMyCourseCache() {
	if(roleClickFlag == trainer_contant){
		traineeSearchCache = {
				pageNum : criteria.pageNum,
				pageSize : criteria.pageSize,
				sortSign : criteria.sortSign,
				sortName : criteria.sortName,
				queryString : criteria.queryString,
				searchFields : criteria.searchFields,
				divideByStauts : criteria.divideByStauts
		};
	}
	if(roleClickFlag == trainee_contant){
		trainerSearchCache = {
				pageNum : criteria.pageNum,
				pageSize : criteria.pageSize,
				sortSign : criteria.sortSign,
				sortName : criteria.sortName,
				queryString : criteria.queryString,
				searchFields : criteria.searchFields,
				divideByStauts : criteria.divideByStauts
		};
	}
}

/**
 * for search my plan page, set search condition cache
 * @return
 */
function getSearchMyCourseCache() {
	if(roleClickFlag == trainer_contant){
		criteriaParam = {
				pageNum : trainerSearchCache.pageNum,
				pageSize : trainerSearchCache.pageSize,
				sortSign : trainerSearchCache.sortSign,
				sortName : trainerSearchCache.sortName,
				queryString : trainerSearchCache.queryString,
				searchFields : trainerSearchCache.searchFields,
				divideByStauts : trainerSearchCache.divideByStauts
		};
		criteria.pageNum = trainerSearchCache.pageNum;
		criteria.sortSign = trainerSearchCache.sortSign;
		criteria.sortName = trainerSearchCache.sortName;
	}
	if(roleClickFlag == trainee_contant){
		criteriaParam = {
				pageNum : traineeSearchCache.pageNum,
				pageSize : traineeSearchCache.pageSize,
				sortSign : traineeSearchCache.sortSign,
				sortName : traineeSearchCache.sortName,
				queryString : traineeSearchCache.queryString,
				searchFields : traineeSearchCache.searchFields,
				divideByStauts : traineeSearchCache.divideByStauts
		};
		criteria.pageNum = traineeSearchCache.pageNum;
		criteria.sortSign = traineeSearchCache.sortSign;
		criteria.sortName = traineeSearchCache.sortName;
	}
}