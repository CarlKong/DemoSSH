package com.augmentum.ot.dataObject.constant;

/**
 * 
 *  json or map key constants
 *
 */
public class JsonKeyConstants {
	
    public static final String OPERATION_FLAG = "operationFlag";
    public static final String OPERATION = "operation";
    
	public static final String JSON_OBJECT = "jsonObject";
	public static final String ERROR_CODE_RESULT = "errorCodeResult";
	public static final String ERR = "err";
    
	// login keys
	public static final String HAS_LOGIN = "hasLogin";
	public static final String AUTHORITY_VALUE = "authorityValue";
	public static final String LOGIN_EMPLOYEE = "loginEmployee";
	public static final String EMPLOYEE_ROLE_NAMES = "employeeRoleNames";
	
	// page keys
	public static final String COUNT = "count";
	public static final String TOTAL_PAGE = "totalPage";
	public static final String TOTAL_RECORD = "totalRecord";
	public static final String PAGE_NOW = "pageNow";
	public static final String PAGE_SIZE = "pageSize";
	public static final String PAGE_SIZES_JSON = "pageSizesJson";
	public static final String PAGE_SIZE_INT = "pageSizeInt";
	public static final String FIELDS_DATA = "fieldsData";
	
	// course / plan keys
	public static final String NAMES = "names";
	public static final String COURSE_TYPES = "courseTypes";
	public static final String COURSE_TAGS = "courseTags";
	public static final String PLAN_TYPES = "planTypes";
	public static final String PLAN_TAGS = "planTags";
	public static final String ATTACHMENTS = "attachments";
	public static final String FIELDS_JSON = "fieldsJson";
	
	public static final String COURSE_NAME = "courseName";
	public static final String PLAN_INFO ="planInfo";
	public static final String COURSE_INFO = "courseInfo";
	public static final String SESSION_INFO = "sessionInfo";
	public static final String COURSE_NAME_HAS_TAG = "courseNameHasTag";
	
	public static final String PLAN = "plan";
	public static final String PLAN_ID = "planId";
	public static final String PLAN_LIST = "planList";
	public static final String PLAN_COURSE_LIST = "planCourseList";
	
	public static final String ACTUAL_COURSE_LIST = "actualCourseList";
	public static final String ACTUAL_COURSE_ROW_COUNT = "actualCourseRowCount";
	public static final String ACRUAL_COURSES = "actualCourses";
	public static final String COURSE_TARGET_TRAINEE = "courseTargetTrainee";
    public static final String ACTUAL_COURSE_ID = "actualCourseId";
    public static final String ACTUAL_COURSE_NAME = "actualCourseName";
    public static final String ACTUAL_COURSE_PREFIX_ID = "actualCoursePrefixId";
    public static final String ACTUAL_COURSE_ROOM_NUMBER = "actualCourseRoomNumber";
    public static final String ACTUAL_COURSE_DATE = "actualCourseDate";
    public static final String ACTUAL_COURSE_TIME = "actualCourseTime";
    public static final String ACTUAL_COURSE_STATUS = "actualCourseStatus";
    public static final String ACTUAL_COURSE_TRAINER = "actualCourseTrainer";
    public static final String ACTUAL_COURSE_TRAINEE_NUMBER = "actualCourseTraineeNumber";
	
    public static final String PLAN_COURSE_REMIND = "planCoursesRemindList";
	public static final String PLAN_SESSION_REMIND = "planSessionsRemindList";
	
	public static final String PLAN_COURSE_ID = "planCourseId";
	public static final String TITLE = "title";
	public static final String CREATOR = "creator";
	public static final String START_TIME = "startTime";
	public static final String DATA_LIST = "dataList";
	
    public static final String TRAINEE_ID = "traineeId";
    public static final String TRAINEE_NAME = "traineeName";
    public static final String TRAINEE_PREFIX_ID = "traineePrefixId";
    public static final String TRAINEE_LIST = "traineeList";
    public static final String TRAINER_ID = "trainerId";
    public static final String TRAINER_NAME = "trainerName";
    public static final String TRAINER_LIST = "trainerList";
    public static final String ACTUAL_COURSE_LIST_OF_TRAINER = "actualCourseListOfTrainer";
    public static final String MASTER_NAME = "masterName";
    public static final String MASTER_ID = "masterId";
	
    // assessment part keys
    public static final String ASSESSMENT_FOR_SHOW = "assessmentForShow";
    public static final String TRAINEE_2_TRAINER = "trainee2Trainer";
    public static final String TRAINEE_2_COURSE = "trainee2Course";
    
    public static final String ATTEND = "attended";
    public static final String LATE = "late";
    public static final String LEAVE = "leave";
    public static final String ABSENT = "absent";
    
	public static String TYPE_FLAG = "typeFlag";
	public static String ASSESS_IS_DELETED = "assessIsDeleted";
	public static final String IS_IGNORE = "isIgnore";
	public static final String HAS_BEEN_ASSESSED = "hasBeenAssessed";
	public static final String NEED_ASSESSMENT = "needAssessment";
	public static final String NEED_ASSESSMENT_VALUE = "1";
	public static final String NOT_NEED_ASSESSMENT_VALUE = "0";
    
    public static final String ASSESSMENT_ITEM_LIST = "assessmentItemList";
    public static final String ASSESSMENT_INFO_MAP = "assessmentInfoMap";
    public static final String OPTIONAL_ITEM_MAP = "optionalItemMap";
    public static final String COURSE_COMMENTS = "courseComments";
    
    public static final String PLAN_AND_ACTUAL_COURSE_PREFIX_ID = "planAndActualCoursePrefixId";
    public static final String ATTENDENCE_LOGS = "attendenceLogs";
    public static final String BEHAVIOR_SCORE = "behaviorScore";
    public static final String HOMEWORK_SCORE = "homeworkScore";
    public static final String COMMENTS_AND_SUGGESTIONS = "commentsAndSuggestions";
    public static final String ASSESSMENT_LIST = "assessmentList";
    public static final String ASSESSMENT_PAGE = "assessmentPage";
    public static final String ASSESSMENT_ITEM_AVERAGE_RATE_LIST = "assessmentItemAverageScoreAndRateList";
    public static final String SCORE_LIST = "scoreList";
    public static final String ASSESSMENT_EXCEPT_ITEMTYPE = "assessItemType";
    public static final String NOT_ASSESSED_EMPLOYEE_LIST = "notAssessedEmployeeList";
    public static final String RATE = "rate";
    public static final String PLAN_NO_COURSE = "noCourse";
    public static final String ASSESSMENT_NO_DATA = "noData";
    
    public static final String[] TRAINER_PLANCOURSE_EXCLUDES = {"traineeNumber", "projectList", "customColumnsList", "customPageSizeList", "planEmployeeMapList", "actualCourseList", 
    	"leaveList", "courseType", "plan", "roleLevelsForEmployee", "courseList", "attachments", "employeeList", "leaveNoteList" };
    public static final String[] ACTUAL_COURSE_NEED_ASSESSMENT_EXCLUDES = {"leaveNoteList", "courseStartTime", "courseEndTime", "courseType", "plan", "attachments", "employeeList", "traineeNumber", "traineeNumberByEmployee", "couresEndTimeForSearch"};
    
    public static final String NONE_AVG_SCORE = "none";
    public static final String RATE_SPLIT = "/";
    
    public static final String CURRENT_TIME = "currentTime";

}
