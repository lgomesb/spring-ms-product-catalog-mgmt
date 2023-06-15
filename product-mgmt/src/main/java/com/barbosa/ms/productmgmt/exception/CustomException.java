package com.barbosa.ms.productmgmt.exception;

import java.io.Serializable;

import lombok.Builder;
import lombok.Data;

@Data
public class CustomException implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long timestamp; 
	private Integer status;
	private String error; 
	private String messege; 
	private String path;
	
	@Builder
	public CustomException(Integer status, String error, String messege, String path) {
		super();
		this.timestamp = System.currentTimeMillis();
		this.status = status;
		this.error = error;
		this.messege = messege;
		this.path = path;
	}
	
}