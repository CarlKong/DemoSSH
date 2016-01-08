$(document).ready(function(){
    $('.header_navigation_button').mouseenter(function(){
        $(this).find('ul').css({'visibility':'visible'});
    });
    $('.header_navigation_button').mouseleave(function(){
        $(this).find('ul').css({'visibility':'hidden'});
    });
    $('.header_navigation_create').mouseenter(function(){
        $(this).find('ul').css({'visibility':'visible'});
        $(this).find('.header_navigation_create_button').attr('class','header_navigation_create_button_hover');
        $(this).find('.show_dropdown').attr('class','change_dropdown');
    });
    $('.header_navigation_create').mouseleave(function(){
        $(this).find('ul').css({'visibility':'hidden'});
        $(this).find('.header_navigation_create_button_hover').attr('class','header_navigation_create_button');
        $(this).find('.change_dropdown').attr('class','show_dropdown');
    });
    $('.message_close img').click(function(){
        $('#header_crumbs').fadeOut('slow');
    });
    $('.header_navigation_button .cursor_hand').click(function(){
    	var href = $(this).next().find('li a').attr('href');
    	if(href != undefined){
    		window.location = href;
    	}
    });
    completeBread();
});

function i18n(basePath, language) {
    $.post(basePath+"toI18n",
            {"local":language},           
            function (responseText, status) {
                if (status == "success") {
                	var tempArray = location.pathname.split("/");
                	var actionName = tempArray[tempArray.length-1];
                	if(actionName == "viewPreviousRecordDetail" || actionName == "viewNextRecordDetail"){
                        form = $("#preornext");
                        if(form.length != 0){
                        	form.attr("action",actionName);
                        	$("#forChangeLocaleFlag").val(true);
                        	form.submit();
                        }else{
                        	location.reload();
                        }
                	} else {
                        location.reload();
                	}
                } else {
                    // Empty. Loading ERROR!!!!
                }
            });
}


/**
 * i18n
 */
function configI18n() {
	var i18nLan = $("#i18nLan").val();
	if(i18nLan == "" || i18nLan == undefined) {
		getLanguageByBrowser();
	}
	if(i18nLan == "en_US") {
		return "EN";
	}
	if(i18nLan == "zh_CN") {
		return "ZH";
	}
}

/**
 * get Language of browser
 */
function getLanguageByBrowser(){
	var language;
	if (navigator.language) {
		language = navigator.language;
	}else {
	    language = navigator.browserLanguage;
	}
	return language;

}
/**
 * fulfill the bread tag, which is defined by ourselves.
 */
function completeBread(){
    var secondNode = $('#header_crumbs').find('a')[1];
    var thirdNode = $('#header_crumbs').find('a')[2];
    var fourthNode = $('#header_crumbs').find('a')[3];
    var coursePrefixId = $('#course_prefixIDValue').val();
    var planPrefixId= $('#plan_prefixIDValue').val();
    var actualCoursePrefixId = $('#actualCoursePrefixId').val();
    var courseFirst;
    var planFirst;
    var roleFirst;
    var $menu = $('#header_navigation').find('.header_navigation_button').find('a');
    
    $menu.each(function() {
        switch ($(this).attr('id')) {
            case 'header_course':
            	courseFirst = $(this).parent().parent().find('li').first().find("a").attr("href");
                break;
            case 'header_plan':
            	planFirst = $(this).parent().parent().find('li').first().find("a").attr("href");
                break;
            case 'header_role':
            	roleFirst = $(this).parent().parent().find('li').first().find("a").attr("href");
                break;
            default:
                break;
        }
    });
    switch ($(secondNode).html()) {
        case 'Course':
            $(secondNode).attr('href', courseFirst);
            break;
        case '课程':
            $(secondNode).attr('href', courseFirst);
            break;
        case 'Plan':
            $(secondNode).attr('href', planFirst);
            break;
        case '计划':
            $(secondNode).attr('href', planFirst);
            break;
        case 'Admin':
            $(secondNode).attr('href', roleFirst);
            break;
        case '管理员':
            $(secondNode).attr('href', roleFirst);
            break;
        default:
            break;
    };
    switch ($(thirdNode).html()) {
        case 'C':
            $(thirdNode).html(coursePrefixId);
            break;
        case 'P':
            $(thirdNode).html(planPrefixId);
            break;
        default:
            break;
    }
    switch ($(fourthNode).html()) {
    	case 'PC': 
    		var planPexId = $("#plan_prefixIDValue").text();
    		var $pervElem = $(fourthNode).prev();
    		$(fourthNode).html(actualCoursePrefixId);
    		$pervElem.text(planPexId);
    		var subplanPexId = planPexId.substring(2,planPexId.length);
    		var thirdAction = $pervElem.attr("href");
    		var parameter = "?planId="+parseInt(subplanPexId, 10);
    		$pervElem.attr("href",thirdAction+parameter);
    		break;
    	case 'A':
    		setAssessmentCrumb(fourthNode);
    		break;
    	default:
            break;
    }
	var firstNode = $('#header_crumbs').find('a:first');
	firstNode.attr("href",$("#basePath").val()+"dashboard/dashboard_dashboard");
}

function setAssessmentCrumb(fourthNode){ 
	var planId = $("#planId").val();
	var planName = $("#planName").val();
	$(fourthNode).html(getAssessmentsConstant());
	//set the href of third element
	var $thirdElem = $(fourthNode).prev();
	$thirdElem.text(planName); 
	var parameter = "?planId="+parseInt(planId);
	var href = $("#basePath").val()+"plan/viewPlanDetail"+parameter;
	$thirdElem.attr("href",href);
	//set the href of second element
	var $secondElem = $thirdElem.prev();
	$secondElem.attr("href",$("#basePath").val()+"plan/plan_searchPlan");
}