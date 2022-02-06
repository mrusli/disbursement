package com.pyramix.domain.user;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

import com.pyramix.domain.common.IdBasedObject;
import com.pyramix.domain.common.SchemaUtil;

@Entity
@Table(name = "auth_user_role", schema = SchemaUtil.SCHEMA_COMMON)
public class UserRole extends IdBasedObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3704996438997906155L;
	
	@Column(name="user_role")
	private String role_name;
	
	//	`enabled` char(1) DEFAULT NULL,
	@Column(name="enabled")
	@Type(type = "true_false")
	private boolean enabled;
	
	@Override
	public String toString() {
		return "UserRole [id=" + super.getId() + ", role_name=" + role_name + ", enabled=" + enabled + "]";
	}

	public String getRole_name() {
		return role_name;
	}

	public void setRole_name(String role_name) {
		this.role_name = role_name;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	
	
}
