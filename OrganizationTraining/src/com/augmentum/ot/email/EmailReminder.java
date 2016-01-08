package com.augmentum.ot.email;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TimerTask;

import javax.servlet.ServletContext;

import org.apache.log4j.Logger;

import com.augmentum.ot.dataObject.constant.FlagConstants;
import com.augmentum.ot.dataObject.constant.JsonKeyConstants;
import com.augmentum.ot.dataObject.constant.LogConstants;
/**
 * 
 * Send Email Task
 *
 * @version V1.0, 2012-11-26
 */
public class EmailReminder extends TimerTask {
    private ServletContext servletContext;
    private static Logger logger = Logger.getLogger(EmailReminder.class);

    public EmailReminder(ServletContext context) {
        this.servletContext = context;
    }

    @SuppressWarnings("unchecked")
	@Override
    public void run() {
		Map<Integer, Long> planCoursesMap = (Map<Integer, Long>)servletContext
				.getAttribute(JsonKeyConstants.PLAN_COURSE_REMIND);
		Map<Integer, Long> planSessionsMap = (Map<Integer, Long>)servletContext
				.getAttribute(JsonKeyConstants.PLAN_SESSION_REMIND);
		
		List<Integer> sendedPlanCourseKeysList = new ArrayList<Integer>();
		List<Integer> sendedPlanSessionKeysList = new ArrayList<Integer>();
		try {
			//Course list which need to send email  
			for (Integer key : planCoursesMap.keySet()) { 
				if (new Date().getTime() >= planCoursesMap.get(key)) {
					logger.info(LogConstants.message("Send Remind planCourse id=#", key));
					sendedPlanCourseKeysList.add(key);
					EmailUtil.sendEmailForTraineesAndTrainerByCourseId(key, FlagConstants.PLAN_COURSE);
				}
			}
		}catch(Exception e){
			//
		}
		
		for (Integer key : sendedPlanCourseKeysList) {
			planCoursesMap.remove(key);
		}
		
		try {
			//Session list which need to send email 
			for (Integer key : planSessionsMap.keySet()) {
				if (new Date().getTime() >= planSessionsMap.get(key)) {
					logger.info(LogConstants.message("Send Remind planSession id=#", key));
					sendedPlanSessionKeysList.add(key);
					EmailUtil.sendEmailForTraineesAndTrainerByCourseId(key, FlagConstants.PLAN_SESSION);
				}
			}
		}catch(Exception e) {
			//
		}
		
		for (Integer key : sendedPlanSessionKeysList) {
			planSessionsMap.remove(key);
		}
        
    }
}
