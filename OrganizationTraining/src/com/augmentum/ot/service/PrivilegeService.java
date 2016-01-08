package com.augmentum.ot.service;
import java.util.List;
import java.math.BigInteger;

import com.augmentum.ot.exception.ServerErrorException;
import com.augmentum.ot.model.Privilege;

/** 
* @ClassName: PrivilegeService 
* @Description: TODO
* @date 2012-8-7 
* @version V1.0 
*/
public interface PrivilegeService {

     /** 
     * @Description: get privilege value by privilegeName
     * @param privilegeName
     * @return Integer
     * @throws PrivilegeServiceException 
     */ 
    Integer getPrivilegeValueByPrivilegeName(String privilegeName) throws ServerErrorException;
    
     /** 
     * @Description: create authority value for one employee when he logon system
     * @param augUserName
     * @return 
     */ 
    BigInteger createAuthorityValueForEmployee(String augUserName)
        throws ServerErrorException;

	    
    List<Privilege> listAllPrivilige() throws ServerErrorException;
}
