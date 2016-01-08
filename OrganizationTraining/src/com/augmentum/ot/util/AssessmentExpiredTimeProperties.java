package com.augmentum.ot.util;

import java.io.IOException;
import java.util.Properties;

import com.augmentum.ot.dataObject.constant.FlagConstants;

/**
 * This class use the singleton design pattern. This class will only one instance
 * in system. And when the hour number is be updated, the caller must set the isExpiredTimeUpdated.
 * to be true.
 * 
 * @version 0.1 2012-10-26
 *
 */
public class AssessmentExpiredTimeProperties{

    private static final long serialVersionUID = -3647305753394535749L;
    
    /** The properties read the ot.properties. */
    private static Properties expiredProperties = new Properties();
    
    private static AssessmentExpiredTimeProperties assessmentExpiredTimePropertie = new AssessmentExpiredTimeProperties();
    
    private int hourNumber;
    /** When the instance need to be initialized, it means the instance need to read the configure file. */
    private boolean isExpiredTimeUpdated = true;
    
    /**  The expired switch. False means not checked expired. True means checked expired.  */
    private boolean isCheckExpiredSwitchOn = true;
    
    /**
     * The private construct method to make sure this class can not be initialized
     * in other place.
     */
    private AssessmentExpiredTimeProperties() {
        // Do nothing.
    }
    
    /**
     * Return the singleton instance for AssessmentExpiredTimeProperties. If the
     * instance is null or the hour number is updated, read the configure file and
     * get the truth hour number and set the parameter isExpiredTimeUpdated to false.
     * 
     * @return  The singleton instance for AssessmentExpiredTimeProperties.
     */
    public static AssessmentExpiredTimeProperties getInstance() {
        if (assessmentExpiredTimePropertie != null && !assessmentExpiredTimePropertie.isExpiredTimeUpdated) {
            return assessmentExpiredTimePropertie;
        } else {
            // Read the configure file.
            /*
             * 1. Read the assessment.expired.time value from ot.properties and set it into hourNumberExpression.
             * 2. Deal with hourNumberExpression and calculate it.
             * 3. Set the hourNumber and isExpiredTimeUpdated.
             */
            int hourNumberInConfigure = 1;
            String isCheckExpiredSwitchOnInConfigure = null;
            try {
                // 1. Read the assessment.expired.time value from ot.properties and set it into hourNumberExpression.
                expiredProperties.load(AssessmentExpiredTimeProperties.class.getResourceAsStream(FlagConstants.OT_PROPERTIES_PATH));
                String hourNumberExpression = expiredProperties.getProperty(FlagConstants.EXPIRED_HOUR_NUMBER_KEY);
                isCheckExpiredSwitchOnInConfigure = expiredProperties.getProperty(FlagConstants.EXPIRED_SWITCH_KEY);
                
                if (hourNumberExpression == null || hourNumberExpression.isEmpty()) {
                    // If the hourNumberExpression is null or is empty, set the hourNumberInConfigure to default 7 * 24.
                    hourNumberInConfigure = FlagConstants.EXPIRED_DEFAULT_HOUR_NUMBER;
                } else {
                    // Calculate the hourNumberInConfigure split by "*";
                    String[] hourNumberArray = hourNumberExpression.split("\\*");
                    if (hourNumberArray != null && hourNumberArray.length > 0) {
                        for (String hourNumberString: hourNumberArray) {
                            if (hourNumberString.trim() != null) {
                                hourNumberInConfigure *= Integer.parseInt(hourNumberString.trim());
                            }
                        }
                    }
                }
            } catch (IOException e) {
                // Set the hourNumberInConfigure to default 7 * 24.
                hourNumberInConfigure = FlagConstants.EXPIRED_DEFAULT_HOUR_NUMBER;
            }
            
            // 3. Set the hourNumber and isExpiredTimeUpdated.
            assessmentExpiredTimePropertie.setHourNumber(hourNumberInConfigure);
            if (isCheckExpiredSwitchOnInConfigure == null || isCheckExpiredSwitchOnInConfigure.isEmpty()) {
                assessmentExpiredTimePropertie.setCheckExpiredSwitchOn(true);
            } else if(FlagConstants.EXPIRED_SWITCH_KEY_FALSE.equals(isCheckExpiredSwitchOnInConfigure)) {
                // The switch is off.
                assessmentExpiredTimePropertie.setCheckExpiredSwitchOn(false);
            } else {
                // The other value.
                assessmentExpiredTimePropertie.setCheckExpiredSwitchOn(true);
            }
            assessmentExpiredTimePropertie.setExpiredTimeUpdated(false);
            return assessmentExpiredTimePropertie;
        }
    }

    public int getHourNumber() {
        return hourNumber;
    }

    public void setHourNumber(int hourNumber) {
        this.hourNumber = hourNumber;
    }

    public boolean isExpiredTimeUpdated() {
        return isExpiredTimeUpdated;
    }

    public void setExpiredTimeUpdated(boolean isExpiredTimeUpdated) {
        this.isExpiredTimeUpdated = isExpiredTimeUpdated;
    }

    public boolean isCheckExpiredSwitchOn() {
        return isCheckExpiredSwitchOn;
    }

    public void setCheckExpiredSwitchOn(boolean isCheckExpiredSwitchOn) {
        this.isCheckExpiredSwitchOn = isCheckExpiredSwitchOn;
    }
}
