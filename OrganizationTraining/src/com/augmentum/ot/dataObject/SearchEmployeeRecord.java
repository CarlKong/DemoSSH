package com.augmentum.ot.dataObject;

import com.augmentum.ot.dataObject.constant.RoleNameConstants;

public class SearchEmployeeRecord {
    //Employee id: id
    private int id = 0;
    //In Page the record id: we can calculate it.
    private int recordId = 1;
    //Employee job number: employeeEmployeeId
    private String jobNumber = "";
    //Employee name: employeeName
    private String Name = "";
    //Employee department: departmentNameEn
    private String department = "";
    //Employee position: positionNameEn
    private String position = "";
    //Employee projects
    private String projects = "";
    //Employee reptManager: managerName
    private String reptManager = "";
    //Employee role: no related in IAP
    private String role = RoleNameConstants.TRAINER;
    
    //For Employee in ot database.: employeeEmail and employeeDataSite
    private String email = "";
    private String site = "";

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRecordId() {
        return recordId;
    }

    public void setRecordId(int recordId) {
        this.recordId = recordId;
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
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

}
