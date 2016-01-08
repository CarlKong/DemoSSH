package com.augmentum.ot.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.search.annotations.Analyzer;
import org.hibernate.search.annotations.DocumentId;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Fields;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.Store;
import org.wltea.analyzer.lucene.IKAnalyzer;

import com.augmentum.ot.dataObject.constant.IndexFieldConstants;

/**
 * @version 0.2, 07/12/2012
 */
@Entity
@Table(name = "employee")
@Indexed(index = "employee_index")
@Analyzer(impl = IKAnalyzer.class)
public class Employee implements Serializable {

    private static final long serialVersionUID = 1L;

    /** id of employee */
    private Integer employeeId;

    /** The employee email* */
    private String augEmail;
    
    /** The employee maneger email* */
    private String managerEmail;

    /** The employee job number* */
    private String augEmpId;

    /** The employee name in the LDAP server* */
    private String augUserName;

    /** The employee position* */
    private String augPosition;

    /** The employee site* */
    private String augSite;

    /** The department* */
    private String augDept;

    /** The role* */
    private String augLevelRole;

    /** The manager of the employee primary key * */
    private String augRMEmployeeId;
    
    /** The manager of the employee job number * */
    private String augRMEmployeeJobID;

    /** The manager of the employee name * */
    private String augRMEmployeeEnName;

    /** The employee projects */
    private List<Project> projectList = new ArrayList<Project>();

    /** the employee custom columns list */
    private List<CustomColumns> customColumnsList = new ArrayList<CustomColumns>();

    private List<PageSize> customPageSizeList = new ArrayList<PageSize>();

    private List<PlanEmployeeMap> planEmployeeMapList = new ArrayList<PlanEmployeeMap>();

    private List<EmployeeRoleLevelMap> roleLevelsForEmployee = new ArrayList<EmployeeRoleLevelMap>();
    
    /** the employee is the author of the course in the following courseList */
    private List<Course> courseList = new ArrayList<Course>();
    
    /**  The identity which shows whether the dataList is modify  **/
    private int customColumnTypeIdentity;
    
    private List<ActualCourse> actualCourseList = new ArrayList<ActualCourse>();
    
    /** contains the actualCourses for which trainee apply leave*/
    private List<LeaveNote> leaveList = new ArrayList<LeaveNote>();
    
    private String password;
   
    @GenericGenerator(name = "generator", strategy = "increment")
    @Id
    @GeneratedValue(generator = "generator")
    @Column(name = "id", unique = true, nullable = false)
    @DocumentId(name = IndexFieldConstants.EMPLOYEE_ID)
    public Integer getEmployeeId() {
        return employeeId;
    }

	public void setEmployeeId(Integer employeeId) {
        this.employeeId = employeeId;
    }

    @Column(name = "email", length = 255)
    @Field(name = IndexFieldConstants.EMPLOYEE_EMAIL, index=Index.TOKENIZED, store=Store.YES)
    public String getAugEmail() {
        return augEmail;
    }

    public void setAugEmail(String augEmail) {
        this.augEmail = augEmail;
    }
    @Column(name = "managerEmail", length = 255)
    @Field(name = IndexFieldConstants.EMPLOYEE_EMAIL, index=Index.TOKENIZED, store=Store.YES)
    public String getManagerEmail() {
        return managerEmail;
    }

    public void setManagerEmail(String managerEmail) {
        this.managerEmail = managerEmail;
    }

    @Column(name = "job_number", length = 255)
    @Field(name = IndexFieldConstants.EMPLOYEE_JOB_NUMBER, index=Index.TOKENIZED, store=Store.YES)
    public String getAugEmpId() {
        return augEmpId;
    }

    public void setAugEmpId(String augEmpId) {
        this.augEmpId = augEmpId;
    }

    @Column(name = "name", length = 255)
    @Fields({
        @Field(name = IndexFieldConstants.EMPLOYEE_NAME, index = Index.TOKENIZED, store = Store.YES),
        @Field(name = IndexFieldConstants.EMPLOYEE_NAME_SORT, index = Index.UN_TOKENIZED, store = Store.YES)
    })
    public String getAugUserName() {
        return augUserName;
    }

    public void setAugUserName(String augUserName) {
        this.augUserName = augUserName;
    }
    @Transient
    public String getAugPosition() {
        return augPosition;
    }

    public void setAugPosition(String augPosition) {
        this.augPosition = augPosition;
    }

    @Column(name = "site", length = 255)
    public String getAugSite() {
        return augSite;
    }

    public void setAugSite(String augSite) {
        this.augSite = augSite;
    }
    @Transient
    public String getAugDept() {
        return augDept;
    }

    public void setAugDept(String augDept) {
        this.augDept = augDept;
    }
    @Transient
    public String getAugLevelRole() {
        return augLevelRole;
    }

    public void setAugLevelRole(String augLevelRole) {
        this.augLevelRole = augLevelRole;
    }
    
    @Transient
    public String getAugRMEmployeeId() {
		return augRMEmployeeId;
	}

	public void setAugRMEmployeeId(String augRMEmployeeId) {
		this.augRMEmployeeId = augRMEmployeeId;
	}

	@Transient
	public String getAugRMEmployeeJobID() {
		return augRMEmployeeJobID;
	}

	public void setAugRMEmployeeJobID(String augRMEmployeeJobID) {
		this.augRMEmployeeJobID = augRMEmployeeJobID;
	}

	@Transient
    public String getAugRMEmployeeEnName() {
        return augRMEmployeeEnName;
    }

    public void setAugRMEmployeeEnName(String augRMEmployeeEnName) {
        this.augRMEmployeeEnName = augRMEmployeeEnName;
    }

    @Transient
    public List<Project> getProjectList() {
		return projectList;
	}

	public void setProjectList(List<Project> projectList) {
		this.projectList = projectList;
	}

	@ManyToMany(targetEntity=CustomColumns.class, fetch=FetchType.LAZY, cascade=CascadeType.ALL)
	@JoinTable(name="employee_custom_column_map", 
			joinColumns={@JoinColumn(name="employee_id", referencedColumnName="id")},
			inverseJoinColumns={@JoinColumn(name="custom_column_id", referencedColumnName="id")})
	public List<CustomColumns> getCustomColumnsList() {
		return customColumnsList;
	}

	public void setCustomColumnsList(List<CustomColumns> customColumnsList) {
		this.customColumnsList = customColumnsList;
	}

	@ManyToMany(targetEntity=PageSize.class, fetch=FetchType.LAZY, cascade=CascadeType.ALL)
	@JoinTable(name="employee_page_size_map", 
			joinColumns={@JoinColumn(name="employee_id", referencedColumnName="id")},
			inverseJoinColumns={@JoinColumn(name="page_size_id", referencedColumnName="id")})
	public List<PageSize> getCustomPageSizeList() {
		return customPageSizeList;
	}

	public void setCustomPageSizeList(List<PageSize> customPageSizeList) {
		this.customPageSizeList = customPageSizeList;
	}
	
	@OneToMany(targetEntity=PlanEmployeeMap.class, fetch=FetchType.LAZY, mappedBy="employee")
	public List<PlanEmployeeMap> getPlanEmployeeMapList() {
		return planEmployeeMapList;
	}

	public void setPlanEmployeeMapList(List<PlanEmployeeMap> planEmployeeMapList) {
		this.planEmployeeMapList = planEmployeeMapList;
	}
	
	@Column(name = "custom_column_type_identity", columnDefinition = "int(11) default 0" )
    public Integer getCustomColumnTypeIdentity() {
        return customColumnTypeIdentity;
    }

    public void setCustomColumnTypeIdentity(Integer customColumnTypeIdentity) {
        this.customColumnTypeIdentity = customColumnTypeIdentity;
    }

    @OneToMany(targetEntity=EmployeeRoleLevelMap.class, fetch=FetchType.LAZY, mappedBy="employee")
	public List<EmployeeRoleLevelMap> getRoleLevelsForEmployee() {
        return roleLevelsForEmployee;
    }

    public void setRoleLevelsForEmployee(
            List<EmployeeRoleLevelMap> roleLevelsForEmployee) {
        this.roleLevelsForEmployee = roleLevelsForEmployee;
    }

    @ManyToMany(targetEntity=Course.class, fetch=FetchType.LAZY, cascade=CascadeType.ALL)
    @JoinTable(name="course_author_map", 
            joinColumns={@JoinColumn(name="employee_id", referencedColumnName="id")},
            inverseJoinColumns={@JoinColumn(name="course_id", referencedColumnName="id")})
    public List<Course> getCourseList() {
        return courseList;
    }

    public void setCourseList(List<Course> courseList) {
        this.courseList = courseList;
    }

    @ManyToMany(targetEntity=ActualCourse.class, fetch=FetchType.LAZY, cascade=CascadeType.ALL)
    @JoinTable(name="actual_course_employee_map", 
            joinColumns={@JoinColumn(name="employee_id", referencedColumnName="id")},
            inverseJoinColumns={@JoinColumn(name="actual_course_id", referencedColumnName="id")})
    public List<ActualCourse> getActualCourseList() {
		return actualCourseList;
	}

	public void setActualCourseList(List<ActualCourse> actualCourseList) {
		this.actualCourseList = actualCourseList;
	}
	
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "employee", targetEntity = LeaveNote.class)
	public List<LeaveNote> getLeaveList() {
        return leaveList;
    }

    public void setLeaveList(List<LeaveNote> leaveList) {
        this.leaveList = leaveList;
    }
    
    @Column(name = "password", length = 255)
    public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((augDept == null) ? 0 : augDept.hashCode());
        result = prime * result
                + ((augEmail == null) ? 0 : augEmail.hashCode());
        result = prime * result
                + ((augEmpId == null) ? 0 : augEmpId.hashCode());
        result = prime * result
                + ((augLevelRole == null) ? 0 : augLevelRole.hashCode());
        result = prime * result
                + ((augPosition == null) ? 0 : augPosition.hashCode());
        result = prime
                * result
                + ((augRMEmployeeEnName == null) ? 0 : augRMEmployeeEnName
                        .hashCode());
        result = prime * result
                + ((augRMEmployeeId == null) ? 0 : augRMEmployeeId.hashCode());
        result = prime
                * result
                + ((augRMEmployeeJobID == null) ? 0 : augRMEmployeeJobID
                        .hashCode());
        result = prime * result + ((augSite == null) ? 0 : augSite.hashCode());
        result = prime * result
                + ((augUserName == null) ? 0 : augUserName.hashCode());
        result = prime * result
                + ((employeeId == null) ? 0 : employeeId.hashCode());
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
		Employee other = (Employee) obj;
		if (employeeId == null) {
			if (other.employeeId != null)
				return false;
		} else if (!employeeId.equals(other.employeeId))
			return false;
		return true;
	}

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Employee [augDept=");
        builder.append(augDept);
        builder.append(", augEmail=");
        builder.append(augEmail);
        builder.append(", augEmpId=");
        builder.append(augEmpId);
        builder.append(", augLevelRole=");
        builder.append(augLevelRole);
        builder.append(", augPosition=");
        builder.append(augPosition);
        builder.append(", augRMEmployeeEnName=");
        builder.append(augRMEmployeeEnName);
        builder.append(", augRMEmployeeId=");
        builder.append(augRMEmployeeId);
        builder.append(", augRMEmployeeJobID=");
        builder.append(augRMEmployeeJobID);
        builder.append(", augSite=");
        builder.append(augSite);
        builder.append(", augUserName=");
        builder.append(augUserName);
        builder.append(", employeeId=");
        builder.append(employeeId);
        builder.append(", roleLevelsForEmployee=");
        builder.append("]");
        return builder.toString();
    }
}
