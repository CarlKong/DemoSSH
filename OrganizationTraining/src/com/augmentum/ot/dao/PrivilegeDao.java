package com.augmentum.ot.dao;

import com.augmentum.ot.model.Privilege;

/** 
* @ClassName: PrivilegeDao 
* @Description: A interface about Privilege,provide additional methods
* @date 2012-7-23 
* @version V1.0 
*/
public interface PrivilegeDao extends BaseDao<Privilege> {


     /** 
     * @Description: get privilege by privilege name 
     * @param privilegeName
     * @return Privilege
     */ 
    public Privilege getPrivilegeByPrivilegeName(String privilegeName);
    
   
    
}
