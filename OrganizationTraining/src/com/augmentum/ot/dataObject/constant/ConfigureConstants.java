package com.augmentum.ot.dataObject.constant;

/**
 * configure constants
 * includes:
 * 1/ property file names
 * 2/ language constants
 */
public abstract class ConfigureConstants {
	
	/**property file names*/
    public static final String INDEX_PROPERTIES_PATH = "hibernate_search.properties";
    
    public static final String INDEX_BASE_KEY = "hibernate.search.default.indexBase";

    public static final String INDEX_BATCH_SIZE = "batch_size";

    public static final String UPLOAD_FILE_CONF = "uploadFileConfiguration.properties";

    public static final String EXCEPTION_XML_FILE = "exception/Exception.xml";
    
    public static final String OT_CONF_FILE = "ot.properties";

    /**language constants*/
    public static final String LOCALE_ZH = "zh_CN";
    
    public static final String LOCALE_EN = "en_US";
    
    public static final String LANGUAGE_ZH = "zh";
    
    public static final String LANGUAGE_EN = "en";
    
    public static final String LANGUAGE_BOTH = "en,zh";
    
    public static final String LANGUAGE = "language";
    
    public static final Integer LANGUAGE_ZH_INT = 0;
    
    public static final Integer LANGUAGE_EN_INT = 1;
    
    public static final Integer LANGUAGE_BOTH_INT = 2;
    
    public static final String WW_TRANS_I18N_LOCALE = "WW_TRANS_I18N_LOCALE";
    
    public static final String LOCALE = "locale";
}
