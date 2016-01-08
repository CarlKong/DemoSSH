package com.augmentum.ot.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;

import com.augmentum.ot.dataObject.constant.ErrorCodeConstants;
import com.augmentum.ot.dataObject.constant.FlagConstants;
import com.augmentum.ot.exception.ServerErrorException;
import com.augmentum.ot.model.Privilege;
import com.augmentum.ot.service.PrivilegeService;

public class AuthorityValueUtil {
	public static void putAuthorityValuesToContex(ServletContext servletContext) throws ServerErrorException{
		try {
			PrivilegeService privilegeService = BeanFactory.getPrivilegeService();
			List<Privilege> privilegeList = privilegeService.listAllPrivilige();
			if (privilegeList == null || privilegeList.isEmpty()) {
				throw new ServerErrorException(ErrorCodeConstants.SERVER_ERROR);
			}
			Map<String, Integer> authorityValueMap = new HashMap<String, Integer>();
			for (Privilege privilege : privilegeList) {
				authorityValueMap.put(privilege.getPrivilegeName().toLowerCase(), privilege.getPrivilegeValue());
			}
			servletContext.setAttribute(FlagConstants.AUTHORITY_VALUE, authorityValueMap);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
