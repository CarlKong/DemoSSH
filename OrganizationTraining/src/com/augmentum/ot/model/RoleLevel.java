package com.augmentum.ot.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "role_level")
public class RoleLevel implements Serializable {

	private static final long serialVersionUID = 1L;

	private int id;
    
    private int level;
    
    private List<EmployeeRoleLevelMap> roleLevelsForRoleLevel = new ArrayList<EmployeeRoleLevelMap>();

    @GenericGenerator(name = "generator", strategy = "increment")
    @Id
    @GeneratedValue(generator = "generator")
    @Column(name = "id", unique = true, nullable = false)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Column(name = "level", nullable = false)
    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    @OneToMany(targetEntity=EmployeeRoleLevelMap.class, fetch=FetchType.LAZY, mappedBy="roleLevel")
    public List<EmployeeRoleLevelMap> getRoleLevelsForRoleLevel() {
        return roleLevelsForRoleLevel;
    }

    public void setRoleLevelsForRoleLevel(
            List<EmployeeRoleLevelMap> roleLevelsForRoleLevel) {
        this.roleLevelsForRoleLevel = roleLevelsForRoleLevel;
    }

}
