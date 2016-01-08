(function($){ 
$.fn.messageBar = function(options){
	//These are all default   about yellow bar.
 var defaults = {
		 //responseMessge is the info that the yellow board show.  
		responseMessage:"I am shown by default, id is blank",
		 //itemId is just useful for execute that have id.
		 //such as: create/update/delete 
		itemId:"",
		 //top is length between the message bar and window top-board.
		top:100,
		//height of message board
		height:30,
		//width of message board
		width:610,
		 //isAutoHide used to define whether auto hide in system default time.
		isAutoHide:true,
		 //autoHideTime redefine the system default time for hide. 
	    autoHideTime:"2000",
		// preposition id.
		isPrepositionId:false
		
}; 
var options = $.extend(defaults, options); 
this.each(function(){ 
	var noticeDiv = $(this);
	var t ;
	noticeDiv.css({"display":"none"});
	if (noticeDiv.children().length > 0) {
		noticeDiv.children().remove();
	} 
	var messageStr="";
	if(options.isPrepositionId){
		messageStr = "<div class='message_board'><div class='message_content'><span class='message_id'>"+options.itemId+"</span>"
		   + "<span class='message_notice'>"+options.responseMessage+"</span></div>"
		   + "<div class='close_btn'><span id='close_img'></span></div></div>";
	}else{
		messageStr = "<div class='message_board'><div class='message_content'><span class='message_notice'>"+options.responseMessage+"</span>"
		   + "<span class='message_id'>"+options.itemId+"</span></div>"
		   + "<div class='close_btn'><span id='close_img'></span></div></div>";
	}
	
	noticeDiv.append(messageStr);
	$("#close_img").addClass("close_img");
    var left = (document.body.clientWidth - options.width)/2; 
	var winHeight = document.documentElement.scrollTop+document.body.scrollTop;
	if (winHeight < options.top){
		$(".message_board").css({"top":options.top+"px","left":left+"px", "width":options.width+"px", "height":options.height+"px"});	
	}else{
		$(".message_board").css({"top":winHeight+"px", "left":left+"px","width":options.width+"px", "height":options.height+"px"});
	}
	var imgMargin = ($(".message_board").height()-$("#close_img").height())/2;
	var message_notice_font_size = $(".message_notice").css("font-size");
	var message_id_font_size = $(".message_id").css("font-size");
	var contentMargin = ($(".message_board").height()-parseInt(message_notice_font_size.substring(0,message_notice_font_size.length - 2)))/2;
	var contentIdMargin = (($(".message_board").height()-parseInt(message_id_font_size.substring(0,message_id_font_size.length - 2)))/2)-5;
	var messageContentWidth = options.width - $(".close_btn").width();
	var messageContentWidthPercentage = ((options.width - $(".close_btn").width())/options.width)*100;
	$(".message_content").css({"width":messageContentWidthPercentage+"%"});
	$("#close_img").css({"margin-top":imgMargin+"px"});
	$(".message_notice").css({"margin-top":contentMargin+"px"});
	$(".message_id").css({"margin-top":contentIdMargin+"px"});
	noticeDiv.fadeIn("slow");
	$(".close_btn").mouseover(function(){
		$("#close_img").removeClass();
		$("#close_img").addClass("close_hover_img");
	});
	$(".close_btn").mouseout(function(){
		$("#close_img").removeClass();
		$("#close_img").addClass("close_img");
	});
	$("#close_img").bind("click",function(){clear();});
	
	if (options.isAutoHide){
		t = setTimeout(function(){clear();},options.autoHideTime);
	}
	 //autoHide disable when the mouse over the yellow board.
	noticeDiv.mouseover(function(){
		clearTimeout(t);
	});
	noticeDiv.mouseout(function(){
		if (options.isAutoHide){
			t = setTimeout(function(){clear();},options.autoHideTime);
		}
    });
     // After show clear the yellow board.
	function clear(){
		    noticeDiv.fadeOut("slow",function() {
			noticeDiv.children().remove();
		    });
	}
});
}; 
})(jQuery); 