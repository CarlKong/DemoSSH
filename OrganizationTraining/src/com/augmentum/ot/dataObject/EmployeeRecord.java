package com.augmentum.ot.dataObject;

import com.augmentum.ot.dataObject.constant.RoleNameConstants;

public class EmployeeRecord {
    /**Employee Id**/
    private int id;
    
    /**Employee job number**/
    private String jobNumber = "";
    
    /**Employee name**/
    private String Name = "";
	
	/**Employee email**/
    private String Email = "";
    
    /**Employee department**/
    private String department = "";
    
    /**Employee position**/
    private String position = "";
    
    /**Employee projects**/
    private String projects = "";
    
    /**Employee reptManager**/
    private String reptManager = "";
    
    /**Employee role**/
    private String role = RoleNameConstants.TRAINER;
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getJobNumber() {
        return jobNumber;
    }

    public void setJobNumber(String jobNumber) {
        this.jobNumber = jobNumber;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getProjects() {
        return projects;
    }

    public void setProjects(String projects) {
        this.projects = projects;
    }

    public String getReptManager() {
        return reptManager;
    }

    public void setReptManager(String reptManager) {
        this.reptManager = reptManager;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
	
	public String getEmail() {
		return Email;
	}

	public void setEmail(String email) {
		Email = email;
	}
}
