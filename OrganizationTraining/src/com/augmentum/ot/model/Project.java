package com.augmentum.ot.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;



/**
 * @version 0.2, 07/12/2012
 */
public class Project implements Serializable {

	private static final long serialVersionUID = 1L;
	
	/** Project id*/
	private String augProjectID;
	
	/** Project name*/
	private String augProjectName;
	
	/** Project code*/
	private String augProjectCode;
	
	/** Project status*/
	private String augProjectStatus;
	
	/** Project start time*/
	private Date augStartTime;
	
	/** Project inactive time*/
	private Date augInactiveTime;
	
	/** Project PM id*/
	private String augPMEmployeeId;
	
	/** Project PM name*/
	private String augPMName;
	
	/** Project PD id*/
	private String augPDEmployeeId;
	
	/** Project PD name*/
	private String augPDName;
	
	/** Project last update time*/
	private Date augLastUpdatedTime;

	/** Project employees set*/
	private List<Employee> employeeList = new ArrayList<Employee>();

	public String getAugProjectID() {
		return augProjectID;
	}

	public String getAugProjectName() {
		return augProjectName;
	}

	public String getAugProjectCode() {
		return augProjectCode;
	}

	public String getAugProjectStatus() {
		return augProjectStatus;
	}

	public Date getAugStartTime() {
		return augStartTime;
	}

	public Date getAugInactiveTime() {
		return augInactiveTime;
	}

	public String getAugPMEmployeeId() {
		return augPMEmployeeId;
	}

	public String getAugPMName() {
		return augPMName;
	}

	public String getAugPDEmployeeId() {
		return augPDEmployeeId;
	}

	public String getAugPDName() {
		return augPDName;
	}

	public Date getAugLastUpdatedTime() {
		return augLastUpdatedTime;
	}

	public List<Employee> getEmployeeList() {
		return employeeList;
	}

	public void setEmployeeList(List<Employee> employeeList) {
		this.employeeList = employeeList;
	}

	public void setAugProjectID(String augProjectID) {
		this.augProjectID = augProjectID;
	}

	public void setAugProjectName(String augProjectName) {
		this.augProjectName = augProjectName;
	}

	public void setAugProjectCode(String augProjectCode) {
		this.augProjectCode = augProjectCode;
	}

	public void setAugProjectStatus(String augProjectStatus) {
		this.augProjectStatus = augProjectStatus;
	}

	public void setAugStartTime(Date augStartTime) {
		this.augStartTime = augStartTime;
	}

	public void setAugInactiveTime(Date augInactiveTime) {
		this.augInactiveTime = augInactiveTime;
	}

	public void setAugPMEmployeeId(String augPMEmployeeId) {
		this.augPMEmployeeId = augPMEmployeeId;
	}

	public void setAugPMName(String augPMName) {
		this.augPMName = augPMName;
	}

	public void setAugPDEmployeeId(String augPDEmployeeId) {
		this.augPDEmployeeId = augPDEmployeeId;
	}

	public void setAugPDName(String augPDName) {
		this.augPDName = augPDName;
	}

	public void setAugLastUpdatedTime(Date augLastUpdatedTime) {
		this.augLastUpdatedTime = augLastUpdatedTime;
	}

	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((augInactiveTime == null) ? 0 : augInactiveTime.hashCode());
		result = prime
				* result
				+ ((augLastUpdatedTime == null) ? 0 : augLastUpdatedTime
						.hashCode());
		result = prime * result
				+ ((augPDEmployeeId == null) ? 0 : augPDEmployeeId.hashCode());
		result = prime * result
				+ ((augPDName == null) ? 0 : augPDName.hashCode());
		result = prime * result
				+ ((augPMEmployeeId == null) ? 0 : augPMEmployeeId.hashCode());
		result = prime * result
				+ ((augPMName == null) ? 0 : augPMName.hashCode());
		result = prime * result
				+ ((augProjectCode == null) ? 0 : augProjectCode.hashCode());
		result = prime * result
				+ ((augProjectID == null) ? 0 : augProjectID.hashCode());
		result = prime * result
				+ ((augProjectName == null) ? 0 : augProjectName.hashCode());
		result = prime
				* result
				+ ((augProjectStatus == null) ? 0 : augProjectStatus.hashCode());
		result = prime * result
				+ ((augStartTime == null) ? 0 : augStartTime.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Project other = (Project) obj;
		if (augInactiveTime == null) {
			if (other.augInactiveTime != null)
				return false;
		} else if (!augInactiveTime.equals(other.augInactiveTime))
			return false;
		if (augLastUpdatedTime == null) {
			if (other.augLastUpdatedTime != null)
				return false;
		} else if (!augLastUpdatedTime.equals(other.augLastUpdatedTime))
			return false;
		if (augPDEmployeeId == null) {
			if (other.augPDEmployeeId != null)
				return false;
		} else if (!augPDEmployeeId.equals(other.augPDEmployeeId))
			return false;
		if (augPDName == null) {
			if (other.augPDName != null)
				return false;
		} else if (!augPDName.equals(other.augPDName))
			return false;
		if (augPMEmployeeId == null) {
			if (other.augPMEmployeeId != null)
				return false;
		} else if (!augPMEmployeeId.equals(other.augPMEmployeeId))
			return false;
		if (augPMName == null) {
			if (other.augPMName != null)
				return false;
		} else if (!augPMName.equals(other.augPMName))
			return false;
		if (augProjectCode == null) {
			if (other.augProjectCode != null)
				return false;
		} else if (!augProjectCode.equals(other.augProjectCode))
			return false;
		if (augProjectID == null) {
			if (other.augProjectID != null)
				return false;
		} else if (!augProjectID.equals(other.augProjectID))
			return false;
		if (augProjectName == null) {
			if (other.augProjectName != null)
				return false;
		} else if (!augProjectName.equals(other.augProjectName))
			return false;
		if (augProjectStatus == null) {
			if (other.augProjectStatus != null)
				return false;
		} else if (!augProjectStatus.equals(other.augProjectStatus))
			return false;
		if (augStartTime == null) {
			if (other.augStartTime != null)
				return false;
		} else if (!augStartTime.equals(other.augStartTime))
			return false;
		return true;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Project [augInactiveTime=");
		builder.append(augInactiveTime);
		builder.append(", augLastUpdatedTime=");
		builder.append(augLastUpdatedTime);
		builder.append(", augPDEmployeeId=");
		builder.append(augPDEmployeeId);
		builder.append(", augPDName=");
		builder.append(augPDName);
		builder.append(", augPMEmployeeId=");
		builder.append(augPMEmployeeId);
		builder.append(", augPMName=");
		builder.append(augPMName);
		builder.append(", augProjectCode=");
		builder.append(augProjectCode);
		builder.append(", augProjectID=");
		builder.append(augProjectID);
		builder.append(", augProjectName=");
		builder.append(augProjectName);
		builder.append(", augProjectStatus=");
		builder.append(augProjectStatus);
		builder.append(", augStartTime=");
		builder.append(augStartTime);
		builder.append(", employeeList.size()=");
		builder.append(employeeList.size());
		builder.append("]");
		return builder.toString();
	}

}

