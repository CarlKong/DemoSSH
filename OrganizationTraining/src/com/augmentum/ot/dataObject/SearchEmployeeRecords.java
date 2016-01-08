package com.augmentum.ot.dataObject;

import java.util.ArrayList;
import java.util.List;

public class SearchEmployeeRecords {
    //All record number
    private int count = 0;
    //All page number
    private int totalPage = 0;
    private List<EmployeeRecord> employeeRecords = new ArrayList<EmployeeRecord>();

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public List<EmployeeRecord> getEmployeeRecords() {
        return employeeRecords;
    }

    public void setEmployeeRecords(List<EmployeeRecord> employeeRecords) {
        this.employeeRecords = employeeRecords;
    }

}
