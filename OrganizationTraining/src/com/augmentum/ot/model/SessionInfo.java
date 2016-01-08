package com.augmentum.ot.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.search.annotations.DocumentId;

@Entity
@Table(name = "session_info")
public class SessionInfo implements Serializable{

	/**  
	 * @Fields: serialVersionUID 
	 * @Todo: TODO 
	 */ 
	private static final long serialVersionUID = 5445251172162058894L;
	
	private Integer sessionInfoId;

	@GenericGenerator(name = "generator", strategy = "increment")
    @Id
    @GeneratedValue(generator = "generator")
    @Column(name = "id", nullable = false, unique = true)
    @DocumentId(name="id")
	public Integer getSessionInfoId() {
		return sessionInfoId;
	}

	public void setSessionInfoId(Integer sessionInfoId) {
		this.sessionInfoId = sessionInfoId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((sessionInfoId == null) ? 0 : sessionInfoId.hashCode());
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
		SessionInfo other = (SessionInfo) obj;
		if (sessionInfoId == null) {
			if (other.sessionInfoId != null)
				return false;
		} else if (!sessionInfoId.equals(other.sessionInfoId))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "SessionInfo [sessionInfoId=" + sessionInfoId + "]";
	}
	
	
}
