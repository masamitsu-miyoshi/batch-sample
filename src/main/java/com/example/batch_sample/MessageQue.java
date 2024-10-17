package com.example.batch_sample;

import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.stereotype.Component;

@JobScope
@Component
public class MessageQue {
	
	private String message = "INIT";
	
	public void push(String message) {
		this.message = message;
	}
	
	public String pop() {
		var message = this.message;
		this.message = null;
		return message;
	}
	
	public boolean hasMessage() {
		return message == null;
	}
	

}
