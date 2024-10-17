package com.example.batch_sample;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MessageQueListener implements StepExecutionListener {
	
	@Autowired
	MessageQue messageQue;
	
	@Override
	public ExitStatus afterStep(StepExecution stepExecution) {
		if (messageQue.hasMessage()) {
			return ExitStatus.COMPLETED;
		}
		return new ExitStatus("DERIVE");
		
	}
}
