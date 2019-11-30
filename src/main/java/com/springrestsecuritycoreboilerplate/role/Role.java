package com.springrestsecuritycoreboilerplate.role;

import java.io.Serializable;
import java.util.Collection;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.springrestsecuritycoreboilerplate.user.AppUser;

import javax.persistence.JoinColumn;

@Entity
@Table(name = "ROLE")
public class Role implements Serializable {

	@Id
	@GeneratedValue(generator = "hibernate-uuid")
	@GenericGenerator(name = "hibernate-uuid", strategy = "uuid2")
	private String id;

	private String name;

	@OneToMany(mappedBy = "role")
	private Collection<AppUser> users;

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "roles_privileges", joinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "privilege_id", referencedColumnName = "id"))
	private Collection<Privilege> privileges;

	public Role() {
		super();
	}

	public Role(final String name) {
		super();
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@JsonIgnore
	public Collection<AppUser> getUsers() {
		return users;
	}

	public void setUsers(Collection<AppUser> users) {
		this.users = users;
	}

	public Collection<Privilege> getPrivileges() {
		return privileges;
	}

	public void setPrivileges(Collection<Privilege> privileges) {
		this.privileges = privileges;
	}

}