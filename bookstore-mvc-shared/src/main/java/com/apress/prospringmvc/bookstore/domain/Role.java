package com.apress.prospringmvc.bookstore.domain;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
/**
 * 
 * @author Marten Deinum

 *
 */
@Entity
@Table(uniqueConstraints = { @UniqueConstraint(columnNames = { "role" }) })
@SuppressWarnings("serial")
public class Role implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String role;

	@ManyToMany(cascade = CascadeType.ALL)
	private List<Permission> permissions = new ArrayList<>();

	Role() {
		// For ORM
	}

	public Role(String role) {
		this.role = role;
	}

	public Long getId() {
		return id;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public List<Permission> getPermissions() {
		return permissions;
	}

	public void setPermissions(List<Permission> permissions) {
		this.permissions = permissions;
	}
}
