package com.springrestsecuritycoreboilerplate.common;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

@MappedSuperclass
public class BaseEntity {
	@Column(insertable = true, nullable = false, updatable = false)
	private Date creationDate;
	@Column(insertable = false, nullable = true, updatable = true)
	private Date updateDate;
	@Column(insertable = true, nullable = true, updatable = true)
	private Boolean deleted;

	@PrePersist
	protected void onCreate() {
		deleted = false;
		creationDate = new Date();
	}

	@PreUpdate
	protected void onUpdate() {
		updateDate = new Date();
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	public Boolean getDeleted() {
		return deleted;
	}

	public void setDeleted(Boolean deleted) {
		this.deleted = deleted;
	}

}
