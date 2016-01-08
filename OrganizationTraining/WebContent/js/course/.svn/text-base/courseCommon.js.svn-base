
/**
 * set course brief
 * @return
 */
function initXHEditorCourse() {
    var $courseBrief = $("#courseBrief");
    var editor = $courseBrief.xheditor(
            {
                tools:"Fontface,FontSize,Bold,Italic,Underline,FontColor," +
                      "Align,List,Outdent,Indent,Img,Fullscreen",
                upImgUrl:"../attachment/uploadImage",
                upImgExt:"jpg,jpeg,gif,png,bmp",
                forcePtag:false,
                showMessage:function(message){
            			     alert("Upload file size > " + message + ", so you can't upload it");
            			    },
            	maxFileSize:2
            });
    editor.settings.blur = function(){
    	if ($courseBrief.parent().find('.xheTool').find('span a').hasClass('xheActive')) {
    		return false;
    	}
        checkCourseBrief('blur');
    };
}

/**
 * get & set course type data
 * @return
 */
function courseTypeData(){
    $.ajax({
        type: "POST",
        url: $('#basePath').val()+"searchCommon/searchCommon_findCourseTypes",
        success: function(data) {
            courseTypeDataCallBack(data);
        }
     });
}

function courseTypeDataCallBack(data){
    var listCourseType = data.courseTypes;
    var typeTotals = listCourseType.length;
    var innerHTML = '';
    var num = 0;
    var courseTypeDiv = $("#radio_courseTypeId");
    var radios = $(".course_type_radios");
	if(null != radios && undefined != radios && '' != radios){
		$(".course_type_radios").remove();
	}
    $.each(listCourseType,function(i,courseType){
    	if((i+1) % 3 == 0){
    		innerHTML += '<input id="courseTypeId_'+courseType.courseTypeId+'" class="radio_type_input" type="radio" name="course.courseType.courseTypeId" value="'+courseType.courseTypeId+'">'
			           +'<label for="courseTypeId_'+courseType.courseTypeId+'" ></label>'
			           +'<span>'+courseType.typeName+'</span>';
    		var radioDivInnerHTML = '<div class="common_radio course_type_radios"></div>';
    		courseTypeDiv.append(radioDivInnerHTML);
    		$('.common_radio').eq(num).append(innerHTML);
    		num++;
    		innerHTML = '';
    	}else{
    		innerHTML += '<input id="courseTypeId_'+courseType.courseTypeId+'" class="radio_type_input" type="radio" name="course.courseType.courseTypeId" value="'+courseType.courseTypeId+'">'
			           +'<label for="courseTypeId_'+courseType.courseTypeId+'" ></label>'
			           +'<span class="typeValue_span">'+courseType.typeName+'</span>';
    	}
    });
    $('.course_type_radios:last').attr('id','common_radio_last');
    $('.course_type_radios:first').find('label').eq(0).addClass('checked');
    $('.course_type_radios:first').find('input[type="radio"]').eq(0).attr('checked','checked');
    changeRadioChecked('radio_courseTypeId');
    var courseTypeId = $("#courseTypeId").val();
    if (undefined != courseTypeId){
    	$("label[for=courseTypeId_"+courseTypeId+"]").trigger("click");
    }
}

function changeRadioChecked(radioDiv){
	if(!radioDiv || radioDiv == ' ' ){
		return false;
	}
	if(document.getElementById(radioDiv) == undefined){
		return false;
	}
	var labels = document.getElementById(radioDiv)
			.getElementsByTagName('label');
	var radios = document.getElementById(radioDiv)
			.getElementsByTagName('input');
	for (i = 0, j = labels.length; i < j; i++) {
		labels[i].onclick = function() {
			if (this.className == '') {
				for (k = 0, l = labels.length; k < l; k++) {
					labels[k].className = '';
				}
				this.className = 'checked';
			}
		};
	}
}

/**
 * get & set common tag data
 * @return
 */
function commonTagData(){
	$.ajax({
        type: "POST",
        url: $('#basePath').val()+"searchCommon/searchCommon_findCourseTags",
        success: function(data) {
			commonTagDataCallBack(data);
        }
     });
}

function commonTagDataCallBack(data){
	var listCommonTag = data.courseTags;
	var tagTotals = listCommonTag.length;
	var innerHTML = '';
	var commonTagContent = $('#common_tag_content');
	$.each(listCommonTag,function(i, courseTag){
		if(i < tagTotals-1){
			innerHTML += '<a href="javascript:void(0)" class="commonTag">'+courseTag.tagName+'</a>'
			           + '<span class="common_tag_pice" >|</span>';
		}else{
			innerHTML += '<a href="javascript:void(0)" class="commonTag">'+courseTag.tagName+'</a>';
		}
	});
	commonTagContent.append(innerHTML);
	commonTagContent.find("a").click(function(){
		//entryCommonTag(this);
		tokenInputObject[0].addTags($(this).text());
	});
}

function entryCommonTag(obj){
	var commonTagValue = $(obj).html();
	var val = $("#courseCategoryTag").val();
	var value = "";
	if (val == "") {
		value = {'label':commonTagValue,'value':commonTagValue};
		$("#courseCategoryTag").getAutoCompleteInstance().add(value);
	} else {
		var tagArray = val.split(";");
		value = val + "; " + commonTagValue;
		$("#courseCategoryTag").getAutoCompleteInstance().clear();
		$("#courseCategoryTag").getAutoCompleteInstance().addItemsByString(value);
	}
	$("#courseCategoryTag").focus();
}

/**
 * configure Tag Token
 * @return
 */
var tokenInputObject;
function configureTagToken() {
	tokenInputObject = $("#tagDiv").TagControl({
		maxTagCount: 5,
		inputWidth : 80,
		valueObject : $("#courseCategoryTag"),
		lenVaildationMes: getTagLenVaildationMesI18n(),
		illegalVaildationMes: getIllegalVaildationMesI18n()
	});
}

/**
 * get & set employee data source
 * @return
 */
function initEmployeeName(methodName){
    loadAllEmployeeNames($('#basePath').val()+
            "searchCommon/searchCommon_findAllEmployeeNames", function(array){
        validanguage.settings.foo = array;
        initJQueryAutoCompleteSource(array, methodName, 454);
    });;
}

//initial the course author name autoComplete
function initJQueryAutoCompleteSource(data, methodName, widthValue) {
    var tokenInputObject = $("#courseAuthorName").autoComplete({
       	source:data,
		tokenLimit:5, 
	   	width:widthValue, 			//default set 400
       	height:30, 			//default set 200
       	dropdownWidth:150,  
       	dropdownHeight:100, 
       	maxRows:4, 			
       	minChars:1, 		
       	searchDelay:100,	
		inputSplit:";",
	 	tokenDelimiter:";",
	 	onAdd:function(){
    	
    	}
    });
    // if edit course, set author name
    if(methodName == 'editCourse') {
    	loadAutoCompData($("#courseAuthorName_value"), $("#courseAuthorName"));
    }
}

/**
 * course attachment part function
 */
var courseAttachment;
function courseUploadAttachment(methodName){
	var attachmentFields = [
		{
			fieldType:"text",
			fieldName:attachmentNameMS(),
			fieldKey:["courseAttachmentName", "size", "createDateTime"],
			uploadKey:["name", "fileSize", "uploadTime"],
			fieldWidth:300
		},
		{
			fieldType:"progress",
			fieldName:attachmentProgressBarMS(),
			fieldWidth:150
		},
		{
			fieldType:"checkbox",
			fieldName:attachmentPublicMS(),
			fieldKey:["courseAttachmentvisible"],
			defaultValue:[1],
			fieldWidth:60
		},
		{
			fieldType:"delete",
			fieldName:attachmentActionMS(),
			fieldWidth:100
		}
	];                       
	if ('createCourse' == methodName) {
		courseAttachment = $("#course_add_attachment").attachmentUI({
			fields:attachmentFields,
			$attachmentContent : $("#upload_attachment_list"),
			uploadUrl : $('#basePath').val()+"attachment/uploadCourseAttachment",
			fileTagName : "attachment",
			uploadNameKey : "attachmentFileName"
		});
	}
	if ('editCourse' == methodName) {
		courseAttachment = $("#course_add_attachment").attachmentUI({
			fields:attachmentFields,
			$attachmentContent : $("#upload_attachment_list"),
			attachmentsJsonstr : $('#attachmentList').val(),
			uploadUrl : $('#basePath').val()+"attachment/uploadCourseAttachment",
			fileTagName : "attachment",
			uploadNameKey : "attachmentFileName"
		});
	}
}

/**view course attachment*/
function courseViewAttachment(){
	var attachmentFields = [
		    {
				fieldType:"text",
				fieldName:attachmentNameMS(),
				fieldKey:["courseAttachmentName", "size", "createDateTime"],
				uploadKey:["name", "fileSize", "uploadTime"],
				fieldWidth:390
			},
			{
				fieldType:"selectResult",
				fieldName:attachmentPublicMS(),
				fieldKey:["courseAttachmentvisible"],
				fieldWidth:120
			},
			{
				fieldType:"download",
				fieldName:attachmentDownloadMS(),
				fieldWidth:100,
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
		isNeedUpload : false,
		downloadUrl:$('#basePath').val() + "attachment/downLoadAttachment",
		attachmentsJsonstr : $('#attachmentList').val()
	});
	
	if ("[]" != $('#attachmentList').val()) {
		$('#upload_attachment_list').css('margin-top','10px');
	}
	
}

/**
 * Get auto complete values for edit
 * @param sourceObj:  hidden area
 * @param showObj: input for show
 * @return
 */
function loadAutoCompData(sourceObj, showObj){
	var sourceValue = sourceObj.val();
	if(sourceValue != null && sourceValue != "" && sourceValue != undefined){
	    if(sourceValue.indexOf(";") == -1){
	    	 sourceValue = {'label':sourceValue,'value':sourceValue};
	    	 showObj.getAutoCompleteInstance().add(sourceValue);
	    }else{
	    	 showObj.getAutoCompleteInstance().addItemsByString(sourceValue);
	    }
	}
}