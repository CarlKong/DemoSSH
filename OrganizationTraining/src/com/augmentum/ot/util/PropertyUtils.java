package com.augmentum.ot.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;



public class PropertyUtils {

    private static Logger logger = Logger.getLogger(PropertyUtils.class);

    //read value by key
    public static String readValue(String filePath, String key){
        Properties props = new Properties();
        InputStream in = null;
        try{
            in = Thread.currentThread().getContextClassLoader().getResourceAsStream(filePath);
            props.load(in);
            String value = props.getProperty(key);
            return value;
        }catch(Exception e){
            logger.error("Read property value by key failed!", e);
            return null;
        }finally {
            try {
                in.close();
            } catch (IOException e) {
                logger.error("Close inputStream failed!", e);
            }
        }
    }
}
