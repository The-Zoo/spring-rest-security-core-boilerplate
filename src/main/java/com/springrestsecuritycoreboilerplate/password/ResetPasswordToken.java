package com.springrestsecuritycoreboilerplate.password;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.springrestsecuritycoreboilerplate.common.BaseEntity;
import com.springrestsecuritycoreboilerplate.user.AppUser;

@Entity
public class ResetPasswordToken extends BaseEntity implements Serializable {

	private static final int EXPIRATION = 60 * 24;

	@Id
	@GeneratedValue(generator = "hibernate-uuid")
	@GenericGenerator(name = "hibernate-uuid", strategy = "uuid2")
	private String id;

	private String token;
	@JsonIgnore
	@ManyToOne(fetch = FetchType.EAGER)
	private AppUser user;

	private Date expiryDate;

	public ResetPasswordToken() {
		super();
	}

	public ResetPasswordToken(AppUser user) {
		super();
		this.token = UUID.randomUUID().toString();
		this.expiryDate = calculateExpiryDate(EXPIRATION);
		this.user = user;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public AppUser getUser() {
		return user;
	}

	public void setUser(AppUser user) {
		this.user = user;
	}

	public Date getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(Date expiryDate) {
		this.expiryDate = expiryDate;
	}

	private Date calculateExpiryDate(int expiryTimeInMinutes) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Timestamp(cal.getTime().getTime()));
		cal.add(Calendar.MINUTE, expiryTimeInMinutes);
		return new Date(cal.getTime().getTime());
	}

}
