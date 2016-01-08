package com.augmentum.ot.action;

import java.util.Locale;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.augmentum.ot.dataObject.constant.ConfigureConstants;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

@Controller("languageAction")
@Scope("prototype")
public class LanguageAction extends ActionSupport{

    private static final long serialVersionUID = -2221447094394009942L;
    private String local;

    public void changeLanguage() {
        ActionContext actionContext = ActionContext.getContext();
        HttpSession session = ServletActionContext.getRequest().getSession();
        HttpServletResponse response = ServletActionContext.getResponse();

        Locale locale = Locale.getDefault();
        if (ConfigureConstants.LOCALE_EN.equals(local)) {
            locale = Locale.US;
            session.setAttribute(ConfigureConstants.LANGUAGE, ConfigureConstants.LOCALE_EN);
        }
        if (ConfigureConstants.LOCALE_ZH.equals(local)) {
            locale = Locale.CHINA;
            session.setAttribute(ConfigureConstants.LANGUAGE, ConfigureConstants.LOCALE_ZH);
        }
        actionContext.setLocale(locale);
        session.setAttribute(ConfigureConstants.WW_TRANS_I18N_LOCALE, locale);
        response.setLocale(locale);
    }

    public String getLocal() {
        return local;
    }

    public void setLocal(String local) {
        this.local = local;
    }
}
