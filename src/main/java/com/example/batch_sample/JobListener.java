package com.example.batch_sample;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

@JobScope
@Component
public class JobListener implements JobExecutionListener {
	
    private static final Logger logger =
            LoggerFactory.getLogger(JobListener.class);
    
	@Autowired
	PlatformTransactionManager txManager;
	
	TransactionStatus txStatus;
	
	@Override
	public void beforeJob(JobExecution jobExecution) {
		logger.info("トランザクションを開始する");
		
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setName("JobScopeTransaction");
		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
		
		// トランザクションを開始する。
		txStatus = txManager.getTransaction(def);
	}
	  
	@Override
	public void afterJob(JobExecution jobExecution) {
		// ステータスが正常終了ならコミットする。
		var status = jobExecution.getExitStatus();
		
		logger.info("終了 {}", status);
		if (ExitStatus.COMPLETED.equals(status)) {
			txManager.commit(txStatus);
			logger.info("トランザクションをコミットした");
		} else {
			txManager.rollback(txStatus);
			logger.info("トランザクションをロールバックした");
		}
		
	}
}
