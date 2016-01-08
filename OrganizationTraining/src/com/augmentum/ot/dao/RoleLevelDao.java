package com.augmentum.ot.dao;

import com.augmentum.ot.model.RoleLevel;

/**
 * Dao for Role Level.
 * @author Peter.Zhang
 *
 */
public interface RoleLevelDao extends BaseDao<RoleLevel> {

    RoleLevel getRoleLevelbyLevel(int level);

}
