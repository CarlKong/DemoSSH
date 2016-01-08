$(document).ready(function() {
	console.log(hex_md5("abc123_"));
	var language;
    $(".englishBtn").click(function(event){
    	language = "en_US";
    	changeLanguage(language);
    });
    $(".chineseBtn").click(function(event){
    	language = "zh_CN";
    	changeLanguage(language);
    });
    setI18nBtnBackground( $("#lang").val());
	keyPassword('username','showUsername');
	keyPassword('password','showPassword');
	$("#loginFomrSubmit").bind('click', function(){
		clickSubmit();
	});
});


function keyword(id){
	var keyword = $("#"+id);
	keyword.css("color","#808080");
	var defaultValue = keyword.val();
	keyword.focus(function(){
        if(keyword.val()==defaultValue){
        	keyword.val(''); 
        	keyword.css('color','#333');
        }        
   });
    keyword.blur(function(){
       if(keyword.val()==''){
    	   keyword.val(defaultValue); 
    	   keyword.css('color','#808080');
        }      
   });
}

function keyPassword(password,showPassword){
	var keypassWord = $("#"+password);
	var keyShowWord = $("#"+showPassword);
	keyShowWord.css('color','#808080');
	keyShowWord.focus(function(){
		keyShowWord.hide();
		keypassWord.show().focus();
	});
	keypassWord.blur(function(){
		if(keypassWord.val()==''){
			keypassWord.hide();
			keyShowWord.show();
		}
	});
}


function changeLanguage(language){
	$.ajax({
        type: "POST",
        url: "i18n/changeLanguage.action",
        data: 
            "language="+language
        ,
        success : function(data) {
		    parent.location.reload();
	}
 });
}

function setI18nBtnBackground(langFlag){
	if(langFlag == undefined || langFlag == ""){
		 if(getLanguageByBrowe() == "zh-cn"){
			 setChineseBtn();
		     return ;
		 }
		 else{
			 setEnglishBtn();
		     return ;
		 }
	}else if(langFlag == "zh_CN"){
		setChineseBtn();
    	return ;
	}else if(langFlag == "en_US"){
		setEnglishBtn();
    	return ;
	}
}

function getLanguageByBrowe(){
	var language;
	if (navigator.language) {
		language = navigator.language;
	}else {
	    language = navigator.browserLanguage;
	}
	return language;
}
function setChineseBtn(){
	 $(".chineseBtn").css("background","");
	 $(".chineseBtn").attr("disabled","true");
	 $(".chineseBtn").css("cursor","default");
    $(".englishBtn").css("background","url(image/login/english.png) no-repeat");
    $(".englishBtn").removeAttr("disabled");
}

function setEnglishBtn(){
	$(".englishBtn").css("background","");
	$(".englishBtn").attr("disabled","true");
	$(".englishBtn").css("cursor","default");
   $(".chineseBtn").css("background","url(image/login/english.png) no-repeat");
   $(".chineseBtn").removeAttr("disabled");
}

function clickSubmit() {
	$("#passwordEncode").val(hex_md5($("#password").val()));
}