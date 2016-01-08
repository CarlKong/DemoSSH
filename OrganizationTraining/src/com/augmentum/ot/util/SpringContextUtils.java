package com.augmentum.ot.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
/**
 * 
 * Get the applicationContext
 *
 * @version V1.0, 2012-8-24
 */
@Component("springContextUtils")
public class SpringContextUtils implements ApplicationContextAware {
	
	private static ApplicationContext applicationContext;

	
	@Override
	public void setApplicationContext(ApplicationContext arg0)
			throws BeansException {
		SpringContextUtils.applicationContext = arg0;
		
	}


	public static ApplicationContext getApplicationContext() {
		return applicationContext;
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T getBean(String beanName) {
		return (T)applicationContext.getBean(beanName);
	}
	
	

}
