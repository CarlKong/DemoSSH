package com.augmentum.ot.action;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.opensymphony.xwork2.ActionSupport;
/**
 * 
 * Find JSP Page
 *
 * @version V1.0, 2012-8-14
 */
@Component("findPagesAction")
@Scope("prototype")
public class FindPagesAction extends ActionSupport {

	private static final long serialVersionUID = 1L;
	
	public String execute() {
		return SUCCESS;
	}

}
