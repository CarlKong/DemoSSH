package com.augmentum.ot.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * @version 0.2, 07/12/2012
 */
@Entity
@Table(name = "privilege")
public class Privilege implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer privilegeId;

    private String privilegeName;

    private Integer privilegeValue;

   // private List<Role> roleList = new ArrayList<Role>();

    @GenericGenerator(name = "generator", strategy = "increment")
    @Id
    @GeneratedValue(generator = "generator")
    @Column(name = "id", unique = true, nullable = false)
    public Integer getPrivilegeId() {
        return privilegeId;
    }

    public void setPrivilegeId(Integer privilegeId) {
        this.privilegeId = privilegeId;
    }

    @Column(name = "name", nullable = false)
    public String getPrivilegeName() {
        return privilegeName;
    }

    public void setPrivilegeName(String privilegeName) {
        this.privilegeName = privilegeName;
    }

    @Column(name = "privilege_value", nullable = false)
    public Integer getPrivilegeValue() {
        return privilegeValue;
    }

    public void setPrivilegeValue(Integer privilegeValue) {
        this.privilegeValue = privilegeValue;
    }

    /*@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "role_privilege_rs_tb", joinColumns = { @JoinColumn(name = "privilege_id", referencedColumnName = "privilege_id") }, inverseJoinColumns = { @JoinColumn(name = "role_id", referencedColumnName = "role_id") })
    public List<Role> getRoleList() {
        return roleList;
    }

    public void setRoleList(List<Role> roleList) {
        this.roleList = roleList;
    }*/



    @Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((privilegeId == null) ? 0 : privilegeId.hashCode());
		result = prime * result
				+ ((privilegeName == null) ? 0 : privilegeName.hashCode());
		result = prime * result
				+ ((privilegeValue == null) ? 0 : privilegeValue.hashCode());
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
		Privilege other = (Privilege) obj;
		if (privilegeId == null) {
			if (other.privilegeId != null)
				return false;
		} else if (!privilegeId.equals(other.privilegeId))
			return false;
		if (privilegeName == null) {
			if (other.privilegeName != null)
				return false;
		} else if (!privilegeName.equals(other.privilegeName))
			return false;
		if (privilegeValue == null) {
			if (other.privilegeValue != null)
				return false;
		} else if (!privilegeValue.equals(other.privilegeValue))
			return false;
		return true;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Privilege [privilegeId=");
		builder.append(privilegeId);
		builder.append(", privilegeName=");
		builder.append(privilegeName);
		builder.append(", privilegeValue=");
		builder.append(privilegeValue);
		builder.append("]");
		return builder.toString();
	}

}
