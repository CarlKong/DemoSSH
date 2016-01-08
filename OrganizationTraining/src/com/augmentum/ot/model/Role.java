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
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;


/**
 * @version 0.2, 07/12/2012
 */
@Entity
@Table(name = "role")
public class Role implements Serializable {

    private static final long serialVersionUID = 1L;

    /** id of role */
    private Integer roleId;

    /** name of role */
    private String roleName;

    /** value of privilege */
    private String rolePrivilegeValue;

    private List<ExternalRole> externalRoleList = new ArrayList<ExternalRole>();

    private List<Privilege> privilegeList = new ArrayList<Privilege>();

    private List<EmployeeRoleLevelMap> roleLevelsForRole = new ArrayList<EmployeeRoleLevelMap>();

    @GenericGenerator(name = "generator", strategy = "increment")
    @Id
    @GeneratedValue(generator = "generator")
    @Column(name = "id", unique = true, nullable = false)
    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    @Column(name = "name", length = 50, nullable = false)
    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    @Lob
    @Column(name = "privilege_value_sum", columnDefinition = "BLOB")
    public String getRolePrivilegeValue() {
        return rolePrivilegeValue;
    }

    public void setRolePrivilegeValue(String rolePrivilegeValue) {
        this.rolePrivilegeValue = rolePrivilegeValue;
    }

    @ManyToMany(mappedBy = "roleList")
    public List<ExternalRole> getExternalRoleList() {
        return externalRoleList;
    }

    public void setExternalRoleList(List<ExternalRole> externalRoleList) {
        this.externalRoleList = externalRoleList;
    }

    @ManyToMany(targetEntity=Privilege.class, fetch=FetchType.LAZY, cascade=CascadeType.ALL)
    @JoinTable(name="role_privilege_map", 
    		joinColumns={@JoinColumn(name="role_id", referencedColumnName="id")},
    		inverseJoinColumns={@JoinColumn(name="privilege_id", referencedColumnName="id")})
    public List<Privilege> getPrivilegeList() {
        return privilegeList;
    }

    public void setPrivilegeList(List<Privilege> privilegeList) {
        this.privilegeList = privilegeList;
    }

    @OneToMany(targetEntity=EmployeeRoleLevelMap.class, fetch=FetchType.LAZY, mappedBy="role")
	public List<EmployeeRoleLevelMap> getRoleLevelsForRole() {
        return roleLevelsForRole;
    }

    public void setRoleLevelsForRole(List<EmployeeRoleLevelMap> roleLevelsForRole) {
        this.roleLevelsForRole = roleLevelsForRole;
    }

    @Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((roleId == null) ? 0 : roleId.hashCode());
		result = prime * result
				+ ((roleName == null) ? 0 : roleName.hashCode());
		result = prime
				* result
				+ ((rolePrivilegeValue == null) ? 0 : rolePrivilegeValue
						.hashCode());
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
		Role other = (Role) obj;
		if (roleId == null) {
			if (other.roleId != null)
				return false;
		} else if (!roleId.equals(other.roleId))
			return false;
		if (roleName == null) {
			if (other.roleName != null)
				return false;
		} else if (!roleName.equals(other.roleName))
			return false;
		if (rolePrivilegeValue == null) {
			if (other.rolePrivilegeValue != null)
				return false;
		} else if (!rolePrivilegeValue.equals(other.rolePrivilegeValue))
			return false;
		return true;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Role [roleLevelsForRole.size()=");
		builder.append(roleLevelsForRole.size());
		builder.append(", externalRoleList.size()=");
		builder.append(externalRoleList.size());
		builder.append(", privilegeList.size()=");
		builder.append(privilegeList.size());
		builder.append(", roleId=");
		builder.append(roleId);
		builder.append(", roleName=");
		builder.append(roleName);
		builder.append(", rolePrivilegeValue=");
		builder.append(rolePrivilegeValue);
		builder.append("]");
		return builder.toString();
	}

	
    
    
}
