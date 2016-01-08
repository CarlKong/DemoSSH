package com.augmentum.ot.util;

import java.io.IOException;
import java.util.Properties;

/**
 * Deal with the internationalization.
 * 
 * @author Michael.Ding
 * @version 0.1 2012-09-25
 *
 */
public abstract class InternationalizationUtils {

    private static Properties properties = new Properties();
    private static final String US_PATH = "i18n/mess_en_US.properties";
    private static final String ZH_PATH = "i18n/mess_zh_CN.properties";
    
    /**
     * Get English value by key.
     * 
     * @param key  
     * @return  English value.
     */
    public static String getValueByKeyEN(String key) {
        Properties prop = getProperties(US_PATH);
        if (prop != null) {
            String value = prop.getProperty(key);
            return value;
        }
        return null;
    }
    
    /**
     * Get Chinese value by key.
     * 
     * @param key
     * @return  Chinese value.
     */
    public static String getValueByKeyZH(String key) {
        Properties prop = getProperties(ZH_PATH);
        if (prop != null) {
            String value = prop.getProperty(key);
            return value;
        }
        return null;
    }
    
    /**
     * Get the properties.
     * 
     * @param filePath  File path.
     * @return  The properties.
     */
    public static Properties getProperties(String filePath) {
        try {
            properties.load(InternationalizationUtils.class.getClassLoader().getResourceAsStream(filePath));
        } catch (IOException e) {
            return null;
        }
        return properties;
    }
    
    public static void main(String[] args) {
        System.out.println(InternationalizationUtils.getValueByKeyZH("Culture&Policy"));
    }
    
}
