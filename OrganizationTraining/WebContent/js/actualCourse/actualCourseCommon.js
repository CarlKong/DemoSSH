function actualCourseUploadAttachment($uploadButton, $content){
	var actualCourseAttachmentUI;
	if (!actualCourseAttachmentUI) {
		var attachmentFields = [
			{
				fieldType:"text",
				fieldName:attachmentNameMS(),
				fieldKey:["actualCoursAttachmentName", "size", "createDateTime"],
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
				fieldKey:["actualCourseAttachmentVisible"],
				defaultValue:[1],
				fieldWidth:60
			},
			{
				fieldType:"delete",
				fieldName:attachmentActionMS(),
				fieldWidth:100
			}
		];		                        
		actualCourseAttachmentUI = $uploadButton.attachmentUI({
			fields:attachmentFields,
			$attachmentContent : $content,
			uploadUrl : $('#basePath').val()+"attachment/uploadActualCourseAttachment",
			fileTagName : "attachment",
			uploadNameKey : "attachmentFileName"
		});
	}
	return actualCourseAttachmentUI;
}

function actualCourseViewAttachment() {
	var attachmentFields = [
		{
			fieldType:"text",
			fieldName:attachmentNameMS(),
			fieldKey:["actualCoursAttachmentName", "size", "createDateTime"],
			uploadKey:["name", "fileSize", "uploadTime"],
			fieldWidth:390
		},
		{
			fieldType:"selectResult",
			fieldName:attachmentPublicMS(),
			fieldKey:["actualCourseAttachmentVisible"],
			fieldWidth:120
		},
		{
			fieldType:"download",
			fieldName:attachmentDownloadMS(),
			fieldWidth:100,
			downloadSetting:{
				realNameKey:"fileRealName",
				downloadPathKey:"fileFileName",
				nameKeyInJson:"actualCoursAttachmentName",
				pathKeyInJson:"actualCourseAttachmentPath"
			}
		}
	];

	var actualCourseAttachment = $("#upload_attachment_list").attachmentUI({
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