package com.springrestsecuritycoreboilerplate.user;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import org.hibernate.annotations.GenericGenerator;
import com.springrestsecuritycoreboilerplate.role.Role;

@Entity
@Table(name = "APP_USER")
public class AppUser implements Serializable {

	@Id
	@GeneratedValue(generator = "hibernate-uuid")
	@GenericGenerator(name = "hibernate-uuid", strategy = "uuid2")
	private String id;
	private String name;
	@NotNull
	private String username;
	@NotNull
	private String password;
	private Boolean canBeModified;
	@NotNull
	private String email;

	@ManyToOne
	private Role role;

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
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

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Boolean getCanBeModified() {
		return canBeModified;
	}

	public void setCanBeModified(Boolean canBeDeleted) {
		this.canBeModified = canBeDeleted;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

}