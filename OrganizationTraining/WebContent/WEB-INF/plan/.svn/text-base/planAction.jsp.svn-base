<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="planStatusControl" uri="/WEB-INF/tld/planStatusControl"%>
<%@ taglib prefix="privilege" uri="/WEB-INF/tld/privilege"%>
				<planStatusControl:control plan="${plan}" controlObjectFlag="1">
	            	<a id="publishButton" class="a_common_button action_part_button">
	                	<span><s:text name="_plan_publish"></s:text></span>
	            	</a>
            	</planStatusControl:control>
            	<planStatusControl:control plan="${plan}" controlObjectFlag="2">
	            	<a id="cancelButton" class="a_common_button action_part_button">
	            		<span><s:text name="_plan_cancel"></s:text></span>
	            	</a>
            	</planStatusControl:control>
            	<planStatusControl:control plan="${plan}" controlObjectFlag="17">
	            	<a id="applyLeavePlan" class="a_common_button action_part_button">
	            		<span><s:text name="_planCourse_apply_leave"></s:text></span>
	            	</a>
            	</planStatusControl:control>
            	<planStatusControl:control plan="${plan}" controlObjectFlag="18">
	            	<a id="backToPlan" class="a_common_button action_part_button">
	            		<span><s:text name="_cancel_leave"></s:text></span>
	            	</a>
            	</planStatusControl:control>
            	<planStatusControl:control plan="${plan}" controlObjectFlag="4">
	            	<a id="traineeAssessPlan" class="a_common_button action_part_button" >
	                	<span><s:text name="_assess_plan"></s:text></span>
	            	</a>
            	</planStatusControl:control>
            	<planStatusControl:control plan="${plan}" controlObjectFlag="5">
	            	<a id="traineeViewPlanAssessment" class="a_common_button action_part_button" >
	                	<span><s:text name="_view_plan_assessment"></s:text></span>
	            	</a>
            	</planStatusControl:control>
            	<planStatusControl:control plan="${plan}" controlObjectFlag="6">
	            	<a id="trainerAssessPlan" class="a_common_button action_part_button" >
	                	<span><s:text name="_assess_plan"></s:text></span>
	            	</a>
            	</planStatusControl:control>
            	<planStatusControl:control plan="${plan}" controlObjectFlag="7">
	            	<a id="trainerViewPlanAssessment" class="a_common_button action_part_button" >
	                	<span><s:text name="_view_plan_assessment"></s:text></span>
	            	</a>
            	</planStatusControl:control>
            	<planStatusControl:control plan="${plan}" controlObjectFlag="8">
	            	<a id="masterAssessTrainer" class="a_common_button action_part_button" >
	                	<span><s:text name="_assess_trainer"></s:text></span>
	            	</a>
            	</planStatusControl:control>
            	<planStatusControl:control plan="${plan}" controlObjectFlag="9">
	            	<a id="masterViewTrainerAssessment" class="a_common_button action_part_button" >
	                	<span><s:text name="_view_trainer_assessment"></s:text></span>
	            	</a>
            	</planStatusControl:control>
            	<planStatusControl:control plan="${plan}" controlObjectFlag="16">
	            	<a id="viewAllAssessment" class="a_common_button action_part_button" >
	                	<span><s:text name="_view_all_assessment"></s:text></span>
	            	</a>
            	</planStatusControl:control>
<!-- split -->
				<privilege:operate operateID="plan[toEditPlan]" creator="${plan.planCreator}" planEndTime="${plan.planExecuteEndTime}">
					<div class="edit_plan_button">
						<a id="editButton" class="a_common_button a_create_button" onmouseup="className='a_common_button a_create_button'" onmousedown="className='a_common_button a_create_button_hover'">
                			<span class="a_common_button_span"><s:text name="_btn_edit"></s:text></span>
           				</a>
					</div>
	  			</privilege:operate>
	  			<privilege:operate operateID="plan[deletePlan]" creator="${plan.planCreator}" special="true">
					<div class="delete_plan_button">
						<a id="deleteButton" class="a_common_button a_cancel_button" onmouseup="className='a_common_button a_cancel_button'" onmousedown="className='a_common_button a_cancel_button_hover'">
                			<span class="a_common_button_span"><s:text name="_btn_delete"></s:text></span>
            			</a>
					</div>
	  			</privilege:operate>



