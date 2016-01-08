<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="planStatusControl" uri="/WEB-INF/tld/planStatusControl"%>
				<planStatusControl:control plan="${actualCourse.plan}" actualCourse="${actualCourse}" controlObjectFlag="10">
		            <a id="applyLeaveButton" class="a_common_button action_part_button">
		                <span><s:text name="_planCourse_apply_leave"></s:text></span>
		            </a>
	            </planStatusControl:control>
	            <planStatusControl:control plan="${actualCourse.plan}" actualCourse="${actualCourse}" controlObjectFlag="19">
		            <a id="backToCourse" class="a_common_button action_part_button">
		                <span><s:text name="_cancel_leave"></s:text></span>
		            </a>
	            </planStatusControl:control>
	             <planStatusControl:control plan="${actualCourse.plan}" actualCourse="${actualCourse}" controlObjectFlag="12">
	            	<a id="traineeAssessCourse" class="a_common_button action_part_button">
	                	<span><s:text name="_assess_planCourse"></s:text></span>
	            	</a>
            	</planStatusControl:control>
            	<planStatusControl:control plan="${actualCourse.plan}" actualCourse="${actualCourse}" controlObjectFlag="13">
	            	<a id="traineeViewCourseAssessment" class="a_common_button action_part_button">
	                	<span><s:text name="_view_planCourse_assessment"></s:text></span>
	            	</a>
            	</planStatusControl:control>
            	<planStatusControl:control plan="${actualCourse.plan}" actualCourse="${actualCourse}" controlObjectFlag="14">
	            	<a id="trainerAssessTrainee" class="a_common_button action_part_button">
	            		<span><s:text name="_assess_trainee"></s:text></span>
	            	</a>
            	</planStatusControl:control>
            	<planStatusControl:control plan="${actualCourse.plan}" actualCourse="${actualCourse}" controlObjectFlag="15">
	            	<a id="trainerViewTraineeAssessment" class="a_common_button action_part_button">
	            		<span><s:text name="_view_trainee_assessment"></s:text></span>
	            	</a>
            	</planStatusControl:control>