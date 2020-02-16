package com.springrestsecuritycoreboilerplate.user;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Where;

import com.springrestsecuritycoreboilerplate.common.BaseEntity;
import com.springrestsecuritycoreboilerplate.password.ResetPasswordToken;
import com.springrestsecuritycoreboilerplate.registration.VerificationToken;
import com.springrestsecuritycoreboilerplate.role.Role;

@Entity
@Table(name = "APP_USER")
public class AppUser extends BaseEntity implements Serializable {

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
	@NotNull
	private Boolean verified = false;

	@ManyToMany(fetch = FetchType.EAGER)
//	@JoinTable(name = "users_roles", joinColumns = @JoinColumn(name = "appuser_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
	private Set<Role> roles = new HashSet<Role>();

	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "user")
	@Where(clause = "deleted=false")
	private Set<VerificationToken> verificationToken = new HashSet<VerificationToken>();

	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "user")
	@Where(clause = "deleted=false")
	private Set<ResetPasswordToken> resetPasswordTokens = new HashSet<ResetPasswordToken>();

	public Set<ResetPasswordToken> getResetPasswordTokens() {
		return resetPasswordTokens;
	}

	public void setResetPasswordTokens(Set<ResetPasswordToken> resetPasswordTokens) {
		this.resetPasswordTokens = resetPasswordTokens;
	}

	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

	public Set<VerificationToken> getVerificationToken() {
		return verificationToken;
	}

	public void setVerificationToken(Set<VerificationToken> verificationToken) {
		this.verificationToken = verificationToken;
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

	public Boolean getVerified() {
		return verified;
	}

	public void setVerified(Boolean verified) {
		this.verified = verified;
	}

}