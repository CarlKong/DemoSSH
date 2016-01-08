package com.augmentum.ot.dao;

import com.augmentum.ot.model.Role;


/** 
* @ClassName: RoleDao 
* @Description: TODO
* @date 2012-8-7 
* @version V1.0 
*/
public interface RoleDao extends BaseDao<Role> {



     /** 
     * @Description: get role by role name
     * @param roleName
     * @return 
     */ 
    public Role getRoleByRoleName(String roleName);
    
   
    
}
