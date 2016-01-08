package com.augmentum.ot.dataObject;

/**
 * 
 * @version 0.1, 07/13/2012
 */
public class EmployeeSearchCondition extends SearchCondition {
    
    
    /** All selected plan type names MUST BE: "employeeRoleName1 employeeRoleName2" */
    private String employeeRoleNames;
    

    public String getEmployeeRoleNames() {
        return employeeRoleNames;
    }

    public void setEmployeeRoleNames(String employeeRoleNames) {
        this.employeeRoleNames = employeeRoleNames;
    }
}
