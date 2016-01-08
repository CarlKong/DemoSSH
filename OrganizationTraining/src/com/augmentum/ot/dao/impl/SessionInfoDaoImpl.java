package com.augmentum.ot.dao.impl;

import org.springframework.stereotype.Component;

import com.augmentum.ot.dao.SessionInfoDao;
import com.augmentum.ot.model.SessionInfo;


/**
 * 
 * @Description: TODO 
 *
 * @version V1.0, 2012-12-20
 */
@Component("sessionInfoDao")
public class SessionInfoDaoImpl extends BaseDaoImpl<SessionInfo> implements SessionInfoDao {

	
    @Override
    public Class<SessionInfo> getEntityClass() {
        return SessionInfo.class;
    }
    
}
