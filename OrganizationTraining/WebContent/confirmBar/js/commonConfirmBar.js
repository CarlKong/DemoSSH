/**
 * common function to set confirm bar
 * @param confirmContent
 * @param yesFunction
 * @param noFunction
 * @return
 */
function initialConfirmBar(confirmContent, yesFunction, noFunction, yesFunParam) {
	if(!($("#confirmDialog").length > 0)){
		var confirmHtml = '<div id="confirmDialog">'+
						  	'<div class="confirmContent">'+
						  	'</div>'+
						  '</div>';
		$('body').append(confirmHtml);
	}
	$('.confirmContent').html(confirmContent);
	$('#confirmDialog').dialog({
     	autoOpen: false,
    	width: 350, // in this project notice pop-up is 350px
     	modal: true, 
     	resizable: false,
     	draggable: false,
     	title: getBtnNotice(), // method of message.js.jsp
     	buttons: [
     	  {
         	  text: getBtnYes(),
         	  id : "greenButton",
         	  click: function() {
     		    if (yesFunParam) {
     		    	yesFunction(yesFunParam);
     		    } else {
     		    	yesFunction();
     		    }
     		  	
        		$(this).dialog("close");
     	      }
     	  },{
     		  text: getBtnNo(),
     		  id : "grayButton",
     		  click: function() {
     		  	if(noFunction != undefined) {
     		  		noFunction();
     		  	}
       			$(this).dialog("close");
    	      }
     	  }        
     	]
	});
	$("#confirmDialog").dialog("open");
}
