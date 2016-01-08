package com.augmentum.ot.util;

/**
 * Provide the functions about deal with custom column identity.
 * 
 * @version 0.1 
 */
public abstract class CustomColumnUtils {

    /**
     * Judge whether the employee has modified the custom column of this type.
     * The customColumnTypeIdentity of parameter had better from the constant of 
     * class <code>CustomColumnTypeConstants</code>. 
     * 
     * @param customColumnTypeIdentity  The identity of custom column type.
     * @param employeeCustomColumnTypeIdentity  The custom column identity of employee.
     * @return  The result of judgment.
     */
    public static boolean isCustomColumnModified(Integer customColumnTypeIdentity, Integer employeeCustomColumnTypeIdentity) {
        boolean flag = false;
        if (customColumnTypeIdentity > 0) {
            Integer resultIdentity = customColumnTypeIdentity & employeeCustomColumnTypeIdentity;
            if (resultIdentity.equals(customColumnTypeIdentity)) {
                flag = true;
            }
        }
        return flag;
    }
    
    /**
     * Get the custom column of employee.
     * The customColumnTypeIdentity of parameter had better from the constant of 
     * class <code>CustomColumnTypeConstants</code>. 
     * 
     * @param customColumnTypeIdentity  The identity of custom column type.
     * @param employeeCustomColumnTypeIdentity  The custom column identity of employee.
     * @return  The custom column type identity.
     */
    public static Integer getCustomColumnTypeIdentity(Integer customColumnTypeIdentity, Integer employeeCustomColumnTypeIdentity) {
        if (customColumnTypeIdentity > 0) {
            employeeCustomColumnTypeIdentity = employeeCustomColumnTypeIdentity | customColumnTypeIdentity;
        }
        return employeeCustomColumnTypeIdentity;
    }
    
    /**
     * Get 2 categoryFlag power.
     * 
     * @param categoryFlag
     * @return  2 categoryFlag power.
     */
    public static Integer getIdentityByCategoryFlag(Integer categoryFlag) {
        Integer identity = 1;
        for (int i = 0; i < categoryFlag; i++) {
            identity *= 2;
        }
        return identity;
    }
    
}
