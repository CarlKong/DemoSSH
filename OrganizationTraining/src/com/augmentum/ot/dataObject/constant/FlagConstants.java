package com.augmentum.ot.dataObject.constant;

/**
 * 
 *  flag constants
 *  include:
 *  1/ DB flags
 *  2/ operation flags or parameters
 *  3/ button flags
 *  4/ Error back message and validation results
 *
 */
public abstract class FlagConstants {
	/**DB flags*/
	public static final int UN_DELETED = 0;
	public static final int IS_DELETED = 1;

	public static final int HAS_ATTACHMENT = 1;
	public static final int NO_ATTACHMENT = 0;
	
	public static final int VISIBLE = 1;
	public static final int UN_VISIBLE = 0;
	
	public static final int IS_COMPLETED = 1;
	public static final int UN_COMPLETED = 0;

	public static final int IS_PUBLISHED = 1;
	public static final int UN_PUBLISHED = 0;

	public static final int IS_CANCELED = 1;
	public static final int UN_CANCELED = 0;

	public static final int NO_HOMEWORK = 0;
	public static final int HAS_HOMEWORK = 1;

	public static final int IS_CERTIFICATED = 1;
	public static final int UN_CERTIFICATED = 0;

	public static final int UN_IGNORE = 0;
	public static final int IS_IGNORE = 1;
	
	public static final Integer NEED_ASSESSMENT = 1;
	public static final Integer UN_NEED_ASSESSMENT = 0;

	public static final String COURSE_NUMBER_PREFIX = "C";
	public static final String PLAN_NUMBER_PREFIX = "PL";
	public static final String PLAN_COURSE_NUMBER_PREFIX = "PC";
	public static final String PLAN_SESSION_NUMBER_PREFIX = "PS";

	public static final int TRAINEE_TO_PLAN = 1;
	public static final int TRAINEE_TO_COURSE = 2;
	public static final int TRAINEE_TO_TRAINER = 3;
	public static final int TRAINING_MASTER_TO_TRAINERS = 4;
	public static final int TRAINER_TO_PLAN = 5;
	public static final int TRAINER_TO_TRAINEES = 6;
	public static final int TRAINER_TO_COURSE = 7;

	public static final int ALL_TRAINEES = 0;
	public static final int IS_ALL_EMPLOYEES = 1;
	public static final int UN_ALL_EMPLOYEES = 0;

	public static final int ATTEND_TYPE_OPTIONAL = 0;
	public static final int ATTEND_TYPE_INVITED = 1;
	public static final int ATTEND_TYPE_SPECIFIC = 2;
	public static final int ATTEND_TYPE_JOIN = 3;
	public static final int ATTEND_TYPE_TRAINER = 4;
	public static final int ATTEND_TYPE_ALL = 5;
	
	public static final String INVITED_PLAN = "Invited";
	public static final String PUBLIC_PLAN = "Public";
	public static final int INVITED_PLAN_ID = 1;
	public static final int PUBLIC_PLAN_ID = 2;
	
	public static final String EMPLOYEE_NAME = "augUserName";
	public static final String EMPLOYEE_FIELDS_EMPLOYEE_ID = "employeeId";
    
    public static final Double ASSESSMENT_ATTENDANCE_ATTEND = 1.0;
    public static final Double ASSESSMENT_ATTENDANCE_LATE = 2.0;
    public static final Double ASSESSMENT_ATTENDANCE_LEAVE_EARLY = 3.0;
    public static final Double ASSESSMENT_ATTENDANCE_ABSENCE = 4.0;

	public static final Integer ASSESSMENT_UNASSESSED = 0;
	public static final Integer ASSESSMENT_HAS_BEEN_ASSESSED = 1;
	public static final Integer ASSESSMENT_NO_HOMEWORK_FLAG = -1;
	public static final Integer ITEM_UNSELECTED_SCORE = -1;
	
	public static String TYPE_FLAG = "typeFlag";
	public static String ASSESS_IS_DELETED = "assessIsDeleted";
	
    /**assessment item id*/
    // Trainee to plan
    public static final Integer ASSESSMENT_ITEM_TYPE_TRAINING_ARRAGEMENT = 1;
    public static final Integer ASSESSMENT_ITEM_TYPE_TRAINING_COURSE = 2;
    // Trainee to planCourse
    public static final Integer ASSESSMENT_ITEM_TYPE_TRAINING_MATERIAL = 3;
    public static final Integer ASSESSMENT_ITEM_TYPE_DIFFICULT_APPROPRIATE_TO_ME = 4;
    public static final Integer ASSESSMENT_ITEM_TYPE_VALUABLE_TO_ME = 5;
    // Trainee to trainer
    public static final Integer ASSESSMENT_ITEM_TYPE_PREPARATION = 6;
    public static final Integer ASSESSMENT_ITEM_TYPE_EXPRESSION = 7;
    public static final Integer ASSESSMENT_ITEM_TYPE_INTERACTION = 8;
    public static final Integer ASSESSMENT_ITEM_TYPE_TIME_MAGAGEMENT = 9;
    // Trainer to plan
    public static final Integer ASSESSMENT_ITEM_TYPE_PLAN_OBJECTIVES = 14;
    public static final Integer ASSESSMENT_ITEM_TYPE_PLAN_TRAINING_ARRAGEMENT = 15;
    public static final Integer ASSESSMENT_ITEM_TYPE_PLAN_TRAINING_COURSE = 16;

    public static final Integer ASSESSMENT_ITEM_TYPE_ATTENDENCE_LOGS = 17;
    public static final Integer ASSESSMENT_ITEM_TYPE_BEHAVIOR = 18;
    public static final Integer ASSESSMENT_ITEM_TYPE_HOMEWORK = 19;
	
	/**operation flags or parameters*/
	public static final String CREATE_OPERATION = "create";
	public static final String SEARCH_OPERATION = "search";
	public static final String EDIT_OPERATION = "edit";
	public static final String DELETE_OPERATION = "delete";
	public static final String PUBLISH_OPERATION = "publish";
	public static final String CANCEL_OPERATION = "cancel";
	public static final String VIEW_MY_OPERATION = "viewMy";
	public static final String SAVE_AS_OPERATION = "saveAs";
	public static final String TO_EDIT_PAGE = "toEditPage";
	public static final String SESSION_SUCCESS = "sessionSuccess";
	public static final String SEARCH_PLAN = "searchPlan";
	public static final String SEARCH_MY_PLAN = "searchMyPlan";
	
	public static final String SPLIT_COMMA = ",";
	public static final String SPLIT_SEMICOLON = ";";
	
	public static final Integer ID_BLANK = 0;
    public static final double ASSESSMENT_DEFAULT_ZERO = 0;
	
	public static final int JOIN = 1;
	public static final int QUIT = 0;
	
	public static final int CAN_JOIN = 1;
	public static final int CAN_NOT_JOIN = 0;
	
	public static final int NO_COURSE = 0;
	public static final int HAVE_COURSE = 1;
	
	public static final Integer IS_OPTIONAL = 1;
	public static final Integer UN_OPTIONAL = 0;
	
	public static final Boolean IS_SAVETO_PLAN_COURSE_EMPLOYEE_MAP = true;
	public static final Boolean UN_SAVETO_PLAN_COURSE_EMPLOYEE_MAP = false;
	
	public static final Integer ACTUAL_COURSE_SEARCH_STATUS_DIVIDED = 0;
	public static final Integer ACTUAL_COURSE_SEARCH_STATUS_NOT_DIVIDED = 1;
	
	public static final int COURSE_ID_NUMBER = 5;
	public static final int PLAN_ID_NUMBER = 5;
	public static final int ACTUAL_COURSE_ID_NUMBER = 5;
	
	public static final int OPERATE_SUCCESS = 1;
	public static final int OPERATE_HAS_DONE = 0;
	public static final int OPERATE_FAILURE = 2;
	
	public static final String STATUS_RED = "red";
	public static final String STATUS_GREEN = "green";
	public static final String STATUS_YELLOW = "yellow";
	public static final String STATUS_GRAY = "gray";
	
	public static final int TRAINEE_LEAVED = 2;
	public static final int TRAINEE_NOT_LEAVED = 1;
	public static final int COURSE_STARTS = 0;
	
    public static final int ASSESSMENT_NO_DATA_VALUE = 1;
    public static final int ASSESSMENT_HAVE_DATA_VALUE = 0;
	
	/**email part flags*/
	public static final int SEND_EMAIL = 1;
	public static final int SEND_TO_MANAGER = 1;
	public static final int DONT_SEND_TO_MANAGER = 0;
	public static final int SEND_ALL_TRAINEE = 1;
	public static final int SEND_MODIFY_TRAINEE = 1;
	public static final int NO_REMIND_EMAIL = 0;
	public static final int PLAN_COURSE = 1;
	public static final int PLAN_SESSION = 0;
	
	/**The assessment expired*/
    public static final String OT_PROPERTIES_PATH = "/ot.properties";
    public static final String EXPIRED_HOUR_NUMBER_KEY = "assessment.expired.time";
    public static final String EXPIRED_SWITCH_KEY = "assessment.expired.switch";
    public static final String EXPIRED_SWITCH_KEY_TRUE = "true";
    public static final String EXPIRED_SWITCH_KEY_FALSE = "false";
    public static final Integer EXPIRED_DEFAULT_HOUR_NUMBER = 24 * 7;
	
	/**button flags*/
	public static final int PUBLISH_BTN = 1;
	public static final int CANCEL_BTN = 2;
	public static final int TRAINEE_ASSESS_PLAN_BTN = 4;
	public static final int TRAINEE_VIEW_PLAN_ASSESSMENT_BTN = 5;
	public static final int TRAINER_ASSESS_PLAN_BTN = 6;
	public static final int TRAINER_VIEW_PLAN_ASSESSMENT_BTN = 7;
	public static final int MASTER_ASSESS_TRAINER_BTN = 8;
	public static final int MASTER_VIEW_TRAINER_ASSESSMENT_BTN = 9;
	public static final int APPLY_LEAVE_BTN = 10;
	public static final int TRAINEE_ASSESS_COURSE_BTN = 12;
	public static final int TRAINEE_VIEW_COURSE_ASSESSMENT_BTN = 13;
	public static final int TRAINER_ASSESS_TRAINEE_BTN = 14;
	public static final int TRAINER_VIEW_TRAINEE_ASSESSMENT_BTN = 15;
	public static final int VIEW_ALL_ASSESSMENT_BTN = 16;
	public static final int TRAINEE_APPLY_LEAVE_PLAN_BTN = 17;
	public static final int TRAINEE_APPLY_BACK_PLAN_BTN = 18;
	public static final int TRAINEE_APPLY_BACK_COURSE_BTN = 19;
	
	/**Error back message and validation results*/
	public static final String VALIDATION_ERROR = "validation_error";
	public static final String VALIDATION_ERROR_JSON = "error_json";
	public static final String ERROR_SERVER = "error_server";
	public static final String ERROR_SERVER_JSON = "error_json";
	public static final String ERROR_404 = "error_404";
	public static final String ERROR_500 = "error_500";
	public static final String DATA_WARNING = "data_warning";
	public static final String DATA_WARNING_JSON = "error_json";
	public static final String NO_ACCESS = "noAccess";
	public static final String RELOGIN = "reLogin";

	public static final Integer PLAN_IS_NULL = 0;
	public static final Integer PLAN_VALIDATEION_RESULT_TRUE = 1;
	public static final Integer PLAN_VALIDATEION_RESULT_FALSE = 2;
	
	public static final String EDIT_PLAN_ACTION = "plan[toEditPlan]";
	
	public static final int REMOVE_ONE_ROLE=0;
	public static final int REMOVE_ALL_ROLE=1;
	
	public static final String AUTHORITY_VALUE= "privilegeValue";
	public static final String ERROR_CODE_MAP = "errorCodeMap";
}
