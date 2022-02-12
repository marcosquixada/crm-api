package com.crm.api.payload.response;

import java.util.UUID;

public class MessageResponse {
	private String message;
	private UUID id;

	public MessageResponse(String message) {
	    this.message = message;
	}

	public MessageResponse(String message, UUID id){
		this.message = message;
		this.id = id;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}
}
