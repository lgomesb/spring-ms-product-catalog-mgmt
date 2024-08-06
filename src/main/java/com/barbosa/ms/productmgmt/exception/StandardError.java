package com.barbosa.ms.productmgmt.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
public class StandardError implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long timestamp; 
	private Integer status;
	private String error; 

	@JsonInclude(Include.NON_NULL)
	private String message;
	private String path;
	
	@Builder
	public StandardError(Integer status, String error, String message, String path) {
		super();
		this.timestamp = System.currentTimeMillis();
		this.status = status;
		this.error = error;
		this.message = message;
		this.path = path;
	}
	
}