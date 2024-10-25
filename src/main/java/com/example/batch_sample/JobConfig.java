package com.example.batch_sample;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class JobConfig {
	
	private final JobRepository jobRepository;
	
	private final PlatformTransactionManager transactionManager;
	
	private final JobListener jobListener;
	
	private final MessageQueListener messageQueListener;
	
	public JobConfig(JobRepository jobRepository, PlatformTransactionManager transactionManager, JobListener jobListener, MessageQueListener messageQueListener) {
		this.jobRepository = jobRepository;
		this.transactionManager = transactionManager;
		this.jobListener = jobListener;
		this.messageQueListener = messageQueListener;
	}
	
	@Bean
	public Job importUserJob(AppTasklet appTasklet) {
		
		// STEP定義
		var step1 = new StepBuilder("step1", jobRepository)
			.tasklet(appTasklet, transactionManager)
			.listener(messageQueListener)
			.build();
		
		// JOB定義
		return new JobBuilder("importUserJob", jobRepository)
				.listener(jobListener)
				.incrementer(new RunIdIncrementer())
				.flow(step1).on("DERIVE").to(step1)
				.from(step1).on("*").end()
				.end()
				.build();
	}
	
}
