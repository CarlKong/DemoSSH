/**
 * Load JS
 * @param url
 * 			the JS url
 * @param callback
 * 			Load success, and execute the callback function
 * @param charset
 * @return
 */
function loadJS(url,callback,charset){
	var script = document.createElement('script');
	script.onload = script.onreadystatechange = function ()
	{
		if (script && script.readyState && /^(?!(?:loaded|complete)$)/.test(script.readyState)) return;
		script.onload = script.onreadystatechange = null;
		script.src = '';
		script.parentNode.removeChild(script);
		script = null;
		if(callback) callback();
	};
	script.charset=charset || document.charset || document.characterSet;
	script.src = url;
	try {document.getElementsByTagName("head")[0].appendChild(script);} catch (e) {}
}

/**
 * 
 * @return
 */
function initXhEditor_setLanguange(xHEditor,language){
	if(!xHEditor || xHEditor == ' ') return;
	if (!language) {
		var defaultLan = getLanguageByBrowser();
		if (defaultLan.indexOf("zh") !== -1) {
			loadJS('../xheditor/xheditor-1.1.12-zh-cn.js',xHEditor);
		}else {
			loadJS('../xheditor/xheditor-1.1.12-en.js',xHEditor);
		}
	}else if (language.indexOf("zh") !== -1) {
		loadJS('../xheditor/xheditor-1.1.12-zh-cn.js',xHEditor);
	} else {
		loadJS('../xheditor/xheditor-1.1.12-en.js',xHEditor);
	}
}

/**
 * add a prototype in javaScript function of String 
 * @return {} function(). used string.trim()
 * @author Tanner.Cai
 */
String.prototype.trim = function(){
    return this.replace(/(^\s*)|(\s*$)/g, "");
}

/**
 * check the two string if the have some is same.
 * @param {} subStr
 * @param {} separator
 * @return {} If have same string in two string.
 * @author Tanner.Cai
 */
function checkTheSameForStr(subStr,separator){
	var flag = false;
	var array = subStr.trim().split(separator);
	if(array.length >= 2){
		for(var i = 0 ; i < array.length ; i++){
			if(countInstances(subStr,array[i])>=2){
				flag=true;
				break;
			}
		}
	}
	return flag;
}

/**
 * find the count for two string if one include other.
 * @param {} mainStr
 * @param {} subStr
 * @return {} the count of the same string
 * @author Tanner.Cai
 */
function countInstances(mainStr, subStr){
	var count = 0;
	var offset = 0;
	do {
		offset = mainStr.indexOf(subStr, offset);
		if(offset != -1){
			count++;
			offset += subStr.length;
		}
	}while(offset != -1);
		return count;
}

function loadAllEmployeeNames(url, callback, $node) {
	$.ajax({
		type:"POST",
		url: url,
		datatype:"html",
		success:function(text){
			if (!text || !text.names) {
				return false;
			}
			var array = text.names.split(",");
			if ($node) {
				$node.data("employeeNamesData", array);
			}
			callback(array);
			
	    },
	    error:function(){
	    	alert("Server Error");
	    }
	});
}

function checkResponseData(data){
	if (data.errorCodeId) {
		if (!data.flag) {
			alert(data.message);
			return false;
		}
		window.location.href = "";
	}
}

function getEmployeeNamesFromAutoComplete(obj) {
	var tokens = obj.getAutoCompleteInstance().getTokens();
	var names = new Array();
	$.each(tokens, function(i, n){
		names.push(n.label);
	});
	return names;
}

function ArrayIsEqual(array1, array2) {
	var result = array1.sort().toString() === array2.sort().toString();
	return result;
}

function commonCheckBox(obj) {
	if($(obj).attr("check") === "checked") {
		$(obj).attr("check", "unchecked");
		$(obj).addClass("common_checkbox_unchecked").removeClass("common_checkbox_checked");
	}else{
		$(obj).attr("check", "checked");
		$(obj).addClass("common_checkbox_checked").removeClass("common_checkbox_unchecked");
	}
}

function commonRadio(obj) {
	var name = $(obj).attr('radioName');
	$("span[radioName='"+name+"']:not(check='checked')").attr("check", "unchecked")
		.addClass("common_radio_unchecked").removeClass("common_radio_checked");
	$(obj).attr("check", "checked");
	$(obj).addClass("common_radio_checked").removeClass("common_radio_unchecked");
}

/**
 * Handle ajax exception 
 * @param data
 * @return
 */
function handleException(data, finalHandleFunc) {
	if (data&&data.errorCodeId) {
		//Server Error
		if (data.flag == "1") {
			if (data.errorCodeId == "E0001") {
				window.location.href=$("#basePath").val()+"serverError";
			}
			if (data.errorCodeId == "E0002") {
				window.location.href=$("#basePath").val()+"serverValidationError";
			}
		}
		//Data Warning
		if (data.flag == "0") {
			initialErrorMsgBar(data.errorMessage, finalHandleFunc);
		}
		return false;
	}
	return true;
}

/**
 * Data warning pop-up
 * @param errorMsg
 * @param finalHandleFunc
 * @return
 */
function initialErrorMsgBar(errorMsg, finalHandleFunc) {
	if(!($("#erroMsgDialog").length > 0)){
		var confirmHtml = '<div id="erroMsgDialog">'+
						  	'<div class="confirmContent">'+
						  	'</div>'+
						  '</div>';
		$('body').append(confirmHtml);
	}
	$("#erroMsgDialog").find('.confirmContent').html(errorMsg);
	$('#erroMsgDialog').dialog({
     	autoOpen: false,
    	width: 350, 
     	modal: true, 
     	resizable: false,
     	draggable: false,
     	title: getBtnNotice(),
     	buttons: [
     	  {
         	  text: getBTNClose(),
         	  id : "greenButton",
         	  click: function() {
        		$(this).dialog("close");
        		if (finalHandleFunc) {
        			finalHandleFunc.call();
        		}
     	      }
     	  } 
     	]
	});
	$("#erroMsgDialog").dialog("open");
}

function checkAutoComplete($node) {
	var validateResult = true;
	$node.find("ul li").each(function(index, node){
		if ($(node).attr("error")) {
			validateResult = false;
			return;
		}
	});
	return validateResult;
}

/**
 * Get server current time.
 * @return
 */
function getServerCurrentTime() {
	var serverCurrentTime;
	$.ajax ({
		type: "POST",
		async: false,
        url: $('#basePath').val()+"getCurrentServerTime",
        success: function(currentTime) {
			serverCurrentTime = currentTime;
        }
	});
	return serverCurrentTime;
}