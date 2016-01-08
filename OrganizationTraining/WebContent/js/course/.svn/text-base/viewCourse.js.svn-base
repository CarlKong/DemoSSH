var course_id = $('#course_id').val();
var prefixIDValue = $('#course_prefixIDValue').val();
var operationFlag = $('#operationFlag').val();

$(document).ready(function(){
	// attachment
	courseViewAttachment();
	// message bar
	setMessageBar();
    // button operation
    $('#editButton').click(function(){
    	window.location = $('#basePath').val()+"course/toEditCourse?courseId="+course_id;
    });
    $('#deleteButton').click(function(){
    	setConfirmBar();
		return false;
    });
    $('.course_previous').each(function(){
    	$(this).click(function(){
    		goPrevious();
    	});
    });
    $('.course_next').each(function(){
    	$(this).click(function(){
    		goNext();
    	});
    });
    $('.course_back_to_search').each(function(){
    	$(this).click(function(){
    		backSearch();
    	});
    });
});

//set Previous and Next
function goPrevious(){
    var form = $('#preornext');
    $("#forChangeLocaleFlag").val(false);
    form.attr("action", "viewPreviousRecordDetail");
    form.submit();
}

function goNext(){
    var form = $('#preornext');
    $("#forChangeLocaleFlag").val(false);
    form.attr("action", "viewNextRecordDetail");
    form.submit();
}

// set Back Search
function backSearch(){
    var form = $('#preornext');
    form.attr("action", "preSearchCourse");
    form.submit();
}

//set message bar information
function setMessageBar() {
	var messageBar_create = $('#messageBar_create').html();
	if ("create" == operationFlag) {
		$("#messageBar").messageBar({
			responseMessage: messageBar_create+" ",
    	    itemId: prefixIDValue,
    	    top: 100
		});
	}
	clearOperation();
}
function clearOperation() {
	$.ajax({
        type: "POST",
        url: $('#basePath').val()+"course/clearOperation",
        data: {},
        success: function() {
        }
     });
}

//set confirm bar operation
function setConfirmBar() {
	var confirmContent = getCourseDeleteMessage();
	initialConfirmBar(confirmContent, toDeleteCourse);
}

function toDeleteCourse() {
	$.ajax({
		type: "POST",
        url: $('#basePath').val()+"course/deleteCourse",
        data: {
			"courseId" : course_id
		},
        success: function(errorCode) {
			if(handleException(errorCode)){
				window.location = $('#basePath').val()+'course/course_searchCourse?operationFlag=delete&prefixIDValue='+prefixIDValue;
			};
        }
	});
}
