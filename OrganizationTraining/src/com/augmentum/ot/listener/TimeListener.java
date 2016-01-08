package com.augmentum.ot.listener;

import java.util.Timer;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.augmentum.ot.email.EmailConstant;
import com.augmentum.ot.email.EmailReminder;

/**
 * Application Lifecycle Listener implementation class EmailReminderListener
 * 
 */
public class TimeListener implements ServletContextListener {

    private Timer timer;

    /**
     * @see ServletContextListener#contextInitialized(ServletContextEvent)
     */
    public void contextInitialized(ServletContextEvent event) {
        timer = new Timer(true);
        long delay = 0;
        long period = EmailConstant.EMAIL_TIMER;
        timer.schedule(new EmailReminder(event.getServletContext()), delay, period);

    }

    /**
     * @see ServletContextListener#contextDestroyed(ServletContextEvent)
     */
    public void contextDestroyed(ServletContextEvent event) {
        timer.cancel();
    }

}
