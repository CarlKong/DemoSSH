package com.augmentum.ot.filter;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpSession;

public class RequestWrapper extends HttpServletRequestWrapper{
	private Locale locale = null;
	public RequestWrapper(HttpServletRequest request) {
		super(request);
		HttpSession session = request.getSession();
		locale = (Locale)session.getAttribute("WW_TRANS_I18N_LOCALE");
	}
	/**
	 * struts2 Bug fixed
	 */
	@Override
	public String getHeader(String name) {
		String value = super.getHeader(name);
		if ("Accept-Language".equals(name) && locale != null) {   
            value = locale.getLanguage() + "_" + locale.getCountry()   
                    + value.substring(6, value.length());   
        }  
        return value; 
	}
	@Override
	public Locale getLocale() {
		if(locale != null){
			return locale;
		}
		return super.getLocale();
	}
}
