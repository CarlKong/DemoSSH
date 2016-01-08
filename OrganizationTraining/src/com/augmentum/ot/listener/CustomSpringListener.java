package com.augmentum.ot.listener;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import javax.servlet.ServletContextEvent;

import org.apache.log4j.Logger;
import org.springframework.web.context.ContextLoaderListener;

import com.augmentum.ot.dataObject.constant.ConfigureConstants;
import com.augmentum.ot.dataObject.constant.LogConstants;
import com.augmentum.ot.email.InitNeededRemindCoursesAndSession;
import com.augmentum.ot.exception.ServerErrorException;
import com.augmentum.ot.service.ActualCourseService;
import com.augmentum.ot.service.CourseService;
import com.augmentum.ot.service.PlanService;
import com.augmentum.ot.util.BeanFactory;
import com.augmentum.ot.util.PropertyUtils;


/** 
* @ClassName: CustomSpringListener 
* @date 2012-7-25 
* @version V1.0 
*/
public class CustomSpringListener extends ContextLoaderListener {
	private static Logger logger = Logger.getLogger(CustomSpringListener.class);
	private final static String INDEX_INIT_FILE = "initVersion.properties";
	private final static String SUCCESS = "success";
	private final static String FAILURE = "failure";
	
	@Override
	public void contextInitialized(ServletContextEvent event) {
		super.contextInitialized(event);

		String indexStorePath = PropertyUtils.readValue(ConfigureConstants.INDEX_PROPERTIES_PATH, ConfigureConstants.INDEX_BASE_KEY);
		File initVersionFile = new File(indexStorePath + File.separator + INDEX_INIT_FILE);
        if(!initVersionFile.exists()) {
            try {
                initVersionFile.createNewFile();
				CourseService courseService = BeanFactory.getCourseService();
				PlanService planService = BeanFactory.getPlanService();
				ActualCourseService actualCourseService = BeanFactory.getActualCourseService();
				
				String courseIndexesFlag;
				String planIndexesFlag;
				String actualCourseIndexesFlag;
				try {
                    courseService.createAllCourseIndexes();
                    courseIndexesFlag = SUCCESS;
                } catch (ServerErrorException e) {
                	logger.error(LogConstants.exceptionMessage("Initialize course indexes failure") + e);
                    courseIndexesFlag = FAILURE;
                }
				try {
                    planService.createAllPlanIndexes();
                    planIndexesFlag = SUCCESS;
                } catch (ServerErrorException e) {
                	logger.error(LogConstants.exceptionMessage("Initialize plan indexes failure"), e);
                    planIndexesFlag = FAILURE;
                }
				try {
                    actualCourseService.createAllActualCourseIndexes();
                    actualCourseIndexesFlag = SUCCESS;
                } catch (ServerErrorException e) {
                	logger.error(LogConstants.exceptionMessage("Initialize plan course indexes failure"), e);
                    actualCourseIndexesFlag = FAILURE;
                }
				Properties props = new Properties();
				FileOutputStream out = null;
                try {
                    out = new FileOutputStream(indexStorePath + File.separator + INDEX_INIT_FILE);
                } catch (FileNotFoundException e) {
                	logger.error(LogConstants.exceptionMessage("File not find"), e);
                }
				props.setProperty("INDEX_INIT_COURSE", courseIndexesFlag);
				props.setProperty("INDEX_INIT_PLAN", planIndexesFlag);
				props.setProperty("INDEX_INIT_PLAN_COURSE", actualCourseIndexesFlag);
				try {
                    props.store(out, "initialized Indexes !");
                } catch (IOException e) {
                	logger.error(LogConstants.exceptionMessage("Store initalized index info failure"), e);
                }
             } catch (IOException e) {
            	 logger.error(LogConstants.exceptionMessage("Opend file["
            			 + indexStorePath + File.separator + INDEX_INIT_FILE +" failure"), e);
             }
         }
        //Init the need to remind plan courses and session list
        //TODO Margaret
        new InitNeededRemindCoursesAndSession(event.getServletContext()).InitNeededRemindData();
        
	}
}
