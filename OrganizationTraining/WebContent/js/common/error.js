$(document).ready(function(){
	$('.error_click_home').bind('click', function(){
		window.location.href=$("#basePath").val();
	});
	$('.error_click_search').bind('click', function(){
	    if($('#search').val() == 'course') {
	        window.location.href=$("#basePath").val()+"course/course_searchCourse";
	    }
	    
	    if($('#search').val() == 'plan') {
            window.location.href=$("#basePath").val()+"plan/plan_searchPlan";
        }
	    
	    if($('#search').val() == 'roleList') {
            window.location.href=$("#basePath").val()+"roleList/roleList_roleList";
        }
	});
});