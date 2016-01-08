package com.augmentum.ot.interceptor;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.StrutsStatics;

import com.augmentum.ot.dataObject.constant.ConfigureConstants;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.Interceptor;

public class I18nInterceptor implements Interceptor{


    /**
     * 
     */
    private static final long serialVersionUID = 8782152566505342836L;

    @Override
    public void destroy() {
        
    }

    @Override
    public void init() {
        
    }

    @Override
    public String intercept(ActionInvocation invocation) throws Exception {
        ActionContext ctx = invocation.getInvocationContext();
        HttpServletRequest request= (HttpServletRequest) ctx.get(StrutsStatics.HTTP_REQUEST);
        HttpServletResponse response = ServletActionContext.getResponse();
        String language = request.getParameter(ConfigureConstants.LOCALE);
        HttpSession session = ServletActionContext.getRequest().getSession();
        Locale locale = Locale.getDefault();
        if(ConfigureConstants.LANGUAGE_EN.equals(language)) {
            locale = Locale.US;
            session.setAttribute(ConfigureConstants.LANGUAGE, ConfigureConstants.LOCALE_EN);
        } 
        if(ConfigureConstants.LANGUAGE_ZH.equals(language)){
            locale = Locale.CHINA;
            session.setAttribute(ConfigureConstants.LANGUAGE, ConfigureConstants.LOCALE_ZH);
        }
        ctx.setLocale(locale);
        session.setAttribute(ConfigureConstants.WW_TRANS_I18N_LOCALE, locale);
        request.setAttribute(ConfigureConstants.LOCALE, language);
        response.setLocale(locale);
        return invocation.invoke();
    }

}
