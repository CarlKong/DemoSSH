package com.augmentum.ot.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.augmentum.ot.util.SessionObjectUtils;

public class LoginFilter implements Filter{
    public void init(FilterConfig arg0) throws ServletException {
    }

    public void doFilter(ServletRequest request, ServletResponse response,
      FilterChain chain) throws IOException, ServletException {
      
      HttpServletRequest httpRequest=(HttpServletRequest)request;
      HttpServletResponse httpResponse = (HttpServletResponse) response;
      if (SessionObjectUtils.getEmployeeFromSession() == null) {
    	  return ;
      }
      chain.doFilter(httpRequest, httpResponse);
    }

    public void destroy() {
    	//Do nothing
    }
}
