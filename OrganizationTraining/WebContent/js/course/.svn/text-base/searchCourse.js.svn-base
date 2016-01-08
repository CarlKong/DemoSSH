var operationFlag = $('#operationFlag').val();
var prefixIDValue = $('#prefixIDValue').val();
	
$(document).ready(function(){
	initialCriteriaParam();
	initialKeywords();
    findCourseTypes();
    findDataListInfo(0);// 0 is course
    $("#searchButton").click(function() {
    	setCriteria();
    	dataList.criteria = criteria;
		criteria.sortName = null;
        dataList.search();
    });
    $("#keyword").keyup(function(event) {
    	if(event.keyCode == "13"){
	    	setCriteria();
	    	dataList.criteria = criteria;
    		criteria.sortName = null;
	        dataList.search();
	     	$(this).blur();
    	}
    });
    initialViewDetail();
    initialDownLoad();
    // message bar
	if (operationFlag == "delete") {
		$("#messageBar").messageBar({
			isPrepositionId: true,
			responseMessage: ": "+getCourseDeleteMessageBar(),
    	    itemId: prefixIDValue,
    	    top: 100
		});
	}
});	
// set down load
function initialDownLoad() {
	$(".downloadImag").live("click",function(){
		var courseId = $(this).attr("courseId");
		$.ajax({
	        type: "POST",
	        url: $('#basePath').val()+"course/getCourseAttachmentsById?courseId="+courseId,
	        data: {},
	        success: function(data) {
	        	$("#attachmentList").val(JSON.stringify(data));
	        	searchCourseViewAttachment();
	        	initialPopup();
	        }
	     });
	});
	initialClose();
}
var courseAttachment;
function searchCourseViewAttachment() {
	if(courseAttachment) {
		courseAttachment.reloadAttachment($('#attachmentList').val());
		return;
	}
	var	attachmentFields = [
		    {
				fieldType:"text",
				fieldName:attachmentNameMS(),
				fieldKey:["courseAttachmentName", "size", "createDateTime"],
				uploadKey:["name", "fileSize", "uploadTime"],
				fieldWidth:360
			},
			{
				fieldType:"selectResult",
				fieldName:attachmentPublicMS(),
				fieldKey:["courseAttachmentvisible"],
				fieldWidth:60
			},
			{
				fieldType:"download",
				fieldName:attachmentDownloadMS(),
				fieldWidth:105,
				downloadSetting:{
					realNameKey:"fileRealName",
					downloadPathKey:"fileFileName",
					nameKeyInJson:"courseAttachmentName",
					pathKeyInJson:"courseAttachmentPath"
				}
			}
	];
	courseAttachment = $("#upload_attachment_list").attachmentUI({
		fields : attachmentFields,
		$attachmentContent : $("#upload_attachment_list"),
		maxLine : 3,
		isNeedUpload : false,
		downloadUrl:$('#basePath').val() + "attachment/downLoadAttachment",
		attachmentsJsonstr : $('#attachmentList').val()
	});
}
// A flag for complete info from view detail page to search page
var hasCondition = document.getElementById('_hasCondition').value;
/**
 * If none select fields is selected in filter box, the flag is 1.
 * Or the flag is 0.
 */
var isNoSelectedflag=0;
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
             {EN:'Name', ZH:'名称', sortName:'course_name_sort', width:100, isMustShow:true, align : 'left'},
             {EN:'Brief', ZH:'简介', width:180, autoWidth: true, align : 'left'},
             {EN:'Target Attendees', ZH:'目标人群', width:150, align : 'left'},
             {EN:'Duration', ZH:'持续时间', sortName:'course_duration', width:100},
             {EN:'Type', ZH:'类别', sortName:getI18NSortField('courseType_type_name_sort'), width:100},
             {EN:'Tag', ZH:'标签', sortName:'course_tag', width:80, align : 'left'},
             {EN:'If-Certificated', ZH:'是否认证', sortName:'course_is_certificated', width:130},
             {EN:'Update History', ZH:'更新历史', width:150},
             {EN:'Author', ZH:'作者', sortName:'course_author_name_sort', width:100},
             {EN:'History Trainer', ZH:'历史培训者', width:150}
         ],
	language: configI18n(),
	criteria: criteria,
	minHeight: 150,
	pageSizes: [],
	hasAttachmentIcon: true,
	pageItemSize: 5, 
	url: $('#basePath').val()+'course/searchCourse',
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

// set Criteria
function setCriteria() {
	setQueryString();
	setSearchFields();
	
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
	criteria.typeIds = typeIds;
	
	// set isCertificateds
	var isCertificateds = "";
	$("#isCertificateds").find(":checked").each(function() {
		if (isCertificateds == null || isCertificateds == "") {
			isCertificateds = this.value;
		} else {
			isCertificateds = isCertificateds + " " + this.value;
		}
	});
	criteria.isCertificateds = isCertificateds;
}
