package com.springrestsecuritycoreboilerplate.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ResponseObject {
	
	
//	@JsonProperty("status")
//	private String status;
	@JsonProperty("data")
	private Object data;

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

//	public String getStatus() {
//		return status;
//	}
//
//	public void setStatus(String status) {
//		this.status = status;
//	}

}
