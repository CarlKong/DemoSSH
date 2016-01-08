var planTokenInputObject;
var allTrainersOld;
function configuePlanTagToken(){
	planTokenInputObject = $("#planTagDiv").TagControl({
		maxTagCount: 5,
		inputWidth : 80,
		valueObject : $("#planCategoryTag"),
		lenVaildationMes: getTagLenVaildationMesI18n(),
		illegalVaildationMes: getIllegalVaildationMesI18n()
	});
}

function addAutoComplete(source){
	var $invitedAutoComplete = $("#invited_trainee").autoComplete({
		source:source,
		width:432, 
        height:88,
        dropdownWidth:120,
        maxRows:5
	 }
	);
	var invitedTrainees = $("#invited_trainee").text();
	if (""!=invitedTrainees) {
		$invitedAutoComplete.addItemsByString(invitedTrainees + ";");
	}
	
	var $optionAutoComplete = $("#option_trainee").autoComplete({
		source:source,
		width:432, 
        height:88,
        dropdownWidth:120,
        maxRows:5
	});
	var optionTrainees = $("#option_trainee").text();
	if (""!=optionTrainees) {
		$optionAutoComplete.addItemsByString(optionTrainees + ";");
	}
	
	var $specificAutoComplete = $("#specific_trainee").autoComplete({
		source:source,
		width:432, 
        height:86,
        dropdownWidth:120,
        maxRows:5
	});
	var specificTrainees = $("#specific_trainee").text();
	if (""!=specificTrainees) {
		$specificAutoComplete.addItemsByString(specificTrainees + ";");
	}
	
	var $authorAutoComplete = $("#courseAuthorName").autoComplete({
		source:source,
		width:228, 
        height:30,
        dropdownWidth:120,
        maxRows:5
	 }
	);
	var authorNames = $("#courseAuthorName").text();
	if (""!=authorNames) {
		$authorAutoComplete.addItemsByString(authorNames + ";");
	}
	
	if (($("#actualCourseJsonStr").length ===1) && $("#actualCourseJsonStr").val() != "") {
		getActualCoursesJsonForEdit();
		allTrainersOld = getAllTrainers();
	}
}

function initSelectData(){
	$(".plan_type_title0").data("val",planProperty.invitedType);
	$(".plan_type_title1").data("val",planProperty.publicType);
	$(".trainee_all_title").data("val",planProperty.isAllEmployee);
	$(".trainee_spec_title").data("val",planProperty.specificEmployee);
	$(".register_notic_title").data("val",planProperty.registerNoticeYes);
	$(".register_notic_no_title").data("val",planProperty.registerNoticeNo);
	$("#publicReminderTime1").data("val",planProperty.reminderEmailOneDay);
	$("#publicReminderTime2").data("val",planProperty.reminderEmailFourHours);
	$("#publicReminderTime3").data("val",planProperty.reminderEmailTwoHours);
	$("#publicReminderTime4").data("val",planProperty.reminderEmail0neHour);
	$("#publicReminderTime5").data("val",planProperty.reminderEmailNo);
	$("#reminderTimeTitle1").data("val",planProperty.reminderEmailOneDay);
	$("#reminderTimeTitle2").data("val",planProperty.reminderEmailFourHours);
	$("#reminderTimeTitle3").data("val",planProperty.reminderEmailTwoHours);
	$("#reminderTimeTitle4").data("val",planProperty.reminderEmail0neHour);
	$("#reminderTimeTitle5").data("val",planProperty.reminderEmailNo);
}

function showData(){
	var planTypeId = defaultProperty.defaultPlanType;
	var invitedReminderEmailTime = defaultProperty.defaultReminderTime;
	var publicReminderEmailTime = defaultProperty.defaultReminderTime;
	var isAllEmployees = defaultProperty.defaultIsAllEmployee;
	var isRegisterNotice = defaultProperty.defaultRegisterNotice; 
	if( $("#hidePlanTypeId").val()== planProperty.publicType){
		// this situation is editing public plan, so no invited plan button
		$('#plan_type_category_0').hide();
		$('#PlanTypeCategoryImag0').unbind();
		
		showPlanTypeToPublic();
		if($("#planReminderEmail").val()){
			publicReminderEmailTime = $("#planReminderEmail").val();	
		}
		if($("#hideIsAllEmployee").val()){
		isAllEmployees = $("#hideIsAllEmployee").val();
		}
		if($("#hideRegisterNoticeId").val()){
			isRegisterNotice = $("#hideRegisterNoticeId").val();
		}
	}else{
		if( $("#hidePlanTypeId").val()== planProperty.invitedType){
			// this situation is editing invited plan, so no public plan button
			$('#plan_type_category_1').hide();
			$('#PlanTypeCategoryImag1').unbind();
		}
		showPlanTypeToInvited();
		if($("#planReminderEmail" ).val()){
			invitedReminderEmailTime = $("#planReminderEmail").val();	
		}
		if($("#hideIsAllEmployee").val()){
		   isAllEmployees = defaultProperty.defaultIsAllEmployee;
		}
		if($("#hideRegisterNoticeId").val()){
			isRegisterNotice = defaultProperty.defaultRegisterNotice;
		}
	}
	if(isAllEmployees == planProperty.isAllEmployee){
		showAllemployee();
	}else{
		showSqecEmployee();
	}
	defaultSelectAction("publicReminderTimeId", "reminder_time_image",publicReminderEmailTime);
	defaultSelectAction("invitedReminderTimeId", "reminder_time_image",invitedReminderEmailTime);
	defaultSelectAction("registerNoticeId", "register_notic_image",isRegisterNotice);
} 

function bindClickAction(){
	$("#PlanTypeCategoryImag0").bind("click",function(){
		// invited
	    showPlanTypeToInvited();
	    $("#planTypeId").data("val",planProperty.invitedType);
	});
	$("#PlanTypeCategoryImag1").bind("click",function(){
		// public
		showPlanTypeToPublic();
		$("#planTypeId").data("val",planProperty.publicType);
	});
	
	$("#isallEmployeeId").bind("click",function(){
		showAllemployee();
	});
	$("#sqecEmployeeId").bind("click",function(){
		showSqecEmployee();
	});
	selectAction("invitedReminderTimeId", "reminder_time_image");
	selectAction("publicReminderTimeId", "reminder_time_image");
	selectAction("registerNoticeId", "register_notic_image");
	selectAction("traineeScopeId", "trainee_all_image");
	selectAction("planTypeId", "plan_type_image");
}

function getAllCategoryPlan() {
	$.ajax({
		type:"POST",
		url: $('#basePath').val()+"searchCommon/searchCommon_findPlanTags",
		data: null,
		datatype:"html",
		success:function(data){
			getAllCategoryCallBackPlan(data);
	    }
	  }
	);
}

function getAllCategoryCallBackPlan(data) {
		var textArray = data.planTags;
		$("#categoryTextPlan").empty();
		$.each(textArray,function(i,n){
			if (i < textArray.length - 1) {
				$("<a class='plan_common_tag_items'>" + n.planCategoryName + "</a>")
						.appendTo($("#categoryTextPlan"));			
				$("<span class='pipe'>|</span>").appendTo($("#categoryTextPlan"));
			} else {
				$("<a class='plan_common_tag_items'>" + n.planCategoryName + "</a>")
						.appendTo($("#categoryTextPlan"));
			}
		});
		$("#categoryTextPlan").find("a").click(function() {
			planTokenInputObject[0].addTags($(this).text());
		});
}

function getCategoryPlan(obj) {
	var commonTagValue = $(obj).html();
	var val = $("#planCategoryTag").val();
	var value = "";
	if (val) {
		value = {'label':commonTagValue,'value':commonTagValue};
		$("#planCategoryTag").getAutoCompleteInstance().add(value);
		$("#planCategoryTag").focus();
		return true;
	}
	var tagArray = val.split(";");
	value = val + "; " + commonTagValue;
	$("#planCategoryTag").getAutoCompleteInstance().clear();
	$("#planCategoryTag").getAutoCompleteInstance().addItemsByString(value);
	$("#planCategoryTag").focus();
}

function getPlanTypes() {
	$.ajax({
		type:"POST",
		url: $('#basePath').val()+"searchCommon/searchCommon_findPlanTypes",
		data: null,
		success:function(data){
			planTypeCallBack(data);
	    }
	});
} 

function planTypeCallBack(data){
	    var listPlanType = data.planTypes;
	    var innerHTML = "";
	    $.each(listPlanType,function(i,n){
	      	innerHTML += "<span class='plan_type_category'id='plan_type_category_"+i+"'>" +
	      			"<span class='plan_type_image' id='PlanTypeCategoryImag"+i+"'></span>";	
	    	innerHTML +="<span class='plan_type_title"+i+"'>"+n.planTypeName +"</span></span>";
	    });	
	    $("#planTypeId").append(innerHTML);
	    initSelectData();
	    bindClickAction();
	    showData();
}

function showPlanTypeToInvited(){
	defaultSelectAction("planTypeId", "plan_type_image",planProperty.invitedType);
	$("#plan_public_trainees").hide();
	$("#plan_invited_trainees").show();
	// have session when plan is invited
	$('#sessionPart').show();
	$("#addPlanSession").bind("click", function(){
		$("#editPlanSessionBtn").attr("id", "createPlanSessionBtn");
		showSessionPopup(null);
	});
}

function showPlanTypeToPublic(){
	defaultSelectAction("planTypeId", "plan_type_image",planProperty.publicType);
	$("#plan_invited_trainees").hide();
	$("#plan_public_trainees").show();
	// no session when plan is public
	$('#sessionPart').hide();
	$('#addPlanSession').unbind();
	// when have session in page, show popup to mind user remove it
	var hasSession = checkHasSession();
	if (hasSession) {
		var confirmContent = getRemoveSessionMsg();
		initialConfirmBar(confirmContent, removeSession, keepBeInvited);
	}
};
function removeSession() {
	var listLength = $('#planCourseList > .courseListDataBg').length;
	$(".courseListDataBg").each(function(index, node){
		if ($(node).attr("courseType") === "session") {
			//last one need to resort course list
			var isSort = false;
		    if(index == (listLength - 1)) {
		    	isSort = true;
		    }
			deleteRecord($(node), isSort);
		}
	});
}
function keepBeInvited() {
	$('#PlanTypeCategoryImag0').click();
}

function showAllemployee(){
	defaultSelectAction("traineeScopeId", "trainee_all_image",planProperty.isAllEmployee);
	$(".trainee_input").hide();
	$(".trainee_notice").hide();
}

function showSqecEmployee(){
	defaultSelectAction("traineeScopeId", "trainee_all_image",planProperty.specificEmployee);
	$(".trainee_input").show();
	$(".trainee_notice").show();
}

function selectAction(str, str1){
$('#'+str).find("."+str1).each(function(){
	$(this).click(function(){
    $(this).css('background-image','url(../image/commonIMG/ICN_Radio_Active_14x14.png)');
    $(this).parent().prevAll().each(function(){
    	$(this).children().eq(0).css('background-image','url(../image/commonIMG/ICN_Radio_14x14.png)');
    });
    $(this).parent().nextAll().each(function(){
    	$(this).children().eq(0).css('background-image','url(../image/commonIMG/ICN_Radio_14x14.png)');
    });
    $('#'+str).data("val", $(this).next().data("val"));
  }); 
 });
}

function defaultSelectAction(str, str1, value){
	$('#'+str).find("."+str1).each(function(){
		if($(this).next().data("val") == value){
	    $(this).css('background-image','url(../image/commonIMG/ICN_Radio_Active_14x14.png)');
	    $(this).parent().prevAll().each(function(){
	    	$(this).children().eq(0).css('background-image','url(../image/commonIMG/ICN_Radio_14x14.png)');
	    });
	    $(this).parent().nextAll().each(function(){
	    	$(this).children().eq(0).css('background-image','url(../image/commonIMG/ICN_Radio_14x14.png)');
	    });
		}
		$('#'+str).data("val", value);	
	 });
}

function prepareDataForSubmit(){
		  var planType = $("#planTypeId").data("val");
		  var publicReminderTime = $("#publicReminderTimeId").data("val");
		  var invitedReminderTime = $("#invitedReminderTimeId").data("val");
		  var traineeScope = $("#traineeScopeId").data("val");
		  var registerNotice = $("#registerNoticeId").data("val");
		  
		if(planType == planProperty.invitedType){
			$("#planReminderEmail").val(invitedReminderTime);
			$("#hideIsAllEmployee").val(defaultProperty.defaultIsAllEmployee);
			$("#hidePlanTypeId").val(planProperty.invitedType);
			$("#hideRegisterNoticeId").val(defaultProperty.defaultRegisterNotice);
			$("#specific_trainee").val(null);
			$("#hideRegisterNoticeId").val(registerNotice);
		}else{
			if(traineeScope == planProperty.isAllEmployee){
				$("#hideIsAllEmployee").val(planProperty.isAllEmployee);	
				$("#specific_trainee").val(null);
			}else{
				$("#hideIsAllEmployee").val(planProperty.specificEmployee);	
			}
			$("#hidePlanTypeId").val(planProperty.publicType);
			$("#planReminderEmail").val(publicReminderTime);
			$("#hideRegisterNoticeId").val(registerNotice);
		}		
}

/**
 * initialize xheditor
 * @return
 */
function initXHEditorPlan() {
	var xheditorSetting = {
        tools:"Fontface,FontSize,Bold,Italic,Underline,FontColor," +
            "Align,List,Outdent,Indent,Img,Fullscreen",
        upImgUrl:"../attachment/uploadImage",
        upImgExt:"jpg,jpeg,gif,png,bmp",
        forcePtag : false,
		showMessage : function(message) {
			alert("Upload file size > " + message + ", so you can't upload it");
		},
		maxFileSize : 2
    };
	//initialize xheditor of plan brief.
    var $planBrief = $("#planBriefEditor");
    var planEditor = $planBrief.xheditor(xheditorSetting);
    planEditor.settings.blur = function(){
    	if ($planBrief.parent().find('.xheTool').find('span a').hasClass('xheActive')) {
    		return false;
    	}
    	checkPlanBrief('blur'); // planValidanguage.js
    };
    
    //initialize xheditor of plan session brief.
    var $planSessionBrief = $("#sessionBriefEditor");
    var planSessionEditor = $planSessionBrief.xheditor(xheditorSetting);
    planSessionEditor.settings.blur = function(){
    	if ($planSessionBrief.parent().find('.xheTool').find('span a').hasClass('xheActive')) {
    		return false;
    	}
    	checkPlanSessionBrief('blur'); // planValidanguage.js
    };
    
    //initialize xheditor of plan course brief.
    var $courseBrief = $("#courseBrief");
    var editor = $courseBrief.xheditor(xheditorSetting);
    editor.settings.blur = function(){
    	if ($courseBrief.parent().find('.xheTool').find('span a').hasClass('xheActive')) {
    		return false;
    	}
        checkCourseBrief('blur');
    };
}

/**
 * plan attachment functions
 */
var planAttachment;
function planUploadAttachment(methodName){
	var attachmentFields = [
		{
			fieldType:"text",
			fieldName:attachmentNameMS(),
			fieldKey:["planAttachmentName", "size", "createDateTime"],
			uploadKey:["name", "fileSize", "uploadTime"],
			fieldWidth:370
		},
		{
			fieldType:"progress",
			fieldName:attachmentProgressBarMS(),
			fieldWidth:230
		},
		{
			fieldType:"delete",
			fieldName:attachmentActionMS(),
			fieldWidth:100
		}
	];                       
	if ('createPlan' == methodName) {
		planAttachment = $("#plan_attachment_list_url").attachmentUI({
			fields:attachmentFields,
			$attachmentContent : $("#plan_attachment_list"),
			uploadUrl : $('#basePath').val()+"attachment/uploadPlanAttachment",
			fileTagName : "attachment",
			uploadNameKey : "attachmentFileName"
		});
	}
	if ('editPlan' == methodName) {
		planAttachment = $("#plan_attachment_list_url").attachmentUI({
			fields:attachmentFields,
			$attachmentContent : $("#plan_attachment_list"),
			attachmentsJsonstr : $('#plan_attachmentsJson').val(),
			uploadUrl : $('#basePath').val()+"attachment/uploadPlanAttachment",
			fileTagName : "attachment",
			uploadNameKey : "attachmentFileName"
		});
	}	                		
}

function planViewAttachment() {
	var attachmentFields = [
		{
			fieldType:"text",
			fieldName:attachmentNameMS(),
			fieldKey:["planAttachmentName", "size", "createDateTime"],
			uploadKey:["name", "fileSize", "uploadTime"],
			fieldWidth:450
		},
		{
			fieldType:"download",
			fieldName:attachmentDownloadMS(),
			fieldWidth:160,
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
		isNeedUpload : false,
		downloadUrl:$('#basePath').val() + "attachment/downLoadAttachment",
		attachmentsJsonstr : $('#plan_attachmentsJson').val()
	});
	
	if ("[]" != $('#plan_attachmentsJson').val()) {
		$('#upload_attachment_list').css('margin-top','10px');
	}
}

function setPlanNeedAssessmentFlag(){
	var assessRadio = $("#enableAssess");
	var noAssessRadio = $("#disableAssess");
	var assessmentFlag = $("#planNeedAssessmentFlag").val();
	var assessLabel = $('<label for="enableAssess"  class="checked"></label>');
	var noAssessLabel = $('<label for="disableAssess"  class="checked"></label>');
	if(assessmentFlag == 1){
		assessLabel.insertAfter(assessRadio);
		noAssessLabel.removeClass("checked");
		assessRadio.attr("checked","checked");
		noAssessLabel.insertAfter(noAssessRadio);
	}else{
		assessLabel.removeClass("checked");
		assessLabel.insertAfter(assessRadio);
		noAssessLabel.insertAfter(noAssessRadio);
		noAssessRadio.attr("checked","checked");
	}
}
