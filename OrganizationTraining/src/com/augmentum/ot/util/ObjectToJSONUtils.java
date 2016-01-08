package com.augmentum.ot.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.augmentum.ot.dataObject.constant.DateFormatConstants;
import com.augmentum.ot.dataObject.constant.FlagConstants;
import com.augmentum.ot.dataObject.constant.JsonKeyConstants;
import com.augmentum.ot.dataObject.constant.RoleNameConstants;
import com.augmentum.ot.model.ActualCourse;
import com.augmentum.ot.model.Employee;
import com.augmentum.ot.model.PlanEmployeeMap;

/**
 * 
 * @version 0.1 2012-10-10
 *
 */
public abstract class ObjectToJSONUtils {
    
    /**
     * Get the plan course and plan session JSON according to role.
     * 
     * @param planCourseList
     * @param isForTrainer
     * @return
     */
    public static String getActualCourseJSONForDashBoard(List<ActualCourse> actualCourseList, String role) {
        String planCourseJson = "";
        if (actualCourseList == null || actualCourseList.size() <= 0 || role == null || role.isEmpty()) {
            planCourseJson = "[]";
        } else {
            JSONArray planCourseArray = new JSONArray();
            JSONObject planCourseItem = null;
            for (ActualCourse actualCourse: actualCourseList) {
                planCourseItem = new JSONObject();
                /*
                 * 1. plan course name.
                 * 2. plan course data.
                 * 3. plan course time.
                 * 4. plan course room number.
                 * 5. plan course trainer.
                 * 6. plan course status.
                 * 7. plan course id.
                 * 8. plan course prefix id.
                 */
                String actualCourseName = (actualCourse.getCourseName() != null)? actualCourse.getCourseName(): "&nbsp;";
                String actualCourseRoomNumber = (actualCourse.getCourseRoomNum() != null)? actualCourse.getCourseRoomNum(): "&nbsp;";
                Integer actualCourseId = actualCourse.getActualCourseId();
                String actualCoursePrefixId = actualCourse.getPrefixIdValue();
                String actualCourseTrainer = "";
                Integer actualCourseTraineeNumber = 0;
                if (RoleNameConstants.TRAINEE.equals(role)) {
                    actualCourseTrainer = (actualCourse.getCourseTrainer() != null)? actualCourse.getCourseTrainer(): "&nbsp;";
                } else if (RoleNameConstants.TRAINER.equals(role)) {
                    actualCourseTraineeNumber = actualCourse.getTraineeNumber();
                }
                String actualCourseDate = "";
                String actualCourseTime = "";
                String actualCourseStatus = actualCourse.getStatus();
                
                Date actualCourseStartTime = actualCourse.getCourseStartTime();
                Date actualCourseEndTime = actualCourse.getCourseEndTime();
                if (actualCourseStartTime == null && actualCourseEndTime == null) {
                    // The date and time are not confirmed, do nothing.
                } else {
                    if (actualCourseStartTime != null) {
                        // Confirm the date and start time.
                        actualCourseDate = DateHandlerUtils.dateToString(DateFormatConstants.YYYY_MM_DD_, actualCourseStartTime);
                        actualCourseTime = DateHandlerUtils.dateToString(DateFormatConstants.HH_MM, actualCourseStartTime);
                        actualCourseTime += "-";
                    }
                    if (actualCourseEndTime != null) {
                        if (actualCourseDate == null || actualCourseDate.isEmpty()) {
                            actualCourseDate = DateHandlerUtils.dateToString(DateFormatConstants.YYYY_MM_DD_, actualCourseEndTime);
                        }
                        if (actualCourseTime == null || actualCourseTime.isEmpty()) {
                            // The planCourseTime will like "-endTime".
                            actualCourseTime = "-" + DateHandlerUtils.dateToString(DateFormatConstants.HH_MM, actualCourseEndTime);
                        } else {
                            actualCourseTime += DateHandlerUtils.dateToString(DateFormatConstants.HH_MM, actualCourseEndTime);
                        }
                    }
                    
                }
                
                planCourseItem.put(JsonKeyConstants.ACTUAL_COURSE_ID, actualCourseId);
                planCourseItem.put(JsonKeyConstants.ACTUAL_COURSE_PREFIX_ID, actualCoursePrefixId);
                planCourseItem.put(JsonKeyConstants.ACTUAL_COURSE_NAME, actualCourseName);
                planCourseItem.put(JsonKeyConstants.ACTUAL_COURSE_ROOM_NUMBER, actualCourseRoomNumber);
                planCourseItem.put(JsonKeyConstants.ACTUAL_COURSE_DATE, actualCourseDate);
                planCourseItem.put(JsonKeyConstants.ACTUAL_COURSE_TIME, actualCourseTime);
                planCourseItem.put(JsonKeyConstants.ACTUAL_COURSE_STATUS, actualCourseStatus);
                if (RoleNameConstants.TRAINEE.equals(role)) {
                    planCourseItem.put(JsonKeyConstants.ACTUAL_COURSE_TRAINER, actualCourseTrainer);
                } else if (RoleNameConstants.TRAINER.equals(role)) {
                    planCourseItem.put(JsonKeyConstants.ACTUAL_COURSE_TRAINEE_NUMBER, actualCourseTraineeNumber);
                }
                planCourseArray.add(planCourseItem);
            }
            planCourseJson = planCourseArray.toString();
        }
        return planCourseJson;
    }
    
    /**
     * Make the data in map to JSON object.
     * 
     * @param map
     * @return  The JSON object.
     */
    public static JSONObject transformMapToJson(Map<String, Object> map) {
    	JSONObject jsonObject = new JSONObject();
        if (map != null && !map.isEmpty()) {
            for (Entry<String, Object> entry: map.entrySet()) {
                if (entry != null) {
                    jsonObject.put(entry.getKey(), entry.getValue());
                }
            }
        }
        return jsonObject;
    }
    
    /**
     * Get the JSON object for view assessment to trainee.
     * The JSON object contains planCourseList and traineeList.
     * 
     * @param planCourseList
     * @param planEmployeeMapList
     * @param planPrefixId
     * @return  The JSON object.
     */
    public static JSONObject getJSONObjectForViewAssessmentToTrainee(String planPrefixId, List<ActualCourse> actualCourseList, 
            Set<PlanEmployeeMap> planEmployeeMapList) {
        JSONObject jsonObject = new JSONObject();
        /*
         * 1. Deal with the planCourseList to JSON array.
         * 2. Deal with the planEmployeeMapList to JSON array.
         * 3. Make up JSON object.
         */
        
        // Deal with the planCourseList to JSON array, each JSON item just contains planCourseId and planCourseName.
        JSONArray planCourseJsonArray = null;
        if (actualCourseList != null && !actualCourseList.isEmpty()) {
            planCourseJsonArray = new JSONArray();
            JSONObject planCourseJsonObject = null;
            // The planCourseId and planCourseName
            Integer actualCourseId = null;
            String actualCourseName = null;
            String planAndActualCoursePrefixId = null;
            for (ActualCourse actualCourse: actualCourseList) {
                if (actualCourse != null) {
                	actualCourseId = actualCourse.getActualCourseId();
                	actualCourseName = actualCourse.getCourseName();
                    planAndActualCoursePrefixId = planPrefixId + "_" + actualCourse.getPrefixIdValue();
                    
                    // Add the data into planCourseJsonObject.
                    planCourseJsonObject = new JSONObject();
                    planCourseJsonObject.put(JsonKeyConstants.ACTUAL_COURSE_ID, actualCourseId);
                    planCourseJsonObject.put(JsonKeyConstants.ACTUAL_COURSE_NAME, actualCourseName);
                    planCourseJsonObject.put(JsonKeyConstants.PLAN_AND_ACTUAL_COURSE_PREFIX_ID, planAndActualCoursePrefixId);;
                    
                    planCourseJsonArray.add(planCourseJsonObject);
                }
            }
        }
        
        // Deal with the planEmployeeMapList to JSON array.
        JSONArray traineeJsonArray = null;
        List<Integer> traineeExistList = null;
        if (planEmployeeMapList != null && !planEmployeeMapList.isEmpty()) {
            traineeJsonArray = new JSONArray();
            traineeExistList = new ArrayList<Integer>();
            JSONObject traineeJsonObject = null;
            Employee employee = null;
            // The traineeId and traineeName
            Integer traineeId = null;
            String traineeName = null;
            String traineePrefixId = null;
            for (PlanEmployeeMap planEmployeeMap: planEmployeeMapList) {
                if (planEmployeeMap != null 
                        && new Integer(FlagConstants.UN_DELETED).equals(planEmployeeMap.getPlanEmployeeIsDeleted())) {
                    // The planEmployeeMap is not null and is not be deleted.
                    employee = planEmployeeMap.getEmployee();
                    if (employee != null) {
                        traineeId = employee.getEmployeeId();
                        if (traineeExistList.contains(traineeId)) {
                            // This trainee is existed. Do nothing.
                        } else {
                            // Add the traineeId to traineeExistList.
                            traineeExistList.add(traineeId);
                            
                            traineeName = employee.getAugUserName();
                            traineePrefixId = employee.getAugEmpId();
                            
                            // Add the data into employee
                            traineeJsonObject = new JSONObject();
                            traineeJsonObject.put(JsonKeyConstants.TRAINEE_ID, traineeId);
                            traineeJsonObject.put(JsonKeyConstants.TRAINEE_NAME, traineeName);
                            traineeJsonObject.put(JsonKeyConstants.TRAINEE_PREFIX_ID, traineePrefixId);
                            
                            traineeJsonArray.add(traineeJsonObject);
                        }
                    }
                }
            }
        }
        
        // Put the data into JSON object.
        jsonObject.put(JsonKeyConstants.PLAN_COURSE_LIST, planCourseJsonArray);
        jsonObject.put(JsonKeyConstants.TRAINEE_LIST, traineeJsonArray);
        
        return jsonObject;
    }
    
    /**
     * Get the assessment JSON object according to planTraineeAssessmentMap.
     * 
     * @param planTraineeAssessmentMap
     * @return  The JSON object.
     */
    @SuppressWarnings("unchecked")
    public static JSONObject getJSONObjectAssessmentForPlanAndTraineeId(Map<String, Object> planTraineeAssessmentMap) {
        JSONObject jsonObject = new JSONObject();
        /*
         * 1. Deal with assessmentMap
         * 2. Add the planTraineeAssessmentMap key-value to JSON object.
         */
        
        if (planTraineeAssessmentMap != null && !planTraineeAssessmentMap.isEmpty()) {
            // Deal with assessment map.
            JSONArray assessmentJsonArray = null;
            List<Map<String, Object>> assessmentList = (List<Map<String, Object>>) planTraineeAssessmentMap.get(JsonKeyConstants.ASSESSMENT_LIST);
            if (assessmentList != null && !assessmentList.isEmpty() ) {
                assessmentJsonArray = new JSONArray();
                
                JSONObject assessmentItemJsonObject = null;
                String planCourseName = null;
                String attendenceLogs = null;
                Float behaviorScore = null;
                Float homeworkScore = null;
                String commentsAndSuggestions = null;
                for(Map<String, Object> assessmentItemMap: assessmentList) {
                    if (assessmentItemMap != null && !assessmentItemMap.isEmpty()) {
                        
                        assessmentItemJsonObject = new JSONObject();
                        
                        planCourseName = (String) assessmentItemMap.get(JsonKeyConstants.ACTUAL_COURSE_NAME);
                        attendenceLogs = (String) assessmentItemMap.get(JsonKeyConstants.ATTENDENCE_LOGS);
                        behaviorScore = (Float) assessmentItemMap.get(JsonKeyConstants.BEHAVIOR_SCORE);
                        homeworkScore = (Float) assessmentItemMap.get(JsonKeyConstants.HOMEWORK_SCORE);
                        commentsAndSuggestions = (String) assessmentItemMap.get(JsonKeyConstants.COMMENTS_AND_SUGGESTIONS);
                        
                        // Put the data into assessmentItemJsonObject.
                        assessmentItemJsonObject.put(JsonKeyConstants.ACTUAL_COURSE_NAME, planCourseName);
                        assessmentItemJsonObject.put(JsonKeyConstants.ATTENDENCE_LOGS, attendenceLogs);
                        assessmentItemJsonObject.put(JsonKeyConstants.BEHAVIOR_SCORE, behaviorScore);
                        assessmentItemJsonObject.put(JsonKeyConstants.HOMEWORK_SCORE, homeworkScore);
                        assessmentItemJsonObject.put(JsonKeyConstants.COMMENTS_AND_SUGGESTIONS, commentsAndSuggestions);
                        
                        assessmentJsonArray.add(assessmentItemJsonObject);
                    }
                }
            }
            // Replace the value of key assessmentMap
            planTraineeAssessmentMap.put(JsonKeyConstants.ASSESSMENT_LIST, assessmentJsonArray);
            
            // Add the planTraineeAssessmentMap key-value to JSON object.
            jsonObject = transformMapToJson(planTraineeAssessmentMap);
        }
        
        return jsonObject;
    }
    
    /**
     * Get the assessment JSON object according to planCourseAssessmentMap.
     * 
     * @param planCourseAssessmentMap
     * @return  The JSON object.
     */
    @SuppressWarnings("unchecked")
    public static JSONObject getJSONObjectAssessmentForPlanAndPlanCourseId(Map<String, Object> planCourseAssessmentMap) {
        JSONObject jsonObject = new JSONObject();
        /*
         * 1. Deal with assessmentMap.
         * 2. Add the planTraineeAssessmentMap key-value to JSON object.
         */
        
        if (planCourseAssessmentMap != null && !planCourseAssessmentMap.isEmpty()) {
            // Deal with assessmentMap.
            JSONArray assessmentJsonArray = null;
            List<Map<String, Object>> assessmentList = (List<Map<String, Object>>) planCourseAssessmentMap.get(JsonKeyConstants.ASSESSMENT_LIST);
            if (assessmentList != null && !assessmentList.isEmpty()) {
                assessmentJsonArray = new JSONArray();
                
                JSONObject assessmentItemJsonObject = null;
                String traineeName = null;
                String attendenceLogs = null;
                Float behaviorScore = null;
                Float homeworkScore = null;
                String commentsAndSuggestions = null;
                
                for(Map<String, Object> assessmentItemMap: assessmentList) {
                    if (assessmentItemMap != null && !assessmentItemMap.isEmpty()) {
                        
                        assessmentItemJsonObject = new JSONObject();
                        
                        traineeName = (String) assessmentItemMap.get(JsonKeyConstants.TRAINEE_NAME);
                        attendenceLogs = (String) assessmentItemMap.get(JsonKeyConstants.ATTENDENCE_LOGS);
                        behaviorScore = (Float) assessmentItemMap.get(JsonKeyConstants.BEHAVIOR_SCORE);
                        homeworkScore = (Float) assessmentItemMap.get(JsonKeyConstants.HOMEWORK_SCORE);
                        commentsAndSuggestions = (String) assessmentItemMap.get(JsonKeyConstants.COMMENTS_AND_SUGGESTIONS);
                        
                        // Put the data into assessmentItemJsonObject.
                        assessmentItemJsonObject.put(JsonKeyConstants.TRAINEE_NAME, traineeName);
                        assessmentItemJsonObject.put(JsonKeyConstants.ATTENDENCE_LOGS, attendenceLogs);
                        assessmentItemJsonObject.put(JsonKeyConstants.BEHAVIOR_SCORE, behaviorScore);
                        assessmentItemJsonObject.put(JsonKeyConstants.HOMEWORK_SCORE, homeworkScore);
                        assessmentItemJsonObject.put(JsonKeyConstants.COMMENTS_AND_SUGGESTIONS, commentsAndSuggestions);
                        
                        assessmentJsonArray.add(assessmentItemJsonObject);
                    }
                }
            }
            // Replace the value of key assessmentMap
            planCourseAssessmentMap.put(JsonKeyConstants.ASSESSMENT_LIST, assessmentJsonArray);
            
            // Add the planTraineeAssessmentMap key-value to JSON object.
            jsonObject = transformMapToJson(planCourseAssessmentMap);
        }
        
        return jsonObject;
    }
    
    /**
     * Get the trainer JSON object.
     * 
     * @param trainerList
     * @return  The JSON object.
     */
    @SuppressWarnings("unchecked")
    public static JSONObject getJSONObjectForTrainerAndActualCourseList(Map<String, Object> trainerAndPlanCourseMap) {
        JSONObject jsonObject = new JSONObject();
        /*
         * 1. Deal with the trainerList, get the trainerId and trainerName for each item of trainerList.
         * 2. Deal with the planCourseList, get the planCourseId and planCourseName for each item of planCourseList.
         * 3. Put data into jsonObject.
         */
        if (trainerAndPlanCourseMap != null && !trainerAndPlanCourseMap.isEmpty()) {
            // 1. Deal with the trainerList, get the trainerId and trainerName for each item of trainerList.
            List<Employee> trainerList = (List<Employee>) trainerAndPlanCourseMap.get(JsonKeyConstants.TRAINER_LIST);
            if (trainerList != null && !trainerList.isEmpty()) {
                JSONArray trainerJsonArray = new JSONArray();
                
                JSONObject trainerItemJsonObject = null;
                Integer trainerId = null;
                String trainerName = null;
                for (Employee trainer: trainerList) {
                    if (trainer != null) {
                        trainerId = trainer.getEmployeeId();
                        trainerName = trainer.getAugUserName();
                        
                        // Put the data into trainer item.
                        trainerItemJsonObject = new JSONObject();
                        trainerItemJsonObject.put(JsonKeyConstants.TRAINER_ID, trainerId);
                        trainerItemJsonObject.put(JsonKeyConstants.TRAINER_NAME, trainerName);
                        
                        trainerJsonArray.add(trainerItemJsonObject);
                    }
                }
                
                jsonObject.put(JsonKeyConstants.TRAINER_LIST, trainerJsonArray);
            }
            
            // 2. Deal with the planCourseList, get the planCourseId and planCourseName for each item of planCourseList.
            List<ActualCourse> actualCourseList = (List<ActualCourse>) trainerAndPlanCourseMap.get(JsonKeyConstants.ACTUAL_COURSE_LIST_OF_TRAINER);
            if (actualCourseList != null && !actualCourseList.isEmpty()) {
                JSONArray actualCourseJsonArray = new JSONArray();
                
                JSONObject actualCourseItemJsonObject = null;
                Integer actualCourseId = null;
                String actualCourseName = null;
                for (ActualCourse actualCourse: actualCourseList) {
                    if (actualCourse != null) {
                    	actualCourseId = actualCourse.getActualCourseId();
                    	actualCourseName = actualCourse.getCourseName();
                        
                        // Put the data into planCourseItemJsonObject.
                    	actualCourseItemJsonObject = new JSONObject();
                    	actualCourseItemJsonObject.put(JsonKeyConstants.ACTUAL_COURSE_ID, actualCourseId);
                    	actualCourseItemJsonObject.put(JsonKeyConstants.ACTUAL_COURSE_NAME, actualCourseName);
                        
                    	actualCourseJsonArray.add(actualCourseItemJsonObject);
                    }
                }
                
                jsonObject.put(JsonKeyConstants.ACTUAL_COURSE_LIST_OF_TRAINER, actualCourseJsonArray);
            }
             
        }
        
        return jsonObject;
    }
    
}
