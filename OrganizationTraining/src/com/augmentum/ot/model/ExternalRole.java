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
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "external_role")
public class ExternalRole implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer externalRoleId;

    private String externalRoleName;

    private List<Role> roleList = new ArrayList<Role>();

    @GenericGenerator(name = "generator", strategy = "increment")
    @Id
    @GeneratedValue(generator = "generator")
    @Column(name = "id", unique = true, nullable = false)
    public Integer getExternalRoleId() {
        return externalRoleId;
    }

    public void setExternalRoleId(Integer externalRoleId) {
        this.externalRoleId = externalRoleId;
    }

    @Column(name = "name", nullable = false, length = 50)
    public String getExternalRoleName() {
        return externalRoleName;
    }

    public void setExternalRoleName(String externalRoleName) {
        this.externalRoleName = externalRoleName;
    }

    @ManyToMany(targetEntity=Role.class, fetch=FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name ="external_role_map", 
    		joinColumns = {@JoinColumn(name="external_role_id", referencedColumnName="id") },
    		inverseJoinColumns = {@JoinColumn(name = "role_id", referencedColumnName="id") })
    public List<Role> getRoleList() {
        return roleList;
    }

    public void setRoleList(List<Role> roleList) {
        this.roleList = roleList;
    }

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((externalRoleId == null) ? 0 : externalRoleId.hashCode());
		result = prime
				* result
				+ ((externalRoleName == null) ? 0 : externalRoleName.hashCode());
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
		ExternalRole other = (ExternalRole) obj;
		if (externalRoleId == null) {
			if (other.externalRoleId != null)
				return false;
		} else if (!externalRoleId.equals(other.externalRoleId))
			return false;
		if (externalRoleName == null) {
			if (other.externalRoleName != null)
				return false;
		} else if (!externalRoleName.equals(other.externalRoleName))
			return false;
		return true;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ExternalRole [externalRoleId=");
		builder.append(externalRoleId);
		builder.append(", externalRoleName=");
		builder.append(externalRoleName);
		builder.append(", roleList.size()=");
		builder.append(roleList.size());
		builder.append("]");
		return builder.toString();
	}
	
}
