package com.augmentum.ot.service;

import java.util.Set;

import com.augmentum.ot.exception.ServerErrorException;



/** 
* @ClassName: RoleService 
* @Description: TODO
* @date 2012-8-10 
* @version V1.0 
*/
public interface RoleService {

	/**
	 * findEmployeeJobNumbersByRoleNames
	 * @param roleNames
	 * @return
	 * @throws ServerErrorException
	 */
    Set<String> findEmployeeJobNumbersByRoleNames(String[] roleNames) throws ServerErrorException;

}
