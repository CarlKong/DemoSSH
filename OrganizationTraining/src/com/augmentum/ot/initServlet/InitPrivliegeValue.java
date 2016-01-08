package com.augmentum.ot.initServlet;

import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.springframework.stereotype.Component;

import com.augmentum.ot.dataObject.ErrorCode;
import com.augmentum.ot.dataObject.constant.FlagConstants;
import com.augmentum.ot.exception.ServerErrorException;
import com.augmentum.ot.util.AuthorityValueUtil;
import com.augmentum.ot.util.ReaderXmlUtils;
@Component
public class InitPrivliegeValue extends HttpServlet{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2705746245967369017L;
	@Override
	public void init() throws ServletException {
		super.init();
		ServletContext servletContext = getServletContext();
		try {
			AuthorityValueUtil.putAuthorityValuesToContex(servletContext);
			Map<String, ErrorCode> errorCodeMap = ReaderXmlUtils.getErrorCodes();
			servletContext.setAttribute(FlagConstants.ERROR_CODE_MAP, errorCodeMap);
		} catch (ServerErrorException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
