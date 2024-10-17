package com.example.batch_sample;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class JobConfig {
	
	@Autowired
	JobRepository jobRepository;
	
	@Autowired
	PlatformTransactionManager transactionManager;
	
	@Autowired
	JobListener jobListener;
	
	@Autowired
	MessageQueListener messageQueListener;
	
	@Bean
	public Job importUserJob(AppTasklet appTasklet) {
		
		var step1 = new StepBuilder("step1", jobRepository)
			.tasklet(appTasklet, transactionManager)
			.listener(messageQueListener)
			.build();
		
		return new JobBuilder("importUserJob", jobRepository)
				.listener(jobListener)
				.incrementer(new RunIdIncrementer())
				.flow(step1).on("DERIVE").to(step1)
				.from(step1).on("*").end()
				.end()
				.build();
	}
	
}
