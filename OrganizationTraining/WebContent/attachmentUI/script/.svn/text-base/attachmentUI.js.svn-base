(function($) {
	
	$.attachmentUI = function (options, $button){
		this.options = $.extend({},$.fn.attachmentUI.defaults, options);
		this._init($button);
	};
	
	$.attachmentUI.prototype = {
		
		_init : function($button){
			this.totalCount = 0;
			var $attachmentUI = $("<div>", {"class":"attachment-ui", "hasBody":"0"})
								.appendTo(this.options.$attachmentContent);
			this._drawHead($attachmentUI);
			this._prepareListBody($attachmentUI);
			if (this.options.attachmentsJsonstr && "" != this.options.attachmentsJsonstr) {
				var attachmentsJsonArray = eval("("+this.options.attachmentsJsonstr+")");
				if (attachmentsJsonArray.length > 0) {
					this._drawHead($attachmentUI);
					this._loadAttachmentByJson(attachmentsJsonArray);
				}
			}
			
			var $fileInput;
			if (this.options.isNeedUpload) {
				$fileInput = this._createFileInput($attachmentUI);
				$button.bind("click", function(){
					$fileInput.click();
				});
			}
		},
		_loadAttachmentByJson : function(attachmentsJson) {
			var attachmentUIObj = this;
			$.each(attachmentsJson, function(i, _attachment){
				attachmentUIObj._appendAttachmentForEdit(_attachment);
			});
		},
		/**** create file input ****/
		_createFileInput : function($attachmentUI) {
			var opts = this.options;
			var attachmentUIObj = this;
			var $form = $("<form>", {"method":"post", "enctype":"multipart/form-data", "action":opts.uploadUrl})
							.appendTo($attachmentUI);
			var $fileInput = $("<input>", {"type":"file", "name":opts.fileTagName}).appendTo($form);
			var $fileNameInput = $("<input>", {"type":"hidden", "name":opts.uploadNameKey}).appendTo($form);
			$fileInput.css("display", "none");
			$fileInput[0].addEventListener("change", function(event) {
				var files = event.target.files || event.dataTransfer.files;
				_prepareUpload(attachmentUIObj,files,$fileNameInput);
			}, false);
			return $fileInput;
		},
		
		/**** upload file ****/
		_submitFileData : function(file, $progress, $attachmentRecord) {
//			var $fileInputNode = $form.find("input[type='file']");
			var formData = new FormData();
			formData.append(this.options.fileTagName, file);
			var xhr = _createXHR();
			if (xhr.upload) {
				xhr.upload.addEventListener("progress", function(e) {
					var pc = parseInt(e.loaded / e.total * 100);
					var ratio = e.loaded / e.total;
					if (pc%10 == 0) {
						_progressing($progress, ratio, pc);
					}
					
				}, false);
			}
			xhr.onreadystatechange = function (event) {
       			if (xhr.readyState == 4) {
           			if ((xhr.status >= 200 && xhr.status < 300) || xhr.status == 304) {
           				console.log(xhr.responseText);
           				var attachmentJsonObj = eval("("+xhr.responseText+")");
           				$attachmentRecord.data("attachment", attachmentJsonObj);
            		} else {
                		alert("Request was unsuccessful: " + xhr.status);
            		}
        		}
    		};
    		xhr.open("post", this.options.uploadUrl, true);
    		xhr.send(formData);
		},
		
		_drawHead : function($attachmentUI) {
			if (this.$head) {
				this.$head.removeClass("hide-div-class");
				this.$head.show();
			} else {
				var $attachmentHead = $("<div>", {"class":"attachment-head hide-div-class"}).appendTo($attachmentUI);
				var fieldWidth = 0;
				for(i=0; i<this.options.fields.length; i++) {
					var field = this.options.fields[i];
					var $headTitle = $("<div></div>").appendTo($attachmentHead);
					$headTitle.width(field.fieldWidth);
					$headTitle.text(field.fieldName);
					$headTitle.css("float","left");
					fieldWidth = fieldWidth + this.options.fields[i].fieldWidth;
				}
				$("<div class='clear'></div>").appendTo($attachmentHead); 
				$attachmentUI.width(fieldWidth);
				this.$head = $attachmentHead;
			}
		},
		
		_prepareListBody : function($attachmentUI) {
			if ($attachmentUI.attr("hasBody") == "0") {
				if (this.options.maxLine <= 0) {
					var $showDiv = $("<div>", {"class" : "show-attachment-div"}).appendTo($attachmentUI);
					var $realDataDiv = $("<div>", {"class" : "real-attachment-data-div"}).appendTo($showDiv);
					this.$showDataDiv = $showDiv;
					this.$realDataDiv = $realDataDiv;
				} else {
					var showHeight = this.options.maxLine * 30;
					this.showHeight = showHeight;
					var $showDiv = $("<div>", {"class" : "show-attachment-div-scroll"}).appendTo($attachmentUI);
					$showDiv.css("max-height",showHeight+"px");
					var $realDataDiv = $("<div>", {"class" : "real-attachment-data-div-scroll"}).appendTo($showDiv);
					var $verticalSlide = $("<div>", {"class" : "vertical-slide"}).appendTo($showDiv);
					$verticalSlide.height(showHeight);
					var $verticalSlideBlock = $("<div>", {"class" : "vertical-slide-block"}).appendTo($verticalSlide);
					$verticalSlideBlock.mousedown(function(e){
						var $this = $(this);
						var x = e.clientX;
						var y = e.clientY;
						var top = this.offsetTop;
						$(document).bind({
							"mousemove.attachmentUI" : function(e){
								var _top = e.clientY - y + top;
								if (_top < 0) {
									_top = 0;
								}
								if (_top + $this.height() > showHeight) {
									_top = showHeight - $this.height();
								}
								$this.css("top", _top);
								var verticalRatio = showHeight/($realDataDiv.height());
								$realDataDiv.css("top", (-1)*_top/verticalRatio);
							}
						});
						$(document).bind({
							"mouseup.attachmentUI" : function(){
								$(document).unbind("mousemove.attachmentUI").unbind("mouseup.attachmentUI");
							}
						});
						return false;
					});
					this.$showDataDiv = $showDiv;
					this.$realDataDiv = $realDataDiv;
					this.$verticalSlide = $verticalSlide;
					this.$verticalSlideBlock = $verticalSlideBlock;
				}
				$attachmentUI.attr("hasBody", "1");
			} 
		},
		
		_appendAttachmentForEdit : function(jsonObj, file) {
			this.$head.show();
			$attachmentRecord = $("<div class='attachment-item-data'></div>").appendTo(this.$realDataDiv);
			this.totalCount ++;
			var $progress;
			for (var i=0, field; field = this.options.fields[i]; i++) {
				$field = $("<div class='attachment-item-field'></div>").appendTo($attachmentRecord);
				$field.width(field.fieldWidth);
				var fieldText = "";
				var oneKey;
				if (field.fieldKey && field.fieldKey.length ===1) {
					oneKey = field.fieldKey[0];
				}
				if (field.fieldKey && field.fieldKey.length>0) {
					for (var j=0, fieldKey; fieldKey = field.fieldKey[j]; j++) {
						fieldText = fieldText + jsonObj[fieldKey];
					}
				}
				if (field.fieldType === "text") {
					for (var j=0, fieldKey; fieldKey = field.fieldKey[j]; j++) {
						var $childField = $("<div></div>").appendTo($field);
						$childField.text(jsonObj[fieldKey]);
						$childField.css("max-width", field.fieldWidth/3+"px");
						$childField.addClass("child-field");
						//Add toolTip
						if (j===0) {
							var $realNameDiv = $('<div class="hideName"></div>').appendTo($field);
							$realNameDiv.text(jsonObj[fieldKey]);
							var toolTipWidth = field.fieldWidth/3;
							console.log($realNameDiv.width());
							if ($realNameDiv.width() > toolTipWidth) {
								$childField.poshytip({
									allowTipHover : true ,
									className: 'tip-green',
									content: $realNameDiv.html()
								});
							}
						}
					}
					$("<div>",{"class":"clear"}).appendTo($field);
				}
				if (field.fieldType === "checkbox") {
					_createCheckBox($attachmentRecord, $field, fieldText, oneKey);
				}
				if (field.fieldType === "delete") {
					_createDeleteButton($field, $attachmentRecord, this);
				}
				if (field.fieldType === "selectResult") {
					_createSelectResultTag($field, fieldText, this.options.slectedStyleClass, this.options.unslectedStyleClass);
				}
				if (field.fieldType === "download") {
					_createDownloadButton($field,this.options.downloadUrl, field.downloadSetting, jsonObj[field.downloadSetting.pathKeyInJson], jsonObj[field.downloadSetting.nameKeyInJson]);
				}
				if (field.fieldType === "progress") {
					if (file) {
						$progress = _createProgress($field);
					}
				}
				if (field.fieldType === "userDefine") {
					$.fn.attachmentUI.userDefineField($field, this.options, jsonObj);
				}
			}
			if (this.options.maxLine>0 && this.totalCount>this.options.maxLine) {
				this.$showDataDiv.height(this.showHeight);
			} else {
				this.$showDataDiv.height(this.totalCount*30);
			}
			//Reset vertical scroll bar
			_resetVerticalScroll(this);
			
			if (this.options.isNeedUpload && file) {
				this._submitFileData(file, $progress, $attachmentRecord);
			} else {
				$attachmentRecord.data("attachment", jsonObj);
			}
		},
		
		
		getAttachmentsJsonStr : function() {
			var attachmentsJsonStr = "[]";
			if (this.totalCount > 0) {
				var jsonStrArray = [];
				this.$realDataDiv.children().each(function(index, node) {
					var attachmentJson = $(node).data("attachment");
					jsonStrArray.push(JSON.stringify(attachmentJson));
				});
				attachmentsJsonStr = "[" + jsonStrArray.join(",") + "]";
				return attachmentsJsonStr;
			} else {
				return attachmentsJsonStr;
			}
			
		},
		
		removeAllAttachments : function() {
			this.$head.hide();
			this.$realDataDiv.children().remove();
		},
		
		reloadAttachment : function(jsonListStr) {
			this.$realDataDiv.children().remove();
			this.totalCount = 0;
			if (jsonListStr && "" != jsonListStr) {
				var attachmentsJsonArray = eval("("+jsonListStr+")");
				if (attachmentsJsonArray.length > 0) {
					this._drawHead();
					this._loadAttachmentByJson(attachmentsJsonArray);
				} else {
					this.$head.hide();
				}
			}
			
		},
		
		getAttachmentsCount : function() {
			return this.totalCount;
		}
		
	};
	
	$.fn.attachmentUI = function(options){
	    return new $.attachmentUI(options, $(this));
	};
	
	/**
	 * Private function, create XMLHttpRequest object.
	 * @return {TypeName} 
	 */
	function _createXHR() {
    	if (typeof XMLHttpRequest != "undefined") {
        	return new XMLHttpRequest();
    	} else {
    		alert("Your browser version is too low!");
    	}
    }
	
	function _prepareUpload(attachmentUIObj, files, $fileNameInput) {
		var $attachmentUI = attachmentUIObj.options.$attachmentContent.find(".attachment-ui");
		var showHeight = attachmentUIObj.options.maxLine * 30;
		for (var i = 0, f; f = files[i]; i++) {
			if (f.size > attachmentUIObj.options.maxUploadSize) {
				alert("The file is too big!");
				//TODO
				return;
			} 
			f.uploadTime = $.fn.attachmentUI.getDateStr(new Date());
			attachmentUIObj._drawHead($attachmentUI);
			$fileNameInput.val(f.name);
			var attachmentJSONStr = [];
			f.fileSize = _getFileSize(f.size);
			for (var j = 0, field; field = attachmentUIObj.options.fields[j]; j++) {
				if (field.fieldKey && field.uploadKey&&field.fieldKey.length>0&&field.uploadKey.length>0) {
					for (var k=0, uploadKeyword; uploadKeyword=field.uploadKey[k]; k++ ) {
						attachmentJSONStr.push("\'" + field.fieldKey[k]+"\':\'"+f[uploadKeyword]+"\'");
					}
				}
				if (field.fieldKey && field.defaultValue&&field.fieldKey.length>0&&field.defaultValue.length>0) {
					for (var k=0, defaultVal; defaultVal=field.defaultValue[k]; k++ ) {
						attachmentJSONStr.push("\'" + field.fieldKey[k]+"\':\'"+defaultVal +"\'");
					}
				}
			}
			var attachmentStr = "{" + attachmentJSONStr.join(",")+ "}";
			var jsonObj = eval("("+attachmentStr+")");
			attachmentUIObj._appendAttachmentForEdit(jsonObj, f);
		}
	}
	
	function _createCheckBox($attachmentRecord, $content, checkBoxValue, oneKey) {
		var $checkBox = $("<span></span>").appendTo($content);
		$checkBox.attr("checkBoxValue",checkBoxValue);
		if (checkBoxValue === "1") {
			$checkBox.addClass("checkbox-checked");
		} else if (checkBoxValue === "0") {
			$checkBox.addClass("checkbox-unchecked");
		}
		_alineCenter($checkBox, $content);		
		$checkBox.bind("click", function(){
			if ($(this).attr("checkBoxValue") === "1") {
				$(this).addClass("checkbox-unchecked").removeClass("checkbox-checked");
				$(this).attr("checkBoxValue", "0");
				$attachmentRecord.data("attachment")[oneKey] = "0";
				return;
			}
			if ($(this).attr("checkBoxValue") === "0") {
				$(this).addClass("checkbox-checked").removeClass("checkbox-unchecked");
				$(this).attr("checkBoxValue", "1");
				$attachmentRecord.data("attachment")[oneKey] = "1";
				return;
			}
		});
	}
	
	function _createDeleteButton($content, $attachmentRecord, attachmentUI) {
		var $deleteButton = $("<span class='delete-button'></span>").appendTo($content);
		_alineCenter($deleteButton, $content);
		$deleteButton.bind("click", function(){
			$attachmentRecord.remove();
			attachmentUI.totalCount = attachmentUI.totalCount -1;
			if (attachmentUI.totalCount === 0) {
				attachmentUI.$head.hide();
			}
			_resetVerticalScroll(attachmentUI);
		});
	}
	
	function _createSelectResultTag($content, selectedResult, slectedStyleClass, unslectedStyleClass) {
		var $selectedResult = $("<span></span>").appendTo($content);
		if (selectedResult === "1") {
			$selectedResult.addClass(slectedStyleClass);
		} 
		if (selectedResult === "0") {
			$selectedResult.addClass(unslectedStyleClass);
		}
		_alineCenter($selectedResult, $content);
	}
	
	function _createDownloadButton($content, downloadUrl, downloadSetting, downloadPath, realName ) {
		var $downLoadButton = $("<span>", {"class":"download-button"}).appendTo($content);
		_alineCenter($downLoadButton, $content);
		$downLoadButton.bind("click", function(){
			window.location.href = downloadUrl + "?"+downloadSetting.downloadPathKey+"=" + downloadPath + "&"+downloadSetting.realNameKey+"=" + realName;
		});
	}
	
	function _createProgress($content) {
		var $progress = $('<div class="loading"><div></div></div> ').appendTo($content);
		return $progress;
	}
	
	function _progressing($progress, ratio, ratioText) {
		$progress.find("div").width($progress.width()*ratio);
	}
	
	function _alineCenter($node, $content) {
		var marginTop = ($content.height() - $node.height())/2;
		var marginLeft = ($content.width() - $node.width())/2;
		$node.css({"margin-top": marginTop + "px", "margin-left": marginLeft + "px"});
	}
	
	function _getFileSize(fileSize) {
		if (fileSize<1000) {
			return fileSize + ' B';
		} 
		fileSize = fileSize/1024;
		if (fileSize<1000) {
			return fileSize.toFixed(2) + ' KB';
		}
		fileSize = fileSize/1024;
		return fileSize.toFixed(2) + ' MB';
	}
	//Judge if vertical slide should be shown, and calculate its location.
	function _resetVerticalScroll(attachmentUI) {
		if (attachmentUI.options.maxLine>0) {
				if (attachmentUI.totalCount >= attachmentUI.options.maxLine) {
					var verticalRatio = attachmentUI.showHeight/(attachmentUI.totalCount * 30);
					attachmentUI.$verticalSlideBlock.height(attachmentUI.showHeight*verticalRatio);
					var slideBlockTop = attachmentUI.showHeight - attachmentUI.$verticalSlideBlock.height();
					attachmentUI.$verticalSlideBlock.css("top", slideBlockTop);
					var realDataTop = attachmentUI.showHeight - (attachmentUI.totalCount * 30);
					attachmentUI.$realDataDiv.css("top", realDataTop);
					attachmentUI.$verticalSlide.show();
				}
				if (attachmentUI.totalCount === attachmentUI.options.maxLine) {
					attachmentUI.$verticalSlide.hide();
				}
				if (attachmentUI.totalCount <= attachmentUI.options.maxLine){
					attachmentUI.$showDataDiv.height(attachmentUI.totalCount * 30);
				}
			} else {
				attachmentUI.$showDataDiv.height((attachmentUI.totalCount * 30));
			}
	}
	
	$.fn.attachmentUI.getDateStr = function(date) {
		return date.getFullYear()+"-"+(date.getMonth()+1)+"-"+date.getDate();
	};
	
	$.fn.attachmentUI.userDefineField = function($content, options, fileJson) {
		//user define.
	};
	
	$.fn.attachmentUI.defaults = {
		fields :[],
		attachmentsJsonstr : "",
		uploadUrl : "uploadAction",
		downloadUrl : "",
		$attachmentContent : $("#attachment-content"),
		maxUploadSize :100971520,
		isNeedUpload : true,
		fileTagName : "myFile",
		slectedStyleClass : "selected-result",
		unslectedStyleClass : "unselected-result",
		uploadNameKey:"",
		maxLine : 0
	};
	
})(jQuery);
