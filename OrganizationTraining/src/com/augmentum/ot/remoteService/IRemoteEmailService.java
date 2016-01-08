package com.augmentum.ot.remoteService;

import java.util.Map;

import com.augmentum.ot.exception.ServerErrorException;



public interface IRemoteEmailService {
    /**
     * search, sort and paginate the employees by search criteria from IAP
     * 
     * @param messageMap
     * @param request
     * @return boolean
     * @throws ServerErrorException
     */
    public void sendEmail(Map<String, String> messageMap) throws ServerErrorException;

}
